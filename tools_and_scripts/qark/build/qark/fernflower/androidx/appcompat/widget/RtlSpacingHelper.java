package androidx.appcompat.widget;

class RtlSpacingHelper {
   public static final int UNDEFINED = Integer.MIN_VALUE;
   private int mEnd = Integer.MIN_VALUE;
   private int mExplicitLeft = 0;
   private int mExplicitRight = 0;
   private boolean mIsRelative = false;
   private boolean mIsRtl = false;
   private int mLeft = 0;
   private int mRight = 0;
   private int mStart = Integer.MIN_VALUE;

   public int getEnd() {
      return this.mIsRtl ? this.mLeft : this.mRight;
   }

   public int getLeft() {
      return this.mLeft;
   }

   public int getRight() {
      return this.mRight;
   }

   public int getStart() {
      return this.mIsRtl ? this.mRight : this.mLeft;
   }

   public void setAbsolute(int var1, int var2) {
      this.mIsRelative = false;
      if (var1 != Integer.MIN_VALUE) {
         this.mExplicitLeft = var1;
         this.mLeft = var1;
      }

      if (var2 != Integer.MIN_VALUE) {
         this.mExplicitRight = var2;
         this.mRight = var2;
      }

   }

   public void setDirection(boolean var1) {
      if (var1 != this.mIsRtl) {
         this.mIsRtl = var1;
         if (this.mIsRelative) {
            int var2;
            if (var1) {
               var2 = this.mEnd;
               if (var2 == Integer.MIN_VALUE) {
                  var2 = this.mExplicitLeft;
               }

               this.mLeft = var2;
               var2 = this.mStart;
               if (var2 == Integer.MIN_VALUE) {
                  var2 = this.mExplicitRight;
               }

               this.mRight = var2;
            } else {
               var2 = this.mStart;
               if (var2 == Integer.MIN_VALUE) {
                  var2 = this.mExplicitLeft;
               }

               this.mLeft = var2;
               var2 = this.mEnd;
               if (var2 == Integer.MIN_VALUE) {
                  var2 = this.mExplicitRight;
               }

               this.mRight = var2;
            }
         } else {
            this.mLeft = this.mExplicitLeft;
            this.mRight = this.mExplicitRight;
         }
      }
   }

   public void setRelative(int var1, int var2) {
      this.mStart = var1;
      this.mEnd = var2;
      this.mIsRelative = true;
      if (this.mIsRtl) {
         if (var2 != Integer.MIN_VALUE) {
            this.mLeft = var2;
         }

         if (var1 != Integer.MIN_VALUE) {
            this.mRight = var1;
            return;
         }
      } else {
         if (var1 != Integer.MIN_VALUE) {
            this.mLeft = var1;
         }

         if (var2 != Integer.MIN_VALUE) {
            this.mRight = var2;
         }
      }

   }
}
