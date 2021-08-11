/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.RectF
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package com.google.android.material.canvas;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;

public class CanvasCompat {
    private CanvasCompat() {
    }

    public static int saveLayerAlpha(Canvas canvas, float f, float f2, float f3, float f4, int n) {
        if (Build.VERSION.SDK_INT > 21) {
            return canvas.saveLayerAlpha(f, f2, f3, f4, n);
        }
        return canvas.saveLayerAlpha(f, f2, f3, f4, n, 31);
    }

    public static int saveLayerAlpha(Canvas canvas, RectF rectF, int n) {
        if (Build.VERSION.SDK_INT > 21) {
            return canvas.saveLayerAlpha(rectF, n);
        }
        return canvas.saveLayerAlpha(rectF, n, 31);
    }
}

