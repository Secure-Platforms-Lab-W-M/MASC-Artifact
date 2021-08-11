// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

import java.net.SocketTimeoutException;
import okio.AsyncTimeout;
import java.io.EOFException;
import okio.Buffer;
import java.io.InterruptedIOException;
import java.util.Collection;
import java.util.ArrayList;
import okio.BufferedSource;
import okio.Timeout;
import okio.Source;
import okio.Sink;
import java.io.IOException;
import java.util.List;

public final class Http2Stream
{
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    ErrorCode errorCode;
    private boolean hasResponseHeaders;
    final int id;
    final StreamTimeout readTimeout;
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final FramingSink sink;
    private final FramingSource source;
    long unacknowledgedBytesRead;
    final StreamTimeout writeTimeout;
    
    Http2Stream(final int id, final Http2Connection connection, final boolean finished, final boolean finished2, final List<Header> requestHeaders) {
        this.unacknowledgedBytesRead = 0L;
        this.readTimeout = new StreamTimeout();
        this.writeTimeout = new StreamTimeout();
        this.errorCode = null;
        if (connection == null) {
            throw new NullPointerException("connection == null");
        }
        if (requestHeaders == null) {
            throw new NullPointerException("requestHeaders == null");
        }
        this.id = id;
        this.connection = connection;
        this.bytesLeftInWriteWindow = connection.peerSettings.getInitialWindowSize();
        this.source = new FramingSource(connection.okHttpSettings.getInitialWindowSize());
        this.sink = new FramingSink();
        this.source.finished = finished2;
        this.sink.finished = finished;
        this.requestHeaders = requestHeaders;
    }
    
    private boolean closeInternal(final ErrorCode errorCode) {
        assert !Thread.holdsLock(this);
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
        }
        final ErrorCode errorCode2;
        this.errorCode = errorCode2;
        this.notifyAll();
        // monitorexit(this)
        this.connection.removeStream(this.id);
        return true;
    }
    
    void addBytesToWriteWindow(final long n) {
        this.bytesLeftInWriteWindow += n;
        if (n > 0L) {
            this.notifyAll();
        }
    }
    
    void cancelStreamIfNecessary() throws IOException {
        assert !Thread.holdsLock(this);
        while (true) {
            while (true) {
                Label_0112: {
                    final boolean open;
                    synchronized (this) {
                        if (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed)) {
                            break Label_0112;
                        }
                        final int n = 0;
                        open = this.isOpen();
                        // monitorexit(this)
                        if (n != 0) {
                            this.close(ErrorCode.CANCEL);
                            return;
                        }
                    }
                    if (!open) {
                        break;
                    }
                    return;
                }
                final int n = 1;
                continue;
            }
        }
        this.connection.removeStream(this.id);
    }
    
    void checkOutNotClosed() throws IOException {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        }
        if (this.sink.finished) {
            throw new IOException("stream finished");
        }
        if (this.errorCode != null) {
            throw new StreamResetException(this.errorCode);
        }
    }
    
    public void close(final ErrorCode errorCode) throws IOException {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynReset(this.id, errorCode);
    }
    
    public void closeLater(final ErrorCode errorCode) {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynResetLater(this.id, errorCode);
    }
    
    public Http2Connection getConnection() {
        return this.connection;
    }
    
    public ErrorCode getErrorCode() {
        synchronized (this) {
            return this.errorCode;
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }
    
    public Sink getSink() {
        synchronized (this) {
            if (!this.hasResponseHeaders && !this.isLocallyInitiated()) {
                throw new IllegalStateException("reply before requesting the sink");
            }
        }
        // monitorexit(this)
        return this.sink;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public boolean isLocallyInitiated() {
        return this.connection.client == ((this.id & 0x1) == 0x1);
    }
    
    public boolean isOpen() {
        boolean b = false;
        synchronized (this) {
            if (this.errorCode == null && ((!this.source.finished && !this.source.closed) || (!this.sink.finished && !this.sink.closed) || !this.hasResponseHeaders)) {
                b = true;
            }
            return b;
        }
    }
    
    public Timeout readTimeout() {
        return this.readTimeout;
    }
    
    void receiveData(final BufferedSource bufferedSource, final int n) throws IOException {
        assert !Thread.holdsLock(this);
        this.source.receive(bufferedSource, n);
    }
    
    void receiveFin() {
        assert !Thread.holdsLock(this);
        synchronized (this) {
            this.source.finished = true;
            final boolean open = this.isOpen();
            this.notifyAll();
            // monitorexit(this)
            if (!open) {
                this.connection.removeStream(this.id);
            }
        }
    }
    
    void receiveHeaders(final List<Header> responseHeaders) {
        assert !Thread.holdsLock(this);
        boolean open = true;
        synchronized (this) {
            this.hasResponseHeaders = true;
            if (this.responseHeaders == null) {
                this.responseHeaders = responseHeaders;
                open = this.isOpen();
                this.notifyAll();
            }
            else {
                final ArrayList<Object> responseHeaders2 = new ArrayList<Object>();
                responseHeaders2.addAll(this.responseHeaders);
                responseHeaders2.add(null);
                responseHeaders2.addAll(responseHeaders);
                this.responseHeaders = (List<Header>)responseHeaders2;
            }
            // monitorexit(this)
            if (!open) {
                this.connection.removeStream(this.id);
            }
        }
    }
    
    void receiveRstStream(final ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode == null) {
                this.errorCode = errorCode;
                this.notifyAll();
            }
        }
    }
    
    public void sendResponseHeaders(final List<Header> list, final boolean b) throws IOException {
        assert !Thread.holdsLock(this);
        if (list == null) {
            throw new NullPointerException("responseHeaders == null");
        }
        boolean b2 = false;
        synchronized (this) {
            this.hasResponseHeaders = true;
            if (!b) {
                this.sink.finished = true;
                b2 = true;
            }
            // monitorexit(this)
            this.connection.writeSynReply(this.id, b2, list);
            if (b2) {
                this.connection.flush();
            }
        }
    }
    
    public List<Header> takeResponseHeaders() throws IOException {
        synchronized (this) {
            if (!this.isLocallyInitiated()) {
                throw new IllegalStateException("servers cannot read response headers");
            }
        }
        this.readTimeout.enter();
        try {
            while (this.responseHeaders == null && this.errorCode == null) {
                this.waitForIo();
            }
        }
        finally {
            this.readTimeout.exitAndThrowIfTimedOut();
        }
        this.readTimeout.exitAndThrowIfTimedOut();
        final List<Header> responseHeaders = this.responseHeaders;
        if (responseHeaders != null) {
            this.responseHeaders = null;
            // monitorexit(this)
            return responseHeaders;
        }
        throw new StreamResetException(this.errorCode);
    }
    
    void waitForIo() throws InterruptedIOException {
        try {
            this.wait();
        }
        catch (InterruptedException ex) {
            throw new InterruptedIOException();
        }
    }
    
    public Timeout writeTimeout() {
        return this.writeTimeout;
    }
    
    final class FramingSink implements Sink
    {
        private static final long EMIT_BUFFER_SIZE = 16384L;
        boolean closed;
        boolean finished;
        private final Buffer sendBuffer;
        
        FramingSink() {
            this.sendBuffer = new Buffer();
        }
        
        private void emitFrame(final boolean b) throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.writeTimeout.enter();
                try {
                    while (Http2Stream.this.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                        Http2Stream.this.waitForIo();
                    }
                }
                finally {
                    Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                }
            }
            Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
            Http2Stream.this.checkOutNotClosed();
            final long min = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
            final Http2Stream this$0 = Http2Stream.this;
            this$0.bytesLeftInWriteWindow -= min;
            // monitorexit(http2Stream)
            Http2Stream.this.writeTimeout.enter();
            try {
                Http2Stream.this.connection.writeData(Http2Stream.this.id, b && min == this.sendBuffer.size(), this.sendBuffer, min);
            }
            finally {
                Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
            }
        }
        
        @Override
        public void close() throws IOException {
            assert !Thread.holdsLock(Http2Stream.this);
            Label_0113: {
                synchronized (Http2Stream.this) {
                    if (this.closed) {
                        return;
                    }
                    // monitorexit(this.this$0)
                    if (Http2Stream.this.sink.finished) {
                        break Label_0113;
                    }
                    if (this.sendBuffer.size() > 0L) {
                        while (this.sendBuffer.size() > 0L) {
                            this.emitFrame(true);
                        }
                        break Label_0113;
                    }
                }
                Http2Stream.this.connection.writeData(Http2Stream.this.id, true, null, 0L);
            }
            synchronized (Http2Stream.this) {
                this.closed = true;
                // monitorexit(this.this$0)
                Http2Stream.this.connection.flush();
                Http2Stream.this.cancelStreamIfNecessary();
            }
        }
        
        @Override
        public void flush() throws IOException {
            assert !Thread.holdsLock(Http2Stream.this);
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed();
                // monitorexit(this.this$0)
                while (this.sendBuffer.size() > 0L) {
                    this.emitFrame(false);
                    Http2Stream.this.connection.flush();
                }
            }
        }
        
        @Override
        public Timeout timeout() {
            return Http2Stream.this.writeTimeout;
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            assert !Thread.holdsLock(Http2Stream.this);
            this.sendBuffer.write(buffer, n);
            while (this.sendBuffer.size() >= 16384L) {
                this.emitFrame(false);
            }
        }
    }
    
    private final class FramingSource implements Source
    {
        static final /* synthetic */ boolean $assertionsDisabled;
        boolean closed;
        boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer;
        private final Buffer receiveBuffer;
        
        FramingSource(final long maxByteCount) {
            this.receiveBuffer = new Buffer();
            this.readBuffer = new Buffer();
            this.maxByteCount = maxByteCount;
        }
        
        private void checkNotClosed() throws IOException {
            if (this.closed) {
                throw new IOException("stream closed");
            }
            if (Http2Stream.this.errorCode != null) {
                throw new StreamResetException(Http2Stream.this.errorCode);
            }
        }
        
        private void waitUntilReadable() throws IOException {
            Http2Stream.this.readTimeout.enter();
            try {
                while (this.readBuffer.size() == 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                    Http2Stream.this.waitForIo();
                }
            }
            finally {
                Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
            }
            Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
        }
        
        @Override
        public void close() throws IOException {
            synchronized (Http2Stream.this) {
                this.closed = true;
                this.readBuffer.clear();
                Http2Stream.this.notifyAll();
                // monitorexit(this.this$0)
                Http2Stream.this.cancelStreamIfNecessary();
            }
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            if (read < 0L) {
                throw new IllegalArgumentException("byteCount < 0: " + read);
            }
            synchronized (Http2Stream.this) {
                this.waitUntilReadable();
                this.checkNotClosed();
                if (this.readBuffer.size() == 0L) {
                    return -1L;
                }
                read = this.readBuffer.read(buffer, Math.min(read, this.readBuffer.size()));
                final Http2Stream this$0 = Http2Stream.this;
                this$0.unacknowledgedBytesRead += read;
                if (Http2Stream.this.unacknowledgedBytesRead >= Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2) {
                    Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.id, Http2Stream.this.unacknowledgedBytesRead);
                    Http2Stream.this.unacknowledgedBytesRead = 0L;
                }
                // monitorexit(this.this$0)
                synchronized (Http2Stream.this.connection) {
                    final Http2Connection connection = Http2Stream.this.connection;
                    connection.unacknowledgedBytesRead += read;
                    if (Http2Stream.this.connection.unacknowledgedBytesRead >= Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2) {
                        Http2Stream.this.connection.writeWindowUpdateLater(0, Http2Stream.this.connection.unacknowledgedBytesRead);
                        Http2Stream.this.connection.unacknowledgedBytesRead = 0L;
                    }
                    return read;
                }
            }
        }
        
        void receive(final BufferedSource bufferedSource, long read) throws IOException {
            long n = read;
            Label_0087: {
                if (FramingSource.$assertionsDisabled) {
                    break Label_0087;
                }
                n = read;
                if (Thread.holdsLock(Http2Stream.this)) {
                    throw new AssertionError();
                }
                break Label_0087;
                while (true) {
                    n -= read;
                    synchronized (Http2Stream.this) {
                        int n2;
                        if (this.readBuffer.size() == 0L) {
                            n2 = 1;
                        }
                        else {
                            n2 = 0;
                        }
                        this.readBuffer.writeAll(this.receiveBuffer);
                        if (n2 != 0) {
                            Http2Stream.this.notifyAll();
                        }
                        // monitorexit(this.this$0)
                        if (n <= 0L) {
                            return;
                        }
                        final Http2Stream this$0 = Http2Stream.this;
                        final boolean finished;
                        synchronized (Http2Stream.this) {
                            finished = this.finished;
                            int n3;
                            if (this.readBuffer.size() + n > this.maxByteCount) {
                                n3 = 1;
                            }
                            else {
                                n3 = 0;
                            }
                            // monitorexit(this.this$0)
                            if (n3 != 0) {
                                bufferedSource.skip(n);
                                Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                                return;
                            }
                        }
                        final BufferedSource bufferedSource2;
                        if (finished) {
                            bufferedSource2.skip(n);
                            return;
                        }
                        read = bufferedSource2.read(this.receiveBuffer, n);
                        if (read == -1L) {
                            throw new EOFException();
                        }
                        continue;
                    }
                }
            }
        }
        
        @Override
        public Timeout timeout() {
            return Http2Stream.this.readTimeout;
        }
    }
    
    class StreamTimeout extends AsyncTimeout
    {
        public void exitAndThrowIfTimedOut() throws IOException {
            if (this.exit()) {
                throw this.newTimeoutException(null);
            }
        }
        
        @Override
        protected IOException newTimeoutException(final IOException ex) {
            final SocketTimeoutException ex2 = new SocketTimeoutException("timeout");
            if (ex != null) {
                ex2.initCause(ex);
            }
            return ex2;
        }
        
        @Override
        protected void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
        }
    }
}
