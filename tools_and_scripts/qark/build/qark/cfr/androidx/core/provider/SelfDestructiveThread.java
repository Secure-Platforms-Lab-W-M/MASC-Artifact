/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 */
package androidx.core.provider;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Handler.Callback mCallback;
    private final int mDestructAfterMillisec;
    private int mGeneration;
    private Handler mHandler;
    private final Object mLock = new Object();
    private final int mPriority;
    private HandlerThread mThread;
    private final String mThreadName;

    public SelfDestructiveThread(String string2, int n, int n2) {
        this.mCallback = new Handler.Callback(){

            public boolean handleMessage(Message message) {
                int n = message.what;
                if (n != 0) {
                    if (n != 1) {
                        return true;
                    }
                    SelfDestructiveThread.this.onInvokeRunnable((Runnable)message.obj);
                    return true;
                }
                SelfDestructiveThread.this.onDestruction();
                return true;
            }
        };
        this.mThreadName = string2;
        this.mPriority = n;
        this.mDestructAfterMillisec = n2;
        this.mGeneration = 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void post(Runnable runnable) {
        Object object = this.mLock;
        synchronized (object) {
            if (this.mThread == null) {
                HandlerThread handlerThread;
                this.mThread = handlerThread = new HandlerThread(this.mThreadName, this.mPriority);
                handlerThread.start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                ++this.mGeneration;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, (Object)runnable));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getGeneration() {
        Object object = this.mLock;
        synchronized (object) {
            return this.mGeneration;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isRunning() {
        Object object = this.mLock;
        synchronized (object) {
            if (this.mThread == null) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void onDestruction() {
        Object object = this.mLock;
        synchronized (object) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void onInvokeRunnable(Runnable object) {
        object.run();
        object = this.mLock;
        synchronized (object) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long)this.mDestructAfterMillisec);
            return;
        }
    }

    public <T> void postAndReply(final Callable<T> callable, ReplyCallback<T> replyCallback) {
        this.post(new Runnable(new Handler(), replyCallback){
            final /* synthetic */ Handler val$callingHandler;
            final /* synthetic */ ReplyCallback val$reply;
            {
                this.val$callingHandler = handler;
                this.val$reply = replyCallback;
            }

            @Override
            public void run() {
                Object v;
                try {
                    v = callable.call();
                }
                catch (Exception exception) {
                    v = null;
                }
                this.val$callingHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        2.this.val$reply.onReply(v);
                    }
                });
            }

        });
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public <T> T postAndWait(final Callable<T> var1_1, int var2_4) throws InterruptedException {
        block7 : {
            var7_8 = new ReentrantLock();
            var8_9 = var7_8.newCondition();
            var9_10 = new AtomicReference<V>();
            var10_11 = new AtomicBoolean(true);
            this.post(new Runnable(){

                @Override
                public void run() {
                    try {
                        var9_10.set(var1_1.call());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    var7_8.lock();
                    try {
                        var10_11.set(false);
                        var8_9.signal();
                        return;
                    }
                    finally {
                        var7_8.unlock();
                    }
                }
            });
            var7_8.lock();
            if (var10_11.get()) break block7;
            var1_2 = var9_10.get();
            var7_8.unlock();
            return (T)var1_2;
        }
        try {
            var3_12 = TimeUnit.MILLISECONDS.toNanos((long)var2_7);
            do lbl-1000: // 2 sources:
            {
                try {
                    var3_12 = var5_13 = var8_9.awaitNanos(var3_12);
                }
                catch (InterruptedException var1_4) {
                    // empty catch block
                }
                if (var10_11.get()) continue;
                var1_5 = var9_10.get();
                var7_8.unlock();
                break;
            } while (true);
        }
        catch (Throwable var1_6) {
            var7_8.unlock();
            throw var1_6;
        }
        {
            return (T)var1_5;
            ** while (var3_12 > 0L)
        }
lbl30: // 2 sources:
        throw new InterruptedException("timeout");
    }

    public static interface ReplyCallback<T> {
        public void onReply(T var1);
    }

}

