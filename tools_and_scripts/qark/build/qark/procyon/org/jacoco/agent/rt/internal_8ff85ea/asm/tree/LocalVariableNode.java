// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public class LocalVariableNode
{
    public String desc;
    public LabelNode end;
    public int index;
    public String name;
    public String signature;
    public LabelNode start;
    
    public LocalVariableNode(final String name, final String desc, final String signature, final LabelNode start, final LabelNode end, final int index) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.start = start;
        this.end = end;
        this.index = index;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(this.name, this.desc, this.signature, this.start.getLabel(), this.end.getLabel(), this.index);
    }
}
