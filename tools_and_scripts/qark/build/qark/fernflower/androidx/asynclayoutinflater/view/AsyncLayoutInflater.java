package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pools;
import java.util.concurrent.ArrayBlockingQueue;

public final class AsyncLayoutInflater {
   private static final String TAG = "AsyncLayoutInflater";
   Handler mHandler;
   private Callback mHandlerCallback = new Callback() {
      public boolean handleMessage(Message var1) {
         AsyncLayoutInflater.InflateRequest var2 = (AsyncLayoutInflater.InflateRequest)var1.obj;
         if (var2.view == null) {
            var2.view = AsyncLayoutInflater.this.mInflater.inflate(var2.resid, var2.parent, false);
         }

         var2.callback.onInflateFinished(var2.view, var2.resid, var2.parent);
         AsyncLayoutInflater.this.mInflateThread.releaseRequest(var2);
         return true;
      }
   };
   AsyncLayoutInflater.InflateThread mInflateThread;
   LayoutInflater mInflater;

   public AsyncLayoutInflater(Context var1) {
      this.mInflater = new AsyncLayoutInflater.BasicInflater(var1);
      this.mHandler = new Handler(this.mHandlerCallback);
      this.mInflateThread = AsyncLayoutInflater.InflateThread.getInstance();
   }

   public void inflate(int var1, ViewGroup var2, AsyncLayoutInflater.OnInflateFinishedListener var3) {
      if (var3 != null) {
         AsyncLayoutInflater.InflateRequest var4 = this.mInflateThread.obtainRequest();
         var4.inflater = this;
         var4.resid = var1;
         var4.parent = var2;
         var4.callback = var3;
         this.mInflateThread.enqueue(var4);
      } else {
         throw new NullPointerException("callback argument may not be null!");
      }
   }

   private static class BasicInflater extends LayoutInflater {
      private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.webkit.", "android.app."};

      BasicInflater(Context var1) {
         super(var1);
      }

      public LayoutInflater cloneInContext(Context var1) {
         return new AsyncLayoutInflater.BasicInflater(var1);
      }

      protected View onCreateView(String var1, AttributeSet var2) throws ClassNotFoundException {
         String[] var5 = sClassPrefixList;
         int var4 = var5.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            String var6 = var5[var3];

            View var8;
            try {
               var8 = this.createView(var1, var6, var2);
            } catch (ClassNotFoundException var7) {
               continue;
            }

            if (var8 != null) {
               return var8;
            }
         }

         return super.onCreateView(var1, var2);
      }
   }

   private static class InflateRequest {
      AsyncLayoutInflater.OnInflateFinishedListener callback;
      AsyncLayoutInflater inflater;
      ViewGroup parent;
      int resid;
      View view;

      InflateRequest() {
      }
   }

   private static class InflateThread extends Thread {
      private static final AsyncLayoutInflater.InflateThread sInstance;
      private ArrayBlockingQueue mQueue = new ArrayBlockingQueue(10);
      private Pools.SynchronizedPool mRequestPool = new Pools.SynchronizedPool(10);

      static {
         AsyncLayoutInflater.InflateThread var0 = new AsyncLayoutInflater.InflateThread();
         sInstance = var0;
         var0.start();
      }

      public static AsyncLayoutInflater.InflateThread getInstance() {
         return sInstance;
      }

      public void enqueue(AsyncLayoutInflater.InflateRequest var1) {
         try {
            this.mQueue.put(var1);
         } catch (InterruptedException var2) {
            throw new RuntimeException("Failed to enqueue async inflate request", var2);
         }
      }

      public AsyncLayoutInflater.InflateRequest obtainRequest() {
         AsyncLayoutInflater.InflateRequest var2 = (AsyncLayoutInflater.InflateRequest)this.mRequestPool.acquire();
         AsyncLayoutInflater.InflateRequest var1 = var2;
         if (var2 == null) {
            var1 = new AsyncLayoutInflater.InflateRequest();
         }

         return var1;
      }

      public void releaseRequest(AsyncLayoutInflater.InflateRequest var1) {
         var1.callback = null;
         var1.inflater = null;
         var1.parent = null;
         var1.resid = 0;
         var1.view = null;
         this.mRequestPool.release(var1);
      }

      public void run() {
         while(true) {
            this.runInner();
         }
      }

      public void runInner() {
         AsyncLayoutInflater.InflateRequest var1;
         try {
            var1 = (AsyncLayoutInflater.InflateRequest)this.mQueue.take();
         } catch (InterruptedException var4) {
            Log.w("AsyncLayoutInflater", var4);
            return;
         }

         try {
            var1.view = var1.inflater.mInflater.inflate(var1.resid, var1.parent, false);
         } catch (RuntimeException var3) {
            Log.w("AsyncLayoutInflater", "Failed to inflate resource in the background! Retrying on the UI thread", var3);
         }

         Message.obtain(var1.inflater.mHandler, 0, var1).sendToTarget();
      }
   }

   public interface OnInflateFinishedListener {
      void onInflateFinished(View var1, int var2, ViewGroup var3);
   }
}
