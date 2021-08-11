package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Interpolator;

@TargetApi(14)
@RequiresApi(14)
class ViewPropertyAnimatorCompatICS {
   public static void alpha(View var0, float var1) {
      var0.animate().alpha(var1);
   }

   public static void alphaBy(View var0, float var1) {
      var0.animate().alphaBy(var1);
   }

   public static void cancel(View var0) {
      var0.animate().cancel();
   }

   public static long getDuration(View var0) {
      return var0.animate().getDuration();
   }

   public static long getStartDelay(View var0) {
      return var0.animate().getStartDelay();
   }

   public static void rotation(View var0, float var1) {
      var0.animate().rotation(var1);
   }

   public static void rotationBy(View var0, float var1) {
      var0.animate().rotationBy(var1);
   }

   public static void rotationX(View var0, float var1) {
      var0.animate().rotationX(var1);
   }

   public static void rotationXBy(View var0, float var1) {
      var0.animate().rotationXBy(var1);
   }

   public static void rotationY(View var0, float var1) {
      var0.animate().rotationY(var1);
   }

   public static void rotationYBy(View var0, float var1) {
      var0.animate().rotationYBy(var1);
   }

   public static void scaleX(View var0, float var1) {
      var0.animate().scaleX(var1);
   }

   public static void scaleXBy(View var0, float var1) {
      var0.animate().scaleXBy(var1);
   }

   public static void scaleY(View var0, float var1) {
      var0.animate().scaleY(var1);
   }

   public static void scaleYBy(View var0, float var1) {
      var0.animate().scaleYBy(var1);
   }

   public static void setDuration(View var0, long var1) {
      var0.animate().setDuration(var1);
   }

   public static void setInterpolator(View var0, Interpolator var1) {
      var0.animate().setInterpolator(var1);
   }

   public static void setListener(final View var0, final ViewPropertyAnimatorListener var1) {
      if (var1 != null) {
         var0.animate().setListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               var1.onAnimationCancel(var0);
            }

            public void onAnimationEnd(Animator var1x) {
               var1.onAnimationEnd(var0);
            }

            public void onAnimationStart(Animator var1x) {
               var1.onAnimationStart(var0);
            }
         });
      } else {
         var0.animate().setListener((AnimatorListener)null);
      }
   }

   public static void setStartDelay(View var0, long var1) {
      var0.animate().setStartDelay(var1);
   }

   public static void start(View var0) {
      var0.animate().start();
   }

   public static void translationX(View var0, float var1) {
      var0.animate().translationX(var1);
   }

   public static void translationXBy(View var0, float var1) {
      var0.animate().translationXBy(var1);
   }

   public static void translationY(View var0, float var1) {
      var0.animate().translationY(var1);
   }

   public static void translationYBy(View var0, float var1) {
      var0.animate().translationYBy(var1);
   }

   // $FF: renamed from: x (android.view.View, float) void
   public static void method_22(View var0, float var1) {
      var0.animate().x(var1);
   }

   public static void xBy(View var0, float var1) {
      var0.animate().xBy(var1);
   }

   // $FF: renamed from: y (android.view.View, float) void
   public static void method_23(View var0, float var1) {
      var0.animate().y(var1);
   }

   public static void yBy(View var0, float var1) {
      var0.animate().yBy(var1);
   }
}
