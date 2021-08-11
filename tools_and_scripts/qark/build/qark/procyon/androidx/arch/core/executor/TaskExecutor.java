// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.arch.core.executor;

public abstract class TaskExecutor
{
    public abstract void executeOnDiskIO(final Runnable p0);
    
    public void executeOnMainThread(final Runnable runnable) {
        if (this.isMainThread()) {
            runnable.run();
            return;
        }
        this.postToMainThread(runnable);
    }
    
    public abstract boolean isMainThread();
    
    public abstract void postToMainThread(final Runnable p0);
}
