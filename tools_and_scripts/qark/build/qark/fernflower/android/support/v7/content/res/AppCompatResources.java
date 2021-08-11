package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;

public final class AppCompatResources {
   private static final String LOG_TAG = "AppCompatResources";
   private static final ThreadLocal TL_TYPED_VALUE = new ThreadLocal();
   private static final Object sColorStateCacheLock = new Object();
   private static final WeakHashMap sColorStateCaches = new WeakHashMap(0);

   private AppCompatResources() {
   }

   private static void addColorStateListToCache(@NonNull Context var0, @ColorRes int var1, @NonNull ColorStateList var2) {
      Object var5 = sColorStateCacheLock;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label176: {
         SparseArray var4;
         try {
            var4 = (SparseArray)sColorStateCaches.get(var0);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label176;
         }

         SparseArray var3 = var4;
         if (var4 == null) {
            try {
               var3 = new SparseArray();
               sColorStateCaches.put(var0, var3);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            var3.append(var1, new AppCompatResources.ColorStateListCacheEntry(var2, var0.getResources().getConfiguration()));
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   @Nullable
   private static ColorStateList getCachedColorStateList(@NonNull Context var0, @ColorRes int var1) {
      Object var2 = sColorStateCacheLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label404: {
         SparseArray var3;
         try {
            var3 = (SparseArray)sColorStateCaches.get(var0);
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label404;
         }

         if (var3 != null) {
            label402: {
               AppCompatResources.ColorStateListCacheEntry var4;
               try {
                  if (var3.size() <= 0) {
                     break label402;
                  }

                  var4 = (AppCompatResources.ColorStateListCacheEntry)var3.get(var1);
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label404;
               }

               if (var4 != null) {
                  try {
                     if (var4.configuration.equals(var0.getResources().getConfiguration())) {
                        ColorStateList var47 = var4.value;
                        return var47;
                     }
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label404;
                  }

                  try {
                     var3.remove(var1);
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label404;
                  }
               }
            }
         }

         label375:
         try {
            return null;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label375;
         }
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   public static ColorStateList getColorStateList(@NonNull Context var0, @ColorRes int var1) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getColorStateList(var1);
      } else {
         ColorStateList var2 = getCachedColorStateList(var0, var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = inflateColorStateList(var0, var1);
            if (var2 != null) {
               addColorStateListToCache(var0, var1, var2);
               return var2;
            } else {
               return ContextCompat.getColorStateList(var0, var1);
            }
         }
      }
   }

   @Nullable
   public static Drawable getDrawable(@NonNull Context var0, @DrawableRes int var1) {
      return AppCompatDrawableManager.get().getDrawable(var0, var1);
   }

   @NonNull
   private static TypedValue getTypedValue() {
      TypedValue var1 = (TypedValue)TL_TYPED_VALUE.get();
      TypedValue var0 = var1;
      if (var1 == null) {
         var0 = new TypedValue();
         TL_TYPED_VALUE.set(var0);
      }

      return var0;
   }

   @Nullable
   private static ColorStateList inflateColorStateList(Context var0, int var1) {
      if (isColorInt(var0, var1)) {
         return null;
      } else {
         Resources var2 = var0.getResources();
         XmlResourceParser var3 = var2.getXml(var1);

         try {
            ColorStateList var5 = AppCompatColorStateListInflater.createFromXml(var2, var3, var0.getTheme());
            return var5;
         } catch (Exception var4) {
            Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", var4);
            return null;
         }
      }
   }

   private static boolean isColorInt(@NonNull Context var0, @ColorRes int var1) {
      Resources var3 = var0.getResources();
      TypedValue var2 = getTypedValue();
      var3.getValue(var1, var2, true);
      return var2.type >= 28 && var2.type <= 31;
   }

   private static class ColorStateListCacheEntry {
      final Configuration configuration;
      final ColorStateList value;

      ColorStateListCacheEntry(@NonNull ColorStateList var1, @NonNull Configuration var2) {
         this.value = var1;
         this.configuration = var2;
      }
   }
}
