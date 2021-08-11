/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 *  android.graphics.Matrix
 */
package com.google.android.material.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator
implements TypeEvaluator<Matrix> {
    private final float[] tempEndValues = new float[9];
    private final Matrix tempMatrix = new Matrix();
    private final float[] tempStartValues = new float[9];

    public Matrix evaluate(float f, Matrix arrf, Matrix arrf2) {
        arrf.getValues(this.tempStartValues);
        arrf2.getValues(this.tempEndValues);
        for (int i = 0; i < 9; ++i) {
            arrf = this.tempEndValues;
            float f2 = arrf[i];
            arrf2 = this.tempStartValues;
            float f3 = arrf2[i];
            arrf[i] = arrf2[i] + f * (f2 - f3);
        }
        this.tempMatrix.setValues(this.tempEndValues);
        return this.tempMatrix;
    }
}

