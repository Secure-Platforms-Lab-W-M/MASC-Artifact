// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.commons;

import java.util.Set;
import java.util.AbstractMap;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.LookupSwitchInsnNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.TableSwitchInsnNode;
import java.util.Iterator;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.InsnNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.JumpInsnNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.AbstractInsnNode;
import java.util.List;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.LocalVariableNode;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.TryCatchBlockNode;
import java.util.ArrayList;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.InsnList;
import java.util.LinkedList;
import java.util.HashMap;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.LabelNode;
import java.util.Map;
import java.util.BitSet;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import org.jacoco.agent.rt.internal_8ff85ea.asm.tree.MethodNode;

public class JSRInlinerAdapter extends MethodNode implements Opcodes
{
    private static final boolean LOGGING = false;
    final BitSet dualCitizens;
    private final BitSet mainSubroutine;
    private final Map<LabelNode, BitSet> subroutineHeads;
    
    protected JSRInlinerAdapter(final int n, final MethodVisitor mv, final int n2, final String s, final String s2, final String s3, final String[] array) {
        super(n, n2, s, s2, s3, array);
        this.subroutineHeads = new HashMap<LabelNode, BitSet>();
        this.mainSubroutine = new BitSet();
        this.dualCitizens = new BitSet();
        this.mv = mv;
    }
    
    public JSRInlinerAdapter(final MethodVisitor methodVisitor, final int n, final String s, final String s2, final String s3, final String[] array) {
        this(327680, methodVisitor, n, s, s2, s3, array);
        if (this.getClass() == JSRInlinerAdapter.class) {
            return;
        }
        throw new IllegalStateException();
    }
    
    private void emitCode() {
        final LinkedList<Instantiation> list = new LinkedList<Instantiation>();
        list.add(new Instantiation(null, this.mainSubroutine));
        final InsnList instructions = new InsnList();
        final ArrayList<TryCatchBlockNode> tryCatchBlocks = new ArrayList<TryCatchBlockNode>();
        final ArrayList<LocalVariableNode> localVariables = new ArrayList<LocalVariableNode>();
        while (!list.isEmpty()) {
            this.emitSubroutine(list.removeFirst(), list, instructions, tryCatchBlocks, localVariables);
        }
        this.instructions = instructions;
        this.tryCatchBlocks = tryCatchBlocks;
        this.localVariables = localVariables;
    }
    
    private void emitSubroutine(final Instantiation instantiation, final List<Instantiation> list, final InsnList list2, final List<TryCatchBlockNode> list3, final List<LocalVariableNode> list4) {
        LabelNode labelNode = null;
        for (int i = 0; i < this.instructions.size(); ++i) {
            final AbstractInsnNode value = this.instructions.get(i);
            final Instantiation owner = instantiation.findOwner(i);
            LabelNode labelNode2;
            if (value.getType() == 8) {
                final LabelNode rangeLabel = instantiation.rangeLabel((LabelNode)value);
                if (rangeLabel != (labelNode2 = labelNode)) {
                    list2.add(rangeLabel);
                    labelNode2 = rangeLabel;
                }
            }
            else if (owner != instantiation) {
                labelNode2 = labelNode;
            }
            else if (value.getOpcode() == 169) {
                LabelNode returnLabel = null;
                for (Instantiation previous = instantiation; previous != null; previous = previous.previous) {
                    if (previous.subroutine.get(i)) {
                        returnLabel = previous.returnLabel;
                    }
                }
                if (returnLabel == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Instruction #");
                    sb.append(i);
                    sb.append(" is a RET not owned by any subroutine");
                    throw new RuntimeException(sb.toString());
                }
                list2.add(new JumpInsnNode(167, returnLabel));
                labelNode2 = labelNode;
            }
            else {
                if (value.getOpcode() == 168) {
                    final LabelNode label = ((JumpInsnNode)value).label;
                    final Instantiation instantiation2 = new Instantiation(instantiation, this.subroutineHeads.get(label));
                    final LabelNode gotoLabel = instantiation2.gotoLabel(label);
                    list2.add(new InsnNode(1));
                    list2.add(new JumpInsnNode(167, gotoLabel));
                    list2.add(instantiation2.returnLabel);
                    list.add(instantiation2);
                    continue;
                }
                list2.add(value.clone(instantiation));
                continue;
            }
            labelNode = labelNode2;
        }
        for (final TryCatchBlockNode tryCatchBlockNode : this.tryCatchBlocks) {
            final LabelNode rangeLabel2 = instantiation.rangeLabel(tryCatchBlockNode.start);
            final LabelNode rangeLabel3 = instantiation.rangeLabel(tryCatchBlockNode.end);
            if (rangeLabel2 == rangeLabel3) {
                continue;
            }
            final LabelNode gotoLabel2 = instantiation.gotoLabel(tryCatchBlockNode.handler);
            if (rangeLabel2 == null || rangeLabel3 == null || gotoLabel2 == null) {
                throw new RuntimeException("Internal error!");
            }
            list3.add(new TryCatchBlockNode(rangeLabel2, rangeLabel3, gotoLabel2, tryCatchBlockNode.type));
        }
        for (final LocalVariableNode localVariableNode : this.localVariables) {
            final LabelNode rangeLabel4 = instantiation.rangeLabel(localVariableNode.start);
            final LabelNode rangeLabel5 = instantiation.rangeLabel(localVariableNode.end);
            if (rangeLabel4 == rangeLabel5) {
                continue;
            }
            list4.add(new LocalVariableNode(localVariableNode.name, localVariableNode.desc, localVariableNode.signature, rangeLabel4, rangeLabel5, localVariableNode.index));
        }
    }
    
    private static void log(final String s) {
        System.err.println(s);
    }
    
    private void markSubroutineWalk(final BitSet set, int n, final BitSet set2) {
        this.markSubroutineWalkDFS(set, n, set2);
        int i = 1;
        while (i != 0) {
            n = 0;
            final Iterator<TryCatchBlockNode> iterator = this.tryCatchBlocks.iterator();
            while (true) {
                i = n;
                if (!iterator.hasNext()) {
                    break;
                }
                final TryCatchBlockNode tryCatchBlockNode = iterator.next();
                final int index = this.instructions.indexOf(tryCatchBlockNode.handler);
                if (set.get(index)) {
                    continue;
                }
                final int index2 = this.instructions.indexOf(tryCatchBlockNode.start);
                final int index3 = this.instructions.indexOf(tryCatchBlockNode.end);
                final int nextSetBit = set.nextSetBit(index2);
                int n2 = n;
                if (nextSetBit != -1) {
                    n2 = n;
                    if (nextSetBit < index3) {
                        this.markSubroutineWalkDFS(set, index, set2);
                        n2 = 1;
                    }
                }
                n = n2;
            }
        }
    }
    
    private void markSubroutineWalkDFS(final BitSet set, int n, final BitSet set2) {
    Label_0235_Outer:
        while (true) {
            final AbstractInsnNode value = this.instructions.get(n);
            if (set.get(n)) {
                break;
            }
            set.set(n);
            if (set2.get(n)) {
                this.dualCitizens.set(n);
            }
            set2.set(n);
            if (value.getType() == 7 && value.getOpcode() != 168) {
                this.markSubroutineWalkDFS(set, this.instructions.indexOf(((JumpInsnNode)value).label), set2);
            }
            if (value.getType() == 11) {
                final TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)value;
                this.markSubroutineWalkDFS(set, this.instructions.indexOf(tableSwitchInsnNode.dflt), set2);
                for (int i = tableSwitchInsnNode.labels.size() - 1; i >= 0; --i) {
                    this.markSubroutineWalkDFS(set, this.instructions.indexOf(tableSwitchInsnNode.labels.get(i)), set2);
                }
            }
            Label_0285: {
                if (value.getType() != 12) {
                    break Label_0285;
                }
                final LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode)value;
                this.markSubroutineWalkDFS(set, this.instructions.indexOf(lookupSwitchInsnNode.dflt), set2);
                int n2 = lookupSwitchInsnNode.labels.size() - 1;
            Block_11_Outer:
                while (true) {
                    if (n2 < 0) {
                        break Label_0285;
                    }
                    final int index = this.instructions.indexOf(lookupSwitchInsnNode.labels.get(n2));
                    try {
                        this.markSubroutineWalkDFS(set, index, set2);
                        --n2;
                        continue Block_11_Outer;
                        // switch([Lcom.strobel.decompiler.ast.Label;@3525d57c, opcode)
                        // iftrue(Label_0387:, opcode == 167 || opcode == 191)
                        while (true) {
                            final int opcode = this.instructions.get(n).getOpcode();
                            continue;
                        }
                        Label_0387: {
                            return;
                        }
                        Label_0384:
                        continue Label_0235_Outer;
                        Label_0368:
                        ++n;
                    }
                    // iftrue(Label_0384:, n < this.instructions.size())
                    catch (Throwable t) {
                        throw t;
                    }
                    break;
                }
            }
        }
    }
    
    private void markSubroutines() {
        final BitSet set = new BitSet();
        this.markSubroutineWalk(this.mainSubroutine, 0, set);
        for (final Map.Entry<LabelNode, BitSet> entry : this.subroutineHeads.entrySet()) {
            this.markSubroutineWalk(entry.getValue(), this.instructions.indexOf(entry.getKey()), set);
        }
    }
    
    @Override
    public void visitEnd() {
        if (!this.subroutineHeads.isEmpty()) {
            this.markSubroutines();
            this.emitCode();
        }
        if (this.mv != null) {
            this.accept(this.mv);
        }
    }
    
    @Override
    public void visitJumpInsn(final int n, final Label label) {
        super.visitJumpInsn(n, label);
        final LabelNode label2 = ((JumpInsnNode)this.instructions.getLast()).label;
        if (n == 168 && !this.subroutineHeads.containsKey(label2)) {
            this.subroutineHeads.put(label2, new BitSet());
        }
    }
    
    private class Instantiation extends AbstractMap<LabelNode, LabelNode>
    {
        final Instantiation previous;
        public final Map<LabelNode, LabelNode> rangeTable;
        public final LabelNode returnLabel;
        public final BitSet subroutine;
        
        Instantiation(final Instantiation previous, final BitSet subroutine) {
            this.rangeTable = new HashMap<LabelNode, LabelNode>();
            this.previous = previous;
            this.subroutine = subroutine;
            for (Instantiation previous2 = previous; previous2 != null; previous2 = previous2.previous) {
                if (previous2.subroutine == subroutine) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Recursive invocation of ");
                    sb.append(subroutine);
                    throw new RuntimeException(sb.toString());
                }
            }
            if (previous != null) {
                this.returnLabel = new LabelNode();
            }
            else {
                this.returnLabel = null;
            }
            LabelNode labelNode = null;
            for (int i = 0; i < JSRInlinerAdapter.this.instructions.size(); ++i) {
                final AbstractInsnNode value = JSRInlinerAdapter.this.instructions.get(i);
                if (value.getType() == 8) {
                    final LabelNode labelNode2 = (LabelNode)value;
                    LabelNode labelNode3;
                    if ((labelNode3 = labelNode) == null) {
                        labelNode3 = new LabelNode();
                    }
                    this.rangeTable.put(labelNode2, labelNode3);
                    labelNode = labelNode3;
                }
                else if (this.findOwner(i) == this) {
                    labelNode = null;
                }
            }
        }
        
        @Override
        public Set<Entry<LabelNode, LabelNode>> entrySet() {
            return null;
        }
        
        public Instantiation findOwner(final int n) {
            if (!this.subroutine.get(n)) {
                return null;
            }
            if (!JSRInlinerAdapter.this.dualCitizens.get(n)) {
                return this;
            }
            Instantiation instantiation = this;
            for (Instantiation instantiation2 = this.previous; instantiation2 != null; instantiation2 = instantiation2.previous) {
                if (instantiation2.subroutine.get(n)) {
                    instantiation = instantiation2;
                }
            }
            return instantiation;
        }
        
        @Override
        public LabelNode get(final Object o) {
            return this.gotoLabel((LabelNode)o);
        }
        
        public LabelNode gotoLabel(final LabelNode labelNode) {
            return this.findOwner(JSRInlinerAdapter.this.instructions.indexOf(labelNode)).rangeTable.get(labelNode);
        }
        
        public LabelNode rangeLabel(final LabelNode labelNode) {
            return this.rangeTable.get(labelNode);
        }
    }
}
