package org.apache.http.impl.client;

import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;

@Deprecated
public class ContentEncodingHttpClient extends DefaultHttpClient {
   public ContentEncodingHttpClient() {
      this((HttpParams)null);
   }

   public ContentEncodingHttpClient(ClientConnectionManager var1, HttpParams var2) {
      super(var1, var2);
   }

   public ContentEncodingHttpClient(HttpParams var1) {
      this((ClientConnectionManager)null, var1);
   }

   protected BasicHttpProcessor createHttpProcessor() {
      BasicHttpProcessor var1 = super.createHttpProcessor();
      var1.addRequestInterceptor(new RequestAcceptEncoding());
      var1.addResponseInterceptor(new ResponseContentEncoding());
      return var1;
   }
}
