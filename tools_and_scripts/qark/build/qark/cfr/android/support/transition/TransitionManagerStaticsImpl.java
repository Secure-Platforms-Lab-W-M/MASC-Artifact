/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.support.transition.SceneImpl;
import android.support.transition.TransitionImpl;
import android.view.ViewGroup;

abstract class TransitionManagerStaticsImpl {
    TransitionManagerStaticsImpl() {
    }

    public abstract void beginDelayedTransition(ViewGroup var1);

    public abstract void beginDelayedTransition(ViewGroup var1, TransitionImpl var2);

    public abstract void go(SceneImpl var1);

    public abstract void go(SceneImpl var1, TransitionImpl var2);
}

