// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import java.util.HashMap;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import java.util.Map;
import org.jacoco.agent.rt.internal_8ff85ea.asm.commons.AnalyzerAdapter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;

public final class MethodProbesAdapter extends MethodVisitor
{
    private AnalyzerAdapter analyzer;
    private final IProbeIdGenerator idGenerator;
    private final MethodProbesVisitor probesVisitor;
    private final Map<Label, Label> tryCatchProbeLabels;
    
    public MethodProbesAdapter(final MethodProbesVisitor probesVisitor, final IProbeIdGenerator idGenerator) {
        super(327680, probesVisitor);
        this.probesVisitor = probesVisitor;
        this.idGenerator = idGenerator;
        this.tryCatchProbeLabels = new HashMap<Label, Label>();
    }
    
    private IFrame frame(final int n) {
        return FrameSnapshot.create(this.analyzer, n);
    }
    
    private int jumpPopCount(final int n) {
        if (n == 167) {
            return 0;
        }
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        return 2;
                    }
                    case 198:
                    case 199: {
                        return 1;
                    }
                }
                break;
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158: {
                return 1;
            }
        }
    }
    
    private boolean markLabels(Label label, final Label[] array) {
        boolean b = false;
        LabelInfo.resetDone(array);
        if (LabelInfo.isMultiTarget(label)) {
            LabelInfo.setProbeId(label, this.idGenerator.nextId());
            b = true;
        }
        LabelInfo.setDone(label);
        boolean b2;
        for (int length = array.length, i = 0; i < length; ++i, b = b2) {
            label = array[i];
            b2 = b;
            if (LabelInfo.isMultiTarget(label)) {
                b2 = b;
                if (!LabelInfo.isDone(label)) {
                    LabelInfo.setProbeId(label, this.idGenerator.nextId());
                    b2 = true;
                }
            }
            LabelInfo.setDone(label);
        }
        return b;
    }
    
    public void setAnalyzer(final AnalyzerAdapter analyzer) {
        this.analyzer = analyzer;
    }
    
    @Override
    public void visitInsn(final int n) {
        if (n != 191) {
            switch (n) {
                default: {
                    this.probesVisitor.visitInsn(n);
                    return;
                }
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177: {
                    break;
                }
            }
        }
        this.probesVisitor.visitInsnWithProbe(n, this.idGenerator.nextId());
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label label) {
        if (LabelInfo.isMultiTarget(label)) {
            this.probesVisitor.visitJumpInsnWithProbe(n, label, this.idGenerator.nextId(), this.frame(this.jumpPopCount(n)));
            return;
        }
        this.probesVisitor.visitJumpInsn(n, label);
    }
    
    @Override
    public void visitLabel(final Label label) {
        if (LabelInfo.needsProbe(label)) {
            if (this.tryCatchProbeLabels.containsKey(label)) {
                this.probesVisitor.visitLabel(this.tryCatchProbeLabels.get(label));
            }
            this.probesVisitor.visitProbe(this.idGenerator.nextId());
        }
        this.probesVisitor.visitLabel(label);
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        if (this.markLabels(label, array2)) {
            this.probesVisitor.visitLookupSwitchInsnWithProbes(label, array, array2, this.frame(1));
            return;
        }
        this.probesVisitor.visitLookupSwitchInsn(label, array, array2);
    }
    
    @Override
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        if (this.markLabels(label, array)) {
            this.probesVisitor.visitTableSwitchInsnWithProbes(n, n2, label, array, this.frame(1));
            return;
        }
        this.probesVisitor.visitTableSwitchInsn(n, n2, label, array);
    }
    
    @Override
    public void visitTryCatchBlock(final Label label, final Label label2, final Label label3, final String s) {
        Label successor;
        if (this.tryCatchProbeLabels.containsKey(label)) {
            successor = this.tryCatchProbeLabels.get(label);
        }
        else {
            successor = label;
            if (LabelInfo.needsProbe(label)) {
                successor = new Label();
                LabelInfo.setSuccessor(successor);
                this.tryCatchProbeLabels.put(label, successor);
            }
        }
        this.probesVisitor.visitTryCatchBlock(successor, label2, label3, s);
    }
}
