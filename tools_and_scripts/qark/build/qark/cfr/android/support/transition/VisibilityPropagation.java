/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.transition;

import android.support.transition.TransitionPropagation;
import android.support.transition.TransitionValues;
import android.view.View;
import java.util.Map;

public abstract class VisibilityPropagation
extends TransitionPropagation {
    private static final String PROPNAME_VIEW_CENTER = "android:visibilityPropagation:center";
    private static final String PROPNAME_VISIBILITY = "android:visibilityPropagation:visibility";
    private static final String[] VISIBILITY_PROPAGATION_VALUES = new String[]{"android:visibilityPropagation:visibility", "android:visibilityPropagation:center"};

    private static int getViewCoordinate(TransitionValues arrn, int n) {
        if (arrn == null) {
            return -1;
        }
        arrn = (int[])arrn.values.get("android:visibilityPropagation:center");
        if (arrn == null) {
            return -1;
        }
        return arrn[n];
    }

    @Override
    public void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        int[] arrn = (int[])transitionValues.values.get("android:visibility:visibility");
        if (arrn == null) {
            arrn = view.getVisibility();
        }
        transitionValues.values.put("android:visibilityPropagation:visibility", (Integer)arrn);
        arrn = new int[2];
        view.getLocationOnScreen(arrn);
        arrn[0] = arrn[0] + Math.round(view.getTranslationX());
        arrn[0] = arrn[0] + view.getWidth() / 2;
        arrn[1] = arrn[1] + Math.round(view.getTranslationY());
        arrn[1] = arrn[1] + view.getHeight() / 2;
        transitionValues.values.put("android:visibilityPropagation:center", arrn);
    }

    @Override
    public String[] getPropagationProperties() {
        return VISIBILITY_PROPAGATION_VALUES;
    }

    public int getViewVisibility(TransitionValues object) {
        if (object == null) {
            return 8;
        }
        object = (Integer)object.values.get("android:visibilityPropagation:visibility");
        if (object == null) {
            return 8;
        }
        return object.intValue();
    }

    public int getViewX(TransitionValues transitionValues) {
        return VisibilityPropagation.getViewCoordinate(transitionValues, 0);
    }

    public int getViewY(TransitionValues transitionValues) {
        return VisibilityPropagation.getViewCoordinate(transitionValues, 1);
    }
}

