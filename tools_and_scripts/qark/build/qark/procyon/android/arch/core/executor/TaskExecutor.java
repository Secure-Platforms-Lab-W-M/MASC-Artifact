// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.core.executor;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public abstract class TaskExecutor
{
    public abstract void executeOnDiskIO(@NonNull final Runnable p0);
    
    public void executeOnMainThread(@NonNull final Runnable runnable) {
        if (this.isMainThread()) {
            runnable.run();
            return;
        }
        this.postToMainThread(runnable);
    }
    
    public abstract boolean isMainThread();
    
    public abstract void postToMainThread(@NonNull final Runnable p0);
}
