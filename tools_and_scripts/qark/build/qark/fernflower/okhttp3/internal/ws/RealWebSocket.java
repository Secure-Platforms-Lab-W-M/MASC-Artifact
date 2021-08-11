package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
   private static final long MAX_QUEUE_SIZE = 16777216L;
   private static final List ONLY_HTTP1;
   private Call call;
   private ScheduledFuture cancelFuture;
   private boolean enqueuedClose;
   private ScheduledExecutorService executor;
   private boolean failed;
   private final String key;
   final WebSocketListener listener;
   private final ArrayDeque messageAndCloseQueue = new ArrayDeque();
   private final Request originalRequest;
   int pingCount;
   int pongCount;
   private final ArrayDeque pongQueue = new ArrayDeque();
   private long queueSize;
   private final Random random;
   private WebSocketReader reader;
   private int receivedCloseCode = -1;
   private String receivedCloseReason;
   private RealWebSocket.Streams streams;
   private WebSocketWriter writer;
   private final Runnable writerRunnable;

   static {
      ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
   }

   public RealWebSocket(Request var1, WebSocketListener var2, Random var3) {
      if ("GET".equals(var1.method())) {
         this.originalRequest = var1;
         this.listener = var2;
         this.random = var3;
         byte[] var4 = new byte[16];
         var3.nextBytes(var4);
         this.key = ByteString.method_6(var4).base64();
         this.writerRunnable = new Runnable() {
            public void run() {
               boolean var1;
               do {
                  try {
                     var1 = RealWebSocket.this.writeOneFrame();
                  } catch (IOException var3) {
                     RealWebSocket.this.failWebSocket(var3, (Response)null);
                     return;
                  }
               } while(var1);

            }
         };
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Request must be GET: ");
         var5.append(var1.method());
         throw new IllegalArgumentException(var5.toString());
      }
   }

   private void runWriter() {
      ScheduledExecutorService var1 = this.executor;
      if (var1 != null) {
         var1.execute(this.writerRunnable);
      }

   }

   private boolean send(ByteString var1, int var2) {
      synchronized(this){}

      try {
         if (this.failed || this.enqueuedClose) {
            return false;
         }

         if (this.queueSize + (long)var1.size() <= 16777216L) {
            this.queueSize += (long)var1.size();
            this.messageAndCloseQueue.add(new RealWebSocket.Message(var2, var1));
            this.runWriter();
            return true;
         }

         this.close(1001, (String)null);
      } finally {
         ;
      }

      return false;
   }

   void awaitTermination(int var1, TimeUnit var2) throws InterruptedException {
      this.executor.awaitTermination((long)var1, var2);
   }

   public void cancel() {
      this.call.cancel();
   }

   void checkResponse(Response var1) throws ProtocolException {
      StringBuilder var2;
      if (var1.code() == 101) {
         String var6 = var1.header("Connection");
         StringBuilder var4;
         if ("Upgrade".equalsIgnoreCase(var6)) {
            var6 = var1.header("Upgrade");
            if ("websocket".equalsIgnoreCase(var6)) {
               String var5 = var1.header("Sec-WebSocket-Accept");
               var2 = new StringBuilder();
               var2.append(this.key);
               var2.append("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
               var6 = ByteString.encodeUtf8(var2.toString()).sha1().base64();
               if (!var6.equals(var5)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Expected 'Sec-WebSocket-Accept' header value '");
                  var3.append(var6);
                  var3.append("' but was '");
                  var3.append(var5);
                  var3.append("'");
                  throw new ProtocolException(var3.toString());
               }
            } else {
               var4 = new StringBuilder();
               var4.append("Expected 'Upgrade' header value 'websocket' but was '");
               var4.append(var6);
               var4.append("'");
               throw new ProtocolException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("Expected 'Connection' header value 'Upgrade' but was '");
            var4.append(var6);
            var4.append("'");
            throw new ProtocolException(var4.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append("Expected HTTP 101 response but was '");
         var2.append(var1.code());
         var2.append(" ");
         var2.append(var1.message());
         var2.append("'");
         throw new ProtocolException(var2.toString());
      }
   }

   public boolean close(int var1, String var2) {
      return this.close(var1, var2, 60000L);
   }

   boolean close(int var1, String var2, long var3) {
      synchronized(this){}

      Throwable var10000;
      label331: {
         boolean var10001;
         try {
            WebSocketProtocol.validateCloseCode(var1);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label331;
         }

         ByteString var5 = null;
         if (var2 != null) {
            label330: {
               try {
                  var5 = ByteString.encodeUtf8(var2);
                  if ((long)var5.size() <= 123L) {
                     break label330;
                  }
               } catch (Throwable var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label331;
               }

               try {
                  StringBuilder var37 = new StringBuilder();
                  var37.append("reason.size() > 123: ");
                  var37.append(var2);
                  throw new IllegalArgumentException(var37.toString());
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label331;
               }
            }
         }

         label310: {
            try {
               if (!this.failed && !this.enqueuedClose) {
                  break label310;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label331;
            }

            return false;
         }

         try {
            this.enqueuedClose = true;
            this.messageAndCloseQueue.add(new RealWebSocket.Close(var1, var5, var3));
            this.runWriter();
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label331;
         }

         return true;
      }

      Throwable var36 = var10000;
      throw var36;
   }

   public void connect(OkHttpClient var1) {
      OkHttpClient var3 = var1.newBuilder().protocols(ONLY_HTTP1).build();
      final int var2 = var3.pingIntervalMillis();
      final Request var4 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
      Call var5 = Internal.instance.newWebSocketCall(var3, var4);
      this.call = var5;
      var5.enqueue(new Callback() {
         public void onFailure(Call var1, IOException var2x) {
            RealWebSocket.this.failWebSocket(var2x, (Response)null);
         }

         public void onResponse(Call var1, Response var2x) {
            try {
               RealWebSocket.this.checkResponse(var2x);
            } catch (ProtocolException var5) {
               RealWebSocket.this.failWebSocket(var5, var2x);
               Util.closeQuietly((Closeable)var2x);
               return;
            }

            StreamAllocation var6 = Internal.instance.streamAllocation(var1);
            var6.noNewStreams();
            RealWebSocket.Streams var3 = var6.connection().newWebSocketStreams(var6);

            try {
               RealWebSocket.this.listener.onOpen(RealWebSocket.this, var2x);
               StringBuilder var7 = new StringBuilder();
               var7.append("OkHttp WebSocket ");
               var7.append(var4.url().redact());
               String var8 = var7.toString();
               RealWebSocket.this.initReaderAndWriter(var8, (long)var2, var3);
               var6.connection().socket().setSoTimeout(0);
               RealWebSocket.this.loopReader();
            } catch (Exception var4x) {
               RealWebSocket.this.failWebSocket(var4x, (Response)null);
            }
         }
      });
   }

   public void failWebSocket(Exception var1, Response var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label379: {
         try {
            if (this.failed) {
               return;
            }
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label379;
         }

         RealWebSocket.Streams var3;
         try {
            this.failed = true;
            var3 = this.streams;
            this.streams = null;
            if (this.cancelFuture != null) {
               this.cancelFuture.cancel(false);
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label379;
         }

         try {
            if (this.executor != null) {
               this.executor.shutdown();
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label379;
         }

         try {
            ;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label379;
         }

         try {
            this.listener.onFailure(this, var1, var2);
         } finally {
            Util.closeQuietly((Closeable)var3);
         }

         return;
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   public void initReaderAndWriter(String var1, long var2, RealWebSocket.Streams var4) throws IOException {
      synchronized(this){}

      label281: {
         Throwable var10000;
         boolean var10001;
         label282: {
            ScheduledThreadPoolExecutor var35;
            try {
               this.streams = var4;
               this.writer = new WebSocketWriter(var4.client, var4.sink, this.random);
               var35 = new ScheduledThreadPoolExecutor(1, Util.threadFactory(var1, false));
               this.executor = var35;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label282;
            }

            if (var2 != 0L) {
               try {
                  var35.scheduleAtFixedRate(new RealWebSocket.PingRunnable(), var2, var2, TimeUnit.MILLISECONDS);
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label282;
               }
            }

            try {
               if (!this.messageAndCloseQueue.isEmpty()) {
                  this.runWriter();
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label282;
            }

            label266:
            try {
               break label281;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label266;
            }
         }

         while(true) {
            Throwable var36 = var10000;

            try {
               throw var36;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               continue;
            }
         }
      }

      this.reader = new WebSocketReader(var4.client, var4.source, this);
   }

   public void loopReader() throws IOException {
      while(this.receivedCloseCode == -1) {
         this.reader.processNextFrame();
      }

   }

   public void onReadClose(int var1, String var2) {
      if (var1 != -1) {
         Object var4 = null;
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         Throwable var95;
         label896: {
            label889: {
               try {
                  if (this.receivedCloseCode == -1) {
                     this.receivedCloseCode = var1;
                     this.receivedCloseReason = var2;
                     break label889;
                  }
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label896;
               }

               try {
                  throw new IllegalStateException("already closed");
               } catch (Throwable var93) {
                  var10000 = var93;
                  var10001 = false;
                  break label896;
               }
            }

            RealWebSocket.Streams var3 = (RealWebSocket.Streams)var4;

            label897: {
               try {
                  if (!this.enqueuedClose) {
                     break label897;
                  }
               } catch (Throwable var92) {
                  var10000 = var92;
                  var10001 = false;
                  break label896;
               }

               var3 = (RealWebSocket.Streams)var4;

               try {
                  if (!this.messageAndCloseQueue.isEmpty()) {
                     break label897;
                  }

                  var3 = this.streams;
                  this.streams = null;
                  if (this.cancelFuture != null) {
                     this.cancelFuture.cancel(false);
                  }
               } catch (Throwable var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label896;
               }

               try {
                  this.executor.shutdown();
               } catch (Throwable var90) {
                  var10000 = var90;
                  var10001 = false;
                  break label896;
               }
            }

            try {
               ;
            } catch (Throwable var89) {
               var10000 = var89;
               var10001 = false;
               break label896;
            }

            label858: {
               label899: {
                  try {
                     this.listener.onClosing(this, var1, var2);
                  } catch (Throwable var88) {
                     var10000 = var88;
                     var10001 = false;
                     break label899;
                  }

                  if (var3 == null) {
                     break label858;
                  }

                  label853:
                  try {
                     this.listener.onClosed(this, var1, var2);
                     break label858;
                  } catch (Throwable var87) {
                     var10000 = var87;
                     var10001 = false;
                     break label853;
                  }
               }

               var95 = var10000;
               Util.closeQuietly((Closeable)var3);
               throw var95;
            }

            Util.closeQuietly((Closeable)var3);
            return;
         }

         while(true) {
            var95 = var10000;

            try {
               throw var95;
            } catch (Throwable var86) {
               var10000 = var86;
               var10001 = false;
               continue;
            }
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void onReadMessage(String var1) throws IOException {
      this.listener.onMessage(this, (String)var1);
   }

   public void onReadMessage(ByteString var1) throws IOException {
      this.listener.onMessage(this, (ByteString)var1);
   }

   public void onReadPing(ByteString var1) {
      synchronized(this){}

      try {
         if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
            this.pongQueue.add(var1);
            this.runWriter();
            ++this.pingCount;
            return;
         }
      } finally {
         ;
      }

   }

   public void onReadPong(ByteString var1) {
      synchronized(this){}

      try {
         ++this.pongCount;
      } finally {
         ;
      }

   }

   int pingCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.pingCount;
      } finally {
         ;
      }

      return var1;
   }

   boolean pong(ByteString var1) {
      synchronized(this){}

      try {
         if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
            this.pongQueue.add(var1);
            this.runWriter();
            return true;
         }
      } finally {
         ;
      }

      return false;
   }

   int pongCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.pongCount;
      } finally {
         ;
      }

      return var1;
   }

   boolean processNextFrame() throws IOException {
      boolean var2 = false;

      int var1;
      try {
         this.reader.processNextFrame();
         var1 = this.receivedCloseCode;
      } catch (Exception var4) {
         this.failWebSocket(var4, (Response)null);
         return false;
      }

      if (var1 == -1) {
         var2 = true;
      }

      return var2;
   }

   public long queueSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.queueSize;
      } finally {
         ;
      }

      return var1;
   }

   public Request request() {
      return this.originalRequest;
   }

   public boolean send(String var1) {
      if (var1 != null) {
         return this.send(ByteString.encodeUtf8(var1), 1);
      } else {
         throw new NullPointerException("text == null");
      }
   }

   public boolean send(ByteString var1) {
      if (var1 != null) {
         return this.send(var1, 2);
      } else {
         throw new NullPointerException("bytes == null");
      }
   }

   void tearDown() throws InterruptedException {
      ScheduledFuture var1 = this.cancelFuture;
      if (var1 != null) {
         var1.cancel(false);
      }

      this.executor.shutdown();
      this.executor.awaitTermination(10L, TimeUnit.SECONDS);
   }

   boolean writeOneFrame() throws IOException {
      // $FF: Couldn't be decompiled
   }

   void writePingFrame() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label147: {
         try {
            if (this.failed) {
               return;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label147;
         }

         WebSocketWriter var18;
         try {
            var18 = this.writer;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label147;
         }

         try {
            var18.writePing(ByteString.EMPTY);
            return;
         } catch (IOException var14) {
            this.failWebSocket(var14, (Response)null);
            return;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   final class CancelRunnable implements Runnable {
      public void run() {
         RealWebSocket.this.cancel();
      }
   }

   static final class Close {
      final long cancelAfterCloseMillis;
      final int code;
      final ByteString reason;

      Close(int var1, ByteString var2, long var3) {
         this.code = var1;
         this.reason = var2;
         this.cancelAfterCloseMillis = var3;
      }
   }

   static final class Message {
      final ByteString data;
      final int formatOpcode;

      Message(int var1, ByteString var2) {
         this.formatOpcode = var1;
         this.data = var2;
      }
   }

   private final class PingRunnable implements Runnable {
      PingRunnable() {
      }

      public void run() {
         RealWebSocket.this.writePingFrame();
      }
   }

   public abstract static class Streams implements Closeable {
      public final boolean client;
      public final BufferedSink sink;
      public final BufferedSource source;

      public Streams(boolean var1, BufferedSource var2, BufferedSink var3) {
         this.client = var1;
         this.source = var2;
         this.sink = var3;
      }
   }
}
