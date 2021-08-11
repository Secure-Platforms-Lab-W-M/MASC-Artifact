// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.animation;

import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface AnimatorListenerCompat
{
    void onAnimationCancel(final ValueAnimatorCompat p0);
    
    void onAnimationEnd(final ValueAnimatorCompat p0);
    
    void onAnimationRepeat(final ValueAnimatorCompat p0);
    
    void onAnimationStart(final ValueAnimatorCompat p0);
}
