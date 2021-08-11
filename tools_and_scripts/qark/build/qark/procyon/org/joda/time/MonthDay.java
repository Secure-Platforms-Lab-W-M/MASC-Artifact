// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.util.Locale;
import org.joda.convert.ToString;
import java.util.Collection;
import java.util.ArrayList;
import org.joda.time.field.FieldUtils;
import org.joda.convert.FromString;
import java.util.Date;
import java.util.Calendar;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeFormatter;
import java.io.Serializable;
import org.joda.time.base.BasePartial;

public final class MonthDay extends BasePartial implements ReadablePartial, Serializable
{
    public static final int DAY_OF_MONTH = 1;
    private static final DateTimeFieldType[] FIELD_TYPES;
    public static final int MONTH_OF_YEAR = 0;
    private static final DateTimeFormatter PARSER;
    private static final long serialVersionUID = 2954560699050434609L;
    
    static {
        FIELD_TYPES = new DateTimeFieldType[] { DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
        PARSER = new DateTimeFormatterBuilder().appendOptional(ISODateTimeFormat.localDateParser().getParser()).appendOptional(DateTimeFormat.forPattern("--MM-dd").getParser()).toFormatter();
    }
    
    public MonthDay() {
    }
    
    public MonthDay(final int n, final int n2) {
        this(n, n2, null);
    }
    
    public MonthDay(final int n, final int n2, final Chronology chronology) {
        super(new int[] { n, n2 }, chronology);
    }
    
    public MonthDay(final long n) {
        super(n);
    }
    
    public MonthDay(final long n, final Chronology chronology) {
        super(n, chronology);
    }
    
    public MonthDay(final Object o) {
        super(o, null, ISODateTimeFormat.localDateParser());
    }
    
    public MonthDay(final Object o, final Chronology chronology) {
        super(o, DateTimeUtils.getChronology(chronology), ISODateTimeFormat.localDateParser());
    }
    
    public MonthDay(final Chronology chronology) {
        super(chronology);
    }
    
    public MonthDay(final DateTimeZone dateTimeZone) {
        super((Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    MonthDay(final MonthDay monthDay, final Chronology chronology) {
        super(monthDay, chronology);
    }
    
    MonthDay(final MonthDay monthDay, final int[] array) {
        super(monthDay, array);
    }
    
    public static MonthDay fromCalendarFields(final Calendar calendar) {
        if (calendar != null) {
            return new MonthDay(calendar.get(2) + 1, calendar.get(5));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    public static MonthDay fromDateFields(final Date date) {
        if (date != null) {
            return new MonthDay(date.getMonth() + 1, date.getDate());
        }
        throw new IllegalArgumentException("The date must not be null");
    }
    
    public static MonthDay now() {
        return new MonthDay();
    }
    
    public static MonthDay now(final Chronology chronology) {
        if (chronology != null) {
            return new MonthDay(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static MonthDay now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new MonthDay(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static MonthDay parse(final String s) {
        return parse(s, MonthDay.PARSER);
    }
    
    public static MonthDay parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        final LocalDate localDate = dateTimeFormatter.parseLocalDate(s);
        return new MonthDay(localDate.getMonthOfYear(), localDate.getDayOfMonth());
    }
    
    private Object readResolve() {
        if (!DateTimeZone.UTC.equals(this.getChronology().getZone())) {
            return new MonthDay(this, this.getChronology().withUTC());
        }
        return this;
    }
    
    public MonthDay.MonthDay$Property dayOfMonth() {
        return new MonthDay.MonthDay$Property(this, 1);
    }
    
    public int getDayOfMonth() {
        return this.getValue(1);
    }
    
    @Override
    protected DateTimeField getField(final int n, final Chronology chronology) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid index: ");
                sb.append(n);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            case 1: {
                return chronology.dayOfMonth();
            }
            case 0: {
                return chronology.monthOfYear();
            }
        }
    }
    
    @Override
    public DateTimeFieldType getFieldType(final int n) {
        return MonthDay.FIELD_TYPES[n];
    }
    
    @Override
    public DateTimeFieldType[] getFieldTypes() {
        return MonthDay.FIELD_TYPES.clone();
    }
    
    public int getMonthOfYear() {
        return this.getValue(0);
    }
    
    public MonthDay minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public MonthDay minusDays(final int n) {
        return this.withFieldAdded(DurationFieldType.days(), FieldUtils.safeNegate(n));
    }
    
    public MonthDay minusMonths(final int n) {
        return this.withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate(n));
    }
    
    public MonthDay.MonthDay$Property monthOfYear() {
        return new MonthDay.MonthDay$Property(this, 0);
    }
    
    public MonthDay plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public MonthDay plusDays(final int n) {
        return this.withFieldAdded(DurationFieldType.days(), n);
    }
    
    public MonthDay plusMonths(final int n) {
        return this.withFieldAdded(DurationFieldType.months(), n);
    }
    
    public MonthDay.MonthDay$Property property(final DateTimeFieldType dateTimeFieldType) {
        return new MonthDay.MonthDay$Property(this, this.indexOfSupported(dateTimeFieldType));
    }
    
    @Override
    public int size() {
        return 2;
    }
    
    public LocalDate toLocalDate(final int n) {
        return new LocalDate(n, this.getMonthOfYear(), this.getDayOfMonth(), this.getChronology());
    }
    
    @ToString
    @Override
    public String toString() {
        final ArrayList<DateTimeFieldType> list = new ArrayList<DateTimeFieldType>();
        list.add(DateTimeFieldType.monthOfYear());
        list.add(DateTimeFieldType.dayOfMonth());
        return ISODateTimeFormat.forFields((Collection)list, true, true).print((ReadablePartial)this);
    }
    
    @Override
    public String toString(final String s) {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).print((ReadablePartial)this);
    }
    
    @Override
    public String toString(final String s, final Locale locale) throws IllegalArgumentException {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).withLocale(locale).print((ReadablePartial)this);
    }
    
    public MonthDay withChronologyRetainFields(Chronology withUTC) {
        withUTC = DateTimeUtils.getChronology(withUTC).withUTC();
        if (withUTC == this.getChronology()) {
            return this;
        }
        final MonthDay monthDay = new MonthDay(this, withUTC);
        withUTC.validate((ReadablePartial)monthDay, this.getValues());
        return monthDay;
    }
    
    public MonthDay withDayOfMonth(final int n) {
        return new MonthDay(this, this.getChronology().dayOfMonth().set((ReadablePartial)this, 1, this.getValues(), n));
    }
    
    public MonthDay withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        final int indexOfSupported = this.indexOfSupported(dateTimeFieldType);
        if (n == this.getValue(indexOfSupported)) {
            return this;
        }
        return new MonthDay(this, this.getField(indexOfSupported).set((ReadablePartial)this, indexOfSupported, this.getValues(), n));
    }
    
    public MonthDay withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        final int indexOfSupported = this.indexOfSupported(durationFieldType);
        if (n == 0) {
            return this;
        }
        return new MonthDay(this, this.getField(indexOfSupported).add((ReadablePartial)this, indexOfSupported, this.getValues(), n));
    }
    
    public MonthDay withMonthOfYear(final int n) {
        return new MonthDay(this, this.getChronology().monthOfYear().set((ReadablePartial)this, 0, this.getValues(), n));
    }
    
    public MonthDay withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        int[] array = this.getValues();
        for (int i = 0; i < readablePeriod.size(); ++i) {
            final int index = this.indexOf(readablePeriod.getFieldType(i));
            if (index >= 0) {
                array = this.getField(index).add((ReadablePartial)this, index, array, FieldUtils.safeMultiply(readablePeriod.getValue(i), n));
            }
        }
        return new MonthDay(this, array);
    }
}
