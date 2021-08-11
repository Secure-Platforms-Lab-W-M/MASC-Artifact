/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.animation.Animator;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;

interface VisibilityImpl {
    public boolean isVisible(TransitionValues var1);

    public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5);

    public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5);
}

