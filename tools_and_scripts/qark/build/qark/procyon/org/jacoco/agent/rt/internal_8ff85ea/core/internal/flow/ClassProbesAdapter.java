// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.commons.AnalyzerAdapter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.MethodNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;

public class ClassProbesAdapter extends ClassVisitor implements IProbeIdGenerator
{
    private static final MethodProbesVisitor EMPTY_METHOD_PROBES_VISITOR;
    private int counter;
    private final ClassProbesVisitor cv;
    private String name;
    private final boolean trackFrames;
    
    static {
        EMPTY_METHOD_PROBES_VISITOR = new MethodProbesVisitor() {};
    }
    
    public ClassProbesAdapter(final ClassProbesVisitor cv, final boolean trackFrames) {
        super(327680, cv);
        this.counter = 0;
        this.cv = cv;
        this.trackFrames = trackFrames;
    }
    
    @Override
    public int nextId() {
        return this.counter++;
    }
    
    @Override
    public void visit(final int n, final int n2, final String name, final String s, final String s2, final String[] array) {
        super.visit(n, n2, this.name = name, s, s2, array);
    }
    
    @Override
    public void visitEnd() {
        this.cv.visitTotalProbeCount(this.counter);
        super.visitEnd();
    }
    
    @Override
    public final MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        MethodProbesVisitor methodProbesVisitor = this.cv.visitMethod(n, s, s2, s3, array);
        if (methodProbesVisitor == null) {
            methodProbesVisitor = ClassProbesAdapter.EMPTY_METHOD_PROBES_VISITOR;
        }
        return new MethodSanitizer(null, n, s, s2, s3, array) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                LabelFlowAnalyzer.markLabels(this);
                final MethodProbesAdapter methodProbesAdapter = new MethodProbesAdapter(methodProbesVisitor, ClassProbesAdapter.this);
                if (ClassProbesAdapter.this.trackFrames) {
                    final AnalyzerAdapter analyzer = new AnalyzerAdapter(ClassProbesAdapter.this.name, this.access, this.name, this.desc, methodProbesAdapter);
                    methodProbesAdapter.setAnalyzer(analyzer);
                    this.accept(analyzer);
                    return;
                }
                this.accept(methodProbesAdapter);
            }
        };
    }
}
