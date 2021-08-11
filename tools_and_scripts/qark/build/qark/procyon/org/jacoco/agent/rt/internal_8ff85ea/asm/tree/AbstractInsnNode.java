// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;
import java.util.ArrayList;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import java.util.Map;
import java.util.List;

public abstract class AbstractInsnNode
{
    public static final int FIELD_INSN = 4;
    public static final int FRAME = 14;
    public static final int IINC_INSN = 10;
    public static final int INSN = 0;
    public static final int INT_INSN = 1;
    public static final int INVOKE_DYNAMIC_INSN = 6;
    public static final int JUMP_INSN = 7;
    public static final int LABEL = 8;
    public static final int LDC_INSN = 9;
    public static final int LINE = 15;
    public static final int LOOKUPSWITCH_INSN = 12;
    public static final int METHOD_INSN = 5;
    public static final int MULTIANEWARRAY_INSN = 13;
    public static final int TABLESWITCH_INSN = 11;
    public static final int TYPE_INSN = 3;
    public static final int VAR_INSN = 2;
    int index;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    AbstractInsnNode next;
    protected int opcode;
    AbstractInsnNode prev;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    
    protected AbstractInsnNode(final int opcode) {
        this.opcode = opcode;
        this.index = -1;
    }
    
    static LabelNode clone(final LabelNode labelNode, final Map<LabelNode, LabelNode> map) {
        return map.get(labelNode);
    }
    
    static LabelNode[] clone(final List<LabelNode> list, final Map<LabelNode, LabelNode> map) {
        final LabelNode[] array = new LabelNode[list.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = map.get(list.get(i));
        }
        return array;
    }
    
    public abstract void accept(final MethodVisitor p0);
    
    protected final void acceptAnnotations(final MethodVisitor methodVisitor) {
        int size;
        if (this.visibleTypeAnnotations == null) {
            size = 0;
        }
        else {
            size = this.visibleTypeAnnotations.size();
        }
        for (int i = 0; i < size; ++i) {
            final TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i);
            typeAnnotationNode.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size2;
        if (this.invisibleTypeAnnotations == null) {
            size2 = 0;
        }
        else {
            size2 = this.invisibleTypeAnnotations.size();
        }
        for (int j = 0; j < size2; ++j) {
            final TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(j);
            typeAnnotationNode2.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
    }
    
    public abstract AbstractInsnNode clone(final Map<LabelNode, LabelNode> p0);
    
    protected final AbstractInsnNode cloneAnnotations(final AbstractInsnNode abstractInsnNode) {
        final List<TypeAnnotationNode> visibleTypeAnnotations = abstractInsnNode.visibleTypeAnnotations;
        final int n = 0;
        if (visibleTypeAnnotations != null) {
            this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            for (int i = 0; i < abstractInsnNode.visibleTypeAnnotations.size(); ++i) {
                final TypeAnnotationNode typeAnnotationNode = abstractInsnNode.visibleTypeAnnotations.get(i);
                final TypeAnnotationNode typeAnnotationNode2 = new TypeAnnotationNode(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc);
                typeAnnotationNode.accept(typeAnnotationNode2);
                this.visibleTypeAnnotations.add(typeAnnotationNode2);
            }
        }
        if (abstractInsnNode.invisibleTypeAnnotations != null) {
            this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            for (int j = n; j < abstractInsnNode.invisibleTypeAnnotations.size(); ++j) {
                final TypeAnnotationNode typeAnnotationNode3 = abstractInsnNode.invisibleTypeAnnotations.get(j);
                final TypeAnnotationNode typeAnnotationNode4 = new TypeAnnotationNode(typeAnnotationNode3.typeRef, typeAnnotationNode3.typePath, typeAnnotationNode3.desc);
                typeAnnotationNode3.accept(typeAnnotationNode4);
                this.invisibleTypeAnnotations.add(typeAnnotationNode4);
            }
        }
        return this;
    }
    
    public AbstractInsnNode getNext() {
        return this.next;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public AbstractInsnNode getPrevious() {
        return this.prev;
    }
    
    public abstract int getType();
}
