package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.graphics.Canvas;

public class SimpleMonthView extends MonthView {
   public SimpleMonthView(Context var1) {
      super(var1);
   }

   public void drawMonthDay(Canvas var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
      if (this.mSelectedDay == var4) {
         var1.drawCircle((float)var5, (float)(var6 - MINI_DAY_NUMBER_TEXT_SIZE / 3), (float)DAY_SELECTED_CIRCLE_SIZE, this.mSelectedCirclePaint);
      }

      if (this.mHasToday && this.mToday == var4) {
         this.mMonthNumPaint.setColor(this.mTodayNumberColor);
      } else if (var11) {
         this.mMonthNumPaint.setColor(this.mDayTextColorEnabled);
      } else {
         this.mMonthNumPaint.setColor(this.mDayTextColorDisabled);
      }

      var1.drawText(String.format("%d", var4), (float)var5, (float)var6, this.mMonthNumPaint);
   }
}
