package org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

public class EntityBuilder {
   private byte[] binary;
   private boolean chunked;
   private String contentEncoding;
   private ContentType contentType;
   private File file;
   private boolean gzipCompress;
   private List parameters;
   private Serializable serializable;
   private InputStream stream;
   private String text;

   EntityBuilder() {
   }

   private void clearContent() {
      this.text = null;
      this.binary = null;
      this.stream = null;
      this.parameters = null;
      this.serializable = null;
      this.file = null;
   }

   public static EntityBuilder create() {
      return new EntityBuilder();
   }

   private ContentType getContentOrDefault(ContentType var1) {
      ContentType var2 = this.contentType;
      return var2 != null ? var2 : var1;
   }

   public HttpEntity build() {
      String var1 = this.text;
      Object var4;
      if (var1 != null) {
         var4 = new StringEntity(var1, this.getContentOrDefault(ContentType.DEFAULT_TEXT));
      } else {
         byte[] var5 = this.binary;
         if (var5 != null) {
            var4 = new ByteArrayEntity(var5, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
         } else {
            InputStream var6 = this.stream;
            if (var6 != null) {
               var4 = new InputStreamEntity(var6, -1L, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
            } else {
               List var2 = this.parameters;
               if (var2 != null) {
                  ContentType var8 = this.contentType;
                  Charset var9;
                  if (var8 != null) {
                     var9 = var8.getCharset();
                  } else {
                     var9 = null;
                  }

                  var4 = new UrlEncodedFormEntity(var2, var9);
               } else {
                  Serializable var10 = this.serializable;
                  if (var10 != null) {
                     var4 = new SerializableEntity(var10);
                     ((AbstractHttpEntity)var4).setContentType(ContentType.DEFAULT_BINARY.toString());
                  } else {
                     File var3 = this.file;
                     if (var3 != null) {
                        var4 = new FileEntity(var3, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
                     } else {
                        var4 = new BasicHttpEntity();
                     }
                  }
               }
            }
         }
      }

      if (((AbstractHttpEntity)var4).getContentType() != null) {
         ContentType var7 = this.contentType;
         if (var7 != null) {
            ((AbstractHttpEntity)var4).setContentType(var7.toString());
         }
      }

      ((AbstractHttpEntity)var4).setContentEncoding(this.contentEncoding);
      ((AbstractHttpEntity)var4).setChunked(this.chunked);
      return (HttpEntity)(this.gzipCompress ? new GzipCompressingEntity((HttpEntity)var4) : var4);
   }

   public EntityBuilder chunked() {
      this.chunked = true;
      return this;
   }

   public byte[] getBinary() {
      return this.binary;
   }

   public String getContentEncoding() {
      return this.contentEncoding;
   }

   public ContentType getContentType() {
      return this.contentType;
   }

   public File getFile() {
      return this.file;
   }

   public List getParameters() {
      return this.parameters;
   }

   public Serializable getSerializable() {
      return this.serializable;
   }

   public InputStream getStream() {
      return this.stream;
   }

   public String getText() {
      return this.text;
   }

   public EntityBuilder gzipCompress() {
      this.gzipCompress = true;
      return this;
   }

   public boolean isChunked() {
      return this.chunked;
   }

   public boolean isGzipCompress() {
      return this.gzipCompress;
   }

   public EntityBuilder setBinary(byte[] var1) {
      this.clearContent();
      this.binary = var1;
      return this;
   }

   public EntityBuilder setContentEncoding(String var1) {
      this.contentEncoding = var1;
      return this;
   }

   public EntityBuilder setContentType(ContentType var1) {
      this.contentType = var1;
      return this;
   }

   public EntityBuilder setFile(File var1) {
      this.clearContent();
      this.file = var1;
      return this;
   }

   public EntityBuilder setParameters(List var1) {
      this.clearContent();
      this.parameters = var1;
      return this;
   }

   public EntityBuilder setParameters(NameValuePair... var1) {
      return this.setParameters(Arrays.asList(var1));
   }

   public EntityBuilder setSerializable(Serializable var1) {
      this.clearContent();
      this.serializable = var1;
      return this;
   }

   public EntityBuilder setStream(InputStream var1) {
      this.clearContent();
      this.stream = var1;
      return this;
   }

   public EntityBuilder setText(String var1) {
      this.clearContent();
      this.text = var1;
      return this;
   }
}
