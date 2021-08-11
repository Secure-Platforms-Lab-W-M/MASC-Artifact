// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;

public class InvokeDynamicInsnNode extends AbstractInsnNode
{
    public Handle bsm;
    public Object[] bsmArgs;
    public String desc;
    public String name;
    
    public InvokeDynamicInsnNode(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
        super(186);
        this.name = name;
        this.desc = desc;
        this.bsm = bsm;
        this.bsmArgs = bsmArgs;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 6;
    }
}
