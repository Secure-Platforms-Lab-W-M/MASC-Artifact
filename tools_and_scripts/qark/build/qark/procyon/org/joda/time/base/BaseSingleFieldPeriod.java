// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.Chronology;
import org.joda.time.ReadablePartial;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import java.io.Serializable;
import org.joda.time.ReadablePeriod;

public abstract class BaseSingleFieldPeriod implements ReadablePeriod, Comparable<BaseSingleFieldPeriod>, Serializable
{
    private static final long START_1972 = 63072000000L;
    private static final long serialVersionUID = 9386874258972L;
    private volatile int iPeriod;
    
    protected BaseSingleFieldPeriod(final int iPeriod) {
        this.iPeriod = iPeriod;
    }
    
    protected static int between(final ReadableInstant readableInstant, final ReadableInstant readableInstant2, final DurationFieldType durationFieldType) {
        if (readableInstant != null && readableInstant2 != null) {
            return durationFieldType.getField(DateTimeUtils.getInstantChronology(readableInstant)).getDifference(readableInstant2.getMillis(), readableInstant.getMillis());
        }
        throw new IllegalArgumentException("ReadableInstant objects must not be null");
    }
    
    protected static int between(final ReadablePartial readablePartial, final ReadablePartial readablePartial2, final ReadablePeriod readablePeriod) {
        if (readablePartial == null || readablePartial2 == null) {
            throw new IllegalArgumentException("ReadablePartial objects must not be null");
        }
        if (readablePartial.size() != readablePartial2.size()) {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        for (int size = readablePartial.size(), i = 0; i < size; ++i) {
            if (readablePartial.getFieldType(i) != readablePartial2.getFieldType(i)) {
                throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
            }
        }
        if (DateTimeUtils.isContiguous(readablePartial)) {
            final Chronology withUTC = DateTimeUtils.getChronology(readablePartial.getChronology()).withUTC();
            return withUTC.get(readablePeriod, withUTC.set(readablePartial, 63072000000L), withUTC.set(readablePartial2, 63072000000L))[0];
        }
        throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
    }
    
    protected static int standardPeriodIn(final ReadablePeriod readablePeriod, final long n) {
        int i = 0;
        if (readablePeriod == null) {
            return 0;
        }
        final ISOChronology instanceUTC = ISOChronology.getInstanceUTC();
        long safeAdd = 0L;
        while (i < readablePeriod.size()) {
            final int value = readablePeriod.getValue(i);
            if (value != 0) {
                final DurationField field = readablePeriod.getFieldType(i).getField((Chronology)instanceUTC);
                if (!field.isPrecise()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot convert period to duration as ");
                    sb.append(field.getName());
                    sb.append(" is not precise in the period ");
                    sb.append(readablePeriod);
                    throw new IllegalArgumentException(sb.toString());
                }
                safeAdd = FieldUtils.safeAdd(safeAdd, FieldUtils.safeMultiply(field.getUnitMillis(), value));
            }
            ++i;
        }
        return FieldUtils.safeToInt(safeAdd / n);
    }
    
    @Override
    public int compareTo(final BaseSingleFieldPeriod baseSingleFieldPeriod) {
        if (baseSingleFieldPeriod.getClass() != this.getClass()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass());
            sb.append(" cannot be compared to ");
            sb.append(baseSingleFieldPeriod.getClass());
            throw new ClassCastException(sb.toString());
        }
        final int value = baseSingleFieldPeriod.getValue();
        final int value2 = this.getValue();
        if (value2 > value) {
            return 1;
        }
        if (value2 < value) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReadablePeriod)) {
            return false;
        }
        final ReadablePeriod readablePeriod = (ReadablePeriod)o;
        return readablePeriod.getPeriodType() == this.getPeriodType() && readablePeriod.getValue(0) == this.getValue();
    }
    
    @Override
    public int get(final DurationFieldType durationFieldType) {
        if (durationFieldType == this.getFieldType()) {
            return this.getValue();
        }
        return 0;
    }
    
    public abstract DurationFieldType getFieldType();
    
    @Override
    public DurationFieldType getFieldType(final int n) {
        if (n == 0) {
            return this.getFieldType();
        }
        throw new IndexOutOfBoundsException(String.valueOf(n));
    }
    
    @Override
    public abstract PeriodType getPeriodType();
    
    protected int getValue() {
        return this.iPeriod;
    }
    
    @Override
    public int getValue(final int n) {
        if (n == 0) {
            return this.getValue();
        }
        throw new IndexOutOfBoundsException(String.valueOf(n));
    }
    
    @Override
    public int hashCode() {
        return (459 + this.getValue()) * 27 + this.getFieldType().hashCode();
    }
    
    @Override
    public boolean isSupported(final DurationFieldType durationFieldType) {
        return durationFieldType == this.getFieldType();
    }
    
    protected void setValue(final int iPeriod) {
        this.iPeriod = iPeriod;
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public MutablePeriod toMutablePeriod() {
        final MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(this);
        return mutablePeriod;
    }
    
    @Override
    public Period toPeriod() {
        return Period.ZERO.withFields(this);
    }
}
