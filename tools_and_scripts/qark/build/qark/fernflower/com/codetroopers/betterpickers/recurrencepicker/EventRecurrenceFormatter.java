package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.TimeFormatException;
import com.codetroopers.betterpickers.R.array;
import com.codetroopers.betterpickers.R.plurals;
import com.codetroopers.betterpickers.R.string;

public class EventRecurrenceFormatter {
   private static int[] mMonthRepeatByDayOfWeekIds;
   private static String[][] mMonthRepeatByDayOfWeekStrs;

   private static void cacheMonthRepeatStrings(Resources var0, int var1) {
      if (mMonthRepeatByDayOfWeekIds == null) {
         int[] var2 = new int[7];
         mMonthRepeatByDayOfWeekIds = var2;
         var2[0] = array.repeat_by_nth_sun;
         mMonthRepeatByDayOfWeekIds[1] = array.repeat_by_nth_mon;
         mMonthRepeatByDayOfWeekIds[2] = array.repeat_by_nth_tues;
         mMonthRepeatByDayOfWeekIds[3] = array.repeat_by_nth_wed;
         mMonthRepeatByDayOfWeekIds[4] = array.repeat_by_nth_thurs;
         mMonthRepeatByDayOfWeekIds[5] = array.repeat_by_nth_fri;
         mMonthRepeatByDayOfWeekIds[6] = array.repeat_by_nth_sat;
      }

      if (mMonthRepeatByDayOfWeekStrs == null) {
         mMonthRepeatByDayOfWeekStrs = new String[7][];
      }

      String[][] var3 = mMonthRepeatByDayOfWeekStrs;
      if (var3[var1] == null) {
         var3[var1] = var0.getStringArray(mMonthRepeatByDayOfWeekIds[var1]);
      }

   }

   private static String dayToString(int var0, int var1) {
      return DateUtils.getDayOfWeekString(dayToUtilDay(var0), var1);
   }

   private static int dayToUtilDay(int var0) {
      if (var0 != 65536) {
         if (var0 != 131072) {
            if (var0 != 262144) {
               if (var0 != 524288) {
                  if (var0 != 1048576) {
                     if (var0 != 2097152) {
                        if (var0 == 4194304) {
                           return 7;
                        } else {
                           StringBuilder var1 = new StringBuilder();
                           var1.append("bad day argument: ");
                           var1.append(var0);
                           throw new IllegalArgumentException(var1.toString());
                        }
                     } else {
                        return 6;
                     }
                  } else {
                     return 5;
                  }
               } else {
                  return 4;
               }
            } else {
               return 3;
            }
         } else {
            return 2;
         }
      } else {
         return 1;
      }
   }

   public static String getRepeatString(Context var0, Resources var1, EventRecurrence var2, boolean var3) {
      String var8 = "";
      String var11;
      if (var3) {
         StringBuilder var15 = new StringBuilder();
         if (var2.until != null) {
            try {
               Time var9 = new Time();
               var9.parse(var2.until);
               var11 = DateUtils.formatDateTime(var0, var9.toMillis(false), 131072);
               var15.append(var1.getString(string.endByDate, new Object[]{var11}));
            } catch (TimeFormatException var10) {
            }
         }

         if (var2.count > 0) {
            var15.append(var1.getQuantityString(plurals.endByCount, var2.count, new Object[]{var2.count}));
         }

         var8 = var15.toString();
      }

      int var4;
      if (var2.interval <= 1) {
         var4 = 1;
      } else {
         var4 = var2.interval;
      }

      int var5 = var2.freq;
      StringBuilder var12;
      if (var5 == 3) {
         var12 = new StringBuilder();
         var12.append(var1.getQuantityString(plurals.hourly, var4, new Object[]{var4}));
         var12.append(var8);
         return var12.toString();
      } else if (var5 != 4) {
         int var6;
         int var7;
         StringBuilder var13;
         if (var5 != 5) {
            if (var5 != 6) {
               if (var5 != 7) {
                  return null;
               } else {
                  var12 = new StringBuilder();
                  var12.append(var1.getQuantityString(plurals.yearly_plain, var4, new Object[]{var4, ""}));
                  var12.append(var8);
                  return var12.toString();
               }
            } else {
               var11 = "";
               if (var2.byday != null) {
                  var7 = EventRecurrence.day2CalendarDay(var2.byday[0]) - 1;
                  cacheMonthRepeatStrings(var1, var7);
                  var6 = var2.bydayNum[0];
                  var5 = var6;
                  if (var6 == -1) {
                     var5 = 5;
                  }

                  var11 = mMonthRepeatByDayOfWeekStrs[var7][var5 - 1];
               }

               var13 = new StringBuilder();
               var13.append(var1.getQuantityString(plurals.monthly, var4, new Object[]{var4, var11}));
               var13.append(var8);
               return var13.toString();
            }
         } else if (var2.repeatsOnEveryWeekDay()) {
            var12 = new StringBuilder();
            var12.append(var1.getString(string.every_weekday));
            var12.append(var8);
            return var12.toString();
         } else {
            byte var14 = 20;
            if (var2.bydayCount == 1) {
               var14 = 10;
            }

            var12 = new StringBuilder();
            if (var2.bydayCount > 0) {
               var7 = var2.bydayCount - 1;

               for(var6 = 0; var6 < var7; ++var6) {
                  var12.append(dayToString(var2.byday[var6], var14));
                  var12.append(", ");
               }

               var12.append(dayToString(var2.byday[var7], var14));
               var11 = var12.toString();
            } else {
               if (var2.startDate == null) {
                  return null;
               }

               var11 = dayToString(EventRecurrence.timeDay2Day(var2.startDate.weekDay), 10);
            }

            var13 = new StringBuilder();
            var13.append(var1.getQuantityString(plurals.weekly, var4, new Object[]{var4, var11}));
            var13.append(var8);
            return var13.toString();
         }
      } else {
         var12 = new StringBuilder();
         var12.append(var1.getQuantityString(plurals.daily, var4, new Object[]{var4}));
         var12.append(var8);
         return var12.toString();
      }
   }
}
