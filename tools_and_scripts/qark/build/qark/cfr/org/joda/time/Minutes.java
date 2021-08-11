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
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Hours;
import org.joda.time.LocalTime;
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

public final class Minutes
extends BaseSingleFieldPeriod {
    public static final Minutes MAX_VALUE;
    public static final Minutes MIN_VALUE;
    public static final Minutes ONE;
    private static final PeriodFormatter PARSER;
    public static final Minutes THREE;
    public static final Minutes TWO;
    public static final Minutes ZERO;
    private static final long serialVersionUID = 87525275727380863L;

    static {
        ZERO = new Minutes(0);
        ONE = new Minutes(1);
        TWO = new Minutes(2);
        THREE = new Minutes(3);
        MAX_VALUE = new Minutes(Integer.MAX_VALUE);
        MIN_VALUE = new Minutes(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.minutes());
    }

    private Minutes(int n) {
        super(n);
    }

    public static Minutes minutes(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Minutes(n);
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

    public static Minutes minutesBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Minutes.minutes(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.minutes()));
    }

    public static Minutes minutesBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalTime && readablePartial2 instanceof LocalTime) {
            return Minutes.minutes(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).minutes().getDifference(((LocalTime)readablePartial2).getLocalMillis(), ((LocalTime)readablePartial).getLocalMillis()));
        }
        return Minutes.minutes(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Minutes minutesIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Minutes.minutes(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.minutes()));
    }

    @FromString
    public static Minutes parseMinutes(String string) {
        if (string == null) {
            return ZERO;
        }
        return Minutes.minutes(PARSER.parsePeriod(string).getMinutes());
    }

    private Object readResolve() {
        return Minutes.minutes(this.getValue());
    }

    public static Minutes standardMinutesIn(ReadablePeriod readablePeriod) {
        return Minutes.minutes(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 60000L));
    }

    public Minutes dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Minutes.minutes(this.getValue() / n);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.minutes();
    }

    public int getMinutes() {
        return this.getValue();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.minutes();
    }

    public boolean isGreaterThan(Minutes minutes) {
        if (minutes == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > minutes.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Minutes minutes) {
        if (minutes == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < minutes.getValue()) {
            return true;
        }
        return false;
    }

    public Minutes minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Minutes minus(Minutes minutes) {
        if (minutes == null) {
            return this;
        }
        return this.minus(minutes.getValue());
    }

    public Minutes multipliedBy(int n) {
        return Minutes.minutes(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Minutes negated() {
        return Minutes.minutes(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Minutes plus(int n) {
        if (n == 0) {
            return this;
        }
        return Minutes.minutes(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Minutes plus(Minutes minutes) {
        if (minutes == null) {
            return this;
        }
        return this.plus(minutes.getValue());
    }

    public Days toStandardDays() {
        return Days.days(this.getValue() / 1440);
    }

    public Duration toStandardDuration() {
        return new Duration((long)this.getValue() * 60000L);
    }

    public Hours toStandardHours() {
        return Hours.hours(this.getValue() / 60);
    }

    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply((int)this.getValue(), (int)60));
    }

    public Weeks toStandardWeeks() {
        return Weeks.weeks(this.getValue() / 10080);
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PT");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("M");
        return stringBuilder.toString();
    }
}

