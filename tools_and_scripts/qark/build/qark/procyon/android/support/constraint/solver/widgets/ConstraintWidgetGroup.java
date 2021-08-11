// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.Collection;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConstraintWidgetGroup
{
    public List<ConstraintWidget> mConstrainedGroup;
    public final int[] mGroupDimensions;
    int mGroupHeight;
    int mGroupWidth;
    public boolean mSkipSolver;
    List<ConstraintWidget> mStartHorizontalWidgets;
    List<ConstraintWidget> mStartVerticalWidgets;
    List<ConstraintWidget> mUnresolvedWidgets;
    HashSet<ConstraintWidget> mWidgetsToSetHorizontal;
    HashSet<ConstraintWidget> mWidgetsToSetVertical;
    List<ConstraintWidget> mWidgetsToSolve;
    
    ConstraintWidgetGroup(final List<ConstraintWidget> mConstrainedGroup) {
        this.mGroupWidth = -1;
        this.mGroupHeight = -1;
        this.mSkipSolver = false;
        this.mGroupDimensions = new int[] { this.mGroupWidth, this.mGroupHeight };
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet<ConstraintWidget>();
        this.mWidgetsToSetVertical = new HashSet<ConstraintWidget>();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = mConstrainedGroup;
    }
    
    ConstraintWidgetGroup(final List<ConstraintWidget> mConstrainedGroup, final boolean mSkipSolver) {
        this.mGroupWidth = -1;
        this.mGroupHeight = -1;
        this.mSkipSolver = false;
        this.mGroupDimensions = new int[] { this.mGroupWidth, this.mGroupHeight };
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet<ConstraintWidget>();
        this.mWidgetsToSetVertical = new HashSet<ConstraintWidget>();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = mConstrainedGroup;
        this.mSkipSolver = mSkipSolver;
    }
    
    private void getWidgetsToSolveTraversal(final ArrayList<ConstraintWidget> list, final ConstraintWidget constraintWidget) {
        if (constraintWidget.mGroupsToSolver) {
            return;
        }
        list.add(constraintWidget);
        constraintWidget.mGroupsToSolver = true;
        if (constraintWidget.isFullyResolved()) {
            return;
        }
        if (constraintWidget instanceof Helper) {
            final Helper helper = (Helper)constraintWidget;
            for (int mWidgetsCount = helper.mWidgetsCount, i = 0; i < mWidgetsCount; ++i) {
                this.getWidgetsToSolveTraversal(list, helper.mWidgets[i]);
            }
        }
        for (int length = constraintWidget.mListAnchors.length, j = 0; j < length; ++j) {
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[j].mTarget;
            if (mTarget != null) {
                final ConstraintWidget mOwner = mTarget.mOwner;
                if (mTarget != null && mOwner != constraintWidget.getParent()) {
                    this.getWidgetsToSolveTraversal(list, mOwner);
                }
            }
        }
    }
    
    private void updateResolvedDimension(final ConstraintWidget constraintWidget) {
        int mx = 0;
        if (!constraintWidget.mOptimizerMeasurable) {
            return;
        }
        if (constraintWidget.isFullyResolved()) {
            return;
        }
        final ConstraintAnchor mTarget = constraintWidget.mRight.mTarget;
        final boolean b = false;
        final boolean b2 = mTarget != null;
        ConstraintAnchor constraintAnchor;
        if (b2) {
            constraintAnchor = constraintWidget.mRight.mTarget;
        }
        else {
            constraintAnchor = constraintWidget.mLeft.mTarget;
        }
        if (constraintAnchor != null) {
            if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                this.updateResolvedDimension(constraintAnchor.mOwner);
            }
            if (constraintAnchor.mType == ConstraintAnchor.Type.RIGHT) {
                mx = constraintAnchor.mOwner.mX + constraintAnchor.mOwner.getWidth();
            }
            else if (constraintAnchor.mType == ConstraintAnchor.Type.LEFT) {
                mx = constraintAnchor.mOwner.mX;
            }
        }
        int my;
        if (b2) {
            my = mx - constraintWidget.mRight.getMargin();
        }
        else {
            my = mx + (constraintWidget.mLeft.getMargin() + constraintWidget.getWidth());
        }
        constraintWidget.setHorizontalDimension(my - constraintWidget.getWidth(), my);
        if (constraintWidget.mBaseline.mTarget != null) {
            final ConstraintAnchor mTarget2 = constraintWidget.mBaseline.mTarget;
            if (!mTarget2.mOwner.mOptimizerMeasured) {
                this.updateResolvedDimension(mTarget2.mOwner);
            }
            final int n = mTarget2.mOwner.mY + mTarget2.mOwner.mBaselineDistance - constraintWidget.mBaselineDistance;
            constraintWidget.setVerticalDimension(n, constraintWidget.mHeight + n);
            constraintWidget.mOptimizerMeasured = true;
            return;
        }
        boolean b3 = b;
        if (constraintWidget.mBottom.mTarget != null) {
            b3 = true;
        }
        ConstraintAnchor constraintAnchor2;
        if (b3) {
            constraintAnchor2 = constraintWidget.mBottom.mTarget;
        }
        else {
            constraintAnchor2 = constraintWidget.mTop.mTarget;
        }
        if (constraintAnchor2 != null) {
            if (!constraintAnchor2.mOwner.mOptimizerMeasured) {
                this.updateResolvedDimension(constraintAnchor2.mOwner);
            }
            if (constraintAnchor2.mType == ConstraintAnchor.Type.BOTTOM) {
                my = constraintAnchor2.mOwner.mY + constraintAnchor2.mOwner.getHeight();
            }
            else if (constraintAnchor2.mType == ConstraintAnchor.Type.TOP) {
                my = constraintAnchor2.mOwner.mY;
            }
        }
        int n2;
        if (b3) {
            n2 = my - constraintWidget.mBottom.getMargin();
        }
        else {
            n2 = my + (constraintWidget.mTop.getMargin() + constraintWidget.getHeight());
        }
        constraintWidget.setVerticalDimension(n2 - constraintWidget.getHeight(), n2);
        constraintWidget.mOptimizerMeasured = true;
    }
    
    void addWidgetsToSet(final ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            this.mWidgetsToSetHorizontal.add(constraintWidget);
            return;
        }
        if (n == 1) {
            this.mWidgetsToSetVertical.add(constraintWidget);
        }
    }
    
    public List<ConstraintWidget> getStartWidgets(final int n) {
        if (n == 0) {
            return this.mStartHorizontalWidgets;
        }
        if (n == 1) {
            return this.mStartVerticalWidgets;
        }
        return null;
    }
    
    Set<ConstraintWidget> getWidgetsToSet(final int n) {
        if (n == 0) {
            return this.mWidgetsToSetHorizontal;
        }
        if (n == 1) {
            return this.mWidgetsToSetVertical;
        }
        return null;
    }
    
    List<ConstraintWidget> getWidgetsToSolve() {
        if (!this.mWidgetsToSolve.isEmpty()) {
            return this.mWidgetsToSolve;
        }
        for (int size = this.mConstrainedGroup.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mConstrainedGroup.get(i);
            if (!constraintWidget.mOptimizerMeasurable) {
                this.getWidgetsToSolveTraversal((ArrayList)this.mWidgetsToSolve, constraintWidget);
            }
        }
        this.mUnresolvedWidgets.clear();
        this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
        this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
        return this.mWidgetsToSolve;
    }
    
    void updateUnresolvedWidgets() {
        for (int size = this.mUnresolvedWidgets.size(), i = 0; i < size; ++i) {
            this.updateResolvedDimension(this.mUnresolvedWidgets.get(i));
        }
    }
}
