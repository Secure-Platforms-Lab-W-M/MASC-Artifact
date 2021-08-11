package javax.jmdns.impl;

import java.net.InetAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.jmdns.impl.tasks.RecordReaper;
import javax.jmdns.impl.tasks.Responder;
import javax.jmdns.impl.tasks.resolver.ServiceInfoResolver;
import javax.jmdns.impl.tasks.resolver.ServiceResolver;
import javax.jmdns.impl.tasks.resolver.TypeResolver;
import javax.jmdns.impl.tasks.state.Announcer;
import javax.jmdns.impl.tasks.state.Canceler;
import javax.jmdns.impl.tasks.state.Prober;
import javax.jmdns.impl.tasks.state.Renewer;

public interface DNSTaskStarter {
   void cancelStateTimer();

   void cancelTimer();

   void purgeStateTimer();

   void purgeTimer();

   void startAnnouncer();

   void startCanceler();

   void startProber();

   void startReaper();

   void startRenewer();

   void startResponder(DNSIncoming var1, InetAddress var2, int var3);

   void startServiceInfoResolver(ServiceInfoImpl var1);

   void startServiceResolver(String var1);

   void startTypeResolver();

   public static final class DNSTaskStarterImpl implements DNSTaskStarter {
      private final JmDNSImpl _jmDNSImpl;
      private final Timer _stateTimer;
      private final Timer _timer;

      public DNSTaskStarterImpl(JmDNSImpl var1) {
         this._jmDNSImpl = var1;
         StringBuilder var2 = new StringBuilder();
         var2.append("JmDNS(");
         var2.append(this._jmDNSImpl.getName());
         var2.append(").Timer");
         this._timer = new DNSTaskStarter.DNSTaskStarterImpl.StarterTimer(var2.toString(), true);
         var2 = new StringBuilder();
         var2.append("JmDNS(");
         var2.append(this._jmDNSImpl.getName());
         var2.append(").State.Timer");
         this._stateTimer = new DNSTaskStarter.DNSTaskStarterImpl.StarterTimer(var2.toString(), false);
      }

      public void cancelStateTimer() {
         this._stateTimer.cancel();
      }

      public void cancelTimer() {
         this._timer.cancel();
      }

      public void purgeStateTimer() {
         this._stateTimer.purge();
      }

      public void purgeTimer() {
         this._timer.purge();
      }

      public void startAnnouncer() {
         (new Announcer(this._jmDNSImpl)).start(this._stateTimer);
      }

      public void startCanceler() {
         (new Canceler(this._jmDNSImpl)).start(this._stateTimer);
      }

      public void startProber() {
         (new Prober(this._jmDNSImpl)).start(this._stateTimer);
      }

      public void startReaper() {
         (new RecordReaper(this._jmDNSImpl)).start(this._timer);
      }

      public void startRenewer() {
         (new Renewer(this._jmDNSImpl)).start(this._stateTimer);
      }

      public void startResponder(DNSIncoming var1, InetAddress var2, int var3) {
         (new Responder(this._jmDNSImpl, var1, var2, var3)).start(this._timer);
      }

      public void startServiceInfoResolver(ServiceInfoImpl var1) {
         (new ServiceInfoResolver(this._jmDNSImpl, var1)).start(this._timer);
      }

      public void startServiceResolver(String var1) {
         (new ServiceResolver(this._jmDNSImpl, var1)).start(this._timer);
      }

      public void startTypeResolver() {
         (new TypeResolver(this._jmDNSImpl)).start(this._timer);
      }

      public static class StarterTimer extends Timer {
         private volatile boolean _cancelled = false;

         public StarterTimer() {
         }

         public StarterTimer(String var1) {
            super(var1);
         }

         public StarterTimer(String var1, boolean var2) {
            super(var1, var2);
         }

         public StarterTimer(boolean var1) {
            super(var1);
         }

         public void cancel() {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var1;
               boolean var10001;
               try {
                  var1 = this._cancelled;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label78;
               }

               if (var1) {
                  return;
               }

               try {
                  this._cancelled = true;
                  super.cancel();
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var2 = var10000;
            throw var2;
         }

         public void schedule(TimerTask var1, long var2) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var4;
               try {
                  var4 = this._cancelled;
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label78;
               }

               if (var4) {
                  return;
               }

               try {
                  super.schedule(var1, var2);
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var11 = var10000;
            throw var11;
         }

         public void schedule(TimerTask var1, long var2, long var4) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var6;
               try {
                  var6 = this._cancelled;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label78;
               }

               if (var6) {
                  return;
               }

               try {
                  super.schedule(var1, var2, var4);
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var13 = var10000;
            throw var13;
         }

         public void schedule(TimerTask var1, Date var2) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var3;
               try {
                  var3 = this._cancelled;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label78;
               }

               if (var3) {
                  return;
               }

               try {
                  super.schedule(var1, var2);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var10 = var10000;
            throw var10;
         }

         public void schedule(TimerTask var1, Date var2, long var3) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var5;
               try {
                  var5 = this._cancelled;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label78;
               }

               if (var5) {
                  return;
               }

               try {
                  super.schedule(var1, var2, var3);
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var12 = var10000;
            throw var12;
         }

         public void scheduleAtFixedRate(TimerTask var1, long var2, long var4) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var6;
               try {
                  var6 = this._cancelled;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label78;
               }

               if (var6) {
                  return;
               }

               try {
                  super.scheduleAtFixedRate(var1, var2, var4);
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var13 = var10000;
            throw var13;
         }

         public void scheduleAtFixedRate(TimerTask var1, Date var2, long var3) {
            synchronized(this){}

            Throwable var10000;
            label78: {
               boolean var10001;
               boolean var5;
               try {
                  var5 = this._cancelled;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label78;
               }

               if (var5) {
                  return;
               }

               try {
                  super.scheduleAtFixedRate(var1, var2, var3);
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label78;
               }

               return;
            }

            Throwable var12 = var10000;
            throw var12;
         }
      }
   }

   public static final class Factory {
      private static final AtomicReference _databaseClassDelegate = new AtomicReference();
      private static volatile DNSTaskStarter.Factory _instance;
      private final ConcurrentMap _instances = new ConcurrentHashMap(20);

      private Factory() {
      }

      public static DNSTaskStarter.Factory.ClassDelegate classDelegate() {
         return (DNSTaskStarter.Factory.ClassDelegate)_databaseClassDelegate.get();
      }

      public static DNSTaskStarter.Factory getInstance() {
         if (_instance == null) {
            synchronized(DNSTaskStarter.Factory.class){}

            Throwable var10000;
            boolean var10001;
            label144: {
               try {
                  if (_instance == null) {
                     _instance = new DNSTaskStarter.Factory();
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label144;
               }

               label141:
               try {
                  return _instance;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label141;
               }
            }

            while(true) {
               Throwable var0 = var10000;

               try {
                  throw var0;
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  continue;
               }
            }
         } else {
            return _instance;
         }
      }

      protected static DNSTaskStarter newDNSTaskStarter(JmDNSImpl var0) {
         DNSTaskStarter var1 = null;
         DNSTaskStarter.Factory.ClassDelegate var2 = (DNSTaskStarter.Factory.ClassDelegate)_databaseClassDelegate.get();
         if (var2 != null) {
            var1 = var2.newDNSTaskStarter(var0);
         }

         return (DNSTaskStarter)(var1 != null ? var1 : new DNSTaskStarter.DNSTaskStarterImpl(var0));
      }

      public static void setClassDelegate(DNSTaskStarter.Factory.ClassDelegate var0) {
         _databaseClassDelegate.set(var0);
      }

      public void disposeStarter(JmDNSImpl var1) {
         this._instances.remove(var1);
      }

      public DNSTaskStarter getStarter(JmDNSImpl var1) {
         DNSTaskStarter var3 = (DNSTaskStarter)this._instances.get(var1);
         DNSTaskStarter var2 = var3;
         if (var3 == null) {
            this._instances.putIfAbsent(var1, newDNSTaskStarter(var1));
            var2 = (DNSTaskStarter)this._instances.get(var1);
         }

         return var2;
      }

      public interface ClassDelegate {
         DNSTaskStarter newDNSTaskStarter(JmDNSImpl var1);
      }
   }
}
