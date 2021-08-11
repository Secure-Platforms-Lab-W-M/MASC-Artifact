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
 *  androidx.transition.R
 *  androidx.transition.R$id
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.view.View;
import androidx.transition.AnimatorUtils;
import androidx.transition.R;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

class TranslationAnimationCreator {
    private TranslationAnimationCreator() {
    }

    static Animator createAnimation(View object, TransitionValues transitionValues, int n, int n2, float f, float f2, float f3, float f4, TimeInterpolator timeInterpolator, Transition transition) {
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
        transition.addListener((Transition.TransitionListener)object);
        objectAnimator.addListener((Animator.AnimatorListener)object);
        AnimatorUtils.addPauseListener((Animator)objectAnimator, (AnimatorListenerAdapter)object);
        objectAnimator.setInterpolator(timeInterpolator);
        return objectAnimator;
    }

    private static class TransitionPositionListener
    extends AnimatorListenerAdapter
    implements Transition.TransitionListener {
        private final View mMovingView;
        private float mPausedX;
        private float mPausedY;
        private final int mStartX;
        private final int mStartY;
        private final float mTerminalX;
        private final float mTerminalY;
        private int[] mTransitionPosition;
        private final View mViewInHierarchy;

        TransitionPositionListener(View arrn, View view, int n, int n2, float f, float f2) {
            this.mMovingView = arrn;
            this.mViewInHierarchy = view;
            this.mStartX = n - Math.round(arrn.getTranslationX());
            this.mStartY = n2 - Math.round(this.mMovingView.getTranslationY());
            this.mTerminalX = f;
            this.mTerminalY = f2;
            arrn = (int[])this.mViewInHierarchy.getTag(R.id.transition_position);
            this.mTransitionPosition = arrn;
            if (arrn != null) {
                this.mViewInHierarchy.setTag(R.id.transition_position, (Object)null);
            }
        }

        public void onAnimationCancel(Animator animator) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(R.id.transition_position, (Object)this.mTransitionPosition);
        }

        public void onAnimationPause(Animator animator) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationResume(Animator animator) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
            transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }

        @Override
        public void onTransitionStart(Transition transition) {
        }
    }

}

