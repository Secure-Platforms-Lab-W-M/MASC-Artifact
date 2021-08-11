/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Process
 *  android.util.Log
 */
package android.support.v4.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus = Status.PENDING;
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable<Params, Result> mWorker;

    static {
        sThreadFactory = new ThreadFactory(){
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ModernAsyncTask #");
                stringBuilder.append(this.mCount.getAndIncrement());
                return new Thread(runnable, stringBuilder.toString());
            }
        };
        sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
        sDefaultExecutor = ModernAsyncTask.THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    }

    public ModernAsyncTask() {
        this.mWorker = new WorkerRunnable<Params, Result>(){

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Result call() throws Exception {
                Throwable throwable222;
                Result Result2;
                ModernAsyncTask.this.mTaskInvoked.set(true);
                Result Result3 = null;
                Result Result4 = Result2 = null;
                Result Result5 = Result3;
                Process.setThreadPriority((int)10);
                Result4 = Result2;
                Result5 = Result3;
                Result4 = Result2 = (Result)ModernAsyncTask.this.doInBackground(this.mParams);
                Result5 = Result2;
                Binder.flushPendingCommands();
                ModernAsyncTask.this.postResult(Result2);
                return Result2;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (Throwable throwable3) {}
                    Result4 = Result5;
                    {
                        ModernAsyncTask.this.mCancelled.set(true);
                        Result4 = Result5;
                        throw throwable3;
                    }
                }
                ModernAsyncTask.this.postResult(Result4);
                throw throwable222;
            }
        };
        this.mFuture = new FutureTask<Result>(this.mWorker){

            @Override
            protected void done() {
                try {
                    Object v = this.get();
                    ModernAsyncTask.this.postResultIfNotInvoked(v);
                    return;
                }
                catch (Throwable throwable) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", throwable);
                }
                catch (CancellationException cancellationException) {
                    ModernAsyncTask.this.postResultIfNotInvoked(null);
                    return;
                }
                catch (ExecutionException executionException) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
                }
                catch (InterruptedException interruptedException) {
                    Log.w((String)"AsyncTask", (Throwable)interruptedException);
                    return;
                }
            }
        };
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Handler getHandler() {
        synchronized (ModernAsyncTask.class) {
            if (sHandler != null) return sHandler;
            sHandler = new InternalHandler();
            return sHandler;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    public final boolean cancel(boolean bl) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(bl);
    }

    protected /* varargs */ abstract Result doInBackground(Params ... var1);

    public final /* varargs */ ModernAsyncTask<Params, Progress, Result> execute(Params ... arrParams) {
        return this.executeOnExecutor(sDefaultExecutor, arrParams);
    }

    public final /* varargs */ ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params ... arrParams) {
        if (this.mStatus != Status.PENDING) {
            switch (.$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[this.mStatus.ordinal()]) {
                default: {
                    throw new IllegalStateException("We should never reach this state");
                }
                case 2: {
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                }
                case 1: 
            }
            throw new IllegalStateException("Cannot execute task: the task is already running.");
        }
        this.mStatus = Status.RUNNING;
        this.onPreExecute();
        this.mWorker.mParams = arrParams;
        executor.execute(this.mFuture);
        return this;
    }

    void finish(Result Result2) {
        if (this.isCancelled()) {
            this.onCancelled(Result2);
        } else {
            this.onPostExecute(Result2);
        }
        this.mStatus = Status.FINISHED;
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(l, timeUnit);
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    protected void onCancelled() {
    }

    protected void onCancelled(Result Result2) {
        this.onCancelled();
    }

    protected void onPostExecute(Result Result2) {
    }

    protected void onPreExecute() {
    }

    protected /* varargs */ void onProgressUpdate(Progress ... arrProgress) {
    }

    Result postResult(Result Result2) {
        ModernAsyncTask.getHandler().obtainMessage(1, new AsyncTaskResult<Object>(this, Result2)).sendToTarget();
        return Result2;
    }

    void postResultIfNotInvoked(Result Result2) {
        if (!this.mTaskInvoked.get()) {
            this.postResult(Result2);
            return;
        }
    }

    protected final /* varargs */ void publishProgress(Progress ... arrProgress) {
        if (!this.isCancelled()) {
            ModernAsyncTask.getHandler().obtainMessage(2, new AsyncTaskResult<Progress>(this, arrProgress)).sendToTarget();
            return;
        }
    }

    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ModernAsyncTask mTask;

        /* varargs */ AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data ... arrData) {
            this.mTask = modernAsyncTask;
            this.mData = arrData;
        }
    }

    private static class InternalHandler
    extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            switch (message.what) {
                default: {
                    return;
                }
                case 2: {
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                    return;
                }
                case 1: 
            }
            asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
        }
    }

    public static enum Status {
        PENDING,
        RUNNING,
        FINISHED;
        

        private Status() {
        }
    }

    private static abstract class WorkerRunnable<Params, Result>
    implements Callable<Result> {
        Params[] mParams;

        WorkerRunnable() {
        }
    }

}

