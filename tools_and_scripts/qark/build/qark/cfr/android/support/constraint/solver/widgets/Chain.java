/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ChainHead;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Optimizer;
import java.util.ArrayList;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int n) {
        ChainHead[] arrchainHead;
        int n2;
        int n3;
        if (n == 0) {
            n2 = 0;
            n3 = constraintWidgetContainer.mHorizontalChainsSize;
            arrchainHead = constraintWidgetContainer.mHorizontalChainsArray;
        } else {
            n2 = 2;
            n3 = constraintWidgetContainer.mVerticalChainsSize;
            arrchainHead = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < n3; ++i) {
            ChainHead chainHead = arrchainHead[i];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, n, n2, chainHead)) continue;
                Chain.applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
                continue;
            }
            Chain.applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1_1, int var2_2, int var3_3, ChainHead var4_4) {
        block68 : {
            block71 : {
                block70 : {
                    block69 : {
                        block67 : {
                            block66 : {
                                block65 : {
                                    block63 : {
                                        block64 : {
                                            var19_5 = var4_4.mFirst;
                                            var23_6 = var4_4.mLast;
                                            var16_7 = var4_4.mFirstVisibleWidget;
                                            var24_8 = var4_4.mLastVisibleWidget;
                                            var22_9 = var4_4.mHead;
                                            var5_10 = var4_4.mTotalWeight;
                                            var20_11 = var4_4.mFirstMatchConstraintWidget;
                                            var18_12 = var4_4.mLastMatchConstraintWidget;
                                            var12_13 = var0.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                                            if (var2_2 == 0) {
                                                var8_14 = var22_9.mHorizontalChainStyle == 0 ? 1 : 0;
                                                var9_15 = var22_9.mHorizontalChainStyle;
                                                var11_16 = var8_14;
                                                var8_14 = var9_15 == 1 ? 1 : 0;
                                                var9_15 = var22_9.mHorizontalChainStyle == 2 ? 1 : 0;
                                                var10_17 = var8_14;
                                                var15_18 = var19_5;
                                                var8_14 = 0;
                                                var13_19 = var9_15;
                                            } else {
                                                var8_14 = var22_9.mVerticalChainStyle == 0 ? 1 : 0;
                                                var9_15 = var22_9.mVerticalChainStyle;
                                                var11_16 = var8_14;
                                                var8_14 = var9_15 == 1 ? 1 : 0;
                                                var9_15 = var22_9.mVerticalChainStyle == 2 ? 1 : 0;
                                                var15_18 = var19_5;
                                                var14_20 = 0;
                                                var10_17 = var8_14;
                                                var13_19 = var9_15;
                                                var8_14 = var14_20;
                                            }
                                            while (var8_14 == 0) {
                                                var17_21 = var15_18.mListAnchors[var3_3];
                                                var9_15 = 4;
                                                if (var12_13 != 0 || var13_19 != 0) {
                                                    var9_15 = 1;
                                                }
                                                var14_20 = var17_21.getMargin();
                                                if (var17_21.mTarget != null && var15_18 != var19_5) {
                                                    var14_20 += var17_21.mTarget.getMargin();
                                                }
                                                if (var13_19 != 0 && var15_18 != var19_5 && var15_18 != var16_7) {
                                                    var9_15 = 6;
                                                } else if (var11_16 != 0 && var12_13 != 0) {
                                                    var9_15 = 4;
                                                }
                                                if (var17_21.mTarget != null) {
                                                    if (var15_18 == var16_7) {
                                                        var1_1.addGreaterThan(var17_21.mSolverVariable, var17_21.mTarget.mSolverVariable, var14_20, 5);
                                                    } else {
                                                        var1_1.addGreaterThan(var17_21.mSolverVariable, var17_21.mTarget.mSolverVariable, var14_20, 6);
                                                    }
                                                    var1_1.addEquality(var17_21.mSolverVariable, var17_21.mTarget.mSolverVariable, var14_20, var9_15);
                                                }
                                                if (var12_13 != 0) {
                                                    if (var15_18.getVisibility() != 8 && var15_18.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                                        var1_1.addGreaterThan(var15_18.mListAnchors[var3_3 + 1].mSolverVariable, var15_18.mListAnchors[var3_3].mSolverVariable, 0, 5);
                                                    }
                                                    var1_1.addGreaterThan(var15_18.mListAnchors[var3_3].mSolverVariable, var0.mListAnchors[var3_3].mSolverVariable, 0, 6);
                                                }
                                                if ((var17_21 = var15_18.mListAnchors[var3_3 + 1].mTarget) != null) {
                                                    var17_21 = var17_21.mOwner;
                                                    if (var17_21.mListAnchors[var3_3].mTarget == null || var17_21.mListAnchors[var3_3].mTarget.mOwner != var15_18) {
                                                        var17_21 = null;
                                                    }
                                                } else {
                                                    var17_21 = null;
                                                }
                                                if (var17_21 != null) {
                                                    var15_18 = var17_21;
                                                    continue;
                                                }
                                                var8_14 = 1;
                                            }
                                            if (var24_8 != null && var23_6.mListAnchors[var3_3 + 1].mTarget != null) {
                                                var17_21 = var24_8.mListAnchors[var3_3 + 1];
                                                var1_1.addLowerThan(var17_21.mSolverVariable, var23_6.mListAnchors[var3_3 + 1].mTarget.mSolverVariable, - var17_21.getMargin(), 5);
                                            }
                                            if (var12_13 != 0) {
                                                var1_1.addGreaterThan(var0.mListAnchors[var3_3 + 1].mSolverVariable, var23_6.mListAnchors[var3_3 + 1].mSolverVariable, var23_6.mListAnchors[var3_3 + 1].getMargin(), 6);
                                            }
                                            if ((var0 = var4_4.mWeightedMatchConstraintsWidgets) == null) break block63;
                                            var8_14 = var0.size();
                                            if (var8_14 <= 1) break block64;
                                            var6_22 = var4_4.mHasUndefinedWeights != false && var4_4.mHasComplexMatchWeights == false ? (float)var4_4.mWidgetsMatchCount : var5_10;
                                            var21_23 = null;
                                            var7_24 = 0.0f;
                                            var17_21 = var20_11;
                                            var20_11 = var21_23;
                                            break block65;
                                        }
                                        var0 = var15_18;
                                        var0 = var18_12;
                                        break block66;
                                    }
                                    var17_21 = var0;
                                    var0 = var18_12;
                                    var0 = var15_18;
                                    var0 = var17_21;
                                    break block66;
                                }
                                for (var9_15 = 0; var9_15 < var8_14; ++var9_15) {
                                    var21_23 = (ConstraintWidget)var0.get(var9_15);
                                    var5_10 = var21_23.mWeight[var2_2];
                                    if (var5_10 >= 0.0f) ** GOTO lbl99
                                    if (var4_4.mHasComplexMatchWeights) {
                                        var1_1.addEquality(var21_23.mListAnchors[var3_3 + 1].mSolverVariable, var21_23.mListAnchors[var3_3].mSolverVariable, 0, 4);
                                        var5_10 = var7_24;
                                    } else {
                                        var5_10 = 1.0f;
lbl99: // 2 sources:
                                        if (var5_10 == 0.0f) {
                                            var1_1.addEquality(var21_23.mListAnchors[var3_3 + 1].mSolverVariable, var21_23.mListAnchors[var3_3].mSolverVariable, 0, 6);
                                            var5_10 = var7_24;
                                        } else {
                                            if (var20_11 != null) {
                                                var25_25 = var20_11.mListAnchors[var3_3].mSolverVariable;
                                                var20_11 = var20_11.mListAnchors[var3_3 + 1].mSolverVariable;
                                                var26_26 = var21_23.mListAnchors[var3_3].mSolverVariable;
                                                var27_27 = var21_23.mListAnchors[var3_3 + 1].mSolverVariable;
                                                var28_28 = var1_1.createRow();
                                                var28_28.createRowEqualMatchDimensions(var7_24, var6_22, var5_10, var25_25, (SolverVariable)var20_11, var26_26, var27_27);
                                                var1_1.addConstraint(var28_28);
                                            }
                                            var20_11 = var21_23;
                                        }
                                    }
                                    var7_24 = var5_10;
                                }
                                var0 = var15_18;
                                var0 = var18_12;
                            }
                            if (var16_7 == null || var16_7 != var24_8 && var13_19 == 0) break block67;
                            var15_18 = var19_5.mListAnchors[var3_3];
                            var17_21 = var23_6.mListAnchors[var3_3 + 1];
                            var0 = var19_5.mListAnchors[var3_3].mTarget != null ? var19_5.mListAnchors[var3_3].mTarget.mSolverVariable : null;
                            var4_4 = var23_6.mListAnchors[var3_3 + 1].mTarget != null ? var23_6.mListAnchors[var3_3 + 1].mTarget.mSolverVariable : null;
                            if (var16_7 == var24_8) {
                                var15_18 = var16_7.mListAnchors[var3_3];
                                var17_21 = var16_7.mListAnchors[var3_3 + 1];
                            }
                            if (var0 != null && var4_4 != null) {
                                var5_10 = var2_2 == 0 ? var22_9.mHorizontalBiasPercent : var22_9.mVerticalBiasPercent;
                                var2_2 = var15_18.getMargin();
                                var8_14 = var17_21.getMargin();
                                var1_1.addCentering(var15_18.mSolverVariable, (SolverVariable)var0, var2_2, var5_10, (SolverVariable)var4_4, var17_21.mSolverVariable, var8_14, 5);
                            }
                            break block68;
                        }
                        if (var11_16 == 0 || var16_7 == null) break block69;
                        var12_13 = var4_4.mWidgetsMatchCount > 0 && var4_4.mWidgetsCount == var4_4.mWidgetsMatchCount ? 1 : 0;
                        var15_18 = var16_7;
                        var0 = var16_7;
                        break block70;
                    }
                    if (var10_17 == 0 || var16_7 == null) break block68;
                    var8_14 = var4_4.mWidgetsMatchCount > 0 && var4_4.mWidgetsCount == var4_4.mWidgetsMatchCount ? 1 : 0;
                    var15_18 = var16_7;
                    var4_4 = var16_7;
                    break block71;
                }
                while (var15_18 != null) {
                    var4_4 = var15_18.mNextChainWidget[var2_2];
                    while (var4_4 != null && var4_4.getVisibility() == 8) {
                        var4_4 = var4_4.mNextChainWidget[var2_2];
                    }
                    if (var4_4 != null || var15_18 == var24_8) {
                        var20_11 = var15_18.mListAnchors[var3_3];
                        var25_25 = var20_11.mSolverVariable;
                        var17_21 = var20_11.mTarget != null ? var20_11.mTarget.mSolverVariable : null;
                        if (var0 != var15_18) {
                            var17_21 = var0.mListAnchors[var3_3 + 1].mSolverVariable;
                        } else if (var15_18 == var16_7 && var0 == var15_18) {
                            var17_21 = var19_5.mListAnchors[var3_3].mTarget != null ? var19_5.mListAnchors[var3_3].mTarget.mSolverVariable : null;
                        }
                        var18_12 = null;
                        var9_15 = var20_11.getMargin();
                        var8_14 = var15_18.mListAnchors[var3_3 + 1].getMargin();
                        if (var4_4 != null) {
                            var18_12 = var4_4.mListAnchors[var3_3];
                            var20_11 = var18_12.mSolverVariable;
                            var21_23 = var15_18.mListAnchors[var3_3 + 1].mSolverVariable;
                            var22_9 = var20_11;
                        } else {
                            var20_11 = var23_6.mListAnchors[var3_3 + 1].mTarget;
                            if (var20_11 != null) {
                                var18_12 = var20_11.mSolverVariable;
                            }
                            var21_23 = var15_18.mListAnchors[var3_3 + 1].mSolverVariable;
                            var22_9 = var18_12;
                            var18_12 = var20_11;
                        }
                        if (var18_12 != null) {
                            var8_14 += var18_12.getMargin();
                        }
                        if (var0 != null) {
                            var9_15 += var0.mListAnchors[var3_3 + 1].getMargin();
                        }
                        if (var25_25 != null && var17_21 != null && var22_9 != null && var21_23 != null) {
                            if (var15_18 == var16_7) {
                                var9_15 = var16_7.mListAnchors[var3_3].getMargin();
                            }
                            if (var15_18 == var24_8) {
                                var8_14 = var24_8.mListAnchors[var3_3 + 1].getMargin();
                            }
                            var13_19 = var12_13 != 0 ? 6 : 4;
                            var1_1.addCentering(var25_25, (SolverVariable)var17_21, var9_15, 0.5f, (SolverVariable)var22_9, (SolverVariable)var21_23, var8_14, var13_19);
                        }
                    }
                    if (var15_18.getVisibility() != 8) {
                        var0 = var15_18;
                    }
                    var15_18 = var4_4;
                }
                break block68;
            }
            while (var15_18 != null) {
                var0 = var15_18.mNextChainWidget[var2_2];
                while (var0 != null && var0.getVisibility() == 8) {
                    var0 = var0.mNextChainWidget[var2_2];
                }
                if (var15_18 != var16_7 && var15_18 != var24_8 && var0 != null) {
                    if (var0 == var24_8) {
                        var0 = null;
                    }
                    var18_12 = var15_18.mListAnchors[var3_3];
                    var22_9 = var18_12.mSolverVariable;
                    if (var18_12.mTarget != null) {
                        var17_21 = var18_12.mTarget.mSolverVariable;
                    }
                    var25_25 = var4_4.mListAnchors[var3_3 + 1].mSolverVariable;
                    var17_21 = null;
                    var12_13 = var18_12.getMargin();
                    var9_15 = var15_18.mListAnchors[var3_3 + 1].getMargin();
                    if (var0 != null) {
                        var20_11 = var0.mListAnchors[var3_3];
                        var18_12 = var20_11.mSolverVariable;
                        var17_21 = var20_11.mTarget != null ? var20_11.mTarget.mSolverVariable : null;
                        var21_23 = var18_12;
                    } else {
                        var18_12 = var15_18.mListAnchors[var3_3 + 1].mTarget;
                        if (var18_12 != null) {
                            var17_21 = var18_12.mSolverVariable;
                        }
                        var20_11 = var15_18.mListAnchors[var3_3 + 1].mSolverVariable;
                        var21_23 = var17_21;
                        var17_21 = var20_11;
                        var20_11 = var18_12;
                    }
                    if (var20_11 != null) {
                        var9_15 += var20_11.getMargin();
                    }
                    if (var4_4 != null) {
                        var12_13 += var4_4.mListAnchors[var3_3 + 1].getMargin();
                    }
                    var13_19 = var8_14 != 0 ? 6 : 4;
                    if (var22_9 != null && var25_25 != null && var21_23 != null && var17_21 != null) {
                        var1_1.addCentering((SolverVariable)var22_9, var25_25, var12_13, 0.5f, (SolverVariable)var21_23, (SolverVariable)var17_21, var9_15, var13_19);
                    }
                }
                if (var15_18.getVisibility() != 8) {
                    var4_4 = var15_18;
                }
                var15_18 = var0;
            }
            var15_18 = var16_7.mListAnchors[var3_3];
            var17_21 = var19_5.mListAnchors[var3_3].mTarget;
            var4_4 = var24_8.mListAnchors[var3_3 + 1];
            var0 = var23_6.mListAnchors[var3_3 + 1].mTarget;
            if (var17_21 != null) {
                if (var16_7 != var24_8) {
                    var1_1.addEquality(var15_18.mSolverVariable, var17_21.mSolverVariable, var15_18.getMargin(), 5);
                } else if (var0 != null) {
                    var1_1.addCentering(var15_18.mSolverVariable, var17_21.mSolverVariable, var15_18.getMargin(), 0.5f, var4_4.mSolverVariable, var0.mSolverVariable, var4_4.getMargin(), 5);
                }
            }
            if (var0 != null && var16_7 != var24_8) {
                var1_1.addEquality(var4_4.mSolverVariable, var0.mSolverVariable, - var4_4.getMargin(), 5);
            }
        }
        if (var11_16 == 0) {
            if (var10_17 == 0) return;
        }
        if (var16_7 == null) return;
        var15_18 = var16_7.mListAnchors[var3_3];
        var17_21 = var24_8.mListAnchors[var3_3 + 1];
        var4_4 = var15_18.mTarget != null ? var15_18.mTarget.mSolverVariable : null;
        var0 = var17_21.mTarget != null ? var17_21.mTarget.mSolverVariable : null;
        if (var23_6 != var24_8) {
            var0 = var23_6.mListAnchors[var3_3 + 1];
            var0 = var0.mTarget != null ? var0.mTarget.mSolverVariable : null;
        }
        if (var16_7 == var24_8) {
            var15_18 = var16_7.mListAnchors[var3_3];
            var16_7 = var16_7.mListAnchors[var3_3 + 1];
        } else {
            var16_7 = var17_21;
        }
        if (var4_4 == null) return;
        if (var0 == null) return;
        var2_2 = var15_18.getMargin();
        var17_21 = var24_8 == null ? var23_6 : var24_8;
        var3_3 = var17_21.mListAnchors[var3_3 + 1].getMargin();
        var1_1.addCentering(var15_18.mSolverVariable, (SolverVariable)var4_4, var2_2, 0.5f, (SolverVariable)var0, var16_7.mSolverVariable, var3_3, 5);
    }
}

