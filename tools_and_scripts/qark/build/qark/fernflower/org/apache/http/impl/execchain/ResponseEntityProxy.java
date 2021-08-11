package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

class ResponseEntityProxy extends HttpEntityWrapper implements EofSensorWatcher {
   private final ConnectionHolder connHolder;

   ResponseEntityProxy(HttpEntity var1, ConnectionHolder var2) {
      super(var1);
      this.connHolder = var2;
   }

   private void abortConnection() {
      ConnectionHolder var1 = this.connHolder;
      if (var1 != null) {
         var1.abortConnection();
      }

   }

   private void cleanup() throws IOException {
      ConnectionHolder var1 = this.connHolder;
      if (var1 != null) {
         var1.close();
      }

   }

   public static void enchance(HttpResponse var0, ConnectionHolder var1) {
      HttpEntity var2 = var0.getEntity();
      if (var2 != null && var2.isStreaming() && var1 != null) {
         var0.setEntity(new ResponseEntityProxy(var2, var1));
      }

   }

   public void consumeContent() throws IOException {
      this.releaseConnection();
   }

   public boolean eofDetected(InputStream param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public InputStream getContent() throws IOException {
      return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
   }

   public boolean isRepeatable() {
      return false;
   }

   public void releaseConnection() {
      ConnectionHolder var1 = this.connHolder;
      if (var1 != null) {
         var1.releaseConnection();
      }

   }

   public boolean streamAbort(InputStream var1) throws IOException {
      this.cleanup();
      return false;
   }

   public boolean streamClosed(InputStream param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("ResponseEntityProxy{");
      var1.append(this.wrappedEntity);
      var1.append('}');
      return var1.toString();
   }

   public void writeTo(OutputStream param1) throws IOException {
      // $FF: Couldn't be decompiled
   }
}
