/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceTranscoder<Z, R> {
    public Resource<R> transcode(Resource<Z> var1, Options var2);
}

