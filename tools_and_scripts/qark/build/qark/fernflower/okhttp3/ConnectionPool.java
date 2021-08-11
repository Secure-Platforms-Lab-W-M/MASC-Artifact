package okhttp3;

import java.lang.ref.Reference;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.platform.Platform;

public final class ConnectionPool {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final Executor executor;
   private final Runnable cleanupRunnable;
   boolean cleanupRunning;
   private final Deque connections;
   private final long keepAliveDurationNs;
   private final int maxIdleConnections;
   final RouteDatabase routeDatabase;

   static {
      executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
   }

   public ConnectionPool() {
      this(5, 5L, TimeUnit.MINUTES);
   }

   public ConnectionPool(int var1, long var2, TimeUnit var4) {
      this.cleanupRunnable = new Runnable() {
         public void run() {
            while(true) {
               long var1 = ConnectionPool.this.cleanup(System.nanoTime());
               if (var1 == -1L) {
                  return;
               }

               if (var1 > 0L) {
                  long var3 = var1 / 1000000L;
                  ConnectionPool var5 = ConnectionPool.this;
                  synchronized(var5){}

                  Throwable var10000;
                  boolean var10001;
                  label164: {
                     try {
                        try {
                           ConnectionPool.this.wait(var3, (int)(var1 - 1000000L * var3));
                        } catch (InterruptedException var21) {
                        }
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label164;
                     }

                     label150:
                     try {
                        continue;
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label150;
                     }
                  }

                  while(true) {
                     Throwable var6 = var10000;

                     try {
                        throw var6;
                     } catch (Throwable var19) {
                        var10000 = var19;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            }
         }
      };
      this.connections = new ArrayDeque();
      this.routeDatabase = new RouteDatabase();
      this.maxIdleConnections = var1;
      this.keepAliveDurationNs = var4.toNanos(var2);
      if (var2 <= 0L) {
         StringBuilder var5 = new StringBuilder();
         var5.append("keepAliveDuration <= 0: ");
         var5.append(var2);
         throw new IllegalArgumentException(var5.toString());
      }
   }

   private int pruneAndGetAllocationCount(RealConnection var1, long var2) {
      List var5 = var1.allocations;
      int var4 = 0;

      while(var4 < var5.size()) {
         Reference var6 = (Reference)var5.get(var4);
         if (var6.get() != null) {
            ++var4;
         } else {
            StreamAllocation.StreamAllocationReference var8 = (StreamAllocation.StreamAllocationReference)var6;
            StringBuilder var7 = new StringBuilder();
            var7.append("A connection to ");
            var7.append(var1.route().address().url());
            var7.append(" was leaked. Did you forget to close a response body?");
            String var9 = var7.toString();
            Platform.get().logCloseableLeak(var9, var8.callStackTrace);
            var5.remove(var4);
            var1.noNewStreams = true;
            if (var5.isEmpty()) {
               var1.idleAtNanos = var2 - this.keepAliveDurationNs;
               return 0;
            }
         }
      }

      return var5.size();
   }

   long cleanup(long var1) {
      int var4 = 0;
      int var3 = 0;
      RealConnection var11 = null;
      long var5 = Long.MIN_VALUE;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label908: {
         Iterator var13;
         try {
            var13 = this.connections.iterator();
         } catch (Throwable var102) {
            var10000 = var102;
            var10001 = false;
            break label908;
         }

         label907:
         while(true) {
            label912: {
               while(true) {
                  RealConnection var12;
                  try {
                     if (!var13.hasNext()) {
                        break;
                     }

                     var12 = (RealConnection)var13.next();
                     if (this.pruneAndGetAllocationCount(var12, var1) > 0) {
                        break label912;
                     }
                  } catch (Throwable var103) {
                     var10000 = var103;
                     var10001 = false;
                     break label907;
                  }

                  ++var3;

                  long var9;
                  try {
                     var9 = var1 - var12.idleAtNanos;
                  } catch (Throwable var101) {
                     var10000 = var101;
                     var10001 = false;
                     break label907;
                  }

                  long var7 = var5;
                  if (var9 > var5) {
                     var7 = var9;
                     var11 = var12;
                  }

                  var5 = var7;
               }

               label914: {
                  label887:
                  try {
                     if (var5 < this.keepAliveDurationNs && var3 <= this.maxIdleConnections) {
                        break label887;
                     }
                     break label914;
                  } catch (Throwable var100) {
                     var10000 = var100;
                     var10001 = false;
                     break;
                  }

                  if (var3 > 0) {
                     try {
                        var1 = this.keepAliveDurationNs;
                     } catch (Throwable var96) {
                        var10000 = var96;
                        var10001 = false;
                        break;
                     }

                     return var1 - var5;
                  } else if (var4 > 0) {
                     try {
                        var1 = this.keepAliveDurationNs;
                        return var1;
                     } catch (Throwable var97) {
                        var10000 = var97;
                        var10001 = false;
                        break;
                     }
                  } else {
                     try {
                        this.cleanupRunning = false;
                        return -1L;
                     } catch (Throwable var98) {
                        var10000 = var98;
                        var10001 = false;
                        break;
                     }
                  }
               }

               try {
                  this.connections.remove(var11);
               } catch (Throwable var99) {
                  var10000 = var99;
                  var10001 = false;
                  break;
               }

               Util.closeQuietly(var11.socket());
               return 0L;
            }

            ++var4;
         }
      }

      while(true) {
         Throwable var104 = var10000;

         try {
            throw var104;
         } catch (Throwable var95) {
            var10000 = var95;
            var10001 = false;
            continue;
         }
      }
   }

   boolean connectionBecameIdle(RealConnection var1) {
      if (!var1.noNewStreams && this.maxIdleConnections != 0) {
         this.notifyAll();
         return false;
      } else {
         this.connections.remove(var1);
         return true;
      }
   }

   public int connectionCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.connections.size();
      } finally {
         ;
      }

      return var1;
   }

   @Nullable
   Socket deduplicate(Address var1, StreamAllocation var2) {
      Iterator var3 = this.connections.iterator();

      RealConnection var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (RealConnection)var3.next();
      } while(!var4.isEligible(var1, (Route)null) || !var4.isMultiplexed() || var4 == var2.connection());

      return var2.releaseAndAcquire(var4);
   }

   public void evictAll() {
      ArrayList var1 = new ArrayList();
      synchronized(this){}

      label283: {
         Throwable var10000;
         boolean var10001;
         label278: {
            Iterator var2;
            try {
               var2 = this.connections.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label278;
            }

            while(true) {
               try {
                  while(var2.hasNext()) {
                     RealConnection var3 = (RealConnection)var2.next();
                     if (var3.allocations.isEmpty()) {
                        var3.noNewStreams = true;
                        var1.add(var3);
                        var2.remove();
                     }
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               try {
                  break label283;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }

      Iterator var25 = var1.iterator();

      while(var25.hasNext()) {
         Util.closeQuietly(((RealConnection)var25.next()).socket());
      }

   }

   @Nullable
   RealConnection get(Address var1, StreamAllocation var2, Route var3) {
      Iterator var4 = this.connections.iterator();

      RealConnection var5;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         var5 = (RealConnection)var4.next();
      } while(!var5.isEligible(var1, var3));

      var2.acquire(var5);
      return var5;
   }

   public int idleConnectionCount() {
      synchronized(this){}
      int var1 = 0;

      Throwable var10000;
      label91: {
         boolean var10001;
         Iterator var4;
         try {
            var4 = this.connections.iterator();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label91;
         }

         while(true) {
            boolean var3;
            try {
               if (!var4.hasNext()) {
                  return var1;
               }

               var3 = ((RealConnection)var4.next()).allocations.isEmpty();
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            int var2 = var1;
            if (var3) {
               var2 = var1 + 1;
            }

            var1 = var2;
         }
      }

      Throwable var11 = var10000;
      throw var11;
   }

   void put(RealConnection var1) {
      if (!this.cleanupRunning) {
         this.cleanupRunning = true;
         executor.execute(this.cleanupRunnable);
      }

      this.connections.add(var1);
   }
}
