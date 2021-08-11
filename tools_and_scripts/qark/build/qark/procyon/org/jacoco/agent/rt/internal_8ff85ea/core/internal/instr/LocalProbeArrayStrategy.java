// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;

class LocalProbeArrayStrategy implements IProbeArrayStrategy
{
    private final IExecutionDataAccessorGenerator accessorGenerator;
    private final long classId;
    private final String className;
    private final int probeCount;
    
    LocalProbeArrayStrategy(final String className, final long classId, final int probeCount, final IExecutionDataAccessorGenerator accessorGenerator) {
        this.className = className;
        this.classId = classId;
        this.probeCount = probeCount;
        this.accessorGenerator = accessorGenerator;
    }
    
    @Override
    public void addMembers(final ClassVisitor classVisitor, final int n) {
    }
    
    @Override
    public int storeInstance(final MethodVisitor methodVisitor, final boolean b, final int n) {
        final int generateDataAccessor = this.accessorGenerator.generateDataAccessor(this.classId, this.className, this.probeCount, methodVisitor);
        methodVisitor.visitVarInsn(58, n);
        return generateDataAccessor;
    }
}
