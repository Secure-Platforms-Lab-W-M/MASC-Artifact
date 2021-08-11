package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class PoolingHttpClientConnectionManager implements HttpClientConnectionManager, ConnPoolControl, Closeable {
   private final PoolingHttpClientConnectionManager.ConfigData configData;
   private final HttpClientConnectionOperator connectionOperator;
   private final AtomicBoolean isShutDown;
   private final Log log;
   private final CPool pool;

   public PoolingHttpClientConnectionManager() {
      this(getDefaultRegistry());
   }

   public PoolingHttpClientConnectionManager(long var1, TimeUnit var3) {
      this(getDefaultRegistry(), (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null, var1, var3);
   }

   public PoolingHttpClientConnectionManager(Registry var1) {
      this(var1, (HttpConnectionFactory)null, (DnsResolver)null);
   }

   public PoolingHttpClientConnectionManager(Registry var1, DnsResolver var2) {
      this(var1, (HttpConnectionFactory)null, var2);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2) {
      this(var1, var2, (DnsResolver)null);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2, DnsResolver var3) {
      this(var1, var2, (SchemePortResolver)null, var3, -1L, TimeUnit.MILLISECONDS);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2, SchemePortResolver var3, DnsResolver var4, long var5, TimeUnit var7) {
      this(new DefaultHttpClientConnectionOperator(var1, var3, var4), var2, var5, var7);
   }

   public PoolingHttpClientConnectionManager(HttpClientConnectionOperator var1, HttpConnectionFactory var2, long var3, TimeUnit var5) {
      this.log = LogFactory.getLog(this.getClass());
      PoolingHttpClientConnectionManager.ConfigData var6 = new PoolingHttpClientConnectionManager.ConfigData();
      this.configData = var6;
      CPool var7 = new CPool(new PoolingHttpClientConnectionManager.InternalConnectionFactory(var6, var2), 2, 20, var3, var5);
      this.pool = var7;
      var7.setValidateAfterInactivity(2000);
      this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(var1, "HttpClientConnectionOperator");
      this.isShutDown = new AtomicBoolean(false);
   }

   public PoolingHttpClientConnectionManager(HttpConnectionFactory var1) {
      this(getDefaultRegistry(), var1, (DnsResolver)null);
   }

   PoolingHttpClientConnectionManager(CPool var1, Lookup var2, SchemePortResolver var3, DnsResolver var4) {
      this.log = LogFactory.getLog(this.getClass());
      this.configData = new PoolingHttpClientConnectionManager.ConfigData();
      this.pool = var1;
      this.connectionOperator = new DefaultHttpClientConnectionOperator(var2, var3, var4);
      this.isShutDown = new AtomicBoolean(false);
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

   private String format(CPoolEntry var1) {
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

   private static Registry getDefaultRegistry() {
      return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
   }

   private SocketConfig resolveSocketConfig(HttpHost var1) {
      SocketConfig var2 = this.configData.getSocketConfig(var1);
      SocketConfig var3 = var2;
      if (var2 == null) {
         var3 = this.configData.getDefaultSocketConfig();
      }

      var2 = var3;
      if (var3 == null) {
         var2 = SocketConfig.DEFAULT;
      }

      return var2;
   }

   public void close() {
      this.shutdown();
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

   public void connect(HttpClientConnection param1, HttpRoute param2, int param3, HttpContext param4) throws IOException {
      // $FF: Couldn't be decompiled
   }

   protected void enumAvailable(PoolEntryCallback var1) {
      this.pool.enumAvailable(var1);
   }

   protected void enumLeased(PoolEntryCallback var1) {
      this.pool.enumLeased(var1);
   }

   protected void finalize() throws Throwable {
      try {
         this.shutdown();
      } finally {
         super.finalize();
      }

   }

   public ConnectionConfig getConnectionConfig(HttpHost var1) {
      return this.configData.getConnectionConfig(var1);
   }

   public ConnectionConfig getDefaultConnectionConfig() {
      return this.configData.getDefaultConnectionConfig();
   }

   public int getDefaultMaxPerRoute() {
      return this.pool.getDefaultMaxPerRoute();
   }

   public SocketConfig getDefaultSocketConfig() {
      return this.configData.getDefaultSocketConfig();
   }

   public int getMaxPerRoute(HttpRoute var1) {
      return this.pool.getMaxPerRoute(var1);
   }

   public int getMaxTotal() {
      return this.pool.getMaxTotal();
   }

   public Set getRoutes() {
      return this.pool.getRoutes();
   }

   public SocketConfig getSocketConfig(HttpHost var1) {
      return this.configData.getSocketConfig(var1);
   }

   public PoolStats getStats(HttpRoute var1) {
      return this.pool.getStats(var1);
   }

   public PoolStats getTotalStats() {
      return this.pool.getTotalStats();
   }

   public int getValidateAfterInactivity() {
      return this.pool.getValidateAfterInactivity();
   }

   protected HttpClientConnection leaseConnection(Future var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
      CPoolEntry var13;
      try {
         var13 = (CPoolEntry)var1.get(var2, var4);
      } catch (TimeoutException var11) {
         throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
      }

      boolean var10001;
      if (var13 != null) {
         label57: {
            boolean var5;
            label46: {
               label45: {
                  try {
                     if (var1.isCancelled()) {
                        break label57;
                     }

                     if (var13.getConnection() != null) {
                        break label45;
                     }
                  } catch (TimeoutException var10) {
                     var10001 = false;
                     throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
                  }

                  var5 = false;
                  break label46;
               }

               var5 = true;
            }

            try {
               Asserts.check(var5, "Pool entry with no connection");
               if (this.log.isDebugEnabled()) {
                  Log var12 = this.log;
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Connection leased: ");
                  var6.append(this.format(var13));
                  var6.append(this.formatStats((HttpRoute)var13.getRoute()));
                  var12.debug(var6.toString());
               }
            } catch (TimeoutException var9) {
               var10001 = false;
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }

            try {
               return CPoolProxy.newProxy(var13);
            } catch (TimeoutException var7) {
               var10001 = false;
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }
         }
      }

      try {
         throw new ExecutionException(new CancellationException("Operation cancelled"));
      } catch (TimeoutException var8) {
         var10001 = false;
         throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
      }
   }

   public void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5) {
      Args.notNull(var1, "Managed connection");
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      Throwable var285;
      label2367: {
         CPoolEntry var8;
         try {
            var8 = CPoolProxy.detach(var1);
         } catch (Throwable var282) {
            var10000 = var282;
            var10001 = false;
            break label2367;
         }

         if (var8 == null) {
            label2319:
            try {
               return;
            } catch (Throwable var271) {
               var10000 = var271;
               var10001 = false;
               break label2319;
            }
         } else {
            label2371: {
               ManagedHttpClientConnection var9;
               try {
                  var9 = (ManagedHttpClientConnection)var8.getConnection();
               } catch (Throwable var281) {
                  var10000 = var281;
                  var10001 = false;
                  break label2371;
               }

               boolean var7 = true;
               boolean var6 = true;

               label2372: {
                  Log var288;
                  label2373: {
                     try {
                        if (!var9.isOpen()) {
                           break label2372;
                        }
                     } catch (Throwable var280) {
                        var10000 = var280;
                        var10001 = false;
                        break label2373;
                     }

                     if (var5 == null) {
                        try {
                           var5 = TimeUnit.MILLISECONDS;
                        } catch (Throwable var279) {
                           var10000 = var279;
                           var10001 = false;
                           break label2373;
                        }
                     }

                     label2374: {
                        try {
                           var8.setState(var2);
                           var8.updateExpiry(var3, var5);
                           if (!this.log.isDebugEnabled()) {
                              break label2374;
                           }
                        } catch (Throwable var278) {
                           var10000 = var278;
                           var10001 = false;
                           break label2373;
                        }

                        String var284;
                        if (var3 > 0L) {
                           try {
                              StringBuilder var283 = new StringBuilder();
                              var283.append("for ");
                              var283.append((double)var5.toMillis(var3) / 1000.0D);
                              var283.append(" seconds");
                              var284 = var283.toString();
                           } catch (Throwable var277) {
                              var10000 = var277;
                              var10001 = false;
                              break label2373;
                           }
                        } else {
                           var284 = "indefinitely";
                        }

                        try {
                           var288 = this.log;
                           StringBuilder var10 = new StringBuilder();
                           var10.append("Connection ");
                           var10.append(this.format(var8));
                           var10.append(" can be kept alive ");
                           var10.append(var284);
                           var288.debug(var10.toString());
                        } catch (Throwable var276) {
                           var10000 = var276;
                           var10001 = false;
                           break label2373;
                        }
                     }

                     label2339:
                     try {
                        var9.setSocketTimeout(0);
                        break label2372;
                     } catch (Throwable var275) {
                        var10000 = var275;
                        var10001 = false;
                        break label2339;
                     }
                  }

                  var285 = var10000;

                  CPool var289;
                  label2315: {
                     label2314: {
                        try {
                           var289 = this.pool;
                           if (!var9.isOpen() || !var8.isRouteComplete()) {
                              break label2314;
                           }
                        } catch (Throwable var270) {
                           var10000 = var270;
                           var10001 = false;
                           break label2371;
                        }

                        var6 = var7;
                        break label2315;
                     }

                     var6 = false;
                  }

                  try {
                     var289.release(var8, var6);
                     if (this.log.isDebugEnabled()) {
                        var288 = this.log;
                        StringBuilder var291 = new StringBuilder();
                        var291.append("Connection released: ");
                        var291.append(this.format(var8));
                        var291.append(this.formatStats((HttpRoute)var8.getRoute()));
                        var288.debug(var291.toString());
                     }
                  } catch (Throwable var269) {
                     var10000 = var269;
                     var10001 = false;
                     break label2371;
                  }

                  try {
                     throw var285;
                  } catch (Throwable var268) {
                     var10000 = var268;
                     var10001 = false;
                     break label2371;
                  }
               }

               CPool var286;
               label2375: {
                  label2331:
                  try {
                     var286 = this.pool;
                     if (!var9.isOpen() || !var8.isRouteComplete()) {
                        break label2331;
                     }
                     break label2375;
                  } catch (Throwable var274) {
                     var10000 = var274;
                     var10001 = false;
                     break label2371;
                  }

                  var6 = false;
               }

               try {
                  var286.release(var8, var6);
                  if (this.log.isDebugEnabled()) {
                     Log var287 = this.log;
                     StringBuilder var290 = new StringBuilder();
                     var290.append("Connection released: ");
                     var290.append(this.format(var8));
                     var290.append(this.formatStats((HttpRoute)var8.getRoute()));
                     var287.debug(var290.toString());
                  }
               } catch (Throwable var273) {
                  var10000 = var273;
                  var10001 = false;
                  break label2371;
               }

               label2321:
               try {
                  return;
               } catch (Throwable var272) {
                  var10000 = var272;
                  var10001 = false;
                  break label2321;
               }
            }
         }
      }

      while(true) {
         var285 = var10000;

         try {
            throw var285;
         } catch (Throwable var267) {
            var10000 = var267;
            var10001 = false;
            continue;
         }
      }
   }

   public ConnectionRequest requestConnection(final HttpRoute var1, Object var2) {
      Args.notNull(var1, "HTTP route");
      if (this.log.isDebugEnabled()) {
         Log var3 = this.log;
         StringBuilder var4 = new StringBuilder();
         var4.append("Connection request: ");
         var4.append(this.format(var1, var2));
         var4.append(this.formatStats(var1));
         var3.debug(var4.toString());
      }

      return new ConnectionRequest(this.pool.lease(var1, var2, (FutureCallback)null)) {
         // $FF: synthetic field
         final Future val$future;

         {
            this.val$future = var2;
         }

         public boolean cancel() {
            return this.val$future.cancel(true);
         }

         public HttpClientConnection get(long var1x, TimeUnit var3) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
            HttpClientConnection var4 = PoolingHttpClientConnectionManager.this.leaseConnection(this.val$future, var1x, var3);
            if (var4.isOpen()) {
               HttpHost var5;
               if (var1.getProxyHost() != null) {
                  var5 = var1.getProxyHost();
               } else {
                  var5 = var1.getTargetHost();
               }

               var4.setSocketTimeout(PoolingHttpClientConnectionManager.this.resolveSocketConfig(var5).getSoTimeout());
            }

            return var4;
         }
      };
   }

   public void routeComplete(HttpClientConnection param1, HttpRoute param2, HttpContext param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void setConnectionConfig(HttpHost var1, ConnectionConfig var2) {
      this.configData.setConnectionConfig(var1, var2);
   }

   public void setDefaultConnectionConfig(ConnectionConfig var1) {
      this.configData.setDefaultConnectionConfig(var1);
   }

   public void setDefaultMaxPerRoute(int var1) {
      this.pool.setDefaultMaxPerRoute(var1);
   }

   public void setDefaultSocketConfig(SocketConfig var1) {
      this.configData.setDefaultSocketConfig(var1);
   }

   public void setMaxPerRoute(HttpRoute var1, int var2) {
      this.pool.setMaxPerRoute(var1, var2);
   }

   public void setMaxTotal(int var1) {
      this.pool.setMaxTotal(var1);
   }

   public void setSocketConfig(HttpHost var1, SocketConfig var2) {
      this.configData.setSocketConfig(var1, var2);
   }

   public void setValidateAfterInactivity(int var1) {
      this.pool.setValidateAfterInactivity(var1);
   }

   public void shutdown() {
      if (this.isShutDown.compareAndSet(false, true)) {
         this.log.debug("Connection manager is shutting down");

         try {
            this.pool.shutdown();
         } catch (IOException var2) {
            this.log.debug("I/O exception shutting down connection manager", var2);
         }

         this.log.debug("Connection manager shut down");
      }

   }

   public void upgrade(HttpClientConnection param1, HttpRoute param2, HttpContext param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   static class ConfigData {
      private final Map connectionConfigMap = new ConcurrentHashMap();
      private volatile ConnectionConfig defaultConnectionConfig;
      private volatile SocketConfig defaultSocketConfig;
      private final Map socketConfigMap = new ConcurrentHashMap();

      public ConnectionConfig getConnectionConfig(HttpHost var1) {
         return (ConnectionConfig)this.connectionConfigMap.get(var1);
      }

      public ConnectionConfig getDefaultConnectionConfig() {
         return this.defaultConnectionConfig;
      }

      public SocketConfig getDefaultSocketConfig() {
         return this.defaultSocketConfig;
      }

      public SocketConfig getSocketConfig(HttpHost var1) {
         return (SocketConfig)this.socketConfigMap.get(var1);
      }

      public void setConnectionConfig(HttpHost var1, ConnectionConfig var2) {
         this.connectionConfigMap.put(var1, var2);
      }

      public void setDefaultConnectionConfig(ConnectionConfig var1) {
         this.defaultConnectionConfig = var1;
      }

      public void setDefaultSocketConfig(SocketConfig var1) {
         this.defaultSocketConfig = var1;
      }

      public void setSocketConfig(HttpHost var1, SocketConfig var2) {
         this.socketConfigMap.put(var1, var2);
      }
   }

   static class InternalConnectionFactory implements ConnFactory {
      private final PoolingHttpClientConnectionManager.ConfigData configData;
      private final HttpConnectionFactory connFactory;

      InternalConnectionFactory(PoolingHttpClientConnectionManager.ConfigData var1, HttpConnectionFactory var2) {
         if (var1 == null) {
            var1 = new PoolingHttpClientConnectionManager.ConfigData();
         }

         this.configData = var1;
         if (var2 == null) {
            var2 = ManagedHttpClientConnectionFactory.INSTANCE;
         }

         this.connFactory = (HttpConnectionFactory)var2;
      }

      public ManagedHttpClientConnection create(HttpRoute var1) throws IOException {
         ConnectionConfig var3 = null;
         if (var1.getProxyHost() != null) {
            var3 = this.configData.getConnectionConfig(var1.getProxyHost());
         }

         ConnectionConfig var2 = var3;
         if (var3 == null) {
            var2 = this.configData.getConnectionConfig(var1.getTargetHost());
         }

         var3 = var2;
         if (var2 == null) {
            var3 = this.configData.getDefaultConnectionConfig();
         }

         var2 = var3;
         if (var3 == null) {
            var2 = ConnectionConfig.DEFAULT;
         }

         return (ManagedHttpClientConnection)this.connFactory.create(var1, var2);
      }
   }
}
