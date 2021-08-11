/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public interface ResourceDecoder<T, Z> {
    public Resource<Z> decode(T var1, int var2, int var3, Options var4) throws IOException;

    public boolean handles(T var1, Options var2) throws IOException;
}

