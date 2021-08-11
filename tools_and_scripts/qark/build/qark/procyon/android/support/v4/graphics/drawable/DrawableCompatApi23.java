// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class DrawableCompatApi23
{
    public static int getLayoutDirection(final Drawable drawable) {
        return drawable.getLayoutDirection();
    }
    
    public static boolean setLayoutDirection(final Drawable drawable, final int layoutDirection) {
        return drawable.setLayoutDirection(layoutDirection);
    }
}
