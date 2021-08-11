package android.support.v4.animation;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(12)
@RequiresApi(12)
class HoneycombMr1AnimatorCompatProvider implements AnimatorProvider {
   private TimeInterpolator mDefaultInterpolator;

   public void clearInterpolator(View var1) {
      if (this.mDefaultInterpolator == null) {
         this.mDefaultInterpolator = (new ValueAnimator()).getInterpolator();
      }

      var1.animate().setInterpolator(this.mDefaultInterpolator);
   }

   public ValueAnimatorCompat emptyValueAnimator() {
      return new HoneycombMr1AnimatorCompatProvider.HoneycombValueAnimatorCompat(ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}));
   }

   static class AnimatorListenerCompatWrapper implements AnimatorListener {
      final ValueAnimatorCompat mValueAnimatorCompat;
      final AnimatorListenerCompat mWrapped;

      public AnimatorListenerCompatWrapper(AnimatorListenerCompat var1, ValueAnimatorCompat var2) {
         this.mWrapped = var1;
         this.mValueAnimatorCompat = var2;
      }

      public void onAnimationCancel(Animator var1) {
         this.mWrapped.onAnimationCancel(this.mValueAnimatorCompat);
      }

      public void onAnimationEnd(Animator var1) {
         this.mWrapped.onAnimationEnd(this.mValueAnimatorCompat);
      }

      public void onAnimationRepeat(Animator var1) {
         this.mWrapped.onAnimationRepeat(this.mValueAnimatorCompat);
      }

      public void onAnimationStart(Animator var1) {
         this.mWrapped.onAnimationStart(this.mValueAnimatorCompat);
      }
   }

   static class HoneycombValueAnimatorCompat implements ValueAnimatorCompat {
      final Animator mWrapped;

      public HoneycombValueAnimatorCompat(Animator var1) {
         this.mWrapped = var1;
      }

      public void addListener(AnimatorListenerCompat var1) {
         this.mWrapped.addListener(new HoneycombMr1AnimatorCompatProvider.AnimatorListenerCompatWrapper(var1, this));
      }

      public void addUpdateListener(final AnimatorUpdateListenerCompat var1) {
         if (this.mWrapped instanceof ValueAnimator) {
            ((ValueAnimator)this.mWrapped).addUpdateListener(new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1x) {
                  var1.onAnimationUpdate(HoneycombValueAnimatorCompat.this);
               }
            });
         }

      }

      public void cancel() {
         this.mWrapped.cancel();
      }

      public float getAnimatedFraction() {
         return ((ValueAnimator)this.mWrapped).getAnimatedFraction();
      }

      public void setDuration(long var1) {
         this.mWrapped.setDuration(var1);
      }

      public void setTarget(View var1) {
         this.mWrapped.setTarget(var1);
      }

      public void start() {
         this.mWrapped.start();
      }
   }
}
