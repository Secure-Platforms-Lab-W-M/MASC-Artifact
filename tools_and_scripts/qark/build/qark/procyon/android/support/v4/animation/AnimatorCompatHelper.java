// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.animation;

import android.view.View;
import android.os.Build$VERSION;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class AnimatorCompatHelper
{
    private static final AnimatorProvider IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 12) {
            IMPL = new HoneycombMr1AnimatorCompatProvider();
            return;
        }
        IMPL = new GingerbreadAnimatorCompatProvider();
    }
    
    private AnimatorCompatHelper() {
    }
    
    public static void clearInterpolator(final View view) {
        AnimatorCompatHelper.IMPL.clearInterpolator(view);
    }
    
    public static ValueAnimatorCompat emptyValueAnimator() {
        return AnimatorCompatHelper.IMPL.emptyValueAnimator();
    }
}
