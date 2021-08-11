/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;

public class GoalRow
extends ArrayRow {
    public GoalRow(Cache cache) {
        super(cache);
    }

    @Override
    public void addError(SolverVariable solverVariable) {
        super.addError(solverVariable);
        --solverVariable.usageInRowCount;
    }
}

