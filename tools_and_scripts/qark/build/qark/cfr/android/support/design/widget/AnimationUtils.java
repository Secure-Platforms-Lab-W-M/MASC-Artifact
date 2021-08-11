/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 */
package android.support.design.widget;

import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

class AnimationUtils {
    static final Interpolator DECELERATE_INTERPOLATOR;
    static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR;
    static final Interpolator LINEAR_INTERPOLATOR;
    static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR;

    static {
        LINEAR_INTERPOLATOR = new LinearInterpolator();
        FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
        FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
        LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
        DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    }

    AnimationUtils() {
    }

    static float lerp(float f, float f2, float f3) {
        return (f2 - f) * f3 + f;
    }

    static int lerp(int n, int n2, float f) {
        return Math.round((float)(n2 - n) * f) + n;
    }
}

