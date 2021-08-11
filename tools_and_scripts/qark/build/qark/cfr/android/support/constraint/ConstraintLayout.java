/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 */
package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.constraint.R;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ConstraintLayout
extends ViewGroup {
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean SIMPLE_LAYOUT = true;
    private static final String TAG = "ConstraintLayout";
    public static final String VERSION = "ConstraintLayout-1.0.0";
    SparseArray<View> mChildrenByIds = new SparseArray();
    private ConstraintSet mConstraintSet = null;
    private boolean mDirtyHierarchy = true;
    ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMinHeight = 0;
    private int mMinWidth = 0;
    private int mOptimizationLevel = 2;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList(100);

    public ConstraintLayout(Context context) {
        super(context);
        this.init(null);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    private final ConstraintWidget getTargetWidget(int n) {
        if (n == 0) {
            return this.mLayoutWidget;
        }
        View view = (View)this.mChildrenByIds.get(n);
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).widget;
    }

    private final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).widget;
    }

    private void init(AttributeSet attributeSet) {
        this.mLayoutWidget.setCompanionWidget((Object)this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = attributeSet.getDimensionPixelOffset(n2, this.mMinWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = attributeSet.getDimensionPixelOffset(n2, this.mMinHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = attributeSet.getDimensionPixelOffset(n2, this.mMaxWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = attributeSet.getDimensionPixelOffset(n2, this.mMaxHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = attributeSet.getInt(n2, this.mOptimizationLevel);
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_constraintSet) continue;
                n2 = attributeSet.getResourceId(n2, 0);
                this.mConstraintSet = new ConstraintSet();
                this.mConstraintSet.load(this.getContext(), n2);
            }
            attributeSet.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    /*
     * Exception decompiling
     */
    private void internalMeasureChildren(int var1_1, int var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Statement already marked as first in another block
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.markFirstStatementInBlock(Op03SimpleStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.markWholeBlock(Misc.java:219)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.ConditionalRewriter.considerAsSimpleIf(ConditionalRewriter.java:619)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.ConditionalRewriter.identifyNonjumpingConditionals(ConditionalRewriter.java:45)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:679)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void setChildrenConstraints() {
        var13_1 = this.mConstraintSet;
        if (var13_1 != null) {
            var13_1.applyToInternal(this);
        }
        var12_2 = this.getChildCount();
        this.mLayoutWidget.removeAllChildren();
        var8_3 = 0;
        while (var8_3 < var12_2) {
            block44 : {
                block46 : {
                    block45 : {
                        var15_15 = this.getChildAt(var8_3);
                        var14_14 = this.getViewWidget((View)var15_15);
                        if (var14_14 == null) break block44;
                        var13_1 = (LayoutParams)var15_15.getLayoutParams();
                        var14_14.reset();
                        var14_14.setVisibility(var15_15.getVisibility());
                        var14_14.setCompanionWidget(var15_15);
                        this.mLayoutWidget.add(var14_14);
                        if (!var13_1.verticalDimensionFixed || !var13_1.horizontalDimensionFixed) {
                            this.mVariableDimensionsWidgets.add(var14_14);
                        }
                        if (!var13_1.isGuideline) break block45;
                        var14_14 = (android.support.constraint.solver.widgets.Guideline)var14_14;
                        if (var13_1.guideBegin != -1) {
                            var14_14.setGuideBegin(var13_1.guideBegin);
                        }
                        if (var13_1.guideEnd != -1) {
                            var14_14.setGuideEnd(var13_1.guideEnd);
                        }
                        if (var13_1.guidePercent != -1.0f) {
                            var14_14.setGuidePercent(var13_1.guidePercent);
                        }
                        break block44;
                    }
                    if (var13_1.resolvedLeftToLeft == -1 && var13_1.resolvedLeftToRight == -1 && var13_1.resolvedRightToLeft == -1 && var13_1.resolvedRightToRight == -1 && var13_1.topToTop == -1 && var13_1.topToBottom == -1 && var13_1.bottomToTop == -1 && var13_1.bottomToBottom == -1 && var13_1.baselineToBaseline == -1 && var13_1.editorAbsoluteX == -1 && var13_1.editorAbsoluteY == -1 && var13_1.width != -1 && var13_1.height != -1) break block44;
                    var4_7 = var13_1.resolvedLeftToLeft;
                    var5_8 = var13_1.resolvedLeftToRight;
                    var3_6 = var13_1.resolvedRightToLeft;
                    var2_5 = var13_1.resolvedRightToRight;
                    var6_9 = var13_1.resolveGoneLeftMargin;
                    var7_10 = var13_1.resolveGoneRightMargin;
                    var1_4 = var13_1.resolvedHorizontalBias;
                    if (Build.VERSION.SDK_INT >= 17) break block46;
                    var2_5 = var13_1.leftToLeft;
                    var3_6 = var13_1.leftToRight;
                    var7_10 = var13_1.rightToLeft;
                    var6_9 = var13_1.rightToRight;
                    var5_8 = var13_1.goneLeftMargin;
                    var4_7 = var13_1.goneRightMargin;
                    var1_4 = var13_1.horizontalBias;
                    if (var2_5 == -1 && var3_6 == -1) {
                        if (var13_1.startToStart != -1) {
                            var2_5 = var13_1.startToStart;
                        } else if (var13_1.startToEnd != -1) {
                            var3_6 = var13_1.startToEnd;
                        }
                    }
                    if (var7_10 != -1 || var6_9 != -1) ** GOTO lbl-1000
                    if (var13_1.endToStart != -1) {
                        var11_13 = var13_1.endToStart;
                        var7_10 = var3_6;
                        var9_11 = var5_8;
                        var10_12 = var4_7;
                        var3_6 = var11_13;
                        var4_7 = var2_5;
                        var2_5 = var6_9;
                        var5_8 = var7_10;
                        var6_9 = var9_11;
                        var7_10 = var10_12;
                    } else if (var13_1.endToEnd != -1) {
                        var11_13 = var13_1.endToEnd;
                        var6_9 = var3_6;
                        var9_11 = var5_8;
                        var10_12 = var4_7;
                        var3_6 = var7_10;
                        var5_8 = var11_13;
                        var4_7 = var2_5;
                        var2_5 = var5_8;
                        var5_8 = var6_9;
                        var6_9 = var9_11;
                        var7_10 = var10_12;
                    } else lbl-1000: // 2 sources:
                    {
                        var9_11 = var3_6;
                        var10_12 = var5_8;
                        var11_13 = var4_7;
                        var3_6 = var7_10;
                        var4_7 = var2_5;
                        var2_5 = var6_9;
                        var5_8 = var9_11;
                        var6_9 = var10_12;
                        var7_10 = var11_13;
                    }
                }
                if (var4_7 != -1) {
                    var15_15 = this.getTargetWidget(var4_7);
                    if (var15_15 != null) {
                        var14_14.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var15_15, ConstraintAnchor.Type.LEFT, var13_1.leftMargin, var6_9);
                    }
                } else if (var5_8 != -1 && (var15_15 = this.getTargetWidget(var5_8)) != null) {
                    var14_14.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var15_15, ConstraintAnchor.Type.RIGHT, var13_1.leftMargin, var6_9);
                }
                if (var3_6 != -1) {
                    var15_15 = this.getTargetWidget(var3_6);
                    if (var15_15 != null) {
                        var14_14.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var15_15, ConstraintAnchor.Type.LEFT, var13_1.rightMargin, var7_10);
                    }
                } else if (var2_5 != -1 && (var15_15 = this.getTargetWidget(var2_5)) != null) {
                    var14_14.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var15_15, ConstraintAnchor.Type.RIGHT, var13_1.rightMargin, var7_10);
                }
                if (var13_1.topToTop != -1) {
                    var15_15 = this.getTargetWidget(var13_1.topToTop);
                    if (var15_15 != null) {
                        var14_14.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var15_15, ConstraintAnchor.Type.TOP, var13_1.topMargin, var13_1.goneTopMargin);
                    }
                } else if (var13_1.topToBottom != -1 && (var15_15 = this.getTargetWidget(var13_1.topToBottom)) != null) {
                    var14_14.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var15_15, ConstraintAnchor.Type.BOTTOM, var13_1.topMargin, var13_1.goneTopMargin);
                }
                if (var13_1.bottomToTop != -1) {
                    var15_15 = this.getTargetWidget(var13_1.bottomToTop);
                    if (var15_15 != null) {
                        var14_14.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var15_15, ConstraintAnchor.Type.TOP, var13_1.bottomMargin, var13_1.goneBottomMargin);
                    }
                } else if (var13_1.bottomToBottom != -1 && (var15_15 = this.getTargetWidget(var13_1.bottomToBottom)) != null) {
                    var14_14.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var15_15, ConstraintAnchor.Type.BOTTOM, var13_1.bottomMargin, var13_1.goneBottomMargin);
                }
                if (var13_1.baselineToBaseline != -1) {
                    var16_16 = (View)this.mChildrenByIds.get(var13_1.baselineToBaseline);
                    var15_15 = this.getTargetWidget(var13_1.baselineToBaseline);
                    if (var15_15 != null && var16_16 != null && var16_16.getLayoutParams() instanceof LayoutParams) {
                        var16_16 = (LayoutParams)var16_16.getLayoutParams();
                        var13_1.needsBaseline = true;
                        var16_16.needsBaseline = true;
                        var14_14.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var15_15.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                        var14_14.getAnchor(ConstraintAnchor.Type.TOP).reset();
                        var14_14.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                    }
                }
                if (var1_4 >= 0.0f && var1_4 != 0.5f) {
                    var14_14.setHorizontalBiasPercent(var1_4);
                }
                if (var13_1.verticalBias >= 0.0f && var13_1.verticalBias != 0.5f) {
                    var14_14.setVerticalBiasPercent(var13_1.verticalBias);
                }
                if (this.isInEditMode() && (var13_1.editorAbsoluteX != -1 || var13_1.editorAbsoluteY != -1)) {
                    var14_14.setOrigin(var13_1.editorAbsoluteX, var13_1.editorAbsoluteY);
                }
                if (!var13_1.horizontalDimensionFixed) {
                    if (var13_1.width == -1) {
                        var14_14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                        var14_14.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.LEFT).mMargin = var13_1.leftMargin;
                        var14_14.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.RIGHT).mMargin = var13_1.rightMargin;
                    } else {
                        var14_14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                        var14_14.setWidth(0);
                    }
                } else {
                    var14_14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    var14_14.setWidth(var13_1.width);
                }
                if (!var13_1.verticalDimensionFixed) {
                    if (var13_1.height == -1) {
                        var14_14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                        var14_14.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.TOP).mMargin = var13_1.topMargin;
                        var14_14.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.BOTTOM).mMargin = var13_1.bottomMargin;
                    } else {
                        var14_14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                        var14_14.setHeight(0);
                    }
                } else {
                    var14_14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    var14_14.setHeight(var13_1.height);
                }
                if (var13_1.dimensionRatio != null) {
                    var14_14.setDimensionRatio(var13_1.dimensionRatio);
                }
                var14_14.setHorizontalWeight(var13_1.horizontalWeight);
                var14_14.setVerticalWeight(var13_1.verticalWeight);
                var14_14.setHorizontalChainStyle(var13_1.horizontalChainStyle);
                var14_14.setVerticalChainStyle(var13_1.verticalChainStyle);
                var14_14.setHorizontalMatchStyle(var13_1.matchConstraintDefaultWidth, var13_1.matchConstraintMinWidth, var13_1.matchConstraintMaxWidth);
                var14_14.setVerticalMatchStyle(var13_1.matchConstraintDefaultHeight, var13_1.matchConstraintMinHeight, var13_1.matchConstraintMaxHeight);
            }
            ++var8_3;
        }
    }

    private void setSelfDimensionBehaviour(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        n = View.MeasureSpec.getSize((int)n);
        int n4 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        int n5 = this.getPaddingTop();
        int n6 = this.getPaddingBottom();
        int n7 = this.getPaddingLeft();
        int n8 = this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        int n9 = 0;
        int n10 = 0;
        this.getLayoutParams();
        if (n3 != Integer.MIN_VALUE) {
            if (n3 != 0) {
                n = n3 != 1073741824 ? n9 : Math.min(this.mMaxWidth, n) - (n7 + n8);
            } else {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n = n9;
            }
        } else {
            dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        if (n4 != Integer.MIN_VALUE) {
            if (n4 != 0) {
                n2 = n4 != 1073741824 ? n10 : Math.min(this.mMaxHeight, n2) - (n5 + n6);
            } else {
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n2 = n10;
            }
        } else {
            dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        this.mLayoutWidget.setMinWidth(0);
        this.mLayoutWidget.setMinHeight(0);
        this.mLayoutWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
        this.mLayoutWidget.setWidth(n);
        this.mLayoutWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
        this.mLayoutWidget.setHeight(n2);
        this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
        this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
    }

    private void updateHierarchy() {
        boolean bl;
        int n = this.getChildCount();
        boolean bl2 = false;
        int n2 = 0;
        do {
            bl = bl2;
            if (n2 >= n) break;
            if (this.getChildAt(n2).isLayoutRequested()) {
                bl = true;
                break;
            }
            ++n2;
        } while (true);
        if (bl) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
            return;
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n, layoutParams);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
            return;
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
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

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = this.getChildCount();
        bl = this.isInEditMode();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            Object object = (LayoutParams)view.getLayoutParams();
            if (view.getVisibility() == 8 && !object.isGuideline && !bl) continue;
            object = object.widget;
            n3 = object.getDrawX();
            n4 = object.getDrawY();
            view.layout(n3, n4, object.getWidth() + n3, object.getHeight() + n4);
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        int n4;
        int n5 = this.getPaddingLeft();
        int n6 = this.getPaddingTop();
        this.mLayoutWidget.setX(n5);
        this.mLayoutWidget.setY(n6);
        this.setSelfDimensionBehaviour(n, n2);
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
        }
        this.internalMeasureChildren(n, n2);
        if (this.getChildCount() > 0) {
            this.solveLinearSystem();
        }
        int n7 = 0;
        int n8 = 0;
        int n9 = this.mVariableDimensionsWidgets.size();
        int n10 = this.getPaddingBottom() + n6;
        int n11 = this.getPaddingRight() + n5;
        if (n9 > 0) {
            n3 = 0;
            Object object = this.mLayoutWidget.getHorizontalDimensionBehaviour();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            n4 = 1;
            n7 = object == dimensionBehaviour ? 1 : 0;
            if (this.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                n4 = 0;
            }
            int n12 = n7;
            n7 = n8;
            for (int i = 0; i < n9; ++i) {
                object = this.mVariableDimensionsWidgets.get(i);
                if (object instanceof android.support.constraint.solver.widgets.Guideline || (dimensionBehaviour = (View)object.getCompanionWidget()) == null || dimensionBehaviour.getVisibility() == 8) continue;
                LayoutParams layoutParams = (LayoutParams)dimensionBehaviour.getLayoutParams();
                n8 = layoutParams.width == -2 ? ConstraintLayout.getChildMeasureSpec((int)n, (int)n11, (int)layoutParams.width) : View.MeasureSpec.makeMeasureSpec((int)object.getWidth(), (int)1073741824);
                int n13 = layoutParams.height == -2 ? ConstraintLayout.getChildMeasureSpec((int)n2, (int)n10, (int)layoutParams.height) : View.MeasureSpec.makeMeasureSpec((int)object.getHeight(), (int)1073741824);
                dimensionBehaviour.measure(n8, n13);
                n13 = dimensionBehaviour.getMeasuredWidth();
                n8 = dimensionBehaviour.getMeasuredHeight();
                if (n13 != object.getWidth()) {
                    object.setWidth(n13);
                    if (n12 != 0 && object.getRight() > this.mLayoutWidget.getWidth()) {
                        n3 = object.getRight();
                        n13 = object.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin();
                        this.mLayoutWidget.setWidth(Math.max(this.mMinWidth, n3 + n13));
                    }
                    n3 = 1;
                }
                if (n8 != object.getHeight()) {
                    object.setHeight(n8);
                    if (n4 != 0 && object.getBottom() > this.mLayoutWidget.getHeight()) {
                        n3 = object.getBottom();
                        n8 = object.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin();
                        this.mLayoutWidget.setHeight(Math.max(this.mMinHeight, n3 + n8));
                    }
                    n3 = 1;
                }
                if (layoutParams.needsBaseline && (n8 = dimensionBehaviour.getBaseline()) != -1 && n8 != object.getBaselineDistance()) {
                    object.setBaselineDistance(n8);
                    n3 = 1;
                }
                if (Build.VERSION.SDK_INT < 11) continue;
                n7 = ConstraintLayout.combineMeasuredStates((int)n7, (int)dimensionBehaviour.getMeasuredState());
            }
            if (n3 != 0) {
                this.solveLinearSystem();
            }
        }
        n3 = this.mLayoutWidget.getWidth() + n11;
        n4 = this.mLayoutWidget.getHeight() + n10;
        if (Build.VERSION.SDK_INT >= 11) {
            n = ConstraintLayout.resolveSizeAndState((int)n3, (int)n, (int)n7);
            n2 = ConstraintLayout.resolveSizeAndState((int)n4, (int)n2, (int)(n7 << 16));
            n = Math.min(this.mMaxWidth, n);
            n2 = Math.min(this.mMaxHeight, n2);
            n &= 16777215;
            n2 &= 16777215;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                n |= 16777216;
            }
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                n2 |= 16777216;
            }
            this.setMeasuredDimension(n, n2);
            return;
        }
        this.setMeasuredDimension(n3, n4);
    }

    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        Object object = this.getViewWidget(view);
        if (view instanceof Guideline && !(object instanceof android.support.constraint.solver.widgets.Guideline)) {
            object = (LayoutParams)view.getLayoutParams();
            object.widget = new android.support.constraint.solver.widgets.Guideline();
            object.isGuideline = true;
            ((android.support.constraint.solver.widgets.Guideline)object.widget).setOrientation(object.orientation);
            object = object.widget;
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }

    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(this.getViewWidget(view));
        this.mDirtyHierarchy = true;
    }

    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
            return;
        }
    }

    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
    }

    public void setConstraintSet(ConstraintSet constraintSet) {
        this.mConstraintSet = constraintSet;
    }

    public void setId(int n) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(n);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }

    public void setMaxHeight(int n) {
        if (n == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = n;
        this.requestLayout();
    }

    public void setMaxWidth(int n) {
        if (n == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = n;
        this.requestLayout();
    }

    public void setMinHeight(int n) {
        if (n == this.mMinHeight) {
            return;
        }
        this.mMinHeight = n;
        this.requestLayout();
    }

    public void setMinWidth(int n) {
        if (n == this.mMinWidth) {
            return;
        }
        this.mMinWidth = n;
        this.requestLayout();
    }

    public void setOptimizationLevel(int n) {
        this.mLayoutWidget.setOptimizationLevel(n);
    }

    protected void solveLinearSystem() {
        this.mLayoutWidget.layout();
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
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
        public int baselineToBaseline = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public String dimensionRatio = null;
        int dimensionRatioSide = 1;
        float dimensionRatioValue = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneLeftMargin = -1;
        public int goneRightMargin = -1;
        public int goneStartMargin = -1;
        public int goneTopMargin = -1;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        boolean horizontalDimensionFixed = true;
        public float horizontalWeight = 0.0f;
        boolean isGuideline = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMinWidth = 0;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        boolean verticalDimensionFixed = true;
        public float verticalWeight = 0.0f;
        ConstraintWidget widget = new ConstraintWidget();

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet object) {
            super(context, (AttributeSet)object);
            context = context.obtainStyledAttributes((AttributeSet)object, R.styleable.ConstraintLayout_Layout);
            int n = context.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = context.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
                    this.leftToLeft = context.getResourceId(n2, this.leftToLeft);
                    if (this.leftToLeft != -1) continue;
                    this.leftToLeft = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
                    this.leftToRight = context.getResourceId(n2, this.leftToRight);
                    if (this.leftToRight != -1) continue;
                    this.leftToRight = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
                    this.rightToLeft = context.getResourceId(n2, this.rightToLeft);
                    if (this.rightToLeft != -1) continue;
                    this.rightToLeft = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
                    this.rightToRight = context.getResourceId(n2, this.rightToRight);
                    if (this.rightToRight != -1) continue;
                    this.rightToRight = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
                    this.topToTop = context.getResourceId(n2, this.topToTop);
                    if (this.topToTop != -1) continue;
                    this.topToTop = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
                    this.topToBottom = context.getResourceId(n2, this.topToBottom);
                    if (this.topToBottom != -1) continue;
                    this.topToBottom = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
                    this.bottomToTop = context.getResourceId(n2, this.bottomToTop);
                    if (this.bottomToTop != -1) continue;
                    this.bottomToTop = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
                    this.bottomToBottom = context.getResourceId(n2, this.bottomToBottom);
                    if (this.bottomToBottom != -1) continue;
                    this.bottomToBottom = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
                    this.baselineToBaseline = context.getResourceId(n2, this.baselineToBaseline);
                    if (this.baselineToBaseline != -1) continue;
                    this.baselineToBaseline = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
                    this.editorAbsoluteX = context.getDimensionPixelOffset(n2, this.editorAbsoluteX);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
                    this.editorAbsoluteY = context.getDimensionPixelOffset(n2, this.editorAbsoluteY);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
                    this.guideBegin = context.getDimensionPixelOffset(n2, this.guideBegin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
                    this.guideEnd = context.getDimensionPixelOffset(n2, this.guideEnd);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
                    this.guidePercent = context.getFloat(n2, this.guidePercent);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_orientation) {
                    this.orientation = context.getInt(n2, this.orientation);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
                    this.startToEnd = context.getResourceId(n2, this.startToEnd);
                    if (this.startToEnd != -1) continue;
                    this.startToEnd = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
                    this.startToStart = context.getResourceId(n2, this.startToStart);
                    if (this.startToStart != -1) continue;
                    this.startToStart = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
                    this.endToStart = context.getResourceId(n2, this.endToStart);
                    if (this.endToStart != -1) continue;
                    this.endToStart = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
                    this.endToEnd = context.getResourceId(n2, this.endToEnd);
                    if (this.endToEnd != -1) continue;
                    this.endToEnd = context.getInt(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
                    this.goneLeftMargin = context.getDimensionPixelSize(n2, this.goneLeftMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
                    this.goneTopMargin = context.getDimensionPixelSize(n2, this.goneTopMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
                    this.goneRightMargin = context.getDimensionPixelSize(n2, this.goneRightMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
                    this.goneBottomMargin = context.getDimensionPixelSize(n2, this.goneBottomMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
                    this.goneStartMargin = context.getDimensionPixelSize(n2, this.goneStartMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
                    this.goneEndMargin = context.getDimensionPixelSize(n2, this.goneEndMargin);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
                    this.horizontalBias = context.getFloat(n2, this.horizontalBias);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
                    this.verticalBias = context.getFloat(n2, this.verticalBias);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
                    this.dimensionRatio = context.getString(n2);
                    this.dimensionRatioValue = Float.NaN;
                    this.dimensionRatioSide = -1;
                    object = this.dimensionRatio;
                    if (object == null) continue;
                    int n3 = object.length();
                    n2 = this.dimensionRatio.indexOf(44);
                    if (n2 > 0 && n2 < n3 - 1) {
                        object = this.dimensionRatio.substring(0, n2);
                        if (object.equalsIgnoreCase("W")) {
                            this.dimensionRatioSide = 0;
                        } else if (object.equalsIgnoreCase("H")) {
                            this.dimensionRatioSide = 1;
                        }
                        ++n2;
                    } else {
                        n2 = 0;
                    }
                    int n4 = this.dimensionRatio.indexOf(58);
                    if (n4 >= 0 && n4 < n3 - 1) {
                        float f;
                        float f2;
                        object = this.dimensionRatio.substring(n2, n4);
                        String string2 = this.dimensionRatio.substring(n4 + 1);
                        if (object.length() <= 0 || string2.length() <= 0) continue;
                        try {
                            f = Float.parseFloat((String)object);
                            f2 = Float.parseFloat(string2);
                            if (f <= 0.0f || f2 <= 0.0f) continue;
                        }
                        catch (NumberFormatException numberFormatException) {}
                        if (this.dimensionRatioSide == 1) {
                            this.dimensionRatioValue = Math.abs(f2 / f);
                            continue;
                        }
                        this.dimensionRatioValue = Math.abs(f / f2);
                        continue;
                    }
                    object = this.dimensionRatio.substring(n2);
                    if (object.length() <= 0) continue;
                    try {
                        this.dimensionRatioValue = Float.parseFloat((String)object);
                    }
                    catch (NumberFormatException numberFormatException) {}
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight) {
                    this.horizontalWeight = context.getFloat(n2, 0.0f);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight) {
                    this.verticalWeight = context.getFloat(n2, 0.0f);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle) {
                    this.horizontalChainStyle = context.getInt(n2, 0);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle) {
                    this.verticalChainStyle = context.getInt(n2, 0);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default) {
                    this.matchConstraintDefaultWidth = context.getInt(n2, 0);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default) {
                    this.matchConstraintDefaultHeight = context.getInt(n2, 0);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min) {
                    this.matchConstraintMinWidth = context.getDimensionPixelSize(n2, this.matchConstraintMinWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max) {
                    this.matchConstraintMaxWidth = context.getDimensionPixelSize(n2, this.matchConstraintMaxWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min) {
                    this.matchConstraintMinHeight = context.getDimensionPixelSize(n2, this.matchConstraintMinHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max) {
                    this.matchConstraintMaxHeight = context.getDimensionPixelSize(n2, this.matchConstraintMaxHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator || n2 == R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator || n2 == R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator || n2 == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator) continue;
                n2 = R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator;
            }
            context.recycle();
            this.validate();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
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

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        @TargetApi(value=17)
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
            int n2 = this.getLayoutDirection();
            n = 1;
            if (1 != n2) {
                n = 0;
            }
            if (n != 0) {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                } else {
                    n = this.startToStart;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                    }
                }
                n = this.endToStart;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                if ((n = this.endToEnd) != -1) {
                    this.resolvedLeftToLeft = n;
                }
                if ((n = this.goneStartMargin) != -1) {
                    this.resolveGoneRightMargin = n;
                }
                if ((n = this.goneEndMargin) != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
            } else {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                if ((n = this.startToStart) != -1) {
                    this.resolvedLeftToLeft = n;
                }
                if ((n = this.endToStart) != -1) {
                    this.resolvedRightToLeft = n;
                }
                if ((n = this.endToEnd) != -1) {
                    this.resolvedRightToRight = n;
                }
                if ((n = this.goneStartMargin) != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                if ((n = this.goneEndMargin) != -1) {
                    this.resolveGoneRightMargin = n;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1) {
                n = this.rightToLeft;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                } else {
                    n = this.rightToRight;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                    }
                }
            }
            if (this.startToStart == -1 && this.startToEnd == -1) {
                n = this.leftToLeft;
                if (n != -1) {
                    this.resolvedLeftToLeft = n;
                    return;
                }
                n = this.leftToRight;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                    return;
                }
                return;
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
            if (!(this.widget instanceof android.support.constraint.solver.widgets.Guideline)) {
                this.widget = new android.support.constraint.solver.widgets.Guideline();
            }
            ((android.support.constraint.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
        }
    }

}

