// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util.conpool;

import java.io.IOException;
import java.io.InputStream;

public class PooledConnectionInputStream extends InputStream
{
    private InputStream in;
    private long traffic;
    private boolean valid;
    
    public PooledConnectionInputStream(final InputStream in) {
        this.in = null;
        this.valid = true;
        this.traffic = 0L;
        this.in = in;
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
        final int read = this.in.read();
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        if (read != -1) {
            ++this.traffic;
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        read = this.in.read(array, read, n);
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        this.traffic += read;
        return read;
    }
}
