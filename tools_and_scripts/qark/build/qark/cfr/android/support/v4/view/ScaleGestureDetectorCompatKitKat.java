/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ScaleGestureDetector
 */
package android.support.v4.view;

import android.view.ScaleGestureDetector;

class ScaleGestureDetectorCompatKitKat {
    private ScaleGestureDetectorCompatKitKat() {
    }

    public static boolean isQuickScaleEnabled(Object object) {
        return ((ScaleGestureDetector)object).isQuickScaleEnabled();
    }

    public static void setQuickScaleEnabled(Object object, boolean bl) {
        ((ScaleGestureDetector)object).setQuickScaleEnabled(bl);
    }
}

