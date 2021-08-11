/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;

public interface MemoryCache {
    public void clearMemory();

    public long getCurrentSize();

    public long getMaxSize();

    public Resource<?> put(Key var1, Resource<?> var2);

    public Resource<?> remove(Key var1);

    public void setResourceRemovedListener(ResourceRemovedListener var1);

    public void setSizeMultiplier(float var1);

    public void trimMemory(int var1);

    public static interface ResourceRemovedListener {
        public void onResourceRemoved(Resource<?> var1);
    }

}

