package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;

class TranslationAnimationCreator {
   static Animator createAnimation(View var0, TransitionValues var1, int var2, int var3, float var4, float var5, float var6, float var7, TimeInterpolator var8) {
      float var9 = var0.getTranslationX();
      float var10 = var0.getTranslationY();
      int[] var13 = (int[])var1.view.getTag(R$id.transition_position);
      if (var13 != null) {
         var4 = (float)(var13[0] - var2);
         var5 = (float)(var13[1] - var3);
         var4 += var9;
         var5 += var10;
      }

      int var11 = Math.round(var4 - var9);
      int var12 = Math.round(var5 - var10);
      var0.setTranslationX(var4);
      var0.setTranslationY(var5);
      if (var4 == var6 && var5 == var7) {
         return null;
      } else {
         ObjectAnimator var15 = ObjectAnimator.ofPropertyValuesHolder(var0, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{var4, var6}), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{var5, var7})});
         TranslationAnimationCreator.TransitionPositionListener var14 = new TranslationAnimationCreator.TransitionPositionListener(var0, var1.view, var2 + var11, var3 + var12, var9, var10);
         var15.addListener(var14);
         AnimatorUtils.addPauseListener(var15, var14);
         var15.setInterpolator(var8);
         return var15;
      }
   }

   private static class TransitionPositionListener extends AnimatorListenerAdapter {
      private final View mMovingView;
      private float mPausedX;
      private float mPausedY;
      private final int mStartX;
      private final int mStartY;
      private final float mTerminalX;
      private final float mTerminalY;
      private int[] mTransitionPosition;
      private final View mViewInHierarchy;

      private TransitionPositionListener(View var1, View var2, int var3, int var4, float var5, float var6) {
         this.mMovingView = var1;
         this.mViewInHierarchy = var2;
         this.mStartX = var3 - Math.round(this.mMovingView.getTranslationX());
         this.mStartY = var4 - Math.round(this.mMovingView.getTranslationY());
         this.mTerminalX = var5;
         this.mTerminalY = var6;
         this.mTransitionPosition = (int[])this.mViewInHierarchy.getTag(R$id.transition_position);
         if (this.mTransitionPosition != null) {
            this.mViewInHierarchy.setTag(R$id.transition_position, (Object)null);
         }
      }

      // $FF: synthetic method
      TransitionPositionListener(View var1, View var2, int var3, int var4, float var5, float var6, Object var7) {
         this(var1, var2, var3, var4, var5, var6);
      }

      public void onAnimationCancel(Animator var1) {
         if (this.mTransitionPosition == null) {
            this.mTransitionPosition = new int[2];
         }

         this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
         this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
         this.mViewInHierarchy.setTag(R$id.transition_position, this.mTransitionPosition);
      }

      public void onAnimationEnd(Animator var1) {
         this.mMovingView.setTranslationX(this.mTerminalX);
         this.mMovingView.setTranslationY(this.mTerminalY);
      }

      public void onAnimationPause(Animator var1) {
         this.mPausedX = this.mMovingView.getTranslationX();
         this.mPausedY = this.mMovingView.getTranslationY();
         this.mMovingView.setTranslationX(this.mTerminalX);
         this.mMovingView.setTranslationY(this.mTerminalY);
      }

      public void onAnimationResume(Animator var1) {
         this.mMovingView.setTranslationX(this.mPausedX);
         this.mMovingView.setTranslationY(this.mPausedY);
      }
   }
}
