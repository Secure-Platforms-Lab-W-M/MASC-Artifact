/*
 * Decompiled with CFR 0_124.
 */
package android.arch.core.executor;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class TaskExecutor {
    public abstract void executeOnDiskIO(@NonNull Runnable var1);

    public void executeOnMainThread(@NonNull Runnable runnable) {
        if (this.isMainThread()) {
            runnable.run();
            return;
        }
        this.postToMainThread(runnable);
    }

    public abstract boolean isMainThread();

    public abstract void postToMainThread(@NonNull Runnable var1);
}

