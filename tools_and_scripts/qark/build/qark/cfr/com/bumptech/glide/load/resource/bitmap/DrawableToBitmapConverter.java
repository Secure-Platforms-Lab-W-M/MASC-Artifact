/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import java.util.concurrent.locks.Lock;

final class DrawableToBitmapConverter {
    private static final BitmapPool NO_RECYCLE_BITMAP_POOL = new BitmapPoolAdapter(){

        @Override
        public void put(Bitmap bitmap) {
        }
    };
    private static final String TAG = "DrawableToBitmap";

    private DrawableToBitmapConverter() {
    }

    static Resource<Bitmap> convert(BitmapPool bitmapPool, Drawable drawable2, int n, int n2) {
        Drawable drawable3 = drawable2.getCurrent();
        drawable2 = null;
        boolean bl = false;
        if (drawable3 instanceof BitmapDrawable) {
            drawable2 = ((BitmapDrawable)drawable3).getBitmap();
        } else if (!(drawable3 instanceof Animatable)) {
            drawable2 = DrawableToBitmapConverter.drawToBitmap(bitmapPool, drawable3, n, n2);
            bl = true;
        }
        if (!bl) {
            bitmapPool = NO_RECYCLE_BITMAP_POOL;
        }
        return BitmapResource.obtain((Bitmap)drawable2, bitmapPool);
    }

    private static Bitmap drawToBitmap(BitmapPool object, Drawable drawable2, int n, int n2) {
        if (n == Integer.MIN_VALUE && drawable2.getIntrinsicWidth() <= 0) {
            if (Log.isLoggable((String)"DrawableToBitmap", (int)5)) {
                object = new StringBuilder();
                object.append("Unable to draw ");
                object.append((Object)drawable2);
                object.append(" to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic width");
                Log.w((String)"DrawableToBitmap", (String)object.toString());
            }
            return null;
        }
        if (n2 == Integer.MIN_VALUE && drawable2.getIntrinsicHeight() <= 0) {
            if (Log.isLoggable((String)"DrawableToBitmap", (int)5)) {
                object = new StringBuilder();
                object.append("Unable to draw ");
                object.append((Object)drawable2);
                object.append(" to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic height");
                Log.w((String)"DrawableToBitmap", (String)object.toString());
            }
            return null;
        }
        if (drawable2.getIntrinsicWidth() > 0) {
            n = drawable2.getIntrinsicWidth();
        }
        if (drawable2.getIntrinsicHeight() > 0) {
            n2 = drawable2.getIntrinsicHeight();
        }
        Lock lock = TransformationUtils.getBitmapDrawableLock();
        lock.lock();
        object = object.get(n, n2, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas((Bitmap)object);
            drawable2.setBounds(0, 0, n, n2);
            drawable2.draw(canvas);
            canvas.setBitmap(null);
            return object;
        }
        finally {
            lock.unlock();
        }
    }

}

