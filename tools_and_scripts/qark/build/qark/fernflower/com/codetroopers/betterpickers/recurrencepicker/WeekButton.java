package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ToggleButton;
import androidx.core.view.ViewCompat;

public class WeekButton extends ToggleButton {
   private static int mWidth;

   public WeekButton(Context var1) {
      super(var1);
   }

   public WeekButton(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public WeekButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public static void setSuggestedWidth(int var0) {
      mWidth = var0;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      var1 = this.getMeasuredHeight();
      var2 = this.getMeasuredWidth();
      int var3 = var1;
      int var4 = var2;
      if (var1 > 0) {
         var3 = var1;
         var4 = var2;
         if (var2 > 0) {
            if (var2 < var1) {
               var3 = var1;
               var4 = var2;
               if (MeasureSpec.getMode(ViewCompat.getMeasuredHeightAndState(this)) != 1073741824) {
                  var3 = var2;
                  var4 = var2;
               }
            } else {
               var3 = var1;
               var4 = var2;
               if (var1 < var2) {
                  var3 = var1;
                  var4 = var2;
                  if (MeasureSpec.getMode(ViewCompat.getMeasuredWidthAndState(this)) != 1073741824) {
                     var4 = var1;
                     var3 = var1;
                  }
               }
            }
         }
      }

      this.setMeasuredDimension(var4, var3);
   }
}
