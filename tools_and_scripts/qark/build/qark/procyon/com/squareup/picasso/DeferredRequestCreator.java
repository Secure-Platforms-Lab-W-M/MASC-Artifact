// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import android.view.ViewTreeObserver$OnPreDrawListener;

class DeferredRequestCreator implements ViewTreeObserver$OnPreDrawListener
{
    Callback callback;
    final RequestCreator creator;
    final WeakReference<ImageView> target;
    
    DeferredRequestCreator(final RequestCreator requestCreator, final ImageView imageView) {
        this(requestCreator, imageView, null);
    }
    
    DeferredRequestCreator(final RequestCreator creator, final ImageView imageView, final Callback callback) {
        this.creator = creator;
        this.target = new WeakReference<ImageView>(imageView);
        this.callback = callback;
        imageView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
    }
    
    void cancel() {
        this.callback = null;
        final ImageView imageView = this.target.get();
        if (imageView != null) {
            final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
            }
        }
    }
    
    public boolean onPreDraw() {
        final ImageView imageView = this.target.get();
        if (imageView != null) {
            final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                final int width = imageView.getWidth();
                final int height = imageView.getHeight();
                if (width > 0 && height > 0) {
                    viewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    this.creator.unfit().resize(width, height).into(imageView, this.callback);
                    return true;
                }
            }
        }
        return true;
    }
}
