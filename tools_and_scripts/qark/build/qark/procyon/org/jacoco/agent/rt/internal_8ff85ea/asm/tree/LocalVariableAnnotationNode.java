// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;
import java.util.List;

public class LocalVariableAnnotationNode extends TypeAnnotationNode
{
    public List<LabelNode> end;
    public List<Integer> index;
    public List<LabelNode> start;
    
    public LocalVariableAnnotationNode(int i, int length, final TypePath typePath, final LabelNode[] array, final LabelNode[] array2, final int[] array3, final String s) {
        super(i, length, typePath, s);
        (this.start = new ArrayList<LabelNode>(array.length)).addAll(Arrays.asList(array));
        (this.end = new ArrayList<LabelNode>(array2.length)).addAll(Arrays.asList(array2));
        this.index = new ArrayList<Integer>(array3.length);
        for (length = array3.length, i = 0; i < length; ++i) {
            this.index.add(array3[i]);
        }
    }
    
    public LocalVariableAnnotationNode(final int n, final TypePath typePath, final LabelNode[] array, final LabelNode[] array2, final int[] array3, final String s) {
        this(327680, n, typePath, array, array2, array3, s);
    }
    
    public void accept(final MethodVisitor methodVisitor, final boolean b) {
        final Label[] array = new Label[this.start.size()];
        final Label[] array2 = new Label[this.end.size()];
        final int[] array3 = new int[this.index.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.start.get(i).getLabel();
            array2[i] = this.end.get(i).getLabel();
            array3[i] = this.index.get(i);
        }
        this.accept(methodVisitor.visitLocalVariableAnnotation(this.typeRef, this.typePath, array, array2, array3, this.desc, true));
    }
}
