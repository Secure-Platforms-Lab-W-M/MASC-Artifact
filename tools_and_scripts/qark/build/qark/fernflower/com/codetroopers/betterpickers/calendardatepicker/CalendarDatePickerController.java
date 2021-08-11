package com.codetroopers.betterpickers.calendardatepicker;

import android.util.SparseArray;

interface CalendarDatePickerController {
   SparseArray getDisabledDays();

   int getFirstDayOfWeek();

   MonthAdapter.CalendarDay getMaxDate();

   MonthAdapter.CalendarDay getMinDate();

   MonthAdapter.CalendarDay getSelectedDay();

   void onDayOfMonthSelected(int var1, int var2, int var3);

   void onYearSelected(int var1);

   void registerOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1);

   void tryVibrate();

   void unregisterOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1);
}
