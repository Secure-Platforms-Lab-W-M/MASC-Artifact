/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import java.io.IOException;
import java.io.InputStream;
import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Attribute;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ByteVector;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Context;
import org.jacoco.agent.rt.internal_8ff85ea.asm.FieldVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Item;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Type;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;

public class ClassReader {
    static final boolean ANNOTATIONS = true;
    static final int EXPAND_ASM_INSNS = 256;
    public static final int EXPAND_FRAMES = 8;
    static final boolean FRAMES = true;
    static final boolean RESIZE = true;
    static final boolean SIGNATURES = true;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    static final boolean WRITER = true;
    public final byte[] b;
    public final int header;
    private final int[] items;
    private final int maxStringLength;
    private final String[] strings;

    public ClassReader(InputStream inputStream) throws IOException {
        this(ClassReader.readClass(inputStream, false));
    }

    public ClassReader(String string2) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.replace('.', '/'));
        stringBuilder.append(".class");
        this(ClassReader.readClass(ClassLoader.getSystemResourceAsStream(stringBuilder.toString()), true));
    }

    public ClassReader(byte[] arrby) {
        this(arrby, 0, arrby.length);
    }

    /*
     * Exception decompiling
     */
    public ClassReader(byte[] var1_1, int var2_2, int var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:486)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private void copyBootstrapMethods(ClassWriter classWriter, Item[] object, char[] arrc) {
        int n;
        int n2 = this.getAttributes();
        int n3 = 0;
        int n4 = this.readUnsignedShort(n2);
        do {
            n = n3;
            if (n4 <= 0) break;
            if ("BootstrapMethods".equals(this.readUTF8(n2 + 2, arrc))) {
                n = 1;
                break;
            }
            n2 += this.readInt(n2 + 4) + 6;
            --n4;
        } while (true);
        if (n == 0) {
            return;
        }
        int n5 = this.readUnsignedShort(n2 + 8);
        n = 0;
        n4 = n2 + 10;
        do {
            n3 = n4;
            if (n >= n5) break;
            int n6 = this.readConst(this.readUnsignedShort(n3), arrc).hashCode();
            n4 = n3;
            for (int i = this.readUnsignedShort((int)(n3 + 2)); i > 0; --i) {
                n6 ^= this.readConst(this.readUnsignedShort(n4 + 4), arrc).hashCode();
                n4 += 2;
            }
            n4 += 4;
            Item item = new Item(n);
            item.set(n3 - n2 - 10, Integer.MAX_VALUE & n6);
            n3 = item.hashCode % object.length;
            item.next = object[n3];
            object[n3] = item;
            ++n;
        } while (true);
        n4 = this.readInt(n2 + 4);
        object = new ByteVector(n4 + 62);
        object.putByteArray(this.b, n2 + 10, n4 - 2);
        classWriter.bootstrapMethodsCount = n5;
        classWriter.bootstrapMethods = object;
    }

    private int getAttributes() {
        int n;
        int n2;
        int n3 = this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        for (n2 = this.readUnsignedShort((int)n3); n2 > 0; --n2) {
            for (n = this.readUnsignedShort((int)(n3 + 8)); n > 0; --n) {
                n3 += this.readInt(n3 + 12) + 6;
            }
            n3 += 8;
        }
        for (n2 = this.readUnsignedShort((int)(n3 += 2)); n2 > 0; --n2) {
            for (n = this.readUnsignedShort((int)(n3 + 8)); n > 0; --n) {
                n3 += this.readInt(n3 + 12) + 6;
            }
            n3 += 8;
        }
        return n3 + 2;
    }

    private void getImplicitFrame(Context context) {
        int n;
        String string2 = context.desc;
        Object[] arrobject = context.local;
        if ((context.access & 8) == 0) {
            if ("<init>".equals(context.name)) {
                n = 0 + 1;
                arrobject[0] = Opcodes.UNINITIALIZED_THIS;
            } else {
                n = 0 + 1;
                arrobject[0] = this.readClass(this.header + 2, context.buffer);
            }
        } else {
            n = 0;
        }
        int n2 = 1;
        int n3 = n;
        block8 : do {
            int n4;
            n = n4 = n2 + 1;
            int n5 = n4;
            switch (string2.charAt(n2)) {
                default: {
                    break block8;
                }
                case '[': {
                    while (string2.charAt(n) == '[') {
                        ++n;
                    }
                    n4 = n;
                    if (string2.charAt(n) == 'L') {
                        ++n;
                        do {
                            n4 = n;
                            if (string2.charAt(n) == ';') break;
                            ++n;
                        } while (true);
                    }
                    n = n3 + 1;
                    arrobject[n3] = string2.substring(n2, ++n4);
                    break;
                }
                case 'L': {
                    while (string2.charAt(n5) != ';') {
                        ++n5;
                    }
                    arrobject[n3] = string2.substring(n2 + 1, n5);
                    n = n3 + 1;
                    n4 = n5 + 1;
                    break;
                }
                case 'J': {
                    n = n3 + 1;
                    arrobject[n3] = Opcodes.LONG;
                    break;
                }
                case 'F': {
                    n = n3 + 1;
                    arrobject[n3] = Opcodes.FLOAT;
                    break;
                }
                case 'D': {
                    n = n3 + 1;
                    arrobject[n3] = Opcodes.DOUBLE;
                    break;
                }
                case 'B': 
                case 'C': 
                case 'I': 
                case 'S': 
                case 'Z': {
                    n = n3 + 1;
                    arrobject[n3] = Opcodes.INTEGER;
                }
            }
            n2 = n4;
            n3 = n;
        } while (true);
        context.localCount = n3;
    }

    private int readAnnotationTarget(Context context, int n) {
        int n2;
        int n3 = this.readInt(n);
        int n4 = n3 >>> 24;
        block0 : switch (n4) {
            default: {
                n2 = -16777216;
                switch (n4) {
                    default: {
                        switch (n4) {
                            default: {
                                switch (n4) {
                                    default: {
                                        if (n3 >>> 24 < 67) {
                                            n2 = -256;
                                        }
                                        n2 = n3 & n2;
                                        n3 = n + 3;
                                        n = n2;
                                        n2 = n3;
                                        break block0;
                                    }
                                    case 71: 
                                    case 72: 
                                    case 73: 
                                    case 74: 
                                    case 75: 
                                }
                                n2 = n3 & -16776961;
                                n3 = n + 4;
                                n = n2;
                                n2 = n3;
                                break block0;
                            }
                            case 64: 
                            case 65: 
                        }
                        n3 &= -16777216;
                        n4 = this.readUnsignedShort(n + 1);
                        context.start = new Label[n4];
                        context.end = new Label[n4];
                        context.index = new int[n4];
                        n2 = n + 3;
                        for (n = 0; n < n4; ++n) {
                            int n5 = this.readUnsignedShort(n2);
                            int n6 = this.readUnsignedShort(n2 + 2);
                            context.start[n] = this.readLabel(n5, context.labels);
                            context.end[n] = this.readLabel(n5 + n6, context.labels);
                            context.index[n] = this.readUnsignedShort(n2 + 4);
                            n2 += 6;
                        }
                        n = n3;
                        break block0;
                    }
                    case 19: 
                    case 20: 
                    case 21: {
                        n2 = n3 & -16777216;
                        n3 = n + 1;
                        n = n2;
                        n2 = n3;
                        break block0;
                    }
                    case 22: 
                }
            }
            case 0: 
            case 1: {
                n2 = n + 2;
                n = n3 &= -65536;
            }
        }
        n3 = this.readByte(n2);
        context.typeRef = n;
        TypePath typePath = n3 == 0 ? null : new TypePath(this.b, n2);
        context.typePath = typePath;
        return n2 + 1 + n3 * 2;
    }

    private int readAnnotationValue(int n, char[] object, String string2, AnnotationVisitor annotationVisitor) {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        if (annotationVisitor == null) {
            int n9 = this.b[n] & 255;
            if (n9 != 64) {
                if (n9 != 91) {
                    if (n9 != 101) {
                        return n + 3;
                    }
                    return n + 5;
                }
                return this.readAnnotationValues(n + 1, (char[])object, false, null);
            }
            return this.readAnnotationValues(n + 3, (char[])object, true, null);
        }
        byte[] arrby = this.b;
        int n10 = n + 1;
        switch (arrby[n] & 255) {
            default: {
                return n10;
            }
            case 115: {
                annotationVisitor.visit(string2, this.readUTF8(n10, (char[])object));
                return n10 + 2;
            }
            case 101: {
                annotationVisitor.visitEnum(string2, this.readUTF8(n10, (char[])object), this.readUTF8(n10 + 2, (char[])object));
                return n10 + 4;
            }
            case 99: {
                annotationVisitor.visit(string2, Type.getType(this.readUTF8(n10, (char[])object)));
                return n10 + 2;
            }
            case 91: {
                int n11 = this.readUnsignedShort(n10);
                n10 += 2;
                if (n11 == 0) {
                    return this.readAnnotationValues(n10 - 2, (char[])object, false, annotationVisitor.visitArray(string2));
                }
                arrby = this.b;
                n = n10 + 1;
                if ((n10 = arrby[n10] & 255) != 70) {
                    if (n10 != 83) {
                        if (n10 != 90) {
                            switch (n10) {
                                default: {
                                    switch (n10) {
                                        default: {
                                            return this.readAnnotationValues(n - 3, (char[])object, false, annotationVisitor.visitArray(string2));
                                        }
                                        case 74: {
                                            object = new long[n11];
                                            n10 = n;
                                            for (n = n8; n < n11; ++n) {
                                                object[n] = this.readLong(this.items[this.readUnsignedShort(n10)]);
                                                n10 += 3;
                                            }
                                            annotationVisitor.visit(string2, object);
                                            return n10 - 1;
                                        }
                                        case 73: 
                                    }
                                    object = new int[n11];
                                    n10 = n;
                                    for (n = n2; n < n11; ++n) {
                                        object[n] = this.readInt(this.items[this.readUnsignedShort(n10)]);
                                        n10 += 3;
                                    }
                                    annotationVisitor.visit(string2, object);
                                    return n10 - 1;
                                }
                                case 68: {
                                    object = new double[n11];
                                    n10 = n;
                                    for (n = n3; n < n11; ++n) {
                                        object[n] = (float)Double.longBitsToDouble(this.readLong(this.items[this.readUnsignedShort(n10)]));
                                        n10 += 3;
                                    }
                                    annotationVisitor.visit(string2, object);
                                    return n10 - 1;
                                }
                                case 67: {
                                    object = new char[n11];
                                    n10 = n;
                                    for (n = n4; n < n11; ++n) {
                                        object[n] = (char)this.readInt(this.items[this.readUnsignedShort(n10)]);
                                        n10 += 3;
                                    }
                                    annotationVisitor.visit(string2, object);
                                    return n10 - 1;
                                }
                                case 66: 
                            }
                            object = new byte[n11];
                            n10 = n;
                            for (n = n5; n < n11; ++n) {
                                object[n] = (byte)this.readInt(this.items[this.readUnsignedShort(n10)]);
                                n10 += 3;
                            }
                            annotationVisitor.visit(string2, object);
                            return n10 - 1;
                        }
                        object = new boolean[n11];
                        n8 = 0;
                        n10 = n;
                        for (n = n8; n < n11; ++n) {
                            boolean bl = this.readInt(this.items[this.readUnsignedShort(n10)]) != 0;
                            object[n] = (float)bl;
                            n10 += 3;
                        }
                        annotationVisitor.visit(string2, object);
                        return n10 - 1;
                    }
                    object = new short[n11];
                    n10 = n;
                    for (n = n6; n < n11; ++n) {
                        object[n] = (short)this.readInt(this.items[this.readUnsignedShort(n10)]);
                        n10 += 3;
                    }
                    annotationVisitor.visit(string2, object);
                    return n10 - 1;
                }
                object = new float[n11];
                n10 = n;
                for (n = n7; n < n11; ++n) {
                    object[n] = Float.intBitsToFloat(this.readInt(this.items[this.readUnsignedShort(n10)]));
                    n10 += 3;
                }
                annotationVisitor.visit(string2, object);
                return n10 - 1;
            }
            case 90: {
                object = this.readInt(this.items[this.readUnsignedShort(n10)]) == 0 ? Boolean.FALSE : Boolean.TRUE;
                annotationVisitor.visit(string2, object);
                return n10 + 2;
            }
            case 83: {
                annotationVisitor.visit(string2, (short)this.readInt(this.items[this.readUnsignedShort(n10)]));
                return n10 + 2;
            }
            case 68: 
            case 70: 
            case 73: 
            case 74: {
                annotationVisitor.visit(string2, this.readConst(this.readUnsignedShort(n10), (char[])object));
                return n10 + 2;
            }
            case 67: {
                annotationVisitor.visit(string2, Character.valueOf((char)this.readInt(this.items[this.readUnsignedShort(n10)])));
                return n10 + 2;
            }
            case 66: {
                annotationVisitor.visit(string2, (byte)this.readInt(this.items[this.readUnsignedShort(n10)]));
                return n10 + 2;
            }
            case 64: 
        }
        return this.readAnnotationValues(n10 + 2, (char[])object, true, annotationVisitor.visitAnnotation(string2, this.readUTF8(n10, (char[])object)));
    }

    private int readAnnotationValues(int n, char[] arrc, boolean bl, AnnotationVisitor annotationVisitor) {
        int n2 = this.readUnsignedShort(n);
        int n3 = n + 2;
        int n4 = n2;
        n = n3;
        if (bl) {
            n = n3;
            n4 = n2;
            do {
                n2 = n;
                if (n4 > 0) {
                    n = this.readAnnotationValue(n + 2, arrc, this.readUTF8(n, arrc), annotationVisitor);
                    --n4;
                    continue;
                }
                break;
            } while (true);
        } else {
            do {
                n2 = n;
                if (n4 <= 0) break;
                n = this.readAnnotationValue(n, arrc, null, annotationVisitor);
                --n4;
            } while (true);
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return n2;
    }

    private Attribute readAttribute(Attribute[] arrattribute, String string2, int n, int n2, char[] arrc, int n3, Label[] arrlabel) {
        for (int i = 0; i < arrattribute.length; ++i) {
            if (!arrattribute[i].type.equals(string2)) continue;
            return arrattribute[i].read(this, n, n2, arrc, n3, arrlabel);
        }
        return new Attribute(string2).read(this, n, n2, null, -1, null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static byte[] readClass(InputStream var0, boolean var1_1) throws IOException {
        if (var0 == null) throw new IOException("Class not found");
        try {
            var4_2 = new byte[var0.available()];
            var2_4 = 0;
            do lbl-1000: // 2 sources:
            {
                if ((var3_5 = var0.read(var4_2, var2_4, var4_2.length - var2_4)) != -1) break block6;
                var5_6 = var4_2;
                if (var2_4 < var4_2.length) {
                    var5_6 = new byte[var2_4];
                    System.arraycopy(var4_2, 0, var5_6, 0, var2_4);
                }
                if (var1_1 == false) return var5_6;
                break;
            } while (true);
        }
        catch (Throwable var4_3) {
            if (var1_1 == false) throw var4_3;
            var0.close();
            throw var4_3;
        }
        {
            block7 : {
                block8 : {
                    block6 : {
                        var0.close();
                        return var5_6;
                    }
                    var2_4 = var3_5 = var2_4 + var3_5;
                    var5_6 = var4_2;
                    if (var3_5 != var4_2.length) break block7;
                    var2_4 = var0.read();
                    if (var2_4 >= 0) break block8;
                    if (var1_1 == false) return var4_2;
                    var0.close();
                    return var4_2;
                }
                var5_6 = new byte[var4_2.length + 1000];
                System.arraycopy(var4_2, 0, var5_6, 0, var3_5);
                var5_6[var3_5] = (byte)var2_4;
                var2_4 = var3_5 + 1;
            }
            var4_2 = var5_6;
            ** while (true)
        }
    }

    private void readCode(MethodVisitor methodVisitor, Context context, int n) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.useAs(TypeTransformer.java:868)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:668)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    private int readField(ClassVisitor object, Context context, int n) {
        int n2;
        char[] arrc = context.buffer;
        int n3 = this.readUnsignedShort(n);
        String string2 = this.readUTF8(n + 2, arrc);
        String string3 = this.readUTF8(n + 4, arrc);
        int n4 = n + 6;
        n = 0;
        Object object2 = null;
        int n5 = 0;
        int n6 = 0;
        Object object3 = null;
        Object object4 = null;
        int n7 = 0;
        for (n2 = this.readUnsignedShort((int)n4); n2 > 0; --n2) {
            Object object5 = this.readUTF8(n4 + 2, arrc);
            if ("ConstantValue".equals(object5)) {
                int n8 = this.readUnsignedShort(n4 + 8);
                object3 = n8 == 0 ? null : this.readConst(n8, arrc);
            } else if ("Signature".equals(object5)) {
                object2 = this.readUTF8(n4 + 8, arrc);
            } else if ("Deprecated".equals(object5)) {
                n3 |= 131072;
            } else if ("Synthetic".equals(object5)) {
                n3 |= 266240;
            } else if ("RuntimeVisibleAnnotations".equals(object5)) {
                n5 = n4 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(object5)) {
                n7 = n4 + 8;
            } else if ("RuntimeInvisibleAnnotations".equals(object5)) {
                n6 = n4 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(object5)) {
                n = n4 + 8;
            } else {
                Attribute attribute = this.readAttribute(context.attrs, (String)object5, n4 + 8, this.readInt(n4 + 4), arrc, -1, null);
                object5 = object4;
                if (attribute != null) {
                    attribute.next = object4;
                    object5 = attribute;
                }
                object4 = object5;
            }
            n4 += this.readInt(n4 + 4) + 6;
        }
        n2 = n4 + 2;
        if ((object2 = object.visitField(n3, string2, string3, (String)object2, object3)) == null) {
            return n2;
        }
        if (n5 != 0) {
            n4 = this.readUnsignedShort(n5);
            n3 = n5 + 2;
            for (n5 = n4; n5 > 0; --n5) {
                n3 = this.readAnnotationValues(n3 + 2, arrc, true, object2.visitAnnotation(this.readUTF8(n3, arrc), true));
            }
        }
        if (n6 != 0) {
            n3 = this.readUnsignedShort(n6);
            n5 = n6 + 2;
            for (n6 = n3; n6 > 0; --n6) {
                n5 = this.readAnnotationValues(n5 + 2, arrc, true, object2.visitAnnotation(this.readUTF8(n5, arrc), false));
            }
        }
        n3 = n7;
        if (n7 != 0) {
            n6 = this.readUnsignedShort(n7);
            n5 = n7 + 2;
            do {
                n3 = n7;
                if (n6 <= 0) break;
                n5 = this.readAnnotationTarget(context, n5);
                n5 = this.readAnnotationValues(n5 + 2, arrc, true, object2.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(n5, arrc), true));
                --n6;
            } while (true);
        }
        n5 = n;
        if (n != 0) {
            n7 = this.readUnsignedShort(n);
            n6 = n + 2;
            do {
                n5 = n;
                if (n7 <= 0) break;
                n6 = this.readAnnotationTarget(context, n6);
                n6 = this.readAnnotationValues(n6 + 2, arrc, true, object2.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(n6, arrc), false));
                --n7;
            } while (true);
        }
        while (object4 != null) {
            object = object4.next;
            object4.next = null;
            object2.visitAttribute((Attribute)object4);
            object4 = object;
        }
        object2.visitEnd();
        return n2;
    }

    private int readFrame(int n, boolean bl, boolean bl2, Context context) {
        int n2;
        int n3;
        char[] arrc = context.buffer;
        Label[] arrlabel = context.labels;
        if (bl) {
            n3 = this.b[n] & 255;
            n2 = n + 1;
            n = n3;
            n3 = n2;
        } else {
            context.offset = -1;
            n2 = 255;
            n3 = n;
            n = n2;
        }
        int n4 = 0;
        context.localDiff = 0;
        if (n < 64) {
            context.mode = 3;
            context.stackCount = 0;
            n2 = n;
            n = n3;
            n3 = n2;
        } else if (n < 128) {
            n3 = this.readFrameType(context.stack, 0, n3, arrc, arrlabel);
            context.mode = 4;
            context.stackCount = 1;
            n2 = n - 64;
            n = n3;
            n3 = n2;
        } else {
            int n5 = this.readUnsignedShort(n3);
            n2 = n3 + 2;
            if (n == 247) {
                n = this.readFrameType(context.stack, 0, n2, arrc, arrlabel);
                context.mode = 4;
                context.stackCount = 1;
                n3 = n5;
            } else if (n >= 248 && n < 251) {
                context.mode = 2;
                context.localDiff = 251 - n;
                context.localCount -= context.localDiff;
                context.stackCount = 0;
                n = n2;
                n3 = n5;
            } else if (n == 251) {
                context.mode = 3;
                context.stackCount = 0;
                n = n2;
                n3 = n5;
            } else if (n < 255) {
                n3 = bl2 ? context.localCount : 0;
                int n6 = n - 251;
                n4 = n3;
                n3 = n2;
                n2 = n4;
                n4 = n6;
                while (n4 > 0) {
                    n3 = this.readFrameType(context.local, n2, n3, arrc, arrlabel);
                    --n4;
                    ++n2;
                }
                context.mode = 1;
                context.localDiff = n - 251;
                context.localCount += context.localDiff;
                context.stackCount = 0;
                n = n3;
                n3 = n5;
            } else {
                context.mode = 0;
                context.localDiff = n3 = this.readUnsignedShort(n2);
                context.localCount = n3;
                n = n2 + 2;
                n2 = n3;
                n3 = 0;
                while (n2 > 0) {
                    n = this.readFrameType(context.local, n3, n, arrc, arrlabel);
                    --n2;
                    ++n3;
                }
                n2 = this.readUnsignedShort(n);
                n += 2;
                context.stackCount = n2;
                n3 = n4;
                do {
                    n4 = n3;
                    if (n2 <= 0) break;
                    Object[] arrobject = context.stack;
                    n3 = n4 + 1;
                    n = this.readFrameType(arrobject, n4, n, arrc, arrlabel);
                    --n2;
                } while (true);
                n3 = n5;
            }
        }
        context.offset += n3 + 1;
        this.readLabel(context.offset, arrlabel);
        return n;
    }

    private int readFrameType(Object[] arrobject, int n, int n2, char[] arrc, Label[] arrlabel) {
        byte[] arrby = this.b;
        int n3 = n2 + 1;
        switch (arrby[n2] & 255) {
            default: {
                arrobject[n] = this.readLabel(this.readUnsignedShort(n3), arrlabel);
                return n3 + 2;
            }
            case 7: {
                arrobject[n] = this.readClass(n3, arrc);
                return n3 + 2;
            }
            case 6: {
                arrobject[n] = Opcodes.UNINITIALIZED_THIS;
                return n3;
            }
            case 5: {
                arrobject[n] = Opcodes.NULL;
                return n3;
            }
            case 4: {
                arrobject[n] = Opcodes.LONG;
                return n3;
            }
            case 3: {
                arrobject[n] = Opcodes.DOUBLE;
                return n3;
            }
            case 2: {
                arrobject[n] = Opcodes.FLOAT;
                return n3;
            }
            case 1: {
                arrobject[n] = Opcodes.INTEGER;
                return n3;
            }
            case 0: 
        }
        arrobject[n] = Opcodes.TOP;
        return n3;
    }

    private int readMethod(ClassVisitor object, Context context, int n) {
        int n2;
        int n3;
        Object object2;
        char[] arrc = context.buffer;
        context.access = this.readUnsignedShort(n);
        context.name = this.readUTF8(n + 2, arrc);
        context.desc = this.readUTF8(n + 4, arrc);
        int n4 = n + 6;
        int n5 = 0;
        int n6 = 0;
        int n7 = n4;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        Object object3 = null;
        String[] arrstring = null;
        int n12 = 0;
        Object object4 = null;
        n = 0;
        int n13 = 0;
        int n14 = 0;
        for (n3 = this.readUnsignedShort((int)n4); n3 > 0; --n3) {
            object2 = this.readUTF8(n7 + 2, arrc);
            if ("Code".equals(object2)) {
                if ((context.flags & 1) == 0) {
                    n8 = n7 + 8;
                }
            } else if ("Exceptions".equals(object2)) {
                arrstring = new String[this.readUnsignedShort(n7 + 8)];
                n10 = n7 + 10;
                for (n2 = 0; n2 < arrstring.length; ++n2) {
                    arrstring[n2] = this.readClass(n10, arrc);
                    n10 += 2;
                }
            } else if ("Signature".equals(object2)) {
                object4 = this.readUTF8(n7 + 8, arrc);
            } else if ("Deprecated".equals(object2)) {
                context.access |= 131072;
            } else if ("RuntimeVisibleAnnotations".equals(object2)) {
                n13 = n7 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(object2)) {
                n6 = n7 + 8;
            } else if ("AnnotationDefault".equals(object2)) {
                n12 = n7 + 8;
            } else if ("Synthetic".equals(object2)) {
                context.access |= 266240;
            } else if ("RuntimeInvisibleAnnotations".equals(object2)) {
                n14 = n7 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(object2)) {
                n = n7 + 8;
            } else if ("RuntimeVisibleParameterAnnotations".equals(object2)) {
                n11 = n7 + 8;
            } else if ("RuntimeInvisibleParameterAnnotations".equals(object2)) {
                n9 = n7 + 8;
            } else if ("MethodParameters".equals(object2)) {
                n5 = n7 + 8;
            } else if ((object2 = this.readAttribute(context.attrs, (String)object2, n7 + 8, this.readInt(n7 + 4), arrc, -1, null)) != null) {
                object2.next = object3;
                object3 = object2;
            }
            n7 += this.readInt(n7 + 4) + 6;
        }
        int n15 = n7 + 2;
        object2 = object.visitMethod(context.access, context.name, context.desc, (String)object4, arrstring);
        if (object2 == null) {
            return n15;
        }
        if (object2 instanceof MethodWriter) {
            object = (MethodWriter)object2;
            if (object.cw.cr == this && object4 == object.signature) {
                n3 = 0;
                if (arrstring == null) {
                    n10 = object.exceptionCount == 0 ? 1 : 0;
                    n3 = n10;
                } else if (arrstring.length == object.exceptionCount) {
                    n2 = 1;
                    n3 = arrstring.length - 1;
                    n7 = n10;
                    n10 = n3;
                    do {
                        n3 = n2;
                        if (n10 < 0) break;
                        if (object.exceptions[n10] != this.readUnsignedShort(n7 -= 2)) {
                            n3 = 0;
                            break;
                        }
                        --n10;
                    } while (true);
                }
                if (n3 != 0) {
                    object.classReaderOffset = n4;
                    object.classReaderLength = n15 - n4;
                    return n15;
                }
            }
        }
        n10 = n4;
        if (n5 != 0) {
            n3 = this.b[n5] & 255;
            ++n5;
            while (n3 > 0) {
                object2.visitParameter(this.readUTF8(n5, arrc), this.readUnsignedShort(n5 + 2));
                --n3;
                n5 += 4;
            }
        }
        if (n12 != 0) {
            object = object2.visitAnnotationDefault();
            this.readAnnotationValue(n12, arrc, null, (AnnotationVisitor)object);
            if (object != null) {
                object.visitEnd();
            }
        }
        if (n13 != 0) {
            n3 = this.readUnsignedShort(n13);
            n5 = n13 + 2;
            for (n13 = n3; n13 > 0; --n13) {
                n5 = this.readAnnotationValues(n5 + 2, arrc, true, object2.visitAnnotation(this.readUTF8(n5, arrc), true));
            }
        }
        if (n14 != 0) {
            n5 = this.readUnsignedShort(n14);
            n13 = n14 + 2;
            for (n14 = n5; n14 > 0; --n14) {
                n13 = this.readAnnotationValues(n13 + 2, arrc, true, object2.visitAnnotation(this.readUTF8(n13, arrc), false));
            }
        }
        n13 = n10;
        n13 = n6;
        if (n6 != 0) {
            n14 = this.readUnsignedShort(n6);
            n12 = n6 + 2;
            do {
                n13 = n10;
                n13 = n6;
                if (n14 <= 0) break;
                n12 = this.readAnnotationTarget(context, n12);
                n12 = this.readAnnotationValues(n12 + 2, arrc, true, object2.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(n12, arrc), true));
                --n14;
            } while (true);
        }
        n12 = n;
        if (n != 0) {
            n6 = this.readUnsignedShort(n);
            n14 = n + 2;
            do {
                n12 = n;
                if (n6 <= 0) break;
                n14 = this.readAnnotationTarget(context, n14);
                n14 = this.readAnnotationValues(n14 + 2, arrc, true, object2.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(n14, arrc), false));
                --n6;
            } while (true);
        }
        if (n11 != 0) {
            this.readParameterAnnotations((MethodVisitor)object2, context, n11, true);
        }
        object = object3;
        if (n9 != 0) {
            this.readParameterAnnotations((MethodVisitor)object2, context, n9, false);
            object = object3;
        }
        while (object != null) {
            object4 = object.next;
            object.next = null;
            object2.visitAttribute((Attribute)object);
            object = object4;
        }
        if (n8 != 0) {
            object2.visitCode();
            this.readCode((MethodVisitor)object2, context, n8);
        }
        object2.visitEnd();
        return n15;
    }

    private void readParameterAnnotations(MethodVisitor methodVisitor, Context arrc, int n, boolean bl) {
        Object object = this.b;
        int n2 = n + 1;
        int n3 = object[n] & 255;
        int n4 = Type.getArgumentTypes(arrc.desc).length - n3;
        for (n = 0; n < n4; ++n) {
            object = methodVisitor.visitParameterAnnotation(n, "Ljava/lang/Synthetic;", false);
            if (object == null) continue;
            object.visitEnd();
        }
        arrc = arrc.buffer;
        int n5 = n;
        n = n2;
        while (n5 < n3 + n4) {
            n2 = this.readUnsignedShort(n);
            n += 2;
            while (n2 > 0) {
                n = this.readAnnotationValues(n + 2, arrc, true, methodVisitor.visitParameterAnnotation(n5, this.readUTF8(n, arrc), bl));
                --n2;
            }
            ++n5;
        }
    }

    private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int n, boolean bl) {
        char[] arrc = context.buffer;
        int[] arrn = new int[this.readUnsignedShort(n)];
        n += 2;
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = n;
            int n2 = this.readInt(n);
            int n3 = n2 >>> 24;
            block0 : switch (n3) {
                default: {
                    switch (n3) {
                        default: {
                            switch (n3) {
                                default: {
                                    switch (n3) {
                                        default: {
                                            n += 3;
                                            break block0;
                                        }
                                        case 71: 
                                        case 72: 
                                        case 73: 
                                        case 74: 
                                        case 75: 
                                    }
                                    n += 4;
                                    break block0;
                                }
                                case 64: 
                                case 65: 
                            }
                            int n4 = this.readUnsignedShort(n + 1);
                            n3 = n;
                            for (n = n4; n > 0; --n) {
                                n4 = this.readUnsignedShort(n3 + 3);
                                int n5 = this.readUnsignedShort(n3 + 5);
                                this.readLabel(n4, context.labels);
                                this.readLabel(n4 + n5, context.labels);
                                n3 += 6;
                            }
                            n = n3 + 3;
                            break block0;
                        }
                        case 19: 
                        case 20: 
                        case 21: {
                            ++n;
                            break block0;
                        }
                        case 22: 
                    }
                }
                case 0: 
                case 1: {
                    n += 2;
                }
            }
            n3 = this.readByte(n);
            TypePath typePath = null;
            if (n2 >>> 24 == 66) {
                if (n3 != 0) {
                    typePath = new TypePath(this.b, n);
                }
                n += n3 * 2 + 1;
                n = this.readAnnotationValues(n + 2, arrc, true, methodVisitor.visitTryCatchAnnotation(n2, typePath, this.readUTF8(n, arrc), bl));
                continue;
            }
            n = this.readAnnotationValues(n + 3 + n3 * 2, arrc, true, null);
        }
        return arrn;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private String readUTF(int var1_1, int var2_2, char[] var3_3) {
        var10_4 = this.b;
        var4_5 = 0;
        var7_6 = 0;
        var6_7 = var1_1;
        var8_8 = 0;
        while (var6_7 < var1_1 + var2_2) {
            block7 : {
                var9_10 = var10_4[var6_7];
                switch (var4_5) {
                    default: {
                        var5_9 = var8_8;
                        ** break;
                    }
                    case 2: {
                        var5_9 = (char)(var8_8 << 6 | var9_10 & 63);
                        var4_5 = 1;
                        ** break;
                    }
                    case 1: {
                        var5_9 = var7_6 + 1;
                        var3_3[var7_6] = (char)(var8_8 << 6 | var9_10 & 63);
                        var4_5 = 0;
                        break;
                    }
                    case 0: {
                        if ((var9_10 &= 255) >= 128) break block7;
                        var5_9 = var7_6 + 1;
                        var3_3[var7_6] = (char)var9_10;
                    }
                }
                var7_6 = var5_9;
                var5_9 = var8_8;
                ** break;
            }
            if (var9_10 < 224 && var9_10 > 191) {
                var5_9 = (char)(var9_10 & 31);
                var4_5 = 1;
                ** break;
            }
            var5_9 = (char)(var9_10 & 15);
            var4_5 = 2;
lbl35: // 5 sources:
            ++var6_7;
            var8_8 = var5_9;
        }
        return new String(var3_3, 0, var7_6);
    }

    public void accept(ClassVisitor classVisitor, int n) {
        this.accept(classVisitor, new Attribute[0], n);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void accept(ClassVisitor var1_1, Attribute[] var2_2, int var3_3) {
        var4_4 = this.header;
        var24_5 = new char[this.maxStringLength];
        var23_6 = new Context();
        var23_6.attrs = var2_2;
        var23_6.flags = var3_3;
        var23_6.buffer = var24_5;
        var9_7 = this.readUnsignedShort(var4_4);
        var25_8 = this.readClass(var4_4 + 2, var24_5);
        var26_9 = this.readClass(var4_4 + 4, var24_5);
        var22_10 = new String[this.readUnsignedShort(var4_4 + 6)];
        var5_11 = var4_4 + 8;
        for (var4_4 = 0; var4_4 < var22_10.length; var5_11 += 2, ++var4_4) {
            var22_10[var4_4] = this.readClass(var5_11, var24_5);
        }
        var11_12 = this.getAttributes();
        var8_14 = 0;
        var7_15 = 0;
        var6_16 = 0;
        var5_11 = 0;
        var19_17 = null;
        var20_18 = null;
        var15_19 = null;
        var14_20 = null;
        var18_21 = null;
        var16_22 = null;
        var17_23 = null;
        var4_4 = 0;
        for (var10_13 = this.readUnsignedShort((int)var11_12); var10_13 > 0; var11_12 += this.readInt((int)(var11_12 + 4)) + 6, --var10_13) {
            block32 : {
                var27_27 = this.readUTF8(var11_12 + 2, var24_5);
                if ("SourceFile".equals(var27_27)) {
                    var16_22 = this.readUTF8(var11_12 + 8, var24_5);
                    continue;
                }
                if ("InnerClasses".equals(var27_27)) {
                    var4_4 = var11_12 + 8;
                    continue;
                }
                if ("EnclosingMethod".equals(var27_27)) {
                    var18_21 = this.readClass(var11_12 + 8, var24_5);
                    var12_24 = this.readUnsignedShort(var11_12 + 10);
                    if (var12_24 == 0) continue;
                    var14_20 = this.readUTF8(this.items[var12_24], var24_5);
                    var15_19 = this.readUTF8(this.items[var12_24] + 2, var24_5);
                    continue;
                }
                if ("Signature".equals(var27_27)) {
                    var20_18 = this.readUTF8(var11_12 + 8, var24_5);
                    continue;
                }
                if ("RuntimeVisibleAnnotations".equals(var27_27)) {
                    var8_14 = var11_12 + 8;
                    continue;
                }
                if ("RuntimeVisibleTypeAnnotations".equals(var27_27)) {
                    var6_16 = var11_12 + 8;
                    continue;
                }
                if ("Deprecated".equals(var27_27)) {
                    var9_7 |= 131072;
                    continue;
                }
                if ("Synthetic".equals(var27_27)) {
                    var9_7 |= 266240;
                    continue;
                }
                if ("SourceDebugExtension".equals(var27_27)) {
                    var12_24 = this.readInt(var11_12 + 4);
                    var17_23 = this.readUTF(var11_12 + 8, var12_24, new char[var12_24]);
                    continue;
                }
                var21_26 = var14_20;
                if ("RuntimeInvisibleAnnotations".equals(var27_27)) {
                    var7_15 = var11_12 + 8;
                    continue;
                }
                if ("RuntimeInvisibleTypeAnnotations".equals(var27_27)) {
                    var5_11 = var11_12 + 8;
                    continue;
                }
                if (!"BootstrapMethods".equals(var27_27)) break block32;
                var21_26 = new int[this.readUnsignedShort(var11_12 + 8)];
                var13_25 = var11_12 + 10;
                for (var12_24 = 0; var12_24 < var21_26.length; var13_25 += this.readUnsignedShort((int)(var13_25 + 2)) + 2 << 1, ++var12_24) {
                    var21_26[var12_24] = var13_25;
                }
                var23_6.bootstrapMethods = var21_26;
                var21_26 = var18_21;
                ** GOTO lbl88
            }
            var12_24 = this.readInt(var11_12 + 4);
            var21_26 = this.readAttribute((Attribute[])var2_2, var27_27, var11_12 + 8, var12_24, var24_5, -1, null);
            if (var21_26 != null) {
                var21_26.next = var19_17;
                var19_17 = var21_26;
                var21_26 = var18_21;
                var18_21 = var19_17;
            } else {
                var21_26 = var18_21;
lbl88: // 2 sources:
                var18_21 = var19_17;
            }
            var19_17 = var18_21;
            var18_21 = var21_26;
        }
        var1_1.visit(this.readInt(this.items[1] - 7), var9_7, var25_8, var20_18, var26_9, var22_10);
        if ((var3_3 & 2) == 0 && ((var2_2 = var16_22) != null || var17_23 != null)) {
            var1_1.visitSource((String)var2_2, var17_23);
        }
        if (var18_21 != null) {
            var1_1.visitOuterClass((String)var18_21, (String)var14_20, var15_19);
        }
        var10_13 = var8_14;
        var2_2 = var14_20;
        if (var8_14 != 0) {
            var3_3 = this.readUnsignedShort(var8_14);
            var9_7 = var8_14 + 2;
            do {
                var10_13 = var8_14;
                var2_2 = var14_20;
                if (var3_3 <= 0) break;
                var9_7 = this.readAnnotationValues(var9_7 + 2, var24_5, true, var1_1.visitAnnotation(this.readUTF8(var9_7, var24_5), true));
                --var3_3;
            } while (true);
        }
        var9_7 = var7_15;
        if (var7_15 != 0) {
            var3_3 = this.readUnsignedShort(var7_15);
            var8_14 = var7_15 + 2;
            do {
                var9_7 = var7_15;
                if (var3_3 <= 0) break;
                var8_14 = this.readAnnotationValues(var8_14 + 2, var24_5, true, var1_1.visitAnnotation(this.readUTF8(var8_14, var24_5), false));
                --var3_3;
            } while (true);
        }
        var8_14 = var3_3 = var6_16;
        var2_2 = var16_22;
        var2_2 = var17_23;
        if (var3_3 != 0) {
            var6_16 = this.readUnsignedShort(var3_3);
            var7_15 = var3_3 + 2;
            do {
                var8_14 = var3_3;
                var2_2 = var16_22;
                var2_2 = var17_23;
                if (var6_16 <= 0) break;
                var7_15 = this.readAnnotationTarget(var23_6, var7_15);
                var7_15 = this.readAnnotationValues(var7_15 + 2, var24_5, true, var1_1.visitTypeAnnotation(var23_6.typeRef, var23_6.typePath, this.readUTF8(var7_15, var24_5), true));
                --var6_16;
            } while (true);
        }
        var7_15 = var3_3 = var5_11;
        if (var3_3 != 0) {
            var5_11 = this.readUnsignedShort(var3_3);
            var6_16 = var3_3 + 2;
            do {
                var7_15 = var3_3;
                if (var5_11 <= 0) break;
                var6_16 = this.readAnnotationTarget(var23_6, var6_16);
                var6_16 = this.readAnnotationValues(var6_16 + 2, var24_5, true, var1_1.visitTypeAnnotation(var23_6.typeRef, var23_6.typePath, this.readUTF8(var6_16, var24_5), false));
                --var5_11;
            } while (true);
        }
        while (var19_17 != null) {
            var2_2 = var19_17.next;
            var19_17.next = null;
            var1_1.visitAttribute((Attribute)var19_17);
            var19_17 = var2_2;
        }
        var6_16 = var3_3 = var4_4;
        if (var3_3 != 0) {
            var5_11 = var3_3 + 2;
            var4_4 = this.readUnsignedShort(var3_3);
            do {
                var6_16 = var3_3;
                if (var4_4 <= 0) break;
                var1_1.visitInnerClass(this.readClass(var5_11, var24_5), this.readClass(var5_11 + 2, var24_5), this.readUTF8(var5_11 + 4, var24_5), this.readUnsignedShort(var5_11 + 6));
                var5_11 += 8;
                --var4_4;
            } while (true);
        }
        var4_4 = this.header + 10 + var22_10.length * 2;
        for (var3_3 = this.readUnsignedShort((int)(var4_4 - 2)); var3_3 > 0; --var3_3) {
            var4_4 = this.readField(var1_1, var23_6, var4_4);
        }
        var3_3 = this.readUnsignedShort((var4_4 += 2) - 2);
        do {
            if (var3_3 <= 0) {
                var1_1.visitEnd();
                return;
            }
            var4_4 = this.readMethod(var1_1, var23_6, var4_4);
            --var3_3;
        } while (true);
    }

    void copyPool(ClassWriter classWriter) {
        int n;
        char[] arrc = new char[this.maxStringLength];
        int n2 = this.items.length;
        Item[] arritem = new Item[n2];
        for (n = 1; n < n2; ++n) {
            int n3 = this.items[n];
            int n4 = this.b[n3 - 1];
            Item item = new Item(n);
            if (n4 != 1) {
                int n5;
                if (n4 != 15) {
                    if (n4 != 18) {
                        block0 : switch (n4) {
                            default: {
                                switch (n4) {
                                    default: {
                                        item.set(n4, this.readUTF8(n3, arrc), null, null);
                                        break block0;
                                    }
                                    case 12: {
                                        item.set(n4, this.readUTF8(n3, arrc), this.readUTF8(n3 + 2, arrc), null);
                                        break block0;
                                    }
                                    case 9: 
                                    case 10: 
                                    case 11: 
                                }
                                n5 = this.items[this.readUnsignedShort(n3 + 2)];
                                item.set(n4, this.readClass(n3, arrc), this.readUTF8(n5, arrc), this.readUTF8(n5 + 2, arrc));
                                break;
                            }
                            case 6: {
                                item.set(Double.longBitsToDouble(this.readLong(n3)));
                                ++n;
                                break;
                            }
                            case 5: {
                                item.set(this.readLong(n3));
                                ++n;
                                break;
                            }
                            case 4: {
                                item.set(Float.intBitsToFloat(this.readInt(n3)));
                                break;
                            }
                            case 3: {
                                item.set(this.readInt(n3));
                                break;
                            }
                        }
                    } else {
                        if (classWriter.bootstrapMethods == null) {
                            this.copyBootstrapMethods(classWriter, arritem, arrc);
                        }
                        n4 = this.items[this.readUnsignedShort(n3 + 2)];
                        item.set(this.readUTF8(n4, arrc), this.readUTF8(n4 + 2, arrc), this.readUnsignedShort(n3));
                    }
                } else {
                    n4 = this.items[this.readUnsignedShort(n3 + 1)];
                    n5 = this.items[this.readUnsignedShort(n4 + 2)];
                    item.set(this.readByte(n3) + 20, this.readClass(n4, arrc), this.readUTF8(n5, arrc), this.readUTF8(n5 + 2, arrc));
                }
            } else {
                String[] arrstring = this.strings[n];
                Object object = arrstring;
                if (arrstring == null) {
                    n3 = this.items[n];
                    arrstring = this.strings;
                    arrstring[n] = object = this.readUTF(n3 + 2, this.readUnsignedShort(n3), arrc);
                }
                item.set(n4, (String)object, null, null);
            }
            n3 = item.hashCode % arritem.length;
            item.next = arritem[n3];
            arritem[n3] = item;
        }
        n = this.items[1] - 1;
        classWriter.pool.putByteArray(this.b, n, this.header - n);
        classWriter.items = arritem;
        classWriter.threshold = (int)((double)n2 * 0.75);
        classWriter.index = n2;
    }

    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }

    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int n = this.header + 6;
        int n2 = this.readUnsignedShort(n);
        String[] arrstring = new String[n2];
        if (n2 > 0) {
            char[] arrc = new char[this.maxStringLength];
            for (int i = 0; i < n2; ++i) {
                arrstring[i] = this.readClass(n += 2, arrc);
            }
        }
        return arrstring;
    }

    public int getItem(int n) {
        return this.items[n];
    }

    public int getItemCount() {
        return this.items.length;
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public String getSuperName() {
        return this.readClass(this.header + 4, new char[this.maxStringLength]);
    }

    public int readByte(int n) {
        return this.b[n] & 255;
    }

    public String readClass(int n, char[] arrc) {
        return this.readUTF8(this.items[this.readUnsignedShort(n)], arrc);
    }

    public Object readConst(int n, char[] arrc) {
        int n2 = this.items[n];
        n = this.b[n2 - 1];
        if (n != 16) {
            switch (n) {
                default: {
                    n = this.readByte(n2);
                    int[] arrn = this.items;
                    n2 = arrn[this.readUnsignedShort(n2 + 1)];
                    boolean bl = this.b[n2 - 1] == 11;
                    String string2 = this.readClass(n2, arrc);
                    n2 = arrn[this.readUnsignedShort(n2 + 2)];
                    return new Handle(n, string2, this.readUTF8(n2, arrc), this.readUTF8(n2 + 2, arrc), bl);
                }
                case 8: {
                    return this.readUTF8(n2, arrc);
                }
                case 7: {
                    return Type.getObjectType(this.readUTF8(n2, arrc));
                }
                case 6: {
                    return Double.longBitsToDouble(this.readLong(n2));
                }
                case 5: {
                    return this.readLong(n2);
                }
                case 4: {
                    return Float.valueOf(Float.intBitsToFloat(this.readInt(n2)));
                }
                case 3: 
            }
            return this.readInt(n2);
        }
        return Type.getMethodType(this.readUTF8(n2, arrc));
    }

    public int readInt(int n) {
        byte[] arrby = this.b;
        return (arrby[n] & 255) << 24 | (arrby[n + 1] & 255) << 16 | (arrby[n + 2] & 255) << 8 | arrby[n + 3] & 255;
    }

    protected Label readLabel(int n, Label[] arrlabel) {
        if (arrlabel[n] == null) {
            arrlabel[n] = new Label();
        }
        return arrlabel[n];
    }

    public long readLong(int n) {
        return (long)this.readInt(n) << 32 | (long)this.readInt(n + 4) & 0xFFFFFFFFL;
    }

    public short readShort(int n) {
        byte[] arrby = this.b;
        return (short)((arrby[n] & 255) << 8 | arrby[n + 1] & 255);
    }

    public String readUTF8(int n, char[] object) {
        int n2 = this.readUnsignedShort(n);
        if (n != 0 && n2 != 0) {
            String[] arrstring = this.strings[n2];
            if (arrstring != null) {
                return arrstring;
            }
            n = this.items[n2];
            arrstring = this.strings;
            arrstring[n2] = object = this.readUTF(n + 2, this.readUnsignedShort(n), (char[])object);
            return object;
        }
        return null;
    }

    public int readUnsignedShort(int n) {
        byte[] arrby = this.b;
        return (arrby[n] & 255) << 8 | arrby[n + 1] & 255;
    }
}

