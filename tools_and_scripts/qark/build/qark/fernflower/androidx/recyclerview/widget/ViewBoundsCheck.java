package androidx.recyclerview.widget;

import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class ViewBoundsCheck {
   static final int CVE_PVE_POS = 12;
   static final int CVE_PVS_POS = 8;
   static final int CVS_PVE_POS = 4;
   static final int CVS_PVS_POS = 0;
   // $FF: renamed from: EQ int
   static final int field_181 = 2;
   static final int FLAG_CVE_EQ_PVE = 8192;
   static final int FLAG_CVE_EQ_PVS = 512;
   static final int FLAG_CVE_GT_PVE = 4096;
   static final int FLAG_CVE_GT_PVS = 256;
   static final int FLAG_CVE_LT_PVE = 16384;
   static final int FLAG_CVE_LT_PVS = 1024;
   static final int FLAG_CVS_EQ_PVE = 32;
   static final int FLAG_CVS_EQ_PVS = 2;
   static final int FLAG_CVS_GT_PVE = 16;
   static final int FLAG_CVS_GT_PVS = 1;
   static final int FLAG_CVS_LT_PVE = 64;
   static final int FLAG_CVS_LT_PVS = 4;
   // $FF: renamed from: GT int
   static final int field_182 = 1;
   // $FF: renamed from: LT int
   static final int field_183 = 4;
   static final int MASK = 7;
   ViewBoundsCheck.BoundFlags mBoundFlags;
   final ViewBoundsCheck.Callback mCallback;

   ViewBoundsCheck(ViewBoundsCheck.Callback var1) {
      this.mCallback = var1;
      this.mBoundFlags = new ViewBoundsCheck.BoundFlags();
   }

   View findOneViewWithinBoundFlags(int var1, int var2, int var3, int var4) {
      int var6 = this.mCallback.getParentStart();
      int var7 = this.mCallback.getParentEnd();
      byte var5;
      if (var2 > var1) {
         var5 = 1;
      } else {
         var5 = -1;
      }

      View var10;
      View var11;
      for(var10 = null; var1 != var2; var10 = var11) {
         View var12 = this.mCallback.getChildAt(var1);
         int var8 = this.mCallback.getChildStart(var12);
         int var9 = this.mCallback.getChildEnd(var12);
         this.mBoundFlags.setBounds(var6, var7, var8, var9);
         if (var3 != 0) {
            this.mBoundFlags.resetFlags();
            this.mBoundFlags.addFlags(var3);
            if (this.mBoundFlags.boundsMatch()) {
               return var12;
            }
         }

         var11 = var10;
         if (var4 != 0) {
            this.mBoundFlags.resetFlags();
            this.mBoundFlags.addFlags(var4);
            var11 = var10;
            if (this.mBoundFlags.boundsMatch()) {
               var11 = var12;
            }
         }

         var1 += var5;
      }

      return var10;
   }

   boolean isViewWithinBoundFlags(View var1, int var2) {
      this.mBoundFlags.setBounds(this.mCallback.getParentStart(), this.mCallback.getParentEnd(), this.mCallback.getChildStart(var1), this.mCallback.getChildEnd(var1));
      if (var2 != 0) {
         this.mBoundFlags.resetFlags();
         this.mBoundFlags.addFlags(var2);
         return this.mBoundFlags.boundsMatch();
      } else {
         return false;
      }
   }

   static class BoundFlags {
      int mBoundFlags = 0;
      int mChildEnd;
      int mChildStart;
      int mRvEnd;
      int mRvStart;

      void addFlags(int var1) {
         this.mBoundFlags |= var1;
      }

      boolean boundsMatch() {
         int var1 = this.mBoundFlags;
         if ((var1 & 7) != 0 && (var1 & this.compare(this.mChildStart, this.mRvStart) << 0) == 0) {
            return false;
         } else {
            var1 = this.mBoundFlags;
            if ((var1 & 112) != 0 && (var1 & this.compare(this.mChildStart, this.mRvEnd) << 4) == 0) {
               return false;
            } else {
               var1 = this.mBoundFlags;
               if ((var1 & 1792) != 0 && (var1 & this.compare(this.mChildEnd, this.mRvStart) << 8) == 0) {
                  return false;
               } else {
                  var1 = this.mBoundFlags;
                  return (var1 & 28672) == 0 || (var1 & this.compare(this.mChildEnd, this.mRvEnd) << 12) != 0;
               }
            }
         }
      }

      int compare(int var1, int var2) {
         if (var1 > var2) {
            return 1;
         } else {
            return var1 == var2 ? 2 : 4;
         }
      }

      void resetFlags() {
         this.mBoundFlags = 0;
      }

      void setBounds(int var1, int var2, int var3, int var4) {
         this.mRvStart = var1;
         this.mRvEnd = var2;
         this.mChildStart = var3;
         this.mChildEnd = var4;
      }
   }

   interface Callback {
      View getChildAt(int var1);

      int getChildEnd(View var1);

      int getChildStart(View var1);

      int getParentEnd();

      int getParentStart();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface ViewBounds {
   }
}
