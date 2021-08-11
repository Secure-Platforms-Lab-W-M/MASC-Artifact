package android.support.constraint.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget {
   protected ConstraintWidget[] mWidgets = new ConstraintWidget[4];
   protected int mWidgetsCount = 0;

   public void add(ConstraintWidget var1) {
      int var2 = this.mWidgetsCount;
      ConstraintWidget[] var3 = this.mWidgets;
      if (var2 + 1 > var3.length) {
         this.mWidgets = (ConstraintWidget[])Arrays.copyOf(var3, var3.length * 2);
      }

      var3 = this.mWidgets;
      var2 = this.mWidgetsCount;
      var3[var2] = var1;
      this.mWidgetsCount = var2 + 1;
   }

   public void removeAllIds() {
      this.mWidgetsCount = 0;
   }
}
