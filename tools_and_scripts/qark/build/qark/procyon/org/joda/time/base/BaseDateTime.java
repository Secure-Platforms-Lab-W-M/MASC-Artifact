// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.time.convert.InstantConverter;
import org.joda.time.convert.ConverterManager;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Chronology;
import java.io.Serializable;
import org.joda.time.ReadableDateTime;

public abstract class BaseDateTime extends AbstractDateTime implements ReadableDateTime, Serializable
{
    private static final long serialVersionUID = -6728882245981L;
    private volatile Chronology iChronology;
    private volatile long iMillis;
    
    public BaseDateTime() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    public BaseDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstance());
    }
    
    public BaseDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
        this.iMillis = this.checkInstant(this.iChronology.getDateTimeMillis(n, n2, n3, n4, n5, n6, n7), this.iChronology);
        this.adjustForMinMax();
    }
    
    public BaseDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final DateTimeZone dateTimeZone) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public BaseDateTime(final long n) {
        this(n, (Chronology)ISOChronology.getInstance());
    }
    
    public BaseDateTime(final long n, final Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
        this.iMillis = this.checkInstant(n, this.iChronology);
        this.adjustForMinMax();
    }
    
    public BaseDateTime(final long n, final DateTimeZone dateTimeZone) {
        this(n, (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public BaseDateTime(final Object o, final Chronology chronology) {
        final InstantConverter instantConverter = ConverterManager.getInstance().getInstantConverter(o);
        this.iChronology = this.checkChronology(instantConverter.getChronology(o, chronology));
        this.iMillis = this.checkInstant(instantConverter.getInstantMillis(o, chronology), this.iChronology);
        this.adjustForMinMax();
    }
    
    public BaseDateTime(final Object o, final DateTimeZone dateTimeZone) {
        final InstantConverter instantConverter = ConverterManager.getInstance().getInstantConverter(o);
        final Chronology checkChronology = this.checkChronology(instantConverter.getChronology(o, dateTimeZone));
        this.iChronology = checkChronology;
        this.iMillis = this.checkInstant(instantConverter.getInstantMillis(o, checkChronology), checkChronology);
        this.adjustForMinMax();
    }
    
    public BaseDateTime(final Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }
    
    public BaseDateTime(final DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    private void adjustForMinMax() {
        if (this.iMillis != Long.MIN_VALUE && this.iMillis != Long.MAX_VALUE) {
            return;
        }
        this.iChronology = this.iChronology.withUTC();
    }
    
    protected Chronology checkChronology(final Chronology chronology) {
        return DateTimeUtils.getChronology(chronology);
    }
    
    protected long checkInstant(final long n, final Chronology chronology) {
        return n;
    }
    
    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }
    
    @Override
    public long getMillis() {
        return this.iMillis;
    }
    
    protected void setChronology(final Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
    }
    
    protected void setMillis(final long n) {
        this.iMillis = this.checkInstant(n, this.iChronology);
    }
}
