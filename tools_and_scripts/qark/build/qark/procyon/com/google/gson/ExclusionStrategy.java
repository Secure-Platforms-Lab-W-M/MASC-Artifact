// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

public interface ExclusionStrategy
{
    boolean shouldSkipClass(final Class<?> p0);
    
    boolean shouldSkipField(final FieldAttributes p0);
}
