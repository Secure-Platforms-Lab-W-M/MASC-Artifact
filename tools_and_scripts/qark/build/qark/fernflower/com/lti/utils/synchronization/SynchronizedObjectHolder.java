package com.lti.utils.synchronization;

public class SynchronizedObjectHolder {
   private Object object;

   public SynchronizedObjectHolder() {
   }

   public SynchronizedObjectHolder(Object var1) {
      this.setObject(var1);
   }

   public Object getObject() {
      synchronized(this){}

      Object var1;
      try {
         var1 = this.object;
      } finally {
         ;
      }

      return var1;
   }

   public void setObject(Object var1) {
      synchronized(this){}

      try {
         this.object = var1;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void waitUntilNotNull() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.object != null) {
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

   public boolean waitUntilNotNull(int var1) throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label151: {
         boolean var10001;
         long var2;
         try {
            var2 = System.currentTimeMillis();
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            break label151;
         }

         while(true) {
            long var4;
            try {
               if (this.object != null) {
                  return true;
               }

               var4 = System.currentTimeMillis();
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break;
            }

            var4 = (long)var1 - (var4 - var2);
            if (var4 <= 0L) {
               return false;
            }

            try {
               this.wait(var4);
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var6 = var10000;
      throw var6;
   }
}
