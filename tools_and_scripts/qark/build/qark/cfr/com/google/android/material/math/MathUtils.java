/*
 * Decompiled with CFR 0_124.
 */
package com.google.android.material.math;

public final class MathUtils {
    public static final float DEFAULT_EPSILON = 1.0E-4f;

    private MathUtils() {
    }

    public static float dist(float f, float f2, float f3, float f4) {
        return (float)Math.hypot(f3 - f, f4 - f2);
    }

    public static float distanceToFurthestCorner(float f, float f2, float f3, float f4, float f5, float f6) {
        return MathUtils.max(MathUtils.dist(f, f2, f3, f4), MathUtils.dist(f, f2, f5, f4), MathUtils.dist(f, f2, f5, f6), MathUtils.dist(f, f2, f3, f6));
    }

    public static boolean geq(float f, float f2, float f3) {
        if (f + f3 >= f2) {
            return true;
        }
        return false;
    }

    public static float lerp(float f, float f2, float f3) {
        return (1.0f - f3) * f + f3 * f2;
    }

    private static float max(float f, float f2, float f3, float f4) {
        if (f > f2 && f > f3 && f > f4) {
            return f;
        }
        if (f2 > f3 && f2 > f4) {
            return f2;
        }
        if (f3 > f4) {
            return f3;
        }
        return f4;
    }
}

