/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import util.DateRetriever;
import util.LoggerInterface;

public class FileLogger
implements LoggerInterface,
Runnable {
    private boolean closed = false;
    private int curSlot = 0;
    private long curSlotSize = 0L;
    private OutputStream fout = null;
    private String header;
    private String logFolderPath;
    private String name;
    private PipedInputStream pin;
    private PipedOutputStream pout;
    private PrintStream psout;
    private int slotCount;
    private long slotSize;
    private boolean timeStampEnabled = false;

    public FileLogger(String string2, String string3, long l, int n, String string4) throws IOException {
        if (l >= 1L && n >= 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("/");
            stringBuilder.append(string3);
            this.logFolderPath = stringBuilder.toString();
            this.name = string3;
            this.slotSize = l;
            this.slotCount = n;
            this.header = string4;
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
        Object object = new StringBuilder();
        object.append(this.logFolderPath);
        object.append("/");
        object.append(this.name);
        object.append("_");
        object.append(this.curSlot);
        object.append(".log");
        this.fout = new FileOutputStream(new File(object.toString()));
        this.curSlotSize = 0L;
        if (this.header != null) {
            object = this.fout;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.header);
            stringBuilder.append("\r\n");
            object.write(stringBuilder.toString().getBytes());
            this.fout.flush();
        }
        return this.fout;
    }

    private void logOpen() throws IOException {
        Object object = new File(this.logFolderPath);
        if (!object.exists()) {
            object.mkdirs();
        }
        long l = 0L;
        for (int i = 0; i < this.slotCount; ++i) {
            object = new StringBuilder();
            object.append(this.logFolderPath);
            object.append("/");
            object.append(this.name);
            object.append("_");
            object.append(i);
            object.append(".log");
            object = new File(object.toString());
            long l2 = l;
            if (object.exists()) {
                l2 = l;
                if (object.lastModified() > l) {
                    l2 = object.lastModified();
                    this.curSlotSize = object.length();
                    this.curSlot = i;
                }
            }
            l = l2;
        }
        object = new StringBuilder();
        object.append(this.logFolderPath);
        object.append("/");
        object.append(this.name);
        object.append("_");
        object.append(this.curSlot);
        object.append(".log");
        this.fout = new FileOutputStream(new File(object.toString()), true);
        if (this.curSlotSize == 0L && this.header != null) {
            object = this.fout;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.header);
            stringBuilder.append("\r\n");
            object.write(stringBuilder.toString().getBytes());
            this.fout.flush();
        }
        this.pin = new PipedInputStream(10240);
        this.pout = new PipedOutputStream(this.pin);
        this.psout = new PrintStream(this.pout, true);
        object = new Thread(this);
        object.setDaemon(true);
        object.start();
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
        this.fout.close();
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

    @Override
    public void message(String string2) {
        this.log(string2);
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
        byte[] arrby = new byte[2048];
        int n = 0;
        while (!this.closed) {
            int n2;
            int n3 = n;
            Closeable closeable = this.pin;
            n3 = n;
            // MONITORENTER : closeable
            do {
                n3 = n;
                n2 = this.pin.available();
                if (n2 > 0) break;
                n3 = n;
                boolean bl = this.closed;
                if (bl) break;
                n3 = n;
                try {
                    this.pin.wait();
                }
                catch (InterruptedException interruptedException) {
                    n3 = n;
                    interruptedException.printStackTrace();
                }
            } while (true);
            int n4 = n;
            n3 = n;
            if (!this.closed) {
                n3 = n;
                n4 = this.pin.read(arrby);
            }
            n3 = n4;
            // MONITOREXIT : closeable
            n3 = n4;
            n = n4;
            try {
                if (this.closed) continue;
                n3 = n4;
                closeable = this.getOutputStream();
                n3 = n4;
                closeable.write(arrby, 0, n4);
                n3 = n4;
                this.curSlotSize += (long)n4;
                n = n4;
                if (n2 != n4) continue;
                n3 = n4;
                closeable.flush();
                n = n4;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                n = n3;
            }
        }
    }
}

