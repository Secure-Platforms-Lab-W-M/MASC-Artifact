// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.core.internal;

import android.support.annotation.NonNull;
import java.util.Map;
import java.util.HashMap;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V>
{
    private HashMap<K, Entry<K, V>> mHashMap;
    
    public FastSafeIterableMap() {
        this.mHashMap = new HashMap<K, Entry<K, V>>();
    }
    
    public Map.Entry<K, V> ceil(final K k) {
        if (this.contains(k)) {
            return this.mHashMap.get(k).mPrevious;
        }
        return null;
    }
    
    public boolean contains(final K k) {
        return this.mHashMap.containsKey(k);
    }
    
    @Override
    protected Entry<K, V> get(final K k) {
        return this.mHashMap.get(k);
    }
    
    @Override
    public V putIfAbsent(@NonNull final K k, @NonNull final V v) {
        final Entry<K, V> value = this.get(k);
        if (value != null) {
            return value.mValue;
        }
        this.mHashMap.put(k, this.put(k, v));
        return null;
    }
    
    @Override
    public V remove(@NonNull final K k) {
        final V remove = super.remove(k);
        this.mHashMap.remove(k);
        return remove;
    }
}
