package okhttp3.internal.ws;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

final class WebSocketReader {
   boolean closed;
   long frameBytesRead;
   final WebSocketReader.FrameCallback frameCallback;
   long frameLength;
   final boolean isClient;
   boolean isControlFrame;
   boolean isFinalFrame;
   boolean isMasked;
   final byte[] maskBuffer = new byte[8192];
   final byte[] maskKey = new byte[4];
   int opcode;
   final BufferedSource source;

   WebSocketReader(boolean var1, BufferedSource var2, WebSocketReader.FrameCallback var3) {
      if (var2 != null) {
         if (var3 != null) {
            this.isClient = var1;
            this.source = var2;
            this.frameCallback = var3;
         } else {
            throw new NullPointerException("frameCallback == null");
         }
      } else {
         throw new NullPointerException("source == null");
      }
   }

   private void readControlFrame() throws IOException {
      Buffer var7 = new Buffer();
      long var2 = this.frameBytesRead;
      long var4 = this.frameLength;
      if (var2 < var4) {
         if (this.isClient) {
            this.source.readFully(var7, var4);
         } else {
            while(true) {
               var2 = this.frameBytesRead;
               var4 = this.frameLength;
               if (var2 >= var4) {
                  break;
               }

               int var1 = (int)Math.min(var4 - var2, (long)this.maskBuffer.length);
               var1 = this.source.read(this.maskBuffer, 0, var1);
               if (var1 == -1) {
                  throw new EOFException();
               }

               WebSocketProtocol.toggleMask(this.maskBuffer, (long)var1, this.maskKey, this.frameBytesRead);
               var7.write(this.maskBuffer, 0, var1);
               this.frameBytesRead += (long)var1;
            }
         }
      }

      switch(this.opcode) {
      case 8:
         short var8 = 1005;
         String var6 = "";
         var2 = var7.size();
         if (var2 != 1L) {
            if (var2 != 0L) {
               var8 = var7.readShort();
               var6 = var7.readUtf8();
               String var10 = WebSocketProtocol.closeCodeExceptionMessage(var8);
               if (var10 != null) {
                  throw new ProtocolException(var10);
               }
            }

            this.frameCallback.onReadClose(var8, var6);
            this.closed = true;
            return;
         }

         throw new ProtocolException("Malformed close payload length of 1.");
      case 9:
         this.frameCallback.onReadPing(var7.readByteString());
         return;
      case 10:
         this.frameCallback.onReadPong(var7.readByteString());
         return;
      default:
         StringBuilder var9 = new StringBuilder();
         var9.append("Unknown control opcode: ");
         var9.append(Integer.toHexString(this.opcode));
         throw new ProtocolException(var9.toString());
      }
   }

   private void readHeader() throws IOException {
      if (!this.closed) {
         long var4 = this.source.timeout().timeoutNanos();
         this.source.timeout().clearTimeout();
         boolean var10 = false;

         byte var1;
         try {
            var10 = true;
            var1 = this.source.readByte();
            var10 = false;
         } finally {
            if (var10) {
               this.source.timeout().timeout(var4, TimeUnit.NANOSECONDS);
            }
         }

         int var3 = var1 & 255;
         this.source.timeout().timeout(var4, TimeUnit.NANOSECONDS);
         this.opcode = var3 & 15;
         boolean var7 = true;
         boolean var6;
         if ((var3 & 128) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.isFinalFrame = var6;
         if ((var3 & 8) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.isControlFrame = var6;
         if (var6 && !this.isFinalFrame) {
            throw new ProtocolException("Control frames must be final.");
         } else {
            boolean var12;
            if ((var3 & 64) != 0) {
               var12 = true;
            } else {
               var12 = false;
            }

            boolean var2;
            if ((var3 & 32) != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            boolean var14;
            if ((var3 & 16) != 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            if (!var12 && !var2 && !var14) {
               int var13 = this.source.readByte() & 255;
               if ((var13 & 128) != 0) {
                  var6 = var7;
               } else {
                  var6 = false;
               }

               this.isMasked = var6;
               if (var6 == this.isClient) {
                  String var15;
                  if (this.isClient) {
                     var15 = "Server-sent frames must not be masked.";
                  } else {
                     var15 = "Client-sent frames must be masked.";
                  }

                  throw new ProtocolException(var15);
               } else {
                  var4 = (long)(var13 & 127);
                  this.frameLength = var4;
                  if (var4 == 126L) {
                     this.frameLength = (long)this.source.readShort() & 65535L;
                  } else if (var4 == 127L) {
                     var4 = this.source.readLong();
                     this.frameLength = var4;
                     if (var4 < 0L) {
                        StringBuilder var8 = new StringBuilder();
                        var8.append("Frame length 0x");
                        var8.append(Long.toHexString(this.frameLength));
                        var8.append(" > 0x7FFFFFFFFFFFFFFF");
                        throw new ProtocolException(var8.toString());
                     }
                  }

                  this.frameBytesRead = 0L;
                  if (this.isControlFrame && this.frameLength > 125L) {
                     throw new ProtocolException("Control frame must be less than 125B.");
                  } else {
                     if (this.isMasked) {
                        this.source.readFully(this.maskKey);
                     }

                  }
               }
            } else {
               throw new ProtocolException("Reserved flags are unsupported.");
            }
         }
      } else {
         throw new IOException("closed");
      }
   }

   private void readMessage(Buffer var1) throws IOException {
      long var2;
      for(; !this.closed; this.frameBytesRead += var2) {
         if (this.frameBytesRead == this.frameLength) {
            if (this.isFinalFrame) {
               return;
            }

            this.readUntilNonControlFrame();
            if (this.opcode != 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append("Expected continuation opcode. Got: ");
               var4.append(Integer.toHexString(this.opcode));
               throw new ProtocolException(var4.toString());
            }

            if (this.isFinalFrame && this.frameLength == 0L) {
               return;
            }
         }

         var2 = this.frameLength - this.frameBytesRead;
         if (this.isMasked) {
            var2 = Math.min(var2, (long)this.maskBuffer.length);
            var2 = (long)this.source.read(this.maskBuffer, 0, (int)var2);
            if (var2 == -1L) {
               throw new EOFException();
            }

            WebSocketProtocol.toggleMask(this.maskBuffer, var2, this.maskKey, this.frameBytesRead);
            var1.write(this.maskBuffer, 0, (int)var2);
         } else {
            var2 = this.source.read(var1, var2);
            if (var2 == -1L) {
               throw new EOFException();
            }
         }
      }

      throw new IOException("closed");
   }

   private void readMessageFrame() throws IOException {
      int var1 = this.opcode;
      if (var1 != 1 && var1 != 2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unknown opcode: ");
         var3.append(Integer.toHexString(var1));
         throw new ProtocolException(var3.toString());
      } else {
         Buffer var2 = new Buffer();
         this.readMessage(var2);
         if (var1 == 1) {
            this.frameCallback.onReadMessage(var2.readUtf8());
         } else {
            this.frameCallback.onReadMessage(var2.readByteString());
         }
      }
   }

   void processNextFrame() throws IOException {
      this.readHeader();
      if (this.isControlFrame) {
         this.readControlFrame();
      } else {
         this.readMessageFrame();
      }
   }

   void readUntilNonControlFrame() throws IOException {
      while(!this.closed) {
         this.readHeader();
         if (!this.isControlFrame) {
            return;
         }

         this.readControlFrame();
      }

   }

   public interface FrameCallback {
      void onReadClose(int var1, String var2);

      void onReadMessage(String var1) throws IOException;

      void onReadMessage(ByteString var1) throws IOException;

      void onReadPing(ByteString var1);

      void onReadPong(ByteString var1);
   }
}
