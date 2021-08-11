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

public final class Years
extends BaseSingleFieldPeriod {
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

    private Years(int n) {
        super(n);
    }

    @FromString
    public static Years parseYears(String string) {
        if (string == null) {
            return ZERO;
        }
        return Years.years(PARSER.parsePeriod(string).getYears());
    }

    private Object readResolve() {
        return Years.years(this.getValue());
    }

    public static Years years(int n) {
        if (n != Integer.MIN_VALUE) {
            if (n != Integer.MAX_VALUE) {
                switch (n) {
                    default: {
                        return new Years(n);
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

    public static Years yearsBetween(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        return Years.years(BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.years()));
    }

    public static Years yearsBetween(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial instanceof LocalDate && readablePartial2 instanceof LocalDate) {
            return Years.years(DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).years().getDifference(((LocalDate)readablePartial2).getLocalMillis(), ((LocalDate)readablePartial).getLocalMillis()));
        }
        return Years.years(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, ZERO));
    }

    public static Years yearsIn(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            return ZERO;
        }
        return Years.years(BaseSingleFieldPeriod.between(readableInterval.getStart(), readableInterval.getEnd(), DurationFieldType.years()));
    }

    public Years dividedBy(int n) {
        if (n == 1) {
            return this;
        }
        return Years.years(this.getValue() / n);
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

    public boolean isGreaterThan(Years years) {
        if (years == null) {
            if (this.getValue() > 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() > years.getValue()) {
            return true;
        }
        return false;
    }

    public boolean isLessThan(Years years) {
        if (years == null) {
            if (this.getValue() < 0) {
                return true;
            }
            return false;
        }
        if (this.getValue() < years.getValue()) {
            return true;
        }
        return false;
    }

    public Years minus(int n) {
        return this.plus(FieldUtils.safeNegate((int)n));
    }

    public Years minus(Years years) {
        if (years == null) {
            return this;
        }
        return this.minus(years.getValue());
    }

    public Years multipliedBy(int n) {
        return Years.years(FieldUtils.safeMultiply((int)this.getValue(), (int)n));
    }

    public Years negated() {
        return Years.years(FieldUtils.safeNegate((int)this.getValue()));
    }

    public Years plus(int n) {
        if (n == 0) {
            return this;
        }
        return Years.years(FieldUtils.safeAdd((int)this.getValue(), (int)n));
    }

    public Years plus(Years years) {
        if (years == null) {
            return this;
        }
        return this.plus(years.getValue());
    }

    @ToString
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("P");
        stringBuilder.append(String.valueOf(this.getValue()));
        stringBuilder.append("Y");
        return stringBuilder.toString();
    }
}

