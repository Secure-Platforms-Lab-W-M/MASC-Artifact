// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Analyzer
{
    private Analyzer() {
    }
    
    public static void determineGroups(final ConstraintWidgetContainer constraintWidgetContainer) {
        if ((constraintWidgetContainer.getOptimizationLevel() & 0x20) != 0x20) {
            singleGroup(constraintWidgetContainer);
            return;
        }
        constraintWidgetContainer.mSkipSolver = true;
        constraintWidgetContainer.mGroupsWrapOptimized = false;
        constraintWidgetContainer.mHorizontalWrapOptimized = false;
        constraintWidgetContainer.mVerticalWrapOptimized = false;
        final ArrayList<ConstraintWidget> mChildren = constraintWidgetContainer.mChildren;
        final List<ConstraintWidgetGroup> mWidgetGroups = constraintWidgetContainer.mWidgetGroups;
        final boolean b = constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        final boolean b2 = constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        final boolean b3 = b || b2;
        mWidgetGroups.clear();
        for (final ConstraintWidget constraintWidget : mChildren) {
            constraintWidget.mBelongingGroup = null;
            constraintWidget.mGroupsToSolver = false;
            constraintWidget.resetResolutionNodes();
        }
        for (final ConstraintWidget constraintWidget2 : mChildren) {
            if (constraintWidget2.mBelongingGroup == null && !determineGroups(constraintWidget2, mWidgetGroups, b3)) {
                singleGroup(constraintWidgetContainer);
                constraintWidgetContainer.mSkipSolver = false;
                return;
            }
        }
        int max = 0;
        int max2 = 0;
        for (final ConstraintWidgetGroup constraintWidgetGroup : mWidgetGroups) {
            max = Math.max(max, getMaxDimension(constraintWidgetGroup, 0));
            max2 = Math.max(max2, getMaxDimension(constraintWidgetGroup, 1));
        }
        if (b) {
            constraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setWidth(max);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mHorizontalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedWidth = max;
        }
        if (b2) {
            constraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setHeight(max2);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mVerticalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedHeight = max2;
        }
        setPosition(mWidgetGroups, 0, constraintWidgetContainer.getWidth());
        setPosition(mWidgetGroups, 1, constraintWidgetContainer.getHeight());
    }
    
    private static boolean determineGroups(final ConstraintWidget constraintWidget, final List<ConstraintWidgetGroup> list, final boolean b) {
        final ConstraintWidgetGroup constraintWidgetGroup = new ConstraintWidgetGroup(new ArrayList<ConstraintWidget>(), true);
        list.add(constraintWidgetGroup);
        return traverse(constraintWidget, constraintWidgetGroup, list, b);
    }
    
    private static int getMaxDimension(final ConstraintWidgetGroup constraintWidgetGroup, final int n) {
        int max = 0;
        final int n2 = n * 2;
        final List<ConstraintWidget> startWidgets = constraintWidgetGroup.getStartWidgets(n);
        for (int size = startWidgets.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = startWidgets.get(i);
            max = Math.max(max, getMaxDimensionTraversal(constraintWidget, n, constraintWidget.mListAnchors[n2 + 1].mTarget == null || (constraintWidget.mListAnchors[n2].mTarget != null && constraintWidget.mListAnchors[n2 + 1].mTarget != null), 0));
        }
        return constraintWidgetGroup.mGroupDimensions[n] = max;
    }
    
    private static int getMaxDimensionTraversal(final ConstraintWidget constraintWidget, final int n, final boolean b, int n2) {
        final boolean mOptimizerMeasurable = constraintWidget.mOptimizerMeasurable;
        final boolean b2 = false;
        if (!mOptimizerMeasurable) {
            return 0;
        }
        final int n3 = 0;
        final boolean b3 = false;
        boolean b4 = b2;
        if (constraintWidget.mBaseline.mTarget != null) {
            b4 = b2;
            if (n == 1) {
                b4 = true;
            }
        }
        int baselineDistance;
        int baselineDistance2;
        int n4;
        int n5;
        if (b) {
            baselineDistance = constraintWidget.getBaselineDistance();
            baselineDistance2 = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            n4 = n * 2;
            n5 = n4 + 1;
        }
        else {
            baselineDistance = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            baselineDistance2 = constraintWidget.getBaselineDistance();
            n5 = n * 2;
            n4 = n5 + 1;
        }
        int n6;
        if (constraintWidget.mListAnchors[n5].mTarget != null && constraintWidget.mListAnchors[n4].mTarget == null) {
            n6 = -1;
            final int n7 = n4;
            n4 = n5;
            n5 = n7;
        }
        else {
            n6 = 1;
        }
        int n8;
        if (b4) {
            n8 = n2 - baselineDistance;
        }
        else {
            n8 = n2;
        }
        final int n9 = constraintWidget.mListAnchors[n4].getMargin() * n6 + getParentBiasOffset(constraintWidget, n);
        final int n10 = n9 + n8;
        if (n == 0) {
            n2 = constraintWidget.getWidth();
        }
        else {
            n2 = constraintWidget.getHeight();
        }
        int n11 = n2 * n6;
        final Iterator<ResolutionNode> iterator = constraintWidget.mListAnchors[n4].getResolutionNode().dependents.iterator();
        final boolean b5 = b3;
        n2 = n3;
        while (iterator.hasNext()) {
            n2 = Math.max(n2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, n, b, n10));
        }
        final Iterator<ResolutionNode> iterator2 = constraintWidget.mListAnchors[n5].getResolutionNode().dependents.iterator();
        final int n12 = b5 ? 1 : 0;
        final int n13 = n5;
        int max = n12;
        while (iterator2.hasNext()) {
            max = Math.max(max, getMaxDimensionTraversal(((ResolutionAnchor)iterator2.next()).myAnchor.mOwner, n, b, n11 + n10));
        }
        int n14;
        int n15;
        if (b4) {
            n14 = n2 - baselineDistance;
            n15 = max + baselineDistance2;
        }
        else {
            int n16;
            if (n == 0) {
                n16 = constraintWidget.getWidth();
            }
            else {
                n16 = constraintWidget.getHeight();
            }
            n15 = max + n16 * n6;
            n14 = n2;
        }
        n2 = 0;
        int n17 = 0;
        if (n == 1) {
            final Iterator<ResolutionNode> iterator3 = constraintWidget.mBaseline.getResolutionNode().dependents.iterator();
            n2 = n11;
            while (iterator3.hasNext()) {
                final ResolutionAnchor resolutionAnchor = (ResolutionAnchor)iterator3.next();
                if (n6 == 1) {
                    n17 = Math.max(n17, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, b, baselineDistance + n10));
                }
                else {
                    n17 = Math.max(n17, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, b, baselineDistance2 * n6 + n10));
                }
            }
            n11 = n2;
            if (constraintWidget.mBaseline.getResolutionNode().dependents.size() > 0 && !b4) {
                if (n6 == 1) {
                    n2 = n17 + baselineDistance;
                }
                else {
                    n2 = n17 - baselineDistance2;
                }
            }
            else {
                n2 = n17;
            }
        }
        final int n18 = n9 + Math.max(n14, Math.max(n15, n2));
        n2 = n8 + n9;
        int n19 = n2 + n11;
        int n20;
        if (n6 == -1) {
            n20 = n2;
        }
        else {
            n20 = n19;
            n19 = n2;
        }
        if (b) {
            Optimizer.setOptimizedWidget(constraintWidget, n, n19);
            constraintWidget.setFrame(n19, n20, n);
        }
        else {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
            constraintWidget.setRelativePositioning(n19, n);
        }
        if (constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio != 0.0f) {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
        }
        if (constraintWidget.mListAnchors[n4].mTarget == null || constraintWidget.mListAnchors[n13].mTarget == null) {
            return n18;
        }
        final ConstraintWidget parent = constraintWidget.getParent();
        if (constraintWidget.mListAnchors[n4].mTarget.mOwner == parent && constraintWidget.mListAnchors[n13].mTarget.mOwner == parent) {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
            return n18;
        }
        return n18;
    }
    
    private static int getParentBiasOffset(final ConstraintWidget constraintWidget, int length) {
        final int n = length * 2;
        final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n];
        final ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n + 1];
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner == constraintWidget.mParent && constraintAnchor2.mTarget != null && constraintAnchor2.mTarget.mOwner == constraintWidget.mParent) {
            final int length2 = constraintWidget.mParent.getLength(length);
            float n2;
            if (length == 0) {
                n2 = constraintWidget.mHorizontalBiasPercent;
            }
            else {
                n2 = constraintWidget.mVerticalBiasPercent;
            }
            length = constraintWidget.getLength(length);
            return (int)((length2 - constraintAnchor.getMargin() - constraintAnchor2.getMargin() - length) * n2);
        }
        return 0;
    }
    
    private static void invalidate(final ConstraintWidgetContainer constraintWidgetContainer, final ConstraintWidget constraintWidget, final ConstraintWidgetGroup constraintWidgetGroup) {
        constraintWidgetGroup.mSkipSolver = false;
        constraintWidgetContainer.mSkipSolver = false;
        constraintWidget.mOptimizerMeasurable = false;
    }
    
    private static int resolveDimensionRatio(final ConstraintWidget constraintWidget) {
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int width;
            if (constraintWidget.mDimensionRatioSide == 0) {
                width = (int)(constraintWidget.getHeight() * constraintWidget.mDimensionRatio);
            }
            else {
                width = (int)(constraintWidget.getHeight() / constraintWidget.mDimensionRatio);
            }
            constraintWidget.setWidth(width);
            return width;
        }
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int height;
            if (constraintWidget.mDimensionRatioSide == 1) {
                height = (int)(constraintWidget.getWidth() * constraintWidget.mDimensionRatio);
            }
            else {
                height = (int)(constraintWidget.getWidth() / constraintWidget.mDimensionRatio);
            }
            constraintWidget.setHeight(height);
            return height;
        }
        return -1;
    }
    
    private static void setConnection(final ConstraintAnchor constraintAnchor) {
        final ResolutionAnchor resolutionNode = constraintAnchor.getResolutionNode();
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mTarget != constraintAnchor) {
            constraintAnchor.mTarget.getResolutionNode().addDependent(resolutionNode);
        }
    }
    
    public static void setPosition(final List<ConstraintWidgetGroup> list, final int n, final int n2) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            for (final ConstraintWidget constraintWidget : list.get(i).getWidgetsToSet(n)) {
                if (constraintWidget.mOptimizerMeasurable) {
                    updateSizeDependentWidgets(constraintWidget, n, n2);
                }
            }
        }
    }
    
    private static void singleGroup(final ConstraintWidgetContainer constraintWidgetContainer) {
        constraintWidgetContainer.mWidgetGroups.clear();
        constraintWidgetContainer.mWidgetGroups.add(0, new ConstraintWidgetGroup(constraintWidgetContainer.mChildren));
    }
    
    private static boolean traverse(final ConstraintWidget constraintWidget, final ConstraintWidgetGroup constraintWidgetGroup, final List<ConstraintWidgetGroup> list, final boolean b) {
        if (constraintWidget == null) {
            return true;
        }
        constraintWidget.mOptimizerMeasured = false;
        final ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget.getParent();
        if (constraintWidget.mBelongingGroup == null) {
            constraintWidget.mOptimizerMeasurable = true;
            constraintWidgetGroup.mConstrainedGroup.add(constraintWidget);
            constraintWidget.mBelongingGroup = constraintWidgetGroup;
            if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null && constraintWidget.mBaseline.mTarget == null && constraintWidget.mCenter.mTarget == null) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
            }
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                if (constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {}
                if (b) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                    return false;
                }
                if (constraintWidget.mTop.mTarget.mOwner != constraintWidget.getParent() || constraintWidget.mBottom.mTarget.mOwner != constraintWidget.getParent()) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                }
            }
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                if (constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {}
                if (b) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                    return false;
                }
                if (constraintWidget.mLeft.mTarget.mOwner != constraintWidget.getParent() || constraintWidget.mRight.mTarget.mOwner != constraintWidget.getParent()) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                }
            }
            if ((constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ^ constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && constraintWidget.mDimensionRatio != 0.0f) {
                resolveDimensionRatio(constraintWidget);
            }
            else if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
            }
            if (((constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) || ((constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mRight.mTarget == null) || (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mLeft.mTarget == null)) || (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidget.mParent)) && constraintWidget.mCenter.mTarget == null) {
                if (!(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Helper)) {
                    constraintWidgetGroup.mStartHorizontalWidgets.add(constraintWidget);
                }
            }
            if (((constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) || ((constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mBottom.mTarget == null) || (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mTop.mTarget == null)) || (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mParent)) && constraintWidget.mCenter.mTarget == null && constraintWidget.mBaseline.mTarget == null) {
                if (!(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Helper)) {
                    constraintWidgetGroup.mStartVerticalWidgets.add(constraintWidget);
                }
            }
            if (constraintWidget instanceof Helper) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
                final Helper helper = (Helper)constraintWidget;
                for (int i = 0; i < helper.mWidgetsCount; ++i) {
                    if (!traverse(helper.mWidgets[i], constraintWidgetGroup, list, b)) {
                        return false;
                    }
                }
            }
            for (int length = constraintWidget.mListAnchors.length, j = 0; j < length; ++j) {
                final ConstraintAnchor connection = constraintWidget.mListAnchors[j];
                if (connection.mTarget != null && connection.mTarget.mOwner != constraintWidget.getParent()) {
                    if (connection.mType == ConstraintAnchor.Type.CENTER) {
                        invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                        if (b) {
                            return false;
                        }
                    }
                    else {
                        setConnection(connection);
                    }
                    if (!traverse(connection.mTarget.mOwner, constraintWidgetGroup, list, b)) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (constraintWidget.mBelongingGroup != constraintWidgetGroup) {
            constraintWidgetGroup.mConstrainedGroup.addAll(constraintWidget.mBelongingGroup.mConstrainedGroup);
            constraintWidgetGroup.mStartHorizontalWidgets.addAll(constraintWidget.mBelongingGroup.mStartHorizontalWidgets);
            constraintWidgetGroup.mStartVerticalWidgets.addAll(constraintWidget.mBelongingGroup.mStartVerticalWidgets);
            if (!constraintWidget.mBelongingGroup.mSkipSolver) {
                constraintWidgetGroup.mSkipSolver = false;
            }
            list.remove(constraintWidget.mBelongingGroup);
            final Iterator<ConstraintWidget> iterator = constraintWidget.mBelongingGroup.mConstrainedGroup.iterator();
            while (iterator.hasNext()) {
                iterator.next().mBelongingGroup = constraintWidgetGroup;
            }
            return true;
        }
        return true;
    }
    
    private static void updateSizeDependentWidgets(final ConstraintWidget constraintWidget, final int n, int resolveDimensionRatio) {
        final int n2 = n * 2;
        final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n2];
        final ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n2 + 1];
        if (constraintAnchor.mTarget != null && constraintAnchor2.mTarget != null) {
            Optimizer.setOptimizedWidget(constraintWidget, n, getParentBiasOffset(constraintWidget, n) + constraintAnchor.getMargin());
            return;
        }
        if (constraintWidget.mDimensionRatio != 0.0f && constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            resolveDimensionRatio = resolveDimensionRatio(constraintWidget);
            final int n3 = (int)constraintWidget.mListAnchors[n2].getResolutionNode().resolvedOffset;
            constraintAnchor2.getResolutionNode().resolvedTarget = constraintAnchor.getResolutionNode();
            constraintAnchor2.getResolutionNode().resolvedOffset = (float)resolveDimensionRatio;
            constraintAnchor2.getResolutionNode().state = 1;
            constraintWidget.setFrame(n3, n3 + resolveDimensionRatio, n);
            return;
        }
        resolveDimensionRatio -= constraintWidget.getRelativePositioning(n);
        final int n4 = resolveDimensionRatio - constraintWidget.getLength(n);
        constraintWidget.setFrame(n4, resolveDimensionRatio, n);
        Optimizer.setOptimizedWidget(constraintWidget, n, n4);
    }
}
