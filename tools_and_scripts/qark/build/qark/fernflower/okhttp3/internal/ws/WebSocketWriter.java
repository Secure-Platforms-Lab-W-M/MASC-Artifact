package okhttp3.internal.ws;

import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

final class WebSocketWriter {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   boolean activeWriter;
   final Buffer buffer = new Buffer();
   final WebSocketWriter.FrameSink frameSink = new WebSocketWriter.FrameSink();
   final boolean isClient;
   final byte[] maskBuffer;
   final byte[] maskKey;
   final Random random;
   final BufferedSink sink;
   boolean writerClosed;

   WebSocketWriter(boolean var1, BufferedSink var2, Random var3) {
      if (var2 != null) {
         if (var3 != null) {
            this.isClient = var1;
            this.sink = var2;
            this.random = var3;
            var3 = null;
            byte[] var4;
            if (var1) {
               var4 = new byte[4];
            } else {
               var4 = null;
            }

            this.maskKey = var4;
            var4 = (byte[])var3;
            if (var1) {
               var4 = new byte[8192];
            }

            this.maskBuffer = var4;
         } else {
            throw new NullPointerException("random == null");
         }
      } else {
         throw new NullPointerException("sink == null");
      }
   }

   private void writeControlFrameSynchronized(int var1, ByteString var2) throws IOException {
      if (!this.writerClosed) {
         int var3 = var2.size();
         if ((long)var3 <= 125L) {
            this.sink.writeByte(var1 | 128);
            if (this.isClient) {
               this.sink.writeByte(var3 | 128);
               this.random.nextBytes(this.maskKey);
               this.sink.write(this.maskKey);
               byte[] var4 = var2.toByteArray();
               WebSocketProtocol.toggleMask(var4, (long)var4.length, this.maskKey, 0L);
               this.sink.write(var4);
            } else {
               this.sink.writeByte(var3);
               this.sink.write(var2);
            }

            this.sink.flush();
         } else {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
         }
      } else {
         throw new IOException("closed");
      }
   }

   Sink newMessageSink(int var1, long var2) {
      if (!this.activeWriter) {
         this.activeWriter = true;
         this.frameSink.formatOpcode = var1;
         this.frameSink.contentLength = var2;
         this.frameSink.isFirstFrame = true;
         this.frameSink.closed = false;
         return this.frameSink;
      } else {
         throw new IllegalStateException("Another message writer is active. Did you call close()?");
      }
   }

   void writeClose(int var1, ByteString var2) throws IOException {
      ByteString var3 = ByteString.EMPTY;
      if (var1 != 0 || var2 != null) {
         if (var1 != 0) {
            WebSocketProtocol.validateCloseCode(var1);
         }

         Buffer var24 = new Buffer();
         var24.writeShort(var1);
         if (var2 != null) {
            var24.write(var2);
         }

         var3 = var24.readByteString();
      }

      synchronized(this){}

      try {
         this.writeControlFrameSynchronized(8, var3);
      } finally {
         try {
            this.writerClosed = true;
         } catch (Throwable var21) {
            Throwable var10000 = var21;
            boolean var10001 = false;

            while(true) {
               Throwable var23 = var10000;

               try {
                  throw var23;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

   }

   void writeMessageFrameSynchronized(int var1, long var2, boolean var4, boolean var5) throws IOException {
      if (this.writerClosed) {
         throw new IOException("closed");
      } else {
         if (!var4) {
            var1 = 0;
         }

         int var6 = var1;
         if (var5) {
            var6 = var1 | 128;
         }

         this.sink.writeByte(var6);
         var1 = 0;
         if (this.isClient) {
            var1 = 0 | 128;
         }

         if (var2 <= 125L) {
            var6 = (int)var2;
            this.sink.writeByte(var1 | var6);
         } else if (var2 <= 65535L) {
            this.sink.writeByte(var1 | 126);
            this.sink.writeShort((int)var2);
         } else {
            this.sink.writeByte(var1 | 127);
            this.sink.writeLong(var2);
         }

         if (this.isClient) {
            this.random.nextBytes(this.maskKey);
            this.sink.write(this.maskKey);

            for(long var7 = 0L; var7 < var2; var7 += (long)var1) {
               var1 = (int)Math.min(var2, (long)this.maskBuffer.length);
               var1 = this.buffer.read(this.maskBuffer, 0, var1);
               if (var1 == -1) {
                  throw new AssertionError();
               }

               WebSocketProtocol.toggleMask(this.maskBuffer, (long)var1, this.maskKey, var7);
               this.sink.write(this.maskBuffer, 0, var1);
            }
         } else {
            this.sink.write(this.buffer, var2);
         }

         this.sink.emit();
      }
   }

   void writePing(ByteString param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   void writePong(ByteString param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   final class FrameSink implements Sink {
      boolean closed;
      long contentLength;
      int formatOpcode;
      boolean isFirstFrame;

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void flush() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public Timeout timeout() {
         return WebSocketWriter.this.sink.timeout();
      }

      public void write(Buffer param1, long param2) throws IOException {
         // $FF: Couldn't be decompiled
      }
   }
}
