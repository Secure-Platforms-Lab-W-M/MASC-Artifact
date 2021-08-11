/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 */
package android.support.v4.content.res;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

class ResourcesCompatApi21 {
    ResourcesCompatApi21() {
    }

    public static Drawable getDrawable(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        return resources.getDrawable(n, theme);
    }

    public static Drawable getDrawableForDensity(Resources resources, int n, int n2, Resources.Theme theme) throws Resources.NotFoundException {
        return resources.getDrawableForDensity(n, n2, theme);
    }
}

