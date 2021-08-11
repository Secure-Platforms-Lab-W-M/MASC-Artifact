// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.PipedInputStream;

public class AsyncLogger implements LoggerInterface, Runnable
{
    static final int LOG = 1;
    static final int LOG_EXC = 4;
    static final int LOG_LN = 2;
    static final int LOG_MSG = 3;
    private boolean closed;
    private LoggerInterface out;
    private PipedInputStream pin;
    private DataOutputStream pout;
    
    public AsyncLogger(final LoggerInterface out) throws IOException {
        this.out = null;
        this.closed = false;
        this.out = out;
        this.logOpen();
    }
    
    private void logOpen() throws IOException {
        this.pin = new PipedInputStream(10240);
        this.pout = new DataOutputStream(new PipedOutputStream(this.pin));
        final Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    
    private void writeLog(final int n, final byte[] array) {
        try {
            this.pout.writeShort(n);
            this.pout.writeInt(array.length);
            this.pout.write(array);
            this.pout.flush();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
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
    
    @Override
    public void log(final String s) {
        synchronized (this.pout) {
            synchronized (this.pin) {
                this.writeLog(1, s.getBytes());
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void logException(final Exception ex) {
        try {
            final byte[] serializeObject = Utils.serializeObject(ex);
            synchronized (this.pout) {
                synchronized (this.pin) {
                    this.writeLog(4, serializeObject);
                    this.pin.notifyAll();
                }
            }
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
    
    @Override
    public void logLine(final String s) {
        synchronized (this.pout) {
            synchronized (this.pin) {
                this.writeLog(2, s.getBytes());
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void message(final String s) {
        synchronized (this.pout) {
            synchronized (this.pin) {
                this.writeLog(3, s.getBytes());
                this.pin.notifyAll();
            }
        }
    }
    
    @Override
    public void run() {
        final byte[] array = new byte[4096];
        final DataInputStream dataInputStream = new DataInputStream(this.pin);
        while (!this.closed) {
            try {
                Object pin = this.pin;
                synchronized (pin) {
                Label_0060_Outer:
                    while (this.pin.available() <= 0 && !this.closed) {
                        while (true) {
                            try {
                                this.pin.wait();
                                continue Label_0060_Outer;
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                                continue;
                            }
                            break;
                        }
                        break;
                    }
                    if (this.closed) {
                        return;
                    }
                    final short short1 = dataInputStream.readShort();
                    final byte[] array2 = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(array2);
                    // monitorexit(pin)
                    switch (short1) {
                        default: {
                            pin = new StringBuilder();
                            ((StringBuilder)pin).append("Unknown log Msg type: ");
                            ((StringBuilder)pin).append(short1);
                            throw new IOException(((StringBuilder)pin).toString());
                        }
                        case 4: {
                            this.out.logException((Exception)Utils.deserializeObject(array2));
                            continue;
                        }
                        case 3: {
                            this.out.message(new String(array2));
                            continue;
                        }
                        case 2: {
                            this.out.logLine(new String(array2));
                            continue;
                        }
                        case 1: {
                            this.out.log(new String(array2));
                            continue;
                        }
                    }
                }
            }
            catch (Exception ex2) {
                if (this.closed) {
                    continue;
                }
                ex2.printStackTrace();
            }
        }
    }
}
