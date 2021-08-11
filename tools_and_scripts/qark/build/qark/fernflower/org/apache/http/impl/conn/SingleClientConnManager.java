package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class SingleClientConnManager implements ClientConnectionManager {
   public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
   protected final boolean alwaysShutDown;
   protected final ClientConnectionOperator connOperator;
   protected volatile long connectionExpiresTime;
   protected volatile boolean isShutDown;
   protected volatile long lastReleaseTime;
   private final Log log;
   protected volatile SingleClientConnManager.ConnAdapter managedConn;
   protected final SchemeRegistry schemeRegistry;
   protected volatile SingleClientConnManager.PoolEntry uniquePoolEntry;

   public SingleClientConnManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   public SingleClientConnManager(SchemeRegistry var1) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.connOperator = this.createConnectionOperator(var1);
      this.uniquePoolEntry = new SingleClientConnManager.PoolEntry();
      this.managedConn = null;
      this.lastReleaseTime = -1L;
      this.alwaysShutDown = false;
      this.isShutDown = false;
   }

   @Deprecated
   public SingleClientConnManager(HttpParams var1, SchemeRegistry var2) {
      this(var2);
   }

   protected final void assertStillUp() throws IllegalStateException {
      Asserts.check(this.isShutDown ^ true, "Manager is shut down");
   }

   public void closeExpiredConnections() {
      long var1 = this.connectionExpiresTime;
      if (System.currentTimeMillis() >= var1) {
         this.closeIdleConnections(0L, TimeUnit.MILLISECONDS);
      }

   }

   public void closeIdleConnections(long param1, TimeUnit param3) {
      // $FF: Couldn't be decompiled
   }

   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry var1) {
      return new DefaultClientConnectionOperator(var1);
   }

   protected void finalize() throws Throwable {
      try {
         this.shutdown();
      } finally {
         super.finalize();
      }

   }

   public ManagedClientConnection getConnection(HttpRoute param1, Object param2) {
      // $FF: Couldn't be decompiled
   }

   public SchemeRegistry getSchemeRegistry() {
      return this.schemeRegistry;
   }

   public void releaseConnection(ManagedClientConnection param1, long param2, TimeUnit param4) {
      // $FF: Couldn't be decompiled
   }

   public final ClientConnectionRequest requestConnection(final HttpRoute var1, final Object var2) {
      return new ClientConnectionRequest() {
         public void abortRequest() {
         }

         public ManagedClientConnection getConnection(long var1x, TimeUnit var3) {
            return SingleClientConnManager.this.getConnection(var1, var2);
         }
      };
   }

   protected void revokeConnection() {
      SingleClientConnManager.ConnAdapter var1 = this.managedConn;
      if (var1 != null) {
         var1.detach();
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label213: {
            label205: {
               IOException var27;
               try {
                  try {
                     this.uniquePoolEntry.shutdown();
                     break label205;
                  } catch (IOException var25) {
                     var27 = var25;
                  }
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label213;
               }

               try {
                  this.log.debug("Problem while shutting down connection.", var27);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label213;
               }
            }

            label197:
            try {
               return;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label197;
            }
         }

         while(true) {
            Throwable var28 = var10000;

            try {
               throw var28;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void shutdown() {
      this.isShutDown = true;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      Throwable var83;
      label621: {
         label616: {
            label622: {
               label623: {
                  IOException var1;
                  try {
                     try {
                        if (this.uniquePoolEntry != null) {
                           this.uniquePoolEntry.shutdown();
                        }
                        break label622;
                     } catch (IOException var81) {
                        var1 = var81;
                     }
                  } catch (Throwable var82) {
                     var10000 = var82;
                     var10001 = false;
                     break label623;
                  }

                  try {
                     this.log.debug("Problem while shutting down manager.", var1);
                  } catch (Throwable var80) {
                     var10000 = var80;
                     var10001 = false;
                     break label623;
                  }

                  try {
                     this.uniquePoolEntry = null;
                     break label616;
                  } catch (Throwable var78) {
                     var10000 = var78;
                     var10001 = false;
                     break label621;
                  }
               }

               var83 = var10000;

               try {
                  this.uniquePoolEntry = null;
                  this.managedConn = null;
                  throw var83;
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label621;
               }
            }

            try {
               this.uniquePoolEntry = null;
            } catch (Throwable var79) {
               var10000 = var79;
               var10001 = false;
               break label621;
            }
         }

         try {
            this.managedConn = null;
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label621;
         }

         label596:
         try {
            return;
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label596;
         }
      }

      while(true) {
         var83 = var10000;

         try {
            throw var83;
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            continue;
         }
      }
   }

   protected class ConnAdapter extends AbstractPooledConnAdapter {
      protected ConnAdapter(SingleClientConnManager.PoolEntry var2, HttpRoute var3) {
         super(SingleClientConnManager.this, var2);
         this.markReusable();
         var2.route = var3;
      }
   }

   protected class PoolEntry extends AbstractPoolEntry {
      protected PoolEntry() {
         super(SingleClientConnManager.this.connOperator, (HttpRoute)null);
      }

      protected void close() throws IOException {
         this.shutdownEntry();
         if (this.connection.isOpen()) {
            this.connection.close();
         }

      }

      protected void shutdown() throws IOException {
         this.shutdownEntry();
         if (this.connection.isOpen()) {
            this.connection.shutdown();
         }

      }
   }
}
