// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Transformation
{
    String key();
    
    Bitmap transform(final Bitmap p0);
}
