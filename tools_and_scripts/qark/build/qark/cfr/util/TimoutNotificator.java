/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.util.HashSet;
import java.util.Vector;
import util.TimeoutListener;

public class TimoutNotificator
implements Runnable {
    public static TimoutNotificator instance = new TimoutNotificator();
    private volatile long curTime = 0L;
    public HashSet listeners = new HashSet();
    private boolean stopped = false;
    public boolean threadAvailable = false;

    public static TimoutNotificator getInstance() {
        return instance;
    }

    public static TimoutNotificator getNewInstance() {
        return new TimoutNotificator();
    }

    public long getCurrentTime() {
        if (this.threadAvailable) {
            return this.curTime;
        }
        return System.currentTimeMillis();
    }

    public void register(TimeoutListener object) {
        synchronized (this) {
            this.listeners.add(object);
            if (!this.threadAvailable) {
                this.curTime = System.currentTimeMillis();
                this.threadAvailable = true;
                object = new Thread(this);
                object.setDaemon(true);
                object.start();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void run() {
        Vector<TimeoutListener> vector = new Vector<TimeoutListener>();
        int n = 0;
        block5 : while (n == 0) {
            TimeoutListener[] arrtimeoutListener;
            synchronized (this) {
                vector.removeAllElements();
                this.curTime = System.currentTimeMillis();
                try {
                    this.wait(1000L);
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                this.curTime += 1000L;
                if (!this.stopped) {
                    arrtimeoutListener = this.listeners.toArray(new TimeoutListener[0]);
                    for (n = 0; n < arrtimeoutListener.length; ++n) {
                        long l = arrtimeoutListener[n].getTimoutTime();
                        if (this.curTime <= l) continue;
                        this.listeners.remove(arrtimeoutListener[n]);
                        vector.add(arrtimeoutListener[n]);
                    }
                }
                n = !this.listeners.isEmpty() && !this.stopped ? 0 : 1;
                if (n != 0) {
                    this.threadAvailable = false;
                }
            }
            arrtimeoutListener = vector.toArray(new TimeoutListener[0]);
            int n2 = 0;
            do {
                if (n2 >= arrtimeoutListener.length) continue block5;
                arrtimeoutListener[n2].timeoutNotification();
                ++n2;
            } while (true);
            break;
        }
        return;
    }

    public void shutdown() {
        synchronized (this) {
            this.stopped = true;
            this.notifyAll();
            return;
        }
    }

    public void unregister(TimeoutListener timeoutListener) {
        synchronized (this) {
            this.listeners.remove(timeoutListener);
            return;
        }
    }
}

