package org.apache.http.entity.mime.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class StringBody extends AbstractContentBody {
   private final byte[] content;

   @Deprecated
   public StringBody(String var1) throws UnsupportedEncodingException {
      this(var1, "text/plain", Consts.ASCII);
   }

   @Deprecated
   public StringBody(String var1, String var2, Charset var3) throws UnsupportedEncodingException {
      if (var3 == null) {
         var3 = Consts.ASCII;
      }

      this(var1, ContentType.create(var2, var3));
   }

   @Deprecated
   public StringBody(String var1, Charset var2) throws UnsupportedEncodingException {
      this(var1, "text/plain", var2);
   }

   public StringBody(String var1, ContentType var2) {
      super(var2);
      Args.notNull(var1, "Text");
      Charset var3 = var2.getCharset();
      if (var3 == null) {
         var3 = Consts.ASCII;
      }

      this.content = var1.getBytes(var3);
   }

   @Deprecated
   public static StringBody create(String var0) throws IllegalArgumentException {
      return create(var0, (String)null, (Charset)null);
   }

   @Deprecated
   public static StringBody create(String var0, String var1, Charset var2) throws IllegalArgumentException {
      try {
         StringBody var4 = new StringBody(var0, var1, var2);
         return var4;
      } catch (UnsupportedEncodingException var3) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Charset ");
         var5.append(var2);
         var5.append(" is not supported");
         throw new IllegalArgumentException(var5.toString(), var3);
      }
   }

   @Deprecated
   public static StringBody create(String var0, Charset var1) throws IllegalArgumentException {
      return create(var0, (String)null, var1);
   }

   public long getContentLength() {
      return (long)this.content.length;
   }

   public String getFilename() {
      return null;
   }

   public Reader getReader() {
      Charset var1 = this.getContentType().getCharset();
      ByteArrayInputStream var2 = new ByteArrayInputStream(this.content);
      if (var1 == null) {
         var1 = Consts.ASCII;
      }

      return new InputStreamReader(var2, var1);
   }

   public String getTransferEncoding() {
      return "8bit";
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      ByteArrayInputStream var3 = new ByteArrayInputStream(this.content);
      byte[] var4 = new byte[4096];

      while(true) {
         int var2 = var3.read(var4);
         if (var2 == -1) {
            var1.flush();
            return;
         }

         var1.write(var4, 0, var2);
      }
   }
}
