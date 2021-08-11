package com.nineoldandroids.view;

import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class ViewPropertyAnimatorHC extends ViewPropertyAnimator {
   private static final int ALPHA = 512;
   private static final int NONE = 0;
   private static final int ROTATION = 16;
   private static final int ROTATION_X = 32;
   private static final int ROTATION_Y = 64;
   private static final int SCALE_X = 4;
   private static final int SCALE_Y = 8;
   private static final int TRANSFORM_MASK = 511;
   private static final int TRANSLATION_X = 1;
   private static final int TRANSLATION_Y = 2;
   // $FF: renamed from: X int
   private static final int field_64 = 128;
   // $FF: renamed from: Y int
   private static final int field_65 = 256;
   private Runnable mAnimationStarter = new Runnable() {
      public void run() {
         ViewPropertyAnimatorHC.this.startAnimation();
      }
   };
   private ViewPropertyAnimatorHC.AnimatorEventListener mAnimatorEventListener = new ViewPropertyAnimatorHC.AnimatorEventListener();
   private HashMap mAnimatorMap = new HashMap();
   private long mDuration;
   private boolean mDurationSet = false;
   private Interpolator mInterpolator;
   private boolean mInterpolatorSet = false;
   private Animator.AnimatorListener mListener = null;
   ArrayList mPendingAnimations = new ArrayList();
   private long mStartDelay = 0L;
   private boolean mStartDelaySet = false;
   private final WeakReference mView;

   ViewPropertyAnimatorHC(View var1) {
      this.mView = new WeakReference(var1);
   }

   private void animateProperty(int var1, float var2) {
      float var3 = this.getValue(var1);
      this.animatePropertyBy(var1, var3, var2 - var3);
   }

   private void animatePropertyBy(int var1, float var2) {
      this.animatePropertyBy(var1, this.getValue(var1), var2);
   }

   private void animatePropertyBy(int var1, float var2, float var3) {
      if (this.mAnimatorMap.size() > 0) {
         Object var5 = null;
         Iterator var6 = this.mAnimatorMap.keySet().iterator();

         Animator var4;
         ViewPropertyAnimatorHC.PropertyBundle var7;
         do {
            var4 = (Animator)var5;
            if (!var6.hasNext()) {
               break;
            }

            var4 = (Animator)var6.next();
            var7 = (ViewPropertyAnimatorHC.PropertyBundle)this.mAnimatorMap.get(var4);
         } while(!var7.cancel(var1) || var7.mPropertyMask != 0);

         if (var4 != null) {
            var4.cancel();
         }
      }

      ViewPropertyAnimatorHC.NameValuesHolder var8 = new ViewPropertyAnimatorHC.NameValuesHolder(var1, var2, var3);
      this.mPendingAnimations.add(var8);
      View var9 = (View)this.mView.get();
      if (var9 != null) {
         var9.removeCallbacks(this.mAnimationStarter);
         var9.post(this.mAnimationStarter);
      }

   }

   private float getValue(int var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         if (var1 == 1) {
            return var2.getTranslationX();
         }

         if (var1 == 2) {
            return var2.getTranslationY();
         }

         if (var1 == 4) {
            return var2.getScaleX();
         }

         if (var1 == 8) {
            return var2.getScaleY();
         }

         if (var1 == 16) {
            return var2.getRotation();
         }

         if (var1 == 32) {
            return var2.getRotationX();
         }

         if (var1 == 64) {
            return var2.getRotationY();
         }

         if (var1 == 128) {
            return var2.getX();
         }

         if (var1 == 256) {
            return var2.getY();
         }

         if (var1 == 512) {
            return var2.getAlpha();
         }
      }

      return 0.0F;
   }

   private void setValue(int var1, float var2) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 4) {
                  if (var1 != 8) {
                     if (var1 != 16) {
                        if (var1 != 32) {
                           if (var1 != 64) {
                              if (var1 != 128) {
                                 if (var1 != 256) {
                                    if (var1 != 512) {
                                       return;
                                    }

                                    var3.setAlpha(var2);
                                    return;
                                 }

                                 var3.setY(var2);
                                 return;
                              }

                              var3.setX(var2);
                              return;
                           }

                           var3.setRotationY(var2);
                           return;
                        }

                        var3.setRotationX(var2);
                        return;
                     }

                     var3.setRotation(var2);
                     return;
                  }

                  var3.setScaleY(var2);
                  return;
               }

               var3.setScaleX(var2);
               return;
            }

            var3.setTranslationY(var2);
            return;
         }

         var3.setTranslationX(var2);
      }

   }

   private void startAnimation() {
      ValueAnimator var4 = ValueAnimator.ofFloat(1.0F);
      ArrayList var5 = (ArrayList)this.mPendingAnimations.clone();
      this.mPendingAnimations.clear();
      int var2 = 0;
      int var3 = var5.size();

      for(int var1 = 0; var1 < var3; ++var1) {
         var2 |= ((ViewPropertyAnimatorHC.NameValuesHolder)var5.get(var1)).mNameConstant;
      }

      this.mAnimatorMap.put(var4, new ViewPropertyAnimatorHC.PropertyBundle(var2, var5));
      var4.addUpdateListener(this.mAnimatorEventListener);
      var4.addListener(this.mAnimatorEventListener);
      if (this.mStartDelaySet) {
         var4.setStartDelay(this.mStartDelay);
      }

      if (this.mDurationSet) {
         var4.setDuration(this.mDuration);
      }

      if (this.mInterpolatorSet) {
         var4.setInterpolator(this.mInterpolator);
      }

      var4.start();
   }

   public ViewPropertyAnimator alpha(float var1) {
      this.animateProperty(512, var1);
      return this;
   }

   public ViewPropertyAnimator alphaBy(float var1) {
      this.animatePropertyBy(512, var1);
      return this;
   }

   public void cancel() {
      if (this.mAnimatorMap.size() > 0) {
         Iterator var1 = ((HashMap)this.mAnimatorMap.clone()).keySet().iterator();

         while(var1.hasNext()) {
            ((Animator)var1.next()).cancel();
         }
      }

      this.mPendingAnimations.clear();
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.removeCallbacks(this.mAnimationStarter);
      }

   }

   public long getDuration() {
      return this.mDurationSet ? this.mDuration : (new ValueAnimator()).getDuration();
   }

   public long getStartDelay() {
      return this.mStartDelaySet ? this.mStartDelay : 0L;
   }

   public ViewPropertyAnimator rotation(float var1) {
      this.animateProperty(16, var1);
      return this;
   }

   public ViewPropertyAnimator rotationBy(float var1) {
      this.animatePropertyBy(16, var1);
      return this;
   }

   public ViewPropertyAnimator rotationX(float var1) {
      this.animateProperty(32, var1);
      return this;
   }

   public ViewPropertyAnimator rotationXBy(float var1) {
      this.animatePropertyBy(32, var1);
      return this;
   }

   public ViewPropertyAnimator rotationY(float var1) {
      this.animateProperty(64, var1);
      return this;
   }

   public ViewPropertyAnimator rotationYBy(float var1) {
      this.animatePropertyBy(64, var1);
      return this;
   }

   public ViewPropertyAnimator scaleX(float var1) {
      this.animateProperty(4, var1);
      return this;
   }

   public ViewPropertyAnimator scaleXBy(float var1) {
      this.animatePropertyBy(4, var1);
      return this;
   }

   public ViewPropertyAnimator scaleY(float var1) {
      this.animateProperty(8, var1);
      return this;
   }

   public ViewPropertyAnimator scaleYBy(float var1) {
      this.animatePropertyBy(8, var1);
      return this;
   }

   public ViewPropertyAnimator setDuration(long var1) {
      if (var1 >= 0L) {
         this.mDurationSet = true;
         this.mDuration = var1;
         return this;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Animators cannot have negative duration: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public ViewPropertyAnimator setInterpolator(Interpolator var1) {
      this.mInterpolatorSet = true;
      this.mInterpolator = var1;
      return this;
   }

   public ViewPropertyAnimator setListener(Animator.AnimatorListener var1) {
      this.mListener = var1;
      return this;
   }

   public ViewPropertyAnimator setStartDelay(long var1) {
      if (var1 >= 0L) {
         this.mStartDelaySet = true;
         this.mStartDelay = var1;
         return this;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Animators cannot have negative duration: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void start() {
      this.startAnimation();
   }

   public ViewPropertyAnimator translationX(float var1) {
      this.animateProperty(1, var1);
      return this;
   }

   public ViewPropertyAnimator translationXBy(float var1) {
      this.animatePropertyBy(1, var1);
      return this;
   }

   public ViewPropertyAnimator translationY(float var1) {
      this.animateProperty(2, var1);
      return this;
   }

   public ViewPropertyAnimator translationYBy(float var1) {
      this.animatePropertyBy(2, var1);
      return this;
   }

   // $FF: renamed from: x (float) com.nineoldandroids.view.ViewPropertyAnimator
   public ViewPropertyAnimator method_21(float var1) {
      this.animateProperty(128, var1);
      return this;
   }

   public ViewPropertyAnimator xBy(float var1) {
      this.animatePropertyBy(128, var1);
      return this;
   }

   // $FF: renamed from: y (float) com.nineoldandroids.view.ViewPropertyAnimator
   public ViewPropertyAnimator method_22(float var1) {
      this.animateProperty(256, var1);
      return this;
   }

   public ViewPropertyAnimator yBy(float var1) {
      this.animatePropertyBy(256, var1);
      return this;
   }

   private class AnimatorEventListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
      private AnimatorEventListener() {
      }

      // $FF: synthetic method
      AnimatorEventListener(Object var2) {
         this();
      }

      public void onAnimationCancel(Animator var1) {
         if (ViewPropertyAnimatorHC.this.mListener != null) {
            ViewPropertyAnimatorHC.this.mListener.onAnimationCancel(var1);
         }

      }

      public void onAnimationEnd(Animator var1) {
         if (ViewPropertyAnimatorHC.this.mListener != null) {
            ViewPropertyAnimatorHC.this.mListener.onAnimationEnd(var1);
         }

         ViewPropertyAnimatorHC.this.mAnimatorMap.remove(var1);
         if (ViewPropertyAnimatorHC.this.mAnimatorMap.isEmpty()) {
            ViewPropertyAnimatorHC.this.mListener = null;
         }

      }

      public void onAnimationRepeat(Animator var1) {
         if (ViewPropertyAnimatorHC.this.mListener != null) {
            ViewPropertyAnimatorHC.this.mListener.onAnimationRepeat(var1);
         }

      }

      public void onAnimationStart(Animator var1) {
         if (ViewPropertyAnimatorHC.this.mListener != null) {
            ViewPropertyAnimatorHC.this.mListener.onAnimationStart(var1);
         }

      }

      public void onAnimationUpdate(ValueAnimator var1) {
         float var2 = var1.getAnimatedFraction();
         ViewPropertyAnimatorHC.PropertyBundle var8 = (ViewPropertyAnimatorHC.PropertyBundle)ViewPropertyAnimatorHC.this.mAnimatorMap.get(var1);
         if ((var8.mPropertyMask & 511) != 0) {
            View var7 = (View)ViewPropertyAnimatorHC.this.mView.get();
            if (var7 != null) {
               var7.invalidate();
            }
         }

         ArrayList var9 = var8.mNameValuesHolder;
         if (var9 != null) {
            int var6 = var9.size();

            for(int var5 = 0; var5 < var6; ++var5) {
               ViewPropertyAnimatorHC.NameValuesHolder var11 = (ViewPropertyAnimatorHC.NameValuesHolder)var9.get(var5);
               float var3 = var11.mFromValue;
               float var4 = var11.mDeltaValue;
               ViewPropertyAnimatorHC.this.setValue(var11.mNameConstant, var3 + var4 * var2);
            }
         }

         View var10 = (View)ViewPropertyAnimatorHC.this.mView.get();
         if (var10 != null) {
            var10.invalidate();
         }

      }
   }

   private static class NameValuesHolder {
      float mDeltaValue;
      float mFromValue;
      int mNameConstant;

      NameValuesHolder(int var1, float var2, float var3) {
         this.mNameConstant = var1;
         this.mFromValue = var2;
         this.mDeltaValue = var3;
      }
   }

   private static class PropertyBundle {
      ArrayList mNameValuesHolder;
      int mPropertyMask;

      PropertyBundle(int var1, ArrayList var2) {
         this.mPropertyMask = var1;
         this.mNameValuesHolder = var2;
      }

      boolean cancel(int var1) {
         if ((this.mPropertyMask & var1) != 0) {
            ArrayList var4 = this.mNameValuesHolder;
            if (var4 != null) {
               int var3 = var4.size();

               for(int var2 = 0; var2 < var3; ++var2) {
                  if (((ViewPropertyAnimatorHC.NameValuesHolder)this.mNameValuesHolder.get(var2)).mNameConstant == var1) {
                     this.mNameValuesHolder.remove(var2);
                     this.mPropertyMask &= var1;
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }
}
