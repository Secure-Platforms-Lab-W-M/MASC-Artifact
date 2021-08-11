/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Handler
 *  android.os.Looper
 */
package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFillRunner;
import com.bumptech.glide.load.engine.prefill.PreFillQueue;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public final class BitmapPreFiller {
    private final BitmapPool bitmapPool;
    private BitmapPreFillRunner current;
    private final DecodeFormat defaultFormat;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final MemoryCache memoryCache;

    public BitmapPreFiller(MemoryCache memoryCache, BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.defaultFormat = decodeFormat;
    }

    private static int getSizeInBytes(PreFillType preFillType) {
        return Util.getBitmapByteSize(preFillType.getWidth(), preFillType.getHeight(), preFillType.getConfig());
    }

    /* varargs */ PreFillQueue generateAllocationOrder(PreFillType ... arrpreFillType) {
        int n;
        long l = this.memoryCache.getMaxSize();
        long l2 = this.memoryCache.getCurrentSize();
        long l3 = this.bitmapPool.getMaxSize();
        int n2 = 0;
        int n3 = arrpreFillType.length;
        int n4 = 0;
        for (n = 0; n < n3; ++n) {
            n2 += arrpreFillType[n].getWeight();
        }
        float f = (float)(l - l2 + l3) / (float)n2;
        HashMap<PreFillType, Integer> hashMap = new HashMap<PreFillType, Integer>();
        n2 = arrpreFillType.length;
        for (n = n4; n < n2; ++n) {
            PreFillType preFillType = arrpreFillType[n];
            hashMap.put(preFillType, Math.round((float)preFillType.getWeight() * f) / BitmapPreFiller.getSizeInBytes(preFillType));
        }
        return new PreFillQueue(hashMap);
    }

    public /* varargs */ void preFill(PreFillType.Builder ... object) {
        BitmapPreFillRunner bitmapPreFillRunner = this.current;
        if (bitmapPreFillRunner != null) {
            bitmapPreFillRunner.cancel();
        }
        PreFillType[] arrpreFillType = new PreFillType[object.length];
        for (int i = 0; i < object.length; ++i) {
            PreFillType.Builder builder = object[i];
            if (builder.getConfig() == null) {
                bitmapPreFillRunner = this.defaultFormat == DecodeFormat.PREFER_ARGB_8888 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                builder.setConfig((Bitmap.Config)bitmapPreFillRunner);
            }
            arrpreFillType[i] = builder.build();
        }
        object = this.generateAllocationOrder(arrpreFillType);
        this.current = object = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, (PreFillQueue)object);
        this.handler.post((Runnable)object);
    }
}

