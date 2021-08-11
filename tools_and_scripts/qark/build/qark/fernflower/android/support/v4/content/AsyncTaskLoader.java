package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.v4.os.OperationCanceledException;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public abstract class AsyncTaskLoader extends Loader {
   static final boolean DEBUG = false;
   static final String TAG = "AsyncTaskLoader";
   volatile AsyncTaskLoader.LoadTask mCancellingTask;
   private final Executor mExecutor;
   Handler mHandler;
   long mLastLoadCompleteTime;
   volatile AsyncTaskLoader.LoadTask mTask;
   long mUpdateThrottle;

   public AsyncTaskLoader(Context var1) {
      this(var1, ModernAsyncTask.THREAD_POOL_EXECUTOR);
   }

   private AsyncTaskLoader(Context var1, Executor var2) {
      super(var1);
      this.mLastLoadCompleteTime = -10000L;
      this.mExecutor = var2;
   }

   public void cancelLoadInBackground() {
   }

   void dispatchOnCancelled(AsyncTaskLoader.LoadTask var1, Object var2) {
      this.onCanceled(var2);
      if (this.mCancellingTask == var1) {
         this.rollbackContentChanged();
         this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
         this.mCancellingTask = null;
         this.deliverCancellation();
         this.executePendingTask();
      }

   }

   void dispatchOnLoadComplete(AsyncTaskLoader.LoadTask var1, Object var2) {
      if (this.mTask != var1) {
         this.dispatchOnCancelled(var1, var2);
      } else if (this.isAbandoned()) {
         this.onCanceled(var2);
      } else {
         this.commitContentChanged();
         this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
         this.mTask = null;
         this.deliverResult(var2);
      }
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      super.dump(var1, var2, var3, var4);
      if (this.mTask != null) {
         var3.print(var1);
         var3.print("mTask=");
         var3.print(this.mTask);
         var3.print(" waiting=");
         var3.println(this.mTask.waiting);
      }

      if (this.mCancellingTask != null) {
         var3.print(var1);
         var3.print("mCancellingTask=");
         var3.print(this.mCancellingTask);
         var3.print(" waiting=");
         var3.println(this.mCancellingTask.waiting);
      }

      if (this.mUpdateThrottle != 0L) {
         var3.print(var1);
         var3.print("mUpdateThrottle=");
         TimeUtils.formatDuration(this.mUpdateThrottle, var3);
         var3.print(" mLastLoadCompleteTime=");
         TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), var3);
         var3.println();
      }

   }

   void executePendingTask() {
      if (this.mCancellingTask == null && this.mTask != null) {
         if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
         }

         if (this.mUpdateThrottle > 0L && SystemClock.uptimeMillis() < this.mLastLoadCompleteTime + this.mUpdateThrottle) {
            this.mTask.waiting = true;
            this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
            return;
         }

         this.mTask.executeOnExecutor(this.mExecutor, (Void[])null);
      }

   }

   public boolean isLoadInBackgroundCanceled() {
      return this.mCancellingTask != null;
   }

   public abstract Object loadInBackground();

   protected boolean onCancelLoad() {
      if (this.mTask != null) {
         if (!this.mStarted) {
            this.mContentChanged = true;
         }

         if (this.mCancellingTask != null) {
            if (this.mTask.waiting) {
               this.mTask.waiting = false;
               this.mHandler.removeCallbacks(this.mTask);
            }

            this.mTask = null;
            return false;
         } else if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
            this.mTask = null;
            return false;
         } else {
            boolean var1 = this.mTask.cancel(false);
            if (var1) {
               this.mCancellingTask = this.mTask;
               this.cancelLoadInBackground();
            }

            this.mTask = null;
            return var1;
         }
      } else {
         return false;
      }
   }

   public void onCanceled(Object var1) {
   }

   protected void onForceLoad() {
      super.onForceLoad();
      this.cancelLoad();
      this.mTask = new AsyncTaskLoader.LoadTask();
      this.executePendingTask();
   }

   protected Object onLoadInBackground() {
      return this.loadInBackground();
   }

   public void setUpdateThrottle(long var1) {
      this.mUpdateThrottle = var1;
      if (var1 != 0L) {
         this.mHandler = new Handler();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void waitForLoader() {
      AsyncTaskLoader.LoadTask var1 = this.mTask;
      if (var1 != null) {
         var1.waitForLoader();
      }

   }

   final class LoadTask extends ModernAsyncTask implements Runnable {
      private final CountDownLatch mDone = new CountDownLatch(1);
      boolean waiting;

      protected Object doInBackground(Void... var1) {
         try {
            Object var3 = AsyncTaskLoader.this.onLoadInBackground();
            return var3;
         } catch (OperationCanceledException var2) {
            if (this.isCancelled()) {
               return null;
            } else {
               throw var2;
            }
         }
      }

      protected void onCancelled(Object var1) {
         try {
            AsyncTaskLoader.this.dispatchOnCancelled(this, var1);
         } finally {
            this.mDone.countDown();
         }

      }

      protected void onPostExecute(Object var1) {
         try {
            AsyncTaskLoader.this.dispatchOnLoadComplete(this, var1);
         } finally {
            this.mDone.countDown();
         }

      }

      public void run() {
         this.waiting = false;
         AsyncTaskLoader.this.executePendingTask();
      }

      public void waitForLoader() {
         try {
            this.mDone.await();
         } catch (InterruptedException var2) {
         }
      }
   }
}
