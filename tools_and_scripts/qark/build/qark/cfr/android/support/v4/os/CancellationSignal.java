/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 */
package android.support.v4.os;

import android.os.Build;
import android.support.v4.os.OperationCanceledException;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void cancel() {
        block13 : {
            // MONITORENTER : this
            if (this.mIsCanceled) {
                // MONITOREXIT : this
                return;
            }
            this.mIsCanceled = true;
            this.mCancelInProgress = true;
            var1_1 = this.mOnCancelListener;
            var2_4 = this.mCancellationSignalObj;
            // MONITOREXIT : this
            if (var1_1 == null) ** GOTO lbl16
            try {
                var1_1.onCancel();
lbl16: // 2 sources:
                if (var2_4 == null || Build.VERSION.SDK_INT < 16) break block13;
                ((android.os.CancellationSignal)var2_4).cancel();
            }
            catch (Throwable var1_2) {}
        }
        // MONITORENTER : this
        this.mCancelInProgress = false;
        this.notifyAll();
        // MONITOREXIT : this
        return;
        // MONITORENTER : this
        this.mCancelInProgress = false;
        this.notifyAll();
        // MONITOREXIT : this
        throw var1_2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Object getCancellationSignalObject() {
        if (Build.VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj != null) return this.mCancellationSignalObj;
            this.mCancellationSignalObj = new android.os.CancellationSignal();
            if (!this.mIsCanceled) return this.mCancellationSignalObj;
            ((android.os.CancellationSignal)this.mCancellationSignalObj).cancel();
            return this.mCancellationSignalObj;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnCancelListener(OnCancelListener onCancelListener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == onCancelListener) {
                return;
            }
            this.mOnCancelListener = onCancelListener;
            if (this.mIsCanceled && onCancelListener != null) {
                // MONITOREXIT [2, 3] lbl8 : MonitorExitStatement: MONITOREXIT : this
                onCancelListener.onCancel();
                return;
            }
            return;
        }
    }

    public void throwIfCanceled() {
        if (!this.isCanceled()) {
            return;
        }
        throw new OperationCanceledException();
    }

    public static interface OnCancelListener {
        public void onCancel();
    }

}

