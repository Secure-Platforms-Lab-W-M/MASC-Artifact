// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class ByteVector
{
    byte[] data;
    int length;
    
    public ByteVector() {
        this.data = new byte[64];
    }
    
    public ByteVector(final int n) {
        this.data = new byte[n];
    }
    
    private void enlarge(int n) {
        final int n2 = this.data.length * 2;
        n += this.length;
        if (n2 > n) {
            n = n2;
        }
        final byte[] data = new byte[n];
        System.arraycopy(this.data, 0, data, 0, this.length);
        this.data = data;
    }
    
    ByteVector encodeUTF8(final String s, int length, int i) {
        int length2;
        int j;
        int n;
        int n2;
        for (length2 = s.length(), n = (j = length); j < length2; ++j, n = n2) {
            final char char1 = s.charAt(j);
            if (char1 >= '\u0001' && char1 <= '\u007f') {
                n2 = n + 1;
            }
            else if (char1 > '\u07ff') {
                n2 = n + 3;
            }
            else {
                n2 = n + 2;
            }
        }
        if (n <= i) {
            i = this.length - length - 2;
            if (i >= 0) {
                this.data[i] = (byte)(n >>> 8);
                this.data[i + 1] = (byte)n;
            }
            if (this.length + n - length > this.data.length) {
                this.enlarge(n - length);
            }
            final int length3 = this.length;
            i = length;
            length = length3;
            while (i < length2) {
                final char char2 = s.charAt(i);
                if (char2 >= '\u0001' && char2 <= '\u007f') {
                    final byte[] data = this.data;
                    final int n3 = length + 1;
                    data[length] = (byte)char2;
                    length = n3;
                }
                else if (char2 > '\u07ff') {
                    final byte[] data2 = this.data;
                    final int n4 = length + 1;
                    data2[length] = (byte)((char2 >> 12 & 0xF) | 0xE0);
                    final byte[] data3 = this.data;
                    final int n5 = n4 + 1;
                    data3[n4] = (byte)((char2 >> 6 & 0x3F) | 0x80);
                    final byte[] data4 = this.data;
                    length = n5 + 1;
                    data4[n5] = (byte)((char2 & '?') | 0x80);
                }
                else {
                    final byte[] data5 = this.data;
                    final int n6 = length + 1;
                    data5[length] = (byte)((char2 >> 6 & 0x1F) | 0xC0);
                    this.data[n6] = (byte)((char2 & '?') | 0x80);
                    length = n6 + 1;
                }
                ++i;
            }
            this.length = length;
            return this;
        }
        throw new IllegalArgumentException();
    }
    
    ByteVector put11(final int n, final int n2) {
        final int length = this.length;
        if (length + 2 > this.data.length) {
            this.enlarge(2);
        }
        final byte[] data = this.data;
        final int n3 = length + 1;
        data[length] = (byte)n;
        data[n3] = (byte)n2;
        this.length = n3 + 1;
        return this;
    }
    
    ByteVector put12(int n, final int n2) {
        final int length = this.length;
        if (length + 3 > this.data.length) {
            this.enlarge(3);
        }
        final byte[] data = this.data;
        final int n3 = length + 1;
        data[length] = (byte)n;
        n = n3 + 1;
        data[n3] = (byte)(n2 >>> 8);
        data[n] = (byte)n2;
        this.length = n + 1;
        return this;
    }
    
    public ByteVector putByte(final int n) {
        final int length = this.length;
        if (length + 1 > this.data.length) {
            this.enlarge(1);
        }
        this.data[length] = (byte)n;
        this.length = length + 1;
        return this;
    }
    
    public ByteVector putByteArray(final byte[] array, final int n, final int n2) {
        if (this.length + n2 > this.data.length) {
            this.enlarge(n2);
        }
        if (array != null) {
            System.arraycopy(array, n, this.data, this.length, n2);
        }
        this.length += n2;
        return this;
    }
    
    public ByteVector putInt(final int n) {
        final int length = this.length;
        if (length + 4 > this.data.length) {
            this.enlarge(4);
        }
        final byte[] data = this.data;
        final int n2 = length + 1;
        data[length] = (byte)(n >>> 24);
        final int n3 = n2 + 1;
        data[n2] = (byte)(n >>> 16);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n >>> 8);
        data[n4] = (byte)n;
        this.length = n4 + 1;
        return this;
    }
    
    public ByteVector putLong(final long n) {
        final int length = this.length;
        if (length + 8 > this.data.length) {
            this.enlarge(8);
        }
        final byte[] data = this.data;
        final int n2 = (int)(n >>> 32);
        final int n3 = length + 1;
        data[length] = (byte)(n2 >>> 24);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n2 >>> 16);
        final int n5 = n4 + 1;
        data[n4] = (byte)(n2 >>> 8);
        final int n6 = n5 + 1;
        data[n5] = (byte)n2;
        final int n7 = (int)n;
        final int n8 = n6 + 1;
        data[n6] = (byte)(n7 >>> 24);
        final int n9 = n8 + 1;
        data[n8] = (byte)(n7 >>> 16);
        final int n10 = n9 + 1;
        data[n9] = (byte)(n7 >>> 8);
        data[n10] = (byte)n7;
        this.length = n10 + 1;
        return this;
    }
    
    public ByteVector putShort(final int n) {
        final int length = this.length;
        if (length + 2 > this.data.length) {
            this.enlarge(2);
        }
        final byte[] data = this.data;
        final int n2 = length + 1;
        data[length] = (byte)(n >>> 8);
        data[n2] = (byte)n;
        this.length = n2 + 1;
        return this;
    }
    
    public ByteVector putUTF8(final String s) {
        final int length = s.length();
        if (length <= 65535) {
            final int length2 = this.length;
            if (length2 + 2 + length > this.data.length) {
                this.enlarge(length + 2);
            }
            final byte[] data = this.data;
            final int n = length2 + 1;
            data[length2] = (byte)(length >>> 8);
            int n2 = n + 1;
            data[n] = (byte)length;
            for (int i = 0; i < length; ++i, ++n2) {
                final char char1 = s.charAt(i);
                if (char1 < '\u0001' || char1 > '\u007f') {
                    this.length = n2;
                    return this.encodeUTF8(s, i, 65535);
                }
                data[n2] = (byte)char1;
            }
            this.length = n2;
            return this;
        }
        throw new IllegalArgumentException();
    }
}
