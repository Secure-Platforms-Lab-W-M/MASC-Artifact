/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.InstantConverter
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.base.AbstractInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class Instant
extends AbstractInstant
implements ReadableInstant,
Serializable {
    private static final long serialVersionUID = 3299096530934209741L;
    private final long iMillis;

    public Instant() {
        this.iMillis = DateTimeUtils.currentTimeMillis();
    }

    public Instant(long l) {
        this.iMillis = l;
    }

    public Instant(Object object) {
        this.iMillis = ConverterManager.getInstance().getInstantConverter(object).getInstantMillis(object, (Chronology)ISOChronology.getInstanceUTC());
    }

    public static Instant now() {
        return new Instant();
    }

    @FromString
    public static Instant parse(String string) {
        return Instant.parse(string, ISODateTimeFormat.dateTimeParser());
    }

    public static Instant parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(string).toInstant();
    }

    @Override
    public Chronology getChronology() {
        return ISOChronology.getInstanceUTC();
    }

    @Override
    public long getMillis() {
        return this.iMillis;
    }

    public Instant minus(long l) {
        return this.withDurationAdded(l, -1);
    }

    public Instant minus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }

    public Instant plus(long l) {
        return this.withDurationAdded(l, 1);
    }

    public Instant plus(ReadableDuration readableDuration) {
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

    public Instant withDurationAdded(long l, int n) {
        if (l != 0L) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(this.getChronology().add(this.getMillis(), l, n));
        }
        return this;
    }

    public Instant withDurationAdded(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            if (n == 0) {
                return this;
            }
            return this.withDurationAdded(readableDuration.getMillis(), n);
        }
        return this;
    }

    public Instant withMillis(long l) {
        if (l == this.iMillis) {
            return this;
        }
        return new Instant(l);
    }
}

