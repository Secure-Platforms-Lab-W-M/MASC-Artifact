// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class ClassWriter extends ClassVisitor
{
    static final int ACC_SYNTHETIC_ATTRIBUTE = 262144;
    static final int ASM_LABEL_INSN = 18;
    static final int BSM = 33;
    static final int CLASS = 7;
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final int DOUBLE = 6;
    static final int FIELD = 9;
    static final int FIELDORMETH_INSN = 6;
    static final int FLOAT = 4;
    static final int F_INSERT = 256;
    static final int HANDLE = 15;
    static final int HANDLE_BASE = 20;
    static final int IINC_INSN = 13;
    static final int IMETH = 11;
    static final int IMPLVAR_INSN = 4;
    static final int INDY = 18;
    static final int INDYMETH_INSN = 8;
    static final int INT = 3;
    static final int ITFMETH_INSN = 7;
    static final int LABELW_INSN = 10;
    static final int LABEL_INSN = 9;
    static final int LDCW_INSN = 12;
    static final int LDC_INSN = 11;
    static final int LONG = 5;
    static final int LOOK_INSN = 15;
    static final int MANA_INSN = 16;
    static final int METH = 10;
    static final int MTYPE = 16;
    static final int NAME_TYPE = 12;
    static final int NOARG_INSN = 0;
    static final int SBYTE_INSN = 1;
    static final int SHORT_INSN = 2;
    static final int STR = 8;
    static final int TABL_INSN = 14;
    static final int TO_ACC_SYNTHETIC = 64;
    static final byte[] TYPE;
    static final int TYPE_INSN = 5;
    static final int TYPE_MERGED = 32;
    static final int TYPE_NORMAL = 30;
    static final int TYPE_UNINIT = 31;
    static final int UTF8 = 1;
    static final int VAR_INSN = 3;
    static final int WIDE_INSN = 17;
    private int access;
    private AnnotationWriter anns;
    private Attribute attrs;
    ByteVector bootstrapMethods;
    int bootstrapMethodsCount;
    private int compute;
    ClassReader cr;
    private int enclosingMethod;
    private int enclosingMethodOwner;
    FieldWriter firstField;
    MethodWriter firstMethod;
    boolean hasAsmInsns;
    private AnnotationWriter ianns;
    int index;
    private ByteVector innerClasses;
    private int innerClassesCount;
    private int interfaceCount;
    private int[] interfaces;
    private AnnotationWriter itanns;
    Item[] items;
    final Item key;
    final Item key2;
    final Item key3;
    final Item key4;
    FieldWriter lastField;
    MethodWriter lastMethod;
    private int name;
    final ByteVector pool;
    private int signature;
    private ByteVector sourceDebug;
    private int sourceFile;
    private int superName;
    private AnnotationWriter tanns;
    String thisName;
    int threshold;
    private short typeCount;
    Item[] typeTable;
    int version;
    
    static {
        final byte[] type = new byte[220];
        for (int i = 0; i < type.length; ++i) {
            type[i] = (byte)("AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKSSSSSSSSSSSSSSSSSS".charAt(i) - 'A');
        }
        TYPE = type;
    }
    
    public ClassWriter(int compute) {
        super(327680);
        this.index = 1;
        this.pool = new ByteVector();
        this.items = new Item[256];
        this.threshold = (int)(this.items.length * 0.75);
        this.key = new Item();
        this.key2 = new Item();
        this.key3 = new Item();
        this.key4 = new Item();
        if ((compute & 0x2) != 0x0) {
            compute = 0;
        }
        else if ((compute & 0x1) != 0x0) {
            compute = 2;
        }
        else {
            compute = 3;
        }
        this.compute = compute;
    }
    
    public ClassWriter(final ClassReader cr, final int n) {
        this(n);
        cr.copyPool(this);
        this.cr = cr;
    }
    
    private Item addType(Item item) {
        ++this.typeCount;
        item = new Item(this.typeCount, this.key);
        this.put(item);
        if (this.typeTable == null) {
            this.typeTable = new Item[16];
        }
        if (this.typeCount == this.typeTable.length) {
            final Item[] typeTable = new Item[this.typeTable.length * 2];
            System.arraycopy(this.typeTable, 0, typeTable, 0, this.typeTable.length);
            this.typeTable = typeTable;
        }
        return this.typeTable[this.typeCount] = item;
    }
    
    private Item get(final Item item) {
        Item next;
        for (next = this.items[item.hashCode % this.items.length]; next != null && (next.type != item.type || !item.isEqualTo(next)); next = next.next) {}
        return next;
    }
    
    private Item newString(final String s) {
        this.key2.set(8, s, null, null);
        Item value;
        if ((value = this.get(this.key2)) == null) {
            this.pool.put12(8, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    private void put(final Item item) {
        if (this.index + this.typeCount > this.threshold) {
            final int length = this.items.length;
            final int n = length * 2 + 1;
            final Item[] items = new Item[n];
            for (int i = length - 1; i >= 0; --i) {
                Item next;
                for (Item item2 = this.items[i]; item2 != null; item2 = next) {
                    final int n2 = item2.hashCode % items.length;
                    next = item2.next;
                    item2.next = items[n2];
                    items[n2] = item2;
                }
            }
            this.items = items;
            this.threshold = (int)(n * 0.75);
        }
        final int n3 = item.hashCode % this.items.length;
        item.next = this.items[n3];
        this.items[n3] = item;
    }
    
    private void put112(final int n, final int n2, final int n3) {
        this.pool.put11(n, n2).putShort(n3);
    }
    
    private void put122(final int n, final int n2, final int n3) {
        this.pool.put12(n, n2).putShort(n3);
    }
    
    int addType(final String s) {
        this.key.set(30, s, null, null);
        Item item;
        if ((item = this.get(this.key)) == null) {
            item = this.addType(this.key);
        }
        return item.index;
    }
    
    int addUninitializedType(final String strVal1, final int intVal) {
        this.key.type = 31;
        this.key.intVal = intVal;
        this.key.strVal1 = strVal1;
        this.key.hashCode = (Integer.MAX_VALUE & strVal1.hashCode() + 31 + intVal);
        Item item;
        if ((item = this.get(this.key)) == null) {
            item = this.addType(this.key);
        }
        return item.index;
    }
    
    protected String getCommonSuperClass(final String s, final String s2) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            final Class<?> forName = Class.forName(s.replace('/', '.'), false, classLoader);
            final Class<?> forName2 = Class.forName(s2.replace('/', '.'), false, classLoader);
            if (forName.isAssignableFrom(forName2)) {
                return s;
            }
            if (forName2.isAssignableFrom(forName)) {
                return s2;
            }
            if (!forName.isInterface()) {
                Class<?> superclass = forName;
                if (!forName2.isInterface()) {
                    Class<?> clazz;
                    do {
                        clazz = (superclass = superclass.getSuperclass());
                    } while (!clazz.isAssignableFrom(forName2));
                    return clazz.getName().replace('.', '/');
                }
            }
            return "java/lang/Object";
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }
    
    int getMergedType(final int n, final int n2) {
        this.key2.type = 32;
        this.key2.longVal = ((long)n | (long)n2 << 32);
        this.key2.hashCode = (n + 32 + n2 & Integer.MAX_VALUE);
        Item value;
        if ((value = this.get(this.key2)) == null) {
            this.key2.intVal = this.addType(this.getCommonSuperClass(this.typeTable[n].strVal1, this.typeTable[n2].strVal1));
            value = new Item(0, this.key2);
            this.put(value);
        }
        return value.intVal;
    }
    
    public int newClass(final String s) {
        return this.newClassItem(s).index;
    }
    
    Item newClassItem(final String s) {
        this.key2.set(7, s, null, null);
        Item value;
        if ((value = this.get(this.key2)) == null) {
            this.pool.put12(7, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    public int newConst(final Object o) {
        return this.newConstItem(o).index;
    }
    
    Item newConstItem(final Object o) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
    }
    
    Item newDouble(final double n) {
        this.key.set(n);
        Item value;
        if ((value = this.get(this.key)) == null) {
            this.pool.putByte(6).putLong(this.key.longVal);
            value = new Item(this.index, this.key);
            this.index += 2;
            this.put(value);
        }
        return value;
    }
    
    public int newField(final String s, final String s2, final String s3) {
        return this.newFieldItem(s, s2, s3).index;
    }
    
    Item newFieldItem(final String s, final String s2, final String s3) {
        this.key3.set(9, s, s2, s3);
        Item value;
        if ((value = this.get(this.key3)) == null) {
            this.put122(9, this.newClass(s), this.newNameType(s2, s3));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    Item newFloat(final float n) {
        this.key.set(n);
        Item value;
        if ((value = this.get(this.key)) == null) {
            this.pool.putByte(4).putInt(this.key.intVal);
            value = new Item(this.index++, this.key);
            this.put(value);
        }
        return value;
    }
    
    @Deprecated
    public int newHandle(final int n, final String s, final String s2, final String s3) {
        return this.newHandle(n, s, s2, s3, n == 9);
    }
    
    public int newHandle(final int n, final String s, final String s2, final String s3, final boolean b) {
        return this.newHandleItem(n, s, s2, s3, b).index;
    }
    
    Item newHandleItem(int n, final String s, final String s2, final String s3, final boolean b) {
        this.key4.set(n + 20, s, s2, s3);
        Item value;
        if ((value = this.get(this.key4)) == null) {
            if (n <= 4) {
                this.put112(15, n, this.newField(s, s2, s3));
            }
            else {
                this.put112(15, n, this.newMethod(s, s2, s3, b));
            }
            n = this.index++;
            value = new Item(n, this.key4);
            this.put(value);
        }
        return value;
    }
    
    Item newInteger(int n) {
        this.key.set(n);
        Item value;
        if ((value = this.get(this.key)) == null) {
            this.pool.putByte(3).putInt(n);
            n = this.index++;
            value = new Item(n, this.key);
            this.put(value);
        }
        return value;
    }
    
    public int newInvokeDynamic(final String s, final String s2, final Handle handle, final Object... array) {
        return this.newInvokeDynamicItem(s, s2, handle, array).index;
    }
    
    Item newInvokeDynamicItem(final String s, final String s2, final Handle handle, final Object... array) {
        ByteVector bootstrapMethods;
        if ((bootstrapMethods = this.bootstrapMethods) == null) {
            bootstrapMethods = new ByteVector();
            this.bootstrapMethods = bootstrapMethods;
        }
        final int length = bootstrapMethods.length;
        int hashCode = handle.hashCode();
        bootstrapMethods.putShort(this.newHandle(handle.tag, handle.owner, handle.name, handle.desc, handle.isInterface()));
        final int length2 = array.length;
        bootstrapMethods.putShort(length2);
        for (int i = 0; i < length2; ++i) {
            final Object o = array[i];
            hashCode ^= o.hashCode();
            bootstrapMethods.putShort(this.newConst(o));
        }
        final byte[] data = bootstrapMethods.data;
        final int n = Integer.MAX_VALUE & hashCode;
        Item item = this.items[n % this.items.length];
    Label_0159:
        while (item != null) {
            if (item.type == 33 && item.hashCode == n) {
                final int intVal = item.intVal;
                for (int j = 0; j < length2 + 2 << 1; ++j) {
                    if (data[length + j] != data[intVal + j]) {
                        item = item.next;
                        continue Label_0159;
                    }
                }
                break;
            }
            item = item.next;
        }
        int index;
        if (item != null) {
            index = item.index;
            bootstrapMethods.length = length;
        }
        else {
            index = this.bootstrapMethodsCount++;
            final Item item2 = new Item(index);
            item2.set(length, n);
            this.put(item2);
        }
        this.key3.set(s, s2, index);
        Item value;
        if ((value = this.get(this.key3)) == null) {
            this.put122(18, index, this.newNameType(s, s2));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    Item newLong(final long n) {
        this.key.set(n);
        Item value;
        if ((value = this.get(this.key)) == null) {
            this.pool.putByte(5).putLong(n);
            value = new Item(this.index, this.key);
            this.index += 2;
            this.put(value);
        }
        return value;
    }
    
    public int newMethod(final String s, final String s2, final String s3, final boolean b) {
        return this.newMethodItem(s, s2, s3, b).index;
    }
    
    Item newMethodItem(final String s, final String s2, final String s3, final boolean b) {
        int n;
        if (b) {
            n = 11;
        }
        else {
            n = 10;
        }
        this.key3.set(n, s, s2, s3);
        Item value;
        if ((value = this.get(this.key3)) == null) {
            this.put122(n, this.newClass(s), this.newNameType(s2, s3));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    public int newMethodType(final String s) {
        return this.newMethodTypeItem(s).index;
    }
    
    Item newMethodTypeItem(final String s) {
        this.key2.set(16, s, null, null);
        Item value;
        if ((value = this.get(this.key2)) == null) {
            this.pool.put12(16, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    public int newNameType(final String s, final String s2) {
        return this.newNameTypeItem(s, s2).index;
    }
    
    Item newNameTypeItem(final String s, final String s2) {
        this.key2.set(12, s, s2, null);
        Item value;
        if ((value = this.get(this.key2)) == null) {
            this.put122(12, this.newUTF8(s), this.newUTF8(s2));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    public int newUTF8(final String s) {
        this.key.set(1, s, null, null);
        Item value;
        if ((value = this.get(this.key)) == null) {
            this.pool.putByte(1).putUTF8(s);
            value = new Item(this.index++, this.key);
            this.put(value);
        }
        return value.index;
    }
    
    public byte[] toByteArray() {
        if (this.index > 65535) {
            throw new RuntimeException("Class file too large!");
        }
        int n = this.interfaceCount * 2 + 24;
        FieldWriter firstField = this.firstField;
        int n2 = 0;
        while (firstField != null) {
            ++n2;
            n += firstField.getSize();
            firstField = (FieldWriter)firstField.fv;
        }
        MethodWriter firstMethod = this.firstMethod;
        int n3 = 0;
        while (firstMethod != null) {
            ++n3;
            n += firstMethod.getSize();
            firstMethod = (MethodWriter)firstMethod.mv;
        }
        int n4 = 0;
        int n5 = n;
        if (this.bootstrapMethods != null) {
            n4 = 0 + 1;
            n5 = n + (this.bootstrapMethods.length + 8);
            this.newUTF8("BootstrapMethods");
        }
        int n6 = n5;
        int n7 = n4;
        if (this.signature != 0) {
            n7 = n4 + 1;
            n6 = n5 + 8;
            this.newUTF8("Signature");
        }
        int n8 = n6;
        int n9 = n7;
        if (this.sourceFile != 0) {
            n9 = n7 + 1;
            n8 = n6 + 8;
            this.newUTF8("SourceFile");
        }
        int n10 = n8;
        int n11 = n9;
        if (this.sourceDebug != null) {
            n11 = n9 + 1;
            n10 = n8 + (this.sourceDebug.length + 6);
            this.newUTF8("SourceDebugExtension");
        }
        int n12 = n10;
        int n13 = n11;
        if (this.enclosingMethodOwner != 0) {
            n13 = n11 + 1;
            n12 = n10 + 10;
            this.newUTF8("EnclosingMethod");
        }
        int n14 = n12;
        int n15 = n13;
        if ((this.access & 0x20000) != 0x0) {
            n15 = n13 + 1;
            n14 = n12 + 6;
            this.newUTF8("Deprecated");
        }
        int n16 = n14;
        int n17 = n15;
        Label_0364: {
            if ((this.access & 0x1000) != 0x0) {
                if ((this.version & 0xFFFF) >= 49) {
                    n16 = n14;
                    n17 = n15;
                    if ((this.access & 0x40000) == 0x0) {
                        break Label_0364;
                    }
                }
                n17 = n15 + 1;
                n16 = n14 + 6;
                this.newUTF8("Synthetic");
            }
        }
        int n18 = n16;
        int n19 = n17;
        if (this.innerClasses != null) {
            n19 = n17 + 1;
            n18 = n16 + (this.innerClasses.length + 8);
            this.newUTF8("InnerClasses");
        }
        int n20 = n18;
        int n21 = n19;
        if (this.anns != null) {
            n21 = n19 + 1;
            n20 = n18 + (this.anns.getSize() + 8);
            this.newUTF8("RuntimeVisibleAnnotations");
        }
        int n22 = n20;
        int n23 = n21;
        if (this.ianns != null) {
            n23 = n21 + 1;
            n22 = n20 + (this.ianns.getSize() + 8);
            this.newUTF8("RuntimeInvisibleAnnotations");
        }
        int n24 = n22;
        int n25 = n23;
        if (this.tanns != null) {
            n25 = n23 + 1;
            n24 = n22 + (this.tanns.getSize() + 8);
            this.newUTF8("RuntimeVisibleTypeAnnotations");
        }
        int n26 = n24;
        int n27 = n25;
        if (this.itanns != null) {
            n27 = n25 + 1;
            n26 = n24 + (this.itanns.getSize() + 8);
            this.newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        if (this.attrs != null) {
            final int count = this.attrs.getCount();
            n26 += this.attrs.getSize(this, null, 0, -1, -1);
            n27 += count;
        }
        final ByteVector byteVector = new ByteVector(n26 + this.pool.length);
        byteVector.putInt(-889275714).putInt(this.version);
        byteVector.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
        byteVector.putShort(this.access & ((this.access & 0x40000) / 64 | 0x60000)).putShort(this.name).putShort(this.superName);
        byteVector.putShort(this.interfaceCount);
        for (int i = 0; i < this.interfaceCount; ++i) {
            byteVector.putShort(this.interfaces[i]);
        }
        byteVector.putShort(n2);
        for (FieldWriter firstField2 = this.firstField; firstField2 != null; firstField2 = (FieldWriter)firstField2.fv) {
            firstField2.put(byteVector);
        }
        byteVector.putShort(n3);
        for (MethodWriter firstMethod2 = this.firstMethod; firstMethod2 != null; firstMethod2 = (MethodWriter)firstMethod2.mv) {
            firstMethod2.put(byteVector);
        }
        byteVector.putShort(n27);
        if (this.bootstrapMethods != null) {
            byteVector.putShort(this.newUTF8("BootstrapMethods"));
            byteVector.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
            byteVector.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
        }
        if (this.signature != 0) {
            byteVector.putShort(this.newUTF8("Signature")).putInt(2).putShort(this.signature);
        }
        if (this.sourceFile != 0) {
            byteVector.putShort(this.newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
        }
        if (this.sourceDebug != null) {
            final int length = this.sourceDebug.length;
            byteVector.putShort(this.newUTF8("SourceDebugExtension")).putInt(length);
            byteVector.putByteArray(this.sourceDebug.data, 0, length);
        }
        if (this.enclosingMethodOwner != 0) {
            byteVector.putShort(this.newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
        }
        if ((this.access & 0x20000) != 0x0) {
            byteVector.putShort(this.newUTF8("Deprecated")).putInt(0);
        }
        if ((this.access & 0x1000) != 0x0 && ((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            byteVector.putShort(this.newUTF8("Synthetic")).putInt(0);
        }
        if (this.innerClasses != null) {
            byteVector.putShort(this.newUTF8("InnerClasses"));
            byteVector.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
            byteVector.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        if (this.anns != null) {
            byteVector.putShort(this.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(this.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(this.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(this.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this, null, 0, -1, -1, byteVector);
        }
        if (this.hasAsmInsns) {
            this.anns = null;
            this.ianns = null;
            this.attrs = null;
            this.innerClassesCount = 0;
            this.innerClasses = null;
            this.firstField = null;
            this.lastField = null;
            this.firstMethod = null;
            this.lastMethod = null;
            this.compute = 1;
            this.hasAsmInsns = false;
            new ClassReader(byteVector.data).accept(this, 264);
            return this.toByteArray();
        }
        return byteVector.data;
    }
    
    @Override
    public final void visit(int i, int access, final String thisName, final String s, final String s2, final String[] array) {
        this.version = i;
        this.access = access;
        this.name = this.newClass(thisName);
        this.thisName = thisName;
        if (s != null) {
            this.signature = this.newUTF8(s);
        }
        access = 0;
        if (s2 == null) {
            i = 0;
        }
        else {
            i = this.newClass(s2);
        }
        this.superName = i;
        if (array != null && array.length > 0) {
            this.interfaceCount = array.length;
            this.interfaces = new int[this.interfaceCount];
            for (i = access; i < this.interfaceCount; ++i) {
                this.interfaces[i] = this.newClass(array[i]);
            }
        }
    }
    
    @Override
    public final AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (b) {
            annotationWriter.next = this.anns;
            return this.anns = annotationWriter;
        }
        annotationWriter.next = this.ianns;
        return this.ianns = annotationWriter;
    }
    
    @Override
    public final void visitAttribute(final Attribute attrs) {
        attrs.next = this.attrs;
        this.attrs = attrs;
    }
    
    @Override
    public final void visitEnd() {
    }
    
    @Override
    public final FieldVisitor visitField(final int n, final String s, final String s2, final String s3, final Object o) {
        return new FieldWriter(this, n, s, s2, s3, o);
    }
    
    @Override
    public final void visitInnerClass(final String s, final String s2, final String s3, final int n) {
        if (this.innerClasses == null) {
            this.innerClasses = new ByteVector();
        }
        final Item classItem = this.newClassItem(s);
        if (classItem.intVal == 0) {
            ++this.innerClassesCount;
            this.innerClasses.putShort(classItem.index);
            final ByteVector innerClasses = this.innerClasses;
            final boolean b = false;
            int class1;
            if (s2 == null) {
                class1 = 0;
            }
            else {
                class1 = this.newClass(s2);
            }
            innerClasses.putShort(class1);
            final ByteVector innerClasses2 = this.innerClasses;
            int utf8;
            if (s3 == null) {
                utf8 = (b ? 1 : 0);
            }
            else {
                utf8 = this.newUTF8(s3);
            }
            innerClasses2.putShort(utf8);
            this.innerClasses.putShort(n);
            classItem.intVal = this.innerClassesCount;
        }
    }
    
    @Override
    public final MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        return new MethodWriter(this, n, s, s2, s3, array, this.compute);
    }
    
    @Override
    public final void visitOuterClass(final String s, final String s2, final String s3) {
        this.enclosingMethodOwner = this.newClass(s);
        if (s2 != null && s3 != null) {
            this.enclosingMethod = this.newNameType(s2, s3);
        }
    }
    
    @Override
    public final void visitSource(final String s, final String s2) {
        if (s != null) {
            this.sourceFile = this.newUTF8(s);
        }
        if (s2 != null) {
            this.sourceDebug = new ByteVector().encodeUTF8(s2, 0, Integer.MAX_VALUE);
        }
    }
    
    @Override
    public final AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(n, typePath, byteVector);
        byteVector.putShort(this.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.tanns;
            return this.tanns = annotationWriter;
        }
        annotationWriter.next = this.itanns;
        return this.itanns = annotationWriter;
    }
}
