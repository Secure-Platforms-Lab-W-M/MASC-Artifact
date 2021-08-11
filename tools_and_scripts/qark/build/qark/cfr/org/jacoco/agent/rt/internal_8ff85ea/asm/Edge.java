/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;

class Edge {
    static final int EXCEPTION = Integer.MAX_VALUE;
    static final int NORMAL = 0;
    int info;
    Edge next;
    Label successor;

    Edge() {
    }
}

