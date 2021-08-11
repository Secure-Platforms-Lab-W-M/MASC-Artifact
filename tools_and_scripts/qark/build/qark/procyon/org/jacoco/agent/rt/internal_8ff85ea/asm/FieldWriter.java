// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

final class FieldWriter extends FieldVisitor
{
    private final int access;
    private AnnotationWriter anns;
    private Attribute attrs;
    private final ClassWriter cw;
    private final int desc;
    private AnnotationWriter ianns;
    private AnnotationWriter itanns;
    private final int name;
    private int signature;
    private AnnotationWriter tanns;
    private int value;
    
    FieldWriter(final ClassWriter cw, final int access, final String s, final String s2, final String s3, final Object o) {
        super(327680);
        if (cw.firstField == null) {
            cw.firstField = this;
        }
        else {
            cw.lastField.fv = this;
        }
        cw.lastField = this;
        this.cw = cw;
        this.access = access;
        this.name = cw.newUTF8(s);
        this.desc = cw.newUTF8(s2);
        if (s3 != null) {
            this.signature = cw.newUTF8(s3);
        }
        if (o != null) {
            this.value = cw.newConstItem(o).index;
        }
    }
    
    int getSize() {
        int n = 8;
        if (this.value != 0) {
            this.cw.newUTF8("ConstantValue");
            n = 8 + 8;
        }
        int n2 = n;
        Label_0081: {
            if ((this.access & 0x1000) != 0x0) {
                if ((this.cw.version & 0xFFFF) >= 49) {
                    n2 = n;
                    if ((this.access & 0x40000) == 0x0) {
                        break Label_0081;
                    }
                }
                this.cw.newUTF8("Synthetic");
                n2 = n + 6;
            }
        }
        int n3 = n2;
        if ((this.access & 0x20000) != 0x0) {
            this.cw.newUTF8("Deprecated");
            n3 = n2 + 6;
        }
        int n4 = n3;
        if (this.signature != 0) {
            this.cw.newUTF8("Signature");
            n4 = n3 + 8;
        }
        int n5 = n4;
        if (this.anns != null) {
            this.cw.newUTF8("RuntimeVisibleAnnotations");
            n5 = n4 + (this.anns.getSize() + 8);
        }
        int n6 = n5;
        if (this.ianns != null) {
            this.cw.newUTF8("RuntimeInvisibleAnnotations");
            n6 = n5 + (this.ianns.getSize() + 8);
        }
        int n7 = n6;
        if (this.tanns != null) {
            this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            n7 = n6 + (this.tanns.getSize() + 8);
        }
        int n8 = n7;
        if (this.itanns != null) {
            this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            n8 = n7 + (this.itanns.getSize() + 8);
        }
        int n9 = n8;
        if (this.attrs != null) {
            n9 = n8 + this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return n9;
    }
    
    void put(final ByteVector byteVector) {
        byteVector.putShort(this.access & ((this.access & 0x40000) / 64 | 0x60000)).putShort(this.name).putShort(this.desc);
        int n = 0;
        if (this.value != 0) {
            n = 0 + 1;
        }
        int n2 = n;
        Label_0096: {
            if ((this.access & 0x1000) != 0x0) {
                if ((this.cw.version & 0xFFFF) >= 49) {
                    n2 = n;
                    if ((this.access & 0x40000) == 0x0) {
                        break Label_0096;
                    }
                }
                n2 = n + 1;
            }
        }
        int n3 = n2;
        if ((this.access & 0x20000) != 0x0) {
            n3 = n2 + 1;
        }
        int n4 = n3;
        if (this.signature != 0) {
            n4 = n3 + 1;
        }
        int n5 = n4;
        if (this.anns != null) {
            n5 = n4 + 1;
        }
        int n6 = n5;
        if (this.ianns != null) {
            n6 = n5 + 1;
        }
        int n7 = n6;
        if (this.tanns != null) {
            n7 = n6 + 1;
        }
        int n8 = n7;
        if (this.itanns != null) {
            n8 = n7 + 1;
        }
        int n9 = n8;
        if (this.attrs != null) {
            n9 = n8 + this.attrs.getCount();
        }
        byteVector.putShort(n9);
        if (this.value != 0) {
            byteVector.putShort(this.cw.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.value);
        }
        if ((this.access & 0x1000) != 0x0 && ((this.cw.version & 0xFFFF) < 49 || (0x40000 & this.access) != 0x0)) {
            byteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 0x20000) != 0x0) {
            byteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != 0) {
            byteVector.putShort(this.cw.newUTF8("Signature"));
            byteVector.putInt(2).putShort(this.signature);
        }
        if (this.anns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this.cw, null, 0, -1, -1, byteVector);
        }
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
        if (b) {
            annotationWriter.next = this.anns;
            return this.anns = annotationWriter;
        }
        annotationWriter.next = this.ianns;
        return this.ianns = annotationWriter;
    }
    
    @Override
    public void visitAttribute(final Attribute attrs) {
        attrs.next = this.attrs;
        this.attrs = attrs;
    }
    
    @Override
    public void visitEnd() {
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(n, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.tanns;
            return this.tanns = annotationWriter;
        }
        annotationWriter.next = this.itanns;
        return this.itanns = annotationWriter;
    }
}
