// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.TryCatchBlockNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.MethodNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public final class LabelFlowAnalyzer extends MethodVisitor
{
    boolean first;
    Label lineStart;
    boolean successor;
    
    public LabelFlowAnalyzer() {
        super(327680);
        this.successor = false;
        this.first = true;
        this.lineStart = null;
    }
    
    public static void markLabels(final MethodNode methodNode) {
        final LabelFlowAnalyzer labelFlowAnalyzer = new LabelFlowAnalyzer();
        int size = methodNode.tryCatchBlocks.size();
        while (true) {
            --size;
            if (size < 0) {
                break;
            }
            methodNode.tryCatchBlocks.get(size).accept(labelFlowAnalyzer);
        }
        methodNode.instructions.accept(labelFlowAnalyzer);
    }
    
    private void markMethodInvocationLine() {
        if (this.lineStart != null) {
            LabelInfo.setMethodInvocationLine(this.lineStart);
        }
    }
    
    private static void setTargetIfNotDone(final Label label) {
        if (!LabelInfo.isDone(label)) {
            LabelInfo.setTarget(label);
            LabelInfo.setDone(label);
        }
    }
    
    private void visitSwitchInsn(final Label targetIfNotDone, final Label[] array) {
        LabelInfo.resetDone(targetIfNotDone);
        LabelInfo.resetDone(array);
        setTargetIfNotDone(targetIfNotDone);
        for (int length = array.length, i = 0; i < length; ++i) {
            setTargetIfNotDone(array[i]);
        }
        this.successor = false;
        this.first = false;
    }
    
    @Override
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitIincInsn(final int n, final int n2) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitInsn(final int n) {
        if (n != 169) {
            Label_0065: {
                if (n != 191) {
                    switch (n) {
                        default: {
                            this.successor = true;
                            break Label_0065;
                        }
                        case 172:
                        case 173:
                        case 174:
                        case 175:
                        case 176:
                        case 177: {
                            break;
                        }
                    }
                }
                this.successor = false;
            }
            this.first = false;
            return;
        }
        throw new AssertionError((Object)"Subroutines not supported.");
    }
    
    @Override
    public void visitIntInsn(final int n, final int n2) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        this.successor = true;
        this.first = false;
        this.markMethodInvocationLine();
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label target) {
        LabelInfo.setTarget(target);
        if (n != 168) {
            this.successor = (n != 167);
            this.first = false;
            return;
        }
        throw new AssertionError((Object)"Subroutines not supported.");
    }
    
    @Override
    public void visitLabel(final Label label) {
        if (this.first) {
            LabelInfo.setTarget(label);
        }
        if (this.successor) {
            LabelInfo.setSuccessor(label);
        }
    }
    
    @Override
    public void visitLdcInsn(final Object o) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitLineNumber(final int n, final Label lineStart) {
        this.lineStart = lineStart;
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        this.visitSwitchInsn(label, array2);
    }
    
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        this.successor = true;
        this.first = false;
        this.markMethodInvocationLine();
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String s, final int n) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        this.visitSwitchInsn(label, array);
    }
    
    @Override
    public void visitTryCatchBlock(final Label target, final Label label, final Label target2, final String s) {
        LabelInfo.setTarget(target);
        LabelInfo.setTarget(target2);
    }
    
    @Override
    public void visitTypeInsn(final int n, final String s) {
        this.successor = true;
        this.first = false;
    }
    
    @Override
    public void visitVarInsn(final int n, final int n2) {
        this.successor = true;
        this.first = false;
    }
}
