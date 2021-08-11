package androidx.appcompat.app;

import android.content.res.Resources;
import android.os.Build.VERSION;
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

   private ResourcesFlusher() {
   }

   static void flush(Resources var0) {
      if (VERSION.SDK_INT < 28) {
         if (VERSION.SDK_INT >= 24) {
            flushNougats(var0);
         } else if (VERSION.SDK_INT >= 23) {
            flushMarshmallows(var0);
         } else {
            if (VERSION.SDK_INT >= 21) {
               flushLollipops(var0);
            }

         }
      }
   }

   private static void flushLollipops(Resources var0) {
      Field var1;
      if (!sDrawableCacheFieldFetched) {
         try {
            var1 = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField = var1;
            var1.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var4);
         }

         sDrawableCacheFieldFetched = true;
      }

      Field var2 = sDrawableCacheField;
      if (var2 != null) {
         var1 = null;

         Map var5;
         try {
            var5 = (Map)var2.get(var0);
         } catch (IllegalAccessException var3) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var3);
            var5 = var1;
         }

         if (var5 != null) {
            var5.clear();
         }
      }

   }

   private static void flushMarshmallows(Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            Field var1 = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField = var1;
            var1.setAccessible(true);
         } catch (NoSuchFieldException var5) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var5);
         }

         sDrawableCacheFieldFetched = true;
      }

      Object var2 = null;
      Field var3 = sDrawableCacheField;
      Object var6 = var2;
      if (var3 != null) {
         try {
            var6 = var3.get(var0);
         } catch (IllegalAccessException var4) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var4);
            var6 = var2;
         }
      }

      if (var6 != null) {
         flushThemedResourcesCache(var6);
      }
   }

   private static void flushNougats(Resources var0) {
      Field var1;
      if (!sResourcesImplFieldFetched) {
         try {
            var1 = Resources.class.getDeclaredField("mResourcesImpl");
            sResourcesImplField = var1;
            var1.setAccessible(true);
         } catch (NoSuchFieldException var7) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", var7);
         }

         sResourcesImplFieldFetched = true;
      }

      Field var2 = sResourcesImplField;
      if (var2 != null) {
         var1 = null;

         Object var8;
         try {
            var8 = var2.get(var0);
         } catch (IllegalAccessException var6) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", var6);
            var8 = var1;
         }

         if (var8 != null) {
            if (!sDrawableCacheFieldFetched) {
               try {
                  var1 = var8.getClass().getDeclaredField("mDrawableCache");
                  sDrawableCacheField = var1;
                  var1.setAccessible(true);
               } catch (NoSuchFieldException var5) {
                  Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", var5);
               }

               sDrawableCacheFieldFetched = true;
            }

            var2 = null;
            Field var3 = sDrawableCacheField;
            Object var9 = var2;
            if (var3 != null) {
               try {
                  var9 = var3.get(var8);
               } catch (IllegalAccessException var4) {
                  Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", var4);
                  var9 = var2;
               }
            }

            if (var9 != null) {
               flushThemedResourcesCache(var9);
            }

         }
      }
   }

   private static void flushThemedResourcesCache(Object var0) {
      if (!sThemedResourceCacheClazzFetched) {
         try {
            sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
         } catch (ClassNotFoundException var5) {
            Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", var5);
         }

         sThemedResourceCacheClazzFetched = true;
      }

      Class var1 = sThemedResourceCacheClazz;
      if (var1 != null) {
         if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
               Field var7 = var1.getDeclaredField("mUnthemedEntries");
               sThemedResourceCache_mUnthemedEntriesField = var7;
               var7.setAccessible(true);
            } catch (NoSuchFieldException var4) {
               Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", var4);
            }

            sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
         }

         Field var2 = sThemedResourceCache_mUnthemedEntriesField;
         if (var2 != null) {
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
            }

         }
      }
   }
}
