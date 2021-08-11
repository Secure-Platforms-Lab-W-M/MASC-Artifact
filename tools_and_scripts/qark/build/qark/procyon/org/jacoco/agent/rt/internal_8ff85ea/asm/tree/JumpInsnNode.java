// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public class JumpInsnNode extends AbstractInsnNode
{
    public LabelNode label;
    
    public JumpInsnNode(final int n, final LabelNode label) {
        super(n);
        this.label = label;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitJumpInsn(this.opcode, this.label.getLabel());
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new JumpInsnNode(this.opcode, AbstractInsnNode.clone(this.label, map)).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 7;
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
}
