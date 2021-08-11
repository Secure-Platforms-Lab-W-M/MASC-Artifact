// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintWidget
{
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
    public int mHorizontalResolution;
    float mHorizontalWeight;
    boolean mHorizontalWrapVisited;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
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
    public int mVerticalResolution;
    float mVerticalWeight;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;
    
    static {
        ConstraintWidget.DEFAULT_BIAS = 0.5f;
    }
    
    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList<ConstraintAnchor>();
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
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
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
    
    public ConstraintWidget(final int n, final int n2) {
        this(0, 0, n, n2);
    }
    
    public ConstraintWidget(final int mx, final int my, final int mWidth, final int mHeight) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList<ConstraintAnchor>();
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
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
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
        this.mX = mx;
        this.mY = my;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
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
    
    private void applyConstraints(final LinearSystem linearSystem, final boolean b, boolean b2, final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, int n, final int n2, int n3, final int n4, final float n5, final boolean b3, final boolean b4, final int n6, final int n7, final int n8) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintAnchor);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(constraintAnchor2);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        final int margin = constraintAnchor.getMargin();
        final int margin2 = constraintAnchor2.getMargin();
        if (this.mVisibility == 8) {
            b2 = true;
            n3 = 0;
        }
        if (objectVariable3 == null && objectVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, n));
            if (b3) {
                return;
            }
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
                return;
            }
            if (b2) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n3, false));
                return;
            }
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, n2));
        }
        else if (objectVariable3 != null && objectVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
                return;
            }
            if (b3) {
                return;
            }
            if (b2) {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
                return;
            }
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, n2));
        }
        else if (objectVariable3 == null && objectVariable4 != null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, margin2 * -1));
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
                return;
            }
            if (b3) {
                return;
            }
            if (b2) {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
                return;
            }
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, n));
        }
        else if (b2) {
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
            }
            else {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
            }
            if (constraintAnchor.getStrength() != constraintAnchor2.getStrength()) {
                if (constraintAnchor.getStrength() == ConstraintAnchor.Strength.STRONG) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
                    final SolverVariable slackVariable = linearSystem.createSlackVariable();
                    final ArrayRow row = linearSystem.createRow();
                    row.createRowLowerThan(objectVariable2, objectVariable4, slackVariable, margin2 * -1);
                    linearSystem.addConstraint(row);
                    return;
                }
                final SolverVariable slackVariable2 = linearSystem.createSlackVariable();
                final ArrayRow row2 = linearSystem.createRow();
                row2.createRowGreaterThan(objectVariable, objectVariable3, slackVariable2, margin);
                linearSystem.addConstraint(row2);
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, margin2 * -1));
            }
            else {
                if (objectVariable3 == objectVariable4) {
                    linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, 0, 0.5f, objectVariable4, objectVariable2, 0, true));
                    return;
                }
                if (!b4) {
                    linearSystem.addConstraint(LinearSystem.createRowGreaterThan(linearSystem, objectVariable, objectVariable3, margin, constraintAnchor.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT));
                    linearSystem.addConstraint(LinearSystem.createRowLowerThan(linearSystem, objectVariable2, objectVariable4, margin2 * -1, constraintAnchor2.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT));
                    linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, false));
                }
            }
        }
        else {
            if (b3) {
                linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 3);
                linearSystem.addLowerThan(objectVariable2, objectVariable4, margin2 * -1, 3);
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, true));
                return;
            }
            if (b4) {
                return;
            }
            if (n6 == 1) {
                if (n7 > n3) {
                    n = n7;
                }
                else {
                    n = n3;
                }
                if (n8 > 0) {
                    if (n8 < n) {
                        n = n8;
                    }
                    else {
                        linearSystem.addLowerThan(objectVariable2, objectVariable, n8, 3);
                    }
                }
                linearSystem.addEquality(objectVariable2, objectVariable, n, 3);
                linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 2);
                linearSystem.addLowerThan(objectVariable2, objectVariable4, -margin2, 2);
                linearSystem.addCentering(objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, 4);
                return;
            }
            if (n7 == 0 && n8 == 0) {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, margin2 * -1));
                return;
            }
            if (n8 > 0) {
                linearSystem.addLowerThan(objectVariable2, objectVariable, n8, 3);
            }
            linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 2);
            linearSystem.addLowerThan(objectVariable2, objectVariable4, -margin2, 2);
            linearSystem.addCentering(objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, 4);
        }
    }
    
    public void addToSolver(final LinearSystem linearSystem) {
        this.addToSolver(linearSystem, Integer.MAX_VALUE);
    }
    
    public void addToSolver(final LinearSystem linearSystem, int n) {
        SolverVariable objectVariable = null;
        SolverVariable objectVariable2 = null;
        if (n == Integer.MAX_VALUE || this.mLeft.mGroup == n) {
            objectVariable = linearSystem.createObjectVariable(this.mLeft);
        }
        if (n == Integer.MAX_VALUE || this.mRight.mGroup == n) {
            objectVariable2 = linearSystem.createObjectVariable(this.mRight);
        }
        SolverVariable objectVariable3;
        if (n != Integer.MAX_VALUE && this.mTop.mGroup != n) {
            objectVariable3 = null;
        }
        else {
            objectVariable3 = linearSystem.createObjectVariable(this.mTop);
        }
        SolverVariable objectVariable4;
        if (n != Integer.MAX_VALUE && this.mBottom.mGroup != n) {
            objectVariable4 = null;
        }
        else {
            objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        }
        SolverVariable objectVariable5;
        if (n != Integer.MAX_VALUE && this.mBaseline.mGroup != n) {
            objectVariable5 = null;
        }
        else {
            objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
        }
        boolean b = false;
        boolean b2 = false;
        boolean b3;
        boolean b4;
        if (this.mParent != null) {
            if ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                b = true;
            }
            if ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                b2 = true;
            }
            if (this.mParent.getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && !b) {
                if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                    if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                        this.mLeft.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable6 = linearSystem.createObjectVariable(this.mParent.mLeft);
                    final ArrayRow row = linearSystem.createRow();
                    row.createRowGreaterThan(objectVariable, objectVariable6, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row);
                }
                if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                    if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                        this.mRight.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable7 = linearSystem.createObjectVariable(this.mParent.mRight);
                    final ArrayRow row2 = linearSystem.createRow();
                    row2.createRowGreaterThan(objectVariable7, objectVariable2, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row2);
                }
            }
            if (this.mParent.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && !b2) {
                if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                    if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                        this.mTop.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable8 = linearSystem.createObjectVariable(this.mParent.mTop);
                    final ArrayRow row3 = linearSystem.createRow();
                    row3.createRowGreaterThan(objectVariable3, objectVariable8, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row3);
                }
                if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                    if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                        this.mBottom.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable9 = linearSystem.createObjectVariable(this.mParent.mBottom);
                    final ArrayRow row4 = linearSystem.createRow();
                    row4.createRowGreaterThan(objectVariable9, objectVariable4, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row4);
                }
            }
            b3 = b;
            b4 = b2;
        }
        else {
            b3 = false;
            b4 = false;
        }
        int n2 = this.mWidth;
        if (n2 < this.mMinWidth) {
            n2 = this.mMinWidth;
        }
        int n3 = this.mHeight;
        if (n3 < this.mMinHeight) {
            n3 = this.mMinHeight;
        }
        boolean b5 = this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
        boolean b6 = this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
        if (!b5) {
            final ConstraintAnchor mLeft = this.mLeft;
            if (mLeft != null && this.mRight != null && (mLeft.mTarget == null || this.mRight.mTarget == null)) {
                b5 = true;
            }
        }
        Label_1085: {
            if (!b6) {
                final ConstraintAnchor mTop = this.mTop;
                if (mTop != null && this.mBottom != null) {
                    if (mTop.mTarget == null || this.mBottom.mTarget == null) {
                        Label_1079: {
                            if (this.mBaselineDistance != 0) {
                                if (this.mBaseline != null) {
                                    if (this.mTop.mTarget == null) {
                                        break Label_1079;
                                    }
                                    if (this.mBaseline.mTarget == null) {
                                        break Label_1079;
                                    }
                                }
                                break Label_1085;
                            }
                        }
                        b6 = true;
                    }
                }
            }
        }
        boolean b7 = false;
        final int mDimensionRatioSide = this.mDimensionRatioSide;
        float mDimensionRatio = this.mDimensionRatio;
        int n4 = 0;
        int n5 = 0;
        Label_1308: {
            if (this.mDimensionRatio > 0.0f && this.mVisibility != 8) {
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    b7 = true;
                    if (b5 && !b6) {
                        n4 = n2;
                        b7 = true;
                        n5 = 0;
                        break Label_1308;
                    }
                    if (!b5 && b6) {
                        if (this.mDimensionRatioSide == -1) {
                            mDimensionRatio = 1.0f / mDimensionRatio;
                            n4 = n2;
                            n5 = 1;
                            b7 = true;
                            break Label_1308;
                        }
                        n4 = n2;
                        b7 = true;
                        n5 = 1;
                        break Label_1308;
                    }
                }
                else {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        n4 = (int)(this.mHeight * mDimensionRatio);
                        b5 = true;
                        b7 = false;
                        n5 = 0;
                        break Label_1308;
                    }
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (this.mDimensionRatioSide == -1) {
                            mDimensionRatio = 1.0f / mDimensionRatio;
                        }
                        n3 = (int)(this.mWidth * mDimensionRatio);
                        n4 = n2;
                        b6 = true;
                        b7 = false;
                        n5 = 1;
                        break Label_1308;
                    }
                }
            }
            n4 = n2;
            n5 = mDimensionRatioSide;
        }
        final boolean b8 = b7 && (n5 == 0 || n5 == -1);
        final boolean b9 = this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        if (this.mHorizontalResolution != 2) {
            if (n == Integer.MAX_VALUE || (this.mLeft.mGroup == n && this.mRight.mGroup == n)) {
                if (b8 && this.mLeft.mTarget != null && this.mRight.mTarget != null) {
                    final SolverVariable objectVariable10 = linearSystem.createObjectVariable(this.mLeft);
                    final SolverVariable objectVariable11 = linearSystem.createObjectVariable(this.mRight);
                    final SolverVariable objectVariable12 = linearSystem.createObjectVariable(this.mLeft.getTarget());
                    final SolverVariable objectVariable13 = linearSystem.createObjectVariable(this.mRight.getTarget());
                    linearSystem.addGreaterThan(objectVariable10, objectVariable12, this.mLeft.getMargin(), 3);
                    linearSystem.addLowerThan(objectVariable11, objectVariable13, this.mRight.getMargin() * -1, 3);
                    if (!b3) {
                        linearSystem.addCentering(objectVariable10, objectVariable12, this.mLeft.getMargin(), this.mHorizontalBiasPercent, objectVariable13, objectVariable11, this.mRight.getMargin(), 4);
                    }
                }
                else {
                    final ConstraintAnchor mLeft2 = this.mLeft;
                    final ConstraintAnchor mRight = this.mRight;
                    final int mx = this.mX;
                    this.applyConstraints(linearSystem, b9, b5, mLeft2, mRight, mx, mx + n4, n4, this.mMinWidth, this.mHorizontalBiasPercent, b8, b3, this.mMatchConstraintDefaultWidth, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth);
                }
            }
        }
        final SolverVariable solverVariable = objectVariable2;
        final SolverVariable solverVariable2 = objectVariable;
        boolean b10 = false;
        if (this.mVerticalResolution == 2) {
            return;
        }
        final boolean b11 = this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        if (b7) {
            final int n6 = n5;
            if (n6 == 1 || n6 == -1) {
                b10 = true;
            }
        }
        final int n7 = n5;
        SolverVariable solverVariable4;
        ConstraintWidget constraintWidget;
        LinearSystem linearSystem3;
        SolverVariable solverVariable6;
        if (this.mBaselineDistance > 0) {
            final ConstraintAnchor mBottom = this.mBottom;
            if (n == Integer.MAX_VALUE || (this.mBottom.mGroup == n && this.mBaseline.mGroup == n)) {
                linearSystem.addEquality(objectVariable5, objectVariable3, this.getBaselineDistance(), 5);
            }
            int mBaselineDistance;
            ConstraintAnchor mBaseline;
            if (this.mBaseline.mTarget != null) {
                mBaselineDistance = this.mBaselineDistance;
                mBaseline = this.mBaseline;
            }
            else {
                mBaselineDistance = n3;
                mBaseline = mBottom;
            }
            LinearSystem linearSystem2;
            if (n != Integer.MAX_VALUE && (this.mTop.mGroup != n || mBaseline.mGroup != n)) {
                final SolverVariable solverVariable3 = objectVariable3;
                linearSystem2 = linearSystem;
                solverVariable4 = solverVariable3;
            }
            else if (b10 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                final SolverVariable objectVariable14 = linearSystem.createObjectVariable(this.mTop);
                final SolverVariable objectVariable15 = linearSystem.createObjectVariable(this.mBottom);
                final SolverVariable objectVariable16 = linearSystem.createObjectVariable(this.mTop.getTarget());
                final SolverVariable objectVariable17 = linearSystem.createObjectVariable(this.mBottom.getTarget());
                linearSystem.addGreaterThan(objectVariable14, objectVariable16, this.mTop.getMargin(), 3);
                linearSystem.addLowerThan(objectVariable15, objectVariable17, this.mBottom.getMargin() * -1, 3);
                if (!b4) {
                    linearSystem.addCentering(objectVariable14, objectVariable16, this.mTop.getMargin(), this.mVerticalBiasPercent, objectVariable17, objectVariable15, this.mBottom.getMargin(), 4);
                }
                solverVariable4 = objectVariable3;
                linearSystem2 = linearSystem;
            }
            else {
                final ConstraintAnchor mTop2 = this.mTop;
                final int my = this.mY;
                this.applyConstraints(linearSystem, b11, b6, mTop2, mBaseline, my, my + mBaselineDistance, mBaselineDistance, this.mMinHeight, this.mVerticalBiasPercent, b10, b4, this.mMatchConstraintDefaultHeight, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight);
                linearSystem.addEquality(objectVariable4, objectVariable3, n3, 5);
                solverVariable4 = objectVariable3;
                linearSystem2 = linearSystem;
            }
            final SolverVariable solverVariable5 = objectVariable4;
            constraintWidget = this;
            linearSystem3 = linearSystem2;
            solverVariable6 = solverVariable5;
        }
        else {
            linearSystem3 = linearSystem;
            if (n != Integer.MAX_VALUE && (this.mTop.mGroup != n || this.mBottom.mGroup != n)) {
                final SolverVariable solverVariable7 = objectVariable4;
                final SolverVariable solverVariable8 = objectVariable3;
                constraintWidget = this;
                solverVariable6 = solverVariable7;
                solverVariable4 = solverVariable8;
            }
            else if (b10 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                final SolverVariable objectVariable18 = linearSystem3.createObjectVariable(this.mTop);
                final SolverVariable objectVariable19 = linearSystem3.createObjectVariable(this.mBottom);
                final SolverVariable objectVariable20 = linearSystem3.createObjectVariable(this.mTop.getTarget());
                final SolverVariable objectVariable21 = linearSystem3.createObjectVariable(this.mBottom.getTarget());
                linearSystem3.addGreaterThan(objectVariable18, objectVariable20, this.mTop.getMargin(), 3);
                linearSystem3.addLowerThan(objectVariable19, objectVariable21, this.mBottom.getMargin() * -1, 3);
                if (!b4) {
                    linearSystem.addCentering(objectVariable18, objectVariable20, this.mTop.getMargin(), this.mVerticalBiasPercent, objectVariable21, objectVariable19, this.mBottom.getMargin(), 4);
                }
                final SolverVariable solverVariable9 = objectVariable4;
                final SolverVariable solverVariable10 = objectVariable3;
                constraintWidget = this;
                solverVariable6 = solverVariable9;
                solverVariable4 = solverVariable10;
            }
            else {
                final ConstraintAnchor mTop3 = this.mTop;
                final ConstraintAnchor mBottom2 = this.mBottom;
                final int my2 = this.mY;
                this.applyConstraints(linearSystem, b11, b6, mTop3, mBottom2, my2, my2 + n3, n3, this.mMinHeight, this.mVerticalBiasPercent, b10, b4, this.mMatchConstraintDefaultHeight, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight);
                solverVariable4 = objectVariable3;
                solverVariable6 = objectVariable4;
                constraintWidget = this;
            }
        }
        if (!b7) {
            return;
        }
        final ArrayRow row5 = linearSystem.createRow();
        if (n != Integer.MAX_VALUE && (constraintWidget.mLeft.mGroup != n || constraintWidget.mRight.mGroup != n)) {
            return;
        }
        if (n7 == 0) {
            linearSystem3.addConstraint(row5.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable6, solverVariable4, mDimensionRatio));
            return;
        }
        if (n7 == 1) {
            linearSystem3.addConstraint(row5.createRowDimensionRatio(solverVariable6, solverVariable4, solverVariable, solverVariable2, mDimensionRatio));
            return;
        }
        n = constraintWidget.mMatchConstraintMinWidth;
        if (n > 0) {
            linearSystem3.addGreaterThan(solverVariable, solverVariable2, n, 3);
        }
        n = constraintWidget.mMatchConstraintMinHeight;
        if (n > 0) {
            linearSystem3.addGreaterThan(solverVariable6, solverVariable4, n, 3);
        }
        row5.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable6, solverVariable4, mDimensionRatio);
        final SolverVariable errorVariable = linearSystem.createErrorVariable();
        final SolverVariable errorVariable2 = linearSystem.createErrorVariable();
        errorVariable.strength = 4;
        errorVariable2.strength = 4;
        row5.addError(errorVariable, errorVariable2);
        linearSystem3.addConstraint(row5);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n) {
        this.connect(type, constraintWidget, type2, n, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final ConstraintAnchor.Strength strength) {
        this.connect(type, constraintWidget, type2, n, strength, 0);
    }
    
    public void connect(ConstraintAnchor.Type right, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type, int n, final ConstraintAnchor.Strength strength, final int n2) {
        Label_0424: {
            if (right != ConstraintAnchor.Type.CENTER) {
                break Label_0424;
            }
            if (type == ConstraintAnchor.Type.CENTER) {
                final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
                final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
                final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                n = 0;
                boolean b = false;
                if (anchor == null || !anchor.isConnected()) {
                    if (anchor2 == null || !anchor2.isConnected()) {
                        this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength, n2);
                        this.connect(ConstraintAnchor.Type.RIGHT, constraintWidget, ConstraintAnchor.Type.RIGHT, 0, strength, n2);
                        n = 1;
                    }
                }
                if (anchor3 == null || !anchor3.isConnected()) {
                    if (anchor4 == null || !anchor4.isConnected()) {
                        this.connect(ConstraintAnchor.Type.TOP, constraintWidget, ConstraintAnchor.Type.TOP, 0, strength, n2);
                        this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, ConstraintAnchor.Type.BOTTOM, 0, strength, n2);
                        b = true;
                    }
                }
                if (n != 0 && b) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, n2);
                    return;
                }
                if (n != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, n2);
                    return;
                }
                if (b) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, n2);
                }
                return;
            }
            if (type != ConstraintAnchor.Type.LEFT && type != ConstraintAnchor.Type.RIGHT) {
                if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                    this.connect(ConstraintAnchor.Type.TOP, constraintWidget, type, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type, 0, strength, n2);
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type), 0, n2);
                }
                return;
            }
            this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, type, 0, strength, n2);
            right = ConstraintAnchor.Type.RIGHT;
            try {
                this.connect(right, constraintWidget, type, 0, strength, n2);
                this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type), 0, n2);
                Label_1011: {
                    return;
                }
                // iftrue(Label_0902:, !anchor7.isConnected())
                // iftrue(Label_0982:, !anchor8.isConnected())
                // iftrue(Label_0503:, right != ConstraintAnchor.Type.CENTER_X || type != ConstraintAnchor.Type.LEFT && type != ConstraintAnchor.Type.RIGHT)
                // iftrue(Label_0905:, right == ConstraintAnchor.Type.TOP || right == ConstraintAnchor.Type.BOTTOM)
                // iftrue(Label_0790:, anchor9 == null)
                // iftrue(Label_1011:, !anchor11.isValidConnection(anchor12))
                // iftrue(Label_0844:, right == ConstraintAnchor.Type.LEFT || right == ConstraintAnchor.Type.RIGHT)
                // iftrue(Label_0656:, right != ConstraintAnchor.Type.CENTER_X || type != ConstraintAnchor.Type.CENTER_X)
                // iftrue(Label_0867:, anchor13.getTarget() == anchor12)
                // iftrue(Label_0578:, right != ConstraintAnchor.Type.CENTER_Y || type != ConstraintAnchor.Type.TOP && type != ConstraintAnchor.Type.BOTTOM)
                // iftrue(Label_0807:, right != ConstraintAnchor.Type.BASELINE)
                // iftrue(Label_0801:, anchor10 == null)
                // iftrue(Label_0924:, anchor6 == null)
                // iftrue(Label_0734:, right != ConstraintAnchor.Type.CENTER_Y || type != ConstraintAnchor.Type.CENTER_Y)
                // iftrue(Label_0947:, anchor14.getTarget() == anchor12)
            Label_0985:
                while (true) {
                    ConstraintAnchor anchor5;
                    ConstraintAnchor anchor6;
                    ConstraintAnchor opposite = null;
                    ConstraintAnchor anchor7 = null;
                    ConstraintAnchor opposite2 = null;
                    ConstraintAnchor anchor8 = null;
                    ConstraintAnchor anchor9 = null;
                    ConstraintAnchor anchor10;
                    ConstraintAnchor anchor11;
                    ConstraintAnchor anchor12;
                    ConstraintAnchor anchor13 = null;
                    ConstraintAnchor anchor14 = null;
                    ConstraintAnchor anchor15;
                    ConstraintAnchor anchor16;
                    ConstraintAnchor anchor17;
                    Block_37:Label_0947_Outer:
                    while (true) {
                        Block_36: {
                            Block_25: {
                                Label_0445:Label_0841_Outer:Label_0790_Outer:
                                while (true) {
                                Block_39:
                                    while (true) {
                                        Block_30: {
                                        Label_0841:
                                            while (true) {
                                                Label_0924: {
                                                    while (true) {
                                                        Block_40:Block_31_Outer:
                                                        while (true) {
                                                            while (true) {
                                                                while (true) {
                                                                    Block_28: {
                                                                    Label_0801:
                                                                        while (true) {
                                                                            anchor5 = constraintWidget.getAnchor(type);
                                                                            this.getAnchor(ConstraintAnchor.Type.TOP).connect(anchor5, 0, n2);
                                                                            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor5, 0, n2);
                                                                            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(anchor5, 0, n2);
                                                                            return;
                                                                            anchor6.reset();
                                                                            break Label_0924;
                                                                            opposite = this.getAnchor(right).getOpposite();
                                                                            anchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                                                                            break Block_37;
                                                                            opposite2 = this.getAnchor(right).getOpposite();
                                                                            anchor8 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
                                                                            break Block_40;
                                                                            break Label_0445;
                                                                            Label_0807:
                                                                            Label_0824: {
                                                                                break Label_0824;
                                                                                anchor9 = this.getAnchor(ConstraintAnchor.Type.TOP);
                                                                                anchor10 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                                                                                break Block_30;
                                                                                anchor10.reset();
                                                                                break Label_0801;
                                                                                Label_0734:
                                                                                anchor11 = this.getAnchor(right);
                                                                                anchor12 = constraintWidget.getAnchor(type);
                                                                                break Block_28;
                                                                            }
                                                                            break Label_0841;
                                                                            Label_0578:
                                                                            break Block_25;
                                                                            Label_0844:
                                                                            anchor13 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                                                                            break Block_36;
                                                                            this.getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP), 0, n2);
                                                                            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, n2);
                                                                            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(type), 0, n2);
                                                                            return;
                                                                            Label_0503:
                                                                            continue Label_0947_Outer;
                                                                        }
                                                                        n = 0;
                                                                        Label_0902:
                                                                        break Label_0985;
                                                                    }
                                                                    continue Block_31_Outer;
                                                                }
                                                                continue Label_0841_Outer;
                                                            }
                                                            Label_0905:
                                                            anchor6 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
                                                            continue Label_0947_Outer;
                                                        }
                                                        opposite2.reset();
                                                        anchor8.reset();
                                                        break Label_0841;
                                                        Label_0656:
                                                        continue Label_0790_Outer;
                                                    }
                                                    break Label_0985;
                                                }
                                                anchor14 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                                                break Block_39;
                                                Label_0982:
                                                continue Label_0841;
                                            }
                                        }
                                        anchor9.reset();
                                        continue;
                                    }
                                    anchor14.reset();
                                    continue Label_0790_Outer;
                                }
                                anchor15 = this.getAnchor(ConstraintAnchor.Type.LEFT);
                                anchor16 = constraintWidget.getAnchor(type);
                                anchor17 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                                anchor15.connect(anchor16, 0, n2);
                                anchor17.connect(anchor16, 0, n2);
                                this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(anchor16, 0, n2);
                                return;
                                anchor11.connect(anchor12, n, strength, n2);
                                anchor12.getOwner().connectedTo(anchor11.getOwner());
                                return;
                            }
                            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT), 0, n2);
                            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT), 0, n2);
                            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(type), 0, n2);
                            return;
                        }
                        anchor13.reset();
                        continue Label_0947_Outer;
                    }
                    opposite.reset();
                    anchor7.reset();
                    continue Label_0985;
                }
            }
            catch (Throwable t) {
                throw t;
            }
        }
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, 0);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final int n2) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, n2);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final ConstraintAnchor.Strength strength, final int n2) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n, strength, n2);
        }
    }
    
    public void connectedTo(final ConstraintWidget constraintWidget) {
    }
    
    public void disconnectUnlockedWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget) {
                if (constraintAnchor.getConnectionCreator() == 2) {
                    constraintAnchor.reset();
                }
            }
        }
    }
    
    public void disconnectWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget) {
                constraintAnchor.reset();
            }
        }
    }
    
    public void forceUpdateDrawPosition() {
        final int mx = this.mX;
        final int my = this.mY;
        final int mx2 = this.mX;
        final int mWidth = this.mWidth;
        final int my2 = this.mY;
        final int mHeight = this.mHeight;
        this.mDrawX = mx;
        this.mDrawY = my;
        this.mDrawWidth = mx2 + mWidth - mx;
        this.mDrawHeight = my2 + mHeight - my;
    }
    
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (type) {
            default: {
                return null;
            }
            case CENTER: {
                return this.mCenter;
            }
            case CENTER_Y: {
                return this.mCenterY;
            }
            case CENTER_X: {
                return this.mCenterX;
            }
            case BASELINE: {
                return this.mBaseline;
            }
            case BOTTOM: {
                return this.mBottom;
            }
            case RIGHT: {
                return this.mRight;
            }
            case TOP: {
                return this.mTop;
            }
            case LEFT: {
                return this.mLeft;
            }
        }
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
        ConstraintWidget constraintWidget = null;
        if (this.isInHorizontalChain()) {
            ConstraintWidget constraintWidget2 = this;
            while (constraintWidget == null && constraintWidget2 != null) {
                final ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor target = null;
                ConstraintAnchor target2;
                if (anchor == null) {
                    target2 = null;
                }
                else {
                    target2 = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target2 == null) {
                    owner = null;
                }
                else {
                    owner = target2.getOwner();
                }
                if (owner == this.getParent()) {
                    return constraintWidget2;
                }
                if (owner != null) {
                    target = owner.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (target != null && target.getOwner() != constraintWidget2) {
                    constraintWidget = constraintWidget2;
                }
                else {
                    constraintWidget2 = owner;
                }
            }
            return constraintWidget;
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
        final int mHeight = this.mHeight;
        if (this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
            return mHeight;
        }
        int mHeight2;
        if (this.mMatchConstraintDefaultHeight == 1) {
            mHeight2 = Math.max(this.mMatchConstraintMinHeight, mHeight);
        }
        else if (this.mMatchConstraintMinHeight > 0) {
            mHeight2 = this.mMatchConstraintMinHeight;
            this.mHeight = mHeight2;
        }
        else {
            mHeight2 = 0;
        }
        final int mMatchConstraintMaxHeight = this.mMatchConstraintMaxHeight;
        if (mMatchConstraintMaxHeight > 0 && mMatchConstraintMaxHeight < mHeight2) {
            return this.mMatchConstraintMaxHeight;
        }
        return mHeight2;
    }
    
    public int getOptimizerWrapWidth() {
        final int mWidth = this.mWidth;
        if (this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
            return mWidth;
        }
        int mWidth2;
        if (this.mMatchConstraintDefaultWidth == 1) {
            mWidth2 = Math.max(this.mMatchConstraintMinWidth, mWidth);
        }
        else if (this.mMatchConstraintMinWidth > 0) {
            mWidth2 = this.mMatchConstraintMinWidth;
            this.mWidth = mWidth2;
        }
        else {
            mWidth2 = 0;
        }
        final int mMatchConstraintMaxWidth = this.mMatchConstraintMaxWidth;
        if (mMatchConstraintMaxWidth > 0 && mMatchConstraintMaxWidth < mWidth2) {
            return this.mMatchConstraintMaxWidth;
        }
        return mWidth2;
    }
    
    public ConstraintWidget getParent() {
        return this.mParent;
    }
    
    public int getRight() {
        return this.getX() + this.mWidth;
    }
    
    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget parent;
        for (parent = this; parent.getParent() != null; parent = parent.getParent()) {}
        if (parent instanceof WidgetContainer) {
            return (WidgetContainer)parent;
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
        ConstraintWidget constraintWidget = null;
        if (this.isInVerticalChain()) {
            ConstraintWidget constraintWidget2 = this;
            while (constraintWidget == null && constraintWidget2 != null) {
                final ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor target = null;
                ConstraintAnchor target2;
                if (anchor == null) {
                    target2 = null;
                }
                else {
                    target2 = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target2 == null) {
                    owner = null;
                }
                else {
                    owner = target2.getOwner();
                }
                if (owner == this.getParent()) {
                    return constraintWidget2;
                }
                if (owner != null) {
                    target = owner.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (target != null && target.getOwner() != constraintWidget2) {
                    constraintWidget = constraintWidget2;
                }
                else {
                    constraintWidget2 = owner;
                }
            }
            return constraintWidget;
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
    
    public boolean hasAncestor(final ConstraintWidget constraintWidget) {
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
        return this.mBaselineDistance > 0;
    }
    
    public void immediateConnect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }
    
    public boolean isInHorizontalChain() {
        return (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
    }
    
    public boolean isInVerticalChain() {
        return (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
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
        return this.mParent == null;
    }
    
    public boolean isRootContainer() {
        if (this instanceof ConstraintWidgetContainer) {
            final ConstraintWidget mParent = this.mParent;
            if (mParent == null || !(mParent instanceof ConstraintWidgetContainer)) {
                return true;
            }
        }
        return false;
    }
    
    public void reset() {
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
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
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
        this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        if (this instanceof ConstraintWidgetContainer) {
            return;
        }
        if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.getWidth() > this.getMinWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
        if (this.getVerticalDimensionBehaviour() != DimensionBehaviour.MATCH_CONSTRAINT) {
            return;
        }
        if (this.getHeight() == this.getWrapHeight()) {
            this.setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            return;
        }
        if (this.getHeight() > this.getMinHeight()) {
            this.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
        }
    }
    
    public void resetAnchor(final ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null) {
            if (this.getParent() instanceof ConstraintWidgetContainer) {
                if (((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
                    return;
                }
            }
        }
        final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
        final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
        final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        final ConstraintAnchor anchor5 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        final ConstraintAnchor anchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        final ConstraintAnchor anchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == anchor5) {
            if (anchor.isConnected() && anchor2.isConnected()) {
                if (anchor.getTarget() == anchor2.getTarget()) {
                    anchor.reset();
                    anchor2.reset();
                }
            }
            if (anchor3.isConnected() && anchor4.isConnected()) {
                if (anchor3.getTarget() == anchor4.getTarget()) {
                    anchor3.reset();
                    anchor4.reset();
                }
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor6) {
            if (anchor.isConnected() && anchor2.isConnected()) {
                if (anchor.getTarget().getOwner() == anchor2.getTarget().getOwner()) {
                    anchor.reset();
                    anchor2.reset();
                }
            }
            this.mHorizontalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor7) {
            if (anchor3.isConnected() && anchor4.isConnected()) {
                if (anchor3.getTarget().getOwner() == anchor4.getTarget().getOwner()) {
                    anchor3.reset();
                    anchor4.reset();
                }
            }
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor != anchor && constraintAnchor != anchor2) {
            if (constraintAnchor == anchor3 || constraintAnchor == anchor4) {
                if (anchor3.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                    anchor5.reset();
                }
            }
        }
        else if (anchor.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
            anchor5.reset();
        }
        constraintAnchor.reset();
    }
    
    public void resetAnchors() {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            this.mAnchors.get(i).reset();
        }
    }
    
    public void resetAnchors(final int n) {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = this.mAnchors.get(i);
            if (n == constraintAnchor.getConnectionCreator()) {
                if (constraintAnchor.isVerticalAnchor()) {
                    this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                else {
                    this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                constraintAnchor.reset();
            }
        }
    }
    
    public void resetGroups() {
        for (int size = this.mAnchors.size(), i = 0; i < size; ++i) {
            this.mAnchors.get(i).mGroup = Integer.MAX_VALUE;
        }
    }
    
    public void resetSolverVariables(final Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }
    
    public void setBaselineDistance(final int mBaselineDistance) {
        this.mBaselineDistance = mBaselineDistance;
    }
    
    public void setCompanionWidget(final Object mCompanionWidget) {
        this.mCompanionWidget = mCompanionWidget;
    }
    
    public void setContainerItemSkip(final int mContainerItemSkip) {
        if (mContainerItemSkip >= 0) {
            this.mContainerItemSkip = mContainerItemSkip;
            return;
        }
        this.mContainerItemSkip = 0;
    }
    
    public void setDebugName(final String mDebugName) {
        this.mDebugName = mDebugName;
    }
    
    public void setDebugSolverName(final LinearSystem linearSystem, final String mDebugName) {
        this.mDebugName = mDebugName;
        final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mLeft);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mTop);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(this.mRight);
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        final StringBuilder sb = new StringBuilder();
        sb.append(mDebugName);
        sb.append(".left");
        objectVariable.setName(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(mDebugName);
        sb2.append(".top");
        objectVariable2.setName(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(mDebugName);
        sb3.append(".right");
        objectVariable3.setName(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(mDebugName);
        sb4.append(".bottom");
        objectVariable4.setName(sb4.toString());
        if (this.mBaselineDistance > 0) {
            final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(mDebugName);
            sb5.append(".baseline");
            objectVariable5.setName(sb5.toString());
        }
    }
    
    public void setDimension(int mWidth, int mMinHeight) {
        this.mWidth = mWidth;
        mWidth = this.mWidth;
        final int mMinWidth = this.mMinWidth;
        if (mWidth < mMinWidth) {
            this.mWidth = mMinWidth;
        }
        this.mHeight = mMinHeight;
        mWidth = this.mHeight;
        mMinHeight = this.mMinHeight;
        if (mWidth < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setDimensionRatio(final float mDimensionRatio, final int mDimensionRatioSide) {
        this.mDimensionRatio = mDimensionRatio;
        this.mDimensionRatioSide = mDimensionRatioSide;
    }
    
    public void setDimensionRatio(String s) {
        if (s == null || s.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int mDimensionRatioSide = -1;
        float mDimensionRatio = 0.0f;
        final float n = 0.0f;
        final float n2 = 0.0f;
        final int length = s.length();
        final int index = s.indexOf(44);
        int n3;
        if (index > 0 && index < length - 1) {
            final String substring = s.substring(0, index);
            if (substring.equalsIgnoreCase("W")) {
                mDimensionRatioSide = 0;
            }
            else if (substring.equalsIgnoreCase("H")) {
                mDimensionRatioSide = 1;
            }
            n3 = index + 1;
        }
        else {
            n3 = 0;
        }
        final int index2 = s.indexOf(58);
        if (index2 >= 0 && index2 < length - 1) {
            final String substring2 = s.substring(n3, index2);
            s = s.substring(index2 + 1);
            if (substring2.length() > 0 && s.length() > 0) {
                try {
                    final float float1 = Float.parseFloat(substring2);
                    final float float2 = Float.parseFloat(s);
                    if (float1 > 0.0f && float2 > 0.0f) {
                        if (mDimensionRatioSide == 1) {
                            mDimensionRatio = Math.abs(float2 / float1);
                        }
                        else {
                            mDimensionRatio = Math.abs(float1 / float2);
                        }
                    }
                    else {
                        mDimensionRatio = n2;
                    }
                }
                catch (NumberFormatException ex) {}
            }
        }
        else {
            s = s.substring(n3);
            if (s.length() > 0) {
                try {
                    mDimensionRatio = Float.parseFloat(s);
                }
                catch (NumberFormatException ex2) {
                    mDimensionRatio = n;
                }
            }
            else {
                mDimensionRatio = n;
            }
        }
        if (mDimensionRatio > 0.0f) {
            this.mDimensionRatio = mDimensionRatio;
            this.mDimensionRatioSide = mDimensionRatioSide;
        }
    }
    
    public void setDrawHeight(final int mDrawHeight) {
        this.mDrawHeight = mDrawHeight;
    }
    
    public void setDrawOrigin(final int n, final int n2) {
        this.mDrawX = n - this.mOffsetX;
        this.mDrawY = n2 - this.mOffsetY;
        this.mX = this.mDrawX;
        this.mY = this.mDrawY;
    }
    
    public void setDrawWidth(final int mDrawWidth) {
        this.mDrawWidth = mDrawWidth;
    }
    
    public void setDrawX(final int n) {
        this.mDrawX = n - this.mOffsetX;
        this.mX = this.mDrawX;
    }
    
    public void setDrawY(final int n) {
        this.mDrawY = n - this.mOffsetY;
        this.mY = this.mDrawY;
    }
    
    public void setFrame(int n, int n2, int n3, final int n4) {
        final int n5 = n3 - n;
        n3 = n4 - n2;
        this.mX = n;
        this.mY = n2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.FIXED && n5 < this.mWidth) {
            n = this.mWidth;
        }
        else {
            n = n5;
        }
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.FIXED && n3 < this.mHeight) {
            n2 = this.mHeight;
        }
        else {
            n2 = n3;
        }
        this.mWidth = n;
        this.mHeight = n2;
        n = this.mHeight;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
        n = this.mWidth;
        n2 = this.mMinWidth;
        if (n < n2) {
            this.mWidth = n2;
        }
    }
    
    public void setGoneMargin(final ConstraintAnchor.Type type, final int n) {
        switch (type) {
            default: {}
            case BOTTOM: {
                this.mBottom.mGoneMargin = n;
            }
            case RIGHT: {
                this.mRight.mGoneMargin = n;
            }
            case TOP: {
                this.mTop.mGoneMargin = n;
            }
            case LEFT: {
                this.mLeft.mGoneMargin = n;
            }
        }
    }
    
    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        mHeight = this.mHeight;
        final int mMinHeight = this.mMinHeight;
        if (mHeight < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setHorizontalBiasPercent(final float mHorizontalBiasPercent) {
        this.mHorizontalBiasPercent = mHorizontalBiasPercent;
    }
    
    public void setHorizontalChainStyle(final int mHorizontalChainStyle) {
        this.mHorizontalChainStyle = mHorizontalChainStyle;
    }
    
    public void setHorizontalDimension(int mWidth, int mMinWidth) {
        this.mX = mWidth;
        this.mWidth = mMinWidth - mWidth;
        mWidth = this.mWidth;
        mMinWidth = this.mMinWidth;
        if (mWidth < mMinWidth) {
            this.mWidth = mMinWidth;
        }
    }
    
    public void setHorizontalDimensionBehaviour(final DimensionBehaviour mHorizontalDimensionBehaviour) {
        this.mHorizontalDimensionBehaviour = mHorizontalDimensionBehaviour;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
        }
    }
    
    public void setHorizontalMatchStyle(final int mMatchConstraintDefaultWidth, final int mMatchConstraintMinWidth, final int mMatchConstraintMaxWidth) {
        this.mMatchConstraintDefaultWidth = mMatchConstraintDefaultWidth;
        this.mMatchConstraintMinWidth = mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = mMatchConstraintMaxWidth;
    }
    
    public void setHorizontalWeight(final float mHorizontalWeight) {
        this.mHorizontalWeight = mHorizontalWeight;
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight < 0) {
            this.mMinHeight = 0;
            return;
        }
        this.mMinHeight = mMinHeight;
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth < 0) {
            this.mMinWidth = 0;
            return;
        }
        this.mMinWidth = mMinWidth;
    }
    
    public void setOffset(final int mOffsetX, final int mOffsetY) {
        this.mOffsetX = mOffsetX;
        this.mOffsetY = mOffsetY;
    }
    
    public void setOrigin(final int mx, final int my) {
        this.mX = mx;
        this.mY = my;
    }
    
    public void setParent(final ConstraintWidget mParent) {
        this.mParent = mParent;
    }
    
    public void setType(final String mType) {
        this.mType = mType;
    }
    
    public void setVerticalBiasPercent(final float mVerticalBiasPercent) {
        this.mVerticalBiasPercent = mVerticalBiasPercent;
    }
    
    public void setVerticalChainStyle(final int mVerticalChainStyle) {
        this.mVerticalChainStyle = mVerticalChainStyle;
    }
    
    public void setVerticalDimension(int mHeight, int mMinHeight) {
        this.mY = mHeight;
        this.mHeight = mMinHeight - mHeight;
        mHeight = this.mHeight;
        mMinHeight = this.mMinHeight;
        if (mHeight < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setVerticalDimensionBehaviour(final DimensionBehaviour mVerticalDimensionBehaviour) {
        this.mVerticalDimensionBehaviour = mVerticalDimensionBehaviour;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
        }
    }
    
    public void setVerticalMatchStyle(final int mMatchConstraintDefaultHeight, final int mMatchConstraintMinHeight, final int mMatchConstraintMaxHeight) {
        this.mMatchConstraintDefaultHeight = mMatchConstraintDefaultHeight;
        this.mMatchConstraintMinHeight = mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = mMatchConstraintMaxHeight;
    }
    
    public void setVerticalWeight(final float mVerticalWeight) {
        this.mVerticalWeight = mVerticalWeight;
    }
    
    public void setVisibility(final int mVisibility) {
        this.mVisibility = mVisibility;
    }
    
    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
        mWidth = this.mWidth;
        final int mMinWidth = this.mMinWidth;
        if (mWidth < mMinWidth) {
            this.mWidth = mMinWidth;
        }
    }
    
    public void setWrapHeight(final int mWrapHeight) {
        this.mWrapHeight = mWrapHeight;
    }
    
    public void setWrapWidth(final int mWrapWidth) {
        this.mWrapWidth = mWrapWidth;
    }
    
    public void setX(final int mx) {
        this.mX = mx;
    }
    
    public void setY(final int my) {
        this.mY = my;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String string;
        if (this.mType != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("type: ");
            sb2.append(this.mType);
            sb2.append(" ");
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        String string2;
        if (this.mDebugName != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("id: ");
            sb3.append(this.mDebugName);
            sb3.append(" ");
            string2 = sb3.toString();
        }
        else {
            string2 = "";
        }
        sb.append(string2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        sb.append(" wrap: (");
        sb.append(this.mWrapWidth);
        sb.append(" x ");
        sb.append(this.mWrapHeight);
        sb.append(")");
        return sb.toString();
    }
    
    public void updateDrawPosition() {
        final int mx = this.mX;
        final int my = this.mY;
        final int mx2 = this.mX;
        final int mWidth = this.mWidth;
        final int my2 = this.mY;
        final int mHeight = this.mHeight;
        this.mDrawX = mx;
        this.mDrawY = my;
        this.mDrawWidth = mx2 + mWidth - mx;
        this.mDrawHeight = my2 + mHeight - my;
    }
    
    public void updateFromSolver(final LinearSystem linearSystem) {
        this.updateFromSolver(linearSystem, Integer.MAX_VALUE);
    }
    
    public void updateFromSolver(final LinearSystem linearSystem, final int n) {
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
        }
    }
    
    public enum ContentAlignment
    {
        BEGIN, 
        BOTTOM, 
        END, 
        LEFT, 
        MIDDLE, 
        RIGHT, 
        TOP, 
        VERTICAL_MIDDLE;
    }
    
    public enum DimensionBehaviour
    {
        FIXED, 
        MATCH_CONSTRAINT, 
        MATCH_PARENT, 
        WRAP_CONTENT;
    }
}
