/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ViewStub
 *  android.view.ViewStub$OnInflateListener
 */
package androidx.databinding.adapters;

import android.view.ViewStub;
import androidx.databinding.ViewStubProxy;

public class ViewStubBindingAdapter {
    public static void setOnInflateListener(ViewStubProxy viewStubProxy, ViewStub.OnInflateListener onInflateListener) {
        viewStubProxy.setOnInflateListener(onInflateListener);
    }
}

