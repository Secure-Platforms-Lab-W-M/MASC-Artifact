/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.graphics.Rect;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.transition.VisibilityPropagation;
import android.view.ViewGroup;

public class CircularPropagation
extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;

    private static float distance(float f, float f2, float f3, float f4) {
        f = f3 - f;
        f2 = f4 - f2;
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    @Override
    public long getStartDelay(ViewGroup viewGroup, Transition transition, TransitionValues arrn, TransitionValues transitionValues) {
        int n;
        int n2;
        if (arrn == null && transitionValues == null) {
            return 0L;
        }
        int n3 = 1;
        if (transitionValues != null && this.getViewVisibility((TransitionValues)arrn) != 0) {
            arrn = transitionValues;
        } else {
            n3 = -1;
        }
        int n4 = this.getViewX((TransitionValues)arrn);
        int n5 = this.getViewY((TransitionValues)arrn);
        arrn = transition.getEpicenter();
        if (arrn != null) {
            n2 = arrn.centerX();
            n = arrn.centerY();
        } else {
            arrn = new int[2];
            viewGroup.getLocationOnScreen(arrn);
            n2 = Math.round((float)(arrn[0] + viewGroup.getWidth() / 2) + viewGroup.getTranslationX());
            n = Math.round((float)(arrn[1] + viewGroup.getHeight() / 2) + viewGroup.getTranslationY());
        }
        float f = CircularPropagation.distance(n4, n5, n2, n) / CircularPropagation.distance(0.0f, 0.0f, viewGroup.getWidth(), viewGroup.getHeight());
        long l = transition.getDuration();
        if (l < 0L) {
            l = 300L;
        }
        return Math.round((float)((long)n3 * l) / this.mPropagationSpeed * f);
    }

    public void setPropagationSpeed(float f) {
        if (f != 0.0f) {
            this.mPropagationSpeed = f;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }
}

