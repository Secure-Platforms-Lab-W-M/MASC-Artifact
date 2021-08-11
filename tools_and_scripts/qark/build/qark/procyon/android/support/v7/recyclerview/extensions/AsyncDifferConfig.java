// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.recyclerview.extensions;

import android.os.Looper;
import android.os.Handler;
import java.util.concurrent.Executors;
import android.support.annotation.RestrictTo;
import android.support.v7.util.DiffUtil;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

public final class AsyncDifferConfig<T>
{
    @NonNull
    private final Executor mBackgroundThreadExecutor;
    @NonNull
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    @NonNull
    private final Executor mMainThreadExecutor;
    
    private AsyncDifferConfig(@NonNull final Executor mMainThreadExecutor, @NonNull final Executor mBackgroundThreadExecutor, @NonNull final DiffUtil.ItemCallback<T> mDiffCallback) {
        this.mMainThreadExecutor = mMainThreadExecutor;
        this.mBackgroundThreadExecutor = mBackgroundThreadExecutor;
        this.mDiffCallback = mDiffCallback;
    }
    
    @NonNull
    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }
    
    @NonNull
    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
    
    public static final class Builder<T>
    {
        private static Executor sDiffExecutor;
        private static final Object sExecutorLock;
        private static final Executor sMainThreadExecutor;
        private Executor mBackgroundThreadExecutor;
        private final DiffUtil.ItemCallback<T> mDiffCallback;
        private Executor mMainThreadExecutor;
        
        static {
            sExecutorLock = new Object();
            Builder.sDiffExecutor = null;
            sMainThreadExecutor = new MainThreadExecutor();
        }
        
        public Builder(@NonNull final DiffUtil.ItemCallback<T> mDiffCallback) {
            this.mDiffCallback = mDiffCallback;
        }
        
        @NonNull
        public AsyncDifferConfig<T> build() {
            if (this.mMainThreadExecutor == null) {
                this.mMainThreadExecutor = Builder.sMainThreadExecutor;
            }
            Label_0049: {
                if (this.mBackgroundThreadExecutor != null) {
                    break Label_0049;
                }
                synchronized (Builder.sExecutorLock) {
                    if (Builder.sDiffExecutor == null) {
                        Builder.sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                    // monitorexit(Builder.sExecutorLock)
                    this.mBackgroundThreadExecutor = Builder.sDiffExecutor;
                    return new AsyncDifferConfig<T>(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback, null);
                }
            }
        }
        
        @NonNull
        public Builder<T> setBackgroundThreadExecutor(final Executor mBackgroundThreadExecutor) {
            this.mBackgroundThreadExecutor = mBackgroundThreadExecutor;
            return this;
        }
        
        @NonNull
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Builder<T> setMainThreadExecutor(final Executor mMainThreadExecutor) {
            this.mMainThreadExecutor = mMainThreadExecutor;
            return this;
        }
        
        private static class MainThreadExecutor implements Executor
        {
            final Handler mHandler;
            
            private MainThreadExecutor() {
                this.mHandler = new Handler(Looper.getMainLooper());
            }
            
            @Override
            public void execute(@NonNull final Runnable runnable) {
                this.mHandler.post(runnable);
            }
        }
    }
}
