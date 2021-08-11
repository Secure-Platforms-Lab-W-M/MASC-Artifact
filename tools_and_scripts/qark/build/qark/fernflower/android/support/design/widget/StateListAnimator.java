package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.util.StateSet;
import java.util.ArrayList;

final class StateListAnimator {
   private final AnimatorListener mAnimationListener = new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator var1) {
         if (StateListAnimator.this.mRunningAnimator == var1) {
            StateListAnimator.this.mRunningAnimator = null;
         }
      }
   };
   private StateListAnimator.Tuple mLastMatch = null;
   ValueAnimator mRunningAnimator = null;
   private final ArrayList mTuples = new ArrayList();

   private void cancel() {
      ValueAnimator var1 = this.mRunningAnimator;
      if (var1 != null) {
         var1.cancel();
         this.mRunningAnimator = null;
      }
   }

   private void start(StateListAnimator.Tuple var1) {
      this.mRunningAnimator = var1.mAnimator;
      this.mRunningAnimator.start();
   }

   public void addState(int[] var1, ValueAnimator var2) {
      StateListAnimator.Tuple var3 = new StateListAnimator.Tuple(var1, var2);
      var2.addListener(this.mAnimationListener);
      this.mTuples.add(var3);
   }

   public void jumpToCurrentState() {
      ValueAnimator var1 = this.mRunningAnimator;
      if (var1 != null) {
         var1.end();
         this.mRunningAnimator = null;
      }
   }

   void setState(int[] var1) {
      Object var5 = null;
      int var3 = this.mTuples.size();
      int var2 = 0;

      StateListAnimator.Tuple var4;
      while(true) {
         var4 = (StateListAnimator.Tuple)var5;
         if (var2 >= var3) {
            break;
         }

         var4 = (StateListAnimator.Tuple)this.mTuples.get(var2);
         if (StateSet.stateSetMatches(var4.mSpecs, var1)) {
            break;
         }

         ++var2;
      }

      StateListAnimator.Tuple var6 = this.mLastMatch;
      if (var4 != var6) {
         if (var6 != null) {
            this.cancel();
         }

         this.mLastMatch = var4;
         if (var4 != null) {
            this.start(var4);
         }
      }
   }

   static class Tuple {
      final ValueAnimator mAnimator;
      final int[] mSpecs;

      Tuple(int[] var1, ValueAnimator var2) {
         this.mSpecs = var1;
         this.mAnimator = var2;
      }
   }
}
