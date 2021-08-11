// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.MethodProbesVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesVisitor;

class ProbeCounter extends ClassProbesVisitor
{
    private int count;
    private boolean methods;
    
    ProbeCounter() {
        this.count = 0;
        this.methods = false;
    }
    
    int getCount() {
        return this.count;
    }
    
    boolean hasMethods() {
        return this.methods;
    }
    
    @Override
    public MethodProbesVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        if (!"<clinit>".equals(s) && (n & 0x400) == 0x0) {
            this.methods = true;
        }
        return null;
    }
    
    @Override
    public void visitTotalProbeCount(final int count) {
        this.count = count;
    }
}
