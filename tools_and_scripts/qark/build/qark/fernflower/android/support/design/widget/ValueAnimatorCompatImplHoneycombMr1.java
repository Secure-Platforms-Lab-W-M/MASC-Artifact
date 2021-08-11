package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;

@TargetApi(12)
@RequiresApi(12)
class ValueAnimatorCompatImplHoneycombMr1 extends ValueAnimatorCompat.Impl {
   private final ValueAnimator mValueAnimator = new ValueAnimator();

   public void addListener(final ValueAnimatorCompat.Impl.AnimatorListenerProxy var1) {
      this.mValueAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            var1.onAnimationCancel();
         }

         public void onAnimationEnd(Animator var1x) {
            var1.onAnimationEnd();
         }

         public void onAnimationStart(Animator var1x) {
            var1.onAnimationStart();
         }
      });
   }

   public void addUpdateListener(final ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy var1) {
      this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1x) {
            var1.onAnimationUpdate();
         }
      });
   }

   public void cancel() {
      this.mValueAnimator.cancel();
   }

   public void end() {
      this.mValueAnimator.end();
   }

   public float getAnimatedFloatValue() {
      return (Float)this.mValueAnimator.getAnimatedValue();
   }

   public float getAnimatedFraction() {
      return this.mValueAnimator.getAnimatedFraction();
   }

   public int getAnimatedIntValue() {
      return (Integer)this.mValueAnimator.getAnimatedValue();
   }

   public long getDuration() {
      return this.mValueAnimator.getDuration();
   }

   public boolean isRunning() {
      return this.mValueAnimator.isRunning();
   }

   public void setDuration(long var1) {
      this.mValueAnimator.setDuration(var1);
   }

   public void setFloatValues(float var1, float var2) {
      this.mValueAnimator.setFloatValues(new float[]{var1, var2});
   }

   public void setIntValues(int var1, int var2) {
      this.mValueAnimator.setIntValues(new int[]{var1, var2});
   }

   public void setInterpolator(Interpolator var1) {
      this.mValueAnimator.setInterpolator(var1);
   }

   public void start() {
      this.mValueAnimator.start();
   }
}
