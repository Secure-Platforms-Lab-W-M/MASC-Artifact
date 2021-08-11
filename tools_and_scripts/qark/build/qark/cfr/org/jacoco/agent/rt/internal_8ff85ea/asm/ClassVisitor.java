/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Attribute;
import org.jacoco.agent.rt.internal_8ff85ea.asm.FieldVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;

public abstract class ClassVisitor {
    protected final int api;
    protected ClassVisitor cv;

    public ClassVisitor(int n) {
        this(n, null);
    }

    public ClassVisitor(int n, ClassVisitor classVisitor) {
        if (n != 262144 && n != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = n;
        this.cv = classVisitor;
    }

    public void visit(int n, int n2, String string2, String string3, String string4, String[] arrstring) {
        if (this.cv != null) {
            this.cv.visit(n, n2, string2, string3, string4, arrstring);
        }
    }

    public AnnotationVisitor visitAnnotation(String string2, boolean bl) {
        if (this.cv != null) {
            return this.cv.visitAnnotation(string2, bl);
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute(attribute);
        }
    }

    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }

    public FieldVisitor visitField(int n, String string2, String string3, String string4, Object object) {
        if (this.cv != null) {
            return this.cv.visitField(n, string2, string3, string4, object);
        }
        return null;
    }

    public void visitInnerClass(String string2, String string3, String string4, int n) {
        if (this.cv != null) {
            this.cv.visitInnerClass(string2, string3, string4, n);
        }
    }

    public MethodVisitor visitMethod(int n, String string2, String string3, String string4, String[] arrstring) {
        if (this.cv != null) {
            return this.cv.visitMethod(n, string2, string3, string4, arrstring);
        }
        return null;
    }

    public void visitOuterClass(String string2, String string3, String string4) {
        if (this.cv != null) {
            this.cv.visitOuterClass(string2, string3, string4);
        }
    }

    public void visitSource(String string2, String string3) {
        if (this.cv != null) {
            this.cv.visitSource(string2, string3);
        }
    }

    public AnnotationVisitor visitTypeAnnotation(int n, TypePath typePath, String string2, boolean bl) {
        if (this.api >= 327680) {
            if (this.cv != null) {
                return this.cv.visitTypeAnnotation(n, typePath, string2, bl);
            }
            return null;
        }
        throw new RuntimeException();
    }
}

