/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

public abstract class AnnotationVisitor {
    protected final int api;
    protected AnnotationVisitor av;

    public AnnotationVisitor(int n) {
        this(n, null);
    }

    public AnnotationVisitor(int n, AnnotationVisitor annotationVisitor) {
        if (n != 262144 && n != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = n;
        this.av = annotationVisitor;
    }

    public void visit(String string2, Object object) {
        if (this.av != null) {
            this.av.visit(string2, object);
        }
    }

    public AnnotationVisitor visitAnnotation(String string2, String string3) {
        if (this.av != null) {
            return this.av.visitAnnotation(string2, string3);
        }
        return null;
    }

    public AnnotationVisitor visitArray(String string2) {
        if (this.av != null) {
            return this.av.visitArray(string2);
        }
        return null;
    }

    public void visitEnd() {
        if (this.av != null) {
            this.av.visitEnd();
        }
    }

    public void visitEnum(String string2, String string3, String string4) {
        if (this.av != null) {
            this.av.visitEnum(string2, string3, string4);
        }
    }
}

