package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AbstractSessionInputBuffer implements SessionInputBuffer, BufferInfo {
   private boolean ascii;
   private byte[] buffer;
   private int bufferLen;
   private int bufferPos;
   private CharBuffer cbuf;
   private Charset charset;
   private CharsetDecoder decoder;
   private InputStream inStream;
   private ByteArrayBuffer lineBuffer;
   private int maxLineLen;
   private HttpTransportMetricsImpl metrics;
   private int minChunkLimit;
   private CodingErrorAction onMalformedCharAction;
   private CodingErrorAction onUnmappableCharAction;

   private int appendDecoded(CharArrayBuffer var1, ByteBuffer var2) throws IOException {
      if (!var2.hasRemaining()) {
         return 0;
      } else {
         if (this.decoder == null) {
            CharsetDecoder var5 = this.charset.newDecoder();
            this.decoder = var5;
            var5.onMalformedInput(this.onMalformedCharAction);
            this.decoder.onUnmappableCharacter(this.onUnmappableCharAction);
         }

         if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(1024);
         }

         this.decoder.reset();

         int var3;
         for(var3 = 0; var2.hasRemaining(); var3 += this.handleDecodingResult(this.decoder.decode(var2, this.cbuf, true), var1, var2)) {
         }

         int var4 = this.handleDecodingResult(this.decoder.flush(this.cbuf), var1, var2);
         this.cbuf.clear();
         return var3 + var4;
      }
   }

   private int handleDecodingResult(CoderResult var1, CharArrayBuffer var2, ByteBuffer var3) throws IOException {
      if (var1.isError()) {
         var1.throwException();
      }

      this.cbuf.flip();
      int var4 = this.cbuf.remaining();

      while(this.cbuf.hasRemaining()) {
         var2.append(this.cbuf.get());
      }

      this.cbuf.compact();
      return var4;
   }

   private int lineFromLineBuffer(CharArrayBuffer var1) throws IOException {
      int var4 = this.lineBuffer.length();
      int var2 = var4;
      if (var4 > 0) {
         int var3 = var4;
         if (this.lineBuffer.byteAt(var4 - 1) == 10) {
            var3 = var4 - 1;
         }

         var2 = var3;
         if (var3 > 0) {
            var2 = var3;
            if (this.lineBuffer.byteAt(var3 - 1) == 13) {
               var2 = var3 - 1;
            }
         }
      }

      if (this.ascii) {
         var1.append(this.lineBuffer, 0, var2);
      } else {
         var2 = this.appendDecoded(var1, ByteBuffer.wrap(this.lineBuffer.buffer(), 0, var2));
      }

      this.lineBuffer.clear();
      return var2;
   }

   private int lineFromReadBuffer(CharArrayBuffer var1, int var2) throws IOException {
      int var4 = this.bufferPos;
      this.bufferPos = var2 + 1;
      int var3 = var2;
      if (var2 > var4) {
         var3 = var2;
         if (this.buffer[var2 - 1] == 13) {
            var3 = var2 - 1;
         }
      }

      var2 = var3 - var4;
      if (this.ascii) {
         var1.append(this.buffer, var4, var2);
         return var2;
      } else {
         return this.appendDecoded(var1, ByteBuffer.wrap(this.buffer, var4, var2));
      }
   }

   private int locateLF() {
      for(int var1 = this.bufferPos; var1 < this.bufferLen; ++var1) {
         if (this.buffer[var1] == 10) {
            return var1;
         }
      }

      return -1;
   }

   public int available() {
      return this.capacity() - this.length();
   }

   public int capacity() {
      return this.buffer.length;
   }

   protected HttpTransportMetricsImpl createTransportMetrics() {
      return new HttpTransportMetricsImpl();
   }

   protected int fillBuffer() throws IOException {
      int var1 = this.bufferPos;
      int var2;
      byte[] var3;
      if (var1 > 0) {
         var2 = this.bufferLen - var1;
         if (var2 > 0) {
            var3 = this.buffer;
            System.arraycopy(var3, var1, var3, 0, var2);
         }

         this.bufferPos = 0;
         this.bufferLen = var2;
      }

      var1 = this.bufferLen;
      var3 = this.buffer;
      var2 = var3.length;
      var2 = this.inStream.read(var3, var1, var2 - var1);
      if (var2 == -1) {
         return -1;
      } else {
         this.bufferLen = var1 + var2;
         this.metrics.incrementBytesTransferred((long)var2);
         return var2;
      }
   }

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }

   protected boolean hasBufferedData() {
      return this.bufferPos < this.bufferLen;
   }

   protected void init(InputStream var1, int var2, HttpParams var3) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      Args.notNull(var3, "HTTP parameters");
      this.inStream = var1;
      this.buffer = new byte[var2];
      this.bufferPos = 0;
      this.bufferLen = 0;
      this.lineBuffer = new ByteArrayBuffer(var2);
      String var4 = (String)var3.getParameter("http.protocol.element-charset");
      Charset var5;
      if (var4 != null) {
         var5 = Charset.forName(var4);
      } else {
         var5 = Consts.ASCII;
      }

      this.charset = var5;
      this.ascii = var5.equals(Consts.ASCII);
      this.decoder = null;
      this.maxLineLen = var3.getIntParameter("http.connection.max-line-length", -1);
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
      return this.bufferLen - this.bufferPos;
   }

   public int read() throws IOException {
      while(true) {
         if (!this.hasBufferedData()) {
            if (this.fillBuffer() != -1) {
               continue;
            }

            return -1;
         }

         byte[] var2 = this.buffer;
         int var1 = this.bufferPos++;
         return var2[var1] & 255;
      }
   }

   public int read(byte[] var1) throws IOException {
      return var1 == null ? 0 : this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         return 0;
      } else if (this.hasBufferedData()) {
         var3 = Math.min(var3, this.bufferLen - this.bufferPos);
         System.arraycopy(this.buffer, this.bufferPos, var1, var2, var3);
         this.bufferPos += var3;
         return var3;
      } else if (var3 > this.minChunkLimit) {
         var2 = this.inStream.read(var1, var2, var3);
         if (var2 > 0) {
            this.metrics.incrementBytesTransferred((long)var2);
         }

         return var2;
      } else {
         do {
            if (this.hasBufferedData()) {
               var3 = Math.min(var3, this.bufferLen - this.bufferPos);
               System.arraycopy(this.buffer, this.bufferPos, var1, var2, var3);
               this.bufferPos += var3;
               return var3;
            }
         } while(this.fillBuffer() != -1);

         return -1;
      }
   }

   public int readLine(CharArrayBuffer var1) throws IOException {
      Args.notNull(var1, "Char array buffer");
      int var3 = 0;
      boolean var2 = true;

      do {
         if (!var2) {
            if (var3 == -1 && this.lineBuffer.isEmpty()) {
               return -1;
            }

            return this.lineFromLineBuffer(var1);
         }

         int var4 = this.locateLF();
         if (var4 != -1) {
            if (this.lineBuffer.isEmpty()) {
               return this.lineFromReadBuffer(var1, var4);
            }

            var2 = false;
            int var5 = this.bufferPos;
            this.lineBuffer.append(this.buffer, var5, var4 + 1 - var5);
            this.bufferPos = var4 + 1;
         } else {
            if (this.hasBufferedData()) {
               var3 = this.bufferLen;
               var4 = this.bufferPos;
               this.lineBuffer.append(this.buffer, var4, var3 - var4);
               this.bufferPos = this.bufferLen;
            }

            var4 = this.fillBuffer();
            var3 = var4;
            if (var4 == -1) {
               var2 = false;
               var3 = var4;
            }
         }
      } while(this.maxLineLen <= 0 || this.lineBuffer.length() < this.maxLineLen);

      throw new IOException("Maximum line length limit exceeded");
   }

   public String readLine() throws IOException {
      CharArrayBuffer var1 = new CharArrayBuffer(64);
      return this.readLine(var1) != -1 ? var1.toString() : null;
   }
}
