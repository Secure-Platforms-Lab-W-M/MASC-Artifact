package okhttp3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public final class Dispatcher {
   @Nullable
   private ExecutorService executorService;
   @Nullable
   private Runnable idleCallback;
   private int maxRequests = 64;
   private int maxRequestsPerHost = 5;
   private final Deque readyAsyncCalls = new ArrayDeque();
   private final Deque runningAsyncCalls = new ArrayDeque();
   private final Deque runningSyncCalls = new ArrayDeque();

   public Dispatcher() {
   }

   public Dispatcher(ExecutorService var1) {
      this.executorService = var1;
   }

   private void finished(Deque var1, Object var2, boolean var3) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label300: {
         label295: {
            try {
               if (var1.remove(var2)) {
                  break label295;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label300;
            }

            try {
               throw new AssertionError("Call wasn't in-flight!");
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label300;
            }
         }

         if (var3) {
            try {
               this.promoteCalls();
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label300;
            }
         }

         int var4;
         Runnable var36;
         try {
            var4 = this.runningCallsCount();
            var36 = this.idleCallback;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label300;
         }

         if (var4 == 0 && var36 != null) {
            var36.run();
         }

         return;
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   private void promoteCalls() {
      if (this.runningAsyncCalls.size() < this.maxRequests) {
         if (!this.readyAsyncCalls.isEmpty()) {
            Iterator var1 = this.readyAsyncCalls.iterator();

            do {
               if (!var1.hasNext()) {
                  return;
               }

               RealCall.AsyncCall var2 = (RealCall.AsyncCall)var1.next();
               if (this.runningCallsForHost(var2) < this.maxRequestsPerHost) {
                  var1.remove();
                  this.runningAsyncCalls.add(var2);
                  this.executorService().execute(var2);
               }
            } while(this.runningAsyncCalls.size() < this.maxRequests);

         }
      }
   }

   private int runningCallsForHost(RealCall.AsyncCall var1) {
      int var2 = 0;

      int var3;
      for(Iterator var4 = this.runningAsyncCalls.iterator(); var4.hasNext(); var2 = var3) {
         var3 = var2;
         if (((RealCall.AsyncCall)var4.next()).host().equals(var1.host())) {
            var3 = var2 + 1;
         }
      }

      return var2;
   }

   public void cancelAll() {
      synchronized(this){}

      Throwable var10000;
      label427: {
         Iterator var1;
         boolean var10001;
         try {
            var1 = this.readyAsyncCalls.iterator();
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label427;
         }

         label426:
         while(true) {
            try {
               if (var1.hasNext()) {
                  ((RealCall.AsyncCall)var1.next()).get().cancel();
                  continue;
               }
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break;
            }

            try {
               var1 = this.runningAsyncCalls.iterator();
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break;
            }

            while(true) {
               try {
                  if (!var1.hasNext()) {
                     break;
                  }

                  ((RealCall.AsyncCall)var1.next()).get().cancel();
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label426;
               }
            }

            try {
               var1 = this.runningSyncCalls.iterator();
            } catch (Throwable var39) {
               var10000 = var39;
               var10001 = false;
               break;
            }

            while(true) {
               try {
                  if (!var1.hasNext()) {
                     return;
                  }

                  ((RealCall)var1.next()).cancel();
               } catch (Throwable var38) {
                  var10000 = var38;
                  var10001 = false;
                  break label426;
               }
            }
         }
      }

      Throwable var44 = var10000;
      throw var44;
   }

   void enqueue(RealCall.AsyncCall var1) {
      synchronized(this){}

      try {
         if (this.runningAsyncCalls.size() < this.maxRequests && this.runningCallsForHost(var1) < this.maxRequestsPerHost) {
            this.runningAsyncCalls.add(var1);
            this.executorService().execute(var1);
         } else {
            this.readyAsyncCalls.add(var1);
         }
      } finally {
         ;
      }

   }

   void executed(RealCall var1) {
      synchronized(this){}

      try {
         this.runningSyncCalls.add(var1);
      } finally {
         ;
      }

   }

   public ExecutorService executorService() {
      synchronized(this){}

      ExecutorService var1;
      try {
         if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
         }

         var1 = this.executorService;
      } finally {
         ;
      }

      return var1;
   }

   void finished(RealCall.AsyncCall var1) {
      this.finished(this.runningAsyncCalls, var1, true);
   }

   void finished(RealCall var1) {
      this.finished(this.runningSyncCalls, var1, false);
   }

   public int getMaxRequests() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.maxRequests;
      } finally {
         ;
      }

      return var1;
   }

   public int getMaxRequestsPerHost() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.maxRequestsPerHost;
      } finally {
         ;
      }

      return var1;
   }

   public List queuedCalls() {
      synchronized(this){}

      Throwable var10000;
      label132: {
         ArrayList var1;
         boolean var10001;
         Iterator var2;
         try {
            var1 = new ArrayList();
            var2 = this.readyAsyncCalls.iterator();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label132;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  var1.add(((RealCall.AsyncCall)var2.next()).get());
                  continue;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            try {
               List var16 = Collections.unmodifiableList(var1);
               return var16;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public int queuedCallsCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.readyAsyncCalls.size();
      } finally {
         ;
      }

      return var1;
   }

   public List runningCalls() {
      synchronized(this){}

      Throwable var10000;
      label132: {
         ArrayList var1;
         boolean var10001;
         Iterator var2;
         try {
            var1 = new ArrayList();
            var1.addAll(this.runningSyncCalls);
            var2 = this.runningAsyncCalls.iterator();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label132;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  var1.add(((RealCall.AsyncCall)var2.next()).get());
                  continue;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            try {
               List var16 = Collections.unmodifiableList(var1);
               return var16;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public int runningCallsCount() {
      synchronized(this){}

      int var1;
      int var2;
      try {
         var1 = this.runningAsyncCalls.size();
         var2 = this.runningSyncCalls.size();
      } finally {
         ;
      }

      return var1 + var2;
   }

   public void setIdleCallback(@Nullable Runnable var1) {
      synchronized(this){}

      try {
         this.idleCallback = var1;
      } finally {
         ;
      }

   }

   public void setMaxRequests(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 >= 1) {
         label51: {
            try {
               this.maxRequests = var1;
               this.promoteCalls();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label51;
            }

            return;
         }
      } else {
         label53:
         try {
            StringBuilder var9 = new StringBuilder();
            var9.append("max < 1: ");
            var9.append(var1);
            throw new IllegalArgumentException(var9.toString());
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label53;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void setMaxRequestsPerHost(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 >= 1) {
         label51: {
            try {
               this.maxRequestsPerHost = var1;
               this.promoteCalls();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label51;
            }

            return;
         }
      } else {
         label53:
         try {
            StringBuilder var9 = new StringBuilder();
            var9.append("max < 1: ");
            var9.append(var1);
            throw new IllegalArgumentException(var9.toString());
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label53;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }
}
