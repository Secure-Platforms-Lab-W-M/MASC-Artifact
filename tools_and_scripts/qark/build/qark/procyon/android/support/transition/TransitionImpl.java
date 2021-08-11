// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.util.List;
import android.animation.TimeInterpolator;
import android.animation.Animator;
import android.view.ViewGroup;
import android.view.View;

abstract class TransitionImpl
{
    public abstract TransitionImpl addListener(final TransitionInterfaceListener p0);
    
    public abstract TransitionImpl addTarget(final int p0);
    
    public abstract TransitionImpl addTarget(final View p0);
    
    public abstract void captureEndValues(final TransitionValues p0);
    
    public abstract void captureStartValues(final TransitionValues p0);
    
    public abstract Animator createAnimator(final ViewGroup p0, final TransitionValues p1, final TransitionValues p2);
    
    public abstract TransitionImpl excludeChildren(final int p0, final boolean p1);
    
    public abstract TransitionImpl excludeChildren(final View p0, final boolean p1);
    
    public abstract TransitionImpl excludeChildren(final Class p0, final boolean p1);
    
    public abstract TransitionImpl excludeTarget(final int p0, final boolean p1);
    
    public abstract TransitionImpl excludeTarget(final View p0, final boolean p1);
    
    public abstract TransitionImpl excludeTarget(final Class p0, final boolean p1);
    
    public abstract long getDuration();
    
    public abstract TimeInterpolator getInterpolator();
    
    public abstract String getName();
    
    public abstract long getStartDelay();
    
    public abstract List<Integer> getTargetIds();
    
    public abstract List<View> getTargets();
    
    public abstract String[] getTransitionProperties();
    
    public abstract TransitionValues getTransitionValues(final View p0, final boolean p1);
    
    public void init(final TransitionInterface transitionInterface) {
        this.init(transitionInterface, null);
    }
    
    public abstract void init(final TransitionInterface p0, final Object p1);
    
    public abstract TransitionImpl removeListener(final TransitionInterfaceListener p0);
    
    public abstract TransitionImpl removeTarget(final int p0);
    
    public abstract TransitionImpl removeTarget(final View p0);
    
    public abstract TransitionImpl setDuration(final long p0);
    
    public abstract TransitionImpl setInterpolator(final TimeInterpolator p0);
    
    public abstract TransitionImpl setStartDelay(final long p0);
}
