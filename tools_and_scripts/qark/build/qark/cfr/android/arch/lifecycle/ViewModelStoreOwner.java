/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.ViewModelStore;
import android.support.annotation.NonNull;

public interface ViewModelStoreOwner {
    @NonNull
    public ViewModelStore getViewModelStore();
}

