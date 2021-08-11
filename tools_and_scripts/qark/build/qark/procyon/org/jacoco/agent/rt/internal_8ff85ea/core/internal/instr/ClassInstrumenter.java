// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.MethodProbesVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.FieldVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesVisitor;

public class ClassInstrumenter extends ClassProbesVisitor
{
    private String className;
    private final IProbeArrayStrategy probeArrayStrategy;
    
    public ClassInstrumenter(final IProbeArrayStrategy probeArrayStrategy, final ClassVisitor classVisitor) {
        super(classVisitor);
        this.probeArrayStrategy = probeArrayStrategy;
    }
    
    @Override
    public void visit(final int n, final int n2, final String className, final String s, final String s2, final String[] array) {
        super.visit(n, n2, this.className = className, s, s2, array);
    }
    
    @Override
    public FieldVisitor visitField(final int n, final String s, final String s2, final String s3, final Object o) {
        InstrSupport.assertNotInstrumented(s, this.className);
        return super.visitField(n, s, s2, s3, o);
    }
    
    @Override
    public MethodProbesVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        InstrSupport.assertNotInstrumented(s, this.className);
        final MethodVisitor visitMethod = this.cv.visitMethod(n, s, s2, s3, array);
        if (visitMethod == null) {
            return null;
        }
        final ProbeInserter probeInserter = new ProbeInserter(n, s, s2, new DuplicateFrameEliminator(visitMethod), this.probeArrayStrategy);
        return new MethodInstrumenter(probeInserter, probeInserter);
    }
    
    @Override
    public void visitTotalProbeCount(final int n) {
        this.probeArrayStrategy.addMembers(this.cv, n);
    }
}
