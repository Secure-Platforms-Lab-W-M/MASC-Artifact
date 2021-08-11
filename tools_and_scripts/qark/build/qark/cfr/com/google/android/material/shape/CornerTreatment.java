/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.RectF
 */
package com.google.android.material.shape;

import android.graphics.RectF;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapePath;

public class CornerTreatment {
    @Deprecated
    public void getCornerPath(float f, float f2, ShapePath shapePath) {
    }

    public void getCornerPath(ShapePath shapePath, float f, float f2, float f3) {
        this.getCornerPath(f, f2, shapePath);
    }

    public void getCornerPath(ShapePath shapePath, float f, float f2, RectF rectF, CornerSize cornerSize) {
        this.getCornerPath(shapePath, f, f2, cornerSize.getCornerSize(rectF));
    }
}

