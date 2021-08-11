/*
 * Decompiled with CFR 0_124.
 */
package android.support.transition;

import android.support.transition.SceneImpl;
import android.support.transition.TransitionImpl;

abstract class TransitionManagerImpl {
    TransitionManagerImpl() {
    }

    public abstract void setTransition(SceneImpl var1, SceneImpl var2, TransitionImpl var3);

    public abstract void setTransition(SceneImpl var1, TransitionImpl var2);

    public abstract void transitionTo(SceneImpl var1);
}

