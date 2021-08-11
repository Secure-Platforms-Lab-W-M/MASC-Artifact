// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

class Frame
{
    static final int ARRAY_OF = 268435456;
    static final int BASE = 16777216;
    static final int BASE_KIND = 267386880;
    static final int BASE_VALUE = 1048575;
    static final int BOOLEAN = 16777225;
    static final int BYTE = 16777226;
    static final int CHAR = 16777227;
    static final int DIM = -268435456;
    static final int DOUBLE = 16777219;
    static final int ELEMENT_OF = -268435456;
    static final int FLOAT = 16777218;
    static final int INTEGER = 16777217;
    static final int KIND = 251658240;
    private static final int LOCAL = 33554432;
    static final int LONG = 16777220;
    static final int NULL = 16777221;
    static final int OBJECT = 24117248;
    static final int SHORT = 16777228;
    static final int[] SIZE;
    private static final int STACK = 50331648;
    static final int TOP = 16777216;
    static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
    static final int UNINITIALIZED = 25165824;
    static final int UNINITIALIZED_THIS = 16777222;
    static final int VALUE = 8388607;
    private int initializationCount;
    private int[] initializations;
    int[] inputLocals;
    int[] inputStack;
    private int[] outputLocals;
    private int[] outputStack;
    int outputStackTop;
    Label owner;
    
    static {
        final int[] size = new int[202];
        for (int i = 0; i < size.length; ++i) {
            size[i] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i) - 'E';
        }
        SIZE = size;
    }
    
    private static int convert(final ClassWriter classWriter, final int n, final Object[] array, final int[] array2) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (array[i] instanceof Integer) {
                final int n3 = n2 + 1;
                array2[n2] = ((int)array[i] | 0x1000000);
                if (array[i] != Opcodes.LONG) {
                    n2 = n3;
                    if (array[i] != Opcodes.DOUBLE) {
                        continue;
                    }
                }
                n2 = n3 + 1;
                array2[n3] = 16777216;
            }
            else if (array[i] instanceof String) {
                final int n4 = n2 + 1;
                array2[n2] = type(classWriter, Type.getObjectType((String)array[i]).getDescriptor());
                n2 = n4;
            }
            else {
                final int n5 = n2 + 1;
                array2[n2] = (0x1800000 | classWriter.addUninitializedType("", ((Label)array[i]).position));
                n2 = n5;
            }
        }
        return n2;
    }
    
    private int get(final int n) {
        if (this.outputLocals != null && n < this.outputLocals.length) {
            int n2;
            if ((n2 = this.outputLocals[n]) == 0) {
                final int[] outputLocals = this.outputLocals;
                n2 = (0x2000000 | n);
                outputLocals[n] = n2;
            }
            return n2;
        }
        return n | 0x2000000;
    }
    
    private int init(final ClassWriter classWriter, final int n) {
        int n2;
        if (n == 16777222) {
            n2 = (0x1700000 | classWriter.addType(classWriter.thisName));
        }
        else {
            if ((0xFFF00000 & n) != 0x1800000) {
                return n;
            }
            n2 = (0x1700000 | classWriter.addType(classWriter.typeTable[0xFFFFF & n].strVal1));
        }
        for (int i = 0; i < this.initializationCount; ++i) {
            final int n3 = this.initializations[i];
            final int n4 = 0xF0000000 & n3;
            final int n5 = 0xF000000 & n3;
            int n6;
            if (n5 == 33554432) {
                n6 = n4 + this.inputLocals[0x7FFFFF & n3];
            }
            else {
                n6 = n3;
                if (n5 == 50331648) {
                    n6 = n4 + this.inputStack[this.inputStack.length - (0x7FFFFF & n3)];
                }
            }
            if (n == n6) {
                return n2;
            }
        }
        return n;
    }
    
    private void init(final int n) {
        if (this.initializations == null) {
            this.initializations = new int[2];
        }
        final int length = this.initializations.length;
        if (this.initializationCount >= length) {
            final int[] initializations = new int[Math.max(this.initializationCount + 1, length * 2)];
            System.arraycopy(this.initializations, 0, initializations, 0, length);
            this.initializations = initializations;
        }
        this.initializations[this.initializationCount++] = n;
    }
    
    private static boolean merge(final ClassWriter classWriter, int min, final int[] array, final int n) {
        final int n2 = array[n];
        if (n2 == min) {
            return false;
        }
        int n3 = min;
        if ((0xFFFFFFF & min) == 0x1000005) {
            if (n2 == 16777221) {
                return false;
            }
            n3 = 16777221;
        }
        if (n2 == 0) {
            array[n] = n3;
            return true;
        }
        min = 16777216;
        if ((n2 & 0xFF00000) != 0x1700000 && (n2 & 0xF0000000) == 0x0) {
            if (n2 == 16777221) {
                if ((n3 & 0xFF00000) == 0x1700000 || (n3 & 0xF0000000) != 0x0) {
                    min = n3;
                }
            }
        }
        else {
            if (n3 == 16777221) {
                return false;
            }
            if ((n3 & 0xFFF00000) == (0xFFF00000 & n2)) {
                if ((n2 & 0xFF00000) == 0x1700000) {
                    min = ((n3 & 0xF0000000) | 0x1700000 | classWriter.getMergedType(n3 & 0xFFFFF, 0xFFFFF & n2));
                }
                else {
                    min = ((n2 & 0xF0000000) - 268435456 | 0x1700000 | classWriter.addType("java/lang/Object"));
                }
            }
            else if ((n3 & 0xFF00000) != 0x1700000 && (n3 & 0xF0000000) == 0x0) {
                min = 16777216;
            }
            else {
                if ((n3 & 0xF0000000) != 0x0 && (n3 & 0xFF00000) != 0x1700000) {
                    min = -268435456;
                }
                else {
                    min = 0;
                }
                int n4;
                if ((n2 & 0xF0000000) != 0x0 && (0xFF00000 & n2) != 0x1700000) {
                    n4 = -268435456;
                }
                else {
                    n4 = 0;
                }
                min = Math.min(min + (n3 & 0xF0000000), n4 + (n2 & 0xF0000000));
                min = (classWriter.addType("java/lang/Object") | (min | 0x1700000));
            }
        }
        if (n2 != min) {
            array[n] = min;
            return true;
        }
        return false;
    }
    
    private int pop() {
        if (this.outputStackTop > 0) {
            final int[] outputStack = this.outputStack;
            final int outputStackTop = this.outputStackTop - 1;
            this.outputStackTop = outputStackTop;
            return outputStack[outputStackTop];
        }
        final Label owner = this.owner;
        return 0x3000000 | -(--owner.inputStackTop);
    }
    
    private void pop(final int n) {
        if (this.outputStackTop >= n) {
            this.outputStackTop -= n;
            return;
        }
        final Label owner = this.owner;
        owner.inputStackTop -= n - this.outputStackTop;
        this.outputStackTop = 0;
    }
    
    private void pop(final String s) {
        final char char1 = s.charAt(0);
        if (char1 == '(') {
            this.pop((Type.getArgumentsAndReturnSizes(s) >> 2) - 1);
            return;
        }
        if (char1 != 'J' && char1 != 'D') {
            this.pop(1);
            return;
        }
        this.pop(2);
    }
    
    private void push(int outputStackMax) {
        if (this.outputStack == null) {
            this.outputStack = new int[10];
        }
        final int length = this.outputStack.length;
        if (this.outputStackTop >= length) {
            final int[] outputStack = new int[Math.max(this.outputStackTop + 1, length * 2)];
            System.arraycopy(this.outputStack, 0, outputStack, 0, length);
            this.outputStack = outputStack;
        }
        this.outputStack[this.outputStackTop++] = outputStackMax;
        outputStackMax = this.owner.inputStackTop + this.outputStackTop;
        if (outputStackMax > this.owner.outputStackMax) {
            this.owner.outputStackMax = outputStackMax;
        }
    }
    
    private void push(final ClassWriter classWriter, final String s) {
        final int type = type(classWriter, s);
        if (type != 0) {
            this.push(type);
            if (type == 16777220 || type == 16777219) {
                this.push(16777216);
            }
        }
    }
    
    private void set(final int n, final int n2) {
        if (this.outputLocals == null) {
            this.outputLocals = new int[10];
        }
        final int length = this.outputLocals.length;
        if (n >= length) {
            final int[] outputLocals = new int[Math.max(n + 1, length * 2)];
            System.arraycopy(this.outputLocals, 0, outputLocals, 0, length);
            this.outputLocals = outputLocals;
        }
        this.outputLocals[n] = n2;
    }
    
    private static int type(final ClassWriter classWriter, final String s) {
        int n;
        if (s.charAt(0) == '(') {
            n = s.indexOf(41) + 1;
        }
        else {
            n = 0;
        }
        final char char1 = s.charAt(n);
        if (char1 == 'F') {
            return 16777218;
        }
        if (char1 != 'L') {
            Label_0300: {
                if (char1 != 'S') {
                    if (char1 == 'V') {
                        return 0;
                    }
                    if (char1 != 'Z') {
                        switch (char1) {
                            default: {
                                switch (char1) {
                                    default: {
                                        int n2;
                                        for (n2 = n + 1; s.charAt(n2) == '['; ++n2) {}
                                        final char char2 = s.charAt(n2);
                                        int n3 = 0;
                                        Label_0282: {
                                            if (char2 != 'F') {
                                                if (char2 != 'S') {
                                                    if (char2 != 'Z') {
                                                        switch (char2) {
                                                            default: {
                                                                switch (char2) {
                                                                    default: {
                                                                        n3 = (classWriter.addType(s.substring(n2 + 1, s.length() - 1)) | 0x1700000);
                                                                        break Label_0282;
                                                                    }
                                                                    case 74: {
                                                                        n3 = 16777220;
                                                                        break Label_0282;
                                                                    }
                                                                    case 73: {
                                                                        n3 = 16777217;
                                                                        break Label_0282;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 68: {
                                                                n3 = 16777219;
                                                                break;
                                                            }
                                                            case 67: {
                                                                n3 = 16777227;
                                                                break;
                                                            }
                                                            case 66: {
                                                                n3 = 16777226;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        n3 = 16777225;
                                                    }
                                                }
                                                else {
                                                    n3 = 16777228;
                                                }
                                            }
                                            else {
                                                n3 = 16777218;
                                            }
                                        }
                                        return n2 - n << 28 | n3;
                                    }
                                    case 74: {
                                        return 16777220;
                                    }
                                    case 73: {
                                        break Label_0300;
                                    }
                                }
                                break;
                            }
                            case 68: {
                                return 16777219;
                            }
                            case 66:
                            case 67: {
                                break;
                            }
                        }
                    }
                }
            }
            return 16777217;
        }
        return classWriter.addType(s.substring(n + 1, s.length() - 1)) | 0x1700000;
    }
    
    void execute(int n, int n2, final ClassWriter classWriter, final Item item) {
        Label_1937: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            Label_1682: {
                                switch (n) {
                                    default: {
                                        Label_1499: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            this.pop(n2);
                                                            this.push(classWriter, item.strVal1);
                                                            return;
                                                        }
                                                        case 198:
                                                        case 199: {
                                                            break Label_1499;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 192: {
                                                    final String strVal1 = item.strVal1;
                                                    this.pop();
                                                    if (strVal1.charAt(0) == '[') {
                                                        this.push(classWriter, strVal1);
                                                        return;
                                                    }
                                                    this.push(0x1700000 | classWriter.addType(strVal1));
                                                    return;
                                                }
                                                case 189: {
                                                    final String strVal2 = item.strVal1;
                                                    this.pop();
                                                    if (strVal2.charAt(0) == '[') {
                                                        final StringBuilder sb = new StringBuilder();
                                                        sb.append('[');
                                                        sb.append(strVal2);
                                                        this.push(classWriter, sb.toString());
                                                        return;
                                                    }
                                                    this.push(0x11700000 | classWriter.addType(strVal2));
                                                    return;
                                                }
                                                case 188: {
                                                    this.pop();
                                                    switch (n2) {
                                                        default: {
                                                            this.push(285212676);
                                                            return;
                                                        }
                                                        case 10: {
                                                            this.push(285212673);
                                                            return;
                                                        }
                                                        case 9: {
                                                            this.push(285212684);
                                                            return;
                                                        }
                                                        case 8: {
                                                            this.push(285212682);
                                                            return;
                                                        }
                                                        case 7: {
                                                            this.push(285212675);
                                                            return;
                                                        }
                                                        case 6: {
                                                            this.push(285212674);
                                                            return;
                                                        }
                                                        case 5: {
                                                            this.push(285212683);
                                                            return;
                                                        }
                                                        case 4: {
                                                            this.push(285212681);
                                                            return;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 187: {
                                                    this.push(0x1800000 | classWriter.addUninitializedType(item.strVal1, n2));
                                                    return;
                                                }
                                                case 186: {
                                                    this.pop(item.strVal2);
                                                    this.push(classWriter, item.strVal2);
                                                    return;
                                                }
                                                case 182:
                                                case 183:
                                                case 184:
                                                case 185: {
                                                    this.pop(item.strVal3);
                                                    if (n != 184) {
                                                        n2 = this.pop();
                                                        if (n == 183 && item.strVal2.charAt(0) == '<') {
                                                            this.init(n2);
                                                        }
                                                    }
                                                    this.push(classWriter, item.strVal3);
                                                    return;
                                                }
                                                case 181: {
                                                    this.pop(item.strVal3);
                                                    this.pop();
                                                    return;
                                                }
                                                case 180: {
                                                    this.pop(1);
                                                    this.push(classWriter, item.strVal3);
                                                    return;
                                                }
                                                case 179: {
                                                    this.pop(item.strVal3);
                                                    return;
                                                }
                                                case 178: {
                                                    this.push(classWriter, item.strVal3);
                                                    return;
                                                }
                                                case 168:
                                                case 169: {
                                                    throw new RuntimeException("JSR/RET are not supported with computeFrames option");
                                                }
                                                case 148:
                                                case 151:
                                                case 152: {
                                                    this.pop(4);
                                                    this.push(16777217);
                                                    return;
                                                }
                                                case 143: {
                                                    break Label_1682;
                                                }
                                                case 139:
                                                case 190:
                                                case 193: {
                                                    this.pop(1);
                                                    this.push(16777217);
                                                    return;
                                                }
                                                case 135:
                                                case 141: {
                                                    this.pop(1);
                                                    this.push(16777219);
                                                    this.push(16777216);
                                                    return;
                                                }
                                                case 134: {
                                                    this.pop(1);
                                                    this.push(16777218);
                                                    return;
                                                }
                                                case 133:
                                                case 140: {
                                                    this.pop(1);
                                                    this.push(16777220);
                                                    this.push(16777216);
                                                    return;
                                                }
                                                case 132: {
                                                    this.set(n2, 16777217);
                                                    return;
                                                }
                                                case 121:
                                                case 123:
                                                case 125: {
                                                    this.pop(3);
                                                    this.push(16777220);
                                                    this.push(16777216);
                                                    return;
                                                }
                                                case 99:
                                                case 103:
                                                case 107:
                                                case 111:
                                                case 115: {
                                                    this.pop(4);
                                                    this.push(16777219);
                                                    this.push(16777216);
                                                    return;
                                                }
                                                case 98:
                                                case 102:
                                                case 106:
                                                case 110:
                                                case 114:
                                                case 137:
                                                case 144: {
                                                    this.pop(2);
                                                    this.push(16777218);
                                                    return;
                                                }
                                                case 97:
                                                case 101:
                                                case 105:
                                                case 109:
                                                case 113:
                                                case 127:
                                                case 129:
                                                case 131: {
                                                    this.pop(4);
                                                    this.push(16777220);
                                                    this.push(16777216);
                                                    return;
                                                }
                                                case 96:
                                                case 100:
                                                case 104:
                                                case 108:
                                                case 112:
                                                case 120:
                                                case 122:
                                                case 124:
                                                case 126:
                                                case 128:
                                                case 130:
                                                case 136:
                                                case 142:
                                                case 149:
                                                case 150: {
                                                    this.pop(2);
                                                    this.push(16777217);
                                                    return;
                                                }
                                                case 95: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    this.push(n);
                                                    this.push(n2);
                                                    return;
                                                }
                                                case 94: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    final int pop = this.pop();
                                                    final int pop2 = this.pop();
                                                    this.push(n2);
                                                    this.push(n);
                                                    this.push(pop2);
                                                    this.push(pop);
                                                    this.push(n2);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 93: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    final int pop3 = this.pop();
                                                    this.push(n2);
                                                    this.push(n);
                                                    this.push(pop3);
                                                    this.push(n2);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 92: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    this.push(n2);
                                                    this.push(n);
                                                    this.push(n2);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 91: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    final int pop4 = this.pop();
                                                    this.push(n);
                                                    this.push(pop4);
                                                    this.push(n2);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 90: {
                                                    n = this.pop();
                                                    n2 = this.pop();
                                                    this.push(n);
                                                    this.push(n2);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 89: {
                                                    n = this.pop();
                                                    this.push(n);
                                                    this.push(n);
                                                    return;
                                                }
                                                case 88:
                                                case 159:
                                                case 160:
                                                case 161:
                                                case 162:
                                                case 163:
                                                case 164:
                                                case 165:
                                                case 166:
                                                case 173:
                                                case 175: {
                                                    this.pop(2);
                                                    return;
                                                }
                                                case 87:
                                                case 153:
                                                case 154:
                                                case 155:
                                                case 156:
                                                case 157:
                                                case 158:
                                                case 170:
                                                case 171:
                                                case 172:
                                                case 174:
                                                case 176:
                                                case 191:
                                                case 194:
                                                case 195: {
                                                    this.pop(1);
                                                    return;
                                                }
                                                case 80:
                                                case 82: {
                                                    this.pop(4);
                                                    return;
                                                }
                                                case 79:
                                                case 81:
                                                case 83:
                                                case 84:
                                                case 85:
                                                case 86: {
                                                    this.pop(3);
                                                    return;
                                                }
                                                case 138: {
                                                    break Label_1682;
                                                }
                                                case 116:
                                                case 117:
                                                case 118:
                                                case 119:
                                                case 145:
                                                case 146:
                                                case 147:
                                                case 167:
                                                case 177: {
                                                    return;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    case 47: {
                                        this.pop(2);
                                        this.push(16777220);
                                        this.push(16777216);
                                        return;
                                    }
                                    case 55:
                                    case 57: {
                                        this.pop(1);
                                        this.set(n2, this.pop());
                                        this.set(n2 + 1, 16777216);
                                        if (n2 <= 0) {
                                            return;
                                        }
                                        n = this.get(n2 - 1);
                                        if (n == 16777220 || n == 16777219) {
                                            this.set(n2 - 1, 16777216);
                                            return;
                                        }
                                        if ((n & 0xF000000) != 0x1000000) {
                                            this.set(n2 - 1, n | 0x800000);
                                        }
                                        return;
                                    }
                                    case 54:
                                    case 56:
                                    case 58: {
                                        this.set(n2, this.pop());
                                        if (n2 <= 0) {
                                            return;
                                        }
                                        n = this.get(n2 - 1);
                                        if (n == 16777220 || n == 16777219) {
                                            this.set(n2 - 1, 16777216);
                                            return;
                                        }
                                        if ((n & 0xF000000) != 0x1000000) {
                                            this.set(n2 - 1, n | 0x800000);
                                        }
                                        return;
                                    }
                                    case 50: {
                                        this.pop(1);
                                        this.push(-268435456 + this.pop());
                                        return;
                                    }
                                    case 49: {
                                        this.pop(2);
                                        this.push(16777219);
                                        this.push(16777216);
                                        return;
                                    }
                                    case 48: {
                                        this.pop(2);
                                        this.push(16777218);
                                        return;
                                    }
                                    case 46:
                                    case 51:
                                    case 52:
                                    case 53: {
                                        this.pop(2);
                                        this.push(16777217);
                                        return;
                                    }
                                }
                            }
                            break;
                        }
                        case 25: {
                            this.push(this.get(n2));
                            return;
                        }
                        case 24: {
                            break Label_1937;
                        }
                        case 23: {
                            break Label_1937;
                        }
                        case 22: {
                            break Label_1937;
                        }
                        case 21: {
                            break Label_1937;
                        }
                    }
                    break;
                }
                case 18: {
                    n = item.type;
                    if (n == 16) {
                        this.push(0x1700000 | classWriter.addType("java/lang/invoke/MethodType"));
                        return;
                    }
                    switch (n) {
                        default: {
                            this.push(0x1700000 | classWriter.addType("java/lang/invoke/MethodHandle"));
                            return;
                        }
                        case 8: {
                            this.push(0x1700000 | classWriter.addType("java/lang/String"));
                            return;
                        }
                        case 7: {
                            this.push(0x1700000 | classWriter.addType("java/lang/Class"));
                            return;
                        }
                        case 6: {
                            this.push(16777219);
                            this.push(16777216);
                            return;
                        }
                        case 5: {
                            this.push(16777220);
                            this.push(16777216);
                            return;
                        }
                        case 4: {
                            this.push(16777218);
                            return;
                        }
                        case 3: {
                            this.push(16777217);
                            return;
                        }
                    }
                    break;
                }
                case 14:
                case 15: {
                    this.push(16777219);
                    this.push(16777216);
                }
                case 11:
                case 12:
                case 13: {
                    this.push(16777218);
                }
                case 9:
                case 10: {
                    this.push(16777220);
                    this.push(16777216);
                }
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 16:
                case 17: {
                    this.push(16777217);
                }
                case 1: {
                    this.push(16777221);
                }
                case 0: {}
            }
        }
    }
    
    final void initInputFrame(final ClassWriter classWriter, int n, final Type[] array, final int n2) {
        this.inputLocals = new int[n2];
        int n3 = 0;
        this.inputStack = new int[0];
        if ((n & 0x8) == 0x0) {
            if ((0x80000 & n) == 0x0) {
                final int[] inputLocals = this.inputLocals;
                n = 0 + 1;
                inputLocals[0] = (0x1700000 | classWriter.addType(classWriter.thisName));
            }
            else {
                final int[] inputLocals2 = this.inputLocals;
                n = 0 + 1;
                inputLocals2[0] = 16777222;
            }
        }
        else {
            n = 0;
        }
        int i;
        while (true) {
            i = n;
            if (n3 >= array.length) {
                break;
            }
            final int type = type(classWriter, array[n3].getDescriptor());
            final int[] inputLocals3 = this.inputLocals;
            final int n4 = n + 1;
            inputLocals3[n] = type;
            if (type != 16777220 && type != 16777219) {
                n = n4;
            }
            else {
                this.inputLocals[n4] = 16777216;
                n = n4 + 1;
            }
            ++n3;
        }
        while (i < n2) {
            this.inputLocals[i] = 16777216;
            ++i;
        }
    }
    
    final boolean merge(final ClassWriter classWriter, final Frame frame, int i) {
        boolean b = false;
        final int length = this.inputLocals.length;
        final int length2 = this.inputStack.length;
        if (frame.inputLocals == null) {
            frame.inputLocals = new int[length];
            b = true;
        }
        for (int j = 0; j < length; ++j) {
            int n2;
            if (this.outputLocals != null && j < this.outputLocals.length) {
                final int n = this.outputLocals[j];
                if (n == 0) {
                    n2 = this.inputLocals[j];
                }
                else {
                    final int n3 = 0xF0000000 & n;
                    final int n4 = 0xF000000 & n;
                    if (n4 == 16777216) {
                        n2 = n;
                    }
                    else {
                        if (n4 == 33554432) {
                            n2 = this.inputLocals[n & 0x7FFFFF] + n3;
                        }
                        else {
                            n2 = this.inputStack[length2 - (n & 0x7FFFFF)] + n3;
                        }
                        if ((0x800000 & n) != 0x0 && (n2 == 16777220 || n2 == 16777219)) {
                            n2 = 16777216;
                        }
                    }
                }
            }
            else {
                n2 = this.inputLocals[j];
            }
            int init = n2;
            if (this.initializations != null) {
                init = this.init(classWriter, n2);
            }
            b |= merge(classWriter, init, frame.inputLocals, j);
        }
        if (i > 0) {
            for (int k = 0; k < length; ++k) {
                b |= merge(classWriter, this.inputLocals[k], frame.inputLocals, k);
            }
            if (frame.inputStack == null) {
                frame.inputStack = new int[1];
                b = true;
            }
            return merge(classWriter, i, frame.inputStack, 0) | b;
        }
        final int n5 = this.inputStack.length + this.owner.inputStackTop;
        if (frame.inputStack == null) {
            frame.inputStack = new int[this.outputStackTop + n5];
            b = true;
        }
        int init2;
        for (i = 0; i < n5; ++i) {
            init2 = this.inputStack[i];
            if (this.initializations != null) {
                init2 = this.init(classWriter, init2);
            }
            b |= merge(classWriter, init2, frame.inputStack, i);
        }
        for (int l = 0; l < this.outputStackTop; ++l) {
            final int n6 = this.outputStack[l];
            i = (n6 & 0xF0000000);
            final int n7 = n6 & 0xF000000;
            int n8;
            if (n7 == 16777216) {
                n8 = n6;
            }
            else {
                if (n7 == 33554432) {
                    i += this.inputLocals[n6 & 0x7FFFFF];
                }
                else {
                    i += this.inputStack[length2 - (n6 & 0x7FFFFF)];
                }
                n8 = i;
                if ((n6 & 0x800000) != 0x0) {
                    if (i == 16777220 || (n8 = i) == 16777219) {
                        n8 = 16777216;
                    }
                }
            }
            i = n8;
            if (this.initializations != null) {
                i = this.init(classWriter, n8);
            }
            b |= merge(classWriter, i, frame.inputStack, n5 + l);
        }
        return b;
    }
    
    final void set(final ClassWriter classWriter, int i, final Object[] array, final int n, final Object[] array2) {
        for (i = convert(classWriter, i, array, this.inputLocals); i < array.length; ++i) {
            this.inputLocals[i] = 16777216;
        }
        int n2 = 0;
        int n3;
        for (i = 0; i < n; ++i, n2 = n3) {
            if (array2[i] != Opcodes.LONG) {
                n3 = n2;
                if (array2[i] != Opcodes.DOUBLE) {
                    continue;
                }
            }
            n3 = n2 + 1;
        }
        convert(classWriter, n, array2, this.inputStack = new int[n + n2]);
        this.outputStackTop = 0;
        this.initializationCount = 0;
    }
    
    final void set(final Frame frame) {
        this.inputLocals = frame.inputLocals;
        this.inputStack = frame.inputStack;
        this.outputLocals = frame.outputLocals;
        this.outputStack = frame.outputStack;
        this.outputStackTop = frame.outputStackTop;
        this.initializationCount = frame.initializationCount;
        this.initializations = frame.initializations;
    }
}
