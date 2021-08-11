package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class ConnRouteParams implements ConnRoutePNames {
   public static final HttpHost NO_HOST;
   public static final HttpRoute NO_ROUTE;

   static {
      HttpHost var0 = new HttpHost("127.0.0.255", 0, "no-host");
      NO_HOST = var0;
      NO_ROUTE = new HttpRoute(var0);
   }

   private ConnRouteParams() {
   }

   public static HttpHost getDefaultProxy(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      HttpHost var1 = (HttpHost)var0.getParameter("http.route.default-proxy");
      HttpHost var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (NO_HOST.equals(var1)) {
            var2 = null;
         }
      }

      return var2;
   }

   public static HttpRoute getForcedRoute(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      HttpRoute var1 = (HttpRoute)var0.getParameter("http.route.forced-route");
      HttpRoute var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (NO_ROUTE.equals(var1)) {
            var2 = null;
         }
      }

      return var2;
   }

   public static InetAddress getLocalAddress(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      return (InetAddress)var0.getParameter("http.route.local-address");
   }

   public static void setDefaultProxy(HttpParams var0, HttpHost var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.default-proxy", var1);
   }

   public static void setForcedRoute(HttpParams var0, HttpRoute var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.forced-route", var1);
   }

   public static void setLocalAddress(HttpParams var0, InetAddress var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.local-address", var1);
   }
}
