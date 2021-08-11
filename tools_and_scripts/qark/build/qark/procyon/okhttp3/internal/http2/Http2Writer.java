// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

import java.util.List;
import okhttp3.internal.Util;
import java.util.logging.Level;
import java.io.IOException;
import okio.BufferedSink;
import okio.Buffer;
import java.util.logging.Logger;
import java.io.Closeable;

final class Http2Writer implements Closeable
{
    private static final Logger logger;
    private final boolean client;
    private boolean closed;
    private final Buffer hpackBuffer;
    final Hpack.Writer hpackWriter;
    private int maxFrameSize;
    private final BufferedSink sink;
    
    static {
        logger = Logger.getLogger(Http2.class.getName());
    }
    
    Http2Writer(final BufferedSink sink, final boolean client) {
        this.sink = sink;
        this.client = client;
        this.hpackBuffer = new Buffer();
        this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
        this.maxFrameSize = 16384;
    }
    
    private void writeContinuationFrames(final int n, long n2) throws IOException {
        while (n2 > 0L) {
            final int n3 = (int)Math.min(this.maxFrameSize, n2);
            n2 -= n3;
            byte b;
            if (n2 == 0L) {
                b = 4;
            }
            else {
                b = 0;
            }
            this.frameHeader(n, n3, (byte)9, b);
            this.sink.write(this.hpackBuffer, n3);
        }
    }
    
    private static void writeMedium(final BufferedSink bufferedSink, final int n) throws IOException {
        bufferedSink.writeByte(n >>> 16 & 0xFF);
        bufferedSink.writeByte(n >>> 8 & 0xFF);
        bufferedSink.writeByte(n & 0xFF);
    }
    
    public void applyAndAckSettings(final Settings settings) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final Settings settings2;
        this.maxFrameSize = settings2.getMaxFrameSize(this.maxFrameSize);
        if (settings2.getHeaderTableSize() != -1) {
            this.hpackWriter.setHeaderTableSizeSetting(settings2.getHeaderTableSize());
        }
        this.frameHeader(0, 0, (byte)4, (byte)1);
        this.sink.flush();
    }
    // monitorexit(this)
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            this.closed = true;
            this.sink.close();
        }
    }
    
    public void connectionPreface() throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        if (this.client) {
            if (Http2Writer.logger.isLoggable(Level.FINE)) {
                Http2Writer.logger.fine(Util.format(">> CONNECTION %s", Http2.CONNECTION_PREFACE.hex()));
            }
            this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
            this.sink.flush();
        }
    }
    // monitorexit(this)
    
    public void data(final boolean b, final int n, final Buffer buffer, final int n2) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        byte b2 = 0;
        if (b) {
            b2 = 1;
        }
        final Buffer buffer2;
        this.dataFrame(n, b2, buffer2, n2);
    }
    // monitorexit(this)
    
    void dataFrame(final int n, final byte b, final Buffer buffer, final int n2) throws IOException {
        this.frameHeader(n, n2, (byte)0, b);
        if (n2 > 0) {
            this.sink.write(buffer, n2);
        }
    }
    
    public void flush() throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        this.sink.flush();
    }
    // monitorexit(this)
    
    public void frameHeader(final int n, final int n2, final byte b, final byte b2) throws IOException {
        if (Http2Writer.logger.isLoggable(Level.FINE)) {
            Http2Writer.logger.fine(Http2.frameLog(false, n, n2, b, b2));
        }
        if (n2 > this.maxFrameSize) {
            throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", this.maxFrameSize, n2);
        }
        if ((Integer.MIN_VALUE & n) != 0x0) {
            throw Http2.illegalArgument("reserved bit set: %s", n);
        }
        writeMedium(this.sink, n2);
        this.sink.writeByte(b & 0xFF);
        this.sink.writeByte(b2 & 0xFF);
        this.sink.writeInt(Integer.MAX_VALUE & n);
    }
    
    public void goAway(final int n, final ErrorCode errorCode, final byte[] array) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final ErrorCode errorCode2;
        if (errorCode2.httpCode == -1) {
            throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
        }
        this.frameHeader(0, array.length + 8, (byte)7, (byte)0);
        this.sink.writeInt(n);
        this.sink.writeInt(errorCode2.httpCode);
        if (array.length > 0) {
            this.sink.write(array);
        }
        this.sink.flush();
    }
    // monitorexit(this)
    
    public void headers(final int n, final List<Header> list) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final List<Header> list2;
        this.headers(false, n, list2);
    }
    // monitorexit(this)
    
    void headers(final boolean b, final int n, final List<Header> list) throws IOException {
        if (this.closed) {
            throw new IOException("closed");
        }
        this.hpackWriter.writeHeaders(list);
        final long size = this.hpackBuffer.size();
        final int n2 = (int)Math.min(this.maxFrameSize, size);
        byte b2;
        if (size == n2) {
            b2 = 4;
        }
        else {
            b2 = 0;
        }
        byte b3 = b2;
        if (b) {
            b3 = (byte)(b2 | 0x1);
        }
        this.frameHeader(n, n2, (byte)1, b3);
        this.sink.write(this.hpackBuffer, n2);
        if (size > n2) {
            this.writeContinuationFrames(n, size - n2);
        }
    }
    
    public int maxDataLength() {
        return this.maxFrameSize;
    }
    
    public void ping(final boolean b, final int n, final int n2) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        byte b2;
        if (b) {
            b2 = 1;
        }
        else {
            b2 = 0;
        }
        this.frameHeader(0, 8, (byte)6, b2);
        this.sink.writeInt(n);
        this.sink.writeInt(n2);
        this.sink.flush();
    }
    // monitorexit(this)
    
    public void pushPromise(final int n, final int n2, final List<Header> list) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final List<Header> list2;
        this.hpackWriter.writeHeaders(list2);
        final long size = this.hpackBuffer.size();
        final int n3 = (int)Math.min(this.maxFrameSize - 4, size);
        byte b;
        if (size == n3) {
            b = 4;
        }
        else {
            b = 0;
        }
        this.frameHeader(n, n3 + 4, (byte)5, b);
        this.sink.writeInt(Integer.MAX_VALUE & n2);
        this.sink.write(this.hpackBuffer, n3);
        if (size > n3) {
            this.writeContinuationFrames(n, size - n3);
        }
    }
    // monitorexit(this)
    
    public void rstStream(final int n, final ErrorCode errorCode) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final ErrorCode errorCode2;
        if (errorCode2.httpCode == -1) {
            throw new IllegalArgumentException();
        }
        this.frameHeader(n, 4, (byte)3, (byte)0);
        this.sink.writeInt(errorCode2.httpCode);
        this.sink.flush();
    }
    // monitorexit(this)
    
    public void settings(final Settings settings) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final Settings settings2;
        this.frameHeader(0, settings2.size() * 6, (byte)4, (byte)0);
        for (int i = 0; i < 10; ++i) {
            if (settings2.isSet(i)) {
                final int n = i;
                int n2;
                if (n == 4) {
                    n2 = 3;
                }
                else if ((n2 = n) == 7) {
                    n2 = 4;
                }
                this.sink.writeShort(n2);
                this.sink.writeInt(settings2.get(i));
            }
        }
        this.sink.flush();
    }
    // monitorexit(this)
    
    public void synReply(final boolean b, final int n, final List<Header> list) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final List<Header> list2;
        this.headers(b, n, list2);
    }
    // monitorexit(this)
    
    public void synStream(final boolean b, final int n, final int n2, final List<Header> list) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        final List<Header> list2;
        this.headers(b, n, list2);
    }
    // monitorexit(this)
    
    public void windowUpdate(final int n, final long n2) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
        }
        if (n2 == 0L || n2 > 2147483647L) {
            throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", n2);
        }
        this.frameHeader(n, 4, (byte)8, (byte)0);
        this.sink.writeInt((int)n2);
        this.sink.flush();
    }
    // monitorexit(this)
}
