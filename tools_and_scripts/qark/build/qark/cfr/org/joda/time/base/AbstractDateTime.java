/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.ToString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 */
package org.joda.time.base;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.base.AbstractInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractDateTime
extends AbstractInstant
implements ReadableDateTime {
    protected AbstractDateTime() {
    }

    @Override
    public int get(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            return dateTimeFieldType.getField(this.getChronology()).get(this.getMillis());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    @Override
    public int getCenturyOfEra() {
        return this.getChronology().centuryOfEra().get(this.getMillis());
    }

    @Override
    public int getDayOfMonth() {
        return this.getChronology().dayOfMonth().get(this.getMillis());
    }

    @Override
    public int getDayOfWeek() {
        return this.getChronology().dayOfWeek().get(this.getMillis());
    }

    @Override
    public int getDayOfYear() {
        return this.getChronology().dayOfYear().get(this.getMillis());
    }

    @Override
    public int getEra() {
        return this.getChronology().era().get(this.getMillis());
    }

    @Override
    public int getHourOfDay() {
        return this.getChronology().hourOfDay().get(this.getMillis());
    }

    @Override
    public int getMillisOfDay() {
        return this.getChronology().millisOfDay().get(this.getMillis());
    }

    @Override
    public int getMillisOfSecond() {
        return this.getChronology().millisOfSecond().get(this.getMillis());
    }

    @Override
    public int getMinuteOfDay() {
        return this.getChronology().minuteOfDay().get(this.getMillis());
    }

    @Override
    public int getMinuteOfHour() {
        return this.getChronology().minuteOfHour().get(this.getMillis());
    }

    @Override
    public int getMonthOfYear() {
        return this.getChronology().monthOfYear().get(this.getMillis());
    }

    @Override
    public int getSecondOfDay() {
        return this.getChronology().secondOfDay().get(this.getMillis());
    }

    @Override
    public int getSecondOfMinute() {
        return this.getChronology().secondOfMinute().get(this.getMillis());
    }

    @Override
    public int getWeekOfWeekyear() {
        return this.getChronology().weekOfWeekyear().get(this.getMillis());
    }

    @Override
    public int getWeekyear() {
        return this.getChronology().weekyear().get(this.getMillis());
    }

    @Override
    public int getYear() {
        return this.getChronology().year().get(this.getMillis());
    }

    @Override
    public int getYearOfCentury() {
        return this.getChronology().yearOfCentury().get(this.getMillis());
    }

    @Override
    public int getYearOfEra() {
        return this.getChronology().yearOfEra().get(this.getMillis());
    }

    public Calendar toCalendar(Locale cloneable) {
        if (cloneable == null) {
            cloneable = Locale.getDefault();
        }
        cloneable = Calendar.getInstance(this.getZone().toTimeZone(), cloneable);
        cloneable.setTime(this.toDate());
        return cloneable;
    }

    public GregorianCalendar toGregorianCalendar() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(this.getZone().toTimeZone());
        gregorianCalendar.setTime(this.toDate());
        return gregorianCalendar;
    }

    @ToString
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toString(String string) {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).print((ReadableInstant)this);
    }

    @Override
    public String toString(String string, Locale locale) throws IllegalArgumentException {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).withLocale(locale).print((ReadableInstant)this);
    }
}

