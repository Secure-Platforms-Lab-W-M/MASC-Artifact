package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.util.Args;

@Deprecated
public class BasicPoolEntry extends AbstractPoolEntry {
   private final long created;
   private long expiry;
   private long updated;
   private final long validUntil;

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2) {
      this(var1, var2, -1L, TimeUnit.MILLISECONDS);
   }

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2, long var3, TimeUnit var5) {
      super(var1, var2);
      Args.notNull(var2, "HTTP route");
      long var6 = System.currentTimeMillis();
      this.created = var6;
      if (var3 > 0L) {
         this.validUntil = var6 + var5.toMillis(var3);
      } else {
         this.validUntil = Long.MAX_VALUE;
      }

      this.expiry = this.validUntil;
   }

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2, ReferenceQueue var3) {
      super(var1, var2);
      Args.notNull(var2, "HTTP route");
      this.created = System.currentTimeMillis();
      this.validUntil = Long.MAX_VALUE;
      this.expiry = Long.MAX_VALUE;
   }

   protected final OperatedClientConnection getConnection() {
      return super.connection;
   }

   public long getCreated() {
      return this.created;
   }

   public long getExpiry() {
      return this.expiry;
   }

   protected final HttpRoute getPlannedRoute() {
      return super.route;
   }

   public long getUpdated() {
      return this.updated;
   }

   public long getValidUntil() {
      return this.validUntil;
   }

   protected final BasicPoolEntryRef getWeakRef() {
      return null;
   }

   public boolean isExpired(long var1) {
      return var1 >= this.expiry;
   }

   protected void shutdownEntry() {
      super.shutdownEntry();
   }

   public void updateExpiry(long var1, TimeUnit var3) {
      long var4 = System.currentTimeMillis();
      this.updated = var4;
      if (var1 > 0L) {
         var1 = var4 + var3.toMillis(var1);
      } else {
         var1 = Long.MAX_VALUE;
      }

      this.expiry = Math.min(this.validUntil, var1);
   }
}
