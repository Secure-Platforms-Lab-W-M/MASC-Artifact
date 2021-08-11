/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.util.Calendar;
import util.Logger;

public class DateRetriever
implements Runnable {
    private static int PRECISION_MILLIS = 1000;
    private static DateRetriever RETRIEVER_INSTANCE = new DateRetriever();
    private Thread _thread = null;
    private String current;
    private boolean picked = false;

    private String dateStr(Calendar calendar) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.int2Str(calendar.get(2) + 1));
        stringBuilder.append("/");
        stringBuilder.append(this.int2Str(calendar.get(5)));
        stringBuilder.append("/");
        stringBuilder.append(calendar.get(1));
        stringBuilder.append(" ");
        stringBuilder.append(this.int2Str(calendar.get(11)));
        stringBuilder.append(":");
        stringBuilder.append(this.int2Str(calendar.get(12)));
        stringBuilder.append(":");
        stringBuilder.append(this.int2Str(calendar.get(13)));
        return stringBuilder.toString();
    }

    public static String getDateString() {
        return RETRIEVER_INSTANCE.retrieveDateString();
    }

    private String int2Str(int n) {
        if (n < 10) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(n);
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(n);
        return stringBuilder.toString();
    }

    private String retrieveDateString() {
        synchronized (this) {
            block4 : {
                this.picked = true;
                if (this._thread == null) break block4;
                String string2 = this.current;
                return string2;
            }
            this.current = this.dateStr(Calendar.getInstance());
            this._thread = new Thread(this);
            this._thread.setDaemon(true);
            this._thread.start();
            String string3 = this.current;
            return string3;
        }
    }

    private void waitMillis(long l) {
        try {
            this.wait(l);
            return;
        }
        catch (InterruptedException interruptedException) {
            Logger.getLogger().logException(interruptedException);
            return;
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            this._thread = Thread.currentThread();
            this.waitMillis(PRECISION_MILLIS);
            while (this.picked) {
                this.current = this.dateStr(Calendar.getInstance());
                this.picked = false;
                this.waitMillis(PRECISION_MILLIS);
            }
            this._thread = null;
            return;
        }
    }
}

