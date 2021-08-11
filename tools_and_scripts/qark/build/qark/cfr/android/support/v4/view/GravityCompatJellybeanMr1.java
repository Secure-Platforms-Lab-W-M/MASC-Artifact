/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.Gravity
 */
package android.support.v4.view;

import android.graphics.Rect;
import android.view.Gravity;

class GravityCompatJellybeanMr1 {
    GravityCompatJellybeanMr1() {
    }

    public static void apply(int n, int n2, int n3, Rect rect, int n4, int n5, Rect rect2, int n6) {
        Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (int)n4, (int)n5, (Rect)rect2, (int)n6);
    }

    public static void apply(int n, int n2, int n3, Rect rect, Rect rect2, int n4) {
        Gravity.apply((int)n, (int)n2, (int)n3, (Rect)rect, (Rect)rect2, (int)n4);
    }

    public static void applyDisplay(int n, Rect rect, Rect rect2, int n2) {
        Gravity.applyDisplay((int)n, (Rect)rect, (Rect)rect2, (int)n2);
    }

    public static int getAbsoluteGravity(int n, int n2) {
        return Gravity.getAbsoluteGravity((int)n, (int)n2);
    }
}

