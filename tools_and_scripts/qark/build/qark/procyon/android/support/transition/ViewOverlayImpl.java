// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
interface ViewOverlayImpl
{
    void add(@NonNull final Drawable p0);
    
    void clear();
    
    void remove(@NonNull final Drawable p0);
}
