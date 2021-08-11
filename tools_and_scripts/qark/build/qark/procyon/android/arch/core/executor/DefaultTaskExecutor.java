// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.core.executor;

import android.os.Looper;
import java.util.concurrent.Executors;
import android.support.annotation.Nullable;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class DefaultTaskExecutor extends TaskExecutor
{
    private ExecutorService mDiskIO;
    private final Object mLock;
    @Nullable
    private volatile Handler mMainHandler;
    
    public DefaultTaskExecutor() {
        this.mLock = new Object();
        this.mDiskIO = Executors.newFixedThreadPool(2);
    }
    
    @Override
    public void executeOnDiskIO(final Runnable runnable) {
        this.mDiskIO.execute(runnable);
    }
    
    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        Label_0037: {
            if (this.mMainHandler != null) {
                break Label_0037;
            }
            synchronized (this.mLock) {
                if (this.mMainHandler == null) {
                    this.mMainHandler = new Handler(Looper.getMainLooper());
                }
                // monitorexit(this.mLock)
                this.mMainHandler.post(runnable);
            }
        }
    }
}
