/*
 * Decompiled with CFR 0_124.
 */
package android.arch.core.internal;

import android.arch.core.internal.SafeIterableMap;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class FastSafeIterableMap<K, V>
extends SafeIterableMap<K, V> {
    private HashMap<K, SafeIterableMap.Entry<K, V>> mHashMap = new HashMap();

    public Map.Entry<K, V> ceil(K k) {
        if (this.contains(k)) {
            return this.mHashMap.get(k).mPrevious;
        }
        return null;
    }

    public boolean contains(K k) {
        return this.mHashMap.containsKey(k);
    }

    @Override
    protected SafeIterableMap.Entry<K, V> get(K k) {
        return this.mHashMap.get(k);
    }

    @Override
    public V putIfAbsent(@NonNull K k, @NonNull V v) {
        SafeIterableMap.Entry<K, V> entry = this.get(k);
        if (entry != null) {
            return entry.mValue;
        }
        this.mHashMap.put(k, this.put(k, v));
        return null;
    }

    @Override
    public V remove(@NonNull K k) {
        Object v = super.remove(k);
        this.mHashMap.remove(k);
        return v;
    }
}

