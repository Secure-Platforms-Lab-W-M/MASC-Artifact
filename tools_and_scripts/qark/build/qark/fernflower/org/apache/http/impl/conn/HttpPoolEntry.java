package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.pool.PoolEntry;

@Deprecated
class HttpPoolEntry extends PoolEntry {
   private final Log log;
   private final RouteTracker tracker;

   public HttpPoolEntry(Log var1, String var2, HttpRoute var3, OperatedClientConnection var4, long var5, TimeUnit var7) {
      super(var2, var3, var4, var5, var7);
      this.log = var1;
      this.tracker = new RouteTracker(var3);
   }

   public void close() {
      OperatedClientConnection var1 = (OperatedClientConnection)this.getConnection();

      try {
         var1.close();
      } catch (IOException var2) {
         this.log.debug("I/O error closing connection", var2);
      }
   }

   HttpRoute getEffectiveRoute() {
      return this.tracker.toRoute();
   }

   HttpRoute getPlannedRoute() {
      return (HttpRoute)this.getRoute();
   }

   RouteTracker getTracker() {
      return this.tracker;
   }

   public boolean isClosed() {
      return ((OperatedClientConnection)this.getConnection()).isOpen() ^ true;
   }

   public boolean isExpired(long var1) {
      boolean var3 = super.isExpired(var1);
      if (var3 && this.log.isDebugEnabled()) {
         Log var4 = this.log;
         StringBuilder var5 = new StringBuilder();
         var5.append("Connection ");
         var5.append(this);
         var5.append(" expired @ ");
         var5.append(new Date(this.getExpiry()));
         var4.debug(var5.toString());
      }

      return var3;
   }
}
