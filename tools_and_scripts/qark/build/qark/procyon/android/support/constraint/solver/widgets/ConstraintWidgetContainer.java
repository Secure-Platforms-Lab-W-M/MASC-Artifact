// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import java.util.Arrays;
import android.support.constraint.solver.LinearSystem;

public class ConstraintWidgetContainer extends WidgetContainer
{
    static boolean ALLOW_ROOT_GROUP = false;
    private static final int CHAIN_FIRST = 0;
    private static final int CHAIN_FIRST_VISIBLE = 2;
    private static final int CHAIN_LAST = 1;
    private static final int CHAIN_LAST_VISIBLE = 3;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_OPTIMIZE = false;
    private static final int FLAG_CHAIN_DANGLING = 1;
    private static final int FLAG_CHAIN_OPTIMIZE = 0;
    private static final int FLAG_RECOMPUTE_BOUNDS = 2;
    private static final int MAX_ITERATIONS = 8;
    public static final int OPTIMIZATION_ALL = 2;
    public static final int OPTIMIZATION_BASIC = 4;
    public static final int OPTIMIZATION_CHAIN = 8;
    public static final int OPTIMIZATION_NONE = 1;
    private static final boolean USE_SNAPSHOT = true;
    private static final boolean USE_THREAD = false;
    private boolean[] flags;
    protected LinearSystem mBackgroundSystem;
    private ConstraintWidget[] mChainEnds;
    private boolean mHeightMeasuredTooSmall;
    private ConstraintWidget[] mHorizontalChainsArray;
    private int mHorizontalChainsSize;
    private ConstraintWidget[] mMatchConstraintsChainedWidgets;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    private ConstraintWidget[] mVerticalChainsArray;
    private int mVerticalChainsSize;
    private boolean mWidthMeasuredTooSmall;
    int mWrapHeight;
    int mWrapWidth;
    
    static {
        ConstraintWidgetContainer.ALLOW_ROOT_GROUP = true;
    }
    
    public ConstraintWidgetContainer() {
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    private void addHorizontalChain(final ConstraintWidget constraintWidget) {
        int n = 0;
        while (true) {
            final int mHorizontalChainsSize = this.mHorizontalChainsSize;
            if (n >= mHorizontalChainsSize) {
                final ConstraintWidget[] mHorizontalChainsArray = this.mHorizontalChainsArray;
                if (mHorizontalChainsSize + 1 >= mHorizontalChainsArray.length) {
                    this.mHorizontalChainsArray = Arrays.copyOf(mHorizontalChainsArray, mHorizontalChainsArray.length * 2);
                }
                final ConstraintWidget[] mHorizontalChainsArray2 = this.mHorizontalChainsArray;
                final int mHorizontalChainsSize2 = this.mHorizontalChainsSize;
                mHorizontalChainsArray2[mHorizontalChainsSize2] = constraintWidget;
                this.mHorizontalChainsSize = mHorizontalChainsSize2 + 1;
                return;
            }
            if (this.mHorizontalChainsArray[n] == constraintWidget) {
                return;
            }
            ++n;
        }
    }
    
    private void addVerticalChain(final ConstraintWidget constraintWidget) {
        int n = 0;
        while (true) {
            final int mVerticalChainsSize = this.mVerticalChainsSize;
            if (n >= mVerticalChainsSize) {
                final ConstraintWidget[] mVerticalChainsArray = this.mVerticalChainsArray;
                if (mVerticalChainsSize + 1 >= mVerticalChainsArray.length) {
                    this.mVerticalChainsArray = Arrays.copyOf(mVerticalChainsArray, mVerticalChainsArray.length * 2);
                }
                final ConstraintWidget[] mVerticalChainsArray2 = this.mVerticalChainsArray;
                final int mVerticalChainsSize2 = this.mVerticalChainsSize;
                mVerticalChainsArray2[mVerticalChainsSize2] = constraintWidget;
                this.mVerticalChainsSize = mVerticalChainsSize2 + 1;
                return;
            }
            if (this.mVerticalChainsArray[n] == constraintWidget) {
                return;
            }
            ++n;
        }
    }
    
    private void applyHorizontalChain(final LinearSystem linearSystem) {
        LinearSystem linearSystem2 = linearSystem;
        for (int i = 0; i < this.mHorizontalChainsSize; ++i) {
            final ConstraintWidget[] mHorizontalChainsArray = this.mHorizontalChainsArray;
            final ConstraintWidget constraintWidget = mHorizontalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem, this.mChainEnds, mHorizontalChainsArray[i], 0, this.flags);
            ConstraintWidget mHorizontalNextWidget = this.mChainEnds[2];
            if (mHorizontalNextWidget != null) {
                if (this.flags[1]) {
                    int drawX = constraintWidget.getDrawX();
                    while (mHorizontalNextWidget != null) {
                        linearSystem2.addEquality(mHorizontalNextWidget.mLeft.mSolverVariable, drawX);
                        final ConstraintWidget mHorizontalNextWidget2 = mHorizontalNextWidget.mHorizontalNextWidget;
                        drawX += mHorizontalNextWidget.mLeft.getMargin() + mHorizontalNextWidget.getWidth() + mHorizontalNextWidget.mRight.getMargin();
                        mHorizontalNextWidget = mHorizontalNextWidget2;
                    }
                }
                else {
                    final boolean b = constraintWidget.mHorizontalChainStyle == 0;
                    final boolean b2 = constraintWidget.mHorizontalChainStyle == 2;
                    final ConstraintWidget constraintWidget2 = constraintWidget;
                    final boolean b3 = this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT;
                    final int mOptimizationLevel = this.mOptimizationLevel;
                    if ((mOptimizationLevel == 2 || mOptimizationLevel == 8) && this.flags[0] && constraintWidget2.mHorizontalChainFixedPosition && !b2 && !b3 && constraintWidget.mHorizontalChainStyle == 0) {
                        Optimizer.applyDirectResolutionHorizontalChain(this, linearSystem2, countMatchConstraintsChainedWidgets, constraintWidget2);
                    }
                    else if (countMatchConstraintsChainedWidgets != 0 && !b2) {
                        ConstraintWidget constraintWidget3 = null;
                        float n = 0.0f;
                        while (mHorizontalNextWidget != null) {
                            if (mHorizontalNextWidget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                                int margin = mHorizontalNextWidget.mLeft.getMargin();
                                if (constraintWidget3 != null) {
                                    margin += constraintWidget3.mRight.getMargin();
                                }
                                int n2 = 3;
                                if (mHorizontalNextWidget.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n2 = 2;
                                }
                                linearSystem2.addGreaterThan(mHorizontalNextWidget.mLeft.mSolverVariable, mHorizontalNextWidget.mLeft.mTarget.mSolverVariable, margin, n2);
                                int margin2 = mHorizontalNextWidget.mRight.getMargin();
                                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget != null && mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == mHorizontalNextWidget) {
                                    margin2 += mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.getMargin();
                                }
                                int n3 = 3;
                                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n3 = 2;
                                }
                                linearSystem2.addLowerThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mRight.mTarget.mSolverVariable, -margin2, n3);
                            }
                            else {
                                n += mHorizontalNextWidget.mHorizontalWeight;
                                int margin3 = 0;
                                if (mHorizontalNextWidget.mRight.mTarget != null) {
                                    margin3 = mHorizontalNextWidget.mRight.getMargin();
                                    if (mHorizontalNextWidget != this.mChainEnds[3]) {
                                        margin3 += mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.getMargin();
                                    }
                                }
                                linearSystem2.addGreaterThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mLeft.mSolverVariable, 0, 1);
                                linearSystem2.addLowerThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mRight.mTarget.mSolverVariable, -margin3, 1);
                            }
                            constraintWidget3 = mHorizontalNextWidget;
                            mHorizontalNextWidget = mHorizontalNextWidget.mHorizontalNextWidget;
                        }
                        if (countMatchConstraintsChainedWidgets == 1) {
                            final ConstraintWidget constraintWidget4 = this.mMatchConstraintsChainedWidgets[0];
                            int margin4 = constraintWidget4.mLeft.getMargin();
                            if (constraintWidget4.mLeft.mTarget != null) {
                                margin4 += constraintWidget4.mLeft.mTarget.getMargin();
                            }
                            int margin5 = constraintWidget4.mRight.getMargin();
                            if (constraintWidget4.mRight.mTarget != null) {
                                margin5 += constraintWidget4.mRight.mTarget.getMargin();
                            }
                            SolverVariable solverVariable = constraintWidget2.mRight.mTarget.mSolverVariable;
                            final ConstraintWidget[] mChainEnds = this.mChainEnds;
                            if (constraintWidget4 == mChainEnds[3]) {
                                solverVariable = mChainEnds[1].mRight.mTarget.mSolverVariable;
                            }
                            if (constraintWidget4.mMatchConstraintDefaultWidth == 1) {
                                linearSystem2.addGreaterThan(constraintWidget2.mLeft.mSolverVariable, constraintWidget2.mLeft.mTarget.mSolverVariable, margin4, 1);
                                linearSystem2.addLowerThan(constraintWidget2.mRight.mSolverVariable, solverVariable, -margin5, 1);
                                linearSystem2.addEquality(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mLeft.mSolverVariable, constraintWidget2.getWidth(), 2);
                            }
                            else {
                                linearSystem2.addEquality(constraintWidget4.mLeft.mSolverVariable, constraintWidget4.mLeft.mTarget.mSolverVariable, margin4, 1);
                                linearSystem2.addEquality(constraintWidget4.mRight.mSolverVariable, solverVariable, -margin5, 1);
                            }
                        }
                        else {
                            for (int j = 0, n4 = countMatchConstraintsChainedWidgets; j < n4 - 1; ++j) {
                                final ConstraintWidget[] mMatchConstraintsChainedWidgets = this.mMatchConstraintsChainedWidgets;
                                final ConstraintWidget constraintWidget5 = mMatchConstraintsChainedWidgets[j];
                                final ConstraintWidget constraintWidget6 = mMatchConstraintsChainedWidgets[j + 1];
                                final SolverVariable mSolverVariable = constraintWidget5.mLeft.mSolverVariable;
                                final SolverVariable mSolverVariable2 = constraintWidget5.mRight.mSolverVariable;
                                final SolverVariable mSolverVariable3 = constraintWidget6.mLeft.mSolverVariable;
                                SolverVariable solverVariable2 = constraintWidget6.mRight.mSolverVariable;
                                final ConstraintWidget[] mChainEnds2 = this.mChainEnds;
                                if (constraintWidget6 == mChainEnds2[3]) {
                                    solverVariable2 = mChainEnds2[1].mRight.mSolverVariable;
                                }
                                int margin6 = constraintWidget5.mLeft.getMargin();
                                if (constraintWidget5.mLeft.mTarget != null && constraintWidget5.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget5.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget5) {
                                    margin6 += constraintWidget5.mLeft.mTarget.mOwner.mRight.getMargin();
                                }
                                linearSystem2.addGreaterThan(mSolverVariable, constraintWidget5.mLeft.mTarget.mSolverVariable, margin6, 2);
                                final int margin7 = constraintWidget5.mRight.getMargin();
                                int n5;
                                if (constraintWidget5.mRight.mTarget != null && constraintWidget5.mHorizontalNextWidget != null) {
                                    int margin8;
                                    if (constraintWidget5.mHorizontalNextWidget.mLeft.mTarget != null) {
                                        margin8 = constraintWidget5.mHorizontalNextWidget.mLeft.getMargin();
                                    }
                                    else {
                                        margin8 = 0;
                                    }
                                    n5 = margin7 + margin8;
                                }
                                else {
                                    n5 = margin7;
                                }
                                linearSystem2.addLowerThan(mSolverVariable2, constraintWidget5.mRight.mTarget.mSolverVariable, -n5, 2);
                                if (j + 1 == n4 - 1) {
                                    int margin9 = constraintWidget6.mLeft.getMargin();
                                    if (constraintWidget6.mLeft.mTarget != null && constraintWidget6.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget6.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget6) {
                                        margin9 += constraintWidget6.mLeft.mTarget.mOwner.mRight.getMargin();
                                    }
                                    linearSystem2.addGreaterThan(mSolverVariable3, constraintWidget6.mLeft.mTarget.mSolverVariable, margin9, 2);
                                    ConstraintAnchor constraintAnchor = constraintWidget6.mRight;
                                    final ConstraintWidget[] mChainEnds3 = this.mChainEnds;
                                    if (constraintWidget6 == mChainEnds3[3]) {
                                        constraintAnchor = mChainEnds3[1].mRight;
                                    }
                                    int margin10 = constraintAnchor.getMargin();
                                    if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner.mLeft.mTarget != null && constraintAnchor.mTarget.mOwner.mLeft.mTarget.mOwner == constraintWidget6) {
                                        margin10 += constraintAnchor.mTarget.mOwner.mLeft.getMargin();
                                    }
                                    linearSystem2.addLowerThan(solverVariable2, constraintAnchor.mTarget.mSolverVariable, -margin10, 2);
                                }
                                if (constraintWidget2.mMatchConstraintMaxWidth > 0) {
                                    linearSystem2.addLowerThan(mSolverVariable2, mSolverVariable, constraintWidget2.mMatchConstraintMaxWidth, 2);
                                }
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualDimension(constraintWidget5.mHorizontalWeight, n, constraintWidget6.mHorizontalWeight, mSolverVariable, constraintWidget5.mLeft.getMargin(), mSolverVariable2, constraintWidget5.mRight.getMargin(), mSolverVariable3, constraintWidget6.mLeft.getMargin(), solverVariable2, constraintWidget6.mRight.getMargin());
                                linearSystem2.addConstraint(row);
                            }
                        }
                    }
                    else {
                        boolean b4 = false;
                        ConstraintWidget constraintWidget7 = null;
                        ConstraintWidget constraintWidget8 = null;
                        final LinearSystem linearSystem3 = linearSystem2;
                        ConstraintWidget constraintWidget9 = mHorizontalNextWidget;
                        SolverVariable solverVariable3;
                        while (true) {
                            final ConstraintWidget constraintWidget10 = constraintWidget9;
                            solverVariable3 = null;
                            final ConstraintWidget constraintWidget11 = null;
                            if (constraintWidget10 == null) {
                                break;
                            }
                            final ConstraintWidget mHorizontalNextWidget3 = constraintWidget10.mHorizontalNextWidget;
                            if (mHorizontalNextWidget3 == null) {
                                constraintWidget7 = this.mChainEnds[1];
                                b4 = true;
                            }
                            ConstraintWidget mOwner = null;
                            Label_2363: {
                                if (b2) {
                                    final ConstraintAnchor mLeft = constraintWidget10.mLeft;
                                    int margin11 = mLeft.getMargin();
                                    if (constraintWidget8 != null) {
                                        margin11 += constraintWidget8.mRight.getMargin();
                                    }
                                    int n6 = 1;
                                    if (mHorizontalNextWidget != constraintWidget10) {
                                        n6 = 3;
                                    }
                                    linearSystem3.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, margin11, n6);
                                    if (constraintWidget10.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                        final ConstraintAnchor mRight = constraintWidget10.mRight;
                                        if (constraintWidget10.mMatchConstraintDefaultWidth == 1) {
                                            linearSystem3.addEquality(mRight.mSolverVariable, mLeft.mSolverVariable, Math.max(constraintWidget10.mMatchConstraintMinWidth, constraintWidget10.getWidth()), 3);
                                        }
                                        else {
                                            linearSystem3.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, mLeft.mMargin, 3);
                                            linearSystem3.addLowerThan(mRight.mSolverVariable, mLeft.mSolverVariable, constraintWidget10.mMatchConstraintMinWidth, 3);
                                        }
                                    }
                                }
                                else if (!b && b4 && constraintWidget8 != null) {
                                    if (constraintWidget10.mRight.mTarget == null) {
                                        linearSystem3.addEquality(constraintWidget10.mRight.mSolverVariable, constraintWidget10.getDrawRight());
                                    }
                                    else {
                                        linearSystem3.addEquality(constraintWidget10.mRight.mSolverVariable, constraintWidget7.mRight.mTarget.mSolverVariable, -constraintWidget10.mRight.getMargin(), 5);
                                    }
                                }
                                else if (!b && !b4 && constraintWidget8 == null) {
                                    if (constraintWidget10.mLeft.mTarget == null) {
                                        linearSystem3.addEquality(constraintWidget10.mLeft.mSolverVariable, constraintWidget10.getDrawX());
                                    }
                                    else {
                                        linearSystem3.addEquality(constraintWidget10.mLeft.mSolverVariable, constraintWidget.mLeft.mTarget.mSolverVariable, constraintWidget10.mLeft.getMargin(), 5);
                                    }
                                }
                                else {
                                    final ConstraintAnchor mLeft2 = constraintWidget10.mLeft;
                                    final ConstraintAnchor mRight2 = constraintWidget10.mRight;
                                    final int margin12 = mLeft2.getMargin();
                                    final int margin13 = mRight2.getMargin();
                                    linearSystem3.addGreaterThan(mLeft2.mSolverVariable, mLeft2.mTarget.mSolverVariable, margin12, 1);
                                    linearSystem3.addLowerThan(mRight2.mSolverVariable, mRight2.mTarget.mSolverVariable, -margin13, 1);
                                    SolverVariable mSolverVariable4;
                                    if (mLeft2.mTarget != null) {
                                        mSolverVariable4 = mLeft2.mTarget.mSolverVariable;
                                    }
                                    else {
                                        mSolverVariable4 = null;
                                    }
                                    SolverVariable solverVariable4;
                                    if (constraintWidget8 == null) {
                                        SolverVariable mSolverVariable5;
                                        if (constraintWidget.mLeft.mTarget != null) {
                                            mSolverVariable5 = constraintWidget.mLeft.mTarget.mSolverVariable;
                                        }
                                        else {
                                            mSolverVariable5 = null;
                                        }
                                        solverVariable4 = mSolverVariable5;
                                    }
                                    else {
                                        solverVariable4 = mSolverVariable4;
                                    }
                                    if (mHorizontalNextWidget3 == null) {
                                        if (constraintWidget7.mRight.mTarget != null) {
                                            mOwner = constraintWidget7.mRight.mTarget.mOwner;
                                        }
                                        else {
                                            mOwner = null;
                                        }
                                    }
                                    else {
                                        mOwner = mHorizontalNextWidget3;
                                    }
                                    if (mOwner == null) {
                                        break Label_2363;
                                    }
                                    SolverVariable solverVariable5 = mOwner.mLeft.mSolverVariable;
                                    if (b4) {
                                        if (constraintWidget7.mRight.mTarget != null) {
                                            solverVariable5 = constraintWidget7.mRight.mTarget.mSolverVariable;
                                        }
                                        else {
                                            solverVariable5 = null;
                                        }
                                    }
                                    if (solverVariable4 != null && solverVariable5 != null) {
                                        linearSystem.addCentering(mLeft2.mSolverVariable, solverVariable4, margin12, 0.5f, solverVariable5, mRight2.mSolverVariable, margin13, 4);
                                    }
                                    break Label_2363;
                                }
                                mOwner = mHorizontalNextWidget3;
                            }
                            ConstraintWidget constraintWidget12;
                            if (b4) {
                                constraintWidget12 = constraintWidget11;
                            }
                            else {
                                constraintWidget12 = mOwner;
                            }
                            constraintWidget9 = constraintWidget12;
                            constraintWidget8 = constraintWidget10;
                        }
                        linearSystem2 = linearSystem3;
                        if (b2) {
                            final ConstraintAnchor mLeft3 = mHorizontalNextWidget.mLeft;
                            final ConstraintAnchor mRight3 = constraintWidget7.mRight;
                            final int margin14 = mLeft3.getMargin();
                            final int margin15 = mRight3.getMargin();
                            SolverVariable mSolverVariable6;
                            if (constraintWidget.mLeft.mTarget != null) {
                                mSolverVariable6 = constraintWidget.mLeft.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable6 = null;
                            }
                            SolverVariable mSolverVariable7 = solverVariable3;
                            if (constraintWidget7.mRight.mTarget != null) {
                                mSolverVariable7 = constraintWidget7.mRight.mTarget.mSolverVariable;
                            }
                            if (mSolverVariable6 != null && mSolverVariable7 != null) {
                                linearSystem2.addLowerThan(mRight3.mSolverVariable, mSolverVariable7, -margin15, 1);
                                linearSystem.addCentering(mLeft3.mSolverVariable, mSolverVariable6, margin14, constraintWidget.mHorizontalBiasPercent, mSolverVariable7, mRight3.mSolverVariable, margin15, 4);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void applyVerticalChain(final LinearSystem linearSystem) {
        LinearSystem linearSystem2 = linearSystem;
        for (int i = 0; i < this.mVerticalChainsSize; ++i) {
            final ConstraintWidget[] mVerticalChainsArray = this.mVerticalChainsArray;
            final ConstraintWidget constraintWidget = mVerticalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem, this.mChainEnds, mVerticalChainsArray[i], 1, this.flags);
            ConstraintWidget mVerticalNextWidget = this.mChainEnds[2];
            if (mVerticalNextWidget != null) {
                if (this.flags[1]) {
                    int drawY = constraintWidget.getDrawY();
                    while (mVerticalNextWidget != null) {
                        linearSystem2.addEquality(mVerticalNextWidget.mTop.mSolverVariable, drawY);
                        final ConstraintWidget mVerticalNextWidget2 = mVerticalNextWidget.mVerticalNextWidget;
                        drawY += mVerticalNextWidget.mTop.getMargin() + mVerticalNextWidget.getHeight() + mVerticalNextWidget.mBottom.getMargin();
                        mVerticalNextWidget = mVerticalNextWidget2;
                    }
                }
                else {
                    final boolean b = constraintWidget.mVerticalChainStyle == 0;
                    final boolean b2 = constraintWidget.mVerticalChainStyle == 2;
                    final ConstraintWidget constraintWidget2 = constraintWidget;
                    final boolean b3 = this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT;
                    final int mOptimizationLevel = this.mOptimizationLevel;
                    if ((mOptimizationLevel == 2 || mOptimizationLevel == 8) && this.flags[0] && constraintWidget2.mVerticalChainFixedPosition && !b2 && !b3 && constraintWidget.mVerticalChainStyle == 0) {
                        Optimizer.applyDirectResolutionVerticalChain(this, linearSystem2, countMatchConstraintsChainedWidgets, constraintWidget2);
                    }
                    else if (countMatchConstraintsChainedWidgets != 0 && !b2) {
                        ConstraintWidget constraintWidget3 = null;
                        float n = 0.0f;
                        while (mVerticalNextWidget != null) {
                            if (mVerticalNextWidget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                                int margin = mVerticalNextWidget.mTop.getMargin();
                                if (constraintWidget3 != null) {
                                    margin += constraintWidget3.mBottom.getMargin();
                                }
                                int n2 = 3;
                                if (mVerticalNextWidget.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n2 = 2;
                                }
                                linearSystem2.addGreaterThan(mVerticalNextWidget.mTop.mSolverVariable, mVerticalNextWidget.mTop.mTarget.mSolverVariable, margin, n2);
                                int margin2 = mVerticalNextWidget.mBottom.getMargin();
                                if (mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget != null && mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == mVerticalNextWidget) {
                                    margin2 += mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.getMargin();
                                }
                                int n3 = 3;
                                if (mVerticalNextWidget.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n3 = 2;
                                }
                                linearSystem2.addLowerThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mBottom.mTarget.mSolverVariable, -margin2, n3);
                            }
                            else {
                                n += mVerticalNextWidget.mVerticalWeight;
                                int margin3 = 0;
                                if (mVerticalNextWidget.mBottom.mTarget != null) {
                                    margin3 = mVerticalNextWidget.mBottom.getMargin();
                                    if (mVerticalNextWidget != this.mChainEnds[3]) {
                                        margin3 += mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.getMargin();
                                    }
                                }
                                linearSystem2.addGreaterThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mTop.mSolverVariable, 0, 1);
                                linearSystem2.addLowerThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mBottom.mTarget.mSolverVariable, -margin3, 1);
                            }
                            constraintWidget3 = mVerticalNextWidget;
                            mVerticalNextWidget = mVerticalNextWidget.mVerticalNextWidget;
                        }
                        if (countMatchConstraintsChainedWidgets == 1) {
                            final ConstraintWidget constraintWidget4 = this.mMatchConstraintsChainedWidgets[0];
                            int margin4 = constraintWidget4.mTop.getMargin();
                            if (constraintWidget4.mTop.mTarget != null) {
                                margin4 += constraintWidget4.mTop.mTarget.getMargin();
                            }
                            int margin5 = constraintWidget4.mBottom.getMargin();
                            if (constraintWidget4.mBottom.mTarget != null) {
                                margin5 += constraintWidget4.mBottom.mTarget.getMargin();
                            }
                            SolverVariable solverVariable = constraintWidget2.mBottom.mTarget.mSolverVariable;
                            final ConstraintWidget[] mChainEnds = this.mChainEnds;
                            if (constraintWidget4 == mChainEnds[3]) {
                                solverVariable = mChainEnds[1].mBottom.mTarget.mSolverVariable;
                            }
                            if (constraintWidget4.mMatchConstraintDefaultHeight == 1) {
                                linearSystem2.addGreaterThan(constraintWidget2.mTop.mSolverVariable, constraintWidget2.mTop.mTarget.mSolverVariable, margin4, 1);
                                linearSystem2.addLowerThan(constraintWidget2.mBottom.mSolverVariable, solverVariable, -margin5, 1);
                                linearSystem2.addEquality(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mTop.mSolverVariable, constraintWidget2.getHeight(), 2);
                            }
                            else {
                                linearSystem2.addEquality(constraintWidget4.mTop.mSolverVariable, constraintWidget4.mTop.mTarget.mSolverVariable, margin4, 1);
                                linearSystem2.addEquality(constraintWidget4.mBottom.mSolverVariable, solverVariable, -margin5, 1);
                            }
                        }
                        else {
                            for (int j = 0, n4 = countMatchConstraintsChainedWidgets; j < n4 - 1; ++j) {
                                final ConstraintWidget[] mMatchConstraintsChainedWidgets = this.mMatchConstraintsChainedWidgets;
                                final ConstraintWidget constraintWidget5 = mMatchConstraintsChainedWidgets[j];
                                final ConstraintWidget constraintWidget6 = mMatchConstraintsChainedWidgets[j + 1];
                                final SolverVariable mSolverVariable = constraintWidget5.mTop.mSolverVariable;
                                final SolverVariable mSolverVariable2 = constraintWidget5.mBottom.mSolverVariable;
                                final SolverVariable mSolverVariable3 = constraintWidget6.mTop.mSolverVariable;
                                SolverVariable solverVariable2 = constraintWidget6.mBottom.mSolverVariable;
                                final ConstraintWidget[] mChainEnds2 = this.mChainEnds;
                                if (constraintWidget6 == mChainEnds2[3]) {
                                    solverVariable2 = mChainEnds2[1].mBottom.mSolverVariable;
                                }
                                int margin6 = constraintWidget5.mTop.getMargin();
                                if (constraintWidget5.mTop.mTarget != null && constraintWidget5.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget5.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget5) {
                                    margin6 += constraintWidget5.mTop.mTarget.mOwner.mBottom.getMargin();
                                }
                                linearSystem2.addGreaterThan(mSolverVariable, constraintWidget5.mTop.mTarget.mSolverVariable, margin6, 2);
                                final int margin7 = constraintWidget5.mBottom.getMargin();
                                int n5;
                                if (constraintWidget5.mBottom.mTarget != null && constraintWidget5.mVerticalNextWidget != null) {
                                    int margin8;
                                    if (constraintWidget5.mVerticalNextWidget.mTop.mTarget != null) {
                                        margin8 = constraintWidget5.mVerticalNextWidget.mTop.getMargin();
                                    }
                                    else {
                                        margin8 = 0;
                                    }
                                    n5 = margin7 + margin8;
                                }
                                else {
                                    n5 = margin7;
                                }
                                linearSystem2.addLowerThan(mSolverVariable2, constraintWidget5.mBottom.mTarget.mSolverVariable, -n5, 2);
                                if (j + 1 == n4 - 1) {
                                    int margin9 = constraintWidget6.mTop.getMargin();
                                    if (constraintWidget6.mTop.mTarget != null && constraintWidget6.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget6.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget6) {
                                        margin9 += constraintWidget6.mTop.mTarget.mOwner.mBottom.getMargin();
                                    }
                                    linearSystem2.addGreaterThan(mSolverVariable3, constraintWidget6.mTop.mTarget.mSolverVariable, margin9, 2);
                                    ConstraintAnchor constraintAnchor = constraintWidget6.mBottom;
                                    final ConstraintWidget[] mChainEnds3 = this.mChainEnds;
                                    if (constraintWidget6 == mChainEnds3[3]) {
                                        constraintAnchor = mChainEnds3[1].mBottom;
                                    }
                                    int margin10 = constraintAnchor.getMargin();
                                    if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner.mTop.mTarget != null && constraintAnchor.mTarget.mOwner.mTop.mTarget.mOwner == constraintWidget6) {
                                        margin10 += constraintAnchor.mTarget.mOwner.mTop.getMargin();
                                    }
                                    linearSystem2.addLowerThan(solverVariable2, constraintAnchor.mTarget.mSolverVariable, -margin10, 2);
                                }
                                if (constraintWidget2.mMatchConstraintMaxHeight > 0) {
                                    linearSystem2.addLowerThan(mSolverVariable2, mSolverVariable, constraintWidget2.mMatchConstraintMaxHeight, 2);
                                }
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualDimension(constraintWidget5.mVerticalWeight, n, constraintWidget6.mVerticalWeight, mSolverVariable, constraintWidget5.mTop.getMargin(), mSolverVariable2, constraintWidget5.mBottom.getMargin(), mSolverVariable3, constraintWidget6.mTop.getMargin(), solverVariable2, constraintWidget6.mBottom.getMargin());
                                linearSystem2.addConstraint(row);
                            }
                        }
                    }
                    else {
                        boolean b4 = false;
                        ConstraintWidget constraintWidget7 = null;
                        ConstraintWidget constraintWidget8 = null;
                        final LinearSystem linearSystem3 = linearSystem2;
                        final boolean b5 = b;
                        ConstraintWidget constraintWidget9 = mVerticalNextWidget;
                        SolverVariable solverVariable3;
                        while (true) {
                            final ConstraintWidget constraintWidget10 = constraintWidget9;
                            solverVariable3 = null;
                            final ConstraintWidget constraintWidget11 = null;
                            if (constraintWidget10 == null) {
                                break;
                            }
                            final ConstraintWidget mVerticalNextWidget3 = constraintWidget10.mVerticalNextWidget;
                            if (mVerticalNextWidget3 == null) {
                                constraintWidget7 = this.mChainEnds[1];
                                b4 = true;
                            }
                            ConstraintWidget mOwner = null;
                            Label_2452: {
                                if (b2) {
                                    final ConstraintAnchor mTop = constraintWidget10.mTop;
                                    int margin11 = mTop.getMargin();
                                    if (constraintWidget8 != null) {
                                        margin11 += constraintWidget8.mBottom.getMargin();
                                    }
                                    int n6 = 1;
                                    if (mVerticalNextWidget != constraintWidget10) {
                                        n6 = 3;
                                    }
                                    SolverVariable solverVariable4 = null;
                                    SolverVariable solverVariable5 = null;
                                    if (mTop.mTarget != null) {
                                        solverVariable4 = mTop.mSolverVariable;
                                        solverVariable5 = mTop.mTarget.mSolverVariable;
                                    }
                                    else if (constraintWidget10.mBaseline.mTarget != null) {
                                        solverVariable4 = constraintWidget10.mBaseline.mSolverVariable;
                                        solverVariable5 = constraintWidget10.mBaseline.mTarget.mSolverVariable;
                                        margin11 -= mTop.getMargin();
                                    }
                                    if (solverVariable4 != null && solverVariable5 != null) {
                                        linearSystem3.addGreaterThan(solverVariable4, solverVariable5, margin11, n6);
                                    }
                                    if (constraintWidget10.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                        final ConstraintAnchor mBottom = constraintWidget10.mBottom;
                                        if (constraintWidget10.mMatchConstraintDefaultHeight == 1) {
                                            linearSystem3.addEquality(mBottom.mSolverVariable, mTop.mSolverVariable, Math.max(constraintWidget10.mMatchConstraintMinHeight, constraintWidget10.getHeight()), 3);
                                        }
                                        else {
                                            linearSystem3.addGreaterThan(mTop.mSolverVariable, mTop.mTarget.mSolverVariable, mTop.mMargin, 3);
                                            linearSystem3.addLowerThan(mBottom.mSolverVariable, mTop.mSolverVariable, constraintWidget10.mMatchConstraintMinHeight, 3);
                                        }
                                    }
                                }
                                else if (!b5 && b4 && constraintWidget8 != null) {
                                    if (constraintWidget10.mBottom.mTarget == null) {
                                        linearSystem3.addEquality(constraintWidget10.mBottom.mSolverVariable, constraintWidget10.getDrawBottom());
                                    }
                                    else {
                                        linearSystem3.addEquality(constraintWidget10.mBottom.mSolverVariable, constraintWidget7.mBottom.mTarget.mSolverVariable, -constraintWidget10.mBottom.getMargin(), 5);
                                    }
                                }
                                else if (!b5 && !b4 && constraintWidget8 == null) {
                                    if (constraintWidget10.mTop.mTarget == null) {
                                        linearSystem3.addEquality(constraintWidget10.mTop.mSolverVariable, constraintWidget10.getDrawY());
                                    }
                                    else {
                                        linearSystem3.addEquality(constraintWidget10.mTop.mSolverVariable, constraintWidget.mTop.mTarget.mSolverVariable, constraintWidget10.mTop.getMargin(), 5);
                                    }
                                }
                                else {
                                    final ConstraintAnchor mTop2 = constraintWidget10.mTop;
                                    final ConstraintAnchor mBottom2 = constraintWidget10.mBottom;
                                    final int margin12 = mTop2.getMargin();
                                    final int margin13 = mBottom2.getMargin();
                                    linearSystem3.addGreaterThan(mTop2.mSolverVariable, mTop2.mTarget.mSolverVariable, margin12, 1);
                                    linearSystem3.addLowerThan(mBottom2.mSolverVariable, mBottom2.mTarget.mSolverVariable, -margin13, 1);
                                    SolverVariable mSolverVariable4;
                                    if (mTop2.mTarget != null) {
                                        mSolverVariable4 = mTop2.mTarget.mSolverVariable;
                                    }
                                    else {
                                        mSolverVariable4 = null;
                                    }
                                    SolverVariable solverVariable6;
                                    if (constraintWidget8 == null) {
                                        SolverVariable mSolverVariable5;
                                        if (constraintWidget.mTop.mTarget != null) {
                                            mSolverVariable5 = constraintWidget.mTop.mTarget.mSolverVariable;
                                        }
                                        else {
                                            mSolverVariable5 = null;
                                        }
                                        solverVariable6 = mSolverVariable5;
                                    }
                                    else {
                                        solverVariable6 = mSolverVariable4;
                                    }
                                    if (mVerticalNextWidget3 == null) {
                                        if (constraintWidget7.mBottom.mTarget != null) {
                                            mOwner = constraintWidget7.mBottom.mTarget.mOwner;
                                        }
                                        else {
                                            mOwner = null;
                                        }
                                    }
                                    else {
                                        mOwner = mVerticalNextWidget3;
                                    }
                                    if (mOwner == null) {
                                        break Label_2452;
                                    }
                                    SolverVariable solverVariable7 = mOwner.mTop.mSolverVariable;
                                    if (b4) {
                                        if (constraintWidget7.mBottom.mTarget != null) {
                                            solverVariable7 = constraintWidget7.mBottom.mTarget.mSolverVariable;
                                        }
                                        else {
                                            solverVariable7 = null;
                                        }
                                    }
                                    if (solverVariable6 != null && solverVariable7 != null) {
                                        linearSystem.addCentering(mTop2.mSolverVariable, solverVariable6, margin12, 0.5f, solverVariable7, mBottom2.mSolverVariable, margin13, 4);
                                    }
                                    break Label_2452;
                                }
                                mOwner = mVerticalNextWidget3;
                            }
                            ConstraintWidget constraintWidget12;
                            if (b4) {
                                constraintWidget12 = constraintWidget11;
                            }
                            else {
                                constraintWidget12 = mOwner;
                            }
                            constraintWidget9 = constraintWidget12;
                            constraintWidget8 = constraintWidget10;
                        }
                        linearSystem2 = linearSystem3;
                        if (b2) {
                            final ConstraintAnchor mTop3 = mVerticalNextWidget.mTop;
                            final ConstraintAnchor mBottom3 = constraintWidget7.mBottom;
                            final int margin14 = mTop3.getMargin();
                            final int margin15 = mBottom3.getMargin();
                            SolverVariable mSolverVariable6;
                            if (constraintWidget.mTop.mTarget != null) {
                                mSolverVariable6 = constraintWidget.mTop.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable6 = null;
                            }
                            SolverVariable mSolverVariable7 = solverVariable3;
                            if (constraintWidget7.mBottom.mTarget != null) {
                                mSolverVariable7 = constraintWidget7.mBottom.mTarget.mSolverVariable;
                            }
                            if (mSolverVariable6 != null && mSolverVariable7 != null) {
                                linearSystem2.addLowerThan(mBottom3.mSolverVariable, mSolverVariable7, -margin15, 1);
                                linearSystem.addCentering(mTop3.mSolverVariable, mSolverVariable6, margin14, constraintWidget.mVerticalBiasPercent, mSolverVariable7, mBottom3.mSolverVariable, margin15, 4);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private int countMatchConstraintsChainedWidgets(final LinearSystem linearSystem, final ConstraintWidget[] array, final ConstraintWidget constraintWidget, int n, final boolean[] array2) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        final int n2 = 0;
        final int n3 = 0;
        array2[0] = true;
        array2[1] = false;
        array[2] = (array[0] = null);
        array[3] = (array[1] = null);
        if (n == 0) {
            boolean mHorizontalChainFixedPosition = true;
            ConstraintWidget mOwner = null;
            if (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mLeft.mTarget.mOwner != this) {
                mHorizontalChainFixedPosition = false;
            }
            constraintWidget2.mHorizontalNextWidget = null;
            ConstraintWidget constraintWidget3 = null;
            if (constraintWidget.getVisibility() != 8) {
                constraintWidget3 = constraintWidget;
            }
            ConstraintWidget constraintWidget4 = constraintWidget3;
            n = n3;
            while (constraintWidget2.mRight.mTarget != null) {
                constraintWidget2.mHorizontalNextWidget = null;
                if (constraintWidget2.getVisibility() != 8) {
                    if (constraintWidget3 == null) {
                        constraintWidget3 = constraintWidget2;
                    }
                    if (constraintWidget4 != null && constraintWidget4 != constraintWidget2) {
                        constraintWidget4.mHorizontalNextWidget = constraintWidget2;
                    }
                    constraintWidget4 = constraintWidget2;
                }
                else {
                    linearSystem.addEquality(constraintWidget2.mLeft.mSolverVariable, constraintWidget2.mLeft.mTarget.mSolverVariable, 0, 5);
                    linearSystem.addEquality(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mLeft.mSolverVariable, 0, 5);
                }
                if (constraintWidget2.getVisibility() != 8 && constraintWidget2.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (constraintWidget2.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        array2[0] = false;
                    }
                    if (constraintWidget2.mDimensionRatio <= 0.0f) {
                        array2[0] = false;
                        final ConstraintWidget[] mMatchConstraintsChainedWidgets = this.mMatchConstraintsChainedWidgets;
                        if (n + 1 >= mMatchConstraintsChainedWidgets.length) {
                            this.mMatchConstraintsChainedWidgets = Arrays.copyOf(mMatchConstraintsChainedWidgets, mMatchConstraintsChainedWidgets.length * 2);
                        }
                        this.mMatchConstraintsChainedWidgets[n] = constraintWidget2;
                        ++n;
                    }
                }
                if (constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget == null) {
                    break;
                }
                if (constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != constraintWidget2) {
                    break;
                }
                if (constraintWidget2.mRight.mTarget.mOwner == constraintWidget2) {
                    break;
                }
                constraintWidget2 = (mOwner = constraintWidget2.mRight.mTarget.mOwner);
            }
            if (constraintWidget2.mRight.mTarget != null && constraintWidget2.mRight.mTarget.mOwner != this) {
                mHorizontalChainFixedPosition = false;
            }
            if (constraintWidget.mLeft.mTarget == null || mOwner.mRight.mTarget == null) {
                array2[1] = true;
            }
            constraintWidget.mHorizontalChainFixedPosition = mHorizontalChainFixedPosition;
            mOwner.mHorizontalNextWidget = null;
            array[0] = constraintWidget;
            array[2] = constraintWidget3;
            array[1] = mOwner;
            array[3] = constraintWidget4;
            return n;
        }
        boolean mVerticalChainFixedPosition = true;
        ConstraintWidget mOwner2 = null;
        if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mTop.mTarget.mOwner != this) {
            mVerticalChainFixedPosition = false;
        }
        constraintWidget2.mVerticalNextWidget = null;
        ConstraintWidget constraintWidget5 = null;
        if (constraintWidget.getVisibility() != 8) {
            constraintWidget5 = constraintWidget;
        }
        ConstraintWidget constraintWidget6 = constraintWidget5;
        n = n2;
        while (constraintWidget2.mBottom.mTarget != null) {
            constraintWidget2.mVerticalNextWidget = null;
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget5 == null) {
                    constraintWidget5 = constraintWidget2;
                }
                if (constraintWidget6 != null && constraintWidget6 != constraintWidget2) {
                    constraintWidget6.mVerticalNextWidget = constraintWidget2;
                }
                constraintWidget6 = constraintWidget2;
            }
            else {
                linearSystem.addEquality(constraintWidget2.mTop.mSolverVariable, constraintWidget2.mTop.mTarget.mSolverVariable, 0, 5);
                linearSystem.addEquality(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mTop.mSolverVariable, 0, 5);
            }
            if (constraintWidget2.getVisibility() != 8 && constraintWidget2.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (constraintWidget2.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    array2[0] = false;
                }
                if (constraintWidget2.mDimensionRatio <= 0.0f) {
                    array2[0] = false;
                    final ConstraintWidget[] mMatchConstraintsChainedWidgets2 = this.mMatchConstraintsChainedWidgets;
                    if (n + 1 >= mMatchConstraintsChainedWidgets2.length) {
                        this.mMatchConstraintsChainedWidgets = Arrays.copyOf(mMatchConstraintsChainedWidgets2, mMatchConstraintsChainedWidgets2.length * 2);
                    }
                    this.mMatchConstraintsChainedWidgets[n] = constraintWidget2;
                    ++n;
                }
            }
            if (constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget == null) {
                break;
            }
            if (constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != constraintWidget2) {
                break;
            }
            if (constraintWidget2.mBottom.mTarget.mOwner == constraintWidget2) {
                break;
            }
            constraintWidget2 = (mOwner2 = constraintWidget2.mBottom.mTarget.mOwner);
        }
        if (constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.mOwner != this) {
            mVerticalChainFixedPosition = false;
        }
        if (constraintWidget.mTop.mTarget == null || mOwner2.mBottom.mTarget == null) {
            array2[1] = true;
        }
        constraintWidget.mVerticalChainFixedPosition = mVerticalChainFixedPosition;
        mOwner2.mVerticalNextWidget = null;
        array[0] = constraintWidget;
        array[2] = constraintWidget5;
        array[1] = mOwner2;
        array[3] = constraintWidget6;
        return n;
    }
    
    public static ConstraintWidgetContainer createContainer(final ConstraintWidgetContainer constraintWidgetContainer, final String debugName, final ArrayList<ConstraintWidget> list, int i) {
        final Rectangle bounds = WidgetContainer.getBounds(list);
        if (bounds.width != 0 && bounds.height != 0) {
            if (i > 0) {
                final int min = Math.min(bounds.x, bounds.y);
                if (i > min) {
                    i = min;
                }
                bounds.grow(i, i);
            }
            constraintWidgetContainer.setOrigin(bounds.x, bounds.y);
            constraintWidgetContainer.setDimension(bounds.width, bounds.height);
            constraintWidgetContainer.setDebugName(debugName);
            final ConstraintWidget parent = list.get(0).getParent();
            ConstraintWidget constraintWidget;
            for (i = 0; i < list.size(); ++i) {
                constraintWidget = list.get(i);
                if (constraintWidget.getParent() == parent) {
                    constraintWidgetContainer.add(constraintWidget);
                    constraintWidget.setX(constraintWidget.getX() - bounds.x);
                    constraintWidget.setY(constraintWidget.getY() - bounds.y);
                }
            }
            return constraintWidgetContainer;
        }
        return null;
    }
    
    private boolean optimize(final LinearSystem linearSystem) {
        final int size = this.mChildren.size();
        final boolean b = false;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        int n4 = 0;
        int n5;
        int n6;
        int n7;
        int n8;
        while (true) {
            n5 = (b ? 1 : 0);
            n6 = n;
            n7 = n2;
            n8 = n3;
            if (n4 >= size) {
                break;
            }
            final ConstraintWidget constraintWidget = this.mChildren.get(n4);
            constraintWidget.mHorizontalResolution = -1;
            constraintWidget.mVerticalResolution = -1;
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mHorizontalResolution = 1;
                constraintWidget.mVerticalResolution = 1;
            }
            ++n4;
        }
        while (true) {
            final int n9 = n7;
            final int n10 = n6;
            if (n5 != 0) {
                break;
            }
            n6 = 0;
            n7 = 0;
            final int n11 = n8 + 1;
            for (int i = 0; i < size; ++i) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(i);
                if (constraintWidget2.mHorizontalResolution == -1) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mHorizontalResolution = 1;
                    }
                    else {
                        Optimizer.checkHorizontalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mVerticalResolution = 1;
                    }
                    else {
                        Optimizer.checkVerticalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    ++n6;
                }
                if (constraintWidget2.mHorizontalResolution == -1) {
                    ++n7;
                }
            }
            if (n6 == 0 && n7 == 0) {
                n5 = 1;
            }
            else if (n10 == n6 && n9 == n7) {
                n5 = 1;
            }
            n8 = n11;
        }
        int n12 = 0;
        int n13 = 0;
        for (int j = 0; j < size; ++j) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(j);
            if (constraintWidget3.mHorizontalResolution == 1 || constraintWidget3.mHorizontalResolution == -1) {
                ++n12;
            }
            if (constraintWidget3.mVerticalResolution == 1 || constraintWidget3.mVerticalResolution == -1) {
                ++n13;
            }
        }
        return n12 == 0 && n13 == 0;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    static int setGroup(final ConstraintAnchor constraintAnchor, int n) {
        final int mGroup = constraintAnchor.mGroup;
        if (constraintAnchor.mOwner.getParent() == null) {
            return n;
        }
        if (mGroup <= n) {
            return mGroup;
        }
        constraintAnchor.mGroup = n;
        final ConstraintAnchor opposite = constraintAnchor.getOpposite();
        final ConstraintAnchor mTarget = constraintAnchor.mTarget;
        if (opposite != null) {
            n = setGroup(opposite, n);
        }
        if (mTarget != null) {
            n = setGroup(mTarget, n);
        }
        if (opposite != null) {
            n = setGroup(opposite, n);
        }
        return constraintAnchor.mGroup = n;
    }
    
    void addChain(ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            while (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget == constraintWidget.mLeft && constraintWidget.mLeft.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mLeft.mTarget.mOwner;
            }
            this.addHorizontalChain(constraintWidget);
            return;
        }
        if (n == 1) {
            while (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget == constraintWidget.mTop && constraintWidget.mTop.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mTop.mTarget.mOwner;
            }
            this.addVerticalChain(constraintWidget);
        }
    }
    
    public boolean addChildrenToSolver(final LinearSystem linearSystem, final int n) {
        this.addToSolver(linearSystem, n);
        final int size = this.mChildren.size();
        boolean b = false;
        final int mOptimizationLevel = this.mOptimizationLevel;
        if (mOptimizationLevel != 2 && mOptimizationLevel != 4) {
            b = true;
        }
        else if (this.optimize(linearSystem)) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour mHorizontalDimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
                final DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem, n);
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(mHorizontalDimensionBehaviour);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(mVerticalDimensionBehaviour);
                }
            }
            else {
                if (b) {
                    Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                }
                constraintWidget.addToSolver(linearSystem, n);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            this.applyHorizontalChain(linearSystem);
        }
        if (this.mVerticalChainsSize > 0) {
            this.applyVerticalChain(linearSystem);
        }
        return true;
    }
    
    public void findHorizontalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final DimensionBehaviour mHorizontalDimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
        final DimensionBehaviour match_CONSTRAINT = DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean b = false;
        if (mHorizontalDimensionBehaviour == match_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        int mDistToLeft = constraintWidget.getOptimizerWrapWidth();
        if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f)) {
            array[0] = false;
            return;
        }
        int relativeEnd = mDistToLeft;
        final ConstraintWidget constraintWidget2 = null;
        ConstraintWidget mOwner = null;
        constraintWidget.mHorizontalWrapVisited = true;
        if (constraintWidget instanceof Guideline) {
            final Guideline guideline = (Guideline)constraintWidget;
            if (guideline.getOrientation() == 1) {
                mDistToLeft = 0;
                relativeEnd = 0;
                if (guideline.getRelativeBegin() != -1) {
                    mDistToLeft = guideline.getRelativeBegin();
                }
                else if (guideline.getRelativeEnd() != -1) {
                    relativeEnd = guideline.getRelativeEnd();
                }
            }
        }
        else if (!constraintWidget.mRight.isConnected() && !constraintWidget.mLeft.isConnected()) {
            mDistToLeft += constraintWidget.getX();
        }
        else {
            if (constraintWidget.mRight.mTarget != null && constraintWidget.mLeft.mTarget != null && (constraintWidget.mRight.mTarget == constraintWidget.mLeft.mTarget || (constraintWidget.mRight.mTarget.mOwner == constraintWidget.mLeft.mTarget.mOwner && constraintWidget.mRight.mTarget.mOwner != constraintWidget.mParent))) {
                array[0] = false;
                return;
            }
            if (constraintWidget.mRight.mTarget != null) {
                mOwner = constraintWidget.mRight.mTarget.mOwner;
                relativeEnd += constraintWidget.mRight.getMargin();
                if (!mOwner.isRoot() && !mOwner.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive(mOwner, array);
                }
            }
            ConstraintWidget constraintWidget3;
            if (constraintWidget.mLeft.mTarget != null) {
                final ConstraintWidget mOwner2 = constraintWidget.mLeft.mTarget.mOwner;
                mDistToLeft += constraintWidget.mLeft.getMargin();
                if (!mOwner2.isRoot() && !mOwner2.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive(mOwner2, array);
                    constraintWidget3 = mOwner2;
                }
                else {
                    constraintWidget3 = mOwner2;
                }
            }
            else {
                constraintWidget3 = constraintWidget2;
            }
            if (constraintWidget.mRight.mTarget != null && !mOwner.isRoot()) {
                if (constraintWidget.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                    relativeEnd += mOwner.mDistToRight - mOwner.getOptimizerWrapWidth();
                }
                else if (constraintWidget.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                    relativeEnd += mOwner.mDistToRight;
                }
                constraintWidget.mRightHasCentered = (mOwner.mRightHasCentered || (mOwner.mLeft.mTarget != null && mOwner.mRight.mTarget != null && mOwner.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                Label_0603: {
                    if (constraintWidget.mRightHasCentered) {
                        if (mOwner.mLeft.mTarget != null) {
                            if (mOwner.mLeft.mTarget.mOwner == constraintWidget) {
                                break Label_0603;
                            }
                        }
                        relativeEnd += relativeEnd - mOwner.mDistToRight;
                    }
                }
            }
            if (constraintWidget.mLeft.mTarget != null && !constraintWidget3.isRoot()) {
                if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                    mDistToLeft += constraintWidget3.mDistToLeft - constraintWidget3.getOptimizerWrapWidth();
                }
                else if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                    mDistToLeft += constraintWidget3.mDistToLeft;
                }
                constraintWidget.mLeftHasCentered = (constraintWidget3.mLeftHasCentered || (constraintWidget3.mLeft.mTarget != null && constraintWidget3.mRight.mTarget != null && constraintWidget3.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) || b);
                Label_0782: {
                    if (constraintWidget.mLeftHasCentered) {
                        if (constraintWidget3.mRight.mTarget != null) {
                            if (constraintWidget3.mRight.mTarget.mOwner == constraintWidget) {
                                break Label_0782;
                            }
                        }
                        mDistToLeft += mDistToLeft - constraintWidget3.mDistToLeft;
                    }
                }
            }
        }
        if (constraintWidget.getVisibility() == 8) {
            mDistToLeft -= constraintWidget.mWidth;
            relativeEnd -= constraintWidget.mWidth;
        }
        constraintWidget.mDistToLeft = mDistToLeft;
        constraintWidget.mDistToRight = relativeEnd;
    }
    
    public void findVerticalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
        final DimensionBehaviour match_CONSTRAINT = DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean b = false;
        if (mVerticalDimensionBehaviour == match_CONSTRAINT && (constraintWidget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f)) {
            array[0] = false;
            return;
        }
        int mDistToTop;
        int relativeEnd;
        final int n = relativeEnd = (mDistToTop = constraintWidget.getOptimizerWrapHeight());
        ConstraintWidget owner = null;
        final ConstraintWidget constraintWidget2 = null;
        constraintWidget.mVerticalWrapVisited = true;
        if (constraintWidget instanceof Guideline) {
            final Guideline guideline = (Guideline)constraintWidget;
            if (guideline.getOrientation() == 0) {
                mDistToTop = 0;
                relativeEnd = 0;
                if (guideline.getRelativeBegin() != -1) {
                    mDistToTop = guideline.getRelativeBegin();
                }
                else if (guideline.getRelativeEnd() != -1) {
                    relativeEnd = guideline.getRelativeEnd();
                }
            }
        }
        else if (constraintWidget.mBaseline.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
            mDistToTop += constraintWidget.getY();
        }
        else {
            if (constraintWidget.mBottom.mTarget != null && constraintWidget.mTop.mTarget != null && (constraintWidget.mBottom.mTarget == constraintWidget.mTop.mTarget || (constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mTop.mTarget.mOwner && constraintWidget.mBottom.mTarget.mOwner != constraintWidget.mParent))) {
                array[0] = false;
                return;
            }
            if (constraintWidget.mBaseline.isConnected()) {
                final ConstraintWidget owner2 = constraintWidget.mBaseline.mTarget.getOwner();
                if (!owner2.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(owner2, array);
                }
                int max = Math.max(owner2.mDistToTop - owner2.mHeight + n, n);
                int max2 = Math.max(owner2.mDistToBottom - owner2.mHeight + n, n);
                if (constraintWidget.getVisibility() == 8) {
                    max -= constraintWidget.mHeight;
                    max2 -= constraintWidget.mHeight;
                }
                constraintWidget.mDistToTop = max;
                constraintWidget.mDistToBottom = max2;
                return;
            }
            if (constraintWidget.mTop.isConnected()) {
                owner = constraintWidget.mTop.mTarget.getOwner();
                mDistToTop += constraintWidget.mTop.getMargin();
                if (!owner.isRoot() && !owner.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(owner, array);
                }
            }
            ConstraintWidget constraintWidget3;
            if (constraintWidget.mBottom.isConnected()) {
                final ConstraintWidget owner3 = constraintWidget.mBottom.mTarget.getOwner();
                relativeEnd += constraintWidget.mBottom.getMargin();
                if (!owner3.isRoot() && !owner3.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(owner3, array);
                    constraintWidget3 = owner3;
                }
                else {
                    constraintWidget3 = owner3;
                }
            }
            else {
                constraintWidget3 = constraintWidget2;
            }
            if (constraintWidget.mTop.mTarget != null && !owner.isRoot()) {
                if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                    mDistToTop += owner.mDistToTop - owner.getOptimizerWrapHeight();
                }
                else if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                    mDistToTop += owner.mDistToTop;
                }
                constraintWidget.mTopHasCentered = (owner.mTopHasCentered || (owner.mTop.mTarget != null && owner.mTop.mTarget.mOwner != constraintWidget && owner.mBottom.mTarget != null && owner.mBottom.mTarget.mOwner != constraintWidget && owner.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                Label_0736: {
                    if (constraintWidget.mTopHasCentered) {
                        if (owner.mBottom.mTarget != null) {
                            if (owner.mBottom.mTarget.mOwner == constraintWidget) {
                                break Label_0736;
                            }
                        }
                        mDistToTop += mDistToTop - owner.mDistToTop;
                    }
                }
            }
            if (constraintWidget.mBottom.mTarget != null && !constraintWidget3.isRoot()) {
                if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                    relativeEnd += constraintWidget3.mDistToBottom - constraintWidget3.getOptimizerWrapHeight();
                }
                else if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                    relativeEnd += constraintWidget3.mDistToBottom;
                }
                constraintWidget.mBottomHasCentered = (constraintWidget3.mBottomHasCentered || (constraintWidget3.mTop.mTarget != null && constraintWidget3.mTop.mTarget.mOwner != constraintWidget && constraintWidget3.mBottom.mTarget != null && constraintWidget3.mBottom.mTarget.mOwner != constraintWidget && constraintWidget3.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) || b);
                Label_0943: {
                    if (constraintWidget.mBottomHasCentered) {
                        if (constraintWidget3.mTop.mTarget != null) {
                            if (constraintWidget3.mTop.mTarget.mOwner == constraintWidget) {
                                break Label_0943;
                            }
                        }
                        relativeEnd += relativeEnd - constraintWidget3.mDistToBottom;
                    }
                }
            }
        }
        if (constraintWidget.getVisibility() == 8) {
            mDistToTop -= constraintWidget.mHeight;
            relativeEnd -= constraintWidget.mHeight;
        }
        constraintWidget.mDistToTop = mDistToTop;
        constraintWidget.mDistToBottom = relativeEnd;
    }
    
    public void findWrapSize(final ArrayList<ConstraintWidget> list, final boolean[] array) {
        int max = 0;
        int max2 = 0;
        int max3 = 0;
        int max4 = 0;
        int max5 = 0;
        int max6 = 0;
        final int size = list.size();
        array[0] = true;
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (!constraintWidget.isRoot()) {
                if (!constraintWidget.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive(constraintWidget, array);
                }
                if (!constraintWidget.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(constraintWidget, array);
                }
                if (!array[0]) {
                    return;
                }
                int n = constraintWidget.mDistToLeft + constraintWidget.mDistToRight - constraintWidget.getWidth();
                int n2 = constraintWidget.mDistToTop + constraintWidget.mDistToBottom - constraintWidget.getHeight();
                if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    n = constraintWidget.getWidth() + constraintWidget.mLeft.mMargin + constraintWidget.mRight.mMargin;
                }
                if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    n2 = constraintWidget.getHeight() + constraintWidget.mTop.mMargin + constraintWidget.mBottom.mMargin;
                }
                if (constraintWidget.getVisibility() == 8) {
                    n = 0;
                    n2 = 0;
                }
                max2 = Math.max(max2, constraintWidget.mDistToLeft);
                max3 = Math.max(max3, constraintWidget.mDistToRight);
                max4 = Math.max(max4, constraintWidget.mDistToBottom);
                max = Math.max(max, constraintWidget.mDistToTop);
                max5 = Math.max(max5, n);
                max6 = Math.max(max6, n2);
            }
        }
        this.mWrapWidth = Math.max(this.mMinWidth, Math.max(Math.max(max2, max3), max5));
        this.mWrapHeight = Math.max(this.mMinHeight, Math.max(Math.max(max, max4), max6));
        for (int j = 0; j < size; ++j) {
            final ConstraintWidget constraintWidget2 = list.get(j);
            constraintWidget2.mHorizontalWrapVisited = false;
            constraintWidget2.mVerticalWrapVisited = false;
            constraintWidget2.mLeftHasCentered = false;
            constraintWidget2.mRightHasCentered = false;
            constraintWidget2.mTopHasCentered = false;
            constraintWidget2.mBottomHasCentered = false;
        }
    }
    
    public ArrayList<Guideline> getHorizontalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 0) {
                    list.add(guideline);
                }
            }
        }
        return list;
    }
    
    public LinearSystem getSystem() {
        return this.mSystem;
    }
    
    @Override
    public String getType() {
        return "ConstraintLayout";
    }
    
    public ArrayList<Guideline> getVerticalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 1) {
                    list.add(guideline);
                }
            }
        }
        return list;
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }
    
    @Override
    public void layout() {
        final int mx = this.mX;
        final int my = this.mY;
        final int max = Math.max(0, this.getWidth());
        final int max2 = Math.max(0, this.getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.setX(this.mPaddingLeft);
            this.setY(this.mPaddingTop);
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        int n = 0;
        final DimensionBehaviour mVerticalDimensionBehaviour = this.mVerticalDimensionBehaviour;
        final DimensionBehaviour mHorizontalDimensionBehaviour = this.mHorizontalDimensionBehaviour;
        if (this.mOptimizationLevel == 2 && (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT || this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)) {
            this.findWrapSize(this.mChildren, this.flags);
            n = (this.flags[0] ? 1 : 0);
            if (max > 0 && max2 > 0 && (this.mWrapWidth > max || this.mWrapHeight > max2)) {
                n = 0;
            }
            if (n != 0) {
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                    if (max > 0 && max < this.mWrapWidth) {
                        this.mWidthMeasuredTooSmall = true;
                        this.setWidth(max);
                    }
                    else {
                        this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
                    }
                }
                if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                    if (max2 > 0 && max2 < this.mWrapHeight) {
                        this.mHeightMeasuredTooSmall = true;
                        this.setHeight(max2);
                    }
                    else {
                        this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
                    }
                }
            }
        }
        this.resetChains();
        final int size = this.mChildren.size();
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
        int j = 1;
        int n2 = 0;
        while (j != 0) {
            final int n3 = n2 + 1;
            int n4 = j;
            try {
                this.mSystem.reset();
                n4 = j;
                final boolean addChildrenToSolver = this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE);
                if (addChildrenToSolver) {
                    n4 = (addChildrenToSolver ? 1 : 0);
                    this.mSystem.minimize();
                }
                n4 = (addChildrenToSolver ? 1 : 0);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (n4 != 0) {
                this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
            }
            else {
                this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);
                for (int k = 0; k < size; ++k) {
                    final ConstraintWidget constraintWidget2 = this.mChildren.get(k);
                    if (constraintWidget2.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.getWidth() < constraintWidget2.getWrapWidth()) {
                        this.flags[2] = true;
                        break;
                    }
                    if (constraintWidget2.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.getHeight() < constraintWidget2.getWrapHeight()) {
                        this.flags[2] = true;
                        break;
                    }
                }
            }
            final boolean b = false;
            boolean b2 = false;
            int n6;
            int n7;
            if (n3 < 8 && this.flags[2]) {
                int max3 = 0;
                int max4 = 0;
                for (int l = 0; l < size; ++l) {
                    final ConstraintWidget constraintWidget3 = this.mChildren.get(l);
                    max3 = Math.max(max3, constraintWidget3.mX + constraintWidget3.getWidth());
                    max4 = Math.max(max4, constraintWidget3.mY + constraintWidget3.getHeight());
                }
                final int max5 = Math.max(this.mMinWidth, max3);
                final int max6 = Math.max(this.mMinHeight, max4);
                int n5;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    if (this.getWidth() < max5) {
                        this.setWidth(max5);
                        this.mHorizontalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                        n5 = 1;
                        b2 = true;
                    }
                    else {
                        n5 = n;
                    }
                }
                else {
                    n5 = n;
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    if (this.getHeight() < max6) {
                        this.setHeight(max6);
                        this.mVerticalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                        n6 = 1;
                        n7 = 1;
                    }
                    else {
                        n7 = (b2 ? 1 : 0);
                        n6 = n5;
                    }
                }
                else {
                    n7 = (b2 ? 1 : 0);
                    n6 = n5;
                }
            }
            else {
                n6 = n;
                n7 = (b ? 1 : 0);
            }
            final int max7 = Math.max(this.mMinWidth, this.getWidth());
            int n8;
            int n9;
            if (max7 > this.getWidth()) {
                this.setWidth(max7);
                this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                n8 = 1;
                n9 = 1;
            }
            else {
                n8 = n6;
                n9 = n7;
            }
            final int max8 = Math.max(this.mMinHeight, this.getHeight());
            int n10;
            if (max8 > this.getHeight()) {
                this.setHeight(max8);
                this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                n10 = 1;
                n9 = 1;
            }
            else {
                n10 = n8;
            }
            int n12;
            int n13;
            if (n10 == 0) {
                int n11;
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && max > 0) {
                    if (this.getWidth() > max) {
                        this.mWidthMeasuredTooSmall = true;
                        n11 = 1;
                        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                        this.setWidth(max);
                        n9 = 1;
                    }
                    else {
                        n11 = n10;
                    }
                }
                else {
                    n11 = n10;
                }
                if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && max2 > 0 && this.getHeight() > max2) {
                    this.mHeightMeasuredTooSmall = true;
                    n12 = 1;
                    this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                    this.setHeight(max2);
                    n13 = 1;
                }
                else {
                    n13 = n9;
                    n12 = n11;
                }
            }
            else {
                final int n14 = n9;
                n12 = n10;
                n13 = n14;
            }
            j = n13;
            n2 = n3;
            n = n12;
        }
        if (this.mParent != null) {
            final int max9 = Math.max(this.mMinWidth, this.getWidth());
            final int max10 = Math.max(this.mMinHeight, this.getHeight());
            this.mSnapshot.applyTo(this);
            this.setWidth(this.mPaddingLeft + max9 + this.mPaddingRight);
            this.setHeight(this.mPaddingTop + max10 + this.mPaddingBottom);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (n != 0) {
            this.mHorizontalDimensionBehaviour = mHorizontalDimensionBehaviour;
            this.mVerticalDimensionBehaviour = mVerticalDimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    public int layoutFindGroups() {
        final ConstraintAnchor.Type[] array = { ConstraintAnchor.Type.LEFT, ConstraintAnchor.Type.RIGHT, ConstraintAnchor.Type.TOP, ConstraintAnchor.Type.BASELINE, ConstraintAnchor.Type.BOTTOM };
        int n = 1;
        final int size = this.mChildren.size();
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            final ConstraintAnchor mLeft = constraintWidget.mLeft;
            if (mLeft.mTarget != null) {
                if (setGroup(mLeft, n) == n) {
                    ++n;
                }
            }
            else {
                mLeft.mGroup = Integer.MAX_VALUE;
            }
            final ConstraintAnchor mTop = constraintWidget.mTop;
            if (mTop.mTarget != null) {
                if (setGroup(mTop, n) == n) {
                    ++n;
                }
            }
            else {
                mTop.mGroup = Integer.MAX_VALUE;
            }
            final ConstraintAnchor mRight = constraintWidget.mRight;
            if (mRight.mTarget != null) {
                if (setGroup(mRight, n) == n) {
                    ++n;
                }
            }
            else {
                mRight.mGroup = Integer.MAX_VALUE;
            }
            final ConstraintAnchor mBottom = constraintWidget.mBottom;
            if (mBottom.mTarget != null) {
                if (setGroup(mBottom, n) == n) {
                    ++n;
                }
            }
            else {
                mBottom.mGroup = Integer.MAX_VALUE;
            }
            final ConstraintAnchor mBaseline = constraintWidget.mBaseline;
            if (mBaseline.mTarget != null) {
                if (setGroup(mBaseline, n) == n) {
                    ++n;
                }
            }
            else {
                mBaseline.mGroup = Integer.MAX_VALUE;
            }
        }
        int j = 1;
        int n2 = 0;
        int n3 = 0;
        while (j != 0) {
            final boolean b = false;
            final int n4 = n2 + 1;
            int k = 0;
            int n5 = b ? 1 : 0;
            while (k < size) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(k);
                for (int l = 0; l < array.length; ++l) {
                    final ConstraintAnchor.Type type = array[l];
                    ConstraintAnchor constraintAnchor = null;
                    switch (type) {
                        case BASELINE: {
                            constraintAnchor = constraintWidget2.mBaseline;
                            break;
                        }
                        case BOTTOM: {
                            constraintAnchor = constraintWidget2.mBottom;
                            break;
                        }
                        case RIGHT: {
                            constraintAnchor = constraintWidget2.mRight;
                            break;
                        }
                        case TOP: {
                            constraintAnchor = constraintWidget2.mTop;
                            break;
                        }
                        case LEFT: {
                            constraintAnchor = constraintWidget2.mLeft;
                            break;
                        }
                    }
                    final ConstraintAnchor mTarget = constraintAnchor.mTarget;
                    if (mTarget != null) {
                        if (mTarget.mOwner.getParent() != null && mTarget.mGroup != constraintAnchor.mGroup) {
                            int n6;
                            if (constraintAnchor.mGroup > mTarget.mGroup) {
                                n6 = mTarget.mGroup;
                            }
                            else {
                                n6 = constraintAnchor.mGroup;
                            }
                            constraintAnchor.mGroup = n6;
                            mTarget.mGroup = n6;
                            ++n3;
                            n5 = 1;
                        }
                        final ConstraintAnchor opposite = mTarget.getOpposite();
                        if (opposite != null && opposite.mGroup != constraintAnchor.mGroup) {
                            int n7;
                            if (constraintAnchor.mGroup > opposite.mGroup) {
                                n7 = opposite.mGroup;
                            }
                            else {
                                n7 = constraintAnchor.mGroup;
                            }
                            constraintAnchor.mGroup = n7;
                            opposite.mGroup = n7;
                            ++n3;
                            n5 = 1;
                        }
                    }
                }
                ++k;
            }
            j = n5;
            n2 = n4;
        }
        int n8 = 0;
        final int[] array2 = new int[this.mChildren.size() * array.length + 1];
        Arrays.fill(array2, -1);
        for (int n9 = 0; n9 < size; ++n9) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(n9);
            final ConstraintAnchor mLeft2 = constraintWidget3.mLeft;
            if (mLeft2.mGroup != Integer.MAX_VALUE) {
                final int mGroup = mLeft2.mGroup;
                if (array2[mGroup] == -1) {
                    array2[mGroup] = n8;
                    ++n8;
                }
                mLeft2.mGroup = array2[mGroup];
            }
            final ConstraintAnchor mTop2 = constraintWidget3.mTop;
            if (mTop2.mGroup != Integer.MAX_VALUE) {
                final int mGroup2 = mTop2.mGroup;
                if (array2[mGroup2] == -1) {
                    array2[mGroup2] = n8;
                    ++n8;
                }
                mTop2.mGroup = array2[mGroup2];
            }
            final ConstraintAnchor mRight2 = constraintWidget3.mRight;
            if (mRight2.mGroup != Integer.MAX_VALUE) {
                final int mGroup3 = mRight2.mGroup;
                if (array2[mGroup3] == -1) {
                    array2[mGroup3] = n8;
                    ++n8;
                }
                mRight2.mGroup = array2[mGroup3];
            }
            final ConstraintAnchor mBottom2 = constraintWidget3.mBottom;
            if (mBottom2.mGroup != Integer.MAX_VALUE) {
                final int mGroup4 = mBottom2.mGroup;
                if (array2[mGroup4] == -1) {
                    array2[mGroup4] = n8;
                    ++n8;
                }
                mBottom2.mGroup = array2[mGroup4];
            }
            final ConstraintAnchor mBaseline2 = constraintWidget3.mBaseline;
            if (mBaseline2.mGroup != Integer.MAX_VALUE) {
                final int mGroup5 = mBaseline2.mGroup;
                if (array2[mGroup5] == -1) {
                    final int n10 = n8 + 1;
                    array2[mGroup5] = n8;
                    n8 = n10;
                }
                mBaseline2.mGroup = array2[mGroup5];
            }
        }
        return n8;
    }
    
    public int layoutFindGroupsSimple() {
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.mLeft.mGroup = 0;
            constraintWidget.mRight.mGroup = 0;
            constraintWidget.mTop.mGroup = 1;
            constraintWidget.mBottom.mGroup = 1;
            constraintWidget.mBaseline.mGroup = 1;
        }
        return 2;
    }
    
    public void layoutWithGroup(int width) {
        final int mx = this.mX;
        final int my = this.mY;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.mX = 0;
            this.mY = 0;
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        for (int j = 0; j < width; ++j) {
            try {
                this.addToSolver(this.mSystem, j);
                this.mSystem.minimize();
                this.updateFromSolver(this.mSystem, j);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            this.updateFromSolver(this.mSystem, -2);
        }
        if (this.mParent != null) {
            width = this.getWidth();
            final int height = this.getHeight();
            this.mSnapshot.applyTo(this);
            this.setWidth(width);
            this.setHeight(height);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        super.reset();
    }
    
    public void setOptimizationLevel(final int mOptimizationLevel) {
        this.mOptimizationLevel = mOptimizationLevel;
    }
    
    public void setPadding(final int mPaddingLeft, final int mPaddingTop, final int mPaddingRight, final int mPaddingBottom) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingTop = mPaddingTop;
        this.mPaddingRight = mPaddingRight;
        this.mPaddingBottom = mPaddingBottom;
    }
    
    public void updateChildrenFromSolver(final LinearSystem linearSystem, final int n, final boolean[] array) {
        array[2] = false;
        this.updateFromSolver(linearSystem, n);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem, n);
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                    array[2] = true;
                }
            }
            if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                    array[2] = true;
                }
            }
        }
    }
}
