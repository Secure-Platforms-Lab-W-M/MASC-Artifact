package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
   private static final int APP_VERSION = 1;
   private static final String TAG = "DiskLruCacheWrapper";
   private static final int VALUE_COUNT = 1;
   private static DiskLruCacheWrapper wrapper;
   private final File directory;
   private DiskLruCache diskLruCache;
   private final long maxSize;
   private final SafeKeyGenerator safeKeyGenerator;
   private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

   @Deprecated
   protected DiskLruCacheWrapper(File var1, long var2) {
      this.directory = var1;
      this.maxSize = var2;
      this.safeKeyGenerator = new SafeKeyGenerator();
   }

   public static DiskCache create(File var0, long var1) {
      return new DiskLruCacheWrapper(var0, var1);
   }

   @Deprecated
   public static DiskCache get(File var0, long var1) {
      synchronized(DiskLruCacheWrapper.class){}

      DiskLruCacheWrapper var5;
      try {
         if (wrapper == null) {
            wrapper = new DiskLruCacheWrapper(var0, var1);
         }

         var5 = wrapper;
      } finally {
         ;
      }

      return var5;
   }

   private DiskLruCache getDiskCache() throws IOException {
      synchronized(this){}

      DiskLruCache var1;
      try {
         if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
         }

         var1 = this.diskLruCache;
      } finally {
         ;
      }

      return var1;
   }

   private void resetDiskCache() {
      synchronized(this){}

      try {
         this.diskLruCache = null;
      } finally {
         ;
      }

   }

   public void clear() {
      synchronized(this){}

      Throwable var10000;
      Throwable var38;
      label290: {
         boolean var10001;
         label295: {
            label296: {
               IOException var1;
               try {
                  try {
                     this.getDiskCache().delete();
                     break label295;
                  } catch (IOException var36) {
                     var1 = var36;
                  }
               } catch (Throwable var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label296;
               }

               try {
                  if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                     Log.w("DiskLruCacheWrapper", "Unable to clear disk cache or disk cache cleared externally", var1);
                  }
               } catch (Throwable var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label296;
               }

               try {
                  this.resetDiskCache();
                  return;
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label290;
               }
            }

            var38 = var10000;

            try {
               this.resetDiskCache();
               throw var38;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label290;
            }
         }

         label278:
         try {
            this.resetDiskCache();
            return;
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label278;
         }
      }

      var38 = var10000;
      throw var38;
   }

   public void delete(Key var1) {
      String var3 = this.safeKeyGenerator.getSafeKey(var1);

      try {
         this.getDiskCache().remove(var3);
      } catch (IOException var2) {
         if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
            Log.w("DiskLruCacheWrapper", "Unable to delete from disk cache", var2);
         }

      }
   }

   public File get(Key var1) {
      String var2 = this.safeKeyGenerator.getSafeKey(var1);
      if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Get: Obtained: ");
         var3.append(var2);
         var3.append(" for for Key: ");
         var3.append(var1);
         Log.v("DiskLruCacheWrapper", var3.toString());
      }

      File var6 = null;

      IOException var10000;
      label36: {
         boolean var10001;
         DiskLruCache.Value var8;
         try {
            var8 = this.getDiskCache().get(var2);
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
            break label36;
         }

         if (var8 == null) {
            return var6;
         }

         try {
            var6 = var8.getFile(0);
            return var6;
         } catch (IOException var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      IOException var7 = var10000;
      if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
         Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", var7);
      }

      return null;
   }

   public void put(Key param1, DiskCache.Writer param2) {
      // $FF: Couldn't be decompiled
   }
}
