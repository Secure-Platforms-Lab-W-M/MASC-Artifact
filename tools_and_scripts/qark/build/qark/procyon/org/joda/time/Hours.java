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

public final class Hours extends BaseSingleFieldPeriod
{
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
    
    private Hours(final int n) {
        super(n);
    }
    
    public static Hours hours(final int n) {
        if (n == Integer.MIN_VALUE) {
            return Hours.MIN_VALUE;
        }
        if (n == Integer.MAX_VALUE) {
            return Hours.MAX_VALUE;
        }
        switch (n) {
            default: {
                return new Hours(n);
            }
            case 8: {
                return Hours.EIGHT;
            }
            case 7: {
                return Hours.SEVEN;
            }
            case 6: {
                return Hours.SIX;
            }
            case 5: {
                return Hours.FIVE;
            }
            case 4: {
                return Hours.FOUR;
            }
            case 3: {
                return Hours.THREE;
            }
            case 2: {
                return Hours.TWO;
            }
            case 1: {
                return Hours.ONE;
            }
            case 0: {
                return Hours.ZERO;
            }
        }
    }
    
    public static Hours hoursBetween(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        return hours(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.hours()));
    }
    
    public static Hours hoursBetween(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalTime && readablePartial2 instanceof LocalTime) {
            return hours(DateTimeUtils.getChronology(readablePartial.getChronology()).hours().getDifference(((LocalTime)readablePartial2).getLocalMillis(), ((LocalTime)readablePartial).getLocalMillis()));
        }
        return hours(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, Hours.ZERO));
    }
    
    public static Hours hoursIn(final ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return Hours.ZERO;
        }
        return hours(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.hours()));
    }
    
    @FromString
    public static Hours parseHours(final String s) {
        if (s == null) {
            return Hours.ZERO;
        }
        return hours(Hours.PARSER.parsePeriod(s).getHours());
    }
    
    private Object readResolve() {
        return hours(this.getValue());
    }
    
    public static Hours standardHoursIn(final ReadablePeriod readablePeriod) {
        return hours(BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 3600000L));
    }
    
    public Hours dividedBy(final int n) {
        if (n == 1) {
            return this;
        }
        return hours(this.getValue() / n);
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
    
    public boolean isGreaterThan(final Hours hours) {
        if (hours == null) {
            return this.getValue() > 0;
        }
        return this.getValue() > hours.getValue();
    }
    
    public boolean isLessThan(final Hours hours) {
        if (hours == null) {
            return this.getValue() < 0;
        }
        return this.getValue() < hours.getValue();
    }
    
    public Hours minus(final int n) {
        return this.plus(FieldUtils.safeNegate(n));
    }
    
    public Hours minus(final Hours hours) {
        if (hours == null) {
            return this;
        }
        return this.minus(hours.getValue());
    }
    
    public Hours multipliedBy(final int n) {
        return hours(FieldUtils.safeMultiply(this.getValue(), n));
    }
    
    public Hours negated() {
        return hours(FieldUtils.safeNegate(this.getValue()));
    }
    
    public Hours plus(final int n) {
        if (n == 0) {
            return this;
        }
        return hours(FieldUtils.safeAdd(this.getValue(), n));
    }
    
    public Hours plus(final Hours hours) {
        if (hours == null) {
            return this;
        }
        return this.plus(hours.getValue());
    }
    
    public Days toStandardDays() {
        return Days.days(this.getValue() / 24);
    }
    
    public Duration toStandardDuration() {
        return new Duration(this.getValue() * 3600000L);
    }
    
    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply(this.getValue(), 60));
    }
    
    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply(this.getValue(), 3600));
    }
    
    public Weeks toStandardWeeks() {
        return Weeks.weeks(this.getValue() / 168);
    }
    
    @ToString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PT");
        sb.append(String.valueOf(this.getValue()));
        sb.append("H");
        return sb.toString();
    }
}
