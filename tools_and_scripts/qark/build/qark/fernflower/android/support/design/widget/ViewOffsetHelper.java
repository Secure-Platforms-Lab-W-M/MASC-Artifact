package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

class ViewOffsetHelper {
   private int mLayoutLeft;
   private int mLayoutTop;
   private int mOffsetLeft;
   private int mOffsetTop;
   private final View mView;

   public ViewOffsetHelper(View var1) {
      this.mView = var1;
   }

   private void updateOffsets() {
      View var1 = this.mView;
      ViewCompat.offsetTopAndBottom(var1, this.mOffsetTop - (var1.getTop() - this.mLayoutTop));
      var1 = this.mView;
      ViewCompat.offsetLeftAndRight(var1, this.mOffsetLeft - (var1.getLeft() - this.mLayoutLeft));
   }

   public int getLayoutLeft() {
      return this.mLayoutLeft;
   }

   public int getLayoutTop() {
      return this.mLayoutTop;
   }

   public int getLeftAndRightOffset() {
      return this.mOffsetLeft;
   }

   public int getTopAndBottomOffset() {
      return this.mOffsetTop;
   }

   public void onViewLayout() {
      this.mLayoutTop = this.mView.getTop();
      this.mLayoutLeft = this.mView.getLeft();
      this.updateOffsets();
   }

   public boolean setLeftAndRightOffset(int var1) {
      if (this.mOffsetLeft != var1) {
         this.mOffsetLeft = var1;
         this.updateOffsets();
         return true;
      } else {
         return false;
      }
   }

   public boolean setTopAndBottomOffset(int var1) {
      if (this.mOffsetTop != var1) {
         this.mOffsetTop = var1;
         this.updateOffsets();
         return true;
      } else {
         return false;
      }
   }
}
