// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.data;

public final class CRC64
{
    private static final long[] LOOKUPTABLE;
    private static final long POLY64REV = -2882303761517117440L;
    
    static {
        LOOKUPTABLE = new long[256];
        for (int i = 0; i < 256; ++i) {
            long n = i;
            for (int j = 0; j < 8; ++j) {
                if ((n & 0x1L) == 0x1L) {
                    n = (n >>> 1 ^ 0xD800000000000000L);
                }
                else {
                    n >>>= 1;
                }
            }
            CRC64.LOOKUPTABLE[i] = n;
        }
    }
    
    private CRC64() {
    }
    
    public static long checksum(final byte[] array) {
        long n = 0L;
        for (int length = array.length, i = 0; i < length; ++i) {
            n = (n >>> 8 ^ CRC64.LOOKUPTABLE[((int)n ^ array[i]) & 0xFF]);
        }
        return n;
    }
}
