// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.util.Calendar;

public class DateRetriever implements Runnable
{
    private static int PRECISION_MILLIS;
    private static DateRetriever RETRIEVER_INSTANCE;
    private Thread _thread;
    private String current;
    private boolean picked;
    
    static {
        DateRetriever.PRECISION_MILLIS = 1000;
        DateRetriever.RETRIEVER_INSTANCE = new DateRetriever();
    }
    
    public DateRetriever() {
        this._thread = null;
        this.picked = false;
    }
    
    private String dateStr(final Calendar calendar) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.int2Str(calendar.get(2) + 1));
        sb.append("/");
        sb.append(this.int2Str(calendar.get(5)));
        sb.append("/");
        sb.append(calendar.get(1));
        sb.append(" ");
        sb.append(this.int2Str(calendar.get(11)));
        sb.append(":");
        sb.append(this.int2Str(calendar.get(12)));
        sb.append(":");
        sb.append(this.int2Str(calendar.get(13)));
        return sb.toString();
    }
    
    public static String getDateString() {
        return DateRetriever.RETRIEVER_INSTANCE.retrieveDateString();
    }
    
    private String int2Str(final int n) {
        if (n < 10) {
            final StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(n);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(n);
        return sb2.toString();
    }
    
    private String retrieveDateString() {
        synchronized (this) {
            this.picked = true;
            if (this._thread != null) {
                return this.current;
            }
            this.current = this.dateStr(Calendar.getInstance());
            (this._thread = new Thread(this)).setDaemon(true);
            this._thread.start();
            return this.current;
        }
    }
    
    private void waitMillis(final long n) {
        try {
            this.wait(n);
        }
        catch (InterruptedException ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    @Override
    public void run() {
        synchronized (this) {
            this._thread = Thread.currentThread();
            this.waitMillis(DateRetriever.PRECISION_MILLIS);
            while (this.picked) {
                this.current = this.dateStr(Calendar.getInstance());
                this.picked = false;
                this.waitMillis(DateRetriever.PRECISION_MILLIS);
            }
            this._thread = null;
        }
    }
}
