// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public class IincInsnNode extends AbstractInsnNode
{
    public int incr;
    public int var;
    
    public IincInsnNode(final int var, final int incr) {
        super(132);
        this.var = var;
        this.incr = incr;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitIincInsn(this.var, this.incr);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new IincInsnNode(this.var, this.incr).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 10;
    }
}
