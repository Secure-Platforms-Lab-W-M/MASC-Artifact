/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.convert.ToString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.ReadableInterval
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.ISOPeriodFormat
 *  org.joda.time.format.PeriodFormatter
 */
package org.joda.time;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Days
extends BaseSingleFieldPeriod {
    public static final Days FIVE;
    public static final Days FOUR;
    public static final Days MAX_VALUE;
    public static final Days MIN_VALUE;
    public static final Days ONE;
    private static final PeriodFormatter PARSER;
    public static final Days SEVEN;
    public static final Days SIX;
    public static final Days THREE;
    public static final Days TWO;
    public static final Days ZERO;
    private static final long serialVersionUID = 87525275727380865L;

    static {
        ZERO = new Days(0);
        ONE = new Days(1);
        TWO = new Days(2);
        THREE = new Days(3);
        FOUR = new Days(4);
        FIVE = new Days(5);
        SIX = new Days(6);
        SEVEN = new Days(7);
        MAX_VALUE = new Days(Integer.MAX_VALUE);
        MIN_VALUE = new Days(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.days());
    }

    private Days(int n) {
        super(n);
    }

    public static Days days(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Days(n);
                    }
                    case 7: {
                        return SEVEN;
                    }
                    case 6: {
                        return SIX;
                    }
                    case 5: {
                        return FIVE;
                    }
                    case 4: {
                        return FOUR;
                    }
                    case 3: {
                        return THREE;
                    }
                    case 2: {
                        return TWO;
                    }
                    case 1: {
                        return ONE;
                    }
                    case 0: 
                }
                return ZERO;
            }
            return MAX_VALUE;
        }
        return MIN_VALUE;
    }

    public static Days daysBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Days.days(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.days()));
    }

    public static Days daysBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return Days.days(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).days().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return Days.days(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Days daysIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Days.days(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.days()));
    }

    @FromString
    public static Days parseDays(String string) {
        if (string == null) {
            return ZERO;
        }
        return Days.days(PARSER.parsePeriod(string).getDays());
    }

    private Object readResolve() {
        return Days.days(this.getValue());
    }

    public static Days standardDaysIn(ReadablePeriod readablePeriod) {
        return Days.days(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 86400000L));
    }

    public Days dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Days.days(this.getValue() / n);
    }

    public int getDays() {
        return this.getValue();
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.days();
    }

    public boolean isGreaterThan(Days days) {
        if (days == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > days.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Days days) {
        if (days == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < days.getValue()) {
            return true;
        }
        return false;
    }

    public Days minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Days minus(Days days) {
        if (days == null) {
            return this;
        }
        return this.minus(days.getValue());
    }

    public Days multipliedBy(int n) {
        return Days.days(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Days negated() {
        return Days.days(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Days plus(int n) {
        if (n == 0) {
            return this;
        }
        return Days.days(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Days plus(Days days) {
        if (days == null) {
            return this;
        }
        return this.plus(days.getValue());
    }

    public Duration toStandardDuration() {
        return new Duration((long)this.getValue() * 86400000L);
    }

    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeMultiply((int)this.getValue(), (int)24));
    }

    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply((int)this.getValue(), (int)1440));
    }

    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply((int)this.getValue(), (int)86400));
    }

    public Weeks toStandardWeeks() {
        return Weeks.weeks(this.getValue() / 7);
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("P");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("D");
        return stringBuilder.toString();
    }
}

