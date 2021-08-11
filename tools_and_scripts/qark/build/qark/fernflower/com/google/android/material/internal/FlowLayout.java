package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.styleable;

public class FlowLayout extends ViewGroup {
   private int itemSpacing;
   private int lineSpacing;
   private boolean singleLine;

   public FlowLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public FlowLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public FlowLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.singleLine = false;
      this.loadFromAttributes(var1, var2);
   }

   public FlowLayout(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.singleLine = false;
      this.loadFromAttributes(var1, var2);
   }

   private static int getMeasuredDimension(int var0, int var1, int var2) {
      if (var1 != Integer.MIN_VALUE) {
         return var1 != 1073741824 ? var2 : var0;
      } else {
         return Math.min(var2, var0);
      }
   }

   private void loadFromAttributes(Context var1, AttributeSet var2) {
      TypedArray var3 = var1.getTheme().obtainStyledAttributes(var2, styleable.FlowLayout, 0, 0);
      this.lineSpacing = var3.getDimensionPixelSize(styleable.FlowLayout_lineSpacing, 0);
      this.itemSpacing = var3.getDimensionPixelSize(styleable.FlowLayout_itemSpacing, 0);
      var3.recycle();
   }

   protected int getItemSpacing() {
      return this.itemSpacing;
   }

   protected int getLineSpacing() {
      return this.lineSpacing;
   }

   public boolean isSingleLine() {
      return this.singleLine;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.getChildCount() != 0) {
         var3 = ViewCompat.getLayoutDirection(this);
         boolean var6 = true;
         if (var3 != 1) {
            var6 = false;
         }

         if (var6) {
            var3 = this.getPaddingRight();
         } else {
            var3 = this.getPaddingLeft();
         }

         int var7;
         if (var6) {
            var7 = this.getPaddingLeft();
         } else {
            var7 = this.getPaddingRight();
         }

         int var9 = this.getPaddingTop();
         int var8 = var9;
         int var12 = var4 - var2 - var7;
         var7 = 0;
         var2 = var9;

         for(var4 = var3; var7 < this.getChildCount(); ++var7) {
            View var14 = this.getChildAt(var7);
            if (var14.getVisibility() != 8) {
               LayoutParams var15 = var14.getLayoutParams();
               var9 = 0;
               int var10 = 0;
               if (var15 instanceof MarginLayoutParams) {
                  MarginLayoutParams var16 = (MarginLayoutParams)var15;
                  var9 = MarginLayoutParamsCompat.getMarginStart(var16);
                  var10 = MarginLayoutParamsCompat.getMarginEnd(var16);
               }

               int var13 = var14.getMeasuredWidth();
               int var11 = var4;
               var5 = var2;
               if (!this.singleLine) {
                  var11 = var4;
                  var5 = var2;
                  if (var4 + var9 + var13 > var12) {
                     var11 = var3;
                     var5 = var8 + this.lineSpacing;
                  }
               }

               var2 = var11 + var9 + var14.getMeasuredWidth();
               var8 = var14.getMeasuredHeight() + var5;
               if (var6) {
                  var14.layout(var12 - var2, var5, var12 - var11 - var9, var8);
               } else {
                  var14.layout(var11 + var9, var5, var2, var8);
               }

               var4 = var11 + var9 + var10 + var14.getMeasuredWidth() + this.itemSpacing;
               var2 = var5;
            }
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      int var13 = MeasureSpec.getSize(var1);
      int var15 = MeasureSpec.getMode(var1);
      int var16 = MeasureSpec.getSize(var2);
      int var17 = MeasureSpec.getMode(var2);
      int var3;
      if (var15 != Integer.MIN_VALUE && var15 != 1073741824) {
         var3 = Integer.MAX_VALUE;
      } else {
         var3 = var13;
      }

      int var5 = this.getPaddingLeft();
      int var6 = this.getPaddingTop();
      int var7 = var6;
      int var4 = 0;
      int var18 = this.getPaddingRight();

      for(int var9 = 0; var9 < this.getChildCount(); ++var9) {
         View var19 = this.getChildAt(var9);
         if (var19.getVisibility() != 8) {
            this.measureChild(var19, var1, var2);
            LayoutParams var20 = var19.getLayoutParams();
            int var11 = 0;
            int var10 = 0;
            if (var20 instanceof MarginLayoutParams) {
               MarginLayoutParams var21 = (MarginLayoutParams)var20;
               var11 = 0 + var21.leftMargin;
               var10 = 0 + var21.rightMargin;
            }

            int var12;
            if (var5 + var11 + var19.getMeasuredWidth() > var3 - var18 && !this.isSingleLine()) {
               var12 = this.getPaddingLeft();
               var6 = this.lineSpacing + var7;
            } else {
               var12 = var5;
            }

            int var14 = var12 + var11 + var19.getMeasuredWidth();
            var7 = var19.getMeasuredHeight() + var6;
            var5 = var4;
            if (var14 > var4) {
               var5 = var14;
            }

            var11 = var12 + var11 + var10 + var19.getMeasuredWidth() + this.itemSpacing;
            if (var9 == this.getChildCount() - 1) {
               var4 = var5 + var10;
               var5 = var11;
            } else {
               var4 = var5;
               var5 = var11;
            }
         }
      }

      var1 = this.getPaddingRight();
      var2 = this.getPaddingBottom();
      this.setMeasuredDimension(getMeasuredDimension(var13, var15, var4 + var1), getMeasuredDimension(var16, var17, var7 + var2));
   }

   protected void setItemSpacing(int var1) {
      this.itemSpacing = var1;
   }

   protected void setLineSpacing(int var1) {
      this.lineSpacing = var1;
   }

   public void setSingleLine(boolean var1) {
      this.singleLine = var1;
   }
}
