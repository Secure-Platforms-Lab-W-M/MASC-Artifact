package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;
import androidx.transition.R.id;

class TranslationAnimationCreator {
   private TranslationAnimationCreator() {
   }

   static Animator createAnimation(View var0, TransitionValues var1, int var2, int var3, float var4, float var5, float var6, float var7, TimeInterpolator var8, Transition var9) {
      float var10 = var0.getTranslationX();
      float var11 = var0.getTranslationY();
      int[] var14 = (int[])((int[])var1.view.getTag(id.transition_position));
      if (var14 != null) {
         var4 = (float)(var14[0] - var2);
         var5 = (float)(var14[1] - var3);
         var4 += var10;
         var5 += var11;
      }

      int var12 = Math.round(var4 - var10);
      int var13 = Math.round(var5 - var11);
      var0.setTranslationX(var4);
      var0.setTranslationY(var5);
      if (var4 == var6 && var5 == var7) {
         return null;
      } else {
         ObjectAnimator var16 = ObjectAnimator.ofPropertyValuesHolder(var0, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{var4, var6}), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{var5, var7})});
         TranslationAnimationCreator.TransitionPositionListener var15 = new TranslationAnimationCreator.TransitionPositionListener(var0, var1.view, var2 + var12, var3 + var13, var10, var11);
         var9.addListener(var15);
         var16.addListener(var15);
         AnimatorUtils.addPauseListener(var16, var15);
         var16.setInterpolator(var8);
         return var16;
      }
   }

   private static class TransitionPositionListener extends AnimatorListenerAdapter implements Transition.TransitionListener {
      private final View mMovingView;
      private float mPausedX;
      private float mPausedY;
      private final int mStartX;
      private final int mStartY;
      private final float mTerminalX;
      private final float mTerminalY;
      private int[] mTransitionPosition;
      private final View mViewInHierarchy;

      TransitionPositionListener(View var1, View var2, int var3, int var4, float var5, float var6) {
         this.mMovingView = var1;
         this.mViewInHierarchy = var2;
         this.mStartX = var3 - Math.round(var1.getTranslationX());
         this.mStartY = var4 - Math.round(this.mMovingView.getTranslationY());
         this.mTerminalX = var5;
         this.mTerminalY = var6;
         int[] var7 = (int[])((int[])this.mViewInHierarchy.getTag(id.transition_position));
         this.mTransitionPosition = var7;
         if (var7 != null) {
            this.mViewInHierarchy.setTag(id.transition_position, (Object)null);
         }

      }

      public void onAnimationCancel(Animator var1) {
         if (this.mTransitionPosition == null) {
            this.mTransitionPosition = new int[2];
         }

         this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
         this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
         this.mViewInHierarchy.setTag(id.transition_position, this.mTransitionPosition);
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

      public void onTransitionCancel(Transition var1) {
      }

      public void onTransitionEnd(Transition var1) {
         this.mMovingView.setTranslationX(this.mTerminalX);
         this.mMovingView.setTranslationY(this.mTerminalY);
         var1.removeListener(this);
      }

      public void onTransitionPause(Transition var1) {
      }

      public void onTransitionResume(Transition var1) {
      }

      public void onTransitionStart(Transition var1) {
      }
   }
}
