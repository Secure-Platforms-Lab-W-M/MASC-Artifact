/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;

public abstract class TransitionPropagation {
    public abstract void captureValues(TransitionValues var1);

    public abstract String[] getPropagationProperties();

    public abstract long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4);
}

