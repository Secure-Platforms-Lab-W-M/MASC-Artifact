// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.util.zip.DataFormatException;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.Inflater;

public final class InflaterSource implements Source
{
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;
    
    InflaterSource(final BufferedSource source, final Inflater inflater) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (inflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.source = source;
        this.inflater = inflater;
    }
    
    public InflaterSource(final Source source, final Inflater inflater) {
        this(Okio.buffer(source), inflater);
    }
    
    private void releaseInflatedBytes() throws IOException {
        if (this.bufferBytesHeldByInflater == 0) {
            return;
        }
        final int n = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
        this.bufferBytesHeldByInflater -= n;
        this.source.skip(n);
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }
    
    @Override
    public long read(final Buffer buffer, final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + n);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (n == 0L) {
            return 0L;
        }
        while (true) {
            final boolean refill = this.refill();
            try {
                final Segment writableSegment = buffer.writableSegment(1);
                final int inflate = this.inflater.inflate(writableSegment.data, writableSegment.limit, (int)Math.min(n, 8192 - writableSegment.limit));
                if (inflate > 0) {
                    writableSegment.limit += inflate;
                    buffer.size += inflate;
                    return inflate;
                }
                if (this.inflater.finished() || this.inflater.needsDictionary()) {
                    this.releaseInflatedBytes();
                    if (writableSegment.pos == writableSegment.limit) {
                        buffer.head = writableSegment.pop();
                        SegmentPool.recycle(writableSegment);
                    }
                }
                else {
                    if (refill) {
                        throw new EOFException("source exhausted prematurely");
                    }
                    continue;
                }
            }
            catch (DataFormatException ex) {
                throw new IOException(ex);
            }
            break;
        }
        return -1L;
    }
    
    public boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        this.releaseInflatedBytes();
        if (this.inflater.getRemaining() != 0) {
            throw new IllegalStateException("?");
        }
        if (this.source.exhausted()) {
            return true;
        }
        final Segment head = this.source.buffer().head;
        this.bufferBytesHeldByInflater = head.limit - head.pos;
        this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
        return false;
    }
    
    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}
