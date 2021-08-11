// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface WrappedDrawable
{
    Drawable getWrappedDrawable();
    
    void setWrappedDrawable(final Drawable p0);
}
