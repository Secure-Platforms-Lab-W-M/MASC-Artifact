package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class BasicClientConnectionManager implements ClientConnectionManager {
   private static final AtomicLong COUNTER = new AtomicLong();
   public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
   private ManagedClientConnectionImpl conn;
   private final ClientConnectionOperator connOperator;
   private final Log log;
   private HttpPoolEntry poolEntry;
   private final SchemeRegistry schemeRegistry;
   private volatile boolean shutdown;

   public BasicClientConnectionManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   public BasicClientConnectionManager(SchemeRegistry var1) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.connOperator = this.createConnectionOperator(var1);
   }

   private void assertNotShutdown() {
      Asserts.check(this.shutdown ^ true, "Connection manager has been shut down");
   }

   private void shutdownConnection(HttpClientConnection var1) {
      try {
         var1.shutdown();
      } catch (IOException var2) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("I/O exception shutting down connection", var2);
         }

      }
   }

   public void closeExpiredConnections() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            this.assertNotShutdown();
            long var1 = System.currentTimeMillis();
            if (this.poolEntry != null && this.poolEntry.isExpired(var1)) {
               this.poolEntry.close();
               this.poolEntry.getTracker().reset();
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label216: {
         long var4;
         try {
            this.assertNotShutdown();
            var4 = var3.toMillis(var1);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label216;
         }

         var1 = var4;
         if (var4 < 0L) {
            var1 = 0L;
         }

         try {
            var4 = System.currentTimeMillis();
            if (this.poolEntry != null && this.poolEntry.getUpdated() <= var4 - var1) {
               this.poolEntry.close();
               this.poolEntry.getTracker().reset();
            }
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label216;
         }

         label205:
         try {
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label205;
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
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

   ManagedClientConnection getConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "Route");
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label553: {
         try {
            this.assertNotShutdown();
            if (this.log.isDebugEnabled()) {
               Log var65 = this.log;
               StringBuilder var6 = new StringBuilder();
               var6.append("Get connection for route ");
               var6.append(var1);
               var65.debug(var6.toString());
            }
         } catch (Throwable var62) {
            var10000 = var62;
            var10001 = false;
            break label553;
         }

         boolean var3;
         label545: {
            label544: {
               try {
                  if (this.conn != null) {
                     break label544;
                  }
               } catch (Throwable var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label553;
               }

               var3 = true;
               break label545;
            }

            var3 = false;
         }

         try {
            Asserts.check(var3, "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
            if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(var1)) {
               this.poolEntry.close();
               this.poolEntry = null;
            }
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label553;
         }

         try {
            if (this.poolEntry == null) {
               String var66 = Long.toString(COUNTER.getAndIncrement());
               OperatedClientConnection var67 = this.connOperator.createConnection();
               this.poolEntry = new HttpPoolEntry(this.log, var66, var1, var67, 0L, TimeUnit.MILLISECONDS);
            }
         } catch (Throwable var59) {
            var10000 = var59;
            var10001 = false;
            break label553;
         }

         try {
            long var4 = System.currentTimeMillis();
            if (this.poolEntry.isExpired(var4)) {
               this.poolEntry.close();
               this.poolEntry.getTracker().reset();
            }
         } catch (Throwable var58) {
            var10000 = var58;
            var10001 = false;
            break label553;
         }

         label529:
         try {
            ManagedClientConnectionImpl var64 = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
            this.conn = var64;
            return var64;
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label529;
         }
      }

      while(true) {
         Throwable var63 = var10000;

         try {
            throw var63;
         } catch (Throwable var56) {
            var10000 = var56;
            var10001 = false;
            continue;
         }
      }
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
            return BasicClientConnectionManager.this.getConnection(var1, var2);
         }
      };
   }

   public void shutdown() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label242: {
         try {
            this.shutdown = true;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label242;
         }

         try {
            if (this.poolEntry != null) {
               this.poolEntry.close();
            }

            return;
         } finally {
            label236:
            try {
               this.poolEntry = null;
               this.conn = null;
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label236;
            }
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            continue;
         }
      }
   }
}
