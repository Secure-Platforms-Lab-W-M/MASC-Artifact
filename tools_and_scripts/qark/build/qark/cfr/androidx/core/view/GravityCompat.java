/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.Gravity
 */
package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;

public final class GravityCompat {
    public static final int END = 8388613;
    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
    public static final int START = 8388611;

    private GravityCompat() {
    }

    public static void apply(int n, int n2, int n3, Rect rect, int n4, int n5, Rect rect2, int n6) {
        if (Build.VERSION.SDK_INT >= 17) {
            Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (int)n4, (int)n5, (Rect)rect2, (int)n6);
            return;
        }
        Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (int)n4, (int)n5, (Rect)rect2);
    }

    public static void apply(int n, int n2, int n3, Rect rect, Rect rect2, int n4) {
        if (Build.VERSION.SDK_INT >= 17) {
            Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (Rect)rect2, (int)n4);
            return;
        }
        Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (Rect)rect2);
    }

    public static void applyDisplay(int n, Rect rect, Rect rect2, int n2) {
        if (Build.VERSION.SDK_INT >= 17) {
            Gravity.applyDisplay((int)n, (Rect)rect, (Rect)rect2, (int)n2);
            return;
        }
        Gravity.applyDisplay((int)n, (Rect)rect, (Rect)rect2);
    }

    public static int getAbsoluteGravity(int n, int n2) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Gravity.getAbsoluteGravity((int)n, (int)n2);
        }
        return -8388609 & n;
    }
}

