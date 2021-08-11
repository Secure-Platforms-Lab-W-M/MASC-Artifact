/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public interface DiskCache {
    public void clear();

    public void delete(Key var1);

    public File get(Key var1);

    public void put(Key var1, Writer var2);

    public static interface Factory {
        public static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
        public static final int DEFAULT_DISK_CACHE_SIZE = 262144000;

        public DiskCache build();
    }

    public static interface Writer {
        public boolean write(File var1);
    }

}

