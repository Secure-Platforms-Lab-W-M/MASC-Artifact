package com.google.android.material.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.animation.AnimationUtils;

public class HideBottomViewOnScrollBehavior extends CoordinatorLayout.Behavior {
   protected static final int ENTER_ANIMATION_DURATION = 225;
   protected static final int EXIT_ANIMATION_DURATION = 175;
   private static final int STATE_SCROLLED_DOWN = 1;
   private static final int STATE_SCROLLED_UP = 2;
   private int additionalHiddenOffsetY = 0;
   private ViewPropertyAnimator currentAnimator;
   private int currentState = 2;
   private int height = 0;

   public HideBottomViewOnScrollBehavior() {
   }

   public HideBottomViewOnScrollBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void animateChildTo(View var1, int var2, long var3, TimeInterpolator var5) {
      this.currentAnimator = var1.animate().translationY((float)var2).setInterpolator(var5).setDuration(var3).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            HideBottomViewOnScrollBehavior.this.currentAnimator = null;
         }
      });
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      MarginLayoutParams var4 = (MarginLayoutParams)var2.getLayoutParams();
      this.height = var2.getMeasuredHeight() + var4.bottomMargin;
      return super.onLayoutChild(var1, var2, var3);
   }

   public void onNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int var6, int var7) {
      if (var5 > 0) {
         this.slideDown(var2);
      } else {
         if (var5 < 0) {
            this.slideUp(var2);
         }

      }
   }

   public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5) {
      return var5 == 2;
   }

   public void setAdditionalHiddenOffsetY(View var1, int var2) {
      this.additionalHiddenOffsetY = var2;
      if (this.currentState == 1) {
         var1.setTranslationY((float)(this.height + var2));
      }

   }

   public void slideDown(View var1) {
      if (this.currentState != 1) {
         ViewPropertyAnimator var2 = this.currentAnimator;
         if (var2 != null) {
            var2.cancel();
            var1.clearAnimation();
         }

         this.currentState = 1;
         this.animateChildTo(var1, this.height + this.additionalHiddenOffsetY, 175L, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
      }
   }

   public void slideUp(View var1) {
      if (this.currentState != 2) {
         ViewPropertyAnimator var2 = this.currentAnimator;
         if (var2 != null) {
            var2.cancel();
            var1.clearAnimation();
         }

         this.currentState = 2;
         this.animateChildTo(var1, 0, 225L, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
      }
   }
}
