/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 *  android.graphics.Rect
 */
package androidx.transition;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

class RectEvaluator
implements TypeEvaluator<Rect> {
    private Rect mRect;

    RectEvaluator() {
    }

    RectEvaluator(Rect rect) {
        this.mRect = rect;
    }

    public Rect evaluate(float f, Rect rect, Rect rect2) {
        int n = rect.left + (int)((float)(rect2.left - rect.left) * f);
        int n2 = rect.top + (int)((float)(rect2.top - rect.top) * f);
        int n3 = rect.right + (int)((float)(rect2.right - rect.right) * f);
        int n4 = rect.bottom + (int)((float)(rect2.bottom - rect.bottom) * f);
        rect = this.mRect;
        if (rect == null) {
            return new Rect(n, n2, n3, n4);
        }
        rect.set(n, n2, n3, n4);
        return this.mRect;
    }
}

