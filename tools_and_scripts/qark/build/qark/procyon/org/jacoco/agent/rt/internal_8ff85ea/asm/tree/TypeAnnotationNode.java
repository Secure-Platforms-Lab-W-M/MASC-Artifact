// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;

public class TypeAnnotationNode extends AnnotationNode
{
    public TypePath typePath;
    public int typeRef;
    
    public TypeAnnotationNode(final int n, final int typeRef, final TypePath typePath, final String s) {
        super(n, s);
        this.typeRef = typeRef;
        this.typePath = typePath;
    }
    
    public TypeAnnotationNode(final int n, final TypePath typePath, final String s) {
        this(327680, n, typePath, s);
        if (this.getClass() == TypeAnnotationNode.class) {
            return;
        }
        throw new IllegalStateException();
    }
}
