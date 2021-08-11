/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.Preconditions;
import java.util.Collections;
import java.util.List;

public interface ModelLoader<Model, Data> {
    public LoadData<Data> buildLoadData(Model var1, int var2, int var3, Options var4);

    public boolean handles(Model var1);

    public static class LoadData<Data> {
        public final List<Key> alternateKeys;
        public final DataFetcher<Data> fetcher;
        public final Key sourceKey;

        public LoadData(Key key, DataFetcher<Data> dataFetcher) {
            this(key, Collections.emptyList(), dataFetcher);
        }

        public LoadData(Key key, List<Key> list, DataFetcher<Data> dataFetcher) {
            this.sourceKey = Preconditions.checkNotNull(key);
            this.alternateKeys = Preconditions.checkNotNull(list);
            this.fetcher = Preconditions.checkNotNull(dataFetcher);
        }
    }

}

