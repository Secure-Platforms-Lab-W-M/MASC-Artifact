package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class LinearLayoutWithMaxWidth extends LinearLayout {
   public LinearLayoutWithMaxWidth(Context var1) {
      super(var1);
   }

   public LinearLayoutWithMaxWidth(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public LinearLayoutWithMaxWidth(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      WeekButton.setSuggestedWidth(MeasureSpec.getSize(var1) / 7);
      super.onMeasure(var1, var2);
   }
}
