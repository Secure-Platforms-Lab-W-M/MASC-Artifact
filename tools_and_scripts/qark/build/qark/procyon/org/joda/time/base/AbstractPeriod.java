// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.format.PeriodFormatter;
import org.joda.convert.ToString;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePeriod;

public abstract class AbstractPeriod implements ReadablePeriod
{
    protected AbstractPeriod() {
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
        if (this.size() != readablePeriod.size()) {
            return false;
        }
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.getValue(i) != readablePeriod.getValue(i)) {
                return false;
            }
            if (this.getFieldType(i) != readablePeriod.getFieldType(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int get(final DurationFieldType durationFieldType) {
        final int index = this.indexOf(durationFieldType);
        if (index == -1) {
            return 0;
        }
        return this.getValue(index);
    }
    
    @Override
    public DurationFieldType getFieldType(final int n) {
        return this.getPeriodType().getFieldType(n);
    }
    
    public DurationFieldType[] getFieldTypes() {
        final DurationFieldType[] array = new DurationFieldType[this.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.getFieldType(i);
        }
        return array;
    }
    
    public int[] getValues() {
        final int[] array = new int[this.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.getValue(i);
        }
        return array;
    }
    
    @Override
    public int hashCode() {
        final int size = this.size();
        int n = 17;
        for (int i = 0; i < size; ++i) {
            n = (n * 27 + this.getValue(i)) * 27 + this.getFieldType(i).hashCode();
        }
        return n;
    }
    
    public int indexOf(final DurationFieldType durationFieldType) {
        return this.getPeriodType().indexOf(durationFieldType);
    }
    
    @Override
    public boolean isSupported(final DurationFieldType durationFieldType) {
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
    
    public String toString(final PeriodFormatter periodFormatter) {
        if (periodFormatter == null) {
            return this.toString();
        }
        return periodFormatter.print((ReadablePeriod)this);
    }
}
