package com.nineoldandroids.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.util.WeakHashMap;

public abstract class ViewPropertyAnimator {
   private static final WeakHashMap ANIMATORS = new WeakHashMap(0);

   public static ViewPropertyAnimator animate(View var0) {
      ViewPropertyAnimator var3 = (ViewPropertyAnimator)ANIMATORS.get(var0);
      Object var2 = var3;
      if (var3 == null) {
         int var1 = Integer.valueOf(VERSION.SDK);
         if (var1 >= 14) {
            var2 = new ViewPropertyAnimatorICS(var0);
         } else if (var1 >= 11) {
            var2 = new ViewPropertyAnimatorHC(var0);
         } else {
            var2 = new ViewPropertyAnimatorPreHC(var0);
         }

         ANIMATORS.put(var0, var2);
      }

      return (ViewPropertyAnimator)var2;
   }

   public abstract ViewPropertyAnimator alpha(float var1);

   public abstract ViewPropertyAnimator alphaBy(float var1);

   public abstract void cancel();

   public abstract long getDuration();

   public abstract long getStartDelay();

   public abstract ViewPropertyAnimator rotation(float var1);

   public abstract ViewPropertyAnimator rotationBy(float var1);

   public abstract ViewPropertyAnimator rotationX(float var1);

   public abstract ViewPropertyAnimator rotationXBy(float var1);

   public abstract ViewPropertyAnimator rotationY(float var1);

   public abstract ViewPropertyAnimator rotationYBy(float var1);

   public abstract ViewPropertyAnimator scaleX(float var1);

   public abstract ViewPropertyAnimator scaleXBy(float var1);

   public abstract ViewPropertyAnimator scaleY(float var1);

   public abstract ViewPropertyAnimator scaleYBy(float var1);

   public abstract ViewPropertyAnimator setDuration(long var1);

   public abstract ViewPropertyAnimator setInterpolator(Interpolator var1);

   public abstract ViewPropertyAnimator setListener(Animator.AnimatorListener var1);

   public abstract ViewPropertyAnimator setStartDelay(long var1);

   public abstract void start();

   public abstract ViewPropertyAnimator translationX(float var1);

   public abstract ViewPropertyAnimator translationXBy(float var1);

   public abstract ViewPropertyAnimator translationY(float var1);

   public abstract ViewPropertyAnimator translationYBy(float var1);

   // $FF: renamed from: x (float) com.nineoldandroids.view.ViewPropertyAnimator
   public abstract ViewPropertyAnimator method_21(float var1);

   public abstract ViewPropertyAnimator xBy(float var1);

   // $FF: renamed from: y (float) com.nineoldandroids.view.ViewPropertyAnimator
   public abstract ViewPropertyAnimator method_22(float var1);

   public abstract ViewPropertyAnimator yBy(float var1);
}
