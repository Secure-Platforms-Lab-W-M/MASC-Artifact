package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(16)
@RequiresApi(16)
class ViewPropertyAnimatorCompatJB {
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

   public static void withEndAction(View var0, Runnable var1) {
      var0.animate().withEndAction(var1);
   }

   public static void withLayer(View var0) {
      var0.animate().withLayer();
   }

   public static void withStartAction(View var0, Runnable var1) {
      var0.animate().withStartAction(var1);
   }
}
