/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher<T> {
    public void cancel();

    public void cleanup();

    public Class<T> getDataClass();

    public DataSource getDataSource();

    public void loadData(Priority var1, DataCallback<? super T> var2);

    public static interface DataCallback<T> {
        public void onDataReady(T var1);

        public void onLoadFailed(Exception var1);
    }

}

