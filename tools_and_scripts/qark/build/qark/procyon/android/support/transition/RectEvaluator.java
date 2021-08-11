// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.RequiresApi;
import android.graphics.Rect;
import android.animation.TypeEvaluator;

@RequiresApi(14)
class RectEvaluator implements TypeEvaluator<Rect>
{
    private Rect mRect;
    
    RectEvaluator() {
    }
    
    RectEvaluator(final Rect mRect) {
        this.mRect = mRect;
    }
    
    public Rect evaluate(final float n, Rect mRect, final Rect rect) {
        final int n2 = mRect.left + (int)((rect.left - mRect.left) * n);
        final int n3 = mRect.top + (int)((rect.top - mRect.top) * n);
        final int n4 = mRect.right + (int)((rect.right - mRect.right) * n);
        final int n5 = mRect.bottom + (int)((rect.bottom - mRect.bottom) * n);
        mRect = this.mRect;
        if (mRect == null) {
            return new Rect(n2, n3, n4, n5);
        }
        mRect.set(n2, n3, n4, n5);
        return this.mRect;
    }
}
