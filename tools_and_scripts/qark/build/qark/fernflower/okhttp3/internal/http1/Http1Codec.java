package okhttp3.internal.http1;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingTimeout;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http1Codec implements HttpCodec {
   private static final int STATE_CLOSED = 6;
   private static final int STATE_IDLE = 0;
   private static final int STATE_OPEN_REQUEST_BODY = 1;
   private static final int STATE_OPEN_RESPONSE_BODY = 4;
   private static final int STATE_READING_RESPONSE_BODY = 5;
   private static final int STATE_READ_RESPONSE_HEADERS = 3;
   private static final int STATE_WRITING_REQUEST_BODY = 2;
   final OkHttpClient client;
   final BufferedSink sink;
   final BufferedSource source;
   int state = 0;
   final StreamAllocation streamAllocation;

   public Http1Codec(OkHttpClient var1, StreamAllocation var2, BufferedSource var3, BufferedSink var4) {
      this.client = var1;
      this.streamAllocation = var2;
      this.source = var3;
      this.sink = var4;
   }

   private Source getTransferStream(Response var1) throws IOException {
      if (!HttpHeaders.hasBody(var1)) {
         return this.newFixedLengthSource(0L);
      } else if ("chunked".equalsIgnoreCase(var1.header("Transfer-Encoding"))) {
         return this.newChunkedSource(var1.request().url());
      } else {
         long var2 = HttpHeaders.contentLength(var1);
         return var2 != -1L ? this.newFixedLengthSource(var2) : this.newUnknownLengthSource();
      }
   }

   public void cancel() {
      RealConnection var1 = this.streamAllocation.connection();
      if (var1 != null) {
         var1.cancel();
      }

   }

   public Sink createRequestBody(Request var1, long var2) {
      if ("chunked".equalsIgnoreCase(var1.header("Transfer-Encoding"))) {
         return this.newChunkedSink();
      } else if (var2 != -1L) {
         return this.newFixedLengthSink(var2);
      } else {
         throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
      }
   }

   void detachTimeout(ForwardingTimeout var1) {
      Timeout var2 = var1.delegate();
      var1.setDelegate(Timeout.NONE);
      var2.clearDeadline();
      var2.clearTimeout();
   }

   public void finishRequest() throws IOException {
      this.sink.flush();
   }

   public void flushRequest() throws IOException {
      this.sink.flush();
   }

   public boolean isClosed() {
      return this.state == 6;
   }

   public Sink newChunkedSink() {
      if (this.state == 1) {
         this.state = 2;
         return new Http1Codec.ChunkedSink();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("state: ");
         var1.append(this.state);
         throw new IllegalStateException(var1.toString());
      }
   }

   public Source newChunkedSource(HttpUrl var1) throws IOException {
      if (this.state == 4) {
         this.state = 5;
         return new Http1Codec.ChunkedSource(var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("state: ");
         var2.append(this.state);
         throw new IllegalStateException(var2.toString());
      }
   }

   public Sink newFixedLengthSink(long var1) {
      if (this.state == 1) {
         this.state = 2;
         return new Http1Codec.FixedLengthSink(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("state: ");
         var3.append(this.state);
         throw new IllegalStateException(var3.toString());
      }
   }

   public Source newFixedLengthSource(long var1) throws IOException {
      if (this.state == 4) {
         this.state = 5;
         return new Http1Codec.FixedLengthSource(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("state: ");
         var3.append(this.state);
         throw new IllegalStateException(var3.toString());
      }
   }

   public Source newUnknownLengthSource() throws IOException {
      if (this.state == 4) {
         StreamAllocation var2 = this.streamAllocation;
         if (var2 != null) {
            this.state = 5;
            var2.noNewStreams();
            return new Http1Codec.UnknownLengthSource();
         } else {
            throw new IllegalStateException("streamAllocation == null");
         }
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("state: ");
         var1.append(this.state);
         throw new IllegalStateException(var1.toString());
      }
   }

   public ResponseBody openResponseBody(Response var1) throws IOException {
      Source var2 = this.getTransferStream(var1);
      return new RealResponseBody(var1.headers(), Okio.buffer(var2));
   }

   public Headers readHeaders() throws IOException {
      Headers.Builder var1 = new Headers.Builder();

      while(true) {
         String var2 = this.source.readUtf8LineStrict();
         if (var2.length() == 0) {
            return var1.build();
         }

         Internal.instance.addLenient(var1, var2);
      }
   }

   public Response.Builder readResponseHeaders(boolean var1) throws IOException {
      int var2 = this.state;
      if (var2 != 1 && var2 != 3) {
         StringBuilder var9 = new StringBuilder();
         var9.append("state: ");
         var9.append(this.state);
         throw new IllegalStateException(var9.toString());
      } else {
         EOFException var10000;
         label38: {
            StatusLine var3;
            Response.Builder var4;
            boolean var10001;
            try {
               var3 = StatusLine.parse(this.source.readUtf8LineStrict());
               var4 = (new Response.Builder()).protocol(var3.protocol).code(var3.code).message(var3.message).headers(this.readHeaders());
            } catch (EOFException var7) {
               var10000 = var7;
               var10001 = false;
               break label38;
            }

            if (var1) {
               try {
                  if (var3.code == 100) {
                     return null;
                  }
               } catch (EOFException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label38;
               }
            }

            try {
               this.state = 4;
               return var4;
            } catch (EOFException var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         EOFException var8 = var10000;
         StringBuilder var10 = new StringBuilder();
         var10.append("unexpected end of stream on ");
         var10.append(this.streamAllocation);
         IOException var11 = new IOException(var10.toString());
         var11.initCause(var8);
         throw var11;
      }
   }

   public void writeRequest(Headers var1, String var2) throws IOException {
      if (this.state != 0) {
         StringBuilder var5 = new StringBuilder();
         var5.append("state: ");
         var5.append(this.state);
         throw new IllegalStateException(var5.toString());
      } else {
         this.sink.writeUtf8(var2).writeUtf8("\r\n");
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            this.sink.writeUtf8(var1.name(var3)).writeUtf8(": ").writeUtf8(var1.value(var3)).writeUtf8("\r\n");
         }

         this.sink.writeUtf8("\r\n");
         this.state = 1;
      }
   }

   public void writeRequestHeaders(Request var1) throws IOException {
      String var2 = RequestLine.get(var1, this.streamAllocation.connection().route().proxy().type());
      this.writeRequest(var1.headers(), var2);
   }

   private abstract class AbstractSource implements Source {
      protected boolean closed;
      protected final ForwardingTimeout timeout;

      private AbstractSource() {
         this.timeout = new ForwardingTimeout(Http1Codec.this.source.timeout());
      }

      // $FF: synthetic method
      AbstractSource(Object var2) {
         this();
      }

      protected final void endOfInput(boolean var1) throws IOException {
         if (Http1Codec.this.state != 6) {
            if (Http1Codec.this.state == 5) {
               Http1Codec.this.detachTimeout(this.timeout);
               Http1Codec.this.state = 6;
               if (Http1Codec.this.streamAllocation != null) {
                  Http1Codec.this.streamAllocation.streamFinished(var1 ^ true, Http1Codec.this);
               }

            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("state: ");
               var2.append(Http1Codec.this.state);
               throw new IllegalStateException(var2.toString());
            }
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }
   }

   private final class ChunkedSink implements Sink {
      private boolean closed;
      private final ForwardingTimeout timeout;

      ChunkedSink() {
         this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
      }

      public void close() throws IOException {
         synchronized(this){}

         Throwable var10000;
         label78: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.closed;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label78;
            }

            if (var1) {
               return;
            }

            try {
               this.closed = true;
               Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
               Http1Codec.this.detachTimeout(this.timeout);
               Http1Codec.this.state = 3;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label78;
            }

            return;
         }

         Throwable var2 = var10000;
         throw var2;
      }

      public void flush() throws IOException {
         synchronized(this){}

         Throwable var10000;
         label78: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.closed;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label78;
            }

            if (var1) {
               return;
            }

            try {
               Http1Codec.this.sink.flush();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label78;
            }

            return;
         }

         Throwable var2 = var10000;
         throw var2;
      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         if (!this.closed) {
            if (var2 != 0L) {
               Http1Codec.this.sink.writeHexadecimalUnsignedLong(var2);
               Http1Codec.this.sink.writeUtf8("\r\n");
               Http1Codec.this.sink.write(var1, var2);
               Http1Codec.this.sink.writeUtf8("\r\n");
            }
         } else {
            throw new IllegalStateException("closed");
         }
      }
   }

   private class ChunkedSource extends Http1Codec.AbstractSource {
      private static final long NO_CHUNK_YET = -1L;
      private long bytesRemainingInChunk = -1L;
      private boolean hasMoreChunks = true;
      private final HttpUrl url;

      ChunkedSource(HttpUrl var2) {
         super(null);
         this.url = var2;
      }

      private void readChunkSize() throws IOException {
         if (this.bytesRemainingInChunk != -1L) {
            Http1Codec.this.source.readUtf8LineStrict();
         }

         label37: {
            NumberFormatException var10000;
            label36: {
               boolean var10001;
               String var2;
               label35: {
                  boolean var1;
                  try {
                     this.bytesRemainingInChunk = Http1Codec.this.source.readHexadecimalUnsignedLong();
                     var2 = Http1Codec.this.source.readUtf8LineStrict().trim();
                     if (this.bytesRemainingInChunk < 0L) {
                        break label35;
                     }

                     if (var2.isEmpty()) {
                        break label37;
                     }

                     var1 = var2.startsWith(";");
                  } catch (NumberFormatException var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label36;
                  }

                  if (var1) {
                     break label37;
                  }
               }

               try {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("expected chunk size and optional extensions but was \"");
                  var3.append(this.bytesRemainingInChunk);
                  var3.append(var2);
                  var3.append("\"");
                  throw new ProtocolException(var3.toString());
               } catch (NumberFormatException var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            NumberFormatException var6 = var10000;
            throw new ProtocolException(var6.getMessage());
         }

         if (this.bytesRemainingInChunk == 0L) {
            this.hasMoreChunks = false;
            HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.url, Http1Codec.this.readHeaders());
            this.endOfInput(true);
         }

      }

      public void close() throws IOException {
         if (!this.closed) {
            if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
               this.endOfInput(false);
            }

            this.closed = true;
         }
      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 < 0L) {
            StringBuilder var6 = new StringBuilder();
            var6.append("byteCount < 0: ");
            var6.append(var2);
            throw new IllegalArgumentException(var6.toString());
         } else if (!this.closed) {
            if (!this.hasMoreChunks) {
               return -1L;
            } else {
               long var4 = this.bytesRemainingInChunk;
               if (var4 == 0L || var4 == -1L) {
                  this.readChunkSize();
                  if (!this.hasMoreChunks) {
                     return -1L;
                  }
               }

               var2 = Http1Codec.this.source.read(var1, Math.min(var2, this.bytesRemainingInChunk));
               if (var2 != -1L) {
                  this.bytesRemainingInChunk -= var2;
                  return var2;
               } else {
                  this.endOfInput(false);
                  throw new ProtocolException("unexpected end of stream");
               }
            }
         } else {
            throw new IllegalStateException("closed");
         }
      }
   }

   private final class FixedLengthSink implements Sink {
      private long bytesRemaining;
      private boolean closed;
      private final ForwardingTimeout timeout;

      FixedLengthSink(long var2) {
         this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
         this.bytesRemaining = var2;
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            if (this.bytesRemaining <= 0L) {
               Http1Codec.this.detachTimeout(this.timeout);
               Http1Codec.this.state = 3;
            } else {
               throw new ProtocolException("unexpected end of stream");
            }
         }
      }

      public void flush() throws IOException {
         if (!this.closed) {
            Http1Codec.this.sink.flush();
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         if (!this.closed) {
            Util.checkOffsetAndCount(var1.size(), 0L, var2);
            if (var2 <= this.bytesRemaining) {
               Http1Codec.this.sink.write(var1, var2);
               this.bytesRemaining -= var2;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("expected ");
               var4.append(this.bytesRemaining);
               var4.append(" bytes but received ");
               var4.append(var2);
               throw new ProtocolException(var4.toString());
            }
         } else {
            throw new IllegalStateException("closed");
         }
      }
   }

   private class FixedLengthSource extends Http1Codec.AbstractSource {
      private long bytesRemaining;

      FixedLengthSource(long var2) throws IOException {
         super(null);
         this.bytesRemaining = var2;
         if (var2 == 0L) {
            this.endOfInput(true);
         }

      }

      public void close() throws IOException {
         if (!this.closed) {
            if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
               this.endOfInput(false);
            }

            this.closed = true;
         }
      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 >= 0L) {
            if (!this.closed) {
               if (this.bytesRemaining == 0L) {
                  return -1L;
               } else {
                  var2 = Http1Codec.this.source.read(var1, Math.min(this.bytesRemaining, var2));
                  if (var2 != -1L) {
                     long var4 = this.bytesRemaining - var2;
                     this.bytesRemaining = var4;
                     if (var4 == 0L) {
                        this.endOfInput(true);
                     }

                     return var2;
                  } else {
                     this.endOfInput(false);
                     throw new ProtocolException("unexpected end of stream");
                  }
               }
            } else {
               throw new IllegalStateException("closed");
            }
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("byteCount < 0: ");
            var6.append(var2);
            throw new IllegalArgumentException(var6.toString());
         }
      }
   }

   private class UnknownLengthSource extends Http1Codec.AbstractSource {
      private boolean inputExhausted;

      UnknownLengthSource() {
         super(null);
      }

      public void close() throws IOException {
         if (!this.closed) {
            if (!this.inputExhausted) {
               this.endOfInput(false);
            }

            this.closed = true;
         }
      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 >= 0L) {
            if (!this.closed) {
               if (this.inputExhausted) {
                  return -1L;
               } else {
                  var2 = Http1Codec.this.source.read(var1, var2);
                  if (var2 == -1L) {
                     this.inputExhausted = true;
                     this.endOfInput(true);
                     return -1L;
                  } else {
                     return var2;
                  }
               }
            } else {
               throw new IllegalStateException("closed");
            }
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("byteCount < 0: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }
      }
   }
}
