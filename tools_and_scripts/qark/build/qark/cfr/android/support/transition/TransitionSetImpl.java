/*
 * Decompiled with CFR 0_124.
 */
package android.support.transition;

import android.support.transition.TransitionImpl;

interface TransitionSetImpl {
    public TransitionSetImpl addTransition(TransitionImpl var1);

    public int getOrdering();

    public TransitionSetImpl removeTransition(TransitionImpl var1);

    public TransitionSetImpl setOrdering(int var1);
}

