// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadablePeriod
{
    boolean equals(final Object p0);
    
    int get(final DurationFieldType p0);
    
    DurationFieldType getFieldType(final int p0);
    
    PeriodType getPeriodType();
    
    int getValue(final int p0);
    
    int hashCode();
    
    boolean isSupported(final DurationFieldType p0);
    
    int size();
    
    MutablePeriod toMutablePeriod();
    
    Period toPeriod();
    
    String toString();
}
