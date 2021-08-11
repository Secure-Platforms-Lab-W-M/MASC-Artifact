package androidx.databinding;

import android.view.View;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;

public class ViewStubProxy {
   private ViewDataBinding mContainingBinding;
   private OnInflateListener mOnInflateListener;
   private OnInflateListener mProxyListener;
   private View mRoot;
   private ViewDataBinding mViewDataBinding;
   private ViewStub mViewStub;

   public ViewStubProxy(ViewStub var1) {
      OnInflateListener var2 = new OnInflateListener() {
         public void onInflate(ViewStub var1, View var2) {
            ViewStubProxy.this.mRoot = var2;
            ViewStubProxy var3 = ViewStubProxy.this;
            var3.mViewDataBinding = DataBindingUtil.bind(var3.mContainingBinding.mBindingComponent, var2, var1.getLayoutResource());
            ViewStubProxy.this.mViewStub = null;
            if (ViewStubProxy.this.mOnInflateListener != null) {
               ViewStubProxy.this.mOnInflateListener.onInflate(var1, var2);
               ViewStubProxy.this.mOnInflateListener = null;
            }

            ViewStubProxy.this.mContainingBinding.invalidateAll();
            ViewStubProxy.this.mContainingBinding.forceExecuteBindings();
         }
      };
      this.mProxyListener = var2;
      this.mViewStub = var1;
      var1.setOnInflateListener(var2);
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
      return this.mRoot != null;
   }

   public void setContainingBinding(ViewDataBinding var1) {
      this.mContainingBinding = var1;
   }

   public void setOnInflateListener(OnInflateListener var1) {
      if (this.mViewStub != null) {
         this.mOnInflateListener = var1;
      }

   }
}
