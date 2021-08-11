// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.ViewTreeObserver;
import android.view.View;
import android.view.View$OnAttachStateChangeListener;
import android.view.ViewTreeObserver$OnPreDrawListener;

public final class OneShotPreDrawListener implements ViewTreeObserver$OnPreDrawListener, View$OnAttachStateChangeListener
{
    private final Runnable mRunnable;
    private final View mView;
    private ViewTreeObserver mViewTreeObserver;
    
    private OneShotPreDrawListener(final View mView, final Runnable mRunnable) {
        this.mView = mView;
        this.mViewTreeObserver = mView.getViewTreeObserver();
        this.mRunnable = mRunnable;
    }
    
    public static OneShotPreDrawListener add(final View view, final Runnable runnable) {
        if (view == null) {
            throw new NullPointerException("view == null");
        }
        if (runnable != null) {
            final OneShotPreDrawListener oneShotPreDrawListener = new OneShotPreDrawListener(view, runnable);
            view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)oneShotPreDrawListener);
            view.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)oneShotPreDrawListener);
            return oneShotPreDrawListener;
        }
        throw new NullPointerException("runnable == null");
    }
    
    public boolean onPreDraw() {
        this.removeListener();
        this.mRunnable.run();
        return true;
    }
    
    public void onViewAttachedToWindow(final View view) {
        this.mViewTreeObserver = view.getViewTreeObserver();
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.removeListener();
    }
    
    public void removeListener() {
        if (this.mViewTreeObserver.isAlive()) {
            this.mViewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
        }
        else {
            this.mView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
        }
        this.mView.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
    }
}
