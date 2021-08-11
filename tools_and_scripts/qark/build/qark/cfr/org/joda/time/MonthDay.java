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
 *  org.joda.time.MonthDay$Property
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.DateTimeFormatterBuilder
 *  org.joda.time.format.DateTimeParser
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;

public final class MonthDay
extends BasePartial
implements ReadablePartial,
Serializable {
    public static final int DAY_OF_MONTH = 1;
    private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[]{DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
    public static final int MONTH_OF_YEAR = 0;
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendOptional(ISODateTimeFormat.localDateParser().getParser()).appendOptional(DateTimeFormat.forPattern((String)"--MM-dd").getParser()).toFormatter();
    private static final long serialVersionUID = 2954560699050434609L;

    public MonthDay() {
    }

    public MonthDay(int n, int n2) {
        this(n, n2, null);
    }

    public MonthDay(int n, int n2, Chronology chronology) {
        super(new int[]{n, n2}, chronology);
    }

    public MonthDay(long l) {
        super(l);
    }

    public MonthDay(long l, Chronology chronology) {
        super(l, chronology);
    }

    public MonthDay(Object object) {
        super(object, null, ISODateTimeFormat.localDateParser());
    }

    public MonthDay(Object object, Chronology chronology) {
        super(object, DateTimeUtils.getChronology((Chronology)chronology), ISODateTimeFormat.localDateParser());
    }

    public MonthDay(Chronology chronology) {
        super(chronology);
    }

    public MonthDay(DateTimeZone dateTimeZone) {
        super((Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    MonthDay(MonthDay monthDay, Chronology chronology) {
        super((BasePartial)monthDay, chronology);
    }

    MonthDay(MonthDay monthDay, int[] arrn) {
        super((BasePartial)monthDay, arrn);
    }

    public static MonthDay fromCalendarFields(Calendar calendar) {
        if (calendar != null) {
            return new MonthDay(calendar.get(2) + 1, calendar.get(5));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }

    public static MonthDay fromDateFields(Date date) {
        if (date != null) {
            return new MonthDay(date.getMonth() + 1, date.getDate());
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static MonthDay now() {
        return new MonthDay();
    }

    public static MonthDay now(Chronology chronology) {
        if (chronology != null) {
            return new MonthDay(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static MonthDay now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new MonthDay(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static MonthDay parse(String string) {
        return MonthDay.parse(string, PARSER);
    }

    public static MonthDay parse(String object, DateTimeFormatter dateTimeFormatter) {
        object = dateTimeFormatter.parseLocalDate((String)object);
        return new MonthDay(object.getMonthOfYear(), object.getDayOfMonth());
    }

    private Object readResolve() {
        if (!DateTimeZone.UTC.equals(this.getChronology().getZone())) {
            return new MonthDay(this, this.getChronology().withUTC());
        }
        return this;
    }

    public  dayOfMonth() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public int getDayOfMonth() {
        return this.getValue(1);
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
                return object.dayOfMonth();
            }
            case 0: 
        }
        return object.monthOfYear();
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
        return this.getValue(0);
    }

    public MonthDay minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public MonthDay minusDays(int n) {
        return this.withFieldAdded(DurationFieldType.days(), FieldUtils.safeNegate((int)n));
    }

    public MonthDay minusMonths(int n) {
        return this.withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate((int)n));
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public MonthDay plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public MonthDay plusDays(int n) {
        return this.withFieldAdded(DurationFieldType.days(), n);
    }

    public MonthDay plusMonths(int n) {
        return this.withFieldAdded(DurationFieldType.months(), n);
    }

    public  property(DateTimeFieldType dateTimeFieldType) {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int size() {
        return 2;
    }

    public LocalDate toLocalDate(int n) {
        return new LocalDate(n, this.getMonthOfYear(), this.getDayOfMonth(), this.getChronology());
    }

    @ToString
    @Override
    public String toString() {
        ArrayList<DateTimeFieldType> arrayList = new ArrayList<DateTimeFieldType>();
        arrayList.add(DateTimeFieldType.monthOfYear());
        arrayList.add(DateTimeFieldType.dayOfMonth());
        return ISODateTimeFormat.forFields(arrayList, (boolean)true, (boolean)true).print((ReadablePartial)this);
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

    public MonthDay withChronologyRetainFields(Chronology chronology) {
        if ((chronology = DateTimeUtils.getChronology((Chronology)chronology).withUTC()) == this.getChronology()) {
            return this;
        }
        MonthDay monthDay = new MonthDay(this, chronology);
        chronology.validate((ReadablePartial)monthDay, this.getValues());
        return monthDay;
    }

    public MonthDay withDayOfMonth(int n) {
        int[] arrn = this.getValues();
        return new MonthDay(this, this.getChronology().dayOfMonth().set((ReadablePartial)this, 1, arrn, n));
    }

    public MonthDay withField(DateTimeFieldType arrn, int n) {
        int n2 = this.indexOfSupported((DateTimeFieldType)arrn);
        if (n == this.getValue(n2)) {
            return this;
        }
        arrn = this.getValues();
        return new MonthDay(this, this.getField(n2).set((ReadablePartial)this, n2, arrn, n));
    }

    public MonthDay withFieldAdded(DurationFieldType arrn, int n) {
        int n2 = this.indexOfSupported((DurationFieldType)arrn);
        if (n == 0) {
            return this;
        }
        arrn = this.getValues();
        return new MonthDay(this, this.getField(n2).add((ReadablePartial)this, n2, arrn, n));
    }

    public MonthDay withMonthOfYear(int n) {
        int[] arrn = this.getValues();
        return new MonthDay(this, this.getChronology().monthOfYear().set((ReadablePartial)this, 0, arrn, n));
    }

    public MonthDay withPeriodAdded(ReadablePeriod readablePeriod, int n) {
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
            return new MonthDay(this, arrn);
        }
        return this;
    }
}

