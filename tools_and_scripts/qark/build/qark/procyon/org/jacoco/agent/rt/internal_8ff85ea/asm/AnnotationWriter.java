// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

final class AnnotationWriter extends AnnotationVisitor
{
    private final ByteVector bv;
    private final ClassWriter cw;
    private final boolean named;
    AnnotationWriter next;
    private final int offset;
    private final ByteVector parent;
    AnnotationWriter prev;
    private int size;
    
    AnnotationWriter(final ClassWriter cw, final boolean named, final ByteVector bv, final ByteVector parent, final int offset) {
        super(327680);
        this.cw = cw;
        this.named = named;
        this.bv = bv;
        this.parent = parent;
        this.offset = offset;
    }
    
    static void put(final AnnotationWriter[] array, int i, final ByteVector byteVector) {
        int n = (array.length - i) * 2 + 1;
        int n2 = i;
        while (true) {
            final int length = array.length;
            int size = 0;
            if (n2 >= length) {
                break;
            }
            if (array[n2] != null) {
                size = array[n2].getSize();
            }
            n += size;
            ++n2;
        }
        byteVector.putInt(n).putByte(array.length - i);
        while (i < array.length) {
            AnnotationWriter next = array[i];
            AnnotationWriter prev = null;
            int n3 = 0;
            while (next != null) {
                ++n3;
                next.visitEnd();
                next.prev = prev;
                prev = next;
                next = next.next;
            }
            byteVector.putShort(n3);
            while (prev != null) {
                byteVector.putByteArray(prev.bv.data, 0, prev.bv.length);
                prev = prev.prev;
            }
            ++i;
        }
    }
    
    static void putTarget(int n, final TypePath typePath, final ByteVector byteVector) {
        final int n2 = n >>> 24;
        Label_0136: {
            switch (n2) {
                default: {
                    switch (n2) {
                        default: {
                            switch (n2) {
                                default: {
                                    byteVector.put12(n >>> 24, (0xFFFF00 & n) >> 8);
                                    break Label_0136;
                                }
                                case 71:
                                case 72:
                                case 73:
                                case 74:
                                case 75: {
                                    byteVector.putInt(n);
                                    break Label_0136;
                                }
                            }
                            break;
                        }
                        case 19:
                        case 20:
                        case 21: {
                            byteVector.putByte(n >>> 24);
                            break Label_0136;
                        }
                        case 22: {
                            break Label_0136;
                        }
                    }
                    break;
                }
                case 0:
                case 1: {
                    byteVector.putShort(n >>> 16);
                    break;
                }
            }
        }
        if (typePath == null) {
            byteVector.putByte(0);
            return;
        }
        n = typePath.b[typePath.offset];
        byteVector.putByteArray(typePath.b, typePath.offset, n * 2 + 1);
    }
    
    int getSize() {
        int n = 0;
        for (AnnotationWriter next = this; next != null; next = next.next) {
            n += next.bv.length;
        }
        return n;
    }
    
    void put(final ByteVector byteVector) {
        int n = 0;
        int n2 = 2;
        AnnotationWriter next = this;
        AnnotationWriter prev = null;
        while (next != null) {
            ++n;
            n2 += next.bv.length;
            next.visitEnd();
            next.prev = prev;
            prev = next;
            next = next.next;
        }
        byteVector.putInt(n2);
        byteVector.putShort(n);
        while (prev != null) {
            byteVector.putByteArray(prev.bv.data, 0, prev.bv.length);
            prev = prev.prev;
        }
    }
    
    @Override
    public void visit(final String s, final Object o) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:539)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final String s2) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(s));
        }
        this.bv.put12(64, this.cw.newUTF8(s2)).putShort(0);
        return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
    }
    
    @Override
    public AnnotationVisitor visitArray(final String s) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(s));
        }
        this.bv.put12(91, 0);
        return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
    }
    
    @Override
    public void visitEnd() {
        if (this.parent != null) {
            final byte[] data = this.parent.data;
            data[this.offset] = (byte)(this.size >>> 8);
            data[this.offset + 1] = (byte)this.size;
        }
    }
    
    @Override
    public void visitEnum(final String s, final String s2, final String s3) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(s));
        }
        this.bv.put12(101, this.cw.newUTF8(s2)).putShort(this.cw.newUTF8(s3));
    }
}
