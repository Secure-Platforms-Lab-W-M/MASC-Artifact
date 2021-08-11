package android.support.v4.animation;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

@TargetApi(9)
@RequiresApi(9)
class GingerbreadAnimatorCompatProvider implements AnimatorProvider {
   public void clearInterpolator(View var1) {
   }

   public ValueAnimatorCompat emptyValueAnimator() {
      return new GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator();
   }

   private static class GingerbreadFloatValueAnimator implements ValueAnimatorCompat {
      private long mDuration = 200L;
      private boolean mEnded = false;
      private float mFraction = 0.0F;
      List mListeners = new ArrayList();
      private Runnable mLoopRunnable = new Runnable() {
         public void run() {
            float var1 = (float)(GingerbreadFloatValueAnimator.this.getTime() - GingerbreadFloatValueAnimator.this.mStartTime) * 1.0F / (float)GingerbreadFloatValueAnimator.this.mDuration;
            if (var1 > 1.0F || GingerbreadFloatValueAnimator.this.mTarget.getParent() == null) {
               var1 = 1.0F;
            }

            GingerbreadFloatValueAnimator.this.mFraction = var1;
            GingerbreadFloatValueAnimator.this.notifyUpdateListeners();
            if (GingerbreadFloatValueAnimator.this.mFraction >= 1.0F) {
               GingerbreadFloatValueAnimator.this.dispatchEnd();
            } else {
               GingerbreadFloatValueAnimator.this.mTarget.postDelayed(GingerbreadFloatValueAnimator.this.mLoopRunnable, 16L);
            }
         }
      };
      private long mStartTime;
      private boolean mStarted = false;
      View mTarget;
      List mUpdateListeners = new ArrayList();

      public GingerbreadFloatValueAnimator() {
      }

      private void dispatchCancel() {
         for(int var1 = this.mListeners.size() - 1; var1 >= 0; --var1) {
            ((AnimatorListenerCompat)this.mListeners.get(var1)).onAnimationCancel(this);
         }

      }

      private void dispatchEnd() {
         for(int var1 = this.mListeners.size() - 1; var1 >= 0; --var1) {
            ((AnimatorListenerCompat)this.mListeners.get(var1)).onAnimationEnd(this);
         }

      }

      private void dispatchStart() {
         for(int var1 = this.mListeners.size() - 1; var1 >= 0; --var1) {
            ((AnimatorListenerCompat)this.mListeners.get(var1)).onAnimationStart(this);
         }

      }

      private long getTime() {
         return this.mTarget.getDrawingTime();
      }

      private void notifyUpdateListeners() {
         for(int var1 = this.mUpdateListeners.size() - 1; var1 >= 0; --var1) {
            ((AnimatorUpdateListenerCompat)this.mUpdateListeners.get(var1)).onAnimationUpdate(this);
         }

      }

      public void addListener(AnimatorListenerCompat var1) {
         this.mListeners.add(var1);
      }

      public void addUpdateListener(AnimatorUpdateListenerCompat var1) {
         this.mUpdateListeners.add(var1);
      }

      public void cancel() {
         if (!this.mEnded) {
            this.mEnded = true;
            if (this.mStarted) {
               this.dispatchCancel();
            }

            this.dispatchEnd();
         }
      }

      public float getAnimatedFraction() {
         return this.mFraction;
      }

      public void setDuration(long var1) {
         if (!this.mStarted) {
            this.mDuration = var1;
         }

      }

      public void setTarget(View var1) {
         this.mTarget = var1;
      }

      public void start() {
         if (!this.mStarted) {
            this.mStarted = true;
            this.dispatchStart();
            this.mFraction = 0.0F;
            this.mStartTime = this.getTime();
            this.mTarget.postDelayed(this.mLoopRunnable, 16L);
         }
      }
   }
}
