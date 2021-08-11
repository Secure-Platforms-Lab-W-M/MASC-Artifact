/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.Property
 *  android.view.View
 */
package android.support.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.ViewOverlayImpl;
import android.support.transition.ViewUtilsApi14;
import android.support.transition.ViewUtilsApi18;
import android.support.transition.ViewUtilsApi19;
import android.support.transition.ViewUtilsApi21;
import android.support.transition.ViewUtilsApi22;
import android.support.transition.ViewUtilsImpl;
import android.support.transition.WindowIdImpl;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.Property;
import android.view.View;
import java.lang.reflect.Field;

class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS;
    private static final ViewUtilsImpl IMPL;
    private static final String TAG = "ViewUtils";
    static final Property<View, Float> TRANSITION_ALPHA;
    private static final int VISIBILITY_MASK = 12;
    private static Field sViewFlagsField;
    private static boolean sViewFlagsFieldFetched;

    static {
        IMPL = Build.VERSION.SDK_INT >= 22 ? new ViewUtilsApi22() : (Build.VERSION.SDK_INT >= 21 ? new ViewUtilsApi21() : (Build.VERSION.SDK_INT >= 19 ? new ViewUtilsApi19() : (Build.VERSION.SDK_INT >= 18 ? new ViewUtilsApi18() : new ViewUtilsApi14())));
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

    ViewUtils() {
    }

    static void clearNonTransitionAlpha(@NonNull View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    private static void fetchViewFlagsField() {
        if (!sViewFlagsFieldFetched) {
            try {
                sViewFlagsField = View.class.getDeclaredField("mViewFlags");
                sViewFlagsField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.i((String)"ViewUtils", (String)"fetchViewFlagsField: ");
            }
            sViewFlagsFieldFetched = true;
            return;
        }
    }

    static ViewOverlayImpl getOverlay(@NonNull View view) {
        return IMPL.getOverlay(view);
    }

    static float getTransitionAlpha(@NonNull View view) {
        return IMPL.getTransitionAlpha(view);
    }

    static WindowIdImpl getWindowId(@NonNull View view) {
        return IMPL.getWindowId(view);
    }

    static void saveNonTransitionAlpha(@NonNull View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    static void setAnimationMatrix(@NonNull View view, @Nullable Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    static void setLeftTopRightBottom(@NonNull View view, int n, int n2, int n3, int n4) {
        IMPL.setLeftTopRightBottom(view, n, n2, n3, n4);
    }

    static void setTransitionAlpha(@NonNull View view, float f) {
        IMPL.setTransitionAlpha(view, f);
    }

    static void setTransitionVisibility(@NonNull View view, int n) {
        ViewUtils.fetchViewFlagsField();
        Field field = sViewFlagsField;
        if (field != null) {
            try {
                int n2 = field.getInt((Object)view);
                sViewFlagsField.setInt((Object)view, n2 & -13 | n);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                return;
            }
        }
    }

    static void transformMatrixToGlobal(@NonNull View view, @NonNull Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    static void transformMatrixToLocal(@NonNull View view, @NonNull Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

}

