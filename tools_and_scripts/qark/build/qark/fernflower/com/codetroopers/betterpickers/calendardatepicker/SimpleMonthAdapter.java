package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;

public class SimpleMonthAdapter extends MonthAdapter {
   public SimpleMonthAdapter(Context var1, CalendarDatePickerController var2) {
      super(var1, var2);
   }

   public MonthView createMonthView(Context var1) {
      return new SimpleMonthView(var1);
   }
}
