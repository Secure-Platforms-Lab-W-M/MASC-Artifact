// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class TypePath
{
    public static final int ARRAY_ELEMENT = 0;
    public static final int INNER_TYPE = 1;
    public static final int TYPE_ARGUMENT = 3;
    public static final int WILDCARD_BOUND = 2;
    byte[] b;
    int offset;
    
    TypePath(final byte[] b, final int offset) {
        this.b = b;
        this.offset = offset;
    }
    
    public static TypePath fromString(final String s) {
        if (s != null && s.length() != 0) {
            final int length = s.length();
            final ByteVector byteVector = new ByteVector(length);
            byteVector.putByte(0);
            int i = 0;
            while (i < length) {
                int j = i + 1;
                final char char1 = s.charAt(i);
                if (char1 == '[') {
                    byteVector.put11(0, 0);
                    i = j;
                }
                else if (char1 == '.') {
                    byteVector.put11(1, 0);
                    i = j;
                }
                else if (char1 == '*') {
                    byteVector.put11(2, 0);
                    i = j;
                }
                else {
                    i = j;
                    if (char1 < '0') {
                        continue;
                    }
                    i = j;
                    if (char1 > '9') {
                        continue;
                    }
                    int n = char1 - '0';
                    while (j < length) {
                        final char char2 = s.charAt(j);
                        if (char2 < '0' || char2 > '9') {
                            break;
                        }
                        n = n * 10 + char2 - 48;
                        ++j;
                    }
                    if ((i = j) < length) {
                        i = j;
                        if (s.charAt(j) == ';') {
                            i = j + 1;
                        }
                    }
                    byteVector.put11(3, n);
                }
            }
            byteVector.data[0] = (byte)(byteVector.length / 2);
            return new TypePath(byteVector.data, 0);
        }
        return null;
    }
    
    public int getLength() {
        return this.b[this.offset];
    }
    
    public int getStep(final int n) {
        return this.b[this.offset + n * 2 + 1];
    }
    
    public int getStepArgument(final int n) {
        return this.b[this.offset + n * 2 + 2];
    }
    
    @Override
    public String toString() {
        final int length = this.getLength();
        final StringBuilder sb = new StringBuilder(length * 2);
        for (int i = 0; i < length; ++i) {
            switch (this.getStep(i)) {
                default: {
                    sb.append('_');
                    break;
                }
                case 3: {
                    sb.append(this.getStepArgument(i));
                    sb.append(';');
                    break;
                }
                case 2: {
                    sb.append('*');
                    break;
                }
                case 1: {
                    sb.append('.');
                    break;
                }
                case 0: {
                    sb.append('[');
                    break;
                }
            }
        }
        return sb.toString();
    }
}
