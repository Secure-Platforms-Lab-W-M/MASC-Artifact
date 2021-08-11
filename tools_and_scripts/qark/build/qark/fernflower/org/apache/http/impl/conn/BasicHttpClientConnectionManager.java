package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

public class BasicHttpClientConnectionManager implements HttpClientConnectionManager, Closeable {
   private ManagedHttpClientConnection conn;
   private ConnectionConfig connConfig;
   private final HttpConnectionFactory connFactory;
   private final HttpClientConnectionOperator connectionOperator;
   private long expiry;
   private final AtomicBoolean isShutdown;
   private boolean leased;
   private final Log log;
   private HttpRoute route;
   private SocketConfig socketConfig;
   private Object state;
   private long updated;

   public BasicHttpClientConnectionManager() {
      this(getDefaultRegistry(), (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null);
   }

   public BasicHttpClientConnectionManager(Lookup var1) {
      this(var1, (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null);
   }

   public BasicHttpClientConnectionManager(Lookup var1, HttpConnectionFactory var2) {
      this(var1, var2, (SchemePortResolver)null, (DnsResolver)null);
   }

   public BasicHttpClientConnectionManager(Lookup var1, HttpConnectionFactory var2, SchemePortResolver var3, DnsResolver var4) {
      this((HttpClientConnectionOperator)(new DefaultHttpClientConnectionOperator(var1, var3, var4)), var2);
   }

   public BasicHttpClientConnectionManager(HttpClientConnectionOperator var1, HttpConnectionFactory var2) {
      this.log = LogFactory.getLog(this.getClass());
      this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(var1, "Connection operator");
      if (var2 == null) {
         var2 = ManagedHttpClientConnectionFactory.INSTANCE;
      }

      this.connFactory = (HttpConnectionFactory)var2;
      this.expiry = Long.MAX_VALUE;
      this.socketConfig = SocketConfig.DEFAULT;
      this.connConfig = ConnectionConfig.DEFAULT;
      this.isShutdown = new AtomicBoolean(false);
   }

   private void checkExpiry() {
      if (this.conn != null && System.currentTimeMillis() >= this.expiry) {
         if (this.log.isDebugEnabled()) {
            Log var1 = this.log;
            StringBuilder var2 = new StringBuilder();
            var2.append("Connection expired @ ");
            var2.append(new Date(this.expiry));
            var1.debug(var2.toString());
         }

         this.closeConnection();
      }

   }

   private void closeConnection() {
      // $FF: Couldn't be decompiled
   }

   private static Registry getDefaultRegistry() {
      return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
   }

   public void close() {
      if (this.isShutdown.compareAndSet(false, true)) {
         this.closeConnection();
      }

   }

   public void closeExpiredConnections() {
      synchronized(this){}

      Throwable var10000;
      label90: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isShutdown.get();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label90;
         }

         if (var1) {
            return;
         }

         label81:
         try {
            if (!this.leased) {
               this.checkExpiry();
            }

            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label81;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      synchronized(this){}

      Throwable var10000;
      label169: {
         boolean var10001;
         boolean var6;
         try {
            Args.notNull(var3, "Time unit");
            var6 = this.isShutdown.get();
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            break label169;
         }

         if (var6) {
            return;
         }

         long var4;
         try {
            if (this.leased) {
               return;
            }

            var4 = var3.toMillis(var1);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label169;
         }

         var1 = var4;
         if (var4 < 0L) {
            var1 = 0L;
         }

         label155:
         try {
            var4 = System.currentTimeMillis();
            if (this.updated <= var4 - var1) {
               this.closeConnection();
            }

            return;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label155;
         }
      }

      Throwable var19 = var10000;
      throw var19;
   }

   public void connect(HttpClientConnection var1, HttpRoute var2, int var3, HttpContext var4) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "HTTP route");
      boolean var5;
      if (var1 == this.conn) {
         var5 = true;
      } else {
         var5 = false;
      }

      Asserts.check(var5, "Connection not obtained from this manager");
      HttpHost var6;
      if (var2.getProxyHost() != null) {
         var6 = var2.getProxyHost();
      } else {
         var6 = var2.getTargetHost();
      }

      InetSocketAddress var7 = var2.getLocalSocketAddress();
      this.connectionOperator.connect(this.conn, var6, var7, var3, this.socketConfig, var4);
   }

   protected void finalize() throws Throwable {
      try {
         this.shutdown();
      } finally {
         super.finalize();
      }

   }

   HttpClientConnection getConnection(HttpRoute var1, Object var2) {
      synchronized(this){}

      Throwable var10000;
      label568: {
         boolean var3;
         boolean var10001;
         try {
            var3 = this.isShutdown.get();
         } catch (Throwable var62) {
            var10000 = var62;
            var10001 = false;
            break label568;
         }

         boolean var4 = false;
         if (!var3) {
            var3 = true;
         } else {
            var3 = false;
         }

         try {
            Asserts.check(var3, "Connection manager has been shut down");
            if (this.log.isDebugEnabled()) {
               Log var5 = this.log;
               StringBuilder var6 = new StringBuilder();
               var6.append("Get connection for route ");
               var6.append(var1);
               var5.debug(var6.toString());
            }
         } catch (Throwable var61) {
            var10000 = var61;
            var10001 = false;
            break label568;
         }

         var3 = var4;

         label554: {
            try {
               if (this.leased) {
                  break label554;
               }
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label568;
            }

            var3 = true;
         }

         label548: {
            try {
               Asserts.check(var3, "Connection is still allocated");
               if (LangUtils.equals(this.route, var1) && LangUtils.equals(this.state, var2)) {
                  break label548;
               }
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label568;
            }

            try {
               this.closeConnection();
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label568;
            }
         }

         try {
            this.route = var1;
            this.state = var2;
            this.checkExpiry();
            if (this.conn == null) {
               this.conn = (ManagedHttpClientConnection)this.connFactory.create(var1, this.connConfig);
            }
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label568;
         }

         label536:
         try {
            this.conn.setSocketTimeout(this.socketConfig.getSoTimeout());
            this.leased = true;
            ManagedHttpClientConnection var64 = this.conn;
            return var64;
         } catch (Throwable var56) {
            var10000 = var56;
            var10001 = false;
            break label536;
         }
      }

      Throwable var63 = var10000;
      throw var63;
   }

   public ConnectionConfig getConnectionConfig() {
      synchronized(this){}

      ConnectionConfig var1;
      try {
         var1 = this.connConfig;
      } finally {
         ;
      }

      return var1;
   }

   HttpRoute getRoute() {
      return this.route;
   }

   public SocketConfig getSocketConfig() {
      synchronized(this){}

      SocketConfig var1;
      try {
         var1 = this.socketConfig;
      } finally {
         ;
      }

      return var1;
   }

   Object getState() {
      return this.state;
   }

   public void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5) {
      synchronized(this){}

      Throwable var10000;
      Throwable var144;
      label1163: {
         boolean var6;
         boolean var10001;
         label1157: {
            label1156: {
               try {
                  Args.notNull(var1, "Connection");
                  if (var1 == this.conn) {
                     break label1156;
                  }
               } catch (Throwable var140) {
                  var10000 = var140;
                  var10001 = false;
                  break label1163;
               }

               var6 = false;
               break label1157;
            }

            var6 = true;
         }

         try {
            Asserts.check(var6, "Connection not obtained from this manager");
            if (this.log.isDebugEnabled()) {
               Log var7 = this.log;
               StringBuilder var8 = new StringBuilder();
               var8.append("Releasing connection ");
               var8.append(var1);
               var7.debug(var8.toString());
            }
         } catch (Throwable var139) {
            var10000 = var139;
            var10001 = false;
            break label1163;
         }

         try {
            var6 = this.isShutdown.get();
         } catch (Throwable var138) {
            var10000 = var138;
            var10001 = false;
            break label1163;
         }

         if (var6) {
            return;
         }

         label1164: {
            label1165: {
               try {
                  this.updated = System.currentTimeMillis();
                  if (!this.conn.isOpen()) {
                     this.conn = null;
                     this.route = null;
                     this.conn = null;
                     this.expiry = Long.MAX_VALUE;
                     break label1164;
                  }
               } catch (Throwable var137) {
                  var10000 = var137;
                  var10001 = false;
                  break label1165;
               }

               label1166: {
                  try {
                     this.state = var2;
                     this.conn.setSocketTimeout(0);
                     if (!this.log.isDebugEnabled()) {
                        break label1166;
                     }
                  } catch (Throwable var136) {
                     var10000 = var136;
                     var10001 = false;
                     break label1165;
                  }

                  String var142;
                  if (var3 > 0L) {
                     try {
                        StringBuilder var141 = new StringBuilder();
                        var141.append("for ");
                        var141.append(var3);
                        var141.append(" ");
                        var141.append(var5);
                        var142 = var141.toString();
                     } catch (Throwable var135) {
                        var10000 = var135;
                        var10001 = false;
                        break label1165;
                     }
                  } else {
                     var142 = "indefinitely";
                  }

                  try {
                     Log var143 = this.log;
                     StringBuilder var145 = new StringBuilder();
                     var145.append("Connection can be kept alive ");
                     var145.append(var142);
                     var143.debug(var145.toString());
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1165;
                  }
               }

               if (var3 > 0L) {
                  label1124:
                  try {
                     this.expiry = this.updated + var5.toMillis(var3);
                  } catch (Throwable var132) {
                     var10000 = var132;
                     var10001 = false;
                     break label1124;
                  }
               } else {
                  label1126:
                  try {
                     this.expiry = Long.MAX_VALUE;
                  } catch (Throwable var133) {
                     var10000 = var133;
                     var10001 = false;
                     break label1126;
                  }
               }
               break label1164;
            }

            var144 = var10000;

            try {
               this.leased = false;
               throw var144;
            } catch (Throwable var130) {
               var10000 = var130;
               var10001 = false;
               break label1163;
            }
         }

         label1119:
         try {
            this.leased = false;
            return;
         } catch (Throwable var131) {
            var10000 = var131;
            var10001 = false;
            break label1119;
         }
      }

      var144 = var10000;
      throw var144;
   }

   public final ConnectionRequest requestConnection(final HttpRoute var1, final Object var2) {
      Args.notNull(var1, "Route");
      return new ConnectionRequest() {
         public boolean cancel() {
            return false;
         }

         public HttpClientConnection get(long var1x, TimeUnit var3) {
            return BasicHttpClientConnectionManager.this.getConnection(var1, var2);
         }
      };
   }

   public void routeComplete(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
   }

   public void setConnectionConfig(ConnectionConfig var1) {
      Throwable var10000;
      label59: {
         synchronized(this){}
         boolean var10001;
         if (var1 == null) {
            try {
               var1 = ConnectionConfig.DEFAULT;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label59;
            }
         }

         label55:
         try {
            this.connConfig = var1;
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label55;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void setSocketConfig(SocketConfig var1) {
      Throwable var10000;
      label59: {
         synchronized(this){}
         boolean var10001;
         if (var1 == null) {
            try {
               var1 = SocketConfig.DEFAULT;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label59;
            }
         }

         label55:
         try {
            this.socketConfig = var1;
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label55;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void shutdown() {
      this.close();
   }

   public void upgrade(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "HTTP route");
      boolean var4;
      if (var1 == this.conn) {
         var4 = true;
      } else {
         var4 = false;
      }

      Asserts.check(var4, "Connection not obtained from this manager");
      this.connectionOperator.upgrade(this.conn, var2.getTargetHost(), var3);
   }
}
