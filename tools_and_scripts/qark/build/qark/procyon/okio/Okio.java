// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.util.logging.Level;
import java.net.SocketTimeoutException;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.FileInputStream;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.net.Socket;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.logging.Logger;

public final class Okio
{
    static final Logger logger;
    
    static {
        logger = Logger.getLogger(Okio.class.getName());
    }
    
    private Okio() {
    }
    
    public static Sink appendingSink(final File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file, true));
    }
    
    public static Sink blackhole() {
        return new Sink() {
            @Override
            public void close() throws IOException {
            }
            
            @Override
            public void flush() throws IOException {
            }
            
            @Override
            public Timeout timeout() {
                return Timeout.NONE;
            }
            
            @Override
            public void write(final Buffer buffer, final long n) throws IOException {
                buffer.skip(n);
            }
        };
    }
    
    public static BufferedSink buffer(final Sink sink) {
        return new RealBufferedSink(sink);
    }
    
    public static BufferedSource buffer(final Source source) {
        return new RealBufferedSource(source);
    }
    
    static boolean isAndroidGetsocknameError(final AssertionError assertionError) {
        return assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed");
    }
    
    public static Sink sink(final File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file));
    }
    
    public static Sink sink(final OutputStream outputStream) {
        return sink(outputStream, new Timeout());
    }
    
    private static Sink sink(final OutputStream outputStream, final Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Sink() {
            @Override
            public void close() throws IOException {
                outputStream.close();
            }
            
            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }
            
            @Override
            public Timeout timeout() {
                return timeout;
            }
            
            @Override
            public String toString() {
                return "sink(" + outputStream + ")";
            }
            
            @Override
            public void write(final Buffer buffer, long n) throws IOException {
                Util.checkOffsetAndCount(buffer.size, 0L, n);
                while (n > 0L) {
                    timeout.throwIfReached();
                    final Segment head = buffer.head;
                    final int n2 = (int)Math.min(n, head.limit - head.pos);
                    outputStream.write(head.data, head.pos, n2);
                    head.pos += n2;
                    final long n3 = n - n2;
                    buffer.size -= n2;
                    n = n3;
                    if (head.pos == head.limit) {
                        buffer.head = head.pop();
                        SegmentPool.recycle(head);
                        n = n3;
                    }
                }
            }
        };
    }
    
    public static Sink sink(final Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getOutputStream() == null) {
            throw new IOException("socket's output stream == null");
        }
        final AsyncTimeout timeout = timeout(socket);
        return timeout.sink(sink(socket.getOutputStream(), timeout));
    }
    
    @IgnoreJRERequirement
    public static Sink sink(final Path path, final OpenOption... array) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return sink(Files.newOutputStream(path, array));
    }
    
    public static Source source(final File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return source(new FileInputStream(file));
    }
    
    public static Source source(final InputStream inputStream) {
        return source(inputStream, new Timeout());
    }
    
    private static Source source(final InputStream inputStream, final Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Source() {
            @Override
            public void close() throws IOException {
                inputStream.close();
            }
            
            @Override
            public long read(final Buffer buffer, final long n) throws IOException {
                if (n < 0L) {
                    throw new IllegalArgumentException("byteCount < 0: " + n);
                }
                if (n == 0L) {
                    return 0L;
                }
                try {
                    timeout.throwIfReached();
                    final Segment writableSegment = buffer.writableSegment(1);
                    final int read = inputStream.read(writableSegment.data, writableSegment.limit, (int)Math.min(n, 8192 - writableSegment.limit));
                    if (read == -1) {
                        return -1L;
                    }
                    writableSegment.limit += read;
                    buffer.size += read;
                    return read;
                }
                catch (AssertionError assertionError) {
                    if (Okio.isAndroidGetsocknameError(assertionError)) {
                        throw new IOException(assertionError);
                    }
                    throw assertionError;
                }
            }
            
            @Override
            public Timeout timeout() {
                return timeout;
            }
            
            @Override
            public String toString() {
                return "source(" + inputStream + ")";
            }
        };
    }
    
    public static Source source(final Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getInputStream() == null) {
            throw new IOException("socket's input stream == null");
        }
        final AsyncTimeout timeout = timeout(socket);
        return timeout.source(source(socket.getInputStream(), timeout));
    }
    
    @IgnoreJRERequirement
    public static Source source(final Path path, final OpenOption... array) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return source(Files.newInputStream(path, array));
    }
    
    private static AsyncTimeout timeout(final Socket socket) {
        return new AsyncTimeout() {
            @Override
            protected IOException newTimeoutException(@Nullable final IOException ex) {
                final SocketTimeoutException ex2 = new SocketTimeoutException("timeout");
                if (ex != null) {
                    ex2.initCause(ex);
                }
                return ex2;
            }
            
            @Override
            protected void timedOut() {
                try {
                    socket.close();
                }
                catch (Exception ex) {
                    Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, ex);
                }
                catch (AssertionError assertionError) {
                    if (Okio.isAndroidGetsocknameError(assertionError)) {
                        Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, assertionError);
                        return;
                    }
                    throw assertionError;
                }
            }
        };
    }
}
