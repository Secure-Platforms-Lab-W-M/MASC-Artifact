package org.apache.http.conn.routing;

public interface HttpRouteDirector {
   int COMPLETE = 0;
   int CONNECT_PROXY = 2;
   int CONNECT_TARGET = 1;
   int LAYER_PROTOCOL = 5;
   int TUNNEL_PROXY = 4;
   int TUNNEL_TARGET = 3;
   int UNREACHABLE = -1;

   int nextStep(RouteInfo var1, RouteInfo var2);
}
