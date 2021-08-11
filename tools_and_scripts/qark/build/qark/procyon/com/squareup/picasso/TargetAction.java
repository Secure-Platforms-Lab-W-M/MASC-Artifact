// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

final class TargetAction extends Action<Target>
{
    TargetAction(final Picasso picasso, final Target target, final Request request, final int n, final int n2, final Drawable drawable, final String s, final Object o, final int n3) {
        super(picasso, target, request, n, n2, n3, drawable, s, o, false);
    }
    
    @Override
    void complete(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
        if (bitmap == null) {
            throw new AssertionError((Object)String.format("Attempted to complete action with no result!\n%s", this));
        }
        final Target target = this.getTarget();
        if (target != null) {
            target.onBitmapLoaded(bitmap, loadedFrom);
            if (bitmap.isRecycled()) {
                throw new IllegalStateException("Target callback must not recycle bitmap!");
            }
        }
    }
    
    @Override
    void error() {
        final Target target = this.getTarget();
        if (target != null) {
            if (this.errorResId == 0) {
                target.onBitmapFailed(this.errorDrawable);
                return;
            }
            target.onBitmapFailed(this.picasso.context.getResources().getDrawable(this.errorResId));
        }
    }
}
