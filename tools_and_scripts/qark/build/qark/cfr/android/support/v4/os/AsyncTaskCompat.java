/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.AsyncTask
 */
package android.support.v4.os;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

@Deprecated
public final class AsyncTaskCompat {
    private AsyncTaskCompat() {
    }

    @Deprecated
    public static /* varargs */ <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(AsyncTask<Params, Progress, Result> asyncTask, Params ... arrParams) {
        if (asyncTask != null) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])arrParams);
            return asyncTask;
        }
        throw new IllegalArgumentException("task can not be null");
    }
}

