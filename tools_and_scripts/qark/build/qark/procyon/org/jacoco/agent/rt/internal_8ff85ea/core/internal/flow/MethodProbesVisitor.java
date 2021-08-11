// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public abstract class MethodProbesVisitor extends MethodVisitor
{
    public MethodProbesVisitor() {
        this(null);
    }
    
    public MethodProbesVisitor(final MethodVisitor methodVisitor) {
        super(327680, methodVisitor);
    }
    
    public void visitInsnWithProbe(final int n, final int n2) {
    }
    
    public void visitJumpInsnWithProbe(final int n, final Label label, final int n2, final IFrame frame) {
    }
    
    public void visitLookupSwitchInsnWithProbes(final Label label, final int[] array, final Label[] array2, final IFrame frame) {
    }
    
    public void visitProbe(final int n) {
    }
    
    public void visitTableSwitchInsnWithProbes(final int n, final int n2, final Label label, final Label[] array, final IFrame frame) {
    }
}
