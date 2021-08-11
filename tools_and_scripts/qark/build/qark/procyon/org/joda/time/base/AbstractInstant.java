// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.ToString;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.MutableDateTime;
import org.joda.time.Instant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import java.util.Date;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.ReadableInstant;

public abstract class AbstractInstant implements ReadableInstant
{
    protected AbstractInstant() {
    }
    
    @Override
    public int compareTo(final ReadableInstant readableInstant) {
        if (this == readableInstant) {
            return 0;
        }
        final long millis = readableInstant.getMillis();
        final long millis2 = this.getMillis();
        if (millis2 == millis) {
            return 0;
        }
        if (millis2 < millis) {
            return -1;
        }
        return 1;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReadableInstant)) {
            return false;
        }
        final ReadableInstant readableInstant = (ReadableInstant)o;
        return this.getMillis() == readableInstant.getMillis() && FieldUtils.equals((Object)this.getChronology(), (Object)readableInstant.getChronology());
    }
    
    public int get(final DateTimeField dateTimeField) {
        if (dateTimeField != null) {
            return dateTimeField.get(this.getMillis());
        }
        throw new IllegalArgumentException("The DateTimeField must not be null");
    }
    
    @Override
    public int get(final DateTimeFieldType dateTimeFieldType) {
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
    
    public boolean isAfter(final long n) {
        return this.getMillis() > n;
    }
    
    @Override
    public boolean isAfter(final ReadableInstant readableInstant) {
        return this.isAfter(DateTimeUtils.getInstantMillis(readableInstant));
    }
    
    public boolean isAfterNow() {
        return this.isAfter(DateTimeUtils.currentTimeMillis());
    }
    
    public boolean isBefore(final long n) {
        return this.getMillis() < n;
    }
    
    @Override
    public boolean isBefore(final ReadableInstant readableInstant) {
        return this.isBefore(DateTimeUtils.getInstantMillis(readableInstant));
    }
    
    public boolean isBeforeNow() {
        return this.isBefore(DateTimeUtils.currentTimeMillis());
    }
    
    public boolean isEqual(final long n) {
        return this.getMillis() == n;
    }
    
    @Override
    public boolean isEqual(final ReadableInstant readableInstant) {
        return this.isEqual(DateTimeUtils.getInstantMillis(readableInstant));
    }
    
    public boolean isEqualNow() {
        return this.isEqual(DateTimeUtils.currentTimeMillis());
    }
    
    @Override
    public boolean isSupported(final DateTimeFieldType dateTimeFieldType) {
        return dateTimeFieldType != null && dateTimeFieldType.getField(this.getChronology()).isSupported();
    }
    
    public Date toDate() {
        return new Date(this.getMillis());
    }
    
    public DateTime toDateTime() {
        return new DateTime(this.getMillis(), this.getZone());
    }
    
    public DateTime toDateTime(final Chronology chronology) {
        return new DateTime(this.getMillis(), chronology);
    }
    
    public DateTime toDateTime(final DateTimeZone dateTimeZone) {
        return new DateTime(this.getMillis(), DateTimeUtils.getChronology(this.getChronology()).withZone(dateTimeZone));
    }
    
    public DateTime toDateTimeISO() {
        return new DateTime(this.getMillis(), (Chronology)ISOChronology.getInstance(this.getZone()));
    }
    
    @Override
    public Instant toInstant() {
        return new Instant(this.getMillis());
    }
    
    public MutableDateTime toMutableDateTime() {
        return new MutableDateTime(this.getMillis(), this.getZone());
    }
    
    public MutableDateTime toMutableDateTime(final Chronology chronology) {
        return new MutableDateTime(this.getMillis(), chronology);
    }
    
    public MutableDateTime toMutableDateTime(final DateTimeZone dateTimeZone) {
        return new MutableDateTime(this.getMillis(), DateTimeUtils.getChronology(this.getChronology()).withZone(dateTimeZone));
    }
    
    public MutableDateTime toMutableDateTimeISO() {
        return new MutableDateTime(this.getMillis(), (Chronology)ISOChronology.getInstance(this.getZone()));
    }
    
    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.dateTime().print((ReadableInstant)this);
    }
    
    public String toString(final DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            return this.toString();
        }
        return dateTimeFormatter.print((ReadableInstant)this);
    }
}
