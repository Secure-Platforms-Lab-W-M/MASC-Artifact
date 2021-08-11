// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.util;

public final class Pools
{
    private Pools() {
    }
    
    public interface Pool<T>
    {
        T acquire();
        
        boolean release(final T p0);
    }
    
    public static class SimplePool<T> implements Pool<T>
    {
        private final Object[] mPool;
        private int mPoolSize;
        
        public SimplePool(final int n) {
            if (n > 0) {
                this.mPool = new Object[n];
                return;
            }
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        
        private boolean isInPool(final T t) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public T acquire() {
            final int mPoolSize = this.mPoolSize;
            if (mPoolSize > 0) {
                final int n = mPoolSize - 1;
                final Object[] mPool = this.mPool;
                final Object o = mPool[n];
                mPool[n] = null;
                this.mPoolSize = mPoolSize - 1;
                return (T)o;
            }
            return null;
        }
        
        @Override
        public boolean release(final T t) {
            if (this.isInPool(t)) {
                throw new IllegalStateException("Already in the pool!");
            }
            final int mPoolSize = this.mPoolSize;
            final Object[] mPool = this.mPool;
            if (mPoolSize < mPool.length) {
                mPool[mPoolSize] = t;
                this.mPoolSize = mPoolSize + 1;
                return true;
            }
            return false;
        }
    }
    
    public static class SynchronizedPool<T> extends SimplePool<T>
    {
        private final Object mLock;
        
        public SynchronizedPool(final int n) {
            super(n);
            this.mLock = new Object();
        }
        
        @Override
        public T acquire() {
            synchronized (this.mLock) {
                return super.acquire();
            }
        }
        
        @Override
        public boolean release(final T t) {
            synchronized (this.mLock) {
                return super.release(t);
            }
        }
    }
}
