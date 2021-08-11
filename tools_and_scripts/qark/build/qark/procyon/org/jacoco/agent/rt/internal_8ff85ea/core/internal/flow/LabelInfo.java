// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;

public final class LabelInfo
{
    public static final int NO_PROBE = -1;
    private boolean done;
    private Instruction instruction;
    private Label intermediate;
    private boolean methodInvocationLine;
    private boolean multiTarget;
    private int probeid;
    private boolean successor;
    private boolean target;
    
    private LabelInfo() {
        this.target = false;
        this.multiTarget = false;
        this.successor = false;
        this.methodInvocationLine = false;
        this.done = false;
        this.probeid = -1;
        this.intermediate = null;
        this.instruction = null;
    }
    
    private static LabelInfo create(final Label label) {
        LabelInfo value;
        if ((value = get(label)) == null) {
            value = new LabelInfo();
            label.info = value;
        }
        return value;
    }
    
    private static LabelInfo get(final Label label) {
        final Object info = label.info;
        if (info instanceof LabelInfo) {
            return (LabelInfo)info;
        }
        return null;
    }
    
    public static Instruction getInstruction(final Label label) {
        final LabelInfo value = get(label);
        if (value == null) {
            return null;
        }
        return value.instruction;
    }
    
    public static Label getIntermediateLabel(final Label label) {
        final LabelInfo value = get(label);
        if (value == null) {
            return null;
        }
        return value.intermediate;
    }
    
    public static int getProbeId(final Label label) {
        final LabelInfo value = get(label);
        if (value == null) {
            return -1;
        }
        return value.probeid;
    }
    
    public static boolean isDone(final Label label) {
        final LabelInfo value = get(label);
        return value != null && value.done;
    }
    
    public static boolean isMethodInvocationLine(final Label label) {
        final LabelInfo value = get(label);
        return value != null && value.methodInvocationLine;
    }
    
    public static boolean isMultiTarget(final Label label) {
        final LabelInfo value = get(label);
        return value != null && value.multiTarget;
    }
    
    public static boolean isSuccessor(final Label label) {
        final LabelInfo value = get(label);
        return value != null && value.successor;
    }
    
    public static boolean needsProbe(final Label label) {
        final LabelInfo value = get(label);
        return value != null && value.successor && (value.multiTarget || value.methodInvocationLine);
    }
    
    public static void resetDone(final Label label) {
        final LabelInfo value = get(label);
        if (value != null) {
            value.done = false;
        }
    }
    
    public static void resetDone(final Label[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            resetDone(array[i]);
        }
    }
    
    public static void setDone(final Label label) {
        create(label).done = true;
    }
    
    public static void setInstruction(final Label label, final Instruction instruction) {
        create(label).instruction = instruction;
    }
    
    public static void setIntermediateLabel(final Label label, final Label intermediate) {
        create(label).intermediate = intermediate;
    }
    
    public static void setMethodInvocationLine(final Label label) {
        create(label).methodInvocationLine = true;
    }
    
    public static void setProbeId(final Label label, final int probeid) {
        create(label).probeid = probeid;
    }
    
    public static void setSuccessor(final Label label) {
        final LabelInfo create = create(label);
        create.successor = true;
        if (create.target) {
            create.multiTarget = true;
        }
    }
    
    public static void setTarget(final Label label) {
        final LabelInfo create = create(label);
        if (!create.target && !create.successor) {
            create.target = true;
            return;
        }
        create.multiTarget = true;
    }
}
