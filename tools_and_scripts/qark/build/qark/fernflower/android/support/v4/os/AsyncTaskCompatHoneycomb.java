package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.support.annotation.RequiresApi;

@TargetApi(11)
@RequiresApi(11)
class AsyncTaskCompatHoneycomb {
   static void executeParallel(AsyncTask var0, Object... var1) {
      var0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, var1);
   }
}
