// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

class GetAction extends Action<Void>
{
    GetAction(final Picasso picasso, final Request request, final int n, final int n2, final Object o, final String s) {
        super(picasso, null, request, n, n2, 0, null, s, o, false);
    }
    
    @Override
    void complete(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
    }
    
    public void error() {
    }
}
