// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.support.annotation.NonNull;
import android.graphics.Paint;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class PaintCompatApi23
{
    static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        return paint.hasGlyph(s);
    }
}
