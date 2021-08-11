package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import androidx.appcompat.R.id;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class AlertDialogLayout extends LinearLayoutCompat {
   public AlertDialogLayout(Context var1) {
      super(var1);
   }

   public AlertDialogLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void forceUniformWidth(int var1, int var2) {
      int var4 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);

      for(int var3 = 0; var3 < var1; ++var3) {
         View var6 = this.getChildAt(var3);
         if (var6.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            if (var7.width == -1) {
               int var5 = var7.height;
               var7.height = var6.getMeasuredHeight();
               this.measureChildWithMargins(var6, var4, 0, var2, 0);
               var7.height = var5;
            }
         }
      }

   }

   private static int resolveMinimumHeight(View var0) {
      int var1 = ViewCompat.getMinimumHeight(var0);
      if (var1 > 0) {
         return var1;
      } else {
         if (var0 instanceof ViewGroup) {
            ViewGroup var2 = (ViewGroup)var0;
            if (var2.getChildCount() == 1) {
               return resolveMinimumHeight(var2.getChildAt(0));
            }
         }

         return 0;
      }
   }

   private void setChildFrame(View var1, int var2, int var3, int var4, int var5) {
      var1.layout(var2, var3, var2 + var4, var3 + var5);
   }

   private boolean tryOnMeasure(int var1, int var2) {
      View var18 = null;
      View var15 = null;
      View var16 = null;
      int var12 = this.getChildCount();

      int var3;
      int var4;
      View var17;
      for(var3 = 0; var3 < var12; ++var3) {
         var17 = this.getChildAt(var3);
         if (var17.getVisibility() != 8) {
            var4 = var17.getId();
            if (var4 == id.topPanel) {
               var18 = var17;
            } else if (var4 == id.buttonPanel) {
               var15 = var17;
            } else {
               if (var4 != id.contentPanel && var4 != id.customPanel) {
                  return false;
               }

               if (var16 != null) {
                  return false;
               }

               var16 = var17;
            }
         }
      }

      int var14 = MeasureSpec.getMode(var2);
      int var8 = MeasureSpec.getSize(var2);
      int var13 = MeasureSpec.getMode(var1);
      int var6 = 0;
      var3 = this.getPaddingTop() + this.getPaddingBottom();
      int var7 = var3;
      if (var18 != null) {
         var18.measure(var1, 0);
         var7 = var3 + var18.getMeasuredHeight();
         var6 = View.combineMeasuredStates(0, var18.getMeasuredState());
      }

      var3 = 0;
      int var9 = 0;
      var4 = var6;
      int var5 = var7;
      if (var15 != null) {
         var15.measure(var1, 0);
         var3 = resolveMinimumHeight(var15);
         var9 = var15.getMeasuredHeight() - var3;
         var5 = var7 + var3;
         var4 = View.combineMeasuredStates(var6, var15.getMeasuredState());
      }

      int var10 = 0;
      if (var16 != null) {
         if (var14 == 0) {
            var6 = 0;
         } else {
            var6 = MeasureSpec.makeMeasureSpec(Math.max(0, var8 - var5), var14);
         }

         var16.measure(var1, var6);
         var10 = var16.getMeasuredHeight();
         var5 += var10;
         var4 = View.combineMeasuredStates(var4, var16.getMeasuredState());
      }

      int var11 = var8 - var5;
      var6 = var11;
      var7 = var4;
      var8 = var5;
      if (var15 != null) {
         var8 = Math.min(var11, var9);
         var6 = var11;
         var7 = var3;
         if (var8 > 0) {
            var6 = var11 - var8;
            var7 = var3 + var8;
         }

         var15.measure(var1, MeasureSpec.makeMeasureSpec(var7, 1073741824));
         var8 = var5 - var3 + var15.getMeasuredHeight();
         var7 = View.combineMeasuredStates(var4, var15.getMeasuredState());
      }

      var4 = var7;
      var3 = var8;
      if (var16 != null) {
         var4 = var7;
         var3 = var8;
         if (var6 > 0) {
            var16.measure(var1, MeasureSpec.makeMeasureSpec(var10 + var6, var14));
            var3 = var8 - var10 + var16.getMeasuredHeight();
            var4 = View.combineMeasuredStates(var7, var16.getMeasuredState());
            var5 = var6 - var6;
         }
      }

      var6 = 0;

      for(var5 = 0; var5 < var12; var6 = var7) {
         var17 = this.getChildAt(var5);
         var7 = var6;
         if (var17.getVisibility() != 8) {
            var7 = Math.max(var6, var17.getMeasuredWidth());
         }

         ++var5;
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var6 + this.getPaddingLeft() + this.getPaddingRight(), var1, var4), View.resolveSizeAndState(var3, var2, 0));
      if (var13 != 1073741824) {
         this.forceUniformWidth(var12, var2);
      }

      return true;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var7 = this.getPaddingLeft();
      int var8 = var4 - var2;
      int var9 = this.getPaddingRight();
      int var10 = this.getPaddingRight();
      var2 = this.getMeasuredHeight();
      int var11 = this.getChildCount();
      int var12 = this.getGravity();
      var4 = var12 & 112;
      if (var4 != 16) {
         if (var4 != 80) {
            var2 = this.getPaddingTop();
         } else {
            var2 = this.getPaddingTop() + var5 - var3 - var2;
         }
      } else {
         var2 = this.getPaddingTop() + (var5 - var3 - var2) / 2;
      }

      Drawable var15 = this.getDividerDrawable();
      if (var15 == null) {
         var4 = 0;
      } else {
         var4 = var15.getIntrinsicHeight();
      }

      for(var5 = 0; var5 < var11; ++var5) {
         View var16 = this.getChildAt(var5);
         if (var16 != null && var16.getVisibility() != 8) {
            int var13 = var16.getMeasuredWidth();
            int var14 = var16.getMeasuredHeight();
            LinearLayoutCompat.LayoutParams var17 = (LinearLayoutCompat.LayoutParams)var16.getLayoutParams();
            var3 = var17.gravity;
            if (var3 < 0) {
               var3 = var12 & 8388615;
            }

            var3 = GravityCompat.getAbsoluteGravity(var3, ViewCompat.getLayoutDirection(this)) & 7;
            if (var3 != 1) {
               if (var3 != 5) {
                  var3 = var17.leftMargin + var7;
               } else {
                  var3 = var8 - var9 - var13 - var17.rightMargin;
               }
            } else {
               var3 = (var8 - var7 - var10 - var13) / 2 + var7 + var17.leftMargin - var17.rightMargin;
            }

            int var6 = var2;
            if (this.hasDividerBeforeChildAt(var5)) {
               var6 = var2 + var4;
            }

            var2 = var6 + var17.topMargin;
            this.setChildFrame(var16, var3, var2, var13, var14);
            var2 += var14 + var17.bottomMargin;
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (!this.tryOnMeasure(var1, var2)) {
         super.onMeasure(var1, var2);
      }

   }
}
