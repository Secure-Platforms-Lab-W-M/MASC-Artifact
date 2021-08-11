/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class ByteVector {
    byte[] data;
    int length;

    public ByteVector() {
        this.data = new byte[64];
    }

    public ByteVector(int n) {
        this.data = new byte[n];
    }

    private void enlarge(int n) {
        int n2 = this.data.length * 2;
        if (n2 > (n = this.length + n)) {
            n = n2;
        }
        byte[] arrby = new byte[n];
        System.arraycopy(this.data, 0, arrby, 0, this.length);
        this.data = arrby;
    }

    ByteVector encodeUTF8(String string2, int n, int n2) {
        int n3;
        int n4;
        int n5 = string2.length();
        int n6 = n3 = n;
        for (n4 = n3; n4 < n5; ++n4) {
            n3 = string2.charAt(n4);
            n3 = n3 >= 1 && n3 <= 127 ? n6 + 1 : (n3 > 2047 ? n6 + 3 : n6 + 2);
            n6 = n3;
        }
        if (n6 <= n2) {
            n2 = this.length - n - 2;
            if (n2 >= 0) {
                this.data[n2] = (byte)(n6 >>> 8);
                this.data[n2 + 1] = (byte)n6;
            }
            if (this.length + n6 - n > this.data.length) {
                this.enlarge(n6 - n);
            }
            n3 = this.length;
            n2 = n;
            n = n3;
            while (n2 < n5) {
                byte[] arrby;
                n4 = string2.charAt(n2);
                if (n4 >= 1 && n4 <= 127) {
                    arrby = this.data;
                    n3 = n + 1;
                    arrby[n] = (byte)n4;
                    n = n3;
                } else if (n4 > 2047) {
                    arrby = this.data;
                    n3 = n + 1;
                    arrby[n] = (byte)(n4 >> 12 & 15 | 224);
                    arrby = this.data;
                    n6 = n3 + 1;
                    arrby[n3] = (byte)(n4 >> 6 & 63 | 128);
                    arrby = this.data;
                    n = n6 + 1;
                    arrby[n6] = (byte)(n4 & 63 | 128);
                } else {
                    arrby = this.data;
                    n3 = n + 1;
                    arrby[n] = (byte)(n4 >> 6 & 31 | 192);
                    this.data[n3] = (byte)(n4 & 63 | 128);
                    n = n3 + 1;
                }
                ++n2;
            }
            this.length = n;
            return this;
        }
        throw new IllegalArgumentException();
    }

    ByteVector put11(int n, int n2) {
        int n3 = this.length;
        if (n3 + 2 > this.data.length) {
            this.enlarge(2);
        }
        byte[] arrby = this.data;
        int n4 = n3 + 1;
        arrby[n3] = (byte)n;
        arrby[n4] = (byte)n2;
        this.length = n4 + 1;
        return this;
    }

    ByteVector put12(int n, int n2) {
        int n3 = this.length;
        if (n3 + 3 > this.data.length) {
            this.enlarge(3);
        }
        byte[] arrby = this.data;
        int n4 = n3 + 1;
        arrby[n3] = (byte)n;
        n = n4 + 1;
        arrby[n4] = (byte)(n2 >>> 8);
        arrby[n] = (byte)n2;
        this.length = n + 1;
        return this;
    }

    public ByteVector putByte(int n) {
        int n2 = this.length;
        if (n2 + 1 > this.data.length) {
            this.enlarge(1);
        }
        this.data[n2] = (byte)n;
        this.length = n2 + 1;
        return this;
    }

    public ByteVector putByteArray(byte[] arrby, int n, int n2) {
        if (this.length + n2 > this.data.length) {
            this.enlarge(n2);
        }
        if (arrby != null) {
            System.arraycopy(arrby, n, this.data, this.length, n2);
        }
        this.length += n2;
        return this;
    }

    public ByteVector putInt(int n) {
        int n2 = this.length;
        if (n2 + 4 > this.data.length) {
            this.enlarge(4);
        }
        byte[] arrby = this.data;
        int n3 = n2 + 1;
        arrby[n2] = (byte)(n >>> 24);
        n2 = n3 + 1;
        arrby[n3] = (byte)(n >>> 16);
        n3 = n2 + 1;
        arrby[n2] = (byte)(n >>> 8);
        arrby[n3] = (byte)n;
        this.length = n3 + 1;
        return this;
    }

    public ByteVector putLong(long l) {
        int n = this.length;
        if (n + 8 > this.data.length) {
            this.enlarge(8);
        }
        byte[] arrby = this.data;
        int n2 = (int)(l >>> 32);
        int n3 = n + 1;
        arrby[n] = (byte)(n2 >>> 24);
        n = n3 + 1;
        arrby[n3] = (byte)(n2 >>> 16);
        int n4 = n + 1;
        arrby[n] = (byte)(n2 >>> 8);
        n3 = n4 + 1;
        arrby[n4] = (byte)n2;
        n2 = (int)l;
        n = n3 + 1;
        arrby[n3] = (byte)(n2 >>> 24);
        n3 = n + 1;
        arrby[n] = (byte)(n2 >>> 16);
        n = n3 + 1;
        arrby[n3] = (byte)(n2 >>> 8);
        arrby[n] = (byte)n2;
        this.length = n + 1;
        return this;
    }

    public ByteVector putShort(int n) {
        int n2 = this.length;
        if (n2 + 2 > this.data.length) {
            this.enlarge(2);
        }
        byte[] arrby = this.data;
        int n3 = n2 + 1;
        arrby[n2] = (byte)(n >>> 8);
        arrby[n3] = (byte)n;
        this.length = n3 + 1;
        return this;
    }

    public ByteVector putUTF8(String string2) {
        int n = string2.length();
        if (n <= 65535) {
            int n2 = this.length;
            if (n2 + 2 + n > this.data.length) {
                this.enlarge(n + 2);
            }
            byte[] arrby = this.data;
            int n3 = n2 + 1;
            arrby[n2] = (byte)(n >>> 8);
            n2 = n3 + 1;
            arrby[n3] = (byte)n;
            n3 = 0;
            while (n3 < n) {
                char c = string2.charAt(n3);
                if (c >= '\u0001' && c <= '') {
                    arrby[n2] = (byte)c;
                    ++n3;
                    ++n2;
                    continue;
                }
                this.length = n2;
                return this.encodeUTF8(string2, n3, 65535);
            }
            this.length = n2;
            return this;
        }
        throw new IllegalArgumentException();
    }
}

