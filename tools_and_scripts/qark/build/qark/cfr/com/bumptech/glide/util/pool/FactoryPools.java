/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.util.pool;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
    private static final int DEFAULT_POOL_SIZE = 20;
    private static final Resetter<Object> EMPTY_RESETTER = new Resetter<Object>(){

        @Override
        public void reset(Object object) {
        }
    };
    private static final String TAG = "FactoryPools";

    private FactoryPools() {
    }

    private static <T extends Poolable> Pools.Pool<T> build(Pools.Pool<T> pool, Factory<T> factory) {
        return FactoryPools.build(pool, factory, FactoryPools.<T>emptyResetter());
    }

    private static <T> Pools.Pool<T> build(Pools.Pool<T> pool, Factory<T> factory, Resetter<T> resetter) {
        return new FactoryPool<T>(pool, factory, resetter);
    }

    private static <T> Resetter<T> emptyResetter() {
        return EMPTY_RESETTER;
    }

    public static <T extends Poolable> Pools.Pool<T> simple(int n, Factory<T> factory) {
        return FactoryPools.build(new Pools.SimplePool(n), factory);
    }

    public static <T extends Poolable> Pools.Pool<T> threadSafe(int n, Factory<T> factory) {
        return FactoryPools.build(new Pools.SynchronizedPool(n), factory);
    }

    public static <T> Pools.Pool<List<T>> threadSafeList() {
        return FactoryPools.threadSafeList(20);
    }

    public static <T> Pools.Pool<List<T>> threadSafeList(int n) {
        return FactoryPools.build(new Pools.SynchronizedPool(n), new Factory<List<T>>(){

            @Override
            public List<T> create() {
                return new ArrayList();
            }
        }, new Resetter<List<T>>(){

            @Override
            public void reset(List<T> list) {
                list.clear();
            }
        });
    }

    public static interface Factory<T> {
        public T create();
    }

    private static final class FactoryPool<T>
    implements Pools.Pool<T> {
        private final Factory<T> factory;
        private final Pools.Pool<T> pool;
        private final Resetter<T> resetter;

        FactoryPool(Pools.Pool<T> pool, Factory<T> factory, Resetter<T> resetter) {
            this.pool = pool;
            this.factory = factory;
            this.resetter = resetter;
        }

        @Override
        public T acquire() {
            T t = this.pool.acquire();
            Object object = t;
            if (t == null) {
                t = this.factory.create();
                object = t;
                if (Log.isLoggable((String)"FactoryPools", (int)2)) {
                    object = new StringBuilder();
                    object.append("Created new ");
                    object.append(t.getClass());
                    Log.v((String)"FactoryPools", (String)object.toString());
                    object = t;
                }
            }
            if (object instanceof Poolable) {
                ((Poolable)object).getVerifier().setRecycled(false);
            }
            return (T)object;
        }

        @Override
        public boolean release(T t) {
            if (t instanceof Poolable) {
                ((Poolable)t).getVerifier().setRecycled(true);
            }
            this.resetter.reset(t);
            return this.pool.release(t);
        }
    }

    public static interface Poolable {
        public StateVerifier getVerifier();
    }

    public static interface Resetter<T> {
        public void reset(T var1);
    }

}

