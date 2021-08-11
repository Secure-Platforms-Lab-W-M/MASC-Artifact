// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;

public abstract class ClassProbesVisitor extends ClassVisitor
{
    public ClassProbesVisitor() {
        this(null);
    }
    
    public ClassProbesVisitor(final ClassVisitor classVisitor) {
        super(327680, classVisitor);
    }
    
    @Override
    public abstract MethodProbesVisitor visitMethod(final int p0, final String p1, final String p2, final String p3, final String[] p4);
    
    public abstract void visitTotalProbeCount(final int p0);
}
