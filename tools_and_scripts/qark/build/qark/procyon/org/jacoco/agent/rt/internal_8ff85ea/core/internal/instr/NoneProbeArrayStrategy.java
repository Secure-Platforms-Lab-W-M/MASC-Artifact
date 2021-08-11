// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;

class NoneProbeArrayStrategy implements IProbeArrayStrategy
{
    @Override
    public void addMembers(final ClassVisitor classVisitor, final int n) {
    }
    
    @Override
    public int storeInstance(final MethodVisitor methodVisitor, final boolean b, final int n) {
        throw new UnsupportedOperationException();
    }
}
