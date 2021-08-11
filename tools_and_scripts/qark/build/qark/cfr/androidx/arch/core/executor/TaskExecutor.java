/*
 * Decompiled with CFR 0_124.
 */
package androidx.arch.core.executor;

public abstract class TaskExecutor {
    public abstract void executeOnDiskIO(Runnable var1);

    public void executeOnMainThread(Runnable runnable) {
        if (this.isMainThread()) {
            runnable.run();
            return;
        }
        this.postToMainThread(runnable);
    }

    public abstract boolean isMainThread();

    public abstract void postToMainThread(Runnable var1);
}

