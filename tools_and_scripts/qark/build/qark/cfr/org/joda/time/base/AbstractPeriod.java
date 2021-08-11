/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.ToString
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.format.ISOPeriodFormat
 *  org.joda.time.format.PeriodFormatter
 */
package org.joda.time.base;

import org.joda.convert.ToString;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public abstract class AbstractPeriod
implements ReadablePeriod {
    protected AbstractPeriod() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ReadablePeriod)) {
            return false;
        }
        object = (ReadablePeriod)object;
        if (this.size() != object.size()) {
            return false;
        }
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            if (this.getValue(i) == object.getValue(i)) {
                if (this.getFieldType(i) == object.getFieldType(i)) continue;
                return false;
            }
            return false;
        }
        return true;
    }

    @Override
    public int get(DurationFieldType durationFieldType) {
        int n = this.indexOf(durationFieldType);
        if (n == -1) {
            return 0;
        }
        return this.getValue(n);
    }

    @Override
    public DurationFieldType getFieldType(int n) {
        return this.getPeriodType().getFieldType(n);
    }

    public DurationFieldType[] getFieldTypes() {
        DurationFieldType[] arrdurationFieldType = new DurationFieldType[this.size()];
        for (int i = 0; i < arrdurationFieldType.length; ++i) {
            arrdurationFieldType[i] = this.getFieldType(i);
        }
        return arrdurationFieldType;
    }

    public int[] getValues() {
        int[] arrn = new int[this.size()];
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = this.getValue(i);
        }
        return arrn;
    }

    @Override
    public int hashCode() {
        int n = this.size();
        int n2 = 17;
        for (int i = 0; i < n; ++i) {
            n2 = (n2 * 27 + this.getValue(i)) * 27 + this.getFieldType(i).hashCode();
        }
        return n2;
    }

    public int indexOf(DurationFieldType durationFieldType) {
        return this.getPeriodType().indexOf(durationFieldType);
    }

    @Override
    public boolean isSupported(DurationFieldType durationFieldType) {
        return this.getPeriodType().isSupported(durationFieldType);
    }

    @Override
    public int size() {
        return this.getPeriodType().size();
    }

    @Override
    public MutablePeriod toMutablePeriod() {
        return new MutablePeriod(this);
    }

    @Override
    public Period toPeriod() {
        return new Period(this);
    }

    @ToString
    @Override
    public String toString() {
        return ISOPeriodFormat.standard().print((ReadablePeriod)this);
    }

    public String toString(PeriodFormatter periodFormatter) {
        if (periodFormatter == null) {
            return this.toString();
        }
        return periodFormatter.print((ReadablePeriod)this);
    }
}

