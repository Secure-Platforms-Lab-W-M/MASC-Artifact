package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;

public interface HttpClientConnectionManager {
   void closeExpiredConnections();

   void closeIdleConnections(long var1, TimeUnit var3);

   void connect(HttpClientConnection var1, HttpRoute var2, int var3, HttpContext var4) throws IOException;

   void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5);

   ConnectionRequest requestConnection(HttpRoute var1, Object var2);

   void routeComplete(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException;

   void shutdown();

   void upgrade(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException;
}
