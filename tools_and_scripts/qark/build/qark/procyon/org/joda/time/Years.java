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

public final class Years extends BaseSingleFieldPeriod
{
    public static final Years MAX_VALUE;
    public static final Years MIN_VALUE;
    public static final Years ONE;
    private static final PeriodFormatter PARSER;
    public static final Years THREE;
    public static final Years TWO;
    public static final Years ZERO;
    private static final long serialVersionUID = 87525275727380868L;
    
    static {
        ZERO = new Years(0);
        ONE = new Years(1);
        TWO = new Years(2);
        THREE = new Years(3);
        MAX_VALUE = new Years(Integer.MAX_VALUE);
        MIN_VALUE = new Years(Integer.MIN_VALUE);
        PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.years());
    }
    
    private Years(final int n) {
        super(n);
    }
    
    @FromString
    public static Years parseYears(final String s) {
        if (s == null) {
            return Years.ZERO;
        }
        return years(Years.PARSER.parsePeriod(s).getYears());
    }
    
    private Object readResolve() {
        return years(this.getValue());
    }
    
    public static Years years(final int n) {
        if (n == Integer.MIN_VALUE) {
            return Years.MIN_VALUE;
        }
        if (n == Integer.MAX_VALUE) {
            return Years.MAX_VALUE;
        }
        switch (n) {
            default: {
                return new Years(n);
            }
            case 3: {
                return Years.THREE;
            }
            case 2: {
                return Years.TWO;
            }
            case 1: {
                return Years.ONE;
            }
            case 0: {
                return Years.ZERO;
            }
        }
    }
    
    public static Years yearsBetween(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        return years(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.years()));
    }
    
    public static Years yearsBetween(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return years(DateTimeUtils.getChronology(readablePartial.getChronology()).years().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return years(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, Years.ZERO));
    }
    
    public static Years yearsIn(final ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return Years.ZERO;
        }
        return years(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.years()));
    }
    
    public Years dividedBy(final int n) {
        if (n == 1) {
            return this;
        }
        return years(this.getValue() / n);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.years();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return PeriodType.years();
    }
    
    public int getYears() {
        return this.getValue();
    }
    
    public boolean isGreaterThan(final Years years) {
        if (years == null) {
            return this.getValue() > 0;
        }
        return this.getValue() > years.getValue();
    }
    
    public boolean isLessThan(final Years years) {
        if (years == null) {
            return this.getValue() < 0;
        }
        return this.getValue() < years.getValue();
    }
    
    public Years minus(final int n) {
        return this.plus(FieldUtils.safeNegate(n));
    }
    
    public Years minus(final Years years) {
        if (years == null) {
            return this;
        }
        return this.minus(years.getValue());
    }
    
    public Years multipliedBy(final int n) {
        return years(FieldUtils.safeMultiply(this.getValue(), n));
    }
    
    public Years negated() {
        return years(FieldUtils.safeNegate(this.getValue()));
    }
    
    public Years plus(final int n) {
        if (n == 0) {
            return this;
        }
        return years(FieldUtils.safeAdd(this.getValue(), n));
    }
    
    public Years plus(final Years years) {
        if (years == null) {
            return this;
        }
        return this.plus(years.getValue());
    }
    
    @ToString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("P");
        sb.append(String.valueOf(this.getValue()));
        sb.append("Y");
        return sb.toString();
    }
}
