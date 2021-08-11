// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

public enum MemoryPolicy
{
    NO_CACHE(1), 
    NO_STORE(2);
    
    final int index;
    
    private MemoryPolicy(final int index) {
        this.index = index;
    }
    
    static boolean shouldReadFromMemoryCache(final int n) {
        return (MemoryPolicy.NO_CACHE.index & n) == 0x0;
    }
    
    static boolean shouldWriteToMemoryCache(final int n) {
        return (MemoryPolicy.NO_STORE.index & n) == 0x0;
    }
}
