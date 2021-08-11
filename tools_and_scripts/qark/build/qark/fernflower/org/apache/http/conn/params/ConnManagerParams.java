package org.apache.http.conn.params;

import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public final class ConnManagerParams implements ConnManagerPNames {
   private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute() {
      public int getMaxForRoute(HttpRoute var1) {
         return 2;
      }
   };
   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;

   public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      ConnPerRoute var1 = (ConnPerRoute)var0.getParameter("http.conn-manager.max-per-route");
      ConnPerRoute var2 = var1;
      if (var1 == null) {
         var2 = DEFAULT_CONN_PER_ROUTE;
      }

      return var2;
   }

   public static int getMaxTotalConnections(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.conn-manager.max-total", 20);
   }

   @Deprecated
   public static long getTimeout(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getLongParameter("http.conn-manager.timeout", 0L);
   }

   public static void setMaxConnectionsPerRoute(HttpParams var0, ConnPerRoute var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.conn-manager.max-per-route", var1);
   }

   public static void setMaxTotalConnections(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.conn-manager.max-total", var1);
   }

   @Deprecated
   public static void setTimeout(HttpParams var0, long var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setLongParameter("http.conn-manager.timeout", var1);
   }
}
