// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.DurationFieldType;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.ReadablePartial;

public abstract class AbstractPartial implements ReadablePartial, Comparable<ReadablePartial>
{
    protected AbstractPartial() {
    }
    
    @Override
    public int compareTo(final ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (this.size() == readablePartial.size()) {
            for (int size = this.size(), i = 0; i < size; ++i) {
                if (this.getFieldType(i) != readablePartial.getFieldType(i)) {
                    throw new ClassCastException("ReadablePartial objects must have matching field types");
                }
            }
            for (int size2 = this.size(), j = 0; j < size2; ++j) {
                if (this.getValue(j) > readablePartial.getValue(j)) {
                    return 1;
                }
                if (this.getValue(j) < readablePartial.getValue(j)) {
                    return -1;
                }
            }
            return 0;
        }
        throw new ClassCastException("ReadablePartial objects must have matching field types");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReadablePartial)) {
            return false;
        }
        final ReadablePartial readablePartial = (ReadablePartial)o;
        if (this.size() != readablePartial.size()) {
            return false;
        }
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.getValue(i) != readablePartial.getValue(i)) {
                return false;
            }
            if (this.getFieldType(i) != readablePartial.getFieldType(i)) {
                return false;
            }
        }
        return FieldUtils.equals((Object)this.getChronology(), (Object)readablePartial.getChronology());
    }
    
    @Override
    public int get(final DateTimeFieldType dateTimeFieldType) {
        return this.getValue(this.indexOfSupported(dateTimeFieldType));
    }
    
    @Override
    public DateTimeField getField(final int n) {
        return this.getField(n, this.getChronology());
    }
    
    protected abstract DateTimeField getField(final int p0, final Chronology p1);
    
    @Override
    public DateTimeFieldType getFieldType(final int n) {
        return this.getField(n, this.getChronology()).getType();
    }
    
    public DateTimeFieldType[] getFieldTypes() {
        final DateTimeFieldType[] array = new DateTimeFieldType[this.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.getFieldType(i);
        }
        return array;
    }
    
    public DateTimeField[] getFields() {
        final DateTimeField[] array = new DateTimeField[this.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.getField(i);
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
        int n = 157;
        for (int i = 0; i < size; ++i) {
            n = (n * 23 + this.getValue(i)) * 23 + this.getFieldType(i).hashCode();
        }
        return n + this.getChronology().hashCode();
    }
    
    public int indexOf(final DateTimeFieldType dateTimeFieldType) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.getFieldType(i) == dateTimeFieldType) {
                return i;
            }
        }
        return -1;
    }
    
    protected int indexOf(final DurationFieldType durationFieldType) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.getFieldType(i).getDurationType() == durationFieldType) {
                return i;
            }
        }
        return -1;
    }
    
    protected int indexOfSupported(final DateTimeFieldType dateTimeFieldType) {
        final int index = this.indexOf(dateTimeFieldType);
        if (index != -1) {
            return index;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    protected int indexOfSupported(final DurationFieldType durationFieldType) {
        final int index = this.indexOf(durationFieldType);
        if (index != -1) {
            return index;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(durationFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public boolean isAfter(final ReadablePartial readablePartial) {
        if (readablePartial != null) {
            return this.compareTo(readablePartial) > 0;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }
    
    public boolean isBefore(final ReadablePartial readablePartial) {
        if (readablePartial != null) {
            return this.compareTo(readablePartial) < 0;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }
    
    public boolean isEqual(final ReadablePartial readablePartial) {
        if (readablePartial != null) {
            return this.compareTo(readablePartial) == 0;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }
    
    @Override
    public boolean isSupported(final DateTimeFieldType dateTimeFieldType) {
        return this.indexOf(dateTimeFieldType) != -1;
    }
    
    @Override
    public DateTime toDateTime(final ReadableInstant readableInstant) {
        final Chronology instantChronology = DateTimeUtils.getInstantChronology(readableInstant);
        return new DateTime(instantChronology.set((ReadablePartial)this, DateTimeUtils.getInstantMillis(readableInstant)), instantChronology);
    }
    
    public String toString(final DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            return this.toString();
        }
        return dateTimeFormatter.print((ReadablePartial)this);
    }
}
