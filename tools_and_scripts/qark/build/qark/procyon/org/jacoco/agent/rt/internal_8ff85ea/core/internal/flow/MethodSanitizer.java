// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.commons.JSRInlinerAdapter;

class MethodSanitizer extends JSRInlinerAdapter
{
    MethodSanitizer(final MethodVisitor methodVisitor, final int n, final String s, final String s2, final String s3, final String[] array) {
        super(327680, methodVisitor, n, s, s2, s3, array);
    }
    
    @Override
    public void visitLineNumber(final int n, final Label label) {
        if (label.info != null) {
            super.visitLineNumber(n, label);
        }
    }
    
    @Override
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        if (label.info != null && label2.info != null) {
            super.visitLocalVariable(s, s2, s3, label, label2, n);
        }
    }
}
