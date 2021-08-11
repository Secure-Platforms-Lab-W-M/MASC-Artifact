package com.google.android.material.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewAnimationUtils;

public final class CircularRevealCompat {
   private CircularRevealCompat() {
   }

   public static Animator createCircularReveal(CircularRevealWidget var0, float var1, float var2, float var3) {
      ObjectAnimator var5 = ObjectAnimator.ofObject(var0, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(var1, var2, var3)});
      if (VERSION.SDK_INT >= 21) {
         CircularRevealWidget.RevealInfo var6 = var0.getRevealInfo();
         if (var6 != null) {
            float var4 = var6.radius;
            Animator var7 = ViewAnimationUtils.createCircularReveal((View)var0, (int)var1, (int)var2, var4, var3);
            AnimatorSet var8 = new AnimatorSet();
            var8.playTogether(new Animator[]{var5, var7});
            return var8;
         } else {
            throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
         }
      } else {
         return var5;
      }
   }

   public static Animator createCircularReveal(CircularRevealWidget var0, float var1, float var2, float var3, float var4) {
      ObjectAnimator var5 = ObjectAnimator.ofObject(var0, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(var1, var2, var3), new CircularRevealWidget.RevealInfo(var1, var2, var4)});
      if (VERSION.SDK_INT >= 21) {
         Animator var7 = ViewAnimationUtils.createCircularReveal((View)var0, (int)var1, (int)var2, var3, var4);
         AnimatorSet var6 = new AnimatorSet();
         var6.playTogether(new Animator[]{var5, var7});
         return var6;
      } else {
         return var5;
      }
   }

   public static AnimatorListener createCircularRevealListener(final CircularRevealWidget var0) {
      return new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            var0.destroyCircularRevealCache();
         }

         public void onAnimationStart(Animator var1) {
            var0.buildCircularRevealCache();
         }
      };
   }
}
