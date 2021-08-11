// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.core.executor;

import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ArchTaskExecutor extends TaskExecutor
{
    @NonNull
    private static final Executor sIOThreadExecutor;
    private static volatile ArchTaskExecutor sInstance;
    @NonNull
    private static final Executor sMainThreadExecutor;
    @NonNull
    private TaskExecutor mDefaultTaskExecutor;
    @NonNull
    private TaskExecutor mDelegate;
    
    static {
        sMainThreadExecutor = new Executor() {
            @Override
            public void execute(final Runnable runnable) {
                ArchTaskExecutor.getInstance().postToMainThread(runnable);
            }
        };
        sIOThreadExecutor = new Executor() {
            @Override
            public void execute(final Runnable runnable) {
                ArchTaskExecutor.getInstance().executeOnDiskIO(runnable);
            }
        };
    }
    
    private ArchTaskExecutor() {
        this.mDefaultTaskExecutor = new DefaultTaskExecutor();
        this.mDelegate = this.mDefaultTaskExecutor;
    }
    
    @NonNull
    public static Executor getIOThreadExecutor() {
        return ArchTaskExecutor.sIOThreadExecutor;
    }
    
    @NonNull
    public static ArchTaskExecutor getInstance() {
        if (ArchTaskExecutor.sInstance != null) {
            return ArchTaskExecutor.sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (ArchTaskExecutor.sInstance == null) {
                ArchTaskExecutor.sInstance = new ArchTaskExecutor();
            }
            return ArchTaskExecutor.sInstance;
        }
    }
    
    @NonNull
    public static Executor getMainThreadExecutor() {
        return ArchTaskExecutor.sMainThreadExecutor;
    }
    
    @Override
    public void executeOnDiskIO(final Runnable runnable) {
        this.mDelegate.executeOnDiskIO(runnable);
    }
    
    @Override
    public boolean isMainThread() {
        return this.mDelegate.isMainThread();
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        this.mDelegate.postToMainThread(runnable);
    }
    
    public void setDelegate(@Nullable final TaskExecutor taskExecutor) {
        TaskExecutor mDefaultTaskExecutor = taskExecutor;
        if (taskExecutor == null) {
            mDefaultTaskExecutor = this.mDefaultTaskExecutor;
        }
        this.mDelegate = mDefaultTaskExecutor;
    }
}
