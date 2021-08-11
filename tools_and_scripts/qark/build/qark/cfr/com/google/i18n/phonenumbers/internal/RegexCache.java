/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexCache {
    private LRUCache<String, Pattern> cache;

    public RegexCache(int n) {
        this.cache = new LRUCache(n);
    }

    boolean containsRegex(String string2) {
        return this.cache.containsKey(string2);
    }

    public Pattern getPatternForRegex(String string2) {
        Pattern pattern;
        Pattern pattern2 = pattern = this.cache.get(string2);
        if (pattern == null) {
            pattern2 = Pattern.compile(string2);
            this.cache.put(string2, pattern2);
        }
        return pattern2;
    }

    private static class LRUCache<K, V> {
        private LinkedHashMap<K, V> map;
        private int size;

        public LRUCache(int n) {
            this.size = n;
            this.map = new LinkedHashMap<K, V>(n * 4 / 3 + 1, 0.75f, true){

                @Override
                protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
                    if (this.size() > LRUCache.this.size) {
                        return true;
                    }
                    return false;
                }
            };
        }

        public boolean containsKey(K k) {
            synchronized (this) {
                boolean bl = this.map.containsKey(k);
                return bl;
            }
        }

        public V get(K object) {
            synchronized (this) {
                object = this.map.get(object);
                return (V)object;
            }
        }

        public void put(K k, V v) {
            synchronized (this) {
                this.map.put(k, v);
                return;
            }
        }

    }

}

