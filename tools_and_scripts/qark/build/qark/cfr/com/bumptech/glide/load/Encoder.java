/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.Options;
import java.io.File;

public interface Encoder<T> {
    public boolean encode(T var1, File var2, Options var3);
}

