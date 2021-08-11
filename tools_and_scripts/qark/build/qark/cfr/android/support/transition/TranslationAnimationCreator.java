/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.util.Property
 *  android.view.View
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.support.transition.AnimatorUtils;
import android.support.transition.R;
import android.support.transition.TransitionValues;
import android.util.Property;
import android.view.View;

class TranslationAnimationCreator {
    TranslationAnimationCreator() {
    }

    static Animator createAnimation(View object, TransitionValues transitionValues, int n, int n2, float f, float f2, float f3, float f4, TimeInterpolator timeInterpolator) {
        float f5 = object.getTranslationX();
        float f6 = object.getTranslationY();
        ObjectAnimator objectAnimator = (ObjectAnimator)transitionValues.view.getTag(R.id.transition_position);
        if (objectAnimator != null) {
            f = objectAnimator[0] - n;
            f2 = objectAnimator[1] - n2;
            f += f5;
            f2 += f6;
        }
        int n3 = Math.round(f - f5);
        int n4 = Math.round(f2 - f6);
        object.setTranslationX(f);
        object.setTranslationY(f2);
        if (f == f3 && f2 == f4) {
            return null;
        }
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder((Object)object, (PropertyValuesHolder[])new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat((Property)View.TRANSLATION_X, (float[])new float[]{f, f3}), PropertyValuesHolder.ofFloat((Property)View.TRANSLATION_Y, (float[])new float[]{f2, f4})});
        object = new TransitionPositionListener((View)object, transitionValues.view, n + n3, n2 + n4, f5, f6);
        objectAnimator.addListener((Animator.AnimatorListener)object);
        AnimatorUtils.addPauseListener((Animator)objectAnimator, (AnimatorListenerAdapter)object);
        objectAnimator.setInterpolator(timeInterpolator);
        return objectAnimator;
    }

    private static class TransitionPositionListener
    extends AnimatorListenerAdapter {
        private final View mMovingView;
        private float mPausedX;
        private float mPausedY;
        private final int mStartX;
        private final int mStartY;
        private final float mTerminalX;
        private final float mTerminalY;
        private int[] mTransitionPosition;
        private final View mViewInHierarchy;

        private TransitionPositionListener(View view, View view2, int n, int n2, float f, float f2) {
            this.mMovingView = view;
            this.mViewInHierarchy = view2;
            this.mStartX = n - Math.round(this.mMovingView.getTranslationX());
            this.mStartY = n2 - Math.round(this.mMovingView.getTranslationY());
            this.mTerminalX = f;
            this.mTerminalY = f2;
            this.mTransitionPosition = (int[])this.mViewInHierarchy.getTag(R.id.transition_position);
            if (this.mTransitionPosition != null) {
                this.mViewInHierarchy.setTag(R.id.transition_position, (Object)null);
                return;
            }
        }

        public void onAnimationCancel(Animator animator2) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(R.id.transition_position, (Object)this.mTransitionPosition);
        }

        public void onAnimationEnd(Animator animator2) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationPause(Animator animator2) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationResume(Animator animator2) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }
    }

}

