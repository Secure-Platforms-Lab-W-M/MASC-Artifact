package com.nineoldandroids.animation;

public class TimeAnimator extends ValueAnimator {
   private TimeAnimator.TimeListener mListener;
   private long mPreviousTime = -1L;

   void animateValue(float var1) {
   }

   boolean animationFrame(long var1) {
      int var3 = this.mPlayingState;
      long var4 = 0L;
      if (var3 == 0) {
         this.mPlayingState = 1;
         if (this.mSeekTime < 0L) {
            this.mStartTime = var1;
         } else {
            this.mStartTime = var1 - this.mSeekTime;
            this.mSeekTime = -1L;
         }
      }

      if (this.mListener != null) {
         long var6 = this.mStartTime;
         long var8 = this.mPreviousTime;
         if (var8 >= 0L) {
            var4 = var1 - var8;
         }

         this.mPreviousTime = var1;
         this.mListener.onTimeUpdate(this, var1 - var6, var4);
      }

      return false;
   }

   void initAnimation() {
   }

   public void setTimeListener(TimeAnimator.TimeListener var1) {
      this.mListener = var1;
   }

   public interface TimeListener {
      void onTimeUpdate(TimeAnimator var1, long var2, long var4);
   }
}
