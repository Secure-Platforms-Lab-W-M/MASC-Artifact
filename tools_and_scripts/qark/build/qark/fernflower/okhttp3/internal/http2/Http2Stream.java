package okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http2Stream {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   long bytesLeftInWriteWindow;
   final Http2Connection connection;
   ErrorCode errorCode = null;
   private boolean hasResponseHeaders;
   // $FF: renamed from: id int
   final int field_119;
   final Http2Stream.StreamTimeout readTimeout = new Http2Stream.StreamTimeout();
   private final List requestHeaders;
   private List responseHeaders;
   final Http2Stream.FramingSink sink;
   private final Http2Stream.FramingSource source;
   long unacknowledgedBytesRead = 0L;
   final Http2Stream.StreamTimeout writeTimeout = new Http2Stream.StreamTimeout();

   Http2Stream(int var1, Http2Connection var2, boolean var3, boolean var4, List var5) {
      if (var2 != null) {
         if (var5 != null) {
            this.field_119 = var1;
            this.connection = var2;
            this.bytesLeftInWriteWindow = (long)var2.peerSettings.getInitialWindowSize();
            this.source = new Http2Stream.FramingSource((long)var2.okHttpSettings.getInitialWindowSize());
            this.sink = new Http2Stream.FramingSink();
            this.source.finished = var4;
            this.sink.finished = var3;
            this.requestHeaders = var5;
         } else {
            throw new NullPointerException("requestHeaders == null");
         }
      } else {
         throw new NullPointerException("connection == null");
      }
   }

   private boolean closeInternal(ErrorCode var1) {
      synchronized(this){}

      label221: {
         Throwable var10000;
         boolean var10001;
         label222: {
            try {
               if (this.errorCode != null) {
                  return false;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label222;
            }

            try {
               if (this.source.finished && this.sink.finished) {
                  return false;
               }
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label222;
            }

            label210:
            try {
               this.errorCode = var1;
               this.notifyAll();
               break label221;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label210;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      }

      this.connection.removeStream(this.field_119);
      return true;
   }

   void addBytesToWriteWindow(long var1) {
      this.bytesLeftInWriteWindow += var1;
      if (var1 > 0L) {
         this.notifyAll();
      }

   }

   void cancelStreamIfNecessary() throws IOException {
      synchronized(this){}

      boolean var1;
      boolean var2;
      label217: {
         Throwable var10000;
         boolean var10001;
         label212: {
            label211: {
               label210: {
                  try {
                     if (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed)) {
                        break label210;
                     }
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label212;
                  }

                  var1 = false;
                  break label211;
               }

               var1 = true;
            }

            label198:
            try {
               var2 = this.isOpen();
               break label217;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label198;
            }
         }

         while(true) {
            Throwable var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      if (var1) {
         this.close(ErrorCode.CANCEL);
      } else {
         if (!var2) {
            this.connection.removeStream(this.field_119);
         }

      }
   }

   void checkOutNotClosed() throws IOException {
      if (!this.sink.closed) {
         if (!this.sink.finished) {
            if (this.errorCode != null) {
               throw new StreamResetException(this.errorCode);
            }
         } else {
            throw new IOException("stream finished");
         }
      } else {
         throw new IOException("stream closed");
      }
   }

   public void close(ErrorCode var1) throws IOException {
      if (this.closeInternal(var1)) {
         this.connection.writeSynReset(this.field_119, var1);
      }
   }

   public void closeLater(ErrorCode var1) {
      if (this.closeInternal(var1)) {
         this.connection.writeSynResetLater(this.field_119, var1);
      }
   }

   public Http2Connection getConnection() {
      return this.connection;
   }

   public ErrorCode getErrorCode() {
      synchronized(this){}

      ErrorCode var1;
      try {
         var1 = this.errorCode;
      } finally {
         ;
      }

      return var1;
   }

   public int getId() {
      return this.field_119;
   }

   public List getRequestHeaders() {
      return this.requestHeaders;
   }

   public Sink getSink() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label211: {
         label216: {
            try {
               if (!this.hasResponseHeaders && !this.isLocallyInitiated()) {
                  break label216;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label211;
            }

            try {
               ;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label211;
            }

            return this.sink;
         }

         label198:
         try {
            throw new IllegalStateException("reply before requesting the sink");
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label198;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            continue;
         }
      }
   }

   public Source getSource() {
      return this.source;
   }

   public boolean isLocallyInitiated() {
      boolean var1;
      if ((this.field_119 & 1) == 1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return this.connection.client == var1;
   }

   public boolean isOpen() {
      synchronized(this){}

      boolean var1;
      label240: {
         Throwable var10000;
         label245: {
            boolean var10001;
            ErrorCode var2;
            try {
               var2 = this.errorCode;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label245;
            }

            if (var2 != null) {
               return false;
            }

            try {
               if (!this.source.finished && !this.source.closed) {
                  return true;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label245;
            }

            try {
               if (!this.sink.finished && !this.sink.closed) {
                  return true;
               }
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label245;
            }

            label229:
            try {
               var1 = this.hasResponseHeaders;
               break label240;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label229;
            }
         }

         Throwable var23 = var10000;
         throw var23;
      }

      if (var1) {
         return false;
      } else {
         return true;
      }
   }

   public Timeout readTimeout() {
      return this.readTimeout;
   }

   void receiveData(BufferedSource var1, int var2) throws IOException {
      this.source.receive(var1, (long)var2);
   }

   void receiveFin() {
      // $FF: Couldn't be decompiled
   }

   void receiveHeaders(List var1) {
      boolean var2 = true;
      synchronized(this){}

      label229: {
         Throwable var10000;
         boolean var10001;
         label230: {
            label222: {
               try {
                  this.hasResponseHeaders = true;
                  if (this.responseHeaders == null) {
                     this.responseHeaders = var1;
                     var2 = this.isOpen();
                     this.notifyAll();
                     break label222;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label230;
               }

               try {
                  ArrayList var3 = new ArrayList();
                  var3.addAll(this.responseHeaders);
                  var3.add((Object)null);
                  var3.addAll(var1);
                  this.responseHeaders = var3;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label230;
               }
            }

            label213:
            try {
               break label229;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label213;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }

      if (!var2) {
         this.connection.removeStream(this.field_119);
      }

   }

   void receiveRstStream(ErrorCode var1) {
      synchronized(this){}

      try {
         if (this.errorCode == null) {
            this.errorCode = var1;
            this.notifyAll();
         }
      } finally {
         ;
      }

   }

   public void sendResponseHeaders(List var1, boolean var2) throws IOException {
      if (var1 != null) {
         boolean var3 = false;
         synchronized(this){}

         label239: {
            Throwable var10000;
            boolean var10001;
            label240: {
               try {
                  this.hasResponseHeaders = true;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label240;
               }

               if (!var2) {
                  try {
                     this.sink.finished = true;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label240;
                  }

                  var3 = true;
               }

               label224:
               try {
                  break label239;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label224;
               }
            }

            while(true) {
               Throwable var24 = var10000;

               try {
                  throw var24;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }

         this.connection.writeSynReply(this.field_119, var3, var1);
         if (var3) {
            this.connection.flush();
         }

      } else {
         throw new NullPointerException("responseHeaders == null");
      }
   }

   public List takeResponseHeaders() throws IOException {
      // $FF: Couldn't be decompiled
   }

   void waitForIo() throws InterruptedIOException {
      try {
         this.wait();
      } catch (InterruptedException var2) {
         throw new InterruptedIOException();
      }
   }

   public Timeout writeTimeout() {
      return this.writeTimeout;
   }

   final class FramingSink implements Sink {
      // $FF: synthetic field
      static final boolean $assertionsDisabled = false;
      private static final long EMIT_BUFFER_SIZE = 16384L;
      boolean closed;
      boolean finished;
      private final Buffer sendBuffer = new Buffer();

      private void emitFrame(boolean var1) throws IOException {
         Http2Stream var5 = Http2Stream.this;
         synchronized(var5){}

         long var3;
         Throwable var10000;
         boolean var10001;
         label785: {
            label780: {
               try {
                  Http2Stream.this.writeTimeout.enter();
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break label780;
               }

               while(true) {
                  boolean var63 = false;

                  try {
                     var63 = true;
                     if (Http2Stream.this.bytesLeftInWriteWindow <= 0L) {
                        if (!this.finished) {
                           if (!this.closed) {
                              if (Http2Stream.this.errorCode == null) {
                                 Http2Stream.this.waitForIo();
                                 var63 = false;
                                 continue;
                              }

                              var63 = false;
                           } else {
                              var63 = false;
                           }
                        } else {
                           var63 = false;
                        }
                     } else {
                        var63 = false;
                     }
                  } finally {
                     if (var63) {
                        try {
                           Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                        } catch (Throwable var76) {
                           var10000 = var76;
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  try {
                     Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                     Http2Stream.this.checkOutNotClosed();
                     var3 = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
                     Http2Stream var82 = Http2Stream.this;
                     var82.bytesLeftInWriteWindow -= var3;
                     break label785;
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break;
                  }
               }
            }

            while(true) {
               Throwable var6 = var10000;

               try {
                  throw var6;
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  continue;
               }
            }
         }

         Http2Stream.this.writeTimeout.enter();

         label759: {
            label787: {
               int var2;
               Http2Connection var80;
               try {
                  var80 = Http2Stream.this.connection;
                  var2 = Http2Stream.this.field_119;
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label787;
               }

               label790: {
                  if (var1) {
                     label789: {
                        try {
                           if (var3 != this.sendBuffer.size()) {
                              break label789;
                           }
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label787;
                        }

                        var1 = true;
                        break label790;
                     }
                  }

                  var1 = false;
               }

               label745:
               try {
                  var80.writeData(var2, var1, this.sendBuffer, var3);
                  break label759;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label745;
               }
            }

            Throwable var81 = var10000;
            Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
            throw var81;
         }

         Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
      }

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void flush() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public Timeout timeout() {
         return Http2Stream.this.writeTimeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         this.sendBuffer.write(var1, var2);

         while(this.sendBuffer.size() >= 16384L) {
            this.emitFrame(false);
         }

      }
   }

   private final class FramingSource implements Source {
      // $FF: synthetic field
      static final boolean $assertionsDisabled = false;
      boolean closed;
      boolean finished;
      private final long maxByteCount;
      private final Buffer readBuffer = new Buffer();
      private final Buffer receiveBuffer = new Buffer();

      FramingSource(long var2) {
         this.maxByteCount = var2;
      }

      private void checkNotClosed() throws IOException {
         if (!this.closed) {
            if (Http2Stream.this.errorCode != null) {
               throw new StreamResetException(Http2Stream.this.errorCode);
            }
         } else {
            throw new IOException("stream closed");
         }
      }

      private void waitUntilReadable() throws IOException {
         Http2Stream.this.readTimeout.enter();

         while(true) {
            boolean var3 = false;

            try {
               var3 = true;
               if (this.readBuffer.size() == 0L) {
                  if (!this.finished) {
                     if (!this.closed) {
                        if (Http2Stream.this.errorCode == null) {
                           Http2Stream.this.waitForIo();
                           var3 = false;
                           continue;
                        }

                        var3 = false;
                        break;
                     }

                     var3 = false;
                     break;
                  }

                  var3 = false;
                  break;
               }

               var3 = false;
               break;
            } finally {
               if (var3) {
                  Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
               }
            }
         }

         Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
      }

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 >= 0L) {
            Http2Stream var4 = Http2Stream.this;
            synchronized(var4){}

            Throwable var10000;
            boolean var10001;
            label569: {
               label570: {
                  try {
                     this.waitUntilReadable();
                     this.checkNotClosed();
                     if (this.readBuffer.size() == 0L) {
                        return -1L;
                     }
                  } catch (Throwable var60) {
                     var10000 = var60;
                     var10001 = false;
                     break label570;
                  }

                  try {
                     var2 = this.readBuffer.read(var1, Math.min(var2, this.readBuffer.size()));
                     Http2Stream var62 = Http2Stream.this;
                     var62.unacknowledgedBytesRead += var2;
                     if (Http2Stream.this.unacknowledgedBytesRead >= (long)(Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
                        Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.field_119, Http2Stream.this.unacknowledgedBytesRead);
                        Http2Stream.this.unacknowledgedBytesRead = 0L;
                     }
                  } catch (Throwable var59) {
                     var10000 = var59;
                     var10001 = false;
                     break label570;
                  }

                  label556:
                  try {
                     break label569;
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label556;
                  }
               }

               while(true) {
                  Throwable var63 = var10000;

                  try {
                     throw var63;
                  } catch (Throwable var54) {
                     var10000 = var54;
                     var10001 = false;
                     continue;
                  }
               }
            }

            Http2Connection var64 = Http2Stream.this.connection;
            synchronized(var64){}

            label548: {
               try {
                  Http2Connection var65 = Http2Stream.this.connection;
                  var65.unacknowledgedBytesRead += var2;
                  if (Http2Stream.this.connection.unacknowledgedBytesRead >= (long)(Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
                     Http2Stream.this.connection.writeWindowUpdateLater(0, Http2Stream.this.connection.unacknowledgedBytesRead);
                     Http2Stream.this.connection.unacknowledgedBytesRead = 0L;
                  }
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label548;
               }

               label545:
               try {
                  return var2;
               } catch (Throwable var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label545;
               }
            }

            while(true) {
               Throwable var66 = var10000;

               try {
                  throw var66;
               } catch (Throwable var55) {
                  var10000 = var55;
                  var10001 = false;
                  continue;
               }
            }
         } else {
            StringBuilder var61 = new StringBuilder();
            var61.append("byteCount < 0: ");
            var61.append(var2);
            throw new IllegalArgumentException(var61.toString());
         }
      }

      void receive(BufferedSource var1, long var2) throws IOException {
         while(true) {
            if (var2 > 0L) {
               Http2Stream var11 = Http2Stream.this;
               synchronized(var11){}

               boolean var4;
               boolean var5;
               long var6;
               boolean var10;
               Throwable var10000;
               boolean var10001;
               Throwable var84;
               label878: {
                  label879: {
                     long var8;
                     try {
                        var10 = this.finished;
                        var6 = this.readBuffer.size();
                        var8 = this.maxByteCount;
                     } catch (Throwable var83) {
                        var10000 = var83;
                        var10001 = false;
                        break label879;
                     }

                     var5 = true;
                     if (var6 + var2 > var8) {
                        var4 = true;
                     } else {
                        var4 = false;
                     }

                     label866:
                     try {
                        break label878;
                     } catch (Throwable var82) {
                        var10000 = var82;
                        var10001 = false;
                        break label866;
                     }
                  }

                  while(true) {
                     var84 = var10000;

                     try {
                        throw var84;
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        continue;
                     }
                  }
               }

               if (var4) {
                  var1.skip(var2);
                  Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                  return;
               }

               if (var10) {
                  var1.skip(var2);
                  return;
               }

               var6 = var1.read(this.receiveBuffer, var2);
               if (var6 != -1L) {
                  var11 = Http2Stream.this;
                  synchronized(var11){}

                  label881: {
                     label882: {
                        label854: {
                           label853: {
                              try {
                                 if (this.readBuffer.size() != 0L) {
                                    break label853;
                                 }
                              } catch (Throwable var81) {
                                 var10000 = var81;
                                 var10001 = false;
                                 break label882;
                              }

                              var4 = var5;
                              break label854;
                           }

                           var4 = false;
                        }

                        try {
                           this.readBuffer.writeAll(this.receiveBuffer);
                        } catch (Throwable var80) {
                           var10000 = var80;
                           var10001 = false;
                           break label882;
                        }

                        if (var4) {
                           try {
                              Http2Stream.this.notifyAll();
                           } catch (Throwable var79) {
                              var10000 = var79;
                              var10001 = false;
                              break label882;
                           }
                        }

                        label840:
                        try {
                           break label881;
                        } catch (Throwable var78) {
                           var10000 = var78;
                           var10001 = false;
                           break label840;
                        }
                     }

                     while(true) {
                        var84 = var10000;

                        try {
                           throw var84;
                        } catch (Throwable var77) {
                           var10000 = var77;
                           var10001 = false;
                           continue;
                        }
                     }
                  }

                  var2 -= var6;
                  continue;
               }

               throw new EOFException();
            }

            return;
         }
      }

      public Timeout timeout() {
         return Http2Stream.this.readTimeout;
      }
   }

   class StreamTimeout extends AsyncTimeout {
      public void exitAndThrowIfTimedOut() throws IOException {
         if (this.exit()) {
            throw this.newTimeoutException((IOException)null);
         }
      }

      protected IOException newTimeoutException(IOException var1) {
         SocketTimeoutException var2 = new SocketTimeoutException("timeout");
         if (var1 != null) {
            var2.initCause(var1);
         }

         return var2;
      }

      protected void timedOut() {
         Http2Stream.this.closeLater(ErrorCode.CANCEL);
      }
   }
}
