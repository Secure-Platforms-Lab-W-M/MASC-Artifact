package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public final class Okio {
   static final Logger logger = Logger.getLogger(Okio.class.getName());

   private Okio() {
   }

   public static Sink appendingSink(File var0) throws FileNotFoundException {
      if (var0 != null) {
         return sink((OutputStream)(new FileOutputStream(var0, true)));
      } else {
         throw new IllegalArgumentException("file == null");
      }
   }

   public static Sink blackhole() {
      return new Sink() {
         public void close() throws IOException {
         }

         public void flush() throws IOException {
         }

         public Timeout timeout() {
            return Timeout.NONE;
         }

         public void write(Buffer var1, long var2) throws IOException {
            var1.skip(var2);
         }
      };
   }

   public static BufferedSink buffer(Sink var0) {
      return new RealBufferedSink(var0);
   }

   public static BufferedSource buffer(Source var0) {
      return new RealBufferedSource(var0);
   }

   static boolean isAndroidGetsocknameError(AssertionError var0) {
      return var0.getCause() != null && var0.getMessage() != null && var0.getMessage().contains("getsockname failed");
   }

   public static Sink sink(File var0) throws FileNotFoundException {
      if (var0 != null) {
         return sink((OutputStream)(new FileOutputStream(var0)));
      } else {
         throw new IllegalArgumentException("file == null");
      }
   }

   public static Sink sink(OutputStream var0) {
      return sink(var0, new Timeout());
   }

   private static Sink sink(final OutputStream var0, final Timeout var1) {
      if (var0 != null) {
         if (var1 != null) {
            return new Sink() {
               public void close() throws IOException {
                  var0.close();
               }

               public void flush() throws IOException {
                  var0.flush();
               }

               public Timeout timeout() {
                  return var1;
               }

               public String toString() {
                  StringBuilder var1x = new StringBuilder();
                  var1x.append("sink(");
                  var1x.append(var0);
                  var1x.append(")");
                  return var1x.toString();
               }

               public void write(Buffer var1x, long var2) throws IOException {
                  Util.checkOffsetAndCount(var1x.size, 0L, var2);

                  while(var2 > 0L) {
                     var1.throwIfReached();
                     Segment var5 = var1x.head;
                     int var4 = (int)Math.min(var2, (long)(var5.limit - var5.pos));
                     var0.write(var5.data, var5.pos, var4);
                     var5.pos += var4;
                     var2 -= (long)var4;
                     var1x.size -= (long)var4;
                     if (var5.pos == var5.limit) {
                        var1x.head = var5.pop();
                        SegmentPool.recycle(var5);
                     }
                  }

               }
            };
         } else {
            throw new IllegalArgumentException("timeout == null");
         }
      } else {
         throw new IllegalArgumentException("out == null");
      }
   }

   public static Sink sink(Socket var0) throws IOException {
      if (var0 != null) {
         AsyncTimeout var1 = timeout(var0);
         return var1.sink(sink((OutputStream)var0.getOutputStream(), (Timeout)var1));
      } else {
         throw new IllegalArgumentException("socket == null");
      }
   }

   public static Sink sink(Path var0, OpenOption... var1) throws IOException {
      if (var0 != null) {
         return sink(Files.newOutputStream(var0, var1));
      } else {
         throw new IllegalArgumentException("path == null");
      }
   }

   public static Source source(File var0) throws FileNotFoundException {
      if (var0 != null) {
         return source((InputStream)(new FileInputStream(var0)));
      } else {
         throw new IllegalArgumentException("file == null");
      }
   }

   public static Source source(InputStream var0) {
      return source(var0, new Timeout());
   }

   private static Source source(final InputStream var0, final Timeout var1) {
      if (var0 != null) {
         if (var1 != null) {
            return new Source() {
               public void close() throws IOException {
                  var0.close();
               }

               public long read(Buffer var1x, long var2) throws IOException {
                  if (var2 < 0L) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("byteCount < 0: ");
                     var9.append(var2);
                     throw new IllegalArgumentException(var9.toString());
                  } else if (var2 == 0L) {
                     return 0L;
                  } else {
                     AssertionError var10000;
                     label38: {
                        boolean var10001;
                        int var4;
                        Segment var5;
                        try {
                           var1.throwIfReached();
                           var5 = var1x.writableSegment(1);
                           var4 = (int)Math.min(var2, (long)(8192 - var5.limit));
                           var4 = var0.read(var5.data, var5.limit, var4);
                        } catch (AssertionError var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label38;
                        }

                        if (var4 == -1) {
                           return -1L;
                        }

                        try {
                           var5.limit += var4;
                           var1x.size += (long)var4;
                        } catch (AssertionError var6) {
                           var10000 = var6;
                           var10001 = false;
                           break label38;
                        }

                        return (long)var4;
                     }

                     AssertionError var8 = var10000;
                     if (Okio.isAndroidGetsocknameError(var8)) {
                        throw new IOException(var8);
                     } else {
                        throw var8;
                     }
                  }
               }

               public Timeout timeout() {
                  return var1;
               }

               public String toString() {
                  StringBuilder var1x = new StringBuilder();
                  var1x.append("source(");
                  var1x.append(var0);
                  var1x.append(")");
                  return var1x.toString();
               }
            };
         } else {
            throw new IllegalArgumentException("timeout == null");
         }
      } else {
         throw new IllegalArgumentException("in == null");
      }
   }

   public static Source source(Socket var0) throws IOException {
      if (var0 != null) {
         AsyncTimeout var1 = timeout(var0);
         return var1.source(source((InputStream)var0.getInputStream(), (Timeout)var1));
      } else {
         throw new IllegalArgumentException("socket == null");
      }
   }

   public static Source source(Path var0, OpenOption... var1) throws IOException {
      if (var0 != null) {
         return source(Files.newInputStream(var0, var1));
      } else {
         throw new IllegalArgumentException("path == null");
      }
   }

   private static AsyncTimeout timeout(final Socket var0) {
      return new AsyncTimeout() {
         protected IOException newTimeoutException(@Nullable IOException var1) {
            SocketTimeoutException var2 = new SocketTimeoutException("timeout");
            if (var1 != null) {
               var2.initCause(var1);
            }

            return var2;
         }

         protected void timedOut() {
            Logger var2;
            Level var3;
            StringBuilder var4;
            try {
               var0.close();
            } catch (Exception var5) {
               var2 = Okio.logger;
               var3 = Level.WARNING;
               var4 = new StringBuilder();
               var4.append("Failed to close timed out socket ");
               var4.append(var0);
               var2.log(var3, var4.toString(), var5);
            } catch (AssertionError var6) {
               if (Okio.isAndroidGetsocknameError(var6)) {
                  var2 = Okio.logger;
                  var3 = Level.WARNING;
                  var4 = new StringBuilder();
                  var4.append("Failed to close timed out socket ");
                  var4.append(var0);
                  var2.log(var3, var4.toString(), var6);
                  return;
               }

               throw var6;
            }

         }
      };
   }
}
