/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.FocusFinder
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.SoundEffectConstants
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.animation.Interpolator
 *  android.widget.EdgeEffect
 *  android.widget.Scroller
 */
package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager
extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    static {
        LAYOUT_ATTRS = new int[]{16842931};
        COMPARATOR = new Comparator<ItemInfo>(){

            @Override
            public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }

    public ViewPager(Context context) {
        super(context);
        this.mEndScrollRunnable = new Runnable(){

            @Override
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEndScrollRunnable = new ;
        this.mScrollState = 0;
        this.initViewPager();
    }

    private void calculatePageOffsets(ItemInfo itemInfo, int n, ItemInfo itemInfo2) {
        int n2;
        float f;
        int n3 = this.mAdapter.getCount();
        int n4 = this.getClientWidth();
        float f2 = n4 > 0 ? (float)this.mPageMargin / (float)n4 : 0.0f;
        if (itemInfo2 != null) {
            n4 = itemInfo2.position;
            if (n4 < itemInfo.position) {
                n2 = 0;
                f = itemInfo2.offset + itemInfo2.widthFactor + f2;
                ++n4;
                while (n4 <= itemInfo.position && n2 < this.mItems.size()) {
                    itemInfo2 = this.mItems.get(n2);
                    while (n4 > itemInfo2.position && n2 < this.mItems.size() - 1) {
                        itemInfo2 = this.mItems.get(++n2);
                    }
                    while (n4 < itemInfo2.position) {
                        f += this.mAdapter.getPageWidth(n4) + f2;
                        ++n4;
                    }
                    itemInfo2.offset = f;
                    f += itemInfo2.widthFactor + f2;
                    ++n4;
                }
            } else if (n4 > itemInfo.position) {
                n2 = this.mItems.size() - 1;
                f = itemInfo2.offset;
                --n4;
                while (n4 >= itemInfo.position && n2 >= 0) {
                    itemInfo2 = this.mItems.get(n2);
                    while (n4 < itemInfo2.position && n2 > 0) {
                        itemInfo2 = this.mItems.get(--n2);
                    }
                    while (n4 > itemInfo2.position) {
                        f -= this.mAdapter.getPageWidth(n4) + f2;
                        --n4;
                    }
                    itemInfo2.offset = f -= itemInfo2.widthFactor + f2;
                    --n4;
                }
            }
        }
        int n5 = this.mItems.size();
        float f3 = itemInfo.offset;
        n4 = itemInfo.position - 1;
        f = itemInfo.position == 0 ? itemInfo.offset : -3.4028235E38f;
        this.mFirstOffset = f;
        f = itemInfo.position == n3 - 1 ? itemInfo.offset + itemInfo.widthFactor - 1.0f : Float.MAX_VALUE;
        this.mLastOffset = f;
        n2 = n - 1;
        f = f3;
        while (n2 >= 0) {
            itemInfo2 = this.mItems.get(n2);
            while (n4 > itemInfo2.position) {
                f -= this.mAdapter.getPageWidth(n4) + f2;
                --n4;
            }
            itemInfo2.offset = f -= itemInfo2.widthFactor + f2;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = f;
            }
            --n2;
            --n4;
        }
        f = itemInfo.offset + itemInfo.widthFactor + f2;
        n2 = itemInfo.position + 1;
        n4 = n + 1;
        n = n2;
        while (n4 < n5) {
            itemInfo = this.mItems.get(n4);
            while (n < itemInfo.position) {
                f += this.mAdapter.getPageWidth(n) + f2;
                ++n;
            }
            if (itemInfo.position == n3 - 1) {
                this.mLastOffset = itemInfo.widthFactor + f - 1.0f;
            }
            itemInfo.offset = f;
            f += itemInfo.widthFactor + f2;
            ++n4;
            ++n;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean bl) {
        int n;
        boolean bl2 = this.mScrollState == 2;
        if (bl2) {
            this.setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                n = this.getScrollX();
                int n2 = this.getScrollY();
                int n3 = this.mScroller.getCurrX();
                int n4 = this.mScroller.getCurrY();
                if (n != n3 || n2 != n4) {
                    this.scrollTo(n3, n4);
                    if (n3 != n) {
                        this.pageScrolled(n3);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (n = 0; n < this.mItems.size(); ++n) {
            ItemInfo itemInfo = this.mItems.get(n);
            if (!itemInfo.scrolling) continue;
            bl2 = true;
            itemInfo.scrolling = false;
        }
        if (bl2) {
            if (bl) {
                ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
                return;
            }
            this.mEndScrollRunnable.run();
            return;
        }
    }

    private int determineTargetPage(int n, float f, int n2, int n3) {
        if (Math.abs(n3) > this.mFlingDistance && Math.abs(n2) > this.mMinimumVelocity) {
            if (n2 <= 0) {
                ++n;
            }
        } else {
            float f2 = n >= this.mCurItem ? 0.4f : 0.6f;
            n += (int)(f + f2);
        }
        if (this.mItems.size() > 0) {
            ItemInfo itemInfo = this.mItems.get(0);
            Object object = this.mItems;
            object = object.get(object.size() - 1);
            return Math.max(itemInfo.position, Math.min(n, object.position));
        }
        return n;
    }

    private void dispatchOnPageScrolled(int n, float f, int n2) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageScrolled(n, f, n2);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n3 = object.size();
            for (int i = 0; i < n3; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageScrolled(n, f, n2);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageScrolled(n, f, n2);
            return;
        }
    }

    private void dispatchOnPageSelected(int n) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageSelected(n);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n2 = object.size();
            for (int i = 0; i < n2; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageSelected(n);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageSelected(n);
            return;
        }
    }

    private void dispatchOnScrollStateChanged(int n) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageScrollStateChanged(n);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n2 = object.size();
            for (int i = 0; i < n2; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageScrollStateChanged(n);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageScrollStateChanged(n);
            return;
        }
    }

    private void enableLayers(boolean bl) {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            int n2 = bl ? this.mPageTransformerLayerType : 0;
            this.getChildAt(i).setLayerType(n2, null);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
            return;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return rect;
        }
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.top = view.getTop();
        rect.bottom = view.getBottom();
        for (view = view.getParent(); view instanceof ViewGroup && view != this; view = view.getParent()) {
            view = (ViewGroup)view;
            rect.left += view.getLeft();
            rect.right += view.getRight();
            rect.top += view.getTop();
            rect.bottom += view.getBottom();
        }
        return rect;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int n = this.getClientWidth();
        float f = 0.0f;
        float f2 = n > 0 ? (float)this.getScrollX() / (float)n : 0.0f;
        if (n > 0) {
            f = (float)this.mPageMargin / (float)n;
        }
        int n2 = -1;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean bl = true;
        ItemInfo itemInfo = null;
        for (n = 0; n < this.mItems.size(); ++n) {
            ItemInfo itemInfo2 = this.mItems.get(n);
            if (!bl && itemInfo2.position != n2 + 1) {
                itemInfo2 = this.mTempItem;
                itemInfo2.offset = f3 + f4 + f;
                itemInfo2.position = n2 + 1;
                itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
                --n;
            }
            f3 = itemInfo2.offset;
            f4 = itemInfo2.widthFactor;
            if (!bl && f2 < f3) {
                return itemInfo;
            }
            if (f2 >= f4 + f3 + f) {
                if (n == this.mItems.size() - 1) {
                    return itemInfo2;
                }
                bl = false;
                n2 = itemInfo2.position;
                f4 = itemInfo2.widthFactor;
                itemInfo = itemInfo2;
                continue;
            }
            return itemInfo2;
        }
        return itemInfo;
    }

    private static boolean isDecorView(@NonNull View view) {
        if (view.getClass().getAnnotation(DecorView.class) != null) {
            return true;
        }
        return false;
    }

    private boolean isGutterDrag(float f, float f2) {
        block3 : {
            block2 : {
                if (f < (float)this.mGutterSize && f2 > 0.0f) break block2;
                if (f <= (float)(this.getWidth() - this.mGutterSize) || f2 >= 0.0f) break block3;
            }
            return true;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            motionEvent = this.mVelocityTracker;
            if (motionEvent != null) {
                motionEvent.clear();
                return;
            }
            return;
        }
    }

    private boolean pageScrolled(int n) {
        if (this.mItems.size() == 0) {
            if (this.mFirstLayout) {
                return false;
            }
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        ItemInfo itemInfo = this.infoForCurrentScrollPosition();
        int n2 = this.getClientWidth();
        int n3 = this.mPageMargin;
        float f = (float)n3 / (float)n2;
        int n4 = itemInfo.position;
        f = ((float)n / (float)n2 - itemInfo.offset) / (itemInfo.widthFactor + f);
        n = (int)((float)(n2 + n3) * f);
        this.mCalledSuper = false;
        this.onPageScrolled(n4, f, n);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    private boolean performDrag(float f) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        float f2 = this.mLastMotionX;
        this.mLastMotionX = f;
        float f3 = (float)this.getScrollX() + (f2 - f);
        int n = this.getClientWidth();
        f = (float)n * this.mFirstOffset;
        f2 = (float)n * this.mLastOffset;
        boolean bl4 = true;
        boolean bl5 = true;
        ItemInfo itemInfo = this.mItems.get(0);
        Object object = this.mItems;
        object = object.get(object.size() - 1);
        if (itemInfo.position != 0) {
            bl4 = false;
            f = itemInfo.offset * (float)n;
        }
        if (object.position != this.mAdapter.getCount() - 1) {
            bl5 = false;
            f2 = object.offset * (float)n;
        }
        if (f3 < f) {
            if (bl4) {
                this.mLeftEdge.onPull(Math.abs(f - f3) / (float)n);
                bl3 = true;
            }
        } else if (f3 > f2) {
            if (bl5) {
                this.mRightEdge.onPull(Math.abs(f3 - f2) / (float)n);
                bl3 = true;
            } else {
                bl3 = bl;
            }
            f = f2;
        } else {
            f = f3;
            bl3 = bl2;
        }
        this.mLastMotionX += f - (float)((int)f);
        this.scrollTo((int)f, this.getScrollY());
        this.pageScrolled((int)f);
        return bl3;
    }

    private void recomputeScrollPosition(int n, int n2, int n3, int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
                return;
            }
            int n5 = this.getPaddingLeft();
            int n6 = this.getPaddingRight();
            int n7 = this.getPaddingLeft();
            int n8 = this.getPaddingRight();
            float f = (float)this.getScrollX() / (float)(n2 - n7 - n8 + n4);
            this.scrollTo((int)((float)(n - n5 - n6 + n3) * f), this.getScrollY());
            return;
        }
        ItemInfo itemInfo = this.infoForPosition(this.mCurItem);
        float f = itemInfo != null ? Math.min(itemInfo.offset, this.mLastOffset) : 0.0f;
        if ((n = (int)((float)(n - this.getPaddingLeft() - this.getPaddingRight()) * f)) != this.getScrollX()) {
            this.completeScroll(false);
            this.scrollTo(n, this.getScrollY());
            return;
        }
    }

    private void removeNonDecorViews() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            if (((LayoutParams)this.getChildAt((int)i).getLayoutParams()).isDecor) continue;
            this.removeViewAt(i);
            --i;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean bl) {
        ViewParent viewParent = this.getParent();
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(bl);
            return;
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        if (!this.mLeftEdge.isFinished() && !this.mRightEdge.isFinished()) {
            return false;
        }
        return true;
    }

    private void scrollToItem(int n, boolean bl, int n2, boolean bl2) {
        ItemInfo itemInfo = this.infoForPosition(n);
        int n3 = 0;
        if (itemInfo != null) {
            n3 = (int)((float)this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset)));
        }
        if (bl) {
            this.smoothScrollTo(n3, 0, n2);
            if (bl2) {
                this.dispatchOnPageSelected(n);
                return;
            }
            return;
        }
        if (bl2) {
            this.dispatchOnPageSelected(n);
        }
        this.completeScroll(false);
        this.scrollTo(n3, 0);
        this.pageScrolled(n3);
    }

    private void setScrollingCacheEnabled(boolean bl) {
        if (this.mScrollingCacheEnabled != bl) {
            this.mScrollingCacheEnabled = bl;
            return;
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            View view = this.mDrawingOrderedChildren;
            if (view == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                view.clear();
            }
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                view = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(view);
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
            return;
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        int n3 = arrayList.size();
        int n4 = this.getDescendantFocusability();
        if (n4 != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                ItemInfo itemInfo;
                View view = this.getChildAt(i);
                if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
                view.addFocusables(arrayList, n, n2);
            }
        }
        if (n4 == 262144 && n3 != arrayList.size()) {
            return;
        }
        if (!this.isFocusable()) {
            return;
        }
        if ((n2 & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
            return;
        }
        if (arrayList != null) {
            arrayList.add((View)this);
            return;
        }
    }

    ItemInfo addNewItem(int n, int n2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = n;
        itemInfo.object = this.mAdapter.instantiateItem(this, n);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(n);
        if (n2 >= 0 && n2 < this.mItems.size()) {
            this.mItems.add(n2, itemInfo);
            return itemInfo;
        }
        this.mItems.add(itemInfo);
        return itemInfo;
    }

    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>();
        }
        this.mAdapterChangeListeners.add(onAdapterChangeListener);
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(onPageChangeListener);
    }

    public void addTouchables(ArrayList<View> arrayList) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
            view.addTouchables(arrayList);
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (!this.checkLayoutParams(layoutParams)) {
            layoutParams = this.generateLayoutParams(layoutParams);
        }
        LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        layoutParams2.isDecor |= ViewPager.isDecorView(view);
        if (this.mInLayout) {
            if (layoutParams2 != null && layoutParams2.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams2.needsMeasure = true;
            this.addViewInLayout(view, n, layoutParams);
            return;
        }
        super.addView(view, n, layoutParams);
    }

    public boolean arrowScroll(int n) {
        Object object;
        int n2;
        View view = this.findFocus();
        if (view == this) {
            object = null;
        } else if (view != null) {
            n2 = 0;
            object = view.getParent();
            while (object instanceof ViewGroup) {
                if (object == this) {
                    n2 = 1;
                    break;
                }
                object = object.getParent();
            }
            if (n2 == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(view.getClass().getSimpleName());
                object = view.getParent();
                while (object instanceof ViewGroup) {
                    stringBuilder.append(" => ");
                    stringBuilder.append(object.getClass().getSimpleName());
                    object = object.getParent();
                }
                object = new StringBuilder();
                object.append("arrowScroll tried to find focus based on non-child current focused view ");
                object.append(stringBuilder.toString());
                Log.e((String)"ViewPager", (String)object.toString());
                object = null;
            } else {
                object = view;
            }
        } else {
            object = view;
        }
        boolean bl = false;
        boolean bl2 = false;
        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)object, n);
        if (view != null && view != object) {
            if (n == 17) {
                n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                int n3 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                bl2 = object != null && n2 >= n3 ? this.pageLeft() : view.requestFocus();
            } else if (n == 66) {
                n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                int n4 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                bl2 = object != null && n2 <= n4 ? this.pageRight() : view.requestFocus();
            }
        } else {
            bl2 = n != 17 && n != 1 ? (n != 66 && n != 2 ? bl : this.pageRight()) : this.pageLeft();
        }
        if (bl2) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection((int)n));
            return bl2;
        }
        return bl2;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long l = SystemClock.uptimeMillis();
        velocityTracker = MotionEvent.obtain((long)l, (long)l, (int)0, (float)0.0f, (float)0.0f, (int)0);
        this.mVelocityTracker.addMovement((MotionEvent)velocityTracker);
        velocityTracker.recycle();
        this.mFakeDragBeginTime = l;
        return true;
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
        if (bl && view.canScrollHorizontally(- n)) {
            return true;
        }
        return false;
    }

    public boolean canScrollHorizontally(int n) {
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean bl = false;
        boolean bl2 = false;
        if (pagerAdapter == null) {
            return false;
        }
        int n2 = this.getClientWidth();
        int n3 = this.getScrollX();
        if (n < 0) {
            if (n3 > (int)((float)n2 * this.mFirstOffset)) {
                bl2 = true;
            }
            return bl2;
        }
        if (n > 0) {
            bl2 = bl;
            if (n3 < (int)((float)n2 * this.mLastOffset)) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    public void clearOnPageChangeListeners() {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.clear();
            return;
        }
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int n = this.getScrollX();
            int n2 = this.getScrollY();
            int n3 = this.mScroller.getCurrX();
            int n4 = this.mScroller.getCurrY();
            if (n != n3 || n2 != n4) {
                this.scrollTo(n3, n4);
                if (!this.pageScrolled(n3)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, n4);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }

    void dataSetChanged() {
        Object object;
        int n;
        this.mExpectedAdapterCount = n = this.mAdapter.getCount();
        int n2 = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < n ? 1 : 0;
        int n3 = this.mCurItem;
        int n4 = 0;
        for (int i = 0; i < this.mItems.size(); ++i) {
            object = this.mItems.get(i);
            int n5 = this.mAdapter.getItemPosition(object.object);
            if (n5 == -1) continue;
            if (n5 == -2) {
                this.mItems.remove(i);
                --i;
                if (n4 == 0) {
                    this.mAdapter.startUpdate(this);
                    n4 = 1;
                }
                this.mAdapter.destroyItem(this, object.position, object.object);
                n2 = 1;
                if (this.mCurItem != object.position) continue;
                n3 = Math.max(0, Math.min(this.mCurItem, n - 1));
                n2 = 1;
                continue;
            }
            if (object.position == n5) continue;
            if (object.position == this.mCurItem) {
                n3 = n5;
            }
            object.position = n5;
            n2 = 1;
        }
        if (n4 != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (n2 != 0) {
            n4 = this.getChildCount();
            for (n2 = 0; n2 < n4; ++n2) {
                object = (LayoutParams)this.getChildAt(n2).getLayoutParams();
                if (object.isDecor) continue;
                object.widthFactor = 0.0f;
            }
            this.setCurrentItemInternal(n3, false, true);
            this.requestLayout();
            return;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!super.dispatchKeyEvent(keyEvent) && !this.executeKeyEvent(keyEvent)) {
            return false;
        }
        return true;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem || !view.dispatchPopulateAccessibilityEvent(accessibilityEvent)) continue;
            return true;
        }
        return false;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin((f - 0.5f) * 0.47123894f);
    }

    public void draw(Canvas canvas) {
        PagerAdapter pagerAdapter;
        super.draw(canvas);
        int n = 0;
        int n2 = 0;
        int n3 = this.getOverScrollMode();
        if (n3 != 0 && (n3 != 1 || (pagerAdapter = this.mAdapter) == null || pagerAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
            n2 = n;
        } else {
            if (!this.mLeftEdge.isFinished()) {
                n = canvas.save();
                n2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                n3 = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(- n2 + this.getPaddingTop()), this.mFirstOffset * (float)n3);
                this.mLeftEdge.setSize(n2, n3);
                n2 = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(n);
            }
            if (!this.mRightEdge.isFinished()) {
                n = canvas.save();
                n3 = this.getWidth();
                int n4 = this.getHeight();
                int n5 = this.getPaddingTop();
                int n6 = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(- this.getPaddingTop()), (- this.mLastOffset + 1.0f) * (float)n3);
                this.mRightEdge.setSize(n4 - n5 - n6, n3);
                n2 |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(n);
            }
        }
        if (n2 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.mMarginDrawable;
        if (drawable2 != null && drawable2.isStateful()) {
            drawable2.setState(this.getDrawableState());
            return;
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                Object object = this.mVelocityTracker;
                object.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                int n = (int)object.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int n2 = this.getClientWidth();
                int n3 = this.getScrollX();
                object = this.infoForCurrentScrollPosition();
                this.setCurrentItemInternal(this.determineTargetPage(object.position, ((float)n3 / (float)n2 - object.offset) / object.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
            }
            this.endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            int n = keyEvent.getKeyCode();
            if (n != 61) {
                switch (n) {
                    default: {
                        return false;
                    }
                    case 22: {
                        if (keyEvent.hasModifiers(2)) {
                            return this.pageRight();
                        }
                        return this.arrowScroll(66);
                    }
                    case 21: 
                }
                if (keyEvent.hasModifiers(2)) {
                    return this.pageLeft();
                }
                return this.arrowScroll(17);
            }
            if (keyEvent.hasNoModifiers()) {
                return this.arrowScroll(2);
            }
            if (keyEvent.hasModifiers(1)) {
                return this.arrowScroll(1);
            }
            return false;
        }
        return false;
    }

    public void fakeDragBy(float f) {
        if (this.mFakeDragging) {
            if (this.mAdapter == null) {
                return;
            }
            this.mLastMotionX += f;
            float f2 = (float)this.getScrollX() - f;
            int n = this.getClientWidth();
            f = (float)n * this.mFirstOffset;
            float f3 = (float)n * this.mLastOffset;
            ItemInfo itemInfo = this.mItems.get(0);
            Object object = this.mItems;
            object = object.get(object.size() - 1);
            if (itemInfo.position != 0) {
                f = itemInfo.offset * (float)n;
            }
            if (object.position != this.mAdapter.getCount() - 1) {
                f3 = object.offset * (float)n;
            }
            if (f2 >= f) {
                f = f2 > f3 ? f3 : f2;
            }
            this.mLastMotionX += f - (float)((int)f);
            this.scrollTo((int)f, this.getScrollY());
            this.pageScrolled((int)f);
            long l = SystemClock.uptimeMillis();
            itemInfo = MotionEvent.obtain((long)this.mFakeDragBeginTime, (long)l, (int)2, (float)this.mLastMotionX, (float)0.0f, (int)0);
            this.mVelocityTracker.addMovement((MotionEvent)itemInfo);
            itemInfo.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return this.generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    protected int getChildDrawingOrder(int n, int n2) {
        if (this.mDrawingOrder == 2) {
            n2 = n - 1 - n2;
        }
        return ((LayoutParams)this.mDrawingOrderedChildren.get((int)n2).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View view) {
        ViewParent viewParent;
        while ((viewParent = view.getParent()) != this) {
            if (viewParent != null && viewParent instanceof View) {
                view = (View)viewParent;
                continue;
            }
            return null;
        }
        return this.infoForChild(view);
    }

    ItemInfo infoForChild(View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (!this.mAdapter.isViewFromObject(view, itemInfo.object)) continue;
            return itemInfo;
        }
        return null;
    }

    ItemInfo infoForPosition(int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position != n) continue;
            return itemInfo;
        }
        return null;
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int)(25.0f * f);
        this.mCloseEnough = (int)(2.0f * f);
        this.mDefaultGutterSize = (int)(16.0f * f);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(){
            private final Rect mTempRect;
            {
                this.mTempRect = new Rect();
            }

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
                if ((object = ViewCompat.onApplyWindowInsets((View)object, windowInsetsCompat)).isConsumed()) {
                    return object;
                }
                windowInsetsCompat = this.mTempRect;
                windowInsetsCompat.left = object.getSystemWindowInsetLeft();
                windowInsetsCompat.top = object.getSystemWindowInsetTop();
                windowInsetsCompat.right = object.getSystemWindowInsetRight();
                windowInsetsCompat.bottom = object.getSystemWindowInsetBottom();
                int n = ViewPager.this.getChildCount();
                for (int i = 0; i < n; ++i) {
                    WindowInsetsCompat windowInsetsCompat2 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), (WindowInsetsCompat)object);
                    windowInsetsCompat.left = Math.min(windowInsetsCompat2.getSystemWindowInsetLeft(), windowInsetsCompat.left);
                    windowInsetsCompat.top = Math.min(windowInsetsCompat2.getSystemWindowInsetTop(), windowInsetsCompat.top);
                    windowInsetsCompat.right = Math.min(windowInsetsCompat2.getSystemWindowInsetRight(), windowInsetsCompat.right);
                    windowInsetsCompat.bottom = Math.min(windowInsetsCompat2.getSystemWindowInsetBottom(), windowInsetsCompat.bottom);
                }
                return object.replaceSystemWindowInsets(windowInsetsCompat.left, windowInsetsCompat.top, windowInsetsCompat.right, windowInsetsCompat.bottom);
            }
        });
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        Scroller scroller = this.mScroller;
        if (scroller != null && !scroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int n = this.getScrollX();
            int n2 = this.getWidth();
            float f = (float)this.mPageMargin / (float)n2;
            int n3 = 0;
            Object object = this.mItems.get(0);
            float f2 = object.offset;
            int n4 = this.mItems.size();
            int n5 = this.mItems.get((int)(n4 - 1)).position;
            for (int i = object.position; i < n5; ++i) {
                float f3;
                while (i > object.position && n3 < n4) {
                    object = this.mItems;
                    object = (ItemInfo)object.get(++n3);
                }
                if (i == object.position) {
                    f3 = (object.offset + object.widthFactor) * (float)n2;
                    f2 = object.offset + object.widthFactor + f;
                } else {
                    f3 = this.mAdapter.getPageWidth(i);
                    float f4 = n2;
                    float f5 = f2 + (f3 + f);
                    f3 = (f2 + f3) * f4;
                    f2 = f5;
                }
                if ((float)this.mPageMargin + f3 > (float)n) {
                    this.mMarginDrawable.setBounds(Math.round(f3), this.mTopPageBounds, Math.round((float)this.mPageMargin + f3), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (f3 <= (float)(n + n2)) continue;
                return;
            }
            return;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getAction() & 255;
        if (n != 3 && n != 1) {
            if (n != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            if (n != 0) {
                if (n != 2) {
                    if (n == 6) {
                        this.onSecondaryPointerUp(motionEvent);
                    }
                } else {
                    n = this.mActivePointerId;
                    if (n != -1) {
                        n = motionEvent.findPointerIndex(n);
                        float f = motionEvent.getX(n);
                        float f2 = f - this.mLastMotionX;
                        float f3 = Math.abs(f2);
                        float f4 = motionEvent.getY(n);
                        float f5 = Math.abs(f4 - this.mInitialMotionY);
                        if (f2 != 0.0f && !this.isGutterDrag(this.mLastMotionX, f2) && this.canScroll((View)this, false, (int)f2, (int)f, (int)f4)) {
                            this.mLastMotionX = f;
                            this.mLastMotionY = f4;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                        if (f3 > (float)this.mTouchSlop && 0.5f * f3 > f5) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.setScrollState(1);
                            f2 = f2 > 0.0f ? this.mInitialMotionX + (float)this.mTouchSlop : this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionX = f2;
                            this.mLastMotionY = f4;
                            this.setScrollingCacheEnabled(true);
                        } else if (f5 > (float)this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && this.performDrag(f)) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                    }
                }
            } else {
                float f;
                this.mInitialMotionX = f = motionEvent.getX();
                this.mLastMotionX = f;
                this.mInitialMotionY = f = motionEvent.getY();
                this.mLastMotionY = f;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mIsScrollStarted = true;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mIsBeingDragged = true;
                    this.requestParentDisallowInterceptTouchEvent(true);
                    this.setScrollState(1);
                } else {
                    this.completeScroll(false);
                    this.mIsBeingDragged = false;
                }
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            return this.mIsBeingDragged;
        }
        this.resetTouch();
        return false;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        View view;
        int n5;
        LayoutParams layoutParams;
        int n6;
        int n7 = this.getChildCount();
        int n8 = n3 - n;
        int n9 = n4 - n2;
        n = this.getPaddingLeft();
        n2 = this.getPaddingTop();
        int n10 = this.getPaddingRight();
        n4 = this.getPaddingBottom();
        int n11 = this.getScrollX();
        int n12 = 0;
        for (n6 = 0; n6 < n7; ++n6) {
            view = this.getChildAt(n6);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (!layoutParams.isDecor) continue;
            n3 = layoutParams.gravity & 7;
            int n13 = layoutParams.gravity & 112;
            if (n3 != 1) {
                if (n3 != 3) {
                    if (n3 != 5) {
                        n3 = n;
                        n5 = n;
                    } else {
                        n3 = n8 - n10 - view.getMeasuredWidth();
                        n10 += view.getMeasuredWidth();
                        n5 = n;
                    }
                } else {
                    n3 = n;
                    n5 = n + view.getMeasuredWidth();
                }
            } else {
                n3 = Math.max((n8 - view.getMeasuredWidth()) / 2, n);
                n5 = n;
            }
            if (n13 != 16) {
                if (n13 != 48) {
                    if (n13 != 80) {
                        n = n2;
                    } else {
                        n = n9 - n4 - view.getMeasuredHeight();
                        n4 += view.getMeasuredHeight();
                    }
                } else {
                    n = n2;
                    n2 += view.getMeasuredHeight();
                }
            } else {
                n = Math.max((n9 - view.getMeasuredHeight()) / 2, n2);
            }
            view.layout(n3, n, view.getMeasuredWidth() + (n3 += n11), n + view.getMeasuredHeight());
            ++n12;
            n = n5;
        }
        n6 = n8 - n - n10;
        n3 = n8;
        n10 = n7;
        for (n5 = 0; n5 < n10; ++n5) {
            ItemInfo itemInfo;
            view = this.getChildAt(n5);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isDecor || (itemInfo = this.infoForChild(view)) == null) continue;
            n7 = n + (int)((float)n6 * itemInfo.offset);
            if (layoutParams.needsMeasure) {
                layoutParams.needsMeasure = false;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n6 * layoutParams.widthFactor)), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n9 - n2 - n4), (int)1073741824));
            }
            view.layout(n7, n2, view.getMeasuredWidth() + n7, view.getMeasuredHeight() + n2);
        }
        this.mTopPageBounds = n2;
        this.mBottomPageBounds = n9 - n4;
        this.mDecorChildCount = n12;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    protected void onMeasure(int n, int n2) {
        View view;
        int n3;
        LayoutParams layoutParams;
        this.setMeasuredDimension(ViewPager.getDefaultSize((int)0, (int)n), ViewPager.getDefaultSize((int)0, (int)n2));
        int n4 = this.getMeasuredWidth();
        int n5 = n4 / 10;
        this.mGutterSize = Math.min(n5, this.mDefaultGutterSize);
        n = n4 - this.getPaddingLeft() - this.getPaddingRight();
        n2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n6 = this.getChildCount();
        for (int i = 0; i < n6; ++i) {
            int n7;
            view = this.getChildAt(i);
            if (view.getVisibility() == 8 || (layoutParams = (LayoutParams)view.getLayoutParams()) == null || !layoutParams.isDecor) continue;
            int n8 = layoutParams.gravity & 7;
            int n9 = layoutParams.gravity & 112;
            n3 = Integer.MIN_VALUE;
            int n10 = Integer.MIN_VALUE;
            n9 = n9 != 48 && n9 != 80 ? 0 : 1;
            boolean bl = n8 == 3 || n8 == 5;
            if (n9 != 0) {
                n3 = 1073741824;
            } else if (bl) {
                n10 = 1073741824;
            }
            if (layoutParams.width != -2) {
                n7 = 1073741824;
                n3 = layoutParams.width != -1 ? layoutParams.width : n;
            } else {
                n8 = n;
                n7 = n3;
                n3 = n8;
            }
            if (layoutParams.height != -2) {
                if (layoutParams.height != -1) {
                    n10 = layoutParams.height;
                    n8 = 1073741824;
                } else {
                    n8 = 1073741824;
                    n10 = n2;
                }
            } else {
                n8 = n10;
                n10 = n2;
            }
            view.measure(View.MeasureSpec.makeMeasureSpec((int)n3, (int)n7), View.MeasureSpec.makeMeasureSpec((int)n10, (int)n8));
            if (n9 != 0) {
                n2 -= view.getMeasuredHeight();
                continue;
            }
            if (!bl) continue;
            n -= view.getMeasuredWidth();
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n2, (int)1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        n3 = this.getChildCount();
        for (n2 = 0; n2 < n3; ++n2) {
            view = this.getChildAt(n2);
            if (view.getVisibility() == 8 || (layoutParams = (LayoutParams)view.getLayoutParams()) != null && layoutParams.isDecor) continue;
            view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n * layoutParams.widthFactor)), (int)1073741824), this.mChildHeightMeasureSpec);
        }
    }

    @CallSuper
    protected void onPageScrolled(int n, float f, int n2) {
        View view;
        int n3;
        if (this.mDecorChildCount > 0) {
            int n4 = this.getScrollX();
            n3 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = this.getWidth();
            int n7 = this.getChildCount();
            for (int i = 0; i < n7; ++i) {
                view = this.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (!layoutParams.isDecor) continue;
                int n8 = layoutParams.gravity & 7;
                if (n8 != 1) {
                    if (n8 != 3) {
                        if (n8 != 5) {
                            n8 = n3;
                        } else {
                            n8 = n6 - n5 - view.getMeasuredWidth();
                            n5 += view.getMeasuredWidth();
                        }
                    } else {
                        n8 = n3;
                        n3 += view.getWidth();
                    }
                } else {
                    n8 = Math.max((n6 - view.getMeasuredWidth()) / 2, n3);
                }
                n8 = n8 + n4 - view.getLeft();
                if (n8 == 0) continue;
                view.offsetLeftAndRight(n8);
            }
        }
        this.dispatchOnPageScrolled(n, f, n2);
        if (this.mPageTransformer != null) {
            n2 = this.getScrollX();
            n3 = this.getChildCount();
            for (n = 0; n < n3; ++n) {
                view = this.getChildAt(n);
                if (((LayoutParams)view.getLayoutParams()).isDecor) continue;
                f = (float)(view.getLeft() - n2) / (float)this.getClientWidth();
                this.mPageTransformer.transformPage(view, f);
            }
        }
        this.mCalledSuper = true;
    }

    protected boolean onRequestFocusInDescendants(int n, Rect rect) {
        int n2;
        int n3;
        int n4 = this.getChildCount();
        if ((n & 2) != 0) {
            n3 = 0;
            n2 = 1;
        } else {
            n3 = n4 - 1;
            n2 = -1;
            n4 = -1;
        }
        while (n3 != n4) {
            ItemInfo itemInfo;
            View view = this.getChildAt(n3);
            if (view.getVisibility() == 0 && (itemInfo = this.infoForChild(view)) != null && itemInfo.position == this.mCurItem && view.requestFocus(n, rect)) {
                return true;
            }
            n3 += n2;
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(parcelable.adapterState, parcelable.loader);
            this.setCurrentItemInternal(parcelable.position, false, true);
            return;
        }
        this.mRestoredCurItem = parcelable.position;
        this.mRestoredAdapterState = parcelable.adapterState;
        this.mRestoredClassLoader = parcelable.loader;
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
            return savedState;
        }
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            n2 = this.mPageMargin;
            this.recomputeScrollPosition(n, n3, n2, n2);
            return;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        Object object = this.mAdapter;
        if (object != null) {
            if (object.getCount() == 0) {
                return false;
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            int n = motionEvent.getAction();
            boolean bl = false;
            switch (n & 255) {
                default: {
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                    break;
                }
                case 5: {
                    n = motionEvent.getActionIndex();
                    this.mLastMotionX = motionEvent.getX(n);
                    this.mActivePointerId = motionEvent.getPointerId(n);
                    break;
                }
                case 3: {
                    if (!this.mIsBeingDragged) break;
                    this.scrollToItem(this.mCurItem, true, 0, false);
                    bl = this.resetTouch();
                    break;
                }
                case 2: {
                    if (!this.mIsBeingDragged) {
                        n = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (n == -1) {
                            bl = this.resetTouch();
                            break;
                        }
                        float f = motionEvent.getX(n);
                        float f2 = Math.abs(f - this.mLastMotionX);
                        float f3 = motionEvent.getY(n);
                        float f4 = Math.abs(f3 - this.mLastMotionY);
                        if (f2 > (float)this.mTouchSlop && f2 > f4) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            f2 = this.mInitialMotionX;
                            f = f - f2 > 0.0f ? f2 + (float)this.mTouchSlop : f2 - (float)this.mTouchSlop;
                            this.mLastMotionX = f;
                            this.mLastMotionY = f3;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            object = this.getParent();
                            if (object != null) {
                                object.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                    if (!this.mIsBeingDragged) break;
                    bl = false | this.performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                    break;
                }
                case 1: {
                    if (!this.mIsBeingDragged) break;
                    object = this.mVelocityTracker;
                    object.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    n = (int)object.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    int n2 = this.getClientWidth();
                    int n3 = this.getScrollX();
                    object = this.infoForCurrentScrollPosition();
                    float f = (float)this.mPageMargin / (float)n2;
                    this.setCurrentItemInternal(this.determineTargetPage(object.position, ((float)n3 / (float)n2 - object.offset) / (object.widthFactor + f), n, (int)(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, n);
                    bl = this.resetTouch();
                    break;
                }
                case 0: {
                    float f;
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mInitialMotionX = f = motionEvent.getX();
                    this.mLastMotionX = f;
                    this.mInitialMotionY = f = motionEvent.getY();
                    this.mLastMotionY = f;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                }
            }
            if (bl) {
                ViewCompat.postInvalidateOnAnimation((View)this);
                return true;
            }
            return true;
        }
        return false;
    }

    boolean pageLeft() {
        int n = this.mCurItem;
        if (n > 0) {
            this.setCurrentItem(n - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int n) {
        String string2;
        Object object;
        int n2 = this.mCurItem;
        if (n2 != n) {
            object = this.infoForPosition(n2);
            this.mCurItem = n;
        } else {
            object = null;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.getWindowToken() == null) {
            return;
        }
        this.mAdapter.startUpdate(this);
        int n3 = this.mOffscreenPageLimit;
        int n4 = Math.max(0, this.mCurItem - n3);
        int n5 = this.mAdapter.getCount();
        int n6 = Math.min(n5 - 1, this.mCurItem + n3);
        if (n5 == this.mExpectedAdapterCount) {
            Object object2;
            ItemInfo itemInfo = null;
            n = 0;
            do {
                object2 = itemInfo;
                if (n >= this.mItems.size()) break;
                object2 = this.mItems.get(n);
                if (object2.position >= this.mCurItem) {
                    if (object2.position == this.mCurItem) break;
                    object2 = itemInfo;
                    break;
                }
                ++n;
            } while (true);
            itemInfo = object2 == null && n5 > 0 ? this.addNewItem(this.mCurItem, n) : object2;
            if (itemInfo != null) {
                int n7;
                float f;
                float f2 = 0.0f;
                n2 = n - 1;
                object2 = n2 >= 0 ? this.mItems.get(n2) : null;
                int n8 = this.getClientWidth();
                if (n8 <= 0) {
                    f = 0.0f;
                } else {
                    f = itemInfo.widthFactor;
                    f = (float)this.getPaddingLeft() / (float)n8 + (2.0f - f);
                }
                int n9 = n;
                for (n7 = this.mCurItem - 1; n7 >= 0; --n7) {
                    if (f2 >= f && n7 < n4) {
                        if (object2 == null) break;
                        if (n7 == object2.position && !object2.scrolling) {
                            this.mItems.remove(n2);
                            this.mAdapter.destroyItem(this, n7, object2.object);
                            n = n9 - 1;
                            object2 = --n2 >= 0 ? this.mItems.get(n2) : null;
                        } else {
                            n = n9;
                        }
                    } else if (object2 != null && n7 == object2.position) {
                        f2 += object2.widthFactor;
                        object2 = --n2 >= 0 ? this.mItems.get(n2) : null;
                        n = n9;
                    } else {
                        f2 += this.addNewItem((int)n7, (int)(n2 + 1)).widthFactor;
                        n = n9 + 1;
                        object2 = n2 >= 0 ? this.mItems.get(n2) : null;
                    }
                    n9 = n;
                }
                f2 = itemInfo.widthFactor;
                n = n9 + 1;
                if (f2 < 2.0f) {
                    object2 = n < this.mItems.size() ? this.mItems.get(n) : null;
                    f = n8 <= 0 ? 0.0f : (float)this.getPaddingRight() / (float)n8 + 2.0f;
                    n2 = n4;
                    for (n7 = this.mCurItem + 1; n7 < n5; ++n7) {
                        if (f2 >= f && n7 > n6) {
                            if (object2 == null) break;
                            if (n7 != object2.position || object2.scrolling) continue;
                            this.mItems.remove(n);
                            this.mAdapter.destroyItem(this, n7, object2.object);
                            if (n < this.mItems.size()) {
                                object2 = this.mItems.get(n);
                                continue;
                            }
                            object2 = null;
                            continue;
                        }
                        if (object2 != null && n7 == object2.position) {
                            f2 += object2.widthFactor;
                            if (++n < this.mItems.size()) {
                                object2 = this.mItems.get(n);
                                continue;
                            }
                            object2 = null;
                            continue;
                        }
                        object2 = this.addNewItem(n7, n);
                        f2 += object2.widthFactor;
                        object2 = ++n < this.mItems.size() ? this.mItems.get(n) : null;
                    }
                }
                this.calculatePageOffsets(itemInfo, n9, (ItemInfo)object);
            }
            object = this.mAdapter;
            n = this.mCurItem;
            object2 = itemInfo != null ? itemInfo.object : null;
            object.setPrimaryItem(this, n, object2);
            this.mAdapter.finishUpdate(this);
            n2 = this.getChildCount();
            for (n = 0; n < n2; ++n) {
                object = this.getChildAt(n);
                object2 = (LayoutParams)object.getLayoutParams();
                object2.childIndex = n;
                if (object2.isDecor || object2.widthFactor != 0.0f || (object = this.infoForChild((View)object)) == null) continue;
                object2.widthFactor = object.widthFactor;
                object2.position = object.position;
            }
            this.sortChildDrawingOrder();
            if (this.hasFocus()) {
                object2 = this.findFocus();
                object2 = object2 != null ? this.infoForAnyChild((View)object2) : null;
                if (object2 != null && object2.position == this.mCurItem) {
                    return;
                }
                for (n = 0; n < this.getChildCount(); ++n) {
                    object2 = this.getChildAt(n);
                    object = this.infoForChild((View)object2);
                    if (object == null || object.position != this.mCurItem || !object2.requestFocus(2)) continue;
                    return;
                }
                return;
            }
            return;
        }
        try {
            string2 = this.getResources().getResourceName(this.getId());
        }
        catch (Resources.NotFoundException notFoundException) {
            string2 = Integer.toHexString(this.getId());
        }
        object = new StringBuilder();
        object.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
        object.append(this.mExpectedAdapterCount);
        object.append(", found: ");
        object.append(n5);
        object.append(" Pager id: ");
        object.append(string2);
        object.append(" Pager class: ");
        object.append(this.getClass());
        object.append(" Problematic adapter: ");
        object.append(this.mAdapter.getClass());
        throw new IllegalStateException(object.toString());
    }

    public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener onAdapterChangeListener) {
        List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
        if (list != null) {
            list.remove(onAdapterChangeListener);
            return;
        }
    }

    public void removeOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.remove(onPageChangeListener);
            return;
        }
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
            return;
        }
        super.removeView(view);
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        List<OnAdapterChangeListener> list;
        int n;
        Object object = this.mAdapter;
        if (object != null) {
            object.setViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (n = 0; n < this.mItems.size(); ++n) {
                object = this.mItems.get(n);
                this.mAdapter.destroyItem(this, object.position, object.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }
        object = this.mAdapter;
        this.mAdapter = pagerAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean bl = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!bl) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }
        if ((list = this.mAdapterChangeListeners) != null && !list.isEmpty()) {
            int n2 = this.mAdapterChangeListeners.size();
            for (n = 0; n < n2; ++n) {
                this.mAdapterChangeListeners.get(n).onAdapterChanged(this, (PagerAdapter)object, pagerAdapter);
            }
            return;
        }
    }

    public void setCurrentItem(int n) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, this.mFirstLayout ^ true, false);
    }

    public void setCurrentItem(int n, boolean bl) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, bl, false);
    }

    void setCurrentItemInternal(int n, boolean bl, boolean bl2) {
        this.setCurrentItemInternal(n, bl, bl2, 0);
    }

    void setCurrentItemInternal(int n, boolean bl, boolean bl2, int n2) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null && pagerAdapter.getCount() > 0) {
            if (!bl2 && this.mCurItem == n && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
                return;
            }
            bl2 = true;
            if (n < 0) {
                n = 0;
            } else if (n >= this.mAdapter.getCount()) {
                n = this.mAdapter.getCount() - 1;
            }
            int n3 = this.mOffscreenPageLimit;
            int n4 = this.mCurItem;
            if (n > n4 + n3 || n < n4 - n3) {
                for (n3 = 0; n3 < this.mItems.size(); ++n3) {
                    this.mItems.get((int)n3).scrolling = true;
                }
            }
            if (this.mCurItem == n) {
                bl2 = false;
            }
            if (this.mFirstLayout) {
                this.mCurItem = n;
                if (bl2) {
                    this.dispatchOnPageSelected(n);
                }
                this.requestLayout();
                return;
            }
            this.populate(n);
            this.scrollToItem(n, bl, n2, bl2);
            return;
        }
        this.setScrollingCacheEnabled(false);
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void setOffscreenPageLimit(int n) {
        if (n < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(n);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w((String)"ViewPager", (String)stringBuilder.toString());
            n = 1;
        }
        if (n != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = n;
            this.populate();
            return;
        }
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageMargin(int n) {
        int n2 = this.mPageMargin;
        this.mPageMargin = n;
        int n3 = this.getWidth();
        this.recomputeScrollPosition(n3, n3, n, n2);
        this.requestLayout();
    }

    public void setPageMarginDrawable(@DrawableRes int n) {
        this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setPageMarginDrawable(Drawable drawable2) {
        this.mMarginDrawable = drawable2;
        if (drawable2 != null) {
            this.refreshDrawableState();
        }
        boolean bl = drawable2 == null;
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    public void setPageTransformer(boolean bl, PageTransformer pageTransformer) {
        this.setPageTransformer(bl, pageTransformer, 2);
    }

    public void setPageTransformer(boolean bl, PageTransformer pageTransformer, int n) {
        int n2 = 1;
        boolean bl2 = pageTransformer != null;
        boolean bl3 = this.mPageTransformer != null;
        boolean bl4 = bl2 != bl3;
        this.mPageTransformer = pageTransformer;
        this.setChildrenDrawingOrderEnabled(bl2);
        if (bl2) {
            if (bl) {
                n2 = 2;
            }
            this.mDrawingOrder = n2;
            this.mPageTransformerLayerType = n;
        } else {
            this.mDrawingOrder = 0;
        }
        if (bl4) {
            this.populate();
        }
    }

    void setScrollState(int n) {
        if (this.mScrollState == n) {
            return;
        }
        this.mScrollState = n;
        if (this.mPageTransformer != null) {
            boolean bl = n != 0;
            this.enableLayers(bl);
        }
        this.dispatchOnScrollStateChanged(n);
    }

    void smoothScrollTo(int n, int n2) {
        this.smoothScrollTo(n, n2, 0);
    }

    void smoothScrollTo(int n, int n2, int n3) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        Scroller scroller = this.mScroller;
        int n4 = scroller != null && !scroller.isFinished() ? 1 : 0;
        if (n4 != 0) {
            n4 = this.mIsScrollStarted ? this.mScroller.getCurrX() : this.mScroller.getStartX();
            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
        } else {
            n4 = this.getScrollX();
        }
        int n5 = this.getScrollY();
        int n6 = n - n4;
        if (n6 == 0 && (n2 -= n5) == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        n = this.getClientWidth();
        int n7 = n / 2;
        float f = Math.min(1.0f, (float)Math.abs(n6) * 1.0f / (float)n);
        float f2 = n7;
        float f3 = n7;
        f = this.distanceInfluenceForSnapDuration(f);
        n3 = Math.abs(n3);
        if (n3 > 0) {
            n = Math.round(Math.abs((f2 + f3 * f) / (float)n3) * 1000.0f) * 4;
        } else {
            f2 = n;
            f3 = this.mAdapter.getPageWidth(this.mCurItem);
            n = (int)((1.0f + (float)Math.abs(n6) / ((float)this.mPageMargin + f2 * f3)) * 100.0f);
        }
        n = Math.min(n, 600);
        this.mIsScrollStarted = false;
        this.mScroller.startScroll(n4, n5, n6, n2, n);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (!super.verifyDrawable(drawable2) && drawable2 != this.mMarginDrawable) {
            return false;
        }
        return true;
    }

    @Inherited
    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface DecorView {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams
    extends ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, ViewPager.LAYOUT_ATTRS);
            this.gravity = context.getInteger(0, 48);
            context.recycle();
        }
    }

    class MyAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        private boolean canScroll() {
            if (ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1) {
                return true;
            }
            return false;
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ViewPager.class.getName());
            accessibilityEvent.setScrollable(this.canScroll());
            if (accessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                accessibilityEvent.setItemCount(ViewPager.this.mAdapter.getCount());
                accessibilityEvent.setFromIndex(ViewPager.this.mCurItem);
                accessibilityEvent.setToIndex(ViewPager.this.mCurItem);
                return;
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
            accessibilityNodeInfoCompat.setScrollable(this.canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                return;
            }
        }

        @Override
        public boolean performAccessibilityAction(View object, int n, Bundle bundle) {
            if (super.performAccessibilityAction((View)object, n, bundle)) {
                return true;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                if (ViewPager.this.canScrollHorizontally(-1)) {
                    object = ViewPager.this;
                    object.setCurrentItem(object.mCurItem - 1);
                    return true;
                }
                return false;
            }
            if (ViewPager.this.canScrollHorizontally(1)) {
                object = ViewPager.this;
                object.setCurrentItem(object.mCurItem + 1);
                return true;
            }
            return false;
        }
    }

    public static interface OnAdapterChangeListener {
        public void onAdapterChanged(@NonNull ViewPager var1, @Nullable PagerAdapter var2, @Nullable PagerAdapter var3);
    }

    public static interface OnPageChangeListener {
        public void onPageScrollStateChanged(int var1);

        public void onPageScrolled(int var1, float var2, int var3);

        public void onPageSelected(int var1);
    }

    public static interface PageTransformer {
        public void transformPage(View var1, float var2);
    }

    private class PagerObserver
    extends DataSetObserver {
        PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                classLoader = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader);
            this.loader = classLoader;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FragmentPager.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }

    }

    public static class SimpleOnPageChangeListener
    implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int n) {
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
        }

        @Override
        public void onPageSelected(int n) {
        }
    }

    static class ViewPositionComparator
    implements Comparator<View> {
        ViewPositionComparator() {
        }

        @Override
        public int compare(View object, View object2) {
            object = (LayoutParams)object.getLayoutParams();
            object2 = (LayoutParams)object2.getLayoutParams();
            if (object.isDecor != object2.isDecor) {
                if (object.isDecor) {
                    return 1;
                }
                return -1;
            }
            return object.position - object2.position;
        }
    }

}

