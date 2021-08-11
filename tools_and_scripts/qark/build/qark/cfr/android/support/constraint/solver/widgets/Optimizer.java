/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;

public class Optimizer {
    static void applyDirectResolutionHorizontalChain(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int n, ConstraintWidget constraintWidget) {
        int n2;
        int n3 = n;
        ConstraintWidget constraintWidget2 = constraintWidget;
        boolean bl = false;
        ConstraintWidget constraintWidget3 = null;
        int n4 = 0;
        float f = 0.0f;
        int n5 = 0;
        while (constraintWidget != null) {
            n2 = constraintWidget.getVisibility() == 8 ? 1 : 0;
            if (n2 == 0) {
                int n6 = n4 + 1;
                if (constraintWidget.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    int n7 = constraintWidget.getWidth();
                    n2 = constraintWidget.mLeft.mTarget != null ? constraintWidget.mLeft.getMargin() : 0;
                    n4 = constraintWidget.mRight.mTarget != null ? constraintWidget.mRight.getMargin() : 0;
                    n5 = n5 + n7 + n2 + n4;
                    n4 = n6;
                } else {
                    f += constraintWidget.mHorizontalWeight;
                    n4 = n6;
                }
            }
            constraintWidget3 = constraintWidget;
            constraintWidget = constraintWidget.mRight.mTarget != null ? constraintWidget.mRight.mTarget.mOwner : null;
            if (constraintWidget == null || constraintWidget.mLeft.mTarget != null && (constraintWidget.mLeft.mTarget == null || constraintWidget.mLeft.mTarget.mOwner == constraintWidget3)) continue;
            constraintWidget = null;
        }
        n2 = 0;
        if (constraintWidget3 != null) {
            n2 = constraintWidget3.mRight.mTarget != null ? constraintWidget3.mRight.mTarget.mOwner.getX() : 0;
            if (constraintWidget3.mRight.mTarget != null && constraintWidget3.mRight.mTarget.mOwner == constraintWidgetContainer) {
                n2 = constraintWidgetContainer.getRight();
            }
        }
        float f2 = (float)(n2 - 0) - (float)n5;
        float f3 = f2 / (float)(n4 + 1);
        constraintWidget3 = constraintWidget2;
        float f4 = 0.0f;
        if (n3 == 0) {
            f4 = f3;
        } else {
            f3 = f2 / (float)n3;
        }
        while (constraintWidget3 != null) {
            n2 = constraintWidget3.mLeft.mTarget != null ? constraintWidget3.mLeft.getMargin() : 0;
            n5 = constraintWidget3.mRight.mTarget != null ? constraintWidget3.mRight.getMargin() : 0;
            if (constraintWidget3.getVisibility() != 8) {
                linearSystem.addEquality(constraintWidget3.mLeft.mSolverVariable, (int)((f4 += (float)n2) + 0.5f));
                f4 = constraintWidget3.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? (f == 0.0f ? (f4 += f3 - (float)n2 - (float)n5) : (f4 += constraintWidget3.mHorizontalWeight * f2 / f - (float)n2 - (float)n5)) : (f4 += (float)constraintWidget3.getWidth());
                linearSystem.addEquality(constraintWidget3.mRight.mSolverVariable, (int)(f4 + 0.5f));
                if (n == 0) {
                    f4 += f3;
                }
                f4 += (float)n5;
            } else {
                float f5 = f4 - f3 / 2.0f;
                linearSystem.addEquality(constraintWidget3.mLeft.mSolverVariable, (int)(f5 + 0.5f));
                linearSystem.addEquality(constraintWidget3.mRight.mSolverVariable, (int)(f5 + 0.5f));
            }
            constraintWidget = constraintWidget3.mRight.mTarget != null ? constraintWidget3.mRight.mTarget.mOwner : null;
            if (constraintWidget != null && constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner != constraintWidget3) {
                constraintWidget = null;
            }
            if (constraintWidget == constraintWidgetContainer) {
                constraintWidget = null;
            }
            constraintWidget3 = constraintWidget;
        }
    }

    static void applyDirectResolutionVerticalChain(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int n, ConstraintWidget constraintWidget) {
        int n2;
        int n3 = n;
        ConstraintWidget constraintWidget2 = constraintWidget;
        boolean bl = false;
        ConstraintWidget constraintWidget3 = null;
        int n4 = 0;
        float f = 0.0f;
        int n5 = 0;
        while (constraintWidget != null) {
            n2 = constraintWidget.getVisibility() == 8 ? 1 : 0;
            if (n2 == 0) {
                int n6 = n4 + 1;
                if (constraintWidget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    int n7 = constraintWidget.getHeight();
                    n2 = constraintWidget.mTop.mTarget != null ? constraintWidget.mTop.getMargin() : 0;
                    n4 = constraintWidget.mBottom.mTarget != null ? constraintWidget.mBottom.getMargin() : 0;
                    n5 = n5 + n7 + n2 + n4;
                    n4 = n6;
                } else {
                    f += constraintWidget.mVerticalWeight;
                    n4 = n6;
                }
            }
            constraintWidget3 = constraintWidget;
            constraintWidget = constraintWidget.mBottom.mTarget != null ? constraintWidget.mBottom.mTarget.mOwner : null;
            if (constraintWidget == null || constraintWidget.mTop.mTarget != null && (constraintWidget.mTop.mTarget == null || constraintWidget.mTop.mTarget.mOwner == constraintWidget3)) continue;
            constraintWidget = null;
        }
        n2 = 0;
        if (constraintWidget3 != null) {
            n2 = constraintWidget3.mBottom.mTarget != null ? constraintWidget3.mBottom.mTarget.mOwner.getX() : 0;
            if (constraintWidget3.mBottom.mTarget != null && constraintWidget3.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                n2 = constraintWidgetContainer.getBottom();
            }
        }
        float f2 = (float)(n2 - 0) - (float)n5;
        float f3 = f2 / (float)(n4 + 1);
        constraintWidget3 = constraintWidget2;
        float f4 = 0.0f;
        if (n3 == 0) {
            f4 = f3;
        } else {
            f3 = f2 / (float)n3;
        }
        while (constraintWidget3 != null) {
            n2 = constraintWidget3.mTop.mTarget != null ? constraintWidget3.mTop.getMargin() : 0;
            n5 = constraintWidget3.mBottom.mTarget != null ? constraintWidget3.mBottom.getMargin() : 0;
            if (constraintWidget3.getVisibility() != 8) {
                linearSystem.addEquality(constraintWidget3.mTop.mSolverVariable, (int)((f4 += (float)n2) + 0.5f));
                f4 = constraintWidget3.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? (f == 0.0f ? (f4 += f3 - (float)n2 - (float)n5) : (f4 += constraintWidget3.mVerticalWeight * f2 / f - (float)n2 - (float)n5)) : (f4 += (float)constraintWidget3.getHeight());
                linearSystem.addEquality(constraintWidget3.mBottom.mSolverVariable, (int)(f4 + 0.5f));
                if (n == 0) {
                    f4 += f3;
                }
                f4 += (float)n5;
            } else {
                float f5 = f4 - f3 / 2.0f;
                linearSystem.addEquality(constraintWidget3.mTop.mSolverVariable, (int)(f5 + 0.5f));
                linearSystem.addEquality(constraintWidget3.mBottom.mSolverVariable, (int)(f5 + 0.5f));
            }
            constraintWidget = constraintWidget3.mBottom.mTarget != null ? constraintWidget3.mBottom.mTarget.mOwner : null;
            if (constraintWidget != null && constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner != constraintWidget3) {
                constraintWidget = null;
            }
            if (constraintWidget == constraintWidgetContainer) {
                constraintWidget = null;
            }
            constraintWidget3 = constraintWidget;
        }
    }

    static void checkHorizontalSimpleDependency(ConstraintWidgetContainer object, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mHorizontalResolution = 1;
            return;
        }
        if (object.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            int n = constraintWidget.mLeft.mMargin;
            int n2 = object.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
            constraintWidget.setHorizontalDimension(n, n2);
            constraintWidget.mHorizontalResolution = 2;
            return;
        }
        if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
            if (constraintWidget.mLeft.mTarget.mOwner == object && constraintWidget.mRight.mTarget.mOwner == object) {
                int n = constraintWidget.mLeft.getMargin();
                int n3 = constraintWidget.mRight.getMargin();
                if (object.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n3 = object.getWidth() - n3;
                } else {
                    int n4 = constraintWidget.getWidth();
                    n += (int)((float)(object.getWidth() - n - n3 - n4) * constraintWidget.mHorizontalBiasPercent + 0.5f);
                    n3 = constraintWidget.getWidth() + n;
                }
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n3);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n, n3);
                return;
            }
            constraintWidget.mHorizontalResolution = 1;
            return;
        }
        if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == object) {
            int n = constraintWidget.mLeft.getMargin();
            int n5 = constraintWidget.getWidth() + n;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n5);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(n, n5);
            return;
        }
        if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == object) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            int n = object.getWidth() - constraintWidget.mRight.getMargin();
            int n6 = n - constraintWidget.getWidth();
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n6);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(n6, n);
            return;
        }
        if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
            object = constraintWidget.mLeft.mTarget.mSolverVariable;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            int n = (int)(object.computedValue + (float)constraintWidget.mLeft.getMargin() + 0.5f);
            int n7 = constraintWidget.getWidth() + n;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n7);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(n, n7);
            return;
        }
        if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner.mHorizontalResolution == 2) {
            object = constraintWidget.mRight.mTarget.mSolverVariable;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            int n = (int)(object.computedValue - (float)constraintWidget.mRight.getMargin() + 0.5f);
            int n8 = n - constraintWidget.getWidth();
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n8);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(n8, n);
            return;
        }
        int n = constraintWidget.mLeft.mTarget != null ? 1 : 0;
        int n9 = constraintWidget.mRight.mTarget != null ? 1 : 0;
        if (n == 0 && n9 == 0) {
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 1) {
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    float f = guideline.getRelativeBegin() != -1 ? (float)guideline.getRelativeBegin() : (guideline.getRelativeEnd() != -1 ? (float)(object.getWidth() - guideline.getRelativeEnd()) : (float)object.getWidth() * guideline.getRelativePercent());
                    n = (int)(0.5f + f);
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setHorizontalDimension(n, n);
                    constraintWidget.setVerticalDimension(0, object.getHeight());
                }
                return;
            }
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            n = constraintWidget.getX();
            n9 = constraintWidget.getWidth();
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n9 + n);
            constraintWidget.mHorizontalResolution = 2;
            return;
        }
    }

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int n;
        int n2;
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            n = constraintWidget.mLeft.mMargin;
            n2 = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
            constraintWidget.setHorizontalDimension(n, n2);
            constraintWidget.mHorizontalResolution = 2;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            n = constraintWidget.mTop.mMargin;
            n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n);
            }
            constraintWidget.setVerticalDimension(n, n2);
            constraintWidget.mVerticalResolution = 2;
            return;
        }
    }

    static void checkVerticalSimpleDependency(ConstraintWidgetContainer object, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        Object object2 = constraintWidget.mVerticalDimensionBehaviour;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        int n = 1;
        if (object2 == dimensionBehaviour) {
            constraintWidget.mVerticalResolution = 1;
            return;
        }
        if (object.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            int n2 = constraintWidget.mTop.mMargin;
            int n3 = object.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n3);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n2);
            }
            constraintWidget.setVerticalDimension(n2, n3);
            constraintWidget.mVerticalResolution = 2;
            return;
        }
        if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
            if (constraintWidget.mTop.mTarget.mOwner == object && constraintWidget.mBottom.mTarget.mOwner == object) {
                int n4 = constraintWidget.mTop.getMargin();
                int n5 = constraintWidget.mBottom.getMargin();
                if (object.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n5 = constraintWidget.getHeight() + n4;
                } else {
                    n = constraintWidget.getHeight();
                    int n6 = object.getHeight();
                    n4 = (int)((float)n4 + (float)(n6 - n4 - n5 - n) * constraintWidget.mVerticalBiasPercent + 0.5f);
                    n5 = constraintWidget.getHeight() + n4;
                }
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n4);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n5);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n4);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n4, n5);
                return;
            }
            constraintWidget.mVerticalResolution = 1;
            return;
        }
        if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == object) {
            int n7 = constraintWidget.mTop.getMargin();
            int n8 = constraintWidget.getHeight() + n7;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n7);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n8);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n7);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n7, n8);
            return;
        }
        if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == object) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            int n9 = object.getHeight() - constraintWidget.mBottom.getMargin();
            int n10 = n9 - constraintWidget.getHeight();
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n10);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n9);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n10);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n10, n9);
            return;
        }
        if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
            object = constraintWidget.mTop.mTarget.mSolverVariable;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            int n11 = (int)(object.computedValue + (float)constraintWidget.mTop.getMargin() + 0.5f);
            int n12 = constraintWidget.getHeight() + n11;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n11);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n12);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n11);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n11, n12);
            return;
        }
        if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
            object = constraintWidget.mBottom.mTarget.mSolverVariable;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            int n13 = (int)(object.computedValue - (float)constraintWidget.mBottom.getMargin() + 0.5f);
            int n14 = n13 - constraintWidget.getHeight();
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n14);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n13);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n14);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n14, n13);
            return;
        }
        if (constraintWidget.mBaseline.mTarget != null && constraintWidget.mBaseline.mTarget.mOwner.mVerticalResolution == 2) {
            object = constraintWidget.mBaseline.mTarget.mSolverVariable;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            int n15 = (int)(object.computedValue - (float)constraintWidget.mBaselineDistance + 0.5f);
            int n16 = constraintWidget.getHeight() + n15;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n15);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n16);
            constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
            linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n15);
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n15, n16);
            return;
        }
        int n17 = constraintWidget.mBaseline.mTarget != null ? 1 : 0;
        int n18 = constraintWidget.mTop.mTarget != null ? 1 : 0;
        if (constraintWidget.mBottom.mTarget == null) {
            n = 0;
        }
        if (n17 == 0 && n18 == 0 && n == 0) {
            if (constraintWidget instanceof Guideline) {
                object2 = (Guideline)constraintWidget;
                if (object2.getOrientation() == 0) {
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    float f = object2.getRelativeBegin() != -1 ? (float)object2.getRelativeBegin() : (object2.getRelativeEnd() != -1 ? (float)(object.getHeight() - object2.getRelativeEnd()) : (float)object.getHeight() * object2.getRelativePercent());
                    n17 = (int)(0.5f + f);
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n17);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n17);
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setVerticalDimension(n17, n17);
                    constraintWidget.setHorizontalDimension(0, object.getWidth());
                }
                return;
            }
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            n17 = constraintWidget.getY();
            n18 = constraintWidget.getHeight();
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n17);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n18 + n17);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n17);
            }
            constraintWidget.mVerticalResolution = 2;
            return;
        }
    }
}

