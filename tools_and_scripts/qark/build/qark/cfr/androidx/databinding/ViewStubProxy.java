/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewStub
 *  android.view.ViewStub$OnInflateListener
 *  androidx.databinding.DataBindingComponent
 */
package androidx.databinding;

import android.view.View;
import android.view.ViewStub;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class ViewStubProxy {
    private ViewDataBinding mContainingBinding;
    private ViewStub.OnInflateListener mOnInflateListener;
    private ViewStub.OnInflateListener mProxyListener;
    private View mRoot;
    private ViewDataBinding mViewDataBinding;
    private ViewStub mViewStub;

    public ViewStubProxy(ViewStub viewStub) {
        ViewStub.OnInflateListener onInflateListener;
        this.mProxyListener = onInflateListener = new ViewStub.OnInflateListener(){

            public void onInflate(ViewStub viewStub, View view) {
                ViewStubProxy.this.mRoot = view;
                ViewStubProxy viewStubProxy = ViewStubProxy.this;
                viewStubProxy.mViewDataBinding = DataBindingUtil.bind(ViewStubProxy.access$200((ViewStubProxy)viewStubProxy).mBindingComponent, view, viewStub.getLayoutResource());
                ViewStubProxy.this.mViewStub = null;
                if (ViewStubProxy.this.mOnInflateListener != null) {
                    ViewStubProxy.this.mOnInflateListener.onInflate(viewStub, view);
                    ViewStubProxy.this.mOnInflateListener = null;
                }
                ViewStubProxy.this.mContainingBinding.invalidateAll();
                ViewStubProxy.this.mContainingBinding.forceExecuteBindings();
            }
        };
        this.mViewStub = viewStub;
        viewStub.setOnInflateListener(onInflateListener);
    }

    public ViewDataBinding getBinding() {
        return this.mViewDataBinding;
    }

    public View getRoot() {
        return this.mRoot;
    }

    public ViewStub getViewStub() {
        return this.mViewStub;
    }

    public boolean isInflated() {
        if (this.mRoot != null) {
            return true;
        }
        return false;
    }

    public void setContainingBinding(ViewDataBinding viewDataBinding) {
        this.mContainingBinding = viewDataBinding;
    }

    public void setOnInflateListener(ViewStub.OnInflateListener onInflateListener) {
        if (this.mViewStub != null) {
            this.mOnInflateListener = onInflateListener;
        }
    }

}

