package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public abstract class AbstractPoolEntry {
   protected final ClientConnectionOperator connOperator;
   protected final OperatedClientConnection connection;
   protected volatile HttpRoute route;
   protected volatile Object state;
   protected volatile RouteTracker tracker;

   protected AbstractPoolEntry(ClientConnectionOperator var1, HttpRoute var2) {
      Args.notNull(var1, "Connection operator");
      this.connOperator = var1;
      this.connection = var1.createConnection();
      this.route = var2;
      this.tracker = null;
   }

   public Object getState() {
      return this.state;
   }

   public void layerProtocol(HttpContext var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      Asserts.check(this.tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
      Asserts.check(this.tracker.isLayered() ^ true, "Multiple protocol layering not supported");
      HttpHost var3 = this.tracker.getTargetHost();
      this.connOperator.updateSecureConnection(this.connection, var3, var1, var2);
      this.tracker.layerProtocol(this.connection.isSecure());
   }

   public void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Route");
      Args.notNull(var3, "HTTP parameters");
      if (this.tracker != null) {
         Asserts.check(this.tracker.isConnected() ^ true, "Connection already open");
      }

      this.tracker = new RouteTracker(var1);
      HttpHost var5 = var1.getProxyHost();
      ClientConnectionOperator var6 = this.connOperator;
      OperatedClientConnection var7 = this.connection;
      HttpHost var4;
      if (var5 != null) {
         var4 = var5;
      } else {
         var4 = var1.getTargetHost();
      }

      var6.openConnection(var7, var4, var1.getLocalAddress(), var2, var3);
      RouteTracker var8 = this.tracker;
      if (var8 != null) {
         if (var5 == null) {
            var8.connectTarget(this.connection.isSecure());
         } else {
            var8.connectProxy(var5, this.connection.isSecure());
         }
      } else {
         throw new InterruptedIOException("Request aborted");
      }
   }

   public void setState(Object var1) {
      this.state = var1;
   }

   protected void shutdownEntry() {
      this.tracker = null;
      this.state = null;
   }

   public void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Next proxy");
      Args.notNull(var3, "Parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      this.connection.update((Socket)null, var1, var2, var3);
      this.tracker.tunnelProxy(var1, var2);
   }

   public void tunnelTarget(boolean var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      Asserts.check(this.tracker.isTunnelled() ^ true, "Connection is already tunnelled");
      this.connection.update((Socket)null, this.tracker.getTargetHost(), var1, var2);
      this.tracker.tunnelTarget(var1);
   }
}
