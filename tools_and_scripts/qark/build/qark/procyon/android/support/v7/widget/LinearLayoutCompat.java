// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.annotation.RestrictTo;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$MeasureSpec;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public class LinearLayoutCompat extends ViewGroup
{
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;
    
    public LinearLayoutCompat(final Context context) {
        this(context, null);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.LinearLayoutCompat, n, 0);
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (n >= 0) {
            this.setGravity(n);
        }
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!boolean1) {
            this.setBaselineAligned(boolean1);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }
    
    private void forceUniformHeight(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.height == -1) {
                    final int width = layoutParams.width;
                    layoutParams.width = virtualChild.getMeasuredWidth();
                    this.measureChildWithMargins(virtualChild, n2, 0, measureSpec, 0);
                    layoutParams.width = width;
                }
            }
        }
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = virtualChild.getMeasuredHeight();
                    this.measureChildWithMargins(virtualChild, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void drawDividersHorizontal(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8) {
                if (this.hasDividerBeforeChildAt(i)) {
                    final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    int n;
                    if (layoutRtl) {
                        n = virtualChild.getRight() + layoutParams.rightMargin;
                    }
                    else {
                        n = virtualChild.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
                    }
                    this.drawVerticalDivider(canvas, n);
                }
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int paddingLeft;
            if (virtualChild2 == null) {
                if (layoutRtl) {
                    paddingLeft = this.getPaddingLeft();
                }
                else {
                    paddingLeft = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
                }
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                if (layoutRtl) {
                    paddingLeft = virtualChild2.getLeft() - layoutParams2.leftMargin - this.mDividerWidth;
                }
                else {
                    paddingLeft = virtualChild2.getRight() + layoutParams2.rightMargin;
                }
            }
            this.drawVerticalDivider(canvas, paddingLeft);
        }
    }
    
    void drawDividersVertical(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8) {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.drawHorizontalDivider(canvas, virtualChild.getTop() - ((LayoutParams)virtualChild.getLayoutParams()).topMargin - this.mDividerHeight);
                }
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int n;
            if (virtualChild2 == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            }
            else {
                n = virtualChild2.getBottom() + ((LayoutParams)virtualChild2.getLayoutParams()).bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }
    
    void drawHorizontalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }
    
    void drawVerticalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final int mOrientation = this.mOrientation;
        if (mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        final int childCount = this.getChildCount();
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        if (childCount <= mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        final View child = this.getChildAt(mBaselineAlignedChildIndex);
        final int baseline = child.getBaseline();
        if (baseline != -1) {
            int mBaselineChildTop = this.mBaselineChildTop;
            if (this.mOrientation == 1) {
                final int n = this.mGravity & 0x70;
                if (n != 48) {
                    if (n != 16) {
                        if (n == 80) {
                            mBaselineChildTop = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                        }
                    }
                    else {
                        mBaselineChildTop += (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                    }
                }
            }
            return ((LayoutParams)child.getLayoutParams()).topMargin + mBaselineChildTop + baseline;
        }
        if (this.mBaselineAlignedChildIndex == 0) {
            return -1;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
    }
    
    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }
    
    int getChildrenSkipCount(final View view, final int n) {
        return 0;
    }
    
    public Drawable getDividerDrawable() {
        return this.mDivider;
    }
    
    public int getDividerPadding() {
        return this.mDividerPadding;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getDividerWidth() {
        return this.mDividerWidth;
    }
    
    public int getGravity() {
        return this.mGravity;
    }
    
    int getLocationOffset(final View view) {
        return 0;
    }
    
    int getNextLocationOffset(final View view) {
        return 0;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getShowDividers() {
        return this.mShowDividers;
    }
    
    View getVirtualChildAt(final int n) {
        return this.getChildAt(n);
    }
    
    int getVirtualChildCount() {
        return this.getChildCount();
    }
    
    public float getWeightSum() {
        return this.mWeightSum;
    }
    
    protected boolean hasDividerBeforeChildAt(int i) {
        final boolean b = false;
        boolean b2 = false;
        if (i == 0) {
            if ((this.mShowDividers & 0x1) != 0x0) {
                b2 = true;
            }
            return b2;
        }
        if (i == this.getChildCount()) {
            boolean b3 = b;
            if ((this.mShowDividers & 0x4) != 0x0) {
                b3 = true;
            }
            return b3;
        }
        if ((this.mShowDividers & 0x2) != 0x0) {
            for (--i; i >= 0; --i) {
                if (this.getChildAt(i).getVisibility() != 8) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }
    
    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }
    
    void layoutHorizontal(int n, int i, int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - i;
        final int paddingBottom = this.getPaddingBottom();
        final int paddingBottom2 = this.getPaddingBottom();
        final int virtualChildCount = this.getVirtualChildCount();
        final int mGravity = this.mGravity;
        final boolean mBaselineAligned = this.mBaselineAligned;
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        i = GravityCompat.getAbsoluteGravity(mGravity & 0x800007, ViewCompat.getLayoutDirection((View)this));
        if (i != 1) {
            if (i != 5) {
                n = this.getPaddingLeft();
            }
            else {
                n = this.getPaddingLeft() + n2 - n - this.mTotalLength;
            }
        }
        else {
            n = this.getPaddingLeft() + (n2 - n - this.mTotalLength) / 2;
        }
        int n5;
        int n6;
        if (layoutRtl) {
            n5 = virtualChildCount - 1;
            n6 = -1;
        }
        else {
            n5 = 0;
            n6 = 1;
        }
        i = 0;
        n2 = paddingTop;
        n3 = n;
        while (i < virtualChildCount) {
            final int n7 = n5 + n6 * i;
            final View virtualChild = this.getVirtualChildAt(n7);
            if (virtualChild == null) {
                n3 += this.measureNullChild(n7);
            }
            else if (virtualChild.getVisibility() != 8) {
                final int measuredWidth = virtualChild.getMeasuredWidth();
                final int measuredHeight = virtualChild.getMeasuredHeight();
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (mBaselineAligned && layoutParams.height != -1) {
                    n = virtualChild.getBaseline();
                }
                else {
                    n = -1;
                }
                int gravity = layoutParams.gravity;
                if (gravity < 0) {
                    gravity = (mGravity & 0x70);
                }
                final int n8 = gravity & 0x70;
                if (n8 != 16) {
                    if (n8 != 48) {
                        if (n8 != 80) {
                            n = n2;
                        }
                        else {
                            final int n9 = n4 - paddingBottom - measuredHeight - layoutParams.bottomMargin;
                            if (n != -1) {
                                n = n9 - (mMaxDescent[2] - (virtualChild.getMeasuredHeight() - n));
                            }
                            else {
                                n = n9;
                            }
                        }
                    }
                    else {
                        final int n10 = layoutParams.topMargin + n2;
                        if (n != -1) {
                            n = n10 + (mMaxAscent[1] - n);
                        }
                        else {
                            n = n10;
                        }
                    }
                }
                else {
                    n = (n4 - paddingTop - paddingBottom2 - measuredHeight) / 2 + n2 + layoutParams.topMargin - layoutParams.bottomMargin;
                }
                if (this.hasDividerBeforeChildAt(n7)) {
                    n3 += this.mDividerWidth;
                }
                n3 += layoutParams.leftMargin;
                this.setChildFrame(virtualChild, n3 + this.getLocationOffset(virtualChild), n, measuredWidth, measuredHeight);
                n = layoutParams.rightMargin;
                final int nextLocationOffset = this.getNextLocationOffset(virtualChild);
                i += this.getChildrenSkipCount(virtualChild, n7);
                n3 += measuredWidth + n + nextLocationOffset;
            }
            ++i;
        }
    }
    
    void layoutVertical(int paddingTop, int n, int n2, int n3) {
        final int paddingLeft = this.getPaddingLeft();
        final int n4 = n2 - paddingTop;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        final int virtualChildCount = this.getVirtualChildCount();
        final int mGravity = this.mGravity;
        paddingTop = (mGravity & 0x70);
        if (paddingTop != 16) {
            if (paddingTop != 80) {
                paddingTop = this.getPaddingTop();
            }
            else {
                paddingTop = this.getPaddingTop() + n3 - n - this.mTotalLength;
            }
        }
        else {
            paddingTop = this.getPaddingTop() + (n3 - n - this.mTotalLength) / 2;
        }
        n = 0;
        n2 = paddingLeft;
        while (true) {
            n3 = n2;
            if (n >= virtualChildCount) {
                break;
            }
            final View virtualChild = this.getVirtualChildAt(n);
            if (virtualChild == null) {
                paddingTop += this.measureNullChild(n);
            }
            else if (virtualChild.getVisibility() != 8) {
                final int measuredWidth = virtualChild.getMeasuredWidth();
                final int measuredHeight = virtualChild.getMeasuredHeight();
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                n2 = layoutParams.gravity;
                if (n2 < 0) {
                    n2 = (mGravity & 0x800007);
                }
                n2 = (GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                if (n2 != 1) {
                    if (n2 != 5) {
                        n2 = layoutParams.leftMargin + n3;
                    }
                    else {
                        n2 = n4 - paddingRight - measuredWidth - layoutParams.rightMargin;
                    }
                }
                else {
                    n2 = (n4 - paddingLeft - paddingRight2 - measuredWidth) / 2 + n3 + layoutParams.leftMargin - layoutParams.rightMargin;
                }
                if (this.hasDividerBeforeChildAt(n)) {
                    paddingTop += this.mDividerHeight;
                }
                paddingTop += layoutParams.topMargin;
                this.setChildFrame(virtualChild, n2, paddingTop + this.getLocationOffset(virtualChild), measuredWidth, measuredHeight);
                n2 = layoutParams.bottomMargin;
                final int nextLocationOffset = this.getNextLocationOffset(virtualChild);
                n += this.getChildrenSkipCount(virtualChild, n);
                paddingTop += measuredHeight + n2 + nextLocationOffset;
            }
            ++n;
            n2 = n3;
        }
    }
    
    void measureChildBeforeLayout(final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }
    
    void measureHorizontal(final int n, final int n2) {
        this.mTotalLength = 0;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        mMaxAscent[2] = (mMaxAscent[3] = -1);
        mMaxAscent[0] = (mMaxAscent[1] = -1);
        mMaxDescent[2] = (mMaxDescent[3] = -1);
        mMaxDescent[0] = (mMaxDescent[1] = -1);
        final boolean mBaselineAligned = this.mBaselineAligned;
        boolean b = false;
        final boolean mUseLargestChild = this.mUseLargestChild;
        final boolean b2 = mode == 1073741824;
        int n3 = 0;
        int max = Integer.MIN_VALUE;
        boolean b3 = false;
        int n4 = 1;
        int max2 = 0;
        float n5 = 0.0f;
        int i = 0;
        int max3 = 0;
        int max4 = 0;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            int n7;
            int n8;
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
                final int n6 = n3;
                n7 = max3;
                n8 = n6;
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
                final int n9 = n3;
                n7 = max3;
                n8 = n9;
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                n5 += layoutParams.weight;
                if (mode == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (b2) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    }
                    else {
                        final int mTotalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength, layoutParams.leftMargin + mTotalLength + layoutParams.rightMargin);
                    }
                    if (mBaselineAligned) {
                        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChild.measure(measureSpec, measureSpec);
                    }
                    else {
                        b = true;
                    }
                }
                else {
                    int width;
                    if (layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                        layoutParams.width = -2;
                        width = 0;
                    }
                    else {
                        width = Integer.MIN_VALUE;
                    }
                    int mTotalLength2;
                    if (n5 == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, mTotalLength2, n2, 0);
                    if (width != Integer.MIN_VALUE) {
                        layoutParams.width = width;
                    }
                    final LayoutParams layoutParams2 = layoutParams;
                    final int measuredWidth = virtualChild.getMeasuredWidth();
                    if (b2) {
                        this.mTotalLength += layoutParams2.leftMargin + measuredWidth + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild);
                    }
                    else {
                        final int mTotalLength3 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredWidth + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild));
                    }
                    if (mUseLargestChild) {
                        max = Math.max(measuredWidth, max);
                    }
                }
                final int n10 = max4;
                final int n11 = i;
                boolean b4 = false;
                if (mode2 != 1073741824 && layoutParams.height == -1) {
                    b3 = true;
                    b4 = true;
                }
                int n12 = layoutParams.topMargin + layoutParams.bottomMargin;
                final int n13 = virtualChild.getMeasuredHeight() + n12;
                final int combineMeasuredStates = View.combineMeasuredStates(n3, virtualChild.getMeasuredState());
                if (mBaselineAligned) {
                    final int baseline = virtualChild.getBaseline();
                    if (baseline != -1) {
                        int n14;
                        if (layoutParams.gravity < 0) {
                            n14 = this.mGravity;
                        }
                        else {
                            n14 = layoutParams.gravity;
                        }
                        final int n15 = ((n14 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                        mMaxAscent[n15] = Math.max(mMaxAscent[n15], baseline);
                        mMaxDescent[n15] = Math.max(mMaxDescent[n15], n13 - baseline);
                    }
                }
                max2 = Math.max(max2, n13);
                final boolean b5 = n4 != 0 && layoutParams.height == -1;
                if (layoutParams.weight > 0.0f) {
                    if (!b4) {
                        n12 = n13;
                    }
                    max4 = Math.max(n10, n12);
                }
                else {
                    if (!b4) {
                        n12 = n13;
                    }
                    max3 = Math.max(max3, n12);
                    max4 = n10;
                }
                final int childrenSkipCount = this.getChildrenSkipCount(virtualChild, n11);
                n4 = (b5 ? 1 : 0);
                n7 = max3;
                n8 = combineMeasuredStates;
                i = n11 + childrenSkipCount;
            }
            ++i;
            final int n16 = n8;
            max3 = n7;
            n3 = n16;
        }
        final int n17 = max4;
        final int n18 = max2;
        final int n19 = max;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        int max5;
        if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1 && mMaxAscent[3] == -1) {
            max5 = n18;
        }
        else {
            max5 = Math.max(n18, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
        }
        final int n20 = n3;
        if (mUseLargestChild) {
            final int n21 = mode;
            if (n21 == Integer.MIN_VALUE || n21 == 0) {
                this.mTotalLength = 0;
                int j = 0;
                final int n22 = max5;
                while (j < virtualChildCount) {
                    final View virtualChild2 = this.getVirtualChildAt(j);
                    int n23;
                    if (virtualChild2 == null) {
                        this.mTotalLength += this.measureNullChild(j);
                        n23 = j;
                    }
                    else if (virtualChild2.getVisibility() == 8) {
                        n23 = j + this.getChildrenSkipCount(virtualChild2, j);
                    }
                    else {
                        final LayoutParams layoutParams3 = (LayoutParams)virtualChild2.getLayoutParams();
                        if (b2) {
                            final int mTotalLength4 = this.mTotalLength;
                            final int leftMargin = layoutParams3.leftMargin;
                            n23 = j;
                            this.mTotalLength = mTotalLength4 + (leftMargin + n19 + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild2));
                        }
                        else {
                            n23 = j;
                            final int mTotalLength5 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength5, mTotalLength5 + n19 + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild2));
                        }
                    }
                    j = n23 + 1;
                }
                max5 = n22;
            }
        }
        final int n24 = mode;
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), n, 0);
        final int n25 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int n26;
        int n27;
        int max7;
        int n28;
        if (!b && (n25 == 0 || n5 <= 0.0f)) {
            final int max6 = Math.max(max3, n17);
            if (mUseLargestChild && n24 != 1073741824) {
                for (int k = 0; k < virtualChildCount; ++k) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() != 8) {
                            if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(n19, 1073741824), View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredHeight(), 1073741824));
                            }
                        }
                    }
                }
            }
            n26 = n20;
            n27 = virtualChildCount;
            max7 = max5;
            n28 = max6;
        }
        else {
            final float mWeightSum = this.mWeightSum;
            if (mWeightSum > 0.0f) {
                n5 = mWeightSum;
            }
            mMaxAscent[2] = (mMaxAscent[3] = -1);
            mMaxAscent[0] = (mMaxAscent[1] = -1);
            mMaxDescent[2] = (mMaxDescent[3] = -1);
            mMaxDescent[0] = (mMaxDescent[1] = -1);
            final int n29 = -1;
            this.mTotalLength = 0;
            final int n30 = 0;
            final int n31 = max3;
            int combineMeasuredStates2 = n20;
            final int n32 = virtualChildCount;
            int l = n30;
            final int n33 = resolveSizeAndState;
            int n34 = n25;
            final int n35 = n24;
            max7 = n29;
            int n36 = n31;
            while (l < n32) {
                final View virtualChild4 = this.getVirtualChildAt(l);
                if (virtualChild4 != null) {
                    if (virtualChild4.getVisibility() != 8) {
                        final LayoutParams layoutParams4 = (LayoutParams)virtualChild4.getLayoutParams();
                        final float weight = layoutParams4.weight;
                        if (weight > 0.0f) {
                            final int n37 = (int)(n34 * weight / n5);
                            final int childMeasureSpec = getChildMeasureSpec(n2, this.getPaddingTop() + this.getPaddingBottom() + layoutParams4.topMargin + layoutParams4.bottomMargin, layoutParams4.height);
                            if (layoutParams4.width == 0 && n35 == 1073741824) {
                                int n38;
                                if (n37 > 0) {
                                    n38 = n37;
                                }
                                else {
                                    n38 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n38, 1073741824), childMeasureSpec);
                            }
                            else {
                                int n39 = virtualChild4.getMeasuredWidth() + n37;
                                if (n39 < 0) {
                                    n39 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n39, 1073741824), childMeasureSpec);
                            }
                            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates2, virtualChild4.getMeasuredState() & 0xFF000000);
                            n5 -= weight;
                            n34 -= n37;
                        }
                        if (b2) {
                            this.mTotalLength += virtualChild4.getMeasuredWidth() + layoutParams4.leftMargin + layoutParams4.rightMargin + this.getNextLocationOffset(virtualChild4);
                        }
                        else {
                            final int mTotalLength6 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength6, virtualChild4.getMeasuredWidth() + mTotalLength6 + layoutParams4.leftMargin + layoutParams4.rightMargin + this.getNextLocationOffset(virtualChild4));
                        }
                        final boolean b6 = mode2 != 1073741824 && layoutParams4.height == -1;
                        final int n40 = layoutParams4.topMargin + layoutParams4.bottomMargin;
                        final int n41 = virtualChild4.getMeasuredHeight() + n40;
                        final int max8 = Math.max(max7, n41);
                        int n42;
                        if (b6) {
                            n42 = n40;
                        }
                        else {
                            n42 = n41;
                        }
                        final int max9 = Math.max(n36, n42);
                        if (n4 != 0 && layoutParams4.height == -1) {
                            n4 = 1;
                        }
                        else {
                            n4 = 0;
                        }
                        if (mBaselineAligned) {
                            final int baseline2 = virtualChild4.getBaseline();
                            if (baseline2 != -1) {
                                int n43;
                                if (layoutParams4.gravity < 0) {
                                    n43 = this.mGravity;
                                }
                                else {
                                    n43 = layoutParams4.gravity;
                                }
                                final int n44 = ((n43 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                mMaxAscent[n44] = Math.max(mMaxAscent[n44], baseline2);
                                mMaxDescent[n44] = Math.max(mMaxDescent[n44], n41 - baseline2);
                            }
                        }
                        n36 = max9;
                        max7 = max8;
                    }
                }
                ++l;
            }
            final int n45 = n33;
            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            if (mMaxAscent[1] != -1 || mMaxAscent[0] != -1 || mMaxAscent[2] != -1 || mMaxAscent[3] != -1) {
                max7 = Math.max(max7, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
            }
            final int n46 = n36;
            n27 = n32;
            resolveSizeAndState = n45;
            n28 = n46;
            n26 = combineMeasuredStates2;
        }
        if (n4 != 0 || mode2 == 1073741824) {
            n28 = max7;
        }
        this.setMeasuredDimension(resolveSizeAndState | (0xFF000000 & n26), View.resolveSizeAndState(Math.max(n28 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, n26 << 16));
        if (b3) {
            this.forceUniformHeight(n27, n);
        }
    }
    
    int measureNullChild(final int n) {
        return 0;
    }
    
    void measureVertical(final int n, final int n2) {
        this.mTotalLength = 0;
        int combineMeasuredStates = 0;
        float n3 = 0.0f;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        final boolean mUseLargestChild = this.mUseLargestChild;
        boolean b = false;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int max = Integer.MIN_VALUE;
        boolean b2 = false;
        int i = 0;
        int n7 = 1;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                n3 += layoutParams.weight;
                if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    final int mTotalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength, layoutParams.topMargin + mTotalLength + layoutParams.bottomMargin);
                    b2 = true;
                }
                else {
                    int height;
                    if (layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                        layoutParams.height = -2;
                        height = 0;
                    }
                    else {
                        height = Integer.MIN_VALUE;
                    }
                    int mTotalLength2;
                    if (n3 == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    final LayoutParams layoutParams2 = layoutParams;
                    this.measureChildBeforeLayout(virtualChild, i, n, 0, n2, mTotalLength2);
                    if (height != Integer.MIN_VALUE) {
                        layoutParams2.height = height;
                    }
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    final int mTotalLength3 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredHeight + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset(virtualChild));
                    if (mUseLargestChild) {
                        max = Math.max(measuredHeight, max);
                    }
                }
                final int n8 = n6;
                if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex == i + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i < mBaselineAlignedChildIndex && layoutParams.weight > 0.0f) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                boolean b3 = false;
                if (mode != 1073741824 && layoutParams.width == -1) {
                    b = true;
                    b3 = true;
                }
                int n9 = layoutParams.leftMargin + layoutParams.rightMargin;
                final int n10 = virtualChild.getMeasuredWidth() + n9;
                final int max2 = Math.max(n5, n10);
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, virtualChild.getMeasuredState());
                final boolean b4 = n7 != 0 && layoutParams.width == -1;
                int max3;
                int max4;
                if (layoutParams.weight > 0.0f) {
                    if (!b3) {
                        n9 = n10;
                    }
                    max3 = Math.max(n8, n9);
                    max4 = n4;
                }
                else {
                    if (!b3) {
                        n9 = n10;
                    }
                    max4 = Math.max(n4, n9);
                    max3 = n8;
                }
                final boolean b5 = b4;
                i += this.getChildrenSkipCount(virtualChild, i);
                n6 = max3;
                final int n11 = max4;
                n5 = max2;
                n7 = (b5 ? 1 : 0);
                n4 = n11;
            }
            ++i;
        }
        final int n12 = max;
        int n13 = n5;
        final int n14 = n6;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        final int n15 = virtualChildCount;
        if (mUseLargestChild) {
            final int n16 = mode2;
            if (n16 == Integer.MIN_VALUE || n16 == 0) {
                this.mTotalLength = 0;
                for (int j = 0; j < n15; ++j) {
                    final View virtualChild2 = this.getVirtualChildAt(j);
                    if (virtualChild2 == null) {
                        this.mTotalLength += this.measureNullChild(j);
                    }
                    else if (virtualChild2.getVisibility() == 8) {
                        j += this.getChildrenSkipCount(virtualChild2, j);
                    }
                    else {
                        final LayoutParams layoutParams3 = (LayoutParams)virtualChild2.getLayoutParams();
                        final int mTotalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + n12 + layoutParams3.topMargin + layoutParams3.bottomMargin + this.getNextLocationOffset(virtualChild2));
                    }
                }
            }
        }
        final int n17 = mode2;
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), n2, 0);
        final int n18 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int n20;
        if (!b2 && (n18 == 0 || n3 <= 0.0f)) {
            final int max5 = Math.max(n4, n14);
            int n19;
            if (mUseLargestChild) {
                if (n17 != 1073741824) {
                    for (int k = 0; k < n15; ++k) {
                        final View virtualChild3 = this.getVirtualChildAt(k);
                        if (virtualChild3 != null) {
                            if (virtualChild3.getVisibility() != 8) {
                                if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                    virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(n12, 1073741824));
                                }
                            }
                        }
                    }
                    n19 = max5;
                }
                else {
                    n19 = max5;
                }
            }
            else {
                n19 = max5;
            }
            n20 = n19;
        }
        else {
            final float mWeightSum = this.mWeightSum;
            if (mWeightSum > 0.0f) {
                n3 = mWeightSum;
            }
            this.mTotalLength = 0;
            int l = 0;
            int n21 = n4;
            final int n22 = n18;
            final int n23 = n5;
            int combineMeasuredStates2 = combineMeasuredStates;
            final int n24 = n17;
            int n25 = n22;
            int n26 = n23;
            while (l < n15) {
                final View virtualChild4 = this.getVirtualChildAt(l);
                if (virtualChild4.getVisibility() != 8) {
                    final LayoutParams layoutParams4 = (LayoutParams)virtualChild4.getLayoutParams();
                    final float weight = layoutParams4.weight;
                    if (weight > 0.0f) {
                        final int n27 = (int)(n25 * weight / n3);
                        final int paddingLeft = this.getPaddingLeft();
                        final int paddingRight = this.getPaddingRight();
                        n3 -= weight;
                        final int leftMargin = layoutParams4.leftMargin;
                        final int rightMargin = layoutParams4.rightMargin;
                        final int n28 = n25 - n27;
                        final int childMeasureSpec = getChildMeasureSpec(n, paddingLeft + paddingRight + leftMargin + rightMargin, layoutParams4.width);
                        if (layoutParams4.height == 0 && n24 == 1073741824) {
                            int n29;
                            if (n27 > 0) {
                                n29 = n27;
                            }
                            else {
                                n29 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n29, 1073741824));
                        }
                        else {
                            int n30 = virtualChild4.getMeasuredHeight() + n27;
                            if (n30 < 0) {
                                n30 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n30, 1073741824));
                        }
                        combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates2, virtualChild4.getMeasuredState() & 0xFFFFFF00);
                        n25 = n28;
                    }
                    final int n31 = layoutParams4.leftMargin + layoutParams4.rightMargin;
                    final int n32 = virtualChild4.getMeasuredWidth() + n31;
                    final int max6 = Math.max(n26, n32);
                    int n33;
                    if (mode != 1073741824 && layoutParams4.width == -1) {
                        n33 = n31;
                    }
                    else {
                        n33 = n32;
                    }
                    final int max7 = Math.max(n21, n33);
                    final boolean b6 = n7 != 0 && layoutParams4.width == -1;
                    final int mTotalLength5 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength5, mTotalLength5 + virtualChild4.getMeasuredHeight() + layoutParams4.topMargin + layoutParams4.bottomMargin + this.getNextLocationOffset(virtualChild4));
                    n26 = max6;
                    n7 = (b6 ? 1 : 0);
                    n21 = max7;
                }
                ++l;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            final int n34 = n26;
            n20 = n21;
            n13 = n34;
            combineMeasuredStates = combineMeasuredStates2;
        }
        if (n7 == 0 && mode != 1073741824) {
            n13 = n20;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(n13 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, combineMeasuredStates), resolveSizeAndState);
        if (b) {
            this.forceUniformWidth(n15, n2);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawDividersVertical(canvas);
            return;
        }
        this.drawDividersHorizontal(canvas);
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
            return;
        }
        this.layoutHorizontal(n, n2, n3, n4);
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
            return;
        }
        this.measureHorizontal(n, n2);
    }
    
    public void setBaselineAligned(final boolean mBaselineAligned) {
        this.mBaselineAligned = mBaselineAligned;
    }
    
    public void setBaselineAlignedChildIndex(final int mBaselineAlignedChildIndex) {
        if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex < this.getChildCount()) {
            this.mBaselineAlignedChildIndex = mBaselineAlignedChildIndex;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("base aligned child index out of range (0, ");
        sb.append(this.getChildCount());
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setDividerDrawable(final Drawable mDivider) {
        if (mDivider == this.mDivider) {
            return;
        }
        this.mDivider = mDivider;
        boolean willNotDraw = false;
        if (mDivider != null) {
            this.mDividerWidth = mDivider.getIntrinsicWidth();
            this.mDividerHeight = mDivider.getIntrinsicHeight();
        }
        else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (mDivider == null) {
            willNotDraw = true;
        }
        this.setWillNotDraw(willNotDraw);
        this.requestLayout();
    }
    
    public void setDividerPadding(final int mDividerPadding) {
        this.mDividerPadding = mDividerPadding;
    }
    
    public void setGravity(int mGravity) {
        if (this.mGravity != mGravity) {
            if ((0x800007 & mGravity) == 0x0) {
                mGravity |= 0x800003;
            }
            if ((mGravity & 0x70) == 0x0) {
                mGravity |= 0x30;
            }
            this.mGravity = mGravity;
            this.requestLayout();
        }
    }
    
    public void setHorizontalGravity(int n) {
        n &= 0x800007;
        final int mGravity = this.mGravity;
        if ((0x800007 & mGravity) != n) {
            this.mGravity = ((0xFF7FFFF8 & mGravity) | n);
            this.requestLayout();
        }
    }
    
    public void setMeasureWithLargestChildEnabled(final boolean mUseLargestChild) {
        this.mUseLargestChild = mUseLargestChild;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            this.requestLayout();
        }
    }
    
    public void setShowDividers(final int mShowDividers) {
        if (mShowDividers != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = mShowDividers;
    }
    
    public void setVerticalGravity(int n) {
        n &= 0x70;
        final int mGravity = this.mGravity;
        if ((mGravity & 0x70) != n) {
            this.mGravity = ((mGravity & 0xFFFFFF8F) | n);
            this.requestLayout();
        }
    }
    
    public void setWeightSum(final float n) {
        this.mWeightSum = Math.max(0.0f, n);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface DividerMode {
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        public float weight;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = -1;
            this.weight = 0.0f;
        }
        
        public LayoutParams(final int n, final int n2, final float weight) {
            super(n, n2);
            this.gravity = -1;
            this.weight = weight;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = -1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = -1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.gravity = -1;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface OrientationMode {
    }
}
