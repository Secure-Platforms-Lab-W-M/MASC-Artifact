/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.VelocityTracker
 */
package android.support.v4.view;

import android.view.VelocityTracker;

@Deprecated
public final class VelocityTrackerCompat {
    private VelocityTrackerCompat() {
    }

    @Deprecated
    public static float getXVelocity(VelocityTracker velocityTracker, int n) {
        return velocityTracker.getXVelocity(n);
    }

    @Deprecated
    public static float getYVelocity(VelocityTracker velocityTracker, int n) {
        return velocityTracker.getYVelocity(n);
    }
}

