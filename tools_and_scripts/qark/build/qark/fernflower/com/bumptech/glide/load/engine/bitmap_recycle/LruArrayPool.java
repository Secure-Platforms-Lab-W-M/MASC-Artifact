package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool implements ArrayPool {
   private static final int DEFAULT_SIZE = 4194304;
   static final int MAX_OVER_SIZE_MULTIPLE = 8;
   private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
   private final Map adapters = new HashMap();
   private int currentSize;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final LruArrayPool.KeyPool keyPool = new LruArrayPool.KeyPool();
   private final int maxSize;
   private final Map sortedSizes = new HashMap();

   public LruArrayPool() {
      this.maxSize = 4194304;
   }

   public LruArrayPool(int var1) {
      this.maxSize = var1;
   }

   private void decrementArrayOfSize(int var1, Class var2) {
      NavigableMap var4 = this.getSizesForAdapter(var2);
      Integer var3 = (Integer)var4.get(var1);
      if (var3 != null) {
         if (var3 == 1) {
            var4.remove(var1);
         } else {
            var4.put(var1, var3 - 1);
         }
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Tried to decrement empty size, size: ");
         var5.append(var1);
         var5.append(", this: ");
         var5.append(this);
         throw new NullPointerException(var5.toString());
      }
   }

   private void evict() {
      this.evictToSize(this.maxSize);
   }

   private void evictToSize(int var1) {
      while(this.currentSize > var1) {
         Object var2 = this.groupedMap.removeLast();
         Preconditions.checkNotNull(var2);
         ArrayAdapterInterface var3 = this.getAdapterFromObject(var2);
         this.currentSize -= var3.getArrayLength(var2) * var3.getElementSizeInBytes();
         this.decrementArrayOfSize(var3.getArrayLength(var2), var2.getClass());
         if (Log.isLoggable(var3.getTag(), 2)) {
            String var4 = var3.getTag();
            StringBuilder var5 = new StringBuilder();
            var5.append("evicted: ");
            var5.append(var3.getArrayLength(var2));
            Log.v(var4, var5.toString());
         }
      }

   }

   private ArrayAdapterInterface getAdapterFromObject(Object var1) {
      return this.getAdapterFromType(var1.getClass());
   }

   private ArrayAdapterInterface getAdapterFromType(Class var1) {
      ArrayAdapterInterface var2 = (ArrayAdapterInterface)this.adapters.get(var1);
      if (var2 == null) {
         Object var3;
         if (var1.equals(int[].class)) {
            var3 = new IntegerArrayAdapter();
         } else {
            if (!var1.equals(byte[].class)) {
               StringBuilder var4 = new StringBuilder();
               var4.append("No array pool found for: ");
               var4.append(var1.getSimpleName());
               throw new IllegalArgumentException(var4.toString());
            }

            var3 = new ByteArrayAdapter();
         }

         this.adapters.put(var1, var3);
         return (ArrayAdapterInterface)var3;
      } else {
         return var2;
      }
   }

   private Object getArrayForKey(LruArrayPool.Key var1) {
      return this.groupedMap.get(var1);
   }

   private Object getForKey(LruArrayPool.Key var1, Class var2) {
      ArrayAdapterInterface var4 = this.getAdapterFromType(var2);
      Object var3 = this.getArrayForKey(var1);
      if (var3 != null) {
         this.currentSize -= var4.getArrayLength(var3) * var4.getElementSizeInBytes();
         this.decrementArrayOfSize(var4.getArrayLength(var3), var2);
      }

      Object var5 = var3;
      if (var3 == null) {
         if (Log.isLoggable(var4.getTag(), 2)) {
            String var6 = var4.getTag();
            StringBuilder var7 = new StringBuilder();
            var7.append("Allocated ");
            var7.append(var1.size);
            var7.append(" bytes");
            Log.v(var6, var7.toString());
         }

         var5 = var4.newArray(var1.size);
      }

      return var5;
   }

   private NavigableMap getSizesForAdapter(Class var1) {
      NavigableMap var3 = (NavigableMap)this.sortedSizes.get(var1);
      Object var2 = var3;
      if (var3 == null) {
         var2 = new TreeMap();
         this.sortedSizes.put(var1, var2);
      }

      return (NavigableMap)var2;
   }

   private boolean isNoMoreThanHalfFull() {
      int var1 = this.currentSize;
      return var1 == 0 || this.maxSize / var1 >= 2;
   }

   private boolean isSmallEnoughForReuse(int var1) {
      return var1 <= this.maxSize / 2;
   }

   private boolean mayFillRequest(int var1, Integer var2) {
      return var2 != null && (this.isNoMoreThanHalfFull() || var2 <= var1 * 8);
   }

   public void clearMemory() {
      synchronized(this){}

      try {
         this.evictToSize(0);
      } finally {
         ;
      }

   }

   public Object get(int var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label133: {
         boolean var10001;
         LruArrayPool.Key var18;
         label126: {
            try {
               Integer var3 = (Integer)this.getSizesForAdapter(var2).ceilingKey(var1);
               if (this.mayFillRequest(var1, var3)) {
                  var18 = this.keyPool.get(var3, var2);
                  break label126;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label133;
            }

            try {
               var18 = this.keyPool.get(var1, var2);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label133;
            }
         }

         label117:
         try {
            Object var17 = this.getForKey(var18, var2);
            return var17;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   int getCurrentSize() {
      int var1 = 0;
      Iterator var2 = this.sortedSizes.keySet().iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();

         Integer var5;
         ArrayAdapterInterface var6;
         for(Iterator var4 = ((NavigableMap)this.sortedSizes.get(var3)).keySet().iterator(); var4.hasNext(); var1 += var5 * (Integer)((NavigableMap)this.sortedSizes.get(var3)).get(var5) * var6.getElementSizeInBytes()) {
            var5 = (Integer)var4.next();
            var6 = this.getAdapterFromType(var3);
         }
      }

      return var1;
   }

   public Object getExact(int var1, Class var2) {
      synchronized(this){}

      Object var5;
      try {
         var5 = this.getForKey(this.keyPool.get(var1, var2), var2);
      } finally {
         ;
      }

      return var5;
   }

   public void put(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label191: {
         int var2;
         int var3;
         boolean var5;
         Class var7;
         boolean var10001;
         try {
            var7 = var1.getClass();
            ArrayAdapterInterface var6 = this.getAdapterFromType(var7);
            var2 = var6.getArrayLength(var1);
            var3 = var6.getElementSizeInBytes() * var2;
            var5 = this.isSmallEnoughForReuse(var3);
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label191;
         }

         if (!var5) {
            return;
         }

         int var4;
         NavigableMap var28;
         Integer var31;
         try {
            LruArrayPool.Key var30 = this.keyPool.get(var2, var7);
            this.groupedMap.put(var30, var1);
            var28 = this.getSizesForAdapter(var7);
            var31 = (Integer)var28.get(var30.size);
            var4 = var30.size;
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label191;
         }

         var2 = 1;
         if (var31 != null) {
            try {
               var2 = 1 + var31;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label191;
            }
         }

         try {
            var28.put(var4, var2);
            this.currentSize += var3;
            this.evict();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label191;
         }

         return;
      }

      Throwable var29 = var10000;
      throw var29;
   }

   @Deprecated
   public void put(Object var1, Class var2) {
      this.put(var1);
   }

   public void trimMemory(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 >= 40) {
         label68:
         try {
            this.clearMemory();
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label68;
         }
      } else {
         if (var1 < 20 && var1 != 15) {
            return;
         }

         label70:
         try {
            this.evictToSize(this.maxSize / 2);
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label70;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   private static final class Key implements Poolable {
      private Class arrayClass;
      private final LruArrayPool.KeyPool pool;
      int size;

      Key(LruArrayPool.KeyPool var1) {
         this.pool = var1;
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof LruArrayPool.Key;
         boolean var3 = false;
         if (var2) {
            LruArrayPool.Key var4 = (LruArrayPool.Key)var1;
            var2 = var3;
            if (this.size == var4.size) {
               var2 = var3;
               if (this.arrayClass == var4.arrayClass) {
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
         Class var3 = this.arrayClass;
         int var1;
         if (var3 != null) {
            var1 = var3.hashCode();
         } else {
            var1 = 0;
         }

         return var2 * 31 + var1;
      }

      void init(int var1, Class var2) {
         this.size = var1;
         this.arrayClass = var2;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Key{size=");
         var1.append(this.size);
         var1.append("array=");
         var1.append(this.arrayClass);
         var1.append('}');
         return var1.toString();
      }
   }

   private static final class KeyPool extends BaseKeyPool {
      KeyPool() {
      }

      protected LruArrayPool.Key create() {
         return new LruArrayPool.Key(this);
      }

      LruArrayPool.Key get(int var1, Class var2) {
         LruArrayPool.Key var3 = (LruArrayPool.Key)this.get();
         var3.init(var1, var2);
         return var3;
      }
   }
}
