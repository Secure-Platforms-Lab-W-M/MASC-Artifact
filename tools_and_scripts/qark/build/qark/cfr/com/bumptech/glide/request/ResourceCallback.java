/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceCallback {
    public Object getLock();

    public void onLoadFailed(GlideException var1);

    public void onResourceReady(Resource<?> var1, DataSource var2);
}

