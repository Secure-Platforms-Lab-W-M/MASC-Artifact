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

interface TransitionInterface {
    public void captureEndValues(TransitionValues var1);

    public void captureStartValues(TransitionValues var1);

    public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3);
}

