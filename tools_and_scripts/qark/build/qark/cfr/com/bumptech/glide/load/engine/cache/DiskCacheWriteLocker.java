/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.util.Preconditions;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
    private final Map<String, WriteLock> locks = new HashMap<String, WriteLock>();
    private final WriteLockPool writeLockPool = new WriteLockPool();

    DiskCacheWriteLocker() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void acquire(String string2) {
        synchronized (this) {
            WriteLock writeLock;
            WriteLock writeLock2 = writeLock = this.locks.get(string2);
            if (writeLock == null) {
                writeLock2 = this.writeLockPool.obtain();
                this.locks.put(string2, writeLock2);
            }
            ++writeLock2.interestedThreads;
        }
        writeLock2.lock.lock();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void release(String string2) {
        synchronized (this) {
            WriteLock writeLock = Preconditions.checkNotNull(this.locks.get(string2));
            if (writeLock.interestedThreads < 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot release a lock that is not held, safeKey: ");
                stringBuilder.append(string2);
                stringBuilder.append(", interestedThreads: ");
                stringBuilder.append(writeLock.interestedThreads);
                throw new IllegalStateException(stringBuilder.toString());
            }
            --writeLock.interestedThreads;
            if (writeLock.interestedThreads == 0) {
                WriteLock writeLock2 = this.locks.remove(string2);
                if (!writeLock2.equals(writeLock)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Removed the wrong lock, expected to remove: ");
                    stringBuilder.append(writeLock);
                    stringBuilder.append(", but actually removed: ");
                    stringBuilder.append(writeLock2);
                    stringBuilder.append(", safeKey: ");
                    stringBuilder.append(string2);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                this.writeLockPool.offer(writeLock2);
            }
        }
        writeLock.lock.unlock();
    }

    private static class WriteLock {
        int interestedThreads;
        final Lock lock = new ReentrantLock();

        WriteLock() {
        }
    }

    private static class WriteLockPool {
        private static final int MAX_POOL_SIZE = 10;
        private final Queue<WriteLock> pool = new ArrayDeque<WriteLock>();

        WriteLockPool() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        WriteLock obtain() {
            WriteLock writeLock;
            Object object = this.pool;
            synchronized (object) {
                writeLock = this.pool.poll();
            }
            object = writeLock;
            if (writeLock != null) return object;
            return new WriteLock();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        void offer(WriteLock writeLock) {
            Queue<WriteLock> queue = this.pool;
            synchronized (queue) {
                if (this.pool.size() < 10) {
                    this.pool.offer(writeLock);
                }
                return;
            }
        }
    }

}

