/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.util.Property
 *  android.view.View
 */
package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.transition.ViewOverlayApi14;
import androidx.transition.ViewOverlayApi18;
import androidx.transition.ViewOverlayImpl;
import androidx.transition.ViewUtilsApi19;
import androidx.transition.ViewUtilsApi21;
import androidx.transition.ViewUtilsApi22;
import androidx.transition.ViewUtilsApi23;
import androidx.transition.ViewUtilsApi29;
import androidx.transition.ViewUtilsBase;
import androidx.transition.WindowIdApi14;
import androidx.transition.WindowIdApi18;
import androidx.transition.WindowIdImpl;

class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS;
    private static final ViewUtilsBase IMPL;
    private static final String TAG = "ViewUtils";
    static final Property<View, Float> TRANSITION_ALPHA;

    static {
        IMPL = Build.VERSION.SDK_INT >= 29 ? new ViewUtilsApi29() : (Build.VERSION.SDK_INT >= 23 ? new ViewUtilsApi23() : (Build.VERSION.SDK_INT >= 22 ? new ViewUtilsApi22() : (Build.VERSION.SDK_INT >= 21 ? new ViewUtilsApi21() : (Build.VERSION.SDK_INT >= 19 ? new ViewUtilsApi19() : new ViewUtilsBase()))));
        TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha"){

            public Float get(View view) {
                return Float.valueOf(ViewUtils.getTransitionAlpha(view));
            }

            public void set(View view, Float f) {
                ViewUtils.setTransitionAlpha(view, f.floatValue());
            }
        };
        CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds"){

            public Rect get(View view) {
                return ViewCompat.getClipBounds(view);
            }

            public void set(View view, Rect rect) {
                ViewCompat.setClipBounds(view, rect);
            }
        };
    }

    private ViewUtils() {
    }

    static void clearNonTransitionAlpha(View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    static ViewOverlayImpl getOverlay(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewOverlayApi18(view);
        }
        return ViewOverlayApi14.createFrom(view);
    }

    static float getTransitionAlpha(View view) {
        return IMPL.getTransitionAlpha(view);
    }

    static WindowIdImpl getWindowId(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new WindowIdApi18(view);
        }
        return new WindowIdApi14(view.getWindowToken());
    }

    static void saveNonTransitionAlpha(View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    static void setAnimationMatrix(View view, Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    static void setLeftTopRightBottom(View view, int n, int n2, int n3, int n4) {
        IMPL.setLeftTopRightBottom(view, n, n2, n3, n4);
    }

    static void setTransitionAlpha(View view, float f) {
        IMPL.setTransitionAlpha(view, f);
    }

    static void setTransitionVisibility(View view, int n) {
        IMPL.setTransitionVisibility(view, n);
    }

    static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

}

