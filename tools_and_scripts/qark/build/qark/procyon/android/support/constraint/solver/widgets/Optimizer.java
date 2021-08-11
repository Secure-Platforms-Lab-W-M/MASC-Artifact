// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;

public class Optimizer
{
    static void applyDirectResolutionHorizontalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        final ConstraintWidget constraintWidget2 = constraintWidget;
        ConstraintWidget constraintWidget3 = null;
        int n2 = 0;
        float n3 = 0.0f;
        int n4 = 0;
        while (constraintWidget != null) {
            if (constraintWidget.getVisibility() != 8) {
                final int n5 = n2 + 1;
                if (constraintWidget.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int width = constraintWidget.getWidth();
                    int margin;
                    if (constraintWidget.mLeft.mTarget != null) {
                        margin = constraintWidget.mLeft.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget.mRight.mTarget != null) {
                        margin2 = constraintWidget.mRight.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n4 = n4 + width + margin + margin2;
                    n2 = n5;
                }
                else {
                    n3 += constraintWidget.mHorizontalWeight;
                    n2 = n5;
                }
            }
            constraintWidget3 = constraintWidget;
            if (constraintWidget.mRight.mTarget != null) {
                constraintWidget = constraintWidget.mRight.mTarget.mOwner;
            }
            else {
                constraintWidget = null;
            }
            if (constraintWidget != null && (constraintWidget.mLeft.mTarget == null || (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner != constraintWidget3))) {
                constraintWidget = null;
            }
        }
        int n6 = 0;
        if (constraintWidget3 != null) {
            if (constraintWidget3.mRight.mTarget != null) {
                n6 = constraintWidget3.mRight.mTarget.mOwner.getX();
            }
            else {
                n6 = 0;
            }
            if (constraintWidget3.mRight.mTarget != null) {
                if (constraintWidget3.mRight.mTarget.mOwner == constraintWidgetContainer) {
                    n6 = constraintWidgetContainer.getRight();
                }
            }
        }
        final float n7 = n6 - 0 - (float)n4;
        float n8 = n7 / (n2 + 1);
        ConstraintWidget constraintWidget4 = constraintWidget2;
        float n9 = 0.0f;
        if (n == 0) {
            n9 = n8;
        }
        else {
            n8 = n7 / n;
        }
        while (constraintWidget4 != null) {
            int margin3;
            if (constraintWidget4.mLeft.mTarget != null) {
                margin3 = constraintWidget4.mLeft.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget4.mRight.mTarget != null) {
                margin4 = constraintWidget4.mRight.getMargin();
            }
            else {
                margin4 = 0;
            }
            if (constraintWidget4.getVisibility() != 8) {
                final float n10 = n9 + margin3;
                linearSystem.addEquality(constraintWidget4.mLeft.mSolverVariable, (int)(n10 + 0.5f));
                float n11;
                if (constraintWidget4.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n3 == 0.0f) {
                        n11 = n10 + (n8 - margin3 - margin4);
                    }
                    else {
                        n11 = n10 + (constraintWidget4.mHorizontalWeight * n7 / n3 - margin3 - margin4);
                    }
                }
                else {
                    n11 = n10 + constraintWidget4.getWidth();
                }
                linearSystem.addEquality(constraintWidget4.mRight.mSolverVariable, (int)(n11 + 0.5f));
                if (n == 0) {
                    n11 += n8;
                }
                n9 = n11 + margin4;
            }
            else {
                final float n12 = n9 - n8 / 2.0f;
                linearSystem.addEquality(constraintWidget4.mLeft.mSolverVariable, (int)(n12 + 0.5f));
                linearSystem.addEquality(constraintWidget4.mRight.mSolverVariable, (int)(n12 + 0.5f));
            }
            if (constraintWidget4.mRight.mTarget != null) {
                constraintWidget = constraintWidget4.mRight.mTarget.mOwner;
            }
            else {
                constraintWidget = null;
            }
            if (constraintWidget != null && constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner != constraintWidget4) {
                constraintWidget = null;
            }
            if (constraintWidget == constraintWidgetContainer) {
                constraintWidget = null;
            }
            constraintWidget4 = constraintWidget;
        }
    }
    
    static void applyDirectResolutionVerticalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        final ConstraintWidget constraintWidget2 = constraintWidget;
        ConstraintWidget constraintWidget3 = null;
        int n2 = 0;
        float n3 = 0.0f;
        int n4 = 0;
        while (constraintWidget != null) {
            if (constraintWidget.getVisibility() != 8) {
                final int n5 = n2 + 1;
                if (constraintWidget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int height = constraintWidget.getHeight();
                    int margin;
                    if (constraintWidget.mTop.mTarget != null) {
                        margin = constraintWidget.mTop.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget.mBottom.mTarget != null) {
                        margin2 = constraintWidget.mBottom.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n4 = n4 + height + margin + margin2;
                    n2 = n5;
                }
                else {
                    n3 += constraintWidget.mVerticalWeight;
                    n2 = n5;
                }
            }
            constraintWidget3 = constraintWidget;
            if (constraintWidget.mBottom.mTarget != null) {
                constraintWidget = constraintWidget.mBottom.mTarget.mOwner;
            }
            else {
                constraintWidget = null;
            }
            if (constraintWidget != null && (constraintWidget.mTop.mTarget == null || (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner != constraintWidget3))) {
                constraintWidget = null;
            }
        }
        int n6 = 0;
        if (constraintWidget3 != null) {
            if (constraintWidget3.mBottom.mTarget != null) {
                n6 = constraintWidget3.mBottom.mTarget.mOwner.getX();
            }
            else {
                n6 = 0;
            }
            if (constraintWidget3.mBottom.mTarget != null) {
                if (constraintWidget3.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                    n6 = constraintWidgetContainer.getBottom();
                }
            }
        }
        final float n7 = n6 - 0 - (float)n4;
        float n8 = n7 / (n2 + 1);
        ConstraintWidget constraintWidget4 = constraintWidget2;
        float n9 = 0.0f;
        if (n == 0) {
            n9 = n8;
        }
        else {
            n8 = n7 / n;
        }
        while (constraintWidget4 != null) {
            int margin3;
            if (constraintWidget4.mTop.mTarget != null) {
                margin3 = constraintWidget4.mTop.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget4.mBottom.mTarget != null) {
                margin4 = constraintWidget4.mBottom.getMargin();
            }
            else {
                margin4 = 0;
            }
            if (constraintWidget4.getVisibility() != 8) {
                final float n10 = n9 + margin3;
                linearSystem.addEquality(constraintWidget4.mTop.mSolverVariable, (int)(n10 + 0.5f));
                float n11;
                if (constraintWidget4.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n3 == 0.0f) {
                        n11 = n10 + (n8 - margin3 - margin4);
                    }
                    else {
                        n11 = n10 + (constraintWidget4.mVerticalWeight * n7 / n3 - margin3 - margin4);
                    }
                }
                else {
                    n11 = n10 + constraintWidget4.getHeight();
                }
                linearSystem.addEquality(constraintWidget4.mBottom.mSolverVariable, (int)(n11 + 0.5f));
                if (n == 0) {
                    n11 += n8;
                }
                n9 = n11 + margin4;
            }
            else {
                final float n12 = n9 - n8 / 2.0f;
                linearSystem.addEquality(constraintWidget4.mTop.mSolverVariable, (int)(n12 + 0.5f));
                linearSystem.addEquality(constraintWidget4.mBottom.mSolverVariable, (int)(n12 + 0.5f));
            }
            if (constraintWidget4.mBottom.mTarget != null) {
                constraintWidget = constraintWidget4.mBottom.mTarget.mOwner;
            }
            else {
                constraintWidget = null;
            }
            if (constraintWidget != null && constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner != constraintWidget4) {
                constraintWidget = null;
            }
            if (constraintWidget == constraintWidgetContainer) {
                constraintWidget = null;
            }
            constraintWidget4 = constraintWidget;
        }
    }
    
    static void checkHorizontalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mHorizontalResolution = 1;
            return;
        }
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.setHorizontalDimension(mMargin, n);
            constraintWidget.mHorizontalResolution = 2;
            return;
        }
        if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
            if (constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                int margin = constraintWidget.mLeft.getMargin();
                final int margin2 = constraintWidget.mRight.getMargin();
                int n2;
                if (constraintWidgetContainer.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n2 = constraintWidgetContainer.getWidth() - margin2;
                }
                else {
                    margin += (int)((constraintWidgetContainer.getWidth() - margin - margin2 - constraintWidget.getWidth()) * constraintWidget.mHorizontalBiasPercent + 0.5f);
                    n2 = constraintWidget.getWidth() + margin;
                }
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(margin, n2);
                return;
            }
            constraintWidget.mHorizontalResolution = 1;
        }
        else {
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer) {
                final int margin3 = constraintWidget.mLeft.getMargin();
                final int n3 = constraintWidget.getWidth() + margin3;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin3);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n3);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(margin3, n3);
                return;
            }
            if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n4 = constraintWidgetContainer.getWidth() - constraintWidget.mRight.getMargin();
                final int n5 = n4 - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n5);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n4);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n5, n4);
                return;
            }
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                final SolverVariable mSolverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n6 = (int)(mSolverVariable.computedValue + constraintWidget.mLeft.getMargin() + 0.5f);
                final int n7 = constraintWidget.getWidth() + n6;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n6);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n7);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n6, n7);
                return;
            }
            if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner.mHorizontalResolution == 2) {
                final SolverVariable mSolverVariable2 = constraintWidget.mRight.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n8 = (int)(mSolverVariable2.computedValue - constraintWidget.mRight.getMargin() + 0.5f);
                final int n9 = n8 - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n9);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n8);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n9, n8);
                return;
            }
            final boolean b = constraintWidget.mLeft.mTarget != null;
            final boolean b2 = constraintWidget.mRight.mTarget != null;
            if (b || b2) {
                return;
            }
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 1) {
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    float n10;
                    if (guideline.getRelativeBegin() != -1) {
                        n10 = (float)guideline.getRelativeBegin();
                    }
                    else if (guideline.getRelativeEnd() != -1) {
                        n10 = (float)(constraintWidgetContainer.getWidth() - guideline.getRelativeEnd());
                    }
                    else {
                        n10 = constraintWidgetContainer.getWidth() * guideline.getRelativePercent();
                    }
                    final int n11 = (int)(0.5f + n10);
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n11);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n11);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setHorizontalDimension(n11, n11);
                    constraintWidget.setVerticalDimension(0, constraintWidgetContainer.getHeight());
                }
                return;
            }
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int x = constraintWidget.getX();
            final int width = constraintWidget.getWidth();
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, x);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width + x);
            constraintWidget.mHorizontalResolution = 2;
        }
    }
    
    static void checkMatchParent(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.setHorizontalDimension(mMargin, n);
            constraintWidget.mHorizontalResolution = 2;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int mMargin2 = constraintWidget.mTop.mMargin;
            final int n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin2);
            }
            constraintWidget.setVerticalDimension(mMargin2, n2);
            constraintWidget.mVerticalResolution = 2;
        }
    }
    
    static void checkVerticalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        final ConstraintWidget.DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
        final ConstraintWidget.DimensionBehaviour match_CONSTRAINT = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean b = true;
        if (mVerticalDimensionBehaviour == match_CONSTRAINT) {
            constraintWidget.mVerticalResolution = 1;
            return;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int mMargin = constraintWidget.mTop.mMargin;
            final int n = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin);
            }
            constraintWidget.setVerticalDimension(mMargin, n);
            constraintWidget.mVerticalResolution = 2;
            return;
        }
        if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
            if (constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                int margin = constraintWidget.mTop.getMargin();
                final int margin2 = constraintWidget.mBottom.getMargin();
                int n2;
                if (constraintWidgetContainer.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n2 = constraintWidget.getHeight() + margin;
                }
                else {
                    margin = (int)(margin + (constraintWidgetContainer.getHeight() - margin - margin2 - constraintWidget.getHeight()) * constraintWidget.mVerticalBiasPercent + 0.5f);
                    n2 = constraintWidget.getHeight() + margin;
                }
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(margin, n2);
                return;
            }
            constraintWidget.mVerticalResolution = 1;
        }
        else {
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer) {
                final int margin3 = constraintWidget.mTop.getMargin();
                final int n3 = constraintWidget.getHeight() + margin3;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin3);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n3);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin3);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(margin3, n3);
                return;
            }
            if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n4 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.getMargin();
                final int n5 = n4 - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n5);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n4);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n5);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n5, n4);
                return;
            }
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable = constraintWidget.mTop.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n6 = (int)(mSolverVariable.computedValue + constraintWidget.mTop.getMargin() + 0.5f);
                final int n7 = constraintWidget.getHeight() + n6;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n6);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n7);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n6);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n6, n7);
                return;
            }
            if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable2 = constraintWidget.mBottom.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n8 = (int)(mSolverVariable2.computedValue - constraintWidget.mBottom.getMargin() + 0.5f);
                final int n9 = n8 - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n9);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n8);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n9);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n9, n8);
                return;
            }
            if (constraintWidget.mBaseline.mTarget != null && constraintWidget.mBaseline.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable3 = constraintWidget.mBaseline.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n10 = (int)(mSolverVariable3.computedValue - constraintWidget.mBaselineDistance + 0.5f);
                final int n11 = constraintWidget.getHeight() + n10;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n10);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n11);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n10);
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n10, n11);
                return;
            }
            final boolean b2 = constraintWidget.mBaseline.mTarget != null;
            final boolean b3 = constraintWidget.mTop.mTarget != null;
            if (constraintWidget.mBottom.mTarget == null) {
                b = false;
            }
            if (b2 || b3 || b) {
                return;
            }
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 0) {
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    float n12;
                    if (guideline.getRelativeBegin() != -1) {
                        n12 = (float)guideline.getRelativeBegin();
                    }
                    else if (guideline.getRelativeEnd() != -1) {
                        n12 = (float)(constraintWidgetContainer.getHeight() - guideline.getRelativeEnd());
                    }
                    else {
                        n12 = constraintWidgetContainer.getHeight() * guideline.getRelativePercent();
                    }
                    final int n13 = (int)(0.5f + n12);
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n13);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n13);
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setVerticalDimension(n13, n13);
                    constraintWidget.setHorizontalDimension(0, constraintWidgetContainer.getWidth());
                }
                return;
            }
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int y = constraintWidget.getY();
            final int height = constraintWidget.getHeight();
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, y);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height + y);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + y);
            }
            constraintWidget.mVerticalResolution = 2;
        }
    }
}
