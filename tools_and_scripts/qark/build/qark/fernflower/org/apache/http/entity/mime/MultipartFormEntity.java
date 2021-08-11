package org.apache.http.entity.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

class MultipartFormEntity implements HttpEntity {
   private final long contentLength;
   private final org.apache.http.Header contentType;
   private final AbstractMultipartForm multipart;

   MultipartFormEntity(AbstractMultipartForm var1, ContentType var2, long var3) {
      this.multipart = var1;
      this.contentType = new BasicHeader("Content-Type", var2.toString());
      this.contentLength = var3;
   }

   public void consumeContent() {
   }

   public InputStream getContent() throws IOException {
      long var1 = this.contentLength;
      if (var1 >= 0L) {
         if (var1 <= 25600L) {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            this.writeTo(var4);
            var4.flush();
            return new ByteArrayInputStream(var4.toByteArray());
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Content length is too long: ");
            var3.append(this.contentLength);
            throw new ContentTooLongException(var3.toString());
         }
      } else {
         throw new ContentTooLongException("Content length is unknown");
      }
   }

   public org.apache.http.Header getContentEncoding() {
      return null;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public org.apache.http.Header getContentType() {
      return this.contentType;
   }

   AbstractMultipartForm getMultipart() {
      return this.multipart;
   }

   public boolean isChunked() {
      return this.isRepeatable() ^ true;
   }

   public boolean isRepeatable() {
      return this.contentLength != -1L;
   }

   public boolean isStreaming() {
      return this.isRepeatable() ^ true;
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.multipart.writeTo(var1);
   }
}
