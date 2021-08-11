/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DecodePath<DataType, ResourceType, Transcode> {
    private static final String TAG = "DecodePath";
    private final Class<DataType> dataClass;
    private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
    private final String failureMessage;
    private final Pools.Pool<List<Throwable>> listPool;
    private final ResourceTranscoder<ResourceType, Transcode> transcoder;

    public DecodePath(Class<DataType> class_, Class<ResourceType> class_2, Class<Transcode> class_3, List<? extends ResourceDecoder<DataType, ResourceType>> object, ResourceTranscoder<ResourceType, Transcode> resourceTranscoder, Pools.Pool<List<Throwable>> pool) {
        this.dataClass = class_;
        this.decoders = object;
        this.transcoder = resourceTranscoder;
        this.listPool = pool;
        object = new StringBuilder();
        object.append("Failed DecodePath{");
        object.append(class_.getSimpleName());
        object.append("->");
        object.append(class_2.getSimpleName());
        object.append("->");
        object.append(class_3.getSimpleName());
        object.append("}");
        this.failureMessage = object.toString();
    }

    private Resource<ResourceType> decodeResource(DataRewinder<DataType> object, int n, int n2, Options options) throws GlideException {
        List<Throwable> list = Preconditions.checkNotNull(this.listPool.acquire());
        try {
            object = this.decodeResourceWithList((DataRewinder<DataType>)object, n, n2, options, list);
            return object;
        }
        finally {
            this.listPool.release(list);
        }
    }

    private Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> dataRewinder, int n, int n2, Options options, List<Throwable> list) throws GlideException {
        Resource<ResourceType> resource;
        Resource<ResourceType> resource2 = null;
        int n3 = 0;
        int n4 = this.decoders.size();
        do {
            block9 : {
                resource = resource2;
                if (n3 >= n4) break;
                ResourceDecoder<DataType, ResourceType> resourceDecoder = this.decoders.get(n3);
                resource = resource2;
                try {
                    if (resourceDecoder.handles(dataRewinder.rewindAndGet(), options)) {
                        resource = resourceDecoder.decode(dataRewinder.rewindAndGet(), n, n2, options);
                    }
                    resource2 = resource;
                    break block9;
                }
                catch (OutOfMemoryError outOfMemoryError) {
                }
                catch (RuntimeException runtimeException) {
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                if (Log.isLoggable((String)"DecodePath", (int)2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to decode data for ");
                    stringBuilder.append(resourceDecoder);
                    Log.v((String)"DecodePath", (String)stringBuilder.toString(), resource);
                }
                list.add(resource);
            }
            if (resource2 != null) {
                resource = resource2;
                break;
            }
            ++n3;
        } while (true);
        if (resource != null) {
            return resource;
        }
        throw new GlideException(this.failureMessage, new ArrayList<Throwable>(list));
    }

    public Resource<Transcode> decode(DataRewinder<DataType> object, int n, int n2, Options options, DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        object = decodeCallback.onResourceDecoded(this.decodeResource((DataRewinder<DataType>)object, n, n2, options));
        return this.transcoder.transcode((Resource<ResourceType>)object, options);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DecodePath{ dataClass=");
        stringBuilder.append(this.dataClass);
        stringBuilder.append(", decoders=");
        stringBuilder.append(this.decoders);
        stringBuilder.append(", transcoder=");
        stringBuilder.append(this.transcoder);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    static interface DecodeCallback<ResourceType> {
        public Resource<ResourceType> onResourceDecoded(Resource<ResourceType> var1);
    }

}

