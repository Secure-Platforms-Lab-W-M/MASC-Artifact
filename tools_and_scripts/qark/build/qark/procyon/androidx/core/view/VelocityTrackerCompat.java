// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.VelocityTracker;

@Deprecated
public final class VelocityTrackerCompat
{
    private VelocityTrackerCompat() {
    }
    
    @Deprecated
    public static float getXVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getXVelocity(n);
    }
    
    @Deprecated
    public static float getYVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getYVelocity(n);
    }
}
