// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LookupSwitchInsnNode extends AbstractInsnNode
{
    public LabelNode dflt;
    public List<Integer> keys;
    public List<LabelNode> labels;
    
    public LookupSwitchInsnNode(final LabelNode dflt, final int[] array, final LabelNode[] array2) {
        super(171);
        this.dflt = dflt;
        final int n = 0;
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        this.keys = new ArrayList<Integer>(length);
        int length2;
        if (array2 == null) {
            length2 = 0;
        }
        else {
            length2 = array2.length;
        }
        this.labels = new ArrayList<LabelNode>(length2);
        if (array != null) {
            for (int i = n; i < array.length; ++i) {
                this.keys.add(array[i]);
            }
        }
        if (array2 != null) {
            this.labels.addAll(Arrays.asList(array2));
        }
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        final int[] array = new int[this.keys.size()];
        final int n = 0;
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.keys.get(i);
        }
        final Label[] array2 = new Label[this.labels.size()];
        for (int j = n; j < array2.length; ++j) {
            array2[j] = this.labels.get(j).getLabel();
        }
        methodVisitor.visitLookupSwitchInsn(this.dflt.getLabel(), array, array2);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        final LookupSwitchInsnNode lookupSwitchInsnNode = new LookupSwitchInsnNode(AbstractInsnNode.clone(this.dflt, map), null, AbstractInsnNode.clone(this.labels, map));
        lookupSwitchInsnNode.keys.addAll(this.keys);
        return lookupSwitchInsnNode.cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 12;
    }
}
