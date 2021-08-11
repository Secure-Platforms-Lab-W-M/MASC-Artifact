package android.support.constraint.solver;

public class GoalRow extends ArrayRow {
   public GoalRow(Cache var1) {
      super(var1);
   }

   public void addError(SolverVariable var1) {
      super.addError(var1);
      --var1.usageInRowCount;
   }
}
