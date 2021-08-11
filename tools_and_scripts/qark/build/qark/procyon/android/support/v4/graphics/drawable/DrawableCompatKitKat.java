// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(19)
@RequiresApi(19)
class DrawableCompatKitKat
{
    public static int getAlpha(final Drawable drawable) {
        return drawable.getAlpha();
    }
    
    public static boolean isAutoMirrored(final Drawable drawable) {
        return drawable.isAutoMirrored();
    }
    
    public static void setAutoMirrored(final Drawable drawable, final boolean autoMirrored) {
        drawable.setAutoMirrored(autoMirrored);
    }
    
    public static Drawable wrapForTinting(final Drawable drawable) {
        Drawable drawable2 = drawable;
        if (!(drawable instanceof TintAwareDrawable)) {
            drawable2 = new DrawableWrapperKitKat(drawable);
        }
        return drawable2;
    }
}
