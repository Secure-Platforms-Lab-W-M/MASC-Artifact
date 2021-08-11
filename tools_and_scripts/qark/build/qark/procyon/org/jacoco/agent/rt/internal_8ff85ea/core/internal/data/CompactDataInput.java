// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;

public class CompactDataInput extends DataInputStream
{
    public CompactDataInput(final InputStream inputStream) {
        super(inputStream);
    }
    
    public boolean[] readBooleanArray() throws IOException {
        final boolean[] array = new boolean[this.readVarInt()];
        int byte1 = 0;
        for (int i = 0; i < array.length; ++i) {
            if (i % 8 == 0) {
                byte1 = this.readByte();
            }
            array[i] = ((byte1 & 0x1) != 0x0);
            byte1 >>>= 1;
        }
        return array;
    }
    
    public int readVarInt() throws IOException {
        final int n = this.readByte() & 0xFF;
        if ((n & 0x80) == 0x0) {
            return n;
        }
        return (n & 0x7F) | this.readVarInt() << 7;
    }
}
