/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.EdgeEffect
 */
package android.support.v4.widget;

import android.widget.EdgeEffect;

class EdgeEffectCompatLollipop {
    EdgeEffectCompatLollipop() {
    }

    public static boolean onPull(Object object, float f, float f2) {
        ((EdgeEffect)object).onPull(f, f2);
        return true;
    }
}

