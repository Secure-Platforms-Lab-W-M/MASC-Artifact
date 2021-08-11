package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.util.AttributeSet;

public class SimpleDayPickerView extends DayPickerView {
   public SimpleDayPickerView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public SimpleDayPickerView(Context var1, CalendarDatePickerController var2) {
      super(var1, var2);
   }

   public MonthAdapter createMonthAdapter(Context var1, CalendarDatePickerController var2) {
      return new SimpleMonthAdapter(var1, var2);
   }
}
