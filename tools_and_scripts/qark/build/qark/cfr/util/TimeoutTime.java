/*
 * Decompiled with CFR 0_124.
 */
package util;

import util.TimoutNotificator;

public class TimeoutTime {
    private long timeout = Long.MAX_VALUE;
    private TimoutNotificator toHandler;

    public TimeoutTime(TimoutNotificator timoutNotificator) {
        this.toHandler = timoutNotificator;
    }

    public long getTimeout() {
        synchronized (this) {
            long l = this.timeout;
            return l;
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
    public void setTimeout(long var1_1) {
        // MONITORENTER : this
        if (var1_1 > 0L) ** GOTO lbl6
        this.timeout = Long.MAX_VALUE;
        return;
lbl6: // 1 sources:
        this.timeout = this.toHandler.getCurrentTime() + var1_1;
        // MONITOREXIT : this
        return;
    }
}

