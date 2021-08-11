// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;

public class AsyncBulkLogger implements LoggerInterface, Runnable
{
    private boolean closed;
    private LoggerInterface out;
    private PipedInputStream pin;
    private PipedOutputStream pout;
    private PrintStream psout;
    private boolean timeStampEnabled;
    
    public AsyncBulkLogger(final LoggerInterface out) throws IOException {
        this.out = null;
        this.closed = false;
        this.timeStampEnabled = false;
        this.out = out;
        this.logOpen();
    }
    
    private void logOpen() throws IOException {
        this.pin = new PipedInputStream(10240);
        this.pout = new PipedOutputStream(this.pin);
        this.psout = new PrintStream(this.pout, true);
        final Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    
    @Override
    public void closeLogger() {
        final PipedInputStream pin = this.pin;
        // monitorenter(pin)
        try {
            try {
                this.closed = true;
                this.pout.close();
                this.pin.notifyAll();
            }
            finally {
            }
            // monitorexit(pin)
            // monitorexit(pin)
        }
        catch (IOException ex) {}
    }
    
    public void enableTimestamp(final boolean timeStampEnabled) {
        this.timeStampEnabled = timeStampEnabled;
    }
    
    @Override
    public void log(final String s) {
        synchronized (this.psout) {
            synchronized (this.pin) {
                if (this.timeStampEnabled) {
                    final PrintStream psout = this.psout;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(DateRetriever.getDateString());
                    sb.append(" ");
                    psout.print(sb.toString());
                }
                this.psout.print(s);
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void logException(final Exception ex) {
        synchronized (this.psout) {
            synchronized (this.pin) {
                if (this.timeStampEnabled) {
                    final PrintStream psout = this.psout;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(DateRetriever.getDateString());
                    sb.append(" ");
                    psout.print(sb.toString());
                }
                ex.printStackTrace(this.psout);
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void logLine(final String s) {
        synchronized (this.psout) {
            synchronized (this.pin) {
                if (this.timeStampEnabled) {
                    final PrintStream psout = this.psout;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(DateRetriever.getDateString());
                    sb.append(" ");
                    psout.print(sb.toString());
                }
                this.psout.println(s);
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void message(final String s) {
        synchronized (this.pin) {
            this.out.message(s);
        }
    }
    
    @Override
    public void run() {
        final byte[] array = new byte[4096];
        int n = 0;
        while (!this.closed) {
            int n2 = n;
            try {
                final PipedInputStream pin = this.pin;
                n2 = n;
                // monitorenter(pin)
            Label_0063_Outer:
                while (true) {
                    n2 = n;
                    try {
                        if (this.pin.available() <= 0) {
                            n2 = n;
                            if (!this.closed) {
                                n2 = n;
                                while (true) {
                                    try {
                                        this.pin.wait();
                                        continue Label_0063_Outer;
                                    }
                                    catch (InterruptedException ex) {
                                        n2 = n;
                                        ex.printStackTrace();
                                        continue;
                                    }
                                    break;
                                }
                            }
                        }
                        int read = n;
                        n2 = n;
                        if (!this.closed) {
                            n2 = n;
                            read = this.pin.read(array);
                        }
                        n2 = read;
                        // monitorexit(pin)
                        n2 = read;
                        n = read;
                        if (!this.closed) {
                            n2 = read;
                            this.out.log(new String(array, 0, read));
                            n = read;
                            break;
                        }
                        break;
                    }
                    finally {
                    }
                    // monitorexit(pin)
                }
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
                n = n2;
            }
        }
    }
}
