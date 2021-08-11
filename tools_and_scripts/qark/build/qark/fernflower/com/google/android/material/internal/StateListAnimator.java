package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
   private final AnimatorListener animationListener = new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator var1) {
         if (StateListAnimator.this.runningAnimator == var1) {
            StateListAnimator.this.runningAnimator = null;
         }

      }
   };
   private StateListAnimator.Tuple lastMatch = null;
   ValueAnimator runningAnimator = null;
   private final ArrayList tuples = new ArrayList();

   private void cancel() {
      ValueAnimator var1 = this.runningAnimator;
      if (var1 != null) {
         var1.cancel();
         this.runningAnimator = null;
      }

   }

   private void start(StateListAnimator.Tuple var1) {
      ValueAnimator var2 = var1.animator;
      this.runningAnimator = var2;
      var2.start();
   }

   public void addState(int[] var1, ValueAnimator var2) {
      StateListAnimator.Tuple var3 = new StateListAnimator.Tuple(var1, var2);
      var2.addListener(this.animationListener);
      this.tuples.add(var3);
   }

   public void jumpToCurrentState() {
      ValueAnimator var1 = this.runningAnimator;
      if (var1 != null) {
         var1.end();
         this.runningAnimator = null;
      }

   }

   public void setState(int[] var1) {
      Object var5 = null;
      int var3 = this.tuples.size();
      int var2 = 0;

      StateListAnimator.Tuple var4;
      while(true) {
         var4 = (StateListAnimator.Tuple)var5;
         if (var2 >= var3) {
            break;
         }

         var4 = (StateListAnimator.Tuple)this.tuples.get(var2);
         if (StateSet.stateSetMatches(var4.specs, var1)) {
            break;
         }

         ++var2;
      }

      StateListAnimator.Tuple var6 = this.lastMatch;
      if (var4 != var6) {
         if (var6 != null) {
            this.cancel();
         }

         this.lastMatch = var4;
         if (var4 != null) {
            this.start(var4);
         }

      }
   }

   static class Tuple {
      final ValueAnimator animator;
      final int[] specs;

      Tuple(int[] var1, ValueAnimator var2) {
         this.specs = var1;
         this.animator = var2;
      }
   }
}
