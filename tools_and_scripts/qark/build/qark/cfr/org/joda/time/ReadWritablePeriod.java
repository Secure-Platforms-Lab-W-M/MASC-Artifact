/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.DurationFieldType
 *  org.joda.time.ReadableInterval
 */
package org.joda.time;

import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;

public interface ReadWritablePeriod
extends ReadablePeriod {
    public void add(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    public void add(DurationFieldType var1, int var2);

    public void add(ReadableInterval var1);

    public void add(ReadablePeriod var1);

    public void addDays(int var1);

    public void addHours(int var1);

    public void addMillis(int var1);

    public void addMinutes(int var1);

    public void addMonths(int var1);

    public void addSeconds(int var1);

    public void addWeeks(int var1);

    public void addYears(int var1);

    public void clear();

    public void set(DurationFieldType var1, int var2);

    public void setDays(int var1);

    public void setHours(int var1);

    public void setMillis(int var1);

    public void setMinutes(int var1);

    public void setMonths(int var1);

    public void setPeriod(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    public void setPeriod(ReadableInterval var1);

    public void setPeriod(ReadablePeriod var1);

    public void setSeconds(int var1);

    public void setValue(int var1, int var2);

    public void setWeeks(int var1);

    public void setYears(int var1);
}

