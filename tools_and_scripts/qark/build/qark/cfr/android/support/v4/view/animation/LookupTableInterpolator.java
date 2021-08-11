/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.animation.Interpolator
 */
package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator
implements Interpolator {
    private final float mStepSize;
    private final float[] mValues;

    public LookupTableInterpolator(float[] arrf) {
        this.mValues = arrf;
        this.mStepSize = 1.0f / (float)(this.mValues.length - 1);
    }

    public float getInterpolation(float f) {
        if (f >= 1.0f) {
            return 1.0f;
        }
        if (f <= 0.0f) {
            return 0.0f;
        }
        float[] arrf = this.mValues;
        int n = Math.min((int)((float)(arrf.length - 1) * f), arrf.length - 2);
        float f2 = n;
        float f3 = this.mStepSize;
        f = (f - f2 * f3) / f3;
        arrf = this.mValues;
        return arrf[n] + (arrf[n + 1] - arrf[n]) * f;
    }
}

