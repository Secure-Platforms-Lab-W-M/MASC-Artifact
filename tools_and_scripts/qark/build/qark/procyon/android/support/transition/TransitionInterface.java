// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;

interface TransitionInterface
{
    void captureEndValues(final TransitionValues p0);
    
    void captureStartValues(final TransitionValues p0);
    
    Animator createAnimator(final ViewGroup p0, final TransitionValues p1, final TransitionValues p2);
}
