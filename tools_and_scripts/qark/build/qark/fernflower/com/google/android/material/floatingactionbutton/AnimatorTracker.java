package com.google.android.material.floatingactionbutton;

import android.animation.Animator;

class AnimatorTracker {
   private Animator currentAnimator;

   public void cancelCurrent() {
      Animator var1 = this.currentAnimator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public void clear() {
      this.currentAnimator = null;
   }

   public void onNextAnimationStart(Animator var1) {
      this.cancelCurrent();
      this.currentAnimator = var1;
   }
}
