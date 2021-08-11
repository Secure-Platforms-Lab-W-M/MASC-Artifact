// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.commons;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Type;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import java.util.List;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public class AnalyzerAdapter extends MethodVisitor
{
    private List<Label> labels;
    public List<Object> locals;
    private int maxLocals;
    private int maxStack;
    private String owner;
    public List<Object> stack;
    public Map<Object, Object> uninitializedTypes;
    
    protected AnalyzerAdapter(int i, final String owner, final int n, final String s, final String s2, final MethodVisitor methodVisitor) {
        super(i, methodVisitor);
        this.owner = owner;
        this.locals = new ArrayList<Object>();
        this.stack = new ArrayList<Object>();
        this.uninitializedTypes = new HashMap<Object, Object>();
        if ((n & 0x8) == 0x0) {
            if ("<init>".equals(s)) {
                this.locals.add(Opcodes.UNINITIALIZED_THIS);
            }
            else {
                this.locals.add(owner);
            }
        }
        Type[] argumentTypes;
        for (argumentTypes = Type.getArgumentTypes(s2), i = 0; i < argumentTypes.length; ++i) {
            switch (argumentTypes[i].getSort()) {
                default: {
                    this.locals.add(argumentTypes[i].getInternalName());
                    break;
                }
                case 9: {
                    this.locals.add(argumentTypes[i].getDescriptor());
                    break;
                }
                case 8: {
                    this.locals.add(Opcodes.DOUBLE);
                    this.locals.add(Opcodes.TOP);
                    break;
                }
                case 7: {
                    this.locals.add(Opcodes.LONG);
                    this.locals.add(Opcodes.TOP);
                    break;
                }
                case 6: {
                    this.locals.add(Opcodes.FLOAT);
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5: {
                    this.locals.add(Opcodes.INTEGER);
                    break;
                }
            }
        }
        this.maxLocals = this.locals.size();
    }
    
    public AnalyzerAdapter(final String s, final int n, final String s2, final String s3, final MethodVisitor methodVisitor) {
        this(327680, s, n, s2, s3, methodVisitor);
        if (this.getClass() == AnalyzerAdapter.class) {
            return;
        }
        throw new IllegalStateException();
    }
    
    private void doVisitMethodInsn(int i, String o, final String s, final String s2, final boolean b) {
        if (this.mv != null) {
            this.mv.visitMethodInsn(i, (String)o, s, s2, b);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        this.pop(s2);
        if (i != 184) {
            final Object pop = this.pop();
            if (i == 183) {
                final int n = 0;
                if (s.charAt(0) == '<') {
                    if (pop == Opcodes.UNINITIALIZED_THIS) {
                        o = this.owner;
                    }
                    else {
                        o = this.uninitializedTypes.get(pop);
                    }
                    for (i = 0; i < this.locals.size(); ++i) {
                        if (this.locals.get(i) == pop) {
                            this.locals.set(i, o);
                        }
                    }
                    for (i = n; i < this.stack.size(); ++i) {
                        if (this.stack.get(i) == pop) {
                            this.stack.set(i, o);
                        }
                    }
                }
            }
        }
        this.pushDesc(s2);
        this.labels = null;
    }
    
    private void execute(final int n, final int n2, final String s) {
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        while (true) {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            Label_1649: {
                                switch (n) {
                                    default: {
                                        Label_0988: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            switch (n) {
                                                                default: {
                                                                    this.pop(n2);
                                                                    this.pushDesc(s);
                                                                    break Label_1781;
                                                                }
                                                                case 198:
                                                                case 199: {
                                                                    break Label_0988;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 192: {
                                                            this.pop();
                                                            this.pushDesc(Type.getObjectType(s).getDescriptor());
                                                            break Label_1781;
                                                        }
                                                        case 189: {
                                                            this.pop();
                                                            final StringBuilder sb = new StringBuilder();
                                                            sb.append("[");
                                                            sb.append(Type.getObjectType(s));
                                                            this.pushDesc(sb.toString());
                                                            break Label_1781;
                                                        }
                                                        case 188: {
                                                            this.pop();
                                                            switch (n2) {
                                                                default: {
                                                                    this.pushDesc("[J");
                                                                    break Label_1781;
                                                                }
                                                                case 10: {
                                                                    this.pushDesc("[I");
                                                                    break Label_1781;
                                                                }
                                                                case 9: {
                                                                    this.pushDesc("[S");
                                                                    break Label_1781;
                                                                }
                                                                case 8: {
                                                                    this.pushDesc("[B");
                                                                    break Label_1781;
                                                                }
                                                                case 7: {
                                                                    this.pushDesc("[D");
                                                                    break Label_1781;
                                                                }
                                                                case 6: {
                                                                    this.pushDesc("[F");
                                                                    break Label_1781;
                                                                }
                                                                case 5: {
                                                                    this.pushDesc("[C");
                                                                    break Label_1781;
                                                                }
                                                                case 4: {
                                                                    this.pushDesc("[Z");
                                                                    break Label_1781;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 187: {
                                                            this.push(this.labels.get(0));
                                                            break Label_1781;
                                                        }
                                                        case 190:
                                                        case 193: {
                                                            break Label_0988;
                                                        }
                                                        case 191:
                                                        case 194:
                                                        case 195: {
                                                            break Label_0988;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 168:
                                                case 169: {
                                                    throw new RuntimeException("JSR/RET are not supported");
                                                }
                                                case 138: {
                                                    break Label_1649;
                                                }
                                                case 143: {
                                                    break Label_1649;
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
                                                    break Label_1781;
                                                }
                                                case 181: {
                                                    this.pop(s);
                                                    this.pop();
                                                    break Label_1781;
                                                }
                                                case 180: {
                                                    this.pop(1);
                                                    this.pushDesc(s);
                                                    break Label_1781;
                                                }
                                                case 179: {
                                                    this.pop(s);
                                                    break Label_1781;
                                                }
                                                case 178: {
                                                    this.pushDesc(s);
                                                    break Label_1781;
                                                }
                                                case 148:
                                                case 151:
                                                case 152: {
                                                    this.pop(4);
                                                    this.push(Opcodes.INTEGER);
                                                    break Label_1781;
                                                }
                                                case 139: {
                                                    this.pop(1);
                                                    this.push(Opcodes.INTEGER);
                                                    break Label_1781;
                                                }
                                                case 135:
                                                case 141: {
                                                    this.pop(1);
                                                    this.push(Opcodes.DOUBLE);
                                                    this.push(Opcodes.TOP);
                                                    break Label_1781;
                                                }
                                                case 134: {
                                                    this.pop(1);
                                                    this.push(Opcodes.FLOAT);
                                                    break Label_1781;
                                                }
                                                case 133:
                                                case 140: {
                                                    this.pop(1);
                                                    this.push(Opcodes.LONG);
                                                    this.push(Opcodes.TOP);
                                                    break Label_1781;
                                                }
                                                case 132: {
                                                    this.set(n2, Opcodes.INTEGER);
                                                    break Label_1781;
                                                }
                                                case 121:
                                                case 123:
                                                case 125: {
                                                    this.pop(3);
                                                    this.push(Opcodes.LONG);
                                                    this.push(Opcodes.TOP);
                                                    break Label_1781;
                                                }
                                                case 99:
                                                case 103:
                                                case 107:
                                                case 111:
                                                case 115: {
                                                    this.pop(4);
                                                    this.push(Opcodes.DOUBLE);
                                                    this.push(Opcodes.TOP);
                                                    break Label_1781;
                                                }
                                                case 98:
                                                case 102:
                                                case 106:
                                                case 110:
                                                case 114:
                                                case 137:
                                                case 144: {
                                                    this.pop(2);
                                                    this.push(Opcodes.FLOAT);
                                                    break Label_1781;
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
                                                    this.push(Opcodes.LONG);
                                                    this.push(Opcodes.TOP);
                                                    break Label_1781;
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
                                                    this.push(Opcodes.INTEGER);
                                                    break Label_1781;
                                                }
                                                case 95: {
                                                    final Object pop = this.pop();
                                                    final Object pop2 = this.pop();
                                                    this.push(pop);
                                                    this.push(pop2);
                                                    break Label_1781;
                                                }
                                                case 94: {
                                                    final Object pop3 = this.pop();
                                                    final Object pop4 = this.pop();
                                                    final Object pop5 = this.pop();
                                                    final Object pop6 = this.pop();
                                                    this.push(pop4);
                                                    this.push(pop3);
                                                    this.push(pop6);
                                                    this.push(pop5);
                                                    this.push(pop4);
                                                    this.push(pop3);
                                                    break Label_1781;
                                                }
                                                case 93: {
                                                    final Object pop7 = this.pop();
                                                    final Object pop8 = this.pop();
                                                    final Object pop9 = this.pop();
                                                    this.push(pop8);
                                                    this.push(pop7);
                                                    this.push(pop9);
                                                    this.push(pop8);
                                                    this.push(pop7);
                                                    break Label_1781;
                                                }
                                                case 92: {
                                                    final Object pop10 = this.pop();
                                                    final Object pop11 = this.pop();
                                                    this.push(pop11);
                                                    this.push(pop10);
                                                    this.push(pop11);
                                                    this.push(pop10);
                                                    break Label_1781;
                                                }
                                                case 91: {
                                                    final Object pop12 = this.pop();
                                                    final Object pop13 = this.pop();
                                                    final Object pop14 = this.pop();
                                                    this.push(pop12);
                                                    this.push(pop14);
                                                    this.push(pop13);
                                                    this.push(pop12);
                                                    break Label_1781;
                                                }
                                                case 90: {
                                                    final Object pop15 = this.pop();
                                                    final Object pop16 = this.pop();
                                                    this.push(pop15);
                                                    this.push(pop16);
                                                    this.push(pop15);
                                                    break Label_1781;
                                                }
                                                case 89: {
                                                    final Object pop17 = this.pop();
                                                    this.push(pop17);
                                                    this.push(pop17);
                                                    break Label_1781;
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
                                                    break Label_1781;
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
                                                case 176: {
                                                    this.pop(1);
                                                    break Label_1781;
                                                }
                                                case 80:
                                                case 82: {
                                                    this.pop(4);
                                                    break Label_1781;
                                                }
                                                case 79:
                                                case 81:
                                                case 83:
                                                case 84:
                                                case 85:
                                                case 86: {
                                                    this.pop(3);
                                                    break Label_1781;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    case 55:
                                    case 57: {
                                        this.pop(1);
                                        this.set(n2, this.pop());
                                        this.set(n2 + 1, Opcodes.TOP);
                                        if (n2 <= 0) {
                                            break Label_1781;
                                        }
                                        final Object value = this.get(n2 - 1);
                                        if (value == Opcodes.LONG || value == Opcodes.DOUBLE) {
                                            this.set(n2 - 1, Opcodes.TOP);
                                        }
                                        break Label_1781;
                                    }
                                    case 54:
                                    case 56:
                                    case 58: {
                                        this.set(n2, this.pop());
                                        if (n2 <= 0) {
                                            break Label_1781;
                                        }
                                        final Object value2 = this.get(n2 - 1);
                                        if (value2 == Opcodes.LONG || value2 == Opcodes.DOUBLE) {
                                            this.set(n2 - 1, Opcodes.TOP);
                                        }
                                        break Label_1781;
                                    }
                                    case 50: {
                                        this.pop(1);
                                        final Object pop18 = this.pop();
                                        if (pop18 instanceof String) {
                                            this.pushDesc(((String)pop18).substring(1));
                                            break Label_1781;
                                        }
                                        this.push("java/lang/Object");
                                        break Label_1781;
                                    }
                                    case 49: {
                                        this.pop(2);
                                        this.push(Opcodes.DOUBLE);
                                        this.push(Opcodes.TOP);
                                        break Label_1781;
                                    }
                                    case 48: {
                                        this.pop(2);
                                        this.push(Opcodes.FLOAT);
                                        break Label_1781;
                                    }
                                    case 47: {
                                        this.pop(2);
                                        this.push(Opcodes.LONG);
                                        this.push(Opcodes.TOP);
                                        break Label_1781;
                                    }
                                    case 46:
                                    case 51:
                                    case 52:
                                    case 53: {
                                        this.pop(2);
                                        this.push(Opcodes.INTEGER);
                                        break Label_1781;
                                    }
                                }
                            }
                            break;
                        }
                        case 22:
                        case 24: {
                            this.push(this.get(n2));
                            this.push(Opcodes.TOP);
                            break Label_1781;
                        }
                        case 21:
                        case 23:
                        case 25: {
                            this.push(this.get(n2));
                            break Label_1781;
                        }
                    }
                    break;
                }
                case 0: {
                    this.labels = null;
                }
                case 14:
                case 15: {
                    this.push(Opcodes.DOUBLE);
                    this.push(Opcodes.TOP);
                    continue;
                }
                case 11:
                case 12:
                case 13: {
                    this.push(Opcodes.FLOAT);
                    continue;
                }
                case 9:
                case 10: {
                    this.push(Opcodes.LONG);
                    this.push(Opcodes.TOP);
                    continue;
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
                    this.push(Opcodes.INTEGER);
                    continue;
                }
                case 1: {
                    this.push(Opcodes.NULL);
                    continue;
                }
            }
            break;
        }
    }
    
    private Object get(final int n) {
        this.maxLocals = Math.max(this.maxLocals, n + 1);
        if (n < this.locals.size()) {
            return this.locals.get(n);
        }
        return Opcodes.TOP;
    }
    
    private Object pop() {
        return this.stack.remove(this.stack.size() - 1);
    }
    
    private void pop(final int n) {
        for (int size = this.stack.size(), i = size - 1; i >= size - n; --i) {
            this.stack.remove(i);
        }
    }
    
    private void pop(final String s) {
        int i = 0;
        final char char1 = s.charAt(0);
        if (char1 == '(') {
            int n = 0;
            for (Type[] argumentTypes = Type.getArgumentTypes(s); i < argumentTypes.length; ++i) {
                n += argumentTypes[i].getSize();
            }
            this.pop(n);
            return;
        }
        if (char1 != 'J' && char1 != 'D') {
            this.pop(1);
            return;
        }
        this.pop(2);
    }
    
    private void push(final Object o) {
        this.stack.add(o);
        this.maxStack = Math.max(this.maxStack, this.stack.size());
    }
    
    private void pushDesc(final String s) {
        int n = 0;
        if (s.charAt(0) == '(') {
            n = s.indexOf(41) + 1;
        }
        switch (s.charAt(n)) {
            default: {
                if (n == 0) {
                    this.push(s.substring(1, s.length() - 1));
                    return;
                }
                this.push(s.substring(n + 1, s.length() - 1));
            }
            case '[': {
                if (n == 0) {
                    this.push(s);
                    return;
                }
                this.push(s.substring(n, s.length()));
            }
            case 'V': {}
            case 'J': {
                this.push(Opcodes.LONG);
                this.push(Opcodes.TOP);
            }
            case 'F': {
                this.push(Opcodes.FLOAT);
            }
            case 'D': {
                this.push(Opcodes.DOUBLE);
                this.push(Opcodes.TOP);
            }
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z': {
                this.push(Opcodes.INTEGER);
            }
        }
    }
    
    private void set(final int i, final Object o) {
        this.maxLocals = Math.max(this.maxLocals, i + 1);
        while (i >= this.locals.size()) {
            this.locals.add(Opcodes.TOP);
        }
        this.locals.set(i, o);
    }
    
    private static void visitFrameTypes(final int n, final Object[] array, final List<Object> list) {
        for (int i = 0; i < n; ++i) {
            final Object o = array[i];
            list.add(o);
            if (o == Opcodes.LONG || o == Opcodes.DOUBLE) {
                list.add(Opcodes.TOP);
            }
        }
    }
    
    @Override
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        if (this.mv != null) {
            this.mv.visitFieldInsn(n, s, s2, s3);
        }
        this.execute(n, 0, s3);
    }
    
    @Override
    public void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        if (n == -1) {
            if (this.mv != null) {
                this.mv.visitFrame(n, n2, array, n3, array2);
            }
            if (this.locals != null) {
                this.locals.clear();
                this.stack.clear();
            }
            else {
                this.locals = new ArrayList<Object>();
                this.stack = new ArrayList<Object>();
            }
            visitFrameTypes(n2, array, this.locals);
            visitFrameTypes(n3, array2, this.stack);
            this.maxStack = Math.max(this.maxStack, this.stack.size());
            return;
        }
        throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
    }
    
    @Override
    public void visitIincInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitIincInsn(n, n2);
        }
        this.execute(132, n, null);
    }
    
    @Override
    public void visitInsn(final int n) {
        if (this.mv != null) {
            this.mv.visitInsn(n);
        }
        this.execute(n, 0, null);
        if ((n >= 172 && n <= 177) || n == 191) {
            this.locals = null;
            this.stack = null;
        }
    }
    
    @Override
    public void visitIntInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitIntInsn(n, n2);
        }
        this.execute(n, n2, null);
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(s, s2, handle, array);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        this.pop(s2);
        this.pushDesc(s2);
        this.labels = null;
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label label) {
        if (this.mv != null) {
            this.mv.visitJumpInsn(n, label);
        }
        this.execute(n, 0, null);
        if (n == 167) {
            this.locals = null;
            this.stack = null;
        }
    }
    
    @Override
    public void visitLabel(final Label label) {
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
        if (this.labels == null) {
            this.labels = new ArrayList<Label>(3);
        }
        this.labels.add(label);
    }
    
    @Override
    public void visitLdcInsn(final Object o) {
        if (this.mv != null) {
            this.mv.visitLdcInsn(o);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        if (o instanceof Integer) {
            this.push(Opcodes.INTEGER);
        }
        else if (o instanceof Long) {
            this.push(Opcodes.LONG);
            this.push(Opcodes.TOP);
        }
        else if (o instanceof Float) {
            this.push(Opcodes.FLOAT);
        }
        else if (o instanceof Double) {
            this.push(Opcodes.DOUBLE);
            this.push(Opcodes.TOP);
        }
        else if (o instanceof String) {
            this.push("java/lang/String");
        }
        else if (o instanceof Type) {
            final int sort = ((Type)o).getSort();
            if (sort != 10 && sort != 9) {
                if (sort != 11) {
                    throw new IllegalArgumentException();
                }
                this.push("java/lang/invoke/MethodType");
            }
            else {
                this.push("java/lang/Class");
            }
        }
        else {
            if (!(o instanceof Handle)) {
                throw new IllegalArgumentException();
            }
            this.push("java/lang/invoke/MethodHandle");
        }
        this.labels = null;
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(label, array, array2);
        }
        this.execute(171, 0, null);
        this.locals = null;
        this.stack = null;
    }
    
    @Override
    public void visitMaxs(final int n, final int n2) {
        if (this.mv != null) {
            this.maxStack = Math.max(this.maxStack, n);
            this.maxLocals = Math.max(this.maxLocals, n2);
            this.mv.visitMaxs(this.maxStack, this.maxLocals);
        }
    }
    
    @Deprecated
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(n, s, s2, s3);
            return;
        }
        this.doVisitMethodInsn(n, s, s2, s3, n == 185);
    }
    
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        if (this.api < 327680) {
            super.visitMethodInsn(n, s, s2, s3, b);
            return;
        }
        this.doVisitMethodInsn(n, s, s2, s3, b);
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String s, final int n) {
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(s, n);
        }
        this.execute(197, n, s);
    }
    
    @Override
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(n, n2, label, array);
        }
        this.execute(170, 0, null);
        this.locals = null;
        this.stack = null;
    }
    
    @Override
    public void visitTypeInsn(final int n, final String s) {
        if (n == 187) {
            if (this.labels == null) {
                final Label label = new Label();
                (this.labels = new ArrayList<Label>(3)).add(label);
                if (this.mv != null) {
                    this.mv.visitLabel(label);
                }
            }
            for (int i = 0; i < this.labels.size(); ++i) {
                this.uninitializedTypes.put(this.labels.get(i), s);
            }
        }
        if (this.mv != null) {
            this.mv.visitTypeInsn(n, s);
        }
        this.execute(n, 0, s);
    }
    
    @Override
    public void visitVarInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitVarInsn(n, n2);
        }
        this.execute(n, n2, null);
    }
}
