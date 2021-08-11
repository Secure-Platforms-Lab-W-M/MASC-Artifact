package net.sf.fmj.media.rtp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.media.Buffer;

class JitterBuffer {
   private int capacity;
   final Condition condition;
   private Buffer[] elements;
   private int length;
   final Lock lock;
   private int locked;
   private int offset;

   public JitterBuffer(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      } else {
         this.elements = new Buffer[var1];
         int var2 = 0;

         while(true) {
            Buffer[] var3 = this.elements;
            if (var2 >= var3.length) {
               this.capacity = var1;
               this.length = 0;
               this.locked = -1;
               this.offset = 0;
               ReentrantLock var4 = new ReentrantLock();
               this.lock = var4;
               this.condition = var4.newCondition();
               return;
            }

            var3[var2] = new Buffer();
            ++var2;
         }
      }
   }

   private void append(Buffer var1) {
      int var2 = (this.offset + this.length) % this.capacity;
      int var3 = this.locked;
      if (var2 != var3) {
         Buffer[] var4 = this.elements;
         var4[var3] = var4[var2];
         var4[var2] = var1;
      }

      ++this.length;
   }

   private void assertLocked(Buffer var1) throws IllegalStateException {
      int var2 = this.locked;
      if (var2 != -1) {
         if (var1 != this.elements[var2]) {
            throw new IllegalArgumentException("buffer");
         }
      } else {
         throw new IllegalStateException("No Buffer has been retrieved from this JitterBuffer and has not been returned yet.");
      }
   }

   private void assertNotLocked() throws IllegalStateException {
      if (this.locked != -1) {
         throw new IllegalStateException("A Buffer has been retrieved from this JitterBuffer and has not been returned yet.");
      }
   }

   private void insert(Buffer var1) {
      int var2 = this.offset;
      int var4 = (this.offset + this.length) % this.capacity;
      long var6 = var1.getSequenceNumber();

      int var3;
      while(var2 != var4 && this.elements[var2].getSequenceNumber() <= var6) {
         var3 = var2 + 1;
         var2 = var3;
         if (var3 >= this.capacity) {
            var2 = 0;
         }
      }

      if (var2 == this.offset) {
         this.prepend(var1);
      } else if (var2 == var4) {
         this.append(var1);
      } else {
         Buffer[] var8 = this.elements;

         for(var8[this.locked] = var8[var4]; var4 != var2; var4 = var3) {
            int var5 = var4 - 1;
            var3 = var5;
            if (var5 < 0) {
               var3 = this.capacity - 1;
            }

            var8 = this.elements;
            var8[var4] = var8[var3];
         }

         this.elements[var2] = var1;
         ++this.length;
      }
   }

   private void prepend(Buffer var1) {
      int var3 = this.offset - 1;
      int var2 = var3;
      if (var3 < 0) {
         var2 = this.capacity - 1;
      }

      var3 = this.locked;
      if (var2 != var3) {
         Buffer[] var4 = this.elements;
         var4[var3] = var4[var2];
         var4[var2] = var1;
      }

      this.offset = var2;
      ++this.length;
   }

   public void addPkt(Buffer var1) {
      this.assertLocked(var1);
      if (this.noMoreFree()) {
         throw new IllegalStateException("noMoreFree");
      } else {
         long var2 = this.getFirstSeq();
         long var4 = this.getLastSeq();
         long var6 = var1.getSequenceNumber();
         if (var2 == 9223372036854775806L && var4 == 9223372036854775806L) {
            this.append(var1);
         } else if (var6 < var2) {
            this.prepend(var1);
         } else if (var2 < var6 && var6 < var4) {
            this.insert(var1);
         } else if (var6 > var4) {
            this.append(var1);
         } else {
            this.returnFree(var1);
         }

         this.locked = -1;
      }
   }

   void dropFill(int var1) {
      this.assertNotLocked();
      if (var1 >= 0) {
         int var3 = this.length;
         if (var1 < var3) {
            int var4 = this.offset;
            int var5 = this.capacity;
            var1 = (var4 + var1) % var5;
            Buffer var6 = this.elements[var1];
            int var2;
            if (var1 == var4) {
               this.offset = (var4 + 1) % var5;
               var2 = var1;
            } else {
               var2 = var1;
               if (var1 != (var4 + var3 - 1) % var5) {
                  while(true) {
                     var2 = this.offset;
                     if (var1 == var2) {
                        this.elements[var1] = var6;
                        this.offset = (var2 + 1) % this.capacity;
                        var2 = var1;
                        break;
                     }

                     var3 = var1 - 1;
                     var2 = var3;
                     if (var3 < 0) {
                        var2 = this.capacity - 1;
                     }

                     Buffer[] var7 = this.elements;
                     var7[var1] = var7[var2];
                     var1 = var2;
                  }
               }
            }

            --this.length;
            this.locked = var2;
            this.returnFree(var6);
            return;
         }
      }

      throw new IndexOutOfBoundsException(Integer.toString(var1));
   }

   public void dropFirstFill() {
      this.returnFree(this.getFill());
   }

   boolean fillNotEmpty() {
      return this.getFillCount() != 0;
   }

   boolean freeNotEmpty() {
      return this.getFreeCount() != 0;
   }

   public int getCapacity() {
      this.lock.lock();

      int var1;
      try {
         var1 = this.capacity;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public Buffer getFill() {
      this.assertNotLocked();
      if (!this.noMoreFill()) {
         int var1 = this.offset;
         Buffer var2 = this.elements[var1];
         this.offset = (this.offset + 1) % this.capacity;
         --this.length;
         this.locked = var1;
         return var2;
      } else {
         throw new IllegalStateException("noMoreFill");
      }
   }

   public Buffer getFill(int var1) {
      if (var1 >= 0 && var1 < this.length) {
         return this.elements[(this.offset + var1) % this.capacity];
      } else {
         throw new IndexOutOfBoundsException(Integer.toString(var1));
      }
   }

   public int getFillCount() {
      this.lock.lock();

      int var1;
      try {
         var1 = this.length;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public long getFirstSeq() {
      return this.length == 0 ? 9223372036854775806L : this.elements[this.offset].getSequenceNumber();
   }

   public Buffer getFree() {
      this.assertNotLocked();
      if (!this.noMoreFree()) {
         int var1 = (this.offset + this.length) % this.capacity;
         Buffer var2 = this.elements[var1];
         this.locked = var1;
         return var2;
      } else {
         throw new IllegalStateException("noMoreFree");
      }
   }

   public int getFreeCount() {
      return this.capacity - this.length;
   }

   public long getLastSeq() {
      int var1 = this.length;
      return var1 == 0 ? 9223372036854775806L : this.elements[(this.offset + var1 - 1) % this.capacity].getSequenceNumber();
   }

   boolean noMoreFill() {
      return this.getFillCount() == 0;
   }

   boolean noMoreFree() {
      return this.getFreeCount() == 0;
   }

   public void returnFree(Buffer var1) {
      this.assertLocked(var1);
      this.locked = -1;
   }

   public void setCapacity(int var1) {
      this.assertNotLocked();
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      } else if (this.capacity != var1) {
         Buffer[] var4 = new Buffer[var1];

         while(this.getFillCount() > var1) {
            this.dropFirstFill();
         }

         int var3 = Math.min(this.getFillCount(), var1);

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            var4[var2] = this.getFill(var2);
         }

         for(var2 = var3; var2 < var1; ++var2) {
            var4[var2] = new Buffer();
         }

         this.capacity = var1;
         this.elements = var4;
         this.length = var3;
         this.offset = 0;
      }
   }
}
