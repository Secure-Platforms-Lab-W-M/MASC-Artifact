package com.google.android.material.appbar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior {
   private int overlayTop;
   final Rect tempRect1 = new Rect();
   final Rect tempRect2 = new Rect();
   private int verticalLayoutGap = 0;

   public HeaderScrollingViewBehavior() {
   }

   public HeaderScrollingViewBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private static int resolveGravity(int var0) {
      return var0 == 0 ? 8388659 : var0;
   }

   abstract View findFirstDependency(List var1);

   final int getOverlapPixelsForOffset(View var1) {
      if (this.overlayTop == 0) {
         return 0;
      } else {
         float var2 = this.getOverlapRatioForOffset(var1);
         int var3 = this.overlayTop;
         return MathUtils.clamp((int)(var2 * (float)var3), 0, var3);
      }
   }

   float getOverlapRatioForOffset(View var1) {
      return 1.0F;
   }

   public final int getOverlayTop() {
      return this.overlayTop;
   }

   int getScrollRange(View var1) {
      return var1.getMeasuredHeight();
   }

   final int getVerticalLayoutGap() {
      return this.verticalLayoutGap;
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      View var4 = this.findFirstDependency(var1.getDependencies(var2));
      if (var4 != null) {
         CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         Rect var6 = this.tempRect1;
         var6.set(var1.getPaddingLeft() + var5.leftMargin, var4.getBottom() + var5.topMargin, var1.getWidth() - var1.getPaddingRight() - var5.rightMargin, var1.getHeight() + var4.getBottom() - var1.getPaddingBottom() - var5.bottomMargin);
         WindowInsetsCompat var7 = var1.getLastWindowInsets();
         if (var7 != null && ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
            var6.left += var7.getSystemWindowInsetLeft();
            var6.right -= var7.getSystemWindowInsetRight();
         }

         Rect var8 = this.tempRect2;
         GravityCompat.apply(resolveGravity(var5.gravity), var2.getMeasuredWidth(), var2.getMeasuredHeight(), var6, var8, var3);
         var3 = this.getOverlapPixelsForOffset(var4);
         var2.layout(var8.left, var8.top - var3, var8.right, var8.bottom - var3);
         this.verticalLayoutGap = var8.top - var4.getBottom();
      } else {
         super.layoutChild(var1, var2, var3);
         this.verticalLayoutGap = 0;
      }
   }

   public boolean onMeasureChild(CoordinatorLayout var1, View var2, int var3, int var4, int var5, int var6) {
      int var8 = var2.getLayoutParams().height;
      if (var8 == -1 || var8 == -2) {
         View var9 = this.findFirstDependency(var1.getDependencies(var2));
         if (var9 != null) {
            int var7 = MeasureSpec.getSize(var5);
            if (var7 > 0) {
               var5 = var7;
               if (ViewCompat.getFitsSystemWindows(var9)) {
                  WindowInsetsCompat var10 = var1.getLastWindowInsets();
                  var5 = var7;
                  if (var10 != null) {
                     var5 = var7 + var10.getSystemWindowInsetTop() + var10.getSystemWindowInsetBottom();
                  }
               }
            } else {
               var5 = var1.getHeight();
            }

            var5 += this.getScrollRange(var9);
            var7 = var9.getMeasuredHeight();
            if (this.shouldHeaderOverlapScrollingChild()) {
               var2.setTranslationY((float)(-var7));
            } else {
               var5 -= var7;
            }

            if (var8 == -1) {
               var7 = 1073741824;
            } else {
               var7 = Integer.MIN_VALUE;
            }

            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(var5, var7), var6);
            return true;
         }
      }

      return false;
   }

   public final void setOverlayTop(int var1) {
      this.overlayTop = var1;
   }

   protected boolean shouldHeaderOverlapScrollingChild() {
      return false;
   }
}
