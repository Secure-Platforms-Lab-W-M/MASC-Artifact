/*
 * Decompiled with CFR 0_124.
 */
package org.joda.time;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;

public interface ReadableDateTime
extends ReadableInstant {
    public int getCenturyOfEra();

    public int getDayOfMonth();

    public int getDayOfWeek();

    public int getDayOfYear();

    public int getEra();

    public int getHourOfDay();

    public int getMillisOfDay();

    public int getMillisOfSecond();

    public int getMinuteOfDay();

    public int getMinuteOfHour();

    public int getMonthOfYear();

    public int getSecondOfDay();

    public int getSecondOfMinute();

    public int getWeekOfWeekyear();

    public int getWeekyear();

    public int getYear();

    public int getYearOfCentury();

    public int getYearOfEra();

    public DateTime toDateTime();

    public MutableDateTime toMutableDateTime();

    public String toString(String var1) throws IllegalArgumentException;

    public String toString(String var1, Locale var2) throws IllegalArgumentException;
}

