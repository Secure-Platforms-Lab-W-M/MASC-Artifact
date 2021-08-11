/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.view.PointerIcon
 */
package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.view.PointerIcon;

@RequiresApi(value=24)
class PointerIconCompatApi24 {
    PointerIconCompatApi24() {
    }

    public static Object create(Bitmap bitmap, float f, float f2) {
        return PointerIcon.create((Bitmap)bitmap, (float)f, (float)f2);
    }

    public static Object getSystemIcon(Context context, int n) {
        return PointerIcon.getSystemIcon((Context)context, (int)n);
    }

    public static Object load(Resources resources, int n) {
        return PointerIcon.load((Resources)resources, (int)n);
    }
}

