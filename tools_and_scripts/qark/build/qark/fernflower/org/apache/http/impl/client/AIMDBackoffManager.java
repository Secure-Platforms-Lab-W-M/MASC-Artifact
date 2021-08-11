package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

public class AIMDBackoffManager implements BackoffManager {
   private double backoffFactor;
   private int cap;
   private final Clock clock;
   private final ConnPoolControl connPerRoute;
   private long coolDown;
   private final Map lastRouteBackoffs;
   private final Map lastRouteProbes;

   public AIMDBackoffManager(ConnPoolControl var1) {
      this(var1, new SystemClock());
   }

   AIMDBackoffManager(ConnPoolControl var1, Clock var2) {
      this.coolDown = 5000L;
      this.backoffFactor = 0.5D;
      this.cap = 2;
      this.clock = var2;
      this.connPerRoute = var1;
      this.lastRouteProbes = new HashMap();
      this.lastRouteBackoffs = new HashMap();
   }

   private int getBackedOffPoolSize(int var1) {
      return var1 <= 1 ? 1 : (int)Math.floor(this.backoffFactor * (double)var1);
   }

   private Long getLastUpdate(Map var1, HttpRoute var2) {
      Long var4 = (Long)var1.get(var2);
      Long var3 = var4;
      if (var4 == null) {
         var3 = 0L;
      }

      return var3;
   }

   public void backOff(HttpRoute var1) {
      ConnPoolControl var5 = this.connPerRoute;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label123: {
         int var2;
         long var3;
         try {
            var2 = this.connPerRoute.getMaxPerRoute(var1);
            Long var6 = this.getLastUpdate(this.lastRouteBackoffs, var1);
            var3 = this.clock.getCurrentTime();
            if (var3 - var6 < this.coolDown) {
               return;
            }
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.connPerRoute.setMaxPerRoute(var1, this.getBackedOffPoolSize(var2));
            this.lastRouteBackoffs.put(var1, var3);
            return;
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var19 = var10000;

         try {
            throw var19;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            continue;
         }
      }
   }

   public void probe(HttpRoute var1) {
      ConnPoolControl var5 = this.connPerRoute;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label339: {
         int var2;
         label332: {
            try {
               var2 = this.connPerRoute.getMaxPerRoute(var1);
               if (var2 >= this.cap) {
                  var2 = this.cap;
                  break label332;
               }
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label339;
            }

            ++var2;
         }

         long var3;
         label340: {
            try {
               Long var6 = this.getLastUpdate(this.lastRouteProbes, var1);
               Long var7 = this.getLastUpdate(this.lastRouteBackoffs, var1);
               var3 = this.clock.getCurrentTime();
               if (var3 - var6 >= this.coolDown && var3 - var7 >= this.coolDown) {
                  break label340;
               }
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label339;
            }

            try {
               return;
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label339;
            }
         }

         label312:
         try {
            this.connPerRoute.setMaxPerRoute(var1, var2);
            this.lastRouteProbes.put(var1, var3);
            return;
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label312;
         }
      }

      while(true) {
         Throwable var38 = var10000;

         try {
            throw var38;
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            continue;
         }
      }
   }

   public void setBackoffFactor(double var1) {
      boolean var3;
      if (var1 > 0.0D && var1 < 1.0D) {
         var3 = true;
      } else {
         var3 = false;
      }

      Args.check(var3, "Backoff factor must be 0.0 < f < 1.0");
      this.backoffFactor = var1;
   }

   public void setCooldownMillis(long var1) {
      Args.positive(this.coolDown, "Cool down");
      this.coolDown = var1;
   }

   public void setPerHostConnectionCap(int var1) {
      Args.positive(var1, "Per host connection cap");
      this.cap = var1;
   }
}
