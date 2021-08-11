package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.protocol.HttpContext;

@Deprecated
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext {
   private final ClientConnectionManager connManager;
   private volatile long duration;
   private volatile boolean markedReusable;
   private volatile boolean released;
   private volatile OperatedClientConnection wrappedConnection;

   protected AbstractClientConnAdapter(ClientConnectionManager var1, OperatedClientConnection var2) {
      this.connManager = var1;
      this.wrappedConnection = var2;
      this.markedReusable = false;
      this.released = false;
      this.duration = Long.MAX_VALUE;
   }

   public void abortConnection() {
      // $FF: Couldn't be decompiled
   }

   @Deprecated
   protected final void assertNotAborted() throws InterruptedIOException {
      if (this.isReleased()) {
         throw new InterruptedIOException("Connection has been shut down");
      }
   }

   protected final void assertValid(OperatedClientConnection var1) throws ConnectionShutdownException {
      if (this.isReleased() || var1 == null) {
         throw new ConnectionShutdownException();
      }
   }

   public void bind(Socket var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   protected void detach() {
      synchronized(this){}

      try {
         this.wrappedConnection = null;
         this.duration = Long.MAX_VALUE;
      } finally {
         ;
      }

   }

   public void flush() throws IOException {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      var1.flush();
   }

   public Object getAttribute(String var1) {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2 instanceof HttpContext ? ((HttpContext)var2).getAttribute(var1) : null;
   }

   public InetAddress getLocalAddress() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getLocalAddress();
   }

   public int getLocalPort() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getLocalPort();
   }

   protected ClientConnectionManager getManager() {
      return this.connManager;
   }

   public HttpConnectionMetrics getMetrics() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getMetrics();
   }

   public InetAddress getRemoteAddress() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getRemoteAddress();
   }

   public int getRemotePort() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getRemotePort();
   }

   public SSLSession getSSLSession() {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      if (!this.isOpen()) {
         return null;
      } else {
         SSLSession var1 = null;
         Socket var3 = var2.getSocket();
         if (var3 instanceof SSLSocket) {
            var1 = ((SSLSocket)var3).getSession();
         }

         return var1;
      }
   }

   public Socket getSocket() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return !this.isOpen() ? null : var1.getSocket();
   }

   public int getSocketTimeout() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getSocketTimeout();
   }

   protected OperatedClientConnection getWrappedConnection() {
      return this.wrappedConnection;
   }

   public boolean isMarkedReusable() {
      return this.markedReusable;
   }

   public boolean isOpen() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      return var1 == null ? false : var1.isOpen();
   }

   protected boolean isReleased() {
      return this.released;
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2.isResponseAvailable(var1);
   }

   public boolean isSecure() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.isSecure();
   }

   public boolean isStale() {
      if (this.isReleased()) {
         return true;
      } else {
         OperatedClientConnection var1 = this.getWrappedConnection();
         return var1 == null ? true : var1.isStale();
      }
   }

   public void markReusable() {
      this.markedReusable = true;
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.receiveResponseEntity(var1);
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      this.unmarkReusable();
      return var1.receiveResponseHeader();
   }

   public void releaseConnection() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.released;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1) {
            return;
         }

         try {
            this.released = true;
            this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public Object removeAttribute(String var1) {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2 instanceof HttpContext ? ((HttpContext)var2).removeAttribute(var1) : null;
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.sendRequestEntity(var1);
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.sendRequestHeader(var1);
   }

   public void setAttribute(String var1, Object var2) {
      OperatedClientConnection var3 = this.getWrappedConnection();
      this.assertValid(var3);
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
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      var2.setSocketTimeout(var1);
   }

   public void unmarkReusable() {
      this.markedReusable = false;
   }
}
