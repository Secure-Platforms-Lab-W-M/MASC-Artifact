package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

public class BasicResponseHandler extends AbstractResponseHandler {
   public String handleEntity(HttpEntity var1) throws IOException {
      return EntityUtils.toString(var1);
   }

   public String handleResponse(HttpResponse var1) throws HttpResponseException, IOException {
      return (String)super.handleResponse(var1);
   }
}
