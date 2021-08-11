package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SizeConfigStrategy implements LruPoolStrategy {
   private static final Config[] ALPHA_8_IN_CONFIGS;
   private static final Config[] ARGB_4444_IN_CONFIGS;
   private static final Config[] ARGB_8888_IN_CONFIGS;
   private static final int MAX_SIZE_MULTIPLE = 8;
   private static final Config[] RGBA_F16_IN_CONFIGS;
   private static final Config[] RGB_565_IN_CONFIGS;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final SizeConfigStrategy.KeyPool keyPool = new SizeConfigStrategy.KeyPool();
   private final Map sortedSizes = new HashMap();

   static {
      Config[] var1 = new Config[]{Config.ARGB_8888, null};
      Config[] var0 = var1;
      if (VERSION.SDK_INT >= 26) {
         var0 = (Config[])Arrays.copyOf(var1, var1.length + 1);
         var0[var0.length - 1] = Config.RGBA_F16;
      }

      ARGB_8888_IN_CONFIGS = var0;
      RGBA_F16_IN_CONFIGS = var0;
      RGB_565_IN_CONFIGS = new Config[]{Config.RGB_565};
      ARGB_4444_IN_CONFIGS = new Config[]{Config.ARGB_4444};
      ALPHA_8_IN_CONFIGS = new Config[]{Config.ALPHA_8};
   }

   private void decrementBitmapOfSize(Integer var1, Bitmap var2) {
      NavigableMap var3 = this.getSizesForConfig(var2.getConfig());
      Integer var4 = (Integer)var3.get(var1);
      if (var4 != null) {
         if (var4 == 1) {
            var3.remove(var1);
         } else {
            var3.put(var1, var4 - 1);
         }
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Tried to decrement empty size, size: ");
         var5.append(var1);
         var5.append(", removed: ");
         var5.append(this.logBitmap(var2));
         var5.append(", this: ");
         var5.append(this);
         throw new NullPointerException(var5.toString());
      }
   }

   private SizeConfigStrategy.Key findBestKey(int var1, Config var2) {
      SizeConfigStrategy.Key var5 = this.keyPool.get(var1, var2);
      Config[] var6 = getInConfigs(var2);
      int var4 = var6.length;
      int var3 = 0;

      Config var7;
      Integer var8;
      while(true) {
         if (var3 < var4) {
            var7 = var6[var3];
            var8 = (Integer)this.getSizesForConfig(var7).ceilingKey(var1);
            if (var8 == null || var8 > var1 * 8) {
               ++var3;
               continue;
            }

            if (var8 != var1) {
               break;
            }

            if (var7 == null) {
               if (var2 != null) {
                  break;
               }
            } else if (!var7.equals(var2)) {
               break;
            }
         }

         return var5;
      }

      this.keyPool.offer(var5);
      return this.keyPool.get(var8, var7);
   }

   static String getBitmapString(int var0, Config var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("[");
      var2.append(var0);
      var2.append("](");
      var2.append(var1);
      var2.append(")");
      return var2.toString();
   }

   private static Config[] getInConfigs(Config var0) {
      if (VERSION.SDK_INT >= 26 && Config.RGBA_F16.equals(var0)) {
         return RGBA_F16_IN_CONFIGS;
      } else {
         int var1 = null.$SwitchMap$android$graphics$Bitmap$Config[var0.ordinal()];
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  return var1 != 4 ? new Config[]{var0} : ALPHA_8_IN_CONFIGS;
               } else {
                  return ARGB_4444_IN_CONFIGS;
               }
            } else {
               return RGB_565_IN_CONFIGS;
            }
         } else {
            return ARGB_8888_IN_CONFIGS;
         }
      }
   }

   private NavigableMap getSizesForConfig(Config var1) {
      NavigableMap var3 = (NavigableMap)this.sortedSizes.get(var1);
      Object var2 = var3;
      if (var3 == null) {
         var2 = new TreeMap();
         this.sortedSizes.put(var1, var2);
      }

      return (NavigableMap)var2;
   }

   public Bitmap get(int var1, int var2, Config var3) {
      SizeConfigStrategy.Key var4 = this.findBestKey(Util.getBitmapByteSize(var1, var2, var3), var3);
      Bitmap var5 = (Bitmap)this.groupedMap.get(var4);
      if (var5 != null) {
         this.decrementBitmapOfSize(var4.size, var5);
         var5.reconfigure(var1, var2, var3);
      }

      return var5;
   }

   public int getSize(Bitmap var1) {
      return Util.getBitmapByteSize(var1);
   }

   public String logBitmap(int var1, int var2, Config var3) {
      return getBitmapString(Util.getBitmapByteSize(var1, var2, var3), var3);
   }

   public String logBitmap(Bitmap var1) {
      return getBitmapString(Util.getBitmapByteSize(var1), var1.getConfig());
   }

   public void put(Bitmap var1) {
      int var2 = Util.getBitmapByteSize(var1);
      SizeConfigStrategy.Key var4 = this.keyPool.get(var2, var1.getConfig());
      this.groupedMap.put(var4, var1);
      NavigableMap var6 = this.getSizesForConfig(var1.getConfig());
      Integer var5 = (Integer)var6.get(var4.size);
      int var3 = var4.size;
      var2 = 1;
      if (var5 != null) {
         var2 = 1 + var5;
      }

      var6.put(var3, var2);
   }

   public Bitmap removeLast() {
      Bitmap var1 = (Bitmap)this.groupedMap.removeLast();
      if (var1 != null) {
         this.decrementBitmapOfSize(Util.getBitmapByteSize(var1), var1);
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SizeConfigStrategy{groupedMap=");
      var1.append(this.groupedMap);
      var1 = var1.append(", sortedSizes=(");
      Iterator var2 = this.sortedSizes.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append(var3.getKey());
         var1.append('[');
         var1.append(var3.getValue());
         var1.append("], ");
      }

      if (!this.sortedSizes.isEmpty()) {
         var1.replace(var1.length() - 2, var1.length(), "");
      }

      var1.append(")}");
      return var1.toString();
   }

   static final class Key implements Poolable {
      private Config config;
      private final SizeConfigStrategy.KeyPool pool;
      int size;

      public Key(SizeConfigStrategy.KeyPool var1) {
         this.pool = var1;
      }

      Key(SizeConfigStrategy.KeyPool var1, int var2, Config var3) {
         this(var1);
         this.init(var2, var3);
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof SizeConfigStrategy.Key;
         boolean var3 = false;
         if (var2) {
            SizeConfigStrategy.Key var4 = (SizeConfigStrategy.Key)var1;
            var2 = var3;
            if (this.size == var4.size) {
               var2 = var3;
               if (Util.bothNullOrEqual(this.config, var4.config)) {
                  var2 = true;
               }
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var2 = this.size;
         Config var3 = this.config;
         int var1;
         if (var3 != null) {
            var1 = var3.hashCode();
         } else {
            var1 = 0;
         }

         return var2 * 31 + var1;
      }

      public void init(int var1, Config var2) {
         this.size = var1;
         this.config = var2;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         return SizeConfigStrategy.getBitmapString(this.size, this.config);
      }
   }

   static class KeyPool extends BaseKeyPool {
      protected SizeConfigStrategy.Key create() {
         return new SizeConfigStrategy.Key(this);
      }

      public SizeConfigStrategy.Key get(int var1, Config var2) {
         SizeConfigStrategy.Key var3 = (SizeConfigStrategy.Key)this.get();
         var3.init(var1, var2);
         return var3;
      }
   }
}
