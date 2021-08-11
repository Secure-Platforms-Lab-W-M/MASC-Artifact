package okio;

import java.io.IOException;

public final class Pipe {
   final Buffer buffer = new Buffer();
   final long maxBufferSize;
   private final Sink sink = new Pipe.PipeSink();
   boolean sinkClosed;
   private final Source source = new Pipe.PipeSource();
   boolean sourceClosed;

   public Pipe(long var1) {
      if (var1 >= 1L) {
         this.maxBufferSize = var1;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("maxBufferSize < 1: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public Sink sink() {
      return this.sink;
   }

   public Source source() {
      return this.source;
   }

   final class PipeSink implements Sink {
      final Timeout timeout = new Timeout();

      public void close() throws IOException {
         Buffer var1 = Pipe.this.buffer;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label290: {
            try {
               if (Pipe.this.sinkClosed) {
                  return;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label290;
            }

            label291: {
               label280:
               try {
                  if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
                     break label280;
                  }
                  break label291;
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label290;
               }

               try {
                  throw new IOException("source is closed");
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label290;
               }
            }

            label273:
            try {
               Pipe.this.sinkClosed = true;
               Pipe.this.buffer.notifyAll();
               return;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label273;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }

      public void flush() throws IOException {
         Buffer var1 = Pipe.this.buffer;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label297: {
            label296: {
               label302: {
                  try {
                     if (!Pipe.this.sinkClosed) {
                        if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
                           break label296;
                        }
                        break label302;
                     }
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label297;
                  }

                  try {
                     throw new IllegalStateException("closed");
                  } catch (Throwable var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label297;
                  }
               }

               try {
                  return;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label297;
               }
            }

            label279:
            try {
               throw new IOException("source is closed");
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label279;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         Buffer var6 = Pipe.this.buffer;
         synchronized(var6){}

         Throwable var10000;
         boolean var10001;
         label696: {
            label703: {
               try {
                  if (Pipe.this.sinkClosed) {
                     break label703;
                  }
               } catch (Throwable var96) {
                  var10000 = var96;
                  var10001 = false;
                  break label696;
               }

               while(var2 > 0L) {
                  long var4;
                  label688: {
                     try {
                        if (!Pipe.this.sourceClosed) {
                           var4 = Pipe.this.maxBufferSize - Pipe.this.buffer.size();
                           break label688;
                        }
                     } catch (Throwable var95) {
                        var10000 = var95;
                        var10001 = false;
                        break label696;
                     }

                     try {
                        throw new IOException("source is closed");
                     } catch (Throwable var89) {
                        var10000 = var89;
                        var10001 = false;
                        break label696;
                     }
                  }

                  if (var4 == 0L) {
                     try {
                        this.timeout.waitUntilNotified(Pipe.this.buffer);
                     } catch (Throwable var94) {
                        var10000 = var94;
                        var10001 = false;
                        break label696;
                     }
                  } else {
                     try {
                        var4 = Math.min(var4, var2);
                        Pipe.this.buffer.write(var1, var4);
                     } catch (Throwable var93) {
                        var10000 = var93;
                        var10001 = false;
                        break label696;
                     }

                     var2 -= var4;

                     try {
                        Pipe.this.buffer.notifyAll();
                     } catch (Throwable var92) {
                        var10000 = var92;
                        var10001 = false;
                        break label696;
                     }
                  }
               }

               try {
                  return;
               } catch (Throwable var90) {
                  var10000 = var90;
                  var10001 = false;
                  break label696;
               }
            }

            label672:
            try {
               throw new IllegalStateException("closed");
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label672;
            }
         }

         while(true) {
            Throwable var97 = var10000;

            try {
               throw var97;
            } catch (Throwable var88) {
               var10000 = var88;
               var10001 = false;
               continue;
            }
         }
      }
   }

   final class PipeSource implements Source {
      final Timeout timeout = new Timeout();

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public long read(Buffer var1, long var2) throws IOException {
         Buffer var4 = Pipe.this.buffer;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label399: {
            label394: {
               try {
                  if (!Pipe.this.sourceClosed) {
                     break label394;
                  }
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label399;
               }

               try {
                  throw new IllegalStateException("closed");
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label399;
               }
            }

            while(true) {
               try {
                  if (Pipe.this.buffer.size() != 0L) {
                     break;
                  }

                  if (Pipe.this.sinkClosed) {
                     return -1L;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label399;
               }

               try {
                  this.timeout.waitUntilNotified(Pipe.this.buffer);
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label399;
               }
            }

            label374:
            try {
               var2 = Pipe.this.buffer.read(var1, var2);
               Pipe.this.buffer.notifyAll();
               return var2;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label374;
            }
         }

         while(true) {
            Throwable var47 = var10000;

            try {
               throw var47;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               continue;
            }
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }
   }
}
