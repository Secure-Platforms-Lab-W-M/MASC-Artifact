/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUCache
extends LinkedHashMap {
    int MAX_ENTRIES;

    public LRUCache(int n) {
        super(n + 1);
        this.MAX_ENTRIES = n;
    }

    @Override
    public void clear() {
        synchronized (this) {
            super.clear();
            return;
        }
    }

    @Override
    public boolean containsKey(Object object) {
        synchronized (this) {
            boolean bl = super.containsKey(object);
            return bl;
        }
    }

    @Override
    public boolean containsValue(Object object) {
        synchronized (this) {
            boolean bl = super.containsValue(object);
            return bl;
        }
    }

    @Override
    public Set entrySet() {
        synchronized (this) {
            Set set = super.entrySet();
            return set;
        }
    }

    @Override
    public boolean equals(Object object) {
        synchronized (this) {
            boolean bl = super.equals(object);
            return bl;
        }
    }

    @Override
    public Object get(Object object) {
        synchronized (this) {
            object = super.get(object);
            return object;
        }
    }

    @Override
    public int hashCode() {
        synchronized (this) {
            int n = super.hashCode();
            return n;
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (this) {
            boolean bl = super.isEmpty();
            return bl;
        }
    }

    @Override
    public Set keySet() {
        synchronized (this) {
            Set set = super.keySet();
            return set;
        }
    }

    @Override
    public Object put(Object object, Object object2) {
        synchronized (this) {
            object = super.put(object, object2);
            return object;
        }
    }

    @Override
    public void putAll(Map map) {
        synchronized (this) {
            super.putAll(map);
            return;
        }
    }

    @Override
    public Object remove(Object object) {
        synchronized (this) {
            object = super.remove(object);
            return object;
        }
    }

    protected boolean removeEldestEntry(Map.Entry entry) {
        if (this.size() > this.MAX_ENTRIES) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        synchronized (this) {
            int n = super.size();
            return n;
        }
    }

    @Override
    public Collection values() {
        synchronized (this) {
            Collection collection = super.values();
            return collection;
        }
    }
}

