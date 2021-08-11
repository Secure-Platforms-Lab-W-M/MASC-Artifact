// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

public interface ObjectPackagingManager
{
    Object bytesToObject(final byte[] p0, final int p1);
    
    int objectSize();
    
    void objectToBytes(final Object p0, final byte[] p1, final int p2);
}
