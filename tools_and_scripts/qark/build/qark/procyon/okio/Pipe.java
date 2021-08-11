// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;

public final class Pipe
{
    final Buffer buffer;
    final long maxBufferSize;
    private final Sink sink;
    boolean sinkClosed;
    private final Source source;
    boolean sourceClosed;
    
    public Pipe(final long maxBufferSize) {
        this.buffer = new Buffer();
        this.sink = new PipeSink();
        this.source = new PipeSource();
        if (maxBufferSize < 1L) {
            throw new IllegalArgumentException("maxBufferSize < 1: " + maxBufferSize);
        }
        this.maxBufferSize = maxBufferSize;
    }
    
    public Sink sink() {
        return this.sink;
    }
    
    public Source source() {
        return this.source;
    }
    
    final class PipeSink implements Sink
    {
        final Timeout timeout;
        
        PipeSink() {
            this.timeout = new Timeout();
        }
        
        @Override
        public void close() throws IOException {
            synchronized (Pipe.this.buffer) {
                if (Pipe.this.sinkClosed) {
                    return;
                }
                if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
                    throw new IOException("source is closed");
                }
            }
            Pipe.this.sinkClosed = true;
            Pipe.this.buffer.notifyAll();
        }
        // monitorexit(buffer)
        
        @Override
        public void flush() throws IOException {
            synchronized (Pipe.this.buffer) {
                if (Pipe.this.sinkClosed) {
                    throw new IllegalStateException("closed");
                }
            }
            if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
                throw new IOException("source is closed");
            }
        }
        // monitorexit(buffer)
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
        
        @Override
        public void write(final Buffer buffer, long n) throws IOException {
            while (true) {
                Label_0079: {
                    synchronized (Pipe.this.buffer) {
                        if (Pipe.this.sinkClosed) {
                            throw new IllegalStateException("closed");
                        }
                        break Label_0079;
                    }
                    final long n2 = Pipe.this.maxBufferSize - Pipe.this.buffer.size();
                    if (n2 == 0L) {
                        this.timeout.waitUntilNotified(Pipe.this.buffer);
                    }
                    else {
                        final long min = Math.min(n2, n);
                        final Buffer buffer2;
                        Pipe.this.buffer.write(buffer2, min);
                        n -= min;
                        Pipe.this.buffer.notifyAll();
                    }
                }
                if (n <= 0L) {
                    // monitorexit(buffer3)
                    return;
                }
                if (Pipe.this.sourceClosed) {
                    throw new IOException("source is closed");
                }
                continue;
            }
        }
    }
    
    final class PipeSource implements Source
    {
        final Timeout timeout;
        
        PipeSource() {
            this.timeout = new Timeout();
        }
        
        @Override
        public void close() throws IOException {
            synchronized (Pipe.this.buffer) {
                Pipe.this.sourceClosed = true;
                Pipe.this.buffer.notifyAll();
            }
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            while (true) {
                Label_0052: {
                    synchronized (Pipe.this.buffer) {
                        if (Pipe.this.sourceClosed) {
                            throw new IllegalStateException("closed");
                        }
                        break Label_0052;
                    }
                    this.timeout.waitUntilNotified(Pipe.this.buffer);
                }
                if (Pipe.this.buffer.size() != 0L) {
                    final Buffer buffer2;
                    read = Pipe.this.buffer.read(buffer2, read);
                    Pipe.this.buffer.notifyAll();
                    // monitorexit(buffer3)
                    return read;
                }
                if (Pipe.this.sinkClosed) {
                    // monitorexit(buffer3)
                    return -1L;
                }
                continue;
            }
        }
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
    }
}
