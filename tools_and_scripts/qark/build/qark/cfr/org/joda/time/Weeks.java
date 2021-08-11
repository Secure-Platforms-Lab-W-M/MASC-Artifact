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
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Weeks
extends BaseSingleFieldPeriod {
    public static final Weeks MAX_VALUE;
    public static final Weeks MIN_VALUE;
    public static final Weeks ONE;
    private static final PeriodFormatter PARSER;
    public static final Weeks THREE;
    public static final Weeks TWO;
    public static final Weeks ZERO;
    private static final long serialVersionUID = 87525275727380866L;

    static {
        ZERO = new Weeks(0);
        ONE = new Weeks(1);
        TWO = new Weeks(2);
        THREE = new Weeks(3);
        MAX_VALUE = new Weeks(Integer.MAX_VALUE);
        MIN_VALUE = new Weeks(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.weeks());
    }

    private Weeks(int n) {
        super(n);
    }

    @FromString
    public static Weeks parseWeeks(String string) {
        if (string == null) {
            return ZERO;
        }
        return Weeks.weeks(PARSER.parsePeriod(string).getWeeks());
    }

    private Object readResolve() {
        return Weeks.weeks(this.getValue());
    }

    public static Weeks standardWeeksIn(ReadablePeriod readablePeriod) {
        return Weeks.weeks(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 604800000L));
    }

    public static Weeks weeks(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Weeks(n);
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

    public static Weeks weeksBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Weeks.weeks(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.weeks()));
    }

    public static Weeks weeksBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return Weeks.weeks(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).weeks().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return Weeks.weeks(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Weeks weeksIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Weeks.weeks(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.weeks()));
    }

    public Weeks dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Weeks.weeks(this.getValue() / n);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.weeks();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.weeks();
    }

    public int getWeeks() {
        return this.getValue();
    }

    public boolean isGreaterThan(Weeks weeks) {
        if (weeks == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > weeks.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Weeks weeks) {
        if (weeks == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < weeks.getValue()) {
            return true;
        }
        return false;
    }

    public Weeks minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Weeks minus(Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return this.minus(weeks.getValue());
    }

    public Weeks multipliedBy(int n) {
        return Weeks.weeks(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Weeks negated() {
        return Weeks.weeks(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Weeks plus(int n) {
        if (n == 0) {
            return this;
        }
        return Weeks.weeks(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Weeks plus(Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return this.plus(weeks.getValue());
    }

    public Days toStandardDays() {
        return Days.days(FieldUtils.safeMultiply((int)this.getValue(), (int)7));
    }

    public Duration toStandardDuration() {
        return new Duration((long)this.getValue() * 604800000L);
    }

    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeMultiply((int)this.getValue(), (int)168));
    }

    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply((int)this.getValue(), (int)10080));
    }

    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply((int)this.getValue(), (int)604800));
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("P");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("W");
        return stringBuilder.toString();
    }
}

