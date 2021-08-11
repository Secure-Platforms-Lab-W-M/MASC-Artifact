// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.ArrayList;
import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import java.util.Arrays;
import java.util.List;

public class FrameNode extends AbstractInsnNode
{
    public List<Object> local;
    public List<Object> stack;
    public int type;
    
    private FrameNode() {
        super(-1);
    }
    
    public FrameNode(final int type, final int n, final Object[] array, final int n2, final Object[] array2) {
        super(-1);
        switch (this.type = type) {
            default: {}
            case 4: {
                this.stack = asList(1, array2);
            }
            case 3: {}
            case 2: {
                this.local = Arrays.asList(new Object[n]);
            }
            case 1: {
                this.local = asList(n, array);
            }
            case -1:
            case 0: {
                this.local = asList(n, array);
                this.stack = asList(n2, array2);
            }
        }
    }
    
    private static Object[] asArray(final List<Object> list) {
        final Object[] array = new Object[list.size()];
        for (int i = 0; i < array.length; ++i) {
            Object o;
            final LabelNode labelNode = (LabelNode)(o = list.get(i));
            if (labelNode instanceof LabelNode) {
                o = labelNode.getLabel();
            }
            array[i] = o;
        }
        return array;
    }
    
    private static List<Object> asList(final int n, final Object[] array) {
        return Arrays.asList(array).subList(0, n);
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        switch (this.type) {
            default: {}
            case 4: {
                methodVisitor.visitFrame(this.type, 0, null, 1, asArray(this.stack));
            }
            case 3: {
                methodVisitor.visitFrame(this.type, 0, null, 0, null);
            }
            case 2: {
                methodVisitor.visitFrame(this.type, this.local.size(), null, 0, null);
            }
            case 1: {
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), 0, null);
            }
            case -1:
            case 0: {
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), asArray(this.stack));
            }
        }
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        final FrameNode frameNode = new FrameNode();
        frameNode.type = this.type;
        final List<Object> local = this.local;
        final int n = 0;
        if (local != null) {
            frameNode.local = new ArrayList<Object>();
            for (int i = 0; i < this.local.size(); ++i) {
                Object o2;
                final Object o = o2 = this.local.get(i);
                if (o instanceof LabelNode) {
                    o2 = map.get(o);
                }
                frameNode.local.add(o2);
            }
        }
        if (this.stack != null) {
            frameNode.stack = new ArrayList<Object>();
            for (int j = n; j < this.stack.size(); ++j) {
                Object o4;
                final Object o3 = o4 = this.stack.get(j);
                if (o3 instanceof LabelNode) {
                    o4 = map.get(o3);
                }
                frameNode.stack.add(o4);
            }
        }
        return frameNode;
    }
    
    @Override
    public int getType() {
        return 14;
    }
}
