// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import android.os.Build$VERSION;
import android.graphics.Bitmap;

public final class BitmapCompat
{
    private BitmapCompat() {
    }
    
    public static int getAllocationByteCount(final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }
    
    public static boolean hasMipMap(final Bitmap bitmap) {
        return Build$VERSION.SDK_INT >= 18 && bitmap.hasMipMap();
    }
    
    public static void setHasMipMap(final Bitmap bitmap, final boolean hasMipMap) {
        if (Build$VERSION.SDK_INT >= 18) {
            bitmap.setHasMipMap(hasMipMap);
        }
    }
}
