/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.view.animation.Interpolator
 */
package android.support.v4.view.animation;

import android.graphics.Path;
import android.support.v4.view.animation.PathInterpolatorDonut;
import android.view.animation.Interpolator;

class PathInterpolatorCompatBase {
    private PathInterpolatorCompatBase() {
    }

    public static Interpolator create(float f, float f2) {
        return new PathInterpolatorDonut(f, f2);
    }

    public static Interpolator create(float f, float f2, float f3, float f4) {
        return new PathInterpolatorDonut(f, f2, f3, f4);
    }

    public static Interpolator create(Path path) {
        return new PathInterpolatorDonut(path);
    }
}

