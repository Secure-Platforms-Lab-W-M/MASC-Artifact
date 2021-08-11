/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Paint
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.graphics;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.PaintCompatApi14;

public final class PaintCompat {
    private PaintCompat() {
    }

    public static boolean hasGlyph(@NonNull Paint paint, @NonNull String string2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return paint.hasGlyph(string2);
        }
        return PaintCompatApi14.hasGlyph(paint, string2);
    }
}

