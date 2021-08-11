package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R.layout;
import com.google.android.material.R.string;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

class YearGridAdapter extends RecyclerView.Adapter {
   private final MaterialCalendar materialCalendar;

   YearGridAdapter(MaterialCalendar var1) {
      this.materialCalendar = var1;
   }

   private OnClickListener createYearClickListener(final int var1) {
      return new OnClickListener() {
         public void onClick(View var1x) {
            Month var2 = Month.create(var1, YearGridAdapter.this.materialCalendar.getCurrentMonth().month);
            YearGridAdapter.this.materialCalendar.setCurrentMonth(var2);
            YearGridAdapter.this.materialCalendar.setSelector(MaterialCalendar.CalendarSelector.DAY);
         }
      };
   }

   public int getItemCount() {
      return this.materialCalendar.getCalendarConstraints().getYearSpan();
   }

   int getPositionForYear(int var1) {
      return var1 - this.materialCalendar.getCalendarConstraints().getStart().year;
   }

   int getYearForPosition(int var1) {
      return this.materialCalendar.getCalendarConstraints().getStart().year + var1;
   }

   public void onBindViewHolder(YearGridAdapter.ViewHolder var1, int var2) {
      var2 = this.getYearForPosition(var2);
      String var3 = var1.textView.getContext().getString(string.mtrl_picker_navigate_to_year_description);
      var1.textView.setText(String.format(Locale.getDefault(), "%d", var2));
      var1.textView.setContentDescription(String.format(var3, var2));
      CalendarStyle var4 = this.materialCalendar.getCalendarStyle();
      Calendar var5 = UtcDates.getTodayCalendar();
      CalendarItemStyle var7;
      if (var5.get(1) == var2) {
         var7 = var4.todayYear;
      } else {
         var7 = var4.year;
      }

      Iterator var6 = this.materialCalendar.getDateSelector().getSelectedDays().iterator();

      while(var6.hasNext()) {
         var5.setTimeInMillis((Long)var6.next());
         if (var5.get(1) == var2) {
            var7 = var4.selectedYear;
         }
      }

      var7.styleItem(var1.textView);
      var1.textView.setOnClickListener(this.createYearClickListener(var2));
   }

   public YearGridAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new YearGridAdapter.ViewHolder((TextView)LayoutInflater.from(var1.getContext()).inflate(layout.mtrl_calendar_year, var1, false));
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      final TextView textView;

      ViewHolder(TextView var1) {
         super(var1);
         this.textView = var1;
      }
   }
}
