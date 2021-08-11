// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Type;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Handle;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;
import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Attribute;
import java.util.List;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public class MethodNode extends MethodVisitor
{
    public int access;
    public Object annotationDefault;
    public List<Attribute> attrs;
    public String desc;
    public List<String> exceptions;
    public InsnList instructions;
    public List<AnnotationNode> invisibleAnnotations;
    public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;
    public List<AnnotationNode>[] invisibleParameterAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<LocalVariableNode> localVariables;
    public int maxLocals;
    public int maxStack;
    public String name;
    public List<ParameterNode> parameters;
    public String signature;
    public List<TryCatchBlockNode> tryCatchBlocks;
    public List<AnnotationNode> visibleAnnotations;
    public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;
    public List<AnnotationNode>[] visibleParameterAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    private boolean visited;
    
    public MethodNode() {
        this(327680);
        if (this.getClass() == MethodNode.class) {
            return;
        }
        throw new IllegalStateException();
    }
    
    public MethodNode(final int n) {
        super(n);
        this.instructions = new InsnList();
    }
    
    public MethodNode(int length, final int access, final String name, final String desc, final String signature, final String[] array) {
        super(length);
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        final int n = 0;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        this.exceptions = new ArrayList<String>(length);
        length = n;
        if ((access & 0x400) != 0x0) {
            length = 1;
        }
        if (length == 0) {
            this.localVariables = new ArrayList<LocalVariableNode>(5);
        }
        this.tryCatchBlocks = new ArrayList<TryCatchBlockNode>();
        if (array != null) {
            this.exceptions.addAll(Arrays.asList(array));
        }
        this.instructions = new InsnList();
    }
    
    public MethodNode(final int n, final String s, final String s2, final String s3, final String[] array) {
        this(327680, n, s, s2, s3, array);
        if (this.getClass() == MethodNode.class) {
            return;
        }
        throw new IllegalStateException();
    }
    
    private Object[] getLabelNodes(final Object[] array) {
        final Object[] array2 = new Object[array.length];
        for (int i = 0; i < array.length; ++i) {
            Object labelNode;
            final Object o = labelNode = array[i];
            if (o instanceof Label) {
                labelNode = this.getLabelNode((Label)o);
            }
            array2[i] = labelNode;
        }
        return array2;
    }
    
    private LabelNode[] getLabelNodes(final Label[] array) {
        final LabelNode[] array2 = new LabelNode[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.getLabelNode(array[i]);
        }
        return array2;
    }
    
    public void accept(final ClassVisitor classVisitor) {
        final String[] array = new String[this.exceptions.size()];
        this.exceptions.toArray(array);
        final MethodVisitor visitMethod = classVisitor.visitMethod(this.access, this.name, this.desc, this.signature, array);
        if (visitMethod != null) {
            this.accept(visitMethod);
        }
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        int size;
        if (this.parameters == null) {
            size = 0;
        }
        else {
            size = this.parameters.size();
        }
        for (int i = 0; i < size; ++i) {
            final ParameterNode parameterNode = this.parameters.get(i);
            methodVisitor.visitParameter(parameterNode.name, parameterNode.access);
        }
        if (this.annotationDefault != null) {
            final AnnotationVisitor visitAnnotationDefault = methodVisitor.visitAnnotationDefault();
            AnnotationNode.accept(visitAnnotationDefault, null, this.annotationDefault);
            if (visitAnnotationDefault != null) {
                visitAnnotationDefault.visitEnd();
            }
        }
        int size2;
        if (this.visibleAnnotations == null) {
            size2 = 0;
        }
        else {
            size2 = this.visibleAnnotations.size();
        }
        for (int j = 0; j < size2; ++j) {
            final AnnotationNode annotationNode = this.visibleAnnotations.get(j);
            annotationNode.accept(methodVisitor.visitAnnotation(annotationNode.desc, true));
        }
        int size3;
        if (this.invisibleAnnotations == null) {
            size3 = 0;
        }
        else {
            size3 = this.invisibleAnnotations.size();
        }
        for (int k = 0; k < size3; ++k) {
            final AnnotationNode annotationNode2 = this.invisibleAnnotations.get(k);
            annotationNode2.accept(methodVisitor.visitAnnotation(annotationNode2.desc, false));
        }
        int size4;
        if (this.visibleTypeAnnotations == null) {
            size4 = 0;
        }
        else {
            size4 = this.visibleTypeAnnotations.size();
        }
        for (int l = 0; l < size4; ++l) {
            final TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(l);
            typeAnnotationNode.accept(methodVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size5;
        if (this.invisibleTypeAnnotations == null) {
            size5 = 0;
        }
        else {
            size5 = this.invisibleTypeAnnotations.size();
        }
        for (int n = 0; n < size5; ++n) {
            final TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(n);
            typeAnnotationNode2.accept(methodVisitor.visitTypeAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
        int length;
        if (this.visibleParameterAnnotations == null) {
            length = 0;
        }
        else {
            length = this.visibleParameterAnnotations.length;
        }
        for (int n2 = 0; n2 < length; ++n2) {
            final List<AnnotationNode> list = this.visibleParameterAnnotations[n2];
            if (list != null) {
                for (int n3 = 0; n3 < list.size(); ++n3) {
                    final AnnotationNode annotationNode3 = list.get(n3);
                    annotationNode3.accept(methodVisitor.visitParameterAnnotation(n2, annotationNode3.desc, true));
                }
            }
        }
        int length2;
        if (this.invisibleParameterAnnotations == null) {
            length2 = 0;
        }
        else {
            length2 = this.invisibleParameterAnnotations.length;
        }
        for (int n4 = 0; n4 < length2; ++n4) {
            final List<AnnotationNode> list2 = this.invisibleParameterAnnotations[n4];
            if (list2 != null) {
                for (int n5 = 0; n5 < list2.size(); ++n5) {
                    final AnnotationNode annotationNode4 = list2.get(n5);
                    annotationNode4.accept(methodVisitor.visitParameterAnnotation(n4, annotationNode4.desc, false));
                }
            }
        }
        if (this.visited) {
            this.instructions.resetLabels();
        }
        int size6;
        if (this.attrs == null) {
            size6 = 0;
        }
        else {
            size6 = this.attrs.size();
        }
        for (int n6 = 0; n6 < size6; ++n6) {
            methodVisitor.visitAttribute(this.attrs.get(n6));
        }
        if (this.instructions.size() > 0) {
            methodVisitor.visitCode();
            int size7;
            if (this.tryCatchBlocks == null) {
                size7 = 0;
            }
            else {
                size7 = this.tryCatchBlocks.size();
            }
            for (int n7 = 0; n7 < size7; ++n7) {
                this.tryCatchBlocks.get(n7).updateIndex(n7);
                this.tryCatchBlocks.get(n7).accept(methodVisitor);
            }
            this.instructions.accept(methodVisitor);
            int size8;
            if (this.localVariables == null) {
                size8 = 0;
            }
            else {
                size8 = this.localVariables.size();
            }
            for (int n8 = 0; n8 < size8; ++n8) {
                this.localVariables.get(n8).accept(methodVisitor);
            }
            int size9;
            if (this.visibleLocalVariableAnnotations == null) {
                size9 = 0;
            }
            else {
                size9 = this.visibleLocalVariableAnnotations.size();
            }
            for (int n9 = 0; n9 < size9; ++n9) {
                this.visibleLocalVariableAnnotations.get(n9).accept(methodVisitor, true);
            }
            int size10;
            if (this.invisibleLocalVariableAnnotations == null) {
                size10 = 0;
            }
            else {
                size10 = this.invisibleLocalVariableAnnotations.size();
            }
            for (int n10 = 0; n10 < size10; ++n10) {
                this.invisibleLocalVariableAnnotations.get(n10).accept(methodVisitor, false);
            }
            methodVisitor.visitMaxs(this.maxStack, this.maxLocals);
            this.visited = true;
        }
        methodVisitor.visitEnd();
    }
    
    public void check(int i) {
        if (i == 262144) {
            if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.tryCatchBlocks == null) {
                i = 0;
            }
            else {
                i = this.tryCatchBlocks.size();
            }
            for (int j = 0; j < i; ++j) {
                final TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get(j);
                if (tryCatchBlockNode.visibleTypeAnnotations != null && tryCatchBlockNode.visibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (tryCatchBlockNode.invisibleTypeAnnotations != null && tryCatchBlockNode.invisibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
            }
            AbstractInsnNode value;
            for (i = 0; i < this.instructions.size(); ++i) {
                value = this.instructions.get(i);
                if (value.visibleTypeAnnotations != null && value.visibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (value.invisibleTypeAnnotations != null && value.invisibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (value instanceof MethodInsnNode && ((MethodInsnNode)value).itf != (value.opcode == 185)) {
                    throw new RuntimeException();
                }
            }
            if (this.visibleLocalVariableAnnotations != null && this.visibleLocalVariableAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.invisibleLocalVariableAnnotations != null) {
                if (this.invisibleLocalVariableAnnotations.size() <= 0) {
                    return;
                }
                throw new RuntimeException();
            }
        }
    }
    
    protected LabelNode getLabelNode(final Label label) {
        if (!(label.info instanceof LabelNode)) {
            label.info = new LabelNode();
        }
        return (LabelNode)label.info;
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        final AnnotationNode annotationNode = new AnnotationNode(s);
        if (b) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            this.visibleAnnotations.add(annotationNode);
            return annotationNode;
        }
        if (this.invisibleAnnotations == null) {
            this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
        }
        this.invisibleAnnotations.add(annotationNode);
        return annotationNode;
    }
    
    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNode(new ArrayList<Object>(0) {
            @Override
            public boolean add(final Object annotationDefault) {
                MethodNode.this.annotationDefault = annotationDefault;
                return super.add(annotationDefault);
            }
        });
    }
    
    @Override
    public void visitAttribute(final Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList<Attribute>(1);
        }
        this.attrs.add(attribute);
    }
    
    @Override
    public void visitCode() {
    }
    
    @Override
    public void visitEnd() {
    }
    
    @Override
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        this.instructions.add(new FieldInsnNode(n, s, s2, s3));
    }
    
    @Override
    public void visitFrame(final int n, final int n2, Object[] labelNodes, final int n3, Object[] labelNodes2) {
        final InsnList instructions = this.instructions;
        final Object[] array = null;
        if (labelNodes == null) {
            labelNodes = null;
        }
        else {
            labelNodes = this.getLabelNodes(labelNodes);
        }
        if (labelNodes2 == null) {
            labelNodes2 = array;
        }
        else {
            labelNodes2 = this.getLabelNodes(labelNodes2);
        }
        instructions.add(new FrameNode(n, n2, labelNodes, n3, labelNodes2));
    }
    
    @Override
    public void visitIincInsn(final int n, final int n2) {
        this.instructions.add(new IincInsnNode(n, n2));
    }
    
    @Override
    public void visitInsn(final int n) {
        this.instructions.add(new InsnNode(n));
    }
    
    @Override
    public AnnotationVisitor visitInsnAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        AbstractInsnNode abstractInsnNode;
        for (abstractInsnNode = this.instructions.getLast(); abstractInsnNode.getOpcode() == -1; abstractInsnNode = abstractInsnNode.getPrevious()) {}
        final TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(n, typePath, s);
        if (b) {
            if (abstractInsnNode.visibleTypeAnnotations == null) {
                abstractInsnNode.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            abstractInsnNode.visibleTypeAnnotations.add(typeAnnotationNode);
            return typeAnnotationNode;
        }
        if (abstractInsnNode.invisibleTypeAnnotations == null) {
            abstractInsnNode.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
        }
        abstractInsnNode.invisibleTypeAnnotations.add(typeAnnotationNode);
        return typeAnnotationNode;
    }
    
    @Override
    public void visitIntInsn(final int n, final int n2) {
        this.instructions.add(new IntInsnNode(n, n2));
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        this.instructions.add(new InvokeDynamicInsnNode(s, s2, handle, array));
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label label) {
        this.instructions.add(new JumpInsnNode(n, this.getLabelNode(label)));
    }
    
    @Override
    public void visitLabel(final Label label) {
        this.instructions.add(this.getLabelNode(label));
    }
    
    @Override
    public void visitLdcInsn(final Object o) {
        this.instructions.add(new LdcInsnNode(o));
    }
    
    @Override
    public void visitLineNumber(final int n, final Label label) {
        this.instructions.add(new LineNumberNode(n, this.getLabelNode(label)));
    }
    
    @Override
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        this.localVariables.add(new LocalVariableNode(s, s2, s3, this.getLabelNode(label), this.getLabelNode(label2), n));
    }
    
    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(final int n, final TypePath typePath, final Label[] array, final Label[] array2, final int[] array3, final String s, final boolean b) {
        final LocalVariableAnnotationNode localVariableAnnotationNode = new LocalVariableAnnotationNode(n, typePath, this.getLabelNodes(array), this.getLabelNodes(array2), array3, s);
        if (b) {
            if (this.visibleLocalVariableAnnotations == null) {
                this.visibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
            }
            this.visibleLocalVariableAnnotations.add(localVariableAnnotationNode);
            return localVariableAnnotationNode;
        }
        if (this.invisibleLocalVariableAnnotations == null) {
            this.invisibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
        }
        this.invisibleLocalVariableAnnotations.add(localVariableAnnotationNode);
        return localVariableAnnotationNode;
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        this.instructions.add(new LookupSwitchInsnNode(this.getLabelNode(label), array, this.getLabelNodes(array2)));
    }
    
    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }
    
    @Deprecated
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(n, s, s2, s3);
            return;
        }
        this.instructions.add(new MethodInsnNode(n, s, s2, s3));
    }
    
    @Override
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        if (this.api < 327680) {
            super.visitMethodInsn(n, s, s2, s3, b);
            return;
        }
        this.instructions.add(new MethodInsnNode(n, s, s2, s3, b));
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String s, final int n) {
        this.instructions.add(new MultiANewArrayInsnNode(s, n));
    }
    
    @Override
    public void visitParameter(final String s, final int n) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<ParameterNode>(5);
        }
        this.parameters.add(new ParameterNode(s, n));
    }
    
    @Override
    public AnnotationVisitor visitParameterAnnotation(final int n, final String s, final boolean b) {
        final AnnotationNode annotationNode = new AnnotationNode(s);
        if (b) {
            if (this.visibleParameterAnnotations == null) {
                this.visibleParameterAnnotations = (List<AnnotationNode>[])new List[Type.getArgumentTypes(this.desc).length];
            }
            if (this.visibleParameterAnnotations[n] == null) {
                this.visibleParameterAnnotations[n] = new ArrayList<AnnotationNode>(1);
            }
            this.visibleParameterAnnotations[n].add(annotationNode);
            return annotationNode;
        }
        if (this.invisibleParameterAnnotations == null) {
            this.invisibleParameterAnnotations = (List<AnnotationNode>[])new List[Type.getArgumentTypes(this.desc).length];
        }
        if (this.invisibleParameterAnnotations[n] == null) {
            this.invisibleParameterAnnotations[n] = new ArrayList<AnnotationNode>(1);
        }
        this.invisibleParameterAnnotations[n].add(annotationNode);
        return annotationNode;
    }
    
    @Override
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        this.instructions.add(new TableSwitchInsnNode(n, n2, this.getLabelNode(label), this.getLabelNodes(array)));
    }
    
    @Override
    public AnnotationVisitor visitTryCatchAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get((0xFFFF00 & n) >> 8);
        final TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(n, typePath, s);
        if (b) {
            if (tryCatchBlockNode.visibleTypeAnnotations == null) {
                tryCatchBlockNode.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            tryCatchBlockNode.visibleTypeAnnotations.add(typeAnnotationNode);
            return typeAnnotationNode;
        }
        if (tryCatchBlockNode.invisibleTypeAnnotations == null) {
            tryCatchBlockNode.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
        }
        tryCatchBlockNode.invisibleTypeAnnotations.add(typeAnnotationNode);
        return typeAnnotationNode;
    }
    
    @Override
    public void visitTryCatchBlock(final Label label, final Label label2, final Label label3, final String s) {
        this.tryCatchBlocks.add(new TryCatchBlockNode(this.getLabelNode(label), this.getLabelNode(label2), this.getLabelNode(label3), s));
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(n, typePath, s);
        if (b) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            this.visibleTypeAnnotations.add(typeAnnotationNode);
            return typeAnnotationNode;
        }
        if (this.invisibleTypeAnnotations == null) {
            this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
        }
        this.invisibleTypeAnnotations.add(typeAnnotationNode);
        return typeAnnotationNode;
    }
    
    @Override
    public void visitTypeInsn(final int n, final String s) {
        this.instructions.add(new TypeInsnNode(n, s));
    }
    
    @Override
    public void visitVarInsn(final int n, final int n2) {
        this.instructions.add(new VarInsnNode(n, n2));
    }
}
