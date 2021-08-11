// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead
{
    private boolean mDefined;
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mFirstVisibleWidget;
    protected boolean mHasComplexMatchWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasUndefinedWeights;
    protected ConstraintWidget mHead;
    private boolean mIsRtl;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ConstraintWidget mLastVisibleWidget;
    private int mOrientation;
    protected float mTotalWeight;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;
    
    public ChainHead(final ConstraintWidget mFirst, final int mOrientation, final boolean mIsRtl) {
        this.mTotalWeight = 0.0f;
        this.mIsRtl = false;
        this.mFirst = mFirst;
        this.mOrientation = mOrientation;
        this.mIsRtl = mIsRtl;
    }
    
    private void defineChainProperties() {
        final int n = this.mOrientation * 2;
        ConstraintWidget mFirst = this.mFirst;
        ConstraintWidget mFirst2 = this.mFirst;
        final ConstraintWidget mFirst3 = this.mFirst;
        int n2 = 0;
        boolean mHasComplexMatchWeights;
        while (true) {
            mHasComplexMatchWeights = true;
            if (n2 != 0) {
                break;
            }
            ++this.mWidgetsCount;
            mFirst2.mNextChainWidget[this.mOrientation] = null;
            mFirst2.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (mFirst2.getVisibility() != 8) {
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = mFirst2;
                }
                this.mLastVisibleWidget = mFirst2;
                if (mFirst2.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (mFirst2.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || mFirst2.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || mFirst2.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
                    ++this.mWidgetsMatchCount;
                    final float n3 = mFirst2.mWeight[this.mOrientation];
                    if (n3 > 0.0f) {
                        this.mTotalWeight += mFirst2.mWeight[this.mOrientation];
                    }
                    if (isMatchConstraintEqualityCandidate(mFirst2, this.mOrientation)) {
                        if (n3 < 0.0f) {
                            this.mHasUndefinedWeights = true;
                        }
                        else {
                            this.mHasDefinedWeights = true;
                        }
                        if (this.mWeightedMatchConstraintsWidgets == null) {
                            this.mWeightedMatchConstraintsWidgets = new ArrayList<ConstraintWidget>();
                        }
                        this.mWeightedMatchConstraintsWidgets.add(mFirst2);
                    }
                    if (this.mFirstMatchConstraintWidget == null) {
                        this.mFirstMatchConstraintWidget = mFirst2;
                    }
                    final ConstraintWidget mLastMatchConstraintWidget = this.mLastMatchConstraintWidget;
                    if (mLastMatchConstraintWidget != null) {
                        mLastMatchConstraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = mFirst2;
                    }
                    this.mLastMatchConstraintWidget = mFirst2;
                }
            }
            if (mFirst != mFirst2) {
                mFirst.mNextChainWidget[this.mOrientation] = mFirst2;
            }
            mFirst = mFirst2;
            final ConstraintAnchor mTarget = mFirst2.mListAnchors[n + 1].mTarget;
            ConstraintWidget mOwner;
            if (mTarget != null) {
                mOwner = mTarget.mOwner;
                if (mOwner.mListAnchors[n].mTarget == null || mOwner.mListAnchors[n].mTarget.mOwner != mFirst2) {
                    mOwner = null;
                }
            }
            else {
                mOwner = null;
            }
            if (mOwner != null) {
                mFirst2 = mOwner;
            }
            else {
                n2 = 1;
            }
        }
        this.mLast = mFirst2;
        if (this.mOrientation == 0 && this.mIsRtl) {
            this.mHead = this.mLast;
        }
        else {
            this.mHead = this.mFirst;
        }
        if (!this.mHasDefinedWeights || !this.mHasUndefinedWeights) {
            mHasComplexMatchWeights = false;
        }
        this.mHasComplexMatchWeights = mHasComplexMatchWeights;
    }
    
    private static boolean isMatchConstraintEqualityCandidate(final ConstraintWidget constraintWidget, final int n) {
        return constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[n] == 0 || constraintWidget.mResolvedMatchConstraintDefault[n] == 3);
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
