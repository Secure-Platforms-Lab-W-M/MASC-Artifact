package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

class ViewOffsetBehavior extends CoordinatorLayout.Behavior {
   private int tempLeftRightOffset = 0;
   private int tempTopBottomOffset = 0;
   private ViewOffsetHelper viewOffsetHelper;

   public ViewOffsetBehavior() {
   }

   public ViewOffsetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public int getLeftAndRightOffset() {
      ViewOffsetHelper var1 = this.viewOffsetHelper;
      return var1 != null ? var1.getLeftAndRightOffset() : 0;
   }

   public int getTopAndBottomOffset() {
      ViewOffsetHelper var1 = this.viewOffsetHelper;
      return var1 != null ? var1.getTopAndBottomOffset() : 0;
   }

   public boolean isHorizontalOffsetEnabled() {
      ViewOffsetHelper var1 = this.viewOffsetHelper;
      return var1 != null && var1.isHorizontalOffsetEnabled();
   }

   public boolean isVerticalOffsetEnabled() {
      ViewOffsetHelper var1 = this.viewOffsetHelper;
      return var1 != null && var1.isVerticalOffsetEnabled();
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      var1.onLayoutChild(var2, var3);
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      this.layoutChild(var1, var2, var3);
      if (this.viewOffsetHelper == null) {
         this.viewOffsetHelper = new ViewOffsetHelper(var2);
      }

      this.viewOffsetHelper.onViewLayout();
      this.viewOffsetHelper.applyOffsets();
      var3 = this.tempTopBottomOffset;
      if (var3 != 0) {
         this.viewOffsetHelper.setTopAndBottomOffset(var3);
         this.tempTopBottomOffset = 0;
      }

      var3 = this.tempLeftRightOffset;
      if (var3 != 0) {
         this.viewOffsetHelper.setLeftAndRightOffset(var3);
         this.tempLeftRightOffset = 0;
      }

      return true;
   }

   public void setHorizontalOffsetEnabled(boolean var1) {
      ViewOffsetHelper var2 = this.viewOffsetHelper;
      if (var2 != null) {
         var2.setHorizontalOffsetEnabled(var1);
      }

   }

   public boolean setLeftAndRightOffset(int var1) {
      ViewOffsetHelper var2 = this.viewOffsetHelper;
      if (var2 != null) {
         return var2.setLeftAndRightOffset(var1);
      } else {
         this.tempLeftRightOffset = var1;
         return false;
      }
   }

   public boolean setTopAndBottomOffset(int var1) {
      ViewOffsetHelper var2 = this.viewOffsetHelper;
      if (var2 != null) {
         return var2.setTopAndBottomOffset(var1);
      } else {
         this.tempTopBottomOffset = var1;
         return false;
      }
   }

   public void setVerticalOffsetEnabled(boolean var1) {
      ViewOffsetHelper var2 = this.viewOffsetHelper;
      if (var2 != null) {
         var2.setVerticalOffsetEnabled(var1);
      }

   }
}
