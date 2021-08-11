// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.view.ViewGroup;

abstract class TransitionManagerStaticsImpl
{
    public abstract void beginDelayedTransition(final ViewGroup p0);
    
    public abstract void beginDelayedTransition(final ViewGroup p0, final TransitionImpl p1);
    
    public abstract void go(final SceneImpl p0);
    
    public abstract void go(final SceneImpl p0, final TransitionImpl p1);
}
