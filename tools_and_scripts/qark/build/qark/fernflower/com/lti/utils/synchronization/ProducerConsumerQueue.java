package com.lti.utils.synchronization;

import com.lti.utils.collections.Queue;

public class ProducerConsumerQueue {
   // $FF: renamed from: q com.lti.utils.collections.Queue
   private final Queue field_36 = new Queue();
   private final int sizeLimit;

   public ProducerConsumerQueue() {
      this.sizeLimit = -1;
   }

   public ProducerConsumerQueue(int var1) {
      this.sizeLimit = var1;
   }

   public Object get() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (!this.field_36.isEmpty()) {
               Object var1 = this.field_36.dequeue();
               this.notifyAll();
               var3 = false;
               return var1;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public Object get(long var1, Object var3) throws InterruptedException {
      synchronized(this){}

      long var4;
      long var6;
      do {
         boolean var9 = false;

         try {
            var9 = true;
            if (!this.field_36.isEmpty()) {
               var3 = this.field_36.dequeue();
               this.notifyAll();
               var9 = false;
               return var3;
            }

            var4 = System.currentTimeMillis();
            this.wait(var1);
            var6 = System.currentTimeMillis();
            var9 = false;
         } finally {
            if (var9) {
               ;
            }
         }
      } while(var6 - var4 <= var1);

      return var3;
   }

   public boolean isEmpty() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      try {
         var5 = true;
         var1 = this.field_36.size();
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isFull() {
      synchronized(this){}
      boolean var6 = false;

      boolean var3;
      label45: {
         int var1;
         int var2;
         try {
            var6 = true;
            if (this.sizeLimit <= 0) {
               var6 = false;
               break label45;
            }

            var1 = this.field_36.size();
            var2 = this.sizeLimit;
            var6 = false;
         } finally {
            if (var6) {
               ;
            }
         }

         if (var1 >= var2) {
            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   public void put(Object var1) throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.sizeLimit <= 0 || this.field_36.size() < this.sizeLimit) {
               this.field_36.enqueue(var1);
               this.notifyAll();
               var3 = false;
               return;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public boolean put(Object var1, long var2) throws InterruptedException {
      synchronized(this){}

      long var4;
      long var6;
      do {
         boolean var9 = false;

         try {
            var9 = true;
            if (this.sizeLimit <= 0 || this.field_36.size() < this.sizeLimit) {
               this.field_36.enqueue(var1);
               this.notifyAll();
               var9 = false;
               return true;
            }

            var4 = System.currentTimeMillis();
            this.wait(var2);
            var6 = System.currentTimeMillis();
            var9 = false;
         } finally {
            if (var9) {
               ;
            }
         }
      } while(var6 - var4 <= var2);

      return false;
   }

   public int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.field_36.size();
      } finally {
         ;
      }

      return var1;
   }

   public int sizeLimit() {
      return this.sizeLimit;
   }

   public void waitUntilEmpty() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.field_36.isEmpty()) {
               var3 = false;
               return;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public void waitUntilNotEmpty() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (!this.field_36.isEmpty()) {
               var3 = false;
               return;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public boolean waitUntilNotEmpty(long var1) throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label215: {
         boolean var10001;
         long var3;
         try {
            var3 = System.currentTimeMillis();
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label215;
         }

         while(true) {
            long var5;
            try {
               if (!this.field_36.isEmpty()) {
                  break;
               }

               var5 = System.currentTimeMillis();
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label215;
            }

            var5 = var1 - (var5 - var3);
            if (var5 < 1L) {
               return false;
            }

            try {
               this.wait(var5);
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label215;
            }
         }

         boolean var7;
         try {
            var7 = this.field_36.isEmpty();
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label215;
         }

         return var7 ^ true;
      }

      Throwable var8 = var10000;
      throw var8;
   }
}
