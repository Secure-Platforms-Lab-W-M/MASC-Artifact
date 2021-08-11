/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.util.MultiClassKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModelToResourceClassCache {
    private final ArrayMap<MultiClassKey, List<Class<?>>> registeredResourceClassCache = new ArrayMap();
    private final AtomicReference<MultiClassKey> resourceClassKeyRef = new AtomicReference();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void clear() {
        ArrayMap arrayMap = this.registeredResourceClassCache;
        synchronized (arrayMap) {
            this.registeredResourceClassCache.clear();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Class<?>> get(Class<?> object, Class<?> object2, Class<?> object3) {
        MultiClassKey multiClassKey = this.resourceClassKeyRef.getAndSet(null);
        if (multiClassKey == null) {
            object = new MultiClassKey(object, object2, object3);
        } else {
            multiClassKey.set(object, object2, object3);
            object = multiClassKey;
        }
        object2 = this.registeredResourceClassCache;
        synchronized (object2) {
            object3 = this.registeredResourceClassCache.get(object);
        }
        this.resourceClassKeyRef.set((MultiClassKey)object);
        return object3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void put(Class<?> class_, Class<?> class_2, Class<?> class_3, List<Class<?>> list) {
        ArrayMap arrayMap = this.registeredResourceClassCache;
        synchronized (arrayMap) {
            this.registeredResourceClassCache.put(new MultiClassKey(class_, class_2, class_3), list);
            return;
        }
    }
}

