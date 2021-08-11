/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.animation;

import android.support.annotation.RestrictTo;
import android.support.v4.animation.ValueAnimatorCompat;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface AnimatorListenerCompat {
    public void onAnimationCancel(ValueAnimatorCompat var1);

    public void onAnimationEnd(ValueAnimatorCompat var1);

    public void onAnimationRepeat(ValueAnimatorCompat var1);

    public void onAnimationStart(ValueAnimatorCompat var1);
}

