// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.util.Locale;

public interface ReadableDateTime extends ReadableInstant
{
    int getCenturyOfEra();
    
    int getDayOfMonth();
    
    int getDayOfWeek();
    
    int getDayOfYear();
    
    int getEra();
    
    int getHourOfDay();
    
    int getMillisOfDay();
    
    int getMillisOfSecond();
    
    int getMinuteOfDay();
    
    int getMinuteOfHour();
    
    int getMonthOfYear();
    
    int getSecondOfDay();
    
    int getSecondOfMinute();
    
    int getWeekOfWeekyear();
    
    int getWeekyear();
    
    int getYear();
    
    int getYearOfCentury();
    
    int getYearOfEra();
    
    DateTime toDateTime();
    
    MutableDateTime toMutableDateTime();
    
    String toString(final String p0) throws IllegalArgumentException;
    
    String toString(final String p0, final Locale p1) throws IllegalArgumentException;
}
