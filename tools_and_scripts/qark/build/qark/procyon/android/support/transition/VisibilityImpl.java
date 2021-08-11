// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;

interface VisibilityImpl
{
    boolean isVisible(final TransitionValues p0);
    
    Animator onAppear(final ViewGroup p0, final TransitionValues p1, final int p2, final TransitionValues p3, final int p4);
    
    Animator onDisappear(final ViewGroup p0, final TransitionValues p1, final int p2, final TransitionValues p3, final int p4);
}
