package okio;

import javax.annotation.Nullable;

final class Segment {
   static final int SHARE_MINIMUM = 1024;
   static final int SIZE = 8192;
   final byte[] data;
   int limit;
   Segment next;
   boolean owner;
   int pos;
   Segment prev;
   boolean shared;

   Segment() {
      this.data = new byte[8192];
      this.owner = true;
      this.shared = false;
   }

   Segment(Segment var1) {
      this(var1.data, var1.pos, var1.limit);
      var1.shared = true;
   }

   Segment(byte[] var1, int var2, int var3) {
      this.data = var1;
      this.pos = var2;
      this.limit = var3;
      this.owner = false;
      this.shared = true;
   }

   public void compact() {
      Segment var4 = this.prev;
      if (var4 != this) {
         if (var4.owner) {
            int var2 = this.limit - this.pos;
            int var3 = var4.limit;
            int var1;
            if (var4.shared) {
               var1 = 0;
            } else {
               var1 = var4.pos;
            }

            if (var2 <= 8192 - var3 + var1) {
               this.writeTo(this.prev, var2);
               this.pop();
               SegmentPool.recycle(this);
            }
         }
      } else {
         throw new IllegalStateException();
      }
   }

   @Nullable
   public Segment pop() {
      Segment var1 = this.next;
      if (var1 == this) {
         var1 = null;
      }

      Segment var2 = this.prev;
      var2.next = this.next;
      this.next.prev = var2;
      this.next = null;
      this.prev = null;
      return var1;
   }

   public Segment push(Segment var1) {
      var1.prev = this;
      var1.next = this.next;
      this.next.prev = var1;
      this.next = var1;
      return var1;
   }

   public Segment split(int var1) {
      if (var1 > 0 && var1 <= this.limit - this.pos) {
         Segment var2;
         if (var1 >= 1024) {
            var2 = new Segment(this);
         } else {
            var2 = SegmentPool.take();
            System.arraycopy(this.data, this.pos, var2.data, 0, var1);
         }

         var2.limit = var2.pos + var1;
         this.pos += var1;
         this.prev.push(var2);
         return var2;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void writeTo(Segment var1, int var2) {
      if (var1.owner) {
         int var3 = var1.limit;
         if (var3 + var2 > 8192) {
            if (var1.shared) {
               throw new IllegalArgumentException();
            }

            int var4 = var1.pos;
            if (var3 + var2 - var4 > 8192) {
               throw new IllegalArgumentException();
            }

            byte[] var5 = var1.data;
            System.arraycopy(var5, var4, var5, 0, var3 - var4);
            var1.limit -= var1.pos;
            var1.pos = 0;
         }

         System.arraycopy(this.data, this.pos, var1.data, var1.limit, var2);
         var1.limit += var2;
         this.pos += var2;
      } else {
         throw new IllegalArgumentException();
      }
   }
}
