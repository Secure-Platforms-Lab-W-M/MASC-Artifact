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

public final class Months extends BaseSingleFieldPeriod
{
    public static final Months EIGHT;
    public static final Months ELEVEN;
    public static final Months FIVE;
    public static final Months FOUR;
    public static final Months MAX_VALUE;
    public static final Months MIN_VALUE;
    public static final Months NINE;
    public static final Months ONE;
    private static final PeriodFormatter PARSER;
    public static final Months SEVEN;
    public static final Months SIX;
    public static final Months TEN;
    public static final Months THREE;
    public static final Months TWELVE;
    public static final Months TWO;
    public static final Months ZERO;
    private static final long serialVersionUID = 87525275727380867L;
    
    static {
        ZERO = new Months(0);
        ONE = new Months(1);
        TWO = new Months(2);
        THREE = new Months(3);
        FOUR = new Months(4);
        FIVE = new Months(5);
        SIX = new Months(6);
        SEVEN = new Months(7);
        EIGHT = new Months(8);
        NINE = new Months(9);
        TEN = new Months(10);
        ELEVEN = new Months(11);
        TWELVE = new Months(12);
        MAX_VALUE = new Months(Integer.MAX_VALUE);
        MIN_VALUE = new Months(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.months());
    }
    
    private Months(final int n) {
        super(n);
    }
    
    public static Months months(final int n) {
        if (n == Integer.MIN_VALUE) {
            return Months.MIN_VALUE;
        }
        if (n == Integer.MAX_VALUE) {
            return Months.MAX_VALUE;
        }
        switch (n) {
            default: {
                return new Months(n);
            }
            case 12: {
                return Months.TWELVE;
            }
            case 11: {
                return Months.ELEVEN;
            }
            case 10: {
                return Months.TEN;
            }
            case 9: {
                return Months.NINE;
            }
            case 8: {
                return Months.EIGHT;
            }
            case 7: {
                return Months.SEVEN;
            }
            case 6: {
                return Months.SIX;
            }
            case 5: {
                return Months.FIVE;
            }
            case 4: {
                return Months.FOUR;
            }
            case 3: {
                return Months.THREE;
            }
            case 2: {
                return Months.TWO;
            }
            case 1: {
                return Months.ONE;
            }
            case 0: {
                return Months.ZERO;
            }
        }
    }
    
    public static Months monthsBetween(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        return months(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.months()));
    }
    
    public static Months monthsBetween(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return months(DateTimeUtils.getChronology(readablePartial.getChronology()).months().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return months(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, Months.ZERO));
    }
    
    public static Months monthsIn(final ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return Months.ZERO;
        }
        return months(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.months()));
    }
    
    @FromString
    public static Months parseMonths(final String s) {
        if (s == null) {
            return Months.ZERO;
        }
        return months(Months.PARSER.parsePeriod(s).getMonths());
    }
    
    private Object readResolve() {
        return months(this.getValue());
    }
    
    public Months dividedBy(final int n) {
        if (n == 1) {
            return this;
        }
        return months(this.getValue() / n);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.months();
    }
    
    public int getMonths() {
        return this.getValue();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return PeriodType.months();
    }
    
    public boolean isGreaterThan(final Months months) {
        if (months == null) {
            return this.getValue() > 0;
        }
        return this.getValue() > months.getValue();
    }
    
    public boolean isLessThan(final Months months) {
        if (months == null) {
            return this.getValue() < 0;
        }
        return this.getValue() < months.getValue();
    }
    
    public Months minus(final int n) {
        return this.plus(FieldUtils.safeNegate(n));
    }
    
    public Months minus(final Months months) {
        if (months == null) {
            return this;
        }
        return this.minus(months.getValue());
    }
    
    public Months multipliedBy(final int n) {
        return months(FieldUtils.safeMultiply(this.getValue(), n));
    }
    
    public Months negated() {
        return months(FieldUtils.safeNegate(this.getValue()));
    }
    
    public Months plus(final int n) {
        if (n == 0) {
            return this;
        }
        return months(FieldUtils.safeAdd(this.getValue(), n));
    }
    
    public Months plus(final Months months) {
        if (months == null) {
            return this;
        }
        return this.plus(months.getValue());
    }
    
    @ToString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("P");
        sb.append(String.valueOf(this.getValue()));
        sb.append("M");
        return sb.toString();
    }
}
