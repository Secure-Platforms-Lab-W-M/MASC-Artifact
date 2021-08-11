package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public abstract class ResponseBody implements Closeable {
   private Reader reader;

   private Charset charset() {
      MediaType var3 = this.contentType();
      Charset var2 = Util.UTF_8;
      Charset var1 = var2;
      if (var3 != null) {
         var1 = var3.charset(var2);
      }

      return var1;
   }

   public static ResponseBody create(@Nullable final MediaType var0, final long var1, final BufferedSource var3) {
      if (var3 != null) {
         return new ResponseBody() {
            public long contentLength() {
               return var1;
            }

            @Nullable
            public MediaType contentType() {
               return var0;
            }

            public BufferedSource source() {
               return var3;
            }
         };
      } else {
         throw new NullPointerException("source == null");
      }
   }

   public static ResponseBody create(@Nullable MediaType var0, String var1) {
      Charset var2 = Util.UTF_8;
      MediaType var3 = var0;
      if (var0 != null) {
         Charset var4 = var0.charset();
         var2 = var4;
         var3 = var0;
         if (var4 == null) {
            var2 = Util.UTF_8;
            StringBuilder var6 = new StringBuilder();
            var6.append(var0);
            var6.append("; charset=utf-8");
            var3 = MediaType.parse(var6.toString());
         }
      }

      Buffer var5 = (new Buffer()).writeString(var1, var2);
      return create(var3, var5.size(), var5);
   }

   public static ResponseBody create(@Nullable MediaType var0, byte[] var1) {
      Buffer var2 = (new Buffer()).write(var1);
      return create(var0, (long)var1.length, var2);
   }

   public final InputStream byteStream() {
      return this.source().inputStream();
   }

   public final byte[] bytes() throws IOException {
      long var1 = this.contentLength();
      StringBuilder var3;
      if (var1 <= 2147483647L) {
         BufferedSource var7 = this.source();

         byte[] var4;
         try {
            var4 = var7.readByteArray();
         } finally {
            Util.closeQuietly((Closeable)var7);
         }

         if (var1 != -1L) {
            if (var1 == (long)var4.length) {
               return var4;
            } else {
               var3 = new StringBuilder();
               var3.append("Content-Length (");
               var3.append(var1);
               var3.append(") and stream length (");
               var3.append(var4.length);
               var3.append(") disagree");
               throw new IOException(var3.toString());
            }
         } else {
            return var4;
         }
      } else {
         var3 = new StringBuilder();
         var3.append("Cannot buffer entire body for content length: ");
         var3.append(var1);
         throw new IOException(var3.toString());
      }
   }

   public final Reader charStream() {
      Reader var1 = this.reader;
      if (var1 != null) {
         return var1;
      } else {
         ResponseBody.BomAwareReader var2 = new ResponseBody.BomAwareReader(this.source(), this.charset());
         this.reader = var2;
         return var2;
      }
   }

   public void close() {
      Util.closeQuietly((Closeable)this.source());
   }

   public abstract long contentLength();

   @Nullable
   public abstract MediaType contentType();

   public abstract BufferedSource source();

   public final String string() throws IOException {
      BufferedSource var1 = this.source();

      String var2;
      try {
         var2 = var1.readString(Util.bomAwareCharset(var1, this.charset()));
      } finally {
         Util.closeQuietly((Closeable)var1);
      }

      return var2;
   }

   static final class BomAwareReader extends Reader {
      private final Charset charset;
      private boolean closed;
      private Reader delegate;
      private final BufferedSource source;

      BomAwareReader(BufferedSource var1, Charset var2) {
         this.source = var1;
         this.charset = var2;
      }

      public void close() throws IOException {
         this.closed = true;
         Reader var1 = this.delegate;
         if (var1 != null) {
            var1.close();
         } else {
            this.source.close();
         }
      }

      public int read(char[] var1, int var2, int var3) throws IOException {
         if (!this.closed) {
            Reader var5 = this.delegate;
            Object var4 = var5;
            if (var5 == null) {
               Charset var6 = Util.bomAwareCharset(this.source, this.charset);
               var4 = new InputStreamReader(this.source.inputStream(), var6);
               this.delegate = (Reader)var4;
            }

            return ((Reader)var4).read(var1, var2, var3);
         } else {
            throw new IOException("Stream closed");
         }
      }
   }
}
