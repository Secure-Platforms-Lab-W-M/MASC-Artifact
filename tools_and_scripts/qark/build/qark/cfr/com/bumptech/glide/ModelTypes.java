/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 */
package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.File;
import java.net.URL;

interface ModelTypes<T> {
    public T load(Bitmap var1);

    public T load(Drawable var1);

    public T load(Uri var1);

    public T load(File var1);

    public T load(Integer var1);

    public T load(Object var1);

    public T load(String var1);

    @Deprecated
    public T load(URL var1);

    public T load(byte[] var1);
}

