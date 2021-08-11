// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

class FetchAction extends Action<Object>
{
    private Callback callback;
    private final Object target;
    
    FetchAction(final Picasso picasso, final Request request, final int n, final int n2, final Object o, final String s, final Callback callback) {
        super(picasso, null, request, n, n2, 0, null, s, o, false);
        this.target = new Object();
        this.callback = callback;
    }
    
    @Override
    void cancel() {
        super.cancel();
        this.callback = null;
    }
    
    @Override
    void complete(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
        if (this.callback != null) {
            this.callback.onSuccess();
        }
    }
    
    @Override
    void error() {
        if (this.callback != null) {
            this.callback.onError();
        }
    }
    
    @Override
    Object getTarget() {
        return this.target;
    }
}
