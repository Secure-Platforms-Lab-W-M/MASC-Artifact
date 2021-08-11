// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import java.io.IOException;
import android.graphics.BitmapFactory$Options;

public abstract class RequestHandler
{
    static void calculateInSampleSize(int n, int n2, final int n3, final int n4, final BitmapFactory$Options bitmapFactory$Options, final Request request) {
        int inSampleSize = 1;
        if (n4 > n2 || n3 > n) {
            if (n2 == 0) {
                inSampleSize = (int)Math.floor(n3 / (float)n);
            }
            else if (n == 0) {
                inSampleSize = (int)Math.floor(n4 / (float)n2);
            }
            else {
                n2 = (int)Math.floor(n4 / (float)n2);
                n = (int)Math.floor(n3 / (float)n);
                if (request.centerInside) {
                    inSampleSize = Math.max(n2, n);
                }
                else {
                    inSampleSize = Math.min(n2, n);
                }
            }
        }
        bitmapFactory$Options.inSampleSize = inSampleSize;
        bitmapFactory$Options.inJustDecodeBounds = false;
    }
    
    static void calculateInSampleSize(final int n, final int n2, final BitmapFactory$Options bitmapFactory$Options, final Request request) {
        calculateInSampleSize(n, n2, bitmapFactory$Options.outWidth, bitmapFactory$Options.outHeight, bitmapFactory$Options, request);
    }
    
    static BitmapFactory$Options createBitmapOptions(final Request request) {
        final boolean hasSize = request.hasSize();
        boolean b;
        if (request.config != null) {
            b = true;
        }
        else {
            b = false;
        }
        BitmapFactory$Options bitmapFactory$Options = null;
        if (hasSize || b) {
            final BitmapFactory$Options bitmapFactory$Options2 = new BitmapFactory$Options();
            bitmapFactory$Options2.inJustDecodeBounds = hasSize;
            bitmapFactory$Options = bitmapFactory$Options2;
            if (b) {
                bitmapFactory$Options2.inPreferredConfig = request.config;
                bitmapFactory$Options = bitmapFactory$Options2;
            }
        }
        return bitmapFactory$Options;
    }
    
    static boolean requiresInSampleSize(final BitmapFactory$Options bitmapFactory$Options) {
        return bitmapFactory$Options != null && bitmapFactory$Options.inJustDecodeBounds;
    }
    
    public abstract boolean canHandleRequest(final Request p0);
    
    int getRetryCount() {
        return 0;
    }
    
    public abstract Result load(final Request p0, final int p1) throws IOException;
    
    boolean shouldRetry(final boolean b, final NetworkInfo networkInfo) {
        return false;
    }
    
    boolean supportsReplay() {
        return false;
    }
    
    public static final class Result
    {
        private final Bitmap bitmap;
        private final int exifOrientation;
        private final Picasso.LoadedFrom loadedFrom;
        private final InputStream stream;
        
        public Result(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
            this(Utils.checkNotNull(bitmap, "bitmap == null"), null, loadedFrom, 0);
        }
        
        Result(final Bitmap bitmap, final InputStream stream, final Picasso.LoadedFrom loadedFrom, final int exifOrientation) {
            boolean b = true;
            boolean b2;
            if (bitmap != null) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            if (stream == null) {
                b = false;
            }
            if (!(b ^ b2)) {
                throw new AssertionError();
            }
            this.bitmap = bitmap;
            this.stream = stream;
            this.loadedFrom = Utils.checkNotNull(loadedFrom, "loadedFrom == null");
            this.exifOrientation = exifOrientation;
        }
        
        public Result(final InputStream inputStream, final Picasso.LoadedFrom loadedFrom) {
            this(null, Utils.checkNotNull(inputStream, "stream == null"), loadedFrom, 0);
        }
        
        public Bitmap getBitmap() {
            return this.bitmap;
        }
        
        int getExifOrientation() {
            return this.exifOrientation;
        }
        
        public Picasso.LoadedFrom getLoadedFrom() {
            return this.loadedFrom;
        }
        
        public InputStream getStream() {
            return this.stream;
        }
    }
}
