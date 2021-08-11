// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

public class LRUCache extends LinkedHashMap
{
    int MAX_ENTRIES;
    
    public LRUCache(final int max_ENTRIES) {
        super(max_ENTRIES + 1);
        this.MAX_ENTRIES = max_ENTRIES;
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            super.clear();
        }
    }
    
    @Override
    public boolean containsKey(final Object o) {
        synchronized (this) {
            return super.containsKey(o);
        }
    }
    
    @Override
    public boolean containsValue(final Object o) {
        synchronized (this) {
            return super.containsValue(o);
        }
    }
    
    @Override
    public Set entrySet() {
        synchronized (this) {
            return super.entrySet();
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        synchronized (this) {
            return super.equals(o);
        }
    }
    
    @Override
    public Object get(Object value) {
        synchronized (this) {
            value = super.get(value);
            return value;
        }
    }
    
    @Override
    public int hashCode() {
        synchronized (this) {
            return super.hashCode();
        }
    }
    
    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return super.isEmpty();
        }
    }
    
    @Override
    public Set keySet() {
        synchronized (this) {
            return super.keySet();
        }
    }
    
    @Override
    public Object put(Object put, final Object o) {
        synchronized (this) {
            put = super.put(put, o);
            return put;
        }
    }
    
    @Override
    public void putAll(final Map map) {
        synchronized (this) {
            super.putAll(map);
        }
    }
    
    @Override
    public Object remove(Object remove) {
        synchronized (this) {
            remove = super.remove(remove);
            return remove;
        }
    }
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry entry) {
        return this.size() > this.MAX_ENTRIES;
    }
    
    @Override
    public int size() {
        synchronized (this) {
            return super.size();
        }
    }
    
    @Override
    public Collection values() {
        synchronized (this) {
            return super.values();
        }
    }
}
