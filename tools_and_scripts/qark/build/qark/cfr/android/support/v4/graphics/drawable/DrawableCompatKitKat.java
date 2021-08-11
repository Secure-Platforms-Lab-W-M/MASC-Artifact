/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableWrapperKitKat;

class DrawableCompatKitKat {
    DrawableCompatKitKat() {
    }

    public static int getAlpha(Drawable drawable) {
        return drawable.getAlpha();
    }

    public static boolean isAutoMirrored(Drawable drawable) {
        return drawable.isAutoMirrored();
    }

    public static void setAutoMirrored(Drawable drawable, boolean bl) {
        drawable.setAutoMirrored(bl);
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        if (!(drawable instanceof DrawableWrapperKitKat)) {
            return new DrawableWrapperKitKat(drawable);
        }
        return drawable;
    }
}

