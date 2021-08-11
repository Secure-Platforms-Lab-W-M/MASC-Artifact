// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import android.support.constraint.solver.LinearSystem;

class Chain
{
    private static final boolean DEBUG = false;
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n) {
        int n2;
        int n3;
        ChainHead[] array;
        if (n == 0) {
            n2 = 0;
            n3 = constraintWidgetContainer.mHorizontalChainsSize;
            array = constraintWidgetContainer.mHorizontalChainsArray;
        }
        else {
            n2 = 2;
            n3 = constraintWidgetContainer.mVerticalChainsSize;
            array = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < n3; ++i) {
            final ChainHead chainHead = array[i];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, n, n2, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
                }
            }
            else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
            }
        }
    }
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, int n, int margin, final ChainHead chainHead) {
        final ConstraintWidget mFirst = chainHead.mFirst;
        final ConstraintWidget mLast = chainHead.mLast;
        final ConstraintWidget mFirstVisibleWidget = chainHead.mFirstVisibleWidget;
        final ConstraintWidget mLastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget mHead = chainHead.mHead;
        final float mTotalWeight = chainHead.mTotalWeight;
        final ConstraintWidget mFirstMatchConstraintWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget mLastMatchConstraintWidget = chainHead.mLastMatchConstraintWidget;
        final boolean b = constraintWidgetContainer.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        int n2;
        int n3;
        ConstraintWidget constraintWidget;
        int i;
        int n4;
        if (n == 0) {
            final boolean b2 = mHead.mHorizontalChainStyle == 0;
            final int mHorizontalChainStyle = mHead.mHorizontalChainStyle;
            n2 = (b2 ? 1 : 0);
            final boolean b3 = mHorizontalChainStyle == 1;
            final boolean b4 = mHead.mHorizontalChainStyle == 2;
            n3 = (b3 ? 1 : 0);
            constraintWidget = mFirst;
            i = 0;
            n4 = (b4 ? 1 : 0);
        }
        else {
            final boolean b5 = mHead.mVerticalChainStyle == 0;
            final int mVerticalChainStyle = mHead.mVerticalChainStyle;
            n2 = (b5 ? 1 : 0);
            final boolean b6 = mVerticalChainStyle == 1;
            final boolean b7 = mHead.mVerticalChainStyle == 2;
            constraintWidget = mFirst;
            final boolean b8 = false;
            n3 = (b6 ? 1 : 0);
            n4 = (b7 ? 1 : 0);
            i = (b8 ? 1 : 0);
        }
        while (i == 0) {
            final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[margin];
            int n5 = 4;
            if (b || n4 != 0) {
                n5 = 1;
            }
            int margin2 = constraintAnchor.getMargin();
            if (constraintAnchor.mTarget != null && constraintWidget != mFirst) {
                margin2 += constraintAnchor.mTarget.getMargin();
            }
            if (n4 != 0 && constraintWidget != mFirst && constraintWidget != mFirstVisibleWidget) {
                n5 = 6;
            }
            else if (n2 != 0 && b) {
                n5 = 4;
            }
            if (constraintAnchor.mTarget != null) {
                if (constraintWidget == mFirstVisibleWidget) {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 5);
                }
                else {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 6);
                }
                linearSystem.addEquality(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, n5);
            }
            if (b) {
                if (constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin + 1].mSolverVariable, constraintWidget.mListAnchors[margin].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin].mSolverVariable, constraintWidgetContainer.mListAnchors[margin].mSolverVariable, 0, 6);
            }
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[margin + 1].mTarget;
            ConstraintWidget mOwner;
            if (mTarget != null) {
                mOwner = mTarget.mOwner;
                if (mOwner.mListAnchors[margin].mTarget == null || mOwner.mListAnchors[margin].mTarget.mOwner != constraintWidget) {
                    mOwner = null;
                }
            }
            else {
                mOwner = null;
            }
            if (mOwner != null) {
                constraintWidget = mOwner;
            }
            else {
                i = 1;
            }
        }
        if (mLastVisibleWidget != null && mLast.mListAnchors[margin + 1].mTarget != null) {
            final ConstraintAnchor constraintAnchor2 = mLastVisibleWidget.mListAnchors[margin + 1];
            linearSystem.addLowerThan(constraintAnchor2.mSolverVariable, mLast.mListAnchors[margin + 1].mTarget.mSolverVariable, -constraintAnchor2.getMargin(), 5);
        }
        if (b) {
            linearSystem.addGreaterThan(constraintWidgetContainer.mListAnchors[margin + 1].mSolverVariable, mLast.mListAnchors[margin + 1].mSolverVariable, mLast.mListAnchors[margin + 1].getMargin(), 6);
        }
        final ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets = chainHead.mWeightedMatchConstraintsWidgets;
        if (mWeightedMatchConstraintsWidgets != null) {
            final int size = mWeightedMatchConstraintsWidgets.size();
            if (size > 1) {
                float n6;
                if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                    n6 = (float)chainHead.mWidgetsMatchCount;
                }
                else {
                    n6 = mTotalWeight;
                }
                final ConstraintWidget constraintWidget2 = null;
                int j = 0;
                float n7 = 0.0f;
                ConstraintWidget constraintWidget3 = constraintWidget2;
                while (j < size) {
                    final ConstraintWidget constraintWidget4 = mWeightedMatchConstraintsWidgets.get(j);
                    float n8 = constraintWidget4.mWeight[n];
                    Label_1026: {
                        if (n8 < 0.0f) {
                            if (chainHead.mHasComplexMatchWeights) {
                                linearSystem.addEquality(constraintWidget4.mListAnchors[margin + 1].mSolverVariable, constraintWidget4.mListAnchors[margin].mSolverVariable, 0, 4);
                                n8 = n7;
                                break Label_1026;
                            }
                            n8 = 1.0f;
                        }
                        if (n8 == 0.0f) {
                            linearSystem.addEquality(constraintWidget4.mListAnchors[margin + 1].mSolverVariable, constraintWidget4.mListAnchors[margin].mSolverVariable, 0, 6);
                            n8 = n7;
                        }
                        else {
                            if (constraintWidget3 != null) {
                                final SolverVariable mSolverVariable = constraintWidget3.mListAnchors[margin].mSolverVariable;
                                final SolverVariable mSolverVariable2 = constraintWidget3.mListAnchors[margin + 1].mSolverVariable;
                                final SolverVariable mSolverVariable3 = constraintWidget4.mListAnchors[margin].mSolverVariable;
                                final SolverVariable mSolverVariable4 = constraintWidget4.mListAnchors[margin + 1].mSolverVariable;
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualMatchDimensions(n7, n6, n8, mSolverVariable, mSolverVariable2, mSolverVariable3, mSolverVariable4);
                                linearSystem.addConstraint(row);
                            }
                            constraintWidget3 = constraintWidget4;
                        }
                    }
                    ++j;
                    n7 = n8;
                }
            }
        }
        if (mFirstVisibleWidget != null && (mFirstVisibleWidget == mLastVisibleWidget || n4 != 0)) {
            ConstraintAnchor constraintAnchor3 = mFirst.mListAnchors[margin];
            ConstraintAnchor constraintAnchor4 = mLast.mListAnchors[margin + 1];
            SolverVariable mSolverVariable5;
            if (mFirst.mListAnchors[margin].mTarget != null) {
                mSolverVariable5 = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable5 = null;
            }
            SolverVariable mSolverVariable6;
            if (mLast.mListAnchors[margin + 1].mTarget != null) {
                mSolverVariable6 = mLast.mListAnchors[margin + 1].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable6 = null;
            }
            if (mFirstVisibleWidget == mLastVisibleWidget) {
                constraintAnchor3 = mFirstVisibleWidget.mListAnchors[margin];
                constraintAnchor4 = mFirstVisibleWidget.mListAnchors[margin + 1];
            }
            if (mSolverVariable5 != null && mSolverVariable6 != null) {
                float n9;
                if (n == 0) {
                    n9 = mHead.mHorizontalBiasPercent;
                }
                else {
                    n9 = mHead.mVerticalBiasPercent;
                }
                n = constraintAnchor3.getMargin();
                linearSystem.addCentering(constraintAnchor3.mSolverVariable, mSolverVariable5, n, n9, mSolverVariable6, constraintAnchor4.mSolverVariable, constraintAnchor4.getMargin(), 5);
            }
        }
        else if (n2 != 0 && mFirstVisibleWidget != null) {
            final boolean b9 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget5 = mFirstVisibleWidget;
            ConstraintWidget constraintWidget6 = mFirstVisibleWidget;
            while (constraintWidget5 != null) {
                ConstraintWidget constraintWidget7;
                for (constraintWidget7 = constraintWidget5.mNextChainWidget[n]; constraintWidget7 != null && constraintWidget7.getVisibility() == 8; constraintWidget7 = constraintWidget7.mNextChainWidget[n]) {}
                if (constraintWidget7 != null || constraintWidget5 == mLastVisibleWidget) {
                    final ConstraintAnchor constraintAnchor5 = constraintWidget5.mListAnchors[margin];
                    final SolverVariable mSolverVariable7 = constraintAnchor5.mSolverVariable;
                    SolverVariable solverVariable;
                    if (constraintAnchor5.mTarget != null) {
                        solverVariable = constraintAnchor5.mTarget.mSolverVariable;
                    }
                    else {
                        solverVariable = null;
                    }
                    if (constraintWidget6 != constraintWidget5) {
                        solverVariable = constraintWidget6.mListAnchors[margin + 1].mSolverVariable;
                    }
                    else if (constraintWidget5 == mFirstVisibleWidget && constraintWidget6 == constraintWidget5) {
                        if (mFirst.mListAnchors[margin].mTarget != null) {
                            solverVariable = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
                        }
                        else {
                            solverVariable = null;
                        }
                    }
                    SolverVariable mSolverVariable8 = null;
                    int n10 = constraintAnchor5.getMargin();
                    int n11 = constraintWidget5.mListAnchors[margin + 1].getMargin();
                    ConstraintAnchor constraintAnchor6;
                    SolverVariable solverVariable2;
                    SolverVariable solverVariable3;
                    if (constraintWidget7 != null) {
                        constraintAnchor6 = constraintWidget7.mListAnchors[margin];
                        final SolverVariable mSolverVariable9 = constraintAnchor6.mSolverVariable;
                        solverVariable2 = constraintWidget5.mListAnchors[margin + 1].mSolverVariable;
                        solverVariable3 = mSolverVariable9;
                    }
                    else {
                        final ConstraintAnchor mTarget2 = mLast.mListAnchors[margin + 1].mTarget;
                        if (mTarget2 != null) {
                            mSolverVariable8 = mTarget2.mSolverVariable;
                        }
                        solverVariable2 = constraintWidget5.mListAnchors[margin + 1].mSolverVariable;
                        solverVariable3 = mSolverVariable8;
                        constraintAnchor6 = mTarget2;
                    }
                    if (constraintAnchor6 != null) {
                        n11 += constraintAnchor6.getMargin();
                    }
                    if (constraintWidget6 != null) {
                        n10 += constraintWidget6.mListAnchors[margin + 1].getMargin();
                    }
                    if (mSolverVariable7 != null && solverVariable != null && solverVariable3 != null && solverVariable2 != null) {
                        if (constraintWidget5 == mFirstVisibleWidget) {
                            n10 = mFirstVisibleWidget.mListAnchors[margin].getMargin();
                        }
                        if (constraintWidget5 == mLastVisibleWidget) {
                            n11 = mLastVisibleWidget.mListAnchors[margin + 1].getMargin();
                        }
                        int n12;
                        if (b9) {
                            n12 = 6;
                        }
                        else {
                            n12 = 4;
                        }
                        linearSystem.addCentering(mSolverVariable7, solverVariable, n10, 0.5f, solverVariable3, solverVariable2, n11, n12);
                    }
                }
                if (constraintWidget5.getVisibility() != 8) {
                    constraintWidget6 = constraintWidget5;
                }
                constraintWidget5 = constraintWidget7;
            }
        }
        else if (n3 != 0 && mFirstVisibleWidget != null) {
            final boolean b10 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget8 = mFirstVisibleWidget;
            ConstraintWidget constraintWidget9 = mFirstVisibleWidget;
            while (constraintWidget8 != null) {
                ConstraintWidget constraintWidget10;
                for (constraintWidget10 = constraintWidget8.mNextChainWidget[n]; constraintWidget10 != null && constraintWidget10.getVisibility() == 8; constraintWidget10 = constraintWidget10.mNextChainWidget[n]) {}
                if (constraintWidget8 != mFirstVisibleWidget && constraintWidget8 != mLastVisibleWidget && constraintWidget10 != null) {
                    if (constraintWidget10 == mLastVisibleWidget) {
                        constraintWidget10 = null;
                    }
                    final ConstraintAnchor constraintAnchor7 = constraintWidget8.mListAnchors[margin];
                    final SolverVariable mSolverVariable10 = constraintAnchor7.mSolverVariable;
                    if (constraintAnchor7.mTarget != null) {
                        final SolverVariable mSolverVariable11 = constraintAnchor7.mTarget.mSolverVariable;
                    }
                    final SolverVariable mSolverVariable12 = constraintWidget9.mListAnchors[margin + 1].mSolverVariable;
                    SolverVariable mSolverVariable13 = null;
                    int margin3 = constraintAnchor7.getMargin();
                    int margin4 = constraintWidget8.mListAnchors[margin + 1].getMargin();
                    ConstraintAnchor constraintAnchor8;
                    SolverVariable mSolverVariable15;
                    SolverVariable solverVariable4;
                    if (constraintWidget10 != null) {
                        constraintAnchor8 = constraintWidget10.mListAnchors[margin];
                        final SolverVariable mSolverVariable14 = constraintAnchor8.mSolverVariable;
                        if (constraintAnchor8.mTarget != null) {
                            mSolverVariable15 = constraintAnchor8.mTarget.mSolverVariable;
                        }
                        else {
                            mSolverVariable15 = null;
                        }
                        solverVariable4 = mSolverVariable14;
                    }
                    else {
                        final ConstraintAnchor mTarget3 = constraintWidget8.mListAnchors[margin + 1].mTarget;
                        if (mTarget3 != null) {
                            mSolverVariable13 = mTarget3.mSolverVariable;
                        }
                        final SolverVariable mSolverVariable16 = constraintWidget8.mListAnchors[margin + 1].mSolverVariable;
                        solverVariable4 = mSolverVariable13;
                        mSolverVariable15 = mSolverVariable16;
                        constraintAnchor8 = mTarget3;
                    }
                    if (constraintAnchor8 != null) {
                        margin4 += constraintAnchor8.getMargin();
                    }
                    if (constraintWidget9 != null) {
                        margin3 += constraintWidget9.mListAnchors[margin + 1].getMargin();
                    }
                    int n13;
                    if (b10) {
                        n13 = 6;
                    }
                    else {
                        n13 = 4;
                    }
                    if (mSolverVariable10 != null && mSolverVariable12 != null && solverVariable4 != null && mSolverVariable15 != null) {
                        linearSystem.addCentering(mSolverVariable10, mSolverVariable12, margin3, 0.5f, solverVariable4, mSolverVariable15, margin4, n13);
                    }
                }
                if (constraintWidget8.getVisibility() != 8) {
                    constraintWidget9 = constraintWidget8;
                }
                constraintWidget8 = constraintWidget10;
            }
            final ConstraintAnchor constraintAnchor9 = mFirstVisibleWidget.mListAnchors[margin];
            final ConstraintAnchor mTarget4 = mFirst.mListAnchors[margin].mTarget;
            final ConstraintAnchor constraintAnchor10 = mLastVisibleWidget.mListAnchors[margin + 1];
            final ConstraintAnchor mTarget5 = mLast.mListAnchors[margin + 1].mTarget;
            if (mTarget4 != null) {
                if (mFirstVisibleWidget != mLastVisibleWidget) {
                    linearSystem.addEquality(constraintAnchor9.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor9.getMargin(), 5);
                }
                else if (mTarget5 != null) {
                    linearSystem.addCentering(constraintAnchor9.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor9.getMargin(), 0.5f, constraintAnchor10.mSolverVariable, mTarget5.mSolverVariable, constraintAnchor10.getMargin(), 5);
                }
            }
            if (mTarget5 != null && mFirstVisibleWidget != mLastVisibleWidget) {
                linearSystem.addEquality(constraintAnchor10.mSolverVariable, mTarget5.mSolverVariable, -constraintAnchor10.getMargin(), 5);
            }
        }
        if ((n2 == 0 && n3 == 0) || mFirstVisibleWidget == null) {
            return;
        }
        ConstraintAnchor constraintAnchor11 = mFirstVisibleWidget.mListAnchors[margin];
        final ConstraintAnchor constraintAnchor12 = mLastVisibleWidget.mListAnchors[margin + 1];
        SolverVariable mSolverVariable17;
        if (constraintAnchor11.mTarget != null) {
            mSolverVariable17 = constraintAnchor11.mTarget.mSolverVariable;
        }
        else {
            mSolverVariable17 = null;
        }
        SolverVariable solverVariable5;
        if (constraintAnchor12.mTarget != null) {
            solverVariable5 = constraintAnchor12.mTarget.mSolverVariable;
        }
        else {
            solverVariable5 = null;
        }
        if (mLast != mLastVisibleWidget) {
            final ConstraintAnchor constraintAnchor13 = mLast.mListAnchors[margin + 1];
            if (constraintAnchor13.mTarget != null) {
                solverVariable5 = constraintAnchor13.mTarget.mSolverVariable;
            }
            else {
                solverVariable5 = null;
            }
        }
        ConstraintAnchor constraintAnchor14;
        if (mFirstVisibleWidget == mLastVisibleWidget) {
            constraintAnchor11 = mFirstVisibleWidget.mListAnchors[margin];
            constraintAnchor14 = mFirstVisibleWidget.mListAnchors[margin + 1];
        }
        else {
            constraintAnchor14 = constraintAnchor12;
        }
        if (mSolverVariable17 != null && solverVariable5 != null) {
            n = constraintAnchor11.getMargin();
            ConstraintWidget constraintWidget11;
            if (mLastVisibleWidget == null) {
                constraintWidget11 = mLast;
            }
            else {
                constraintWidget11 = mLastVisibleWidget;
            }
            margin = constraintWidget11.mListAnchors[margin + 1].getMargin();
            linearSystem.addCentering(constraintAnchor11.mSolverVariable, mSolverVariable17, n, 0.5f, solverVariable5, constraintAnchor14.mSolverVariable, margin, 5);
        }
    }
}
