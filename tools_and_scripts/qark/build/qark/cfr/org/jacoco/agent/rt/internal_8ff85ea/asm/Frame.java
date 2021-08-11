/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Item;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Type;

class Frame {
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
        int[] arrn = new int[202];
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i) - 69;
        }
        SIZE = arrn;
    }

    Frame() {
    }

    private static int convert(ClassWriter classWriter, int n, Object[] arrobject, int[] arrn) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            int n3;
            if (arrobject[i] instanceof Integer) {
                n3 = n2 + 1;
                arrn[n2] = (Integer)arrobject[i] | 16777216;
                if (arrobject[i] != Opcodes.LONG) {
                    n2 = n3;
                    if (arrobject[i] != Opcodes.DOUBLE) continue;
                }
                n2 = n3 + 1;
                arrn[n3] = 16777216;
                continue;
            }
            if (arrobject[i] instanceof String) {
                n3 = n2 + 1;
                arrn[n2] = Frame.type(classWriter, Type.getObjectType((String)arrobject[i]).getDescriptor());
                n2 = n3;
                continue;
            }
            n3 = n2 + 1;
            arrn[n2] = 25165824 | classWriter.addUninitializedType("", ((Label)arrobject[i]).position);
            n2 = n3;
        }
        return n2;
    }

    private int get(int n) {
        if (this.outputLocals != null && n < this.outputLocals.length) {
            int n2;
            int n3 = n2 = this.outputLocals[n];
            if (n2 == 0) {
                int[] arrn = this.outputLocals;
                arrn[n] = n3 = 33554432 | n;
            }
            return n3;
        }
        return n | 33554432;
    }

    private int init(ClassWriter classWriter, int n) {
        block9 : {
            int n2;
            block8 : {
                block7 : {
                    if (n != 16777222) break block7;
                    n2 = 24117248 | classWriter.addType(classWriter.thisName);
                    break block8;
                }
                if ((-1048576 & n) != 25165824) break block9;
                n2 = 24117248 | classWriter.addType(classWriter.typeTable[1048575 & n].strVal1);
            }
            for (int i = 0; i < this.initializationCount; ++i) {
                int n3;
                int n4 = this.initializations[i];
                int n5 = -268435456 & n4;
                int n6 = 251658240 & n4;
                if (n6 == 33554432) {
                    n3 = n5 + this.inputLocals[8388607 & n4];
                } else {
                    n3 = n4;
                    if (n6 == 50331648) {
                        n3 = n5 + this.inputStack[this.inputStack.length - (8388607 & n4)];
                    }
                }
                if (n != n3) continue;
                return n2;
            }
            return n;
        }
        return n;
    }

    private void init(int n) {
        int[] arrn;
        int n2;
        if (this.initializations == null) {
            this.initializations = new int[2];
        }
        if (this.initializationCount >= (n2 = this.initializations.length)) {
            arrn = new int[Math.max(this.initializationCount + 1, n2 * 2)];
            System.arraycopy(this.initializations, 0, arrn, 0, n2);
            this.initializations = arrn;
        }
        arrn = this.initializations;
        n2 = this.initializationCount;
        this.initializationCount = n2 + 1;
        arrn[n2] = n;
    }

    private static boolean merge(ClassWriter classWriter, int n, int[] arrn, int n2) {
        int n3 = arrn[n2];
        if (n3 == n) {
            return false;
        }
        int n4 = n;
        if ((268435455 & n) == 16777221) {
            if (n3 == 16777221) {
                return false;
            }
            n4 = 16777221;
        }
        if (n3 == 0) {
            arrn[n2] = n4;
            return true;
        }
        n = 16777216;
        if ((n3 & 267386880) != 24117248 && (n3 & -268435456) == 0) {
            if (n3 == 16777221 && ((n4 & 267386880) == 24117248 || (n4 & -268435456) != 0)) {
                n = n4;
            }
        } else {
            if (n4 == 16777221) {
                return false;
            }
            if ((n4 & -1048576) == (-1048576 & n3)) {
                n = (n3 & 267386880) == 24117248 ? n4 & -268435456 | 24117248 | classWriter.getMergedType(n4 & 1048575, 1048575 & n3) : (n3 & -268435456) - 268435456 | 24117248 | classWriter.addType("java/lang/Object");
            } else if ((n4 & 267386880) != 24117248 && (n4 & -268435456) == 0) {
                n = 16777216;
            } else {
                n = (n4 & -268435456) != 0 && (n4 & 267386880) != 24117248 ? -268435456 : 0;
                int n5 = (n3 & -268435456) != 0 && (267386880 & n3) != 24117248 ? -268435456 : 0;
                n = Math.min(n + (n4 & -268435456), n5 + (n3 & -268435456));
                n = classWriter.addType("java/lang/Object") | (n | 24117248);
            }
        }
        if (n3 != n) {
            arrn[n2] = n;
            return true;
        }
        return false;
    }

    private int pop() {
        int n;
        if (this.outputStackTop > 0) {
            int n2;
            int[] arrn = this.outputStack;
            this.outputStackTop = n2 = this.outputStackTop - 1;
            return arrn[n2];
        }
        Label label = this.owner;
        label.inputStackTop = n = label.inputStackTop - 1;
        return 50331648 | - n;
    }

    private void pop(int n) {
        if (this.outputStackTop >= n) {
            this.outputStackTop -= n;
            return;
        }
        Label label = this.owner;
        label.inputStackTop -= n - this.outputStackTop;
        this.outputStackTop = 0;
    }

    private void pop(String string2) {
        char c = string2.charAt(0);
        if (c == '(') {
            this.pop((Type.getArgumentsAndReturnSizes(string2) >> 2) - 1);
            return;
        }
        if (c != 'J' && c != 'D') {
            this.pop(1);
            return;
        }
        this.pop(2);
    }

    private void push(int n) {
        int[] arrn;
        int n2;
        if (this.outputStack == null) {
            this.outputStack = new int[10];
        }
        if (this.outputStackTop >= (n2 = this.outputStack.length)) {
            arrn = new int[Math.max(this.outputStackTop + 1, n2 * 2)];
            System.arraycopy(this.outputStack, 0, arrn, 0, n2);
            this.outputStack = arrn;
        }
        arrn = this.outputStack;
        n2 = this.outputStackTop;
        this.outputStackTop = n2 + 1;
        arrn[n2] = n;
        n = this.owner.inputStackTop + this.outputStackTop;
        if (n > this.owner.outputStackMax) {
            this.owner.outputStackMax = n;
        }
    }

    private void push(ClassWriter classWriter, String string2) {
        int n = Frame.type(classWriter, string2);
        if (n != 0) {
            this.push(n);
            if (n == 16777220 || n == 16777219) {
                this.push(16777216);
            }
        }
    }

    private void set(int n, int n2) {
        int n3;
        if (this.outputLocals == null) {
            this.outputLocals = new int[10];
        }
        if (n >= (n3 = this.outputLocals.length)) {
            int[] arrn = new int[Math.max(n + 1, n3 * 2)];
            System.arraycopy(this.outputLocals, 0, arrn, 0, n3);
            this.outputLocals = arrn;
        }
        this.outputLocals[n] = n2;
    }

    /*
     * Exception decompiling
     */
    private static int type(ClassWriter var0, String var1_1) {
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

    /*
     * Exception decompiling
     */
    void execute(int var1_1, int var2_2, ClassWriter var3_3, Item var4_4) {
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

    final void initInputFrame(ClassWriter classWriter, int n, Type[] arrtype, int n2) {
        int[] arrn;
        int n3;
        this.inputLocals = new int[n2];
        int n4 = 0;
        this.inputStack = new int[0];
        if ((n & 8) == 0) {
            if ((524288 & n) == 0) {
                arrn = this.inputLocals;
                n = 0 + 1;
                arrn[0] = 24117248 | classWriter.addType(classWriter.thisName);
            } else {
                arrn = this.inputLocals;
                n = 0 + 1;
                arrn[0] = 16777222;
            }
        } else {
            n = 0;
        }
        do {
            if (n4 >= arrtype.length) break;
            int n5 = Frame.type(classWriter, arrtype[n4].getDescriptor());
            arrn = this.inputLocals;
            n3 = n + 1;
            arrn[n] = n5;
            if (n5 != 16777220 && n5 != 16777219) {
                n = n3;
            } else {
                this.inputLocals[n3] = 16777216;
                n = n3 + 1;
            }
            ++n4;
        } while (true);
        for (n3 = n; n3 < n2; ++n3) {
            this.inputLocals[n3] = 16777216;
        }
    }

    final boolean merge(ClassWriter classWriter, Frame frame, int n) {
        int n2;
        int n3;
        int n4;
        boolean bl = false;
        int n5 = this.inputLocals.length;
        int n6 = this.inputStack.length;
        if (frame.inputLocals == null) {
            frame.inputLocals = new int[n5];
            bl = true;
        }
        for (n4 = 0; n4 < n5; ++n4) {
            if (this.outputLocals != null && n4 < this.outputLocals.length) {
                n3 = this.outputLocals[n4];
                if (n3 == 0) {
                    n2 = this.inputLocals[n4];
                } else {
                    n2 = -268435456 & n3;
                    int n7 = 251658240 & n3;
                    if (n7 == 16777216) {
                        n2 = n3;
                    } else {
                        n2 = n7 == 33554432 ? this.inputLocals[n3 & 8388607] + n2 : this.inputStack[n6 - (n3 & 8388607)] + n2;
                        if ((8388608 & n3) != 0 && (n2 == 16777220 || n2 == 16777219)) {
                            n2 = 16777216;
                        }
                    }
                }
            } else {
                n2 = this.inputLocals[n4];
            }
            n3 = n2;
            if (this.initializations != null) {
                n3 = this.init(classWriter, n2);
            }
            bl |= Frame.merge(classWriter, n3, frame.inputLocals, n4);
        }
        if (n > 0) {
            for (n2 = 0; n2 < n5; ++n2) {
                bl |= Frame.merge(classWriter, this.inputLocals[n2], frame.inputLocals, n2);
            }
            if (frame.inputStack == null) {
                frame.inputStack = new int[1];
                bl = true;
            }
            return Frame.merge(classWriter, n, frame.inputStack, 0) | bl;
        }
        n5 = this.inputStack.length + this.owner.inputStackTop;
        if (frame.inputStack == null) {
            frame.inputStack = new int[this.outputStackTop + n5];
            bl = true;
        }
        for (n = 0; n < n5; ++n) {
            n2 = n4 = this.inputStack[n];
            if (this.initializations != null) {
                n2 = this.init(classWriter, n4);
            }
            bl |= Frame.merge(classWriter, n2, frame.inputStack, n);
        }
        for (n4 = 0; n4 < this.outputStackTop; ++n4) {
            block23 : {
                block24 : {
                    block22 : {
                        n3 = this.outputStack[n4];
                        n = n3 & -268435456;
                        n2 = n3 & 251658240;
                        if (n2 != 16777216) break block22;
                        n2 = n3;
                        break block23;
                    }
                    n = n2 == 33554432 ? this.inputLocals[n3 & 8388607] + n : this.inputStack[n6 - (n3 & 8388607)] + n;
                    n2 = n;
                    if ((n3 & 8388608) == 0) break block23;
                    if (n == 16777220) break block24;
                    n2 = n;
                    if (n != 16777219) break block23;
                }
                n2 = 16777216;
            }
            n = n2;
            if (this.initializations != null) {
                n = this.init(classWriter, n2);
            }
            bl |= Frame.merge(classWriter, n, frame.inputStack, n5 + n4);
        }
        return bl;
    }

    final void set(ClassWriter classWriter, int n, Object[] arrobject, int n2, Object[] arrobject2) {
        for (n = Frame.convert((ClassWriter)classWriter, (int)n, (Object[])arrobject, (int[])this.inputLocals); n < arrobject.length; ++n) {
            this.inputLocals[n] = 16777216;
        }
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            int n4;
            block5 : {
                block4 : {
                    if (arrobject2[n] == Opcodes.LONG) break block4;
                    n4 = n3;
                    if (arrobject2[n] != Opcodes.DOUBLE) break block5;
                }
                n4 = n3 + 1;
            }
            n3 = n4;
        }
        this.inputStack = new int[n2 + n3];
        Frame.convert(classWriter, n2, arrobject2, this.inputStack);
        this.outputStackTop = 0;
        this.initializationCount = 0;
    }

    final void set(Frame frame) {
        this.inputLocals = frame.inputLocals;
        this.inputStack = frame.inputStack;
        this.outputLocals = frame.outputLocals;
        this.outputStack = frame.outputStack;
        this.outputStackTop = frame.outputStackTop;
        this.initializationCount = frame.initializationCount;
        this.initializations = frame.initializations;
    }
}

