/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.ScaleGestureDetector
 */
package android.support.v4.view;

import android.os.Build;
import android.view.ScaleGestureDetector;

public final class ScaleGestureDetectorCompat {
    private ScaleGestureDetectorCompat() {
    }

    public static boolean isQuickScaleEnabled(ScaleGestureDetector scaleGestureDetector) {
        if (Build.VERSION.SDK_INT >= 19) {
            return scaleGestureDetector.isQuickScaleEnabled();
        }
        return false;
    }

    @Deprecated
    public static boolean isQuickScaleEnabled(Object object) {
        return ScaleGestureDetectorCompat.isQuickScaleEnabled((ScaleGestureDetector)object);
    }

    public static void setQuickScaleEnabled(ScaleGestureDetector scaleGestureDetector, boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            scaleGestureDetector.setQuickScaleEnabled(bl);
            return;
        }
    }

    @Deprecated
    public static void setQuickScaleEnabled(Object object, boolean bl) {
        ScaleGestureDetectorCompat.setQuickScaleEnabled((ScaleGestureDetector)object, bl);
    }
}

