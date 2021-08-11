/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.convert.ToString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationFieldType
 *  org.joda.time.Interval
 *  org.joda.time.YearMonth$Property
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.YearMonth;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class YearMonth
extends BasePartial
implements ReadablePartial,
Serializable {
    private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
    public static final int MONTH_OF_YEAR = 1;
    public static final int YEAR = 0;
    private static final long serialVersionUID = 797544782896179L;

    public YearMonth() {
    }

    public YearMonth(int n, int n2) {
        this(n, n2, null);
    }

    public YearMonth(int n, int n2, Chronology chronology) {
        super(new int[]{n, n2}, chronology);
    }

    public YearMonth(long l) {
        super(l);
    }

    public YearMonth(long l, Chronology chronology) {
        super(l, chronology);
    }

    public YearMonth(Object object) {
        super(object, null, ISODateTimeFormat.localDateParser());
    }

    public YearMonth(Object object, Chronology chronology) {
        super(object, DateTimeUtils.getChronology((Chronology)chronology), ISODateTimeFormat.localDateParser());
    }

    public YearMonth(Chronology chronology) {
        super(chronology);
    }

    public YearMonth(DateTimeZone dateTimeZone) {
        super((Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    YearMonth(YearMonth yearMonth, Chronology chronology) {
        super((BasePartial)yearMonth, chronology);
    }

    YearMonth(YearMonth yearMonth, int[] arrn) {
        super((BasePartial)yearMonth, arrn);
    }

    public static YearMonth fromCalendarFields(Calendar calendar) {
        if (calendar != null) {
            return new YearMonth(calendar.get(1), calendar.get(2) + 1);
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }

    public static YearMonth fromDateFields(Date date) {
        if (date != null) {
            return new YearMonth(date.getYear() + 1900, date.getMonth() + 1);
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static YearMonth now() {
        return new YearMonth();
    }

    public static YearMonth now(Chronology chronology) {
        if (chronology != null) {
            return new YearMonth(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static YearMonth now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new YearMonth(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static YearMonth parse(String string) {
        return YearMonth.parse(string, ISODateTimeFormat.localDateParser());
    }

    public static YearMonth parse(String object, DateTimeFormatter dateTimeFormatter) {
        object = dateTimeFormatter.parseLocalDate((String)object);
        return new YearMonth(object.getYear(), object.getMonthOfYear());
    }

    private Object readResolve() {
        if (!DateTimeZone.UTC.equals(this.getChronology().getZone())) {
            return new YearMonth(this, this.getChronology().withUTC());
        }
        return this;
    }

    @Override
    protected DateTimeField getField(int n, Chronology object) {
        switch (n) {
            default: {
                object = new StringBuilder();
                object.append("Invalid index: ");
                object.append(n);
                throw new IndexOutOfBoundsException(object.toString());
            }
            case 1: {
                return object.monthOfYear();
            }
            case 0: 
        }
        return object.year();
    }

    @Override
    public DateTimeFieldType getFieldType(int n) {
        return FIELD_TYPES[n];
    }

    @Override
    public DateTimeFieldType[] getFieldTypes() {
        return (DateTimeFieldType[])FIELD_TYPES.clone();
    }

    public int getMonthOfYear() {
        return this.getValue(1);
    }

    public int getYear() {
        return this.getValue(0);
    }

    public YearMonth minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public YearMonth minusMonths(int n) {
        return this.withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate((int)n));
    }

    public YearMonth minusYears(int n) {
        return this.withFieldAdded(DurationFieldType.years(), FieldUtils.safeNegate((int)n));
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public YearMonth plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public YearMonth plusMonths(int n) {
        return this.withFieldAdded(DurationFieldType.months(), n);
    }

    public YearMonth plusYears(int n) {
        return this.withFieldAdded(DurationFieldType.years(), n);
    }

    public  property(DateTimeFieldType dateTimeFieldType) {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int size() {
        return 2;
    }

    public Interval toInterval() {
        return this.toInterval(null);
    }

    public Interval toInterval(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        return new Interval((ReadableInstant)this.toLocalDate(1).toDateTimeAtStartOfDay(dateTimeZone), (ReadableInstant)this.plusMonths(1).toLocalDate(1).toDateTimeAtStartOfDay(dateTimeZone));
    }

    public LocalDate toLocalDate(int n) {
        return new LocalDate(this.getYear(), this.getMonthOfYear(), n, this.getChronology());
    }

    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.yearMonth().print((ReadablePartial)this);
    }

    @Override
    public String toString(String string) {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).print((ReadablePartial)this);
    }

    @Override
    public String toString(String string, Locale locale) throws IllegalArgumentException {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).withLocale(locale).print((ReadablePartial)this);
    }

    public YearMonth withChronologyRetainFields(Chronology chronology) {
        if ((chronology = DateTimeUtils.getChronology((Chronology)chronology).withUTC()) == this.getChronology()) {
            return this;
        }
        YearMonth yearMonth = new YearMonth(this, chronology);
        chronology.validate((ReadablePartial)yearMonth, this.getValues());
        return yearMonth;
    }

    public YearMonth withField(DateTimeFieldType arrn, int n) {
        int n2 = this.indexOfSupported((DateTimeFieldType)arrn);
        if (n == this.getValue(n2)) {
            return this;
        }
        arrn = this.getValues();
        return new YearMonth(this, this.getField(n2).set((ReadablePartial)this, n2, arrn, n));
    }

    public YearMonth withFieldAdded(DurationFieldType arrn, int n) {
        int n2 = this.indexOfSupported((DurationFieldType)arrn);
        if (n == 0) {
            return this;
        }
        arrn = this.getValues();
        return new YearMonth(this, this.getField(n2).add((ReadablePartial)this, n2, arrn, n));
    }

    public YearMonth withMonthOfYear(int n) {
        int[] arrn = this.getValues();
        return new YearMonth(this, this.getChronology().monthOfYear().set((ReadablePartial)this, 1, arrn, n));
    }

    public YearMonth withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            int[] arrn = this.getValues();
            for (int i = 0; i < readablePeriod.size(); ++i) {
                int n2 = this.indexOf(readablePeriod.getFieldType(i));
                if (n2 < 0) continue;
                arrn = this.getField(n2).add((ReadablePartial)this, n2, arrn, FieldUtils.safeMultiply((int)readablePeriod.getValue(i), (int)n));
            }
            return new YearMonth(this, arrn);
        }
        return this;
    }

    public YearMonth withYear(int n) {
        int[] arrn = this.getValues();
        return new YearMonth(this, this.getChronology().year().set((ReadablePartial)this, 0, arrn, n));
    }

    public  year() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}

