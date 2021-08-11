package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class PoolingClientConnectionManager implements ClientConnectionManager, ConnPoolControl {
   private final DnsResolver dnsResolver;
   private final Log log;
   private final ClientConnectionOperator operator;
   private final HttpConnPool pool;
   private final SchemeRegistry schemeRegistry;

   public PoolingClientConnectionManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   public PoolingClientConnectionManager(SchemeRegistry var1) {
      this(var1, -1L, TimeUnit.MILLISECONDS);
   }

   public PoolingClientConnectionManager(SchemeRegistry var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, new SystemDefaultDnsResolver());
   }

   public PoolingClientConnectionManager(SchemeRegistry var1, long var2, TimeUnit var4, DnsResolver var5) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Scheme registry");
      Args.notNull(var5, "DNS resolver");
      this.schemeRegistry = var1;
      this.dnsResolver = var5;
      this.operator = this.createConnectionOperator(var1);
      this.pool = new HttpConnPool(this.log, this.operator, 2, 20, var2, var4);
   }

   public PoolingClientConnectionManager(SchemeRegistry var1, DnsResolver var2) {
      this(var1, -1L, TimeUnit.MILLISECONDS, var2);
   }

   private String format(HttpRoute var1, Object var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("[route: ");
      var3.append(var1);
      var3.append("]");
      if (var2 != null) {
         var3.append("[state: ");
         var3.append(var2);
         var3.append("]");
      }

      return var3.toString();
   }

   private String format(HttpPoolEntry var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("[id: ");
      var2.append(var1.getId());
      var2.append("]");
      var2.append("[route: ");
      var2.append(var1.getRoute());
      var2.append("]");
      Object var3 = var1.getState();
      if (var3 != null) {
         var2.append("[state: ");
         var2.append(var3);
         var2.append("]");
      }

      return var2.toString();
   }

   private String formatStats(HttpRoute var1) {
      StringBuilder var2 = new StringBuilder();
      PoolStats var3 = this.pool.getTotalStats();
      PoolStats var4 = this.pool.getStats(var1);
      var2.append("[total kept alive: ");
      var2.append(var3.getAvailable());
      var2.append("; ");
      var2.append("route allocated: ");
      var2.append(var4.getLeased() + var4.getAvailable());
      var2.append(" of ");
      var2.append(var4.getMax());
      var2.append("; ");
      var2.append("total allocated: ");
      var2.append(var3.getLeased() + var3.getAvailable());
      var2.append(" of ");
      var2.append(var3.getMax());
      var2.append("]");
      return var2.toString();
   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      this.pool.closeExpired();
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      if (this.log.isDebugEnabled()) {
         Log var4 = this.log;
         StringBuilder var5 = new StringBuilder();
         var5.append("Closing connections idle longer than ");
         var5.append(var1);
         var5.append(" ");
         var5.append(var3);
         var4.debug(var5.toString());
      }

      this.pool.closeIdle(var1, var3);
   }

   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry var1) {
      return new DefaultClientConnectionOperator(var1, this.dnsResolver);
   }

   protected void finalize() throws Throwable {
      try {
         this.shutdown();
      } finally {
         super.finalize();
      }

   }

   public int getDefaultMaxPerRoute() {
      return this.pool.getDefaultMaxPerRoute();
   }

   public int getMaxPerRoute(HttpRoute var1) {
      return this.pool.getMaxPerRoute(var1);
   }

   public int getMaxTotal() {
      return this.pool.getMaxTotal();
   }

   public SchemeRegistry getSchemeRegistry() {
      return this.schemeRegistry;
   }

   public PoolStats getStats(HttpRoute var1) {
      return this.pool.getStats(var1);
   }

   public PoolStats getTotalStats() {
      return this.pool.getTotalStats();
   }

   ManagedClientConnection leaseConnection(Future var1, long var2, TimeUnit var4) throws InterruptedException, ConnectionPoolTimeoutException {
      ExecutionException var17;
      label100: {
         HttpPoolEntry var19;
         try {
            var19 = (HttpPoolEntry)var1.get(var2, var4);
         } catch (ExecutionException var15) {
            var17 = var15;
            break label100;
         } catch (TimeoutException var16) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
         }

         ExecutionException var10000;
         label84: {
            boolean var5;
            boolean var10001;
            label83: {
               if (var19 != null) {
                  label82: {
                     label66: {
                        try {
                           if (var1.isCancelled()) {
                              break label82;
                           }

                           if (var19.getConnection() == null) {
                              break label66;
                           }
                        } catch (ExecutionException var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label84;
                        } catch (TimeoutException var14) {
                           var10001 = false;
                           throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
                        }

                        var5 = true;
                        break label83;
                     }

                     var5 = false;
                     break label83;
                  }
               }

               try {
                  throw new InterruptedException();
               } catch (ExecutionException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label84;
               } catch (TimeoutException var12) {
                  var10001 = false;
                  throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
               }
            }

            try {
               Asserts.check(var5, "Pool entry with no connection");
               if (this.log.isDebugEnabled()) {
                  Log var18 = this.log;
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Connection leased: ");
                  var6.append(this.format(var19));
                  var6.append(this.formatStats((HttpRoute)var19.getRoute()));
                  var18.debug(var6.toString());
               }
            } catch (ExecutionException var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            } catch (TimeoutException var10) {
               var10001 = false;
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }

            try {
               return new ManagedClientConnectionImpl(this, this.operator, var19);
            } catch (ExecutionException var7) {
               var10000 = var7;
               var10001 = false;
            } catch (TimeoutException var8) {
               var10001 = false;
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }
         }

         var17 = var10000;
      }

      Throwable var21 = var17.getCause();
      Object var20 = var21;
      if (var21 == null) {
         var20 = var17;
      }

      this.log.error("Unexpected exception leasing connection from pool", (Throwable)var20);
      throw new InterruptedException();
   }

   public void releaseConnection(ManagedClientConnection param1, long param2, TimeUnit param4) {
      // $FF: Couldn't be decompiled
   }

   public ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "HTTP route");
      if (this.log.isDebugEnabled()) {
         Log var3 = this.log;
         StringBuilder var4 = new StringBuilder();
         var4.append("Connection request: ");
         var4.append(this.format(var1, var2));
         var4.append(this.formatStats(var1));
         var3.debug(var4.toString());
      }

      return new ClientConnectionRequest(this.pool.lease(var1, var2)) {
         // $FF: synthetic field
         final Future val$future;

         {
            this.val$future = var2;
         }

         public void abortRequest() {
            this.val$future.cancel(true);
         }

         public ManagedClientConnection getConnection(long var1, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException {
            return PoolingClientConnectionManager.this.leaseConnection(this.val$future, var1, var3);
         }
      };
   }

   public void setDefaultMaxPerRoute(int var1) {
      this.pool.setDefaultMaxPerRoute(var1);
   }

   public void setMaxPerRoute(HttpRoute var1, int var2) {
      this.pool.setMaxPerRoute(var1, var2);
   }

   public void setMaxTotal(int var1) {
      this.pool.setMaxTotal(var1);
   }

   public void shutdown() {
      this.log.debug("Connection manager is shutting down");

      try {
         this.pool.shutdown();
      } catch (IOException var2) {
         this.log.debug("I/O exception shutting down connection manager", var2);
      }

      this.log.debug("Connection manager shut down");
   }
}
