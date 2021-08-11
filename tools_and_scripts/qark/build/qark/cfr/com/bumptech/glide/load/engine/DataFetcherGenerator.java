/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;

interface DataFetcherGenerator {
    public void cancel();

    public boolean startNext();

    public static interface FetcherReadyCallback {
        public void onDataFetcherFailed(Key var1, Exception var2, DataFetcher<?> var3, DataSource var4);

        public void onDataFetcherReady(Key var1, Object var2, DataFetcher<?> var3, DataSource var4, Key var5);

        public void reschedule();
    }

}

