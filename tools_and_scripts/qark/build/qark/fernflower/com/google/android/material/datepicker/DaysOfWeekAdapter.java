package com.google.android.material.datepicker;

import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R.layout;
import com.google.android.material.R.string;
import java.util.Calendar;
import java.util.Locale;

class DaysOfWeekAdapter extends BaseAdapter {
   private static final int CALENDAR_DAY_STYLE;
   private static final int NARROW_FORMAT = 4;
   private final Calendar calendar;
   private final int daysInWeek;
   private final int firstDayOfWeek;

   static {
      byte var0;
      if (VERSION.SDK_INT >= 26) {
         var0 = 4;
      } else {
         var0 = 1;
      }

      CALENDAR_DAY_STYLE = var0;
   }

   public DaysOfWeekAdapter() {
      Calendar var1 = UtcDates.getUtcCalendar();
      this.calendar = var1;
      this.daysInWeek = var1.getMaximum(7);
      this.firstDayOfWeek = this.calendar.getFirstDayOfWeek();
   }

   private int positionToDayOfWeek(int var1) {
      int var2 = this.firstDayOfWeek + var1;
      int var3 = this.daysInWeek;
      var1 = var2;
      if (var2 > var3) {
         var1 = var2 - var3;
      }

      return var1;
   }

   public int getCount() {
      return this.daysInWeek;
   }

   public Integer getItem(int var1) {
      return var1 >= this.daysInWeek ? null : this.positionToDayOfWeek(var1);
   }

   public long getItemId(int var1) {
      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      TextView var4 = (TextView)var2;
      if (var2 == null) {
         var4 = (TextView)LayoutInflater.from(var3.getContext()).inflate(layout.mtrl_calendar_day_of_week, var3, false);
      }

      this.calendar.set(7, this.positionToDayOfWeek(var1));
      var4.setText(this.calendar.getDisplayName(7, CALENDAR_DAY_STYLE, Locale.getDefault()));
      var4.setContentDescription(String.format(var3.getContext().getString(string.mtrl_picker_day_of_week_column_header), this.calendar.getDisplayName(7, 2, Locale.getDefault())));
      return var4;
   }
}
