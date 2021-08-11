package net.sf.fmj.utility;

public class RingBuffer {
   private Object[] buckets;
   private int overrunCounter;
   private int readIndex;
   private int writeIndex;

   public RingBuffer(int var1) {
      this.resize(var1);
   }

   public Object get() throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label220: {
         boolean var10001;
         try {
            if (this.isEmpty()) {
               this.wait();
            }
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label220;
         }

         int var1;
         Object[] var3;
         try {
            var3 = this.buckets;
            var1 = this.readIndex;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label220;
         }

         int var2 = var1 + 1;

         try {
            this.readIndex = var2;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label220;
         }

         Object var24 = var3[var1];

         label204:
         try {
            if (var2 >= this.buckets.length) {
               this.readIndex = 0;
            }

            return var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label204;
         }
      }

      Throwable var25 = var10000;
      throw var25;
   }

   public int getOverrunCounter() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.overrunCounter;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isEmpty() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      int var2;
      try {
         var5 = true;
         var1 = this.readIndex;
         var2 = this.writeIndex;
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      return var1 == var2;
   }

   public boolean isFull() {
      synchronized(this){}

      Throwable var10000;
      label148: {
         boolean var10001;
         int var2;
         try {
            var2 = this.writeIndex + 1;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label148;
         }

         int var1 = var2;

         label138: {
            try {
               if (var2 < this.buckets.length) {
                  break label138;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label148;
            }

            var1 = 0;
         }

         label133:
         try {
            var2 = this.readIndex;
            return var1 == var2;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label133;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public Object peek() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isEmpty();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1) {
            return null;
         }

         Object var9;
         try {
            var9 = this.buckets[this.readIndex];
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return var9;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public boolean put(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public void resize(int var1) {
      synchronized(this){}
      int var2 = var1;
      if (var1 < 1) {
         var2 = 1;
      }

      try {
         this.buckets = new Object[var2 + 1];
         this.overrunCounter = 0;
         this.writeIndex = 0;
         this.readIndex = 0;
      } finally {
         ;
      }

   }

   public int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.buckets.length;
      } finally {
         ;
      }

      return var1 - 1;
   }
}
