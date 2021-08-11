// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(18)
@RequiresApi(18)
class BitmapCompatJellybeanMR2
{
    public static boolean hasMipMap(final Bitmap bitmap) {
        return bitmap.hasMipMap();
    }
    
    public static void setHasMipMap(final Bitmap bitmap, final boolean hasMipMap) {
        bitmap.setHasMipMap(hasMipMap);
    }
}
