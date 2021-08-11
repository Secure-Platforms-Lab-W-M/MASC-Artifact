/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import androidx.core.util.Pools;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.util.MultiClassKey;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LoadPathCache {
    private static final LoadPath<?, ?, ?> NO_PATHS_SIGNAL = new LoadPath<Object, Object, Object>(Object.class, Object.class, Object.class, Collections.singletonList(new DecodePath<Object, Object, Object>(Object.class, Object.class, Object.class, Collections.emptyList(), new UnitTranscoder(), null)), null);
    private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache = new ArrayMap();
    private final AtomicReference<MultiClassKey> keyRef = new AtomicReference();

    private MultiClassKey getKey(Class<?> class_, Class<?> class_2, Class<?> class_3) {
        MultiClassKey multiClassKey;
        MultiClassKey multiClassKey2 = multiClassKey = (MultiClassKey)this.keyRef.getAndSet(null);
        if (multiClassKey == null) {
            multiClassKey2 = new MultiClassKey();
        }
        multiClassKey2.set(class_, class_2, class_3);
        return multiClassKey2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(Class<Data> object, Class<TResource> object2, Class<Transcode> object3) {
        object2 = this.getKey(object, object2, object3);
        object = this.cache;
        synchronized (object) {
            object3 = this.cache.get(object2);
        }
        this.keyRef.set((MultiClassKey)object2);
        return object3;
    }

    public boolean isEmptyLoadPath(LoadPath<?, ?, ?> loadPath) {
        return NO_PATHS_SIGNAL.equals(loadPath);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void put(Class<?> object, Class<?> class_, Class<?> class_2, LoadPath<?, ?, ?> loadPath) {
        ArrayMap arrayMap = this.cache;
        synchronized (arrayMap) {
            ArrayMap arrayMap2 = this.cache;
            object = new MultiClassKey(object, class_, class_2);
            if (loadPath == null) {
                loadPath = NO_PATHS_SIGNAL;
            }
            arrayMap2.put((MultiClassKey)object, loadPath);
            return;
        }
    }
}

