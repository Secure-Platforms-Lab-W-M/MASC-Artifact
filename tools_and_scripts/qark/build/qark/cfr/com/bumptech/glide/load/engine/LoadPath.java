/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoadPath<Data, ResourceType, Transcode> {
    private final Class<Data> dataClass;
    private final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
    private final String failureMessage;
    private final Pools.Pool<List<Throwable>> listPool;

    public LoadPath(Class<Data> class_, Class<ResourceType> class_2, Class<Transcode> class_3, List<DecodePath<Data, ResourceType, Transcode>> object, Pools.Pool<List<Throwable>> pool) {
        this.dataClass = class_;
        this.listPool = pool;
        this.decodePaths = Preconditions.checkNotEmpty(object);
        object = new StringBuilder();
        object.append("Failed LoadPath{");
        object.append(class_.getSimpleName());
        object.append("->");
        object.append(class_2.getSimpleName());
        object.append("->");
        object.append(class_3.getSimpleName());
        object.append("}");
        this.failureMessage = object.toString();
    }

    private Resource<Transcode> loadWithExceptionList(DataRewinder<Data> dataRewinder, Options options, int n, int n2, DecodePath.DecodeCallback<ResourceType> decodeCallback, List<Throwable> list) throws GlideException {
        DecodePath<Data, ResourceType, Transcode> decodePath;
        int n3 = this.decodePaths.size();
        int n4 = 0;
        Object object = null;
        do {
            decodePath = object;
            if (n4 >= n3) break;
            decodePath = this.decodePaths.get(n4);
            try {
                decodePath = decodePath.decode(dataRewinder, n, n2, options, decodeCallback);
                object = decodePath;
            }
            catch (GlideException glideException) {
                list.add(glideException);
            }
            if (object != null) {
                decodePath = object;
                break;
            }
            ++n4;
        } while (true);
        if (decodePath != null) {
            return decodePath;
        }
        throw new GlideException(this.failureMessage, new ArrayList<Throwable>(list));
    }

    public Class<Data> getDataClass() {
        return this.dataClass;
    }

    public Resource<Transcode> load(DataRewinder<Data> object, Options options, int n, int n2, DecodePath.DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        List<Throwable> list = Preconditions.checkNotNull(this.listPool.acquire());
        try {
            object = this.loadWithExceptionList((DataRewinder<Data>)object, options, n, n2, decodeCallback, list);
            return object;
        }
        finally {
            this.listPool.release(list);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LoadPath{decodePaths=");
        stringBuilder.append(Arrays.toString(this.decodePaths.toArray()));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

