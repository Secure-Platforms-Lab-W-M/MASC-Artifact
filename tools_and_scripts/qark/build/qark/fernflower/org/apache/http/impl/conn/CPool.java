package org.apache.http.impl.conn;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.PoolEntryCallback;

class CPool extends AbstractConnPool {
   private static final AtomicLong COUNTER = new AtomicLong();
   private final Log log = LogFactory.getLog(CPool.class);
   private final long timeToLive;
   private final TimeUnit timeUnit;

   public CPool(ConnFactory var1, int var2, int var3, long var4, TimeUnit var6) {
      super(var1, var2, var3);
      this.timeToLive = var4;
      this.timeUnit = var6;
   }

   protected CPoolEntry createEntry(HttpRoute var1, ManagedHttpClientConnection var2) {
      String var3 = Long.toString(COUNTER.getAndIncrement());
      return new CPoolEntry(this.log, var3, var1, var2, this.timeToLive, this.timeUnit);
   }

   protected void enumAvailable(PoolEntryCallback var1) {
      super.enumAvailable(var1);
   }

   protected void enumLeased(PoolEntryCallback var1) {
      super.enumLeased(var1);
   }

   protected boolean validate(CPoolEntry var1) {
      return ((ManagedHttpClientConnection)var1.getConnection()).isStale() ^ true;
   }
}
