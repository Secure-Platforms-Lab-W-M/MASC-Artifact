/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.InstantConverter
 */
package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDateTime;
import org.joda.time.base.AbstractDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

public abstract class BaseDateTime
extends AbstractDateTime
implements ReadableDateTime,
Serializable {
    private static final long serialVersionUID = -6728882245981L;
    private volatile Chronology iChronology;
    private volatile long iMillis;

    public BaseDateTime() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }

    public BaseDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstance());
    }

    public BaseDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
        this.iMillis = this.checkInstant(this.iChronology.getDateTimeMillis(n, n2, n3, n4, n5, n6, n7), this.iChronology);
        this.adjustForMinMax();
    }

    public BaseDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, DateTimeZone dateTimeZone) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public BaseDateTime(long l) {
        this(l, (Chronology)ISOChronology.getInstance());
    }

    public BaseDateTime(long l, Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
        this.iMillis = this.checkInstant(l, this.iChronology);
        this.adjustForMinMax();
    }

    public BaseDateTime(long l, DateTimeZone dateTimeZone) {
        this(l, (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public BaseDateTime(Object object, Chronology chronology) {
        InstantConverter instantConverter = ConverterManager.getInstance().getInstantConverter(object);
        this.iChronology = this.checkChronology(instantConverter.getChronology(object, chronology));
        this.iMillis = this.checkInstant(instantConverter.getInstantMillis(object, chronology), this.iChronology);
        this.adjustForMinMax();
    }

    public BaseDateTime(Object object, DateTimeZone dateTimeZone) {
        InstantConverter instantConverter = ConverterManager.getInstance().getInstantConverter(object);
        dateTimeZone = this.checkChronology(instantConverter.getChronology(object, dateTimeZone));
        this.iChronology = dateTimeZone;
        this.iMillis = this.checkInstant(instantConverter.getInstantMillis(object, (Chronology)dateTimeZone), (Chronology)dateTimeZone);
        this.adjustForMinMax();
    }

    public BaseDateTime(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    public BaseDateTime(DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    private void adjustForMinMax() {
        if (this.iMillis != Long.MIN_VALUE && this.iMillis != Long.MAX_VALUE) {
            return;
        }
        this.iChronology = this.iChronology.withUTC();
    }

    protected Chronology checkChronology(Chronology chronology) {
        return DateTimeUtils.getChronology((Chronology)chronology);
    }

    protected long checkInstant(long l, Chronology chronology) {
        return l;
    }

    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }

    @Override
    public long getMillis() {
        return this.iMillis;
    }

    protected void setChronology(Chronology chronology) {
        this.iChronology = this.checkChronology(chronology);
    }

    protected void setMillis(long l) {
        this.iMillis = this.checkInstant(l, this.iChronology);
    }
}

