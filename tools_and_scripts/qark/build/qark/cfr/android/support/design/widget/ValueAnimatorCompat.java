/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.support.annotation.NonNull;
import android.view.animation.Interpolator;

class ValueAnimatorCompat {
    private final Impl mImpl;

    ValueAnimatorCompat(Impl impl) {
        this.mImpl = impl;
    }

    public void addListener(final AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.mImpl.addListener(new Impl.AnimatorListenerProxy(){

                @Override
                public void onAnimationCancel() {
                    animatorListener.onAnimationCancel(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationEnd() {
                    animatorListener.onAnimationEnd(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationStart() {
                    animatorListener.onAnimationStart(ValueAnimatorCompat.this);
                }
            });
            return;
        }
        this.mImpl.addListener(null);
    }

    public void addUpdateListener(final AnimatorUpdateListener animatorUpdateListener) {
        if (animatorUpdateListener != null) {
            this.mImpl.addUpdateListener(new Impl.AnimatorUpdateListenerProxy(){

                @Override
                public void onAnimationUpdate() {
                    animatorUpdateListener.onAnimationUpdate(ValueAnimatorCompat.this);
                }
            });
            return;
        }
        this.mImpl.addUpdateListener(null);
    }

    public void cancel() {
        this.mImpl.cancel();
    }

    public void end() {
        this.mImpl.end();
    }

    public float getAnimatedFloatValue() {
        return this.mImpl.getAnimatedFloatValue();
    }

    public float getAnimatedFraction() {
        return this.mImpl.getAnimatedFraction();
    }

    public int getAnimatedIntValue() {
        return this.mImpl.getAnimatedIntValue();
    }

    public long getDuration() {
        return this.mImpl.getDuration();
    }

    public boolean isRunning() {
        return this.mImpl.isRunning();
    }

    public void setDuration(long l) {
        this.mImpl.setDuration(l);
    }

    public void setFloatValues(float f, float f2) {
        this.mImpl.setFloatValues(f, f2);
    }

    public void setIntValues(int n, int n2) {
        this.mImpl.setIntValues(n, n2);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mImpl.setInterpolator(interpolator);
    }

    public void start() {
        this.mImpl.start();
    }

    static interface AnimatorListener {
        public void onAnimationCancel(ValueAnimatorCompat var1);

        public void onAnimationEnd(ValueAnimatorCompat var1);

        public void onAnimationStart(ValueAnimatorCompat var1);
    }

    static class AnimatorListenerAdapter
    implements AnimatorListener {
        AnimatorListenerAdapter() {
        }

        @Override
        public void onAnimationCancel(ValueAnimatorCompat valueAnimatorCompat) {
        }

        @Override
        public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
        }

        @Override
        public void onAnimationStart(ValueAnimatorCompat valueAnimatorCompat) {
        }
    }

    static interface AnimatorUpdateListener {
        public void onAnimationUpdate(ValueAnimatorCompat var1);
    }

    static interface Creator {
        @NonNull
        public ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        Impl() {
        }

        abstract void addListener(AnimatorListenerProxy var1);

        abstract void addUpdateListener(AnimatorUpdateListenerProxy var1);

        abstract void cancel();

        abstract void end();

        abstract float getAnimatedFloatValue();

        abstract float getAnimatedFraction();

        abstract int getAnimatedIntValue();

        abstract long getDuration();

        abstract boolean isRunning();

        abstract void setDuration(long var1);

        abstract void setFloatValues(float var1, float var2);

        abstract void setIntValues(int var1, int var2);

        abstract void setInterpolator(Interpolator var1);

        abstract void start();

        static interface AnimatorListenerProxy {
            public void onAnimationCancel();

            public void onAnimationEnd();

            public void onAnimationStart();
        }

        static interface AnimatorUpdateListenerProxy {
            public void onAnimationUpdate();
        }

    }

}

