package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import java.util.Calendar;

public class Utils {
   private static final String TAG = "CalUtils";
   public static final int YEAR_MAX = 2036;
   public static final int YEAR_MIN = 1970;

   public static int convertDayOfWeekFromTimeToCalendar(int var0) {
      switch(var0) {
      case 0:
         return 1;
      case 1:
         return 2;
      case 2:
         return 3;
      case 3:
         return 4;
      case 4:
         return 5;
      case 5:
         return 6;
      case 6:
         return 7;
      default:
         throw new IllegalArgumentException("Argument must be between Time.SUNDAY and Time.SATURDAY");
      }
   }

   public static int getFirstDayOfWeek(Context var0) {
      int var1 = Calendar.getInstance().getFirstDayOfWeek();
      if (var1 == 7) {
         return 6;
      } else {
         return var1 == 2 ? 1 : 0;
      }
   }

   public static int getFirstDayOfWeekAsCalendar(Context var0) {
      return convertDayOfWeekFromTimeToCalendar(getFirstDayOfWeek(var0));
   }
}
