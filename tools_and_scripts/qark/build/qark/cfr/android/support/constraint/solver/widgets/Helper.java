/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.Arrays;

public class Helper
extends ConstraintWidget {
    protected ConstraintWidget[] mWidgets = new ConstraintWidget[4];
    protected int mWidgetsCount = 0;

    public void add(ConstraintWidget constraintWidget) {
        int n = this.mWidgetsCount;
        ConstraintWidget[] arrconstraintWidget = this.mWidgets;
        if (n + 1 > arrconstraintWidget.length) {
            this.mWidgets = Arrays.copyOf(arrconstraintWidget, arrconstraintWidget.length * 2);
        }
        arrconstraintWidget = this.mWidgets;
        n = this.mWidgetsCount;
        arrconstraintWidget[n] = constraintWidget;
        this.mWidgetsCount = n + 1;
    }

    public void removeAllIds() {
        this.mWidgetsCount = 0;
    }
}

