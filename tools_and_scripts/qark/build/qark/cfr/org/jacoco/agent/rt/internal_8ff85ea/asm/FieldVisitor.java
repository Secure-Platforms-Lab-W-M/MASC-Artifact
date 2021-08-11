/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Attribute;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;

public abstract class FieldVisitor {
    protected final int api;
    protected FieldVisitor fv;

    public FieldVisitor(int n) {
        this(n, null);
    }

    public FieldVisitor(int n, FieldVisitor fieldVisitor) {
        if (n != 262144 && n != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = n;
        this.fv = fieldVisitor;
    }

    public AnnotationVisitor visitAnnotation(String string2, boolean bl) {
        if (this.fv != null) {
            return this.fv.visitAnnotation(string2, bl);
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.fv != null) {
            this.fv.visitAttribute(attribute);
        }
    }

    public void visitEnd() {
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }

    public AnnotationVisitor visitTypeAnnotation(int n, TypePath typePath, String string2, boolean bl) {
        if (this.api >= 327680) {
            if (this.fv != null) {
                return this.fv.visitTypeAnnotation(n, typePath, string2, bl);
            }
            return null;
        }
        throw new RuntimeException();
    }
}

