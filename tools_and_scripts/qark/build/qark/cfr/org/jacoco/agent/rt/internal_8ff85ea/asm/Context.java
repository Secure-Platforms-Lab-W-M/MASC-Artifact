/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Attribute;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.TypePath;

class Context {
    int access;
    Attribute[] attrs;
    int[] bootstrapMethods;
    char[] buffer;
    String desc;
    Label[] end;
    int flags;
    int[] index;
    Label[] labels;
    Object[] local;
    int localCount;
    int localDiff;
    int mode;
    String name;
    int offset;
    Object[] stack;
    int stackCount;
    Label[] start;
    TypePath typePath;
    int typeRef;

    Context() {
    }
}

