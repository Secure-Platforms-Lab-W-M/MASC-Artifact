// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

class MethodWriter extends MethodVisitor
{
    static final int ACC_CONSTRUCTOR = 524288;
    static final int APPEND_FRAME = 252;
    static final int CHOP_FRAME = 248;
    static final int FRAMES = 0;
    static final int FULL_FRAME = 255;
    static final int INSERTED_FRAMES = 1;
    static final int MAXS = 2;
    static final int NOTHING = 3;
    static final int RESERVED = 128;
    static final int SAME_FRAME = 0;
    static final int SAME_FRAME_EXTENDED = 251;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
    private int access;
    private ByteVector annd;
    private AnnotationWriter anns;
    private Attribute attrs;
    private Attribute cattrs;
    int classReaderLength;
    int classReaderOffset;
    private ByteVector code;
    private final int compute;
    private AnnotationWriter ctanns;
    private Label currentBlock;
    private int currentLocals;
    final ClassWriter cw;
    private final int desc;
    private final String descriptor;
    int exceptionCount;
    int[] exceptions;
    private Handler firstHandler;
    private int[] frame;
    private int frameCount;
    private int handlerCount;
    private AnnotationWriter ianns;
    private AnnotationWriter ictanns;
    private AnnotationWriter[] ipanns;
    private AnnotationWriter itanns;
    private Label labels;
    private int lastCodeOffset;
    private Handler lastHandler;
    private ByteVector lineNumber;
    private int lineNumberCount;
    private ByteVector localVar;
    private int localVarCount;
    private ByteVector localVarType;
    private int localVarTypeCount;
    private int maxLocals;
    private int maxStack;
    private int maxStackSize;
    private ByteVector methodParameters;
    private int methodParametersCount;
    private final int name;
    private AnnotationWriter[] panns;
    private Label previousBlock;
    private int[] previousFrame;
    private int previousFrameOffset;
    String signature;
    private ByteVector stackMap;
    private int stackSize;
    private int subroutines;
    private int synthetics;
    private AnnotationWriter tanns;
    
    MethodWriter(final ClassWriter cw, final int access, final String s, final String descriptor, final String signature, final String[] array, int currentLocals) {
        super(327680);
        this.code = new ByteVector();
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        }
        else {
            cw.lastMethod.mv = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        if ("<init>".equals(s)) {
            this.access |= 0x80000;
        }
        this.name = cw.newUTF8(s);
        this.desc = cw.newUTF8(descriptor);
        this.descriptor = descriptor;
        this.signature = signature;
        if (array != null && array.length > 0) {
            this.exceptionCount = array.length;
            this.exceptions = new int[this.exceptionCount];
            for (int i = 0; i < this.exceptionCount; ++i) {
                this.exceptions[i] = cw.newClass(array[i]);
            }
        }
        if ((this.compute = currentLocals) != 3) {
            final int n = currentLocals = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
            if ((access & 0x8) != 0x0) {
                currentLocals = n - 1;
            }
            this.maxLocals = currentLocals;
            this.currentLocals = currentLocals;
            this.labels = new Label();
            final Label labels = this.labels;
            labels.status |= 0x8;
            this.visitLabel(this.labels);
        }
    }
    
    private void addSuccessor(final int info, final Label successor) {
        final Edge successors = new Edge();
        successors.info = info;
        successors.successor = successor;
        successors.next = this.currentBlock.successors;
        this.currentBlock.successors = successors;
    }
    
    private void endFrame() {
        if (this.previousFrame != null) {
            if (this.stackMap == null) {
                this.stackMap = new ByteVector();
            }
            this.writeFrame();
            ++this.frameCount;
        }
        this.previousFrame = this.frame;
        this.frame = null;
    }
    
    private void noSuccessor() {
        if (this.compute == 0) {
            final Label previousBlock = new Label();
            previousBlock.frame = new Frame();
            (previousBlock.frame.owner = previousBlock).resolve(this, this.code.length, this.code.data);
            this.previousBlock.successor = previousBlock;
            this.previousBlock = previousBlock;
        }
        else {
            this.currentBlock.outputStackMax = this.maxStackSize;
        }
        if (this.compute != 1) {
            this.currentBlock = null;
        }
    }
    
    private int startFrame(final int n, final int n2, final int n3) {
        final int n4 = n2 + 3 + n3;
        if (this.frame == null || this.frame.length < n4) {
            this.frame = new int[n4];
        }
        this.frame[0] = n;
        this.frame[1] = n2;
        this.frame[2] = n3;
        return 3;
    }
    
    private void visitFrame(final Frame frame) {
        int n = 0;
        int i = 0;
        final int n2 = 0;
        final int[] inputLocals = frame.inputLocals;
        final int[] inputStack = frame.inputStack;
        int n4;
        for (int j = 0; j < inputLocals.length; j = n4 + 1) {
            final int n3 = inputLocals[j];
            if (n3 == 16777216) {
                ++n;
            }
            else {
                i += n + 1;
                n = 0;
            }
            if (n3 != 16777220) {
                n4 = j;
                if (n3 != 16777219) {
                    continue;
                }
            }
            n4 = j + 1;
        }
        int k = 0;
        int n5 = n2;
        while (k < inputStack.length) {
            final int n6 = inputStack[k];
            final int n7 = n5 + 1;
            int n8 = 0;
            Label_0138: {
                if (n6 != 16777220) {
                    n8 = k;
                    if (n6 != 16777219) {
                        break Label_0138;
                    }
                }
                n8 = k + 1;
            }
            k = n8 + 1;
            n5 = n7;
        }
        int startFrame = this.startFrame(frame.owner.position, i, n5);
        int n9 = 0;
        while (i > 0) {
            final int n10 = inputLocals[n9];
            int n11 = 0;
            Label_0211: {
                if ((this.frame[startFrame] = n10) != 16777220) {
                    n11 = n9;
                    if (n10 != 16777219) {
                        break Label_0211;
                    }
                }
                n11 = n9 + 1;
            }
            n9 = n11 + 1;
            --i;
            ++startFrame;
        }
        int n13;
        for (int l = 0; l < inputStack.length; l = n13 + 1, ++startFrame) {
            final int n12 = inputStack[l];
            if ((this.frame[startFrame] = n12) != 16777220) {
                n13 = l;
                if (n12 != 16777219) {
                    continue;
                }
            }
            n13 = l + 1;
        }
        this.endFrame();
    }
    
    private void visitImplicitFirstFrame() {
        int startFrame = this.startFrame(0, this.descriptor.length() + 1, 0);
        if ((this.access & 0x8) == 0x0) {
            if ((this.access & 0x80000) == 0x0) {
                final int[] frame = this.frame;
                final int n = startFrame + 1;
                frame[startFrame] = (this.cw.addType(this.cw.thisName) | 0x1700000);
                startFrame = n;
            }
            else {
                final int[] frame2 = this.frame;
                final int n2 = startFrame + 1;
                frame2[startFrame] = 6;
                startFrame = n2;
            }
        }
        int n3 = 1;
        int n4 = startFrame;
        while (true) {
            final String descriptor = this.descriptor;
            int n7;
            int n6;
            int n5 = n6 = (n7 = n3 + 1);
            int n10 = 0;
            switch (descriptor.charAt(n3)) {
                default: {
                    this.frame[1] = n4 - 3;
                    this.endFrame();
                    return;
                }
                case '[': {
                    while (this.descriptor.charAt(n7) == '[') {
                        ++n7;
                    }
                    int n8 = n7;
                    if (this.descriptor.charAt(n7) == 'L') {
                        int n9 = n7 + 1;
                        while (true) {
                            n8 = n9;
                            if (this.descriptor.charAt(n9) == ';') {
                                break;
                            }
                            ++n9;
                        }
                    }
                    final int[] frame3 = this.frame;
                    n10 = n4 + 1;
                    final ClassWriter cw = this.cw;
                    final String descriptor2 = this.descriptor;
                    n5 = n8 + 1;
                    frame3[n4] = (cw.addType(descriptor2.substring(n3, n5)) | 0x1700000);
                    break;
                }
                case 'L': {
                    while (this.descriptor.charAt(n6) != ';') {
                        ++n6;
                    }
                    this.frame[n4] = (this.cw.addType(this.descriptor.substring(n3 + 1, n6)) | 0x1700000);
                    n10 = n4 + 1;
                    n5 = n6 + 1;
                    break;
                }
                case 'J': {
                    final int[] frame4 = this.frame;
                    n10 = n4 + 1;
                    frame4[n4] = 4;
                    break;
                }
                case 'F': {
                    final int[] frame5 = this.frame;
                    n10 = n4 + 1;
                    frame5[n4] = 2;
                    break;
                }
                case 'D': {
                    final int[] frame6 = this.frame;
                    n10 = n4 + 1;
                    frame6[n4] = 3;
                    break;
                }
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z': {
                    final int[] frame7 = this.frame;
                    n10 = n4 + 1;
                    frame7[n4] = 1;
                    break;
                }
            }
            n3 = n5;
            n4 = n10;
        }
    }
    
    private void visitSwitchInsn(Label label, final Label[] array) {
        if (this.currentBlock != null) {
            final int compute = this.compute;
            int i = 0;
            if (compute == 0) {
                this.currentBlock.frame.execute(171, 0, null, null);
                this.addSuccessor(0, label);
                label = label.getFirst();
                label.status |= 0x10;
                for (int j = 0; j < array.length; ++j) {
                    this.addSuccessor(0, array[j]);
                    label = array[j].getFirst();
                    label.status |= 0x10;
                }
            }
            else {
                this.addSuccessor(--this.stackSize, label);
                while (i < array.length) {
                    this.addSuccessor(this.stackSize, array[i]);
                    ++i;
                }
            }
            this.noSuccessor();
        }
    }
    
    private void writeFrame() {
        final int n = this.frame[1];
        final int n2 = this.frame[2];
        final int version = this.cw.version;
        final int n3 = 0;
        if ((version & 0xFFFF) < 50) {
            this.stackMap.putShort(this.frame[0]).putShort(n);
            this.writeFrameTypes(3, n + 3);
            this.stackMap.putShort(n2);
            this.writeFrameTypes(n + 3, n + 3 + n2);
            return;
        }
        int n4 = this.previousFrame[1];
        final int n5 = 255;
        final int n6 = 0;
        int n7;
        if (this.frameCount == 0) {
            n7 = this.frame[0];
        }
        else {
            n7 = this.frame[0] - this.previousFrame[0] - 1;
        }
        int n8;
        int n9 = 0;
        int n10;
        if (n2 == 0) {
            n8 = n - n4;
            switch (n8) {
                default: {
                    n9 = n5;
                    break;
                }
                case 1:
                case 2:
                case 3: {
                    n9 = 252;
                    break;
                }
                case 0: {
                    if (n7 < 64) {
                        n9 = 0;
                    }
                    else {
                        n9 = 251;
                    }
                    break;
                }
                case -3:
                case -2:
                case -1: {
                    n9 = 248;
                    n4 = n;
                    break;
                }
            }
            n10 = n4;
        }
        else {
            n10 = n4;
            n9 = n5;
            n8 = n6;
            if (n == n4) {
                n10 = n4;
                n9 = n5;
                n8 = n6;
                if (n2 == 1) {
                    if (n7 < 63) {
                        n9 = 64;
                    }
                    else {
                        n9 = 247;
                    }
                    n8 = n6;
                    n10 = n4;
                }
            }
        }
        int n11;
        if ((n11 = n9) != 255) {
            int n12 = 3;
            int n13 = n3;
            while (true) {
                n11 = n9;
                if (n13 >= n10) {
                    break;
                }
                if (this.frame[n12] != this.previousFrame[n12]) {
                    n11 = 255;
                    break;
                }
                ++n12;
                ++n13;
            }
        }
        if (n11 == 0) {
            this.stackMap.putByte(n7);
            return;
        }
        if (n11 == 64) {
            this.stackMap.putByte(n7 + 64);
            this.writeFrameTypes(n + 3, n + 4);
            return;
        }
        switch (n11) {
            default: {
                switch (n11) {
                    default: {
                        this.stackMap.putByte(255).putShort(n7).putShort(n);
                        this.writeFrameTypes(3, n + 3);
                        this.stackMap.putShort(n2);
                        this.writeFrameTypes(n + 3, n + 3 + n2);
                        return;
                    }
                    case 252: {
                        this.stackMap.putByte(n8 + 251).putShort(n7);
                        this.writeFrameTypes(n10 + 3, n + 3);
                        return;
                    }
                    case 251: {
                        this.stackMap.putByte(251).putShort(n7);
                        return;
                    }
                }
                break;
            }
            case 248: {
                this.stackMap.putByte(n8 + 251).putShort(n7);
            }
            case 247: {
                this.stackMap.putByte(247).putShort(n7);
                this.writeFrameTypes(n + 3, n + 4);
            }
        }
    }
    
    private void writeFrameType(final Object o) {
        if (o instanceof String) {
            this.stackMap.putByte(7).putShort(this.cw.newClass((String)o));
            return;
        }
        if (o instanceof Integer) {
            this.stackMap.putByte((int)o);
            return;
        }
        this.stackMap.putByte(8).putShort(((Label)o).position);
    }
    
    private void writeFrameTypes(int i, final int n) {
        while (i < n) {
            final int n2 = this.frame[i];
            final int n3 = 0xF0000000 & n2;
            if (n3 == 0) {
                final int n4 = 0xFFFFF & n2;
                final int n5 = 0xFF00000 & n2;
                if (n5 != 24117248) {
                    if (n5 != 25165824) {
                        this.stackMap.putByte(n4);
                    }
                    else {
                        this.stackMap.putByte(8).putShort(this.cw.typeTable[n4].intVal);
                    }
                }
                else {
                    this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[n4].strVal1));
                }
            }
            else {
                final StringBuilder sb = new StringBuilder();
                for (int j = n3 >> 28; j > 0; --j) {
                    sb.append('[');
                }
                Label_0369: {
                    if ((n2 & 0xFF00000) == 0x1700000) {
                        sb.append('L');
                        sb.append(this.cw.typeTable[n2 & 0xFFFFF].strVal1);
                        sb.append(';');
                    }
                    else {
                        final int n6 = n2 & 0xF;
                        switch (n6) {
                            default: {
                                switch (n6) {
                                    default: {
                                        sb.append('J');
                                        break Label_0369;
                                    }
                                    case 12: {
                                        sb.append('S');
                                        break Label_0369;
                                    }
                                    case 11: {
                                        sb.append('C');
                                        break Label_0369;
                                    }
                                    case 10: {
                                        sb.append('B');
                                        break Label_0369;
                                    }
                                    case 9: {
                                        sb.append('Z');
                                        break Label_0369;
                                    }
                                }
                                break;
                            }
                            case 3: {
                                sb.append('D');
                                break;
                            }
                            case 2: {
                                sb.append('F');
                                break;
                            }
                            case 1: {
                                sb.append('I');
                                break;
                            }
                        }
                    }
                }
                this.stackMap.putByte(7).putShort(this.cw.newClass(sb.toString()));
            }
            ++i;
        }
    }
    
    final int getSize() {
        if (this.classReaderOffset != 0) {
            return this.classReaderLength + 6;
        }
        int n = 8;
        if (this.code.length > 0) {
            if (this.code.length > 65535) {
                throw new RuntimeException("Method code too large!");
            }
            this.cw.newUTF8("Code");
            int n3;
            final int n2 = n3 = 8 + (this.code.length + 18 + this.handlerCount * 8);
            if (this.localVar != null) {
                this.cw.newUTF8("LocalVariableTable");
                n3 = n2 + (this.localVar.length + 8);
            }
            int n4 = n3;
            if (this.localVarType != null) {
                this.cw.newUTF8("LocalVariableTypeTable");
                n4 = n3 + (this.localVarType.length + 8);
            }
            int n5 = n4;
            if (this.lineNumber != null) {
                this.cw.newUTF8("LineNumberTable");
                n5 = n4 + (this.lineNumber.length + 8);
            }
            int n6 = n5;
            if (this.stackMap != null) {
                final boolean b = (this.cw.version & 0xFFFF) >= 50;
                final ClassWriter cw = this.cw;
                String s;
                if (b) {
                    s = "StackMapTable";
                }
                else {
                    s = "StackMap";
                }
                cw.newUTF8(s);
                n6 = n5 + (this.stackMap.length + 8);
            }
            int n7 = n6;
            if (this.ctanns != null) {
                this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
                n7 = n6 + (this.ctanns.getSize() + 8);
            }
            int n8 = n7;
            if (this.ictanns != null) {
                this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
                n8 = n7 + (this.ictanns.getSize() + 8);
            }
            n = n8;
            if (this.cattrs != null) {
                n = n8 + this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        int n9 = n;
        if (this.exceptionCount > 0) {
            this.cw.newUTF8("Exceptions");
            n9 = n + (this.exceptionCount * 2 + 8);
        }
        int n10 = n9;
        Label_0464: {
            if ((this.access & 0x1000) != 0x0) {
                if ((this.cw.version & 0xFFFF) >= 49) {
                    n10 = n9;
                    if ((this.access & 0x40000) == 0x0) {
                        break Label_0464;
                    }
                }
                this.cw.newUTF8("Synthetic");
                n10 = n9 + 6;
            }
        }
        int n11 = n10;
        if ((this.access & 0x20000) != 0x0) {
            this.cw.newUTF8("Deprecated");
            n11 = n10 + 6;
        }
        int n12 = n11;
        if (this.signature != null) {
            this.cw.newUTF8("Signature");
            this.cw.newUTF8(this.signature);
            n12 = n11 + 8;
        }
        int n13 = n12;
        if (this.methodParameters != null) {
            this.cw.newUTF8("MethodParameters");
            n13 = n12 + (this.methodParameters.length + 7);
        }
        int n14 = n13;
        if (this.annd != null) {
            this.cw.newUTF8("AnnotationDefault");
            n14 = n13 + (this.annd.length + 6);
        }
        int n15 = n14;
        if (this.anns != null) {
            this.cw.newUTF8("RuntimeVisibleAnnotations");
            n15 = n14 + (this.anns.getSize() + 8);
        }
        int n16 = n15;
        if (this.ianns != null) {
            this.cw.newUTF8("RuntimeInvisibleAnnotations");
            n16 = n15 + (this.ianns.getSize() + 8);
        }
        int n17 = n16;
        if (this.tanns != null) {
            this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            n17 = n16 + (this.tanns.getSize() + 8);
        }
        int n18 = n17;
        if (this.itanns != null) {
            this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            n18 = n17 + (this.itanns.getSize() + 8);
        }
        int n19 = n18;
        if (this.panns != null) {
            this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
            int n20 = n18 + ((this.panns.length - this.synthetics) * 2 + 7);
            int n21 = this.panns.length - 1;
            while (true) {
                n19 = n20;
                if (n21 < this.synthetics) {
                    break;
                }
                int size;
                if (this.panns[n21] == null) {
                    size = 0;
                }
                else {
                    size = this.panns[n21].getSize();
                }
                n20 += size;
                --n21;
            }
        }
        int n22 = n19;
        if (this.ipanns != null) {
            this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            int n23 = n19 + ((this.ipanns.length - this.synthetics) * 2 + 7);
            int n24 = this.ipanns.length - 1;
            while (true) {
                n22 = n23;
                if (n24 < this.synthetics) {
                    break;
                }
                int size2;
                if (this.ipanns[n24] == null) {
                    size2 = 0;
                }
                else {
                    size2 = this.ipanns[n24].getSize();
                }
                n23 += size2;
                --n24;
            }
        }
        int n25 = n22;
        if (this.attrs != null) {
            n25 = n22 + this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return n25;
    }
    
    final void put(final ByteVector byteVector) {
        byteVector.putShort(this.access & ((this.access & 0x40000) / 64 | 0xE0000)).putShort(this.name).putShort(this.desc);
        if (this.classReaderOffset != 0) {
            byteVector.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
            return;
        }
        int n = 0;
        if (this.code.length > 0) {
            n = 0 + 1;
        }
        int n2 = n;
        if (this.exceptionCount > 0) {
            n2 = n + 1;
        }
        int n3 = n2;
        Label_0147: {
            if ((this.access & 0x1000) != 0x0) {
                if ((this.cw.version & 0xFFFF) >= 49) {
                    n3 = n2;
                    if ((this.access & 0x40000) == 0x0) {
                        break Label_0147;
                    }
                }
                n3 = n2 + 1;
            }
        }
        int n4 = n3;
        if ((this.access & 0x20000) != 0x0) {
            n4 = n3 + 1;
        }
        int n5 = n4;
        if (this.signature != null) {
            n5 = n4 + 1;
        }
        int n6 = n5;
        if (this.methodParameters != null) {
            n6 = n5 + 1;
        }
        int n7 = n6;
        if (this.annd != null) {
            n7 = n6 + 1;
        }
        int n8 = n7;
        if (this.anns != null) {
            n8 = n7 + 1;
        }
        int n9 = n8;
        if (this.ianns != null) {
            n9 = n8 + 1;
        }
        int n10 = n9;
        if (this.tanns != null) {
            n10 = n9 + 1;
        }
        int n11 = n10;
        if (this.itanns != null) {
            n11 = n10 + 1;
        }
        int n12 = n11;
        if (this.panns != null) {
            n12 = n11 + 1;
        }
        int n13 = n12;
        if (this.ipanns != null) {
            n13 = n12 + 1;
        }
        int n14 = n13;
        if (this.attrs != null) {
            n14 = n13 + this.attrs.getCount();
        }
        byteVector.putShort(n14);
        if (this.code.length > 0) {
            int n16;
            final int n15 = n16 = this.code.length + 12 + this.handlerCount * 8;
            if (this.localVar != null) {
                n16 = n15 + (this.localVar.length + 8);
            }
            int n17 = n16;
            if (this.localVarType != null) {
                n17 = n16 + (this.localVarType.length + 8);
            }
            int n18 = n17;
            if (this.lineNumber != null) {
                n18 = n17 + (this.lineNumber.length + 8);
            }
            int n19 = n18;
            if (this.stackMap != null) {
                n19 = n18 + (this.stackMap.length + 8);
            }
            int n20 = n19;
            if (this.ctanns != null) {
                n20 = n19 + (this.ctanns.getSize() + 8);
            }
            int n21 = n20;
            if (this.ictanns != null) {
                n21 = n20 + (this.ictanns.getSize() + 8);
            }
            int n22 = n21;
            if (this.cattrs != null) {
                n22 = n21 + this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
            byteVector.putShort(this.cw.newUTF8("Code")).putInt(n22);
            byteVector.putShort(this.maxStack).putShort(this.maxLocals);
            byteVector.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            byteVector.putShort(this.handlerCount);
            if (this.handlerCount > 0) {
                for (Handler handler = this.firstHandler; handler != null; handler = handler.next) {
                    byteVector.putShort(handler.start.position).putShort(handler.end.position).putShort(handler.handler.position).putShort(handler.type);
                }
            }
            int n23 = 0;
            if (this.localVar != null) {
                n23 = 0 + 1;
            }
            int n24 = n23;
            if (this.localVarType != null) {
                n24 = n23 + 1;
            }
            int n25 = n24;
            if (this.lineNumber != null) {
                n25 = n24 + 1;
            }
            int n26 = n25;
            if (this.stackMap != null) {
                n26 = n25 + 1;
            }
            int n27 = n26;
            if (this.ctanns != null) {
                n27 = n26 + 1;
            }
            int n28 = n27;
            if (this.ictanns != null) {
                n28 = n27 + 1;
            }
            int n29 = n28;
            if (this.cattrs != null) {
                n29 = n28 + this.cattrs.getCount();
            }
            byteVector.putShort(n29);
            if (this.localVar != null) {
                byteVector.putShort(this.cw.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.localVar.length + 2).putShort(this.localVarCount);
                byteVector.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            if (this.localVarType != null) {
                byteVector.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
                byteVector.putByteArray(this.localVarType.data, 0, this.localVarType.length);
            }
            if (this.lineNumber != null) {
                byteVector.putShort(this.cw.newUTF8("LineNumberTable"));
                byteVector.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
                byteVector.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
            if (this.stackMap != null) {
                final boolean b = (this.cw.version & 0xFFFF) >= 50;
                final ClassWriter cw = this.cw;
                String s;
                if (b) {
                    s = "StackMapTable";
                }
                else {
                    s = "StackMap";
                }
                byteVector.putShort(cw.newUTF8(s));
                byteVector.putInt(this.stackMap.length + 2).putShort(this.frameCount);
                byteVector.putByteArray(this.stackMap.data, 0, this.stackMap.length);
            }
            if (this.ctanns != null) {
                byteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
                this.ctanns.put(byteVector);
            }
            if (this.ictanns != null) {
                byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
                this.ictanns.put(byteVector);
            }
            if (this.cattrs != null) {
                this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, byteVector);
            }
        }
        if (this.exceptionCount > 0) {
            byteVector.putShort(this.cw.newUTF8("Exceptions")).putInt(this.exceptionCount * 2 + 2);
            byteVector.putShort(this.exceptionCount);
            for (int i = 0; i < this.exceptionCount; ++i) {
                byteVector.putShort(this.exceptions[i]);
            }
        }
        if ((this.access & 0x1000) != 0x0 && ((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            byteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 0x20000) != 0x0) {
            byteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != null) {
            byteVector.putShort(this.cw.newUTF8("Signature")).putInt(2).putShort(this.cw.newUTF8(this.signature));
        }
        if (this.methodParameters != null) {
            byteVector.putShort(this.cw.newUTF8("MethodParameters"));
            byteVector.putInt(this.methodParameters.length + 1).putByte(this.methodParametersCount);
            byteVector.putByteArray(this.methodParameters.data, 0, this.methodParameters.length);
        }
        if (this.annd != null) {
            byteVector.putShort(this.cw.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.annd.length);
            byteVector.putByteArray(this.annd.data, 0, this.annd.length);
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
        if (this.panns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.put(this.panns, this.synthetics, byteVector);
        }
        if (this.ipanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.put(this.ipanns, this.synthetics, byteVector);
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
    public AnnotationVisitor visitAnnotationDefault() {
        this.annd = new ByteVector();
        return new AnnotationWriter(this.cw, false, this.annd, null, 0);
    }
    
    @Override
    public void visitAttribute(final Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.next = this.cattrs;
            this.cattrs = attribute;
            return;
        }
        attribute.next = this.attrs;
        this.attrs = attribute;
    }
    
    @Override
    public void visitCode() {
    }
    
    @Override
    public void visitEnd() {
    }
    
    @Override
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        this.lastCodeOffset = this.code.length;
        final Item fieldItem = this.cw.newFieldItem(s, s2, s3);
        Label_0308: {
            if (this.currentBlock != null) {
                final int compute = this.compute;
                final int n2 = 0;
                if (compute != 0) {
                    final int compute2 = this.compute;
                    final int n3 = 1;
                    if (compute2 != 1) {
                        final char char1 = s3.charAt(0);
                        int n4 = -2;
                        int n5 = 0;
                        switch (n) {
                            default: {
                                final int stackSize = this.stackSize;
                                if (char1 == 'D' || char1 == 'J') {
                                    n4 = -3;
                                }
                                n5 = n4 + stackSize;
                                break;
                            }
                            case 180: {
                                final int stackSize2 = this.stackSize;
                                int n6;
                                if (char1 != 'D' && char1 != 'J') {
                                    n6 = n2;
                                }
                                else {
                                    n6 = 1;
                                }
                                n5 = stackSize2 + n6;
                                break;
                            }
                            case 179: {
                                final int stackSize3 = this.stackSize;
                                int n7 = n4;
                                if (char1 != 'D') {
                                    if (char1 == 'J') {
                                        n7 = n4;
                                    }
                                    else {
                                        n7 = -1;
                                    }
                                }
                                n5 = n7 + stackSize3;
                                break;
                            }
                            case 178: {
                                final int stackSize4 = this.stackSize;
                                int n8;
                                if (char1 != 'D' && char1 != 'J') {
                                    n8 = n3;
                                }
                                else {
                                    n8 = 2;
                                }
                                n5 = stackSize4 + n8;
                                break;
                            }
                        }
                        if (n5 > this.maxStackSize) {
                            this.maxStackSize = n5;
                        }
                        this.stackSize = n5;
                        break Label_0308;
                    }
                }
                this.currentBlock.frame.execute(n, 0, this.cw, fieldItem);
            }
        }
        this.code.put12(n, fieldItem.index);
    }
    
    @Override
    public void visitFrame(int i, int j, final Object[] array, final int n, final Object[] array2) {
        if (this.compute == 0) {
            return;
        }
        if (this.compute == 1) {
            if (this.currentBlock.frame == null) {
                this.currentBlock.frame = new CurrentFrame();
                this.currentBlock.frame.owner = this.currentBlock;
                this.currentBlock.frame.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), j);
                this.visitImplicitFirstFrame();
            }
            else {
                if (i == -1) {
                    this.currentBlock.frame.set(this.cw, j, array, n, array2);
                }
                this.visitFrame(this.currentBlock.frame);
            }
        }
        else {
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            if (i == -1) {
                if (this.previousFrame == null) {
                    this.visitImplicitFirstFrame();
                }
                this.currentLocals = j;
                i = this.startFrame(this.code.length, j, n);
                for (int k = 0; k < j; ++k) {
                    if (array[k] instanceof String) {
                        final int[] frame = this.frame;
                        final int n5 = i + 1;
                        frame[i] = (0x1700000 | this.cw.addType((String)array[k]));
                        i = n5;
                    }
                    else if (array[k] instanceof Integer) {
                        this.frame[i] = (int)array[k];
                        ++i;
                    }
                    else {
                        final int[] frame2 = this.frame;
                        final int n6 = i + 1;
                        frame2[i] = (0x1800000 | this.cw.addUninitializedType("", ((Label)array[k]).position));
                        i = n6;
                    }
                }
                int[] frame3;
                int n7;
                int[] frame4;
                int n8;
                int[] frame5;
                int n9;
                for (j = n4; j < n; ++j) {
                    if (array2[j] instanceof String) {
                        frame3 = this.frame;
                        n7 = i + 1;
                        frame3[i] = (this.cw.addType((String)array2[j]) | 0x1700000);
                        i = n7;
                    }
                    else if (array2[j] instanceof Integer) {
                        frame4 = this.frame;
                        n8 = i + 1;
                        frame4[i] = (int)array2[j];
                        i = n8;
                    }
                    else {
                        frame5 = this.frame;
                        n9 = i + 1;
                        frame5[i] = (this.cw.addUninitializedType("", ((Label)array2[j]).position) | 0x1800000);
                        i = n9;
                    }
                }
                this.endFrame();
            }
            else {
                int length;
                if (this.stackMap == null) {
                    this.stackMap = new ByteVector();
                    length = this.code.length;
                }
                else if ((length = this.code.length - this.previousFrameOffset - 1) < 0) {
                    if (i == 3) {
                        return;
                    }
                    throw new IllegalStateException();
                }
                switch (i) {
                    case 4: {
                        if (length < 64) {
                            this.stackMap.putByte(length + 64);
                        }
                        else {
                            this.stackMap.putByte(247).putShort(length);
                        }
                        this.writeFrameType(array2[0]);
                        break;
                    }
                    case 3: {
                        if (length < 64) {
                            this.stackMap.putByte(length);
                            break;
                        }
                        this.stackMap.putByte(251).putShort(length);
                        break;
                    }
                    case 2: {
                        this.currentLocals -= j;
                        this.stackMap.putByte(251 - j).putShort(length);
                        break;
                    }
                    case 1: {
                        this.currentLocals += j;
                        this.stackMap.putByte(j + 251).putShort(length);
                        for (i = n2; i < j; ++i) {
                            this.writeFrameType(array[i]);
                        }
                        break;
                    }
                    case 0: {
                        this.currentLocals = j;
                        this.stackMap.putByte(255).putShort(length).putShort(j);
                        for (i = 0; i < j; ++i) {
                            this.writeFrameType(array[i]);
                        }
                        this.stackMap.putShort(n);
                        for (i = n3; i < n; ++i) {
                            this.writeFrameType(array2[i]);
                        }
                        break;
                    }
                }
                this.previousFrameOffset = this.code.length;
                ++this.frameCount;
            }
        }
        this.maxStack = Math.max(this.maxStack, n);
        this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
    }
    
    @Override
    public void visitIincInsn(final int n, final int n2) {
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null && (this.compute == 0 || this.compute == 1)) {
            this.currentBlock.frame.execute(132, n, null, null);
        }
        if (this.compute != 3) {
            final int maxLocals = n + 1;
            if (maxLocals > this.maxLocals) {
                this.maxLocals = maxLocals;
            }
        }
        if (n <= 255 && n2 <= 127 && n2 >= -128) {
            this.code.putByte(132).put11(n, n2);
            return;
        }
        this.code.putByte(196).put12(132, n).putShort(n2);
    }
    
    @Override
    public void visitInsn(final int n) {
        this.lastCodeOffset = this.code.length;
        this.code.putByte(n);
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                final int n2 = this.stackSize + Frame.SIZE[n];
                if (n2 > this.maxStackSize) {
                    this.maxStackSize = n2;
                }
                this.stackSize = n2;
            }
            else {
                this.currentBlock.frame.execute(n, 0, null, null);
            }
            if ((n >= 172 && n <= 177) || n == 191) {
                this.noSuccessor();
            }
        }
    }
    
    @Override
    public AnnotationVisitor visitInsnAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget((0xFF0000FF & n) | this.lastCodeOffset << 8, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.ctanns;
            return this.ctanns = annotationWriter;
        }
        annotationWriter.next = this.ictanns;
        return this.ictanns = annotationWriter;
    }
    
    @Override
    public void visitIntInsn(final int n, final int n2) {
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                if (n != 188) {
                    final int n3 = this.stackSize + 1;
                    if (n3 > this.maxStackSize) {
                        this.maxStackSize = n3;
                    }
                    this.stackSize = n3;
                }
            }
            else {
                this.currentBlock.frame.execute(n, n2, null, null);
            }
        }
        if (n == 17) {
            this.code.put12(n, n2);
            return;
        }
        this.code.put11(n, n2);
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        this.lastCodeOffset = this.code.length;
        final Item invokeDynamicItem = this.cw.newInvokeDynamicItem(s, s2, handle, array);
        final int intVal = invokeDynamicItem.intVal;
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                int argumentsAndReturnSizes;
                if ((argumentsAndReturnSizes = intVal) == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(s2);
                    invokeDynamicItem.intVal = argumentsAndReturnSizes;
                }
                final int n = this.stackSize - (argumentsAndReturnSizes >> 2) + (argumentsAndReturnSizes & 0x3) + 1;
                if (n > this.maxStackSize) {
                    this.maxStackSize = n;
                }
                this.stackSize = n;
            }
            else {
                this.currentBlock.frame.execute(186, 0, this.cw, invokeDynamicItem);
            }
        }
        this.code.put12(186, invokeDynamicItem.index);
        this.code.putShort(0);
    }
    
    @Override
    public void visitJumpInsn(int n, final Label label) {
        final boolean b = n >= 200;
        if (b) {
            n -= 33;
        }
        this.lastCodeOffset = this.code.length;
        Label label3;
        final Label label2 = label3 = null;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(n, 0, null, null);
                final Label first = label.getFirst();
                first.status |= 0x10;
                this.addSuccessor(0, label);
                label3 = label2;
                if (n != 167) {
                    label3 = new Label();
                }
            }
            else if (this.compute == 1) {
                this.currentBlock.frame.execute(n, 0, null, null);
                label3 = label2;
            }
            else if (n == 168) {
                if ((label.status & 0x200) == 0x0) {
                    label.status |= 0x200;
                    ++this.subroutines;
                }
                final Label currentBlock = this.currentBlock;
                currentBlock.status |= 0x80;
                this.addSuccessor(this.stackSize + 1, label);
                label3 = new Label();
            }
            else {
                this.addSuccessor(this.stackSize += Frame.SIZE[n], label);
                label3 = label2;
            }
        }
        if ((label.status & 0x2) != 0x0 && label.position - this.code.length < -32768) {
            if (n == 167) {
                this.code.putByte(200);
            }
            else if (n == 168) {
                this.code.putByte(201);
            }
            else {
                if (label3 != null) {
                    label3.status |= 0x10;
                }
                final ByteVector code = this.code;
                int n2;
                if (n <= 166) {
                    n2 = (n + 1 ^ 0x1) - 1;
                }
                else {
                    n2 = (n ^ 0x1);
                }
                code.putByte(n2);
                this.code.putShort(8);
                this.code.putByte(200);
            }
            label.put(this, this.code, this.code.length - 1, true);
        }
        else if (b) {
            this.code.putByte(n + 33);
            label.put(this, this.code, this.code.length - 1, true);
        }
        else {
            this.code.putByte(n);
            label.put(this, this.code, this.code.length - 1, false);
        }
        if (this.currentBlock != null) {
            if (label3 != null) {
                this.visitLabel(label3);
            }
            if (n == 167) {
                this.noSuccessor();
            }
        }
    }
    
    @Override
    public void visitLabel(final Label owner) {
        final ClassWriter cw = this.cw;
        cw.hasAsmInsns |= owner.resolve(this, this.code.length, this.code.data);
        if ((owner.status & 0x1) != 0x0) {
            return;
        }
        if (this.compute == 0) {
            if (this.currentBlock != null) {
                if (owner.position == this.currentBlock.position) {
                    final Label currentBlock = this.currentBlock;
                    currentBlock.status |= (owner.status & 0x10);
                    owner.frame = this.currentBlock.frame;
                    return;
                }
                this.addSuccessor(0, owner);
            }
            this.currentBlock = owner;
            if (owner.frame == null) {
                owner.frame = new Frame();
                owner.frame.owner = owner;
            }
            if (this.previousBlock != null) {
                if (owner.position == this.previousBlock.position) {
                    final Label previousBlock = this.previousBlock;
                    previousBlock.status |= (owner.status & 0x10);
                    owner.frame = this.previousBlock.frame;
                    this.currentBlock = this.previousBlock;
                    return;
                }
                this.previousBlock.successor = owner;
            }
            this.previousBlock = owner;
            return;
        }
        if (this.compute != 1) {
            if (this.compute == 2) {
                if (this.currentBlock != null) {
                    this.currentBlock.outputStackMax = this.maxStackSize;
                    this.addSuccessor(this.stackSize, owner);
                }
                this.currentBlock = owner;
                this.stackSize = 0;
                this.maxStackSize = 0;
                if (this.previousBlock != null) {
                    this.previousBlock.successor = owner;
                }
                this.previousBlock = owner;
            }
            return;
        }
        if (this.currentBlock == null) {
            this.currentBlock = owner;
            return;
        }
        this.currentBlock.frame.owner = owner;
    }
    
    @Override
    public void visitLdcInsn(final Object o) {
        this.lastCodeOffset = this.code.length;
        final Item constItem = this.cw.newConstItem(o);
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                int n;
                if (constItem.type != 5 && constItem.type != 6) {
                    n = this.stackSize + 1;
                }
                else {
                    n = this.stackSize + 2;
                }
                if (n > this.maxStackSize) {
                    this.maxStackSize = n;
                }
                this.stackSize = n;
            }
            else {
                this.currentBlock.frame.execute(18, 0, this.cw, constItem);
            }
        }
        final int index = constItem.index;
        if (constItem.type == 5 || constItem.type == 6) {
            this.code.put12(20, index);
            return;
        }
        if (index >= 256) {
            this.code.put12(19, index);
            return;
        }
        this.code.put11(18, index);
    }
    
    @Override
    public void visitLineNumber(final int n, final Label label) {
        if (this.lineNumber == null) {
            this.lineNumber = new ByteVector();
        }
        ++this.lineNumberCount;
        this.lineNumber.putShort(label.position);
        this.lineNumber.putShort(n);
    }
    
    @Override
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, int maxLocals) {
        int n = 1;
        if (s3 != null) {
            if (this.localVarType == null) {
                this.localVarType = new ByteVector();
            }
            ++this.localVarTypeCount;
            this.localVarType.putShort(label.position).putShort(label2.position - label.position).putShort(this.cw.newUTF8(s)).putShort(this.cw.newUTF8(s3)).putShort(maxLocals);
        }
        if (this.localVar == null) {
            this.localVar = new ByteVector();
        }
        ++this.localVarCount;
        this.localVar.putShort(label.position).putShort(label2.position - label.position).putShort(this.cw.newUTF8(s)).putShort(this.cw.newUTF8(s2)).putShort(maxLocals);
        if (this.compute != 3) {
            final char char1 = s2.charAt(0);
            if (char1 == 'J' || char1 == 'D') {
                n = 2;
            }
            maxLocals += n;
            if (maxLocals > this.maxLocals) {
                this.maxLocals = maxLocals;
            }
        }
    }
    
    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int i, final TypePath typePath, final Label[] array, final Label[] array2, final int[] array3, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        byteVector.putByte(i >>> 24).putShort(array.length);
        for (i = 0; i < array.length; ++i) {
            byteVector.putShort(array[i].position).putShort(array2[i].position - array[i].position).putShort(array3[i]);
        }
        if (typePath == null) {
            byteVector.putByte(0);
        }
        else {
            i = typePath.b[typePath.offset];
            byteVector.putByteArray(typePath.b, typePath.offset, i * 2 + 1);
        }
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.ctanns;
            return this.ctanns = annotationWriter;
        }
        annotationWriter.next = this.ictanns;
        return this.ictanns = annotationWriter;
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        this.lastCodeOffset = this.code.length;
        final int length = this.code.length;
        this.code.putByte(171);
        final ByteVector code = this.code;
        final int length2 = this.code.length;
        int i = 0;
        code.putByteArray(null, 0, (4 - length2 % 4) % 4);
        label.put(this, this.code, length, true);
        this.code.putInt(array2.length);
        while (i < array2.length) {
            this.code.putInt(array[i]);
            array2[i].put(this, this.code, length, true);
            ++i;
        }
        this.visitSwitchInsn(label, array2);
    }
    
    @Override
    public void visitMaxs(int i, int maxLocals) {
        if (this.compute == 0) {
            for (Handler handler = this.firstHandler; handler != null; handler = handler.next) {
                final Label first = handler.start.getFirst();
                final Label first2 = handler.handler.getFirst();
                final Label first3 = handler.end.getFirst();
                String desc;
                if (handler.desc == null) {
                    desc = "java/lang/Throwable";
                }
                else {
                    desc = handler.desc;
                }
                i = this.cw.addType(desc);
                first2.status |= 0x10;
                for (Label successor = first; successor != first3; successor = successor.successor) {
                    final Edge successors = new Edge();
                    successors.info = (0x1700000 | i);
                    successors.successor = first2;
                    successors.next = successor.successors;
                    successor.successors = successors;
                }
            }
            final Frame frame = this.labels.frame;
            frame.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), this.maxLocals);
            this.visitFrame(frame);
            i = 0;
            Label labels = this.labels;
            while (labels != null) {
                final Label next = labels.next;
                labels.next = null;
                final Frame frame2 = labels.frame;
                if ((labels.status & 0x10) != 0x0) {
                    labels.status |= 0x20;
                }
                labels.status |= 0x40;
                final int n = frame2.inputStack.length + labels.outputStackMax;
                if (n > (maxLocals = i)) {
                    maxLocals = n;
                }
                Edge edge = labels.successors;
                labels = next;
                while (edge != null) {
                    final Label first4 = edge.successor.getFirst();
                    Label label = labels;
                    if (frame2.merge(this.cw, first4.frame, edge.info)) {
                        label = labels;
                        if (first4.next == null) {
                            first4.next = labels;
                            label = first4;
                        }
                    }
                    edge = edge.next;
                    labels = label;
                }
                i = maxLocals;
            }
            for (Label label2 = this.labels; label2 != null; label2 = label2.successor, i = maxLocals) {
                final Frame frame3 = label2.frame;
                if ((label2.status & 0x20) != 0x0) {
                    this.visitFrame(frame3);
                }
                maxLocals = i;
                if ((label2.status & 0x40) == 0x0) {
                    final Label successor2 = label2.successor;
                    final int position = label2.position;
                    if (successor2 == null) {
                        maxLocals = this.code.length;
                    }
                    else {
                        maxLocals = successor2.position;
                    }
                    final int n2 = maxLocals - 1;
                    maxLocals = i;
                    if (n2 >= position) {
                        maxLocals = Math.max(i, 1);
                        for (i = position; i < n2; ++i) {
                            this.code.data[i] = 0;
                        }
                        this.code.data[n2] = -65;
                        i = this.startFrame(position, 0, 1);
                        this.frame[i] = (this.cw.addType("java/lang/Throwable") | 0x1700000);
                        this.endFrame();
                        this.firstHandler = Handler.remove(this.firstHandler, label2, successor2);
                    }
                }
            }
            Handler handler2 = this.firstHandler;
            this.handlerCount = 0;
            while (handler2 != null) {
                ++this.handlerCount;
                handler2 = handler2.next;
            }
            this.maxStack = i;
        }
        else {
            if (this.compute != 2) {
                this.maxStack = i;
                this.maxLocals = maxLocals;
                return;
            }
            for (Handler handler3 = this.firstHandler; handler3 != null; handler3 = handler3.next) {
                Label label3 = handler3.start;
                final Label handler4 = handler3.handler;
                while (label3 != handler3.end) {
                    final Edge edge2 = new Edge();
                    edge2.info = Integer.MAX_VALUE;
                    edge2.successor = handler4;
                    if ((label3.status & 0x80) == 0x0) {
                        edge2.next = label3.successors;
                        label3.successors = edge2;
                    }
                    else {
                        edge2.next = label3.successors.next.next;
                        label3.successors.next.next = edge2;
                    }
                    label3 = label3.successor;
                }
            }
            if (this.subroutines > 0) {
                maxLocals = 0;
                this.labels.visitSubroutine(null, 1L, this.subroutines);
                for (Label label4 = this.labels; label4 != null; label4 = label4.successor) {
                    if ((label4.status & 0x80) != 0x0) {
                        final Label successor3 = label4.successors.next.successor;
                        if ((successor3.status & 0x400) == 0x0) {
                            ++maxLocals;
                            successor3.visitSubroutine(null, maxLocals / 32L << 32 | 1L << maxLocals % 32, this.subroutines);
                        }
                    }
                }
                for (Label label5 = this.labels; label5 != null; label5 = label5.successor) {
                    if ((label5.status & 0x80) != 0x0) {
                        for (Label label6 = this.labels; label6 != null; label6 = label6.successor) {
                            label6.status &= 0xFFFFF7FF;
                        }
                        label5.successors.next.successor.visitSubroutine(label5, 0L, this.subroutines);
                    }
                }
            }
            int n3 = 0;
            Label next3;
            for (Label labels2 = this.labels; labels2 != null; labels2 = next3) {
                final Label next2 = labels2.next;
                final int inputStackTop = labels2.inputStackTop;
                final int n4 = labels2.outputStackMax + inputStackTop;
                if (n4 > (maxLocals = n3)) {
                    maxLocals = n4;
                }
                final Edge successors2 = labels2.successors;
                next3 = next2;
                Edge edge3 = successors2;
                if ((labels2.status & 0x80) != 0x0) {
                    edge3 = successors2.next;
                    next3 = next2;
                }
                while (edge3 != null) {
                    final Label successor4 = edge3.successor;
                    Label label7 = next3;
                    if ((successor4.status & 0x8) == 0x0) {
                        int inputStackTop2;
                        if (edge3.info == Integer.MAX_VALUE) {
                            inputStackTop2 = 1;
                        }
                        else {
                            inputStackTop2 = edge3.info + inputStackTop;
                        }
                        successor4.inputStackTop = inputStackTop2;
                        successor4.status |= 0x8;
                        successor4.next = next3;
                        label7 = successor4;
                    }
                    edge3 = edge3.next;
                    next3 = label7;
                }
                n3 = maxLocals;
            }
            this.maxStack = Math.max(i, n3);
        }
    }
    
    @Override
    public void visitMethodInsn(int argumentsAndReturnSizes, final String s, final String s2, final String s3, final boolean b) {
        this.lastCodeOffset = this.code.length;
        final Item methodItem = this.cw.newMethodItem(s, s2, s3, b);
        int intVal;
        final int n = intVal = methodItem.intVal;
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                if ((intVal = n) == 0) {
                    intVal = Type.getArgumentsAndReturnSizes(s3);
                    methodItem.intVal = intVal;
                }
                int n2;
                if (argumentsAndReturnSizes == 184) {
                    n2 = this.stackSize - (intVal >> 2) + (intVal & 0x3) + 1;
                }
                else {
                    n2 = this.stackSize - (intVal >> 2) + (intVal & 0x3);
                }
                if (n2 > this.maxStackSize) {
                    this.maxStackSize = n2;
                }
                this.stackSize = n2;
            }
            else {
                this.currentBlock.frame.execute(argumentsAndReturnSizes, 0, this.cw, methodItem);
                intVal = n;
            }
        }
        if (argumentsAndReturnSizes == 185) {
            if ((argumentsAndReturnSizes = intVal) == 0) {
                argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(s3);
                methodItem.intVal = argumentsAndReturnSizes;
            }
            this.code.put12(185, methodItem.index).put11(argumentsAndReturnSizes >> 2, 0);
            return;
        }
        this.code.put12(argumentsAndReturnSizes, methodItem.index);
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String s, final int n) {
        this.lastCodeOffset = this.code.length;
        final Item classItem = this.cw.newClassItem(s);
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                this.stackSize += 1 - n;
            }
            else {
                this.currentBlock.frame.execute(197, n, this.cw, classItem);
            }
        }
        this.code.put12(197, classItem.index).putByte(n);
    }
    
    @Override
    public void visitParameter(final String s, final int n) {
        if (this.methodParameters == null) {
            this.methodParameters = new ByteVector();
        }
        ++this.methodParametersCount;
        final ByteVector methodParameters = this.methodParameters;
        int utf8;
        if (s == null) {
            utf8 = 0;
        }
        else {
            utf8 = this.cw.newUTF8(s);
        }
        methodParameters.putShort(utf8).putShort(n);
    }
    
    @Override
    public AnnotationVisitor visitParameterAnnotation(final int n, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(s)) {
            this.synthetics = Math.max(this.synthetics, n + 1);
            return new AnnotationWriter(this.cw, false, byteVector, null, 0);
        }
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
        if (b) {
            if (this.panns == null) {
                this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            annotationWriter.next = this.panns[n];
            return this.panns[n] = annotationWriter;
        }
        if (this.ipanns == null) {
            this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
        }
        annotationWriter.next = this.ipanns[n];
        return this.ipanns[n] = annotationWriter;
    }
    
    @Override
    public void visitTableSwitchInsn(int i, final int n, final Label label, final Label... array) {
        this.lastCodeOffset = this.code.length;
        final int length = this.code.length;
        this.code.putByte(170);
        final ByteVector code = this.code;
        final int length2 = this.code.length;
        final int n2 = 0;
        code.putByteArray(null, 0, (4 - length2 % 4) % 4);
        label.put(this, this.code, length, true);
        this.code.putInt(i).putInt(n);
        for (i = n2; i < array.length; ++i) {
            array[i].put(this, this.code, length, true);
        }
        this.visitSwitchInsn(label, array);
    }
    
    @Override
    public AnnotationVisitor visitTryCatchAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(n, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.ctanns;
            return this.ctanns = annotationWriter;
        }
        annotationWriter.next = this.ictanns;
        return this.ictanns = annotationWriter;
    }
    
    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String desc) {
        ++this.handlerCount;
        final Handler lastHandler = new Handler();
        lastHandler.start = start;
        lastHandler.end = end;
        lastHandler.handler = handler;
        lastHandler.desc = desc;
        int class1;
        if (desc != null) {
            class1 = this.cw.newClass(desc);
        }
        else {
            class1 = 0;
        }
        lastHandler.type = class1;
        if (this.lastHandler == null) {
            this.firstHandler = lastHandler;
        }
        else {
            this.lastHandler.next = lastHandler;
        }
        this.lastHandler = lastHandler;
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
    
    @Override
    public void visitTypeInsn(final int n, final String s) {
        this.lastCodeOffset = this.code.length;
        final Item classItem = this.cw.newClassItem(s);
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                if (n == 187) {
                    final int n2 = this.stackSize + 1;
                    if (n2 > this.maxStackSize) {
                        this.maxStackSize = n2;
                    }
                    this.stackSize = n2;
                }
            }
            else {
                this.currentBlock.frame.execute(n, this.code.length, this.cw, classItem);
            }
        }
        this.code.put12(n, classItem.index);
    }
    
    @Override
    public void visitVarInsn(final int n, int n2) {
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null) {
            if (this.compute != 0 && this.compute != 1) {
                if (n == 169) {
                    final Label currentBlock = this.currentBlock;
                    currentBlock.status |= 0x100;
                    this.currentBlock.inputStackTop = this.stackSize;
                    this.noSuccessor();
                }
                else {
                    final int n3 = this.stackSize + Frame.SIZE[n];
                    if (n3 > this.maxStackSize) {
                        this.maxStackSize = n3;
                    }
                    this.stackSize = n3;
                }
            }
            else {
                this.currentBlock.frame.execute(n, n2, null, null);
            }
        }
        if (this.compute != 3) {
            int maxLocals;
            if (n != 22 && n != 24 && n != 55 && n != 57) {
                maxLocals = n2 + 1;
            }
            else {
                maxLocals = n2 + 2;
            }
            if (maxLocals > this.maxLocals) {
                this.maxLocals = maxLocals;
            }
        }
        if (n2 < 4 && n != 169) {
            if (n < 54) {
                n2 += (n - 21 << 2) + 26;
            }
            else {
                n2 += (n - 54 << 2) + 59;
            }
            this.code.putByte(n2);
        }
        else if (n2 >= 256) {
            this.code.putByte(196).put12(n, n2);
        }
        else {
            this.code.put11(n, n2);
        }
        if (n >= 54 && this.compute == 0 && this.handlerCount > 0) {
            this.visitLabel(new Label());
        }
    }
}
