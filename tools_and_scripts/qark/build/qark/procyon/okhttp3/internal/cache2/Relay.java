// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.cache2;

import okio.Timeout;
import java.io.Closeable;
import okhttp3.internal.Util;
import java.io.IOException;
import java.io.File;
import okio.Source;
import java.io.RandomAccessFile;
import okio.Buffer;
import okio.ByteString;

final class Relay
{
    private static final long FILE_HEADER_SIZE = 32L;
    static final ByteString PREFIX_CLEAN;
    static final ByteString PREFIX_DIRTY;
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    final Buffer buffer;
    final long bufferMaxSize;
    boolean complete;
    RandomAccessFile file;
    private final ByteString metadata;
    int sourceCount;
    Source upstream;
    final Buffer upstreamBuffer;
    long upstreamPos;
    Thread upstreamReader;
    
    static {
        PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
        PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
    }
    
    private Relay(final RandomAccessFile file, final Source upstream, final long upstreamPos, final ByteString metadata, final long bufferMaxSize) {
        this.upstreamBuffer = new Buffer();
        this.buffer = new Buffer();
        this.file = file;
        this.upstream = upstream;
        this.complete = (upstream == null);
        this.upstreamPos = upstreamPos;
        this.metadata = metadata;
        this.bufferMaxSize = bufferMaxSize;
    }
    
    public static Relay edit(final File file, final Source source, final ByteString byteString, final long n) throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final Relay relay = new Relay(randomAccessFile, source, 0L, byteString, n);
        randomAccessFile.setLength(0L);
        relay.writeHeader(Relay.PREFIX_DIRTY, -1L, -1L);
        return relay;
    }
    
    public static Relay read(final File file) throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
        final Buffer buffer = new Buffer();
        fileOperator.read(0L, buffer, 32L);
        if (!buffer.readByteString(Relay.PREFIX_CLEAN.size()).equals(Relay.PREFIX_CLEAN)) {
            throw new IOException("unreadable cache file");
        }
        final long long1 = buffer.readLong();
        final long long2 = buffer.readLong();
        final Buffer buffer2 = new Buffer();
        fileOperator.read(32L + long1, buffer2, long2);
        return new Relay(randomAccessFile, null, long1, buffer2.readByteString(), 0L);
    }
    
    private void writeHeader(final ByteString byteString, final long n, final long n2) throws IOException {
        final Buffer buffer = new Buffer();
        buffer.write(byteString);
        buffer.writeLong(n);
        buffer.writeLong(n2);
        if (buffer.size() != 32L) {
            throw new IllegalArgumentException();
        }
        new FileOperator(this.file.getChannel()).write(0L, buffer, 32L);
    }
    
    private void writeMetadata(final long n) throws IOException {
        final Buffer buffer = new Buffer();
        buffer.write(this.metadata);
        new FileOperator(this.file.getChannel()).write(32L + n, buffer, this.metadata.size());
    }
    
    void commit(final long n) throws IOException {
        this.writeMetadata(n);
        this.file.getChannel().force(false);
        this.writeHeader(Relay.PREFIX_CLEAN, n, this.metadata.size());
        this.file.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
            // monitorexit(this)
            Util.closeQuietly(this.upstream);
            this.upstream = null;
        }
    }
    
    boolean isClosed() {
        return this.file == null;
    }
    
    public ByteString metadata() {
        return this.metadata;
    }
    
    public Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            ++this.sourceCount;
            // monitorexit(this)
            return new RelaySource();
        }
    }
    
    class RelaySource implements Source
    {
        private FileOperator fileOperator;
        private long sourcePos;
        final /* synthetic */ Relay this$0;
        private final Timeout timeout;
        
        RelaySource() {
            this.timeout = new Timeout();
            this.fileOperator = new FileOperator(Relay.this.file.getChannel());
        }
        
        @Override
        public void close() throws IOException {
            if (this.fileOperator != null) {
                this.fileOperator = null;
                Closeable file = null;
                synchronized (Relay.this) {
                    final Relay this$0 = Relay.this;
                    --this$0.sourceCount;
                    if (Relay.this.sourceCount == 0) {
                        file = Relay.this.file;
                        Relay.this.file = null;
                    }
                    // monitorexit(this.this$0)
                    if (file != null) {
                        Util.closeQuietly(file);
                    }
                }
            }
        }
        
        @Override
        public long read(Buffer buffer, long n) throws IOException {
            if (this.fileOperator == null) {
                throw new IllegalStateException("closed");
            }
            // monitorexit(relay)
            long upstreamPos = 0L;
            long n4;
            while (true) {
                Label_0159: {
                    Label_0096: {
                        synchronized (Relay.this) {
                            while (true) {
                                final long sourcePos = this.sourcePos;
                                upstreamPos = Relay.this.upstreamPos;
                                if (sourcePos != upstreamPos) {
                                    break Label_0159;
                                }
                                if (Relay.this.complete) {
                                    break;
                                }
                                if (Relay.this.upstreamReader == null) {
                                    break Label_0096;
                                }
                                this.timeout.waitUntilNotified(Relay.this);
                            }
                            return -1L;
                        }
                    }
                    Relay.this.upstreamReader = Thread.currentThread();
                    final int n2 = 1;
                    if (n2 == 2) {
                        n = Math.min(n, upstreamPos - this.sourcePos);
                        this.fileOperator.read(32L + this.sourcePos, buffer, n);
                        this.sourcePos += n;
                        return n;
                    }
                    try {
                        final long read = Relay.this.upstream.read(Relay.this.upstreamBuffer, Relay.this.bufferMaxSize);
                        if (read == -1L) {
                            Relay.this.commit(upstreamPos);
                            synchronized (Relay.this) {
                                Relay.this.upstreamReader = null;
                                Relay.this.notifyAll();
                                return -1L;
                            }
                        }
                        n = Math.min(read, n);
                        Relay.this.upstreamBuffer.copyTo(buffer, 0L, n);
                        this.sourcePos += n;
                        this.fileOperator.write(32L + upstreamPos, Relay.this.upstreamBuffer.clone(), read);
                        buffer = (Buffer)Relay.this;
                        synchronized (buffer) {
                            Relay.this.buffer.write(Relay.this.upstreamBuffer, read);
                            if (Relay.this.buffer.size() > Relay.this.bufferMaxSize) {
                                Relay.this.buffer.skip(Relay.this.buffer.size() - Relay.this.bufferMaxSize);
                            }
                            final Relay this$0 = Relay.this;
                            this$0.upstreamPos += read;
                            // monitorexit(buffer)
                            buffer = (Buffer)Relay.this;
                            // monitorenter(buffer)
                            final RelaySource relaySource = this;
                            final Relay relay2 = relaySource.this$0;
                            final Thread thread = null;
                            relay2.upstreamReader = thread;
                            final RelaySource relaySource2 = this;
                            final Relay relay3 = relaySource2.this$0;
                            relay3.notifyAll();
                            return n;
                        }
                    }
                    finally {
                        buffer = (Buffer)Relay.this;
                        synchronized (buffer) {
                            Relay.this.upstreamReader = null;
                            Relay.this.notifyAll();
                        }
                        // monitorexit(buffer)
                    }
                    try {
                        final RelaySource relaySource = this;
                        final Relay relay2 = relaySource.this$0;
                        final Thread thread = null;
                        relay2.upstreamReader = thread;
                        final RelaySource relaySource2 = this;
                        final Relay relay3 = relaySource2.this$0;
                        relay3.notifyAll();
                        return n;
                    }
                    finally {}
                }
                n4 = upstreamPos - Relay.this.buffer.size();
                if (this.sourcePos < n4) {
                    final int n2 = 2;
                    // monitorexit(relay)
                    continue;
                }
                break;
            }
            n = Math.min(n, upstreamPos - this.sourcePos);
            Relay.this.buffer.copyTo(buffer, this.sourcePos - n4, n);
            this.sourcePos += n;
            // monitorexit(relay)
            return n;
        }
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
    }
}
