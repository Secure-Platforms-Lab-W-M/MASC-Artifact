/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeFieldType
 */
package org.joda.time;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

public interface ReadableInstant
extends Comparable<ReadableInstant> {
    public boolean equals(Object var1);

    public int get(DateTimeFieldType var1);

    public Chronology getChronology();

    public long getMillis();

    public DateTimeZone getZone();

    public int hashCode();

    public boolean isAfter(ReadableInstant var1);

    public boolean isBefore(ReadableInstant var1);

    public boolean isEqual(ReadableInstant var1);

    public boolean isSupported(DateTimeFieldType var1);

    public Instant toInstant();

    public String toString();
}

