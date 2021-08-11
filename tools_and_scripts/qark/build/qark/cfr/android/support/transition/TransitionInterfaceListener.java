/*
 * Decompiled with CFR 0_124.
 */
package android.support.transition;

import android.support.transition.TransitionInterface;

interface TransitionInterfaceListener<TransitionT extends TransitionInterface> {
    public void onTransitionCancel(TransitionT var1);

    public void onTransitionEnd(TransitionT var1);

    public void onTransitionPause(TransitionT var1);

    public void onTransitionResume(TransitionT var1);

    public void onTransitionStart(TransitionT var1);
}

