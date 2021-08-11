package android.support.design.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import java.util.ArrayList;

class ValueAnimatorCompatImplGingerbread extends ValueAnimatorCompat.Impl {
   private static final int DEFAULT_DURATION = 200;
   private static final int HANDLER_DELAY = 10;
   private static final Handler sHandler = new Handler(Looper.getMainLooper());
   private float mAnimatedFraction;
   private long mDuration = 200L;
   private final float[] mFloatValues = new float[2];
   private final int[] mIntValues = new int[2];
   private Interpolator mInterpolator;
   private boolean mIsRunning;
   private ArrayList mListeners;
   private final Runnable mRunnable = new Runnable() {
      public void run() {
         ValueAnimatorCompatImplGingerbread.this.update();
      }
   };
   private long mStartTime;
   private ArrayList mUpdateListeners;

   private void dispatchAnimationCancel() {
      if (this.mListeners != null) {
         int var1 = 0;

         for(int var2 = this.mListeners.size(); var1 < var2; ++var1) {
            ((ValueAnimatorCompat.Impl.AnimatorListenerProxy)this.mListeners.get(var1)).onAnimationCancel();
         }
      }

   }

   private void dispatchAnimationEnd() {
      if (this.mListeners != null) {
         int var1 = 0;

         for(int var2 = this.mListeners.size(); var1 < var2; ++var1) {
            ((ValueAnimatorCompat.Impl.AnimatorListenerProxy)this.mListeners.get(var1)).onAnimationEnd();
         }
      }

   }

   private void dispatchAnimationStart() {
      if (this.mListeners != null) {
         int var1 = 0;

         for(int var2 = this.mListeners.size(); var1 < var2; ++var1) {
            ((ValueAnimatorCompat.Impl.AnimatorListenerProxy)this.mListeners.get(var1)).onAnimationStart();
         }
      }

   }

   private void dispatchAnimationUpdate() {
      if (this.mUpdateListeners != null) {
         int var1 = 0;

         for(int var2 = this.mUpdateListeners.size(); var1 < var2; ++var1) {
            ((ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy)this.mUpdateListeners.get(var1)).onAnimationUpdate();
         }
      }

   }

   public void addListener(ValueAnimatorCompat.Impl.AnimatorListenerProxy var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      this.mListeners.add(var1);
   }

   public void addUpdateListener(ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy var1) {
      if (this.mUpdateListeners == null) {
         this.mUpdateListeners = new ArrayList();
      }

      this.mUpdateListeners.add(var1);
   }

   public void cancel() {
      this.mIsRunning = false;
      sHandler.removeCallbacks(this.mRunnable);
      this.dispatchAnimationCancel();
      this.dispatchAnimationEnd();
   }

   public void end() {
      if (this.mIsRunning) {
         this.mIsRunning = false;
         sHandler.removeCallbacks(this.mRunnable);
         this.mAnimatedFraction = 1.0F;
         this.dispatchAnimationUpdate();
         this.dispatchAnimationEnd();
      }

   }

   public float getAnimatedFloatValue() {
      return AnimationUtils.lerp(this.mFloatValues[0], this.mFloatValues[1], this.getAnimatedFraction());
   }

   public float getAnimatedFraction() {
      return this.mAnimatedFraction;
   }

   public int getAnimatedIntValue() {
      return AnimationUtils.lerp(this.mIntValues[0], this.mIntValues[1], this.getAnimatedFraction());
   }

   public long getDuration() {
      return this.mDuration;
   }

   public boolean isRunning() {
      return this.mIsRunning;
   }

   public void setDuration(long var1) {
      this.mDuration = var1;
   }

   public void setFloatValues(float var1, float var2) {
      this.mFloatValues[0] = var1;
      this.mFloatValues[1] = var2;
   }

   public void setIntValues(int var1, int var2) {
      this.mIntValues[0] = var1;
      this.mIntValues[1] = var2;
   }

   public void setInterpolator(Interpolator var1) {
      this.mInterpolator = var1;
   }

   public void start() {
      if (!this.mIsRunning) {
         if (this.mInterpolator == null) {
            this.mInterpolator = new AccelerateDecelerateInterpolator();
         }

         this.mIsRunning = true;
         this.mAnimatedFraction = 0.0F;
         this.startInternal();
      }
   }

   final void startInternal() {
      this.mStartTime = SystemClock.uptimeMillis();
      this.dispatchAnimationUpdate();
      this.dispatchAnimationStart();
      sHandler.postDelayed(this.mRunnable, 10L);
   }

   final void update() {
      if (this.mIsRunning) {
         float var2 = MathUtils.constrain((float)(SystemClock.uptimeMillis() - this.mStartTime) / (float)this.mDuration, 0.0F, 1.0F);
         float var1 = var2;
         if (this.mInterpolator != null) {
            var1 = this.mInterpolator.getInterpolation(var2);
         }

         this.mAnimatedFraction = var1;
         this.dispatchAnimationUpdate();
         if (SystemClock.uptimeMillis() >= this.mStartTime + this.mDuration) {
            this.mIsRunning = false;
            this.dispatchAnimationEnd();
         }
      }

      if (this.mIsRunning) {
         sHandler.postDelayed(this.mRunnable, 10L);
      }

   }
}
