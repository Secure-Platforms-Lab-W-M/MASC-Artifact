/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 */
package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

class SnackbarManager {
    private static final int LONG_DURATION_MS = 2750;
    static final int MSG_TIMEOUT = 0;
    private static final int SHORT_DURATION_MS = 1500;
    private static SnackbarManager snackbarManager;
    private SnackbarRecord currentSnackbar;
    private final Handler handler;
    private final Object lock = new Object();
    private SnackbarRecord nextSnackbar;

    private SnackbarManager() {
        this.handler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

            public boolean handleMessage(Message message) {
                if (message.what != 0) {
                    return false;
                }
                SnackbarManager.this.handleTimeout((SnackbarRecord)message.obj);
                return true;
            }
        });
    }

    private boolean cancelSnackbarLocked(SnackbarRecord snackbarRecord, int n) {
        Callback callback = snackbarRecord.callback.get();
        if (callback != null) {
            this.handler.removeCallbacksAndMessages((Object)snackbarRecord);
            callback.dismiss(n);
            return true;
        }
        return false;
    }

    static SnackbarManager getInstance() {
        if (snackbarManager == null) {
            snackbarManager = new SnackbarManager();
        }
        return snackbarManager;
    }

    private boolean isCurrentSnackbarLocked(Callback callback) {
        SnackbarRecord snackbarRecord = this.currentSnackbar;
        if (snackbarRecord != null && snackbarRecord.isSnackbar(callback)) {
            return true;
        }
        return false;
    }

    private boolean isNextSnackbarLocked(Callback callback) {
        SnackbarRecord snackbarRecord = this.nextSnackbar;
        if (snackbarRecord != null && snackbarRecord.isSnackbar(callback)) {
            return true;
        }
        return false;
    }

    private void scheduleTimeoutLocked(SnackbarRecord snackbarRecord) {
        if (snackbarRecord.duration == -2) {
            return;
        }
        int n = 2750;
        if (snackbarRecord.duration > 0) {
            n = snackbarRecord.duration;
        } else if (snackbarRecord.duration == -1) {
            n = 1500;
        }
        this.handler.removeCallbacksAndMessages((Object)snackbarRecord);
        Handler handler = this.handler;
        handler.sendMessageDelayed(Message.obtain((Handler)handler, (int)0, (Object)snackbarRecord), (long)n);
    }

    private void showNextSnackbarLocked() {
        Object object = this.nextSnackbar;
        if (object != null) {
            this.currentSnackbar = object;
            this.nextSnackbar = null;
            object = object.callback.get();
            if (object != null) {
                object.show();
                return;
            }
            this.currentSnackbar = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void dismiss(Callback callback, int n) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.cancelSnackbarLocked(this.currentSnackbar, n);
            } else if (this.isNextSnackbarLocked(callback)) {
                this.cancelSnackbarLocked(this.nextSnackbar, n);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void handleTimeout(SnackbarRecord snackbarRecord) {
        Object object = this.lock;
        synchronized (object) {
            if (this.currentSnackbar == snackbarRecord || this.nextSnackbar == snackbarRecord) {
                this.cancelSnackbarLocked(snackbarRecord, 2);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isCurrent(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            return this.isCurrentSnackbarLocked(callback);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isCurrentOrNext(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback)) return true;
            if (!this.isNextSnackbarLocked(callback)) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onDismissed(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar = null;
                if (this.nextSnackbar != null) {
                    this.showNextSnackbarLocked();
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onShown(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.scheduleTimeoutLocked(this.currentSnackbar);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void pauseTimeout(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback) && !this.currentSnackbar.paused) {
                this.currentSnackbar.paused = true;
                this.handler.removeCallbacksAndMessages((Object)this.currentSnackbar);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void restoreTimeoutIfPaused(Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback) && this.currentSnackbar.paused) {
                this.currentSnackbar.paused = false;
                this.scheduleTimeoutLocked(this.currentSnackbar);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void show(int n, Callback callback) {
        Object object = this.lock;
        synchronized (object) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar.duration = n;
                this.handler.removeCallbacksAndMessages((Object)this.currentSnackbar);
                this.scheduleTimeoutLocked(this.currentSnackbar);
                return;
            }
            if (this.isNextSnackbarLocked(callback)) {
                this.nextSnackbar.duration = n;
            } else {
                this.nextSnackbar = new SnackbarRecord(n, callback);
            }
            if (this.currentSnackbar != null && this.cancelSnackbarLocked(this.currentSnackbar, 4)) {
                return;
            }
            this.currentSnackbar = null;
            this.showNextSnackbarLocked();
            return;
        }
    }

    static interface Callback {
        public void dismiss(int var1);

        public void show();
    }

    private static class SnackbarRecord {
        final WeakReference<Callback> callback;
        int duration;
        boolean paused;

        SnackbarRecord(int n, Callback callback) {
            this.callback = new WeakReference<Callback>(callback);
            this.duration = n;
        }

        boolean isSnackbar(Callback callback) {
            if (callback != null && this.callback.get() == callback) {
                return true;
            }
            return false;
        }
    }

}

