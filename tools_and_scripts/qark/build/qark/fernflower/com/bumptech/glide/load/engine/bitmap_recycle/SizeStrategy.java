package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.util.Util;
import java.util.NavigableMap;

final class SizeStrategy implements LruPoolStrategy {
   private static final int MAX_SIZE_MULTIPLE = 8;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final SizeStrategy.KeyPool keyPool = new SizeStrategy.KeyPool();
   private final NavigableMap sortedSizes = new PrettyPrintTreeMap();

   private void decrementBitmapOfSize(Integer var1) {
      Integer var2 = (Integer)this.sortedSizes.get(var1);
      if (var2 == 1) {
         this.sortedSizes.remove(var1);
      } else {
         this.sortedSizes.put(var1, var2 - 1);
      }
   }

   static String getBitmapString(int var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append(var0);
      var1.append("]");
      return var1.toString();
   }

   private static String getBitmapString(Bitmap var0) {
      return getBitmapString(Util.getBitmapByteSize(var0));
   }

   public Bitmap get(int var1, int var2, Config var3) {
      int var4 = Util.getBitmapByteSize(var1, var2, var3);
      SizeStrategy.Key var6 = this.keyPool.get(var4);
      Integer var7 = (Integer)this.sortedSizes.ceilingKey(var4);
      SizeStrategy.Key var5 = var6;
      if (var7 != null) {
         var5 = var6;
         if (var7 != var4) {
            var5 = var6;
            if (var7 <= var4 * 8) {
               this.keyPool.offer(var6);
               var5 = this.keyPool.get(var7);
            }
         }
      }

      Bitmap var8 = (Bitmap)this.groupedMap.get(var5);
      if (var8 != null) {
         var8.reconfigure(var1, var2, var3);
         this.decrementBitmapOfSize(var7);
      }

      return var8;
   }

   public int getSize(Bitmap var1) {
      return Util.getBitmapByteSize(var1);
   }

   public String logBitmap(int var1, int var2, Config var3) {
      return getBitmapString(Util.getBitmapByteSize(var1, var2, var3));
   }

   public String logBitmap(Bitmap var1) {
      return getBitmapString(var1);
   }

   public void put(Bitmap var1) {
      int var2 = Util.getBitmapByteSize(var1);
      SizeStrategy.Key var4 = this.keyPool.get(var2);
      this.groupedMap.put(var4, var1);
      Integer var6 = (Integer)this.sortedSizes.get(var4.size);
      NavigableMap var5 = this.sortedSizes;
      int var3 = var4.size;
      var2 = 1;
      if (var6 != null) {
         var2 = 1 + var6;
      }

      var5.put(var3, var2);
   }

   public Bitmap removeLast() {
      Bitmap var1 = (Bitmap)this.groupedMap.removeLast();
      if (var1 != null) {
         this.decrementBitmapOfSize(Util.getBitmapByteSize(var1));
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SizeStrategy:\n  ");
      var1.append(this.groupedMap);
      var1.append("\n  SortedSizes");
      var1.append(this.sortedSizes);
      return var1.toString();
   }

   static final class Key implements Poolable {
      private final SizeStrategy.KeyPool pool;
      int size;

      Key(SizeStrategy.KeyPool var1) {
         this.pool = var1;
      }

      public boolean equals(Object var1) {
         boolean var3 = var1 instanceof SizeStrategy.Key;
         boolean var2 = false;
         if (var3) {
            SizeStrategy.Key var4 = (SizeStrategy.Key)var1;
            if (this.size == var4.size) {
               var2 = true;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.size;
      }

      public void init(int var1) {
         this.size = var1;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         return SizeStrategy.getBitmapString(this.size);
      }
   }

   static class KeyPool extends BaseKeyPool {
      protected SizeStrategy.Key create() {
         return new SizeStrategy.Key(this);
      }

      public SizeStrategy.Key get(int var1) {
         SizeStrategy.Key var2 = (SizeStrategy.Key)super.get();
         var2.init(var1);
         return var2;
      }
   }
}
