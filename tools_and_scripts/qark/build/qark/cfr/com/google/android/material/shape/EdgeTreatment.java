/*
 * Decompiled with CFR 0_124.
 */
package com.google.android.material.shape;

import com.google.android.material.shape.ShapePath;

public class EdgeTreatment {
    public void getEdgePath(float f, float f2, float f3, ShapePath shapePath) {
        shapePath.lineTo(f, 0.0f);
    }

    @Deprecated
    public void getEdgePath(float f, float f2, ShapePath shapePath) {
        this.getEdgePath(f, f / 2.0f, f2, shapePath);
    }
}

