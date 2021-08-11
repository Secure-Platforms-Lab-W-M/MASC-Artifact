/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.animation;

import android.support.v4.animation.ValueAnimatorCompat;
import android.view.View;

interface AnimatorProvider {
    public void clearInterpolator(View var1);

    public ValueAnimatorCompat emptyValueAnimator();
}

