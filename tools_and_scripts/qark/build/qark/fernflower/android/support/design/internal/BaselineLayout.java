package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
   private int mBaseline = -1;

   public BaselineLayout(Context var1) {
      super(var1, (AttributeSet)null, 0);
   }

   public BaselineLayout(Context var1, AttributeSet var2) {
      super(var1, var2, 0);
   }

   public BaselineLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public int getBaseline() {
      return this.mBaseline;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var7 = this.getChildCount();
      int var8 = this.getPaddingLeft();
      int var9 = this.getPaddingRight();
      int var6 = this.getPaddingTop();

      for(var3 = 0; var3 < var7; ++var3) {
         View var13 = this.getChildAt(var3);
         if (var13.getVisibility() != 8) {
            int var10 = var13.getMeasuredWidth();
            int var11 = var13.getMeasuredHeight();
            int var12 = (var4 - var2 - var9 - var8 - var10) / 2 + var8;
            if (this.mBaseline != -1 && var13.getBaseline() != -1) {
               var5 = this.mBaseline + var6 - var13.getBaseline();
            } else {
               var5 = var6;
            }

            var13.layout(var12, var5, var12 + var10, var5 + var11);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var9 = this.getChildCount();
      int var8 = 0;
      int var3 = 0;
      int var4 = -1;
      int var5 = -1;
      int var6 = 0;

      for(int var7 = 0; var7 < var9; ++var7) {
         View var11 = this.getChildAt(var7);
         if (var11.getVisibility() != 8) {
            this.measureChild(var11, var1, var2);
            int var10 = var11.getBaseline();
            if (var10 != -1) {
               var4 = Math.max(var4, var10);
               var5 = Math.max(var5, var11.getMeasuredHeight() - var10);
            }

            var8 = Math.max(var8, var11.getMeasuredWidth());
            var3 = Math.max(var3, var11.getMeasuredHeight());
            var6 = View.combineMeasuredStates(var6, var11.getMeasuredState());
         }
      }

      if (var4 != -1) {
         var3 = Math.max(var3, var4 + Math.max(var5, this.getPaddingBottom()));
         this.mBaseline = var4;
      }

      var3 = Math.max(var3, this.getSuggestedMinimumHeight());
      this.setMeasuredDimension(View.resolveSizeAndState(Math.max(var8, this.getSuggestedMinimumWidth()), var1, var6), View.resolveSizeAndState(var3, var2, var6 << 16));
   }
}
