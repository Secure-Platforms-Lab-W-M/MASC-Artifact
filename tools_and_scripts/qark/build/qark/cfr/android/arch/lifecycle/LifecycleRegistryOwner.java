/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;

@Deprecated
public interface LifecycleRegistryOwner
extends LifecycleOwner {
    @Override
    public LifecycleRegistry getLifecycle();
}

