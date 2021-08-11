/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 */
package com.google.android.material.shape;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;

public class MaterialShapeUtils {
    private MaterialShapeUtils() {
    }

    static CornerTreatment createCornerTreatment(int n) {
        if (n != 0) {
            if (n != 1) {
                return MaterialShapeUtils.createDefaultCornerTreatment();
            }
            return new CutCornerTreatment();
        }
        return new RoundedCornerTreatment();
    }

    static CornerTreatment createDefaultCornerTreatment() {
        return new RoundedCornerTreatment();
    }

    static EdgeTreatment createDefaultEdgeTreatment() {
        return new EdgeTreatment();
    }

    public static void setElevation(View view, float f) {
        if ((view = view.getBackground()) instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable)view).setElevation(f);
        }
    }

    public static void setParentAbsoluteElevation(View view) {
        Drawable drawable2 = view.getBackground();
        if (drawable2 instanceof MaterialShapeDrawable) {
            MaterialShapeUtils.setParentAbsoluteElevation(view, (MaterialShapeDrawable)drawable2);
        }
    }

    public static void setParentAbsoluteElevation(View view, MaterialShapeDrawable materialShapeDrawable) {
        if (materialShapeDrawable.isElevationOverlayEnabled()) {
            materialShapeDrawable.setParentAbsoluteElevation(ViewUtils.getParentAbsoluteElevation(view));
        }
    }
}

