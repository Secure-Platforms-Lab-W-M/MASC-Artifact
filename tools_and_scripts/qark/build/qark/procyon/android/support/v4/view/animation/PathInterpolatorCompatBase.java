// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;

class PathInterpolatorCompatBase
{
    private PathInterpolatorCompatBase() {
    }
    
    public static Interpolator create(final float n, final float n2) {
        return (Interpolator)new PathInterpolatorDonut(n, n2);
    }
    
    public static Interpolator create(final float n, final float n2, final float n3, final float n4) {
        return (Interpolator)new PathInterpolatorDonut(n, n2, n3, n4);
    }
    
    public static Interpolator create(final Path path) {
        return (Interpolator)new PathInterpolatorDonut(path);
    }
}
