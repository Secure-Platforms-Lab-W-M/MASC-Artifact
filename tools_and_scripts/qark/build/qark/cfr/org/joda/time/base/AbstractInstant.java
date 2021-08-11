/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.ToString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time.base;

import java.util.Date;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public abstract class AbstractInstant
implements ReadableInstant {
    protected AbstractInstant() {
    }

    @Override
    public int compareTo(ReadableInstant readableInstant) {
        if (this == readableInstant) {
            return 0;
        }
        long l = readableInstant.getMillis();
        long l2 = this.getMillis();
        if (l2 == l) {
            return 0;
        }
        if (l2 < l) {
            return -1;
        }
        return 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ReadableInstant)) {
            return false;
        }
        object = (ReadableInstant)object;
        if (this.getMillis() == object.getMillis() && FieldUtils.equals((Object)this.getChronology(), (Object)object.getChronology())) {
            return true;
        }
        return false;
    }

    public int get(DateTimeField dateTimeField) {
        if (dateTimeField != null) {
            return dateTimeField.get(this.getMillis());
        }
        throw new IllegalArgumentException("The DateTimeField must not be null");
    }

    @Override
    public int get(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            return dateTimeFieldType.getField(this.getChronology()).get(this.getMillis());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    @Override
    public DateTimeZone getZone() {
        return this.getChronology().getZone();
    }

    @Override
    public int hashCode() {
        return (int)(this.getMillis() ^ this.getMillis() >>> 32) + this.getChronology().hashCode();
    }

    public boolean isAfter(long l) {
        if (this.getMillis() > l) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAfter(ReadableInstant readableInstant) {
        return this.isAfter(DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant));
    }

    public boolean isAfterNow() {
        return this.isAfter(DateTimeUtils.currentTimeMillis());
    }

    public boolean isBefore(long l) {
        if (this.getMillis() < l) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBefore(ReadableInstant readableInstant) {
        return this.isBefore(DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant));
    }

    public boolean isBeforeNow() {
        return this.isBefore(DateTimeUtils.currentTimeMillis());
    }

    public boolean isEqual(long l) {
        if (this.getMillis() == l) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEqual(ReadableInstant readableInstant) {
        return this.isEqual(DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant));
    }

    public boolean isEqualNow() {
        return this.isEqual(DateTimeUtils.currentTimeMillis());
    }

    @Override
    public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        return dateTimeFieldType.getField(this.getChronology()).isSupported();
    }

    public Date toDate() {
        return new Date(this.getMillis());
    }

    public DateTime toDateTime() {
        return new DateTime(this.getMillis(), this.getZone());
    }

    public DateTime toDateTime(Chronology chronology) {
        return new DateTime(this.getMillis(), chronology);
    }

    public DateTime toDateTime(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getChronology((Chronology)this.getChronology()).withZone(dateTimeZone);
        return new DateTime(this.getMillis(), (Chronology)dateTimeZone);
    }

    public DateTime toDateTimeISO() {
        return new DateTime(this.getMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)this.getZone()));
    }

    @Override
    public Instant toInstant() {
        return new Instant(this.getMillis());
    }

    public MutableDateTime toMutableDateTime() {
        return new MutableDateTime(this.getMillis(), this.getZone());
    }

    public MutableDateTime toMutableDateTime(Chronology chronology) {
        return new MutableDateTime(this.getMillis(), chronology);
    }

    public MutableDateTime toMutableDateTime(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getChronology((Chronology)this.getChronology()).withZone(dateTimeZone);
        return new MutableDateTime(this.getMillis(), (Chronology)dateTimeZone);
    }

    public MutableDateTime toMutableDateTimeISO() {
        return new MutableDateTime(this.getMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)this.getZone()));
    }

    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.dateTime().print((ReadableInstant)this);
    }

    public String toString(DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            return this.toString();
        }
        return dateTimeFormatter.print((ReadableInstant)this);
    }
}

