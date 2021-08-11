// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import java.io.Serializable;
import org.joda.time.base.AbstractInstant;

public final class Instant extends AbstractInstant implements ReadableInstant, Serializable
{
    private static final long serialVersionUID = 3299096530934209741L;
    private final long iMillis;
    
    public Instant() {
        this.iMillis = DateTimeUtils.currentTimeMillis();
    }
    
    public Instant(final long iMillis) {
        this.iMillis = iMillis;
    }
    
    public Instant(final Object o) {
        this.iMillis = ConverterManager.getInstance().getInstantConverter(o).getInstantMillis(o, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public static Instant now() {
        return new Instant();
    }
    
    @FromString
    public static Instant parse(final String s) {
        return parse(s, ISODateTimeFormat.dateTimeParser());
    }
    
    public static Instant parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(s).toInstant();
    }
    
    @Override
    public Chronology getChronology() {
        return (Chronology)ISOChronology.getInstanceUTC();
    }
    
    @Override
    public long getMillis() {
        return this.iMillis;
    }
    
    public Instant minus(final long n) {
        return this.withDurationAdded(n, -1);
    }
    
    public Instant minus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }
    
    public Instant plus(final long n) {
        return this.withDurationAdded(n, 1);
    }
    
    public Instant plus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }
    
    @Override
    public DateTime toDateTime() {
        return new DateTime(this.getMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    @Deprecated
    @Override
    public DateTime toDateTimeISO() {
        return this.toDateTime();
    }
    
    @Override
    public Instant toInstant() {
        return this;
    }
    
    @Override
    public MutableDateTime toMutableDateTime() {
        return new MutableDateTime(this.getMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    @Deprecated
    @Override
    public MutableDateTime toMutableDateTimeISO() {
        return this.toMutableDateTime();
    }
    
    public Instant withDurationAdded(final long n, final int n2) {
        if (n == 0L) {
            return this;
        }
        if (n2 == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().add(this.getMillis(), n, n2));
    }
    
    public Instant withDurationAdded(final ReadableDuration readableDuration, final int n) {
        if (readableDuration == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), n);
    }
    
    public Instant withMillis(final long n) {
        if (n == this.iMillis) {
            return this;
        }
        return new Instant(n);
    }
}
