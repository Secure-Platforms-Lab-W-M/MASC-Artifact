package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableCompat {
   static final DrawableCompat.DrawableCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 23) {
         IMPL = new DrawableCompat.DrawableCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new DrawableCompat.DrawableCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new DrawableCompat.DrawableCompatApi19Impl();
      } else if (VERSION.SDK_INT >= 17) {
         IMPL = new DrawableCompat.DrawableCompatApi17Impl();
      } else {
         IMPL = new DrawableCompat.DrawableCompatBaseImpl();
      }
   }

   private DrawableCompat() {
   }

   public static void applyTheme(@NonNull Drawable var0, @NonNull Theme var1) {
      IMPL.applyTheme(var0, var1);
   }

   public static boolean canApplyTheme(@NonNull Drawable var0) {
      return IMPL.canApplyTheme(var0);
   }

   public static void clearColorFilter(@NonNull Drawable var0) {
      IMPL.clearColorFilter(var0);
   }

   public static int getAlpha(@NonNull Drawable var0) {
      return IMPL.getAlpha(var0);
   }

   public static ColorFilter getColorFilter(@NonNull Drawable var0) {
      return IMPL.getColorFilter(var0);
   }

   public static int getLayoutDirection(@NonNull Drawable var0) {
      return IMPL.getLayoutDirection(var0);
   }

   public static void inflate(@NonNull Drawable var0, @NonNull Resources var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4) throws XmlPullParserException, IOException {
      IMPL.inflate(var0, var1, var2, var3, var4);
   }

   public static boolean isAutoMirrored(@NonNull Drawable var0) {
      return IMPL.isAutoMirrored(var0);
   }

   public static void jumpToCurrentState(@NonNull Drawable var0) {
      IMPL.jumpToCurrentState(var0);
   }

   public static void setAutoMirrored(@NonNull Drawable var0, boolean var1) {
      IMPL.setAutoMirrored(var0, var1);
   }

   public static void setHotspot(@NonNull Drawable var0, float var1, float var2) {
      IMPL.setHotspot(var0, var1, var2);
   }

   public static void setHotspotBounds(@NonNull Drawable var0, int var1, int var2, int var3, int var4) {
      IMPL.setHotspotBounds(var0, var1, var2, var3, var4);
   }

   public static boolean setLayoutDirection(@NonNull Drawable var0, int var1) {
      return IMPL.setLayoutDirection(var0, var1);
   }

   public static void setTint(@NonNull Drawable var0, @ColorInt int var1) {
      IMPL.setTint(var0, var1);
   }

   public static void setTintList(@NonNull Drawable var0, @Nullable ColorStateList var1) {
      IMPL.setTintList(var0, var1);
   }

   public static void setTintMode(@NonNull Drawable var0, @Nullable Mode var1) {
      IMPL.setTintMode(var0, var1);
   }

   public static Drawable unwrap(@NonNull Drawable var0) {
      return var0 instanceof DrawableWrapper ? ((DrawableWrapper)var0).getWrappedDrawable() : var0;
   }

   public static Drawable wrap(@NonNull Drawable var0) {
      return IMPL.wrap(var0);
   }

   @RequiresApi(17)
   static class DrawableCompatApi17Impl extends DrawableCompat.DrawableCompatBaseImpl {
      private static final String TAG = "DrawableCompatApi17";
      private static Method sGetLayoutDirectionMethod;
      private static boolean sGetLayoutDirectionMethodFetched;
      private static Method sSetLayoutDirectionMethod;
      private static boolean sSetLayoutDirectionMethodFetched;

      public int getLayoutDirection(Drawable var1) {
         if (!sGetLayoutDirectionMethodFetched) {
            try {
               sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection");
               sGetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var4) {
               Log.i("DrawableCompatApi17", "Failed to retrieve getLayoutDirection() method", var4);
            }

            sGetLayoutDirectionMethodFetched = true;
         }

         Method var3 = sGetLayoutDirectionMethod;
         if (var3 != null) {
            try {
               int var2 = (Integer)var3.invoke(var1);
               return var2;
            } catch (Exception var5) {
               Log.i("DrawableCompatApi17", "Failed to invoke getLayoutDirection() via reflection", var5);
               sGetLayoutDirectionMethod = null;
            }
         }

         return 0;
      }

      public boolean setLayoutDirection(Drawable var1, int var2) {
         if (!sSetLayoutDirectionMethodFetched) {
            try {
               sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
               sSetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var4) {
               Log.i("DrawableCompatApi17", "Failed to retrieve setLayoutDirection(int) method", var4);
            }

            sSetLayoutDirectionMethodFetched = true;
         }

         Method var3 = sSetLayoutDirectionMethod;
         if (var3 != null) {
            try {
               var3.invoke(var1, var2);
               return true;
            } catch (Exception var5) {
               Log.i("DrawableCompatApi17", "Failed to invoke setLayoutDirection(int) via reflection", var5);
               sSetLayoutDirectionMethod = null;
            }
         }

         return false;
      }
   }

   @RequiresApi(19)
   static class DrawableCompatApi19Impl extends DrawableCompat.DrawableCompatApi17Impl {
      public int getAlpha(Drawable var1) {
         return var1.getAlpha();
      }

      public boolean isAutoMirrored(Drawable var1) {
         return var1.isAutoMirrored();
      }

      public void setAutoMirrored(Drawable var1, boolean var2) {
         var1.setAutoMirrored(var2);
      }

      public Drawable wrap(Drawable var1) {
         return (Drawable)(!(var1 instanceof TintAwareDrawable) ? new DrawableWrapperApi19(var1) : var1);
      }
   }

   @RequiresApi(21)
   static class DrawableCompatApi21Impl extends DrawableCompat.DrawableCompatApi19Impl {
      public void applyTheme(Drawable var1, Theme var2) {
         var1.applyTheme(var2);
      }

      public boolean canApplyTheme(Drawable var1) {
         return var1.canApplyTheme();
      }

      public void clearColorFilter(Drawable var1) {
         var1.clearColorFilter();
         if (var1 instanceof InsetDrawable) {
            this.clearColorFilter(((InsetDrawable)var1).getDrawable());
         } else if (var1 instanceof DrawableWrapper) {
            this.clearColorFilter(((DrawableWrapper)var1).getWrappedDrawable());
         } else {
            if (var1 instanceof DrawableContainer) {
               DrawableContainerState var5 = (DrawableContainerState)((DrawableContainer)var1).getConstantState();
               if (var5 != null) {
                  int var2 = 0;

                  for(int var3 = var5.getChildCount(); var2 < var3; ++var2) {
                     Drawable var4 = var5.getChild(var2);
                     if (var4 != null) {
                        this.clearColorFilter(var4);
                     }
                  }
               }
            }

         }
      }

      public ColorFilter getColorFilter(Drawable var1) {
         return var1.getColorFilter();
      }

      public void inflate(Drawable var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws IOException, XmlPullParserException {
         var1.inflate(var2, var3, var4, var5);
      }

      public void setHotspot(Drawable var1, float var2, float var3) {
         var1.setHotspot(var2, var3);
      }

      public void setHotspotBounds(Drawable var1, int var2, int var3, int var4, int var5) {
         var1.setHotspotBounds(var2, var3, var4, var5);
      }

      public void setTint(Drawable var1, int var2) {
         var1.setTint(var2);
      }

      public void setTintList(Drawable var1, ColorStateList var2) {
         var1.setTintList(var2);
      }

      public void setTintMode(Drawable var1, Mode var2) {
         var1.setTintMode(var2);
      }

      public Drawable wrap(Drawable var1) {
         return (Drawable)(!(var1 instanceof TintAwareDrawable) ? new DrawableWrapperApi21(var1) : var1);
      }
   }

   @RequiresApi(23)
   static class DrawableCompatApi23Impl extends DrawableCompat.DrawableCompatApi21Impl {
      public void clearColorFilter(Drawable var1) {
         var1.clearColorFilter();
      }

      public int getLayoutDirection(Drawable var1) {
         return var1.getLayoutDirection();
      }

      public boolean setLayoutDirection(Drawable var1, int var2) {
         return var1.setLayoutDirection(var2);
      }

      public Drawable wrap(Drawable var1) {
         return var1;
      }
   }

   static class DrawableCompatBaseImpl {
      public void applyTheme(Drawable var1, Theme var2) {
      }

      public boolean canApplyTheme(Drawable var1) {
         return false;
      }

      public void clearColorFilter(Drawable var1) {
         var1.clearColorFilter();
      }

      public int getAlpha(Drawable var1) {
         return 0;
      }

      public ColorFilter getColorFilter(Drawable var1) {
         return null;
      }

      public int getLayoutDirection(Drawable var1) {
         return 0;
      }

      public void inflate(Drawable var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws IOException, XmlPullParserException {
         var1.inflate(var2, var3, var4);
      }

      public boolean isAutoMirrored(Drawable var1) {
         return false;
      }

      public void jumpToCurrentState(Drawable var1) {
         var1.jumpToCurrentState();
      }

      public void setAutoMirrored(Drawable var1, boolean var2) {
      }

      public void setHotspot(Drawable var1, float var2, float var3) {
      }

      public void setHotspotBounds(Drawable var1, int var2, int var3, int var4, int var5) {
      }

      public boolean setLayoutDirection(Drawable var1, int var2) {
         return false;
      }

      public void setTint(Drawable var1, int var2) {
         if (var1 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)var1).setTint(var2);
         }

      }

      public void setTintList(Drawable var1, ColorStateList var2) {
         if (var1 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)var1).setTintList(var2);
         }

      }

      public void setTintMode(Drawable var1, Mode var2) {
         if (var1 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)var1).setTintMode(var2);
         }

      }

      public Drawable wrap(Drawable var1) {
         return (Drawable)(!(var1 instanceof TintAwareDrawable) ? new DrawableWrapperApi14(var1) : var1);
      }
   }
}
