// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadWritableDateTime extends ReadableDateTime, ReadWritableInstant
{
    void addDays(final int p0);
    
    void addHours(final int p0);
    
    void addMillis(final int p0);
    
    void addMinutes(final int p0);
    
    void addMonths(final int p0);
    
    void addSeconds(final int p0);
    
    void addWeeks(final int p0);
    
    void addWeekyears(final int p0);
    
    void addYears(final int p0);
    
    void setDate(final int p0, final int p1, final int p2);
    
    void setDateTime(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    void setDayOfMonth(final int p0);
    
    void setDayOfWeek(final int p0);
    
    void setDayOfYear(final int p0);
    
    void setHourOfDay(final int p0);
    
    void setMillisOfDay(final int p0);
    
    void setMillisOfSecond(final int p0);
    
    void setMinuteOfDay(final int p0);
    
    void setMinuteOfHour(final int p0);
    
    void setMonthOfYear(final int p0);
    
    void setSecondOfDay(final int p0);
    
    void setSecondOfMinute(final int p0);
    
    void setTime(final int p0, final int p1, final int p2, final int p3);
    
    void setWeekOfWeekyear(final int p0);
    
    void setWeekyear(final int p0);
    
    void setYear(final int p0);
}
