// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.convert.ToString;
import org.joda.time.field.FieldUtils;
import org.joda.convert.FromString;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.base.BaseSingleFieldPeriod;

public final class Weeks extends BaseSingleFieldPeriod
{
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
    
    private Weeks(final int n) {
        super(n);
    }
    
    @FromString
    public static Weeks parseWeeks(final String s) {
        if (s == null) {
            return Weeks.ZERO;
        }
        return weeks(Weeks.PARSER.parsePeriod(s).getWeeks());
    }
    
    private Object readResolve() {
        return weeks(this.getValue());
    }
    
    public static Weeks standardWeeksIn(final ReadablePeriod readablePeriod) {
        return weeks(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 604800000L));
    }
    
    public static Weeks weeks(final int n) {
        if (n == Integer.MIN_VALUE) {
            return Weeks.MIN_VALUE;
        }
        if (n == Integer.MAX_VALUE) {
            return Weeks.MAX_VALUE;
        }
        switch (n) {
            default: {
                return new Weeks(n);
            }
            case 3: {
                return Weeks.THREE;
            }
            case 2: {
                return Weeks.TWO;
            }
            case 1: {
                return Weeks.ONE;
            }
            case 0: {
                return Weeks.ZERO;
            }
        }
    }
    
    public static Weeks weeksBetween(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        return weeks(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.weeks()));
    }
    
    public static Weeks weeksBetween(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return weeks(DateTimeUtils.getChronology(readablePartial.getChronology()).weeks().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return weeks(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, Weeks.ZERO));
    }
    
    public static Weeks weeksIn(final ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return Weeks.ZERO;
        }
        return weeks(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.weeks()));
    }
    
    public Weeks dividedBy(final int n) {
        if (n == 1) {
            return this;
        }
        return weeks(this.getValue() / n);
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
    
    public boolean isGreaterThan(final Weeks weeks) {
        if (weeks == null) {
            return this.getValue() > 0;
        }
        return this.getValue() > weeks.getValue();
    }
    
    public boolean isLessThan(final Weeks weeks) {
        if (weeks == null) {
            return this.getValue() < 0;
        }
        return this.getValue() < weeks.getValue();
    }
    
    public Weeks minus(final int n) {
        return this.plus(FieldUtils.safeNegate(n));
    }
    
    public Weeks minus(final Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return this.minus(weeks.getValue());
    }
    
    public Weeks multipliedBy(final int n) {
        return weeks(FieldUtils.safeMultiply(this.getValue(), n));
    }
    
    public Weeks negated() {
        return weeks(FieldUtils.safeNegate(this.getValue()));
    }
    
    public Weeks plus(final int n) {
        if (n == 0) {
            return this;
        }
        return weeks(FieldUtils.safeAdd(this.getValue(), n));
    }
    
    public Weeks plus(final Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return this.plus(weeks.getValue());
    }
    
    public Days toStandardDays() {
        return Days.days(FieldUtils.safeMultiply(this.getValue(), 7));
    }
    
    public Duration toStandardDuration() {
        return new Duration(this.getValue() * 604800000L);
    }
    
    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeMultiply(this.getValue(), 168));
    }
    
    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply(this.getValue(), 10080));
    }
    
    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply(this.getValue(), 604800));
    }
    
    @ToString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("P");
        sb.append(String.valueOf(this.getValue()));
        sb.append("W");
        return sb.toString();
    }
}
