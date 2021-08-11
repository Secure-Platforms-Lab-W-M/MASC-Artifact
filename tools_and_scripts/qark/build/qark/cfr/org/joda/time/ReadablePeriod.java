/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 */
package org.joda.time;

import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public interface ReadablePeriod {
    public boolean equals(Object var1);

    public int get(DurationFieldType var1);

    public DurationFieldType getFieldType(int var1);

    public PeriodType getPeriodType();

    public int getValue(int var1);

    public int hashCode();

    public boolean isSupported(DurationFieldType var1);

    public int size();

    public MutablePeriod toMutablePeriod();

    public Period toPeriod();

    public String toString();
}

