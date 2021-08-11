package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;

public interface RouteInfo {
   int getHopCount();

   HttpHost getHopTarget(int var1);

   RouteInfo.LayerType getLayerType();

   InetAddress getLocalAddress();

   HttpHost getProxyHost();

   HttpHost getTargetHost();

   RouteInfo.TunnelType getTunnelType();

   boolean isLayered();

   boolean isSecure();

   boolean isTunnelled();

   public static enum LayerType {
      LAYERED,
      PLAIN;

      static {
         RouteInfo.LayerType var0 = new RouteInfo.LayerType("LAYERED", 1);
         LAYERED = var0;
      }
   }

   public static enum TunnelType {
      PLAIN,
      TUNNELLED;

      static {
         RouteInfo.TunnelType var0 = new RouteInfo.TunnelType("TUNNELLED", 1);
         TUNNELLED = var0;
      }
   }
}
