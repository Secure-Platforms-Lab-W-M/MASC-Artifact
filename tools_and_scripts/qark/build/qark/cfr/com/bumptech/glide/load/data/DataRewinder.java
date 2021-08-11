/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data;

import java.io.IOException;

public interface DataRewinder<T> {
    public void cleanup();

    public T rewindAndGet() throws IOException;

    public static interface Factory<T> {
        public DataRewinder<T> build(T var1);

        public Class<T> getDataClass();
    }

}

