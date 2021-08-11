// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.OutputStream;

public class FileLogger implements LoggerInterface, Runnable
{
    private boolean closed;
    private int curSlot;
    private long curSlotSize;
    private OutputStream fout;
    private String header;
    private String logFolderPath;
    private String name;
    private PipedInputStream pin;
    private PipedOutputStream pout;
    private PrintStream psout;
    private int slotCount;
    private long slotSize;
    private boolean timeStampEnabled;
    
    public FileLogger(final String s, final String name, final long slotSize, final int slotCount, final String header) throws IOException {
        this.curSlotSize = 0L;
        this.curSlot = 0;
        this.fout = null;
        this.closed = false;
        this.timeStampEnabled = false;
        if (slotSize >= 1L && slotCount >= 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("/");
            sb.append(name);
            this.logFolderPath = sb.toString();
            this.name = name;
            this.slotSize = slotSize;
            this.slotCount = slotCount;
            this.header = header;
            this.logOpen();
            return;
        }
        throw new IllegalArgumentException("slotSize and slotCount must not be less than 1");
    }
    
    private OutputStream getOutputStream() throws IOException {
        if (this.curSlotSize < this.slotSize) {
            return this.fout;
        }
        this.fout.flush();
        this.fout.close();
        this.curSlot = (this.curSlot + 1) % this.slotCount;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.logFolderPath);
        sb.append("/");
        sb.append(this.name);
        sb.append("_");
        sb.append(this.curSlot);
        sb.append(".log");
        this.fout = new FileOutputStream(new File(sb.toString()));
        this.curSlotSize = 0L;
        if (this.header != null) {
            final OutputStream fout = this.fout;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.header);
            sb2.append("\r\n");
            fout.write(sb2.toString().getBytes());
            this.fout.flush();
        }
        return this.fout;
    }
    
    private void logOpen() throws IOException {
        final File file = new File(this.logFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        long n = 0L;
        long lastModified;
        for (int i = 0; i < this.slotCount; ++i, n = lastModified) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.logFolderPath);
            sb.append("/");
            sb.append(this.name);
            sb.append("_");
            sb.append(i);
            sb.append(".log");
            final File file2 = new File(sb.toString());
            lastModified = n;
            if (file2.exists()) {
                lastModified = n;
                if (file2.lastModified() > n) {
                    lastModified = file2.lastModified();
                    this.curSlotSize = file2.length();
                    this.curSlot = i;
                }
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.logFolderPath);
        sb2.append("/");
        sb2.append(this.name);
        sb2.append("_");
        sb2.append(this.curSlot);
        sb2.append(".log");
        this.fout = new FileOutputStream(new File(sb2.toString()), true);
        if (this.curSlotSize == 0L && this.header != null) {
            final OutputStream fout = this.fout;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(this.header);
            sb3.append("\r\n");
            fout.write(sb3.toString().getBytes());
            this.fout.flush();
        }
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
                this.fout.close();
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
        this.log(s);
    }
    
    @Override
    public void run() {
        final byte[] array = new byte[2048];
        int n = 0;
        while (!this.closed) {
            int n2 = n;
            try {
                Closeable closeable = this.pin;
                n2 = n;
                // monitorenter(closeable)
            Label_0067_Outer:
                while (true) {
                    n2 = n;
                    try {
                        final int available = this.pin.available();
                        if (available <= 0) {
                            n2 = n;
                            if (!this.closed) {
                                n2 = n;
                                while (true) {
                                    try {
                                        this.pin.wait();
                                        continue Label_0067_Outer;
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
                        // monitorexit(closeable)
                        n2 = read;
                        n = read;
                        if (this.closed) {
                            break;
                        }
                        n2 = read;
                        closeable = this.getOutputStream();
                        n2 = read;
                        ((OutputStream)closeable).write(array, 0, read);
                        n2 = read;
                        this.curSlotSize += read;
                        if (available == (n = read)) {
                            n2 = read;
                            ((OutputStream)closeable).flush();
                            n = read;
                            break;
                        }
                        break;
                    }
                    finally {
                    }
                    // monitorexit(closeable)
                }
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
                n = n2;
            }
        }
    }
}
