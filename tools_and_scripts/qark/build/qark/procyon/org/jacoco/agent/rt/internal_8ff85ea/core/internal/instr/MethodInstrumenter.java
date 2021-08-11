// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.IFrame;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.LabelInfo;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.MethodProbesVisitor;

class MethodInstrumenter extends MethodProbesVisitor
{
    private final IProbeInserter probeInserter;
    
    public MethodInstrumenter(final MethodVisitor methodVisitor, final IProbeInserter probeInserter) {
        super(methodVisitor);
        this.probeInserter = probeInserter;
    }
    
    private Label createIntermediate(final Label done) {
        if (LabelInfo.getProbeId(done) == -1) {
            return done;
        }
        if (LabelInfo.isDone(done)) {
            return LabelInfo.getIntermediateLabel(done);
        }
        final Label label = new Label();
        LabelInfo.setIntermediateLabel(done, label);
        LabelInfo.setDone(done);
        return label;
    }
    
    private Label[] createIntermediates(final Label[] array) {
        final Label[] array2 = new Label[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.createIntermediate(array[i]);
        }
        return array2;
    }
    
    private int getInverted(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException();
                    }
                    case 199: {
                        return 198;
                    }
                    case 198: {
                        return 199;
                    }
                }
                break;
            }
            case 166: {
                return 165;
            }
            case 165: {
                return 166;
            }
            case 164: {
                return 163;
            }
            case 163: {
                return 164;
            }
            case 162: {
                return 161;
            }
            case 161: {
                return 162;
            }
            case 160: {
                return 159;
            }
            case 159: {
                return 160;
            }
            case 158: {
                return 157;
            }
            case 157: {
                return 158;
            }
            case 156: {
                return 155;
            }
            case 155: {
                return 156;
            }
            case 154: {
                return 153;
            }
            case 153: {
                return 154;
            }
        }
    }
    
    private void insertIntermediateProbe(final Label done, final IFrame frame) {
        final int probeId = LabelInfo.getProbeId(done);
        if (probeId != -1 && !LabelInfo.isDone(done)) {
            this.mv.visitLabel(LabelInfo.getIntermediateLabel(done));
            frame.accept(this.mv);
            this.probeInserter.insertProbe(probeId);
            this.mv.visitJumpInsn(167, done);
            LabelInfo.setDone(done);
        }
    }
    
    private void insertIntermediateProbes(final Label label, final Label[] array, final IFrame frame) {
        LabelInfo.resetDone(label);
        LabelInfo.resetDone(array);
        this.insertIntermediateProbe(label, frame);
        for (int length = array.length, i = 0; i < length; ++i) {
            this.insertIntermediateProbe(array[i], frame);
        }
    }
    
    @Override
    public void visitInsnWithProbe(final int n, final int n2) {
        this.probeInserter.insertProbe(n2);
        this.mv.visitInsn(n);
    }
    
    @Override
    public void visitJumpInsnWithProbe(final int n, final Label label, final int n2, final IFrame frame) {
        if (n == 167) {
            this.probeInserter.insertProbe(n2);
            this.mv.visitJumpInsn(167, label);
            return;
        }
        final Label label2 = new Label();
        this.mv.visitJumpInsn(this.getInverted(n), label2);
        this.probeInserter.insertProbe(n2);
        this.mv.visitJumpInsn(167, label);
        this.mv.visitLabel(label2);
        frame.accept(this.mv);
    }
    
    @Override
    public void visitLookupSwitchInsnWithProbes(final Label label, final int[] array, final Label[] array2, final IFrame frame) {
        LabelInfo.resetDone(label);
        LabelInfo.resetDone(array2);
        this.mv.visitLookupSwitchInsn(this.createIntermediate(label), array, this.createIntermediates(array2));
        this.insertIntermediateProbes(label, array2, frame);
    }
    
    @Override
    public void visitProbe(final int n) {
        this.probeInserter.insertProbe(n);
    }
    
    @Override
    public void visitTableSwitchInsnWithProbes(final int n, final int n2, final Label label, final Label[] array, final IFrame frame) {
        LabelInfo.resetDone(label);
        LabelInfo.resetDone(array);
        this.mv.visitTableSwitchInsn(n, n2, this.createIntermediate(label), this.createIntermediates(array));
        this.insertIntermediateProbes(label, array, frame);
    }
}
