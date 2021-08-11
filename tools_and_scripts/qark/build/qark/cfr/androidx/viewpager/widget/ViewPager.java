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
package androidx.viewpager.widget;

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
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.viewpager.widget.PagerAdapter;
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
        float f;
        int n2;
        int n3;
        float f2;
        int n4 = this.mAdapter.getCount();
        int n5 = this.getClientWidth();
        float f3 = n5 > 0 ? (float)this.mPageMargin / (float)n5 : 0.0f;
        if (itemInfo2 != null) {
            n5 = itemInfo2.position;
            if (n5 < itemInfo.position) {
                n2 = 0;
                f = itemInfo2.offset + itemInfo2.widthFactor + f3;
                ++n5;
                while (n5 <= itemInfo.position && n2 < this.mItems.size()) {
                    itemInfo2 = this.mItems.get(n2);
                    do {
                        f2 = f;
                        n3 = n5;
                        if (n5 <= itemInfo2.position) break;
                        f2 = f;
                        n3 = n5;
                        if (n2 >= this.mItems.size() - 1) break;
                        itemInfo2 = this.mItems.get(++n2);
                    } while (true);
                    while (n3 < itemInfo2.position) {
                        f2 += this.mAdapter.getPageWidth(n3) + f3;
                        ++n3;
                    }
                    itemInfo2.offset = f2;
                    f = f2 + (itemInfo2.widthFactor + f3);
                    n5 = n3 + 1;
                }
            } else if (n5 > itemInfo.position) {
                n2 = this.mItems.size() - 1;
                f = itemInfo2.offset;
                --n5;
                while (n5 >= itemInfo.position && n2 >= 0) {
                    itemInfo2 = this.mItems.get(n2);
                    do {
                        f2 = f;
                        n3 = n5;
                        if (n5 >= itemInfo2.position) break;
                        f2 = f;
                        n3 = n5;
                        if (n2 <= 0) break;
                        itemInfo2 = this.mItems.get(--n2);
                    } while (true);
                    while (n3 > itemInfo2.position) {
                        f2 -= this.mAdapter.getPageWidth(n3) + f3;
                        --n3;
                    }
                    itemInfo2.offset = f = f2 - (itemInfo2.widthFactor + f3);
                    n5 = n3 - 1;
                }
            }
        }
        n3 = this.mItems.size();
        f2 = itemInfo.offset;
        n5 = itemInfo.position - 1;
        f = itemInfo.position == 0 ? itemInfo.offset : -3.4028235E38f;
        this.mFirstOffset = f;
        f = itemInfo.position == n4 - 1 ? itemInfo.offset + itemInfo.widthFactor - 1.0f : Float.MAX_VALUE;
        this.mLastOffset = f;
        n2 = n - 1;
        f = f2;
        while (n2 >= 0) {
            itemInfo2 = this.mItems.get(n2);
            while (n5 > itemInfo2.position) {
                f -= this.mAdapter.getPageWidth(n5) + f3;
                --n5;
            }
            itemInfo2.offset = f -= itemInfo2.widthFactor + f3;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = f;
            }
            --n2;
            --n5;
        }
        f = itemInfo.offset + itemInfo.widthFactor + f3;
        n2 = itemInfo.position + 1;
        n5 = n + 1;
        n = n2;
        while (n5 < n3) {
            itemInfo = this.mItems.get(n5);
            while (n < itemInfo.position) {
                f += this.mAdapter.getPageWidth(n) + f3;
                ++n;
            }
            if (itemInfo.position == n4 - 1) {
                this.mLastOffset = itemInfo.widthFactor + f - 1.0f;
            }
            itemInfo.offset = f;
            f += itemInfo.widthFactor + f3;
            ++n5;
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
        }
    }

    private int determineTargetPage(int n, float f, int n2, int n3) {
        if (Math.abs(n3) > this.mFlingDistance && Math.abs(n2) > this.mMinimumVelocity) {
            if (n2 <= 0) {
                ++n;
            }
        } else {
            float f2 = n >= this.mCurItem ? 0.4f : 0.6f;
            n = (int)(f + f2) + n;
        }
        n2 = n;
        if (this.mItems.size() > 0) {
            ItemInfo itemInfo = this.mItems.get(0);
            Object object = this.mItems;
            object = object.get(object.size() - 1);
            n2 = Math.max(itemInfo.position, Math.min(n, object.position));
        }
        return n2;
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
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        if (view == null) {
            rect2.set(0, 0, 0, 0);
            return rect2;
        }
        rect2.left = view.getLeft();
        rect2.right = view.getRight();
        rect2.top = view.getTop();
        rect2.bottom = view.getBottom();
        for (rect = view.getParent(); rect instanceof ViewGroup && rect != this; rect = rect.getParent()) {
            rect = (ViewGroup)rect;
            rect2.left += rect.getLeft();
            rect2.right += rect.getRight();
            rect2.top += rect.getTop();
            rect2.bottom += rect.getBottom();
        }
        return rect2;
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
        n = 0;
        while (n < this.mItems.size()) {
            ItemInfo itemInfo2 = this.mItems.get(n);
            int n3 = n;
            ItemInfo itemInfo3 = itemInfo2;
            if (!bl) {
                n3 = n;
                itemInfo3 = itemInfo2;
                if (itemInfo2.position != n2 + 1) {
                    itemInfo3 = this.mTempItem;
                    itemInfo3.offset = f3 + f4 + f;
                    itemInfo3.position = n2 + 1;
                    itemInfo3.widthFactor = this.mAdapter.getPageWidth(itemInfo3.position);
                    n3 = n - 1;
                }
            }
            f3 = itemInfo3.offset;
            f4 = itemInfo3.widthFactor;
            if (!bl && f2 < f3) {
                return itemInfo;
            }
            if (f2 >= f4 + f3 + f) {
                if (n3 == this.mItems.size() - 1) {
                    return itemInfo3;
                }
                bl = false;
                n2 = itemInfo3.position;
                f4 = itemInfo3.widthFactor;
                n = n3 + 1;
                itemInfo = itemInfo3;
                continue;
            }
            return itemInfo3;
        }
        return itemInfo;
    }

    private static boolean isDecorView(View view) {
        if (view.getClass().getAnnotation(DecorView.class) != null) {
            return true;
        }
        return false;
    }

    private boolean isGutterDrag(float f, float f2) {
        if (f < (float)this.mGutterSize && f2 > 0.0f || f > (float)(this.getWidth() - this.mGutterSize) && f2 < 0.0f) {
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
            }
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
        } else {
            bl3 = bl2;
            f = f3;
            if (f3 > f2) {
                bl3 = bl;
                if (bl5) {
                    this.mRightEdge.onPull(Math.abs(f3 - f2) / (float)n);
                    bl3 = true;
                }
                f = f2;
            }
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
        }
    }

    private void removeNonDecorViews() {
        int n = 0;
        while (n < this.getChildCount()) {
            int n2 = n;
            if (!((LayoutParams)this.getChildAt((int)n).getLayoutParams()).isDecor) {
                this.removeViewAt(n);
                n2 = n - 1;
            }
            n = n2 + 1;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean bl) {
        ViewParent viewParent = this.getParent();
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(bl);
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
        } else {
            if (bl2) {
                this.dispatchOnPageSelected(n);
            }
            this.completeScroll(false);
            this.scrollTo(n3, 0);
            this.pageScrolled(n3);
        }
    }

    private void setScrollingCacheEnabled(boolean bl) {
        if (this.mScrollingCacheEnabled != bl) {
            this.mScrollingCacheEnabled = bl;
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
        if (n4 != 262144 || n3 == arrayList.size()) {
            if (!this.isFocusable()) {
                return;
            }
            if ((n2 & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }
            if (arrayList != null) {
                arrayList.add((View)this);
            }
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

    public void addOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
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
        ViewGroup.LayoutParams layoutParams2 = layoutParams;
        if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        layoutParams = (LayoutParams)layoutParams2;
        layoutParams.isDecor |= ViewPager.isDecorView(view);
        if (this.mInLayout) {
            if (layoutParams != null && layoutParams.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams.needsMeasure = true;
            this.addViewInLayout(view, n, layoutParams2);
            return;
        }
        super.addView(view, n, layoutParams2);
    }

    public boolean arrowScroll(int n) {
        boolean bl;
        block14 : {
            block15 : {
                block16 : {
                    boolean bl2;
                    block13 : {
                        Object object;
                        int n2;
                        int n3;
                        View view = this.findFocus();
                        if (view == this) {
                            object = null;
                        } else {
                            object = view;
                            if (view != null) {
                                n3 = 0;
                                object = view.getParent();
                                do {
                                    n2 = n3;
                                    if (!(object instanceof ViewGroup)) break;
                                    if (object == this) {
                                        n2 = 1;
                                        break;
                                    }
                                    object = object.getParent();
                                } while (true);
                                object = view;
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
                                }
                            }
                        }
                        bl2 = false;
                        bl = false;
                        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)object, n);
                        if (view == null || view == object) break block13;
                        if (n == 17) {
                            n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                            n3 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                            bl = object != null && n2 >= n3 ? this.pageLeft() : view.requestFocus();
                        } else if (n == 66) {
                            n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                            n3 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                            bl = object != null && n2 <= n3 ? this.pageRight() : view.requestFocus();
                        }
                        break block14;
                    }
                    if (n == 17 || n == 1) break block15;
                    if (n == 66) break block16;
                    bl = bl2;
                    if (n != 2) break block14;
                }
                bl = this.pageRight();
                break block14;
            }
            bl = this.pageLeft();
        }
        if (bl) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection((int)n));
        }
        return bl;
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
        int n5 = 0;
        while (n5 < this.mItems.size()) {
            int n6;
            int n7;
            int n8;
            object = this.mItems.get(n5);
            int n9 = this.mAdapter.getItemPosition(object.object);
            if (n9 == -1) {
                n7 = n3;
                n8 = n4;
                n6 = n5;
            } else if (n9 == -2) {
                this.mItems.remove(n5);
                n9 = n5 - 1;
                n5 = n4;
                if (n4 == 0) {
                    this.mAdapter.startUpdate(this);
                    n5 = 1;
                }
                this.mAdapter.destroyItem(this, object.position, object.object);
                n2 = 1;
                n7 = n3;
                n8 = n5;
                n6 = n9;
                if (this.mCurItem == object.position) {
                    n7 = Math.max(0, Math.min(this.mCurItem, n - 1));
                    n2 = 1;
                    n8 = n5;
                    n6 = n9;
                }
            } else {
                n7 = n3;
                n8 = n4;
                n6 = n5;
                if (object.position != n9) {
                    if (object.position == this.mCurItem) {
                        n3 = n9;
                    }
                    object.position = n9;
                    n2 = 1;
                    n6 = n5;
                    n8 = n4;
                    n7 = n3;
                }
            }
            n5 = n6 + 1;
            n3 = n7;
            n4 = n8;
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
            n = n2;
            if (!this.mRightEdge.isFinished()) {
                n3 = canvas.save();
                n = this.getWidth();
                int n4 = this.getHeight();
                int n5 = this.getPaddingTop();
                int n6 = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(- this.getPaddingTop()), (- this.mLastOffset + 1.0f) * (float)n);
                this.mRightEdge.setSize(n4 - n5 - n6, n);
                n = n2 | this.mRightEdge.draw(canvas);
                canvas.restoreToCount(n3);
            }
        }
        if (n != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.mMarginDrawable;
        if (drawable2 != null && drawable2.isStateful()) {
            drawable2.setState(this.getDrawableState());
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        boolean bl;
        boolean bl2 = bl = false;
        if (keyEvent.getAction() != 0) return bl2;
        int n = keyEvent.getKeyCode();
        if (n != 21) {
            if (n != 22) {
                if (n != 61) {
                    return false;
                }
                if (keyEvent.hasNoModifiers()) {
                    return this.arrowScroll(2);
                }
                bl2 = bl;
                if (!keyEvent.hasModifiers(1)) return bl2;
                return this.arrowScroll(1);
            }
            if (!keyEvent.hasModifiers(2)) return this.arrowScroll(66);
            return this.pageRight();
        }
        if (!keyEvent.hasModifiers(2)) return this.arrowScroll(17);
        return this.pageLeft();
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
                f = f2;
                if (f2 > f3) {
                    f = f3;
                }
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
        int n5;
        int n6;
        LayoutParams layoutParams;
        int n7;
        View view;
        int n8 = this.getChildCount();
        int n9 = n3 - n;
        int n10 = n4 - n2;
        n = this.getPaddingLeft();
        n2 = this.getPaddingTop();
        int n11 = this.getPaddingRight();
        n4 = this.getPaddingBottom();
        int n12 = this.getScrollX();
        int n13 = 0;
        for (n5 = 0; n5 < n8; ++n5) {
            view = this.getChildAt(n5);
            n3 = n;
            n7 = n2;
            n6 = n11;
            int n14 = n4;
            int n15 = n13;
            if (view.getVisibility() != 8) {
                layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams.isDecor) {
                    n3 = layoutParams.gravity & 7;
                    n6 = layoutParams.gravity & 112;
                    if (n3 != 1) {
                        if (n3 != 3) {
                            if (n3 != 5) {
                                n3 = n;
                                n7 = n;
                            } else {
                                n3 = n9 - n11 - view.getMeasuredWidth();
                                n11 += view.getMeasuredWidth();
                                n7 = n;
                            }
                        } else {
                            n3 = n;
                            n7 = n + view.getMeasuredWidth();
                        }
                    } else {
                        n3 = Math.max((n9 - view.getMeasuredWidth()) / 2, n);
                        n7 = n;
                    }
                    if (n6 != 16) {
                        if (n6 != 48) {
                            if (n6 != 80) {
                                n = n2;
                            } else {
                                n = n10 - n4 - view.getMeasuredHeight();
                                n4 += view.getMeasuredHeight();
                            }
                        } else {
                            n = n2;
                            n2 += view.getMeasuredHeight();
                        }
                    } else {
                        n = Math.max((n10 - view.getMeasuredHeight()) / 2, n2);
                    }
                    view.layout(n3, n, view.getMeasuredWidth() + (n3 += n12), n + view.getMeasuredHeight());
                    n15 = n13 + 1;
                    n3 = n7;
                    n7 = n2;
                    n6 = n11;
                    n14 = n4;
                } else {
                    n15 = n13;
                    n14 = n4;
                    n6 = n11;
                    n7 = n2;
                    n3 = n;
                }
            }
            n = n3;
            n2 = n7;
            n11 = n6;
            n4 = n14;
            n13 = n15;
        }
        n5 = n9 - n - n11;
        n3 = n9;
        n11 = n8;
        for (n7 = 0; n7 < n11; ++n7) {
            ItemInfo itemInfo;
            view = this.getChildAt(n7);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isDecor || (itemInfo = this.infoForChild(view)) == null) continue;
            n6 = n + (int)((float)n5 * itemInfo.offset);
            if (layoutParams.needsMeasure) {
                layoutParams.needsMeasure = false;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n5 * layoutParams.widthFactor)), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n10 - n2 - n4), (int)1073741824));
            }
            view.layout(n6, n2, view.getMeasuredWidth() + n6, view.getMeasuredHeight() + n2);
        }
        this.mTopPageBounds = n2;
        this.mBottomPageBounds = n10 - n4;
        this.mDecorChildCount = n13;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        View view;
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
            if (view.getVisibility() != 8) {
                layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    int n8;
                    n3 = layoutParams.gravity & 7;
                    int n9 = layoutParams.gravity & 112;
                    int n10 = Integer.MIN_VALUE;
                    n7 = Integer.MIN_VALUE;
                    n9 = n9 != 48 && n9 != 80 ? 0 : 1;
                    boolean bl = n3 == 3 || n3 == 5;
                    if (n9 != 0) {
                        n3 = 1073741824;
                    } else {
                        n3 = n10;
                        if (bl) {
                            n7 = 1073741824;
                            n3 = n10;
                        }
                    }
                    if (layoutParams.width != -2) {
                        n8 = 1073741824;
                        n3 = layoutParams.width != -1 ? layoutParams.width : n;
                    } else {
                        n10 = n;
                        n8 = n3;
                        n3 = n10;
                    }
                    if (layoutParams.height != -2) {
                        if (layoutParams.height != -1) {
                            n10 = layoutParams.height;
                            n7 = 1073741824;
                        } else {
                            n7 = 1073741824;
                            n10 = n2;
                        }
                    } else {
                        n10 = n2;
                    }
                    view.measure(View.MeasureSpec.makeMeasureSpec((int)n3, (int)n8), View.MeasureSpec.makeMeasureSpec((int)n10, (int)n7));
                    if (n9 != 0) {
                        n7 = n2 - view.getMeasuredHeight();
                        n3 = n;
                    } else {
                        n3 = n;
                        n7 = n2;
                        if (bl) {
                            n3 = n - view.getMeasuredWidth();
                            n7 = n2;
                        }
                    }
                } else {
                    n3 = n;
                    n7 = n2;
                }
            } else {
                n7 = n2;
                n3 = n;
            }
            n = n3;
            n2 = n7;
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

    protected void onPageScrolled(int n, float f, int n2) {
        int n3;
        View view;
        if (this.mDecorChildCount > 0) {
            int n4 = this.getScrollX();
            n3 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = this.getWidth();
            int n7 = this.getChildCount();
            for (int i = 0; i < n7; ++i) {
                int n8;
                int n9;
                view = this.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (!layoutParams.isDecor) {
                    n9 = n3;
                    n8 = n5;
                } else {
                    n9 = layoutParams.gravity & 7;
                    if (n9 != 1) {
                        if (n9 != 3) {
                            if (n9 != 5) {
                                n9 = n3;
                            } else {
                                n9 = n6 - n5 - view.getMeasuredWidth();
                                n5 += view.getMeasuredWidth();
                            }
                        } else {
                            n9 = n3;
                            n3 += view.getWidth();
                        }
                    } else {
                        n9 = Math.max((n6 - view.getMeasuredWidth()) / 2, n3);
                    }
                    int n10 = n9 + n4 - view.getLeft();
                    n9 = n3;
                    n8 = n5;
                    if (n10 != 0) {
                        view.offsetLeftAndRight(n10);
                        n8 = n5;
                        n9 = n3;
                    }
                }
                n3 = n9;
                n5 = n8;
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
        }
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            n2 = this.mPageMargin;
            this.recomputeScrollPosition(n, n3, n2, n2);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        block19 : {
            block16 : {
                block17 : {
                    block18 : {
                        if (this.mFakeDragging) {
                            return true;
                        }
                        if (var1_1.getAction() == 0 && var1_1.getEdgeFlags() != 0) {
                            return false;
                        }
                        var10_2 = this.mAdapter;
                        if (var10_2 == null) return false;
                        if (var10_2.getCount() == 0) {
                            return false;
                        }
                        if (this.mVelocityTracker == null) {
                            this.mVelocityTracker = VelocityTracker.obtain();
                        }
                        this.mVelocityTracker.addMovement(var1_1);
                        var6_3 = var1_1.getAction();
                        var9_4 = false;
                        if ((var6_3 &= 255) == 0) break block16;
                        if (var6_3 == 1) break block17;
                        if (var6_3 == 2) break block18;
                        if (var6_3 != 3) {
                            if (var6_3 != 5) {
                                if (var6_3 == 6) {
                                    this.onSecondaryPointerUp(var1_1);
                                    this.mLastMotionX = var1_1.getX(var1_1.findPointerIndex(this.mActivePointerId));
                                }
                            } else {
                                var6_3 = var1_1.getActionIndex();
                                this.mLastMotionX = var1_1.getX(var6_3);
                                this.mActivePointerId = var1_1.getPointerId(var6_3);
                            }
                        } else if (this.mIsBeingDragged) {
                            this.scrollToItem(this.mCurItem, true, 0, false);
                            var9_4 = this.resetTouch();
                        }
                        break block19;
                    }
                    if (this.mIsBeingDragged) ** GOTO lbl53
                    var6_3 = var1_1.findPointerIndex(this.mActivePointerId);
                    if (var6_3 == -1) {
                        var9_4 = this.resetTouch();
                    } else {
                        var2_5 = var1_1.getX(var6_3);
                        var4_8 = Math.abs(var2_5 - this.mLastMotionX);
                        var3_9 = var1_1.getY(var6_3);
                        var5_10 = Math.abs(var3_9 - this.mLastMotionY);
                        if (var4_8 > (float)this.mTouchSlop && var4_8 > var5_10) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            var4_8 = this.mInitialMotionX;
                            var2_5 = var2_5 - var4_8 > 0.0f ? var4_8 + (float)this.mTouchSlop : var4_8 - (float)this.mTouchSlop;
                            this.mLastMotionX = var2_5;
                            this.mLastMotionY = var3_9;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            var10_2 = this.getParent();
                            if (var10_2 != null) {
                                var10_2.requestDisallowInterceptTouchEvent(true);
                            }
                        }
lbl53: // 6 sources:
                        if (this.mIsBeingDragged) {
                            var9_4 = false | this.performDrag(var1_1.getX(var1_1.findPointerIndex(this.mActivePointerId)));
                        }
                    }
                    break block19;
                }
                if (this.mIsBeingDragged) {
                    var10_2 = this.mVelocityTracker;
                    var10_2.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    var6_3 = (int)var10_2.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    var7_11 = this.getClientWidth();
                    var8_12 = this.getScrollX();
                    var10_2 = this.infoForCurrentScrollPosition();
                    var2_6 = (float)this.mPageMargin / (float)var7_11;
                    this.setCurrentItemInternal(this.determineTargetPage(var10_2.position, ((float)var8_12 / (float)var7_11 - var10_2.offset) / (var10_2.widthFactor + var2_6), var6_3, (int)(var1_1.getX(var1_1.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, var6_3);
                    var9_4 = this.resetTouch();
                }
                break block19;
            }
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            this.populate();
            this.mInitialMotionX = var2_7 = var1_1.getX();
            this.mLastMotionX = var2_7;
            this.mInitialMotionY = var2_7 = var1_1.getY();
            this.mLastMotionY = var2_7;
            this.mActivePointerId = var1_1.getPointerId(0);
        }
        if (var9_4 == false) return true;
        ViewCompat.postInvalidateOnAnimation((View)this);
        return true;
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
            Object object3;
            ItemInfo itemInfo = null;
            n = 0;
            do {
                object3 = itemInfo;
                if (n >= this.mItems.size()) break;
                object2 = this.mItems.get(n);
                if (object2.position >= this.mCurItem) {
                    object3 = itemInfo;
                    if (object2.position != this.mCurItem) break;
                    object3 = object2;
                    break;
                }
                ++n;
            } while (true);
            itemInfo = object3;
            if (object3 == null) {
                itemInfo = object3;
                if (n5 > 0) {
                    itemInfo = this.addNewItem(this.mCurItem, n);
                }
            }
            if (itemInfo != null) {
                int n7;
                float f;
                float f2 = 0.0f;
                int n8 = n - 1;
                object3 = n8 >= 0 ? this.mItems.get(n8) : null;
                int n9 = this.getClientWidth();
                float f3 = n9 <= 0 ? 0.0f : 2.0f - itemInfo.widthFactor + (float)this.getPaddingLeft() / (float)n9;
                object2 = object3;
                int n10 = n;
                for (n7 = this.mCurItem - 1; n7 >= 0; --n7) {
                    if (f2 >= f3 && n7 < n4) {
                        if (object2 == null) break;
                        n = n10;
                        f = f2;
                        n2 = n8;
                        object3 = object2;
                        if (n7 == object2.position) {
                            n = n10;
                            f = f2;
                            n2 = n8;
                            object3 = object2;
                            if (!object2.scrolling) {
                                this.mItems.remove(n8);
                                this.mAdapter.destroyItem(this, n7, object2.object);
                                n2 = n8 - 1;
                                n = n10 - 1;
                                object3 = n2 >= 0 ? this.mItems.get(n2) : null;
                                f = f2;
                            }
                        }
                    } else if (object2 != null && n7 == object2.position) {
                        f = f2 + object2.widthFactor;
                        n2 = n8 - 1;
                        object3 = n2 >= 0 ? this.mItems.get(n2) : null;
                        n = n10;
                    } else {
                        f = f2 + this.addNewItem((int)n7, (int)(n8 + 1)).widthFactor;
                        n = n10 + 1;
                        object3 = n8 >= 0 ? this.mItems.get(n8) : null;
                        n2 = n8;
                    }
                    n10 = n;
                    f2 = f;
                    n8 = n2;
                    object2 = object3;
                }
                f = itemInfo.widthFactor;
                n = n10 + 1;
                if (f < 2.0f) {
                    object3 = n < this.mItems.size() ? this.mItems.get(n) : null;
                    f3 = n9 <= 0 ? 0.0f : (float)this.getPaddingRight() / (float)n9 + 2.0f;
                    n2 = n4;
                    n8 = n3;
                    for (n7 = this.mCurItem + 1; n7 < n5; ++n7) {
                        if (f >= f3 && n7 > n6) {
                            if (object3 == null) break;
                            if (n7 != object3.position || object3.scrolling) continue;
                            this.mItems.remove(n);
                            this.mAdapter.destroyItem(this, n7, object3.object);
                            if (n < this.mItems.size()) {
                                object3 = this.mItems.get(n);
                                continue;
                            }
                            object3 = null;
                            continue;
                        }
                        if (object3 != null && n7 == object3.position) {
                            f += object3.widthFactor;
                            if (++n < this.mItems.size()) {
                                object3 = this.mItems.get(n);
                                continue;
                            }
                            object3 = null;
                            continue;
                        }
                        object3 = this.addNewItem(n7, n);
                        f += object3.widthFactor;
                        object3 = ++n < this.mItems.size() ? this.mItems.get(n) : null;
                    }
                }
                this.calculatePageOffsets(itemInfo, n10, (ItemInfo)object);
                this.mAdapter.setPrimaryItem(this, this.mCurItem, itemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            n2 = this.getChildCount();
            for (n = 0; n < n2; ++n) {
                object = this.getChildAt(n);
                object3 = (LayoutParams)object.getLayoutParams();
                object3.childIndex = n;
                if (object3.isDecor || object3.widthFactor != 0.0f || (object = this.infoForChild((View)object)) == null) continue;
                object3.widthFactor = object.widthFactor;
                object3.position = object.position;
            }
            this.sortChildDrawingOrder();
            if (this.hasFocus()) {
                object3 = this.findFocus();
                object3 = object3 != null ? this.infoForAnyChild((View)object3) : null;
                if (object3 == null || object3.position != this.mCurItem) {
                    for (n = 0; n < this.getChildCount(); ++n) {
                        object3 = this.getChildAt(n);
                        object = this.infoForChild((View)object3);
                        if (object == null || object.position != this.mCurItem || !object3.requestFocus(2)) continue;
                        return;
                    }
                }
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

    public void removeOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
        if (list != null) {
            list.remove(onAdapterChangeListener);
        }
    }

    public void removeOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.remove(onPageChangeListener);
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
        int n;
        List<OnAdapterChangeListener> list;
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
        if (pagerAdapter != null) {
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
        boolean bl3 = false;
        if (pagerAdapter != null && pagerAdapter.getCount() > 0) {
            int n3;
            if (!bl2 && this.mCurItem == n && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
                return;
            }
            if (n < 0) {
                n3 = 0;
            } else {
                n3 = n;
                if (n >= this.mAdapter.getCount()) {
                    n3 = this.mAdapter.getCount() - 1;
                }
            }
            n = this.mOffscreenPageLimit;
            int n4 = this.mCurItem;
            if (n3 > n4 + n || n3 < n4 - n) {
                for (n = 0; n < this.mItems.size(); ++n) {
                    this.mItems.get((int)n).scrolling = true;
                }
            }
            bl2 = bl3;
            if (this.mCurItem != n3) {
                bl2 = true;
            }
            if (this.mFirstLayout) {
                this.mCurItem = n3;
                if (bl2) {
                    this.dispatchOnPageSelected(n3);
                }
                this.requestLayout();
                return;
            }
            this.populate(n3);
            this.scrollToItem(n3, bl, n2, bl2);
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
        int n2 = n;
        if (n < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(n);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w((String)"ViewPager", (String)stringBuilder.toString());
            n2 = 1;
        }
        if (n2 != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = n2;
            this.populate();
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

    public void setPageMarginDrawable(int n) {
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
        public void onAdapterChanged(ViewPager var1, PagerAdapter var2, PagerAdapter var3);
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
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader2);
            this.loader = classLoader2;
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

