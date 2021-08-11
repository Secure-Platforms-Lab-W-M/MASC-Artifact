package org.apache.http.conn.routing;

import org.apache.http.util.Args;

public class BasicRouteDirector implements HttpRouteDirector {
   protected int directStep(RouteInfo var1, RouteInfo var2) {
      if (var2.getHopCount() > 1) {
         return -1;
      } else if (!var1.getTargetHost().equals(var2.getTargetHost())) {
         return -1;
      } else if (var1.isSecure() != var2.isSecure()) {
         return -1;
      } else {
         return var1.getLocalAddress() != null && !var1.getLocalAddress().equals(var2.getLocalAddress()) ? -1 : 0;
      }
   }

   protected int firstStep(RouteInfo var1) {
      int var3 = var1.getHopCount();
      byte var2 = 1;
      if (var3 > 1) {
         var2 = 2;
      }

      return var2;
   }

   public int nextStep(RouteInfo var1, RouteInfo var2) {
      Args.notNull(var1, "Planned route");
      if (var2 != null && var2.getHopCount() >= 1) {
         return var1.getHopCount() > 1 ? this.proxiedStep(var1, var2) : this.directStep(var1, var2);
      } else {
         return this.firstStep(var1);
      }
   }

   protected int proxiedStep(RouteInfo var1, RouteInfo var2) {
      if (var2.getHopCount() <= 1) {
         return -1;
      } else if (!var1.getTargetHost().equals(var2.getTargetHost())) {
         return -1;
      } else {
         int var4 = var1.getHopCount();
         int var5 = var2.getHopCount();
         if (var4 < var5) {
            return -1;
         } else {
            for(int var3 = 0; var3 < var5 - 1; ++var3) {
               if (!var1.getHopTarget(var3).equals(var2.getHopTarget(var3))) {
                  return -1;
               }
            }

            if (var4 > var5) {
               return 4;
            } else if ((!var2.isTunnelled() || var1.isTunnelled()) && (!var2.isLayered() || var1.isLayered())) {
               if (var1.isTunnelled() && !var2.isTunnelled()) {
                  return 3;
               } else if (var1.isLayered() && !var2.isLayered()) {
                  return 5;
               } else {
                  return var1.isSecure() != var2.isSecure() ? -1 : 0;
               }
            } else {
               return -1;
            }
         }
      }
   }
}
