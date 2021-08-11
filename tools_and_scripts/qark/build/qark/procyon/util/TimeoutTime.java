// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

public class TimeoutTime
{
    private long timeout;
    private TimoutNotificator toHandler;
    
    public TimeoutTime(final TimoutNotificator toHandler) {
        this.timeout = Long.MAX_VALUE;
        this.toHandler = toHandler;
    }
    
    public long getTimeout() {
        synchronized (this) {
            return this.timeout;
        }
    }
    
    public void setTimeout(final long n) {
        // monitorenter(this)
        Label_0018: {
            if (n > 0L) {
                break Label_0018;
            }
            while (true) {
                try {
                    this.timeout = Long.MAX_VALUE;
                    while (true) {
                        return;
                        throw;
                        this.timeout = this.toHandler.getCurrentTime() + n;
                        continue;
                    }
                }
                // monitorexit(this)
                // monitorexit(this)
                finally {
                    continue;
                }
                break;
            }
        }
    }
}
