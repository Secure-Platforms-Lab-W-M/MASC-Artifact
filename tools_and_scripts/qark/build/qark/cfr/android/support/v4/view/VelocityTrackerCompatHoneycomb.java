/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.VelocityTracker
 */
package android.support.v4.view;

import android.view.VelocityTracker;

class VelocityTrackerCompatHoneycomb {
    VelocityTrackerCompatHoneycomb() {
    }

    public static float getXVelocity(VelocityTracker velocityTracker, int n) {
        return velocityTracker.getXVelocity(n);
    }

    public static float getYVelocity(VelocityTracker velocityTracker, int n) {
        return velocityTracker.getYVelocity(n);
    }
}

