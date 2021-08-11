package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.Time;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AbsListView.LayoutParams;
import com.codetroopers.betterpickers.Utils;
import java.util.Calendar;
import java.util.HashMap;

public abstract class MonthAdapter extends BaseAdapter implements MonthView.OnDayClickListener {
   protected static final int MONTHS_IN_YEAR = 12;
   private static final String TAG = "SimpleMonthAdapter";
   protected static int WEEK_7_OVERHANG_HEIGHT = 7;
   private final Context mContext;
   private final CalendarDatePickerController mController;
   private MonthAdapter.CalendarDay mSelectedDay;
   private TypedArray mThemeColors;

   public MonthAdapter(Context var1, CalendarDatePickerController var2) {
      this.mContext = var1;
      this.mController = var2;
      this.init();
      this.setSelectedDay(this.mController.getSelectedDay());
   }

   private boolean isDayDisabled(MonthAdapter.CalendarDay var1) {
      SparseArray var3 = this.mController.getDisabledDays();
      boolean var2 = false;
      if (var3 == null) {
         return false;
      } else {
         if (this.mController.getDisabledDays().indexOfKey(Utils.formatDisabledDayForKey(var1.year, var1.month, var1.day)) >= 0) {
            var2 = true;
         }

         return var2;
      }
   }

   private boolean isDayInRange(MonthAdapter.CalendarDay var1) {
      return var1.compareTo(this.mController.getMinDate()) >= 0 && var1.compareTo(this.mController.getMaxDate()) <= 0;
   }

   private boolean isRangeMaxInMonth(int var1, int var2) {
      return this.mController.getMaxDate().year == var1 && this.mController.getMaxDate().month == var2;
   }

   private boolean isRangeMinInMonth(int var1, int var2) {
      return this.mController.getMinDate().year == var1 && this.mController.getMinDate().month == var2;
   }

   private boolean isSelectedDayInMonth(int var1, int var2) {
      return this.mSelectedDay.year == var1 && this.mSelectedDay.month == var2;
   }

   public abstract MonthView createMonthView(Context var1);

   public int getCount() {
      return (this.mController.getMaxDate().year - this.mController.getMinDate().year + 1) * 12 - (11 - this.mController.getMaxDate().month) - this.mController.getMinDate().month;
   }

   public Object getItem(int var1) {
      return null;
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public MonthAdapter.CalendarDay getSelectedDay() {
      return this.mSelectedDay;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      HashMap var8 = null;
      HashMap var9;
      MonthView var10;
      if (var2 != null) {
         var10 = (MonthView)var2;
         var9 = (HashMap)var10.getTag();
      } else {
         var10 = this.createMonthView(this.mContext);
         var10.setTheme(this.mThemeColors);
         var10.setLayoutParams(new LayoutParams(-1, -1));
         var10.setClickable(true);
         var10.setOnDayClickListener(this);
         var9 = var8;
      }

      var8 = var9;
      if (var9 == null) {
         var8 = new HashMap();
      }

      var8.clear();
      int var6 = (this.mController.getMinDate().month + var1) % 12;
      int var7 = (this.mController.getMinDate().month + var1) / 12 + this.mController.getMinDate().year;
      var1 = -1;
      if (this.isSelectedDayInMonth(var7, var6)) {
         var1 = this.mSelectedDay.day;
      }

      int var4 = -1;
      if (this.isRangeMinInMonth(var7, var6)) {
         var4 = this.mController.getMinDate().day;
      }

      int var5 = -1;
      if (this.isRangeMaxInMonth(var7, var6)) {
         var5 = this.mController.getMaxDate().day;
      }

      var10.reuse();
      if (this.mController.getDisabledDays() != null) {
         var10.setDisabledDays(this.mController.getDisabledDays());
      }

      var8.put("selected_day", var1);
      var8.put("year", var7);
      var8.put("month", var6);
      var8.put("week_start", this.mController.getFirstDayOfWeek());
      var8.put("range_min", var4);
      var8.put("range_max", var5);
      var10.setMonthParams(var8);
      var10.invalidate();
      return var10;
   }

   public boolean hasStableIds() {
      return true;
   }

   protected void init() {
      MonthAdapter.CalendarDay var1 = new MonthAdapter.CalendarDay(System.currentTimeMillis());
      this.mSelectedDay = var1;
      if (var1.compareTo(this.mController.getMaxDate()) > 0) {
         this.mSelectedDay = this.mController.getMaxDate();
      }

      if (this.mSelectedDay.compareTo(this.mController.getMinDate()) < 0) {
         this.mSelectedDay = this.mController.getMinDate();
      }

   }

   public void onDayClick(MonthView var1, MonthAdapter.CalendarDay var2) {
      if (var2 != null && this.isDayInRange(var2) && !this.isDayDisabled(var2)) {
         this.onDayTapped(var2);
      }

   }

   protected void onDayTapped(MonthAdapter.CalendarDay var1) {
      this.mController.tryVibrate();
      this.mController.onDayOfMonthSelected(var1.year, var1.month, var1.day);
      this.setSelectedDay(var1);
   }

   public void setSelectedDay(MonthAdapter.CalendarDay var1) {
      this.mSelectedDay = var1;
      this.notifyDataSetChanged();
   }

   public void setTheme(TypedArray var1) {
      this.mThemeColors = var1;
   }

   public static class CalendarDay implements Comparable, Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MonthAdapter.CalendarDay createFromParcel(Parcel var1) {
            return new MonthAdapter.CalendarDay(var1);
         }

         public MonthAdapter.CalendarDay[] newArray(int var1) {
            return new MonthAdapter.CalendarDay[var1];
         }
      };
      private Calendar calendar;
      private long calendarTimeInMillis;
      int day;
      int month;
      private Time time;
      private long timeInMillis;
      int year;

      public CalendarDay() {
         this.setTime(System.currentTimeMillis());
      }

      public CalendarDay(int var1, int var2, int var3) {
         this.setDay(var1, var2, var3);
      }

      public CalendarDay(long var1) {
         this.setTime(var1);
      }

      public CalendarDay(Parcel var1) {
         this.calendarTimeInMillis = var1.readLong();
         Calendar var2 = Calendar.getInstance();
         this.calendar = var2;
         var2.setTimeInMillis(this.calendarTimeInMillis);
         this.timeInMillis = var1.readLong();
         Time var3 = new Time();
         this.time = var3;
         var3.set(this.timeInMillis);
         this.year = var1.readInt();
         this.month = var1.readInt();
         this.day = var1.readInt();
      }

      public CalendarDay(Calendar var1) {
         this.year = var1.get(1);
         this.month = var1.get(2);
         this.day = var1.get(5);
      }

      private void setTime(long var1) {
         if (this.calendar == null) {
            this.calendar = Calendar.getInstance();
         }

         this.calendar.setTimeInMillis(var1);
         this.month = this.calendar.get(2);
         this.year = this.calendar.get(1);
         this.day = this.calendar.get(5);
      }

      public int compareTo(MonthAdapter.CalendarDay var1) {
         int var2 = this.year;
         int var3 = var1.year;
         if (var2 < var3 || var2 == var3 && this.month < var1.month || this.year == var1.year && this.month == var1.month && this.day < var1.day) {
            return -1;
         } else {
            return this.year == var1.year && this.month == var1.month && this.day == var1.day ? 0 : 1;
         }
      }

      public int describeContents() {
         return 0;
      }

      public long getDateInMillis() {
         if (this.calendar == null) {
            Calendar var1 = Calendar.getInstance();
            this.calendar = var1;
            var1.set(this.year, this.month, this.day, 0, 0, 0);
            this.calendar.set(14, 0);
         }

         return this.calendar.getTimeInMillis();
      }

      public void set(MonthAdapter.CalendarDay var1) {
         this.year = var1.year;
         this.month = var1.month;
         this.day = var1.day;
      }

      public void setDay(int var1, int var2, int var3) {
         Calendar var4 = Calendar.getInstance();
         this.calendar = var4;
         var4.set(var1, var2, var3, 0, 0, 0);
         this.calendar.set(14, 0);
         this.year = this.calendar.get(1);
         this.month = this.calendar.get(2);
         this.day = this.calendar.get(5);
      }

      public void setJulianDay(int var1) {
         synchronized(this){}

         try {
            if (this.time == null) {
               this.time = new Time();
            }

            this.time.setJulianDay(var1);
            this.setTime(this.time.toMillis(false));
         } finally {
            ;
         }

      }

      public void writeToParcel(Parcel var1, int var2) {
         Calendar var3 = this.calendar;
         if (var3 != null) {
            this.calendarTimeInMillis = var3.getTimeInMillis();
         }

         var1.writeLong(this.calendarTimeInMillis);
         Time var4 = this.time;
         if (var4 != null) {
            this.timeInMillis = var4.toMillis(false);
         }

         var1.writeInt(this.year);
         var1.writeInt(this.month);
         var1.writeInt(this.day);
      }
   }
}
