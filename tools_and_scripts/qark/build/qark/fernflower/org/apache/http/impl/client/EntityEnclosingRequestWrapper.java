package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolException;
import org.apache.http.entity.HttpEntityWrapper;

@Deprecated
public class EntityEnclosingRequestWrapper extends RequestWrapper implements HttpEntityEnclosingRequest {
   private boolean consumed;
   private HttpEntity entity;

   public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest var1) throws ProtocolException {
      super(var1);
      this.setEntity(var1.getEntity());
   }

   public boolean expectContinue() {
      Header var1 = this.getFirstHeader("Expect");
      return var1 != null && "100-continue".equalsIgnoreCase(var1.getValue());
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public boolean isRepeatable() {
      HttpEntity var1 = this.entity;
      return var1 == null || var1.isRepeatable() || !this.consumed;
   }

   public void setEntity(HttpEntity var1) {
      EntityEnclosingRequestWrapper.EntityWrapper var2;
      if (var1 != null) {
         var2 = new EntityEnclosingRequestWrapper.EntityWrapper(var1);
      } else {
         var2 = null;
      }

      this.entity = var2;
      this.consumed = false;
   }

   class EntityWrapper extends HttpEntityWrapper {
      EntityWrapper(HttpEntity var2) {
         super(var2);
      }

      public void consumeContent() throws IOException {
         EntityEnclosingRequestWrapper.this.consumed = true;
         super.consumeContent();
      }

      public InputStream getContent() throws IOException {
         EntityEnclosingRequestWrapper.this.consumed = true;
         return super.getContent();
      }

      public void writeTo(OutputStream var1) throws IOException {
         EntityEnclosingRequestWrapper.this.consumed = true;
         super.writeTo(var1);
      }
   }
}
