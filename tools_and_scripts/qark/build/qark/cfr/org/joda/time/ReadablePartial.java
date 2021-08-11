/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 */
package org.joda.time;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadableInstant;

public interface ReadablePartial
extends Comparable<ReadablePartial> {
    public boolean equals(Object var1);

    public int get(DateTimeFieldType var1);

    public Chronology getChronology();

    public DateTimeField getField(int var1);

    public DateTimeFieldType getFieldType(int var1);

    public int getValue(int var1);

    public int hashCode();

    public boolean isSupported(DateTimeFieldType var1);

    public int size();

    public DateTime toDateTime(ReadableInstant var1);

    public String toString();
}

