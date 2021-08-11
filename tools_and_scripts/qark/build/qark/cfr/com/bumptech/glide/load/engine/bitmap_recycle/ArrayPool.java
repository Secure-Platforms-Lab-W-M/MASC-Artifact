/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool {
    public static final int STANDARD_BUFFER_SIZE_BYTES = 65536;

    public void clearMemory();

    public <T> T get(int var1, Class<T> var2);

    public <T> T getExact(int var1, Class<T> var2);

    public <T> void put(T var1);

    @Deprecated
    public <T> void put(T var1, Class<T> var2);

    public void trimMemory(int var1);
}

