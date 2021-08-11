/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
    public List<ConstraintWidget> mConstrainedGroup;
    public final int[] mGroupDimensions;
    int mGroupHeight = -1;
    int mGroupWidth = -1;
    public boolean mSkipSolver = false;
    List<ConstraintWidget> mStartHorizontalWidgets;
    List<ConstraintWidget> mStartVerticalWidgets;
    List<ConstraintWidget> mUnresolvedWidgets;
    HashSet<ConstraintWidget> mWidgetsToSetHorizontal;
    HashSet<ConstraintWidget> mWidgetsToSetVertical;
    List<ConstraintWidget> mWidgetsToSolve;

    ConstraintWidgetGroup(List<ConstraintWidget> list) {
        this.mGroupDimensions = new int[]{this.mGroupWidth, this.mGroupHeight};
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet();
        this.mWidgetsToSetVertical = new HashSet();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = list;
    }

    ConstraintWidgetGroup(List<ConstraintWidget> list, boolean bl) {
        this.mGroupDimensions = new int[]{this.mGroupWidth, this.mGroupHeight};
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet();
        this.mWidgetsToSetVertical = new HashSet();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = list;
        this.mSkipSolver = bl;
    }

    private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> arrayList, ConstraintWidget constraintWidget) {
        Object object;
        int n;
        int n2;
        if (constraintWidget.mGroupsToSolver) {
            return;
        }
        arrayList.add(constraintWidget);
        constraintWidget.mGroupsToSolver = true;
        if (constraintWidget.isFullyResolved()) {
            return;
        }
        if (constraintWidget instanceof Helper) {
            object = (Helper)constraintWidget;
            n2 = object.mWidgetsCount;
            for (n = 0; n < n2; ++n) {
                this.getWidgetsToSolveTraversal(arrayList, object.mWidgets[n]);
            }
        }
        n2 = constraintWidget.mListAnchors.length;
        for (n = 0; n < n2; ++n) {
            object = constraintWidget.mListAnchors[n].mTarget;
            if (object == null) continue;
            ConstraintWidget constraintWidget2 = object.mOwner;
            if (object == null || constraintWidget2 == constraintWidget.getParent()) continue;
            this.getWidgetsToSolveTraversal(arrayList, constraintWidget2);
        }
    }

    private void updateResolvedDimension(ConstraintWidget constraintWidget) {
        int n = 0;
        if (constraintWidget.mOptimizerMeasurable) {
            if (constraintWidget.isFullyResolved()) {
                return;
            }
            ConstraintAnchor constraintAnchor = constraintWidget.mRight.mTarget;
            boolean bl = false;
            boolean bl2 = constraintAnchor != null;
            constraintAnchor = bl2 ? constraintWidget.mRight.mTarget : constraintWidget.mLeft.mTarget;
            if (constraintAnchor != null) {
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                if (constraintAnchor.mType == ConstraintAnchor.Type.RIGHT) {
                    n = constraintAnchor.mOwner.mX + constraintAnchor.mOwner.getWidth();
                } else if (constraintAnchor.mType == ConstraintAnchor.Type.LEFT) {
                    n = constraintAnchor.mOwner.mX;
                }
            }
            n = bl2 ? (n -= constraintWidget.mRight.getMargin()) : (n += constraintWidget.mLeft.getMargin() + constraintWidget.getWidth());
            constraintWidget.setHorizontalDimension(n - constraintWidget.getWidth(), n);
            if (constraintWidget.mBaseline.mTarget != null) {
                constraintAnchor = constraintWidget.mBaseline.mTarget;
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                n = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.mBaselineDistance - constraintWidget.mBaselineDistance;
                constraintWidget.setVerticalDimension(n, constraintWidget.mHeight + n);
                constraintWidget.mOptimizerMeasured = true;
                return;
            }
            bl2 = bl;
            if (constraintWidget.mBottom.mTarget != null) {
                bl2 = true;
            }
            constraintAnchor = bl2 ? constraintWidget.mBottom.mTarget : constraintWidget.mTop.mTarget;
            if (constraintAnchor != null) {
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                if (constraintAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                    n = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.getHeight();
                } else if (constraintAnchor.mType == ConstraintAnchor.Type.TOP) {
                    n = constraintAnchor.mOwner.mY;
                }
            }
            n = bl2 ? (n -= constraintWidget.mBottom.getMargin()) : (n += constraintWidget.mTop.getMargin() + constraintWidget.getHeight());
            constraintWidget.setVerticalDimension(n - constraintWidget.getHeight(), n);
            constraintWidget.mOptimizerMeasured = true;
            return;
        }
    }

    void addWidgetsToSet(ConstraintWidget constraintWidget, int n) {
        if (n == 0) {
            this.mWidgetsToSetHorizontal.add(constraintWidget);
            return;
        }
        if (n == 1) {
            this.mWidgetsToSetVertical.add(constraintWidget);
            return;
        }
    }

    public List<ConstraintWidget> getStartWidgets(int n) {
        if (n == 0) {
            return this.mStartHorizontalWidgets;
        }
        if (n == 1) {
            return this.mStartVerticalWidgets;
        }
        return null;
    }

    Set<ConstraintWidget> getWidgetsToSet(int n) {
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
        int n = this.mConstrainedGroup.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = this.mConstrainedGroup.get(i);
            if (constraintWidget.mOptimizerMeasurable) continue;
            this.getWidgetsToSolveTraversal((ArrayList)this.mWidgetsToSolve, constraintWidget);
        }
        this.mUnresolvedWidgets.clear();
        this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
        this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
        return this.mWidgetsToSolve;
    }

    void updateUnresolvedWidgets() {
        int n = this.mUnresolvedWidgets.size();
        for (int i = 0; i < n; ++i) {
            this.updateResolvedDimension(this.mUnresolvedWidgets.get(i));
        }
    }
}

