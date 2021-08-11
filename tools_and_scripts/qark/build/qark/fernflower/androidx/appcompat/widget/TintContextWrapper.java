package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper extends ContextWrapper {
   private static final Object CACHE_LOCK = new Object();
   private static ArrayList sCache;
   private final Resources mResources;
   private final Theme mTheme;

   private TintContextWrapper(Context var1) {
      super(var1);
      if (VectorEnabledTintResources.shouldBeUsed()) {
         VectorEnabledTintResources var2 = new VectorEnabledTintResources(this, var1.getResources());
         this.mResources = var2;
         Theme var3 = var2.newTheme();
         this.mTheme = var3;
         var3.setTo(var1.getTheme());
      } else {
         this.mResources = new TintResources(this, var1.getResources());
         this.mTheme = null;
      }
   }

   private static boolean shouldWrap(Context var0) {
      boolean var2 = var0 instanceof TintContextWrapper;
      boolean var1 = false;
      if (!var2 && !(var0.getResources() instanceof TintResources)) {
         if (var0.getResources() instanceof VectorEnabledTintResources) {
            return false;
         } else {
            if (VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
               var1 = true;
            }

            return var1;
         }
      } else {
         return false;
      }
   }

   public static Context wrap(Context var0) {
      if (shouldWrap(var0)) {
         Object var3 = CACHE_LOCK;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label1159: {
            label1171: {
               try {
                  if (sCache == null) {
                     sCache = new ArrayList();
                     break label1171;
                  }
               } catch (Throwable var135) {
                  var10000 = var135;
                  var10001 = false;
                  break label1159;
               }

               int var1;
               try {
                  var1 = sCache.size() - 1;
               } catch (Throwable var133) {
                  var10000 = var133;
                  var10001 = false;
                  break label1159;
               }

               label1151:
               while(true) {
                  WeakReference var2;
                  if (var1 < 0) {
                     try {
                        var1 = sCache.size() - 1;
                     } catch (Throwable var130) {
                        var10000 = var130;
                        var10001 = false;
                        break label1159;
                     }

                     while(true) {
                        if (var1 < 0) {
                           break label1151;
                        }

                        try {
                           var2 = (WeakReference)sCache.get(var1);
                        } catch (Throwable var129) {
                           var10000 = var129;
                           var10001 = false;
                           break label1159;
                        }

                        TintContextWrapper var138;
                        if (var2 != null) {
                           try {
                              var138 = (TintContextWrapper)var2.get();
                           } catch (Throwable var128) {
                              var10000 = var128;
                              var10001 = false;
                              break label1159;
                           }
                        } else {
                           var138 = null;
                        }

                        if (var138 != null) {
                           try {
                              if (var138.getBaseContext() == var0) {
                                 return var138;
                              }
                           } catch (Throwable var127) {
                              var10000 = var127;
                              var10001 = false;
                              break label1159;
                           }
                        }

                        --var1;
                     }
                  }

                  try {
                     var2 = (WeakReference)sCache.get(var1);
                  } catch (Throwable var132) {
                     var10000 = var132;
                     var10001 = false;
                     break label1159;
                  }

                  label1146: {
                     if (var2 != null) {
                        try {
                           if (var2.get() != null) {
                              break label1146;
                           }
                        } catch (Throwable var134) {
                           var10000 = var134;
                           var10001 = false;
                           break label1159;
                        }
                     }

                     try {
                        sCache.remove(var1);
                     } catch (Throwable var131) {
                        var10000 = var131;
                        var10001 = false;
                        break label1159;
                     }
                  }

                  --var1;
               }
            }

            label1122:
            try {
               TintContextWrapper var137 = new TintContextWrapper(var0);
               sCache.add(new WeakReference(var137));
               return var137;
            } catch (Throwable var126) {
               var10000 = var126;
               var10001 = false;
               break label1122;
            }
         }

         while(true) {
            Throwable var136 = var10000;

            try {
               throw var136;
            } catch (Throwable var125) {
               var10000 = var125;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var0;
      }
   }

   public AssetManager getAssets() {
      return this.mResources.getAssets();
   }

   public Resources getResources() {
      return this.mResources;
   }

   public Theme getTheme() {
      Theme var2 = this.mTheme;
      Theme var1 = var2;
      if (var2 == null) {
         var1 = super.getTheme();
      }

      return var1;
   }

   public void setTheme(int var1) {
      Theme var2 = this.mTheme;
      if (var2 == null) {
         super.setTheme(var1);
      } else {
         var2.applyStyle(var1, true);
      }
   }
}
