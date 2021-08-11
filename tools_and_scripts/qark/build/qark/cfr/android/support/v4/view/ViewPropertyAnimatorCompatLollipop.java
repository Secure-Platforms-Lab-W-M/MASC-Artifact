/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewPropertyAnimator
 */
package android.support.v4.view;

import android.view.View;
import android.view.ViewPropertyAnimator;

class ViewPropertyAnimatorCompatLollipop {
    ViewPropertyAnimatorCompatLollipop() {
    }

    public static void translationZ(View view, float f) {
        view.animate().translationZ(f);
    }

    public static void translationZBy(View view, float f) {
        view.animate().translationZBy(f);
    }

    public static void z(View view, float f) {
        view.animate().z(f);
    }

    public static void zBy(View view, float f) {
        view.animate().zBy(f);
    }
}

