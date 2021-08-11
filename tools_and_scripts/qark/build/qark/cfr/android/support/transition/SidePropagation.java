/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.graphics.Rect;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.transition.VisibilityPropagation;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

public class SidePropagation
extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;
    private int mSide = 80;

    private int distance(View view, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = this.mSide;
        int n10 = 0;
        int n11 = 0;
        if (n9 == 8388611) {
            if (ViewCompat.getLayoutDirection(view) == 1) {
                n11 = 1;
            }
            n11 = n11 != 0 ? 5 : 3;
        } else if (n9 == 8388613) {
            n11 = n10;
            if (ViewCompat.getLayoutDirection(view) == 1) {
                n11 = 1;
            }
            n11 = n11 != 0 ? 3 : 5;
        } else {
            n11 = this.mSide;
        }
        if (n11 != 3) {
            if (n11 != 5) {
                if (n11 != 48) {
                    if (n11 != 80) {
                        return 0;
                    }
                    return n2 - n6 + Math.abs(n3 - n);
                }
                return n8 - n2 + Math.abs(n3 - n);
            }
            return n - n5 + Math.abs(n4 - n2);
        }
        return n7 - n + Math.abs(n4 - n2);
    }

    private int getMaxDistance(ViewGroup viewGroup) {
        int n = this.mSide;
        if (n != 3 && n != 5 && n != 8388611 && n != 8388613) {
            return viewGroup.getHeight();
        }
        return viewGroup.getWidth();
    }

    @Override
    public long getStartDelay(ViewGroup viewGroup, Transition transition, TransitionValues arrn, TransitionValues transitionValues) {
        int n;
        int n2;
        int n3;
        if (arrn == null && transitionValues == null) {
            return 0L;
        }
        Rect rect = transition.getEpicenter();
        if (transitionValues != null && this.getViewVisibility((TransitionValues)arrn) != 0) {
            n3 = 1;
            arrn = transitionValues;
        } else {
            n3 = -1;
        }
        int n4 = this.getViewX((TransitionValues)arrn);
        int n5 = this.getViewY((TransitionValues)arrn);
        arrn = new int[2];
        viewGroup.getLocationOnScreen(arrn);
        int n6 = arrn[0] + Math.round(viewGroup.getTranslationX());
        int n7 = arrn[1] + Math.round(viewGroup.getTranslationY());
        int n8 = n6 + viewGroup.getWidth();
        int n9 = n7 + viewGroup.getHeight();
        if (rect != null) {
            n2 = rect.centerX();
            n = rect.centerY();
        } else {
            n2 = (n6 + n8) / 2;
            n = (n7 + n9) / 2;
        }
        float f = (float)this.distance((View)viewGroup, n4, n5, n2, n, n6, n7, n8, n9) / (float)this.getMaxDistance(viewGroup);
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

    public void setSide(int n) {
        this.mSide = n;
    }
}

