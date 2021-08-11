package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AndroidRuntimeException;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ValueAnimator extends Animator {
   static final int ANIMATION_FRAME = 1;
   static final int ANIMATION_START = 0;
   private static final long DEFAULT_FRAME_DELAY = 10L;
   public static final int INFINITE = -1;
   public static final int RESTART = 1;
   public static final int REVERSE = 2;
   static final int RUNNING = 1;
   static final int SEEKED = 2;
   static final int STOPPED = 0;
   private static ThreadLocal sAnimationHandler = new ThreadLocal();
   private static final ThreadLocal sAnimations = new ThreadLocal() {
      protected ArrayList initialValue() {
         return new ArrayList();
      }
   };
   private static final Interpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
   private static final ThreadLocal sDelayedAnims = new ThreadLocal() {
      protected ArrayList initialValue() {
         return new ArrayList();
      }
   };
   private static final ThreadLocal sEndingAnims = new ThreadLocal() {
      protected ArrayList initialValue() {
         return new ArrayList();
      }
   };
   private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
   private static long sFrameDelay = 10L;
   private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
   private static final ThreadLocal sPendingAnimations = new ThreadLocal() {
      protected ArrayList initialValue() {
         return new ArrayList();
      }
   };
   private static final ThreadLocal sReadyAnims = new ThreadLocal() {
      protected ArrayList initialValue() {
         return new ArrayList();
      }
   };
   private float mCurrentFraction = 0.0F;
   private int mCurrentIteration = 0;
   private long mDelayStartTime;
   private long mDuration = 300L;
   boolean mInitialized = false;
   private Interpolator mInterpolator;
   private boolean mPlayingBackwards = false;
   int mPlayingState = 0;
   private int mRepeatCount = 0;
   private int mRepeatMode = 1;
   private boolean mRunning = false;
   long mSeekTime = -1L;
   private long mStartDelay = 0L;
   long mStartTime;
   private boolean mStarted = false;
   private boolean mStartedDelay = false;
   private ArrayList mUpdateListeners;
   PropertyValuesHolder[] mValues;
   HashMap mValuesMap;

   public ValueAnimator() {
      this.mInterpolator = sDefaultInterpolator;
      this.mUpdateListeners = null;
   }

   public static void clearAllAnimations() {
      ((ArrayList)sAnimations.get()).clear();
      ((ArrayList)sPendingAnimations.get()).clear();
      ((ArrayList)sDelayedAnims.get()).clear();
   }

   private boolean delayedAnimationFrame(long var1) {
      if (!this.mStartedDelay) {
         this.mStartedDelay = true;
         this.mDelayStartTime = var1;
      } else {
         long var3 = var1 - this.mDelayStartTime;
         long var5 = this.mStartDelay;
         if (var3 > var5) {
            this.mStartTime = var1 - (var3 - var5);
            this.mPlayingState = 1;
            return true;
         }
      }

      return false;
   }

   private void endAnimation() {
      ((ArrayList)sAnimations.get()).remove(this);
      ((ArrayList)sPendingAnimations.get()).remove(this);
      ((ArrayList)sDelayedAnims.get()).remove(this);
      this.mPlayingState = 0;
      if (this.mRunning && this.mListeners != null) {
         ArrayList var3 = (ArrayList)this.mListeners.clone();
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ((Animator.AnimatorListener)var3.get(var1)).onAnimationEnd(this);
         }
      }

      this.mRunning = false;
      this.mStarted = false;
   }

   public static int getCurrentAnimationsCount() {
      return ((ArrayList)sAnimations.get()).size();
   }

   public static long getFrameDelay() {
      return sFrameDelay;
   }

   public static ValueAnimator ofFloat(float... var0) {
      ValueAnimator var1 = new ValueAnimator();
      var1.setFloatValues(var0);
      return var1;
   }

   public static ValueAnimator ofInt(int... var0) {
      ValueAnimator var1 = new ValueAnimator();
      var1.setIntValues(var0);
      return var1;
   }

   public static ValueAnimator ofObject(TypeEvaluator var0, Object... var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setObjectValues(var1);
      var2.setEvaluator(var0);
      return var2;
   }

   public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... var0) {
      ValueAnimator var1 = new ValueAnimator();
      var1.setValues(var0);
      return var1;
   }

   public static void setFrameDelay(long var0) {
      sFrameDelay = var0;
   }

   private void start(boolean var1) {
      if (Looper.myLooper() == null) {
         throw new AndroidRuntimeException("Animators may only be run on Looper threads");
      } else {
         this.mPlayingBackwards = var1;
         this.mCurrentIteration = 0;
         this.mPlayingState = 0;
         this.mStarted = true;
         this.mStartedDelay = false;
         ((ArrayList)sPendingAnimations.get()).add(this);
         if (this.mStartDelay == 0L) {
            this.setCurrentPlayTime(this.getCurrentPlayTime());
            this.mPlayingState = 0;
            this.mRunning = true;
            if (this.mListeners != null) {
               ArrayList var4 = (ArrayList)this.mListeners.clone();
               int var3 = var4.size();

               for(int var2 = 0; var2 < var3; ++var2) {
                  ((Animator.AnimatorListener)var4.get(var2)).onAnimationStart(this);
               }
            }
         }

         ValueAnimator.AnimationHandler var5 = (ValueAnimator.AnimationHandler)sAnimationHandler.get();
         ValueAnimator.AnimationHandler var6 = var5;
         if (var5 == null) {
            var6 = new ValueAnimator.AnimationHandler();
            sAnimationHandler.set(var6);
         }

         var6.sendEmptyMessage(0);
      }
   }

   private void startAnimation() {
      this.initAnimation();
      ((ArrayList)sAnimations.get()).add(this);
      if (this.mStartDelay > 0L && this.mListeners != null) {
         ArrayList var3 = (ArrayList)this.mListeners.clone();
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ((Animator.AnimatorListener)var3.get(var1)).onAnimationStart(this);
         }
      }

   }

   public void addUpdateListener(ValueAnimator.AnimatorUpdateListener var1) {
      if (this.mUpdateListeners == null) {
         this.mUpdateListeners = new ArrayList();
      }

      this.mUpdateListeners.add(var1);
   }

   void animateValue(float var1) {
      var1 = this.mInterpolator.getInterpolation(var1);
      this.mCurrentFraction = var1;
      int var3 = this.mValues.length;

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         this.mValues[var2].calculateValue(var1);
      }

      ArrayList var4 = this.mUpdateListeners;
      if (var4 != null) {
         var3 = var4.size();

         for(var2 = 0; var2 < var3; ++var2) {
            ((ValueAnimator.AnimatorUpdateListener)this.mUpdateListeners.get(var2)).onAnimationUpdate(this);
         }
      }

   }

   boolean animationFrame(long var1) {
      boolean var10 = false;
      long var7;
      if (this.mPlayingState == 0) {
         this.mPlayingState = 1;
         var7 = this.mSeekTime;
         if (var7 < 0L) {
            this.mStartTime = var1;
         } else {
            this.mStartTime = var1 - var7;
            this.mSeekTime = -1L;
         }
      }

      int var5 = this.mPlayingState;
      if (var5 != 1 && var5 != 2) {
         return false;
      } else {
         var7 = this.mDuration;
         float var4;
         if (var7 > 0L) {
            var4 = (float)(var1 - this.mStartTime) / (float)var7;
         } else {
            var4 = 1.0F;
         }

         boolean var9 = var10;
         float var3 = var4;
         if (var4 >= 1.0F) {
            var5 = this.mCurrentIteration;
            int var6 = this.mRepeatCount;
            if (var5 >= var6 && var6 != -1) {
               var9 = true;
               var3 = Math.min(var4, 1.0F);
            } else {
               if (this.mListeners != null) {
                  var6 = this.mListeners.size();

                  for(var5 = 0; var5 < var6; ++var5) {
                     ((Animator.AnimatorListener)this.mListeners.get(var5)).onAnimationRepeat(this);
                  }
               }

               if (this.mRepeatMode == 2) {
                  this.mPlayingBackwards ^= true;
               }

               this.mCurrentIteration += (int)var4;
               var3 = var4 % 1.0F;
               this.mStartTime += this.mDuration;
               var9 = var10;
            }
         }

         var4 = var3;
         if (this.mPlayingBackwards) {
            var4 = 1.0F - var3;
         }

         this.animateValue(var4);
         return var9;
      }
   }

   public void cancel() {
      if (this.mPlayingState != 0 || ((ArrayList)sPendingAnimations.get()).contains(this) || ((ArrayList)sDelayedAnims.get()).contains(this)) {
         if (this.mRunning && this.mListeners != null) {
            Iterator var1 = ((ArrayList)this.mListeners.clone()).iterator();

            while(var1.hasNext()) {
               ((Animator.AnimatorListener)var1.next()).onAnimationCancel(this);
            }
         }

         this.endAnimation();
      }

   }

   public ValueAnimator clone() {
      ValueAnimator var3 = (ValueAnimator)super.clone();
      int var1;
      int var2;
      if (this.mUpdateListeners != null) {
         ArrayList var4 = this.mUpdateListeners;
         var3.mUpdateListeners = new ArrayList();
         var2 = var4.size();

         for(var1 = 0; var1 < var2; ++var1) {
            var3.mUpdateListeners.add(var4.get(var1));
         }
      }

      var3.mSeekTime = -1L;
      var3.mPlayingBackwards = false;
      var3.mCurrentIteration = 0;
      var3.mInitialized = false;
      var3.mPlayingState = 0;
      var3.mStartedDelay = false;
      PropertyValuesHolder[] var6 = this.mValues;
      if (var6 != null) {
         var2 = var6.length;
         var3.mValues = new PropertyValuesHolder[var2];
         var3.mValuesMap = new HashMap(var2);

         for(var1 = 0; var1 < var2; ++var1) {
            PropertyValuesHolder var5 = var6[var1].clone();
            var3.mValues[var1] = var5;
            var3.mValuesMap.put(var5.getPropertyName(), var5);
         }
      }

      return var3;
   }

   public void end() {
      if (!((ArrayList)sAnimations.get()).contains(this) && !((ArrayList)sPendingAnimations.get()).contains(this)) {
         this.mStartedDelay = false;
         this.startAnimation();
      } else if (!this.mInitialized) {
         this.initAnimation();
      }

      int var1 = this.mRepeatCount;
      if (var1 > 0 && (var1 & 1) == 1) {
         this.animateValue(0.0F);
      } else {
         this.animateValue(1.0F);
      }

      this.endAnimation();
   }

   public float getAnimatedFraction() {
      return this.mCurrentFraction;
   }

   public Object getAnimatedValue() {
      PropertyValuesHolder[] var1 = this.mValues;
      return var1 != null && var1.length > 0 ? var1[0].getAnimatedValue() : null;
   }

   public Object getAnimatedValue(String var1) {
      PropertyValuesHolder var2 = (PropertyValuesHolder)this.mValuesMap.get(var1);
      return var2 != null ? var2.getAnimatedValue() : null;
   }

   public long getCurrentPlayTime() {
      return this.mInitialized && this.mPlayingState != 0 ? AnimationUtils.currentAnimationTimeMillis() - this.mStartTime : 0L;
   }

   public long getDuration() {
      return this.mDuration;
   }

   public Interpolator getInterpolator() {
      return this.mInterpolator;
   }

   public int getRepeatCount() {
      return this.mRepeatCount;
   }

   public int getRepeatMode() {
      return this.mRepeatMode;
   }

   public long getStartDelay() {
      return this.mStartDelay;
   }

   public PropertyValuesHolder[] getValues() {
      return this.mValues;
   }

   void initAnimation() {
      if (!this.mInitialized) {
         int var2 = this.mValues.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            this.mValues[var1].init();
         }

         this.mInitialized = true;
      }

   }

   public boolean isRunning() {
      int var1 = this.mPlayingState;
      boolean var2 = true;
      if (var1 != 1) {
         if (this.mRunning) {
            return true;
         }

         var2 = false;
      }

      return var2;
   }

   public boolean isStarted() {
      return this.mStarted;
   }

   public void removeAllUpdateListeners() {
      ArrayList var1 = this.mUpdateListeners;
      if (var1 != null) {
         var1.clear();
         this.mUpdateListeners = null;
      }
   }

   public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener var1) {
      ArrayList var2 = this.mUpdateListeners;
      if (var2 != null) {
         var2.remove(var1);
         if (this.mUpdateListeners.size() == 0) {
            this.mUpdateListeners = null;
         }

      }
   }

   public void reverse() {
      this.mPlayingBackwards ^= true;
      if (this.mPlayingState == 1) {
         long var1 = AnimationUtils.currentAnimationTimeMillis();
         long var3 = this.mStartTime;
         this.mStartTime = var1 - (this.mDuration - (var1 - var3));
      } else {
         this.start(true);
      }
   }

   public void setCurrentPlayTime(long var1) {
      this.initAnimation();
      long var3 = AnimationUtils.currentAnimationTimeMillis();
      if (this.mPlayingState != 1) {
         this.mSeekTime = var1;
         this.mPlayingState = 2;
      }

      this.mStartTime = var3 - var1;
      this.animationFrame(var3);
   }

   public ValueAnimator setDuration(long var1) {
      if (var1 >= 0L) {
         this.mDuration = var1;
         return this;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Animators cannot have negative duration: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void setEvaluator(TypeEvaluator var1) {
      if (var1 != null) {
         PropertyValuesHolder[] var2 = this.mValues;
         if (var2 != null && var2.length > 0) {
            var2[0].setEvaluator(var1);
         }
      }

   }

   public void setFloatValues(float... var1) {
      if (var1 != null) {
         if (var1.length != 0) {
            PropertyValuesHolder[] var2 = this.mValues;
            if (var2 != null && var2.length != 0) {
               var2[0].setFloatValues(var1);
            } else {
               this.setValues(PropertyValuesHolder.ofFloat("", var1));
            }

            this.mInitialized = false;
         }
      }
   }

   public void setIntValues(int... var1) {
      if (var1 != null) {
         if (var1.length != 0) {
            PropertyValuesHolder[] var2 = this.mValues;
            if (var2 != null && var2.length != 0) {
               var2[0].setIntValues(var1);
            } else {
               this.setValues(PropertyValuesHolder.ofInt("", var1));
            }

            this.mInitialized = false;
         }
      }
   }

   public void setInterpolator(Interpolator var1) {
      if (var1 != null) {
         this.mInterpolator = var1;
      } else {
         this.mInterpolator = new LinearInterpolator();
      }
   }

   public void setObjectValues(Object... var1) {
      if (var1 != null) {
         if (var1.length != 0) {
            PropertyValuesHolder[] var2 = this.mValues;
            if (var2 != null && var2.length != 0) {
               var2[0].setObjectValues(var1);
            } else {
               this.setValues(PropertyValuesHolder.ofObject("", (TypeEvaluator)null, var1));
            }

            this.mInitialized = false;
         }
      }
   }

   public void setRepeatCount(int var1) {
      this.mRepeatCount = var1;
   }

   public void setRepeatMode(int var1) {
      this.mRepeatMode = var1;
   }

   public void setStartDelay(long var1) {
      this.mStartDelay = var1;
   }

   public void setValues(PropertyValuesHolder... var1) {
      int var3 = var1.length;
      this.mValues = var1;
      this.mValuesMap = new HashMap(var3);

      for(int var2 = 0; var2 < var3; ++var2) {
         PropertyValuesHolder var4 = var1[var2];
         this.mValuesMap.put(var4.getPropertyName(), var4);
      }

      this.mInitialized = false;
   }

   public void start() {
      this.start(false);
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("ValueAnimator@");
      var2.append(Integer.toHexString(this.hashCode()));
      String var4 = var2.toString();
      String var3 = var4;
      if (this.mValues != null) {
         int var1 = 0;

         while(true) {
            var3 = var4;
            if (var1 >= this.mValues.length) {
               break;
            }

            StringBuilder var5 = new StringBuilder();
            var5.append(var4);
            var5.append("\n    ");
            var5.append(this.mValues[var1].toString());
            var4 = var5.toString();
            ++var1;
         }
      }

      return var3;
   }

   private static class AnimationHandler extends Handler {
      private AnimationHandler() {
      }

      // $FF: synthetic method
      AnimationHandler(Object var1) {
         this();
      }

      public void handleMessage(Message var1) {
         boolean var3 = true;
         boolean var2 = true;
         ArrayList var7 = (ArrayList)ValueAnimator.sAnimations.get();
         ArrayList var8 = (ArrayList)ValueAnimator.sDelayedAnims.get();
         int var4 = var1.what;
         ArrayList var9;
         ValueAnimator var10;
         ArrayList var11;
         if (var4 != 0) {
            if (var4 != 1) {
               return;
            }
         } else {
            var11 = (ArrayList)ValueAnimator.sPendingAnimations.get();
            if (var7.size() > 0 || var8.size() > 0) {
               var2 = false;
            }

            while(true) {
               var3 = var2;
               if (var11.size() <= 0) {
                  break;
               }

               var9 = (ArrayList)var11.clone();
               var11.clear();
               var4 = var9.size();

               for(int var13 = 0; var13 < var4; ++var13) {
                  var10 = (ValueAnimator)var9.get(var13);
                  if (var10.mStartDelay == 0L) {
                     var10.startAnimation();
                  } else {
                     var8.add(var10);
                  }
               }
            }
         }

         long var5 = AnimationUtils.currentAnimationTimeMillis();
         var9 = (ArrayList)ValueAnimator.sReadyAnims.get();
         var11 = (ArrayList)ValueAnimator.sEndingAnims.get();
         var4 = var8.size();

         int var12;
         for(var12 = 0; var12 < var4; ++var12) {
            var10 = (ValueAnimator)var8.get(var12);
            if (var10.delayedAnimationFrame(var5)) {
               var9.add(var10);
            }
         }

         var4 = var9.size();
         if (var4 > 0) {
            for(var12 = 0; var12 < var4; ++var12) {
               var10 = (ValueAnimator)var9.get(var12);
               var10.startAnimation();
               var10.mRunning = true;
               var8.remove(var10);
            }

            var9.clear();
         }

         var12 = var7.size();
         var4 = 0;

         while(var4 < var12) {
            ValueAnimator var14 = (ValueAnimator)var7.get(var4);
            if (var14.animationFrame(var5)) {
               var11.add(var14);
            }

            if (var7.size() == var12) {
               ++var4;
            } else {
               --var12;
               var11.remove(var14);
            }
         }

         if (var11.size() > 0) {
            for(var12 = 0; var12 < var11.size(); ++var12) {
               ((ValueAnimator)var11.get(var12)).endAnimation();
            }

            var11.clear();
         }

         if (var3 && (!var7.isEmpty() || !var8.isEmpty())) {
            this.sendEmptyMessageDelayed(1, Math.max(0L, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - var5)));
         }

      }
   }

   public interface AnimatorUpdateListener {
      void onAnimationUpdate(ValueAnimator var1);
   }
}
