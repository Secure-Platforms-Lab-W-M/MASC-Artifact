// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.ws;

import okio.BufferedSource;
import okio.BufferedSink;
import java.net.SocketTimeoutException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.annotation.Nullable;
import java.net.Socket;
import okhttp3.internal.connection.RealConnection;
import okhttp3.HttpUrl;
import okhttp3.internal.connection.StreamAllocation;
import java.io.Closeable;
import okhttp3.internal.Util;
import okhttp3.Callback;
import okhttp3.internal.Internal;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import okhttp3.Response;
import java.util.Collections;
import java.util.Random;
import okio.ByteString;
import okhttp3.Request;
import java.util.ArrayDeque;
import okhttp3.WebSocketListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import okhttp3.Call;
import okhttp3.Protocol;
import java.util.List;
import okhttp3.WebSocket;

public final class RealWebSocket implements WebSocket, FrameCallback
{
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
    private static final long MAX_QUEUE_SIZE = 16777216L;
    private static final List<Protocol> ONLY_HTTP1;
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue;
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private final ArrayDeque<ByteString> pongQueue;
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;
    
    static {
        ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    }
    
    public RealWebSocket(final Request originalRequest, final WebSocketListener listener, final Random random, final long pingIntervalMillis) {
        this.pongQueue = new ArrayDeque<ByteString>();
        this.messageAndCloseQueue = new ArrayDeque<Object>();
        this.receivedCloseCode = -1;
        if (!"GET".equals(originalRequest.method())) {
            throw new IllegalArgumentException("Request must be GET: " + originalRequest.method());
        }
        this.originalRequest = originalRequest;
        this.listener = listener;
        this.random = random;
        this.pingIntervalMillis = pingIntervalMillis;
        final byte[] array = new byte[16];
        random.nextBytes(array);
        this.key = ByteString.of(array).base64();
        this.writerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (RealWebSocket.this.writeOneFrame()) {}
                }
                catch (IOException ex) {
                    RealWebSocket.this.failWebSocket(ex, null);
                }
            }
        };
    }
    
    private void runWriter() {
        assert Thread.holdsLock(this);
        if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
        }
    }
    
    private boolean send(final ByteString byteString, final int n) {
        while (true) {
            final boolean b = false;
            // monitorenter(this)
            boolean b2 = b;
            Label_0068: {
                try {
                    if (!this.failed) {
                        if (this.enqueuedClose) {
                            b2 = b;
                        }
                        else {
                            if (this.queueSize + byteString.size() <= 16777216L) {
                                break Label_0068;
                            }
                            this.close(1001, null);
                            b2 = b;
                        }
                    }
                    return b2;
                }
                finally {
                }
                // monitorexit(this)
            }
            final ByteString byteString2;
            this.queueSize += byteString2.size();
            this.messageAndCloseQueue.add(new Message(n, byteString2));
            this.runWriter();
            b2 = true;
            return b2;
        }
    }
    
    void awaitTermination(final int n, final TimeUnit timeUnit) throws InterruptedException {
        this.executor.awaitTermination(n, timeUnit);
    }
    
    @Override
    public void cancel() {
        this.call.cancel();
    }
    
    void checkResponse(final Response response) throws ProtocolException {
        if (response.code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        final String header = response.header("Connection");
        if (!"Upgrade".equalsIgnoreCase(header)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + header + "'");
        }
        final String header2 = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(header2)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + header2 + "'");
        }
        final String header3 = response.header("Sec-WebSocket-Accept");
        final String base64 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
        if (!base64.equals(header3)) {
            throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + base64 + "' but was '" + header3 + "'");
        }
    }
    
    @Override
    public boolean close(final int n, final String s) {
        return this.close(n, s, 60000L);
    }
    
    boolean close(final int n, final String s, final long n2) {
        boolean b = true;
        ByteString encodeUtf8;
        synchronized (this) {
            WebSocketProtocol.validateCloseCode(n);
            encodeUtf8 = null;
            if (s != null && (encodeUtf8 = ByteString.encodeUtf8(s)).size() > 123L) {
                throw new IllegalArgumentException("reason.size() > 123: " + s);
            }
        }
        if (this.failed || this.enqueuedClose) {
            b = false;
        }
        else {
            this.enqueuedClose = true;
            this.messageAndCloseQueue.add(new Close(n, encodeUtf8, n2));
            this.runWriter();
        }
        // monitorexit(this)
        return b;
    }
    
    public void connect(OkHttpClient build) {
        build = build.newBuilder().eventListener(EventListener.NONE).protocols(RealWebSocket.ONLY_HTTP1).build();
        final Request build2 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        (this.call = Internal.instance.newWebSocketCall(build, build2)).enqueue(new Callback() {
            final /* synthetic */ RealWebSocket this$0;
            
            @Override
            public void onFailure(final Call call, final IOException ex) {
                RealWebSocket.this.failWebSocket(ex, null);
            }
            
            @Override
            public void onResponse(final Call call, final Response response) {
                StreamAllocation streamAllocation;
                Streams webSocketStreams;
                try {
                    RealWebSocket.this.checkResponse(response);
                    streamAllocation = Internal.instance.streamAllocation(call);
                    streamAllocation.noNewStreams();
                    webSocketStreams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                    final Callback callback = this;
                    final RealWebSocket realWebSocket = callback.this$0;
                    final WebSocketListener webSocketListener = realWebSocket.listener;
                    final Callback callback2 = this;
                    final RealWebSocket realWebSocket2 = callback2.this$0;
                    final Response response2 = response;
                    webSocketListener.onOpen(realWebSocket2, response2);
                    final StringBuilder sb = new StringBuilder();
                    final String s = "OkHttp WebSocket ";
                    final StringBuilder sb2 = sb.append(s);
                    final Callback callback3 = this;
                    final Request request = build2;
                    final HttpUrl httpUrl = request.url();
                    final String s2 = httpUrl.redact();
                    final StringBuilder sb3 = sb2.append(s2);
                    final String s3 = sb3.toString();
                    final Callback callback4 = this;
                    final RealWebSocket realWebSocket3 = callback4.this$0;
                    final String s4 = s3;
                    final Streams streams = webSocketStreams;
                    realWebSocket3.initReaderAndWriter(s4, streams);
                    final StreamAllocation streamAllocation2 = streamAllocation;
                    final RealConnection realConnection = streamAllocation2.connection();
                    final Socket socket = realConnection.socket();
                    final int n = 0;
                    socket.setSoTimeout(n);
                    final Callback callback5 = this;
                    final RealWebSocket realWebSocket4 = callback5.this$0;
                    realWebSocket4.loopReader();
                    return;
                }
                catch (ProtocolException ex) {
                    RealWebSocket.this.failWebSocket(ex, response);
                    Util.closeQuietly(response);
                    return;
                }
                try {
                    final Callback callback = this;
                    final RealWebSocket realWebSocket = callback.this$0;
                    final WebSocketListener webSocketListener = realWebSocket.listener;
                    final Callback callback2 = this;
                    final RealWebSocket realWebSocket2 = callback2.this$0;
                    final Response response2 = response;
                    webSocketListener.onOpen(realWebSocket2, response2);
                    final StringBuilder sb = new StringBuilder();
                    final String s = "OkHttp WebSocket ";
                    final StringBuilder sb2 = sb.append(s);
                    final Callback callback3 = this;
                    final Request request = build2;
                    final HttpUrl httpUrl = request.url();
                    final String s2 = httpUrl.redact();
                    final StringBuilder sb3 = sb2.append(s2);
                    final String s3 = sb3.toString();
                    final Callback callback4 = this;
                    final RealWebSocket realWebSocket3 = callback4.this$0;
                    final String s4 = s3;
                    final Streams streams = webSocketStreams;
                    realWebSocket3.initReaderAndWriter(s4, streams);
                    final StreamAllocation streamAllocation2 = streamAllocation;
                    final RealConnection realConnection = streamAllocation2.connection();
                    final Socket socket = realConnection.socket();
                    final int n = 0;
                    socket.setSoTimeout(n);
                    final Callback callback5 = this;
                    final RealWebSocket realWebSocket4 = callback5.this$0;
                    realWebSocket4.loopReader();
                }
                catch (Exception ex2) {
                    RealWebSocket.this.failWebSocket(ex2, null);
                }
            }
        });
    }
    
    public void failWebSocket(final Exception ex, @Nullable final Response response) {
        final Streams streams;
        synchronized (this) {
            if (this.failed) {
                return;
            }
            this.failed = true;
            streams = this.streams;
            this.streams = null;
            if (this.cancelFuture != null) {
                this.cancelFuture.cancel(false);
            }
            if (this.executor != null) {
                this.executor.shutdown();
            }
            // monitorexit(this)
            final RealWebSocket realWebSocket = this;
            final WebSocketListener webSocketListener = realWebSocket.listener;
            final RealWebSocket realWebSocket2 = this;
            final Exception ex2 = ex;
            final Response response2 = response;
            webSocketListener.onFailure(realWebSocket2, ex2, response2);
            return;
        }
        try {
            final RealWebSocket realWebSocket = this;
            final WebSocketListener webSocketListener = realWebSocket.listener;
            final RealWebSocket realWebSocket2 = this;
            final Exception ex2 = ex;
            final Response response2 = response;
            webSocketListener.onFailure(realWebSocket2, ex2, response2);
        }
        finally {
            Util.closeQuietly(streams);
        }
    }
    
    public void initReaderAndWriter(final String s, final Streams streams) throws IOException {
        synchronized (this) {
            this.streams = streams;
            this.writer = new WebSocketWriter(streams.client, streams.sink, this.random);
            this.executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(s, false));
            if (this.pingIntervalMillis != 0L) {
                this.executor.scheduleAtFixedRate(new PingRunnable(), this.pingIntervalMillis, this.pingIntervalMillis, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                this.runWriter();
            }
            // monitorexit(this)
            this.reader = new WebSocketReader(streams.client, streams.source, (WebSocketReader.FrameCallback)this);
        }
    }
    
    public void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }
    
    @Override
    public void onReadClose(final int receivedCloseCode, final String s) {
        if (receivedCloseCode == -1) {
            throw new IllegalArgumentException();
        }
        final Closeable closeable = null;
        synchronized (this) {
            if (this.receivedCloseCode != -1) {
                throw new IllegalStateException("already closed");
            }
        }
        this.receivedCloseCode = receivedCloseCode;
        final String receivedCloseReason;
        this.receivedCloseReason = receivedCloseReason;
        Closeable streams = closeable;
        if (this.enqueuedClose) {
            streams = closeable;
            if (this.messageAndCloseQueue.isEmpty()) {
                streams = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                this.executor.shutdown();
            }
        }
        // monitorexit(this)
        try {
            this.listener.onClosing(this, receivedCloseCode, receivedCloseReason);
            if (streams != null) {
                this.listener.onClosed(this, receivedCloseCode, receivedCloseReason);
            }
        }
        finally {
            Util.closeQuietly(streams);
        }
    }
    
    @Override
    public void onReadMessage(final String s) throws IOException {
        this.listener.onMessage(this, s);
    }
    
    @Override
    public void onReadMessage(final ByteString byteString) throws IOException {
        this.listener.onMessage(this, byteString);
    }
    
    @Override
    public void onReadPing(final ByteString byteString) {
        synchronized (this) {
            if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
                this.pongQueue.add(byteString);
                this.runWriter();
                ++this.receivedPingCount;
            }
        }
    }
    
    @Override
    public void onReadPong(final ByteString byteString) {
        synchronized (this) {
            ++this.receivedPongCount;
            this.awaitingPong = false;
        }
    }
    
    boolean pong(final ByteString byteString) {
        synchronized (this) {
            boolean b;
            if (this.failed || (this.enqueuedClose && this.messageAndCloseQueue.isEmpty())) {
                b = false;
            }
            else {
                this.pongQueue.add(byteString);
                this.runWriter();
                b = true;
            }
            return b;
        }
    }
    
    boolean processNextFrame() throws IOException {
        boolean b = false;
        try {
            this.reader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            this.failWebSocket(ex, null);
            return false;
        }
    }
    
    @Override
    public long queueSize() {
        synchronized (this) {
            return this.queueSize;
        }
    }
    
    int receivedPingCount() {
        synchronized (this) {
            return this.receivedPingCount;
        }
    }
    
    int receivedPongCount() {
        synchronized (this) {
            return this.receivedPongCount;
        }
    }
    
    @Override
    public Request request() {
        return this.originalRequest;
    }
    
    @Override
    public boolean send(final String s) {
        if (s == null) {
            throw new NullPointerException("text == null");
        }
        return this.send(ByteString.encodeUtf8(s), 1);
    }
    
    @Override
    public boolean send(final ByteString byteString) {
        if (byteString == null) {
            throw new NullPointerException("bytes == null");
        }
        return this.send(byteString, 2);
    }
    
    int sentPingCount() {
        synchronized (this) {
            return this.sentPingCount;
        }
    }
    
    void tearDown() throws InterruptedException {
        if (this.cancelFuture != null) {
            this.cancelFuture.cancel(false);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10L, TimeUnit.SECONDS);
    }
    
    boolean writeOneFrame() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: iconst_m1      
        //     4: istore_2       
        //     5: aconst_null    
        //     6: astore          8
        //     8: aconst_null    
        //     9: astore          6
        //    11: aload_0        
        //    12: monitorenter   
        //    13: aload_0        
        //    14: getfield        okhttp3/internal/ws/RealWebSocket.failed:Z
        //    17: ifeq            24
        //    20: aload_0        
        //    21: monitorexit    
        //    22: iconst_0       
        //    23: ireturn        
        //    24: aload_0        
        //    25: getfield        okhttp3/internal/ws/RealWebSocket.writer:Lokhttp3/internal/ws/WebSocketWriter;
        //    28: astore          9
        //    30: aload_0        
        //    31: getfield        okhttp3/internal/ws/RealWebSocket.pongQueue:Ljava/util/ArrayDeque;
        //    34: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    37: checkcast       Lokio/ByteString;
        //    40: astore          10
        //    42: iload_2        
        //    43: istore_1       
        //    44: aload           8
        //    46: astore          5
        //    48: aload           6
        //    50: astore_3       
        //    51: aload           10
        //    53: ifnonnull       112
        //    56: aload_0        
        //    57: getfield        okhttp3/internal/ws/RealWebSocket.messageAndCloseQueue:Ljava/util/ArrayDeque;
        //    60: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    63: astore          7
        //    65: aload           7
        //    67: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //    70: ifeq            179
        //    73: aload_0        
        //    74: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseCode:I
        //    77: istore_1       
        //    78: aload_0        
        //    79: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseReason:Ljava/lang/String;
        //    82: astore          5
        //    84: iload_1        
        //    85: iconst_m1      
        //    86: if_icmpeq       132
        //    89: aload_0        
        //    90: getfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //    93: astore_3       
        //    94: aload_0        
        //    95: aconst_null    
        //    96: putfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //    99: aload_0        
        //   100: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //   103: invokeinterface java/util/concurrent/ScheduledExecutorService.shutdown:()V
        //   108: aload           7
        //   110: astore          4
        //   112: aload_0        
        //   113: monitorexit    
        //   114: aload           10
        //   116: ifnull          201
        //   119: aload           9
        //   121: aload           10
        //   123: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writePong:(Lokio/ByteString;)V
        //   126: aload_3        
        //   127: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   130: iconst_1       
        //   131: ireturn        
        //   132: aload_0        
        //   133: aload_0        
        //   134: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //   137: new             Lokhttp3/internal/ws/RealWebSocket$CancelRunnable;
        //   140: dup            
        //   141: aload_0        
        //   142: invokespecial   okhttp3/internal/ws/RealWebSocket$CancelRunnable.<init>:(Lokhttp3/internal/ws/RealWebSocket;)V
        //   145: aload           7
        //   147: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   150: getfield        okhttp3/internal/ws/RealWebSocket$Close.cancelAfterCloseMillis:J
        //   153: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   156: invokeinterface java/util/concurrent/ScheduledExecutorService.schedule:(Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   161: putfield        okhttp3/internal/ws/RealWebSocket.cancelFuture:Ljava/util/concurrent/ScheduledFuture;
        //   164: aload           7
        //   166: astore          4
        //   168: aload           6
        //   170: astore_3       
        //   171: goto            112
        //   174: astore_3       
        //   175: aload_0        
        //   176: monitorexit    
        //   177: aload_3        
        //   178: athrow         
        //   179: aload           7
        //   181: astore          4
        //   183: iload_2        
        //   184: istore_1       
        //   185: aload           8
        //   187: astore          5
        //   189: aload           6
        //   191: astore_3       
        //   192: aload           7
        //   194: ifnonnull       112
        //   197: aload_0        
        //   198: monitorexit    
        //   199: iconst_0       
        //   200: ireturn        
        //   201: aload           4
        //   203: instanceof      Lokhttp3/internal/ws/RealWebSocket$Message;
        //   206: ifeq            298
        //   209: aload           4
        //   211: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   214: getfield        okhttp3/internal/ws/RealWebSocket$Message.data:Lokio/ByteString;
        //   217: astore          5
        //   219: aload           9
        //   221: aload           4
        //   223: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   226: getfield        okhttp3/internal/ws/RealWebSocket$Message.formatOpcode:I
        //   229: aload           5
        //   231: invokevirtual   okio/ByteString.size:()I
        //   234: i2l            
        //   235: invokevirtual   okhttp3/internal/ws/WebSocketWriter.newMessageSink:(IJ)Lokio/Sink;
        //   238: invokestatic    okio/Okio.buffer:(Lokio/Sink;)Lokio/BufferedSink;
        //   241: astore          4
        //   243: aload           4
        //   245: aload           5
        //   247: invokeinterface okio/BufferedSink.write:(Lokio/ByteString;)Lokio/BufferedSink;
        //   252: pop            
        //   253: aload           4
        //   255: invokeinterface okio/BufferedSink.close:()V
        //   260: aload_0        
        //   261: monitorenter   
        //   262: aload_0        
        //   263: aload_0        
        //   264: getfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   267: aload           5
        //   269: invokevirtual   okio/ByteString.size:()I
        //   272: i2l            
        //   273: lsub           
        //   274: putfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   277: aload_0        
        //   278: monitorexit    
        //   279: goto            126
        //   282: astore          4
        //   284: aload_0        
        //   285: monitorexit    
        //   286: aload           4
        //   288: athrow         
        //   289: astore          4
        //   291: aload_3        
        //   292: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   295: aload           4
        //   297: athrow         
        //   298: aload           4
        //   300: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //   303: ifeq            346
        //   306: aload           4
        //   308: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   311: astore          4
        //   313: aload           9
        //   315: aload           4
        //   317: getfield        okhttp3/internal/ws/RealWebSocket$Close.code:I
        //   320: aload           4
        //   322: getfield        okhttp3/internal/ws/RealWebSocket$Close.reason:Lokio/ByteString;
        //   325: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writeClose:(ILokio/ByteString;)V
        //   328: aload_3        
        //   329: ifnull          126
        //   332: aload_0        
        //   333: getfield        okhttp3/internal/ws/RealWebSocket.listener:Lokhttp3/WebSocketListener;
        //   336: aload_0        
        //   337: iload_1        
        //   338: aload           5
        //   340: invokevirtual   okhttp3/WebSocketListener.onClosed:(Lokhttp3/WebSocket;ILjava/lang/String;)V
        //   343: goto            126
        //   346: new             Ljava/lang/AssertionError;
        //   349: dup            
        //   350: invokespecial   java/lang/AssertionError.<init>:()V
        //   353: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  13     22     174    179    Any
        //  24     42     174    179    Any
        //  56     84     174    179    Any
        //  89     108    174    179    Any
        //  112    114    174    179    Any
        //  119    126    289    298    Any
        //  132    164    174    179    Any
        //  175    177    174    179    Any
        //  197    199    174    179    Any
        //  201    262    289    298    Any
        //  262    279    282    289    Any
        //  284    286    282    289    Any
        //  286    289    289    298    Any
        //  298    328    289    298    Any
        //  332    343    289    298    Any
        //  346    354    289    298    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0126:
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
    
    void writePingFrame() {
        synchronized (this) {
            if (this.failed) {
                return;
            }
            final WebSocketWriter writer = this.writer;
            int sentPingCount;
            if (this.awaitingPong) {
                sentPingCount = this.sentPingCount;
            }
            else {
                sentPingCount = -1;
            }
            ++this.sentPingCount;
            this.awaitingPong = true;
            // monitorexit(this)
            if (sentPingCount != -1) {
                this.failWebSocket(new SocketTimeoutException("sent ping but didn't receive pong within " + this.pingIntervalMillis + "ms (after " + (sentPingCount - 1) + " successful ping/pongs)"), null);
                return;
            }
        }
        try {
            final WebSocketWriter webSocketWriter;
            webSocketWriter.writePing(ByteString.EMPTY);
        }
        catch (IOException ex) {
            this.failWebSocket(ex, null);
        }
    }
    
    final class CancelRunnable implements Runnable
    {
        @Override
        public void run() {
            RealWebSocket.this.cancel();
        }
    }
    
    static final class Close
    {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;
        
        Close(final int code, final ByteString reason, final long cancelAfterCloseMillis) {
            this.code = code;
            this.reason = reason;
            this.cancelAfterCloseMillis = cancelAfterCloseMillis;
        }
    }
    
    static final class Message
    {
        final ByteString data;
        final int formatOpcode;
        
        Message(final int formatOpcode, final ByteString data) {
            this.formatOpcode = formatOpcode;
            this.data = data;
        }
    }
    
    private final class PingRunnable implements Runnable
    {
        PingRunnable() {
        }
        
        @Override
        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }
    
    public abstract static class Streams implements Closeable
    {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;
        
        public Streams(final boolean client, final BufferedSource source, final BufferedSink sink) {
            this.client = client;
            this.source = source;
            this.sink = sink;
        }
    }
}
