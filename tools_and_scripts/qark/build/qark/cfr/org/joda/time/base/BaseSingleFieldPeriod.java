/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 */
package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public abstract class BaseSingleFieldPeriod
implements ReadablePeriod,
Comparable<BaseSingleFieldPeriod>,
Serializable {
    private static final long START_1972 = 63072000000L;
    private static final long serialVersionUID = 9386874258972L;
    private volatile int iPeriod;

    protected BaseSingleFieldPeriod(int n) {
        this.iPeriod = n;
    }

    protected static int between(ReadableInstant readableInstant, ReadableInstant readableInstant2, DurationFieldType durationFieldType) {
        if (readableInstant != null && readableInstant2 != null) {
            return durationFieldType.getField(DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant)).getDifference(readableInstant2.getMillis(), readableInstant.getMillis());
        }
        throw new IllegalArgumentException("ReadableInstant objects must not be null");
    }

    protected static int between(ReadablePartial readablePartial, ReadablePartial readablePartial2, ReadablePeriod readablePeriod) {
        if (readablePartial != null && readablePartial2 != null) {
            if (readablePartial.size() == readablePartial2.size()) {
                int n = readablePartial.size();
                for (int i = 0; i < n; ++i) {
                    if (readablePartial.getFieldType(i) == readablePartial2.getFieldType(i)) {
                        continue;
                    }
                    throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
                }
                if (DateTimeUtils.isContiguous((ReadablePartial)readablePartial)) {
                    Chronology chronology = DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).withUTC();
                    return chronology.get(readablePeriod, chronology.set(readablePartial, 63072000000L), chronology.set(readablePartial2, 63072000000L))[0];
                }
                throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
            }
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        throw new IllegalArgumentException("ReadablePartial objects must not be null");
    }

    protected static int standardPeriodIn(ReadablePeriod readablePeriod, long l) {
        if (readablePeriod == null) {
            return 0;
        }
        Object object = ISOChronology.getInstanceUTC();
        long l2 = 0L;
        for (int i = 0; i < readablePeriod.size(); ++i) {
            int n = readablePeriod.getValue(i);
            if (n == 0) continue;
            DurationField durationField = readablePeriod.getFieldType(i).getField((Chronology)object);
            if (durationField.isPrecise()) {
                l2 = FieldUtils.safeAdd((long)l2, (long)FieldUtils.safeMultiply((long)durationField.getUnitMillis(), (int)n));
                continue;
            }
            object = new StringBuilder();
            object.append("Cannot convert period to duration as ");
            object.append(durationField.getName());
            object.append(" is not precise in the period ");
            object.append(readablePeriod);
            throw new IllegalArgumentException(object.toString());
        }
        return FieldUtils.safeToInt((long)(l2 / l));
    }

    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        if (baseSingleFieldPeriod.getClass() == this.getClass()) {
            int n = baseSingleFieldPeriod.getValue();
            int n2 = this.getValue();
            if (n2 > n) {
                return 1;
            }
            if (n2 < n) {
                return -1;
            }
            return 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass());
        stringBuilder.append(" cannot be compared to ");
        stringBuilder.append(baseSingleFieldPeriod.getClass());
        throw new ClassCastException(stringBuilder.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ReadablePeriod)) {
            return false;
        }
        if ((object = (ReadablePeriod)object).getPeriodType() == this.getPeriodType() && object.getValue(0) == this.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public int get(DurationFieldType durationFieldType) {
        if (durationFieldType == this.getFieldType()) {
            return this.getValue();
        }
        return 0;
    }

    public abstract DurationFieldType getFieldType();

    @Override
    public DurationFieldType getFieldType(int n) {
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
    public int getValue(int n) {
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
    public boolean isSupported(DurationFieldType durationFieldType) {
        if (durationFieldType == this.getFieldType()) {
            return true;
        }
        return false;
    }

    protected void setValue(int n) {
        this.iPeriod = n;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public MutablePeriod toMutablePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(this);
        return mutablePeriod;
    }

    @Override
    public Period toPeriod() {
        return Period.ZERO.withFields(this);
    }
}

