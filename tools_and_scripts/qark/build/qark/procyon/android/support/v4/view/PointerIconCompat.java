// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.support.annotation.RestrictTo;
import android.content.res.Resources;
import android.content.Context;
import android.view.PointerIcon;
import android.os.Build$VERSION;
import android.graphics.Bitmap;

public final class PointerIconCompat
{
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
    
    private PointerIconCompat(final Object mPointerIcon) {
        this.mPointerIcon = mPointerIcon;
    }
    
    public static PointerIconCompat create(final Bitmap bitmap, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 24) {
            return new PointerIconCompat(PointerIcon.create(bitmap, n, n2));
        }
        return new PointerIconCompat(null);
    }
    
    public static PointerIconCompat getSystemIcon(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return new PointerIconCompat(PointerIcon.getSystemIcon(context, n));
        }
        return new PointerIconCompat(null);
    }
    
    public static PointerIconCompat load(final Resources resources, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return new PointerIconCompat(PointerIcon.load(resources, n));
        }
        return new PointerIconCompat(null);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public Object getPointerIcon() {
        return this.mPointerIcon;
    }
}
