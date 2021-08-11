/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.Optimizer;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.constraint.solver.widgets.WidgetContainer;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintWidgetContainer
extends WidgetContainer {
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
    private boolean[] flags = new boolean[3];
    protected LinearSystem mBackgroundSystem = null;
    private ConstraintWidget[] mChainEnds = new ConstraintWidget[4];
    private boolean mHeightMeasuredTooSmall = false;
    private ConstraintWidget[] mHorizontalChainsArray = new ConstraintWidget[4];
    private int mHorizontalChainsSize = 0;
    private ConstraintWidget[] mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
    private int mOptimizationLevel = 2;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem = new LinearSystem();
    private ConstraintWidget[] mVerticalChainsArray = new ConstraintWidget[4];
    private int mVerticalChainsSize = 0;
    private boolean mWidthMeasuredTooSmall = false;
    int mWrapHeight;
    int mWrapWidth;

    static {
        ALLOW_ROOT_GROUP = true;
    }

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int n, int n2) {
        super(n, n2);
    }

    public ConstraintWidgetContainer(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int n;
        int n2;
        for (n2 = 0; n2 < (n = this.mHorizontalChainsSize); ++n2) {
            if (this.mHorizontalChainsArray[n2] != constraintWidget) continue;
            return;
        }
        ConstraintWidget[] arrconstraintWidget = this.mHorizontalChainsArray;
        if (n + 1 >= arrconstraintWidget.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(arrconstraintWidget, arrconstraintWidget.length * 2);
        }
        arrconstraintWidget = this.mHorizontalChainsArray;
        n2 = this.mHorizontalChainsSize;
        arrconstraintWidget[n2] = constraintWidget;
        this.mHorizontalChainsSize = n2 + 1;
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int n;
        int n2;
        for (n2 = 0; n2 < (n = this.mVerticalChainsSize); ++n2) {
            if (this.mVerticalChainsArray[n2] != constraintWidget) continue;
            return;
        }
        ConstraintWidget[] arrconstraintWidget = this.mVerticalChainsArray;
        if (n + 1 >= arrconstraintWidget.length) {
            this.mVerticalChainsArray = Arrays.copyOf(arrconstraintWidget, arrconstraintWidget.length * 2);
        }
        arrconstraintWidget = this.mVerticalChainsArray;
        n2 = this.mVerticalChainsSize;
        arrconstraintWidget[n2] = constraintWidget;
        this.mVerticalChainsSize = n2 + 1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void applyHorizontalChain(LinearSystem var1_1) {
        var9_2 = var1_1;
        var3_3 = 0;
        do {
            block49 : {
                block52 : {
                    block51 : {
                        block50 : {
                            var13_13 = this;
                            if (var3_3 >= var13_13.mHorizontalChainsSize) return;
                            var11_11 = var13_13.mHorizontalChainsArray;
                            var10_10 = var11_11[var3_3];
                            var7_8 = this.countMatchConstraintsChainedWidgets((LinearSystem)var1_1, var13_13.mChainEnds, var11_11[var3_3], 0, var13_13.flags);
                            var11_11 = var13_13.mChainEnds[2];
                            if (var11_11 == null) break block49;
                            if (!var13_13.flags[1]) break block50;
                            var4_5 = var10_10.getDrawX();
                            while (var11_11 != null) {
                                var9_2.addEquality(var11_11.mLeft.mSolverVariable, var4_5);
                                var10_10 = var11_11.mHorizontalNextWidget;
                                var4_5 += var11_11.mLeft.getMargin() + var11_11.getWidth() + var11_11.mRight.getMargin();
                                var11_11 = var10_10;
                            }
                            break block49;
                        }
                        var4_5 = var10_10.mHorizontalChainStyle == 0 ? 1 : 0;
                        var5_6 = var10_10.mHorizontalChainStyle == 2 ? 1 : 0;
                        var14_14 = var10_10;
                        var6_7 = var13_13.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                        var8_9 = var13_13.mOptimizationLevel;
                        if (var8_9 != 2 && var8_9 != 8 || !var13_13.flags[0] || !var14_14.mHorizontalChainFixedPosition || var5_6 != 0 || var6_7 != 0 || var10_10.mHorizontalChainStyle != 0) break block51;
                        Optimizer.applyDirectResolutionHorizontalChain((ConstraintWidgetContainer)var13_13, (LinearSystem)var9_2, var7_8, (ConstraintWidget)var14_14);
                        break block49;
                    }
                    if (var7_8 == 0 || var5_6 != 0) break block52;
                    var12_12 = null;
                    var2_4 = 0.0f;
                    while (var11_11 != null) {
                        if (var11_11.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            var4_5 = var11_11.mLeft.getMargin();
                            if (var12_12 != null) {
                                var4_5 += var12_12.mRight.getMargin();
                            }
                            var5_6 = 3;
                            if (var11_11.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var5_6 = 2;
                            }
                            var9_2.addGreaterThan(var11_11.mLeft.mSolverVariable, var11_11.mLeft.mTarget.mSolverVariable, var4_5, var5_6);
                            var4_5 = var11_11.mRight.getMargin();
                            if (var11_11.mRight.mTarget.mOwner.mLeft.mTarget != null && var11_11.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == var11_11) {
                                var4_5 += var11_11.mRight.mTarget.mOwner.mLeft.getMargin();
                            }
                            var5_6 = 3;
                            if (var11_11.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var5_6 = 2;
                            }
                            var9_2.addLowerThan(var11_11.mRight.mSolverVariable, var11_11.mRight.mTarget.mSolverVariable, - var4_5, var5_6);
                        } else {
                            var2_4 += var11_11.mHorizontalWeight;
                            var4_5 = 0;
                            if (var11_11.mRight.mTarget != null) {
                                var4_5 = var11_11.mRight.getMargin();
                                if (var11_11 != var13_13.mChainEnds[3]) {
                                    var4_5 += var11_11.mRight.mTarget.mOwner.mLeft.getMargin();
                                }
                            }
                            var9_2.addGreaterThan(var11_11.mRight.mSolverVariable, var11_11.mLeft.mSolverVariable, 0, 1);
                            var9_2.addLowerThan(var11_11.mRight.mSolverVariable, var11_11.mRight.mTarget.mSolverVariable, - var4_5, 1);
                        }
                        var12_12 = var11_11;
                        var11_11 = var11_11.mHorizontalNextWidget;
                    }
                    if (var7_8 == 1) {
                        var11_11 = var13_13.mMatchConstraintsChainedWidgets[0];
                        var4_5 = var11_11.mLeft.getMargin();
                        if (var11_11.mLeft.mTarget != null) {
                            var4_5 += var11_11.mLeft.mTarget.getMargin();
                        }
                        var5_6 = var11_11.mRight.getMargin();
                        if (var11_11.mRight.mTarget != null) {
                            var5_6 += var11_11.mRight.mTarget.getMargin();
                        }
                        var10_10 = var14_14.mRight.mTarget.mSolverVariable;
                        var12_12 = var13_13.mChainEnds;
                        if (var11_11 == var12_12[3]) {
                            var10_10 = var12_12[1].mRight.mTarget.mSolverVariable;
                        }
                        if (var11_11.mMatchConstraintDefaultWidth == 1) {
                            var9_2.addGreaterThan(var14_14.mLeft.mSolverVariable, var14_14.mLeft.mTarget.mSolverVariable, var4_5, 1);
                            var9_2.addLowerThan(var14_14.mRight.mSolverVariable, (SolverVariable)var10_10, - var5_6, 1);
                            var9_2.addEquality(var14_14.mRight.mSolverVariable, var14_14.mLeft.mSolverVariable, var14_14.getWidth(), 2);
                        } else {
                            var9_2.addEquality(var11_11.mLeft.mSolverVariable, var11_11.mLeft.mTarget.mSolverVariable, var4_5, 1);
                            var9_2.addEquality(var11_11.mRight.mSolverVariable, (SolverVariable)var10_10, - var5_6, 1);
                        }
                    } else {
                        var5_6 = var7_8;
                        for (var6_7 = 0; var6_7 < var5_6 - 1; ++var6_7) {
                            var11_11 = var13_13.mMatchConstraintsChainedWidgets;
                            var15_15 = var11_11[var6_7];
                            var16_16 = var11_11[var6_7 + 1];
                            var17_17 = var15_15.mLeft.mSolverVariable;
                            var18_18 = var15_15.mRight.mSolverVariable;
                            var19_19 = var16_16.mLeft.mSolverVariable;
                            var11_11 = var16_16.mRight.mSolverVariable;
                            var12_12 = var13_13.mChainEnds;
                            if (var16_16 == var12_12[3]) {
                                var11_11 = var12_12[1].mRight.mSolverVariable;
                            }
                            var4_5 = var15_15.mLeft.getMargin();
                            if (var15_15.mLeft.mTarget != null && var15_15.mLeft.mTarget.mOwner.mRight.mTarget != null && var15_15.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var15_15) {
                                var4_5 += var15_15.mLeft.mTarget.mOwner.mRight.getMargin();
                            }
                            var9_2.addGreaterThan((SolverVariable)var17_17, var15_15.mLeft.mTarget.mSolverVariable, var4_5, 2);
                            var7_8 = var15_15.mRight.getMargin();
                            if (var15_15.mRight.mTarget != null && var15_15.mHorizontalNextWidget != null) {
                                var4_5 = var15_15.mHorizontalNextWidget.mLeft.mTarget != null ? var15_15.mHorizontalNextWidget.mLeft.getMargin() : 0;
                                var4_5 = var7_8 + var4_5;
                            } else {
                                var4_5 = var7_8;
                            }
                            var9_2.addLowerThan((SolverVariable)var18_18, var15_15.mRight.mTarget.mSolverVariable, - var4_5, 2);
                            if (var6_7 + 1 == var5_6 - 1) {
                                var4_5 = var16_16.mLeft.getMargin();
                                if (var16_16.mLeft.mTarget != null && var16_16.mLeft.mTarget.mOwner.mRight.mTarget != null && var16_16.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var16_16) {
                                    var4_5 += var16_16.mLeft.mTarget.mOwner.mRight.getMargin();
                                }
                                var9_2.addGreaterThan(var19_19, var16_16.mLeft.mTarget.mSolverVariable, var4_5, 2);
                                var12_12 = var16_16.mRight;
                                var20_20 = var13_13.mChainEnds;
                                if (var16_16 == var20_20[3]) {
                                    var12_12 = var20_20[1].mRight;
                                }
                                var4_5 = var12_12.getMargin();
                                if (var12_12.mTarget != null && var12_12.mTarget.mOwner.mLeft.mTarget != null && var12_12.mTarget.mOwner.mLeft.mTarget.mOwner == var16_16) {
                                    var4_5 += var12_12.mTarget.mOwner.mLeft.getMargin();
                                }
                                var9_2.addLowerThan((SolverVariable)var11_11, var12_12.mTarget.mSolverVariable, - var4_5, 2);
                            }
                            if (var14_14.mMatchConstraintMaxWidth > 0) {
                                var9_2.addLowerThan((SolverVariable)var18_18, (SolverVariable)var17_17, var14_14.mMatchConstraintMaxWidth, 2);
                            }
                            var12_12 = var1_1.createRow();
                            var12_12.createRowEqualDimension(var15_15.mHorizontalWeight, var2_4, var16_16.mHorizontalWeight, (SolverVariable)var17_17, var15_15.mLeft.getMargin(), (SolverVariable)var18_18, var15_15.mRight.getMargin(), var19_19, var16_16.mLeft.getMargin(), (SolverVariable)var11_11, var16_16.mRight.getMargin());
                            var9_2.addConstraint((ArrayRow)var12_12);
                        }
                    }
                    break block49;
                }
                var6_7 = 0;
                var15_15 = null;
                var13_13 = null;
                var17_17 = null;
                var12_12 = var9_2;
                var9_2 = var11_11;
                do {
                    block54 : {
                        block53 : {
                            var16_16 = var9_2;
                            var17_17 = null;
                            var19_19 = null;
                            if (var16_16 == null) break;
                            var18_18 = var16_16.mHorizontalNextWidget;
                            if (var18_18 == null) {
                                var15_15 = this.mChainEnds[1];
                                var6_7 = 1;
                            }
                            if (var5_6 == 0) break block53;
                            var9_2 = var16_16.mLeft;
                            var7_8 = var9_2.getMargin();
                            if (var13_13 != null) {
                                var7_8 += var13_13.mRight.getMargin();
                            }
                            var8_9 = 1;
                            if (var11_11 != var16_16) {
                                var8_9 = 3;
                            }
                            var12_12.addGreaterThan(var9_2.mSolverVariable, var9_2.mTarget.mSolverVariable, var7_8, var8_9);
                            if (var16_16.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var13_13 = var16_16.mRight;
                                if (var16_16.mMatchConstraintDefaultWidth == 1) {
                                    var7_8 = Math.max(var16_16.mMatchConstraintMinWidth, var16_16.getWidth());
                                    var12_12.addEquality(var13_13.mSolverVariable, var9_2.mSolverVariable, var7_8, 3);
                                } else {
                                    var12_12.addGreaterThan(var9_2.mSolverVariable, var9_2.mTarget.mSolverVariable, var9_2.mMargin, 3);
                                    var12_12.addLowerThan(var13_13.mSolverVariable, var9_2.mSolverVariable, var16_16.mMatchConstraintMinWidth, 3);
                                }
                            }
                            ** GOTO lbl173
                        }
                        if (var4_5 != 0 || var6_7 == 0 || var13_13 == null) break block54;
                        if (var16_16.mRight.mTarget == null) {
                            var12_12.addEquality(var16_16.mRight.mSolverVariable, var16_16.getDrawRight());
                        } else {
                            var7_8 = var16_16.mRight.getMargin();
                            var12_12.addEquality(var16_16.mRight.mSolverVariable, var15_15.mRight.mTarget.mSolverVariable, - var7_8, 5);
                        }
                        ** GOTO lbl173
                    }
                    if (var4_5 == 0 && var6_7 == 0 && var13_13 == null) {
                        if (var16_16.mLeft.mTarget == null) {
                            var12_12.addEquality(var16_16.mLeft.mSolverVariable, var16_16.getDrawX());
                        } else {
                            var7_8 = var16_16.mLeft.getMargin();
                            var12_12.addEquality(var16_16.mLeft.mSolverVariable, var10_10.mLeft.mTarget.mSolverVariable, var7_8, 5);
                        }
lbl173: // 6 sources:
                        var9_2 = var18_18;
                    } else {
                        var21_21 = var16_16.mLeft;
                        var20_20 = var16_16.mRight;
                        var7_8 = var21_21.getMargin();
                        var8_9 = var20_20.getMargin();
                        var12_12.addGreaterThan(var21_21.mSolverVariable, var21_21.mTarget.mSolverVariable, var7_8, 1);
                        var12_12.addLowerThan(var20_20.mSolverVariable, var20_20.mTarget.mSolverVariable, - var8_9, 1);
                        var9_2 = var21_21.mTarget != null ? var21_21.mTarget.mSolverVariable : null;
                        if (var13_13 == null) {
                            var9_2 = var10_10.mLeft.mTarget != null ? var10_10.mLeft.mTarget.mSolverVariable : null;
                            var17_17 = var9_2;
                        } else {
                            var17_17 = var9_2;
                        }
                        var9_2 = var18_18 == null ? (var15_15.mRight.mTarget != null ? var15_15.mRight.mTarget.mOwner : null) : var18_18;
                        if (var9_2 != null) {
                            var13_13 = var9_2.mLeft.mSolverVariable;
                            if (var6_7 != 0) {
                                var13_13 = var15_15.mRight.mTarget != null ? var15_15.mRight.mTarget.mSolverVariable : null;
                            }
                            if (var17_17 != null && var13_13 != null) {
                                var18_18 = var21_21.mSolverVariable;
                                var20_20 = var20_20.mSolverVariable;
                                var1_1.addCentering((SolverVariable)var18_18, (SolverVariable)var17_17, var7_8, 0.5f, (SolverVariable)var13_13, (SolverVariable)var20_20, var8_9, 4);
                            }
                        }
                    }
                    var13_13 = var6_7 != 0 ? var19_19 : var9_2;
                    var17_17 = var9_2;
                    var9_2 = var13_13;
                    var13_13 = var16_16;
                } while (true);
                var9_2 = var12_12;
                if (var5_6 != 0) {
                    var13_13 = var11_11.mLeft;
                    var14_14 = var15_15.mRight;
                    var4_5 = var13_13.getMargin();
                    var5_6 = var14_14.getMargin();
                    var11_11 = var10_10.mLeft.mTarget != null ? var10_10.mLeft.mTarget.mSolverVariable : null;
                    var12_12 = var17_17;
                    if (var15_15.mRight.mTarget != null) {
                        var12_12 = var15_15.mRight.mTarget.mSolverVariable;
                    }
                    if (var11_11 != null && var12_12 != null) {
                        var9_2.addLowerThan(var14_14.mSolverVariable, (SolverVariable)var12_12, - var5_6, 1);
                        var1_1.addCentering(var13_13.mSolverVariable, (SolverVariable)var11_11, var4_5, var10_10.mHorizontalBiasPercent, (SolverVariable)var12_12, var14_14.mSolverVariable, var5_6, 4);
                    }
                }
            }
            ++var3_3;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void applyVerticalChain(LinearSystem var1_1) {
        var9_2 = var1_1;
        var3_3 = 0;
        do {
            block53 : {
                block56 : {
                    block55 : {
                        block54 : {
                            var13_13 = this;
                            if (var3_3 >= var13_13.mVerticalChainsSize) return;
                            var11_11 = var13_13.mVerticalChainsArray;
                            var10_10 = var11_11[var3_3];
                            var7_8 = this.countMatchConstraintsChainedWidgets((LinearSystem)var1_1, var13_13.mChainEnds, var11_11[var3_3], 1, var13_13.flags);
                            var11_11 = var13_13.mChainEnds[2];
                            if (var11_11 == null) break block53;
                            if (!var13_13.flags[1]) break block54;
                            var4_5 = var10_10.getDrawY();
                            while (var11_11 != null) {
                                var9_2.addEquality(var11_11.mTop.mSolverVariable, var4_5);
                                var10_10 = var11_11.mVerticalNextWidget;
                                var4_5 += var11_11.mTop.getMargin() + var11_11.getHeight() + var11_11.mBottom.getMargin();
                                var11_11 = var10_10;
                            }
                            break block53;
                        }
                        var4_5 = var10_10.mVerticalChainStyle == 0 ? 1 : 0;
                        var5_6 = var10_10.mVerticalChainStyle == 2 ? 1 : 0;
                        var14_14 = var10_10;
                        var6_7 = var13_13.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                        var8_9 = var13_13.mOptimizationLevel;
                        if (var8_9 != 2 && var8_9 != 8 || !var13_13.flags[0] || !var14_14.mVerticalChainFixedPosition || var5_6 != 0 || var6_7 != 0 || var10_10.mVerticalChainStyle != 0) break block55;
                        Optimizer.applyDirectResolutionVerticalChain((ConstraintWidgetContainer)var13_13, (LinearSystem)var9_2, var7_8, (ConstraintWidget)var14_14);
                        break block53;
                    }
                    if (var7_8 == 0 || var5_6 != 0) break block56;
                    var12_12 = null;
                    var2_4 = 0.0f;
                    while (var11_11 != null) {
                        if (var11_11.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            var4_5 = var11_11.mTop.getMargin();
                            if (var12_12 != null) {
                                var4_5 += var12_12.mBottom.getMargin();
                            }
                            var5_6 = 3;
                            if (var11_11.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var5_6 = 2;
                            }
                            var9_2.addGreaterThan(var11_11.mTop.mSolverVariable, var11_11.mTop.mTarget.mSolverVariable, var4_5, var5_6);
                            var4_5 = var11_11.mBottom.getMargin();
                            if (var11_11.mBottom.mTarget.mOwner.mTop.mTarget != null && var11_11.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == var11_11) {
                                var4_5 += var11_11.mBottom.mTarget.mOwner.mTop.getMargin();
                            }
                            var5_6 = 3;
                            if (var11_11.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var5_6 = 2;
                            }
                            var9_2.addLowerThan(var11_11.mBottom.mSolverVariable, var11_11.mBottom.mTarget.mSolverVariable, - var4_5, var5_6);
                        } else {
                            var2_4 += var11_11.mVerticalWeight;
                            var4_5 = 0;
                            if (var11_11.mBottom.mTarget != null) {
                                var4_5 = var11_11.mBottom.getMargin();
                                if (var11_11 != var13_13.mChainEnds[3]) {
                                    var4_5 += var11_11.mBottom.mTarget.mOwner.mTop.getMargin();
                                }
                            }
                            var9_2.addGreaterThan(var11_11.mBottom.mSolverVariable, var11_11.mTop.mSolverVariable, 0, 1);
                            var9_2.addLowerThan(var11_11.mBottom.mSolverVariable, var11_11.mBottom.mTarget.mSolverVariable, - var4_5, 1);
                        }
                        var12_12 = var11_11;
                        var11_11 = var11_11.mVerticalNextWidget;
                    }
                    if (var7_8 == 1) {
                        var11_11 = var13_13.mMatchConstraintsChainedWidgets[0];
                        var4_5 = var11_11.mTop.getMargin();
                        if (var11_11.mTop.mTarget != null) {
                            var4_5 += var11_11.mTop.mTarget.getMargin();
                        }
                        var5_6 = var11_11.mBottom.getMargin();
                        if (var11_11.mBottom.mTarget != null) {
                            var5_6 += var11_11.mBottom.mTarget.getMargin();
                        }
                        var10_10 = var14_14.mBottom.mTarget.mSolverVariable;
                        var12_12 = var13_13.mChainEnds;
                        if (var11_11 == var12_12[3]) {
                            var10_10 = var12_12[1].mBottom.mTarget.mSolverVariable;
                        }
                        if (var11_11.mMatchConstraintDefaultHeight == 1) {
                            var9_2.addGreaterThan(var14_14.mTop.mSolverVariable, var14_14.mTop.mTarget.mSolverVariable, var4_5, 1);
                            var9_2.addLowerThan(var14_14.mBottom.mSolverVariable, (SolverVariable)var10_10, - var5_6, 1);
                            var9_2.addEquality(var14_14.mBottom.mSolverVariable, var14_14.mTop.mSolverVariable, var14_14.getHeight(), 2);
                        } else {
                            var9_2.addEquality(var11_11.mTop.mSolverVariable, var11_11.mTop.mTarget.mSolverVariable, var4_5, 1);
                            var9_2.addEquality(var11_11.mBottom.mSolverVariable, (SolverVariable)var10_10, - var5_6, 1);
                        }
                    } else {
                        var5_6 = var7_8;
                        for (var6_7 = 0; var6_7 < var5_6 - 1; ++var6_7) {
                            var11_11 = var13_13.mMatchConstraintsChainedWidgets;
                            var15_15 = var11_11[var6_7];
                            var16_16 = var11_11[var6_7 + 1];
                            var17_17 = var15_15.mTop.mSolverVariable;
                            var18_18 = var15_15.mBottom.mSolverVariable;
                            var19_19 = var16_16.mTop.mSolverVariable;
                            var11_11 = var16_16.mBottom.mSolverVariable;
                            var12_12 = var13_13.mChainEnds;
                            if (var16_16 == var12_12[3]) {
                                var11_11 = var12_12[1].mBottom.mSolverVariable;
                            }
                            var4_5 = var15_15.mTop.getMargin();
                            if (var15_15.mTop.mTarget != null && var15_15.mTop.mTarget.mOwner.mBottom.mTarget != null && var15_15.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var15_15) {
                                var4_5 += var15_15.mTop.mTarget.mOwner.mBottom.getMargin();
                            }
                            var9_2.addGreaterThan((SolverVariable)var17_17, var15_15.mTop.mTarget.mSolverVariable, var4_5, 2);
                            var7_8 = var15_15.mBottom.getMargin();
                            if (var15_15.mBottom.mTarget != null && var15_15.mVerticalNextWidget != null) {
                                var4_5 = var15_15.mVerticalNextWidget.mTop.mTarget != null ? var15_15.mVerticalNextWidget.mTop.getMargin() : 0;
                                var4_5 = var7_8 + var4_5;
                            } else {
                                var4_5 = var7_8;
                            }
                            var9_2.addLowerThan((SolverVariable)var18_18, var15_15.mBottom.mTarget.mSolverVariable, - var4_5, 2);
                            if (var6_7 + 1 == var5_6 - 1) {
                                var4_5 = var16_16.mTop.getMargin();
                                if (var16_16.mTop.mTarget != null && var16_16.mTop.mTarget.mOwner.mBottom.mTarget != null && var16_16.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var16_16) {
                                    var4_5 += var16_16.mTop.mTarget.mOwner.mBottom.getMargin();
                                }
                                var9_2.addGreaterThan(var19_19, var16_16.mTop.mTarget.mSolverVariable, var4_5, 2);
                                var12_12 = var16_16.mBottom;
                                var20_20 = var13_13.mChainEnds;
                                if (var16_16 == var20_20[3]) {
                                    var12_12 = var20_20[1].mBottom;
                                }
                                var4_5 = var12_12.getMargin();
                                if (var12_12.mTarget != null && var12_12.mTarget.mOwner.mTop.mTarget != null && var12_12.mTarget.mOwner.mTop.mTarget.mOwner == var16_16) {
                                    var4_5 += var12_12.mTarget.mOwner.mTop.getMargin();
                                }
                                var9_2.addLowerThan((SolverVariable)var11_11, var12_12.mTarget.mSolverVariable, - var4_5, 2);
                            }
                            if (var14_14.mMatchConstraintMaxHeight > 0) {
                                var9_2.addLowerThan((SolverVariable)var18_18, (SolverVariable)var17_17, var14_14.mMatchConstraintMaxHeight, 2);
                            }
                            var12_12 = var1_1.createRow();
                            var12_12.createRowEqualDimension(var15_15.mVerticalWeight, var2_4, var16_16.mVerticalWeight, (SolverVariable)var17_17, var15_15.mTop.getMargin(), (SolverVariable)var18_18, var15_15.mBottom.getMargin(), var19_19, var16_16.mTop.getMargin(), (SolverVariable)var11_11, var16_16.mBottom.getMargin());
                            var9_2.addConstraint((ArrayRow)var12_12);
                        }
                    }
                    break block53;
                }
                var7_8 = 0;
                var15_15 = null;
                var13_13 = null;
                var17_17 = null;
                var12_12 = var9_2;
                var6_7 = var4_5;
                var9_2 = var11_11;
                do {
                    block58 : {
                        block57 : {
                            var16_16 = var9_2;
                            var17_17 = null;
                            var19_19 = null;
                            if (var16_16 == null) break;
                            var17_17 = var16_16.mVerticalNextWidget;
                            if (var17_17 == null) {
                                var15_15 = this.mChainEnds[1];
                                var7_8 = 1;
                            }
                            if (var5_6 == 0) break block57;
                            var18_18 = var16_16.mTop;
                            var4_5 = var18_18.getMargin();
                            if (var13_13 != null) {
                                var4_5 += var13_13.mBottom.getMargin();
                            }
                            var8_9 = 1;
                            if (var11_11 != var16_16) {
                                var8_9 = 3;
                            }
                            var9_2 = null;
                            var13_13 = null;
                            if (var18_18.mTarget != null) {
                                var9_2 = var18_18.mSolverVariable;
                                var13_13 = var18_18.mTarget.mSolverVariable;
                            } else if (var16_16.mBaseline.mTarget != null) {
                                var9_2 = var16_16.mBaseline.mSolverVariable;
                                var13_13 = var16_16.mBaseline.mTarget.mSolverVariable;
                                var4_5 -= var18_18.getMargin();
                            }
                            if (var9_2 != null && var13_13 != null) {
                                var12_12.addGreaterThan((SolverVariable)var9_2, (SolverVariable)var13_13, var4_5, var8_9);
                            }
                            if (var16_16.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                var9_2 = var16_16.mBottom;
                                if (var16_16.mMatchConstraintDefaultHeight == 1) {
                                    var4_5 = Math.max(var16_16.mMatchConstraintMinHeight, var16_16.getHeight());
                                    var12_12.addEquality(var9_2.mSolverVariable, var18_18.mSolverVariable, var4_5, 3);
                                } else {
                                    var12_12.addGreaterThan(var18_18.mSolverVariable, var18_18.mTarget.mSolverVariable, var18_18.mMargin, 3);
                                    var12_12.addLowerThan(var9_2.mSolverVariable, var18_18.mSolverVariable, var16_16.mMatchConstraintMinHeight, 3);
                                }
                            }
                            ** GOTO lbl185
                        }
                        if (var6_7 != 0 || var7_8 == 0 || var13_13 == null) break block58;
                        if (var16_16.mBottom.mTarget == null) {
                            var12_12.addEquality(var16_16.mBottom.mSolverVariable, var16_16.getDrawBottom());
                        } else {
                            var4_5 = var16_16.mBottom.getMargin();
                            var12_12.addEquality(var16_16.mBottom.mSolverVariable, var15_15.mBottom.mTarget.mSolverVariable, - var4_5, 5);
                        }
                        ** GOTO lbl185
                    }
                    if (var6_7 == 0 && var7_8 == 0 && var13_13 == null) {
                        if (var16_16.mTop.mTarget == null) {
                            var12_12.addEquality(var16_16.mTop.mSolverVariable, var16_16.getDrawY());
                        } else {
                            var4_5 = var16_16.mTop.getMargin();
                            var12_12.addEquality(var16_16.mTop.mSolverVariable, var10_10.mTop.mTarget.mSolverVariable, var4_5, 5);
                        }
lbl185: // 6 sources:
                        var9_2 = var17_17;
                    } else {
                        var21_21 = var16_16.mTop;
                        var20_20 = var16_16.mBottom;
                        var4_5 = var21_21.getMargin();
                        var8_9 = var20_20.getMargin();
                        var12_12.addGreaterThan(var21_21.mSolverVariable, var21_21.mTarget.mSolverVariable, var4_5, 1);
                        var12_12.addLowerThan(var20_20.mSolverVariable, var20_20.mTarget.mSolverVariable, - var8_9, 1);
                        var9_2 = var21_21.mTarget != null ? var21_21.mTarget.mSolverVariable : null;
                        if (var13_13 == null) {
                            var9_2 = var10_10.mTop.mTarget != null ? var10_10.mTop.mTarget.mSolverVariable : null;
                            var18_18 = var9_2;
                        } else {
                            var18_18 = var9_2;
                        }
                        var9_2 = var17_17 == null ? (var15_15.mBottom.mTarget != null ? var15_15.mBottom.mTarget.mOwner : null) : var17_17;
                        if (var9_2 != null) {
                            var13_13 = var9_2.mTop.mSolverVariable;
                            if (var7_8 != 0) {
                                var13_13 = var15_15.mBottom.mTarget != null ? var15_15.mBottom.mTarget.mSolverVariable : null;
                            }
                            if (var18_18 != null && var13_13 != null) {
                                var17_17 = var21_21.mSolverVariable;
                                var20_20 = var20_20.mSolverVariable;
                                var1_1.addCentering((SolverVariable)var17_17, (SolverVariable)var18_18, var4_5, 0.5f, (SolverVariable)var13_13, (SolverVariable)var20_20, var8_9, 4);
                            }
                        }
                    }
                    var13_13 = var7_8 != 0 ? var19_19 : var9_2;
                    var17_17 = var9_2;
                    var9_2 = var13_13;
                    var13_13 = var16_16;
                } while (true);
                var9_2 = var12_12;
                if (var5_6 != 0) {
                    var13_13 = var11_11.mTop;
                    var14_14 = var15_15.mBottom;
                    var4_5 = var13_13.getMargin();
                    var5_6 = var14_14.getMargin();
                    var11_11 = var10_10.mTop.mTarget != null ? var10_10.mTop.mTarget.mSolverVariable : null;
                    var12_12 = var17_17;
                    if (var15_15.mBottom.mTarget != null) {
                        var12_12 = var15_15.mBottom.mTarget.mSolverVariable;
                    }
                    if (var11_11 != null && var12_12 != null) {
                        var9_2.addLowerThan(var14_14.mSolverVariable, (SolverVariable)var12_12, - var5_6, 1);
                        var1_1.addCentering(var13_13.mSolverVariable, (SolverVariable)var11_11, var4_5, var10_10.mVerticalBiasPercent, (SolverVariable)var12_12, var14_14.mSolverVariable, var5_6, 4);
                    }
                }
            }
            ++var3_3;
        } while (true);
    }

    private int countMatchConstraintsChainedWidgets(LinearSystem linearSystem, ConstraintWidget[] arrconstraintWidget, ConstraintWidget constraintWidget, int n, boolean[] arrbl) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        int n2 = 0;
        int n3 = 0;
        arrbl[0] = true;
        arrbl[1] = false;
        arrconstraintWidget[0] = null;
        arrconstraintWidget[2] = null;
        arrconstraintWidget[1] = null;
        arrconstraintWidget[3] = null;
        if (n == 0) {
            boolean bl = true;
            ConstraintWidget constraintWidget3 = null;
            if (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mLeft.mTarget.mOwner != this) {
                bl = false;
            }
            constraintWidget2.mHorizontalNextWidget = null;
            ConstraintWidget constraintWidget4 = null;
            if (constraintWidget.getVisibility() != 8) {
                constraintWidget4 = constraintWidget;
            }
            ConstraintWidget constraintWidget5 = constraintWidget4;
            n = n3;
            while (constraintWidget2.mRight.mTarget != null) {
                constraintWidget2.mHorizontalNextWidget = null;
                if (constraintWidget2.getVisibility() != 8) {
                    if (constraintWidget4 == null) {
                        constraintWidget4 = constraintWidget2;
                    }
                    if (constraintWidget5 != null && constraintWidget5 != constraintWidget2) {
                        constraintWidget5.mHorizontalNextWidget = constraintWidget2;
                    }
                    constraintWidget5 = constraintWidget2;
                } else {
                    linearSystem.addEquality(constraintWidget2.mLeft.mSolverVariable, constraintWidget2.mLeft.mTarget.mSolverVariable, 0, 5);
                    linearSystem.addEquality(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mLeft.mSolverVariable, 0, 5);
                }
                if (constraintWidget2.getVisibility() != 8 && constraintWidget2.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (constraintWidget2.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        arrbl[0] = false;
                    }
                    if (constraintWidget2.mDimensionRatio <= 0.0f) {
                        arrbl[0] = false;
                        ConstraintWidget[] arrconstraintWidget2 = this.mMatchConstraintsChainedWidgets;
                        if (n + 1 >= arrconstraintWidget2.length) {
                            this.mMatchConstraintsChainedWidgets = Arrays.copyOf(arrconstraintWidget2, arrconstraintWidget2.length * 2);
                        }
                        this.mMatchConstraintsChainedWidgets[n] = constraintWidget2;
                        ++n;
                    }
                }
                if (constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget == null || constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != constraintWidget2 || constraintWidget2.mRight.mTarget.mOwner == constraintWidget2) break;
                constraintWidget3 = constraintWidget2 = constraintWidget2.mRight.mTarget.mOwner;
            }
            if (constraintWidget2.mRight.mTarget != null && constraintWidget2.mRight.mTarget.mOwner != this) {
                bl = false;
            }
            if (constraintWidget.mLeft.mTarget == null || constraintWidget3.mRight.mTarget == null) {
                arrbl[1] = true;
            }
            constraintWidget.mHorizontalChainFixedPosition = bl;
            constraintWidget3.mHorizontalNextWidget = null;
            arrconstraintWidget[0] = constraintWidget;
            arrconstraintWidget[2] = constraintWidget4;
            arrconstraintWidget[1] = constraintWidget3;
            arrconstraintWidget[3] = constraintWidget5;
            return n;
        }
        boolean bl = true;
        ConstraintWidget constraintWidget6 = null;
        if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mTop.mTarget.mOwner != this) {
            bl = false;
        }
        constraintWidget2.mVerticalNextWidget = null;
        ConstraintWidget constraintWidget7 = null;
        if (constraintWidget.getVisibility() != 8) {
            constraintWidget7 = constraintWidget;
        }
        ConstraintWidget constraintWidget8 = constraintWidget7;
        n = n2;
        while (constraintWidget2.mBottom.mTarget != null) {
            constraintWidget2.mVerticalNextWidget = null;
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget7 == null) {
                    constraintWidget7 = constraintWidget2;
                }
                if (constraintWidget8 != null && constraintWidget8 != constraintWidget2) {
                    constraintWidget8.mVerticalNextWidget = constraintWidget2;
                }
                constraintWidget8 = constraintWidget2;
            } else {
                linearSystem.addEquality(constraintWidget2.mTop.mSolverVariable, constraintWidget2.mTop.mTarget.mSolverVariable, 0, 5);
                linearSystem.addEquality(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mTop.mSolverVariable, 0, 5);
            }
            if (constraintWidget2.getVisibility() != 8 && constraintWidget2.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (constraintWidget2.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    arrbl[0] = false;
                }
                if (constraintWidget2.mDimensionRatio <= 0.0f) {
                    arrbl[0] = false;
                    ConstraintWidget[] arrconstraintWidget3 = this.mMatchConstraintsChainedWidgets;
                    if (n + 1 >= arrconstraintWidget3.length) {
                        this.mMatchConstraintsChainedWidgets = Arrays.copyOf(arrconstraintWidget3, arrconstraintWidget3.length * 2);
                    }
                    this.mMatchConstraintsChainedWidgets[n] = constraintWidget2;
                    ++n;
                }
            }
            if (constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget == null || constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != constraintWidget2 || constraintWidget2.mBottom.mTarget.mOwner == constraintWidget2) break;
            constraintWidget6 = constraintWidget2 = constraintWidget2.mBottom.mTarget.mOwner;
        }
        if (constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.mOwner != this) {
            bl = false;
        }
        if (constraintWidget.mTop.mTarget == null || constraintWidget6.mBottom.mTarget == null) {
            arrbl[1] = true;
        }
        constraintWidget.mVerticalChainFixedPosition = bl;
        constraintWidget6.mVerticalNextWidget = null;
        arrconstraintWidget[0] = constraintWidget;
        arrconstraintWidget[2] = constraintWidget7;
        arrconstraintWidget[1] = constraintWidget6;
        arrconstraintWidget[3] = constraintWidget8;
        return n;
    }

    public static ConstraintWidgetContainer createContainer(ConstraintWidgetContainer constraintWidgetContainer, String object, ArrayList<ConstraintWidget> arrayList, int n) {
        Rectangle rectangle = ConstraintWidgetContainer.getBounds(arrayList);
        if (rectangle.width != 0 && rectangle.height != 0) {
            int n2;
            if (n > 0) {
                n2 = Math.min(rectangle.x, rectangle.y);
                if (n > n2) {
                    n = n2;
                }
                rectangle.grow(n, n);
            }
            constraintWidgetContainer.setOrigin(rectangle.x, rectangle.y);
            constraintWidgetContainer.setDimension(rectangle.width, rectangle.height);
            constraintWidgetContainer.setDebugName((String)object);
            object = arrayList.get(0).getParent();
            n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                ConstraintWidget constraintWidget = arrayList.get(n);
                if (constraintWidget.getParent() != object) continue;
                constraintWidgetContainer.add(constraintWidget);
                constraintWidget.setX(constraintWidget.getX() - rectangle.x);
                constraintWidget.setY(constraintWidget.getY() - rectangle.y);
            }
            return constraintWidgetContainer;
        }
        return null;
    }

    private boolean optimize(LinearSystem object) {
        ConstraintWidget constraintWidget;
        int n;
        int n2;
        int n3;
        int n4;
        int n5 = this.mChildren.size();
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        do {
            n2 = n6;
            n3 = n7;
            n = n8;
            n4 = n9;
            if (n10 >= n5) break;
            constraintWidget = (ConstraintWidget)this.mChildren.get(n10);
            constraintWidget.mHorizontalResolution = -1;
            constraintWidget.mVerticalResolution = -1;
            if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mHorizontalResolution = 1;
                constraintWidget.mVerticalResolution = 1;
            }
            ++n10;
        } while (true);
        do {
            n6 = n;
            n7 = n3;
            if (n2 != 0) break;
            n3 = 0;
            n = 0;
            n10 = n4 + 1;
            for (n4 = 0; n4 < n5; ++n4) {
                constraintWidget = (ConstraintWidget)this.mChildren.get(n4);
                if (constraintWidget.mHorizontalResolution == -1) {
                    if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget.mHorizontalResolution = 1;
                    } else {
                        Optimizer.checkHorizontalSimpleDependency(this, (LinearSystem)object, constraintWidget);
                    }
                }
                if (constraintWidget.mVerticalResolution == -1) {
                    if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget.mVerticalResolution = 1;
                    } else {
                        Optimizer.checkVerticalSimpleDependency(this, (LinearSystem)object, constraintWidget);
                    }
                }
                if (constraintWidget.mVerticalResolution == -1) {
                    ++n3;
                }
                if (constraintWidget.mHorizontalResolution != -1) continue;
                ++n;
            }
            if (n3 == 0 && n == 0) {
                n2 = 1;
            } else if (n7 == n3 && n6 == n) {
                n2 = 1;
            }
            n4 = n10;
        } while (true);
        n2 = 0;
        n3 = 0;
        for (n = 0; n < n5; ++n) {
            object = (ConstraintWidget)this.mChildren.get(n);
            if (object.mHorizontalResolution == 1 || object.mHorizontalResolution == -1) {
                ++n2;
            }
            if (object.mVerticalResolution != 1 && object.mVerticalResolution != -1) continue;
            ++n3;
        }
        if (n2 == 0 && n3 == 0) {
            return true;
        }
        return false;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    static int setGroup(ConstraintAnchor constraintAnchor, int n) {
        int n2 = constraintAnchor.mGroup;
        if (constraintAnchor.mOwner.getParent() == null) {
            return n;
        }
        if (n2 <= n) {
            return n2;
        }
        constraintAnchor.mGroup = n;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.getOpposite();
        ConstraintAnchor constraintAnchor3 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null) {
            n = ConstraintWidgetContainer.setGroup(constraintAnchor2, n);
        }
        if (constraintAnchor3 != null) {
            n = ConstraintWidgetContainer.setGroup(constraintAnchor3, n);
        }
        if (constraintAnchor2 != null) {
            n = ConstraintWidgetContainer.setGroup(constraintAnchor2, n);
        }
        constraintAnchor.mGroup = n;
        return n;
    }

    void addChain(ConstraintWidget constraintWidget, int n) {
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
            return;
        }
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem, int n) {
        this.addToSolver(linearSystem, n);
        int n2 = this.mChildren.size();
        boolean bl = false;
        int n3 = this.mOptimizationLevel;
        if (n3 != 2 && n3 != 4) {
            bl = true;
        } else if (this.optimize(linearSystem)) {
            return false;
        }
        for (n3 = 0; n3 < n2; ++n3) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(n3);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mVerticalDimensionBehaviour;
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem, n);
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) continue;
                constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
                continue;
            }
            if (bl) {
                Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
            }
            constraintWidget.addToSolver(linearSystem, n);
        }
        if (this.mHorizontalChainsSize > 0) {
            this.applyHorizontalChain(linearSystem);
        }
        if (this.mVerticalChainsSize > 0) {
            this.applyVerticalChain(linearSystem);
        }
        return true;
    }

    public void findHorizontalWrapRecursive(ConstraintWidget constraintWidget, boolean[] object) {
        Object object2 = constraintWidget.mHorizontalDimensionBehaviour;
        Object object3 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean bl = false;
        if (object2 == object3 && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            object[0] = false;
            return;
        }
        int n = constraintWidget.getOptimizerWrapWidth();
        if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            object[0] = false;
            return;
        }
        int n2 = n;
        object3 = null;
        object2 = null;
        constraintWidget.mHorizontalWrapVisited = true;
        if (constraintWidget instanceof Guideline) {
            object = (Guideline)constraintWidget;
            if (object.getOrientation() == 1) {
                n = 0;
                n2 = 0;
                if (object.getRelativeBegin() != -1) {
                    n = object.getRelativeBegin();
                } else if (object.getRelativeEnd() != -1) {
                    n2 = object.getRelativeEnd();
                }
            }
        } else if (!constraintWidget.mRight.isConnected() && !constraintWidget.mLeft.isConnected()) {
            n += constraintWidget.getX();
        } else {
            boolean bl2;
            if (constraintWidget.mRight.mTarget != null && constraintWidget.mLeft.mTarget != null && (constraintWidget.mRight.mTarget == constraintWidget.mLeft.mTarget || constraintWidget.mRight.mTarget.mOwner == constraintWidget.mLeft.mTarget.mOwner && constraintWidget.mRight.mTarget.mOwner != constraintWidget.mParent)) {
                object[0] = false;
                return;
            }
            if (constraintWidget.mRight.mTarget != null) {
                object2 = constraintWidget.mRight.mTarget.mOwner;
                n2 += constraintWidget.mRight.getMargin();
                if (!object2.isRoot() && !object2.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive((ConstraintWidget)object2, (boolean[])object);
                }
            }
            if (constraintWidget.mLeft.mTarget != null) {
                object3 = constraintWidget.mLeft.mTarget.mOwner;
                n += constraintWidget.mLeft.getMargin();
                if (!object3.isRoot() && !object3.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive((ConstraintWidget)object3, (boolean[])object);
                    object = object3;
                } else {
                    object = object3;
                }
            } else {
                object = object3;
            }
            if (constraintWidget.mRight.mTarget != null && !object2.isRoot()) {
                if (constraintWidget.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                    n2 += object2.mDistToRight - object2.getOptimizerWrapWidth();
                } else if (constraintWidget.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                    n2 += object2.mDistToRight;
                }
                bl2 = object2.mRightHasCentered || object2.mLeft.mTarget != null && object2.mRight.mTarget != null && object2.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                constraintWidget.mRightHasCentered = bl2;
                if (constraintWidget.mRightHasCentered && (object2.mLeft.mTarget == null || object2.mLeft.mTarget.mOwner != constraintWidget)) {
                    n2 += n2 - object2.mDistToRight;
                }
            }
            if (constraintWidget.mLeft.mTarget != null && !object.isRoot()) {
                if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                    n += object.mDistToLeft - object.getOptimizerWrapWidth();
                } else if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                    n += object.mDistToLeft;
                }
                bl2 = !(object.mLeftHasCentered || object.mLeft.mTarget != null && object.mRight.mTarget != null && object.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? bl : true;
                constraintWidget.mLeftHasCentered = bl2;
                if (constraintWidget.mLeftHasCentered && (object.mRight.mTarget == null || object.mRight.mTarget.mOwner != constraintWidget)) {
                    n += n - object.mDistToLeft;
                }
            }
        }
        if (constraintWidget.getVisibility() == 8) {
            n -= constraintWidget.mWidth;
            n2 -= constraintWidget.mWidth;
        }
        constraintWidget.mDistToLeft = n;
        constraintWidget.mDistToRight = n2;
    }

    public void findVerticalWrapRecursive(ConstraintWidget constraintWidget, boolean[] object) {
        int n;
        Object object2 = constraintWidget.mVerticalDimensionBehaviour;
        Object object3 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean bl = false;
        if (object2 == object3 && constraintWidget.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            object[0] = false;
            return;
        }
        int n2 = n = constraintWidget.getOptimizerWrapHeight();
        int n3 = n;
        object2 = null;
        object3 = null;
        constraintWidget.mVerticalWrapVisited = true;
        if (constraintWidget instanceof Guideline) {
            object = (Guideline)constraintWidget;
            if (object.getOrientation() == 0) {
                n2 = 0;
                n3 = 0;
                if (object.getRelativeBegin() != -1) {
                    n2 = object.getRelativeBegin();
                } else if (object.getRelativeEnd() != -1) {
                    n3 = object.getRelativeEnd();
                }
            }
        } else if (constraintWidget.mBaseline.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
            n2 += constraintWidget.getY();
        } else {
            boolean bl2;
            if (constraintWidget.mBottom.mTarget != null && constraintWidget.mTop.mTarget != null && (constraintWidget.mBottom.mTarget == constraintWidget.mTop.mTarget || constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mTop.mTarget.mOwner && constraintWidget.mBottom.mTarget.mOwner != constraintWidget.mParent)) {
                object[0] = false;
                return;
            }
            if (constraintWidget.mBaseline.isConnected()) {
                object2 = constraintWidget.mBaseline.mTarget.getOwner();
                if (!object2.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive((ConstraintWidget)object2, (boolean[])object);
                }
                n3 = Math.max(object2.mDistToTop - object2.mHeight + n, n);
                n2 = Math.max(object2.mDistToBottom - object2.mHeight + n, n);
                if (constraintWidget.getVisibility() == 8) {
                    n3 -= constraintWidget.mHeight;
                    n2 -= constraintWidget.mHeight;
                }
                constraintWidget.mDistToTop = n3;
                constraintWidget.mDistToBottom = n2;
                return;
            }
            if (constraintWidget.mTop.isConnected()) {
                object2 = constraintWidget.mTop.mTarget.getOwner();
                n2 += constraintWidget.mTop.getMargin();
                if (!object2.isRoot() && !object2.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive((ConstraintWidget)object2, (boolean[])object);
                }
            }
            if (constraintWidget.mBottom.isConnected()) {
                object3 = constraintWidget.mBottom.mTarget.getOwner();
                n3 += constraintWidget.mBottom.getMargin();
                if (!object3.isRoot() && !object3.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive((ConstraintWidget)object3, (boolean[])object);
                    object = object3;
                } else {
                    object = object3;
                }
            } else {
                object = object3;
            }
            if (constraintWidget.mTop.mTarget != null && !object2.isRoot()) {
                if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                    n2 += object2.mDistToTop - object2.getOptimizerWrapHeight();
                } else if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                    n2 += object2.mDistToTop;
                }
                bl2 = object2.mTopHasCentered || object2.mTop.mTarget != null && object2.mTop.mTarget.mOwner != constraintWidget && object2.mBottom.mTarget != null && object2.mBottom.mTarget.mOwner != constraintWidget && object2.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                constraintWidget.mTopHasCentered = bl2;
                if (constraintWidget.mTopHasCentered && (object2.mBottom.mTarget == null || object2.mBottom.mTarget.mOwner != constraintWidget)) {
                    n2 += n2 - object2.mDistToTop;
                }
            }
            if (constraintWidget.mBottom.mTarget != null && !object.isRoot()) {
                if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                    n3 += object.mDistToBottom - object.getOptimizerWrapHeight();
                } else if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                    n3 += object.mDistToBottom;
                }
                bl2 = !(object.mBottomHasCentered || object.mTop.mTarget != null && object.mTop.mTarget.mOwner != constraintWidget && object.mBottom.mTarget != null && object.mBottom.mTarget.mOwner != constraintWidget && object.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? bl : true;
                constraintWidget.mBottomHasCentered = bl2;
                if (constraintWidget.mBottomHasCentered && (object.mTop.mTarget == null || object.mTop.mTarget.mOwner != constraintWidget)) {
                    n3 += n3 - object.mDistToBottom;
                }
            }
        }
        if (constraintWidget.getVisibility() == 8) {
            n2 -= constraintWidget.mHeight;
            n3 -= constraintWidget.mHeight;
        }
        constraintWidget.mDistToTop = n2;
        constraintWidget.mDistToBottom = n3;
    }

    public void findWrapSize(ArrayList<ConstraintWidget> arrayList, boolean[] object) {
        int n;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = arrayList.size();
        object[0] = true;
        for (int i = 0; i < n8; ++i) {
            ConstraintWidget constraintWidget = arrayList.get(i);
            if (constraintWidget.isRoot()) continue;
            if (!constraintWidget.mHorizontalWrapVisited) {
                this.findHorizontalWrapRecursive(constraintWidget, (boolean[])object);
            }
            if (!constraintWidget.mVerticalWrapVisited) {
                this.findVerticalWrapRecursive(constraintWidget, (boolean[])object);
            }
            if (!object[0]) {
                return;
            }
            n = constraintWidget.mDistToLeft + constraintWidget.mDistToRight - constraintWidget.getWidth();
            int n9 = constraintWidget.mDistToTop + constraintWidget.mDistToBottom - constraintWidget.getHeight();
            if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                n = constraintWidget.getWidth() + constraintWidget.mLeft.mMargin + constraintWidget.mRight.mMargin;
            }
            if (constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                n9 = constraintWidget.getHeight() + constraintWidget.mTop.mMargin + constraintWidget.mBottom.mMargin;
            }
            if (constraintWidget.getVisibility() == 8) {
                n = 0;
                n9 = 0;
            }
            n3 = Math.max(n3, constraintWidget.mDistToLeft);
            n4 = Math.max(n4, constraintWidget.mDistToRight);
            n5 = Math.max(n5, constraintWidget.mDistToBottom);
            n2 = Math.max(n2, constraintWidget.mDistToTop);
            n6 = Math.max(n6, n);
            n7 = Math.max(n7, n9);
        }
        n = Math.max(n3, n4);
        this.mWrapWidth = Math.max(this.mMinWidth, Math.max(n, n6));
        n = Math.max(n2, n5);
        this.mWrapHeight = Math.max(this.mMinHeight, Math.max(n, n7));
        for (n = 0; n < n8; ++n) {
            object = arrayList.get(n);
            object.mHorizontalWrapVisited = false;
            object.mVerticalWrapVisited = false;
            object.mLeftHasCentered = false;
            object.mRightHasCentered = false;
            object.mTopHasCentered = false;
            object.mBottomHasCentered = false;
        }
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || (constraintWidget = (Guideline)constraintWidget).getOrientation() != 0) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    @Override
    public String getType() {
        return "ConstraintLayout";
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || (constraintWidget = (Guideline)constraintWidget).getOrientation() != 1) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
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
        int n;
        ConstraintWidget constraintWidget;
        int n2;
        int n3 = this.mX;
        int n4 = this.mY;
        int n5 = Math.max(0, this.getWidth());
        int n6 = Math.max(0, this.getHeight());
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
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        boolean bl = false;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.mVerticalDimensionBehaviour;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.mHorizontalDimensionBehaviour;
        if (this.mOptimizationLevel == 2 && (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
            this.findWrapSize(this.mChildren, this.flags);
            bl = this.flags[0];
            if (n5 > 0 && n6 > 0 && (this.mWrapWidth > n5 || this.mWrapHeight > n6)) {
                bl = false;
            }
            if (bl) {
                if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                    if (n5 > 0 && n5 < this.mWrapWidth) {
                        this.mWidthMeasuredTooSmall = true;
                        this.setWidth(n5);
                    } else {
                        this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
                    }
                }
                if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                    if (n6 > 0 && n6 < this.mWrapHeight) {
                        this.mHeightMeasuredTooSmall = true;
                        this.setHeight(n6);
                    } else {
                        this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
                    }
                }
            }
        }
        this.resetChains();
        int n7 = this.mChildren.size();
        for (n = 0; n < n7; ++n) {
            constraintWidget = (ConstraintWidget)this.mChildren.get(n);
            if (!(constraintWidget instanceof WidgetContainer)) continue;
            ((WidgetContainer)constraintWidget).layout();
        }
        boolean bl2 = true;
        n = 0;
        while (bl2) {
            int n8;
            boolean bl3;
            block49 : {
                n8 = n + 1;
                bl3 = bl2;
                this.mSystem.reset();
                bl3 = bl2;
                bl2 = this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE);
                if (!bl2) break block49;
                bl3 = bl2;
                try {
                    this.mSystem.minimize();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            bl3 = bl2;
            if (bl3) {
                this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
            } else {
                this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);
                for (n = 0; n < n7; ++n) {
                    constraintWidget = (ConstraintWidget)this.mChildren.get(n);
                    if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                        this.flags[2] = true;
                        break;
                    }
                    if (constraintWidget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getHeight() >= constraintWidget.getWrapHeight()) continue;
                    this.flags[2] = true;
                    break;
                }
            }
            bl2 = false;
            bl3 = false;
            if (n8 < 8 && this.flags[2]) {
                int n9 = 0;
                n = 0;
                for (n2 = 0; n2 < n7; ++n2) {
                    constraintWidget = (ConstraintWidget)this.mChildren.get(n2);
                    n9 = Math.max(n9, constraintWidget.mX + constraintWidget.getWidth());
                    n = Math.max(n, constraintWidget.mY + constraintWidget.getHeight());
                }
                n2 = Math.max(this.mMinWidth, n9);
                n = Math.max(this.mMinHeight, n);
                if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    if (this.getWidth() < n2) {
                        this.setWidth(n2);
                        this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        bl2 = true;
                        bl3 = true;
                    } else {
                        bl2 = bl;
                    }
                } else {
                    bl2 = bl;
                }
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    if (this.getHeight() < n) {
                        this.setHeight(n);
                        this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        bl3 = true;
                        bl = true;
                    } else {
                        bl = bl3;
                        bl3 = bl2;
                    }
                } else {
                    bl = bl3;
                    bl3 = bl2;
                }
            } else {
                bl3 = bl;
                bl = bl2;
            }
            n = Math.max(this.mMinWidth, this.getWidth());
            if (n > this.getWidth()) {
                this.setWidth(n);
                this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                bl2 = true;
                bl3 = true;
            } else {
                bl2 = bl3;
                bl3 = bl;
            }
            n = Math.max(this.mMinHeight, this.getHeight());
            if (n > this.getHeight()) {
                this.setHeight(n);
                this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                bl = true;
                bl3 = true;
            } else {
                bl = bl2;
            }
            if (!bl) {
                if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && n5 > 0) {
                    if (this.getWidth() > n5) {
                        this.mWidthMeasuredTooSmall = true;
                        bl2 = true;
                        this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                        this.setWidth(n5);
                        bl3 = true;
                    } else {
                        bl2 = bl;
                    }
                } else {
                    bl2 = bl;
                }
                if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && n6 > 0 && this.getHeight() > n6) {
                    this.mHeightMeasuredTooSmall = true;
                    bl3 = true;
                    this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                    this.setHeight(n6);
                    bl = true;
                } else {
                    bl = bl3;
                    bl3 = bl2;
                }
            } else {
                bl2 = bl3;
                bl3 = bl;
                bl = bl2;
            }
            bl2 = bl;
            n = n8;
            bl = bl3;
        }
        if (this.mParent != null) {
            n = Math.max(this.mMinWidth, this.getWidth());
            n2 = Math.max(this.mMinHeight, this.getHeight());
            this.mSnapshot.applyTo(this);
            this.setWidth(this.mPaddingLeft + n + this.mPaddingRight);
            this.setHeight(this.mPaddingTop + n2 + this.mPaddingBottom);
        } else {
            this.mX = n3;
            this.mY = n4;
        }
        if (bl) {
            this.mHorizontalDimensionBehaviour = dimensionBehaviour2;
            this.mVerticalDimensionBehaviour = dimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
            return;
        }
    }

    public int layoutFindGroups() {
        Object object;
        int n;
        int n2;
        Object object2;
        Object object3 = new ConstraintAnchor.Type[]{ConstraintAnchor.Type.LEFT, ConstraintAnchor.Type.RIGHT, ConstraintAnchor.Type.TOP, ConstraintAnchor.Type.BASELINE, ConstraintAnchor.Type.BOTTOM};
        int n3 = 1;
        int n4 = this.mChildren.size();
        for (n2 = 0; n2 < n4; ++n2) {
            object = (int[])this.mChildren.get(n2);
            object2 = object.mLeft;
            if (object2.mTarget != null) {
                if (ConstraintWidgetContainer.setGroup((ConstraintAnchor)object2, n3) == n3) {
                    ++n3;
                }
            } else {
                object2.mGroup = Integer.MAX_VALUE;
            }
            object2 = object.mTop;
            if (object2.mTarget != null) {
                if (ConstraintWidgetContainer.setGroup((ConstraintAnchor)object2, n3) == n3) {
                    ++n3;
                }
            } else {
                object2.mGroup = Integer.MAX_VALUE;
            }
            object2 = object.mRight;
            if (object2.mTarget != null) {
                if (ConstraintWidgetContainer.setGroup((ConstraintAnchor)object2, n3) == n3) {
                    ++n3;
                }
            } else {
                object2.mGroup = Integer.MAX_VALUE;
            }
            object2 = object.mBottom;
            if (object2.mTarget != null) {
                if (ConstraintWidgetContainer.setGroup((ConstraintAnchor)object2, n3) == n3) {
                    ++n3;
                }
            } else {
                object2.mGroup = Integer.MAX_VALUE;
            }
            object = object.mBaseline;
            if (object.mTarget != null) {
                if (ConstraintWidgetContainer.setGroup((ConstraintAnchor)object, n3) != n3) continue;
                ++n3;
                continue;
            }
            object.mGroup = Integer.MAX_VALUE;
        }
        int n5 = 1;
        n2 = 0;
        n3 = 0;
        while (n5 != 0) {
            n = 0;
            int n6 = n2 + 1;
            n2 = n;
            for (n5 = 0; n5 < n4; ++n5) {
                object2 = (ConstraintWidget)this.mChildren.get(n5);
                for (n = 0; n < object3.length; ++n) {
                    Object object4 = object3[n];
                    object = null;
                    switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[object4.ordinal()]) {
                        default: {
                            break;
                        }
                        case 5: {
                            object = object2.mBaseline;
                            break;
                        }
                        case 4: {
                            object = object2.mBottom;
                            break;
                        }
                        case 3: {
                            object = object2.mRight;
                            break;
                        }
                        case 2: {
                            object = object2.mTop;
                            break;
                        }
                        case 1: {
                            object = object2.mLeft;
                        }
                    }
                    object4 = object.mTarget;
                    if (object4 == null) continue;
                    if (object4.mOwner.getParent() != null && object4.mGroup != object.mGroup) {
                        n2 = object.mGroup > object4.mGroup ? object4.mGroup : object.mGroup;
                        object.mGroup = n2;
                        object4.mGroup = n2;
                        ++n3;
                        n2 = 1;
                    }
                    if ((object4 = object4.getOpposite()) == null || object4.mGroup == object.mGroup) continue;
                    n2 = object.mGroup > object4.mGroup ? object4.mGroup : object.mGroup;
                    object.mGroup = n2;
                    object4.mGroup = n2;
                    ++n3;
                    n2 = 1;
                }
            }
            n5 = n2;
            n2 = n6;
        }
        n3 = 0;
        object = new int[this.mChildren.size() * object3.length + 1];
        Arrays.fill((int[])object, -1);
        for (n2 = 0; n2 < n4; ++n2) {
            object3 = (ConstraintWidget)this.mChildren.get(n2);
            object2 = object3.mLeft;
            if (object2.mGroup != Integer.MAX_VALUE) {
                n5 = object2.mGroup;
                if (object[n5] == -1) {
                    object[n5] = n3;
                    ++n3;
                }
                object2.mGroup = object[n5];
            }
            object2 = object3.mTop;
            if (object2.mGroup != Integer.MAX_VALUE) {
                n5 = object2.mGroup;
                if (object[n5] == -1) {
                    object[n5] = n3;
                    ++n3;
                }
                object2.mGroup = object[n5];
            }
            object2 = object3.mRight;
            if (object2.mGroup != Integer.MAX_VALUE) {
                n5 = object2.mGroup;
                if (object[n5] == -1) {
                    object[n5] = n3;
                    ++n3;
                }
                object2.mGroup = object[n5];
            }
            object2 = object3.mBottom;
            if (object2.mGroup != Integer.MAX_VALUE) {
                n5 = object2.mGroup;
                if (object[n5] == -1) {
                    object[n5] = n3;
                    ++n3;
                }
                object2.mGroup = object[n5];
            }
            object3 = object3.mBaseline;
            if (object3.mGroup == Integer.MAX_VALUE) continue;
            n = object3.mGroup;
            if (object[n] == -1) {
                n5 = n3 + 1;
                object[n] = n3;
                n3 = n5;
            }
            object3.mGroup = object[n];
        }
        return n3;
    }

    public int layoutFindGroupsSimple() {
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            constraintWidget.mLeft.mGroup = 0;
            constraintWidget.mRight.mGroup = 0;
            constraintWidget.mTop.mGroup = 1;
            constraintWidget.mBottom.mGroup = 1;
            constraintWidget.mBaseline.mGroup = 1;
        }
        return 2;
    }

    public void layoutWithGroup(int n) {
        int n2;
        int n3 = this.mX;
        int n4 = this.mY;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.mX = 0;
            this.mY = 0;
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        int n5 = this.mChildren.size();
        for (n2 = 0; n2 < n5; ++n2) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(n2);
            if (!(constraintWidget instanceof WidgetContainer)) continue;
            ((WidgetContainer)constraintWidget).layout();
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        for (n2 = 0; n2 < n; ++n2) {
            try {
                this.addToSolver(this.mSystem, n2);
                this.mSystem.minimize();
                this.updateFromSolver(this.mSystem, n2);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            this.updateFromSolver(this.mSystem, -2);
        }
        if (this.mParent != null) {
            n = this.getWidth();
            n2 = this.getHeight();
            this.mSnapshot.applyTo(this);
            this.setWidth(n);
            this.setHeight(n2);
        } else {
            this.mX = n3;
            this.mY = n4;
        }
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
            return;
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

    public void setOptimizationLevel(int n) {
        this.mOptimizationLevel = n;
    }

    public void setPadding(int n, int n2, int n3, int n4) {
        this.mPaddingLeft = n;
        this.mPaddingTop = n2;
        this.mPaddingRight = n3;
        this.mPaddingBottom = n4;
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, int n, boolean[] arrbl) {
        arrbl[2] = false;
        this.updateFromSolver(linearSystem, n);
        int n2 = this.mChildren.size();
        for (int i = 0; i < n2; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem, n);
            if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                arrbl[2] = true;
            }
            if (constraintWidget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getHeight() >= constraintWidget.getWrapHeight()) continue;
            arrbl[2] = true;
        }
    }

}

