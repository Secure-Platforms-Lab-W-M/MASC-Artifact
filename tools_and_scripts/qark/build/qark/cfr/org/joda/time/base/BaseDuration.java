/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.Interval
 *  org.joda.time.PeriodType
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.DurationConverter
 *  org.joda.time.field.FieldUtils
 */
package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.base.AbstractDuration;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;
import org.joda.time.field.FieldUtils;

public abstract class BaseDuration
extends AbstractDuration
implements ReadableDuration,
Serializable {
    private static final long serialVersionUID = 2581698638990L;
    private volatile long iMillis;

    protected BaseDuration(long l) {
        this.iMillis = l;
    }

    protected BaseDuration(long l, long l2) {
        this.iMillis = FieldUtils.safeSubtract((long)l2, (long)l);
    }

    protected BaseDuration(Object object) {
        this.iMillis = ConverterManager.getInstance().getDurationConverter(object).getDurationMillis(object);
    }

    protected BaseDuration(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        if (readableInstant == readableInstant2) {
            this.iMillis = 0L;
            return;
        }
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        this.iMillis = FieldUtils.safeSubtract((long)DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant2), (long)l);
    }

    @Override
    public long getMillis() {
        return this.iMillis;
    }

    protected void setMillis(long l) {
        this.iMillis = l;
    }

    public Interval toIntervalFrom(ReadableInstant readableInstant) {
        return new Interval(readableInstant, (ReadableDuration)this);
    }

    public Interval toIntervalTo(ReadableInstant readableInstant) {
        return new Interval((ReadableDuration)this, readableInstant);
    }

    public Period toPeriod(Chronology chronology) {
        return new Period(this.getMillis(), chronology);
    }

    public Period toPeriod(PeriodType periodType) {
        return new Period(this.getMillis(), periodType);
    }

    public Period toPeriod(PeriodType periodType, Chronology chronology) {
        return new Period(this.getMillis(), periodType, chronology);
    }

    public Period toPeriodFrom(ReadableInstant readableInstant) {
        return new Period(readableInstant, this);
    }

    public Period toPeriodFrom(ReadableInstant readableInstant, PeriodType periodType) {
        return new Period(readableInstant, this, periodType);
    }

    public Period toPeriodTo(ReadableInstant readableInstant) {
        return new Period(this, readableInstant);
    }

    public Period toPeriodTo(ReadableInstant readableInstant, PeriodType periodType) {
        return new Period(this, readableInstant, periodType);
    }
}

