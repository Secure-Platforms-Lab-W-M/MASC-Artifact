// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

import java.io.IOException;
import java.io.InputStream;

public class ClassReader
{
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
    
    public ClassReader(final InputStream inputStream) throws IOException {
        this(readClass(inputStream, false));
    }
    
    public ClassReader(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace('.', '/'));
        sb.append(".class");
        this(readClass(ClassLoader.getSystemResourceAsStream(sb.toString()), true));
    }
    
    public ClassReader(final byte[] array) {
        this(array, 0, array.length);
    }
    
    public ClassReader(final byte[] b, int n, int i) {
        this.b = b;
        if (this.readShort(n + 6) <= 52) {
            this.items = new int[this.readUnsignedShort(n + 8)];
            final int length = this.items.length;
            this.strings = new String[length];
            int header = n + 10;
            int maxStringLength = 0;
            byte b2;
            int n2 = 0;
            int n3 = 0;
            int n4;
            for (i = 1; i < length; i = n2 + 1, maxStringLength = n3) {
                this.items[i] = header + 1;
                b2 = b[header];
                n = 3;
                Label_0260: {
                    if (b2 != 1) {
                        if (b2 != 15) {
                            Label_0197: {
                                if (b2 != 18) {
                                    switch (b2) {
                                        default: {
                                            switch (b2) {
                                                default: {
                                                    n2 = i;
                                                    n3 = maxStringLength;
                                                    break Label_0260;
                                                }
                                                case 9:
                                                case 10:
                                                case 11:
                                                case 12: {
                                                    break Label_0197;
                                                }
                                            }
                                            break;
                                        }
                                        case 5:
                                        case 6: {
                                            n = 9;
                                            n2 = i + 1;
                                            n3 = maxStringLength;
                                            break Label_0260;
                                        }
                                        case 3:
                                        case 4: {
                                            break;
                                        }
                                    }
                                }
                            }
                            n = 5;
                            n2 = i;
                            n3 = maxStringLength;
                        }
                        else {
                            n = 4;
                            n2 = i;
                            n3 = maxStringLength;
                        }
                    }
                    else {
                        n4 = 3 + this.readUnsignedShort(header + 1);
                        n2 = i;
                        if ((n = n4) > (n3 = maxStringLength)) {
                            n3 = n4;
                            n = n4;
                            n2 = i;
                        }
                    }
                }
                header += n;
            }
            this.maxStringLength = maxStringLength;
            this.header = header;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private void copyBootstrapMethods(final ClassWriter classWriter, final Item[] array, final char[] array2) {
        int attributes = this.getAttributes();
        final boolean b = false;
        int unsignedShort = this.readUnsignedShort(attributes);
        boolean b2;
        while (true) {
            b2 = b;
            if (unsignedShort <= 0) {
                break;
            }
            if ("BootstrapMethods".equals(this.readUTF8(attributes + 2, array2))) {
                b2 = true;
                break;
            }
            attributes += this.readInt(attributes + 4) + 6;
            --unsignedShort;
        }
        if (!b2) {
            return;
        }
        final int unsignedShort2 = this.readUnsignedShort(attributes + 8);
        int n = 0;
        int n2 = attributes + 10;
        while (true) {
            final int n3 = n2;
            if (n >= unsignedShort2) {
                break;
            }
            int hashCode = this.readConst(this.readUnsignedShort(n3), array2).hashCode();
            int i = this.readUnsignedShort(n3 + 2);
            int n4 = n3;
            while (i > 0) {
                hashCode ^= this.readConst(this.readUnsignedShort(n4 + 4), array2).hashCode();
                n4 += 2;
                --i;
            }
            n2 = n4 + 4;
            final Item item = new Item(n);
            item.set(n3 - attributes - 10, Integer.MAX_VALUE & hashCode);
            final int n5 = item.hashCode % array.length;
            item.next = array[n5];
            array[n5] = item;
            ++n;
        }
        final int int1 = this.readInt(attributes + 4);
        final ByteVector bootstrapMethods = new ByteVector(int1 + 62);
        bootstrapMethods.putByteArray(this.b, attributes + 10, int1 - 2);
        classWriter.bootstrapMethodsCount = unsignedShort2;
        classWriter.bootstrapMethods = bootstrapMethods;
    }
    
    private int getAttributes() {
        int n = this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        for (int i = this.readUnsignedShort(n); i > 0; --i) {
            for (int j = this.readUnsignedShort(n + 8); j > 0; --j) {
                n += this.readInt(n + 12) + 6;
            }
            n += 8;
        }
        int n2 = n + 2;
        for (int k = this.readUnsignedShort(n2); k > 0; --k) {
            for (int l = this.readUnsignedShort(n2 + 8); l > 0; --l) {
                n2 += this.readInt(n2 + 12) + 6;
            }
            n2 += 8;
        }
        return n2 + 2;
    }
    
    private void getImplicitFrame(final Context context) {
        final String desc = context.desc;
        final Object[] local = context.local;
        int n;
        if ((context.access & 0x8) == 0x0) {
            if ("<init>".equals(context.name)) {
                n = 0 + 1;
                local[0] = Opcodes.UNINITIALIZED_THIS;
            }
            else {
                n = 0 + 1;
                local[0] = this.readClass(this.header + 2, context.buffer);
            }
        }
        else {
            n = 0;
        }
        int n2 = 1;
        int localCount = n;
        while (true) {
            int n5;
            int n4;
            int n3 = n4 = (n5 = n2 + 1);
            int n8 = 0;
            switch (desc.charAt(n2)) {
                default: {
                    context.localCount = localCount;
                    return;
                }
                case '[': {
                    while (desc.charAt(n5) == '[') {
                        ++n5;
                    }
                    int n6 = n5;
                    if (desc.charAt(n5) == 'L') {
                        int n7 = n5 + 1;
                        while (true) {
                            n6 = n7;
                            if (desc.charAt(n7) == ';') {
                                break;
                            }
                            ++n7;
                        }
                    }
                    n8 = localCount + 1;
                    n3 = n6 + 1;
                    local[localCount] = desc.substring(n2, n3);
                    break;
                }
                case 'L': {
                    while (desc.charAt(n4) != ';') {
                        ++n4;
                    }
                    local[localCount] = desc.substring(n2 + 1, n4);
                    n8 = localCount + 1;
                    n3 = n4 + 1;
                    break;
                }
                case 'J': {
                    n8 = localCount + 1;
                    local[localCount] = Opcodes.LONG;
                    break;
                }
                case 'F': {
                    n8 = localCount + 1;
                    local[localCount] = Opcodes.FLOAT;
                    break;
                }
                case 'D': {
                    n8 = localCount + 1;
                    local[localCount] = Opcodes.DOUBLE;
                    break;
                }
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z': {
                    n8 = localCount + 1;
                    local[localCount] = Opcodes.INTEGER;
                    break;
                }
            }
            n2 = n3;
            localCount = n8;
        }
    }
    
    private int readAnnotationTarget(final Context context, int i) {
        final int int1 = this.readInt(i);
        final int n = int1 >>> 24;
        int n5 = 0;
        Label_0342: {
            switch (n) {
                default: {
                    int n2 = -16777216;
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            if (int1 >>> 24 < 67) {
                                                n2 = -256;
                                            }
                                            final int n3 = int1 & n2;
                                            final int n4 = i + 3;
                                            i = n3;
                                            n5 = n4;
                                            break Label_0342;
                                        }
                                        case 71:
                                        case 72:
                                        case 73:
                                        case 74:
                                        case 75: {
                                            final int n6 = int1 & 0xFF0000FF;
                                            final int n7 = i + 4;
                                            i = n6;
                                            n5 = n7;
                                            break Label_0342;
                                        }
                                    }
                                    break;
                                }
                                case 64:
                                case 65: {
                                    final int n8 = int1 & 0xFF000000;
                                    final int unsignedShort = this.readUnsignedShort(i + 1);
                                    context.start = new Label[unsignedShort];
                                    context.end = new Label[unsignedShort];
                                    context.index = new int[unsignedShort];
                                    n5 = i + 3;
                                    int unsignedShort2;
                                    int unsignedShort3;
                                    for (i = 0; i < unsignedShort; ++i) {
                                        unsignedShort2 = this.readUnsignedShort(n5);
                                        unsignedShort3 = this.readUnsignedShort(n5 + 2);
                                        context.start[i] = this.readLabel(unsignedShort2, context.labels);
                                        context.end[i] = this.readLabel(unsignedShort2 + unsignedShort3, context.labels);
                                        context.index[i] = this.readUnsignedShort(n5 + 4);
                                        n5 += 6;
                                    }
                                    i = n8;
                                    break Label_0342;
                                }
                            }
                            break;
                        }
                        case 19:
                        case 20:
                        case 21: {
                            final int n9 = int1 & 0xFF000000;
                            final int n10 = i + 1;
                            i = n9;
                            n5 = n10;
                            break Label_0342;
                        }
                        case 22: {
                            break Label_0342;
                        }
                    }
                    break;
                }
                case 0:
                case 1: {
                    final int n11 = int1 & 0xFFFF0000;
                    n5 = i + 2;
                    i = n11;
                    break;
                }
            }
        }
        final int byte1 = this.readByte(n5);
        context.typeRef = i;
        TypePath typePath;
        if (byte1 == 0) {
            typePath = null;
        }
        else {
            typePath = new TypePath(this.b, n5);
        }
        context.typePath = typePath;
        return n5 + 1 + byte1 * 2;
    }
    
    private int readAnnotationValue(int i, char[] array, final String s, final AnnotationVisitor annotationVisitor) {
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        final int n5 = 0;
        final int n6 = 0;
        final int n7 = 0;
        if (annotationVisitor == null) {
            final int n8 = this.b[i] & 0xFF;
            if (n8 == 64) {
                return this.readAnnotationValues(i + 3, array, true, null);
            }
            if (n8 == 91) {
                return this.readAnnotationValues(i + 1, array, false, null);
            }
            if (n8 != 101) {
                return i + 3;
            }
            return i + 5;
        }
        else {
            final byte[] b = this.b;
            final int n9 = i + 1;
            switch (b[i] & 0xFF) {
                default: {
                    return n9;
                }
                case 115: {
                    annotationVisitor.visit(s, this.readUTF8(n9, array));
                    return n9 + 2;
                }
                case 101: {
                    annotationVisitor.visitEnum(s, this.readUTF8(n9, array), this.readUTF8(n9 + 2, array));
                    return n9 + 4;
                }
                case 99: {
                    annotationVisitor.visit(s, Type.getType(this.readUTF8(n9, array)));
                    return n9 + 2;
                }
                case 91: {
                    final int unsignedShort = this.readUnsignedShort(n9);
                    final int n10 = n9 + 2;
                    if (unsignedShort == 0) {
                        return this.readAnnotationValues(n10 - 2, array, false, annotationVisitor.visitArray(s));
                    }
                    final byte[] b2 = this.b;
                    i = n10 + 1;
                    final int n11 = b2[n10] & 0xFF;
                    if (n11 == 70) {
                        final float[] array2 = new float[unsignedShort];
                        int n12 = i;
                        for (i = n6; i < unsignedShort; ++i) {
                            array2[i] = Float.intBitsToFloat(this.readInt(this.items[this.readUnsignedShort(n12)]));
                            n12 += 3;
                        }
                        annotationVisitor.visit(s, array2);
                        return n12 - 1;
                    }
                    if (n11 == 83) {
                        final short[] array3 = new short[unsignedShort];
                        int n13 = i;
                        for (i = n5; i < unsignedShort; ++i) {
                            array3[i] = (short)this.readInt(this.items[this.readUnsignedShort(n13)]);
                            n13 += 3;
                        }
                        annotationVisitor.visit(s, array3);
                        return n13 - 1;
                    }
                    if (n11 == 90) {
                        final boolean[] array4 = new boolean[unsignedShort];
                        final int n14 = 0;
                        int n15 = i;
                        for (i = n14; i < unsignedShort; ++i) {
                            array4[i] = (this.readInt(this.items[this.readUnsignedShort(n15)]) != 0);
                            n15 += 3;
                        }
                        annotationVisitor.visit(s, array4);
                        return n15 - 1;
                    }
                    switch (n11) {
                        default: {
                            switch (n11) {
                                default: {
                                    return this.readAnnotationValues(i - 3, array, false, annotationVisitor.visitArray(s));
                                }
                                case 74: {
                                    final long[] array5 = new long[unsignedShort];
                                    int n16 = i;
                                    for (i = n7; i < unsignedShort; ++i) {
                                        array5[i] = this.readLong(this.items[this.readUnsignedShort(n16)]);
                                        n16 += 3;
                                    }
                                    annotationVisitor.visit(s, array5);
                                    return n16 - 1;
                                }
                                case 73: {
                                    final int[] array6 = new int[unsignedShort];
                                    int n17 = i;
                                    for (i = n; i < unsignedShort; ++i) {
                                        array6[i] = this.readInt(this.items[this.readUnsignedShort(n17)]);
                                        n17 += 3;
                                    }
                                    annotationVisitor.visit(s, array6);
                                    return n17 - 1;
                                }
                            }
                            break;
                        }
                        case 68: {
                            final double[] array7 = new double[unsignedShort];
                            int n18 = i;
                            for (i = n2; i < unsignedShort; ++i) {
                                array7[i] = Double.longBitsToDouble(this.readLong(this.items[this.readUnsignedShort(n18)]));
                                n18 += 3;
                            }
                            annotationVisitor.visit(s, array7);
                            return n18 - 1;
                        }
                        case 67: {
                            array = new char[unsignedShort];
                            int n19 = i;
                            for (i = n3; i < unsignedShort; ++i) {
                                array[i] = (char)this.readInt(this.items[this.readUnsignedShort(n19)]);
                                n19 += 3;
                            }
                            annotationVisitor.visit(s, array);
                            return n19 - 1;
                        }
                        case 66: {
                            final byte[] array8 = new byte[unsignedShort];
                            int n20 = i;
                            for (i = n4; i < unsignedShort; ++i) {
                                array8[i] = (byte)this.readInt(this.items[this.readUnsignedShort(n20)]);
                                n20 += 3;
                            }
                            annotationVisitor.visit(s, array8);
                            return n20 - 1;
                        }
                    }
                    break;
                }
                case 90: {
                    Boolean b3;
                    if (this.readInt(this.items[this.readUnsignedShort(n9)]) == 0) {
                        b3 = Boolean.FALSE;
                    }
                    else {
                        b3 = Boolean.TRUE;
                    }
                    annotationVisitor.visit(s, b3);
                    return n9 + 2;
                }
                case 83: {
                    annotationVisitor.visit(s, (short)this.readInt(this.items[this.readUnsignedShort(n9)]));
                    return n9 + 2;
                }
                case 68:
                case 70:
                case 73:
                case 74: {
                    annotationVisitor.visit(s, this.readConst(this.readUnsignedShort(n9), array));
                    return n9 + 2;
                }
                case 67: {
                    annotationVisitor.visit(s, (char)this.readInt(this.items[this.readUnsignedShort(n9)]));
                    return n9 + 2;
                }
                case 66: {
                    annotationVisitor.visit(s, (byte)this.readInt(this.items[this.readUnsignedShort(n9)]));
                    return n9 + 2;
                }
                case 64: {
                    return this.readAnnotationValues(n9 + 2, array, true, annotationVisitor.visitAnnotation(s, this.readUTF8(n9, array)));
                }
            }
        }
    }
    
    private int readAnnotationValues(int n, final char[] array, final boolean b, final AnnotationVisitor annotationVisitor) {
        final int unsignedShort = this.readUnsignedShort(n);
        final int n2 = n + 2;
        int n3 = unsignedShort;
        n = n2;
        int n5;
        if (b) {
            n = n2;
            int n4 = unsignedShort;
            while (true) {
                n5 = n;
                if (n4 <= 0) {
                    break;
                }
                n = this.readAnnotationValue(n + 2, array, this.readUTF8(n, array), annotationVisitor);
                --n4;
            }
        }
        else {
            while (true) {
                n5 = n;
                if (n3 <= 0) {
                    break;
                }
                n = this.readAnnotationValue(n, array, null, annotationVisitor);
                --n3;
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return n5;
    }
    
    private Attribute readAttribute(final Attribute[] array, final String s, final int n, final int n2, final char[] array2, final int n3, final Label[] array3) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i].type.equals(s)) {
                return array[i].read(this, n, n2, array2, n3, array3);
            }
        }
        return new Attribute(s).read(this, n, n2, null, -1, null);
    }
    
    private static byte[] readClass(final InputStream inputStream, final boolean b) throws IOException {
        if (inputStream != null) {
            try {
                byte[] array = new byte[inputStream.available()];
                int n = 0;
                while (true) {
                    final int read = inputStream.read(array, n, array.length - n);
                    if (read == -1) {
                        byte[] array2 = array;
                        if (n < array.length) {
                            array2 = new byte[n];
                            System.arraycopy(array, 0, array2, 0, n);
                        }
                        return array2;
                    }
                    final int n2 = n += read;
                    byte[] array3 = array;
                    if (n2 == array.length) {
                        final int read2 = inputStream.read();
                        if (read2 < 0) {
                            return array;
                        }
                        array3 = new byte[array.length + 1000];
                        System.arraycopy(array, 0, array3, 0, n2);
                        array3[n2] = (byte)read2;
                        n = n2 + 1;
                    }
                    array = array3;
                }
            }
            finally {
                if (b) {
                    inputStream.close();
                }
            }
        }
        throw new IOException("Class not found");
    }
    
    private void readCode(final MethodVisitor methodVisitor, final Context context, final int n) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.useAs(TypeTransformer.java:868)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:668)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
    }
    
    private int readField(final ClassVisitor classVisitor, final Context context, int n) {
        final char[] buffer = context.buffer;
        int unsignedShort = this.readUnsignedShort(n);
        final String utf8 = this.readUTF8(n + 2, buffer);
        final String utf9 = this.readUTF8(n + 4, buffer);
        int n2 = n + 6;
        n = 0;
        int i = this.readUnsignedShort(n2);
        String utf10 = null;
        int n3 = 0;
        int n4 = 0;
        Object const1 = null;
        Attribute next = null;
        int n5 = 0;
        while (i > 0) {
            final String utf11 = this.readUTF8(n2 + 2, buffer);
            if ("ConstantValue".equals(utf11)) {
                final int unsignedShort2 = this.readUnsignedShort(n2 + 8);
                if (unsignedShort2 == 0) {
                    const1 = null;
                }
                else {
                    const1 = this.readConst(unsignedShort2, buffer);
                }
            }
            else if ("Signature".equals(utf11)) {
                utf10 = this.readUTF8(n2 + 8, buffer);
            }
            else if ("Deprecated".equals(utf11)) {
                unsignedShort |= 0x20000;
            }
            else if ("Synthetic".equals(utf11)) {
                unsignedShort |= 0x41000;
            }
            else if ("RuntimeVisibleAnnotations".equals(utf11)) {
                n3 = n2 + 8;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(utf11)) {
                n5 = n2 + 8;
            }
            else if ("RuntimeInvisibleAnnotations".equals(utf11)) {
                n4 = n2 + 8;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(utf11)) {
                n = n2 + 8;
            }
            else {
                final Attribute attribute = this.readAttribute(context.attrs, utf11, n2 + 8, this.readInt(n2 + 4), buffer, -1, null);
                Attribute attribute2 = next;
                if (attribute != null) {
                    attribute.next = next;
                    attribute2 = attribute;
                }
                next = attribute2;
            }
            n2 += this.readInt(n2 + 4) + 6;
            --i;
        }
        final int n6 = n2 + 2;
        final FieldVisitor visitField = classVisitor.visitField(unsignedShort, utf8, utf9, utf10, const1);
        if (visitField == null) {
            return n6;
        }
        if (n3 != 0) {
            final int unsignedShort3 = this.readUnsignedShort(n3);
            int annotationValues = n3 + 2;
            for (int j = unsignedShort3; j > 0; --j) {
                annotationValues = this.readAnnotationValues(annotationValues + 2, buffer, true, visitField.visitAnnotation(this.readUTF8(annotationValues, buffer), true));
            }
        }
        if (n4 != 0) {
            final int unsignedShort4 = this.readUnsignedShort(n4);
            int annotationValues2 = n4 + 2;
            for (int k = unsignedShort4; k > 0; --k) {
                annotationValues2 = this.readAnnotationValues(annotationValues2 + 2, buffer, true, visitField.visitAnnotation(this.readUTF8(annotationValues2, buffer), false));
            }
        }
        if (n5 != 0) {
            int l = this.readUnsignedShort(n5);
            int annotationValues3 = n5 + 2;
            while (l > 0) {
                final int annotationTarget = this.readAnnotationTarget(context, annotationValues3);
                annotationValues3 = this.readAnnotationValues(annotationTarget + 2, buffer, true, visitField.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget, buffer), true));
                --l;
            }
        }
        if (n != 0) {
            int unsignedShort5 = this.readUnsignedShort(n);
            int annotationValues4 = n + 2;
            while (unsignedShort5 > 0) {
                final int annotationTarget2 = this.readAnnotationTarget(context, annotationValues4);
                annotationValues4 = this.readAnnotationValues(annotationTarget2 + 2, buffer, true, visitField.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget2, buffer), false));
                --unsignedShort5;
            }
        }
        while (next != null) {
            final Attribute next2 = next.next;
            next.next = null;
            visitField.visitAttribute(next);
            next = next2;
        }
        visitField.visitEnd();
        return n6;
    }
    
    private int readFrame(int n, final boolean b, final boolean b2, final Context context) {
        final char[] buffer = context.buffer;
        final Label[] labels = context.labels;
        int n4;
        if (b) {
            final int n2 = this.b[n] & 0xFF;
            final int n3 = n + 1;
            n = n2;
            n4 = n3;
        }
        else {
            context.offset = -1;
            final int n5 = 255;
            n4 = n;
            n = n5;
        }
        final boolean b3 = false;
        context.localDiff = 0;
        int n7;
        if (n < 64) {
            context.mode = 3;
            context.stackCount = 0;
            final int n6 = n;
            n = n4;
            n7 = n6;
        }
        else if (n < 128) {
            final int frameType = this.readFrameType(context.stack, 0, n4, buffer, labels);
            context.mode = 4;
            context.stackCount = 1;
            final int n8 = n - 64;
            n = frameType;
            n7 = n8;
        }
        else {
            final int unsignedShort = this.readUnsignedShort(n4);
            final int n9 = n4 + 2;
            if (n == 247) {
                n = this.readFrameType(context.stack, 0, n9, buffer, labels);
                context.mode = 4;
                context.stackCount = 1;
                n7 = unsignedShort;
            }
            else if (n >= 248 && n < 251) {
                context.mode = 2;
                context.localDiff = 251 - n;
                context.localCount -= context.localDiff;
                context.stackCount = 0;
                n = n9;
                n7 = unsignedShort;
            }
            else if (n == 251) {
                context.mode = 3;
                context.stackCount = 0;
                n = n9;
                n7 = unsignedShort;
            }
            else if (n < 255) {
                int localCount;
                if (b2) {
                    localCount = context.localCount;
                }
                else {
                    localCount = 0;
                }
                final int n10 = n - 251;
                final int n11 = localCount;
                int frameType2 = n9;
                for (int n12 = n11, i = n10; i > 0; --i, ++n12) {
                    frameType2 = this.readFrameType(context.local, n12, frameType2, buffer, labels);
                }
                context.mode = 1;
                context.localDiff = n - 251;
                context.localCount += context.localDiff;
                context.stackCount = 0;
                n = frameType2;
                n7 = unsignedShort;
            }
            else {
                context.mode = 0;
                final int unsignedShort2 = this.readUnsignedShort(n9);
                context.localDiff = unsignedShort2;
                context.localCount = unsignedShort2;
                n = n9 + 2;
                for (int j = unsignedShort2, n13 = 0; j > 0; --j, ++n13) {
                    n = this.readFrameType(context.local, n13, n, buffer, labels);
                }
                int unsignedShort3 = this.readUnsignedShort(n);
                n += 2;
                context.stackCount = unsignedShort3;
                int n14 = b3 ? 1 : 0;
                while (true) {
                    final int n15 = n14;
                    if (unsignedShort3 <= 0) {
                        break;
                    }
                    final Object[] stack = context.stack;
                    n14 = n15 + 1;
                    n = this.readFrameType(stack, n15, n, buffer, labels);
                    --unsignedShort3;
                }
                n7 = unsignedShort;
            }
        }
        this.readLabel(context.offset += n7 + 1, labels);
        return n;
    }
    
    private int readFrameType(final Object[] array, final int n, final int n2, final char[] array2, final Label[] array3) {
        final byte[] b = this.b;
        final int n3 = n2 + 1;
        switch (b[n2] & 0xFF) {
            default: {
                array[n] = this.readLabel(this.readUnsignedShort(n3), array3);
                return n3 + 2;
            }
            case 7: {
                array[n] = this.readClass(n3, array2);
                return n3 + 2;
            }
            case 6: {
                array[n] = Opcodes.UNINITIALIZED_THIS;
                return n3;
            }
            case 5: {
                array[n] = Opcodes.NULL;
                return n3;
            }
            case 4: {
                array[n] = Opcodes.LONG;
                return n3;
            }
            case 3: {
                array[n] = Opcodes.DOUBLE;
                return n3;
            }
            case 2: {
                array[n] = Opcodes.FLOAT;
                return n3;
            }
            case 1: {
                array[n] = Opcodes.INTEGER;
                return n3;
            }
            case 0: {
                array[n] = Opcodes.TOP;
                return n3;
            }
        }
    }
    
    private int readMethod(final ClassVisitor classVisitor, final Context context, int n) {
        final char[] buffer = context.buffer;
        context.access = this.readUnsignedShort(n);
        context.name = this.readUTF8(n + 2, buffer);
        context.desc = this.readUTF8(n + 4, buffer);
        final int classReaderOffset = n + 6;
        int i = this.readUnsignedShort(classReaderOffset);
        int n2 = 0;
        int n3 = 0;
        int n4 = classReaderOffset;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        Attribute next = null;
        String[] array = null;
        int n9 = 0;
        String utf8 = null;
        n = 0;
        int n10 = 0;
        int n11 = 0;
        while (i > 0) {
            final String utf9 = this.readUTF8(n4 + 2, buffer);
            if ("Code".equals(utf9)) {
                if ((context.flags & 0x1) == 0x0) {
                    n5 = n4 + 8;
                }
            }
            else if ("Exceptions".equals(utf9)) {
                array = new String[this.readUnsignedShort(n4 + 8)];
                n7 = n4 + 10;
                for (int j = 0; j < array.length; ++j) {
                    array[j] = this.readClass(n7, buffer);
                    n7 += 2;
                }
            }
            else if ("Signature".equals(utf9)) {
                utf8 = this.readUTF8(n4 + 8, buffer);
            }
            else if ("Deprecated".equals(utf9)) {
                context.access |= 0x20000;
            }
            else if ("RuntimeVisibleAnnotations".equals(utf9)) {
                n10 = n4 + 8;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(utf9)) {
                n3 = n4 + 8;
            }
            else if ("AnnotationDefault".equals(utf9)) {
                n9 = n4 + 8;
            }
            else if ("Synthetic".equals(utf9)) {
                context.access |= 0x41000;
            }
            else if ("RuntimeInvisibleAnnotations".equals(utf9)) {
                n11 = n4 + 8;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(utf9)) {
                n = n4 + 8;
            }
            else if ("RuntimeVisibleParameterAnnotations".equals(utf9)) {
                n8 = n4 + 8;
            }
            else if ("RuntimeInvisibleParameterAnnotations".equals(utf9)) {
                n6 = n4 + 8;
            }
            else if ("MethodParameters".equals(utf9)) {
                n2 = n4 + 8;
            }
            else {
                final Attribute attribute = this.readAttribute(context.attrs, utf9, n4 + 8, this.readInt(n4 + 4), buffer, -1, null);
                if (attribute != null) {
                    attribute.next = next;
                    next = attribute;
                }
            }
            n4 += this.readInt(n4 + 4) + 6;
            --i;
        }
        final int n12 = n4 + 2;
        final MethodVisitor visitMethod = classVisitor.visitMethod(context.access, context.name, context.desc, utf8, array);
        if (visitMethod == null) {
            return n12;
        }
        if (visitMethod instanceof MethodWriter) {
            final MethodWriter methodWriter = (MethodWriter)visitMethod;
            if (methodWriter.cw.cr == this && utf8 == methodWriter.signature) {
                int n13 = 0;
                if (array == null) {
                    n13 = ((methodWriter.exceptionCount == 0) ? 1 : 0);
                }
                else if (array.length == methodWriter.exceptionCount) {
                    final boolean b = true;
                    final int n14 = array.length - 1;
                    int n15 = n7;
                    int n16 = n14;
                    while (true) {
                        n13 = (b ? 1 : 0);
                        if (n16 < 0) {
                            break;
                        }
                        n15 -= 2;
                        if (methodWriter.exceptions[n16] != this.readUnsignedShort(n15)) {
                            n13 = 0;
                            break;
                        }
                        --n16;
                    }
                }
                if (n13 != 0) {
                    methodWriter.classReaderOffset = classReaderOffset;
                    methodWriter.classReaderLength = n12 - classReaderOffset;
                    return n12;
                }
            }
        }
        if (n2 != 0) {
            for (int k = this.b[n2] & 0xFF, n17 = n2 + 1; k > 0; --k, n17 += 4) {
                visitMethod.visitParameter(this.readUTF8(n17, buffer), this.readUnsignedShort(n17 + 2));
            }
        }
        if (n9 != 0) {
            final AnnotationVisitor visitAnnotationDefault = visitMethod.visitAnnotationDefault();
            this.readAnnotationValue(n9, buffer, null, visitAnnotationDefault);
            if (visitAnnotationDefault != null) {
                visitAnnotationDefault.visitEnd();
            }
        }
        if (n10 != 0) {
            final int unsignedShort = this.readUnsignedShort(n10);
            int annotationValues = n10 + 2;
            for (int l = unsignedShort; l > 0; --l) {
                annotationValues = this.readAnnotationValues(annotationValues + 2, buffer, true, visitMethod.visitAnnotation(this.readUTF8(annotationValues, buffer), true));
            }
        }
        if (n11 != 0) {
            final int unsignedShort2 = this.readUnsignedShort(n11);
            int annotationValues2 = n11 + 2;
            for (int n18 = unsignedShort2; n18 > 0; --n18) {
                annotationValues2 = this.readAnnotationValues(annotationValues2 + 2, buffer, true, visitMethod.visitAnnotation(this.readUTF8(annotationValues2, buffer), false));
            }
        }
        if (n3 != 0) {
            int unsignedShort3 = this.readUnsignedShort(n3);
            int annotationValues3 = n3 + 2;
            while (unsignedShort3 > 0) {
                final int annotationTarget = this.readAnnotationTarget(context, annotationValues3);
                annotationValues3 = this.readAnnotationValues(annotationTarget + 2, buffer, true, visitMethod.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget, buffer), true));
                --unsignedShort3;
            }
        }
        if (n != 0) {
            int unsignedShort4 = this.readUnsignedShort(n);
            int annotationValues4 = n + 2;
            while (unsignedShort4 > 0) {
                final int annotationTarget2 = this.readAnnotationTarget(context, annotationValues4);
                annotationValues4 = this.readAnnotationValues(annotationTarget2 + 2, buffer, true, visitMethod.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget2, buffer), false));
                --unsignedShort4;
            }
        }
        if (n8 != 0) {
            this.readParameterAnnotations(visitMethod, context, n8, true);
        }
        Attribute attribute2 = next;
        if (n6 != 0) {
            this.readParameterAnnotations(visitMethod, context, n6, false);
            attribute2 = next;
        }
        while (attribute2 != null) {
            final Attribute next2 = attribute2.next;
            attribute2.next = null;
            visitMethod.visitAttribute(attribute2);
            attribute2 = next2;
        }
        if (n5 != 0) {
            visitMethod.visitCode();
            this.readCode(visitMethod, context, n5);
        }
        visitMethod.visitEnd();
        return n12;
    }
    
    private void readParameterAnnotations(final MethodVisitor methodVisitor, final Context context, int i, final boolean b) {
        final byte[] b2 = this.b;
        final int n = i + 1;
        final int n2 = b2[i] & 0xFF;
        int n3;
        AnnotationVisitor visitParameterAnnotation;
        for (n3 = Type.getArgumentTypes(context.desc).length - n2, i = 0; i < n3; ++i) {
            visitParameterAnnotation = methodVisitor.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
            if (visitParameterAnnotation != null) {
                visitParameterAnnotation.visitEnd();
            }
        }
        final char[] buffer = context.buffer;
        int j = i;
        i = n;
        while (j < n2 + n3) {
            int k = this.readUnsignedShort(i);
            i += 2;
            while (k > 0) {
                i = this.readAnnotationValues(i + 2, buffer, true, methodVisitor.visitParameterAnnotation(j, this.readUTF8(i, buffer), b));
                --k;
            }
            ++j;
        }
    }
    
    private int[] readTypeAnnotations(final MethodVisitor methodVisitor, final Context context, int i, final boolean b) {
        final char[] buffer = context.buffer;
        final int[] array = new int[this.readUnsignedShort(i)];
        i += 2;
        for (int j = 0; j < array.length; ++j) {
            array[j] = i;
            final int int1 = this.readInt(i);
            final int n = int1 >>> 24;
            Label_0275: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        switch (n) {
                                            default: {
                                                i += 3;
                                                break Label_0275;
                                            }
                                            case 71:
                                            case 72:
                                            case 73:
                                            case 74:
                                            case 75: {
                                                i += 4;
                                                break Label_0275;
                                            }
                                        }
                                        break;
                                    }
                                    case 64:
                                    case 65: {
                                        final int unsignedShort = this.readUnsignedShort(i + 1);
                                        int n2 = i;
                                        int unsignedShort2;
                                        int unsignedShort3;
                                        for (i = unsignedShort; i > 0; --i) {
                                            unsignedShort2 = this.readUnsignedShort(n2 + 3);
                                            unsignedShort3 = this.readUnsignedShort(n2 + 5);
                                            this.readLabel(unsignedShort2, context.labels);
                                            this.readLabel(unsignedShort2 + unsignedShort3, context.labels);
                                            n2 += 6;
                                        }
                                        i = n2 + 3;
                                        break Label_0275;
                                    }
                                }
                                break;
                            }
                            case 19:
                            case 20:
                            case 21: {
                                ++i;
                                break Label_0275;
                            }
                            case 22: {
                                break Label_0275;
                            }
                        }
                        break;
                    }
                    case 0:
                    case 1: {
                        i += 2;
                        break;
                    }
                }
            }
            final int byte1 = this.readByte(i);
            TypePath typePath = null;
            if (int1 >>> 24 == 66) {
                if (byte1 != 0) {
                    typePath = new TypePath(this.b, i);
                }
                i += byte1 * 2 + 1;
                i = this.readAnnotationValues(i + 2, buffer, true, methodVisitor.visitTryCatchAnnotation(int1, typePath, this.readUTF8(i, buffer), b));
            }
            else {
                i = this.readAnnotationValues(i + 3 + byte1 * 2, buffer, true, null);
            }
        }
        return array;
    }
    
    private String readUTF(final int n, final int n2, final char[] array) {
        final byte[] b = this.b;
        int n3 = 0;
        int n4 = 0;
        int i = n;
        int n5 = 0;
        while (i < n + n2) {
            final byte b2 = b[i];
            int n6 = 0;
            Label_0196: {
                int n7 = 0;
                switch (n3) {
                    default: {
                        n6 = n5;
                        break Label_0196;
                    }
                    case 2: {
                        n6 = (char)(n5 << 6 | (b2 & 0x3F));
                        n3 = 1;
                        break Label_0196;
                    }
                    case 1: {
                        n7 = n4 + 1;
                        array[n4] = (char)(n5 << 6 | (b2 & 0x3F));
                        n3 = 0;
                        break;
                    }
                    case 0: {
                        final int n8 = b2 & 0xFF;
                        if (n8 < 128) {
                            n7 = n4 + 1;
                            array[n4] = (char)n8;
                            break;
                        }
                        if (n8 < 224 && n8 > 191) {
                            n6 = (char)(n8 & 0x1F);
                            n3 = 1;
                            break Label_0196;
                        }
                        n6 = (char)(n8 & 0xF);
                        n3 = 2;
                        break Label_0196;
                    }
                }
                n4 = n7;
                n6 = n5;
            }
            ++i;
            n5 = n6;
        }
        return new String(array, 0, n4);
    }
    
    public void accept(final ClassVisitor classVisitor, final int n) {
        this.accept(classVisitor, new Attribute[0], n);
    }
    
    public void accept(final ClassVisitor classVisitor, final Attribute[] attrs, int i) {
        final int header = this.header;
        final char[] buffer = new char[this.maxStringLength];
        final Context context = new Context();
        context.attrs = attrs;
        context.flags = i;
        context.buffer = buffer;
        int unsignedShort = this.readUnsignedShort(header);
        final String class1 = this.readClass(header + 2, buffer);
        final String class2 = this.readClass(header + 4, buffer);
        final String[] array = new String[this.readUnsignedShort(header + 6)];
        int n = header + 8;
        for (int j = 0; j < array.length; ++j) {
            array[j] = this.readClass(n, buffer);
            n += 2;
        }
        int attributes = this.getAttributes();
        int k = this.readUnsignedShort(attributes);
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        Attribute next = null;
        String utf8 = null;
        String utf9 = null;
        String utf10 = null;
        String class3 = null;
        String utf11 = null;
        String utf12 = null;
        int n6 = 0;
        while (k > 0) {
            final String utf13 = this.readUTF8(attributes + 2, buffer);
            if ("SourceFile".equals(utf13)) {
                utf11 = this.readUTF8(attributes + 8, buffer);
            }
            else if ("InnerClasses".equals(utf13)) {
                n6 = attributes + 8;
            }
            else if ("EnclosingMethod".equals(utf13)) {
                class3 = this.readClass(attributes + 8, buffer);
                final int unsignedShort2 = this.readUnsignedShort(attributes + 10);
                if (unsignedShort2 != 0) {
                    utf10 = this.readUTF8(this.items[unsignedShort2], buffer);
                    utf9 = this.readUTF8(this.items[unsignedShort2] + 2, buffer);
                }
            }
            else if ("Signature".equals(utf13)) {
                utf8 = this.readUTF8(attributes + 8, buffer);
            }
            else if ("RuntimeVisibleAnnotations".equals(utf13)) {
                n2 = attributes + 8;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(utf13)) {
                n4 = attributes + 8;
            }
            else if ("Deprecated".equals(utf13)) {
                unsignedShort |= 0x20000;
            }
            else if ("Synthetic".equals(utf13)) {
                unsignedShort |= 0x41000;
            }
            else if ("SourceDebugExtension".equals(utf13)) {
                final int int1 = this.readInt(attributes + 4);
                utf12 = this.readUTF(attributes + 8, int1, new char[int1]);
            }
            else if ("RuntimeInvisibleAnnotations".equals(utf13)) {
                n3 = attributes + 8;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(utf13)) {
                n5 = attributes + 8;
            }
            else {
                String s = null;
                Attribute attribute3 = null;
                Label_0680: {
                    if ("BootstrapMethods".equals(utf13)) {
                        final int[] bootstrapMethods = new int[this.readUnsignedShort(attributes + 8)];
                        int l = 0;
                        int n7 = attributes + 10;
                        while (l < bootstrapMethods.length) {
                            bootstrapMethods[l] = n7;
                            n7 += this.readUnsignedShort(n7 + 2) + 2 << 1;
                            ++l;
                        }
                        context.bootstrapMethods = bootstrapMethods;
                        s = class3;
                    }
                    else {
                        final Attribute attribute = this.readAttribute(attrs, utf13, attributes + 8, this.readInt(attributes + 4), buffer, -1, null);
                        if (attribute != null) {
                            attribute.next = next;
                            final Attribute attribute2 = attribute;
                            s = class3;
                            attribute3 = attribute2;
                            break Label_0680;
                        }
                        s = class3;
                    }
                    attribute3 = next;
                }
                next = attribute3;
                class3 = s;
            }
            attributes += this.readInt(attributes + 4) + 6;
            --k;
        }
        classVisitor.visit(this.readInt(this.items[1] - 7), unsignedShort, class1, utf8, class2, array);
        if ((i & 0x2) == 0x0) {
            final String s2 = utf11;
            if (s2 != null || utf12 != null) {
                classVisitor.visitSource(s2, utf12);
            }
        }
        if (class3 != null) {
            classVisitor.visitOuterClass(class3, utf10, utf9);
        }
        if (n2 != 0) {
            i = this.readUnsignedShort(n2);
            int annotationValues = n2 + 2;
            while (i > 0) {
                annotationValues = this.readAnnotationValues(annotationValues + 2, buffer, true, classVisitor.visitAnnotation(this.readUTF8(annotationValues, buffer), true));
                --i;
            }
        }
        if (n3 != 0) {
            i = this.readUnsignedShort(n3);
            int annotationValues2 = n3 + 2;
            while (i > 0) {
                annotationValues2 = this.readAnnotationValues(annotationValues2 + 2, buffer, true, classVisitor.visitAnnotation(this.readUTF8(annotationValues2, buffer), false));
                --i;
            }
        }
        i = n4;
        if (i != 0) {
            int unsignedShort3 = this.readUnsignedShort(i);
            int annotationValues3 = i + 2;
            while (unsignedShort3 > 0) {
                final int annotationTarget = this.readAnnotationTarget(context, annotationValues3);
                annotationValues3 = this.readAnnotationValues(annotationTarget + 2, buffer, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget, buffer), true));
                --unsignedShort3;
            }
        }
        i = n5;
        if (i != 0) {
            int unsignedShort4 = this.readUnsignedShort(i);
            int annotationValues4 = i + 2;
            while (unsignedShort4 > 0) {
                final int annotationTarget2 = this.readAnnotationTarget(context, annotationValues4);
                annotationValues4 = this.readAnnotationValues(annotationTarget2 + 2, buffer, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(annotationTarget2, buffer), false));
                --unsignedShort4;
            }
        }
        while (next != null) {
            final Attribute next2 = next.next;
            next.next = null;
            classVisitor.visitAttribute(next);
            next = next2;
        }
        i = n6;
        if (i != 0) {
            int n8 = i + 2;
            for (int unsignedShort5 = this.readUnsignedShort(i); unsignedShort5 > 0; --unsignedShort5) {
                classVisitor.visitInnerClass(this.readClass(n8, buffer), this.readClass(n8 + 2, buffer), this.readUTF8(n8 + 4, buffer), this.readUnsignedShort(n8 + 6));
                n8 += 8;
            }
        }
        int field = this.header + 10 + array.length * 2;
        for (i = this.readUnsignedShort(field - 2); i > 0; --i) {
            field = this.readField(classVisitor, context, field);
        }
        int method = field + 2;
        for (i = this.readUnsignedShort(method - 2); i > 0; --i) {
            method = this.readMethod(classVisitor, context, method);
        }
        classVisitor.visitEnd();
    }
    
    void copyPool(final ClassWriter classWriter) {
        final char[] array = new char[this.maxStringLength];
        final int length = this.items.length;
        final Item[] items = new Item[length];
        for (int i = 1; i < length; ++i) {
            final int n = this.items[i];
            final byte b = this.b[n - 1];
            final Item item = new Item(i);
            Label_0512: {
                if (b != 1) {
                    if (b != 15) {
                        if (b != 18) {
                            switch (b) {
                                default: {
                                    switch (b) {
                                        default: {
                                            item.set(b, this.readUTF8(n, array), null, null);
                                            break Label_0512;
                                        }
                                        case 12: {
                                            item.set(b, this.readUTF8(n, array), this.readUTF8(n + 2, array), null);
                                            break Label_0512;
                                        }
                                        case 9:
                                        case 10:
                                        case 11: {
                                            final int n2 = this.items[this.readUnsignedShort(n + 2)];
                                            item.set(b, this.readClass(n, array), this.readUTF8(n2, array), this.readUTF8(n2 + 2, array));
                                            break Label_0512;
                                        }
                                    }
                                    break;
                                }
                                case 6: {
                                    item.set(Double.longBitsToDouble(this.readLong(n)));
                                    ++i;
                                    break;
                                }
                                case 5: {
                                    item.set(this.readLong(n));
                                    ++i;
                                    break;
                                }
                                case 4: {
                                    item.set(Float.intBitsToFloat(this.readInt(n)));
                                    break;
                                }
                                case 3: {
                                    item.set(this.readInt(n));
                                    break;
                                }
                            }
                        }
                        else {
                            if (classWriter.bootstrapMethods == null) {
                                this.copyBootstrapMethods(classWriter, items, array);
                            }
                            final int n3 = this.items[this.readUnsignedShort(n + 2)];
                            item.set(this.readUTF8(n3, array), this.readUTF8(n3 + 2, array), this.readUnsignedShort(n));
                        }
                    }
                    else {
                        final int n4 = this.items[this.readUnsignedShort(n + 1)];
                        final int n5 = this.items[this.readUnsignedShort(n4 + 2)];
                        item.set(this.readByte(n) + 20, this.readClass(n4, array), this.readUTF8(n5, array), this.readUTF8(n5 + 2, array));
                    }
                }
                else {
                    String utf;
                    if ((utf = this.strings[i]) == null) {
                        final int n6 = this.items[i];
                        final String[] strings = this.strings;
                        utf = this.readUTF(n6 + 2, this.readUnsignedShort(n6), array);
                        strings[i] = utf;
                    }
                    item.set(b, utf, null, null);
                }
            }
            final int n7 = item.hashCode % items.length;
            item.next = items[n7];
            items[n7] = item;
        }
        final int n8 = this.items[1] - 1;
        classWriter.pool.putByteArray(this.b, n8, this.header - n8);
        classWriter.items = items;
        classWriter.threshold = (int)(length * 0.75);
        classWriter.index = length;
    }
    
    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }
    
    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }
    
    public String[] getInterfaces() {
        int n = this.header + 6;
        final int unsignedShort = this.readUnsignedShort(n);
        final String[] array = new String[unsignedShort];
        if (unsignedShort > 0) {
            final char[] array2 = new char[this.maxStringLength];
            for (int i = 0; i < unsignedShort; ++i) {
                n += 2;
                array[i] = this.readClass(n, array2);
            }
        }
        return array;
    }
    
    public int getItem(final int n) {
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
    
    public int readByte(final int n) {
        return this.b[n] & 0xFF;
    }
    
    public String readClass(final int n, final char[] array) {
        return this.readUTF8(this.items[this.readUnsignedShort(n)], array);
    }
    
    public Object readConst(int byte1, final char[] array) {
        final int n = this.items[byte1];
        byte1 = this.b[n - 1];
        if (byte1 == 16) {
            return Type.getMethodType(this.readUTF8(n, array));
        }
        switch (byte1) {
            default: {
                byte1 = this.readByte(n);
                final int[] items = this.items;
                final int n2 = items[this.readUnsignedShort(n + 1)];
                final boolean b = this.b[n2 - 1] == 11;
                final String class1 = this.readClass(n2, array);
                final int n3 = items[this.readUnsignedShort(n2 + 2)];
                return new Handle(byte1, class1, this.readUTF8(n3, array), this.readUTF8(n3 + 2, array), b);
            }
            case 8: {
                return this.readUTF8(n, array);
            }
            case 7: {
                return Type.getObjectType(this.readUTF8(n, array));
            }
            case 6: {
                return Double.longBitsToDouble(this.readLong(n));
            }
            case 5: {
                return this.readLong(n);
            }
            case 4: {
                return Float.intBitsToFloat(this.readInt(n));
            }
            case 3: {
                return this.readInt(n);
            }
        }
    }
    
    public int readInt(final int n) {
        final byte[] b = this.b;
        return (b[n] & 0xFF) << 24 | (b[n + 1] & 0xFF) << 16 | (b[n + 2] & 0xFF) << 8 | (b[n + 3] & 0xFF);
    }
    
    protected Label readLabel(final int n, final Label[] array) {
        if (array[n] == null) {
            array[n] = new Label();
        }
        return array[n];
    }
    
    public long readLong(final int n) {
        return (long)this.readInt(n) << 32 | ((long)this.readInt(n + 4) & 0xFFFFFFFFL);
    }
    
    public short readShort(final int n) {
        final byte[] b = this.b;
        return (short)((b[n] & 0xFF) << 8 | (b[n + 1] & 0xFF));
    }
    
    public String readUTF8(int n, final char[] array) {
        final int unsignedShort = this.readUnsignedShort(n);
        if (n == 0 || unsignedShort == 0) {
            return null;
        }
        final String s = this.strings[unsignedShort];
        if (s != null) {
            return s;
        }
        n = this.items[unsignedShort];
        return this.strings[unsignedShort] = this.readUTF(n + 2, this.readUnsignedShort(n), array);
    }
    
    public int readUnsignedShort(final int n) {
        final byte[] b = this.b;
        return (b[n] & 0xFF) << 8 | (b[n + 1] & 0xFF);
    }
}
