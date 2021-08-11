package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public abstract class AbstractResponseHandler implements ResponseHandler {
   public abstract Object handleEntity(HttpEntity var1) throws IOException;

   public Object handleResponse(HttpResponse var1) throws HttpResponseException, IOException {
      StatusLine var2 = var1.getStatusLine();
      HttpEntity var3 = var1.getEntity();
      if (var2.getStatusCode() < 300) {
         return var3 == null ? null : this.handleEntity(var3);
      } else {
         EntityUtils.consume(var3);
         throw new HttpResponseException(var2.getStatusCode(), var2.getReasonPhrase());
      }
   }
}
