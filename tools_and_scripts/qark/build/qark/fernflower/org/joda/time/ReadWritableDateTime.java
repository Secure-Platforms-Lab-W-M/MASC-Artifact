package org.joda.time;

public interface ReadWritableDateTime extends ReadableDateTime, ReadWritableInstant {
   void addDays(int var1);

   void addHours(int var1);

   void addMillis(int var1);

   void addMinutes(int var1);

   void addMonths(int var1);

   void addSeconds(int var1);

   void addWeeks(int var1);

   void addWeekyears(int var1);

   void addYears(int var1);

   void setDate(int var1, int var2, int var3);

   void setDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7);

   void setDayOfMonth(int var1);

   void setDayOfWeek(int var1);

   void setDayOfYear(int var1);

   void setHourOfDay(int var1);

   void setMillisOfDay(int var1);

   void setMillisOfSecond(int var1);

   void setMinuteOfDay(int var1);

   void setMinuteOfHour(int var1);

   void setMonthOfYear(int var1);

   void setSecondOfDay(int var1);

   void setSecondOfMinute(int var1);

   void setTime(int var1, int var2, int var3, int var4);

   void setWeekOfWeekyear(int var1);

   void setWeekyear(int var1);

   void setYear(int var1);
}
