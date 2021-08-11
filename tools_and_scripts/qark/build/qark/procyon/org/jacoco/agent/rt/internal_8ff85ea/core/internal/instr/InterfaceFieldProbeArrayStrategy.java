// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;

class InterfaceFieldProbeArrayStrategy implements IProbeArrayStrategy
{
    private static final Object[] FRAME_LOCALS_EMPTY;
    private static final Object[] FRAME_STACK_ARRZ;
    private final IExecutionDataAccessorGenerator accessorGenerator;
    private final long classId;
    private final String className;
    private final int probeCount;
    private boolean seenClinit;
    
    static {
        FRAME_STACK_ARRZ = new Object[] { "[Z" };
        FRAME_LOCALS_EMPTY = new Object[0];
    }
    
    InterfaceFieldProbeArrayStrategy(final String className, final long classId, final int probeCount, final IExecutionDataAccessorGenerator accessorGenerator) {
        this.seenClinit = false;
        this.className = className;
        this.classId = classId;
        this.probeCount = probeCount;
        this.accessorGenerator = accessorGenerator;
    }
    
    private void createClinitMethod(final ClassVisitor classVisitor, int generateDataAccessor) {
        final MethodVisitor visitMethod = classVisitor.visitMethod(4104, "<clinit>", "()V", null, null);
        visitMethod.visitCode();
        generateDataAccessor = this.accessorGenerator.generateDataAccessor(this.classId, this.className, generateDataAccessor, visitMethod);
        visitMethod.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
        visitMethod.visitInsn(177);
        visitMethod.visitMaxs(generateDataAccessor, 0);
        visitMethod.visitEnd();
    }
    
    private void createDataField(final ClassVisitor classVisitor) {
        classVisitor.visitField(4121, "$jacocoData", "[Z", null, null);
    }
    
    private void createInitMethod(final ClassVisitor classVisitor, int generateDataAccessor) {
        final MethodVisitor visitMethod = classVisitor.visitMethod(4106, "$jacocoInit", "()[Z", null, null);
        visitMethod.visitCode();
        visitMethod.visitFieldInsn(178, this.className, "$jacocoData", "[Z");
        visitMethod.visitInsn(89);
        final Label label = new Label();
        visitMethod.visitJumpInsn(199, label);
        visitMethod.visitInsn(87);
        generateDataAccessor = this.accessorGenerator.generateDataAccessor(this.classId, this.className, generateDataAccessor, visitMethod);
        visitMethod.visitFrame(-1, 0, InterfaceFieldProbeArrayStrategy.FRAME_LOCALS_EMPTY, 1, InterfaceFieldProbeArrayStrategy.FRAME_STACK_ARRZ);
        visitMethod.visitLabel(label);
        visitMethod.visitInsn(176);
        visitMethod.visitMaxs(Math.max(generateDataAccessor, 2), 0);
        visitMethod.visitEnd();
    }
    
    @Override
    public void addMembers(final ClassVisitor classVisitor, final int n) {
        this.createDataField(classVisitor);
        this.createInitMethod(classVisitor, n);
        if (!this.seenClinit) {
            this.createClinitMethod(classVisitor, n);
        }
    }
    
    @Override
    public int storeInstance(final MethodVisitor methodVisitor, final boolean b, final int n) {
        if (b) {
            final int generateDataAccessor = this.accessorGenerator.generateDataAccessor(this.classId, this.className, this.probeCount, methodVisitor);
            methodVisitor.visitInsn(89);
            methodVisitor.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
            methodVisitor.visitVarInsn(58, n);
            this.seenClinit = true;
            return Math.max(generateDataAccessor, 2);
        }
        methodVisitor.visitMethodInsn(184, this.className, "$jacocoInit", "()[Z", true);
        methodVisitor.visitVarInsn(58, n);
        return 1;
    }
}
