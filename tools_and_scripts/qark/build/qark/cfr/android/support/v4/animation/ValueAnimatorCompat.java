/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.animation;

import android.support.annotation.RestrictTo;
import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.view.View;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface ValueAnimatorCompat {
    public void addListener(AnimatorListenerCompat var1);

    public void addUpdateListener(AnimatorUpdateListenerCompat var1);

    public void cancel();

    public float getAnimatedFraction();

    public void setDuration(long var1);

    public void setTarget(View var1);

    public void start();
}

