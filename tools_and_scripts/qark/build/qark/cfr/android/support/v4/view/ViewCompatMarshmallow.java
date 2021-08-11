/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.view;

import android.view.View;

class ViewCompatMarshmallow {
    ViewCompatMarshmallow() {
    }

    public static int getScrollIndicators(View view) {
        return view.getScrollIndicators();
    }

    static void offsetLeftAndRight(View view, int n) {
        view.offsetLeftAndRight(n);
    }

    static void offsetTopAndBottom(View view, int n) {
        view.offsetTopAndBottom(n);
    }

    public static void setScrollIndicators(View view, int n) {
        view.setScrollIndicators(n);
    }

    public static void setScrollIndicators(View view, int n, int n2) {
        view.setScrollIndicators(n, n2);
    }
}

