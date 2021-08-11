/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public final class Option<T> {
    private static final CacheKeyUpdater<Object> EMPTY_UPDATER = new CacheKeyUpdater<Object>(){

        @Override
        public void update(byte[] arrby, Object object, MessageDigest messageDigest) {
        }
    };
    private final CacheKeyUpdater<T> cacheKeyUpdater;
    private final T defaultValue;
    private final String key;
    private volatile byte[] keyBytes;

    private Option(String string2, T t, CacheKeyUpdater<T> cacheKeyUpdater) {
        this.key = Preconditions.checkNotEmpty(string2);
        this.defaultValue = t;
        this.cacheKeyUpdater = Preconditions.checkNotNull(cacheKeyUpdater);
    }

    public static <T> Option<T> disk(String string2, CacheKeyUpdater<T> cacheKeyUpdater) {
        return new Option<Object>(string2, null, cacheKeyUpdater);
    }

    public static <T> Option<T> disk(String string2, T t, CacheKeyUpdater<T> cacheKeyUpdater) {
        return new Option<T>(string2, t, cacheKeyUpdater);
    }

    private static <T> CacheKeyUpdater<T> emptyUpdater() {
        return EMPTY_UPDATER;
    }

    private byte[] getKeyBytes() {
        if (this.keyBytes == null) {
            this.keyBytes = this.key.getBytes(Key.CHARSET);
        }
        return this.keyBytes;
    }

    public static <T> Option<T> memory(String string2) {
        return new Option<Object>(string2, null, Option.<T>emptyUpdater());
    }

    public static <T> Option<T> memory(String string2, T t) {
        return new Option<T>(string2, t, Option.<T>emptyUpdater());
    }

    public boolean equals(Object object) {
        if (object instanceof Option) {
            object = (Option)object;
            return this.key.equals(object.key);
        }
        return false;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Option{key='");
        stringBuilder.append(this.key);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public void update(T t, MessageDigest messageDigest) {
        this.cacheKeyUpdater.update(this.getKeyBytes(), t, messageDigest);
    }

    public static interface CacheKeyUpdater<T> {
        public void update(byte[] var1, T var2, MessageDigest var3);
    }

}

