package org.joda.time;

public interface ReadWritablePeriod extends ReadablePeriod {
   void add(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

   void add(DurationFieldType var1, int var2);

   void add(ReadableInterval var1);

   void add(ReadablePeriod var1);

   void addDays(int var1);

   void addHours(int var1);

   void addMillis(int var1);

   void addMinutes(int var1);

   void addMonths(int var1);

   void addSeconds(int var1);

   void addWeeks(int var1);

   void addYears(int var1);

   void clear();

   void set(DurationFieldType var1, int var2);

   void setDays(int var1);

   void setHours(int var1);

   void setMillis(int var1);

   void setMinutes(int var1);

   void setMonths(int var1);

   void setPeriod(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

   void setPeriod(ReadableInterval var1);

   void setPeriod(ReadablePeriod var1);

   void setSeconds(int var1);

   void setValue(int var1, int var2);

   void setWeeks(int var1);

   void setYears(int var1);
}
