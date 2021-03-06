package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public interface ServiceUnavailableRetryStrategy {
   long getRetryInterval();

   boolean retryRequest(HttpResponse var1, int var2, HttpContext var3);
}
