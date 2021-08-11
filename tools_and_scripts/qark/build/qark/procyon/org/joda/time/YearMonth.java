// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.convert.ToString;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import java.util.Date;
import java.util.Calendar;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.ISODateTimeFormat;
import java.io.Serializable;
import org.joda.time.base.BasePartial;

public final class YearMonth extends BasePartial implements ReadablePartial, Serializable
{
    private static final DateTimeFieldType[] FIELD_TYPES;
    public static final int MONTH_OF_YEAR = 1;
    public static final int YEAR = 0;
    private static final long serialVersionUID = 797544782896179L;
    
    static {
        FIELD_TYPES = new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
    }
    
    public YearMonth() {
    }
    
    public YearMonth(final int n, final int n2) {
        this(n, n2, null);
    }
    
    public YearMonth(final int n, final int n2, final Chronology chronology) {
        super(new int[] { n, n2 }, chronology);
    }
    
    public YearMonth(final long n) {
        super(n);
    }
    
    public YearMonth(final long n, final Chronology chronology) {
        super(n, chronology);
    }
    
    public YearMonth(final Object o) {
        super(o, null, ISODateTimeFormat.localDateParser());
    }
    
    public YearMonth(final Object o, final Chronology chronology) {
        super(o, DateTimeUtils.getChronology(chronology), ISODateTimeFormat.localDateParser());
    }
    
    public YearMonth(final Chronology chronology) {
        super(chronology);
    }
    
    public YearMonth(final DateTimeZone dateTimeZone) {
        super((Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    YearMonth(final YearMonth yearMonth, final Chronology chronology) {
        super(yearMonth, chronology);
    }
    
    YearMonth(final YearMonth yearMonth, final int[] array) {
        super(yearMonth, array);
    }
    
    public static YearMonth fromCalendarFields(final Calendar calendar) {
        if (calendar != null) {
            return new YearMonth(calendar.get(1), calendar.get(2) + 1);
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    public static YearMonth fromDateFields(final Date date) {
        if (date != null) {
            return new YearMonth(date.getYear() + 1900, date.getMonth() + 1);
        }
        throw new IllegalArgumentException("The date must not be null");
    }
    
    public static YearMonth now() {
        return new YearMonth();
    }
    
    public static YearMonth now(final Chronology chronology) {
        if (chronology != null) {
            return new YearMonth(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static YearMonth now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new YearMonth(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static YearMonth parse(final String s) {
        return parse(s, ISODateTimeFormat.localDateParser());
    }
    
    public static YearMonth parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        final LocalDate localDate = dateTimeFormatter.parseLocalDate(s);
        return new YearMonth(localDate.getYear(), localDate.getMonthOfYear());
    }
    
    private Object readResolve() {
        if (!DateTimeZone.UTC.equals(this.getChronology().getZone())) {
            return new YearMonth(this, this.getChronology().withUTC());
        }
        return this;
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
                return chronology.monthOfYear();
            }
            case 0: {
                return chronology.year();
            }
        }
    }
    
    @Override
    public DateTimeFieldType getFieldType(final int n) {
        return YearMonth.FIELD_TYPES[n];
    }
    
    @Override
    public DateTimeFieldType[] getFieldTypes() {
        return YearMonth.FIELD_TYPES.clone();
    }
    
    public int getMonthOfYear() {
        return this.getValue(1);
    }
    
    public int getYear() {
        return this.getValue(0);
    }
    
    public YearMonth minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public YearMonth minusMonths(final int n) {
        return this.withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate(n));
    }
    
    public YearMonth minusYears(final int n) {
        return this.withFieldAdded(DurationFieldType.years(), FieldUtils.safeNegate(n));
    }
    
    public YearMonth.YearMonth$Property monthOfYear() {
        return new YearMonth.YearMonth$Property(this, 1);
    }
    
    public YearMonth plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public YearMonth plusMonths(final int n) {
        return this.withFieldAdded(DurationFieldType.months(), n);
    }
    
    public YearMonth plusYears(final int n) {
        return this.withFieldAdded(DurationFieldType.years(), n);
    }
    
    public YearMonth.YearMonth$Property property(final DateTimeFieldType dateTimeFieldType) {
        return new YearMonth.YearMonth$Property(this, this.indexOfSupported(dateTimeFieldType));
    }
    
    @Override
    public int size() {
        return 2;
    }
    
    public Interval toInterval() {
        return this.toInterval(null);
    }
    
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return new Interval((ReadableInstant)this.toLocalDate(1).toDateTimeAtStartOfDay(zone), (ReadableInstant)this.plusMonths(1).toLocalDate(1).toDateTimeAtStartOfDay(zone));
    }
    
    public LocalDate toLocalDate(final int n) {
        return new LocalDate(this.getYear(), this.getMonthOfYear(), n, this.getChronology());
    }
    
    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.yearMonth().print((ReadablePartial)this);
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
    
    public YearMonth withChronologyRetainFields(Chronology withUTC) {
        withUTC = DateTimeUtils.getChronology(withUTC).withUTC();
        if (withUTC == this.getChronology()) {
            return this;
        }
        final YearMonth yearMonth = new YearMonth(this, withUTC);
        withUTC.validate((ReadablePartial)yearMonth, this.getValues());
        return yearMonth;
    }
    
    public YearMonth withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        final int indexOfSupported = this.indexOfSupported(dateTimeFieldType);
        if (n == this.getValue(indexOfSupported)) {
            return this;
        }
        return new YearMonth(this, this.getField(indexOfSupported).set((ReadablePartial)this, indexOfSupported, this.getValues(), n));
    }
    
    public YearMonth withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        final int indexOfSupported = this.indexOfSupported(durationFieldType);
        if (n == 0) {
            return this;
        }
        return new YearMonth(this, this.getField(indexOfSupported).add((ReadablePartial)this, indexOfSupported, this.getValues(), n));
    }
    
    public YearMonth withMonthOfYear(final int n) {
        return new YearMonth(this, this.getChronology().monthOfYear().set((ReadablePartial)this, 1, this.getValues(), n));
    }
    
    public YearMonth withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
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
        return new YearMonth(this, array);
    }
    
    public YearMonth withYear(final int n) {
        return new YearMonth(this, this.getChronology().year().set((ReadablePartial)this, 0, this.getValues(), n));
    }
    
    public YearMonth.YearMonth$Property year() {
        return new YearMonth.YearMonth$Property(this, 0);
    }
}
