package org.apache.http.conn;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;

@Deprecated
public interface ClientConnectionManager {
   void closeExpiredConnections();

   void closeIdleConnections(long var1, TimeUnit var3);

   SchemeRegistry getSchemeRegistry();

   void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4);

   ClientConnectionRequest requestConnection(HttpRoute var1, Object var2);

   void shutdown();
}
