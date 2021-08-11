// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;

public abstract class ForwardingSink implements Sink
{
    private final Sink delegate;
    
    public ForwardingSink(final Sink delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = delegate;
    }
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    
    public final Sink delegate() {
        return this.delegate;
    }
    
    @Override
    public void flush() throws IOException {
        this.delegate.flush();
    }
    
    @Override
    public Timeout timeout() {
        return this.delegate.timeout();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
    }
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        this.delegate.write(buffer, n);
    }
}
