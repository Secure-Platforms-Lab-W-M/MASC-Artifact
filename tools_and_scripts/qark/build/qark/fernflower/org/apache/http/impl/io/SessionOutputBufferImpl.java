package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public class SessionOutputBufferImpl implements SessionOutputBuffer, BufferInfo {
   private static final byte[] CRLF = new byte[]{13, 10};
   private ByteBuffer bbuf;
   private final ByteArrayBuffer buffer;
   private final CharsetEncoder encoder;
   private final int fragementSizeHint;
   private final HttpTransportMetricsImpl metrics;
   private OutputStream outStream;

   public SessionOutputBufferImpl(HttpTransportMetricsImpl var1, int var2) {
      this(var1, var2, var2, (CharsetEncoder)null);
   }

   public SessionOutputBufferImpl(HttpTransportMetricsImpl var1, int var2, int var3, CharsetEncoder var4) {
      Args.positive(var2, "Buffer size");
      Args.notNull(var1, "HTTP transport metrcis");
      this.metrics = var1;
      this.buffer = new ByteArrayBuffer(var2);
      if (var3 < 0) {
         var3 = 0;
      }

      this.fragementSizeHint = var3;
      this.encoder = var4;
   }

   private void flushBuffer() throws IOException {
      int var1 = this.buffer.length();
      if (var1 > 0) {
         this.streamWrite(this.buffer.buffer(), 0, var1);
         this.buffer.clear();
         this.metrics.incrementBytesTransferred((long)var1);
      }

   }

   private void flushStream() throws IOException {
      OutputStream var1 = this.outStream;
      if (var1 != null) {
         var1.flush();
      }

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

   private void streamWrite(byte[] var1, int var2, int var3) throws IOException {
      Asserts.notNull(this.outStream, "Output stream");
      this.outStream.write(var1, var2, var3);
   }

   private void writeEncoded(CharBuffer var1) throws IOException {
      if (var1.hasRemaining()) {
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

   public void bind(OutputStream var1) {
      this.outStream = var1;
   }

   public int capacity() {
      return this.buffer.capacity();
   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.flushStream();
   }

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }

   public boolean isBound() {
      return this.outStream != null;
   }

   public int length() {
      return this.buffer.length();
   }

   public void write(int var1) throws IOException {
      if (this.fragementSizeHint > 0) {
         if (this.buffer.isFull()) {
            this.flushBuffer();
         }

         this.buffer.append(var1);
      } else {
         this.flushBuffer();
         this.outStream.write(var1);
      }
   }

   public void write(byte[] var1) throws IOException {
      if (var1 != null) {
         this.write(var1, 0, var1.length);
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1 != null) {
         if (var3 <= this.fragementSizeHint && var3 <= this.buffer.capacity()) {
            if (var3 > this.buffer.capacity() - this.buffer.length()) {
               this.flushBuffer();
            }

            this.buffer.append(var1, var2, var3);
         } else {
            this.flushBuffer();
            this.streamWrite(var1, var2, var3);
            this.metrics.incrementBytesTransferred((long)var3);
         }
      }
   }

   public void writeLine(String var1) throws IOException {
      if (var1 != null) {
         if (var1.length() > 0) {
            if (this.encoder == null) {
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
         if (this.encoder == null) {
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
