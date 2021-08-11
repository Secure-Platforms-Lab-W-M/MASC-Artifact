/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.WidgetContainer;
import java.util.ArrayList;

public class ConstraintWidget {
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.0f;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    DimensionBehaviour mHorizontalDimensionBehaviour;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution = -1;
    float mHorizontalWeight;
    boolean mHorizontalWrapVisited;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    int mMatchConstraintDefaultHeight = 0;
    int mMatchConstraintDefaultWidth = 0;
    int mMatchConstraintMaxHeight = 0;
    int mMatchConstraintMaxWidth = 0;
    int mMatchConstraintMinHeight = 0;
    int mMatchConstraintMinWidth = 0;
    protected int mMinHeight;
    protected int mMinWidth;
    protected int mOffsetX;
    protected int mOffsetY;
    ConstraintWidget mParent;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    private int mSolverBottom;
    private int mSolverLeft;
    private int mSolverRight;
    private int mSolverTop;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    DimensionBehaviour mVerticalDimensionBehaviour;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution = -1;
    float mVerticalWeight;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;

    static {
        DEFAULT_BIAS = 0.5f;
    }

    public ConstraintWidget() {
        float f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }

    public ConstraintWidget(int n, int n2) {
        this(0, 0, n, n2);
    }

    public ConstraintWidget(int n, int n2, int n3, int n4) {
        float f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = n;
        this.mY = n2;
        this.mWidth = n3;
        this.mHeight = n4;
        this.addAnchors();
        this.forceUpdateDrawPosition();
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mBaseline);
    }

    private void applyConstraints(LinearSystem linearSystem, boolean bl, boolean bl2, ConstraintAnchor object, ConstraintAnchor object2, int n, int n2, int n3, int n4, float f, boolean bl3, boolean bl4, int n5, int n6, int n7) {
        SolverVariable solverVariable = linearSystem.createObjectVariable(object);
        SolverVariable solverVariable2 = linearSystem.createObjectVariable(object2);
        SolverVariable solverVariable3 = linearSystem.createObjectVariable(object.getTarget());
        SolverVariable solverVariable4 = linearSystem.createObjectVariable(object2.getTarget());
        int n8 = object.getMargin();
        int n9 = object2.getMargin();
        if (this.mVisibility == 8) {
            bl2 = true;
            n3 = 0;
        }
        if (solverVariable3 == null && solverVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable, n));
            if (!bl3) {
                if (bl) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, solverVariable2, solverVariable, n4, true));
                    return;
                }
                if (bl2) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, solverVariable2, solverVariable, n3, false));
                    return;
                }
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, n2));
                return;
            }
            return;
        }
        if (solverVariable3 != null && solverVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable, solverVariable3, n8));
            if (bl) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, solverVariable2, solverVariable, n4, true));
                return;
            }
            if (!bl3) {
                if (bl2) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable, n3));
                    return;
                }
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, n2));
                return;
            }
            return;
        }
        if (solverVariable3 == null && solverVariable4 != null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable4, n9 * -1));
            if (bl) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, solverVariable2, solverVariable, n4, true));
                return;
            }
            if (!bl3) {
                if (bl2) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable, n3));
                    return;
                }
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable, n));
                return;
            }
            return;
        }
        if (bl2) {
            if (bl) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, solverVariable2, solverVariable, n4, true));
            } else {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable, n3));
            }
            if (object.getStrength() != object2.getStrength()) {
                if (object.getStrength() == ConstraintAnchor.Strength.STRONG) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable, solverVariable3, n8));
                    object = linearSystem.createSlackVariable();
                    object2 = linearSystem.createRow();
                    object2.createRowLowerThan(solverVariable2, solverVariable4, (SolverVariable)object, n9 * -1);
                    linearSystem.addConstraint((ArrayRow)object2);
                    return;
                }
                object = linearSystem.createSlackVariable();
                object2 = linearSystem.createRow();
                object2.createRowGreaterThan(solverVariable, solverVariable3, (SolverVariable)object, n8);
                linearSystem.addConstraint((ArrayRow)object2);
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable4, n9 * -1));
                return;
            }
            if (solverVariable3 == solverVariable4) {
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, solverVariable, solverVariable3, 0, 0.5f, solverVariable4, solverVariable2, 0, true));
                return;
            }
            if (!bl4) {
                bl = object.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT;
                linearSystem.addConstraint(LinearSystem.createRowGreaterThan(linearSystem, solverVariable, solverVariable3, n8, bl));
                bl = object2.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT;
                linearSystem.addConstraint(LinearSystem.createRowLowerThan(linearSystem, solverVariable2, solverVariable4, n9 * -1, bl));
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, solverVariable, solverVariable3, n8, f, solverVariable4, solverVariable2, n9, false));
                return;
            }
            return;
        }
        if (bl3) {
            linearSystem.addGreaterThan(solverVariable, solverVariable3, n8, 3);
            linearSystem.addLowerThan(solverVariable2, solverVariable4, n9 * -1, 3);
            linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, solverVariable, solverVariable3, n8, f, solverVariable4, solverVariable2, n9, true));
            return;
        }
        if (!bl4) {
            if (n5 == 1) {
                n = n6 > n3 ? n6 : n3;
                if (n7 > 0) {
                    if (n7 < n) {
                        n = n7;
                    } else {
                        linearSystem.addLowerThan(solverVariable2, solverVariable, n7, 3);
                    }
                }
                linearSystem.addEquality(solverVariable2, solverVariable, n, 3);
                linearSystem.addGreaterThan(solverVariable, solverVariable3, n8, 2);
                linearSystem.addLowerThan(solverVariable2, solverVariable4, - n9, 2);
                linearSystem.addCentering(solverVariable, solverVariable3, n8, f, solverVariable4, solverVariable2, n9, 4);
                return;
            }
            if (n6 == 0 && n7 == 0) {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable, solverVariable3, n8));
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(solverVariable2, solverVariable4, n9 * -1));
                return;
            }
            if (n7 > 0) {
                linearSystem.addLowerThan(solverVariable2, solverVariable, n7, 3);
            }
            linearSystem.addGreaterThan(solverVariable, solverVariable3, n8, 2);
            linearSystem.addLowerThan(solverVariable2, solverVariable4, - n9, 2);
            linearSystem.addCentering(solverVariable, solverVariable3, n8, f, solverVariable4, solverVariable2, n9, 4);
            return;
        }
    }

    public void addToSolver(LinearSystem linearSystem) {
        this.addToSolver(linearSystem, Integer.MAX_VALUE);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void addToSolver(LinearSystem var1_1, int var2_2) {
        block66 : {
            block65 : {
                block64 : {
                    block62 : {
                        block63 : {
                            block61 : {
                                block60 : {
                                    var22_3 = null;
                                    var23_4 = null;
                                    if (var2_2 == Integer.MAX_VALUE || this.mLeft.mGroup == var2_2) {
                                        var22_3 = var1_1.createObjectVariable(this.mLeft);
                                    }
                                    if (var2_2 == Integer.MAX_VALUE || this.mRight.mGroup == var2_2) {
                                        var23_4 = var1_1.createObjectVariable(this.mRight);
                                    }
                                    var21_5 = var2_2 != Integer.MAX_VALUE && this.mTop.mGroup != var2_2 ? null : var1_1.createObjectVariable(this.mTop);
                                    var20_6 = var2_2 != Integer.MAX_VALUE && this.mBottom.mGroup != var2_2 ? null : var1_1.createObjectVariable(this.mBottom);
                                    var24_7 = var2_2 != Integer.MAX_VALUE && this.mBaseline.mGroup != var2_2 ? null : var1_1.createObjectVariable(this.mBaseline);
                                    var14_8 = false;
                                    var15_9 = false;
                                    if (this.mParent != null) {
                                        if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft || this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
                                            ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                                            var14_8 = true;
                                        }
                                        if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop || this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
                                            ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                                            var15_9 = true;
                                        }
                                        if (this.mParent.getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && !var14_8) {
                                            if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                                                if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                                                    this.mLeft.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                                                }
                                            } else {
                                                var25_10 = var1_1.createObjectVariable(this.mParent.mLeft);
                                                var26_11 = var1_1.createRow();
                                                var26_11.createRowGreaterThan((SolverVariable)var22_3, (SolverVariable)var25_10, var1_1.createSlackVariable(), 0);
                                                var1_1.addConstraint((ArrayRow)var26_11);
                                            }
                                            if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                                                if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                                                    this.mRight.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                                                }
                                            } else {
                                                var25_10 = var1_1.createObjectVariable(this.mParent.mRight);
                                                var26_11 = var1_1.createRow();
                                                var26_11.createRowGreaterThan((SolverVariable)var25_10, (SolverVariable)var23_4, var1_1.createSlackVariable(), 0);
                                                var1_1.addConstraint((ArrayRow)var26_11);
                                            }
                                        }
                                        if (this.mParent.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && !var15_9) {
                                            if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                                                if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                                                    this.mTop.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                                                }
                                            } else {
                                                var25_10 = var1_1.createObjectVariable(this.mParent.mTop);
                                                var26_11 = var1_1.createRow();
                                                var26_11.createRowGreaterThan((SolverVariable)var21_5, (SolverVariable)var25_10, var1_1.createSlackVariable(), 0);
                                                var1_1.addConstraint((ArrayRow)var26_11);
                                            }
                                            if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                                                if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                                                    this.mBottom.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                                                }
                                            } else {
                                                var25_10 = var1_1.createObjectVariable(this.mParent.mBottom);
                                                var26_11 = var1_1.createRow();
                                                var26_11.createRowGreaterThan((SolverVariable)var25_10, (SolverVariable)var20_6, var1_1.createSlackVariable(), 0);
                                                var1_1.addConstraint((ArrayRow)var26_11);
                                            }
                                        }
                                        var17_12 = var14_8;
                                        var16_13 = var15_9;
                                    } else {
                                        var17_12 = false;
                                        var16_13 = false;
                                    }
                                    var6_14 = this.mWidth;
                                    if (var6_14 < this.mMinWidth) {
                                        var6_14 = this.mMinWidth;
                                    }
                                    if ((var5_15 = this.mHeight) < this.mMinHeight) {
                                        var5_15 = this.mMinHeight;
                                    }
                                    var15_9 = this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
                                    var14_8 = this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
                                    if (var15_9) break block60;
                                    var25_10 = this.mLeft;
                                    if (var25_10 != null && this.mRight != null && (var25_10.mTarget == null || this.mRight.mTarget == null)) {
                                        var15_9 = true;
                                    }
                                    if (var14_8) break block61;
                                }
                                if (!((var25_10 = this.mTop) == null || this.mBottom == null || var25_10.mTarget != null && this.mBottom.mTarget != null || this.mBaselineDistance != 0 && (this.mBaseline == null || this.mTop.mTarget != null && this.mBaseline.mTarget != null))) {
                                    var14_8 = true;
                                }
                            }
                            var7_16 = false;
                            var9_17 = this.mDimensionRatioSide;
                            var3_18 = this.mDimensionRatio;
                            if (this.mDimensionRatio <= 0.0f || this.mVisibility == 8) ** GOTO lbl-1000
                            if (this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT || this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) break block62;
                            var7_16 = true;
                            if (!var15_9 || var14_8) break block63;
                            var8_19 = var6_14;
                            var7_16 = true;
                            var6_14 = 0;
                            break block64;
                        }
                        if (var15_9 || !var14_8) ** GOTO lbl-1000
                        if (this.mDimensionRatioSide == -1) {
                            var3_18 = 1.0f / var3_18;
                            var8_19 = var6_14;
                            var6_14 = 1;
                            var7_16 = true;
                        } else {
                            var8_19 = var6_14;
                            var7_16 = true;
                            var6_14 = 1;
                        }
                        break block64;
                    }
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        var8_19 = (int)((float)this.mHeight * var3_18);
                        var15_9 = true;
                        var7_16 = false;
                        var6_14 = 0;
                    } else if (this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (this.mDimensionRatioSide == -1) {
                            var3_18 = 1.0f / var3_18;
                        }
                        var5_15 = (int)((float)this.mWidth * var3_18);
                        var8_19 = var6_14;
                        var14_8 = true;
                        var7_16 = false;
                        var6_14 = 1;
                    } else lbl-1000: // 3 sources:
                    {
                        var8_19 = var6_14;
                        var6_14 = var9_17;
                    }
                }
                var18_20 = var7_16 != false && (var6_14 == 0 || var6_14 == -1);
                var19_21 = this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer != false;
                if (this.mHorizontalResolution != 2 && (var2_2 == Integer.MAX_VALUE || this.mLeft.mGroup == var2_2 && this.mRight.mGroup == var2_2)) {
                    if (var18_20 && this.mLeft.mTarget != null && this.mRight.mTarget != null) {
                        var25_10 = var1_1.createObjectVariable(this.mLeft);
                        var26_11 = var1_1.createObjectVariable(this.mRight);
                        var27_22 = var1_1.createObjectVariable(this.mLeft.getTarget());
                        var28_23 = var1_1.createObjectVariable(this.mRight.getTarget());
                        var1_1.addGreaterThan((SolverVariable)var25_10, var27_22, this.mLeft.getMargin(), 3);
                        var1_1.addLowerThan((SolverVariable)var26_11, var28_23, this.mRight.getMargin() * -1, 3);
                        if (!var17_12) {
                            var1_1.addCentering((SolverVariable)var25_10, var27_22, this.mLeft.getMargin(), this.mHorizontalBiasPercent, var28_23, (SolverVariable)var26_11, this.mRight.getMargin(), 4);
                        }
                    } else {
                        var25_10 = this.mLeft;
                        var26_11 = this.mRight;
                        var9_17 = this.mX;
                        this.applyConstraints((LinearSystem)var1_1, var19_21, var15_9, (ConstraintAnchor)var25_10, (ConstraintAnchor)var26_11, var9_17, var9_17 + var8_19, var8_19, this.mMinWidth, this.mHorizontalBiasPercent, var18_20, var17_12, this.mMatchConstraintDefaultWidth, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth);
                    }
                }
                var26_11 = var23_4;
                var27_22 = var22_3;
                var17_12 = false;
                if (this.mVerticalResolution == 2) {
                    return;
                }
                var15_9 = this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer != false;
                if (var7_16 && ((var8_19 = var6_14) == 1 || var8_19 == -1)) {
                    var17_12 = true;
                }
                var8_19 = var6_14;
                if (this.mBaselineDistance <= 0) break block65;
                var23_4 = this.mBottom;
                if (var2_2 == Integer.MAX_VALUE || this.mBottom.mGroup == var2_2 && this.mBaseline.mGroup == var2_2) {
                    var1_1.addEquality((SolverVariable)var24_7, (SolverVariable)var21_5, this.getBaselineDistance(), 5);
                }
                var22_3 = var1_1;
                if (this.mBaseline.mTarget != null) {
                    var6_14 = this.mBaselineDistance;
                    var24_7 = this.mBaseline;
                } else {
                    var6_14 = var5_15;
                    var24_7 = var23_4;
                }
                if (var2_2 != Integer.MAX_VALUE && (this.mTop.mGroup != var2_2 || var24_7.mGroup != var2_2)) {
                    var23_4 = var21_5;
                    var21_5 = var22_3;
                    var22_3 = var23_4;
                } else if (var17_12 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                    var23_4 = var22_3.createObjectVariable(this.mTop);
                    var24_7 = var22_3.createObjectVariable(this.mBottom);
                    var25_10 = var22_3.createObjectVariable(this.mTop.getTarget());
                    var28_23 = var22_3.createObjectVariable(this.mBottom.getTarget());
                    var22_3.addGreaterThan((SolverVariable)var23_4, (SolverVariable)var25_10, this.mTop.getMargin(), 3);
                    var22_3.addLowerThan((SolverVariable)var24_7, var28_23, this.mBottom.getMargin() * -1, 3);
                    if (!var16_13) {
                        var1_1.addCentering((SolverVariable)var23_4, (SolverVariable)var25_10, this.mTop.getMargin(), this.mVerticalBiasPercent, var28_23, (SolverVariable)var24_7, this.mBottom.getMargin(), 4);
                    }
                    var22_3 = var21_5;
                    var21_5 = var1_1;
                } else {
                    var22_3 = this.mTop;
                    var9_17 = this.mY;
                    var10_24 = this.mMinHeight;
                    var4_26 = this.mVerticalBiasPercent;
                    var11_28 = this.mMatchConstraintDefaultHeight;
                    var12_30 = this.mMatchConstraintMinHeight;
                    var13_32 = this.mMatchConstraintMaxHeight;
                    var23_4 = var1_1;
                    this.applyConstraints((LinearSystem)var23_4, var15_9, var14_8, (ConstraintAnchor)var22_3, (ConstraintAnchor)var24_7, var9_17, var9_17 + var6_14, var6_14, var10_24, var4_26, var17_12, var16_13, var11_28, var12_30, var13_32);
                    var23_4.addEquality((SolverVariable)var20_6, (SolverVariable)var21_5, var5_15, 5);
                    var22_3 = var21_5;
                    var21_5 = var23_4;
                }
                var24_7 = var20_6;
                var20_6 = this;
                var23_4 = var21_5;
                var21_5 = var24_7;
                break block66;
            }
            var23_4 = var1_1;
            if (var2_2 == Integer.MAX_VALUE) ** GOTO lbl-1000
            var25_10 = this;
            if (var25_10.mTop.mGroup != var2_2 || var25_10.mBottom.mGroup != var2_2) {
                var22_3 = var20_6;
                var24_7 = var21_5;
                var20_6 = var25_10;
                var21_5 = var22_3;
                var22_3 = var24_7;
            } else lbl-1000: // 2 sources:
            {
                var22_3 = this;
                if (var17_12 && var22_3.mTop.mTarget != null && var22_3.mBottom.mTarget != null) {
                    var24_7 = var23_4.createObjectVariable(var22_3.mTop);
                    var25_10 = var23_4.createObjectVariable(var22_3.mBottom);
                    var28_23 = var23_4.createObjectVariable(var22_3.mTop.getTarget());
                    var29_33 = var23_4.createObjectVariable(var22_3.mBottom.getTarget());
                    var23_4.addGreaterThan((SolverVariable)var24_7, var28_23, var22_3.mTop.getMargin(), 3);
                    var23_4.addLowerThan((SolverVariable)var25_10, var29_33, var22_3.mBottom.getMargin() * -1, 3);
                    if (!var16_13) {
                        var1_1.addCentering((SolverVariable)var24_7, var28_23, var22_3.mTop.getMargin(), var22_3.mVerticalBiasPercent, var29_33, (SolverVariable)var25_10, var22_3.mBottom.getMargin(), 4);
                    }
                    var25_10 = var22_3;
                    var22_3 = var20_6;
                    var24_7 = var21_5;
                    var20_6 = var25_10;
                    var21_5 = var22_3;
                    var22_3 = var24_7;
                } else {
                    var24_7 = var22_3;
                    var22_3 = var24_7.mTop;
                    var25_10 = var24_7.mBottom;
                    var6_14 = var24_7.mY;
                    var9_17 = var24_7.mMinHeight;
                    var4_27 = var24_7.mVerticalBiasPercent;
                    var10_25 = var24_7.mMatchConstraintDefaultHeight;
                    var11_29 = var24_7.mMatchConstraintMinHeight;
                    var12_31 = var24_7.mMatchConstraintMaxHeight;
                    this.applyConstraints((LinearSystem)var1_1, var15_9, var14_8, (ConstraintAnchor)var22_3, (ConstraintAnchor)var25_10, var6_14, var6_14 + var5_15, var5_15, var9_17, var4_27, var17_12, var16_13, var10_25, var11_29, var12_31);
                    var22_3 = var21_5;
                    var21_5 = var20_6;
                    var20_6 = var24_7;
                }
            }
        }
        if (var7_16 == false) return;
        var24_7 = var1_1.createRow();
        if (var2_2 != Integer.MAX_VALUE) {
            if (var20_6.mLeft.mGroup != var2_2) return;
            if (var20_6.mRight.mGroup != var2_2) return;
        }
        if (var8_19 == 0) {
            var23_4.addConstraint(var24_7.createRowDimensionRatio((SolverVariable)var26_11, var27_22, (SolverVariable)var21_5, (SolverVariable)var22_3, var3_18));
            return;
        }
        if (var8_19 == 1) {
            var23_4.addConstraint(var24_7.createRowDimensionRatio((SolverVariable)var21_5, (SolverVariable)var22_3, (SolverVariable)var26_11, var27_22, var3_18));
            return;
        }
        var2_2 = var20_6.mMatchConstraintMinWidth;
        if (var2_2 > 0) {
            var23_4.addGreaterThan((SolverVariable)var26_11, var27_22, var2_2, 3);
        }
        if ((var2_2 = var20_6.mMatchConstraintMinHeight) > 0) {
            var23_4.addGreaterThan((SolverVariable)var21_5, (SolverVariable)var22_3, var2_2, 3);
        }
        var24_7.createRowDimensionRatio((SolverVariable)var26_11, var27_22, (SolverVariable)var21_5, (SolverVariable)var22_3, var3_18);
        var20_6 = var1_1.createErrorVariable();
        var1_1 = var1_1.createErrorVariable();
        var20_6.strength = 4;
        var1_1.strength = 4;
        var24_7.addError((SolverVariable)var20_6, (SolverVariable)var1_1);
        var23_4.addConstraint((ArrayRow)var24_7);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0, ConstraintAnchor.Strength.STRONG);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n) {
        this.connect(type, constraintWidget, type2, n, ConstraintAnchor.Strength.STRONG);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n, ConstraintAnchor.Strength strength) {
        this.connect(type, constraintWidget, type2, n, strength, 0);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void connect(ConstraintAnchor.Type object, ConstraintWidget object2, ConstraintAnchor.Type object3, int n, ConstraintAnchor.Strength strength, int n2) {
        if (object == ConstraintAnchor.Type.CENTER) {
            if (object3 == ConstraintAnchor.Type.CENTER) {
                object = this.getAnchor(ConstraintAnchor.Type.LEFT);
                object3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                ConstraintAnchor constraintAnchor = this.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor constraintAnchor2 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                n = 0;
                boolean bl = false;
                if (!(object != null && object.isConnected() || object3 != null && object3.isConnected())) {
                    this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)object2, ConstraintAnchor.Type.LEFT, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)object2, ConstraintAnchor.Type.RIGHT, 0, strength, n2);
                    n = 1;
                }
                if (!(constraintAnchor != null && constraintAnchor.isConnected() || constraintAnchor2 != null && constraintAnchor2.isConnected())) {
                    this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)object2, ConstraintAnchor.Type.TOP, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)object2, ConstraintAnchor.Type.BOTTOM, 0, strength, n2);
                    bl = true;
                }
                if (n != 0 && bl) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(object2.getAnchor(ConstraintAnchor.Type.CENTER), 0, n2);
                    return;
                }
                if (n != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(object2.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, n2);
                    return;
                }
                if (!bl) return;
                this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(object2.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, n2);
                return;
            }
            if (object3 != ConstraintAnchor.Type.LEFT && object3 != ConstraintAnchor.Type.RIGHT) {
                if (object3 != ConstraintAnchor.Type.TOP) {
                    if (object3 != ConstraintAnchor.Type.BOTTOM) return;
                }
                this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0, strength, n2);
                this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0, strength, n2);
                this.getAnchor(ConstraintAnchor.Type.CENTER).connect(object2.getAnchor((ConstraintAnchor.Type)((Object)object3)), 0, n2);
                return;
            }
            this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0, strength, n2);
            object = ConstraintAnchor.Type.RIGHT;
            this.connect((ConstraintAnchor.Type)((Object)object), (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0, strength, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER).connect(object2.getAnchor((ConstraintAnchor.Type)((Object)object3)), 0, n2);
            return;
        }
        if (object == ConstraintAnchor.Type.CENTER_X && (object3 == ConstraintAnchor.Type.LEFT || object3 == ConstraintAnchor.Type.RIGHT)) {
            object = this.getAnchor(ConstraintAnchor.Type.LEFT);
            object2 = object2.getAnchor((ConstraintAnchor.Type)((Object)object3));
            object3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
            object.connect((ConstraintAnchor)object2, 0, n2);
            object3.connect((ConstraintAnchor)object2, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect((ConstraintAnchor)object2, 0, n2);
            return;
        }
        if (object == ConstraintAnchor.Type.CENTER_Y && (object3 == ConstraintAnchor.Type.TOP || object3 == ConstraintAnchor.Type.BOTTOM)) {
            object = object2.getAnchor((ConstraintAnchor.Type)((Object)object3));
            this.getAnchor(ConstraintAnchor.Type.TOP).connect((ConstraintAnchor)object, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect((ConstraintAnchor)object, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect((ConstraintAnchor)object, 0, n2);
            return;
        }
        if (object == ConstraintAnchor.Type.CENTER_X && object3 == ConstraintAnchor.Type.CENTER_X) {
            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(object2.getAnchor(ConstraintAnchor.Type.LEFT), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(object2.getAnchor(ConstraintAnchor.Type.RIGHT), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(object2.getAnchor((ConstraintAnchor.Type)((Object)object3)), 0, n2);
            return;
        }
        if (object == ConstraintAnchor.Type.CENTER_Y && object3 == ConstraintAnchor.Type.CENTER_Y) {
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(object2.getAnchor(ConstraintAnchor.Type.TOP), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(object2.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(object2.getAnchor((ConstraintAnchor.Type)((Object)object3)), 0, n2);
            return;
        }
        ConstraintAnchor constraintAnchor = this.getAnchor((ConstraintAnchor.Type)((Object)object));
        if (!constraintAnchor.isValidConnection((ConstraintAnchor)(object2 = object2.getAnchor((ConstraintAnchor.Type)((Object)object3))))) return;
        if (object == ConstraintAnchor.Type.BASELINE) {
            object = this.getAnchor(ConstraintAnchor.Type.TOP);
            object3 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (object != null) {
                object.reset();
            }
            if (object3 != null) {
                object3.reset();
            }
            n = 0;
        } else if (object != ConstraintAnchor.Type.TOP && object != ConstraintAnchor.Type.BOTTOM) {
            if (object == ConstraintAnchor.Type.LEFT || object == ConstraintAnchor.Type.RIGHT) {
                object3 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                if (object3.getTarget() != object2) {
                    object3.reset();
                }
                object = this.getAnchor((ConstraintAnchor.Type)((Object)object)).getOpposite();
                object3 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                if (object3.isConnected()) {
                    object.reset();
                    object3.reset();
                }
            }
        } else {
            object3 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
            if (object3 != null) {
                object3.reset();
            }
            if ((object3 = this.getAnchor(ConstraintAnchor.Type.CENTER)).getTarget() != object2) {
                object3.reset();
            }
            object = this.getAnchor((ConstraintAnchor.Type)((Object)object)).getOpposite();
            object3 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (object3.isConnected()) {
                object.reset();
                object3.reset();
            }
        }
        constraintAnchor.connect((ConstraintAnchor)object2, n, strength, n2);
        object2.getOwner().connectedTo(constraintAnchor.getOwner());
        return;
        catch (Throwable throwable) {
            throw throwable;
        }
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, 0);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, int n2) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, n2);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, ConstraintAnchor.Strength strength, int n2) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n, strength, n2);
            return;
        }
    }

    public void connectedTo(ConstraintWidget constraintWidget) {
    }

    public void disconnectUnlockedWidget(ConstraintWidget constraintWidget) {
        ArrayList<ConstraintAnchor> arrayList = this.getAnchors();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = arrayList.get(i);
            if (!constraintAnchor.isConnected() || constraintAnchor.getTarget().getOwner() != constraintWidget || constraintAnchor.getConnectionCreator() != 2) continue;
            constraintAnchor.reset();
        }
    }

    public void disconnectWidget(ConstraintWidget constraintWidget) {
        ArrayList<ConstraintAnchor> arrayList = this.getAnchors();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = arrayList.get(i);
            if (!constraintAnchor.isConnected() || constraintAnchor.getTarget().getOwner() != constraintWidget) continue;
            constraintAnchor.reset();
        }
    }

    public void forceUpdateDrawPosition() {
        int n = this.mX;
        int n2 = this.mY;
        int n3 = this.mX;
        int n4 = this.mWidth;
        int n5 = this.mY;
        int n6 = this.mHeight;
        this.mDrawX = n;
        this.mDrawY = n2;
        this.mDrawWidth = n3 + n4 - n;
        this.mDrawHeight = n5 + n6 - n2;
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                return null;
            }
            case 8: {
                return this.mCenter;
            }
            case 7: {
                return this.mCenterY;
            }
            case 6: {
                return this.mCenterX;
            }
            case 5: {
                return this.mBaseline;
            }
            case 4: {
                return this.mBottom;
            }
            case 3: {
                return this.mRight;
            }
            case 2: {
                return this.mTop;
            }
            case 1: 
        }
        return this.mLeft;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public int getBottom() {
        return this.getY() + this.mHeight;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public int getDrawBottom() {
        return this.getDrawY() + this.mDrawHeight;
    }

    public int getDrawHeight() {
        return this.mDrawHeight;
    }

    public int getDrawRight() {
        return this.getDrawX() + this.mDrawWidth;
    }

    public int getDrawWidth() {
        return this.mDrawWidth;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        Object object = null;
        if (this.isInHorizontalChain()) {
            Object object2 = this;
            while (object == null && object2 != null) {
                Object object3 = object2.getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor constraintAnchor = null;
                object3 = object3 == null ? null : object3.getTarget();
                object3 = object3 == null ? null : object3.getOwner();
                if (object3 == this.getParent()) {
                    return object2;
                }
                if (object3 != null) {
                    constraintAnchor = object3.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object2) {
                    object = object2;
                    continue;
                }
                object2 = object3;
            }
            return object;
        }
        return null;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mHorizontalDimensionBehaviour;
    }

    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }

    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }

    int getInternalDrawX() {
        return this.mDrawX;
    }

    int getInternalDrawY() {
        return this.mDrawY;
    }

    public int getLeft() {
        return this.getX();
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getOptimizerWrapHeight() {
        int n = this.mHeight;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultHeight == 1) {
                n = Math.max(this.mMatchConstraintMinHeight, n);
            } else if (this.mMatchConstraintMinHeight > 0) {
                this.mHeight = n = this.mMatchConstraintMinHeight;
            } else {
                n = 0;
            }
            int n2 = this.mMatchConstraintMaxHeight;
            if (n2 > 0 && n2 < n) {
                return this.mMatchConstraintMaxHeight;
            }
            return n;
        }
        return n;
    }

    public int getOptimizerWrapWidth() {
        int n = this.mWidth;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultWidth == 1) {
                n = Math.max(this.mMatchConstraintMinWidth, n);
            } else if (this.mMatchConstraintMinWidth > 0) {
                this.mWidth = n = this.mMatchConstraintMinWidth;
            } else {
                n = 0;
            }
            int n2 = this.mMatchConstraintMaxWidth;
            if (n2 > 0 && n2 < n) {
                return this.mMatchConstraintMaxWidth;
            }
            return n;
        }
        return n;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public int getRight() {
        return this.getX() + this.mWidth;
    }

    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget constraintWidget = this;
        while (constraintWidget.getParent() != null) {
            constraintWidget = constraintWidget.getParent();
        }
        if (constraintWidget instanceof WidgetContainer) {
            return (WidgetContainer)constraintWidget;
        }
        return null;
    }

    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }

    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }

    public int getTop() {
        return this.getY();
    }

    public String getType() {
        return this.mType;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        Object object = null;
        if (this.isInVerticalChain()) {
            Object object2 = this;
            while (object == null && object2 != null) {
                Object object3 = object2.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor constraintAnchor = null;
                object3 = object3 == null ? null : object3.getTarget();
                object3 = object3 == null ? null : object3.getOwner();
                if (object3 == this.getParent()) {
                    return object2;
                }
                if (object3 != null) {
                    constraintAnchor = object3.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object2) {
                    object = object2;
                    continue;
                }
                object2 = object3;
            }
            return object;
        }
        return null;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mVerticalDimensionBehaviour;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getX() {
        return this.mX;
    }

    public int getY() {
        return this.mY;
    }

    public boolean hasAncestor(ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = this.getParent();
        if (constraintWidget2 == constraintWidget) {
            return true;
        }
        if (constraintWidget2 == constraintWidget.getParent()) {
            return false;
        }
        while (constraintWidget2 != null) {
            if (constraintWidget2 == constraintWidget) {
                return true;
            }
            if (constraintWidget2 == constraintWidget.getParent()) {
                return true;
            }
            constraintWidget2 = constraintWidget2.getParent();
        }
        return false;
    }

    public boolean hasBaseline() {
        if (this.mBaselineDistance > 0) {
            return true;
        }
        return false;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n, int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }

    public boolean isInHorizontalChain() {
        block3 : {
            block2 : {
                if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) break block2;
                if (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != this.mRight) break block3;
            }
            return true;
        }
        return false;
    }

    public boolean isInVerticalChain() {
        block3 : {
            block2 : {
                if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) break block2;
                if (this.mBottom.mTarget == null || this.mBottom.mTarget.mTarget != this.mBottom) break block3;
            }
            return true;
        }
        return false;
    }

    public boolean isInsideConstraintLayout() {
        ConstraintWidget constraintWidget = this.getParent();
        if (constraintWidget == null) {
            return false;
        }
        while (constraintWidget != null) {
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                return true;
            }
            constraintWidget = constraintWidget.getParent();
        }
        return false;
    }

    public boolean isRoot() {
        if (this.mParent == null) {
            return true;
        }
        return false;
    }

    public boolean isRootContainer() {
        ConstraintWidget constraintWidget;
        if (this instanceof ConstraintWidgetContainer && ((constraintWidget = this.mParent) == null || !(constraintWidget instanceof ConstraintWidgetContainer))) {
            return true;
        }
        return false;
    }

    public void reset() {
        float f;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
    }

    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(DEFAULT_BIAS);
        this.setHorizontalBiasPercent(DEFAULT_BIAS);
        if (this instanceof ConstraintWidgetContainer) {
            return;
        }
        if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            } else if (this.getWidth() > this.getMinWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
        if (this.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getHeight() == this.getWrapHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                return;
            }
            if (this.getHeight() > this.getMinHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                return;
            }
            return;
        }
    }

    public void resetAnchor(ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        ConstraintAnchor constraintAnchor2 = this.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintAnchor constraintAnchor4 = this.getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor5 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        ConstraintAnchor constraintAnchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        ConstraintAnchor constraintAnchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        ConstraintAnchor constraintAnchor8 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == constraintAnchor6) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor7) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget().getOwner() == constraintAnchor3.getTarget().getOwner()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor8) {
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget().getOwner() == constraintAnchor5.getTarget().getOwner()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor != constraintAnchor2 && constraintAnchor != constraintAnchor3) {
            if (constraintAnchor == constraintAnchor4 || constraintAnchor == constraintAnchor5) {
                if (constraintAnchor4.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                    constraintAnchor6.reset();
                }
            }
        } else if (constraintAnchor2.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
            constraintAnchor6.reset();
        }
        constraintAnchor.reset();
    }

    public void resetAnchors() {
        ConstraintWidget constraintWidget = this.getParent();
        if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            this.mAnchors.get(i).reset();
        }
    }

    public void resetAnchors(int n) {
        Object object = this.getParent();
        if (object != null && object instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        int n2 = this.mAnchors.size();
        for (int i = 0; i < n2; ++i) {
            object = this.mAnchors.get(i);
            if (n != object.getConnectionCreator()) continue;
            if (object.isVerticalAnchor()) {
                this.setVerticalBiasPercent(DEFAULT_BIAS);
            } else {
                this.setHorizontalBiasPercent(DEFAULT_BIAS);
            }
            object.reset();
        }
    }

    public void resetGroups() {
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            this.mAnchors.get((int)i).mGroup = Integer.MAX_VALUE;
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public void setBaselineDistance(int n) {
        this.mBaselineDistance = n;
    }

    public void setCompanionWidget(Object object) {
        this.mCompanionWidget = object;
    }

    public void setContainerItemSkip(int n) {
        if (n >= 0) {
            this.mContainerItemSkip = n;
            return;
        }
        this.mContainerItemSkip = 0;
    }

    public void setDebugName(String string2) {
        this.mDebugName = string2;
    }

    public void setDebugSolverName(LinearSystem object, String string2) {
        this.mDebugName = string2;
        Object object2 = object.createObjectVariable(this.mLeft);
        Object object3 = object.createObjectVariable(this.mTop);
        Object object4 = object.createObjectVariable(this.mRight);
        Object object5 = object.createObjectVariable(this.mBottom);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(".left");
        object2.setName(stringBuilder.toString());
        object2 = new StringBuilder();
        object2.append(string2);
        object2.append(".top");
        object3.setName(object2.toString());
        object3 = new StringBuilder();
        object3.append(string2);
        object3.append(".right");
        object4.setName(object3.toString());
        object4 = new StringBuilder();
        object4.append(string2);
        object4.append(".bottom");
        object5.setName(object4.toString());
        if (this.mBaselineDistance > 0) {
            object = object.createObjectVariable(this.mBaseline);
            object5 = new StringBuilder();
            object5.append(string2);
            object5.append(".baseline");
            object.setName(object5.toString());
            return;
        }
    }

    public void setDimension(int n, int n2) {
        this.mWidth = n;
        int n3 = this.mMinWidth;
        if ((n = this.mWidth) < n3) {
            this.mWidth = n3;
        }
        n = this.mHeight = n2;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
            return;
        }
    }

    public void setDimensionRatio(float f, int n) {
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = n;
    }

    public void setDimensionRatio(String string2) {
        block17 : {
            int n;
            float f;
            block16 : {
                int n2;
                float f2;
                block18 : {
                    float f3;
                    block14 : {
                        float f4;
                        block15 : {
                            String string3;
                            if (string2 == null || string2.length() == 0) break block17;
                            n = -1;
                            f = 0.0f;
                            f2 = 0.0f;
                            f3 = 0.0f;
                            int n3 = string2.length();
                            n2 = string2.indexOf(44);
                            if (n2 > 0 && n2 < n3 - 1) {
                                string3 = string2.substring(0, n2);
                                if (string3.equalsIgnoreCase("W")) {
                                    n = 0;
                                } else if (string3.equalsIgnoreCase("H")) {
                                    n = 1;
                                }
                                ++n2;
                            } else {
                                n2 = 0;
                            }
                            int n4 = string2.indexOf(58);
                            if (n4 < 0 || n4 >= n3 - 1) break block18;
                            string3 = string2.substring(n2, n4);
                            string2 = string2.substring(n4 + 1);
                            if (string3.length() <= 0 || string2.length() <= 0) break block16;
                            try {
                                f2 = Float.parseFloat(string3);
                                f4 = Float.parseFloat(string2);
                                if (f2 <= 0.0f || f4 <= 0.0f) break block14;
                                if (n != 1) break block15;
                            }
                            catch (NumberFormatException numberFormatException) {}
                            f = f3 = Math.abs(f4 / f2);
                        }
                        f = f3 = Math.abs(f2 / f4);
                        break block16;
                    }
                    f = f3;
                    break block16;
                    break block16;
                }
                if ((string2 = string2.substring(n2)).length() > 0) {
                    try {
                        f = Float.parseFloat(string2);
                    }
                    catch (NumberFormatException numberFormatException) {
                        f = f2;
                    }
                } else {
                    f = f2;
                }
            }
            if (f > 0.0f) {
                this.mDimensionRatio = f;
                this.mDimensionRatioSide = n;
                return;
            }
            return;
        }
        this.mDimensionRatio = 0.0f;
    }

    public void setDrawHeight(int n) {
        this.mDrawHeight = n;
    }

    public void setDrawOrigin(int n, int n2) {
        this.mDrawX = n - this.mOffsetX;
        this.mDrawY = n2 - this.mOffsetY;
        this.mX = this.mDrawX;
        this.mY = this.mDrawY;
    }

    public void setDrawWidth(int n) {
        this.mDrawWidth = n;
    }

    public void setDrawX(int n) {
        this.mX = this.mDrawX = n - this.mOffsetX;
    }

    public void setDrawY(int n) {
        this.mY = this.mDrawY = n - this.mOffsetY;
    }

    public void setFrame(int n, int n2, int n3, int n4) {
        int n5 = n3 - n;
        n3 = n4 - n2;
        this.mX = n;
        this.mY = n2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        n = this.mHorizontalDimensionBehaviour == DimensionBehaviour.FIXED && n5 < this.mWidth ? this.mWidth : n5;
        n2 = this.mVerticalDimensionBehaviour == DimensionBehaviour.FIXED && n3 < this.mHeight ? this.mHeight : n3;
        this.mWidth = n;
        n = this.mHeight = n2;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
        if ((n = this.mWidth) < (n2 = this.mMinWidth)) {
            this.mWidth = n2;
            return;
        }
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int n) {
        switch (type) {
            default: {
                return;
            }
            case BOTTOM: {
                this.mBottom.mGoneMargin = n;
                return;
            }
            case RIGHT: {
                this.mRight.mGoneMargin = n;
                return;
            }
            case TOP: {
                this.mTop.mGoneMargin = n;
                return;
            }
            case LEFT: 
        }
        this.mLeft.mGoneMargin = n;
    }

    public void setHeight(int n) {
        this.mHeight = n;
        int n2 = this.mMinHeight;
        if ((n = this.mHeight) < n2) {
            this.mHeight = n2;
            return;
        }
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setHorizontalChainStyle(int n) {
        this.mHorizontalChainStyle = n;
    }

    public void setHorizontalDimension(int n, int n2) {
        this.mX = n;
        n = this.mWidth = n2 - n;
        n2 = this.mMinWidth;
        if (n < n2) {
            this.mWidth = n2;
            return;
        }
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mHorizontalDimensionBehaviour = dimensionBehaviour;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
            return;
        }
    }

    public void setHorizontalMatchStyle(int n, int n2, int n3) {
        this.mMatchConstraintDefaultWidth = n;
        this.mMatchConstraintMinWidth = n2;
        this.mMatchConstraintMaxWidth = n3;
    }

    public void setHorizontalWeight(float f) {
        this.mHorizontalWeight = f;
    }

    public void setMinHeight(int n) {
        if (n < 0) {
            this.mMinHeight = 0;
            return;
        }
        this.mMinHeight = n;
    }

    public void setMinWidth(int n) {
        if (n < 0) {
            this.mMinWidth = 0;
            return;
        }
        this.mMinWidth = n;
    }

    public void setOffset(int n, int n2) {
        this.mOffsetX = n;
        this.mOffsetY = n2;
    }

    public void setOrigin(int n, int n2) {
        this.mX = n;
        this.mY = n2;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void setType(String string2) {
        this.mType = string2;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setVerticalChainStyle(int n) {
        this.mVerticalChainStyle = n;
    }

    public void setVerticalDimension(int n, int n2) {
        this.mY = n;
        n = this.mHeight = n2 - n;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
            return;
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mVerticalDimensionBehaviour = dimensionBehaviour;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
            return;
        }
    }

    public void setVerticalMatchStyle(int n, int n2, int n3) {
        this.mMatchConstraintDefaultHeight = n;
        this.mMatchConstraintMinHeight = n2;
        this.mMatchConstraintMaxHeight = n3;
    }

    public void setVerticalWeight(float f) {
        this.mVerticalWeight = f;
    }

    public void setVisibility(int n) {
        this.mVisibility = n;
    }

    public void setWidth(int n) {
        this.mWidth = n;
        int n2 = this.mMinWidth;
        if ((n = this.mWidth) < n2) {
            this.mWidth = n2;
            return;
        }
    }

    public void setWrapHeight(int n) {
        this.mWrapHeight = n;
    }

    public void setWrapWidth(int n) {
        this.mWrapWidth = n;
    }

    public void setX(int n) {
        this.mX = n;
    }

    public void setY(int n) {
        this.mY = n;
    }

    public String toString() {
        CharSequence charSequence2;
        CharSequence charSequence2;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.mType != null) {
            charSequence2 = new StringBuilder();
            charSequence2.append("type: ");
            charSequence2.append(this.mType);
            charSequence2.append(" ");
            charSequence2 = charSequence2.toString();
        } else {
            charSequence2 = "";
        }
        stringBuilder.append((String)charSequence2);
        if (this.mDebugName != null) {
            charSequence2 = new StringBuilder();
            charSequence2.append("id: ");
            charSequence2.append(this.mDebugName);
            charSequence2.append(" ");
            charSequence2 = charSequence2.toString();
        } else {
            charSequence2 = "";
        }
        stringBuilder.append((String)charSequence2);
        stringBuilder.append("(");
        stringBuilder.append(this.mX);
        stringBuilder.append(", ");
        stringBuilder.append(this.mY);
        stringBuilder.append(") - (");
        stringBuilder.append(this.mWidth);
        stringBuilder.append(" x ");
        stringBuilder.append(this.mHeight);
        stringBuilder.append(")");
        stringBuilder.append(" wrap: (");
        stringBuilder.append(this.mWrapWidth);
        stringBuilder.append(" x ");
        stringBuilder.append(this.mWrapHeight);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void updateDrawPosition() {
        int n = this.mX;
        int n2 = this.mY;
        int n3 = this.mX;
        int n4 = this.mWidth;
        int n5 = this.mY;
        int n6 = this.mHeight;
        this.mDrawX = n;
        this.mDrawY = n2;
        this.mDrawWidth = n3 + n4 - n;
        this.mDrawHeight = n5 + n6 - n2;
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        this.updateFromSolver(linearSystem, Integer.MAX_VALUE);
    }

    public void updateFromSolver(LinearSystem linearSystem, int n) {
        if (n == Integer.MAX_VALUE) {
            this.setFrame(linearSystem.getObjectVariableValue(this.mLeft), linearSystem.getObjectVariableValue(this.mTop), linearSystem.getObjectVariableValue(this.mRight), linearSystem.getObjectVariableValue(this.mBottom));
            return;
        }
        if (n == -2) {
            this.setFrame(this.mSolverLeft, this.mSolverTop, this.mSolverRight, this.mSolverBottom);
            return;
        }
        if (this.mLeft.mGroup == n) {
            this.mSolverLeft = linearSystem.getObjectVariableValue(this.mLeft);
        }
        if (this.mTop.mGroup == n) {
            this.mSolverTop = linearSystem.getObjectVariableValue(this.mTop);
        }
        if (this.mRight.mGroup == n) {
            this.mSolverRight = linearSystem.getObjectVariableValue(this.mRight);
        }
        if (this.mBottom.mGroup == n) {
            this.mSolverBottom = linearSystem.getObjectVariableValue(this.mBottom);
            return;
        }
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

    public static enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT;
        

        private DimensionBehaviour() {
        }
    }

}

