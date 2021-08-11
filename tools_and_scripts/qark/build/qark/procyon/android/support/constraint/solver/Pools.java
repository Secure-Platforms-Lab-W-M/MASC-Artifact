// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

final class Pools
{
    private static final boolean DEBUG = false;
    
    private Pools() {
    }
    
    interface Pool<T>
    {
        T acquire();
        
        boolean release(final T p0);
        
        void releaseAll(final T[] p0, final int p1);
    }
    
    static class SimplePool<T> implements Pool<T>
    {
        private final Object[] mPool;
        private int mPoolSize;
        
        SimplePool(final int n) {
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
            final int mPoolSize = this.mPoolSize;
            final Object[] mPool = this.mPool;
            if (mPoolSize < mPool.length) {
                mPool[mPoolSize] = t;
                this.mPoolSize = mPoolSize + 1;
                return true;
            }
            return false;
        }
        
        @Override
        public void releaseAll(final T[] array, int length) {
            if (length > array.length) {
                length = array.length;
            }
            for (int i = 0; i < length; ++i) {
                final T t = array[i];
                final int mPoolSize = this.mPoolSize;
                final Object[] mPool = this.mPool;
                if (mPoolSize < mPool.length) {
                    mPool[mPoolSize] = t;
                    this.mPoolSize = mPoolSize + 1;
                }
            }
        }
    }
}
