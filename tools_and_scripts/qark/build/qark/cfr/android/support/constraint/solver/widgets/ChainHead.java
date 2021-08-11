/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

public class ChainHead {
    private boolean mDefined;
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mFirstVisibleWidget;
    protected boolean mHasComplexMatchWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasUndefinedWeights;
    protected ConstraintWidget mHead;
    private boolean mIsRtl = false;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ConstraintWidget mLastVisibleWidget;
    private int mOrientation;
    protected float mTotalWeight = 0.0f;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;

    public ChainHead(ConstraintWidget constraintWidget, int n, boolean bl) {
        this.mFirst = constraintWidget;
        this.mOrientation = n;
        this.mIsRtl = bl;
    }

    private void defineChainProperties() {
        boolean bl;
        int n = this.mOrientation * 2;
        ConstraintWidget constraintWidget = this.mFirst;
        Object object = this.mFirst;
        Object object2 = this.mFirst;
        boolean bl2 = false;
        do {
            bl = true;
            if (bl2) break;
            ++this.mWidgetsCount;
            object.mNextChainWidget[this.mOrientation] = null;
            object.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (object.getVisibility() != 8) {
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = object;
                }
                this.mLastVisibleWidget = object;
                if (object.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (object.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || object.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || object.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
                    ++this.mWidgetsMatchCount;
                    float f = object.mWeight[this.mOrientation];
                    if (f > 0.0f) {
                        this.mTotalWeight += object.mWeight[this.mOrientation];
                    }
                    if (ChainHead.isMatchConstraintEqualityCandidate((ConstraintWidget)object, this.mOrientation)) {
                        if (f < 0.0f) {
                            this.mHasUndefinedWeights = true;
                        } else {
                            this.mHasDefinedWeights = true;
                        }
                        if (this.mWeightedMatchConstraintsWidgets == null) {
                            this.mWeightedMatchConstraintsWidgets = new ArrayList();
                        }
                        this.mWeightedMatchConstraintsWidgets.add((ConstraintWidget)object);
                    }
                    if (this.mFirstMatchConstraintWidget == null) {
                        this.mFirstMatchConstraintWidget = object;
                    }
                    if ((object2 = this.mLastMatchConstraintWidget) != null) {
                        object2.mListNextMatchConstraintsWidget[this.mOrientation] = object;
                    }
                    this.mLastMatchConstraintWidget = object;
                }
            }
            if (constraintWidget != object) {
                constraintWidget.mNextChainWidget[this.mOrientation] = object;
            }
            constraintWidget = object;
            object2 = object.mListAnchors[n + 1].mTarget;
            if (object2 != null) {
                object2 = object2.mOwner;
                if (object2.mListAnchors[n].mTarget == null || object2.mListAnchors[n].mTarget.mOwner != object) {
                    object2 = null;
                }
            } else {
                object2 = null;
            }
            if (object2 != null) {
                object = object2;
                continue;
            }
            bl2 = true;
        } while (true);
        this.mLast = object;
        this.mHead = this.mOrientation == 0 && this.mIsRtl ? this.mLast : this.mFirst;
        if (!this.mHasDefinedWeights || !this.mHasUndefinedWeights) {
            bl = false;
        }
        this.mHasComplexMatchWeights = bl;
    }

    private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget constraintWidget, int n) {
        if (constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[n] == 0 || constraintWidget.mResolvedMatchConstraintDefault[n] == 3)) {
            return true;
        }
        return false;
    }

    public void define() {
        if (!this.mDefined) {
            this.defineChainProperties();
        }
        this.mDefined = true;
    }

    public ConstraintWidget getFirst() {
        return this.mFirst;
    }

    public ConstraintWidget getFirstMatchConstraintWidget() {
        return this.mFirstMatchConstraintWidget;
    }

    public ConstraintWidget getFirstVisibleWidget() {
        return this.mFirstVisibleWidget;
    }

    public ConstraintWidget getHead() {
        return this.mHead;
    }

    public ConstraintWidget getLast() {
        return this.mLast;
    }

    public ConstraintWidget getLastMatchConstraintWidget() {
        return this.mLastMatchConstraintWidget;
    }

    public ConstraintWidget getLastVisibleWidget() {
        return this.mLastVisibleWidget;
    }

    public float getTotalWeight() {
        return this.mTotalWeight;
    }
}

