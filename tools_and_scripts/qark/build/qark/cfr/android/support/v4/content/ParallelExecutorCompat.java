/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.AsyncTask
 */
package android.support.v4.content;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

@Deprecated
public final class ParallelExecutorCompat {
    private ParallelExecutorCompat() {
    }

    @Deprecated
    public static Executor getParallelExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }
}

