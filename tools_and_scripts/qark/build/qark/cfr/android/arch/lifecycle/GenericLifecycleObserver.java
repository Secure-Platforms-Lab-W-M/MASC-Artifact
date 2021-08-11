/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;

public interface GenericLifecycleObserver
extends LifecycleObserver {
    public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2);
}

