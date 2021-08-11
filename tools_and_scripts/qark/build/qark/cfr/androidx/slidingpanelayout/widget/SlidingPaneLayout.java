/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 */
package androidx.slidingpanelayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout
extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private boolean mDisplayListReflectionLoaded;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout = true;
    private Method mGetDisplayList;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList();
    boolean mPreservedOpenState;
    private Field mRecreateDisplayList;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor = -858993460;
    private final Rect mTmpRect = new Rect();

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingPaneLayout(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        float f = object.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int)(32.0f * f + 0.5f);
        this.setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility((View)this, 1);
        this.mDragHelper = object = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        object.setMinVelocity(400.0f * f);
    }

    private boolean closePane(View view, int n) {
        if (!this.mFirstLayout && !this.smoothSlideTo(0.0f, n)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private void dimChildView(View object, float f, int n) {
        LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
        if (f > 0.0f && n != 0) {
            int n2 = (int)((float)((-16777216 & n) >>> 24) * f);
            if (layoutParams.dimPaint == null) {
                layoutParams.dimPaint = new Paint();
            }
            layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2 << 24 | 16777215 & n, PorterDuff.Mode.SRC_OVER));
            if (object.getLayerType() != 2) {
                object.setLayerType(2, layoutParams.dimPaint);
            }
            this.invalidateChildRegion((View)object);
        } else if (object.getLayerType() != 0) {
            if (layoutParams.dimPaint != null) {
                layoutParams.dimPaint.setColorFilter(null);
            }
            object = new DisableLayerRunnable((View)object);
            this.mPostedRunnables.add((DisableLayerRunnable)object);
            ViewCompat.postOnAnimation((View)this, (Runnable)object);
            return;
        }
    }

    private boolean openPane(View view, int n) {
        if (!this.mFirstLayout && !this.smoothSlideTo(1.0f, n)) {
            return false;
        }
        this.mPreservedOpenState = true;
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void parallaxOtherViews(float var1_1) {
        var8_2 = this.isLayoutRtlSupport();
        var9_3 = (LayoutParams)this.mSlideableView.getLayoutParams();
        if (!var9_3.dimWhenOffset) ** GOTO lbl-1000
        var3_4 = var8_2 != false ? var9_3.rightMargin : var9_3.leftMargin;
        if (var3_4 <= 0) {
            var3_4 = 1;
        } else lbl-1000: // 2 sources:
        {
            var3_4 = 0;
        }
        var6_5 = this.getChildCount();
        var4_6 = 0;
        while (var4_6 < var6_5) {
            var9_3 = this.getChildAt(var4_6);
            if (var9_3 != this.mSlideableView) {
                var2_7 = this.mParallaxOffset;
                var5_8 = this.mParallaxBy;
                var7_9 = (int)((1.0f - var2_7) * (float)var5_8);
                this.mParallaxOffset = var1_1;
                var5_8 = var7_9 - (int)((1.0f - var1_1) * (float)var5_8);
                if (var8_2) {
                    var5_8 = - var5_8;
                }
                var9_3.offsetLeftAndRight(var5_8);
                if (var3_4 != 0) {
                    var2_7 = this.mParallaxOffset;
                    var2_7 = var8_2 ? (var2_7 -= 1.0f) : 1.0f - var2_7;
                    this.dimChildView((View)var9_3, var2_7, this.mCoveredFadeColor);
                }
            }
            ++var4_6;
        }
    }

    private static boolean viewIsOpaque(View view) {
        if (view.isOpaque()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            return false;
        }
        if ((view = view.getBackground()) != null) {
            if (view.getOpacity() == -1) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected boolean canScroll(View view, boolean bl, int n, int n2, int n3) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int n4 = view.getScrollX();
            int n5 = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                View view2 = viewGroup.getChildAt(i);
                if (n2 + n4 < view2.getLeft() || n2 + n4 >= view2.getRight() || n3 + n5 < view2.getTop() || n3 + n5 >= view2.getBottom() || !this.canScroll(view2, true, n, n2 + n4 - view2.getLeft(), n3 + n5 - view2.getTop())) continue;
                return true;
            }
        }
        if (bl) {
            if (!this.isLayoutRtlSupport()) {
                n = - n;
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

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    public boolean closePane() {
        return this.closePane(this.mSlideableView, 0);
    }

    public void computeScroll() {
        if (this.mDragHelper.continueSettling(true)) {
            if (!this.mCanSlide) {
                this.mDragHelper.abort();
                return;
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    void dispatchOnPanelClosed(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelClosed(view);
        }
        this.sendAccessibilityEvent(32);
    }

    void dispatchOnPanelOpened(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelOpened(view);
        }
        this.sendAccessibilityEvent(32);
    }

    void dispatchOnPanelSlide(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelSlide(view, this.mSlideOffset);
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable2 = this.isLayoutRtlSupport() ? this.mShadowDrawableRight : this.mShadowDrawableLeft;
        View view = this.getChildCount() > 1 ? this.getChildAt(1) : null;
        if (view != null) {
            int n;
            int n2;
            if (drawable2 == null) {
                return;
            }
            int n3 = view.getTop();
            int n4 = view.getBottom();
            int n5 = drawable2.getIntrinsicWidth();
            if (this.isLayoutRtlSupport()) {
                n2 = view.getRight();
                n = n2 + n5;
            } else {
                n = view.getLeft();
                n2 = n - n5;
            }
            drawable2.setBounds(n2, n3, n, n4);
            drawable2.draw(canvas);
            return;
        }
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n = canvas.save();
        if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (this.isLayoutRtlSupport()) {
                layoutParams = this.mTmpRect;
                layoutParams.left = Math.max(layoutParams.left, this.mSlideableView.getRight());
            } else {
                layoutParams = this.mTmpRect;
                layoutParams.right = Math.min(layoutParams.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        boolean bl = super.drawChild(canvas, view, l);
        canvas.restoreToCount(n);
        return bl;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    void invalidateChildRegion(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            ViewCompat.setLayerPaint(view, ((LayoutParams)view.getLayoutParams()).dimPaint);
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            Field field;
            if (!this.mDisplayListReflectionLoaded) {
                try {
                    this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", null);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.e((String)"SlidingPaneLayout", (String)"Couldn't fetch getDisplayList method; dimming won't work right.", (Throwable)noSuchMethodException);
                }
                try {
                    this.mRecreateDisplayList = field = View.class.getDeclaredField("mRecreateDisplayList");
                    field.setAccessible(true);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    Log.e((String)"SlidingPaneLayout", (String)"Couldn't fetch mRecreateDisplayList field; dimming will be slow.", (Throwable)noSuchFieldException);
                }
                this.mDisplayListReflectionLoaded = true;
            }
            if (this.mGetDisplayList != null && (field = this.mRecreateDisplayList) != null) {
                try {
                    field.setBoolean((Object)view, true);
                    this.mGetDisplayList.invoke((Object)view, null);
                }
                catch (Exception exception) {
                    Log.e((String)"SlidingPaneLayout", (String)"Error refreshing display list state", (Throwable)exception);
                }
            } else {
                view.invalidate();
                return;
            }
        }
        ViewCompat.postInvalidateOnAnimation((View)this, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    boolean isDimmed(View object) {
        boolean bl = false;
        if (object == null) {
            return false;
        }
        object = (LayoutParams)object.getLayoutParams();
        boolean bl2 = bl;
        if (this.mCanSlide) {
            bl2 = bl;
            if (object.dimWhenOffset) {
                bl2 = bl;
                if (this.mSlideOffset > 0.0f) {
                    bl2 = true;
                }
            }
        }
        return bl2;
    }

    boolean isLayoutRtlSupport() {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            return true;
        }
        return false;
    }

    public boolean isOpen() {
        if (this.mCanSlide && this.mSlideOffset != 1.0f) {
            return false;
        }
        return true;
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
        int n = this.mPostedRunnables.size();
        for (int i = 0; i < n; ++i) {
            this.mPostedRunnables.get(i).run();
        }
        this.mPostedRunnables.clear();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        View view;
        int n = motionEvent.getActionMasked();
        boolean bl = this.mCanSlide;
        boolean bl2 = true;
        if (!bl && n == 0 && this.getChildCount() > 1 && (view = this.getChildAt(1)) != null) {
            this.mPreservedOpenState = this.mDragHelper.isViewUnder(view, (int)motionEvent.getX(), (int)motionEvent.getY()) ^ true;
        }
        if (this.mCanSlide && (!this.mIsUnableToDrag || n == 0)) {
            if (n != 3 && n != 1) {
                int n2 = 0;
                if (n != 0) {
                    if (n != 2) {
                        n = n2;
                    } else {
                        float f = motionEvent.getX();
                        float f2 = motionEvent.getY();
                        f = Math.abs(f - this.mInitialMotionX);
                        f2 = Math.abs(f2 - this.mInitialMotionY);
                        n = n2;
                        if (f > (float)this.mDragHelper.getTouchSlop()) {
                            n = n2;
                            if (f2 > f) {
                                this.mDragHelper.cancel();
                                this.mIsUnableToDrag = true;
                                return false;
                            }
                        }
                    }
                } else {
                    this.mIsUnableToDrag = false;
                    float f = motionEvent.getX();
                    float f3 = motionEvent.getY();
                    this.mInitialMotionX = f;
                    this.mInitialMotionY = f3;
                    n = n2;
                    if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)f, (int)f3)) {
                        n = n2;
                        if (this.isDimmed(this.mSlideableView)) {
                            n = 1;
                        }
                    }
                }
                if (!this.mDragHelper.shouldInterceptTouchEvent(motionEvent)) {
                    if (n != 0) {
                        return true;
                    }
                    bl2 = false;
                }
                return bl2;
            }
            this.mDragHelper.cancel();
            return false;
        }
        this.mDragHelper.cancel();
        return super.onInterceptTouchEvent(motionEvent);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        boolean bl2 = this.isLayoutRtlSupport();
        if (bl2) {
            this.mDragHelper.setEdgeTrackingEnabled(2);
        } else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        int n5 = n3 - n;
        n2 = bl2 ? this.getPaddingRight() : this.getPaddingLeft();
        n4 = bl2 ? this.getPaddingLeft() : this.getPaddingRight();
        int n6 = this.getPaddingTop();
        int n7 = this.getChildCount();
        n = n3 = n2;
        if (this.mFirstLayout) {
            float f = this.mCanSlide && this.mPreservedOpenState ? 1.0f : 0.0f;
            this.mSlideOffset = f;
        }
        int n8 = n3;
        n3 = n2;
        for (int i = 0; i < n7; ++i) {
            View view = this.getChildAt(i);
            if (view.getVisibility() == 8) {
                n2 = n8;
            } else {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                int n9 = view.getMeasuredWidth();
                int n10 = 0;
                if (layoutParams.slideable) {
                    n2 = layoutParams.leftMargin;
                    int n11 = layoutParams.rightMargin;
                    this.mSlideRange = n11 = Math.min(n, n5 - n4 - this.mOverhangSize) - n8 - (n2 + n11);
                    n2 = bl2 ? layoutParams.rightMargin : layoutParams.leftMargin;
                    bl = n8 + n2 + n11 + n9 / 2 > n5 - n4;
                    layoutParams.dimWhenOffset = bl;
                    n11 = (int)((float)n11 * this.mSlideOffset);
                    n2 = n8 + (n11 + n2);
                    this.mSlideOffset = (float)n11 / (float)this.mSlideRange;
                    n8 = n10;
                } else if (this.mCanSlide && (n2 = this.mParallaxBy) != 0) {
                    n8 = (int)((1.0f - this.mSlideOffset) * (float)n2);
                    n2 = n;
                } else {
                    n2 = n;
                    n8 = n10;
                }
                if (bl2) {
                    n8 = n5 - n2 + n8;
                    n10 = n8 - n9;
                } else {
                    n10 = n2 - n8;
                    n8 = n10 + n9;
                }
                view.layout(n10, n6, n8, view.getMeasuredHeight() + n6);
                n += view.getWidth();
            }
            n8 = n2;
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    this.parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            } else {
                for (n = 0; n < n7; ++n) {
                    this.dimChildView(this.getChildAt(n), 0.0f, this.mSliderFadeColor);
                }
            }
            this.updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void onMeasure(int var1_1, int var2_2) {
        var5_3 = View.MeasureSpec.getMode((int)var1_1);
        var1_1 = View.MeasureSpec.getSize((int)var1_1);
        var9_4 = View.MeasureSpec.getMode((int)var2_2);
        var10_5 = View.MeasureSpec.getSize((int)var2_2);
        if (var5_3 != 1073741824) {
            if (this.isInEditMode() == false) throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            if (var5_3 == Integer.MIN_VALUE) {
                var2_2 = 1073741824;
                var8_6 = var1_1;
                var6_7 = var9_4;
                var7_8 = var10_5;
            } else {
                var2_2 = var5_3;
                var8_6 = var1_1;
                var6_7 = var9_4;
                var7_8 = var10_5;
                if (var5_3 == 0) {
                    var2_2 = 1073741824;
                    var8_6 = 300;
                    var6_7 = var9_4;
                    var7_8 = var10_5;
                }
            }
        } else {
            var2_2 = var5_3;
            var8_6 = var1_1;
            var6_7 = var9_4;
            var7_8 = var10_5;
            if (var9_4 == 0) {
                if (this.isInEditMode() == false) throw new IllegalStateException("Height must not be UNSPECIFIED");
                var2_2 = var5_3;
                var8_6 = var1_1;
                var6_7 = var9_4;
                var7_8 = var10_5;
                if (var9_4 == 0) {
                    var6_7 = Integer.MIN_VALUE;
                    var7_8 = 300;
                    var2_2 = var5_3;
                    var8_6 = var1_1;
                }
            }
        }
        var5_3 = 0;
        var1_1 = 0;
        if (var6_7 != Integer.MIN_VALUE) {
            if (var6_7 == 1073741824) {
                var1_1 = var5_3 = var7_8 - this.getPaddingTop() - this.getPaddingBottom();
            }
        } else {
            var1_1 = var7_8 - this.getPaddingTop() - this.getPaddingBottom();
        }
        var4_9 = 0.0f;
        var16_10 = false;
        var9_4 = var13_11 = var8_6 - this.getPaddingLeft() - this.getPaddingRight();
        var14_12 = this.getChildCount();
        if (var14_12 > 2) {
            Log.e((String)"SlidingPaneLayout", (String)"onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        var11_13 = var7_8;
        var12_14 = var2_2;
        for (var10_5 = 0; var10_5 < var14_12; ++var10_5) {
            block22 : {
                block21 : {
                    var18_18 = this.getChildAt(var10_5);
                    var19_19 = (LayoutParams)var18_18.getLayoutParams();
                    if (var18_18.getVisibility() != 8) break block21;
                    var19_19.dimWhenOffset = false;
                    var2_2 = var5_3;
                    break block22;
                }
                var3_15 = var4_9;
                if (var19_19.weight <= 0.0f) ** GOTO lbl-1000
                var3_15 = var4_9 += var19_19.weight;
                if (var19_19.width == 0) {
                    var2_2 = var5_3;
                } else lbl-1000: // 2 sources:
                {
                    var2_2 = var19_19.leftMargin + var19_19.rightMargin;
                    var2_2 = var19_19.width == -2 ? View.MeasureSpec.makeMeasureSpec((int)(var13_11 - var2_2), (int)Integer.MIN_VALUE) : (var19_19.width == -1 ? View.MeasureSpec.makeMeasureSpec((int)(var13_11 - var2_2), (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)var19_19.width, (int)1073741824));
                    var7_8 = var19_19.height == -2 ? View.MeasureSpec.makeMeasureSpec((int)var1_1, (int)Integer.MIN_VALUE) : (var19_19.height == -1 ? View.MeasureSpec.makeMeasureSpec((int)var1_1, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)var19_19.height, (int)1073741824));
                    var18_18.measure(var2_2, var7_8);
                    var7_8 = var18_18.getMeasuredWidth();
                    var15_16 = var18_18.getMeasuredHeight();
                    var2_2 = var5_3;
                    if (var6_7 == Integer.MIN_VALUE) {
                        var2_2 = var5_3;
                        if (var15_16 > var5_3) {
                            var2_2 = Math.min(var15_16, var1_1);
                        }
                    }
                    var17_17 = (var9_4 -= var7_8) < 0;
                    var19_19.slideable = var17_17;
                    if (var19_19.slideable) {
                        this.mSlideableView = var18_18;
                    }
                    var16_10 = var17_17 | var16_10;
                    var4_9 = var3_15;
                }
            }
            var5_3 = var2_2;
        }
        if (var16_10 || var4_9 > 0.0f) {
            var12_14 = var13_11 - this.mOverhangSize;
            var7_8 = var14_12;
            var2_2 = var1_1;
            var10_5 = var6_7;
            var6_7 = var12_14;
            for (var11_13 = 0; var11_13 < var7_8; ++var11_13) {
                var18_18 = this.getChildAt(var11_13);
                if (var18_18.getVisibility() == 8) continue;
                var19_19 = (LayoutParams)var18_18.getLayoutParams();
                if (var18_18.getVisibility() == 8) continue;
                var1_1 = var19_19.width == 0 && var19_19.weight > 0.0f ? 1 : 0;
                var12_14 = var1_1 != 0 ? 0 : var18_18.getMeasuredWidth();
                if (var16_10 && var18_18 != this.mSlideableView) {
                    if (var19_19.width >= 0 || var12_14 <= var6_7 && var19_19.weight <= 0.0f) continue;
                    var1_1 = var1_1 != 0 ? (var19_19.height == -2 ? View.MeasureSpec.makeMeasureSpec((int)var2_2, (int)Integer.MIN_VALUE) : (var19_19.height == -1 ? View.MeasureSpec.makeMeasureSpec((int)var2_2, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)var19_19.height, (int)1073741824))) : View.MeasureSpec.makeMeasureSpec((int)var18_18.getMeasuredHeight(), (int)1073741824);
                    var18_18.measure(View.MeasureSpec.makeMeasureSpec((int)var6_7, (int)1073741824), var1_1);
                    continue;
                }
                if (var19_19.weight <= 0.0f) continue;
                var1_1 = var19_19.width == 0 ? (var19_19.height == -2 ? View.MeasureSpec.makeMeasureSpec((int)var2_2, (int)Integer.MIN_VALUE) : (var19_19.height == -1 ? View.MeasureSpec.makeMeasureSpec((int)var2_2, (int)1073741824) : View.MeasureSpec.makeMeasureSpec((int)var19_19.height, (int)1073741824))) : View.MeasureSpec.makeMeasureSpec((int)var18_18.getMeasuredHeight(), (int)1073741824);
                if (var16_10) {
                    var14_12 = var13_11 - (var19_19.leftMargin + var19_19.rightMargin);
                    var15_16 = View.MeasureSpec.makeMeasureSpec((int)var14_12, (int)1073741824);
                    if (var12_14 == var14_12) continue;
                    var18_18.measure(var15_16, var1_1);
                    continue;
                }
                var14_12 = Math.max(0, var9_4);
                var18_18.measure(View.MeasureSpec.makeMeasureSpec((int)(var12_14 + (int)(var19_19.weight * (float)var14_12 / var4_9)), (int)1073741824), var1_1);
            }
        }
        this.setMeasuredDimension(var8_6, this.getPaddingTop() + var5_3 + this.getPaddingBottom());
        this.mCanSlide = var16_10;
        if (this.mDragHelper.getViewDragState() == 0) return;
        if (var16_10 != false) return;
        this.mDragHelper.abort();
    }

    void onPanelDragged(int n) {
        float f;
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        boolean bl = this.isLayoutRtlSupport();
        LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
        int n2 = this.mSlideableView.getWidth();
        if (bl) {
            n = this.getWidth() - n - n2;
        }
        n2 = bl ? this.getPaddingRight() : this.getPaddingLeft();
        int n3 = bl ? layoutParams.rightMargin : layoutParams.leftMargin;
        this.mSlideOffset = f = (float)(n - (n2 + n3)) / (float)this.mSlideRange;
        if (this.mParallaxBy != 0) {
            this.parallaxOtherViews(f);
        }
        if (layoutParams.dimWhenOffset) {
            this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        this.dispatchOnPanelSlide(this.mSlideableView);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        if (parcelable.isOpen) {
            this.openPane();
        } else {
            this.closePane();
        }
        this.mPreservedOpenState = parcelable.isOpen;
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        boolean bl = this.isSlideable() ? this.isOpen() : this.mPreservedOpenState;
        savedState.isOpen = bl;
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.mFirstLayout = true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(motionEvent);
        }
        this.mDragHelper.processTouchEvent(motionEvent);
        int n = motionEvent.getActionMasked();
        if (n != 0) {
            float f;
            float f2;
            float f3;
            float f4;
            if (n != 1) {
                return true;
            }
            if (this.isDimmed(this.mSlideableView) && (f = (f4 = motionEvent.getX()) - this.mInitialMotionX) * f + (f3 = (f2 = motionEvent.getY()) - this.mInitialMotionY) * f3 < (float)((n = this.mDragHelper.getTouchSlop()) * n) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)f4, (int)f2)) {
                this.closePane(this.mSlideableView, 0);
                return true;
            }
        } else {
            float f = motionEvent.getX();
            float f5 = motionEvent.getY();
            this.mInitialMotionX = f;
            this.mInitialMotionY = f5;
        }
        return true;
    }

    public boolean openPane() {
        return this.openPane(this.mSlideableView, 0);
    }

    public void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        if (!this.isInTouchMode() && !this.mCanSlide) {
            boolean bl = view == this.mSlideableView;
            this.mPreservedOpenState = bl;
        }
    }

    void setAllChildrenVisible() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 4) continue;
            view.setVisibility(0);
        }
    }

    public void setCoveredFadeColor(int n) {
        this.mCoveredFadeColor = n;
    }

    public void setPanelSlideListener(PanelSlideListener panelSlideListener) {
        this.mPanelSlideListener = panelSlideListener;
    }

    public void setParallaxDistance(int n) {
        this.mParallaxBy = n;
        this.requestLayout();
    }

    @Deprecated
    public void setShadowDrawable(Drawable drawable2) {
        this.setShadowDrawableLeft(drawable2);
    }

    public void setShadowDrawableLeft(Drawable drawable2) {
        this.mShadowDrawableLeft = drawable2;
    }

    public void setShadowDrawableRight(Drawable drawable2) {
        this.mShadowDrawableRight = drawable2;
    }

    @Deprecated
    public void setShadowResource(int n) {
        this.setShadowDrawable(this.getResources().getDrawable(n));
    }

    public void setShadowResourceLeft(int n) {
        this.setShadowDrawableLeft(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setShadowResourceRight(int n) {
        this.setShadowDrawableRight(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setSliderFadeColor(int n) {
        this.mSliderFadeColor = n;
    }

    @Deprecated
    public void smoothSlideClosed() {
        this.closePane();
    }

    @Deprecated
    public void smoothSlideOpen() {
        this.openPane();
    }

    boolean smoothSlideTo(float f, int n) {
        if (!this.mCanSlide) {
            return false;
        }
        boolean bl = this.isLayoutRtlSupport();
        Object object = (LayoutParams)this.mSlideableView.getLayoutParams();
        if (bl) {
            n = this.getPaddingRight();
            int n2 = object.rightMargin;
            int n3 = this.mSlideableView.getWidth();
            n = (int)((float)this.getWidth() - ((float)(n + n2) + (float)this.mSlideRange * f + (float)n3));
        } else {
            n = (int)((float)(this.getPaddingLeft() + object.leftMargin) + (float)this.mSlideRange * f);
        }
        object = this.mDragHelper;
        View view = this.mSlideableView;
        if (object.smoothSlideViewTo(view, n, view.getTop())) {
            this.setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation((View)this);
            return true;
        }
        return false;
    }

    void updateObscuredViewsVisibility(View view) {
        int n;
        int n2;
        int n3;
        int n4;
        boolean bl = this.isLayoutRtlSupport();
        int n5 = bl ? this.getWidth() - this.getPaddingRight() : this.getPaddingLeft();
        int n6 = bl ? this.getPaddingLeft() : this.getWidth() - this.getPaddingRight();
        int n7 = this.getPaddingTop();
        int n8 = this.getHeight();
        int n9 = this.getPaddingBottom();
        if (view != null && SlidingPaneLayout.viewIsOpaque(view)) {
            n = view.getLeft();
            n4 = view.getRight();
            n2 = view.getTop();
            n3 = view.getBottom();
        } else {
            n = 0;
            n3 = 0;
            n2 = 0;
            n4 = 0;
        }
        int n10 = this.getChildCount();
        for (int i = 0; i < n10; ++i) {
            View view2 = this.getChildAt(i);
            if (view2 == view) {
                return;
            }
            if (view2.getVisibility() == 8) continue;
            int n11 = bl ? n6 : n5;
            int n12 = Math.max(n11, view2.getLeft());
            int n13 = Math.max(n7, view2.getTop());
            n11 = bl ? n5 : n6;
            n11 = Math.min(n11, view2.getRight());
            int n14 = Math.min(n8 - n9, view2.getBottom());
            n11 = n12 >= n && n13 >= n2 && n11 <= n4 && n14 <= n3 ? 4 : 0;
            view2.setVisibility(n11);
        }
    }

    class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;

        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
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

        public boolean filter(View view) {
            return SlidingPaneLayout.this.isDimmed(view);
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)SlidingPaneLayout.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
            this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompat2);
            accessibilityNodeInfoCompat2.recycle();
            accessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(view);
            view = ViewCompat.getParentForAccessibility(view);
            if (view instanceof View) {
                accessibilityNodeInfoCompat.setParent(view);
            }
            int n = SlidingPaneLayout.this.getChildCount();
            for (int i = 0; i < n; ++i) {
                view = SlidingPaneLayout.this.getChildAt(i);
                if (this.filter(view) || view.getVisibility() != 0) continue;
                ViewCompat.setImportantForAccessibility(view, 1);
                accessibilityNodeInfoCompat.addChild(view);
            }
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (!this.filter(view)) {
                return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    private class DisableLayerRunnable
    implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View view) {
            this.mChildView = view;
        }

        @Override
        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                this.mChildView.setLayerType(0, null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }

    private class DragHelperCallback
    extends ViewDragHelper.Callback {
        DragHelperCallback() {
        }

        @Override
        public int clampViewPositionHorizontal(View object, int n, int n2) {
            object = (LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                n2 = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + object.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
                int n3 = SlidingPaneLayout.this.mSlideRange;
                return Math.max(Math.min(n, n2), n2 - n3);
            }
            n2 = SlidingPaneLayout.this.getPaddingLeft() + object.leftMargin;
            int n4 = SlidingPaneLayout.this.mSlideRange;
            return Math.min(Math.max(n, n2), n4 + n2);
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            return SlidingPaneLayout.this.mSlideRange;
        }

        @Override
        public void onEdgeDragStarted(int n, int n2) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, n2);
        }

        @Override
        public void onViewCaptured(View view, int n) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }

        @Override
        public void onViewDragStateChanged(int n) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
                if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                    SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
                    slidingPaneLayout.updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
                    slidingPaneLayout = SlidingPaneLayout.this;
                    slidingPaneLayout.dispatchOnPanelClosed(slidingPaneLayout.mSlideableView);
                    SlidingPaneLayout.this.mPreservedOpenState = false;
                    return;
                }
                SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
                slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = true;
            }
        }

        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            SlidingPaneLayout.this.onPanelDragged(n);
            SlidingPaneLayout.this.invalidate();
        }

        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            block7 : {
                block4 : {
                    int n2;
                    block6 : {
                        block5 : {
                            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                            if (!SlidingPaneLayout.this.isLayoutRtlSupport()) break block4;
                            n2 = SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin;
                            if (f < 0.0f) break block5;
                            n = n2;
                            if (f != 0.0f) break block6;
                            n = n2;
                            if (SlidingPaneLayout.this.mSlideOffset <= 0.5f) break block6;
                        }
                        n = n2 + SlidingPaneLayout.this.mSlideRange;
                    }
                    n2 = SlidingPaneLayout.this.mSlideableView.getWidth();
                    n = SlidingPaneLayout.this.getWidth() - n - n2;
                    break block7;
                }
                n = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
                if (f > 0.0f || f == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f) {
                    n += SlidingPaneLayout.this.mSlideRange;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(n, view.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            if (SlidingPaneLayout.this.mIsUnableToDrag) {
                return false;
            }
            return ((LayoutParams)view.getLayoutParams()).slideable;
        }
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{16843137};
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, ATTRS);
            this.weight = context.getFloat(0, 0.0f);
            context.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.weight = layoutParams.weight;
        }
    }

    public static interface PanelSlideListener {
        public void onPanelClosed(View var1);

        public void onPanelOpened(View var1);

        public void onPanelSlide(View var1, float var2);
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, null);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        boolean isOpen;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            boolean bl = parcel.readInt() != 0;
            this.isOpen = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            RuntimeException runtimeException;
            super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            throw runtimeException;
        }

    }

    public static class SimplePanelSlideListener
    implements PanelSlideListener {
        @Override
        public void onPanelClosed(View view) {
        }

        @Override
        public void onPanelOpened(View view) {
        }

        @Override
        public void onPanelSlide(View view, float f) {
        }
    }

}

