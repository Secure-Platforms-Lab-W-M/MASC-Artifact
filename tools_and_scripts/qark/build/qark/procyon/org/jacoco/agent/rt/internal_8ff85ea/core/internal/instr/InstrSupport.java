// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public final class InstrSupport
{
    public static final int ASM_API_VERSION = 327680;
    static final int CLINIT_ACC = 4104;
    static final String CLINIT_DESC = "()V";
    static final String CLINIT_NAME = "<clinit>";
    public static final int DATAFIELD_ACC = 4234;
    public static final String DATAFIELD_DESC = "[Z";
    public static final int DATAFIELD_INTF_ACC = 4121;
    public static final String DATAFIELD_NAME = "$jacocoData";
    public static final int INITMETHOD_ACC = 4106;
    public static final String INITMETHOD_DESC = "()[Z";
    public static final String INITMETHOD_NAME = "$jacocoInit";
    
    private InstrSupport() {
    }
    
    public static void assertNotInstrumented(final String s, final String s2) throws IllegalStateException {
        if (!s.equals("$jacocoData") && !s.equals("$jacocoInit")) {
            return;
        }
        throw new IllegalStateException(String.format("Class %s is already instrumented.", s2));
    }
    
    public static void push(final MethodVisitor methodVisitor, final int n) {
        if (n >= -1 && n <= 5) {
            methodVisitor.visitInsn(n + 3);
            return;
        }
        if (n >= -128 && n <= 127) {
            methodVisitor.visitIntInsn(16, n);
            return;
        }
        if (n >= -32768 && n <= 32767) {
            methodVisitor.visitIntInsn(17, n);
            return;
        }
        methodVisitor.visitLdcInsn(n);
    }
}
