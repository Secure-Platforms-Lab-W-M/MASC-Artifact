// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;

public interface IProbeArrayStrategy
{
    void addMembers(final ClassVisitor p0, final int p1);
    
    int storeInstance(final MethodVisitor p0, final boolean p1, final int p2);
}
