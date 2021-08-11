/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

interface LruPoolStrategy {
    public Bitmap get(int var1, int var2, Bitmap.Config var3);

    public int getSize(Bitmap var1);

    public String logBitmap(int var1, int var2, Bitmap.Config var3);

    public String logBitmap(Bitmap var1);

    public void put(Bitmap var1);

    public Bitmap removeLast();
}

