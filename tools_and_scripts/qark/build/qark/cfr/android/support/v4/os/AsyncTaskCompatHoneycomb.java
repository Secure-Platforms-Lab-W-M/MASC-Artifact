/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.AsyncTask
 */
package android.support.v4.os;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

class AsyncTaskCompatHoneycomb {
    AsyncTaskCompatHoneycomb() {
    }

    static /* varargs */ <Params, Progress, Result> void executeParallel(AsyncTask<Params, Progress, Result> asyncTask, Params ... arrParams) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])arrParams);
    }
}

