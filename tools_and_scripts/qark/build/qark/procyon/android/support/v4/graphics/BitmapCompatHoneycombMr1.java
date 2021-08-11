// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(12)
@RequiresApi(12)
class BitmapCompatHoneycombMr1
{
    static int getAllocationByteCount(final Bitmap bitmap) {
        return bitmap.getByteCount();
    }
}
