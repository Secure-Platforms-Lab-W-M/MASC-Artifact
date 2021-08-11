package org.apache.http.entity.mime;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;

public class MultipartEntityBuilder {
   private static final String DEFAULT_SUBTYPE = "form-data";
   private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
   private List bodyParts;
   private String boundary;
   private Charset charset;
   private ContentType contentType;
   private HttpMultipartMode mode;

   MultipartEntityBuilder() {
      this.mode = HttpMultipartMode.STRICT;
      this.boundary = null;
      this.charset = null;
      this.bodyParts = null;
   }

   public static MultipartEntityBuilder create() {
      return new MultipartEntityBuilder();
   }

   private String generateBoundary() {
      StringBuilder var3 = new StringBuilder();
      Random var4 = new Random();
      int var2 = var4.nextInt(11);

      for(int var1 = 0; var1 < var2 + 30; ++var1) {
         char[] var5 = MULTIPART_CHARS;
         var3.append(var5[var4.nextInt(var5.length)]);
      }

      return var3.toString();
   }

   public MultipartEntityBuilder addBinaryBody(String var1, File var2) {
      ContentType var4 = ContentType.DEFAULT_BINARY;
      String var3;
      if (var2 != null) {
         var3 = var2.getName();
      } else {
         var3 = null;
      }

      return this.addBinaryBody(var1, var2, var4, var3);
   }

   public MultipartEntityBuilder addBinaryBody(String var1, File var2, ContentType var3, String var4) {
      return this.addPart(var1, new FileBody(var2, var3, var4));
   }

   public MultipartEntityBuilder addBinaryBody(String var1, InputStream var2) {
      return this.addBinaryBody(var1, (InputStream)var2, ContentType.DEFAULT_BINARY, (String)null);
   }

   public MultipartEntityBuilder addBinaryBody(String var1, InputStream var2, ContentType var3, String var4) {
      return this.addPart(var1, new InputStreamBody(var2, var3, var4));
   }

   public MultipartEntityBuilder addBinaryBody(String var1, byte[] var2) {
      return this.addBinaryBody(var1, (byte[])var2, ContentType.DEFAULT_BINARY, (String)null);
   }

   public MultipartEntityBuilder addBinaryBody(String var1, byte[] var2, ContentType var3, String var4) {
      return this.addPart(var1, new ByteArrayBody(var2, var3, var4));
   }

   public MultipartEntityBuilder addPart(String var1, ContentBody var2) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Content body");
      return this.addPart(FormBodyPartBuilder.create(var1, var2).build());
   }

   public MultipartEntityBuilder addPart(FormBodyPart var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.bodyParts == null) {
            this.bodyParts = new ArrayList();
         }

         this.bodyParts.add(var1);
         return this;
      }
   }

   public MultipartEntityBuilder addTextBody(String var1, String var2) {
      return this.addTextBody(var1, var2, ContentType.DEFAULT_TEXT);
   }

   public MultipartEntityBuilder addTextBody(String var1, String var2, ContentType var3) {
      return this.addPart(var1, new StringBody(var2, var3));
   }

   public HttpEntity build() {
      return this.buildEntity();
   }

   MultipartFormEntity buildEntity() {
      String var3 = this.boundary;
      String var2 = var3;
      ContentType var4;
      if (var3 == null) {
         var4 = this.contentType;
         var2 = var3;
         if (var4 != null) {
            var2 = var4.getParameter("boundary");
         }
      }

      var3 = var2;
      if (var2 == null) {
         var3 = this.generateBoundary();
      }

      Charset var8 = this.charset;
      Charset var7 = var8;
      ContentType var5;
      if (var8 == null) {
         var5 = this.contentType;
         var7 = var8;
         if (var5 != null) {
            var7 = var5.getCharset();
         }
      }

      ArrayList var9 = new ArrayList(2);
      var9.add(new BasicNameValuePair("boundary", var3));
      if (var7 != null) {
         var9.add(new BasicNameValuePair("charset", var7.name()));
      }

      NameValuePair[] var11 = (NameValuePair[])var9.toArray(new NameValuePair[var9.size()]);
      var5 = this.contentType;
      if (var5 != null) {
         var4 = var5.withParameters(var11);
      } else {
         var4 = ContentType.create("multipart/form-data", var11);
      }

      Object var12;
      if (this.bodyParts != null) {
         var12 = new ArrayList(this.bodyParts);
      } else {
         var12 = Collections.emptyList();
      }

      HttpMultipartMode var6 = this.mode;
      if (var6 == null) {
         var6 = HttpMultipartMode.STRICT;
      }

      int var1 = null.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[var6.ordinal()];
      Object var10;
      if (var1 != 1) {
         if (var1 != 2) {
            var10 = new HttpStrictMultipart(var7, var3, (List)var12);
         } else {
            var10 = new HttpRFC6532Multipart(var7, var3, (List)var12);
         }
      } else {
         var10 = new HttpBrowserCompatibleMultipart(var7, var3, (List)var12);
      }

      return new MultipartFormEntity((AbstractMultipartForm)var10, var4, ((AbstractMultipartForm)var10).getTotalLength());
   }

   @Deprecated
   public MultipartEntityBuilder seContentType(ContentType var1) {
      return this.setContentType(var1);
   }

   public MultipartEntityBuilder setBoundary(String var1) {
      this.boundary = var1;
      return this;
   }

   public MultipartEntityBuilder setCharset(Charset var1) {
      this.charset = var1;
      return this;
   }

   public MultipartEntityBuilder setContentType(ContentType var1) {
      Args.notNull(var1, "Content type");
      this.contentType = var1;
      return this;
   }

   public MultipartEntityBuilder setLaxMode() {
      this.mode = HttpMultipartMode.BROWSER_COMPATIBLE;
      return this;
   }

   public MultipartEntityBuilder setMimeSubtype(String var1) {
      Args.notBlank(var1, "MIME subtype");
      StringBuilder var2 = new StringBuilder();
      var2.append("multipart/");
      var2.append(var1);
      this.contentType = ContentType.create(var2.toString());
      return this;
   }

   public MultipartEntityBuilder setMode(HttpMultipartMode var1) {
      this.mode = var1;
      return this;
   }

   public MultipartEntityBuilder setStrictMode() {
      this.mode = HttpMultipartMode.STRICT;
      return this;
   }
}
