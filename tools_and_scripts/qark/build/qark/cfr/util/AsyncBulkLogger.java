/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import util.DateRetriever;
import util.LoggerInterface;

public class AsyncBulkLogger
implements LoggerInterface,
Runnable {
    private boolean closed = false;
    private LoggerInterface out = null;
    private PipedInputStream pin;
    private PipedOutputStream pout;
    private PrintStream psout;
    private boolean timeStampEnabled = false;

    public AsyncBulkLogger(LoggerInterface loggerInterface) throws IOException {
        this.out = loggerInterface;
        this.logOpen();
    }

    private void logOpen() throws IOException {
        this.pin = new PipedInputStream(10240);
        this.pout = new PipedOutputStream(this.pin);
        this.psout = new PrintStream(this.pout, true);
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void closeLogger() {
        PipedInputStream pipedInputStream = this.pin;
        // MONITORENTER : pipedInputStream
        this.closed = true;
        this.pout.close();
        this.pin.notifyAll();
        return;
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        // MONITOREXIT : pipedInputStream
    }

    public void enableTimestamp(boolean bl) {
        this.timeStampEnabled = bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void log(String string2) {
        PrintStream printStream = this.psout;
        synchronized (printStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                if (this.timeStampEnabled) {
                    PrintStream printStream2 = this.psout;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(DateRetriever.getDateString());
                    stringBuilder.append(" ");
                    printStream2.print(stringBuilder.toString());
                }
                this.psout.print(string2);
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void logException(Exception exception) {
        PrintStream printStream = this.psout;
        synchronized (printStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                if (this.timeStampEnabled) {
                    PrintStream printStream2 = this.psout;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(DateRetriever.getDateString());
                    stringBuilder.append(" ");
                    printStream2.print(stringBuilder.toString());
                }
                exception.printStackTrace(this.psout);
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void logLine(String string2) {
        PrintStream printStream = this.psout;
        synchronized (printStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                if (this.timeStampEnabled) {
                    PrintStream printStream2 = this.psout;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(DateRetriever.getDateString());
                    stringBuilder.append(" ");
                    printStream2.print(stringBuilder.toString());
                }
                this.psout.println(string2);
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void message(String string2) {
        PipedInputStream pipedInputStream = this.pin;
        synchronized (pipedInputStream) {
            this.out.message(string2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        byte[] arrby = new byte[4096];
        int n = 0;
        while (!this.closed) {
            int n2 = n;
            PipedInputStream pipedInputStream = this.pin;
            n2 = n;
            // MONITORENTER : pipedInputStream
            do {
                n2 = n;
                if (this.pin.available() > 0) break;
                n2 = n;
                boolean bl = this.closed;
                if (bl) break;
                n2 = n;
                try {
                    this.pin.wait();
                }
                catch (InterruptedException interruptedException) {
                    n2 = n;
                    interruptedException.printStackTrace();
                }
            } while (true);
            int n3 = n;
            n2 = n;
            if (!this.closed) {
                n2 = n;
                n3 = this.pin.read(arrby);
            }
            n2 = n3;
            // MONITOREXIT : pipedInputStream
            n2 = n3;
            n = n3;
            try {
                if (this.closed) continue;
                n2 = n3;
                this.out.log(new String(arrby, 0, n3));
                n = n3;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                n = n2;
            }
        }
    }
}

