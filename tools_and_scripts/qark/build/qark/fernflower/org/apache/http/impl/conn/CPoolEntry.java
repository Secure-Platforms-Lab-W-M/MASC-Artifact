package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

class CPoolEntry extends PoolEntry {
   private final Log log;
   private volatile boolean routeComplete;

   public CPoolEntry(Log var1, String var2, HttpRoute var3, ManagedHttpClientConnection var4, long var5, TimeUnit var7) {
      super(var2, var3, var4, var5, var7);
      this.log = var1;
   }

   public void close() {
      try {
         this.closeConnection();
      } catch (IOException var2) {
         this.log.debug("I/O error closing connection", var2);
      }
   }

   public void closeConnection() throws IOException {
      ((HttpClientConnection)this.getConnection()).close();
   }

   public boolean isClosed() {
      return ((HttpClientConnection)this.getConnection()).isOpen() ^ true;
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

   public boolean isRouteComplete() {
      return this.routeComplete;
   }

   public void markRouteComplete() {
      this.routeComplete = true;
   }

   public void shutdownConnection() throws IOException {
      ((HttpClientConnection)this.getConnection()).shutdown();
   }
}
