/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 */
package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import com.bumptech.glide.gifdecoder.GifHeader;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public interface GifDecoder {
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    public static final int STATUS_PARTIAL_DECODE = 3;
    public static final int TOTAL_ITERATION_COUNT_FOREVER = 0;

    public void advance();

    public void clear();

    public int getByteSize();

    public int getCurrentFrameIndex();

    public ByteBuffer getData();

    public int getDelay(int var1);

    public int getFrameCount();

    public int getHeight();

    @Deprecated
    public int getLoopCount();

    public int getNetscapeLoopCount();

    public int getNextDelay();

    public Bitmap getNextFrame();

    public int getStatus();

    public int getTotalIterationCount();

    public int getWidth();

    public int read(InputStream var1, int var2);

    public int read(byte[] var1);

    public void resetFrameIndex();

    public void setData(GifHeader var1, ByteBuffer var2);

    public void setData(GifHeader var1, ByteBuffer var2, int var3);

    public void setData(GifHeader var1, byte[] var2);

    public void setDefaultBitmapConfig(Bitmap.Config var1);

    public static interface BitmapProvider {
        public Bitmap obtain(int var1, int var2, Bitmap.Config var3);

        public byte[] obtainByteArray(int var1);

        public int[] obtainIntArray(int var1);

        public void release(Bitmap var1);

        public void release(byte[] var1);

        public void release(int[] var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface GifDecodeStatus {
    }

}

