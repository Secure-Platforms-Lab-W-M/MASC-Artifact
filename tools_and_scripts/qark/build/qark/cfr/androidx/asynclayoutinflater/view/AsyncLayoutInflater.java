/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
    private Handler.Callback mHandlerCallback;
    InflateThread mInflateThread;
    LayoutInflater mInflater;

    public AsyncLayoutInflater(Context context) {
        this.mHandlerCallback = new Handler.Callback(){

            public boolean handleMessage(Message object) {
                object = (InflateRequest)object.obj;
                if (object.view == null) {
                    object.view = AsyncLayoutInflater.this.mInflater.inflate(object.resid, object.parent, false);
                }
                object.callback.onInflateFinished(object.view, object.resid, object.parent);
                AsyncLayoutInflater.this.mInflateThread.releaseRequest((InflateRequest)object);
                return true;
            }
        };
        this.mInflater = new BasicInflater(context);
        this.mHandler = new Handler(this.mHandlerCallback);
        this.mInflateThread = InflateThread.getInstance();
    }

    public void inflate(int n, ViewGroup viewGroup, OnInflateFinishedListener onInflateFinishedListener) {
        if (onInflateFinishedListener != null) {
            InflateRequest inflateRequest = this.mInflateThread.obtainRequest();
            inflateRequest.inflater = this;
            inflateRequest.resid = n;
            inflateRequest.parent = viewGroup;
            inflateRequest.callback = onInflateFinishedListener;
            this.mInflateThread.enqueue(inflateRequest);
            return;
        }
        throw new NullPointerException("callback argument may not be null!");
    }

    private static class BasicInflater
    extends LayoutInflater {
        private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.webkit.", "android.app."};

        BasicInflater(Context context) {
            super(context);
        }

        public LayoutInflater cloneInContext(Context context) {
            return new BasicInflater(context);
        }

        protected View onCreateView(String string, AttributeSet attributeSet) throws ClassNotFoundException {
            for (String string2 : sClassPrefixList) {
                try {
                    string2 = this.createView(string, string2, attributeSet);
                    if (string2 == null) continue;
                    return string2;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            return super.onCreateView(string, attributeSet);
        }
    }

    private static class InflateRequest {
        OnInflateFinishedListener callback;
        AsyncLayoutInflater inflater;
        ViewGroup parent;
        int resid;
        View view;

        InflateRequest() {
        }
    }

    private static class InflateThread
    extends Thread {
        private static final InflateThread sInstance;
        private ArrayBlockingQueue<InflateRequest> mQueue = new ArrayBlockingQueue(10);
        private Pools.SynchronizedPool<InflateRequest> mRequestPool = new Pools.SynchronizedPool(10);

        static {
            InflateThread inflateThread;
            sInstance = inflateThread = new InflateThread();
            inflateThread.start();
        }

        private InflateThread() {
        }

        public static InflateThread getInstance() {
            return sInstance;
        }

        public void enqueue(InflateRequest inflateRequest) {
            try {
                this.mQueue.put(inflateRequest);
                return;
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException("Failed to enqueue async inflate request", interruptedException);
            }
        }

        public InflateRequest obtainRequest() {
            InflateRequest inflateRequest;
            InflateRequest inflateRequest2 = inflateRequest = this.mRequestPool.acquire();
            if (inflateRequest == null) {
                inflateRequest2 = new InflateRequest();
            }
            return inflateRequest2;
        }

        public void releaseRequest(InflateRequest inflateRequest) {
            inflateRequest.callback = null;
            inflateRequest.inflater = null;
            inflateRequest.parent = null;
            inflateRequest.resid = 0;
            inflateRequest.view = null;
            this.mRequestPool.release(inflateRequest);
        }

        @Override
        public void run() {
            do {
                this.runInner();
            } while (true);
        }

        public void runInner() {
            InflateRequest inflateRequest;
            try {
                inflateRequest = this.mQueue.take();
            }
            catch (InterruptedException interruptedException) {
                Log.w((String)"AsyncLayoutInflater", (Throwable)interruptedException);
                return;
            }
            try {
                inflateRequest.view = inflateRequest.inflater.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
            }
            catch (RuntimeException runtimeException) {
                Log.w((String)"AsyncLayoutInflater", (String)"Failed to inflate resource in the background! Retrying on the UI thread", (Throwable)runtimeException);
            }
            Message.obtain((Handler)inflateRequest.inflater.mHandler, (int)0, (Object)inflateRequest).sendToTarget();
        }
    }

    public static interface OnInflateFinishedListener {
        public void onInflateFinished(View var1, int var2, ViewGroup var3);
    }

}

