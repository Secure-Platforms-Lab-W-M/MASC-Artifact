// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadWritablePeriod extends ReadablePeriod
{
    void add(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    void add(final DurationFieldType p0, final int p1);
    
    void add(final ReadableInterval p0);
    
    void add(final ReadablePeriod p0);
    
    void addDays(final int p0);
    
    void addHours(final int p0);
    
    void addMillis(final int p0);
    
    void addMinutes(final int p0);
    
    void addMonths(final int p0);
    
    void addSeconds(final int p0);
    
    void addWeeks(final int p0);
    
    void addYears(final int p0);
    
    void clear();
    
    void set(final DurationFieldType p0, final int p1);
    
    void setDays(final int p0);
    
    void setHours(final int p0);
    
    void setMillis(final int p0);
    
    void setMinutes(final int p0);
    
    void setMonths(final int p0);
    
    void setPeriod(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    void setPeriod(final ReadableInterval p0);
    
    void setPeriod(final ReadablePeriod p0);
    
    void setSeconds(final int p0);
    
    void setValue(final int p0, final int p1);
    
    void setWeeks(final int p0);
    
    void setYears(final int p0);
}
