/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.ByteVector;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassReader;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;

public class Attribute {
    Attribute next;
    public final String type;
    byte[] value;

    protected Attribute(String string2) {
        this.type = string2;
    }

    final int getCount() {
        int n = 0;
        Attribute attribute = this;
        while (attribute != null) {
            ++n;
            attribute = attribute.next;
        }
        return n;
    }

    protected Label[] getLabels() {
        return null;
    }

    final int getSize(ClassWriter classWriter, byte[] arrby, int n, int n2, int n3) {
        Attribute attribute = this;
        int n4 = 0;
        while (attribute != null) {
            classWriter.newUTF8(attribute.type);
            n4 += attribute.write((ClassWriter)classWriter, (byte[])arrby, (int)n, (int)n2, (int)n3).length + 6;
            attribute = attribute.next;
        }
        return n4;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    public boolean isUnknown() {
        return true;
    }

    final void put(ClassWriter classWriter, byte[] arrby, int n, int n2, int n3, ByteVector byteVector) {
        Attribute attribute = this;
        while (attribute != null) {
            ByteVector byteVector2 = attribute.write(classWriter, arrby, n, n2, n3);
            byteVector.putShort(classWriter.newUTF8(attribute.type)).putInt(byteVector2.length);
            byteVector.putByteArray(byteVector2.data, 0, byteVector2.length);
            attribute = attribute.next;
        }
    }

    protected Attribute read(ClassReader classReader, int n, int n2, char[] object, int n3, Label[] arrlabel) {
        object = new Attribute(this.type);
        object.value = new byte[n2];
        System.arraycopy(classReader.b, n, object.value, 0, n2);
        return object;
    }

    protected ByteVector write(ClassWriter object, byte[] arrby, int n, int n2, int n3) {
        object = new ByteVector();
        object.data = this.value;
        object.length = this.value.length;
        return object;
    }
}

