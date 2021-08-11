package javax.jmdns.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DNSStatefulObject {
   boolean advanceState(DNSTask var1);

   void associateWithTask(DNSTask var1, DNSState var2);

   boolean cancelState();

   boolean closeState();

   JmDNSImpl getDns();

   boolean isAnnounced();

   boolean isAnnouncing();

   boolean isAssociatedWithTask(DNSTask var1, DNSState var2);

   boolean isCanceled();

   boolean isCanceling();

   boolean isClosed();

   boolean isClosing();

   boolean isProbing();

   boolean recoverState();

   void removeAssociationWithTask(DNSTask var1);

   boolean revertState();

   boolean waitForAnnounced(long var1);

   boolean waitForCanceled(long var1);

   public static final class DNSStatefulObjectSemaphore {
      private static Logger logger = LoggerFactory.getLogger(DNSStatefulObject.DNSStatefulObjectSemaphore.class.getName());
      private final String _name;
      private final ConcurrentMap _semaphores;

      public DNSStatefulObjectSemaphore(String var1) {
         this._name = var1;
         this._semaphores = new ConcurrentHashMap(50);
      }

      public void signalEvent() {
         Collection var1 = this._semaphores.values();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Semaphore var3 = (Semaphore)var2.next();
            var3.release();
            var1.remove(var3);
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(1000);
         var1.append("Semaphore: ");
         var1.append(this._name);
         if (this._semaphores.size() == 0) {
            var1.append(" no semaphores.");
         } else {
            var1.append(" semaphores:\n");
            Iterator var2 = this._semaphores.entrySet().iterator();

            while(var2.hasNext()) {
               Entry var3 = (Entry)var2.next();
               var1.append("\tThread: ");
               var1.append(((Thread)var3.getKey()).getName());
               var1.append(' ');
               var1.append(var3.getValue());
               var1.append('\n');
            }
         }

         return var1.toString();
      }

      public void waitForEvent(long var1) {
         Thread var3 = Thread.currentThread();
         if ((Semaphore)this._semaphores.get(var3) == null) {
            Semaphore var4 = new Semaphore(1, true);
            var4.drainPermits();
            this._semaphores.putIfAbsent(var3, var4);
         }

         Semaphore var6 = (Semaphore)this._semaphores.get(var3);

         try {
            var6.tryAcquire(var1, TimeUnit.MILLISECONDS);
         } catch (InterruptedException var5) {
            logger.debug("Exception ", var5);
         }
      }
   }

   public static class DefaultImplementation extends ReentrantLock implements DNSStatefulObject {
      private static Logger logger = LoggerFactory.getLogger(DNSStatefulObject.DefaultImplementation.class.getName());
      private static final long serialVersionUID = -3264781576883412227L;
      private final DNSStatefulObject.DNSStatefulObjectSemaphore _announcing;
      private final DNSStatefulObject.DNSStatefulObjectSemaphore _canceling;
      private volatile JmDNSImpl _dns = null;
      protected volatile DNSState _state;
      protected volatile DNSTask _task = null;

      public DefaultImplementation() {
         this._state = DNSState.PROBING_1;
         this._announcing = new DNSStatefulObject.DNSStatefulObjectSemaphore("Announce");
         this._canceling = new DNSStatefulObject.DNSStatefulObjectSemaphore("Cancel");
      }

      private boolean willCancel() {
         return this._state.isCanceled() || this._state.isCanceling();
      }

      private boolean willClose() {
         return this._state.isClosed() || this._state.isClosing();
      }

      public boolean advanceState(DNSTask var1) {
         if (this._task == var1) {
            this.lock();

            try {
               if (this._task == var1) {
                  this.setState(this._state.advance());
               } else {
                  logger.warn("Trying to advance state whhen not the owner. owner: {} perpetrator: {}", this._task, var1);
               }
            } finally {
               this.unlock();
            }

            return true;
         } else {
            return true;
         }
      }

      public void associateWithTask(DNSTask var1, DNSState var2) {
         if (this._task == null && this._state == var2) {
            this.lock();

            try {
               if (this._task == null && this._state == var2) {
                  this.setTask(var1);
               }
            } finally {
               this.unlock();
            }

         }
      }

      public boolean cancelState() {
         boolean var1 = false;
         if (!this.willCancel()) {
            this.lock();
            boolean var4 = false;

            label42: {
               try {
                  var4 = true;
                  if (this.willCancel()) {
                     var4 = false;
                     break label42;
                  }

                  this.setState(DNSState.CANCELING_1);
                  this.setTask((DNSTask)null);
                  var4 = false;
               } finally {
                  if (var4) {
                     this.unlock();
                  }
               }

               var1 = true;
            }

            this.unlock();
            return var1;
         } else {
            return false;
         }
      }

      public boolean closeState() {
         boolean var1 = false;
         if (!this.willClose()) {
            this.lock();
            boolean var4 = false;

            label42: {
               try {
                  var4 = true;
                  if (this.willClose()) {
                     var4 = false;
                     break label42;
                  }

                  this.setState(DNSState.CLOSING);
                  this.setTask((DNSTask)null);
                  var4 = false;
               } finally {
                  if (var4) {
                     this.unlock();
                  }
               }

               var1 = true;
            }

            this.unlock();
            return var1;
         } else {
            return false;
         }
      }

      public JmDNSImpl getDns() {
         return this._dns;
      }

      public boolean isAnnounced() {
         return this._state.isAnnounced();
      }

      public boolean isAnnouncing() {
         return this._state.isAnnouncing();
      }

      public boolean isAssociatedWithTask(DNSTask var1, DNSState var2) {
         this.lock();
         boolean var5 = false;

         boolean var3;
         label46: {
            label45: {
               DNSState var7;
               try {
                  var5 = true;
                  if (this._task != var1) {
                     var5 = false;
                     break label45;
                  }

                  var7 = this._state;
                  var5 = false;
               } finally {
                  if (var5) {
                     this.unlock();
                  }
               }

               if (var7 == var2) {
                  var3 = true;
                  break label46;
               }
            }

            var3 = false;
         }

         this.unlock();
         return var3;
      }

      public boolean isCanceled() {
         return this._state.isCanceled();
      }

      public boolean isCanceling() {
         return this._state.isCanceling();
      }

      public boolean isClosed() {
         return this._state.isClosed();
      }

      public boolean isClosing() {
         return this._state.isClosing();
      }

      public boolean isProbing() {
         return this._state.isProbing();
      }

      public boolean recoverState() {
         this.lock();

         try {
            this.setState(DNSState.PROBING_1);
            this.setTask((DNSTask)null);
         } finally {
            this.unlock();
         }

         return false;
      }

      public void removeAssociationWithTask(DNSTask var1) {
         if (this._task == var1) {
            this.lock();

            try {
               if (this._task == var1) {
                  this.setTask((DNSTask)null);
               }
            } finally {
               this.unlock();
            }

         }
      }

      public boolean revertState() {
         if (!this.willCancel()) {
            this.lock();

            try {
               if (!this.willCancel()) {
                  this.setState(this._state.revert());
                  this.setTask((DNSTask)null);
               }
            } finally {
               this.unlock();
            }

            return true;
         } else {
            return true;
         }
      }

      protected void setDns(JmDNSImpl var1) {
         this._dns = var1;
      }

      protected void setState(DNSState var1) {
         this.lock();

         try {
            this._state = var1;
            if (this.isAnnounced()) {
               this._announcing.signalEvent();
            }

            if (this.isCanceled()) {
               this._canceling.signalEvent();
               this._announcing.signalEvent();
            }
         } finally {
            this.unlock();
         }

      }

      protected void setTask(DNSTask var1) {
         this._task = var1;
      }

      public String toString() {
         String var2 = "NO DNS";

         String var1;
         StringBuilder var3;
         StringBuilder var6;
         label37: {
            boolean var10001;
            label31: {
               try {
                  var3 = new StringBuilder();
                  if (this._dns != null) {
                     var6 = new StringBuilder();
                     var6.append("DNS: ");
                     var6.append(this._dns.getName());
                     var6.append(" [");
                     var6.append(this._dns.getInetAddress());
                     var6.append("]");
                     var1 = var6.toString();
                     break label31;
                  }
               } catch (IOException var5) {
                  var10001 = false;
                  break label37;
               }

               var1 = "NO DNS";
            }

            try {
               var3.append(var1);
               var3.append(" state: ");
               var3.append(this._state);
               var3.append(" task: ");
               var3.append(this._task);
               var1 = var3.toString();
               return var1;
            } catch (IOException var4) {
               var10001 = false;
            }
         }

         var3 = new StringBuilder();
         var1 = var2;
         if (this._dns != null) {
            var6 = new StringBuilder();
            var6.append("DNS: ");
            var6.append(this._dns.getName());
            var1 = var6.toString();
         }

         var3.append(var1);
         var3.append(" state: ");
         var3.append(this._state);
         var3.append(" task: ");
         var3.append(this._task);
         return var3.toString();
      }

      public boolean waitForAnnounced(long var1) {
         if (!this.isAnnounced() && !this.willCancel()) {
            this._announcing.waitForEvent(var1 + 10L);
         }

         if (!this.isAnnounced()) {
            this._announcing.waitForEvent(10L);
            if (!this.isAnnounced()) {
               if (!this.willCancel() && !this.willClose()) {
                  logger.warn("Wait for announced timed out: {}", this);
               } else {
                  logger.debug("Wait for announced cancelled: {}", this);
               }
            }
         }

         return this.isAnnounced();
      }

      public boolean waitForCanceled(long var1) {
         if (!this.isCanceled()) {
            this._canceling.waitForEvent(var1);
         }

         if (!this.isCanceled()) {
            this._canceling.waitForEvent(10L);
            if (!this.isCanceled() && !this.willClose()) {
               logger.warn("Wait for canceled timed out: {}", this);
            }
         }

         return this.isCanceled();
      }
   }
}
