/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import androidx.databinding.ViewDataBinding;

public abstract class OnRebindCallback<T extends ViewDataBinding> {
    public void onBound(T t) {
    }

    public void onCanceled(T t) {
    }

    public boolean onPreBind(T t) {
        return true;
    }
}

