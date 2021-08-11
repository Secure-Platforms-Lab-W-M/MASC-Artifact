/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

public final class BitmapCompat {
    static final BitmapCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 19 ? new BitmapCompatApi19Impl() : (Build.VERSION.SDK_INT >= 18 ? new BitmapCompatApi18Impl() : new BitmapCompatBaseImpl());

    private BitmapCompat() {
    }

    public static int getAllocationByteCount(Bitmap bitmap) {
        return IMPL.getAllocationByteCount(bitmap);
    }

    public static boolean hasMipMap(Bitmap bitmap) {
        return IMPL.hasMipMap(bitmap);
    }

    public static void setHasMipMap(Bitmap bitmap, boolean bl) {
        IMPL.setHasMipMap(bitmap, bl);
    }

    @RequiresApi(value=18)
    static class BitmapCompatApi18Impl
    extends BitmapCompatBaseImpl {
        BitmapCompatApi18Impl() {
        }

        @Override
        public boolean hasMipMap(Bitmap bitmap) {
            return bitmap.hasMipMap();
        }

        @Override
        public void setHasMipMap(Bitmap bitmap, boolean bl) {
            bitmap.setHasMipMap(bl);
        }
    }

    @RequiresApi(value=19)
    static class BitmapCompatApi19Impl
    extends BitmapCompatApi18Impl {
        BitmapCompatApi19Impl() {
        }

        @Override
        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getAllocationByteCount();
        }
    }

    static class BitmapCompatBaseImpl {
        BitmapCompatBaseImpl() {
        }

        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getByteCount();
        }

        public boolean hasMipMap(Bitmap bitmap) {
            return false;
        }

        public void setHasMipMap(Bitmap bitmap, boolean bl) {
        }
    }

}

