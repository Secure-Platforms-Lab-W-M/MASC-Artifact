package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo {
   private static final byte[] CRLF = new byte[]{13, 10};
   private boolean ascii;
   private ByteBuffer bbuf;
   private ByteArrayBuffer buffer;
   private Charset charset;
   private CharsetEncoder encoder;
   private HttpTransportMetricsImpl metrics;
   private int minChunkLimit;
   private CodingErrorAction onMalformedCharAction;
   private CodingErrorAction onUnmappableCharAction;
   private OutputStream outStream;

   public AbstractSessionOutputBuffer() {
   }

   protected AbstractSessionOutputBuffer(OutputStream var1, int var2, Charset var3, int var4, CodingErrorAction var5, CodingErrorAction var6) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      this.outStream = var1;
      this.buffer = new ByteArrayBuffer(var2);
      if (var3 == null) {
         var3 = Consts.ASCII;
      }

      this.charset = var3;
      this.ascii = var3.equals(Consts.ASCII);
      this.encoder = null;
      if (var4 < 0) {
         var4 = 512;
      }

      this.minChunkLimit = var4;
      this.metrics = this.createTransportMetrics();
      if (var5 == null) {
         var5 = CodingErrorAction.REPORT;
      }

      this.onMalformedCharAction = var5;
      if (var6 == null) {
         var6 = CodingErrorAction.REPORT;
      }

      this.onUnmappableCharAction = var6;
   }

   private void handleEncodingResult(CoderResult var1) throws IOException {
      if (var1.isError()) {
         var1.throwException();
      }

      this.bbuf.flip();

      while(this.bbuf.hasRemaining()) {
         this.write(this.bbuf.get());
      }

      this.bbuf.compact();
   }

   private void writeEncoded(CharBuffer var1) throws IOException {
      if (var1.hasRemaining()) {
         if (this.encoder == null) {
            CharsetEncoder var2 = this.charset.newEncoder();
            this.encoder = var2;
            var2.onMalformedInput(this.onMalformedCharAction);
            this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
         }

         if (this.bbuf == null) {
            this.bbuf = ByteBuffer.allocate(1024);
         }

         this.encoder.reset();

         while(var1.hasRemaining()) {
            this.handleEncodingResult(this.encoder.encode(var1, this.bbuf, true));
         }

         this.handleEncodingResult(this.encoder.flush(this.bbuf));
         this.bbuf.clear();
      }
   }

   public int available() {
      return this.capacity() - this.length();
   }

   public int capacity() {
      return this.buffer.capacity();
   }

   protected HttpTransportMetricsImpl createTransportMetrics() {
      return new HttpTransportMetricsImpl();
   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.outStream.flush();
   }

   protected void flushBuffer() throws IOException {
      int var1 = this.buffer.length();
      if (var1 > 0) {
         this.outStream.write(this.buffer.buffer(), 0, var1);
         this.buffer.clear();
         this.metrics.incrementBytesTransferred((long)var1);
      }

   }

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }

   protected void init(OutputStream var1, int var2, HttpParams var3) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      Args.notNull(var3, "HTTP parameters");
      this.outStream = var1;
      this.buffer = new ByteArrayBuffer(var2);
      String var4 = (String)var3.getParameter("http.protocol.element-charset");
      Charset var5;
      if (var4 != null) {
         var5 = Charset.forName(var4);
      } else {
         var5 = Consts.ASCII;
      }

      this.charset = var5;
      this.ascii = var5.equals(Consts.ASCII);
      this.encoder = null;
      this.minChunkLimit = var3.getIntParameter("http.connection.min-chunk-limit", 512);
      this.metrics = this.createTransportMetrics();
      CodingErrorAction var6 = (CodingErrorAction)var3.getParameter("http.malformed.input.action");
      if (var6 == null) {
         var6 = CodingErrorAction.REPORT;
      }

      this.onMalformedCharAction = var6;
      var6 = (CodingErrorAction)var3.getParameter("http.unmappable.input.action");
      if (var6 == null) {
         var6 = CodingErrorAction.REPORT;
      }

      this.onUnmappableCharAction = var6;
   }

   public int length() {
      return this.buffer.length();
   }

   public void write(int var1) throws IOException {
      if (this.buffer.isFull()) {
         this.flushBuffer();
      }

      this.buffer.append(var1);
   }

   public void write(byte[] var1) throws IOException {
      if (var1 != null) {
         this.write(var1, 0, var1.length);
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1 != null) {
         if (var3 <= this.minChunkLimit && var3 <= this.buffer.capacity()) {
            if (var3 > this.buffer.capacity() - this.buffer.length()) {
               this.flushBuffer();
            }

            this.buffer.append(var1, var2, var3);
         } else {
            this.flushBuffer();
            this.outStream.write(var1, var2, var3);
            this.metrics.incrementBytesTransferred((long)var3);
         }
      }
   }

   public void writeLine(String var1) throws IOException {
      if (var1 != null) {
         if (var1.length() > 0) {
            if (this.ascii) {
               for(int var2 = 0; var2 < var1.length(); ++var2) {
                  this.write(var1.charAt(var2));
               }
            } else {
               this.writeEncoded(CharBuffer.wrap(var1));
            }
         }

         this.write(CRLF);
      }
   }

   public void writeLine(CharArrayBuffer var1) throws IOException {
      if (var1 != null) {
         if (this.ascii) {
            int var3 = 0;

            int var4;
            for(int var2 = var1.length(); var2 > 0; var2 -= var4) {
               var4 = Math.min(this.buffer.capacity() - this.buffer.length(), var2);
               if (var4 > 0) {
                  this.buffer.append(var1, var3, var4);
               }

               if (this.buffer.isFull()) {
                  this.flushBuffer();
               }

               var3 += var4;
            }
         } else {
            this.writeEncoded(CharBuffer.wrap(var1.buffer(), 0, var1.length()));
         }

         this.write(CRLF);
      }
   }
}
