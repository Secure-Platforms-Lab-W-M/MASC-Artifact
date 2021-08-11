/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.Helper;
import android.support.constraint.solver.widgets.Optimizer;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Analyzer {
    private Analyzer() {
    }

    public static void determineGroups(ConstraintWidgetContainer constraintWidgetContainer) {
        if ((constraintWidgetContainer.getOptimizationLevel() & 32) != 32) {
            Analyzer.singleGroup(constraintWidgetContainer);
            return;
        }
        constraintWidgetContainer.mSkipSolver = true;
        constraintWidgetContainer.mGroupsWrapOptimized = false;
        constraintWidgetContainer.mHorizontalWrapOptimized = false;
        constraintWidgetContainer.mVerticalWrapOptimized = false;
        Iterator<ConstraintWidgetGroup> iterator = constraintWidgetContainer.mChildren;
        List<ConstraintWidgetGroup> list = constraintWidgetContainer.mWidgetGroups;
        boolean bl = constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean bl2 = constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean bl3 = bl || bl2;
        list.clear();
        Iterator object2 = iterator.iterator();
        while (object2.hasNext()) {
            ConstraintWidget constraintWidget = (ConstraintWidget)object2.next();
            constraintWidget.mBelongingGroup = null;
            constraintWidget.mGroupsToSolver = false;
            constraintWidget.resetResolutionNodes();
        }
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            ConstraintWidget constraintWidget = (ConstraintWidget)iterator.next();
            if (constraintWidget.mBelongingGroup != null || Analyzer.determineGroups(constraintWidget, list, bl3)) continue;
            Analyzer.singleGroup(constraintWidgetContainer);
            constraintWidgetContainer.mSkipSolver = false;
            return;
        }
        int n = 0;
        int n2 = 0;
        for (ConstraintWidgetGroup constraintWidgetGroup : list) {
            n = Math.max(n, Analyzer.getMaxDimension(constraintWidgetGroup, 0));
            n2 = Math.max(n2, Analyzer.getMaxDimension(constraintWidgetGroup, 1));
        }
        if (bl) {
            constraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setWidth(n);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mHorizontalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedWidth = n;
        }
        if (bl2) {
            constraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setHeight(n2);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mVerticalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedHeight = n2;
        }
        Analyzer.setPosition(list, 0, constraintWidgetContainer.getWidth());
        Analyzer.setPosition(list, 1, constraintWidgetContainer.getHeight());
    }

    private static boolean determineGroups(ConstraintWidget constraintWidget, List<ConstraintWidgetGroup> list, boolean bl) {
        ConstraintWidgetGroup constraintWidgetGroup = new ConstraintWidgetGroup(new ArrayList<ConstraintWidget>(), true);
        list.add(constraintWidgetGroup);
        return Analyzer.traverse(constraintWidget, constraintWidgetGroup, list, bl);
    }

    private static int getMaxDimension(ConstraintWidgetGroup constraintWidgetGroup, int n) {
        int n2 = 0;
        int n3 = n * 2;
        List<ConstraintWidget> list = constraintWidgetGroup.getStartWidgets(n);
        int n4 = list.size();
        for (int i = 0; i < n4; ++i) {
            ConstraintWidget constraintWidget = list.get(i);
            boolean bl = constraintWidget.mListAnchors[n3 + 1].mTarget == null || constraintWidget.mListAnchors[n3].mTarget != null && constraintWidget.mListAnchors[n3 + 1].mTarget != null;
            n2 = Math.max(n2, Analyzer.getMaxDimensionTraversal(constraintWidget, n, bl, 0));
        }
        constraintWidgetGroup.mGroupDimensions[n] = n2;
        return n2;
    }

    private static int getMaxDimensionTraversal(ConstraintWidget constraintWidget, int n, boolean bl, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        boolean bl2 = constraintWidget.mOptimizerMeasurable;
        int n8 = 0;
        if (!bl2) {
            return 0;
        }
        int n9 = 0;
        int n10 = 0;
        int n11 = n8;
        if (constraintWidget.mBaseline.mTarget != null) {
            n11 = n8;
            if (n == 1) {
                n11 = 1;
            }
        }
        if (bl) {
            n5 = constraintWidget.getBaselineDistance();
            n3 = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            n8 = n * 2;
            n7 = n8 + 1;
        } else {
            n5 = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            n3 = constraintWidget.getBaselineDistance();
            n7 = n * 2;
            n8 = n7 + 1;
        }
        if (constraintWidget.mListAnchors[n7].mTarget != null && constraintWidget.mListAnchors[n8].mTarget == null) {
            n6 = -1;
            n4 = n8;
            n8 = n7;
            n7 = n4;
        } else {
            n6 = 1;
        }
        int n12 = n11 != 0 ? n2 - n5 : n2;
        int n13 = constraintWidget.mListAnchors[n8].getMargin() * n6 + Analyzer.getParentBiasOffset(constraintWidget, n);
        int n14 = n13 + n12;
        n2 = n == 0 ? constraintWidget.getWidth() : constraintWidget.getHeight();
        int n15 = n2 * n6;
        Object object = constraintWidget.mListAnchors[n8].getResolutionNode().dependents.iterator();
        n4 = n10;
        n2 = n9;
        while (object.hasNext()) {
            n2 = Math.max(n2, Analyzer.getMaxDimensionTraversal(((ResolutionAnchor)((ResolutionNode)object.next())).myAnchor.mOwner, n, bl, n14));
        }
        object = constraintWidget.mListAnchors[n7].getResolutionNode().dependents.iterator();
        n9 = n4;
        n4 = n7;
        n7 = n9;
        while (object.hasNext()) {
            n7 = Math.max(n7, Analyzer.getMaxDimensionTraversal(((ResolutionAnchor)((ResolutionNode)object.next())).myAnchor.mOwner, n, bl, n15 + n14));
        }
        if (n11 != 0) {
            n9 = n2 - n5;
            n10 = n7 + n3;
        } else {
            n9 = n == 0 ? constraintWidget.getWidth() : constraintWidget.getHeight();
            n10 = n7 + n9 * n6;
            n9 = n2;
        }
        n2 = 0;
        n7 = 0;
        if (n == 1) {
            object = constraintWidget.mBaseline.getResolutionNode().dependents.iterator();
            n2 = n15;
            while (object.hasNext()) {
                ResolutionAnchor resolutionAnchor = (ResolutionAnchor)((ResolutionNode)object.next());
                if (n6 == 1) {
                    n7 = Math.max(n7, Analyzer.getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, bl, n5 + n14));
                    continue;
                }
                n7 = Math.max(n7, Analyzer.getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, bl, n3 * n6 + n14));
            }
            n15 = n2;
            n2 = constraintWidget.mBaseline.getResolutionNode().dependents.size() > 0 && n11 == 0 ? (n6 == 1 ? n7 + n5 : n7 - n3) : n7;
        }
        n5 = n13 + Math.max(n9, Math.max(n10, n2));
        n2 = n12 + n13;
        n7 = n2 + n15;
        if (n6 == -1) {
            n11 = n2;
        } else {
            n11 = n7;
            n7 = n2;
        }
        if (bl) {
            Optimizer.setOptimizedWidget(constraintWidget, n, n7);
            constraintWidget.setFrame(n7, n11, n);
        } else {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
            constraintWidget.setRelativePositioning(n7, n);
        }
        if (constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio != 0.0f) {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
        }
        if (constraintWidget.mListAnchors[n8].mTarget != null && constraintWidget.mListAnchors[n4].mTarget != null) {
            object = constraintWidget.getParent();
            if (constraintWidget.mListAnchors[n8].mTarget.mOwner == object && constraintWidget.mListAnchors[n4].mTarget.mOwner == object) {
                constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
                return n5;
            }
            return n5;
        }
        return n5;
    }

    private static int getParentBiasOffset(ConstraintWidget constraintWidget, int n) {
        int n2 = n * 2;
        ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n2];
        ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n2 + 1];
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner == constraintWidget.mParent && constraintAnchor2.mTarget != null && constraintAnchor2.mTarget.mOwner == constraintWidget.mParent) {
            n2 = constraintWidget.mParent.getLength(n);
            float f = n == 0 ? constraintWidget.mHorizontalBiasPercent : constraintWidget.mVerticalBiasPercent;
            n = constraintWidget.getLength(n);
            return (int)((float)(n2 - constraintAnchor.getMargin() - constraintAnchor2.getMargin() - n) * f);
        }
        return 0;
    }

    private static void invalidate(ConstraintWidgetContainer constraintWidgetContainer, ConstraintWidget constraintWidget, ConstraintWidgetGroup constraintWidgetGroup) {
        constraintWidgetGroup.mSkipSolver = false;
        constraintWidgetContainer.mSkipSolver = false;
        constraintWidget.mOptimizerMeasurable = false;
    }

    private static int resolveDimensionRatio(ConstraintWidget constraintWidget) {
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int n = constraintWidget.mDimensionRatioSide == 0 ? (int)((float)constraintWidget.getHeight() * constraintWidget.mDimensionRatio) : (int)((float)constraintWidget.getHeight() / constraintWidget.mDimensionRatio);
            constraintWidget.setWidth(n);
            return n;
        }
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int n = constraintWidget.mDimensionRatioSide == 1 ? (int)((float)constraintWidget.getWidth() * constraintWidget.mDimensionRatio) : (int)((float)constraintWidget.getWidth() / constraintWidget.mDimensionRatio);
            constraintWidget.setHeight(n);
            return n;
        }
        return -1;
    }

    private static void setConnection(ConstraintAnchor constraintAnchor) {
        ResolutionAnchor resolutionAnchor = constraintAnchor.getResolutionNode();
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mTarget != constraintAnchor) {
            constraintAnchor.mTarget.getResolutionNode().addDependent(resolutionAnchor);
            return;
        }
    }

    public static void setPosition(List<ConstraintWidgetGroup> list, int n, int n2) {
        int n3 = list.size();
        for (int i = 0; i < n3; ++i) {
            for (ConstraintWidget constraintWidget : list.get(i).getWidgetsToSet(n)) {
                if (!constraintWidget.mOptimizerMeasurable) continue;
                Analyzer.updateSizeDependentWidgets(constraintWidget, n, n2);
            }
        }
    }

    private static void singleGroup(ConstraintWidgetContainer constraintWidgetContainer) {
        constraintWidgetContainer.mWidgetGroups.clear();
        constraintWidgetContainer.mWidgetGroups.add(0, new ConstraintWidgetGroup(constraintWidgetContainer.mChildren));
    }

    private static boolean traverse(ConstraintWidget object, ConstraintWidgetGroup constraintWidgetGroup, List<ConstraintWidgetGroup> list, boolean bl) {
        block35 : {
            ConstraintWidgetContainer constraintWidgetContainer;
            int n;
            block39 : {
                block38 : {
                    block37 : {
                        block36 : {
                            if (object == null) {
                                return true;
                            }
                            object.mOptimizerMeasured = false;
                            constraintWidgetContainer = (ConstraintWidgetContainer)object.getParent();
                            if (object.mBelongingGroup != null) break block35;
                            object.mOptimizerMeasurable = true;
                            constraintWidgetGroup.mConstrainedGroup.add((ConstraintWidget)((Object)object));
                            object.mBelongingGroup = constraintWidgetGroup;
                            if (object.mLeft.mTarget == null && object.mRight.mTarget == null && object.mTop.mTarget == null && object.mBottom.mTarget == null && object.mBaseline.mTarget == null && object.mCenter.mTarget == null) {
                                Analyzer.invalidate(constraintWidgetContainer, (ConstraintWidget)((Object)object), constraintWidgetGroup);
                                if (bl) {
                                    return false;
                                }
                            }
                            if (object.mTop.mTarget != null && object.mBottom.mTarget != null) {
                                if (constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                    // empty if block
                                }
                                if (bl) {
                                    Analyzer.invalidate(constraintWidgetContainer, (ConstraintWidget)((Object)object), constraintWidgetGroup);
                                    return false;
                                }
                                if (object.mTop.mTarget.mOwner != object.getParent() || object.mBottom.mTarget.mOwner != object.getParent()) {
                                    Analyzer.invalidate(constraintWidgetContainer, object, constraintWidgetGroup);
                                }
                            }
                            if (object.mLeft.mTarget != null && object.mRight.mTarget != null) {
                                if (constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                    // empty if block
                                }
                                if (bl) {
                                    Analyzer.invalidate(constraintWidgetContainer, (ConstraintWidget)((Object)object), constraintWidgetGroup);
                                    return false;
                                }
                                if (object.mLeft.mTarget.mOwner != object.getParent() || object.mRight.mTarget.mOwner != object.getParent()) {
                                    Analyzer.invalidate(constraintWidgetContainer, object, constraintWidgetGroup);
                                }
                            }
                            n = object.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                            int n2 = object.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                            if (n ^ n2 && object.mDimensionRatio != 0.0f) {
                                Analyzer.resolveDimensionRatio(object);
                            } else if (object.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || object.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                Analyzer.invalidate(constraintWidgetContainer, object, constraintWidgetGroup);
                                if (bl) {
                                    return false;
                                }
                            }
                            if (object.mLeft.mTarget == null && object.mRight.mTarget == null) break block36;
                            if (!(object.mLeft.mTarget != null && object.mLeft.mTarget.mOwner == object.mParent && object.mRight.mTarget == null || object.mRight.mTarget != null && object.mRight.mTarget.mOwner == object.mParent && object.mLeft.mTarget == null) && (object.mLeft.mTarget == null || object.mLeft.mTarget.mOwner != object.mParent || object.mRight.mTarget == null || object.mRight.mTarget.mOwner != object.mParent)) break block37;
                        }
                        if (object.mCenter.mTarget == null && !(object instanceof Guideline) && !(object instanceof Helper)) {
                            constraintWidgetGroup.mStartHorizontalWidgets.add((ConstraintWidget)((Object)object));
                        }
                    }
                    if (object.mTop.mTarget == null && object.mBottom.mTarget == null) break block38;
                    if (!(object.mTop.mTarget != null && object.mTop.mTarget.mOwner == object.mParent && object.mBottom.mTarget == null || object.mBottom.mTarget != null && object.mBottom.mTarget.mOwner == object.mParent && object.mTop.mTarget == null) && (object.mTop.mTarget == null || object.mTop.mTarget.mOwner != object.mParent || object.mBottom.mTarget == null || object.mBottom.mTarget.mOwner != object.mParent)) break block39;
                }
                if (object.mCenter.mTarget == null && object.mBaseline.mTarget == null && !(object instanceof Guideline) && !(object instanceof Helper)) {
                    constraintWidgetGroup.mStartVerticalWidgets.add((ConstraintWidget)((Object)object));
                }
            }
            if (object instanceof Helper) {
                Analyzer.invalidate(constraintWidgetContainer, object, constraintWidgetGroup);
                if (bl) {
                    return false;
                }
                Helper object2 = (Helper)((Object)object);
                for (n = 0; n < object2.mWidgetsCount; ++n) {
                    if (Analyzer.traverse(object2.mWidgets[n], constraintWidgetGroup, list, bl)) continue;
                    return false;
                }
            }
            for (ConstraintAnchor constraintAnchor : object.mListAnchors) {
                if (constraintAnchor.mTarget == null || constraintAnchor.mTarget.mOwner == object.getParent()) continue;
                if (constraintAnchor.mType == ConstraintAnchor.Type.CENTER) {
                    Analyzer.invalidate(constraintWidgetContainer, object, constraintWidgetGroup);
                    if (bl) {
                        return false;
                    }
                } else {
                    Analyzer.setConnection(constraintAnchor);
                }
                if (Analyzer.traverse(constraintAnchor.mTarget.mOwner, constraintWidgetGroup, list, bl)) continue;
                return false;
            }
            return true;
        }
        if (object.mBelongingGroup != constraintWidgetGroup) {
            constraintWidgetGroup.mConstrainedGroup.addAll(object.mBelongingGroup.mConstrainedGroup);
            constraintWidgetGroup.mStartHorizontalWidgets.addAll(object.mBelongingGroup.mStartHorizontalWidgets);
            constraintWidgetGroup.mStartVerticalWidgets.addAll(object.mBelongingGroup.mStartVerticalWidgets);
            if (!object.mBelongingGroup.mSkipSolver) {
                constraintWidgetGroup.mSkipSolver = false;
            }
            list.remove(object.mBelongingGroup);
            object = object.mBelongingGroup.mConstrainedGroup.iterator();
            while (object.hasNext()) {
                object.next().mBelongingGroup = constraintWidgetGroup;
            }
            return true;
        }
        return true;
    }

    private static void updateSizeDependentWidgets(ConstraintWidget constraintWidget, int n, int n2) {
        int n3 = n * 2;
        ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n3];
        ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n3 + 1];
        int n4 = constraintAnchor.mTarget != null && constraintAnchor2.mTarget != null ? 1 : 0;
        if (n4 != 0) {
            Optimizer.setOptimizedWidget(constraintWidget, n, Analyzer.getParentBiasOffset(constraintWidget, n) + constraintAnchor.getMargin());
            return;
        }
        if (constraintWidget.mDimensionRatio != 0.0f && constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            n2 = Analyzer.resolveDimensionRatio(constraintWidget);
            n4 = (int)constraintWidget.mListAnchors[n3].getResolutionNode().resolvedOffset;
            constraintAnchor2.getResolutionNode().resolvedTarget = constraintAnchor.getResolutionNode();
            constraintAnchor2.getResolutionNode().resolvedOffset = n2;
            constraintAnchor2.getResolutionNode().state = 1;
            constraintWidget.setFrame(n4, n4 + n2, n);
            return;
        }
        n4 = (n2 -= constraintWidget.getRelativePositioning(n)) - constraintWidget.getLength(n);
        constraintWidget.setFrame(n4, n2, n);
        Optimizer.setOptimizedWidget(constraintWidget, n, n4);
    }
}

