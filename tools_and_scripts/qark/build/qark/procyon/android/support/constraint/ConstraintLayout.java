// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint;

import android.annotation.TargetApi;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.os.Build$VERSION;
import android.support.constraint.solver.widgets.Guideline;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.view.View;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ConstraintLayout extends ViewGroup
{
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean SIMPLE_LAYOUT = true;
    private static final String TAG = "ConstraintLayout";
    public static final String VERSION = "ConstraintLayout-1.0.0";
    SparseArray<View> mChildrenByIds;
    private ConstraintSet mConstraintSet;
    private boolean mDirtyHierarchy;
    ConstraintWidgetContainer mLayoutWidget;
    private int mMaxHeight;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMinWidth;
    private int mOptimizationLevel;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    
    public ConstraintLayout(final Context context) {
        super(context);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
        this.init(null);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
        this.init(set);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
        this.init(set);
    }
    
    private final ConstraintWidget getTargetWidget(final int n) {
        if (n == 0) {
            return this.mLayoutWidget;
        }
        final View view = (View)this.mChildrenByIds.get(n);
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).widget;
    }
    
    private final ConstraintWidget getViewWidget(final View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).widget;
    }
    
    private void init(final AttributeSet set) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = obtainStyledAttributes.getInt(index, this.mOptimizationLevel);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    (this.mConstraintSet = new ConstraintSet()).load(this.getContext(), obtainStyledAttributes.getResourceId(index, 0));
                }
            }
            obtainStyledAttributes.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }
    
    private void internalMeasureChildren(final int n, final int n2) {
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        final int n4 = this.getPaddingLeft() + this.getPaddingRight();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final ConstraintWidget widget = layoutParams.widget;
                if (!layoutParams.isGuideline) {
                    int n5 = layoutParams.width;
                    int n6 = layoutParams.height;
                    final boolean horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
                    boolean b2;
                    final boolean b = b2 = true;
                    Label_0202: {
                        if (!horizontalDimensionFixed) {
                            b2 = b;
                            if (!layoutParams.verticalDimensionFixed) {
                                if (!layoutParams.horizontalDimensionFixed) {
                                    b2 = b;
                                    if (layoutParams.matchConstraintDefaultWidth == 1) {
                                        break Label_0202;
                                    }
                                }
                                b2 = b;
                                if (layoutParams.width != -1) {
                                    if (!layoutParams.verticalDimensionFixed) {
                                        b2 = b;
                                        if (layoutParams.matchConstraintDefaultHeight == 1) {
                                            break Label_0202;
                                        }
                                        if (layoutParams.height == -1) {
                                            b2 = b;
                                            break Label_0202;
                                        }
                                    }
                                    b2 = false;
                                }
                            }
                        }
                    }
                    final boolean b3 = false;
                    final boolean b4 = false;
                    final boolean b5 = false;
                    boolean b6 = false;
                    boolean b7;
                    if (b2) {
                        int n7;
                        if (n5 != 0 && n5 != -1) {
                            n7 = getChildMeasureSpec(n, n4, n5);
                            b7 = b4;
                        }
                        else {
                            n7 = getChildMeasureSpec(n, n4, -2);
                            b7 = true;
                        }
                        int n8;
                        if (n6 != 0 && n6 != -1) {
                            n8 = getChildMeasureSpec(n2, n3, n6);
                        }
                        else {
                            n8 = getChildMeasureSpec(n2, n3, -2);
                            b6 = true;
                        }
                        child.measure(n7, n8);
                        n5 = child.getMeasuredWidth();
                        n6 = child.getMeasuredHeight();
                    }
                    else {
                        b6 = b5;
                        b7 = b3;
                    }
                    widget.setWidth(n5);
                    widget.setHeight(n6);
                    if (b7) {
                        widget.setWrapWidth(n5);
                    }
                    if (b6) {
                        widget.setWrapHeight(n6);
                    }
                    if (layoutParams.needsBaseline) {
                        final int baseline = child.getBaseline();
                        if (baseline != -1) {
                            widget.setBaselineDistance(baseline);
                        }
                    }
                }
            }
        }
    }
    
    private void setChildrenConstraints() {
        final ConstraintSet mConstraintSet = this.mConstraintSet;
        if (mConstraintSet != null) {
            mConstraintSet.applyToInternal(this);
        }
        final int childCount = this.getChildCount();
        this.mLayoutWidget.removeAllChildren();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final ConstraintWidget viewWidget = this.getViewWidget(child);
            if (viewWidget != null) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                viewWidget.reset();
                viewWidget.setVisibility(child.getVisibility());
                viewWidget.setCompanionWidget(child);
                this.mLayoutWidget.add(viewWidget);
                if (!layoutParams.verticalDimensionFixed || !layoutParams.horizontalDimensionFixed) {
                    this.mVariableDimensionsWidgets.add(viewWidget);
                }
                if (layoutParams.isGuideline) {
                    final Guideline guideline = (Guideline)viewWidget;
                    if (layoutParams.guideBegin != -1) {
                        guideline.setGuideBegin(layoutParams.guideBegin);
                    }
                    if (layoutParams.guideEnd != -1) {
                        guideline.setGuideEnd(layoutParams.guideEnd);
                    }
                    if (layoutParams.guidePercent != -1.0f) {
                        guideline.setGuidePercent(layoutParams.guidePercent);
                    }
                }
                else if (layoutParams.resolvedLeftToLeft != -1 || layoutParams.resolvedLeftToRight != -1 || layoutParams.resolvedRightToLeft != -1 || layoutParams.resolvedRightToRight != -1 || layoutParams.topToTop != -1 || layoutParams.topToBottom != -1 || layoutParams.bottomToTop != -1 || layoutParams.bottomToBottom != -1 || layoutParams.baselineToBaseline != -1 || layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1 || layoutParams.width == -1 || layoutParams.height == -1) {
                    int resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
                    int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
                    int resolvedRightToLeft = layoutParams.resolvedRightToLeft;
                    int resolvedRightToRight = layoutParams.resolvedRightToRight;
                    int resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
                    int resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
                    float horizontalBiasPercent = layoutParams.resolvedHorizontalBias;
                    Label_0649: {
                        if (Build$VERSION.SDK_INT < 17) {
                            int n = layoutParams.leftToLeft;
                            int n2 = layoutParams.leftToRight;
                            final int rightToLeft = layoutParams.rightToLeft;
                            final int rightToRight = layoutParams.rightToRight;
                            final int goneLeftMargin = layoutParams.goneLeftMargin;
                            final int goneRightMargin = layoutParams.goneRightMargin;
                            horizontalBiasPercent = layoutParams.horizontalBias;
                            if (n == -1 && n2 == -1) {
                                if (layoutParams.startToStart != -1) {
                                    n = layoutParams.startToStart;
                                }
                                else if (layoutParams.startToEnd != -1) {
                                    n2 = layoutParams.startToEnd;
                                }
                            }
                            if (rightToLeft == -1 && rightToRight == -1) {
                                if (layoutParams.endToStart != -1) {
                                    final int endToStart = layoutParams.endToStart;
                                    final int n3 = n2;
                                    final int n4 = goneLeftMargin;
                                    final int n5 = goneRightMargin;
                                    resolvedRightToLeft = endToStart;
                                    resolvedLeftToLeft = n;
                                    resolvedRightToRight = rightToRight;
                                    resolvedLeftToRight = n3;
                                    resolveGoneLeftMargin = n4;
                                    resolveGoneRightMargin = n5;
                                    break Label_0649;
                                }
                                if (layoutParams.endToEnd != -1) {
                                    final int endToEnd = layoutParams.endToEnd;
                                    final int n6 = n2;
                                    final int n7 = goneLeftMargin;
                                    final int n8 = goneRightMargin;
                                    resolvedRightToLeft = rightToLeft;
                                    final int n9 = endToEnd;
                                    resolvedLeftToLeft = n;
                                    resolvedRightToRight = n9;
                                    resolvedLeftToRight = n6;
                                    resolveGoneLeftMargin = n7;
                                    resolveGoneRightMargin = n8;
                                    break Label_0649;
                                }
                            }
                            final int n10 = n2;
                            final int n11 = goneLeftMargin;
                            final int n12 = goneRightMargin;
                            resolvedRightToLeft = rightToLeft;
                            resolvedLeftToLeft = n;
                            resolvedRightToRight = rightToRight;
                            resolvedLeftToRight = n10;
                            resolveGoneLeftMargin = n11;
                            resolveGoneRightMargin = n12;
                        }
                    }
                    if (resolvedLeftToLeft != -1) {
                        final ConstraintWidget targetWidget = this.getTargetWidget(resolvedLeftToLeft);
                        if (targetWidget != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    }
                    else if (resolvedLeftToRight != -1) {
                        final ConstraintWidget targetWidget2 = this.getTargetWidget(resolvedLeftToRight);
                        if (targetWidget2 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget2, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    }
                    if (resolvedRightToLeft != -1) {
                        final ConstraintWidget targetWidget3 = this.getTargetWidget(resolvedRightToLeft);
                        if (targetWidget3 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget3, ConstraintAnchor.Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    }
                    else if (resolvedRightToRight != -1) {
                        final ConstraintWidget targetWidget4 = this.getTargetWidget(resolvedRightToRight);
                        if (targetWidget4 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget4, ConstraintAnchor.Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    }
                    if (layoutParams.topToTop != -1) {
                        final ConstraintWidget targetWidget5 = this.getTargetWidget(layoutParams.topToTop);
                        if (targetWidget5 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget5, ConstraintAnchor.Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    }
                    else if (layoutParams.topToBottom != -1) {
                        final ConstraintWidget targetWidget6 = this.getTargetWidget(layoutParams.topToBottom);
                        if (targetWidget6 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget6, ConstraintAnchor.Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    }
                    if (layoutParams.bottomToTop != -1) {
                        final ConstraintWidget targetWidget7 = this.getTargetWidget(layoutParams.bottomToTop);
                        if (targetWidget7 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget7, ConstraintAnchor.Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    }
                    else if (layoutParams.bottomToBottom != -1) {
                        final ConstraintWidget targetWidget8 = this.getTargetWidget(layoutParams.bottomToBottom);
                        if (targetWidget8 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget8, ConstraintAnchor.Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    }
                    if (layoutParams.baselineToBaseline != -1) {
                        final View view = (View)this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                        final ConstraintWidget targetWidget9 = this.getTargetWidget(layoutParams.baselineToBaseline);
                        if (targetWidget9 != null && view != null && view.getLayoutParams() instanceof LayoutParams) {
                            final LayoutParams layoutParams2 = (LayoutParams)view.getLayoutParams();
                            layoutParams.needsBaseline = true;
                            layoutParams2.needsBaseline = true;
                            viewWidget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(targetWidget9.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                            viewWidget.getAnchor(ConstraintAnchor.Type.TOP).reset();
                            viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                        }
                    }
                    if (horizontalBiasPercent >= 0.0f && horizontalBiasPercent != 0.5f) {
                        viewWidget.setHorizontalBiasPercent(horizontalBiasPercent);
                    }
                    if (layoutParams.verticalBias >= 0.0f && layoutParams.verticalBias != 0.5f) {
                        viewWidget.setVerticalBiasPercent(layoutParams.verticalBias);
                    }
                    if (this.isInEditMode() && (layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1)) {
                        viewWidget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
                    }
                    if (!layoutParams.horizontalDimensionFixed) {
                        if (layoutParams.width == -1) {
                            viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                            viewWidget.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
                        }
                        else {
                            viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget.setWidth(0);
                        }
                    }
                    else {
                        viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget.setWidth(layoutParams.width);
                    }
                    if (!layoutParams.verticalDimensionFixed) {
                        if (layoutParams.height == -1) {
                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                            viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
                        }
                        else {
                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget.setHeight(0);
                        }
                    }
                    else {
                        viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget.setHeight(layoutParams.height);
                    }
                    if (layoutParams.dimensionRatio != null) {
                        viewWidget.setDimensionRatio(layoutParams.dimensionRatio);
                    }
                    viewWidget.setHorizontalWeight(layoutParams.horizontalWeight);
                    viewWidget.setVerticalWeight(layoutParams.verticalWeight);
                    viewWidget.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
                    viewWidget.setVerticalChainStyle(layoutParams.verticalChainStyle);
                    viewWidget.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth);
                    viewWidget.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight);
                }
            }
        }
    }
    
    private void setSelfDimensionBehaviour(int size, int size2) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        final int mode2 = View$MeasureSpec.getMode(size2);
        size2 = View$MeasureSpec.getSize(size2);
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        final int n = 0;
        final int n2 = 0;
        this.getLayoutParams();
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {
                    size = n;
                }
                else {
                    size = Math.min(this.mMaxWidth, size) - (paddingLeft + paddingRight);
                }
            }
            else {
                horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                size = n;
            }
        }
        else {
            horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        if (mode2 != Integer.MIN_VALUE) {
            if (mode2 != 0) {
                if (mode2 != 1073741824) {
                    size2 = n2;
                }
                else {
                    size2 = Math.min(this.mMaxHeight, size2) - (paddingTop + paddingBottom);
                }
            }
            else {
                verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                size2 = n2;
            }
        }
        else {
            verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        this.mLayoutWidget.setMinWidth(0);
        this.mLayoutWidget.setMinHeight(0);
        this.mLayoutWidget.setHorizontalDimensionBehaviour(horizontalDimensionBehaviour);
        this.mLayoutWidget.setWidth(size);
        this.mLayoutWidget.setVerticalDimensionBehaviour(verticalDimensionBehaviour);
        this.mLayoutWidget.setHeight(size2);
        this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
        this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
    }
    
    private void updateHierarchy() {
        final int childCount = this.getChildCount();
        final boolean b = false;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= childCount) {
                break;
            }
            if (this.getChildAt(n).isLayoutRequested()) {
                b2 = true;
                break;
            }
            ++n;
        }
        if (b2) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return (ViewGroup$LayoutParams)new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getMaxHeight() {
        return this.mMaxHeight;
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    protected void onLayout(final boolean b, int i, int childCount, int drawX, int drawY) {
        childCount = this.getChildCount();
        final boolean inEditMode = this.isInEditMode();
        View child;
        LayoutParams layoutParams;
        ConstraintWidget widget;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            if (child.getVisibility() != 8 || layoutParams.isGuideline || inEditMode) {
                widget = layoutParams.widget;
                drawX = widget.getDrawX();
                drawY = widget.getDrawY();
                child.layout(drawX, drawY, widget.getWidth() + drawX, widget.getHeight() + drawY);
            }
        }
    }
    
    protected void onMeasure(int n, int n2) {
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        this.mLayoutWidget.setX(paddingLeft);
        this.mLayoutWidget.setY(paddingTop);
        this.setSelfDimensionBehaviour(n, n2);
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
        }
        this.internalMeasureChildren(n, n2);
        if (this.getChildCount() > 0) {
            this.solveLinearSystem();
        }
        int combineMeasuredStates = 0;
        final int n3 = 0;
        final int size = this.mVariableDimensionsWidgets.size();
        final int n4 = this.getPaddingBottom() + paddingTop;
        final int n5 = this.getPaddingRight() + paddingLeft;
        if (size > 0) {
            boolean b = false;
            final ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = this.mLayoutWidget.getHorizontalDimensionBehaviour();
            final ConstraintWidget.DimensionBehaviour wrap_CONTENT = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            boolean b2 = true;
            final boolean b3 = horizontalDimensionBehaviour == wrap_CONTENT;
            if (this.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                b2 = false;
            }
            int i = 0;
            final boolean b4 = b3;
            combineMeasuredStates = n3;
            while (i < size) {
                final ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(i);
                if (!(constraintWidget instanceof Guideline)) {
                    final View view = (View)constraintWidget.getCompanionWidget();
                    if (view != null) {
                        if (view.getVisibility() != 8) {
                            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                            int n6;
                            if (layoutParams.width == -2) {
                                n6 = getChildMeasureSpec(n, n5, layoutParams.width);
                            }
                            else {
                                n6 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
                            }
                            int n7;
                            if (layoutParams.height == -2) {
                                n7 = getChildMeasureSpec(n2, n4, layoutParams.height);
                            }
                            else {
                                n7 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
                            }
                            view.measure(n6, n7);
                            final int measuredWidth = view.getMeasuredWidth();
                            final int measuredHeight = view.getMeasuredHeight();
                            if (measuredWidth != constraintWidget.getWidth()) {
                                constraintWidget.setWidth(measuredWidth);
                                if (b4) {
                                    if (constraintWidget.getRight() > this.mLayoutWidget.getWidth()) {
                                        this.mLayoutWidget.setWidth(Math.max(this.mMinWidth, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()));
                                    }
                                }
                                b = true;
                            }
                            if (measuredHeight != constraintWidget.getHeight()) {
                                constraintWidget.setHeight(measuredHeight);
                                if (b2 && constraintWidget.getBottom() > this.mLayoutWidget.getHeight()) {
                                    this.mLayoutWidget.setHeight(Math.max(this.mMinHeight, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()));
                                }
                                b = true;
                            }
                            if (layoutParams.needsBaseline) {
                                final int baseline = view.getBaseline();
                                if (baseline != -1 && baseline != constraintWidget.getBaselineDistance()) {
                                    constraintWidget.setBaselineDistance(baseline);
                                    b = true;
                                }
                            }
                            if (Build$VERSION.SDK_INT >= 11) {
                                combineMeasuredStates = combineMeasuredStates(combineMeasuredStates, view.getMeasuredState());
                            }
                        }
                    }
                }
                ++i;
            }
            if (b) {
                this.solveLinearSystem();
            }
        }
        final int n8 = this.mLayoutWidget.getWidth() + n5;
        final int n9 = this.mLayoutWidget.getHeight() + n4;
        if (Build$VERSION.SDK_INT >= 11) {
            n = resolveSizeAndState(n8, n, combineMeasuredStates);
            n2 = resolveSizeAndState(n9, n2, combineMeasuredStates << 16);
            n = Math.min(this.mMaxWidth, n);
            n2 = Math.min(this.mMaxHeight, n2);
            n &= 0xFFFFFF;
            n2 &= 0xFFFFFF;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                n |= 0x1000000;
            }
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                n2 |= 0x1000000;
            }
            this.setMeasuredDimension(n, n2);
            return;
        }
        this.setMeasuredDimension(n8, n9);
    }
    
    public void onViewAdded(final View view) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        final ConstraintWidget viewWidget = this.getViewWidget(view);
        if (view instanceof android.support.constraint.Guideline) {
            if (!(viewWidget instanceof Guideline)) {
                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                layoutParams.widget = new Guideline();
                layoutParams.isGuideline = true;
                ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
                final ConstraintWidget widget = layoutParams.widget;
            }
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }
    
    public void onViewRemoved(final View view) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(this.getViewWidget(view));
        this.mDirtyHierarchy = true;
    }
    
    public void removeView(final View view) {
        super.removeView(view);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }
    
    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
    }
    
    public void setConstraintSet(final ConstraintSet mConstraintSet) {
        this.mConstraintSet = mConstraintSet;
    }
    
    public void setId(final int id) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(id);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }
    
    public void setMaxHeight(final int mMaxHeight) {
        if (mMaxHeight == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = mMaxHeight;
        this.requestLayout();
    }
    
    public void setMaxWidth(final int mMaxWidth) {
        if (mMaxWidth == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = mMaxWidth;
        this.requestLayout();
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight == this.mMinHeight) {
            return;
        }
        this.mMinHeight = mMinHeight;
        this.requestLayout();
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth == this.mMinWidth) {
            return;
        }
        this.mMinWidth = mMinWidth;
        this.requestLayout();
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        this.mLayoutWidget.setOptimizationLevel(optimizationLevel);
    }
    
    protected void solveLinearSystem() {
        this.mLayoutWidget.layout();
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline;
        public int bottomToBottom;
        public int bottomToTop;
        public String dimensionRatio;
        int dimensionRatioSide;
        float dimensionRatioValue;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public float horizontalBias;
        public int horizontalChainStyle;
        boolean horizontalDimensionFixed;
        public float horizontalWeight;
        boolean isGuideline;
        public int leftToLeft;
        public int leftToRight;
        public int matchConstraintDefaultHeight;
        public int matchConstraintDefaultWidth;
        public int matchConstraintMaxHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMinWidth;
        boolean needsBaseline;
        public int orientation;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
        float resolvedHorizontalBias;
        int resolvedLeftToLeft;
        int resolvedLeftToRight;
        int resolvedRightToLeft;
        int resolvedRightToRight;
        public int rightToLeft;
        public int rightToRight;
        public int startToEnd;
        public int startToStart;
        public int topToBottom;
        public int topToTop;
        public float verticalBias;
        public int verticalChainStyle;
        boolean verticalDimensionFixed;
        public float verticalWeight;
        ConstraintWidget widget;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
        }
        
        public LayoutParams(Context obtainStyledAttributes, final AttributeSet set) {
            super(obtainStyledAttributes, set);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = ((TypedArray)obtainStyledAttributes).getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
                    this.leftToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToLeft);
                    if (this.leftToLeft == -1) {
                        this.leftToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
                    this.leftToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToRight);
                    if (this.leftToRight == -1) {
                        this.leftToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
                    this.rightToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToLeft);
                    if (this.rightToLeft == -1) {
                        this.rightToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
                    this.rightToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToRight);
                    if (this.rightToRight == -1) {
                        this.rightToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
                    this.topToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToTop);
                    if (this.topToTop == -1) {
                        this.topToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
                    this.topToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToBottom);
                    if (this.topToBottom == -1) {
                        this.topToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
                    this.bottomToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToTop);
                    if (this.bottomToTop == -1) {
                        this.bottomToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
                    this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToBottom);
                    if (this.bottomToBottom == -1) {
                        this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
                    this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.baselineToBaseline);
                    if (this.baselineToBaseline == -1) {
                        this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
                    this.editorAbsoluteX = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteX);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
                    this.editorAbsoluteY = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteY);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
                    this.guideBegin = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideBegin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
                    this.guideEnd = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideEnd);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
                    this.guidePercent = ((TypedArray)obtainStyledAttributes).getFloat(index, this.guidePercent);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_orientation) {
                    this.orientation = ((TypedArray)obtainStyledAttributes).getInt(index, this.orientation);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
                    this.startToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToEnd);
                    if (this.startToEnd == -1) {
                        this.startToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
                    this.startToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToStart);
                    if (this.startToStart == -1) {
                        this.startToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
                    this.endToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToStart);
                    if (this.endToStart == -1) {
                        this.endToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
                    this.endToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToEnd);
                    if (this.endToEnd == -1) {
                        this.endToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
                    this.goneLeftMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneLeftMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
                    this.goneTopMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneTopMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
                    this.goneRightMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneRightMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
                    this.goneBottomMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneBottomMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
                    this.goneStartMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneStartMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
                    this.goneEndMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneEndMargin);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
                    this.horizontalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalBias);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
                    this.verticalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalBias);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
                    this.dimensionRatio = ((TypedArray)obtainStyledAttributes).getString(index);
                    this.dimensionRatioValue = Float.NaN;
                    this.dimensionRatioSide = -1;
                    final String dimensionRatio = this.dimensionRatio;
                    if (dimensionRatio != null) {
                        final int length = dimensionRatio.length();
                        final int index2 = this.dimensionRatio.indexOf(44);
                        int n;
                        if (index2 > 0 && index2 < length - 1) {
                            final String substring = this.dimensionRatio.substring(0, index2);
                            if (substring.equalsIgnoreCase("W")) {
                                this.dimensionRatioSide = 0;
                            }
                            else if (substring.equalsIgnoreCase("H")) {
                                this.dimensionRatioSide = 1;
                            }
                            n = index2 + 1;
                        }
                        else {
                            n = 0;
                        }
                        final int index3 = this.dimensionRatio.indexOf(58);
                        if (index3 >= 0 && index3 < length - 1) {
                            final String substring2 = this.dimensionRatio.substring(n, index3);
                            final String substring3 = this.dimensionRatio.substring(index3 + 1);
                            if (substring2.length() > 0 && substring3.length() > 0) {
                                try {
                                    final float float1 = Float.parseFloat(substring2);
                                    final float float2 = Float.parseFloat(substring3);
                                    if (float1 > 0.0f && float2 > 0.0f) {
                                        if (this.dimensionRatioSide == 1) {
                                            this.dimensionRatioValue = Math.abs(float2 / float1);
                                        }
                                        else {
                                            this.dimensionRatioValue = Math.abs(float1 / float2);
                                        }
                                    }
                                }
                                catch (NumberFormatException ex) {}
                            }
                        }
                        else {
                            final String substring4 = this.dimensionRatio.substring(n);
                            if (substring4.length() > 0) {
                                try {
                                    this.dimensionRatioValue = Float.parseFloat(substring4);
                                }
                                catch (NumberFormatException ex2) {}
                            }
                        }
                    }
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight) {
                    this.horizontalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, 0.0f);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight) {
                    this.verticalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, 0.0f);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle) {
                    this.horizontalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle) {
                    this.verticalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default) {
                    this.matchConstraintDefaultWidth = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default) {
                    this.matchConstraintDefaultHeight = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min) {
                    this.matchConstraintMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max) {
                    this.matchConstraintMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min) {
                    this.matchConstraintMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max) {
                    this.matchConstraintMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                }
                else if (index != R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator) {
                    if (index != R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator) {
                        if (index != R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator) {
                            if (index != R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator) {
                                final int constraintLayout_Layout_layout_constraintBaseline_creator = R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator;
                            }
                        }
                    }
                }
            }
            ((TypedArray)obtainStyledAttributes).recycle();
            this.validate();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.guidePercent = layoutParams.guidePercent;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.goneLeftMargin = layoutParams.goneLeftMargin;
            this.goneTopMargin = layoutParams.goneTopMargin;
            this.goneRightMargin = layoutParams.goneRightMargin;
            this.goneBottomMargin = layoutParams.goneBottomMargin;
            this.goneStartMargin = layoutParams.goneStartMargin;
            this.goneEndMargin = layoutParams.goneEndMargin;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.dimensionRatioValue = layoutParams.dimensionRatioValue;
            this.dimensionRatioSide = layoutParams.dimensionRatioSide;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.matchConstraintDefaultWidth = layoutParams.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = layoutParams.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = layoutParams.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = layoutParams.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = layoutParams.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = layoutParams.matchConstraintMaxHeight;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
            this.verticalDimensionFixed = layoutParams.verticalDimensionFixed;
            this.needsBaseline = layoutParams.needsBaseline;
            this.isGuideline = layoutParams.isGuideline;
            this.resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            this.resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            this.resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            this.resolvedRightToRight = layoutParams.resolvedRightToRight;
            this.resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            this.resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            this.widget = layoutParams.widget;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
        }
        
        @TargetApi(17)
        public void resolveLayoutDirection(int n) {
            super.resolveLayoutDirection(n);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            final int layoutDirection = this.getLayoutDirection();
            n = 1;
            if (layoutDirection == 0) {
                n = 0;
            }
            if (n != 0) {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                }
                else {
                    n = this.startToStart;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                    }
                }
                n = this.endToStart;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                n = this.endToEnd;
                if (n != -1) {
                    this.resolvedLeftToLeft = n;
                }
                n = this.goneStartMargin;
                if (n != -1) {
                    this.resolveGoneRightMargin = n;
                }
                n = this.goneEndMargin;
                if (n != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
            }
            else {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                n = this.startToStart;
                if (n != -1) {
                    this.resolvedLeftToLeft = n;
                }
                n = this.endToStart;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                }
                n = this.endToEnd;
                if (n != -1) {
                    this.resolvedRightToRight = n;
                }
                n = this.goneStartMargin;
                if (n != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                n = this.goneEndMargin;
                if (n != -1) {
                    this.resolveGoneRightMargin = n;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1) {
                n = this.rightToLeft;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                }
                else {
                    n = this.rightToRight;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                    }
                }
            }
            if (this.startToStart != -1 || this.startToEnd != -1) {
                return;
            }
            n = this.leftToLeft;
            if (n != -1) {
                this.resolvedLeftToLeft = n;
                return;
            }
            n = this.leftToRight;
            if (n != -1) {
                this.resolvedLeftToRight = n;
            }
        }
        
        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
            }
            if (this.guidePercent == -1.0f && this.guideBegin == -1 && this.guideEnd == -1) {
                return;
            }
            this.isGuideline = true;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (!(this.widget instanceof Guideline)) {
                this.widget = new Guideline();
            }
            ((Guideline)this.widget).setOrientation(this.orientation);
        }
    }
}
