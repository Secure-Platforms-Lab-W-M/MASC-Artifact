package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class ExpandableTransformationBehavior extends ExpandableBehavior {
   private AnimatorSet currentAnimation;

   public ExpandableTransformationBehavior() {
   }

   public ExpandableTransformationBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   protected abstract AnimatorSet onCreateExpandedStateChangeAnimation(View var1, View var2, boolean var3, boolean var4);

   protected boolean onExpandedStateChange(View var1, View var2, boolean var3, boolean var4) {
      boolean var5;
      if (this.currentAnimation != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         this.currentAnimation.cancel();
      }

      AnimatorSet var6 = this.onCreateExpandedStateChangeAnimation(var1, var2, var3, var5);
      this.currentAnimation = var6;
      var6.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            ExpandableTransformationBehavior.this.currentAnimation = null;
         }
      });
      this.currentAnimation.start();
      if (!var4) {
         this.currentAnimation.end();
      }

      return true;
   }
}
