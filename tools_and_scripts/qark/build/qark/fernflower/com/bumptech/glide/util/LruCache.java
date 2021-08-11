package com.bumptech.glide.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache {
   private final Map cache = new LinkedHashMap(100, 0.75F, true);
   private long currentSize;
   private final long initialMaxSize;
   private long maxSize;

   public LruCache(long var1) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   public void clearMemory() {
      this.trimToSize(0L);
   }

   public boolean contains(Object var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.cache.containsKey(var1);
      } finally {
         ;
      }

      return var2;
   }

   public Object get(Object var1) {
      synchronized(this){}

      try {
         var1 = this.cache.get(var1);
      } finally {
         ;
      }

      return var1;
   }

   protected int getCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.cache.size();
      } finally {
         ;
      }

      return var1;
   }

   public long getCurrentSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.currentSize;
      } finally {
         ;
      }

      return var1;
   }

   public long getMaxSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.maxSize;
      } finally {
         ;
      }

      return var1;
   }

   protected int getSize(Object var1) {
      return 1;
   }

   protected void onItemEvicted(Object var1, Object var2) {
   }

   public Object put(Object var1, Object var2) {
      synchronized(this){}

      Throwable var10000;
      label299: {
         int var3;
         boolean var10001;
         label294: {
            try {
               var3 = this.getSize(var2);
               if ((long)var3 < this.maxSize) {
                  break label294;
               }

               this.onItemEvicted(var1, var2);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label299;
            }

            return null;
         }

         if (var2 != null) {
            try {
               this.currentSize += (long)var3;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label299;
            }
         }

         Object var4;
         try {
            var4 = this.cache.put(var1, var2);
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label299;
         }

         if (var4 != null) {
            try {
               this.currentSize -= (long)this.getSize(var4);
               if (!var4.equals(var2)) {
                  this.onItemEvicted(var1, var4);
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label299;
            }
         }

         try {
            this.evict();
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label299;
         }

         return var4;
      }

      Throwable var35 = var10000;
      throw var35;
   }

   public Object remove(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         try {
            var1 = this.cache.remove(var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label75;
         }

         if (var1 == null) {
            return var1;
         }

         label66:
         try {
            this.currentSize -= (long)this.getSize(var1);
            return var1;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label66;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void setSizeMultiplier(float var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 >= 0.0F) {
         label51: {
            try {
               this.maxSize = (long)Math.round((float)this.initialMaxSize * var1);
               this.evict();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label51;
            }

            return;
         }
      } else {
         label53:
         try {
            throw new IllegalArgumentException("Multiplier must be >= 0");
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label53;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void trimToSize(long var1) {
      synchronized(this){}

      while(true) {
         boolean var7 = false;

         try {
            var7 = true;
            if (this.currentSize <= var1) {
               var7 = false;
               return;
            }

            Iterator var3 = this.cache.entrySet().iterator();
            Entry var5 = (Entry)var3.next();
            Object var4 = var5.getValue();
            this.currentSize -= (long)this.getSize(var4);
            Object var9 = var5.getKey();
            var3.remove();
            this.onItemEvicted(var9, var4);
            var7 = false;
         } finally {
            if (var7) {
               ;
            }
         }
      }
   }
}
