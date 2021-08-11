package android.support.design.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.math.MathUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.List;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior {
   private int mOverlayTop;
   final Rect mTempRect1 = new Rect();
   final Rect mTempRect2 = new Rect();
   private int mVerticalLayoutGap = 0;

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
      if (this.mOverlayTop == 0) {
         return 0;
      } else {
         float var2 = this.getOverlapRatioForOffset(var1);
         int var3 = this.mOverlayTop;
         return MathUtils.clamp((int)(var2 * (float)var3), 0, var3);
      }
   }

   float getOverlapRatioForOffset(View var1) {
      return 1.0F;
   }

   public final int getOverlayTop() {
      return this.mOverlayTop;
   }

   int getScrollRange(View var1) {
      return var1.getMeasuredHeight();
   }

   final int getVerticalLayoutGap() {
      return this.mVerticalLayoutGap;
   }

   protected void layoutChild(CoordinatorLayout var1, View var2, int var3) {
      View var4 = this.findFirstDependency(var1.getDependencies(var2));
      if (var4 != null) {
         CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         Rect var6 = this.mTempRect1;
         var6.set(var1.getPaddingLeft() + var5.leftMargin, var4.getBottom() + var5.topMargin, var1.getWidth() - var1.getPaddingRight() - var5.rightMargin, var1.getHeight() + var4.getBottom() - var1.getPaddingBottom() - var5.bottomMargin);
         WindowInsetsCompat var7 = var1.getLastWindowInsets();
         if (var7 != null && ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
            var6.left += var7.getSystemWindowInsetLeft();
            var6.right -= var7.getSystemWindowInsetRight();
         }

         Rect var8 = this.mTempRect2;
         GravityCompat.apply(resolveGravity(var5.gravity), var2.getMeasuredWidth(), var2.getMeasuredHeight(), var6, var8, var3);
         var3 = this.getOverlapPixelsForOffset(var4);
         var2.layout(var8.left, var8.top - var3, var8.right, var8.bottom - var3);
         this.mVerticalLayoutGap = var8.top - var4.getBottom();
      } else {
         super.layoutChild(var1, var2, var3);
         this.mVerticalLayoutGap = 0;
      }
   }

   public boolean onMeasureChild(CoordinatorLayout var1, View var2, int var3, int var4, int var5, int var6) {
      int var7 = var2.getLayoutParams().height;
      if (var7 == -1 || var7 == -2) {
         View var10 = this.findFirstDependency(var1.getDependencies(var2));
         if (var10 != null) {
            if (ViewCompat.getFitsSystemWindows(var10) && !ViewCompat.getFitsSystemWindows(var2)) {
               ViewCompat.setFitsSystemWindows(var2, true);
               if (ViewCompat.getFitsSystemWindows(var2)) {
                  var2.requestLayout();
                  return true;
               }
            }

            var5 = MeasureSpec.getSize(var5);
            if (var5 == 0) {
               var5 = var1.getHeight();
            }

            int var8 = var10.getMeasuredHeight();
            int var9 = this.getScrollRange(var10);
            if (var7 == -1) {
               var7 = 1073741824;
            } else {
               var7 = Integer.MIN_VALUE;
            }

            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(var5 - var8 + var9, var7), var6);
            return true;
         }
      }

      return false;
   }

   public final void setOverlayTop(int var1) {
      this.mOverlayTop = var1;
   }
}
