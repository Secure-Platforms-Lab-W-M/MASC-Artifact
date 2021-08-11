package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R.layout;
import java.util.Iterator;

class MonthAdapter extends BaseAdapter {
   static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendar().getMaximum(4);
   final CalendarConstraints calendarConstraints;
   CalendarStyle calendarStyle;
   final DateSelector dateSelector;
   final Month month;

   MonthAdapter(Month var1, DateSelector var2, CalendarConstraints var3) {
      this.month = var1;
      this.dateSelector = var2;
      this.calendarConstraints = var3;
   }

   private void initializeStyles(Context var1) {
      if (this.calendarStyle == null) {
         this.calendarStyle = new CalendarStyle(var1);
      }

   }

   int dayToPosition(int var1) {
      return this.firstPositionInMonth() + (var1 - 1);
   }

   int firstPositionInMonth() {
      return this.month.daysFromStartOfWeekToFirstOfMonth();
   }

   public int getCount() {
      return this.month.daysInMonth + this.firstPositionInMonth();
   }

   public Long getItem(int var1) {
      return var1 >= this.month.daysFromStartOfWeekToFirstOfMonth() && var1 <= this.lastPositionInMonth() ? this.month.getDay(this.positionToDay(var1)) : null;
   }

   public long getItemId(int var1) {
      return (long)(var1 / this.month.daysInWeek);
   }

   public TextView getView(int var1, View var2, ViewGroup var3) {
      this.initializeStyles(var3.getContext());
      TextView var7 = (TextView)var2;
      if (var2 == null) {
         var7 = (TextView)LayoutInflater.from(var3.getContext()).inflate(layout.mtrl_calendar_day, var3, false);
      }

      int var4 = var1 - this.firstPositionInMonth();
      long var5;
      if (var4 >= 0 && var4 < this.month.daysInMonth) {
         ++var4;
         var7.setTag(this.month);
         var7.setText(String.valueOf(var4));
         var5 = this.month.getDay(var4);
         if (this.month.year == Month.today().year) {
            var7.setContentDescription(DateStrings.getMonthDayOfWeekDay(var5));
         } else {
            var7.setContentDescription(DateStrings.getYearMonthDayOfWeekDay(var5));
         }

         var7.setVisibility(0);
         var7.setEnabled(true);
      } else {
         var7.setVisibility(8);
         var7.setEnabled(false);
      }

      Long var8 = this.getItem(var1);
      if (var8 == null) {
         return var7;
      } else if (this.calendarConstraints.getDateValidator().isValid(var8)) {
         var7.setEnabled(true);
         Iterator var9 = this.dateSelector.getSelectedDays().iterator();

         do {
            if (!var9.hasNext()) {
               if (UtcDates.getTodayCalendar().getTimeInMillis() == var8) {
                  this.calendarStyle.todayDay.styleItem(var7);
                  return var7;
               }

               this.calendarStyle.day.styleItem(var7);
               return var7;
            }

            var5 = (Long)var9.next();
         } while(UtcDates.canonicalYearMonthDay(var8) != UtcDates.canonicalYearMonthDay(var5));

         this.calendarStyle.selectedDay.styleItem(var7);
         return var7;
      } else {
         var7.setEnabled(false);
         this.calendarStyle.invalidDay.styleItem(var7);
         return var7;
      }
   }

   public boolean hasStableIds() {
      return true;
   }

   boolean isFirstInRow(int var1) {
      return var1 % this.month.daysInWeek == 0;
   }

   boolean isLastInRow(int var1) {
      return (var1 + 1) % this.month.daysInWeek == 0;
   }

   int lastPositionInMonth() {
      return this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth - 1;
   }

   int positionToDay(int var1) {
      return var1 - this.month.daysFromStartOfWeekToFirstOfMonth() + 1;
   }

   boolean withinMonth(int var1) {
      return var1 >= this.firstPositionInMonth() && var1 <= this.lastPositionInMonth();
   }
}
