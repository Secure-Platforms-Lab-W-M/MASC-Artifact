// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import android.os.AsyncTask;

class AsyncTaskCompatHoneycomb
{
    static <Params, Progress, Result> void executeParallel(final AsyncTask<Params, Progress, Result> asyncTask, final Params... array) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])array);
    }
}
