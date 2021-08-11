// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.support.annotation.NonNull;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.Pair;

class PaintCompatApi14
{
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "\udb3f\udffd";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal;
    
    static {
        sRectThreadLocal = new ThreadLocal<Pair<Rect, Rect>>();
    }
    
    static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        final int length = s.length();
        if (length == 1 && Character.isWhitespace(s.charAt(0))) {
            return true;
        }
        final float measureText = paint.measureText("\udb3f\udffd");
        final float measureText2 = paint.measureText("m");
        final float measureText3 = paint.measureText(s);
        if (measureText3 == 0.0f) {
            return false;
        }
        if (s.codePointCount(0, s.length()) > 1) {
            if (measureText3 > 2.0f * measureText2) {
                return false;
            }
            float n = 0.0f;
            int charCount;
            for (int i = 0; i < length; i += charCount) {
                charCount = Character.charCount(s.codePointAt(i));
                n += paint.measureText(s, i, i + charCount);
            }
            if (measureText3 >= n) {
                return false;
            }
        }
        if (measureText3 != measureText) {
            return true;
        }
        final Pair<Rect, Rect> obtainEmptyRects = obtainEmptyRects();
        paint.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)obtainEmptyRects.first);
        paint.getTextBounds(s, 0, length, (Rect)obtainEmptyRects.second);
        return true ^ obtainEmptyRects.first.equals((Object)obtainEmptyRects.second);
    }
    
    private static Pair<Rect, Rect> obtainEmptyRects() {
        final Pair<Rect, Rect> pair = PaintCompatApi14.sRectThreadLocal.get();
        if (pair == null) {
            final Pair<Rect, Rect> pair2 = new Pair<Rect, Rect>(new Rect(), new Rect());
            PaintCompatApi14.sRectThreadLocal.set(pair2);
            return pair2;
        }
        pair.first.setEmpty();
        pair.second.setEmpty();
        return pair;
    }
}
