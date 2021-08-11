// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.provider;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import android.os.Message;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.Handler$Callback;

public class SelfDestructiveThread
{
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Handler$Callback mCallback;
    private final int mDestructAfterMillisec;
    private int mGeneration;
    private Handler mHandler;
    private final Object mLock;
    private final int mPriority;
    private HandlerThread mThread;
    private final String mThreadName;
    
    public SelfDestructiveThread(final String mThreadName, final int mPriority, final int mDestructAfterMillisec) {
        this.mLock = new Object();
        this.mCallback = (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                final int what = message.what;
                if (what == 0) {
                    SelfDestructiveThread.this.onDestruction();
                    return true;
                }
                if (what != 1) {
                    return true;
                }
                SelfDestructiveThread.this.onInvokeRunnable((Runnable)message.obj);
                return true;
            }
        };
        this.mThreadName = mThreadName;
        this.mPriority = mPriority;
        this.mDestructAfterMillisec = mDestructAfterMillisec;
        this.mGeneration = 0;
    }
    
    private void post(final Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                (this.mThread = new HandlerThread(this.mThreadName, this.mPriority)).start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                ++this.mGeneration;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, (Object)runnable));
        }
    }
    
    public int getGeneration() {
        synchronized (this.mLock) {
            return this.mGeneration;
        }
    }
    
    public boolean isRunning() {
        while (true) {
            synchronized (this.mLock) {
                if (this.mThread != null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    void onDestruction() {
        synchronized (this.mLock) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
        }
    }
    
    void onInvokeRunnable(final Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long)this.mDestructAfterMillisec);
        }
    }
    
    public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> replyCallback) {
        this.post(new Runnable() {
            final /* synthetic */ Handler val$callingHandler = new Handler();
            
            @Override
            public void run() {
                Object call;
                try {
                    call = callable.call();
                }
                catch (Exception ex) {
                    call = null;
                }
                this.val$callingHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        replyCallback.onReply(call);
                    }
                });
            }
        });
    }
    
    public <T> T postAndWait(final Callable<T> callable, final int n) throws InterruptedException {
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition condition = reentrantLock.newCondition();
        final AtomicReference<T> atomicReference = new AtomicReference<T>();
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        this.post(new Runnable() {
            @Override
            public void run() {
                try {
                    atomicReference.set(callable.call());
                }
                catch (Exception ex) {}
                reentrantLock.lock();
                try {
                    atomicBoolean.set(false);
                    condition.signal();
                }
                finally {
                    reentrantLock.unlock();
                }
            }
        });
        reentrantLock.lock();
        try {
            if (!atomicBoolean.get()) {
                return atomicReference.get();
            }
            long n2 = TimeUnit.MILLISECONDS.toNanos(n);
            while (true) {
                try {
                    n2 = condition.awaitNanos(n2);
                }
                catch (InterruptedException ex) {}
                if (!atomicBoolean.get()) {
                    return atomicReference.get();
                }
                if (n2 > 0L) {
                    continue;
                }
                throw new InterruptedException("timeout");
            }
        }
        finally {
            reentrantLock.unlock();
        }
    }
    
    public interface ReplyCallback<T>
    {
        void onReply(final T p0);
    }
}
