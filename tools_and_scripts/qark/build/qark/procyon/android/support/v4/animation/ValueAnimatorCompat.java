// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.animation;

import android.view.View;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface ValueAnimatorCompat
{
    void addListener(final AnimatorListenerCompat p0);
    
    void addUpdateListener(final AnimatorUpdateListenerCompat p0);
    
    void cancel();
    
    float getAnimatedFraction();
    
    void setDuration(final long p0);
    
    void setTarget(final View p0);
    
    void start();
}
