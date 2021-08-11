// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

class DuplicateFrameEliminator extends MethodVisitor
{
    private boolean instruction;
    
    public DuplicateFrameEliminator(final MethodVisitor methodVisitor) {
        super(327680, methodVisitor);
        this.instruction = true;
    }
    
    @Override
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        this.instruction = true;
        this.mv.visitFieldInsn(n, s, s2, s3);
    }
    
    @Override
    public void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        if (this.instruction) {
            this.instruction = false;
            this.mv.visitFrame(n, n2, array, n3, array2);
        }
    }
    
    @Override
    public void visitIincInsn(final int n, final int n2) {
        this.instruction = true;
        this.mv.visitIincInsn(n, n2);
    }
    
    @Override
    public void visitInsn(final int n) {
        this.instruction = true;
        this.mv.visitInsn(n);
    }
    
    @Override
    public void visitIntInsn(final int n, final int n2) {
        this.instruction = true;
        this.mv.visitIntInsn(n, n2);
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        this.instruction = true;
        this.mv.visitInvokeDynamicInsn(s, s2, handle, array);
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label label) {
        this.instruction = true;
        this.mv.visitJumpInsn(n, label);
    }
    
    @Override
    public void visitLdcInsn(final Object o) {
        this.instruction = true;
        this.mv.visitLdcInsn(o);
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        this.instruction = true;
        this.mv.visitLookupSwitchInsn(label, array, array2);
    }
    
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        this.instruction = true;
        this.mv.visitMethodInsn(n, s, s2, s3, b);
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String s, final int n) {
        this.instruction = true;
        this.mv.visitMultiANewArrayInsn(s, n);
    }
    
    @Override
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        this.instruction = true;
        this.mv.visitTableSwitchInsn(n, n2, label, array);
    }
    
    @Override
    public void visitTypeInsn(final int n, final String s) {
        this.instruction = true;
        this.mv.visitTypeInsn(n, s);
    }
    
    @Override
    public void visitVarInsn(final int n, final int n2) {
        this.instruction = true;
        this.mv.visitVarInsn(n, n2);
    }
}
