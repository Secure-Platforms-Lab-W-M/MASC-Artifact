// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class Attribute
{
    Attribute next;
    public final String type;
    byte[] value;
    
    protected Attribute(final String type) {
        this.type = type;
    }
    
    final int getCount() {
        int n = 0;
        for (Attribute next = this; next != null; next = next.next) {
            ++n;
        }
        return n;
    }
    
    protected Label[] getLabels() {
        return null;
    }
    
    final int getSize(final ClassWriter classWriter, final byte[] array, final int n, final int n2, final int n3) {
        Attribute next = this;
        int n4 = 0;
        while (next != null) {
            classWriter.newUTF8(next.type);
            n4 += next.write(classWriter, array, n, n2, n3).length + 6;
            next = next.next;
        }
        return n4;
    }
    
    public boolean isCodeAttribute() {
        return false;
    }
    
    public boolean isUnknown() {
        return true;
    }
    
    final void put(final ClassWriter classWriter, final byte[] array, final int n, final int n2, final int n3, final ByteVector byteVector) {
        for (Attribute next = this; next != null; next = next.next) {
            final ByteVector write = next.write(classWriter, array, n, n2, n3);
            byteVector.putShort(classWriter.newUTF8(next.type)).putInt(write.length);
            byteVector.putByteArray(write.data, 0, write.length);
        }
    }
    
    protected Attribute read(final ClassReader classReader, final int n, final int n2, final char[] array, final int n3, final Label[] array2) {
        final Attribute attribute = new Attribute(this.type);
        attribute.value = new byte[n2];
        System.arraycopy(classReader.b, n, attribute.value, 0, n2);
        return attribute;
    }
    
    protected ByteVector write(final ClassWriter classWriter, final byte[] array, final int n, final int n2, final int n3) {
        final ByteVector byteVector = new ByteVector();
        byteVector.data = this.value;
        byteVector.length = this.value.length;
        return byteVector;
    }
}
