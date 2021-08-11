/*
 * Decompiled with CFR 0_124.
 */
package org.joda.time;

import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;

public interface ReadWritableDateTime
extends ReadableDateTime,
ReadWritableInstant {
    public void addDays(int var1);

    public void addHours(int var1);

    public void addMillis(int var1);

    public void addMinutes(int var1);

    public void addMonths(int var1);

    public void addSeconds(int var1);

    public void addWeeks(int var1);

    public void addWeekyears(int var1);

    public void addYears(int var1);

    public void setDate(int var1, int var2, int var3);

    public void setDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7);

    public void setDayOfMonth(int var1);

    public void setDayOfWeek(int var1);

    public void setDayOfYear(int var1);

    public void setHourOfDay(int var1);

    public void setMillisOfDay(int var1);

    public void setMillisOfSecond(int var1);

    public void setMinuteOfDay(int var1);

    public void setMinuteOfHour(int var1);

    public void setMonthOfYear(int var1);

    public void setSecondOfDay(int var1);

    public void setSecondOfMinute(int var1);

    public void setTime(int var1, int var2, int var3, int var4);

    public void setWeekOfWeekyear(int var1);

    public void setWeekyear(int var1);

    public void setYear(int var1);
}

