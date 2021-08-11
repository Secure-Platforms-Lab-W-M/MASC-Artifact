package okhttp3.internal.connection;

import java.util.LinkedHashSet;
import java.util.Set;
import okhttp3.Route;

public final class RouteDatabase {
   private final Set failedRoutes = new LinkedHashSet();

   public void connected(Route var1) {
      synchronized(this){}

      try {
         this.failedRoutes.remove(var1);
      } finally {
         ;
      }

   }

   public void failed(Route var1) {
      synchronized(this){}

      try {
         this.failedRoutes.add(var1);
      } finally {
         ;
      }

   }

   public boolean shouldPostpone(Route var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.failedRoutes.contains(var1);
      } finally {
         ;
      }

      return var2;
   }
}
