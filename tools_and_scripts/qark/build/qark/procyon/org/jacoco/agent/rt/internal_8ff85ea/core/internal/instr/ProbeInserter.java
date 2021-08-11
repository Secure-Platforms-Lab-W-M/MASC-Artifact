// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Type;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

class ProbeInserter extends MethodVisitor implements IProbeInserter
{
    private int accessorStackSize;
    private final IProbeArrayStrategy arrayStrategy;
    private final boolean clinit;
    private final int variable;
    
    ProbeInserter(int variable, final String s, final String s2, final MethodVisitor methodVisitor, final IProbeArrayStrategy arrayStrategy) {
        super(327680, methodVisitor);
        this.clinit = "<clinit>".equals(s);
        this.arrayStrategy = arrayStrategy;
        int i = 0;
        if ((variable & 0x8) == 0x0) {
            variable = 1;
        }
        else {
            variable = 0;
        }
        for (Type[] argumentTypes = Type.getArgumentTypes(s2); i < argumentTypes.length; ++i) {
            variable += argumentTypes[i].getSize();
        }
        this.variable = variable;
    }
    
    private int map(final int n) {
        if (n < this.variable) {
            return n;
        }
        return n + 1;
    }
    
    @Override
    public void insertProbe(final int n) {
        this.mv.visitVarInsn(25, this.variable);
        InstrSupport.push(this.mv, n);
        this.mv.visitInsn(4);
        this.mv.visitInsn(84);
    }
    
    @Override
    public void visitCode() {
        this.accessorStackSize = this.arrayStrategy.storeInstance(this.mv, this.clinit, this.variable);
        this.mv.visitCode();
    }
    
    @Override
    public final void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        if (n == -1) {
            final Object[] array3 = new Object[Math.max(n2, this.variable) + 1];
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            while (n5 < n2 || n4 <= this.variable) {
                if (n4 == this.variable) {
                    final int n7 = n6 + 1;
                    array3[n6] = "[Z";
                    ++n4;
                    n6 = n7;
                }
                else if (n5 < n2) {
                    final Object o = array[n5];
                    array3[n6] = o;
                    final int n8 = n4 + 1;
                    Label_0150: {
                        if (o != Opcodes.LONG) {
                            n4 = n8;
                            if (o != Opcodes.DOUBLE) {
                                break Label_0150;
                            }
                        }
                        n4 = n8 + 1;
                    }
                    ++n5;
                    ++n6;
                }
                else {
                    final int n9 = n6 + 1;
                    array3[n6] = Opcodes.TOP;
                    ++n4;
                    n6 = n9;
                }
            }
            this.mv.visitFrame(n, n6, array3, n3, array2);
            return;
        }
        throw new IllegalArgumentException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
    }
    
    @Override
    public final void visitIincInsn(final int n, final int n2) {
        this.mv.visitIincInsn(this.map(n), n2);
    }
    
    @Override
    public final void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        this.mv.visitLocalVariable(s, s2, s3, label, label2, this.map(n));
    }
    
    @Override
    public void visitMaxs(int max, final int n) {
        max = Math.max(max + 3, this.accessorStackSize);
        this.mv.visitMaxs(max, n + 1);
    }
    
    @Override
    public final void visitVarInsn(final int n, final int n2) {
        this.mv.visitVarInsn(n, this.map(n2));
    }
}
