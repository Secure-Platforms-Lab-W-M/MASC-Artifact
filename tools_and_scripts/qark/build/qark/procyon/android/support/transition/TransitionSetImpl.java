// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

interface TransitionSetImpl
{
    TransitionSetImpl addTransition(final TransitionImpl p0);
    
    int getOrdering();
    
    TransitionSetImpl removeTransition(final TransitionImpl p0);
    
    TransitionSetImpl setOrdering(final int p0);
}
