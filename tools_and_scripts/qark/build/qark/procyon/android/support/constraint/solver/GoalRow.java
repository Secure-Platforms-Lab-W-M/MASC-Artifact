// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

public class GoalRow extends ArrayRow
{
    public GoalRow(final Cache cache) {
        super(cache);
    }
    
    @Override
    public void addError(final SolverVariable solverVariable) {
        super.addError(solverVariable);
        --solverVariable.usageInRowCount;
    }
}
