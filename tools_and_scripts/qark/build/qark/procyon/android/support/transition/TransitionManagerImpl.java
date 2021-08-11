// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

abstract class TransitionManagerImpl
{
    public abstract void setTransition(final SceneImpl p0, final SceneImpl p1, final TransitionImpl p2);
    
    public abstract void setTransition(final SceneImpl p0, final TransitionImpl p1);
    
    public abstract void transitionTo(final SceneImpl p0);
}
