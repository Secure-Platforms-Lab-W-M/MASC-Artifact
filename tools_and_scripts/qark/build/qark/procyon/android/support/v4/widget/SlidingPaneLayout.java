// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.content.res.TypedArray;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.os.Parcelable;
import android.util.Log;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.support.annotation.ColorInt;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.view.View;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import android.view.ViewGroup;

public class SlidingPaneLayout extends ViewGroup
{
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables;
    boolean mPreservedOpenState;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;
    
    static {
        if (Build$VERSION.SDK_INT >= 17) {
            IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplJBMR1();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplJB();
            return;
        }
        IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplBase();
    }
    
    public SlidingPaneLayout(final Context context) {
        this(context, null);
    }
    
    public SlidingPaneLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SlidingPaneLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mSliderFadeColor = -858993460;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList<DisableLayerRunnable>();
        final float density = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int)(32.0f * density + 0.5f);
        this.setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility((View)this, 1);
        (this.mDragHelper = ViewDragHelper.create(this, 0.5f, (ViewDragHelper.Callback)new DragHelperCallback())).setMinVelocity(400.0f * density);
    }
    
    private boolean closePane(final View view, final int n) {
        if (!this.mFirstLayout && !this.smoothSlideTo(0.0f, n)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }
    
    private void dimChildView(final View view, final float n, final int n2) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (n > 0.0f && n2 != 0) {
            final int n3 = (int)(((0xFF000000 & n2) >>> 24) * n);
            if (layoutParams.dimPaint == null) {
                layoutParams.dimPaint = new Paint();
            }
            layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(n3 << 24 | (0xFFFFFF & n2), PorterDuff$Mode.SRC_OVER));
            if (view.getLayerType() != 2) {
                view.setLayerType(2, layoutParams.dimPaint);
            }
            this.invalidateChildRegion(view);
        }
        else if (view.getLayerType() != 0) {
            if (layoutParams.dimPaint != null) {
                layoutParams.dimPaint.setColorFilter((ColorFilter)null);
            }
            final DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(view);
            this.mPostedRunnables.add(disableLayerRunnable);
            ViewCompat.postOnAnimation((View)this, disableLayerRunnable);
        }
    }
    
    private boolean openPane(final View view, final int n) {
        return (this.mFirstLayout || this.smoothSlideTo(1.0f, n)) && (this.mPreservedOpenState = true);
    }
    
    private void parallaxOtherViews(final float mParallaxOffset) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
        boolean b = false;
        Label_0057: {
            if (layoutParams.dimWhenOffset) {
                int n;
                if (layoutRtlSupport) {
                    n = layoutParams.rightMargin;
                }
                else {
                    n = layoutParams.leftMargin;
                }
                if (n <= 0) {
                    b = true;
                    break Label_0057;
                }
            }
            b = false;
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child != this.mSlideableView) {
                final float mParallaxOffset2 = this.mParallaxOffset;
                final int mParallaxBy = this.mParallaxBy;
                final int n2 = (int)((1.0f - mParallaxOffset2) * mParallaxBy);
                this.mParallaxOffset = mParallaxOffset;
                int n3 = n2 - (int)((1.0f - mParallaxOffset) * mParallaxBy);
                if (layoutRtlSupport) {
                    n3 = -n3;
                }
                child.offsetLeftAndRight(n3);
                if (b) {
                    float n4;
                    if (layoutRtlSupport) {
                        n4 = this.mParallaxOffset - 1.0f;
                    }
                    else {
                        n4 = 1.0f - this.mParallaxOffset;
                    }
                    this.dimChildView(child, n4, this.mCoveredFadeColor);
                }
            }
        }
    }
    
    private static boolean viewIsOpaque(final View view) {
        if (view.isOpaque()) {
            return true;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            return false;
        }
        final Drawable background = view.getBackground();
        return background != null && background.getOpacity() == -1;
    }
    
    protected boolean canScroll(final View view, final boolean b, int n, final int n2, final int n3) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (n2 + scrollX >= child.getLeft() && n2 + scrollX < child.getRight()) {
                    if (n3 + scrollY >= child.getTop() && n3 + scrollY < child.getBottom()) {
                        if (this.canScroll(child, true, n, n2 + scrollX - child.getLeft(), n3 + scrollY - child.getTop())) {
                            return true;
                        }
                    }
                }
            }
        }
        if (b) {
            if (!this.isLayoutRtlSupport()) {
                n = -n;
            }
            if (view.canScrollHorizontally(n)) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public boolean closePane() {
        return this.closePane(this.mSlideableView, 0);
    }
    
    public void computeScroll() {
        if (!this.mDragHelper.continueSettling(true)) {
            return;
        }
        if (!this.mCanSlide) {
            this.mDragHelper.abort();
            return;
        }
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    void dispatchOnPanelClosed(final View view) {
        final PanelSlideListener mPanelSlideListener = this.mPanelSlideListener;
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelClosed(view);
        }
        this.sendAccessibilityEvent(32);
    }
    
    void dispatchOnPanelOpened(final View view) {
        final PanelSlideListener mPanelSlideListener = this.mPanelSlideListener;
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelOpened(view);
        }
        this.sendAccessibilityEvent(32);
    }
    
    void dispatchOnPanelSlide(final View view) {
        final PanelSlideListener mPanelSlideListener = this.mPanelSlideListener;
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(view, this.mSlideOffset);
        }
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        Drawable drawable;
        if (this.isLayoutRtlSupport()) {
            drawable = this.mShadowDrawableRight;
        }
        else {
            drawable = this.mShadowDrawableLeft;
        }
        View child;
        if (this.getChildCount() > 1) {
            child = this.getChildAt(1);
        }
        else {
            child = null;
        }
        if (child == null) {
            return;
        }
        if (drawable == null) {
            return;
        }
        final int top = child.getTop();
        final int bottom = child.getBottom();
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        int right;
        int left;
        if (this.isLayoutRtlSupport()) {
            right = child.getRight();
            left = right + intrinsicWidth;
        }
        else {
            left = child.getLeft();
            right = left - intrinsicWidth;
        }
        drawable.setBounds(right, top, left, bottom);
        drawable.draw(canvas);
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int save = canvas.save(2);
        if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (this.isLayoutRtlSupport()) {
                final Rect mTmpRect = this.mTmpRect;
                mTmpRect.left = Math.max(mTmpRect.left, this.mSlideableView.getRight());
            }
            else {
                final Rect mTmpRect2 = this.mTmpRect;
                mTmpRect2.right = Math.min(mTmpRect2.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        canvas.restoreToCount(save);
        return drawChild;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return (ViewGroup$LayoutParams)new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return (ViewGroup$LayoutParams)new LayoutParams(viewGroup$LayoutParams);
    }
    
    @ColorInt
    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }
    
    public int getParallaxDistance() {
        return this.mParallaxBy;
    }
    
    @ColorInt
    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }
    
    void invalidateChildRegion(final View view) {
        SlidingPaneLayout.IMPL.invalidateChildRegion(this, view);
    }
    
    boolean isDimmed(final View view) {
        final boolean b = false;
        if (view == null) {
            return false;
        }
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        boolean b2 = b;
        if (this.mCanSlide) {
            b2 = b;
            if (layoutParams.dimWhenOffset) {
                b2 = b;
                if (this.mSlideOffset > 0.0f) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection((View)this) == 1;
    }
    
    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 1.0f;
    }
    
    public boolean isSlideable() {
        return this.mCanSlide;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        for (int i = 0; i < this.mPostedRunnables.size(); ++i) {
            this.mPostedRunnables.get(i).run();
        }
        this.mPostedRunnables.clear();
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final boolean mCanSlide = this.mCanSlide;
        boolean b = true;
        if (!mCanSlide && actionMasked == 0 && this.getChildCount() > 1) {
            final View child = this.getChildAt(1);
            if (child != null) {
                this.mPreservedOpenState = (this.mDragHelper.isViewUnder(child, (int)motionEvent.getX(), (int)motionEvent.getY()) ^ true);
            }
        }
        if (!this.mCanSlide || (this.mIsUnableToDrag && actionMasked != 0)) {
            this.mDragHelper.cancel();
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (actionMasked != 3 && actionMasked != 1) {
            boolean b2 = false;
            if (actionMasked != 0) {
                if (actionMasked == 2) {
                    final float x = motionEvent.getX();
                    final float y = motionEvent.getY();
                    final float abs = Math.abs(x - this.mInitialMotionX);
                    final float abs2 = Math.abs(y - this.mInitialMotionY);
                    if (abs > this.mDragHelper.getTouchSlop() && abs2 > abs) {
                        this.mDragHelper.cancel();
                        this.mIsUnableToDrag = true;
                        return false;
                    }
                }
            }
            else {
                this.mIsUnableToDrag = false;
                final float x2 = motionEvent.getX();
                final float y2 = motionEvent.getY();
                this.mInitialMotionX = x2;
                this.mInitialMotionY = y2;
                if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)x2, (int)y2)) {
                    if (this.isDimmed(this.mSlideableView)) {
                        b2 = true;
                    }
                }
            }
            if (!this.mDragHelper.shouldInterceptTouchEvent(motionEvent)) {
                if (b2) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        this.mDragHelper.cancel();
        return false;
    }
    
    protected void onLayout(final boolean b, int i, int n, int n2, int n3) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        if (layoutRtlSupport) {
            this.mDragHelper.setEdgeTrackingEnabled(2);
        }
        else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        final int n4 = n2 - i;
        if (layoutRtlSupport) {
            n = this.getPaddingRight();
        }
        else {
            n = this.getPaddingLeft();
        }
        if (layoutRtlSupport) {
            n3 = this.getPaddingLeft();
        }
        else {
            n3 = this.getPaddingRight();
        }
        final int paddingTop = this.getPaddingTop();
        final int childCount = this.getChildCount();
        n2 = (i = n);
        if (this.mFirstLayout) {
            float mSlideOffset;
            if (this.mCanSlide && this.mPreservedOpenState) {
                mSlideOffset = 1.0f;
            }
            else {
                mSlideOffset = 0.0f;
            }
            this.mSlideOffset = mSlideOffset;
        }
        int j = 0;
        int n5 = n2;
        n2 = n;
        while (j < childCount) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() == 8) {
                n = n5;
            }
            else {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final int measuredWidth = child.getMeasuredWidth();
                final int n6 = 0;
                int n8 = 0;
                Label_0374: {
                    if (layoutParams.slideable) {
                        n = layoutParams.leftMargin;
                        final int mSlideRange = Math.min(i, n4 - n3 - this.mOverhangSize) - n5 - (n + layoutParams.rightMargin);
                        this.mSlideRange = mSlideRange;
                        if (layoutRtlSupport) {
                            n = layoutParams.rightMargin;
                        }
                        else {
                            n = layoutParams.leftMargin;
                        }
                        layoutParams.dimWhenOffset = (n5 + n + mSlideRange + measuredWidth / 2 > n4 - n3);
                        final int n7 = (int)(mSlideRange * this.mSlideOffset);
                        n = n5 + (n7 + n);
                        this.mSlideOffset = n7 / (float)this.mSlideRange;
                        n8 = n6;
                    }
                    else {
                        if (this.mCanSlide) {
                            n = this.mParallaxBy;
                            if (n != 0) {
                                n8 = (int)((1.0f - this.mSlideOffset) * n);
                                n = i;
                                break Label_0374;
                            }
                        }
                        n = i;
                        n8 = n6;
                    }
                }
                int n9;
                int n10;
                if (layoutRtlSupport) {
                    n9 = n4 - n + n8;
                    n10 = n9 - measuredWidth;
                }
                else {
                    n10 = n - n8;
                    n9 = n10 + measuredWidth;
                }
                child.layout(n10, paddingTop, n9, child.getMeasuredHeight() + paddingTop);
                i += child.getWidth();
            }
            ++j;
            n5 = n;
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    this.parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            }
            else {
                for (i = 0; i < childCount; ++i) {
                    this.dimChildView(this.getChildAt(i), 0.0f, this.mSliderFadeColor);
                }
            }
            this.updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int n, int n2) {
        final int mode = View$MeasureSpec.getMode(n);
        int size = View$MeasureSpec.getSize(n);
        int mode2 = View$MeasureSpec.getMode(n2);
        int size2 = View$MeasureSpec.getSize(n2);
        if (mode != 1073741824) {
            if (!this.isInEditMode()) {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
            if (mode == Integer.MIN_VALUE) {
                n2 = 1073741824;
            }
            else if (mode == 0) {
                n2 = 1073741824;
                size = 300;
            }
            else {
                n2 = mode;
            }
        }
        else if (mode2 == 0) {
            if (!this.isInEditMode()) {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            }
            if (mode2 == 0) {
                mode2 = Integer.MIN_VALUE;
                size2 = 300;
                n2 = mode;
            }
            else {
                n2 = mode;
            }
        }
        else {
            n2 = mode;
        }
        int min = 0;
        n = 0;
        if (mode2 != Integer.MIN_VALUE) {
            if (mode2 == 1073741824) {
                min = (n = size2 - this.getPaddingTop() - this.getPaddingBottom());
            }
        }
        else {
            n = size2 - this.getPaddingTop() - this.getPaddingBottom();
        }
        float n3 = 0.0f;
        boolean mCanSlide = false;
        int n5;
        final int n4 = n5 = size - this.getPaddingLeft() - this.getPaddingRight();
        final int childCount = this.getChildCount();
        if (childCount > 2) {
            Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (child.getVisibility() == 8) {
                layoutParams.dimWhenOffset = false;
            }
            else {
                if (layoutParams.weight > 0.0f) {
                    n3 += layoutParams.weight;
                    if (layoutParams.width == 0) {
                        continue;
                    }
                }
                n2 = layoutParams.leftMargin + layoutParams.rightMargin;
                if (layoutParams.width == -2) {
                    n2 = View$MeasureSpec.makeMeasureSpec(n4 - n2, Integer.MIN_VALUE);
                }
                else if (layoutParams.width == -1) {
                    n2 = View$MeasureSpec.makeMeasureSpec(n4 - n2, 1073741824);
                }
                else {
                    n2 = View$MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                }
                int n6;
                if (layoutParams.height == -2) {
                    n6 = View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE);
                }
                else if (layoutParams.height == -1) {
                    n6 = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
                }
                else {
                    n6 = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                }
                child.measure(n2, n6);
                n2 = child.getMeasuredWidth();
                final int measuredHeight = child.getMeasuredHeight();
                if (mode2 == Integer.MIN_VALUE && measuredHeight > min) {
                    min = Math.min(measuredHeight, n);
                }
                n5 -= n2;
                final boolean slideable = n5 < 0;
                layoutParams.slideable = slideable;
                if (layoutParams.slideable) {
                    this.mSlideableView = child;
                }
                mCanSlide |= slideable;
            }
        }
        if (mCanSlide || n3 > 0.0f) {
            final int n7 = n4 - this.mOverhangSize;
            int j = 0;
            final int n8 = childCount;
            n2 = n;
            final int n9 = n7;
            while (j < n8) {
                final View child2 = this.getChildAt(j);
                if (child2.getVisibility() != 8) {
                    final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                    if (child2.getVisibility() != 8) {
                        if (layoutParams2.width == 0 && layoutParams2.weight > 0.0f) {
                            n = 1;
                        }
                        else {
                            n = 0;
                        }
                        int measuredWidth;
                        if (n != 0) {
                            measuredWidth = 0;
                        }
                        else {
                            measuredWidth = child2.getMeasuredWidth();
                        }
                        if (mCanSlide && child2 != this.mSlideableView) {
                            if (layoutParams2.width < 0) {
                                if (measuredWidth > n9 || layoutParams2.weight > 0.0f) {
                                    if (n != 0) {
                                        if (layoutParams2.height == -2) {
                                            n = View$MeasureSpec.makeMeasureSpec(n2, Integer.MIN_VALUE);
                                        }
                                        else if (layoutParams2.height == -1) {
                                            n = View$MeasureSpec.makeMeasureSpec(n2, 1073741824);
                                        }
                                        else {
                                            n = View$MeasureSpec.makeMeasureSpec(layoutParams2.height, 1073741824);
                                        }
                                    }
                                    else {
                                        n = View$MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                                    }
                                    child2.measure(View$MeasureSpec.makeMeasureSpec(n9, 1073741824), n);
                                }
                            }
                        }
                        else if (layoutParams2.weight > 0.0f) {
                            if (layoutParams2.width == 0) {
                                if (layoutParams2.height == -2) {
                                    n = View$MeasureSpec.makeMeasureSpec(n2, Integer.MIN_VALUE);
                                }
                                else if (layoutParams2.height == -1) {
                                    n = View$MeasureSpec.makeMeasureSpec(n2, 1073741824);
                                }
                                else {
                                    n = View$MeasureSpec.makeMeasureSpec(layoutParams2.height, 1073741824);
                                }
                            }
                            else {
                                n = View$MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                            }
                            if (mCanSlide) {
                                final int n10 = n4 - (layoutParams2.leftMargin + layoutParams2.rightMargin);
                                final int measureSpec = View$MeasureSpec.makeMeasureSpec(n10, 1073741824);
                                if (measuredWidth != n10) {
                                    child2.measure(measureSpec, n);
                                }
                            }
                            else {
                                child2.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth + (int)(layoutParams2.weight * Math.max(0, n5) / n3), 1073741824), n);
                            }
                        }
                    }
                }
                ++j;
            }
        }
        this.setMeasuredDimension(size, this.getPaddingTop() + min + this.getPaddingBottom());
        this.mCanSlide = mCanSlide;
        if (this.mDragHelper.getViewDragState() != 0 && !mCanSlide) {
            this.mDragHelper.abort();
        }
    }
    
    void onPanelDragged(int n) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
        final int width = this.mSlideableView.getWidth();
        if (layoutRtlSupport) {
            n = this.getWidth() - n - width;
        }
        int n2;
        if (layoutRtlSupport) {
            n2 = this.getPaddingRight();
        }
        else {
            n2 = this.getPaddingLeft();
        }
        int n3;
        if (layoutRtlSupport) {
            n3 = layoutParams.rightMargin;
        }
        else {
            n3 = layoutParams.leftMargin;
        }
        this.mSlideOffset = (n - (n2 + n3)) / (float)this.mSlideRange;
        if (this.mParallaxBy != 0) {
            this.parallaxOtherViews(this.mSlideOffset);
        }
        if (layoutParams.dimWhenOffset) {
            this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        this.dispatchOnPanelSlide(this.mSlideableView);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.isOpen) {
            this.openPane();
        }
        else {
            this.closePane();
        }
        this.mPreservedOpenState = savedState.isOpen;
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        boolean isOpen;
        if (this.isSlideable()) {
            isOpen = this.isOpen();
        }
        else {
            isOpen = this.mPreservedOpenState;
        }
        savedState.isOpen = isOpen;
        return (Parcelable)savedState;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.mFirstLayout = true;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(motionEvent);
        }
        this.mDragHelper.processTouchEvent(motionEvent);
        switch (motionEvent.getActionMasked()) {
            default: {
                return true;
            }
            case 1: {
                if (!this.isDimmed(this.mSlideableView)) {
                    return true;
                }
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                final float n = x - this.mInitialMotionX;
                final float n2 = y - this.mInitialMotionY;
                final int touchSlop = this.mDragHelper.getTouchSlop();
                if (n * n + n2 * n2 >= touchSlop * touchSlop) {
                    return true;
                }
                if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)x, (int)y)) {
                    this.closePane(this.mSlideableView, 0);
                    return true;
                }
                return true;
            }
            case 0: {
                final float x2 = motionEvent.getX();
                final float y2 = motionEvent.getY();
                this.mInitialMotionX = x2;
                this.mInitialMotionY = y2;
                return true;
            }
        }
    }
    
    public boolean openPane() {
        return this.openPane(this.mSlideableView, 0);
    }
    
    public void requestChildFocus(final View view, final View view2) {
        super.requestChildFocus(view, view2);
        if (!this.isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = (view == this.mSlideableView);
        }
    }
    
    void setAllChildrenVisible() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 4) {
                child.setVisibility(0);
            }
        }
    }
    
    public void setCoveredFadeColor(@ColorInt final int mCoveredFadeColor) {
        this.mCoveredFadeColor = mCoveredFadeColor;
    }
    
    public void setPanelSlideListener(final PanelSlideListener mPanelSlideListener) {
        this.mPanelSlideListener = mPanelSlideListener;
    }
    
    public void setParallaxDistance(final int mParallaxBy) {
        this.mParallaxBy = mParallaxBy;
        this.requestLayout();
    }
    
    @Deprecated
    public void setShadowDrawable(final Drawable shadowDrawableLeft) {
        this.setShadowDrawableLeft(shadowDrawableLeft);
    }
    
    public void setShadowDrawableLeft(final Drawable mShadowDrawableLeft) {
        this.mShadowDrawableLeft = mShadowDrawableLeft;
    }
    
    public void setShadowDrawableRight(final Drawable mShadowDrawableRight) {
        this.mShadowDrawableRight = mShadowDrawableRight;
    }
    
    @Deprecated
    public void setShadowResource(@DrawableRes final int n) {
        this.setShadowDrawable(this.getResources().getDrawable(n));
    }
    
    public void setShadowResourceLeft(final int n) {
        this.setShadowDrawableLeft(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setShadowResourceRight(final int n) {
        this.setShadowDrawableRight(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setSliderFadeColor(@ColorInt final int mSliderFadeColor) {
        this.mSliderFadeColor = mSliderFadeColor;
    }
    
    @Deprecated
    public void smoothSlideClosed() {
        this.closePane();
    }
    
    @Deprecated
    public void smoothSlideOpen() {
        this.openPane();
    }
    
    boolean smoothSlideTo(final float n, int paddingRight) {
        if (!this.mCanSlide) {
            return false;
        }
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
        if (layoutRtlSupport) {
            paddingRight = this.getPaddingRight();
            paddingRight = (int)(this.getWidth() - (paddingRight + layoutParams.rightMargin + this.mSlideRange * n + this.mSlideableView.getWidth()));
        }
        else {
            paddingRight = (int)(this.getPaddingLeft() + layoutParams.leftMargin + this.mSlideRange * n);
        }
        final ViewDragHelper mDragHelper = this.mDragHelper;
        final View mSlideableView = this.mSlideableView;
        if (mDragHelper.smoothSlideViewTo(mSlideableView, paddingRight, mSlideableView.getTop())) {
            this.setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation((View)this);
            return true;
        }
        return false;
    }
    
    void updateObscuredViewsVisibility(final View view) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        int paddingLeft;
        if (layoutRtlSupport) {
            paddingLeft = this.getWidth() - this.getPaddingRight();
        }
        else {
            paddingLeft = this.getPaddingLeft();
        }
        int paddingLeft2;
        if (layoutRtlSupport) {
            paddingLeft2 = this.getPaddingLeft();
        }
        else {
            paddingLeft2 = this.getWidth() - this.getPaddingRight();
        }
        final int paddingTop = this.getPaddingTop();
        final int height = this.getHeight();
        final int paddingBottom = this.getPaddingBottom();
        int left;
        int right;
        int top;
        int bottom;
        if (view != null && viewIsOpaque(view)) {
            left = view.getLeft();
            right = view.getRight();
            top = view.getTop();
            bottom = view.getBottom();
        }
        else {
            left = 0;
            bottom = 0;
            top = 0;
            right = 0;
        }
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child == view) {
                return;
            }
            if (child.getVisibility() != 8) {
                int n;
                if (layoutRtlSupport) {
                    n = paddingLeft2;
                }
                else {
                    n = paddingLeft;
                }
                final int max = Math.max(n, child.getLeft());
                final int max2 = Math.max(paddingTop, child.getTop());
                int n2;
                if (layoutRtlSupport) {
                    n2 = paddingLeft;
                }
                else {
                    n2 = paddingLeft2;
                }
                final int min = Math.min(n2, child.getRight());
                final int min2 = Math.min(height - paddingBottom, child.getBottom());
                int visibility;
                if (max >= left && max2 >= top && min <= right && min2 <= bottom) {
                    visibility = 4;
                }
                else {
                    visibility = 0;
                }
                child.setVisibility(visibility);
            }
        }
    }
    
    class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private final Rect mTmpRect;
        
        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }
        
        private void copyNodeInfoNoChildren(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            final Rect mTmpRect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
            accessibilityNodeInfoCompat.setMovementGranularities(accessibilityNodeInfoCompat2.getMovementGranularities());
        }
        
        public boolean filter(final View view) {
            return SlidingPaneLayout.this.isDimmed(view);
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)SlidingPaneLayout.class.getName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(View child, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(child, obtain);
            this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, obtain);
            obtain.recycle();
            accessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(child);
            final ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(child);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat.setParent((View)parentForAccessibility);
            }
            for (int childCount = SlidingPaneLayout.this.getChildCount(), i = 0; i < childCount; ++i) {
                child = SlidingPaneLayout.this.getChildAt(i);
                if (!this.filter(child) && child.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(child, 1);
                    accessibilityNodeInfoCompat.addChild(child);
                }
            }
        }
        
        @Override
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return !this.filter(view) && super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }
    
    private class DisableLayerRunnable implements Runnable
    {
        final View mChildView;
        
        DisableLayerRunnable(final View mChildView) {
            this.mChildView = mChildView;
        }
        
        @Override
        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                this.mChildView.setLayerType(0, (Paint)null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }
    
    private class DragHelperCallback extends Callback
    {
        DragHelperCallback() {
        }
        
        @Override
        public int clampViewPositionHorizontal(final View view, final int n, int n2) {
            final LayoutParams layoutParams = (LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                n2 = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
                return Math.max(Math.min(n, n2), n2 - SlidingPaneLayout.this.mSlideRange);
            }
            n2 = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
            return Math.min(Math.max(n, n2), SlidingPaneLayout.this.mSlideRange + n2);
        }
        
        @Override
        public int clampViewPositionVertical(final View view, final int n, final int n2) {
            return view.getTop();
        }
        
        @Override
        public int getViewHorizontalDragRange(final View view) {
            return SlidingPaneLayout.this.mSlideRange;
        }
        
        @Override
        public void onEdgeDragStarted(final int n, final int n2) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, n2);
        }
        
        @Override
        public void onViewCaptured(final View view, final int n) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }
        
        @Override
        public void onViewDragStateChanged(final int n) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() != 0) {
                return;
            }
            if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                final SlidingPaneLayout this$0 = SlidingPaneLayout.this;
                this$0.updateObscuredViewsVisibility(this$0.mSlideableView);
                final SlidingPaneLayout this$2 = SlidingPaneLayout.this;
                this$2.dispatchOnPanelClosed(this$2.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = false;
                return;
            }
            final SlidingPaneLayout this$3 = SlidingPaneLayout.this;
            this$3.dispatchOnPanelOpened(this$3.mSlideableView);
            SlidingPaneLayout.this.mPreservedOpenState = true;
        }
        
        @Override
        public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
            SlidingPaneLayout.this.onPanelDragged(n);
            SlidingPaneLayout.this.invalidate();
        }
        
        @Override
        public void onViewReleased(final View view, final float n, final float n2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n4;
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int n3 = SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin;
                if (n < 0.0f || (n == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    n3 += SlidingPaneLayout.this.mSlideRange;
                }
                n4 = SlidingPaneLayout.this.getWidth() - n3 - SlidingPaneLayout.this.mSlideableView.getWidth();
            }
            else {
                n4 = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
                if (n > 0.0f || (n == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    n4 += SlidingPaneLayout.this.mSlideRange;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(n4, view.getTop());
            SlidingPaneLayout.this.invalidate();
        }
        
        @Override
        public boolean tryCaptureView(final View view, final int n) {
            return !SlidingPaneLayout.this.mIsUnableToDrag && ((LayoutParams)view.getLayoutParams()).slideable;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        private static final int[] ATTRS;
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;
        
        static {
            ATTRS = new int[] { 16843137 };
        }
        
        public LayoutParams() {
            super(-1, -1);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.weight = 0.0f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, LayoutParams.ATTRS);
            this.weight = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.weight = 0.0f;
            this.weight = layoutParams.weight;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.weight = 0.0f;
        }
    }
    
    public interface PanelSlideListener
    {
        void onPanelClosed(final View p0);
        
        void onPanelOpened(final View p0);
        
        void onPanelSlide(final View p0, final float p1);
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean isOpen;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isOpen = (parcel.readInt() != 0);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        }
    }
    
    public static class SimplePanelSlideListener implements PanelSlideListener
    {
        @Override
        public void onPanelClosed(final View view) {
        }
        
        @Override
        public void onPanelOpened(final View view) {
        }
        
        @Override
        public void onPanelSlide(final View view, final float n) {
        }
    }
    
    interface SlidingPanelLayoutImpl
    {
        void invalidateChildRegion(final SlidingPaneLayout p0, final View p1);
    }
    
    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl
    {
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            ViewCompat.postInvalidateOnAnimation((View)slidingPaneLayout, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }
    
    @RequiresApi(16)
    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase
    {
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;
        
        SlidingPanelLayoutImplJB() {
            try {
                this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class<?>[])null);
            }
            catch (NoSuchMethodException ex) {
                Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", (Throwable)ex);
            }
            try {
                (this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList")).setAccessible(true);
            }
            catch (NoSuchFieldException ex2) {
                Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", (Throwable)ex2);
            }
        }
        
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            if (this.mGetDisplayList != null) {
                final Field mRecreateDisplayList = this.mRecreateDisplayList;
                if (mRecreateDisplayList != null) {
                    try {
                        mRecreateDisplayList.setBoolean(view, true);
                        this.mGetDisplayList.invoke(view, (Object[])null);
                    }
                    catch (Exception ex) {
                        Log.e("SlidingPaneLayout", "Error refreshing display list state", (Throwable)ex);
                    }
                    super.invalidateChildRegion(slidingPaneLayout, view);
                    return;
                }
            }
            view.invalidate();
        }
    }
    
    @RequiresApi(17)
    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase
    {
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            ViewCompat.setLayerPaint(view, ((LayoutParams)view.getLayoutParams()).dimPaint);
        }
    }
}
