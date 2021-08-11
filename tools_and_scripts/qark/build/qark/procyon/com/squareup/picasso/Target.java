// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface Target
{
    void onBitmapFailed(final Drawable p0);
    
    void onBitmapLoaded(final Bitmap p0, final Picasso.LoadedFrom p1);
    
    void onPrepareLoad(final Drawable p0);
}
