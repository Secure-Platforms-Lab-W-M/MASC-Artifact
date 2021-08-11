package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class ViewOffsetBehavior extends CoordinatorLayout.Behavior {
   private int mTempLeftRightOffset = 0;
   private int mTempTopBottomOffset = 0;
   private ViewOffsetHelper mViewOffsetHelper;

   public ViewOffsetBehavior() {
   }

   public ViewOffsetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public int getLeftAndRightOffset() {
      ViewOffsetHelper var1 = this.mViewOffsetHelper;
      return var1 != null ? var1.getLeftAndRightOffset() : 0;
   }

   public int getTopAndBottomOffset() {
      ViewOffsetHelper var1 = this.mViewOffsetHelper;
      return var1 != null ? var1.getTopAndBottomOffset() : 0;
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      var1.onLayoutChild(var2, var3);
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      this.layoutChild(var1, var2, var3);
      if (this.mViewOffsetHelper == null) {
         this.mViewOffsetHelper = new ViewOffsetHelper(var2);
      }

      this.mViewOffsetHelper.onViewLayout();
      var3 = this.mTempTopBottomOffset;
      if (var3 != 0) {
         this.mViewOffsetHelper.setTopAndBottomOffset(var3);
         this.mTempTopBottomOffset = 0;
      }

      var3 = this.mTempLeftRightOffset;
      if (var3 != 0) {
         this.mViewOffsetHelper.setLeftAndRightOffset(var3);
         this.mTempLeftRightOffset = 0;
      }

      return true;
   }

   public boolean setLeftAndRightOffset(int var1) {
      ViewOffsetHelper var2 = this.mViewOffsetHelper;
      if (var2 != null) {
         return var2.setLeftAndRightOffset(var1);
      } else {
         this.mTempLeftRightOffset = var1;
         return false;
      }
   }

   public boolean setTopAndBottomOffset(int var1) {
      ViewOffsetHelper var2 = this.mViewOffsetHelper;
      if (var2 != null) {
         return var2.setTopAndBottomOffset(var1);
      } else {
         this.mTempTopBottomOffset = var1;
         return false;
      }
   }
}
