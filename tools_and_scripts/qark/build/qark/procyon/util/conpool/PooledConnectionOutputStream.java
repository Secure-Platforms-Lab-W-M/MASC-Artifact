// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util.conpool;

import java.io.IOException;
import java.io.OutputStream;

public class PooledConnectionOutputStream extends OutputStream
{
    private OutputStream out;
    private long traffic;
    private boolean valid;
    
    public PooledConnectionOutputStream(final OutputStream out) {
        this.valid = true;
        this.traffic = 0L;
        this.out = out;
    }
    
    @Override
    public void close() throws IOException {
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public long getTraffic() {
        return this.traffic;
    }
    
    public void invalidate() {
        this.valid = false;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.out.write(n);
        ++this.traffic;
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.out.write(array);
        this.traffic += array.length;
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.out.write(array, n, n2);
        this.traffic += n2;
        if (!this.valid) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid:");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }
}
