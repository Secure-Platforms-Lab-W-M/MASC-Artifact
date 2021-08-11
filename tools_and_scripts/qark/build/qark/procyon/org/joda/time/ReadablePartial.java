// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadablePartial extends Comparable<ReadablePartial>
{
    boolean equals(final Object p0);
    
    int get(final DateTimeFieldType p0);
    
    Chronology getChronology();
    
    DateTimeField getField(final int p0);
    
    DateTimeFieldType getFieldType(final int p0);
    
    int getValue(final int p0);
    
    int hashCode();
    
    boolean isSupported(final DateTimeFieldType p0);
    
    int size();
    
    DateTime toDateTime(final ReadableInstant p0);
    
    String toString();
}
