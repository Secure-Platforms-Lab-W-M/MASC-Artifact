package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.IdleConnectionHandler;
import org.apache.http.util.Args;

@Deprecated
public abstract class AbstractConnPool {
   protected IdleConnectionHandler idleConnHandler = new IdleConnectionHandler();
   protected volatile boolean isShutDown;
   protected Set issuedConnections;
   protected Set leasedConnections = new HashSet();
   private final Log log = LogFactory.getLog(this.getClass());
   protected int numConnections;
   protected final Lock poolLock = new ReentrantLock();
   protected ReferenceQueue refQueue;

   protected AbstractConnPool() {
   }

   protected void closeConnection(OperatedClientConnection var1) {
      if (var1 != null) {
         try {
            var1.close();
            return;
         } catch (IOException var2) {
            this.log.debug("I/O error closing connection", var2);
         }
      }

   }

   public void closeExpiredConnections() {
      this.poolLock.lock();

      try {
         this.idleConnHandler.closeExpiredConnections();
      } finally {
         this.poolLock.unlock();
      }

   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      this.poolLock.lock();

      try {
         this.idleConnHandler.closeIdleConnections(var3.toMillis(var1));
      } finally {
         this.poolLock.unlock();
      }

   }

   public abstract void deleteClosedConnections();

   public void enableConnectionGC() throws IllegalStateException {
   }

   public abstract void freeEntry(BasicPoolEntry var1, boolean var2, long var3, TimeUnit var5);

   public final BasicPoolEntry getEntry(HttpRoute var1, Object var2, long var3, TimeUnit var5) throws ConnectionPoolTimeoutException, InterruptedException {
      return this.requestPoolEntry(var1, var2).getPoolEntry(var3, var5);
   }

   protected abstract void handleLostEntry(HttpRoute var1);

   public void handleReference(Reference var1) {
   }

   public abstract PoolEntryRequest requestPoolEntry(HttpRoute var1, Object var2);

   public void shutdown() {
      this.poolLock.lock();

      Throwable var10000;
      label210: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isShutDown;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label210;
         }

         if (var1) {
            this.poolLock.unlock();
            return;
         }

         Iterator var2;
         try {
            var2 = this.leasedConnections.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label210;
         }

         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               BasicPoolEntry var3 = (BasicPoolEntry)var2.next();
               var2.remove();
               this.closeConnection(var3.getConnection());
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label210;
            }
         }

         try {
            this.idleConnHandler.removeAll();
            this.isShutDown = true;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label210;
         }

         this.poolLock.unlock();
         return;
      }

      Throwable var24 = var10000;
      this.poolLock.unlock();
      throw var24;
   }
}
