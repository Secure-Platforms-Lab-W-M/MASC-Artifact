package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext {
   private final Map attributes = new HashMap();
   private boolean connSecure;
   private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
   private final Log log = LogFactory.getLog(this.getClass());
   private volatile boolean shutdown;
   private volatile Socket socket;
   private HttpHost targetHost;
   private final Log wireLog = LogFactory.getLog("org.apache.http.wire");

   public void bind(Socket var1) throws IOException {
      this.bind(var1, new BasicHttpParams());
   }

   public void close() throws IOException {
      try {
         super.close();
         if (this.log.isDebugEnabled()) {
            Log var1 = this.log;
            StringBuilder var2 = new StringBuilder();
            var2.append("Connection ");
            var2.append(this);
            var2.append(" closed");
            var1.debug(var2.toString());
         }

      } catch (IOException var3) {
         this.log.debug("I/O error closing connection", var3);
      }
   }

   protected HttpMessageParser createResponseParser(SessionInputBuffer var1, HttpResponseFactory var2, HttpParams var3) {
      return new DefaultHttpResponseParser(var1, (LineParser)null, var2, var3);
   }

   protected SessionInputBuffer createSessionInputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      if (var2 <= 0) {
         var2 = 8192;
      }

      SessionInputBuffer var4 = super.createSessionInputBuffer(var1, var2, var3);
      Object var5 = var4;
      if (this.wireLog.isDebugEnabled()) {
         var5 = new LoggingSessionInputBuffer(var4, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(var3));
      }

      return (SessionInputBuffer)var5;
   }

   protected SessionOutputBuffer createSessionOutputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      if (var2 <= 0) {
         var2 = 8192;
      }

      SessionOutputBuffer var4 = super.createSessionOutputBuffer(var1, var2, var3);
      Object var5 = var4;
      if (this.wireLog.isDebugEnabled()) {
         var5 = new LoggingSessionOutputBuffer(var4, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(var3));
      }

      return (SessionOutputBuffer)var5;
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public String getId() {
      return null;
   }

   public SSLSession getSSLSession() {
      return this.socket instanceof SSLSocket ? ((SSLSocket)this.socket).getSession() : null;
   }

   public final Socket getSocket() {
      return this.socket;
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final boolean isSecure() {
      return this.connSecure;
   }

   public void openCompleted(boolean var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "Parameters");
      this.assertNotOpen();
      this.connSecure = var1;
      this.bind(this.socket, var2);
   }

   public void opening(Socket var1, HttpHost var2) throws IOException {
      this.assertNotOpen();
      this.socket = var1;
      this.targetHost = var2;
      if (this.shutdown) {
         var1.close();
         throw new InterruptedIOException("Connection already shutdown");
      }
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      HttpResponse var3 = super.receiveResponseHeader();
      Log var4;
      StringBuilder var5;
      if (this.log.isDebugEnabled()) {
         var4 = this.log;
         var5 = new StringBuilder();
         var5.append("Receiving response: ");
         var5.append(var3.getStatusLine());
         var4.debug(var5.toString());
      }

      if (this.headerLog.isDebugEnabled()) {
         var4 = this.headerLog;
         var5 = new StringBuilder();
         var5.append("<< ");
         var5.append(var3.getStatusLine().toString());
         var4.debug(var5.toString());
         Header[] var8 = var3.getAllHeaders();
         int var2 = var8.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            Header var9 = var8[var1];
            Log var6 = this.headerLog;
            StringBuilder var7 = new StringBuilder();
            var7.append("<< ");
            var7.append(var9.toString());
            var6.debug(var7.toString());
         }
      }

      return var3;
   }

   public Object removeAttribute(String var1) {
      return this.attributes.remove(var1);
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      Log var4;
      StringBuilder var5;
      if (this.log.isDebugEnabled()) {
         var4 = this.log;
         var5 = new StringBuilder();
         var5.append("Sending request: ");
         var5.append(var1.getRequestLine());
         var4.debug(var5.toString());
      }

      super.sendRequestHeader(var1);
      if (this.headerLog.isDebugEnabled()) {
         var4 = this.headerLog;
         var5 = new StringBuilder();
         var5.append(">> ");
         var5.append(var1.getRequestLine().toString());
         var4.debug(var5.toString());
         Header[] var7 = var1.getAllHeaders();
         int var3 = var7.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            Header var8 = var7[var2];
            Log var9 = this.headerLog;
            StringBuilder var6 = new StringBuilder();
            var6.append(">> ");
            var6.append(var8.toString());
            var9.debug(var6.toString());
         }
      }

   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }

   public void shutdown() throws IOException {
      this.shutdown = true;

      IOException var10000;
      label37: {
         boolean var10001;
         try {
            super.shutdown();
            if (this.log.isDebugEnabled()) {
               Log var1 = this.log;
               StringBuilder var2 = new StringBuilder();
               var2.append("Connection ");
               var2.append(this);
               var2.append(" shut down");
               var1.debug(var2.toString());
            }
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
            break label37;
         }

         Socket var6;
         try {
            var6 = this.socket;
         } catch (IOException var4) {
            var10000 = var4;
            var10001 = false;
            break label37;
         }

         if (var6 == null) {
            return;
         }

         try {
            var6.close();
            return;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IOException var7 = var10000;
      this.log.debug("I/O error shutting down connection", var7);
   }

   public void update(Socket var1, HttpHost var2, boolean var3, HttpParams var4) throws IOException {
      this.assertOpen();
      Args.notNull(var2, "Target host");
      Args.notNull(var4, "Parameters");
      if (var1 != null) {
         this.socket = var1;
         this.bind(var1, var4);
      }

      this.targetHost = var2;
      this.connSecure = var3;
   }
}
