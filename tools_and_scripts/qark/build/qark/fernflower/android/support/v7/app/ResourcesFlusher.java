package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher {
   private static final String TAG = "ResourcesFlusher";
   private static Field sDrawableCacheField;
   private static boolean sDrawableCacheFieldFetched;
   private static Field sResourcesImplField;
   private static boolean sResourcesImplFieldFetched;
   private static Class sThemedResourceCacheClazz;
   private static boolean sThemedResourceCacheClazzFetched;
   private static Field sThemedResourceCache_mUnthemedEntriesField;
   private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

   static boolean flush(@NonNull Resources var0) {
      if (VERSION.SDK_INT >= 24) {
         return flushNougats(var0);
      } else if (VERSION.SDK_INT >= 23) {
         return flushMarshmallows(var0);
      } else {
         return VERSION.SDK_INT >= 21 ? flushLollipops(var0) : false;
      }
   }

   @RequiresApi(21)
   private static boolean flushLollipops(@NonNull Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var4);
         }

         sDrawableCacheFieldFetched = true;
      }

      Field var2 = sDrawableCacheField;
      if (var2 != null) {
         Object var1 = null;

         Map var5;
         try {
            var5 = (Map)var2.get(var0);
         } catch (IllegalAccessException var3) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var3);
            var5 = (Map)var1;
         }

         if (var5 != null) {
            var5.clear();
            return true;
         }
      }

      return false;
   }

   @RequiresApi(23)
   private static boolean flushMarshmallows(@NonNull Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField.setAccessible(true);
         } catch (NoSuchFieldException var7) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var7);
         }

         sDrawableCacheFieldFetched = true;
      }

      Object var4 = null;
      Field var5 = sDrawableCacheField;
      Object var3 = var4;
      if (var5 != null) {
         try {
            var3 = var5.get(var0);
         } catch (IllegalAccessException var6) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var6);
            var3 = var4;
         }
      }

      boolean var2 = false;
      if (var3 == null) {
         return false;
      } else {
         boolean var1 = var2;
         if (var3 != null) {
            var1 = var2;
            if (flushThemedResourcesCache(var3)) {
               var1 = true;
            }
         }

         return var1;
      }
   }

   @RequiresApi(24)
   private static boolean flushNougats(@NonNull Resources var0) {
      if (!sResourcesImplFieldFetched) {
         try {
            sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
            sResourcesImplField.setAccessible(true);
         } catch (NoSuchFieldException var7) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", var7);
         }

         sResourcesImplFieldFetched = true;
      }

      Field var2 = sResourcesImplField;
      if (var2 == null) {
         return false;
      } else {
         Object var1 = null;

         Object var8;
         try {
            var8 = var2.get(var0);
         } catch (IllegalAccessException var6) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", var6);
            var8 = var1;
         }

         if (var8 == null) {
            return false;
         } else {
            if (!sDrawableCacheFieldFetched) {
               try {
                  sDrawableCacheField = var8.getClass().getDeclaredField("mDrawableCache");
                  sDrawableCacheField.setAccessible(true);
               } catch (NoSuchFieldException var5) {
                  Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", var5);
               }

               sDrawableCacheFieldFetched = true;
            }

            var2 = null;
            Field var3 = sDrawableCacheField;
            var1 = var2;
            if (var3 != null) {
               try {
                  var1 = var3.get(var8);
               } catch (IllegalAccessException var4) {
                  Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", var4);
                  var1 = var2;
               }
            }

            return var1 != null && flushThemedResourcesCache(var1);
         }
      }
   }

   @RequiresApi(16)
   private static boolean flushThemedResourcesCache(@NonNull Object var0) {
      if (!sThemedResourceCacheClazzFetched) {
         try {
            sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
         } catch (ClassNotFoundException var5) {
            Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", var5);
         }

         sThemedResourceCacheClazzFetched = true;
      }

      Class var1 = sThemedResourceCacheClazz;
      if (var1 == null) {
         return false;
      } else {
         if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
               sThemedResourceCache_mUnthemedEntriesField = var1.getDeclaredField("mUnthemedEntries");
               sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
            } catch (NoSuchFieldException var4) {
               Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", var4);
            }

            sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
         }

         Field var2 = sThemedResourceCache_mUnthemedEntriesField;
         if (var2 == null) {
            return false;
         } else {
            var1 = null;

            LongSparseArray var6;
            try {
               var6 = (LongSparseArray)var2.get(var0);
            } catch (IllegalAccessException var3) {
               Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", var3);
               var6 = var1;
            }

            if (var6 != null) {
               var6.clear();
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
