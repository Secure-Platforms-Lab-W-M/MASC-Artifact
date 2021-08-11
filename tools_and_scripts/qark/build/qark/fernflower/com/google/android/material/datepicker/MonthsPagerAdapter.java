package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;

class MonthsPagerAdapter extends RecyclerView.Adapter {
   private final CalendarConstraints calendarConstraints;
   private final DateSelector dateSelector;
   private final int itemHeight;
   private final MaterialCalendar.OnDayClickListener onDayClickListener;

   MonthsPagerAdapter(Context var1, DateSelector var2, CalendarConstraints var3, MaterialCalendar.OnDayClickListener var4) {
      Month var8 = var3.getStart();
      Month var9 = var3.getEnd();
      Month var10 = var3.getOpenAt();
      if (var8.compareTo(var10) <= 0) {
         if (var10.compareTo(var9) <= 0) {
            int var6 = MonthAdapter.MAXIMUM_WEEKS;
            int var7 = MaterialCalendar.getDayHeight(var1);
            int var5;
            if (MaterialDatePicker.isFullscreen(var1)) {
               var5 = MaterialCalendar.getDayHeight(var1);
            } else {
               var5 = 0;
            }

            this.itemHeight = var6 * var7 + var5;
            this.calendarConstraints = var3;
            this.dateSelector = var2;
            this.onDayClickListener = var4;
            this.setHasStableIds(true);
         } else {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
         }
      } else {
         throw new IllegalArgumentException("firstPage cannot be after currentPage");
      }
   }

   public int getItemCount() {
      return this.calendarConstraints.getMonthSpan();
   }

   public long getItemId(int var1) {
      return this.calendarConstraints.getStart().monthsLater(var1).getStableId();
   }

   Month getPageMonth(int var1) {
      return this.calendarConstraints.getStart().monthsLater(var1);
   }

   CharSequence getPageTitle(int var1) {
      return this.getPageMonth(var1).getLongName();
   }

   int getPosition(Month var1) {
      return this.calendarConstraints.getStart().monthsUntil(var1);
   }

   public void onBindViewHolder(MonthsPagerAdapter.ViewHolder var1, int var2) {
      Month var3 = this.calendarConstraints.getStart().monthsLater(var2);
      var1.monthTitle.setText(var3.getLongName());
      final MaterialCalendarGridView var5 = (MaterialCalendarGridView)var1.monthGrid.findViewById(id.month_grid);
      if (var5.getAdapter() != null && var3.equals(var5.getAdapter().month)) {
         var5.getAdapter().notifyDataSetChanged();
      } else {
         MonthAdapter var4 = new MonthAdapter(var3, this.dateSelector, this.calendarConstraints);
         var5.setNumColumns(var3.daysInWeek);
         var5.setAdapter((ListAdapter)var4);
      }

      var5.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            if (var5.getAdapter().withinMonth(var3)) {
               MonthsPagerAdapter.this.onDayClickListener.onDayClick(var5.getAdapter().getItem(var3));
            }

         }
      });
   }

   public MonthsPagerAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      LinearLayout var3 = (LinearLayout)LayoutInflater.from(var1.getContext()).inflate(layout.mtrl_calendar_month_labeled, var1, false);
      if (MaterialDatePicker.isFullscreen(var1.getContext())) {
         var3.setLayoutParams(new RecyclerView.LayoutParams(-1, this.itemHeight));
         return new MonthsPagerAdapter.ViewHolder(var3, true);
      } else {
         return new MonthsPagerAdapter.ViewHolder(var3, false);
      }
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      final MaterialCalendarGridView monthGrid;
      final TextView monthTitle;

      ViewHolder(LinearLayout var1, boolean var2) {
         super(var1);
         TextView var3 = (TextView)var1.findViewById(id.month_title);
         this.monthTitle = var3;
         ViewCompat.setAccessibilityHeading(var3, true);
         this.monthGrid = (MaterialCalendarGridView)var1.findViewById(id.month_grid);
         if (!var2) {
            this.monthTitle.setVisibility(8);
         }

      }
   }
}
