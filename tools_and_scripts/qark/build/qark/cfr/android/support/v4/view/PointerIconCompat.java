/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.PointerIcon
 */
package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.view.PointerIcon;

public final class PointerIconCompat {
    public static final int TYPE_ALIAS = 1010;
    public static final int TYPE_ALL_SCROLL = 1013;
    public static final int TYPE_ARROW = 1000;
    public static final int TYPE_CELL = 1006;
    public static final int TYPE_CONTEXT_MENU = 1001;
    public static final int TYPE_COPY = 1011;
    public static final int TYPE_CROSSHAIR = 1007;
    public static final int TYPE_DEFAULT = 1000;
    public static final int TYPE_GRAB = 1020;
    public static final int TYPE_GRABBING = 1021;
    public static final int TYPE_HAND = 1002;
    public static final int TYPE_HELP = 1003;
    public static final int TYPE_HORIZONTAL_DOUBLE_ARROW = 1014;
    public static final int TYPE_NO_DROP = 1012;
    public static final int TYPE_NULL = 0;
    public static final int TYPE_TEXT = 1008;
    public static final int TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW = 1017;
    public static final int TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = 1016;
    public static final int TYPE_VERTICAL_DOUBLE_ARROW = 1015;
    public static final int TYPE_VERTICAL_TEXT = 1009;
    public static final int TYPE_WAIT = 1004;
    public static final int TYPE_ZOOM_IN = 1018;
    public static final int TYPE_ZOOM_OUT = 1019;
    private Object mPointerIcon;

    private PointerIconCompat(Object object) {
        this.mPointerIcon = object;
    }

    public static PointerIconCompat create(Bitmap bitmap, float f, float f2) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new PointerIconCompat((Object)PointerIcon.create((Bitmap)bitmap, (float)f, (float)f2));
        }
        return new PointerIconCompat(null);
    }

    public static PointerIconCompat getSystemIcon(Context context, int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new PointerIconCompat((Object)PointerIcon.getSystemIcon((Context)context, (int)n));
        }
        return new PointerIconCompat(null);
    }

    public static PointerIconCompat load(Resources resources, int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new PointerIconCompat((Object)PointerIcon.load((Resources)resources, (int)n));
        }
        return new PointerIconCompat(null);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public Object getPointerIcon() {
        return this.mPointerIcon;
    }
}

