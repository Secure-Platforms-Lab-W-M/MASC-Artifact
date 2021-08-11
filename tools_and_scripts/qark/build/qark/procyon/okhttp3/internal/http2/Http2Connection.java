// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

import okhttp3.internal.platform.Platform;
import okio.ByteString;
import okio.Okio;
import java.net.InetSocketAddress;
import okio.BufferedSink;
import java.io.InterruptedIOException;
import java.util.concurrent.RejectedExecutionException;
import okhttp3.internal.NamedRunnable;
import okio.Buffer;
import okio.BufferedSource;
import okhttp3.Protocol;
import java.util.List;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import okhttp3.internal.Util;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Map;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.io.Closeable;

public final class Http2Connection implements Closeable
{
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    private static final ExecutorService listenerExecutor;
    private boolean awaitingPong;
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    int nextStreamId;
    Settings okHttpSettings;
    final Settings peerSettings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams;
    long unacknowledgedBytesRead;
    final Http2Writer writer;
    private final ScheduledExecutorService writerExecutor;
    
    static {
        listenerExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Http2Connection", true));
    }
    
    Http2Connection(final Builder builder) {
        this.streams = new LinkedHashMap<Integer, Http2Stream>();
        this.unacknowledgedBytesRead = 0L;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = false;
        this.currentPushRequests = new LinkedHashSet<Integer>();
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        int nextStreamId;
        if (builder.client) {
            nextStreamId = 1;
        }
        else {
            nextStreamId = 2;
        }
        this.nextStreamId = nextStreamId;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", this.hostname), false));
        if (builder.pingIntervalMillis != 0) {
            this.writerExecutor.scheduleAtFixedRate(new PingRunnable(false, 0, 0), builder.pingIntervalMillis, builder.pingIntervalMillis, TimeUnit.MILLISECONDS);
        }
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, 65535);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }
    
    private void failConnection() {
        try {
            this.close(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR);
        }
        catch (IOException ex) {}
    }
    
    private Http2Stream newStream(final int n, final List<Header> list, final boolean b) throws IOException {
        // monitorexit(this)
        // monitorexit(http2Writer)
        while (true) {
            Label_0061: {
                if (b) {
                    break Label_0061;
                }
                final boolean b2 = true;
                Label_0067: {
                    synchronized (this.writer) {
                        synchronized (this) {
                            if (this.nextStreamId > 1073741823) {
                                this.shutdown(ErrorCode.REFUSED_STREAM);
                            }
                            if (this.shutdown) {
                                throw new ConnectionShutdownException();
                            }
                            break Label_0067;
                        }
                    }
                    break Label_0061;
                }
                final int nextStreamId = this.nextStreamId;
                this.nextStreamId += 2;
                final List<Header> list2;
                final Http2Stream http2Stream = new Http2Stream(nextStreamId, this, b2, false, list2);
                int n2;
                if (b && this.bytesLeftInWriteWindow != 0L && http2Stream.bytesLeftInWriteWindow != 0L) {
                    n2 = 0;
                }
                else {
                    n2 = 1;
                }
                if (http2Stream.isOpen()) {
                    this.streams.put(nextStreamId, http2Stream);
                }
                if (n == 0) {
                    this.writer.synStream(b2, nextStreamId, n, list2);
                }
                else {
                    if (this.client) {
                        throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                    }
                    this.writer.pushPromise(n, nextStreamId, list2);
                }
                if (n2 != 0) {
                    this.writer.flush();
                }
                return http2Stream;
            }
            final boolean b2 = false;
            continue;
        }
    }
    
    void addBytesToWriteWindow(final long n) {
        this.bytesLeftInWriteWindow += n;
        if (n > 0L) {
            this.notifyAll();
        }
    }
    
    void awaitPong() throws IOException, InterruptedException {
        synchronized (this) {
            while (this.awaitingPong) {
                this.wait();
            }
        }
    }
    // monitorexit(this)
    
    @Override
    public void close() throws IOException {
        this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }
    
    void close(final ErrorCode p0, final ErrorCode p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     6: aload_0        
        //     7: invokestatic    java/lang/Thread.holdsLock:(Ljava/lang/Object;)Z
        //    10: ifeq            21
        //    13: new             Ljava/lang/AssertionError;
        //    16: dup            
        //    17: invokespecial   java/lang/AssertionError.<init>:()V
        //    20: athrow         
        //    21: aconst_null    
        //    22: astore          5
        //    24: aload_0        
        //    25: aload_1        
        //    26: invokevirtual   okhttp3/internal/http2/Http2Connection.shutdown:(Lokhttp3/internal/http2/ErrorCode;)V
        //    29: aload           5
        //    31: astore_1       
        //    32: aconst_null    
        //    33: astore          6
        //    35: aload_0        
        //    36: monitorenter   
        //    37: aload_0        
        //    38: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    41: invokeinterface java/util/Map.isEmpty:()Z
        //    46: ifne            89
        //    49: aload_0        
        //    50: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    53: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //    58: aload_0        
        //    59: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    62: invokeinterface java/util/Map.size:()I
        //    67: anewarray       Lokhttp3/internal/http2/Http2Stream;
        //    70: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    75: checkcast       [Lokhttp3/internal/http2/Http2Stream;
        //    78: astore          6
        //    80: aload_0        
        //    81: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    84: invokeinterface java/util/Map.clear:()V
        //    89: aload_0        
        //    90: monitorexit    
        //    91: aload_1        
        //    92: astore          5
        //    94: aload           6
        //    96: ifnull          165
        //    99: aload           6
        //   101: arraylength    
        //   102: istore          4
        //   104: iconst_0       
        //   105: istore_3       
        //   106: aload_1        
        //   107: astore          5
        //   109: iload_3        
        //   110: iload           4
        //   112: if_icmpge       165
        //   115: aload           6
        //   117: iload_3        
        //   118: aaload         
        //   119: astore          5
        //   121: aload           5
        //   123: aload_2        
        //   124: invokevirtual   okhttp3/internal/http2/Http2Stream.close:(Lokhttp3/internal/http2/ErrorCode;)V
        //   127: aload_1        
        //   128: astore          5
        //   130: iload_3        
        //   131: iconst_1       
        //   132: iadd           
        //   133: istore_3       
        //   134: aload           5
        //   136: astore_1       
        //   137: goto            106
        //   140: astore_1       
        //   141: goto            32
        //   144: astore_1       
        //   145: aload_0        
        //   146: monitorexit    
        //   147: aload_1        
        //   148: athrow         
        //   149: astore          7
        //   151: aload_1        
        //   152: astore          5
        //   154: aload_1        
        //   155: ifnull          130
        //   158: aload           7
        //   160: astore          5
        //   162: goto            130
        //   165: aload_0        
        //   166: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //   169: invokevirtual   okhttp3/internal/http2/Http2Writer.close:()V
        //   172: aload           5
        //   174: astore_1       
        //   175: aload_0        
        //   176: getfield        okhttp3/internal/http2/Http2Connection.socket:Ljava/net/Socket;
        //   179: invokevirtual   java/net/Socket.close:()V
        //   182: aload_0        
        //   183: getfield        okhttp3/internal/http2/Http2Connection.writerExecutor:Ljava/util/concurrent/ScheduledExecutorService;
        //   186: invokeinterface java/util/concurrent/ScheduledExecutorService.shutdown:()V
        //   191: aload_0        
        //   192: getfield        okhttp3/internal/http2/Http2Connection.pushExecutor:Ljava/util/concurrent/ExecutorService;
        //   195: invokeinterface java/util/concurrent/ExecutorService.shutdown:()V
        //   200: aload_1        
        //   201: ifnull          224
        //   204: aload_1        
        //   205: athrow         
        //   206: astore_2       
        //   207: aload           5
        //   209: astore_1       
        //   210: aload           5
        //   212: ifnonnull       175
        //   215: aload_2        
        //   216: astore_1       
        //   217: goto            175
        //   220: astore_1       
        //   221: goto            182
        //   224: return         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  24     29     140    144    Ljava/io/IOException;
        //  37     89     144    149    Any
        //  89     91     144    149    Any
        //  121    127    149    165    Ljava/io/IOException;
        //  145    147    144    149    Any
        //  165    172    206    220    Ljava/io/IOException;
        //  175    182    220    224    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 113, Size: 113
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }
    
    Http2Stream getStream(final int n) {
        synchronized (this) {
            return this.streams.get(n);
        }
    }
    
    public boolean isShutdown() {
        synchronized (this) {
            return this.shutdown;
        }
    }
    
    public int maxConcurrentStreams() {
        synchronized (this) {
            return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
        }
    }
    
    public Http2Stream newStream(final List<Header> list, final boolean b) throws IOException {
        return this.newStream(0, list, b);
    }
    
    public int openStreamCount() {
        synchronized (this) {
            return this.streams.size();
        }
    }
    
    void pushDataLater(final int n, final BufferedSource bufferedSource, final int n2, final boolean b) throws IOException {
        final Buffer buffer = new Buffer();
        bufferedSource.require(n2);
        bufferedSource.read(buffer, n2);
        if (buffer.size() != n2) {
            throw new IOException(buffer.size() + " != " + n2);
        }
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.hostname, n }) {
            public void execute() {
                try {
                    final boolean onData = Http2Connection.this.pushObserver.onData(n, buffer, n2, b);
                    if (onData) {
                        Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                    }
                    if (onData || b) {
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(n);
                        }
                    }
                }
                catch (IOException ex) {}
            }
        });
    }
    
    void pushHeadersLater(final int n, final List<Header> list, final boolean b) {
        try {
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[] { this.hostname, n }) {
                public void execute() {
                    final boolean onHeaders = Http2Connection.this.pushObserver.onHeaders(n, list, b);
                    while (true) {
                        if (onHeaders) {
                            try {
                                Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                                if (onHeaders || b) {
                                    synchronized (Http2Connection.this) {
                                        Http2Connection.this.currentPushRequests.remove(n);
                                    }
                                }
                            }
                            catch (IOException ex) {}
                            return;
                        }
                        continue;
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    void pushRequestLater(final int n, final List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(n)) {
                this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(n);
            // monitorexit(this)
            try {
                this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[] { this.hostname, n }) {
                    public void execute() {
                        if (Http2Connection.this.pushObserver.onRequest(n, list)) {
                            try {
                                Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                                synchronized (Http2Connection.this) {
                                    Http2Connection.this.currentPushRequests.remove(n);
                                }
                            }
                            catch (IOException ex) {}
                        }
                    }
                });
            }
            catch (RejectedExecutionException ex) {}
        }
    }
    
    void pushResetLater(final int n, final ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.hostname, n }) {
            public void execute() {
                Http2Connection.this.pushObserver.onReset(n, errorCode);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(n);
                }
            }
        });
    }
    
    public Http2Stream pushStream(final int n, final List<Header> list, final boolean b) throws IOException {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        return this.newStream(n, list, b);
    }
    
    boolean pushedStream(final int n) {
        return n != 0 && (n & 0x1) == 0x0;
    }
    
    Http2Stream removeStream(final int n) {
        synchronized (this) {
            final Http2Stream http2Stream = this.streams.remove(n);
            this.notifyAll();
            return http2Stream;
        }
    }
    
    public void setSettings(final Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
            }
        }
        final Settings settings2;
        this.okHttpSettings.merge(settings2);
        // monitorexit(this)
        this.writer.settings(settings2);
    }
    // monitorexit(http2Writer)
    
    public void shutdown(final ErrorCode p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: monitorenter   
        //     9: aload_0        
        //    10: getfield        okhttp3/internal/http2/Http2Connection.shutdown:Z
        //    13: ifeq            21
        //    16: aload_0        
        //    17: monitorexit    
        //    18: aload_3        
        //    19: monitorexit    
        //    20: return         
        //    21: aload_0        
        //    22: iconst_1       
        //    23: putfield        okhttp3/internal/http2/Http2Connection.shutdown:Z
        //    26: aload_0        
        //    27: getfield        okhttp3/internal/http2/Http2Connection.lastGoodStreamId:I
        //    30: istore_2       
        //    31: aload_0        
        //    32: monitorexit    
        //    33: aload_0        
        //    34: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //    37: iload_2        
        //    38: aload_1        
        //    39: getstatic       okhttp3/internal/Util.EMPTY_BYTE_ARRAY:[B
        //    42: invokevirtual   okhttp3/internal/http2/Http2Writer.goAway:(ILokhttp3/internal/http2/ErrorCode;[B)V
        //    45: aload_3        
        //    46: monitorexit    
        //    47: return         
        //    48: astore_1       
        //    49: aload_3        
        //    50: monitorexit    
        //    51: aload_1        
        //    52: athrow         
        //    53: astore_1       
        //    54: aload_0        
        //    55: monitorexit    
        //    56: aload_1        
        //    57: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      9      48     53     Any
        //  9      18     53     58     Any
        //  18     20     48     53     Any
        //  21     33     53     58     Any
        //  33     47     48     53     Any
        //  49     51     48     53     Any
        //  54     56     53     58     Any
        //  56     58     48     53     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0021:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void start() throws IOException {
        this.start(true);
    }
    
    void start(final boolean b) throws IOException {
        if (b) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            final int initialWindowSize = this.okHttpSettings.getInitialWindowSize();
            if (initialWindowSize != 65535) {
                this.writer.windowUpdate(0, initialWindowSize - 65535);
            }
        }
        new Thread(this.readerRunnable).start();
    }
    
    public void writeData(final int n, final boolean b, final Buffer buffer, final long n2) throws IOException {
        long n3 = n2;
        Label_0022: {
            if (n2 == 0L) {
                this.writer.data(b, n, buffer, 0);
            }
            else {
                Label_0098: {
                    break Label_0098;
                Label_0087_Outer:
                    while (true) {
                        while (true) {
                            Label_0164: {
                                while (true) {
                                    try {
                                        while (true) {
                                            final int min = Math.min((int)Math.min(n3, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                                            this.bytesLeftInWriteWindow -= min;
                                            // monitorexit(this)
                                            n3 -= min;
                                            final Http2Writer writer = this.writer;
                                            if (!b || n3 != 0L) {
                                                break Label_0164;
                                            }
                                            final boolean b2 = true;
                                            writer.data(b2, n, buffer, min);
                                            if (n3 <= 0L) {
                                                break Label_0022;
                                            }
                                            // monitorenter(this)
                                            try {
                                                if (this.bytesLeftInWriteWindow > 0L) {
                                                    continue Label_0087_Outer;
                                                }
                                                if (!this.streams.containsKey(n)) {
                                                    throw new IOException("stream closed");
                                                }
                                                break;
                                            }
                                            catch (InterruptedException ex) {
                                                throw new InterruptedIOException();
                                            }
                                        }
                                    }
                                    finally {
                                    }
                                    // monitorexit(this)
                                    this.wait();
                                    continue;
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    void writePing(final boolean b, final int n, final int n2) {
        if (!b) {
            synchronized (this) {
                final boolean awaitingPong = this.awaitingPong;
                this.awaitingPong = true;
                // monitorexit(this)
                if (awaitingPong) {
                    this.failConnection();
                    return;
                }
            }
        }
        try {
            this.writer.ping(b, n, n2);
        }
        catch (IOException ex) {
            this.failConnection();
        }
    }
    
    void writePingAndAwaitPong() throws IOException, InterruptedException {
        this.writePing(false, 1330343787, -257978967);
        this.awaitPong();
    }
    
    void writeSynReply(final int n, final boolean b, final List<Header> list) throws IOException {
        this.writer.synReply(b, n, list);
    }
    
    void writeSynReset(final int n, final ErrorCode errorCode) throws IOException {
        this.writer.rstStream(n, errorCode);
    }
    
    void writeSynResetLater(final int n, final ErrorCode errorCode) {
        try {
            this.writerExecutor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { this.hostname, n }) {
                public void execute() {
                    try {
                        Http2Connection.this.writeSynReset(n, errorCode);
                    }
                    catch (IOException ex) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    void writeWindowUpdateLater(final int n, final long n2) {
        try {
            this.writerExecutor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[] { this.hostname, n }) {
                public void execute() {
                    try {
                        Http2Connection.this.writer.windowUpdate(n, n2);
                    }
                    catch (IOException ex) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    public static class Builder
    {
        boolean client;
        String hostname;
        Listener listener;
        int pingIntervalMillis;
        PushObserver pushObserver;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;
        
        public Builder(final boolean client) {
            this.listener = Listener.REFUSE_INCOMING_STREAMS;
            this.pushObserver = PushObserver.CANCEL;
            this.client = client;
        }
        
        public Http2Connection build() {
            return new Http2Connection(this);
        }
        
        public Builder listener(final Listener listener) {
            this.listener = listener;
            return this;
        }
        
        public Builder pingIntervalMillis(final int pingIntervalMillis) {
            this.pingIntervalMillis = pingIntervalMillis;
            return this;
        }
        
        public Builder pushObserver(final PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }
        
        public Builder socket(final Socket socket) throws IOException {
            return this.socket(socket, ((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }
        
        public Builder socket(final Socket socket, final String hostname, final BufferedSource source, final BufferedSink sink) {
            this.socket = socket;
            this.hostname = hostname;
            this.source = source;
            this.sink = sink;
            return this;
        }
    }
    
    public abstract static class Listener
    {
        public static final Listener REFUSE_INCOMING_STREAMS;
        
        static {
            REFUSE_INCOMING_STREAMS = new Listener() {
                @Override
                public void onStream(final Http2Stream http2Stream) throws IOException {
                    http2Stream.close(ErrorCode.REFUSED_STREAM);
                }
            };
        }
        
        public void onSettings(final Http2Connection http2Connection) {
        }
        
        public abstract void onStream(final Http2Stream p0) throws IOException;
    }
    
    final class PingRunnable extends NamedRunnable
    {
        final int payload1;
        final int payload2;
        final boolean reply;
        
        PingRunnable(final boolean reply, final int payload1, final int payload2) {
            super("OkHttp %s ping %08x%08x", new Object[] { Http2Connection.this.hostname, payload1, payload2 });
            this.reply = reply;
            this.payload1 = payload1;
            this.payload2 = payload2;
        }
        
        public void execute() {
            Http2Connection.this.writePing(this.reply, this.payload1, this.payload2);
        }
    }
    
    class ReaderRunnable extends NamedRunnable implements Handler
    {
        final Http2Reader reader;
        
        ReaderRunnable(final Http2Reader reader) {
            super("OkHttp %s", new Object[] { Http2Connection.this.hostname });
            this.reader = reader;
        }
        
        private void applyAndAckSettings(final Settings settings) {
            try {
                Http2Connection.this.writerExecutor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[] { Http2Connection.this.hostname }) {
                    public void execute() {
                        try {
                            Http2Connection.this.writer.applyAndAckSettings(settings);
                        }
                        catch (IOException ex) {
                            Http2Connection.this.failConnection();
                        }
                    }
                });
            }
            catch (RejectedExecutionException ex) {}
        }
        
        @Override
        public void ackSettings() {
        }
        
        @Override
        public void alternateService(final int n, final String s, final ByteString byteString, final String s2, final int n2, final long n3) {
        }
        
        @Override
        public void data(final boolean b, final int n, final BufferedSource bufferedSource, final int n2) throws IOException {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushDataLater(n, bufferedSource, n2, b);
            }
            else {
                final Http2Stream stream = Http2Connection.this.getStream(n);
                if (stream == null) {
                    Http2Connection.this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                    bufferedSource.skip(n2);
                    return;
                }
                stream.receiveData(bufferedSource, n2);
                if (b) {
                    stream.receiveFin();
                }
            }
        }
        
        @Override
        protected void execute() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: astore_3       
            //     4: getstatic       okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //     7: astore          4
            //     9: aload_3        
            //    10: astore_2       
            //    11: aload_3        
            //    12: astore_1       
            //    13: aload_0        
            //    14: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    17: aload_0        
            //    18: invokevirtual   okhttp3/internal/http2/Http2Reader.readConnectionPreface:(Lokhttp3/internal/http2/Http2Reader$Handler;)V
            //    21: aload_3        
            //    22: astore_2       
            //    23: aload_3        
            //    24: astore_1       
            //    25: aload_0        
            //    26: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    29: iconst_0       
            //    30: aload_0        
            //    31: invokevirtual   okhttp3/internal/http2/Http2Reader.nextFrame:(ZLokhttp3/internal/http2/Http2Reader$Handler;)Z
            //    34: ifne            21
            //    37: aload_3        
            //    38: astore_2       
            //    39: aload_3        
            //    40: astore_1       
            //    41: getstatic       okhttp3/internal/http2/ErrorCode.NO_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    44: astore_3       
            //    45: aload_3        
            //    46: astore_2       
            //    47: aload_3        
            //    48: astore_1       
            //    49: getstatic       okhttp3/internal/http2/ErrorCode.CANCEL:Lokhttp3/internal/http2/ErrorCode;
            //    52: astore          5
            //    54: aload_0        
            //    55: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    58: aload_3        
            //    59: aload           5
            //    61: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //    64: aload_0        
            //    65: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    68: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //    71: return         
            //    72: astore_1       
            //    73: aload_2        
            //    74: astore_1       
            //    75: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    78: astore_2       
            //    79: aload_2        
            //    80: astore_1       
            //    81: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    84: astore_3       
            //    85: aload_0        
            //    86: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    89: aload_2        
            //    90: aload_3        
            //    91: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //    94: aload_0        
            //    95: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    98: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   101: return         
            //   102: astore_2       
            //   103: aload_0        
            //   104: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   107: aload_1        
            //   108: aload           4
            //   110: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //   113: aload_0        
            //   114: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //   117: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   120: aload_2        
            //   121: athrow         
            //   122: astore_1       
            //   123: goto            113
            //   126: astore_1       
            //   127: goto            94
            //   130: astore_1       
            //   131: goto            64
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  13     21     72     102    Ljava/io/IOException;
            //  13     21     102    126    Any
            //  25     37     72     102    Ljava/io/IOException;
            //  25     37     102    126    Any
            //  41     45     72     102    Ljava/io/IOException;
            //  41     45     102    126    Any
            //  49     54     72     102    Ljava/io/IOException;
            //  49     54     102    126    Any
            //  54     64     130    134    Ljava/io/IOException;
            //  75     79     102    126    Any
            //  81     85     102    126    Any
            //  85     94     126    130    Ljava/io/IOException;
            //  103    113    122    126    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0064:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public void goAway(final int n, ErrorCode this$0, final ByteString byteString) {
            if (byteString.size() > 0) {}
            this$0 = (ErrorCode)Http2Connection.this;
            synchronized (this$0) {
                final Http2Stream[] array = Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
                // monitorexit(this$0)
                for (int length = array.length, i = 0; i < length; ++i) {
                    this$0 = (ErrorCode)array[i];
                    if (((Http2Stream)this$0).getId() > n && ((Http2Stream)this$0).isLocallyInitiated()) {
                        ((Http2Stream)this$0).receiveRstStream(ErrorCode.REFUSED_STREAM);
                        Http2Connection.this.removeStream(((Http2Stream)this$0).getId());
                    }
                }
            }
        }
        
        @Override
        public void headers(final boolean b, final int lastGoodStreamId, final int n, final List<Header> list) {
            if (Http2Connection.this.pushedStream(lastGoodStreamId)) {
                Http2Connection.this.pushHeadersLater(lastGoodStreamId, list, b);
            }
            else {
                // monitorexit(http2Connection)
                final Http2Stream stream;
                final List<Header> list2;
                Label_0193: {
                    synchronized (Http2Connection.this) {
                        stream = Http2Connection.this.getStream(lastGoodStreamId);
                        if (stream != null) {
                            break Label_0193;
                        }
                        if (Http2Connection.this.shutdown) {
                            return;
                        }
                    }
                    if (lastGoodStreamId <= Http2Connection.this.lastGoodStreamId) {
                        // monitorexit(http2Connection)
                        return;
                    }
                    if (lastGoodStreamId % 2 == Http2Connection.this.nextStreamId % 2) {
                        // monitorexit(http2Connection)
                        return;
                    }
                    final Http2Stream http2Stream = new Http2Stream(lastGoodStreamId, Http2Connection.this, false, b, list2);
                    Http2Connection.this.lastGoodStreamId = lastGoodStreamId;
                    Http2Connection.this.streams.put(lastGoodStreamId, http2Stream);
                    Http2Connection.listenerExecutor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { Http2Connection.this.hostname, lastGoodStreamId }) {
                        public void execute() {
                            try {
                                Http2Connection.this.listener.onStream(http2Stream);
                            }
                            catch (IOException ex) {
                                Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, ex);
                                try {
                                    http2Stream.close(ErrorCode.PROTOCOL_ERROR);
                                }
                                catch (IOException ex2) {}
                            }
                        }
                    });
                    return;
                }
                // monitorexit(http2Connection)
                stream.receiveHeaders(list2);
                if (b) {
                    stream.receiveFin();
                }
            }
        }
        
        @Override
        public void ping(final boolean b, final int n, final int n2) {
            if (b) {
                synchronized (Http2Connection.this) {
                    Http2Connection.this.awaitingPong = false;
                    Http2Connection.this.notifyAll();
                    return;
                }
            }
            try {
                Http2Connection.this.writerExecutor.execute(new PingRunnable(true, n, n2));
            }
            catch (RejectedExecutionException ex) {}
        }
        
        @Override
        public void priority(final int n, final int n2, final int n3, final boolean b) {
        }
        
        @Override
        public void pushPromise(final int n, final int n2, final List<Header> list) {
            Http2Connection.this.pushRequestLater(n2, list);
        }
        
        @Override
        public void rstStream(final int n, final ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushResetLater(n, errorCode);
            }
            else {
                final Http2Stream removeStream = Http2Connection.this.removeStream(n);
                if (removeStream != null) {
                    removeStream.receiveRstStream(errorCode);
                }
            }
        }
        
        @Override
        public void settings(final boolean p0, final Settings p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: lstore          7
            //     3: aconst_null    
            //     4: astore          9
            //     6: aload_0        
            //     7: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    10: astore          10
            //    12: aload           10
            //    14: monitorenter   
            //    15: aload_0        
            //    16: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    19: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    22: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    25: istore_3       
            //    26: iload_1        
            //    27: ifeq            40
            //    30: aload_0        
            //    31: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    34: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    37: invokevirtual   okhttp3/internal/http2/Settings.clear:()V
            //    40: aload_0        
            //    41: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    44: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    47: aload_2        
            //    48: invokevirtual   okhttp3/internal/http2/Settings.merge:(Lokhttp3/internal/http2/Settings;)V
            //    51: aload_0        
            //    52: aload_2        
            //    53: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable.applyAndAckSettings:(Lokhttp3/internal/http2/Settings;)V
            //    56: aload_0        
            //    57: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    60: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    63: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    66: istore          4
            //    68: lload           7
            //    70: lstore          5
            //    72: aload           9
            //    74: astore_2       
            //    75: iload           4
            //    77: iconst_m1      
            //    78: if_icmpeq       190
            //    81: lload           7
            //    83: lstore          5
            //    85: aload           9
            //    87: astore_2       
            //    88: iload           4
            //    90: iload_3        
            //    91: if_icmpeq       190
            //    94: iload           4
            //    96: iload_3        
            //    97: isub           
            //    98: i2l            
            //    99: lstore          7
            //   101: aload_0        
            //   102: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   105: getfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //   108: ifne            128
            //   111: aload_0        
            //   112: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   115: lload           7
            //   117: invokevirtual   okhttp3/internal/http2/Http2Connection.addBytesToWriteWindow:(J)V
            //   120: aload_0        
            //   121: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   124: iconst_1       
            //   125: putfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //   128: lload           7
            //   130: lstore          5
            //   132: aload           9
            //   134: astore_2       
            //   135: aload_0        
            //   136: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   139: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   142: invokeinterface java/util/Map.isEmpty:()Z
            //   147: ifne            190
            //   150: aload_0        
            //   151: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   154: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   157: invokeinterface java/util/Map.values:()Ljava/util/Collection;
            //   162: aload_0        
            //   163: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   166: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   169: invokeinterface java/util/Map.size:()I
            //   174: anewarray       Lokhttp3/internal/http2/Http2Stream;
            //   177: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
            //   182: checkcast       [Lokhttp3/internal/http2/Http2Stream;
            //   185: astore_2       
            //   186: lload           7
            //   188: lstore          5
            //   190: invokestatic    okhttp3/internal/http2/Http2Connection.access$100:()Ljava/util/concurrent/ExecutorService;
            //   193: new             Lokhttp3/internal/http2/Http2Connection$ReaderRunnable$2;
            //   196: dup            
            //   197: aload_0        
            //   198: ldc_w           "OkHttp %s settings"
            //   201: iconst_1       
            //   202: anewarray       Ljava/lang/Object;
            //   205: dup            
            //   206: iconst_0       
            //   207: aload_0        
            //   208: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   211: getfield        okhttp3/internal/http2/Http2Connection.hostname:Ljava/lang/String;
            //   214: aastore        
            //   215: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable$2.<init>:(Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;Ljava/lang/String;[Ljava/lang/Object;)V
            //   218: invokeinterface java/util/concurrent/ExecutorService.execute:(Ljava/lang/Runnable;)V
            //   223: aload           10
            //   225: monitorexit    
            //   226: aload_2        
            //   227: ifnull          286
            //   230: lload           5
            //   232: lconst_0       
            //   233: lcmp           
            //   234: ifeq            286
            //   237: aload_2        
            //   238: arraylength    
            //   239: istore          4
            //   241: iconst_0       
            //   242: istore_3       
            //   243: iload_3        
            //   244: iload           4
            //   246: if_icmpge       286
            //   249: aload_2        
            //   250: iload_3        
            //   251: aaload         
            //   252: astore          9
            //   254: aload           9
            //   256: monitorenter   
            //   257: aload           9
            //   259: lload           5
            //   261: invokevirtual   okhttp3/internal/http2/Http2Stream.addBytesToWriteWindow:(J)V
            //   264: aload           9
            //   266: monitorexit    
            //   267: iload_3        
            //   268: iconst_1       
            //   269: iadd           
            //   270: istore_3       
            //   271: goto            243
            //   274: astore_2       
            //   275: aload           10
            //   277: monitorexit    
            //   278: aload_2        
            //   279: athrow         
            //   280: astore_2       
            //   281: aload           9
            //   283: monitorexit    
            //   284: aload_2        
            //   285: athrow         
            //   286: return         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  15     26     274    280    Any
            //  30     40     274    280    Any
            //  40     68     274    280    Any
            //  101    128    274    280    Any
            //  135    186    274    280    Any
            //  190    226    274    280    Any
            //  257    267    280    286    Any
            //  275    278    274    280    Any
            //  281    284    280    286    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public void windowUpdate(final int n, final long n2) {
            if (n == 0) {
                synchronized (Http2Connection.this) {
                    final Http2Connection this$0 = Http2Connection.this;
                    this$0.bytesLeftInWriteWindow += n2;
                    Http2Connection.this.notifyAll();
                    return;
                }
            }
            final Http2Stream stream = Http2Connection.this.getStream(n);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(n2);
                }
            }
        }
    }
}
