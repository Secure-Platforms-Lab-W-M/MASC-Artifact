// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassReader;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.Java9Support;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;

public class ModifiedSystemClassRuntime extends AbstractRuntime
{
    private static final String ACCESS_FIELD_TYPE = "Ljava/lang/Object;";
    private final String accessFieldName;
    private final Class<?> systemClass;
    private final String systemClassName;
    
    public ModifiedSystemClassRuntime(final Class<?> systemClass, final String accessFieldName) {
        this.systemClass = systemClass;
        this.systemClassName = systemClass.getName().replace('.', '/');
        this.accessFieldName = accessFieldName;
    }
    
    private static void createDataField(final ClassVisitor classVisitor, final String s) {
        classVisitor.visitField(4233, s, "Ljava/lang/Object;", null, null);
    }
    
    public static IRuntime createFor(final Instrumentation instrumentation, final String s) throws ClassNotFoundException {
        return createFor(instrumentation, s, "$jacocoAccess");
    }
    
    public static IRuntime createFor(final Instrumentation instrumentation, final String s, final String s2) throws ClassNotFoundException {
        final ClassFileTransformer classFileTransformer = new ClassFileTransformer() {
            @Override
            public byte[] transform(final ClassLoader classLoader, final String s, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] array) throws IllegalClassFormatException {
                if (s.equals(s)) {
                    return ModifiedSystemClassRuntime.instrument(array, s2);
                }
                return null;
            }
        };
        instrumentation.addTransformer(classFileTransformer);
        final Class<?> forName = Class.forName(s.replace('/', '.'));
        instrumentation.removeTransformer(classFileTransformer);
        try {
            forName.getField(s2);
            return new ModifiedSystemClassRuntime(forName, s2);
        }
        catch (NoSuchFieldException ex) {
            throw new RuntimeException(String.format("Class %s could not be instrumented.", s), ex);
        }
    }
    
    public static byte[] instrument(final byte[] array, final String s) {
        final ClassReader classReader = new ClassReader(Java9Support.downgradeIfRequired(array));
        final ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(new ClassVisitor(327680, classWriter) {
            @Override
            public void visitEnd() {
                createDataField(this.cv, s);
                super.visitEnd();
            }
        }, 8);
        return classWriter.toByteArray();
    }
    
    @Override
    public int generateDataAccessor(final long n, final String s, final int n2, final MethodVisitor methodVisitor) {
        methodVisitor.visitFieldInsn(178, this.systemClassName, this.accessFieldName, "Ljava/lang/Object;");
        RuntimeData.generateAccessCall(n, s, n2, methodVisitor);
        return 6;
    }
    
    @Override
    public void shutdown() {
    }
    
    @Override
    public void startup(final RuntimeData runtimeData) throws Exception {
        super.startup(runtimeData);
        this.systemClass.getField(this.accessFieldName).set(null, runtimeData);
    }
}
