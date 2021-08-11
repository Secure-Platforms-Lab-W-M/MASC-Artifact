/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;

public abstract class DiskCacheStrategy {
    public static final DiskCacheStrategy ALL = new DiskCacheStrategy(){

        @Override
        public boolean decodeCachedData() {
            return true;
        }

        @Override
        public boolean decodeCachedResource() {
            return true;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            if (dataSource == DataSource.REMOTE) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isResourceCacheable(boolean bl, DataSource dataSource, EncodeStrategy encodeStrategy) {
            if (dataSource != DataSource.RESOURCE_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE) {
                return true;
            }
            return false;
        }
    };
    public static final DiskCacheStrategy AUTOMATIC;
    public static final DiskCacheStrategy DATA;
    public static final DiskCacheStrategy NONE;
    public static final DiskCacheStrategy RESOURCE;

    static {
        NONE = new DiskCacheStrategy(){

            @Override
            public boolean decodeCachedData() {
                return false;
            }

            @Override
            public boolean decodeCachedResource() {
                return false;
            }

            @Override
            public boolean isDataCacheable(DataSource dataSource) {
                return false;
            }

            @Override
            public boolean isResourceCacheable(boolean bl, DataSource dataSource, EncodeStrategy encodeStrategy) {
                return false;
            }
        };
        DATA = new DiskCacheStrategy(){

            @Override
            public boolean decodeCachedData() {
                return true;
            }

            @Override
            public boolean decodeCachedResource() {
                return false;
            }

            @Override
            public boolean isDataCacheable(DataSource dataSource) {
                if (dataSource != DataSource.DATA_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean isResourceCacheable(boolean bl, DataSource dataSource, EncodeStrategy encodeStrategy) {
                return false;
            }
        };
        RESOURCE = new DiskCacheStrategy(){

            @Override
            public boolean decodeCachedData() {
                return false;
            }

            @Override
            public boolean decodeCachedResource() {
                return true;
            }

            @Override
            public boolean isDataCacheable(DataSource dataSource) {
                return false;
            }

            @Override
            public boolean isResourceCacheable(boolean bl, DataSource dataSource, EncodeStrategy encodeStrategy) {
                if (dataSource != DataSource.RESOURCE_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE) {
                    return true;
                }
                return false;
            }
        };
        AUTOMATIC = new DiskCacheStrategy(){

            @Override
            public boolean decodeCachedData() {
                return true;
            }

            @Override
            public boolean decodeCachedResource() {
                return true;
            }

            @Override
            public boolean isDataCacheable(DataSource dataSource) {
                if (dataSource == DataSource.REMOTE) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean isResourceCacheable(boolean bl, DataSource dataSource, EncodeStrategy encodeStrategy) {
                if ((bl && dataSource == DataSource.DATA_DISK_CACHE || dataSource == DataSource.LOCAL) && encodeStrategy == EncodeStrategy.TRANSFORMED) {
                    return true;
                }
                return false;
            }
        };
    }

    public abstract boolean decodeCachedData();

    public abstract boolean decodeCachedResource();

    public abstract boolean isDataCacheable(DataSource var1);

    public abstract boolean isResourceCacheable(boolean var1, DataSource var2, EncodeStrategy var3);

}

