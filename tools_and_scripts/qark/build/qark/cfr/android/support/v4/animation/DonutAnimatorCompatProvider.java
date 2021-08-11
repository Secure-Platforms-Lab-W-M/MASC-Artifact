/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.animation;

import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorProvider;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.view.View;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.List;

class DonutAnimatorCompatProvider
implements AnimatorProvider {
    DonutAnimatorCompatProvider() {
    }

    @Override
    public void clearInterpolator(View view) {
    }

    @Override
    public ValueAnimatorCompat emptyValueAnimator() {
        return new DonutFloatValueAnimator();
    }

    private static class DonutFloatValueAnimator
    implements ValueAnimatorCompat {
        private long mDuration = 200L;
        private boolean mEnded = false;
        private float mFraction = 0.0f;
        List<AnimatorListenerCompat> mListeners = new ArrayList<AnimatorListenerCompat>();
        private Runnable mLoopRunnable;
        private long mStartTime;
        private boolean mStarted = false;
        View mTarget;
        List<AnimatorUpdateListenerCompat> mUpdateListeners = new ArrayList<AnimatorUpdateListenerCompat>();

        public DonutFloatValueAnimator() {
            this.mLoopRunnable = new Runnable(){

                @Override
                public void run() {
                    float f = (float)(DonutFloatValueAnimator.this.getTime() - DonutFloatValueAnimator.this.mStartTime) * 1.0f / (float)DonutFloatValueAnimator.this.mDuration;
                    if (f > 1.0f || DonutFloatValueAnimator.this.mTarget.getParent() == null) {
                        f = 1.0f;
                    }
                    DonutFloatValueAnimator.this.mFraction = f;
                    DonutFloatValueAnimator.this.notifyUpdateListeners();
                    if (DonutFloatValueAnimator.this.mFraction >= 1.0f) {
                        DonutFloatValueAnimator.this.dispatchEnd();
                        return;
                    }
                    DonutFloatValueAnimator.this.mTarget.postDelayed(DonutFloatValueAnimator.this.mLoopRunnable, 16L);
                }
            };
        }

        private void dispatchCancel() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onAnimationCancel(this);
            }
        }

        private void dispatchEnd() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onAnimationEnd(this);
            }
        }

        private void dispatchStart() {
            for (int i = this.mListeners.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onAnimationStart(this);
            }
        }

        private long getTime() {
            return this.mTarget.getDrawingTime();
        }

        private void notifyUpdateListeners() {
            for (int i = this.mUpdateListeners.size() - 1; i >= 0; --i) {
                this.mUpdateListeners.get(i).onAnimationUpdate(this);
            }
        }

        @Override
        public void addListener(AnimatorListenerCompat animatorListenerCompat) {
            this.mListeners.add(animatorListenerCompat);
        }

        @Override
        public void addUpdateListener(AnimatorUpdateListenerCompat animatorUpdateListenerCompat) {
            this.mUpdateListeners.add(animatorUpdateListenerCompat);
        }

        @Override
        public void cancel() {
            if (this.mEnded) {
                return;
            }
            this.mEnded = true;
            if (this.mStarted) {
                this.dispatchCancel();
            }
            this.dispatchEnd();
        }

        @Override
        public float getAnimatedFraction() {
            return this.mFraction;
        }

        @Override
        public void setDuration(long l) {
            if (!this.mStarted) {
                this.mDuration = l;
            }
        }

        @Override
        public void setTarget(View view) {
            this.mTarget = view;
        }

        @Override
        public void start() {
            if (this.mStarted) {
                return;
            }
            this.mStarted = true;
            this.dispatchStart();
            this.mFraction = 0.0f;
            this.mStartTime = this.getTime();
            this.mTarget.postDelayed(this.mLoopRunnable, 16L);
        }

    }

}

