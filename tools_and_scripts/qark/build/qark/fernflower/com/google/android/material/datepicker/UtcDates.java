package com.google.android.material.datepicker;

import android.content.res.Resources;
import android.icu.text.DateFormat;
import com.google.android.material.R.string;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

class UtcDates {
   static final String UTC = "UTC";

   private UtcDates() {
   }

   static long canonicalYearMonthDay(long var0) {
      Calendar var2 = getUtcCalendar();
      var2.setTimeInMillis(var0);
      return getDayCopy(var2).getTimeInMillis();
   }

   private static int findCharactersInDateFormatPattern(String var0, String var1, int var2, int var3) {
      int var4;
      for(; var3 >= 0 && var3 < var0.length() && var1.indexOf(var0.charAt(var3)) == -1; var3 = var4 + var2) {
         var4 = var3;
         if (var0.charAt(var3) == '\'') {
            var3 += var2;

            while(true) {
               var4 = var3;
               if (var3 < 0) {
                  break;
               }

               var4 = var3;
               if (var3 >= var0.length()) {
                  break;
               }

               var4 = var3;
               if (var0.charAt(var3) == '\'') {
                  break;
               }

               var3 += var2;
            }
         }
      }

      return var3;
   }

   static DateFormat getAbbrMonthDayFormat(Locale var0) {
      return getAndroidFormat("MMMd", var0);
   }

   static DateFormat getAbbrMonthWeekdayDayFormat(Locale var0) {
      return getAndroidFormat("MMMEd", var0);
   }

   private static DateFormat getAndroidFormat(String var0, Locale var1) {
      DateFormat var2 = DateFormat.getInstanceForSkeleton(var0, var1);
      var2.setTimeZone(getUtcAndroidTimeZone());
      return var2;
   }

   static Calendar getDayCopy(Calendar var0) {
      var0 = getUtcCalendarOf(var0);
      Calendar var1 = getUtcCalendar();
      var1.set(var0.get(1), var0.get(2), var0.get(5));
      return var1;
   }

   private static java.text.DateFormat getFormat(int var0, Locale var1) {
      java.text.DateFormat var2 = java.text.DateFormat.getDateInstance(var0, var1);
      var2.setTimeZone(getTimeZone());
      return var2;
   }

   static java.text.DateFormat getFullFormat() {
      return getFullFormat(Locale.getDefault());
   }

   static java.text.DateFormat getFullFormat(Locale var0) {
      return getFormat(0, var0);
   }

   static java.text.DateFormat getMediumFormat() {
      return getMediumFormat(Locale.getDefault());
   }

   static java.text.DateFormat getMediumFormat(Locale var0) {
      return getFormat(2, var0);
   }

   static java.text.DateFormat getMediumNoYear() {
      return getMediumNoYear(Locale.getDefault());
   }

   static java.text.DateFormat getMediumNoYear(Locale var0) {
      SimpleDateFormat var1 = (SimpleDateFormat)getMediumFormat(var0);
      var1.applyPattern(removeYearFromDateFormatPattern(var1.toPattern()));
      return var1;
   }

   static SimpleDateFormat getSimpleFormat(String var0) {
      return getSimpleFormat(var0, Locale.getDefault());
   }

   private static SimpleDateFormat getSimpleFormat(String var0, Locale var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(var0, var1);
      var2.setTimeZone(getTimeZone());
      return var2;
   }

   static SimpleDateFormat getTextInputFormat() {
      SimpleDateFormat var0 = new SimpleDateFormat(((SimpleDateFormat)java.text.DateFormat.getDateInstance(3, Locale.getDefault())).toLocalizedPattern().replaceAll("\\s+", ""), Locale.getDefault());
      var0.setTimeZone(getTimeZone());
      var0.setLenient(false);
      return var0;
   }

   static String getTextInputHint(Resources var0, SimpleDateFormat var1) {
      String var4 = var1.toLocalizedPattern();
      String var2 = var0.getString(string.mtrl_picker_text_input_year_abbr);
      String var3 = var0.getString(string.mtrl_picker_text_input_month_abbr);
      return var4.replaceAll("d", var0.getString(string.mtrl_picker_text_input_day_abbr)).replaceAll("M", var3).replaceAll("y", var2);
   }

   private static TimeZone getTimeZone() {
      return TimeZone.getTimeZone("UTC");
   }

   static Calendar getTodayCalendar() {
      return getDayCopy(Calendar.getInstance());
   }

   private static android.icu.util.TimeZone getUtcAndroidTimeZone() {
      return android.icu.util.TimeZone.getTimeZone("UTC");
   }

   static Calendar getUtcCalendar() {
      return getUtcCalendarOf((Calendar)null);
   }

   static Calendar getUtcCalendarOf(Calendar var0) {
      Calendar var1 = Calendar.getInstance(getTimeZone());
      if (var0 == null) {
         var1.clear();
         return var1;
      } else {
         var1.setTimeInMillis(var0.getTimeInMillis());
         return var1;
      }
   }

   static DateFormat getYearAbbrMonthDayFormat(Locale var0) {
      return getAndroidFormat("yMMMd", var0);
   }

   static DateFormat getYearAbbrMonthWeekdayDayFormat(Locale var0) {
      return getAndroidFormat("yMMMEd", var0);
   }

   static SimpleDateFormat getYearMonthFormat() {
      return getYearMonthFormat(Locale.getDefault());
   }

   private static SimpleDateFormat getYearMonthFormat(Locale var0) {
      return getSimpleFormat("MMMM, yyyy", var0);
   }

   private static String removeYearFromDateFormatPattern(String var0) {
      int var1 = findCharactersInDateFormatPattern(var0, "yY", 1, 0);
      if (var1 >= var0.length()) {
         return var0;
      } else {
         String var3 = "EMd";
         int var2 = findCharactersInDateFormatPattern(var0, "EMd", 1, var1);
         if (var2 < var0.length()) {
            StringBuilder var4 = new StringBuilder();
            var4.append("EMd");
            var4.append(",");
            var3 = var4.toString();
         }

         return var0.replace(var0.substring(findCharactersInDateFormatPattern(var0, var3, -1, var1) + 1, var2), " ").trim();
      }
   }
}
