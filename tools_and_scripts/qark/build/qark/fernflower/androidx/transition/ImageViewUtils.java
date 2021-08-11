package androidx.transition;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.widget.ImageView;
import java.lang.reflect.Field;

class ImageViewUtils {
   private static Field sDrawMatrixField;
   private static boolean sDrawMatrixFieldFetched;
   private static boolean sTryHiddenAnimateTransform = true;

   private ImageViewUtils() {
   }

   static void animateTransform(ImageView var0, Matrix var1) {
      if (VERSION.SDK_INT >= 29) {
         var0.animateTransform(var1);
      } else if (var1 == null) {
         Drawable var8 = var0.getDrawable();
         if (var8 != null) {
            var8.setBounds(0, 0, var0.getWidth() - var0.getPaddingLeft() - var0.getPaddingRight(), var0.getHeight() - var0.getPaddingTop() - var0.getPaddingBottom());
            var0.invalidate();
         }

      } else if (VERSION.SDK_INT >= 21) {
         hiddenAnimateTransform(var0, var1);
      } else {
         Drawable var2 = var0.getDrawable();
         if (var2 != null) {
            var2.setBounds(0, 0, var2.getIntrinsicWidth(), var2.getIntrinsicHeight());
            Matrix var9 = null;
            Matrix var3 = null;
            fetchDrawMatrixField();
            Field var4 = sDrawMatrixField;
            if (var4 != null) {
               label77: {
                  var9 = var3;

                  boolean var10001;
                  try {
                     var3 = (Matrix)var4.get(var0);
                  } catch (IllegalAccessException var7) {
                     var10001 = false;
                     break label77;
                  }

                  var9 = var3;
                  if (var3 == null) {
                     var9 = var3;

                     try {
                        var3 = new Matrix();
                     } catch (IllegalAccessException var6) {
                        var10001 = false;
                        break label77;
                     }

                     var9 = var3;

                     try {
                        sDrawMatrixField.set(var0, var3);
                     } catch (IllegalAccessException var5) {
                        var10001 = false;
                        break label77;
                     }

                     var9 = var3;
                  }
               }
            }

            if (var9 != null) {
               var9.set(var1);
            }

            var0.invalidate();
         }

      }
   }

   private static void fetchDrawMatrixField() {
      if (!sDrawMatrixFieldFetched) {
         try {
            Field var0 = ImageView.class.getDeclaredField("mDrawMatrix");
            sDrawMatrixField = var0;
            var0.setAccessible(true);
         } catch (NoSuchFieldException var1) {
         }

         sDrawMatrixFieldFetched = true;
      }

   }

   private static void hiddenAnimateTransform(ImageView var0, Matrix var1) {
      if (sTryHiddenAnimateTransform) {
         try {
            var0.animateTransform(var1);
            return;
         } catch (NoSuchMethodError var2) {
            sTryHiddenAnimateTransform = false;
         }
      }

   }
}
