// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.asm.Opcodes;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.jacoco.agent.rt.internal_8ff85ea.asm.commons.AnalyzerAdapter;

class FrameSnapshot implements IFrame
{
    private static final FrameSnapshot NOP;
    private final Object[] locals;
    private final Object[] stack;
    
    static {
        NOP = new FrameSnapshot(null, null);
    }
    
    private FrameSnapshot(final Object[] locals, final Object[] stack) {
        this.locals = locals;
        this.stack = stack;
    }
    
    static IFrame create(final AnalyzerAdapter analyzerAdapter, final int n) {
        if (analyzerAdapter != null && analyzerAdapter.locals != null) {
            return new FrameSnapshot(reduce(analyzerAdapter.locals, 0), reduce(analyzerAdapter.stack, n));
        }
        return FrameSnapshot.NOP;
    }
    
    private static Object[] reduce(final List<Object> list, int n) {
        final ArrayList list2 = new ArrayList((Collection<? extends E>)list);
        n = list.size() - n;
        list2.subList(n, list.size()).clear();
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final E value = (E)list.get(n);
            if (value != Opcodes.LONG && value != Opcodes.DOUBLE) {
                continue;
            }
            list2.remove(n + 1);
        }
        return list2.toArray();
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        if (this.locals != null) {
            methodVisitor.visitFrame(-1, this.locals.length, this.locals, this.stack.length, this.stack);
        }
    }
}
