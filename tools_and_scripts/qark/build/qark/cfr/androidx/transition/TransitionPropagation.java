/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.view.ViewGroup;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

public abstract class TransitionPropagation {
    public abstract void captureValues(TransitionValues var1);

    public abstract String[] getPropagationProperties();

    public abstract long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4);
}

