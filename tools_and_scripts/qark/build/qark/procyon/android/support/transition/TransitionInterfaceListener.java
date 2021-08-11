// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

interface TransitionInterfaceListener<TransitionT extends TransitionInterface>
{
    void onTransitionCancel(final TransitionT p0);
    
    void onTransitionEnd(final TransitionT p0);
    
    void onTransitionPause(final TransitionT p0);
    
    void onTransitionResume(final TransitionT p0);
    
    void onTransitionStart(final TransitionT p0);
}
