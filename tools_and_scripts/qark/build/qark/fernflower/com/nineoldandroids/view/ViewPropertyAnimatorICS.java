package com.nineoldandroids.view;

import android.animation.Animator.AnimatorListener;
import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.lang.ref.WeakReference;

class ViewPropertyAnimatorICS extends ViewPropertyAnimator {
   private static final long RETURN_WHEN_NULL = -1L;
   private final WeakReference mNative;

   ViewPropertyAnimatorICS(View var1) {
      this.mNative = new WeakReference(var1.animate());
   }

   public ViewPropertyAnimator alpha(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.alpha(var1);
      }

      return this;
   }

   public ViewPropertyAnimator alphaBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.alphaBy(var1);
      }

      return this;
   }

   public void cancel() {
      android.view.ViewPropertyAnimator var1 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var1 != null) {
         var1.cancel();
      }

   }

   public long getDuration() {
      android.view.ViewPropertyAnimator var1 = (android.view.ViewPropertyAnimator)this.mNative.get();
      return var1 != null ? var1.getDuration() : -1L;
   }

   public long getStartDelay() {
      android.view.ViewPropertyAnimator var1 = (android.view.ViewPropertyAnimator)this.mNative.get();
      return var1 != null ? var1.getStartDelay() : -1L;
   }

   public ViewPropertyAnimator rotation(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotation(var1);
      }

      return this;
   }

   public ViewPropertyAnimator rotationBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotationBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator rotationX(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotationX(var1);
      }

      return this;
   }

   public ViewPropertyAnimator rotationXBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotationXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator rotationY(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotationY(var1);
      }

      return this;
   }

   public ViewPropertyAnimator rotationYBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.rotationYBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator scaleX(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.scaleX(var1);
      }

      return this;
   }

   public ViewPropertyAnimator scaleXBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.scaleXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator scaleY(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.scaleY(var1);
      }

      return this;
   }

   public ViewPropertyAnimator scaleYBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.scaleYBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator setDuration(long var1) {
      android.view.ViewPropertyAnimator var3 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var3 != null) {
         var3.setDuration(var1);
      }

      return this;
   }

   public ViewPropertyAnimator setInterpolator(Interpolator var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.setInterpolator(var1);
      }

      return this;
   }

   public ViewPropertyAnimator setListener(final Animator.AnimatorListener var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         if (var1 == null) {
            var2.setListener((AnimatorListener)null);
            return this;
         }

         var2.setListener(new AnimatorListener() {
            public void onAnimationCancel(android.animation.Animator var1x) {
               var1.onAnimationCancel((Animator)null);
            }

            public void onAnimationEnd(android.animation.Animator var1x) {
               var1.onAnimationEnd((Animator)null);
            }

            public void onAnimationRepeat(android.animation.Animator var1x) {
               var1.onAnimationRepeat((Animator)null);
            }

            public void onAnimationStart(android.animation.Animator var1x) {
               var1.onAnimationStart((Animator)null);
            }
         });
      }

      return this;
   }

   public ViewPropertyAnimator setStartDelay(long var1) {
      android.view.ViewPropertyAnimator var3 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var3 != null) {
         var3.setStartDelay(var1);
      }

      return this;
   }

   public void start() {
      android.view.ViewPropertyAnimator var1 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var1 != null) {
         var1.start();
      }

   }

   public ViewPropertyAnimator translationX(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.translationX(var1);
      }

      return this;
   }

   public ViewPropertyAnimator translationXBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.translationXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimator translationY(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.translationY(var1);
      }

      return this;
   }

   public ViewPropertyAnimator translationYBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.translationYBy(var1);
      }

      return this;
   }

   // $FF: renamed from: x (float) com.nineoldandroids.view.ViewPropertyAnimator
   public ViewPropertyAnimator method_21(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.x(var1);
      }

      return this;
   }

   public ViewPropertyAnimator xBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.xBy(var1);
      }

      return this;
   }

   // $FF: renamed from: y (float) com.nineoldandroids.view.ViewPropertyAnimator
   public ViewPropertyAnimator method_22(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.y(var1);
      }

      return this;
   }

   public ViewPropertyAnimator yBy(float var1) {
      android.view.ViewPropertyAnimator var2 = (android.view.ViewPropertyAnimator)this.mNative.get();
      if (var2 != null) {
         var2.yBy(var1);
      }

      return this;
   }
}
