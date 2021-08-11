package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;

class RequestEntityProxy implements HttpEntity {
   private boolean consumed = false;
   private final HttpEntity original;

   RequestEntityProxy(HttpEntity var1) {
      this.original = var1;
   }

   static void enhance(HttpEntityEnclosingRequest var0) {
      HttpEntity var1 = var0.getEntity();
      if (var1 != null && !var1.isRepeatable() && !isEnhanced(var1)) {
         var0.setEntity(new RequestEntityProxy(var1));
      }

   }

   static boolean isEnhanced(HttpEntity var0) {
      return var0 instanceof RequestEntityProxy;
   }

   static boolean isRepeatable(HttpRequest var0) {
      if (var0 instanceof HttpEntityEnclosingRequest) {
         HttpEntity var1 = ((HttpEntityEnclosingRequest)var0).getEntity();
         if (var1 != null) {
            if (isEnhanced(var1) && !((RequestEntityProxy)var1).isConsumed()) {
               return true;
            }

            return var1.isRepeatable();
         }
      }

      return true;
   }

   public void consumeContent() throws IOException {
      this.consumed = true;
      this.original.consumeContent();
   }

   public InputStream getContent() throws IOException, IllegalStateException {
      return this.original.getContent();
   }

   public Header getContentEncoding() {
      return this.original.getContentEncoding();
   }

   public long getContentLength() {
      return this.original.getContentLength();
   }

   public Header getContentType() {
      return this.original.getContentType();
   }

   public HttpEntity getOriginal() {
      return this.original;
   }

   public boolean isChunked() {
      return this.original.isChunked();
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public boolean isRepeatable() {
      return this.original.isRepeatable();
   }

   public boolean isStreaming() {
      return this.original.isStreaming();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("RequestEntityProxy{");
      var1.append(this.original);
      var1.append('}');
      return var1.toString();
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.consumed = true;
      this.original.writeTo(var1);
   }
}
