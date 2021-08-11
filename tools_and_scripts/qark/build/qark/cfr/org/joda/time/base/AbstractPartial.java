/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationFieldType
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormatter
 */
package org.joda.time.base;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractPartial
implements ReadablePartial,
Comparable<ReadablePartial> {
    protected AbstractPartial() {
    }

    @Override
    public int compareTo(ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (this.size() == readablePartial.size()) {
            int n;
            int n2 = this.size();
            for (n = 0; n < n2; ++n) {
                if (this.getFieldType(n) == readablePartial.getFieldType(n)) {
                    continue;
                }
                throw new ClassCastException("ReadablePartial objects must have matching field types");
            }
            n2 = this.size();
            for (n = 0; n < n2; ++n) {
                if (this.getValue(n) > readablePartial.getValue(n)) {
                    return 1;
                }
                if (this.getValue(n) >= readablePartial.getValue(n)) continue;
                return -1;
            }
            return 0;
        }
        throw new ClassCastException("ReadablePartial objects must have matching field types");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ReadablePartial)) {
            return false;
        }
        object = (ReadablePartial)object;
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
        return FieldUtils.equals((Object)this.getChronology(), (Object)object.getChronology());
    }

    @Override
    public int get(DateTimeFieldType dateTimeFieldType) {
        return this.getValue(this.indexOfSupported(dateTimeFieldType));
    }

    @Override
    public DateTimeField getField(int n) {
        return this.getField(n, this.getChronology());
    }

    protected abstract DateTimeField getField(int var1, Chronology var2);

    @Override
    public DateTimeFieldType getFieldType(int n) {
        return this.getField(n, this.getChronology()).getType();
    }

    public DateTimeFieldType[] getFieldTypes() {
        DateTimeFieldType[] arrdateTimeFieldType = new DateTimeFieldType[this.size()];
        for (int i = 0; i < arrdateTimeFieldType.length; ++i) {
            arrdateTimeFieldType[i] = this.getFieldType(i);
        }
        return arrdateTimeFieldType;
    }

    public DateTimeField[] getFields() {
        DateTimeField[] arrdateTimeField = new DateTimeField[this.size()];
        for (int i = 0; i < arrdateTimeField.length; ++i) {
            arrdateTimeField[i] = this.getField(i);
        }
        return arrdateTimeField;
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
        int n2 = 157;
        for (int i = 0; i < n; ++i) {
            n2 = (n2 * 23 + this.getValue(i)) * 23 + this.getFieldType(i).hashCode();
        }
        return n2 + this.getChronology().hashCode();
    }

    public int indexOf(DateTimeFieldType dateTimeFieldType) {
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            if (this.getFieldType(i) != dateTimeFieldType) continue;
            return i;
        }
        return -1;
    }

    protected int indexOf(DurationFieldType durationFieldType) {
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            if (this.getFieldType(i).getDurationType() != durationFieldType) continue;
            return i;
        }
        return -1;
    }

    protected int indexOfSupported(DateTimeFieldType dateTimeFieldType) {
        int n = this.indexOf(dateTimeFieldType);
        if (n != -1) {
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Field '");
        stringBuilder.append((Object)dateTimeFieldType);
        stringBuilder.append("' is not supported");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    protected int indexOfSupported(DurationFieldType durationFieldType) {
        int n = this.indexOf(durationFieldType);
        if (n != -1) {
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Field '");
        stringBuilder.append((Object)durationFieldType);
        stringBuilder.append("' is not supported");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public boolean isAfter(ReadablePartial readablePartial) {
        if (readablePartial != null) {
            if (this.compareTo(readablePartial) > 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }

    public boolean isBefore(ReadablePartial readablePartial) {
        if (readablePartial != null) {
            if (this.compareTo(readablePartial) < 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }

    public boolean isEqual(ReadablePartial readablePartial) {
        if (readablePartial != null) {
            if (this.compareTo(readablePartial) == 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Partial cannot be null");
    }

    @Override
    public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
        if (this.indexOf(dateTimeFieldType) != -1) {
            return true;
        }
        return false;
    }

    @Override
    public DateTime toDateTime(ReadableInstant readableInstant) {
        Chronology chronology = DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant);
        return new DateTime(chronology.set((ReadablePartial)this, DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant)), chronology);
    }

    public String toString(DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            return this.toString();
        }
        return dateTimeFormatter.print((ReadablePartial)this);
    }
}

