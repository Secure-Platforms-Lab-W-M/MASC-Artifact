/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.os.SystemClock
 *  android.util.DisplayMetrics
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 */
package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

public abstract class AutoScrollHelper
implements View.OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMinimumVelocity = new float[]{0.0f, 0.0f};
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private float[] mRelativeEdges = new float[]{0.0f, 0.0f};
    private float[] mRelativeVelocity = new float[]{0.0f, 0.0f};
    private Runnable mRunnable;
    final ClampedScroller mScroller = new ClampedScroller();
    final View mTarget;

    public AutoScrollHelper(View view) {
        this.mTarget = view;
        view = Resources.getSystem().getDisplayMetrics();
        int n = (int)(view.density * 1575.0f + 0.5f);
        int n2 = (int)(view.density * 315.0f + 0.5f);
        this.setMaximumVelocity(n, n);
        this.setMinimumVelocity(n2, n2);
        this.setEdgeType(1);
        this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        this.setRelativeEdges(0.2f, 0.2f);
        this.setRelativeVelocity(1.0f, 1.0f);
        this.setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        this.setRampUpDuration(500);
        this.setRampDownDuration(500);
    }

    private float computeTargetVelocity(int n, float f, float f2, float f3) {
        if ((f = this.getEdgeValue(this.mRelativeEdges[n], f2, this.mMaximumEdges[n], f)) == 0.0f) {
            return 0.0f;
        }
        float f4 = this.mRelativeVelocity[n];
        f2 = this.mMinimumVelocity[n];
        float f5 = this.mMaximumVelocity[n];
        f3 = f4 * f3;
        if (f > 0.0f) {
            return AutoScrollHelper.constrain(f * f3, f2, f5);
        }
        return - AutoScrollHelper.constrain((- f) * f3, f2, f5);
    }

    static float constrain(float f, float f2, float f3) {
        if (f > f3) {
            return f3;
        }
        if (f < f2) {
            return f2;
        }
        return f;
    }

    static int constrain(int n, int n2, int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    private float constrainEdgeValue(float f, float f2) {
        if (f2 == 0.0f) {
            return 0.0f;
        }
        int n = this.mEdgeType;
        if (n != 0 && n != 1) {
            if (n != 2) {
                return 0.0f;
            }
            if (f < 0.0f) {
                return f / (- f2);
            }
        } else if (f < f2) {
            if (f >= 0.0f) {
                return 1.0f - f / f2;
            }
            if (this.mAnimating && this.mEdgeType == 1) {
                return 1.0f;
            }
        }
        return 0.0f;
    }

    private float getEdgeValue(float f, float f2, float f3, float f4) {
        block4 : {
            block3 : {
                block2 : {
                    f = AutoScrollHelper.constrain(f * f2, 0.0f, f3);
                    f3 = this.constrainEdgeValue(f4, f);
                    if ((f = this.constrainEdgeValue(f2 - f4, f) - f3) >= 0.0f) break block2;
                    f = - this.mEdgeInterpolator.getInterpolation(- f);
                    break block3;
                }
                if (f <= 0.0f) break block4;
                f = this.mEdgeInterpolator.getInterpolation(f);
            }
            return AutoScrollHelper.constrain(f, -1.0f, 1.0f);
        }
        return 0.0f;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
            return;
        }
        this.mScroller.requestStop();
    }

    private void startAnimating() {
        int n;
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (!this.mAlreadyDelayed && (n = this.mActivationDelay) > 0) {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, n);
        } else {
            this.mRunnable.run();
        }
        this.mAlreadyDelayed = true;
    }

    public abstract boolean canTargetScrollHorizontally(int var1);

    public abstract boolean canTargetScrollVertically(int var1);

    void cancelTargetTouch() {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
        this.mTarget.onTouchEvent(motionEvent);
        motionEvent.recycle();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bl;
        boolean bl2;
        block9 : {
            block8 : {
                block6 : {
                    block7 : {
                        bl2 = this.mEnabled;
                        bl = false;
                        if (!bl2) {
                            return false;
                        }
                        int n = motionEvent.getActionMasked();
                        if (n == 0) break block6;
                        if (n == 1) break block7;
                        if (n == 2) break block8;
                        if (n != 3) break block9;
                    }
                    this.requestStop();
                    break block9;
                }
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
            }
            float f = this.computeTargetVelocity(0, motionEvent.getX(), view.getWidth(), this.mTarget.getWidth());
            float f2 = this.computeTargetVelocity(1, motionEvent.getY(), view.getHeight(), this.mTarget.getHeight());
            this.mScroller.setTargetVelocity(f, f2);
            if (!this.mAnimating && this.shouldAnimate()) {
                this.startAnimating();
            }
        }
        bl2 = bl;
        if (this.mExclusive) {
            bl2 = bl;
            if (this.mAnimating) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public abstract void scrollTargetBy(int var1, int var2);

    public AutoScrollHelper setActivationDelay(int n) {
        this.mActivationDelay = n;
        return this;
    }

    public AutoScrollHelper setEdgeType(int n) {
        this.mEdgeType = n;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean bl) {
        if (this.mEnabled && !bl) {
            this.requestStop();
        }
        this.mEnabled = bl;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean bl) {
        this.mExclusive = bl;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f2) {
        float[] arrf = this.mMaximumEdges;
        arrf[0] = f;
        arrf[1] = f2;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f2) {
        float[] arrf = this.mMaximumVelocity;
        arrf[0] = f / 1000.0f;
        arrf[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f2) {
        float[] arrf = this.mMinimumVelocity;
        arrf[0] = f / 1000.0f;
        arrf[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int n) {
        this.mScroller.setRampDownDuration(n);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int n) {
        this.mScroller.setRampUpDuration(n);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f2) {
        float[] arrf = this.mRelativeEdges;
        arrf[0] = f;
        arrf[1] = f2;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f2) {
        float[] arrf = this.mRelativeVelocity;
        arrf[0] = f / 1000.0f;
        arrf[1] = f2 / 1000.0f;
        return this;
    }

    boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int n = clampedScroller.getVerticalDirection();
        int n2 = clampedScroller.getHorizontalDirection();
        if (n != 0 && this.canTargetScrollVertically(n) || n2 != 0 && this.canTargetScrollHorizontally(n2)) {
            return true;
        }
        return false;
    }

    private static class ClampedScroller {
        private long mDeltaTime = 0L;
        private int mDeltaX = 0;
        private int mDeltaY = 0;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1L;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        ClampedScroller() {
        }

        private float getValueAt(long l) {
            if (l < this.mStartTime) {
                return 0.0f;
            }
            long l2 = this.mStopTime;
            if (l2 >= 0L && l >= l2) {
                float f = this.mStopValue;
                return 1.0f - f + f * AutoScrollHelper.constrain((float)(l - l2) / (float)this.mEffectiveRampDown, 0.0f, 1.0f);
            }
            return AutoScrollHelper.constrain((float)(l - this.mStartTime) / (float)this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
        }

        private float interpolateValue(float f) {
            return -4.0f * f * f + 4.0f * f;
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime != 0L) {
                long l = AnimationUtils.currentAnimationTimeMillis();
                float f = this.interpolateValue(this.getValueAt(l));
                long l2 = l - this.mDeltaTime;
                this.mDeltaTime = l;
                this.mDeltaX = (int)((float)l2 * f * this.mTargetVelocityX);
                this.mDeltaY = (int)((float)l2 * f * this.mTargetVelocityY);
                return;
            }
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }

        public int getHorizontalDirection() {
            float f = this.mTargetVelocityX;
            return (int)(f / Math.abs(f));
        }

        public int getVerticalDirection() {
            float f = this.mTargetVelocityY;
            return (int)(f / Math.abs(f));
        }

        public boolean isFinished() {
            if (this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + (long)this.mEffectiveRampDown) {
                return true;
            }
            return false;
        }

        public void requestStop() {
            long l = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(l - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = this.getValueAt(l);
            this.mStopTime = l;
        }

        public void setRampDownDuration(int n) {
            this.mRampDownDuration = n;
        }

        public void setRampUpDuration(int n) {
            this.mRampUpDuration = n;
        }

        public void setTargetVelocity(float f, float f2) {
            this.mTargetVelocityX = f;
            this.mTargetVelocityY = f2;
        }

        public void start() {
            long l;
            this.mStartTime = l = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1L;
            this.mDeltaTime = l;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    private class ScrollAnimationRunnable
    implements Runnable {
        ScrollAnimationRunnable() {
        }

        @Override
        public void run() {
            ClampedScroller clampedScroller;
            if (!AutoScrollHelper.this.mAnimating) {
                return;
            }
            if (AutoScrollHelper.this.mNeedsReset) {
                AutoScrollHelper.this.mNeedsReset = false;
                AutoScrollHelper.this.mScroller.start();
            }
            if (!(clampedScroller = AutoScrollHelper.this.mScroller).isFinished() && AutoScrollHelper.this.shouldAnimate()) {
                if (AutoScrollHelper.this.mNeedsCancel) {
                    AutoScrollHelper.this.mNeedsCancel = false;
                    AutoScrollHelper.this.cancelTargetTouch();
                }
                clampedScroller.computeScrollDelta();
                int n = clampedScroller.getDeltaX();
                int n2 = clampedScroller.getDeltaY();
                AutoScrollHelper.this.scrollTargetBy(n, n2);
                ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
                return;
            }
            AutoScrollHelper.this.mAnimating = false;
        }
    }

}

