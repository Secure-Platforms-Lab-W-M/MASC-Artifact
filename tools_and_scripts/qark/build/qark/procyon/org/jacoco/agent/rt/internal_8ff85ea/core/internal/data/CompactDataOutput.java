// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.data;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;

public class CompactDataOutput extends DataOutputStream
{
    public CompactDataOutput(final OutputStream outputStream) {
        super(outputStream);
    }
    
    public void writeBooleanArray(final boolean[] array) throws IOException {
        this.writeVarInt(array.length);
        int n = 0;
        int n2 = 0;
        for (int length = array.length, i = 0; i < length; ++i) {
            int n3 = n;
            if (array[i]) {
                n3 = (n | 1 << n2);
            }
            final int n4 = n2 + 1;
            n = n3;
            if ((n2 = n4) == 8) {
                this.writeByte(n3);
                n = 0;
                n2 = 0;
            }
        }
        if (n2 > 0) {
            this.writeByte(n);
        }
    }
    
    public void writeVarInt(final int n) throws IOException {
        if ((n & 0xFFFFFF80) == 0x0) {
            this.writeByte(n);
            return;
        }
        this.writeByte((n & 0x7F) | 0x80);
        this.writeVarInt(n >>> 7);
    }
}
