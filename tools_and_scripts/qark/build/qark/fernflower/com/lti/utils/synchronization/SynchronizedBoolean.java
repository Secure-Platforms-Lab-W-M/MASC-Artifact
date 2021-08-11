package com.lti.utils.synchronization;

public class SynchronizedBoolean {
   // $FF: renamed from: b boolean
   private boolean field_132 = false;

   public SynchronizedBoolean() {
   }

   public SynchronizedBoolean(boolean var1) {
      this.field_132 = var1;
   }

   public boolean getAndSet(boolean var1, boolean var2) {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var10001;
         boolean var3;
         try {
            var3 = this.field_132;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label78;
         }

         if (var3 != var1) {
            return false;
         }

         try {
            this.setValue(var2);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         return true;
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public boolean getValue() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.field_132;
      } finally {
         ;
      }

      return var1;
   }

   public void setValue(boolean var1) {
      synchronized(this){}

      try {
         if (this.field_132 != var1) {
            this.field_132 = var1;
            this.notifyAll();
         }
      } finally {
         ;
      }

   }

   public void waitUntil(boolean var1) throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var4 = false;

         try {
            var4 = true;
            if (this.field_132 == var1) {
               var4 = false;
               return;
            }

            this.wait();
            var4 = false;
         } finally {
            if (var4) {
               ;
            }
         }
      }
   }

   public boolean waitUntil(boolean var1, int var2) throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label151: {
         boolean var10001;
         long var3;
         try {
            var3 = System.currentTimeMillis();
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label151;
         }

         while(true) {
            long var5;
            try {
               if (this.field_132 == var1) {
                  return true;
               }

               var5 = System.currentTimeMillis();
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break;
            }

            var5 = (long)var2 - (var5 - var3);
            if (var5 <= 0L) {
               return false;
            }

            try {
               this.wait(var5);
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var7 = var10000;
      throw var7;
   }
}
