package android.support.transition;

import android.animation.Animator;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.widget.ImageView;

class ImageViewUtils {
   private static final ImageViewUtilsImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ImageViewUtilsApi21();
      } else {
         IMPL = new ImageViewUtilsApi14();
      }
   }

   static void animateTransform(ImageView var0, Matrix var1) {
      IMPL.animateTransform(var0, var1);
   }

   static void reserveEndAnimateTransform(ImageView var0, Animator var1) {
      IMPL.reserveEndAnimateTransform(var0, var1);
   }

   static void startAnimateTransform(ImageView var0) {
      IMPL.startAnimateTransform(var0);
   }
}
