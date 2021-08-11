package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface ManagedClientConnection extends HttpRoutedConnection, ManagedHttpClientConnection, ConnectionReleaseTrigger {
   HttpRoute getRoute();

   SSLSession getSSLSession();

   Object getState();

   boolean isMarkedReusable();

   boolean isSecure();

   void layerProtocol(HttpContext var1, HttpParams var2) throws IOException;

   void markReusable();

   void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException;

   void setIdleDuration(long var1, TimeUnit var3);

   void setState(Object var1);

   void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException;

   void tunnelTarget(boolean var1, HttpParams var2) throws IOException;

   void unmarkReusable();
}
