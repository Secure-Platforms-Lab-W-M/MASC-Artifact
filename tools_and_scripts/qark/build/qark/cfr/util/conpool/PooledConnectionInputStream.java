/*
 * Decompiled with CFR 0_124.
 */
package util.conpool;

import java.io.IOException;
import java.io.InputStream;

public class PooledConnectionInputStream
extends InputStream {
    private InputStream in = null;
    private long traffic = 0L;
    private boolean valid = true;

    public PooledConnectionInputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    @Override
    public void close() throws IOException {
    }

    public long getTraffic() {
        return this.traffic;
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override
    public int read() throws IOException {
        int n = this.in.read();
        if (!this.valid) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid:");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (n != -1) {
            ++this.traffic;
        }
        return n;
    }

    @Override
    public int read(byte[] arrby) throws IOException {
        return this.read(arrby, 0, arrby.length);
    }

    @Override
    public int read(byte[] object, int n, int n2) throws IOException {
        n = this.in.read((byte[])object, n, n2);
        if (!this.valid) {
            object = new StringBuilder();
            object.append("Invalid:");
            object.append(this);
            throw new IllegalStateException(object.toString());
        }
        this.traffic += (long)n;
        return n;
    }
}

