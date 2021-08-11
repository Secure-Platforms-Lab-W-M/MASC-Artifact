/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Paint
 *  android.graphics.Rect
 */
package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

class PaintCompatApi14 {
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "\udb3f\udffd";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal();

    PaintCompatApi14() {
    }

    static boolean hasGlyph(@NonNull Paint paint, @NonNull String string2) {
        int n = string2.length();
        if (n == 1 && Character.isWhitespace(string2.charAt(0))) {
            return true;
        }
        float f = paint.measureText("\udb3f\udffd");
        float f2 = paint.measureText("m");
        float f3 = paint.measureText(string2);
        if (f3 == 0.0f) {
            return false;
        }
        if (string2.codePointCount(0, string2.length()) > 1) {
            int n2;
            if (f3 > 2.0f * f2) {
                return false;
            }
            f2 = 0.0f;
            for (int i = 0; i < n; i += n2) {
                n2 = Character.charCount(string2.codePointAt(i));
                f2 += paint.measureText(string2, i, i + n2);
            }
            if (f3 >= f2) {
                return false;
            }
        }
        if (f3 != f) {
            return true;
        }
        Pair<Rect, Rect> pair = PaintCompatApi14.obtainEmptyRects();
        paint.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)pair.first);
        paint.getTextBounds(string2, 0, n, (Rect)pair.second);
        return true ^ ((Rect)pair.first).equals(pair.second);
    }

    private static Pair<Rect, Rect> obtainEmptyRects() {
        Pair<Rect, Rect> pair = sRectThreadLocal.get();
        if (pair == null) {
            pair = new Pair<Rect, Rect>(new Rect(), new Rect());
            sRectThreadLocal.set(pair);
            return pair;
        }
        ((Rect)pair.first).setEmpty();
        ((Rect)pair.second).setEmpty();
        return pair;
    }
}

