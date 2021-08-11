/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool<T extends Poolable> {
    private static final int MAX_SIZE = 20;
    private final Queue<T> keyPool = Util.createQueue(20);

    BaseKeyPool() {
    }

    abstract T create();

    T get() {
        Poolable poolable;
        Poolable poolable2 = poolable = (Poolable)this.keyPool.poll();
        if (poolable == null) {
            poolable2 = this.create();
        }
        return (T)poolable2;
    }

    public void offer(T t) {
        if (this.keyPool.size() < 20) {
            this.keyPool.offer(t);
        }
    }
}

