package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

public abstract class RequestBody {
   public static RequestBody create(@Nullable final MediaType var0, final File var1) {
      if (var1 != null) {
         return new RequestBody() {
            public long contentLength() {
               return var1.length();
            }

            @Nullable
            public MediaType contentType() {
               return var0;
            }

            public void writeTo(BufferedSink var1x) throws IOException {
               Source var2 = null;

               Source var3;
               label68: {
                  Throwable var10000;
                  label72: {
                     boolean var10001;
                     try {
                        var3 = Okio.source(var1);
                     } catch (Throwable var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label72;
                     }

                     var2 = var3;

                     label63:
                     try {
                        var1x.writeAll(var3);
                        break label68;
                     } catch (Throwable var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label63;
                     }
                  }

                  Throwable var10 = var10000;
                  Util.closeQuietly((Closeable)var2);
                  throw var10;
               }

               Util.closeQuietly((Closeable)var3);
            }
         };
      } else {
         throw new NullPointerException("content == null");
      }
   }

   public static RequestBody create(@Nullable MediaType var0, String var1) {
      Charset var2 = Util.UTF_8;
      MediaType var3 = var0;
      if (var0 != null) {
         Charset var4 = var0.charset();
         var2 = var4;
         var3 = var0;
         if (var4 == null) {
            var2 = Util.UTF_8;
            StringBuilder var5 = new StringBuilder();
            var5.append(var0);
            var5.append("; charset=utf-8");
            var3 = MediaType.parse(var5.toString());
         }
      }

      return create(var3, var1.getBytes(var2));
   }

   public static RequestBody create(@Nullable final MediaType var0, final ByteString var1) {
      return new RequestBody() {
         public long contentLength() throws IOException {
            return (long)var1.size();
         }

         @Nullable
         public MediaType contentType() {
            return var0;
         }

         public void writeTo(BufferedSink var1x) throws IOException {
            var1x.write(var1);
         }
      };
   }

   public static RequestBody create(@Nullable MediaType var0, byte[] var1) {
      return create(var0, var1, 0, var1.length);
   }

   public static RequestBody create(@Nullable final MediaType var0, final byte[] var1, final int var2, final int var3) {
      if (var1 != null) {
         Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
         return new RequestBody() {
            public long contentLength() {
               return (long)var3;
            }

            @Nullable
            public MediaType contentType() {
               return var0;
            }

            public void writeTo(BufferedSink var1x) throws IOException {
               var1x.write(var1, var2, var3);
            }
         };
      } else {
         throw new NullPointerException("content == null");
      }
   }

   public long contentLength() throws IOException {
      return -1L;
   }

   @Nullable
   public abstract MediaType contentType();

   public abstract void writeTo(BufferedSink var1) throws IOException;
}
