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
import org.joda.time.LocalTime;
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

public final class Hours
extends BaseSingleFieldPeriod {
    public static final Hours EIGHT;
    public static final Hours FIVE;
    public static final Hours FOUR;
    public static final Hours MAX_VALUE;
    public static final Hours MIN_VALUE;
    public static final Hours ONE;
    private static final PeriodFormatter PARSER;
    public static final Hours SEVEN;
    public static final Hours SIX;
    public static final Hours THREE;
    public static final Hours TWO;
    public static final Hours ZERO;
    private static final long serialVersionUID = 87525275727380864L;

    static {
        ZERO = new Hours(0);
        ONE = new Hours(1);
        TWO = new Hours(2);
        THREE = new Hours(3);
        FOUR = new Hours(4);
        FIVE = new Hours(5);
        SIX = new Hours(6);
        SEVEN = new Hours(7);
        EIGHT = new Hours(8);
        MAX_VALUE = new Hours(Integer.MAX_VALUE);
        MIN_VALUE = new Hours(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.hours());
    }

    private Hours(int n) {
        super(n);
    }

    public static Hours hours(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Hours(n);
                    }
                    case 8: {
                        return EIGHT;
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

    public static Hours hoursBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Hours.hours(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.hours()));
    }

    public static Hours hoursBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalTime && readablePartial2 instanceof LocalTime) {
            return Hours.hours(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).hours().getDifference(((LocalTime)readablePartial2).getLocalMillis(), ((LocalTime)readablePartial).getLocalMillis()));
        }
        return Hours.hours(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Hours hoursIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Hours.hours(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.hours()));
    }

    @FromString
    public static Hours parseHours(String string) {
        if (string == null) {
            return ZERO;
        }
        return Hours.hours(PARSER.parsePeriod(string).getHours());
    }

    private Object readResolve() {
        return Hours.hours(this.getValue());
    }

    public static Hours standardHoursIn(ReadablePeriod readablePeriod) {
        return Hours.hours(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 3600000L));
    }

    public Hours dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Hours.hours(this.getValue() / n);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.hours();
    }

    public int getHours() {
        return this.getValue();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.hours();
    }

    public boolean isGreaterThan(Hours hours) {
        if (hours == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > hours.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Hours hours) {
        if (hours == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < hours.getValue()) {
            return true;
        }
        return false;
    }

    public Hours minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Hours minus(Hours hours) {
        if (hours == null) {
            return this;
        }
        return this.minus(hours.getValue());
    }

    public Hours multipliedBy(int n) {
        return Hours.hours(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Hours negated() {
        return Hours.hours(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Hours plus(int n) {
        if (n == 0) {
            return this;
        }
        return Hours.hours(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Hours plus(Hours hours) {
        if (hours == null) {
            return this;
        }
        return this.plus(hours.getValue());
    }

    public Days toStandardDays() {
        return Days.days(this.getValue() / 24);
    }

    public Duration toStandardDuration() {
        return new Duration((long)this.getValue() * 3600000L);
    }

    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply((int)this.getValue(), (int)60));
    }

    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply((int)this.getValue(), (int)3600));
    }

    public Weeks toStandardWeeks() {
        return Weeks.weeks(this.getValue() / 168);
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PT");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("H");
        return stringBuilder.toString();
    }
}

