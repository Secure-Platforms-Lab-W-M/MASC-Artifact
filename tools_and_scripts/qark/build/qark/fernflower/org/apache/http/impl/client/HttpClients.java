package org.apache.http.impl.client;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClients {
   private HttpClients() {
   }

   public static CloseableHttpClient createDefault() {
      return HttpClientBuilder.create().build();
   }

   public static CloseableHttpClient createMinimal() {
      return new MinimalHttpClient(new PoolingHttpClientConnectionManager());
   }

   public static CloseableHttpClient createMinimal(HttpClientConnectionManager var0) {
      return new MinimalHttpClient(var0);
   }

   public static CloseableHttpClient createSystem() {
      return HttpClientBuilder.create().useSystemProperties().build();
   }

   public static HttpClientBuilder custom() {
      return HttpClientBuilder.create();
   }
}
