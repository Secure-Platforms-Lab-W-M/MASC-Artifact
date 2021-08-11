/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.view.animation.AccelerateDecelerateInterpolator
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.DecelerateInterpolator
 */
package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.google.android.material.animation.AnimationUtils;

public class MotionTiming {
    private long delay = 0L;
    private long duration = 300L;
    private TimeInterpolator interpolator = null;
    private int repeatCount = 0;
    private int repeatMode = 1;

    public MotionTiming(long l, long l2) {
        this.delay = l;
        this.duration = l2;
    }

    public MotionTiming(long l, long l2, TimeInterpolator timeInterpolator) {
        this.delay = l;
        this.duration = l2;
        this.interpolator = timeInterpolator;
    }

    static MotionTiming createFromAnimator(ValueAnimator valueAnimator) {
        MotionTiming motionTiming = new MotionTiming(valueAnimator.getStartDelay(), valueAnimator.getDuration(), MotionTiming.getInterpolatorCompat(valueAnimator));
        motionTiming.repeatCount = valueAnimator.getRepeatCount();
        motionTiming.repeatMode = valueAnimator.getRepeatMode();
        return motionTiming;
    }

    private static TimeInterpolator getInterpolatorCompat(ValueAnimator valueAnimator) {
        if (!((valueAnimator = valueAnimator.getInterpolator()) instanceof AccelerateDecelerateInterpolator) && valueAnimator != null) {
            if (valueAnimator instanceof AccelerateInterpolator) {
                return AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
            }
            if (valueAnimator instanceof DecelerateInterpolator) {
                return AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
            }
            return valueAnimator;
        }
        return AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
    }

    public void apply(Animator animator) {
        animator.setStartDelay(this.getDelay());
        animator.setDuration(this.getDuration());
        animator.setInterpolator(this.getInterpolator());
        if (animator instanceof ValueAnimator) {
            ((ValueAnimator)animator).setRepeatCount(this.getRepeatCount());
            ((ValueAnimator)animator).setRepeatMode(this.getRepeatMode());
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MotionTiming)) {
            return false;
        }
        object = (MotionTiming)object;
        if (this.getDelay() != object.getDelay()) {
            return false;
        }
        if (this.getDuration() != object.getDuration()) {
            return false;
        }
        if (this.getRepeatCount() != object.getRepeatCount()) {
            return false;
        }
        if (this.getRepeatMode() != object.getRepeatMode()) {
            return false;
        }
        return this.getInterpolator().getClass().equals(object.getInterpolator().getClass());
    }

    public long getDelay() {
        return this.delay;
    }

    public long getDuration() {
        return this.duration;
    }

    public TimeInterpolator getInterpolator() {
        TimeInterpolator timeInterpolator = this.interpolator;
        if (timeInterpolator != null) {
            return timeInterpolator;
        }
        return AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public int getRepeatMode() {
        return this.repeatMode;
    }

    public int hashCode() {
        return ((((int)(this.getDelay() ^ this.getDelay() >>> 32) * 31 + (int)(this.getDuration() ^ this.getDuration() >>> 32)) * 31 + this.getInterpolator().getClass().hashCode()) * 31 + this.getRepeatCount()) * 31 + this.getRepeatMode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append(this.getClass().getName());
        stringBuilder.append('{');
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" delay: ");
        stringBuilder.append(this.getDelay());
        stringBuilder.append(" duration: ");
        stringBuilder.append(this.getDuration());
        stringBuilder.append(" interpolator: ");
        stringBuilder.append(this.getInterpolator().getClass());
        stringBuilder.append(" repeatCount: ");
        stringBuilder.append(this.getRepeatCount());
        stringBuilder.append(" repeatMode: ");
        stringBuilder.append(this.getRepeatMode());
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
}

