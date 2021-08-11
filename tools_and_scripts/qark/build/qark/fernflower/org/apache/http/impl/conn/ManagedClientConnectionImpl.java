package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
class ManagedClientConnectionImpl implements ManagedClientConnection {
   private volatile long duration;
   private final ClientConnectionManager manager;
   private final ClientConnectionOperator operator;
   private volatile HttpPoolEntry poolEntry;
   private volatile boolean reusable;

   ManagedClientConnectionImpl(ClientConnectionManager var1, ClientConnectionOperator var2, HttpPoolEntry var3) {
      Args.notNull(var1, "Connection manager");
      Args.notNull(var2, "Connection operator");
      Args.notNull(var3, "HTTP pool entry");
      this.manager = var1;
      this.operator = var2;
      this.poolEntry = var3;
      this.reusable = false;
      this.duration = Long.MAX_VALUE;
   }

   private OperatedClientConnection ensureConnection() {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         return (OperatedClientConnection)var1.getConnection();
      } else {
         throw new ConnectionShutdownException();
      }
   }

   private HttpPoolEntry ensurePoolEntry() {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         return var1;
      } else {
         throw new ConnectionShutdownException();
      }
   }

   private OperatedClientConnection getConnection() {
      HttpPoolEntry var1 = this.poolEntry;
      return var1 == null ? null : (OperatedClientConnection)var1.getConnection();
   }

   public void abortConnection() {
      // $FF: Couldn't be decompiled
   }

   public void bind(Socket var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public void close() throws IOException {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         OperatedClientConnection var2 = (OperatedClientConnection)var1.getConnection();
         var1.getTracker().reset();
         var2.close();
      }

   }

   HttpPoolEntry detach() {
      HttpPoolEntry var1 = this.poolEntry;
      this.poolEntry = null;
      return var1;
   }

   public void flush() throws IOException {
      this.ensureConnection().flush();
   }

   public Object getAttribute(String var1) {
      OperatedClientConnection var2 = this.ensureConnection();
      return var2 instanceof HttpContext ? ((HttpContext)var2).getAttribute(var1) : null;
   }

   public String getId() {
      return null;
   }

   public InetAddress getLocalAddress() {
      return this.ensureConnection().getLocalAddress();
   }

   public int getLocalPort() {
      return this.ensureConnection().getLocalPort();
   }

   public ClientConnectionManager getManager() {
      return this.manager;
   }

   public HttpConnectionMetrics getMetrics() {
      return this.ensureConnection().getMetrics();
   }

   HttpPoolEntry getPoolEntry() {
      return this.poolEntry;
   }

   public InetAddress getRemoteAddress() {
      return this.ensureConnection().getRemoteAddress();
   }

   public int getRemotePort() {
      return this.ensureConnection().getRemotePort();
   }

   public HttpRoute getRoute() {
      return this.ensurePoolEntry().getEffectiveRoute();
   }

   public SSLSession getSSLSession() {
      OperatedClientConnection var2 = this.ensureConnection();
      SSLSession var1 = null;
      Socket var3 = var2.getSocket();
      if (var3 instanceof SSLSocket) {
         var1 = ((SSLSocket)var3).getSession();
      }

      return var1;
   }

   public Socket getSocket() {
      return this.ensureConnection().getSocket();
   }

   public int getSocketTimeout() {
      return this.ensureConnection().getSocketTimeout();
   }

   public Object getState() {
      return this.ensurePoolEntry().getState();
   }

   public boolean isMarkedReusable() {
      return this.reusable;
   }

   public boolean isOpen() {
      OperatedClientConnection var1 = this.getConnection();
      return var1 != null ? var1.isOpen() : false;
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      return this.ensureConnection().isResponseAvailable(var1);
   }

   public boolean isSecure() {
      return this.ensureConnection().isSecure();
   }

   public boolean isStale() {
      OperatedClientConnection var1 = this.getConnection();
      return var1 != null ? var1.isStale() : true;
   }

   public void layerProtocol(HttpContext param1, HttpParams param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void markReusable() {
      this.reusable = true;
   }

   public void open(HttpRoute param1, HttpContext param2, HttpParams param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      this.ensureConnection().receiveResponseEntity(var1);
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      return this.ensureConnection().receiveResponseHeader();
   }

   public void releaseConnection() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.poolEntry == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            this.poolEntry = null;
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public Object removeAttribute(String var1) {
      OperatedClientConnection var2 = this.ensureConnection();
      return var2 instanceof HttpContext ? ((HttpContext)var2).removeAttribute(var1) : null;
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      this.ensureConnection().sendRequestEntity(var1);
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      this.ensureConnection().sendRequestHeader(var1);
   }

   public void setAttribute(String var1, Object var2) {
      OperatedClientConnection var3 = this.ensureConnection();
      if (var3 instanceof HttpContext) {
         ((HttpContext)var3).setAttribute(var1, var2);
      }

   }

   public void setIdleDuration(long var1, TimeUnit var3) {
      if (var1 > 0L) {
         this.duration = var3.toMillis(var1);
      } else {
         this.duration = -1L;
      }
   }

   public void setSocketTimeout(int var1) {
      this.ensureConnection().setSocketTimeout(var1);
   }

   public void setState(Object var1) {
      this.ensurePoolEntry().setState(var1);
   }

   public void shutdown() throws IOException {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         OperatedClientConnection var2 = (OperatedClientConnection)var1.getConnection();
         var1.getTracker().reset();
         var2.shutdown();
      }

   }

   public void tunnelProxy(HttpHost param1, boolean param2, HttpParams param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void tunnelTarget(boolean param1, HttpParams param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void unmarkReusable() {
      this.reusable = false;
   }
}
