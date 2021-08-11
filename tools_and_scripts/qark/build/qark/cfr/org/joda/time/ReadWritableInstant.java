/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DurationFieldType
 */
package org.joda.time;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;

public interface ReadWritableInstant
extends ReadableInstant {
    public void add(long var1);

    public void add(DurationFieldType var1, int var2);

    public void add(ReadableDuration var1);

    public void add(ReadableDuration var1, int var2);

    public void add(ReadablePeriod var1);

    public void add(ReadablePeriod var1, int var2);

    public void set(DateTimeFieldType var1, int var2);

    public void setChronology(Chronology var1);

    public void setMillis(long var1);

    public void setMillis(ReadableInstant var1);

    public void setZone(DateTimeZone var1);

    public void setZoneRetainFields(DateTimeZone var1);
}

