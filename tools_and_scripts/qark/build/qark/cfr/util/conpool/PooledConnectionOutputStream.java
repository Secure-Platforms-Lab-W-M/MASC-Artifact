/*
 * Decompiled with CFR 0_124.
 */
package util.conpool;

import java.io.IOException;
import java.io.OutputStream;

public class PooledConnectionOutputStream
extends OutputStream {
    private OutputStream out;
    private long traffic = 0L;
    private boolean valid = true;

    public PooledConnectionOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
        if (!this.valid) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid:");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public long getTraffic() {
        return this.traffic;
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override
    public void write(int n) throws IOException {
        this.out.write(n);
        ++this.traffic;
        if (!this.valid) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid:");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    @Override
    public void write(byte[] object) throws IOException {
        this.out.write((byte[])object);
        this.traffic += (long)object.length;
        if (!this.valid) {
            object = new StringBuilder();
            object.append("Invalid:");
            object.append(this);
            throw new IllegalStateException(object.toString());
        }
    }

    @Override
    public void write(byte[] object, int n, int n2) throws IOException {
        this.out.write((byte[])object, n, n2);
        this.traffic += (long)n2;
        if (!this.valid) {
            object = new StringBuilder();
            object.append("Invalid:");
            object.append(this);
            throw new IllegalStateException(object.toString());
        }
    }
}

