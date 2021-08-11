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
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Months
extends BaseSingleFieldPeriod {
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

    private Months(int n) {
        super(n);
    }

    public static Months months(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Months(n);
                    }
                    case 12: {
                        return TWELVE;
                    }
                    case 11: {
                        return ELEVEN;
                    }
                    case 10: {
                        return TEN;
                    }
                    case 9: {
                        return NINE;
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

    public static Months monthsBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Months.months(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.months()));
    }

    public static Months monthsBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return Months.months(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).months().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return Months.months(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Months monthsIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Months.months(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.months()));
    }

    @FromString
    public static Months parseMonths(String string) {
        if (string == null) {
            return ZERO;
        }
        return Months.months(PARSER.parsePeriod(string).getMonths());
    }

    private Object readResolve() {
        return Months.months(this.getValue());
    }

    public Months dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Months.months(this.getValue() / n);
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

    public boolean isGreaterThan(Months months) {
        if (months == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > months.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Months months) {
        if (months == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < months.getValue()) {
            return true;
        }
        return false;
    }

    public Months minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Months minus(Months months) {
        if (months == null) {
            return this;
        }
        return this.minus(months.getValue());
    }

    public Months multipliedBy(int n) {
        return Months.months(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Months negated() {
        return Months.months(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Months plus(int n) {
        if (n == 0) {
            return this;
        }
        return Months.months(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Months plus(Months months) {
        if (months == null) {
            return this;
        }
        return this.plus(months.getValue());
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("P");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("M");
        return stringBuilder.toString();
    }
}

