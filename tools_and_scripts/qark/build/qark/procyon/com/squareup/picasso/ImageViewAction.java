// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

class ImageViewAction extends Action<ImageView>
{
    Callback callback;
    
    ImageViewAction(final Picasso picasso, final ImageView imageView, final Request request, final int n, final int n2, final int n3, final Drawable drawable, final String s, final Object o, final Callback callback, final boolean b) {
        super(picasso, imageView, request, n, n2, n3, drawable, s, o, b);
        this.callback = callback;
    }
    
    @Override
    void cancel() {
        super.cancel();
        if (this.callback != null) {
            this.callback = null;
        }
    }
    
    public void complete(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
        if (bitmap == null) {
            throw new AssertionError((Object)String.format("Attempted to complete action with no result!\n%s", this));
        }
        final ImageView imageView = this.target.get();
        if (imageView != null) {
            PicassoDrawable.setBitmap(imageView, this.picasso.context, bitmap, loadedFrom, this.noFade, this.picasso.indicatorsEnabled);
            if (this.callback != null) {
                this.callback.onSuccess();
            }
        }
    }
    
    public void error() {
        final ImageView imageView = this.target.get();
        if (imageView != null) {
            if (this.errorResId != 0) {
                imageView.setImageResource(this.errorResId);
            }
            else if (this.errorDrawable != null) {
                imageView.setImageDrawable(this.errorDrawable);
            }
            if (this.callback != null) {
                this.callback.onError();
            }
        }
    }
}
