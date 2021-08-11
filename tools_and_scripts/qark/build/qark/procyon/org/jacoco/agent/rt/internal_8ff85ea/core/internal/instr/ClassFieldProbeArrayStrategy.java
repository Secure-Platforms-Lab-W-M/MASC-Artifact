// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;

class ClassFieldProbeArrayStrategy implements IProbeArrayStrategy
{
    private static final Object[] FRAME_LOCALS_EMPTY;
    private static final Object[] FRAME_STACK_ARRZ;
    private final IExecutionDataAccessorGenerator accessorGenerator;
    private final long classId;
    private final String className;
    private final boolean withFrames;
    
    static {
        FRAME_STACK_ARRZ = new Object[] { "[Z" };
        FRAME_LOCALS_EMPTY = new Object[0];
    }
    
    ClassFieldProbeArrayStrategy(final String className, final long classId, final boolean withFrames, final IExecutionDataAccessorGenerator accessorGenerator) {
        this.className = className;
        this.classId = classId;
        this.withFrames = withFrames;
        this.accessorGenerator = accessorGenerator;
    }
    
    private void createDataField(final ClassVisitor classVisitor) {
        classVisitor.visitField(4234, "$jacocoData", "[Z", null, null);
    }
    
    private void createInitMethod(final ClassVisitor classVisitor, int genInitializeDataField) {
        final MethodVisitor visitMethod = classVisitor.visitMethod(4106, "$jacocoInit", "()[Z", null, null);
        visitMethod.visitCode();
        visitMethod.visitFieldInsn(178, this.className, "$jacocoData", "[Z");
        visitMethod.visitInsn(89);
        final Label label = new Label();
        visitMethod.visitJumpInsn(199, label);
        visitMethod.visitInsn(87);
        genInitializeDataField = this.genInitializeDataField(visitMethod, genInitializeDataField);
        if (this.withFrames) {
            visitMethod.visitFrame(-1, 0, ClassFieldProbeArrayStrategy.FRAME_LOCALS_EMPTY, 1, ClassFieldProbeArrayStrategy.FRAME_STACK_ARRZ);
        }
        visitMethod.visitLabel(label);
        visitMethod.visitInsn(176);
        visitMethod.visitMaxs(Math.max(genInitializeDataField, 2), 0);
        visitMethod.visitEnd();
    }
    
    private int genInitializeDataField(final MethodVisitor methodVisitor, int generateDataAccessor) {
        generateDataAccessor = this.accessorGenerator.generateDataAccessor(this.classId, this.className, generateDataAccessor, methodVisitor);
        methodVisitor.visitInsn(89);
        methodVisitor.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
        return Math.max(generateDataAccessor, 2);
    }
    
    @Override
    public void addMembers(final ClassVisitor classVisitor, final int n) {
        this.createDataField(classVisitor);
        this.createInitMethod(classVisitor, n);
    }
    
    @Override
    public int storeInstance(final MethodVisitor methodVisitor, final boolean b, final int n) {
        methodVisitor.visitMethodInsn(184, this.className, "$jacocoInit", "()[Z", false);
        methodVisitor.visitVarInsn(58, n);
        return 1;
    }
}
