package androidx.collection;

public final class CircularArray {
   private int mCapacityBitmask;
   private Object[] mElements;
   private int mHead;
   private int mTail;

   public CircularArray() {
      this(8);
   }

   public CircularArray(int var1) {
      if (var1 >= 1) {
         if (var1 <= 1073741824) {
            if (Integer.bitCount(var1) != 1) {
               var1 = Integer.highestOneBit(var1 - 1) << 1;
            }

            this.mCapacityBitmask = var1 - 1;
            this.mElements = (Object[])(new Object[var1]);
         } else {
            throw new IllegalArgumentException("capacity must be <= 2^30");
         }
      } else {
         throw new IllegalArgumentException("capacity must be >= 1");
      }
   }

   private void doubleCapacity() {
      Object[] var5 = this.mElements;
      int var1 = var5.length;
      int var2 = this.mHead;
      int var3 = var1 - var2;
      int var4 = var1 << 1;
      if (var4 >= 0) {
         Object[] var6 = new Object[var4];
         System.arraycopy(var5, var2, var6, 0, var3);
         System.arraycopy(this.mElements, 0, var6, var3, this.mHead);
         this.mElements = (Object[])var6;
         this.mHead = 0;
         this.mTail = var1;
         this.mCapacityBitmask = var4 - 1;
      } else {
         throw new RuntimeException("Max array capacity exceeded");
      }
   }

   public void addFirst(Object var1) {
      int var2 = this.mHead - 1 & this.mCapacityBitmask;
      this.mHead = var2;
      this.mElements[var2] = var1;
      if (var2 == this.mTail) {
         this.doubleCapacity();
      }

   }

   public void addLast(Object var1) {
      Object[] var3 = this.mElements;
      int var2 = this.mTail;
      var3[var2] = var1;
      var2 = this.mCapacityBitmask & var2 + 1;
      this.mTail = var2;
      if (var2 == this.mHead) {
         this.doubleCapacity();
      }

   }

   public void clear() {
      this.removeFromStart(this.size());
   }

   public Object get(int var1) {
      if (var1 >= 0 && var1 < this.size()) {
         return this.mElements[this.mHead + var1 & this.mCapacityBitmask];
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public Object getFirst() {
      int var1 = this.mHead;
      if (var1 != this.mTail) {
         return this.mElements[var1];
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public Object getLast() {
      int var1 = this.mHead;
      int var2 = this.mTail;
      if (var1 != var2) {
         return this.mElements[var2 - 1 & this.mCapacityBitmask];
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public boolean isEmpty() {
      return this.mHead == this.mTail;
   }

   public Object popFirst() {
      int var1 = this.mHead;
      if (var1 != this.mTail) {
         Object[] var2 = this.mElements;
         Object var3 = var2[var1];
         var2[var1] = null;
         this.mHead = var1 + 1 & this.mCapacityBitmask;
         return var3;
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public Object popLast() {
      int var1 = this.mHead;
      int var2 = this.mTail;
      if (var1 != var2) {
         var1 = this.mCapacityBitmask & var2 - 1;
         Object[] var3 = this.mElements;
         Object var4 = var3[var1];
         var3[var1] = null;
         this.mTail = var1;
         return var4;
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public void removeFromEnd(int var1) {
      if (var1 > 0) {
         if (var1 > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
         } else {
            int var2 = 0;
            int var3 = this.mTail;
            if (var1 < var3) {
               var2 = var3 - var1;
            }

            var3 = var2;

            while(true) {
               int var4 = this.mTail;
               if (var3 >= var4) {
                  var2 = var4 - var2;
                  var1 -= var2;
                  this.mTail = var4 - var2;
                  if (var1 > 0) {
                     var2 = this.mElements.length;
                     this.mTail = var2;
                     var2 -= var1;

                     for(var1 = var2; var1 < this.mTail; ++var1) {
                        this.mElements[var1] = null;
                     }

                     this.mTail = var2;
                  }

                  return;
               }

               this.mElements[var3] = null;
               ++var3;
            }
         }
      }
   }

   public void removeFromStart(int var1) {
      if (var1 > 0) {
         if (var1 > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
         } else {
            int var3 = this.mElements.length;
            int var4 = this.mHead;
            int var2 = var3;
            if (var1 < var3 - var4) {
               var2 = var4 + var1;
            }

            for(var3 = this.mHead; var3 < var2; ++var3) {
               this.mElements[var3] = null;
            }

            var3 = this.mHead;
            var4 = var2 - var3;
            var2 = var1 - var4;
            this.mHead = var3 + var4 & this.mCapacityBitmask;
            if (var2 > 0) {
               for(var1 = 0; var1 < var2; ++var1) {
                  this.mElements[var1] = null;
               }

               this.mHead = var2;
            }

         }
      }
   }

   public int size() {
      return this.mTail - this.mHead & this.mCapacityBitmask;
   }
}
