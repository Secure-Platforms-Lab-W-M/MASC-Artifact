package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class ThreadSafeClientConnManager implements ClientConnectionManager {
   protected final ClientConnectionOperator connOperator;
   protected final ConnPerRouteBean connPerRoute;
   protected final AbstractConnPool connectionPool;
   private final Log log;
   protected final ConnPoolByRoute pool;
   protected final SchemeRegistry schemeRegistry;

   public ThreadSafeClientConnManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   public ThreadSafeClientConnManager(SchemeRegistry var1) {
      this(var1, -1L, TimeUnit.MILLISECONDS);
   }

   public ThreadSafeClientConnManager(SchemeRegistry var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, new ConnPerRouteBean());
   }

   public ThreadSafeClientConnManager(SchemeRegistry var1, long var2, TimeUnit var4, ConnPerRouteBean var5) {
      Args.notNull(var1, "Scheme registry");
      this.log = LogFactory.getLog(this.getClass());
      this.schemeRegistry = var1;
      this.connPerRoute = var5;
      this.connOperator = this.createConnectionOperator(var1);
      ConnPoolByRoute var6 = this.createConnectionPool(var2, var4);
      this.pool = var6;
      this.connectionPool = var6;
   }

   @Deprecated
   public ThreadSafeClientConnManager(HttpParams var1, SchemeRegistry var2) {
      Args.notNull(var2, "Scheme registry");
      this.log = LogFactory.getLog(this.getClass());
      this.schemeRegistry = var2;
      this.connPerRoute = new ConnPerRouteBean();
      this.connOperator = this.createConnectionOperator(var2);
      ConnPoolByRoute var3 = (ConnPoolByRoute)this.createConnectionPool(var1);
      this.pool = var3;
      this.connectionPool = var3;
   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      this.pool.closeExpiredConnections();
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

      this.pool.closeIdleConnections(var1, var3);
   }

   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry var1) {
      return new DefaultClientConnectionOperator(var1);
   }

   @Deprecated
   protected AbstractConnPool createConnectionPool(HttpParams var1) {
      return new ConnPoolByRoute(this.connOperator, var1);
   }

   protected ConnPoolByRoute createConnectionPool(long var1, TimeUnit var3) {
      return new ConnPoolByRoute(this.connOperator, this.connPerRoute, 20, var1, var3);
   }

   protected void finalize() throws Throwable {
      try {
         this.shutdown();
      } finally {
         super.finalize();
      }

   }

   public int getConnectionsInPool() {
      return this.pool.getConnectionsInPool();
   }

   public int getConnectionsInPool(HttpRoute var1) {
      return this.pool.getConnectionsInPool(var1);
   }

   public int getDefaultMaxPerRoute() {
      return this.connPerRoute.getDefaultMaxPerRoute();
   }

   public int getMaxForRoute(HttpRoute var1) {
      return this.connPerRoute.getMaxForRoute(var1);
   }

   public int getMaxTotal() {
      return this.pool.getMaxTotalConnections();
   }

   public SchemeRegistry getSchemeRegistry() {
      return this.schemeRegistry;
   }

   public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
      Args.check(var1 instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
      BasicPooledConnAdapter var6 = (BasicPooledConnAdapter)var1;
      boolean var5;
      if (var6.getPoolEntry() != null) {
         if (var6.getManager() == this) {
            var5 = true;
         } else {
            var5 = false;
         }

         Asserts.check(var5, "Connection not obtained from this manager");
      }

      synchronized(var6){}

      Throwable var10000;
      boolean var10001;
      Throwable var409;
      label2874: {
         BasicPoolEntry var7;
         try {
            var7 = (BasicPoolEntry)var6.getPoolEntry();
         } catch (Throwable var407) {
            var10000 = var407;
            var10001 = false;
            break label2874;
         }

         if (var7 == null) {
            label2829:
            try {
               return;
            } catch (Throwable var393) {
               var10000 = var393;
               var10001 = false;
               break label2829;
            }
         } else {
            label2880: {
               ConnPoolByRoute var410;
               label2881: {
                  label2882: {
                     label2883: {
                        IOException var408;
                        try {
                           try {
                              if (var6.isOpen() && !var6.isMarkedReusable()) {
                                 var6.shutdown();
                              }
                              break label2882;
                           } catch (IOException var405) {
                              var408 = var405;
                           }
                        } catch (Throwable var406) {
                           var10000 = var406;
                           var10001 = false;
                           break label2883;
                        }

                        try {
                           if (this.log.isDebugEnabled()) {
                              this.log.debug("Exception shutting down released connection.", var408);
                           }
                        } catch (Throwable var404) {
                           var10000 = var404;
                           var10001 = false;
                           break label2883;
                        }

                        label2856: {
                           try {
                              var5 = var6.isMarkedReusable();
                              if (!this.log.isDebugEnabled()) {
                                 break label2856;
                              }
                           } catch (Throwable var403) {
                              var10000 = var403;
                              var10001 = false;
                              break label2880;
                           }

                           if (var5) {
                              try {
                                 this.log.debug("Released connection is reusable.");
                              } catch (Throwable var398) {
                                 var10000 = var398;
                                 var10001 = false;
                                 break label2880;
                              }
                           } else {
                              try {
                                 this.log.debug("Released connection is not reusable.");
                              } catch (Throwable var397) {
                                 var10000 = var397;
                                 var10001 = false;
                                 break label2880;
                              }
                           }
                        }

                        try {
                           var6.detach();
                           var410 = this.pool;
                           break label2881;
                        } catch (Throwable var396) {
                           var10000 = var396;
                           var10001 = false;
                           break label2880;
                        }
                     }

                     var409 = var10000;

                     label2825: {
                        try {
                           var5 = var6.isMarkedReusable();
                           if (!this.log.isDebugEnabled()) {
                              break label2825;
                           }
                        } catch (Throwable var392) {
                           var10000 = var392;
                           var10001 = false;
                           break label2880;
                        }

                        if (var5) {
                           try {
                              this.log.debug("Released connection is reusable.");
                           } catch (Throwable var391) {
                              var10000 = var391;
                              var10001 = false;
                              break label2880;
                           }
                        } else {
                           try {
                              this.log.debug("Released connection is not reusable.");
                           } catch (Throwable var390) {
                              var10000 = var390;
                              var10001 = false;
                              break label2880;
                           }
                        }
                     }

                     try {
                        var6.detach();
                        this.pool.freeEntry(var7, var5, var2, var4);
                        throw var409;
                     } catch (Throwable var389) {
                        var10000 = var389;
                        var10001 = false;
                        break label2880;
                     }
                  }

                  label2851: {
                     try {
                        var5 = var6.isMarkedReusable();
                        if (!this.log.isDebugEnabled()) {
                           break label2851;
                        }
                     } catch (Throwable var402) {
                        var10000 = var402;
                        var10001 = false;
                        break label2880;
                     }

                     if (var5) {
                        try {
                           this.log.debug("Released connection is reusable.");
                        } catch (Throwable var401) {
                           var10000 = var401;
                           var10001 = false;
                           break label2880;
                        }
                     } else {
                        try {
                           this.log.debug("Released connection is not reusable.");
                        } catch (Throwable var400) {
                           var10000 = var400;
                           var10001 = false;
                           break label2880;
                        }
                     }
                  }

                  try {
                     var6.detach();
                     var410 = this.pool;
                  } catch (Throwable var399) {
                     var10000 = var399;
                     var10001 = false;
                     break label2880;
                  }
               }

               try {
                  var410.freeEntry(var7, var5, var2, var4);
               } catch (Throwable var395) {
                  var10000 = var395;
                  var10001 = false;
                  break label2880;
               }

               label2831:
               try {
                  return;
               } catch (Throwable var394) {
                  var10000 = var394;
                  var10001 = false;
                  break label2831;
               }
            }
         }
      }

      while(true) {
         var409 = var10000;

         try {
            throw var409;
         } catch (Throwable var388) {
            var10000 = var388;
            var10001 = false;
            continue;
         }
      }
   }

   public ClientConnectionRequest requestConnection(final HttpRoute var1, Object var2) {
      return new ClientConnectionRequest(this.pool.requestPoolEntry(var1, var2)) {
         // $FF: synthetic field
         final PoolEntryRequest val$poolRequest;

         {
            this.val$poolRequest = var2;
         }

         public void abortRequest() {
            this.val$poolRequest.abortRequest();
         }

         public ManagedClientConnection getConnection(long var1x, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException {
            Args.notNull(var1, "Route");
            if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
               Log var4 = ThreadSafeClientConnManager.this.log;
               StringBuilder var5 = new StringBuilder();
               var5.append("Get connection: ");
               var5.append(var1);
               var5.append(", timeout = ");
               var5.append(var1x);
               var4.debug(var5.toString());
            }

            BasicPoolEntry var6 = this.val$poolRequest.getPoolEntry(var1x, var3);
            return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, var6);
         }
      };
   }

   public void setDefaultMaxPerRoute(int var1) {
      this.connPerRoute.setDefaultMaxPerRoute(var1);
   }

   public void setMaxForRoute(HttpRoute var1, int var2) {
      this.connPerRoute.setMaxForRoute(var1, var2);
   }

   public void setMaxTotal(int var1) {
      this.pool.setMaxTotalConnections(var1);
   }

   public void shutdown() {
      this.log.debug("Shutting down");
      this.pool.shutdown();
   }
}
