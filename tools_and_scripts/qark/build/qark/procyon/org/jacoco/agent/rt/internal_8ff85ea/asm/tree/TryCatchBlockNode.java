// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.Iterator;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import java.util.List;

public class TryCatchBlockNode
{
    public LabelNode end;
    public LabelNode handler;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public LabelNode start;
    public String type;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    
    public TryCatchBlockNode(final LabelNode start, final LabelNode end, final LabelNode handler, final String type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        final Label label = this.start.getLabel();
        final Label label2 = this.end.getLabel();
        Label label3;
        if (this.handler == null) {
            label3 = null;
        }
        else {
            label3 = this.handler.getLabel();
        }
        methodVisitor.visitTryCatchBlock(label, label2, label3, this.type);
        int size;
        if (this.visibleTypeAnnotations == null) {
            size = 0;
        }
        else {
            size = this.visibleTypeAnnotations.size();
        }
        for (int i = 0; i < size; ++i) {
            final TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i);
            typeAnnotationNode.accept(methodVisitor.visitTryCatchAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
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
            typeAnnotationNode2.accept(methodVisitor.visitTryCatchAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
    }
    
    public void updateIndex(int n) {
        n = (n << 8 | 0x42000000);
        if (this.visibleTypeAnnotations != null) {
            final Iterator<TypeAnnotationNode> iterator = this.visibleTypeAnnotations.iterator();
            while (iterator.hasNext()) {
                iterator.next().typeRef = n;
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            final Iterator<TypeAnnotationNode> iterator2 = this.invisibleTypeAnnotations.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().typeRef = n;
            }
        }
    }
}
