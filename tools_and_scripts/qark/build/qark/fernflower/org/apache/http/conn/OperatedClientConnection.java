package org.apache.http.conn;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.params.HttpParams;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection {
   Socket getSocket();

   HttpHost getTargetHost();

   boolean isSecure();

   void openCompleted(boolean var1, HttpParams var2) throws IOException;

   void opening(Socket var1, HttpHost var2) throws IOException;

   void update(Socket var1, HttpHost var2, boolean var3, HttpParams var4) throws IOException;
}
