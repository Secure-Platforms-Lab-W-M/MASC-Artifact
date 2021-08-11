// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadableInstant extends Comparable<ReadableInstant>
{
    boolean equals(final Object p0);
    
    int get(final DateTimeFieldType p0);
    
    Chronology getChronology();
    
    long getMillis();
    
    DateTimeZone getZone();
    
    int hashCode();
    
    boolean isAfter(final ReadableInstant p0);
    
    boolean isBefore(final ReadableInstant p0);
    
    boolean isEqual(final ReadableInstant p0);
    
    boolean isSupported(final DateTimeFieldType p0);
    
    Instant toInstant();
    
    String toString();
}
