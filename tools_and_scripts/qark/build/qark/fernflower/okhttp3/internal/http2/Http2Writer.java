package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

final class Http2Writer implements Closeable {
   private static final Logger logger = Logger.getLogger(Http2.class.getName());
   private final boolean client;
   private boolean closed;
   private final Buffer hpackBuffer;
   final Hpack.Writer hpackWriter;
   private int maxFrameSize;
   private final BufferedSink sink;

   Http2Writer(BufferedSink var1, boolean var2) {
      this.sink = var1;
      this.client = var2;
      Buffer var3 = new Buffer();
      this.hpackBuffer = var3;
      this.hpackWriter = new Hpack.Writer(var3);
      this.maxFrameSize = 16384;
   }

   private void writeContinuationFrames(int var1, long var2) throws IOException {
      while(var2 > 0L) {
         int var5 = (int)Math.min((long)this.maxFrameSize, var2);
         var2 -= (long)var5;
         byte var4;
         if (var2 == 0L) {
            var4 = 4;
         } else {
            var4 = 0;
         }

         this.frameHeader(var1, var5, (byte)9, var4);
         this.sink.write(this.hpackBuffer, (long)var5);
      }

   }

   private static void writeMedium(BufferedSink var0, int var1) throws IOException {
      var0.writeByte(var1 >>> 16 & 255);
      var0.writeByte(var1 >>> 8 & 255);
      var0.writeByte(var1 & 255);
   }

   public void applyAndAckSettings(Settings var1) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         this.maxFrameSize = var1.getMaxFrameSize(this.maxFrameSize);
         if (var1.getHeaderTableSize() != -1) {
            this.hpackWriter.setHeaderTableSizeSetting(var1.getHeaderTableSize());
         }

         this.frameHeader(0, 0, (byte)4, (byte)1);
         this.sink.flush();
      } finally {
         ;
      }

   }

   public void close() throws IOException {
      synchronized(this){}

      try {
         this.closed = true;
         this.sink.close();
      } finally {
         ;
      }

   }

   public void connectionPreface() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label224: {
         boolean var1;
         boolean var10001;
         label219: {
            try {
               if (!this.closed) {
                  var1 = this.client;
                  break label219;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label224;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label224;
            }
         }

         if (!var1) {
            return;
         }

         try {
            if (logger.isLoggable(Level.FINE)) {
               logger.fine(Util.format(">> CONNECTION %s", Http2.CONNECTION_PREFACE.hex()));
            }
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label224;
         }

         try {
            this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
            this.sink.flush();
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label224;
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void data(boolean var1, int var2, Buffer var3, int var4) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label141: {
         boolean var10001;
         label135: {
            try {
               if (!this.closed) {
                  break label135;
               }
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label141;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label141;
            }
         }

         byte var5 = 0;
         if (var1) {
            var5 = (byte)(0 | 1);
         }

         try {
            this.dataFrame(var2, var5, var3, var4);
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label141;
         }

         return;
      }

      Throwable var18 = var10000;
      throw var18;
   }

   void dataFrame(int var1, byte var2, Buffer var3, int var4) throws IOException {
      this.frameHeader(var1, var4, (byte)0, var2);
      if (var4 > 0) {
         this.sink.write(var3, (long)var4);
      }

   }

   public void flush() throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         this.sink.flush();
      } finally {
         ;
      }

   }

   public void frameHeader(int var1, int var2, byte var3, byte var4) throws IOException {
      if (logger.isLoggable(Level.FINE)) {
         logger.fine(Http2.frameLog(false, var1, var2, var3, var4));
      }

      int var5 = this.maxFrameSize;
      if (var2 <= var5) {
         if ((Integer.MIN_VALUE & var1) == 0) {
            writeMedium(this.sink, var2);
            this.sink.writeByte(var3 & 255);
            this.sink.writeByte(var4 & 255);
            this.sink.writeInt(Integer.MAX_VALUE & var1);
         } else {
            throw Http2.illegalArgument("reserved bit set: %s", var1);
         }
      } else {
         throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", var5, var2);
      }
   }

   public void goAway(int var1, ErrorCode var2, byte[] var3) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         if (var2.httpCode == -1) {
            throw Http2.illegalArgument("errorCode.httpCode == -1");
         }

         this.frameHeader(0, var3.length + 8, (byte)7, (byte)0);
         this.sink.writeInt(var1);
         this.sink.writeInt(var2.httpCode);
         if (var3.length > 0) {
            this.sink.write(var3);
         }

         this.sink.flush();
      } finally {
         ;
      }

   }

   public void headers(int var1, List var2) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         this.headers(false, var1, var2);
      } finally {
         ;
      }

   }

   void headers(boolean var1, int var2, List var3) throws IOException {
      if (!this.closed) {
         this.hpackWriter.writeHeaders(var3);
         long var7 = this.hpackBuffer.size();
         int var6 = (int)Math.min((long)this.maxFrameSize, var7);
         byte var4;
         if (var7 == (long)var6) {
            var4 = 4;
         } else {
            var4 = 0;
         }

         byte var5 = var4;
         if (var1) {
            var5 = (byte)(var4 | 1);
         }

         this.frameHeader(var2, var6, (byte)1, var5);
         this.sink.write(this.hpackBuffer, (long)var6);
         if (var7 > (long)var6) {
            this.writeContinuationFrames(var2, var7 - (long)var6);
         }

      } else {
         throw new IOException("closed");
      }
   }

   public int maxDataLength() {
      return this.maxFrameSize;
   }

   public void ping(boolean var1, int var2, int var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         label131: {
            try {
               if (!this.closed) {
                  break label131;
               }
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label136;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label136;
            }
         }

         byte var4;
         if (var1) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         try {
            this.frameHeader(0, 8, (byte)6, var4);
            this.sink.writeInt(var2);
            this.sink.writeInt(var3);
            this.sink.flush();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label136;
         }

         return;
      }

      Throwable var5 = var10000;
      throw var5;
   }

   public void pushPromise(int var1, int var2, List var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label313: {
         int var5;
         long var6;
         boolean var10001;
         label307: {
            try {
               if (!this.closed) {
                  this.hpackWriter.writeHeaders(var3);
                  var6 = this.hpackBuffer.size();
                  var5 = this.maxFrameSize;
                  break label307;
               }
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label313;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label313;
            }
         }

         byte var4 = 4;

         try {
            var5 = (int)Math.min((long)(var5 - 4), var6);
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label313;
         }

         if (var6 != (long)var5) {
            var4 = 0;
         }

         try {
            this.frameHeader(var1, var5 + 4, (byte)5, var4);
            this.sink.writeInt(Integer.MAX_VALUE & var2);
            this.sink.write(this.hpackBuffer, (long)var5);
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label313;
         }

         if (var6 > (long)var5) {
            try {
               this.writeContinuationFrames(var1, var6 - (long)var5);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label313;
            }
         }

         return;
      }

      Throwable var38 = var10000;
      throw var38;
   }

   public void rstStream(int var1, ErrorCode var2) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         if (var2.httpCode == -1) {
            throw new IllegalArgumentException();
         }

         this.frameHeader(var1, 4, (byte)3, (byte)0);
         this.sink.writeInt(var2.httpCode);
         this.sink.flush();
      } finally {
         ;
      }

   }

   public void settings(Settings var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label354: {
         boolean var10001;
         label349: {
            try {
               if (!this.closed) {
                  this.frameHeader(0, var1.size() * 6, (byte)4, (byte)0);
                  break label349;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label354;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label354;
            }
         }

         int var2 = 0;

         while(true) {
            if (var2 >= 10) {
               try {
                  this.sink.flush();
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break;
               }

               return;
            }

            label356: {
               try {
                  if (!var1.isSet(var2)) {
                     break label356;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break;
               }

               int var3;
               if (var2 == 4) {
                  var3 = 3;
               } else {
                  var3 = var2;
                  if (var2 == 7) {
                     var3 = 4;
                  }
               }

               try {
                  this.sink.writeShort(var3);
                  this.sink.writeInt(var1.get(var2));
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break;
               }
            }

            ++var2;
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }

   public void synReply(boolean var1, int var2, List var3) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         this.headers(var1, var2, var3);
      } finally {
         ;
      }

   }

   public void synStream(boolean var1, int var2, int var3, List var4) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            throw new IOException("closed");
         }

         this.headers(var1, var2, var4);
      } finally {
         ;
      }

   }

   public void windowUpdate(int var1, long var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label195: {
         boolean var10001;
         label190: {
            try {
               if (!this.closed) {
                  break label190;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label195;
            }

            try {
               throw new IOException("closed");
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label195;
            }
         }

         if (var2 != 0L && var2 <= 2147483647L) {
            label177: {
               try {
                  this.frameHeader(var1, 4, (byte)8, (byte)0);
                  this.sink.writeInt((int)var2);
                  this.sink.flush();
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label177;
               }

               return;
            }
         } else {
            label179:
            try {
               throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", var2);
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label179;
            }
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }
}
