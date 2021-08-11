package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Http2Reader implements Closeable {
   static final Logger logger = Logger.getLogger(Http2.class.getName());
   private final boolean client;
   private final Http2Reader.ContinuationSource continuation;
   final Hpack.Reader hpackReader;
   private final BufferedSource source;

   Http2Reader(BufferedSource var1, boolean var2) {
      this.source = var1;
      this.client = var2;
      Http2Reader.ContinuationSource var3 = new Http2Reader.ContinuationSource(var1);
      this.continuation = var3;
      this.hpackReader = new Hpack.Reader(4096, var3);
   }

   static int lengthWithoutPadding(int var0, byte var1, short var2) throws IOException {
      int var3 = var0;
      if ((var1 & 8) != 0) {
         var3 = var0 - 1;
      }

      if (var2 <= var3) {
         return (short)(var3 - var2);
      } else {
         throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", var2, var3);
      }
   }

   private void readData(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      short var5 = 0;
      if (var4 != 0) {
         boolean var6 = true;
         boolean var7;
         if ((var3 & 1) != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         if ((var3 & 32) == 0) {
            var6 = false;
         }

         if (!var6) {
            if ((var3 & 8) != 0) {
               var5 = (short)(this.source.readByte() & 255);
            }

            var2 = lengthWithoutPadding(var2, var3, var5);
            var1.data(var7, var4, this.source, var2);
            this.source.skip((long)var5);
         } else {
            throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA");
         }
      } else {
         throw Http2.ioException("PROTOCOL_ERROR: TYPE_DATA streamId == 0");
      }
   }

   private void readGoAway(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 >= 8) {
         if (var4 == 0) {
            int var7 = this.source.readInt();
            var4 = this.source.readInt();
            var2 -= 8;
            ErrorCode var6 = ErrorCode.fromHttp2(var4);
            if (var6 != null) {
               ByteString var5 = ByteString.EMPTY;
               if (var2 > 0) {
                  var5 = this.source.readByteString((long)var2);
               }

               var1.goAway(var7, var6, var5);
            } else {
               throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", var4);
            }
         } else {
            throw Http2.ioException("TYPE_GOAWAY streamId != 0");
         }
      } else {
         throw Http2.ioException("TYPE_GOAWAY length < 8: %s", var2);
      }
   }

   private List readHeaderBlock(int var1, short var2, byte var3, int var4) throws IOException {
      Http2Reader.ContinuationSource var5 = this.continuation;
      var5.left = var1;
      var5.length = var1;
      this.continuation.padding = var2;
      this.continuation.flags = var3;
      this.continuation.streamId = var4;
      this.hpackReader.readHeaders();
      return this.hpackReader.getAndResetHeaderList();
   }

   private void readHeaders(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      short var5 = 0;
      if (var4 != 0) {
         boolean var7;
         if ((var3 & 1) != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         if ((var3 & 8) != 0) {
            var5 = (short)(this.source.readByte() & 255);
         }

         int var6 = var2;
         if ((var3 & 32) != 0) {
            this.readPriority(var1, var4);
            var6 = var2 - 5;
         }

         var1.headers(var7, var4, -1, this.readHeaderBlock(lengthWithoutPadding(var6, var3, var5), var5, var3, var4));
      } else {
         throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0");
      }
   }

   static int readMedium(BufferedSource var0) throws IOException {
      return (var0.readByte() & 255) << 16 | (var0.readByte() & 255) << 8 | var0.readByte() & 255;
   }

   private void readPing(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      boolean var5 = false;
      if (var2 == 8) {
         if (var4 == 0) {
            var2 = this.source.readInt();
            var4 = this.source.readInt();
            if ((var3 & 1) != 0) {
               var5 = true;
            }

            var1.ping(var5, var2, var4);
         } else {
            throw Http2.ioException("TYPE_PING streamId != 0");
         }
      } else {
         throw Http2.ioException("TYPE_PING length != 8: %s", var2);
      }
   }

   private void readPriority(Http2Reader.Handler var1, int var2) throws IOException {
      int var3 = this.source.readInt();
      boolean var4;
      if ((Integer.MIN_VALUE & var3) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var1.priority(var2, Integer.MAX_VALUE & var3, (this.source.readByte() & 255) + 1, var4);
   }

   private void readPriority(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 == 5) {
         if (var4 != 0) {
            this.readPriority(var1, var4);
         } else {
            throw Http2.ioException("TYPE_PRIORITY streamId == 0");
         }
      } else {
         throw Http2.ioException("TYPE_PRIORITY length: %d != 5", var2);
      }
   }

   private void readPushPromise(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      short var5 = 0;
      if (var4 != 0) {
         if ((var3 & 8) != 0) {
            var5 = (short)(this.source.readByte() & 255);
         }

         var1.pushPromise(var4, this.source.readInt() & Integer.MAX_VALUE, this.readHeaderBlock(lengthWithoutPadding(var2 - 4, var3, var5), var5, var3, var4));
      } else {
         throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0");
      }
   }

   private void readRstStream(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 == 4) {
         if (var4 != 0) {
            var2 = this.source.readInt();
            ErrorCode var5 = ErrorCode.fromHttp2(var2);
            if (var5 != null) {
               var1.rstStream(var4, var5);
            } else {
               throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", var2);
            }
         } else {
            throw Http2.ioException("TYPE_RST_STREAM streamId == 0");
         }
      } else {
         throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", var2);
      }
   }

   private void readSettings(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var4 != 0) {
         throw Http2.ioException("TYPE_SETTINGS streamId != 0");
      } else if ((var3 & 1) != 0) {
         if (var2 == 0) {
            var1.ackSettings();
         } else {
            throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!");
         }
      } else if (var2 % 6 != 0) {
         throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", var2);
      } else {
         Settings var7 = new Settings();

         for(var4 = 0; var4 < var2; var4 += 6) {
            short var5 = this.source.readShort();
            int var6 = this.source.readInt();
            short var8;
            if (var5 != 2) {
               if (var5 != 3) {
                  if (var5 != 4) {
                     if (var5 != 5) {
                        var8 = var5;
                     } else {
                        if (var6 < 16384 || var6 > 16777215) {
                           throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", var6);
                        }

                        var8 = var5;
                     }
                  } else {
                     var8 = 7;
                     if (var6 < 0) {
                        throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1");
                     }
                  }
               } else {
                  var8 = 4;
               }
            } else {
               var8 = var5;
               if (var6 != 0) {
                  if (var6 != 1) {
                     throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1");
                  }

                  var8 = var5;
               }
            }

            var7.set(var8, var6);
         }

         var1.settings(false, var7);
      }
   }

   private void readWindowUpdate(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 == 4) {
         long var5 = (long)this.source.readInt() & 2147483647L;
         if (var5 != 0L) {
            var1.windowUpdate(var4, var5);
         } else {
            throw Http2.ioException("windowSizeIncrement was 0", var5);
         }
      } else {
         throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", var2);
      }
   }

   public void close() throws IOException {
      this.source.close();
   }

   public boolean nextFrame(boolean var1, Http2Reader.Handler var2) throws IOException {
      try {
         this.source.require(9L);
      } catch (IOException var7) {
         return false;
      }

      int var5 = readMedium(this.source);
      if (var5 >= 0 && var5 <= 16384) {
         byte var3 = (byte)(this.source.readByte() & 255);
         if (var1 && var3 != 4) {
            throw Http2.ioException("Expected a SETTINGS frame but was %s", var3);
         } else {
            byte var4 = (byte)(this.source.readByte() & 255);
            int var6 = this.source.readInt() & Integer.MAX_VALUE;
            if (logger.isLoggable(Level.FINE)) {
               logger.fine(Http2.frameLog(true, var6, var5, var3, var4));
            }

            switch(var3) {
            case 0:
               this.readData(var2, var5, var4, var6);
               return true;
            case 1:
               this.readHeaders(var2, var5, var4, var6);
               return true;
            case 2:
               this.readPriority(var2, var5, var4, var6);
               return true;
            case 3:
               this.readRstStream(var2, var5, var4, var6);
               return true;
            case 4:
               this.readSettings(var2, var5, var4, var6);
               return true;
            case 5:
               this.readPushPromise(var2, var5, var4, var6);
               return true;
            case 6:
               this.readPing(var2, var5, var4, var6);
               return true;
            case 7:
               this.readGoAway(var2, var5, var4, var6);
               return true;
            case 8:
               this.readWindowUpdate(var2, var5, var4, var6);
               return true;
            default:
               this.source.skip((long)var5);
               return true;
            }
         }
      } else {
         throw Http2.ioException("FRAME_SIZE_ERROR: %s", var5);
      }
   }

   public void readConnectionPreface(Http2Reader.Handler var1) throws IOException {
      if (this.client) {
         if (!this.nextFrame(true, var1)) {
            throw Http2.ioException("Required SETTINGS preface not received");
         }
      } else {
         ByteString var2 = this.source.readByteString((long)Http2.CONNECTION_PREFACE.size());
         if (logger.isLoggable(Level.FINE)) {
            logger.fine(Util.format("<< CONNECTION %s", var2.hex()));
         }

         if (!Http2.CONNECTION_PREFACE.equals(var2)) {
            throw Http2.ioException("Expected a connection header but was %s", var2.utf8());
         }
      }
   }

   static final class ContinuationSource implements Source {
      byte flags;
      int left;
      int length;
      short padding;
      private final BufferedSource source;
      int streamId;

      ContinuationSource(BufferedSource var1) {
         this.source = var1;
      }

      private void readContinuationHeader() throws IOException {
         int var2 = this.streamId;
         int var3 = Http2Reader.readMedium(this.source);
         this.left = var3;
         this.length = var3;
         byte var1 = (byte)(this.source.readByte() & 255);
         this.flags = (byte)(this.source.readByte() & 255);
         if (Http2Reader.logger.isLoggable(Level.FINE)) {
            Http2Reader.logger.fine(Http2.frameLog(true, this.streamId, this.length, var1, this.flags));
         }

         var3 = this.source.readInt() & Integer.MAX_VALUE;
         this.streamId = var3;
         if (var1 == 9) {
            if (var3 != var2) {
               throw Http2.ioException("TYPE_CONTINUATION streamId changed");
            }
         } else {
            throw Http2.ioException("%s != TYPE_CONTINUATION", var1);
         }
      }

      public void close() throws IOException {
      }

      public long read(Buffer var1, long var2) throws IOException {
         while(true) {
            int var4 = this.left;
            if (var4 != 0) {
               var2 = this.source.read(var1, Math.min(var2, (long)var4));
               if (var2 == -1L) {
                  return -1L;
               }

               this.left = (int)((long)this.left - var2);
               return var2;
            }

            this.source.skip((long)this.padding);
            this.padding = 0;
            if ((this.flags & 4) != 0) {
               return -1L;
            }

            this.readContinuationHeader();
         }
      }

      public Timeout timeout() {
         return this.source.timeout();
      }
   }

   interface Handler {
      void ackSettings();

      void alternateService(int var1, String var2, ByteString var3, String var4, int var5, long var6);

      void data(boolean var1, int var2, BufferedSource var3, int var4) throws IOException;

      void goAway(int var1, ErrorCode var2, ByteString var3);

      void headers(boolean var1, int var2, int var3, List var4);

      void ping(boolean var1, int var2, int var3);

      void priority(int var1, int var2, int var3, boolean var4);

      void pushPromise(int var1, int var2, List var3) throws IOException;

      void rstStream(int var1, ErrorCode var2);

      void settings(boolean var1, Settings var2);

      void windowUpdate(int var1, long var2);
   }
}
