/*
 * Decompiled with CFR 0_124.
 */
package android.support.design.widget;

class MathUtils {
    MathUtils() {
    }

    static float constrain(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    static int constrain(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
}

