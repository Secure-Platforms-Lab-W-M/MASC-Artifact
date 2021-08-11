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
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Weeks;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Seconds
extends BaseSingleFieldPeriod {
    public static final Seconds MAX_VALUE;
    public static final Seconds MIN_VALUE;
    public static final Seconds ONE;
    private static final PeriodFormatter PARSER;
    public static final Seconds THREE;
    public static final Seconds TWO;
    public static final Seconds ZERO;
    private static final long serialVersionUID = 87525275727380862L;

    static {
        ZERO = new Seconds(0);
        ONE = new Seconds(1);
        TWO = new Seconds(2);
        THREE = new Seconds(3);
        MAX_VALUE = new Seconds(Integer.MAX_VALUE);
        MIN_VALUE = new Seconds(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.seconds());
    }

    private Seconds(int n) {
        super(n);
    }

    @FromString
    public static Seconds parseSeconds(String string) {
        if (string == null) {
            return ZERO;
        }
        return Seconds.seconds(PARSER.parsePeriod(string).getSeconds());
    }

    private Object readResolve() {
        return Seconds.seconds(this.getValue());
    }

    public static Seconds seconds(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Seconds(n);
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

    public static Seconds secondsBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Seconds.seconds(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.seconds()));
    }

    public static Seconds secondsBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalTime && readablePartial2 instanceof LocalTime) {
            return Seconds.seconds(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).seconds().getDifference(((LocalTime)readablePartial2).getLocalMillis(), ((LocalTime)readablePartial).getLocalMillis()));
        }
        return Seconds.seconds(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Seconds secondsIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Seconds.seconds(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.seconds()));
    }

    public static Seconds standardSecondsIn(ReadablePeriod readablePeriod) {
        return Seconds.seconds(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1000L));
    }

    public Seconds dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Seconds.seconds(this.getValue() / n);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.seconds();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.seconds();
    }

    public int getSeconds() {
        return this.getValue();
    }

    public boolean isGreaterThan(Seconds seconds) {
        if (seconds == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > seconds.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Seconds seconds) {
        if (seconds == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < seconds.getValue()) {
            return true;
        }
        return false;
    }

    public Seconds minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Seconds minus(Seconds seconds) {
        if (seconds == null) {
            return this;
        }
        return this.minus(seconds.getValue());
    }

    public Seconds multipliedBy(int n) {
        return Seconds.seconds(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Seconds negated() {
        return Seconds.seconds(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Seconds plus(int n) {
        if (n == 0) {
            return this;
        }
        return Seconds.seconds(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Seconds plus(Seconds seconds) {
        if (seconds == null) {
            return this;
        }
        return this.plus(seconds.getValue());
    }

    public Days toStandardDays() {
        return Days.days(this.getValue() / 86400);
    }

    public Duration toStandardDuration() {
        return new Duration((long)this.getValue() * 1000L);
    }

    public Hours toStandardHours() {
        return Hours.hours(this.getValue() / 3600);
    }

    public Minutes toStandardMinutes() {
        return Minutes.minutes(this.getValue() / 60);
    }

    public Weeks toStandardWeeks() {
        return Weeks.weeks(this.getValue() / 604800);
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PT");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("S");
        return stringBuilder.toString();
    }
}

