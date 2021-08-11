// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

public enum NetworkPolicy
{
    NO_CACHE(1), 
    NO_STORE(2), 
    OFFLINE(4);
    
    final int index;
    
    private NetworkPolicy(final int index) {
        this.index = index;
    }
    
    public static boolean isOfflineOnly(final int n) {
        return (NetworkPolicy.OFFLINE.index & n) != 0x0;
    }
    
    public static boolean shouldReadFromDiskCache(final int n) {
        return (NetworkPolicy.NO_CACHE.index & n) == 0x0;
    }
    
    public static boolean shouldWriteToDiskCache(final int n) {
        return (NetworkPolicy.NO_STORE.index & n) == 0x0;
    }
}
