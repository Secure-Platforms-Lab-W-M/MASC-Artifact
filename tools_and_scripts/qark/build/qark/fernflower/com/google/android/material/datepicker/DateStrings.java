package com.google.android.material.datepicker;

import android.os.Build.VERSION;
import androidx.core.util.Pair;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DateStrings {
   private DateStrings() {
   }

   static Pair getDateRangeString(Long var0, Long var1) {
      return getDateRangeString(var0, var1, (SimpleDateFormat)null);
   }

   static Pair getDateRangeString(Long var0, Long var1, SimpleDateFormat var2) {
      if (var0 == null && var1 == null) {
         return Pair.create((Object)null, (Object)null);
      } else if (var0 == null) {
         return Pair.create((Object)null, getDateString(var1, var2));
      } else if (var1 == null) {
         return Pair.create(getDateString(var0, var2), (Object)null);
      } else {
         Calendar var3 = UtcDates.getTodayCalendar();
         Calendar var4 = UtcDates.getUtcCalendar();
         var4.setTimeInMillis(var0);
         Calendar var5 = UtcDates.getUtcCalendar();
         var5.setTimeInMillis(var1);
         if (var2 != null) {
            Date var6 = new Date(var0);
            Date var7 = new Date(var1);
            return Pair.create(var2.format(var6), var2.format(var7));
         } else if (var4.get(1) == var5.get(1)) {
            return var4.get(1) == var3.get(1) ? Pair.create(getMonthDay(var0, Locale.getDefault()), getMonthDay(var1, Locale.getDefault())) : Pair.create(getMonthDay(var0, Locale.getDefault()), getYearMonthDay(var1, Locale.getDefault()));
         } else {
            return Pair.create(getYearMonthDay(var0, Locale.getDefault()), getYearMonthDay(var1, Locale.getDefault()));
         }
      }
   }

   static String getDateString(long var0) {
      return getDateString(var0, (SimpleDateFormat)null);
   }

   static String getDateString(long var0, SimpleDateFormat var2) {
      Calendar var3 = UtcDates.getTodayCalendar();
      Calendar var4 = UtcDates.getUtcCalendar();
      var4.setTimeInMillis(var0);
      if (var2 != null) {
         return var2.format(new Date(var0));
      } else {
         return var3.get(1) == var4.get(1) ? getMonthDay(var0) : getYearMonthDay(var0);
      }
   }

   static String getMonthDay(long var0) {
      return getMonthDay(var0, Locale.getDefault());
   }

   static String getMonthDay(long var0, Locale var2) {
      return VERSION.SDK_INT >= 24 ? UtcDates.getAbbrMonthDayFormat(var2).format(new Date(var0)) : UtcDates.getMediumNoYear(var2).format(new Date(var0));
   }

   static String getMonthDayOfWeekDay(long var0) {
      return getMonthDayOfWeekDay(var0, Locale.getDefault());
   }

   static String getMonthDayOfWeekDay(long var0, Locale var2) {
      return VERSION.SDK_INT >= 24 ? UtcDates.getAbbrMonthWeekdayDayFormat(var2).format(new Date(var0)) : UtcDates.getFullFormat(var2).format(new Date(var0));
   }

   static String getYearMonthDay(long var0) {
      return getYearMonthDay(var0, Locale.getDefault());
   }

   static String getYearMonthDay(long var0, Locale var2) {
      return VERSION.SDK_INT >= 24 ? UtcDates.getYearAbbrMonthDayFormat(var2).format(new Date(var0)) : UtcDates.getMediumFormat(var2).format(new Date(var0));
   }

   static String getYearMonthDayOfWeekDay(long var0) {
      return getYearMonthDayOfWeekDay(var0, Locale.getDefault());
   }

   static String getYearMonthDayOfWeekDay(long var0, Locale var2) {
      return VERSION.SDK_INT >= 24 ? UtcDates.getYearAbbrMonthWeekdayDayFormat(var2).format(new Date(var0)) : UtcDates.getFullFormat(var2).format(new Date(var0));
   }
}
