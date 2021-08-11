package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

public class StringEntity extends AbstractHttpEntity implements Cloneable {
   protected final byte[] content;

   public StringEntity(String var1) throws UnsupportedEncodingException {
      this(var1, ContentType.DEFAULT_TEXT);
   }

   public StringEntity(String var1, String var2) throws UnsupportedCharsetException {
      this(var1, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), var2));
   }

   @Deprecated
   public StringEntity(String var1, String var2, String var3) throws UnsupportedEncodingException {
      Args.notNull(var1, "Source string");
      if (var2 == null) {
         var2 = "text/plain";
      }

      if (var3 == null) {
         var3 = "ISO-8859-1";
      }

      this.content = var1.getBytes(var3);
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append("; charset=");
      var4.append(var3);
      this.setContentType(var4.toString());
   }

   public StringEntity(String var1, Charset var2) {
      this(var1, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), var2));
   }

   public StringEntity(String var1, ContentType var2) throws UnsupportedCharsetException {
      Args.notNull(var1, "Source string");
      Charset var3;
      if (var2 != null) {
         var3 = var2.getCharset();
      } else {
         var3 = null;
      }

      Charset var4 = var3;
      if (var3 == null) {
         var4 = HTTP.DEF_CONTENT_CHARSET;
      }

      this.content = var1.getBytes(var4);
      if (var2 != null) {
         this.setContentType(var2.toString());
      }

   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public InputStream getContent() throws IOException {
      return new ByteArrayInputStream(this.content);
   }

   public long getContentLength() {
      return (long)this.content.length;
   }

   public boolean isRepeatable() {
      return true;
   }

   public boolean isStreaming() {
      return false;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      var1.write(this.content);
      var1.flush();
   }
}
