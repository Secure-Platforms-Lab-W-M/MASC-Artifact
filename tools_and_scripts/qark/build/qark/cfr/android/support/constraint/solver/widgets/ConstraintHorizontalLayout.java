/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;

public class ConstraintHorizontalLayout
extends ConstraintWidgetContainer {
    private ContentAlignment mAlignment = ContentAlignment.MIDDLE;

    public ConstraintHorizontalLayout() {
    }

    public ConstraintHorizontalLayout(int n, int n2) {
        super(n, n2);
    }

    public ConstraintHorizontalLayout(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    @Override
    public void addToSolver(LinearSystem linearSystem, int n) {
        if (this.mChildren.size() != 0) {
            ConstraintAnchor.Strength strength;
            ConstraintWidget constraintWidget = this;
            int n2 = this.mChildren.size();
            for (int i = 0; i < n2; ++i) {
                ConstraintWidget constraintWidget2 = (ConstraintWidget)this.mChildren.get(i);
                if (constraintWidget != this) {
                    constraintWidget2.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.RIGHT);
                    constraintWidget.connect(ConstraintAnchor.Type.RIGHT, constraintWidget2, ConstraintAnchor.Type.LEFT);
                } else {
                    strength = ConstraintAnchor.Strength.STRONG;
                    if (this.mAlignment == ContentAlignment.END) {
                        strength = ConstraintAnchor.Strength.WEAK;
                    }
                    constraintWidget2.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength);
                }
                constraintWidget2.connect(ConstraintAnchor.Type.TOP, this, ConstraintAnchor.Type.TOP);
                constraintWidget2.connect(ConstraintAnchor.Type.BOTTOM, this, ConstraintAnchor.Type.BOTTOM);
                constraintWidget = constraintWidget2;
            }
            if (constraintWidget != this) {
                strength = ConstraintAnchor.Strength.STRONG;
                if (this.mAlignment == ContentAlignment.BEGIN) {
                    strength = ConstraintAnchor.Strength.WEAK;
                }
                constraintWidget.connect(ConstraintAnchor.Type.RIGHT, this, ConstraintAnchor.Type.RIGHT, 0, strength);
            }
        }
        super.addToSolver(linearSystem, n);
    }

    public static enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT;
        

        private ContentAlignment() {
        }
    }

}

