/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

public interface RequestListener<R> {
    public boolean onLoadFailed(GlideException var1, Object var2, Target<R> var3, boolean var4);

    public boolean onResourceReady(R var1, Object var2, Target<R> var3, DataSource var4, boolean var5);
}

