// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.convert.ToString;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Locale;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadableDateTime;

public abstract class AbstractDateTime extends AbstractInstant implements ReadableDateTime
{
    protected AbstractDateTime() {
    }
    
    @Override
    public int get(final DateTimeFieldType dateTimeFieldType) {
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
    
    public Calendar toCalendar(Locale default1) {
        if (default1 == null) {
            default1 = Locale.getDefault();
        }
        final Calendar instance = Calendar.getInstance(this.getZone().toTimeZone(), default1);
        instance.setTime(this.toDate());
        return instance;
    }
    
    public GregorianCalendar toGregorianCalendar() {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(this.getZone().toTimeZone());
        gregorianCalendar.setTime(this.toDate());
        return gregorianCalendar;
    }
    
    @ToString
    @Override
    public String toString() {
        return super.toString();
    }
    
    @Override
    public String toString(final String s) {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).print((ReadableInstant)this);
    }
    
    @Override
    public String toString(final String s, final Locale locale) throws IllegalArgumentException {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).withLocale(locale).print((ReadableInstant)this);
    }
}
