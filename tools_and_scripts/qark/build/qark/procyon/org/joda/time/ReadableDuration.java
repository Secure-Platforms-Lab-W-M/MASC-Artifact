// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

public interface ReadableDuration extends Comparable<ReadableDuration>
{
    boolean equals(final Object p0);
    
    long getMillis();
    
    int hashCode();
    
    boolean isEqual(final ReadableDuration p0);
    
    boolean isLongerThan(final ReadableDuration p0);
    
    boolean isShorterThan(final ReadableDuration p0);
    
    Duration toDuration();
    
    Period toPeriod();
    
    String toString();
}
