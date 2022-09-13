package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public abstract class AInterProcOperator implements IOperator {
    final InterprocProperties p;

    public AInterProcOperator(InterprocProperties p) {
        this.p = p;
    }
}
