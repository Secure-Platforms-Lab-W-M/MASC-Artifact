// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.content.res.TypedArray;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Inherited;
import java.lang.annotation.Annotation;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.database.DataSetObserver;
import android.content.res.Resources$NotFoundException;
import android.support.annotation.CallSuper;
import android.view.View$MeasureSpec;
import android.view.ViewConfiguration;
import android.graphics.Canvas;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.os.SystemClock;
import android.view.SoundEffectConstants;
import android.view.FocusFinder;
import android.util.Log;
import android.view.ViewGroup$LayoutParams;
import java.util.Collections;
import android.view.MotionEvent;
import android.support.annotation.NonNull;
import android.view.ViewParent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.widget.Scroller;
import android.os.Parcelable;
import android.graphics.drawable.Drawable;
import android.widget.EdgeEffect;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.view.animation.Interpolator;
import java.util.Comparator;
import android.view.ViewGroup;

public class ViewPager extends ViewGroup
{
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
    private int mActivePointerId;
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
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffect mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842931 };
        COMPARATOR = new Comparator<ItemInfo>() {
            @Override
            public int compare(final ItemInfo itemInfo, final ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }
    
    public ViewPager(final Context context) {
        super(context);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            @Override
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    public ViewPager(final Context context, final AttributeSet set) {
        super(context, set);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            @Override
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    private void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        final int count = this.mAdapter.getCount();
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.mPageMargin / (float)clientWidth;
        }
        else {
            n = 0.0f;
        }
        if (itemInfo2 != null) {
            final int position = itemInfo2.position;
            if (position < itemInfo.position) {
                int n2 = 0;
                float offset = itemInfo2.offset + itemInfo2.widthFactor + n;
                for (int j = position + 1; j <= itemInfo.position && n2 < this.mItems.size(); ++j) {
                    for (itemInfo2 = this.mItems.get(n2); j > itemInfo2.position && n2 < this.mItems.size() - 1; ++n2, itemInfo2 = this.mItems.get(n2)) {}
                    while (j < itemInfo2.position) {
                        offset += this.mAdapter.getPageWidth(j) + n;
                        ++j;
                    }
                    itemInfo2.offset = offset;
                    offset += itemInfo2.widthFactor + n;
                }
            }
            else if (position > itemInfo.position) {
                int n3 = this.mItems.size() - 1;
                float offset2 = itemInfo2.offset;
                for (int k = position - 1; k >= itemInfo.position && n3 >= 0; --k) {
                    for (itemInfo2 = this.mItems.get(n3); k < itemInfo2.position && n3 > 0; --n3, itemInfo2 = this.mItems.get(n3)) {}
                    while (k > itemInfo2.position) {
                        offset2 -= this.mAdapter.getPageWidth(k) + n;
                        --k;
                    }
                    offset2 -= itemInfo2.widthFactor + n;
                    itemInfo2.offset = offset2;
                }
            }
        }
        final int size = this.mItems.size();
        final float offset3 = itemInfo.offset;
        int l = itemInfo.position - 1;
        float offset4;
        if (itemInfo.position == 0) {
            offset4 = itemInfo.offset;
        }
        else {
            offset4 = -3.4028235E38f;
        }
        this.mFirstOffset = offset4;
        float mLastOffset;
        if (itemInfo.position == count - 1) {
            mLastOffset = itemInfo.offset + itemInfo.widthFactor - 1.0f;
        }
        else {
            mLastOffset = Float.MAX_VALUE;
        }
        this.mLastOffset = mLastOffset;
        int n4 = i - 1;
        float n5 = offset3;
        while (n4 >= 0) {
            for (itemInfo2 = this.mItems.get(n4); l > itemInfo2.position; --l) {
                n5 -= this.mAdapter.getPageWidth(l) + n;
            }
            n5 -= itemInfo2.widthFactor + n;
            itemInfo2.offset = n5;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = n5;
            }
            --n4;
            --l;
        }
        float offset5 = itemInfo.offset + itemInfo.widthFactor + n;
        final int n6 = itemInfo.position + 1;
        int n7;
        for (n7 = i + 1, i = n6; n7 < size; ++n7, ++i) {
            for (itemInfo = this.mItems.get(n7); i < itemInfo.position; ++i) {
                offset5 += this.mAdapter.getPageWidth(i) + n;
            }
            if (itemInfo.position == count - 1) {
                this.mLastOffset = itemInfo.widthFactor + offset5 - 1.0f;
            }
            itemInfo.offset = offset5;
            offset5 += itemInfo.widthFactor + n;
        }
        this.mNeedCalculatePageOffsets = false;
    }
    
    private void completeScroll(final boolean b) {
        int n;
        if (this.mScrollState == 2) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            this.setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                final int scrollX = this.getScrollX();
                final int scrollY = this.getScrollY();
                final int currX = this.mScroller.getCurrX();
                final int currY = this.mScroller.getCurrY();
                if (scrollX != currX || scrollY != currY) {
                    this.scrollTo(currX, currY);
                    if (currX != scrollX) {
                        this.pageScrolled(currX);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.scrolling) {
                n = 1;
                itemInfo.scrolling = false;
            }
        }
        if (n == 0) {
            return;
        }
        if (b) {
            ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
            return;
        }
        this.mEndScrollRunnable.run();
    }
    
    private int determineTargetPage(int n, final float n2, final int n3, final int n4) {
        if (Math.abs(n4) > this.mFlingDistance && Math.abs(n3) > this.mMinimumVelocity) {
            if (n3 <= 0) {
                ++n;
            }
        }
        else {
            float n5;
            if (n >= this.mCurItem) {
                n5 = 0.4f;
            }
            else {
                n5 = 0.6f;
            }
            n += (int)(n2 + n5);
        }
        if (this.mItems.size() > 0) {
            final ItemInfo itemInfo = this.mItems.get(0);
            final ArrayList<ItemInfo> mItems = this.mItems;
            return Math.max(itemInfo.position, Math.min(n, mItems.get(mItems.size() - 1).position));
        }
        return n;
    }
    
    private void dispatchOnPageScrolled(final int n, final float n2, final int n3) {
        final OnPageChangeListener mOnPageChangeListener = this.mOnPageChangeListener;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(n, n2, n3);
        }
        final List<OnPageChangeListener> mOnPageChangeListeners = this.mOnPageChangeListeners;
        if (mOnPageChangeListeners != null) {
            for (int i = 0; i < mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(n, n2, n3);
                }
            }
        }
        final OnPageChangeListener mInternalPageChangeListener = this.mInternalPageChangeListener;
        if (mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageScrolled(n, n2, n3);
        }
    }
    
    private void dispatchOnPageSelected(final int n) {
        final OnPageChangeListener mOnPageChangeListener = this.mOnPageChangeListener;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(n);
        }
        final List<OnPageChangeListener> mOnPageChangeListeners = this.mOnPageChangeListeners;
        if (mOnPageChangeListeners != null) {
            for (int i = 0; i < mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(n);
                }
            }
        }
        final OnPageChangeListener mInternalPageChangeListener = this.mInternalPageChangeListener;
        if (mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageSelected(n);
        }
    }
    
    private void dispatchOnScrollStateChanged(final int n) {
        final OnPageChangeListener mOnPageChangeListener = this.mOnPageChangeListener;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(n);
        }
        final List<OnPageChangeListener> mOnPageChangeListeners = this.mOnPageChangeListeners;
        if (mOnPageChangeListeners != null) {
            for (int i = 0; i < mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(n);
                }
            }
        }
        final OnPageChangeListener mInternalPageChangeListener = this.mInternalPageChangeListener;
        if (mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageScrollStateChanged(n);
        }
    }
    
    private void enableLayers(final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            int mPageTransformerLayerType;
            if (b) {
                mPageTransformerLayerType = this.mPageTransformerLayerType;
            }
            else {
                mPageTransformerLayerType = 0;
            }
            this.getChildAt(i).setLayerType(mPageTransformerLayerType, (Paint)null);
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private Rect getChildRectInPagerCoordinates(Rect rect, final View view) {
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
        ViewPager viewPager;
        for (ViewParent viewParent = view.getParent(); viewParent instanceof ViewGroup && viewParent != this; viewParent = viewPager.getParent()) {
            viewPager = (ViewPager)viewParent;
            rect.left += viewPager.getLeft();
            rect.right += viewPager.getRight();
            rect.top += viewPager.getTop();
            rect.bottom += viewPager.getBottom();
        }
        return rect;
    }
    
    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }
    
    private ItemInfo infoForCurrentScrollPosition() {
        final int clientWidth = this.getClientWidth();
        float n = 0.0f;
        float n2;
        if (clientWidth > 0) {
            n2 = this.getScrollX() / (float)clientWidth;
        }
        else {
            n2 = 0.0f;
        }
        if (clientWidth > 0) {
            n = this.mPageMargin / (float)clientWidth;
        }
        int position = -1;
        float offset = 0.0f;
        float widthFactor = 0.0f;
        int n3 = 1;
        ItemInfo itemInfo = null;
        ItemInfo mTempItem;
        for (int i = 0; i < this.mItems.size(); ++i, itemInfo = mTempItem) {
            mTempItem = this.mItems.get(i);
            if (n3 == 0 && mTempItem.position != position + 1) {
                mTempItem = this.mTempItem;
                mTempItem.offset = offset + widthFactor + n;
                mTempItem.position = position + 1;
                mTempItem.widthFactor = this.mAdapter.getPageWidth(mTempItem.position);
                --i;
            }
            offset = mTempItem.offset;
            final float widthFactor2 = mTempItem.widthFactor;
            if (n3 == 0 && n2 < offset) {
                return itemInfo;
            }
            if (n2 < widthFactor2 + offset + n) {
                return mTempItem;
            }
            if (i == this.mItems.size() - 1) {
                return mTempItem;
            }
            n3 = 0;
            position = mTempItem.position;
            widthFactor = mTempItem.widthFactor;
        }
        return itemInfo;
    }
    
    private static boolean isDecorView(@NonNull final View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }
    
    private boolean isGutterDrag(final float n, final float n2) {
        return (n < this.mGutterSize && n2 > 0.0f) || (n > this.getWidth() - this.mGutterSize && n2 < 0.0f);
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) != this.mActivePointerId) {
            return;
        }
        int n;
        if (actionIndex == 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.mLastMotionX = motionEvent.getX(n);
        this.mActivePointerId = motionEvent.getPointerId(n);
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
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
        else {
            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            final int clientWidth = this.getClientWidth();
            final int mPageMargin = this.mPageMargin;
            final float n2 = mPageMargin / (float)clientWidth;
            final int position = infoForCurrentScrollPosition.position;
            final float n3 = (n / (float)clientWidth - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + n2);
            n = (int)((clientWidth + mPageMargin) * n3);
            this.mCalledSuper = false;
            this.onPageScrolled(position, n3, n);
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }
    
    private boolean performDrag(float mLastMotionX) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        final float mLastMotionX2 = this.mLastMotionX;
        this.mLastMotionX = mLastMotionX;
        final float n = this.getScrollX() + (mLastMotionX2 - mLastMotionX);
        final int clientWidth = this.getClientWidth();
        mLastMotionX = clientWidth * this.mFirstOffset;
        float n2 = clientWidth * this.mLastOffset;
        boolean b4 = true;
        boolean b5 = true;
        final ItemInfo itemInfo = this.mItems.get(0);
        final ArrayList<ItemInfo> mItems = this.mItems;
        final ItemInfo itemInfo2 = mItems.get(mItems.size() - 1);
        if (itemInfo.position != 0) {
            b4 = false;
            mLastMotionX = itemInfo.offset * clientWidth;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            b5 = false;
            n2 = itemInfo2.offset * clientWidth;
        }
        if (n < mLastMotionX) {
            if (b4) {
                this.mLeftEdge.onPull(Math.abs(mLastMotionX - n) / clientWidth);
                b3 = true;
            }
        }
        else if (n > n2) {
            if (b5) {
                this.mRightEdge.onPull(Math.abs(n - n2) / clientWidth);
                b3 = true;
            }
            else {
                b3 = b;
            }
            mLastMotionX = n2;
        }
        else {
            mLastMotionX = n;
            b3 = b2;
        }
        this.mLastMotionX += mLastMotionX - (int)mLastMotionX;
        this.scrollTo((int)mLastMotionX, this.getScrollY());
        this.pageScrolled((int)mLastMotionX);
        return b3;
    }
    
    private void recomputeScrollPosition(int n, final int n2, final int n3, final int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
                return;
            }
            this.scrollTo((int)((n - this.getPaddingLeft() - this.getPaddingRight() + n3) * (this.getScrollX() / (float)(n2 - this.getPaddingLeft() - this.getPaddingRight() + n4))), this.getScrollY());
        }
        else {
            final ItemInfo infoForPosition = this.infoForPosition(this.mCurItem);
            float min;
            if (infoForPosition != null) {
                min = Math.min(infoForPosition.offset, this.mLastOffset);
            }
            else {
                min = 0.0f;
            }
            n = (int)((n - this.getPaddingLeft() - this.getPaddingRight()) * min);
            if (n != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(n, this.getScrollY());
            }
        }
    }
    
    private void removeNonDecorViews() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            if (!((LayoutParams)this.getChildAt(i).getLayoutParams()).isDecor) {
                this.removeViewAt(i);
                --i;
            }
        }
    }
    
    private void requestParentDisallowInterceptTouchEvent(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return this.mLeftEdge.isFinished() || this.mRightEdge.isFinished();
    }
    
    private void scrollToItem(final int n, final boolean b, final int n2, final boolean b2) {
        final ItemInfo infoForPosition = this.infoForPosition(n);
        int n3 = 0;
        if (infoForPosition != null) {
            n3 = (int)(this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)));
        }
        if (!b) {
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
            this.completeScroll(false);
            this.scrollTo(n3, 0);
            this.pageScrolled(n3);
            return;
        }
        this.smoothScrollTo(n3, 0, n2);
        if (b2) {
            this.dispatchOnPageSelected(n);
        }
    }
    
    private void setScrollingCacheEnabled(final boolean mScrollingCacheEnabled) {
        if (this.mScrollingCacheEnabled != mScrollingCacheEnabled) {
            this.mScrollingCacheEnabled = mScrollingCacheEnabled;
        }
    }
    
    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            final ArrayList<View> mDrawingOrderedChildren = this.mDrawingOrderedChildren;
            if (mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList<View>();
            }
            else {
                mDrawingOrderedChildren.clear();
            }
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                this.mDrawingOrderedChildren.add(this.getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, ViewPager.sPositionComparator);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final int size = list.size();
        final int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    final ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        child.addFocusables((ArrayList)list, n, n2);
                    }
                }
            }
        }
        if (descendantFocusability == 262144 && size != list.size()) {
            return;
        }
        if (!this.isFocusable()) {
            return;
        }
        if ((n2 & 0x1) == 0x1 && (this.isInTouchMode() && !this.isFocusableInTouchMode())) {
            return;
        }
        if (list != null) {
            list.add((View)this);
        }
    }
    
    ItemInfo addNewItem(final int position, final int n) {
        final ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = position;
        itemInfo.object = this.mAdapter.instantiateItem(this, position);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(position);
        if (n >= 0 && n < this.mItems.size()) {
            this.mItems.add(n, itemInfo);
            return itemInfo;
        }
        this.mItems.add(itemInfo);
        return itemInfo;
    }
    
    public void addOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>();
        }
        this.mAdapterChangeListeners.add(onAdapterChangeListener);
    }
    
    public void addOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(onPageChangeListener);
    }
    
    public void addTouchables(final ArrayList<View> list) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    child.addTouchables((ArrayList)list);
                }
            }
        }
    }
    
    public void addView(final View view, final int n, ViewGroup$LayoutParams generateLayoutParams) {
        if (!this.checkLayoutParams(generateLayoutParams)) {
            generateLayoutParams = this.generateLayoutParams(generateLayoutParams);
        }
        final LayoutParams layoutParams = (LayoutParams)generateLayoutParams;
        layoutParams.isDecor |= isDecorView(view);
        if (!this.mInLayout) {
            super.addView(view, n, generateLayoutParams);
            return;
        }
        if (layoutParams != null && layoutParams.isDecor) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
        layoutParams.needsMeasure = true;
        this.addViewInLayout(view, n, generateLayoutParams);
    }
    
    public boolean arrowScroll(final int n) {
        final View focus = this.findFocus();
        View view;
        if (focus == this) {
            view = null;
        }
        else if (focus != null) {
            boolean b = false;
            for (ViewParent viewParent = focus.getParent(); viewParent instanceof ViewGroup; viewParent = viewParent.getParent()) {
                if (viewParent == this) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                final StringBuilder sb = new StringBuilder();
                sb.append(focus.getClass().getSimpleName());
                for (ViewParent viewParent2 = focus.getParent(); viewParent2 instanceof ViewGroup; viewParent2 = viewParent2.getParent()) {
                    sb.append(" => ");
                    sb.append(viewParent2.getClass().getSimpleName());
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("arrowScroll tried to find focus based on non-child current focused view ");
                sb2.append(sb.toString());
                Log.e("ViewPager", sb2.toString());
                view = null;
            }
            else {
                view = focus;
            }
        }
        else {
            view = focus;
        }
        final boolean b2 = false;
        boolean b3 = false;
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, n);
        if (nextFocus != null && nextFocus != view) {
            if (n == 17) {
                final int left = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left2 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                if (view != null && left >= left2) {
                    b3 = this.pageLeft();
                }
                else {
                    b3 = nextFocus.requestFocus();
                }
            }
            else if (n == 66) {
                final int left3 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left4 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                if (view != null && left3 <= left4) {
                    b3 = this.pageRight();
                }
                else {
                    b3 = nextFocus.requestFocus();
                }
            }
        }
        else if (n != 17 && n != 1) {
            if (n != 66 && n != 2) {
                b3 = b2;
            }
            else {
                b3 = this.pageRight();
            }
        }
        else {
            b3 = this.pageLeft();
        }
        if (b3) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(n));
            return b3;
        }
        return b3;
    }
    
    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            mVelocityTracker.clear();
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3) {
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
        return b && view.canScrollHorizontally(-n);
    }
    
    public boolean canScrollHorizontally(final int n) {
        final PagerAdapter mAdapter = this.mAdapter;
        final boolean b = false;
        boolean b2 = false;
        if (mAdapter == null) {
            return false;
        }
        final int clientWidth = this.getClientWidth();
        final int scrollX = this.getScrollX();
        if (n < 0) {
            if (scrollX > (int)(clientWidth * this.mFirstOffset)) {
                b2 = true;
            }
            return b2;
        }
        if (n > 0) {
            boolean b3 = b;
            if (scrollX < (int)(clientWidth * this.mLastOffset)) {
                b3 = true;
            }
            return b3;
        }
        return false;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void clearOnPageChangeListeners() {
        final List<OnPageChangeListener> mOnPageChangeListeners = this.mOnPageChangeListeners;
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.clear();
        }
    }
    
    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                this.scrollTo(currX, currY);
                if (!this.pageScrolled(currX)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, currY);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }
    
    void dataSetChanged() {
        final int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        boolean b = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < count;
        int n = this.mCurItem;
        int n2 = 0;
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            final int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition != -1) {
                if (itemPosition == -2) {
                    this.mItems.remove(i);
                    --i;
                    if (n2 == 0) {
                        this.mAdapter.startUpdate(this);
                        n2 = 1;
                    }
                    this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
                    b = true;
                    if (this.mCurItem == itemInfo.position) {
                        n = Math.max(0, Math.min(this.mCurItem, count - 1));
                        b = true;
                    }
                }
                else if (itemInfo.position != itemPosition) {
                    if (itemInfo.position == this.mCurItem) {
                        n = itemPosition;
                    }
                    itemInfo.position = itemPosition;
                    b = true;
                }
            }
        }
        if (n2 != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, ViewPager.COMPARATOR);
        if (b) {
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(j).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            this.setCurrentItemInternal(n, false, true);
            this.requestLayout();
        }
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    if (child.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((n - 0.5f) * 0.47123894f);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        final boolean b = false;
        boolean b2 = false;
        final int overScrollMode = this.getOverScrollMode();
        Label_0266: {
            Label_0071: {
                if (overScrollMode != 0) {
                    if (overScrollMode == 1) {
                        final PagerAdapter mAdapter = this.mAdapter;
                        if (mAdapter != null) {
                            if (mAdapter.getCount() > 1) {
                                break Label_0071;
                            }
                        }
                    }
                    this.mLeftEdge.finish();
                    this.mRightEdge.finish();
                    b2 = b;
                    break Label_0266;
                }
            }
            if (!this.mLeftEdge.isFinished()) {
                final int save = canvas.save();
                final int n = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                final int width = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(-n + this.getPaddingTop()), this.mFirstOffset * width);
                this.mLeftEdge.setSize(n, width);
                b2 = (false | this.mLeftEdge.draw(canvas));
                canvas.restoreToCount(save);
            }
            if (!this.mRightEdge.isFinished()) {
                final int save2 = canvas.save();
                final int width2 = this.getWidth();
                final int height = this.getHeight();
                final int paddingTop = this.getPaddingTop();
                final int paddingBottom = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0f) * width2);
                this.mRightEdge.setSize(height - paddingTop - paddingBottom, width2);
                b2 |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(save2);
            }
        }
        if (b2) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable mMarginDrawable = this.mMarginDrawable;
        if (mMarginDrawable != null && mMarginDrawable.isStateful()) {
            mMarginDrawable.setState(this.getDrawableState());
        }
    }
    
    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                final int n = (int)mVelocityTracker.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                final int clientWidth = this.getClientWidth();
                final int scrollX = this.getScrollX();
                final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
                this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / (float)clientWidth - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
            }
            this.endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return false;
        }
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode != 61) {
            switch (keyCode) {
                default: {
                    return false;
                }
                case 22: {
                    if (keyEvent.hasModifiers(2)) {
                        return this.pageRight();
                    }
                    return this.arrowScroll(66);
                }
                case 21: {
                    if (keyEvent.hasModifiers(2)) {
                        return this.pageLeft();
                    }
                    return this.arrowScroll(17);
                }
            }
        }
        else {
            if (keyEvent.hasNoModifiers()) {
                return this.arrowScroll(2);
            }
            return keyEvent.hasModifiers(1) && this.arrowScroll(1);
        }
    }
    
    public void fakeDragBy(float n) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        if (this.mAdapter == null) {
            return;
        }
        this.mLastMotionX += n;
        final float n2 = this.getScrollX() - n;
        final int clientWidth = this.getClientWidth();
        n = clientWidth * this.mFirstOffset;
        float n3 = clientWidth * this.mLastOffset;
        final ItemInfo itemInfo = this.mItems.get(0);
        final ArrayList<ItemInfo> mItems = this.mItems;
        final ItemInfo itemInfo2 = mItems.get(mItems.size() - 1);
        if (itemInfo.position != 0) {
            n = itemInfo.offset * clientWidth;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            n3 = itemInfo2.offset * clientWidth;
        }
        if (n2 >= n) {
            if (n2 > n3) {
                n = n3;
            }
            else {
                n = n2;
            }
        }
        this.mLastMotionX += n - (int)n;
        this.scrollTo((int)n, this.getScrollY());
        this.pageScrolled((int)n);
        final MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return this.generateDefaultLayoutParams();
    }
    
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
    
    protected int getChildDrawingOrder(final int n, int n2) {
        if (this.mDrawingOrder == 2) {
            n2 = n - 1 - n2;
        }
        return ((LayoutParams)this.mDrawingOrderedChildren.get(n2).getLayoutParams()).childIndex;
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
        while (true) {
            final ViewParent parent = view.getParent();
            if (parent == this) {
                return this.infoForChild(view);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            view = (View)parent;
        }
    }
    
    ItemInfo infoForChild(final View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }
    
    ItemInfo infoForPosition(final int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position == n) {
                return itemInfo;
            }
        }
        return null;
    }
    
    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        final Context context = this.getContext();
        this.mScroller = new Scroller(context, ViewPager.sInterpolator);
        final ViewConfiguration value = ViewConfiguration.get(context);
        final float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = value.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * density);
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int)(25.0f * density);
        this.mCloseEnough = (int)(2.0f * density);
        this.mDefaultGutterSize = (int)(16.0f * density);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            private final Rect mTempRect = new Rect();
            
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                final WindowInsetsCompat onApplyWindowInsets = ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
                if (onApplyWindowInsets.isConsumed()) {
                    return onApplyWindowInsets;
                }
                final Rect mTempRect = this.mTempRect;
                mTempRect.left = onApplyWindowInsets.getSystemWindowInsetLeft();
                mTempRect.top = onApplyWindowInsets.getSystemWindowInsetTop();
                mTempRect.right = onApplyWindowInsets.getSystemWindowInsetRight();
                mTempRect.bottom = onApplyWindowInsets.getSystemWindowInsetBottom();
                for (int i = 0; i < ViewPager.this.getChildCount(); ++i) {
                    final WindowInsetsCompat dispatchApplyWindowInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), onApplyWindowInsets);
                    mTempRect.left = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetLeft(), mTempRect.left);
                    mTempRect.top = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetTop(), mTempRect.top);
                    mTempRect.right = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetRight(), mTempRect.right);
                    mTempRect.bottom = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetBottom(), mTempRect.bottom);
                }
                return onApplyWindowInsets.replaceSystemWindowInsets(mTempRect.left, mTempRect.top, mTempRect.right, mTempRect.bottom);
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
        final Scroller mScroller = this.mScroller;
        if (mScroller != null && !mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            final int scrollX = this.getScrollX();
            final int width = this.getWidth();
            final float n = this.mPageMargin / (float)width;
            int n2 = 0;
            ItemInfo itemInfo = this.mItems.get(0);
            float offset = itemInfo.offset;
            for (int size = this.mItems.size(), i = itemInfo.position; i < this.mItems.get(size - 1).position; ++i) {
                while (i > itemInfo.position && n2 < size) {
                    final ArrayList<ItemInfo> mItems = this.mItems;
                    ++n2;
                    itemInfo = mItems.get(n2);
                }
                float n3;
                if (i == itemInfo.position) {
                    n3 = (itemInfo.offset + itemInfo.widthFactor) * width;
                    offset = itemInfo.offset + itemInfo.widthFactor + n;
                }
                else {
                    final float pageWidth = this.mAdapter.getPageWidth(i);
                    final float n4 = (float)width;
                    final float n5 = offset + (pageWidth + n);
                    n3 = (offset + pageWidth) * n4;
                    offset = n5;
                }
                if (this.mPageMargin + n3 > scrollX) {
                    this.mMarginDrawable.setBounds(Math.round(n3), this.mTopPageBounds, Math.round(this.mPageMargin + n3), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (n3 > scrollX + width) {
                    return;
                }
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int n = motionEvent.getAction() & 0xFF;
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
                }
                else {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId != -1) {
                        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                        final float x = motionEvent.getX(pointerIndex);
                        final float n2 = x - this.mLastMotionX;
                        final float abs = Math.abs(n2);
                        final float y = motionEvent.getY(pointerIndex);
                        final float abs2 = Math.abs(y - this.mInitialMotionY);
                        if (n2 != 0.0f && !this.isGutterDrag(this.mLastMotionX, n2) && this.canScroll((View)this, false, (int)n2, (int)x, (int)y)) {
                            this.mLastMotionX = x;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                        if (abs > this.mTouchSlop && 0.5f * abs > abs2) {
                            this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                            this.setScrollState(1);
                            float mLastMotionX;
                            if (n2 > 0.0f) {
                                mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                            }
                            else {
                                mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                            }
                            this.mLastMotionX = mLastMotionX;
                            this.mLastMotionY = y;
                            this.setScrollingCacheEnabled(true);
                        }
                        else if (abs2 > this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged) {
                            if (this.performDrag(x)) {
                                ViewCompat.postInvalidateOnAnimation((View)this);
                            }
                        }
                    }
                }
            }
            else {
                final float x2 = motionEvent.getX();
                this.mInitialMotionX = x2;
                this.mLastMotionX = x2;
                final float y2 = motionEvent.getY();
                this.mInitialMotionY = y2;
                this.mLastMotionY = y2;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mIsScrollStarted = true;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                    this.setScrollState(1);
                }
                else {
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
    
    protected void onLayout(final boolean b, int n, int paddingTop, int max, int paddingBottom) {
        final int childCount = this.getChildCount();
        final int n2 = max - n;
        final int n3 = paddingBottom - paddingTop;
        n = this.getPaddingLeft();
        paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        paddingBottom = this.getPaddingBottom();
        final int scrollX = this.getScrollX();
        int mDecorChildCount = 0;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isDecor) {
                    max = (layoutParams.gravity & 0x7);
                    final int n4 = layoutParams.gravity & 0x70;
                    int n5;
                    if (max != 1) {
                        if (max != 3) {
                            if (max != 5) {
                                max = n;
                                n5 = n;
                            }
                            else {
                                max = n2 - paddingRight - child.getMeasuredWidth();
                                paddingRight += child.getMeasuredWidth();
                                n5 = n;
                            }
                        }
                        else {
                            max = n;
                            n5 = n + child.getMeasuredWidth();
                        }
                    }
                    else {
                        max = Math.max((n2 - child.getMeasuredWidth()) / 2, n);
                        n5 = n;
                    }
                    if (n4 != 16) {
                        if (n4 != 48) {
                            if (n4 != 80) {
                                n = paddingTop;
                            }
                            else {
                                n = n3 - paddingBottom - child.getMeasuredHeight();
                                paddingBottom += child.getMeasuredHeight();
                            }
                        }
                        else {
                            n = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                        }
                    }
                    else {
                        n = Math.max((n3 - child.getMeasuredHeight()) / 2, paddingTop);
                    }
                    max += scrollX;
                    child.layout(max, n, child.getMeasuredWidth() + max, n + child.getMeasuredHeight());
                    ++mDecorChildCount;
                    n = n5;
                }
            }
        }
        final int n6 = n2 - n - paddingRight;
        int j = 0;
        max = n2;
        while (j < childCount) {
            final View child2 = this.getChildAt(j);
            if (child2.getVisibility() != 8) {
                final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    final ItemInfo infoForChild = this.infoForChild(child2);
                    if (infoForChild != null) {
                        final int n7 = n + (int)(n6 * infoForChild.offset);
                        if (layoutParams2.needsMeasure) {
                            layoutParams2.needsMeasure = false;
                            child2.measure(View$MeasureSpec.makeMeasureSpec((int)(n6 * layoutParams2.widthFactor), 1073741824), View$MeasureSpec.makeMeasureSpec(n3 - paddingTop - paddingBottom, 1073741824));
                        }
                        child2.layout(n7, paddingTop, child2.getMeasuredWidth() + n7, child2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
            ++j;
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = n3 - paddingBottom;
        this.mDecorChildCount = mDecorChildCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int n, int i) {
        this.setMeasuredDimension(getDefaultSize(0, n), getDefaultSize(0, i));
        final int measuredWidth = this.getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        n = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        i = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    final int n2 = layoutParams.gravity & 0x7;
                    final int n3 = layoutParams.gravity & 0x70;
                    int n4 = Integer.MIN_VALUE;
                    int n5 = Integer.MIN_VALUE;
                    final boolean b = n3 == 48 || n3 == 80;
                    final boolean b2 = n2 == 3 || n2 == 5;
                    if (b) {
                        n4 = 1073741824;
                    }
                    else if (b2) {
                        n5 = 1073741824;
                    }
                    int n6;
                    int width;
                    if (layoutParams.width != -2) {
                        n6 = 1073741824;
                        if (layoutParams.width != -1) {
                            width = layoutParams.width;
                        }
                        else {
                            width = n;
                        }
                    }
                    else {
                        final int n7 = n;
                        n6 = n4;
                        width = n7;
                    }
                    int height;
                    int n8;
                    if (layoutParams.height != -2) {
                        if (layoutParams.height != -1) {
                            height = layoutParams.height;
                            n8 = 1073741824;
                        }
                        else {
                            n8 = 1073741824;
                            height = i;
                        }
                    }
                    else {
                        n8 = n5;
                        height = i;
                    }
                    child.measure(View$MeasureSpec.makeMeasureSpec(width, n6), View$MeasureSpec.makeMeasureSpec(height, n8));
                    if (b) {
                        i -= child.getMeasuredHeight();
                    }
                    else if (b2) {
                        n -= child.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
        this.mChildHeightMeasureSpec = View$MeasureSpec.makeMeasureSpec(i, 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int childCount2;
        View child2;
        LayoutParams layoutParams2;
        for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2 == null || !layoutParams2.isDecor) {
                    child2.measure(View$MeasureSpec.makeMeasureSpec((int)(n * layoutParams2.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }
    
    @CallSuper
    protected void onPageScrolled(int i, float n, int scrollX) {
        if (this.mDecorChildCount > 0) {
            final int scrollX2 = this.getScrollX();
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            final int width = this.getWidth();
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final View child = this.getChildAt(j);
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isDecor) {
                    final int n2 = layoutParams.gravity & 0x7;
                    int max;
                    if (n2 != 1) {
                        if (n2 != 3) {
                            if (n2 != 5) {
                                max = paddingLeft;
                            }
                            else {
                                max = width - paddingRight - child.getMeasuredWidth();
                                paddingRight += child.getMeasuredWidth();
                            }
                        }
                        else {
                            max = paddingLeft;
                            paddingLeft += child.getWidth();
                        }
                    }
                    else {
                        max = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                    }
                    final int n3 = max + scrollX2 - child.getLeft();
                    if (n3 != 0) {
                        child.offsetLeftAndRight(n3);
                    }
                }
            }
        }
        this.dispatchOnPageScrolled(i, n, scrollX);
        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            int childCount2;
            View child2;
            for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
                child2 = this.getChildAt(i);
                if (!((LayoutParams)child2.getLayoutParams()).isDecor) {
                    n = (child2.getLeft() - scrollX) / (float)this.getClientWidth();
                    this.mPageTransformer.transformPage(child2, n);
                }
            }
        }
        this.mCalledSuper = true;
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int childCount = this.getChildCount();
        int i;
        int n2;
        if ((n & 0x2) != 0x0) {
            i = 0;
            n2 = 1;
        }
        else {
            i = childCount - 1;
            n2 = -1;
            childCount = -1;
        }
        while (i != childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    if (child.requestFocus(n, rect)) {
                        return true;
                    }
                }
            }
            i += n2;
        }
        return false;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        final PagerAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            mAdapter.restoreState(savedState.adapterState, savedState.loader);
            this.setCurrentItemInternal(savedState.position, false, true);
            return;
        }
        this.mRestoredCurItem = savedState.position;
        this.mRestoredAdapterState = savedState.adapterState;
        this.mRestoredClassLoader = savedState.loader;
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        final PagerAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            savedState.adapterState = mAdapter.saveState();
            return (Parcelable)savedState;
        }
        return (Parcelable)savedState;
    }
    
    protected void onSizeChanged(final int n, int mPageMargin, final int n2, final int n3) {
        super.onSizeChanged(n, mPageMargin, n2, n3);
        if (n != n2) {
            mPageMargin = this.mPageMargin;
            this.recomputeScrollPosition(n, n2, mPageMargin, mPageMargin);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        final PagerAdapter mAdapter = this.mAdapter;
        if (mAdapter == null) {
            return false;
        }
        if (mAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int action = motionEvent.getAction();
        boolean b = false;
        switch (action & 0xFF) {
            case 6: {
                this.onSecondaryPointerUp(motionEvent);
                this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                break;
            }
            case 5: {
                final int actionIndex = motionEvent.getActionIndex();
                this.mLastMotionX = motionEvent.getX(actionIndex);
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                break;
            }
            case 3: {
                if (this.mIsBeingDragged) {
                    this.scrollToItem(this.mCurItem, true, 0, false);
                    b = this.resetTouch();
                    break;
                }
                break;
            }
            case 2: {
                if (!this.mIsBeingDragged) {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex == -1) {
                        b = this.resetTouch();
                        break;
                    }
                    final float x = motionEvent.getX(pointerIndex);
                    final float abs = Math.abs(x - this.mLastMotionX);
                    final float y = motionEvent.getY(pointerIndex);
                    final float abs2 = Math.abs(y - this.mLastMotionY);
                    if (abs > this.mTouchSlop && abs > abs2) {
                        this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                        final float mInitialMotionX = this.mInitialMotionX;
                        float mLastMotionX;
                        if (x - mInitialMotionX > 0.0f) {
                            mLastMotionX = mInitialMotionX + this.mTouchSlop;
                        }
                        else {
                            mLastMotionX = mInitialMotionX - this.mTouchSlop;
                        }
                        this.mLastMotionX = mLastMotionX;
                        this.mLastMotionY = y;
                        this.setScrollState(1);
                        this.setScrollingCacheEnabled(true);
                        final ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    b = (false | this.performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId))));
                    break;
                }
                break;
            }
            case 1: {
                if (this.mIsBeingDragged) {
                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                    mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    final int n = (int)mVelocityTracker.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    final int clientWidth = this.getClientWidth();
                    final int scrollX = this.getScrollX();
                    final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
                    this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / (float)clientWidth - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + this.mPageMargin / (float)clientWidth), n, (int)(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, n);
                    b = this.resetTouch();
                    break;
                }
                break;
            }
            case 0: {
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                this.populate();
                final float x2 = motionEvent.getX();
                this.mInitialMotionX = x2;
                this.mLastMotionX = x2;
                final float y2 = motionEvent.getY();
                this.mInitialMotionY = y2;
                this.mLastMotionY = y2;
                this.mActivePointerId = motionEvent.getPointerId(0);
                break;
            }
        }
        if (b) {
            ViewCompat.postInvalidateOnAnimation((View)this);
            return true;
        }
        return true;
    }
    
    boolean pageLeft() {
        final int mCurItem = this.mCurItem;
        if (mCurItem > 0) {
            this.setCurrentItem(mCurItem - 1, true);
            return true;
        }
        return false;
    }
    
    boolean pageRight() {
        final PagerAdapter mAdapter = this.mAdapter;
        if (mAdapter != null && this.mCurItem < mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }
    
    void populate() {
        this.populate(this.mCurItem);
    }
    
    void populate(int i) {
        final int mCurItem = this.mCurItem;
        ItemInfo infoForPosition;
        if (mCurItem != i) {
            infoForPosition = this.infoForPosition(mCurItem);
            this.mCurItem = i;
        }
        else {
            infoForPosition = null;
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
        final int mOffscreenPageLimit = this.mOffscreenPageLimit;
        final int max = Math.max(0, this.mCurItem - mOffscreenPageLimit);
        final int count = this.mAdapter.getCount();
        final int min = Math.min(count - 1, this.mCurItem + mOffscreenPageLimit);
        if (count != this.mExpectedAdapterCount) {
            String s;
            try {
                s = this.getResources().getResourceName(this.getId());
            }
            catch (Resources$NotFoundException ex) {
                s = Integer.toHexString(this.getId());
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
            sb.append(this.mExpectedAdapterCount);
            sb.append(", found: ");
            sb.append(count);
            sb.append(" Pager id: ");
            sb.append(s);
            sb.append(" Pager class: ");
            sb.append(this.getClass());
            sb.append(" Problematic adapter: ");
            sb.append(this.mAdapter.getClass());
            throw new IllegalStateException(sb.toString());
        }
        final ItemInfo itemInfo = null;
        i = 0;
        ItemInfo itemInfo2;
        while (true) {
            itemInfo2 = itemInfo;
            if (i >= this.mItems.size()) {
                break;
            }
            itemInfo2 = this.mItems.get(i);
            if (itemInfo2.position >= this.mCurItem) {
                if (itemInfo2.position == this.mCurItem) {
                    break;
                }
                itemInfo2 = itemInfo;
                break;
            }
            else {
                ++i;
            }
        }
        ItemInfo addNewItem;
        if (itemInfo2 == null && count > 0) {
            addNewItem = this.addNewItem(this.mCurItem, i);
        }
        else {
            addNewItem = itemInfo2;
        }
        if (addNewItem != null) {
            float n = 0.0f;
            int n2 = i - 1;
            ItemInfo itemInfo3;
            if (n2 >= 0) {
                itemInfo3 = this.mItems.get(n2);
            }
            else {
                itemInfo3 = null;
            }
            final int clientWidth = this.getClientWidth();
            float n3;
            if (clientWidth <= 0) {
                n3 = 0.0f;
            }
            else {
                n3 = this.getPaddingLeft() / (float)clientWidth + (2.0f - addNewItem.widthFactor);
            }
            int j = this.mCurItem - 1;
            int n4 = i;
            while (j >= 0) {
                if (n >= n3 && j < max) {
                    if (itemInfo3 == null) {
                        break;
                    }
                    if (j == itemInfo3.position && !itemInfo3.scrolling) {
                        this.mItems.remove(n2);
                        this.mAdapter.destroyItem(this, j, itemInfo3.object);
                        --n2;
                        i = n4 - 1;
                        if (n2 >= 0) {
                            itemInfo3 = this.mItems.get(n2);
                        }
                        else {
                            itemInfo3 = null;
                        }
                    }
                    else {
                        i = n4;
                    }
                }
                else if (itemInfo3 != null && j == itemInfo3.position) {
                    n += itemInfo3.widthFactor;
                    --n2;
                    if (n2 >= 0) {
                        itemInfo3 = this.mItems.get(n2);
                    }
                    else {
                        itemInfo3 = null;
                    }
                    i = n4;
                }
                else {
                    n += this.addNewItem(j, n2 + 1).widthFactor;
                    i = n4 + 1;
                    if (n2 >= 0) {
                        itemInfo3 = this.mItems.get(n2);
                    }
                    else {
                        itemInfo3 = null;
                    }
                }
                --j;
                n4 = i;
            }
            float widthFactor = addNewItem.widthFactor;
            i = n4 + 1;
            if (widthFactor < 2.0f) {
                ItemInfo itemInfo4;
                if (i < this.mItems.size()) {
                    itemInfo4 = this.mItems.get(i);
                }
                else {
                    itemInfo4 = null;
                }
                float n5;
                if (clientWidth <= 0) {
                    n5 = 0.0f;
                }
                else {
                    n5 = this.getPaddingRight() / (float)clientWidth + 2.0f;
                }
                for (int k = this.mCurItem + 1; k < count; ++k) {
                    if (widthFactor >= n5 && k > min) {
                        if (itemInfo4 == null) {
                            break;
                        }
                        if (k == itemInfo4.position && !itemInfo4.scrolling) {
                            this.mItems.remove(i);
                            this.mAdapter.destroyItem(this, k, itemInfo4.object);
                            if (i < this.mItems.size()) {
                                itemInfo4 = this.mItems.get(i);
                            }
                            else {
                                itemInfo4 = null;
                            }
                        }
                    }
                    else if (itemInfo4 != null && k == itemInfo4.position) {
                        widthFactor += itemInfo4.widthFactor;
                        ++i;
                        if (i < this.mItems.size()) {
                            itemInfo4 = this.mItems.get(i);
                        }
                        else {
                            itemInfo4 = null;
                        }
                    }
                    else {
                        final ItemInfo addNewItem2 = this.addNewItem(k, i);
                        ++i;
                        widthFactor += addNewItem2.widthFactor;
                        if (i < this.mItems.size()) {
                            itemInfo4 = this.mItems.get(i);
                        }
                        else {
                            itemInfo4 = null;
                        }
                    }
                }
            }
            this.calculatePageOffsets(addNewItem, n4, infoForPosition);
        }
        final PagerAdapter mAdapter = this.mAdapter;
        i = this.mCurItem;
        Object object;
        if (addNewItem != null) {
            object = addNewItem.object;
        }
        else {
            object = null;
        }
        mAdapter.setPrimaryItem(this, i, object);
        this.mAdapter.finishUpdate(this);
        int childCount;
        View child;
        LayoutParams layoutParams;
        ItemInfo infoForChild;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            layoutParams.childIndex = i;
            if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0f) {
                infoForChild = this.infoForChild(child);
                if (infoForChild != null) {
                    layoutParams.widthFactor = infoForChild.widthFactor;
                    layoutParams.position = infoForChild.position;
                }
            }
        }
        this.sortChildDrawingOrder();
        if (!this.hasFocus()) {
            return;
        }
        final View focus = this.findFocus();
        ItemInfo infoForAnyChild;
        if (focus != null) {
            infoForAnyChild = this.infoForAnyChild(focus);
        }
        else {
            infoForAnyChild = null;
        }
        if (infoForAnyChild != null && infoForAnyChild.position == this.mCurItem) {
            return;
        }
        View child2;
        ItemInfo infoForChild2;
        for (i = 0; i < this.getChildCount(); ++i) {
            child2 = this.getChildAt(i);
            infoForChild2 = this.infoForChild(child2);
            if (infoForChild2 != null && infoForChild2.position == this.mCurItem && child2.requestFocus(2)) {
                return;
            }
        }
    }
    
    public void removeOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        final List<OnAdapterChangeListener> mAdapterChangeListeners = this.mAdapterChangeListeners;
        if (mAdapterChangeListeners != null) {
            mAdapterChangeListeners.remove(onAdapterChangeListener);
        }
    }
    
    public void removeOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        final List<OnPageChangeListener> mOnPageChangeListeners = this.mOnPageChangeListeners;
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.remove(onPageChangeListener);
        }
    }
    
    public void removeView(final View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
            return;
        }
        super.removeView(view);
    }
    
    public void setAdapter(final PagerAdapter mAdapter) {
        final PagerAdapter mAdapter2 = this.mAdapter;
        if (mAdapter2 != null) {
            mAdapter2.setViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); ++i) {
                final ItemInfo itemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.scrollTo(this.mCurItem = 0, 0);
        }
        final PagerAdapter mAdapter3 = this.mAdapter;
        this.mAdapter = mAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            final boolean mFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            }
            else if (!mFirstLayout) {
                this.populate();
            }
            else {
                this.requestLayout();
            }
        }
        final List<OnAdapterChangeListener> mAdapterChangeListeners = this.mAdapterChangeListeners;
        if (mAdapterChangeListeners != null && !mAdapterChangeListeners.isEmpty()) {
            for (int j = 0; j < this.mAdapterChangeListeners.size(); ++j) {
                this.mAdapterChangeListeners.get(j).onAdapterChanged(this, mAdapter3, mAdapter);
            }
        }
    }
    
    public void setCurrentItem(final int n) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, this.mFirstLayout ^ true, false);
    }
    
    public void setCurrentItem(final int n, final boolean b) {
        this.setCurrentItemInternal(n, b, this.mPopulatePending = false);
    }
    
    void setCurrentItemInternal(final int n, final boolean b, final boolean b2) {
        this.setCurrentItemInternal(n, b, b2, 0);
    }
    
    void setCurrentItemInternal(int mCurItem, final boolean b, final boolean b2, final int n) {
        final PagerAdapter mAdapter = this.mAdapter;
        if (mAdapter == null || mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if (!b2 && this.mCurItem == mCurItem && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        boolean b3 = true;
        if (mCurItem < 0) {
            mCurItem = 0;
        }
        else if (mCurItem >= this.mAdapter.getCount()) {
            mCurItem = this.mAdapter.getCount() - 1;
        }
        final int mOffscreenPageLimit = this.mOffscreenPageLimit;
        final int mCurItem2 = this.mCurItem;
        if (mCurItem > mCurItem2 + mOffscreenPageLimit || mCurItem < mCurItem2 - mOffscreenPageLimit) {
            for (int i = 0; i < this.mItems.size(); ++i) {
                this.mItems.get(i).scrolling = true;
            }
        }
        if (this.mCurItem == mCurItem) {
            b3 = false;
        }
        if (this.mFirstLayout) {
            this.mCurItem = mCurItem;
            if (b3) {
                this.dispatchOnPageSelected(mCurItem);
            }
            this.requestLayout();
            return;
        }
        this.populate(mCurItem);
        this.scrollToItem(mCurItem, b, n, b3);
    }
    
    OnPageChangeListener setInternalPageChangeListener(final OnPageChangeListener mInternalPageChangeListener) {
        final OnPageChangeListener mInternalPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = mInternalPageChangeListener;
        return mInternalPageChangeListener2;
    }
    
    public void setOffscreenPageLimit(int mOffscreenPageLimit) {
        if (mOffscreenPageLimit < 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested offscreen page limit ");
            sb.append(mOffscreenPageLimit);
            sb.append(" too small; defaulting to ");
            sb.append(1);
            Log.w("ViewPager", sb.toString());
            mOffscreenPageLimit = 1;
        }
        if (mOffscreenPageLimit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = mOffscreenPageLimit;
            this.populate();
        }
    }
    
    @Deprecated
    public void setOnPageChangeListener(final OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }
    
    public void setPageMargin(final int mPageMargin) {
        final int mPageMargin2 = this.mPageMargin;
        this.mPageMargin = mPageMargin;
        final int width = this.getWidth();
        this.recomputeScrollPosition(width, width, mPageMargin, mPageMargin2);
        this.requestLayout();
    }
    
    public void setPageMarginDrawable(@DrawableRes final int n) {
        this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setPageMarginDrawable(final Drawable mMarginDrawable) {
        this.mMarginDrawable = mMarginDrawable;
        if (mMarginDrawable != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(mMarginDrawable == null);
        this.invalidate();
    }
    
    public void setPageTransformer(final boolean b, final PageTransformer pageTransformer) {
        this.setPageTransformer(b, pageTransformer, 2);
    }
    
    public void setPageTransformer(final boolean b, final PageTransformer mPageTransformer, final int mPageTransformerLayerType) {
        int mDrawingOrder = 1;
        final boolean childrenDrawingOrderEnabled = mPageTransformer != null;
        final boolean b2 = childrenDrawingOrderEnabled != (this.mPageTransformer != null);
        this.mPageTransformer = mPageTransformer;
        this.setChildrenDrawingOrderEnabled(childrenDrawingOrderEnabled);
        if (childrenDrawingOrderEnabled) {
            if (b) {
                mDrawingOrder = 2;
            }
            this.mDrawingOrder = mDrawingOrder;
            this.mPageTransformerLayerType = mPageTransformerLayerType;
        }
        else {
            this.mDrawingOrder = 0;
        }
        if (b2) {
            this.populate();
        }
    }
    
    void setScrollState(final int mScrollState) {
        if (this.mScrollState == mScrollState) {
            return;
        }
        this.mScrollState = mScrollState;
        if (this.mPageTransformer != null) {
            this.enableLayers(mScrollState != 0);
        }
        this.dispatchOnScrollStateChanged(mScrollState);
    }
    
    void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollTo(n, n2, 0);
    }
    
    void smoothScrollTo(int n, int n2, int abs) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        final Scroller mScroller = this.mScroller;
        int n3;
        if (mScroller != null && !mScroller.isFinished()) {
            if (this.mIsScrollStarted) {
                n3 = this.mScroller.getCurrX();
            }
            else {
                n3 = this.mScroller.getStartX();
            }
            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
        }
        else {
            n3 = this.getScrollX();
        }
        final int scrollY = this.getScrollY();
        final int n4 = n - n3;
        n2 -= scrollY;
        if (n4 == 0 && n2 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        n = this.getClientWidth();
        final int n5 = n / 2;
        final float min = Math.min(1.0f, Math.abs(n4) * 1.0f / n);
        final float n6 = (float)n5;
        final float n7 = (float)n5;
        final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
        abs = Math.abs(abs);
        if (abs > 0) {
            n = Math.round(Math.abs((n6 + n7 * distanceInfluenceForSnapDuration) / abs) * 1000.0f) * 4;
        }
        else {
            n = (int)((1.0f + Math.abs(n4) / (this.mPageMargin + n * this.mAdapter.getPageWidth(this.mCurItem))) * 100.0f);
        }
        n = Math.min(n, 600);
        this.mIsScrollStarted = false;
        this.mScroller.startScroll(n3, scrollY, n4, n2, n);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
    
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DecorView {
    }
    
    static class ItemInfo
    {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;
    }
    
    public static class LayoutParams extends ViewGroup$LayoutParams
    {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;
        
        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.widthFactor = 0.0f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }
    
    class MyAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
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
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
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
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            if (super.performAccessibilityAction(view, n, bundle)) {
                return true;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                if (ViewPager.this.canScrollHorizontally(-1)) {
                    final ViewPager this$0 = ViewPager.this;
                    this$0.setCurrentItem(this$0.mCurItem - 1);
                    return true;
                }
                return false;
            }
            else {
                if (ViewPager.this.canScrollHorizontally(1)) {
                    final ViewPager this$2 = ViewPager.this;
                    this$2.setCurrentItem(this$2.mCurItem + 1);
                    return true;
                }
                return false;
            }
        }
    }
    
    public interface OnAdapterChangeListener
    {
        void onAdapterChanged(@NonNull final ViewPager p0, @Nullable final PagerAdapter p1, @Nullable final PagerAdapter p2);
    }
    
    public interface OnPageChangeListener
    {
        void onPageScrollStateChanged(final int p0);
        
        void onPageScrolled(final int p0, final float p1, final int p2);
        
        void onPageSelected(final int p0);
    }
    
    public interface PageTransformer
    {
        void transformPage(final View p0, final float p1);
    }
    
    private class PagerObserver extends DataSetObserver
    {
        PagerObserver() {
        }
        
        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }
        
        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                classLoader = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader);
            this.loader = classLoader;
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("FragmentPager.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" position=");
            sb.append(this.position);
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }
    }
    
    public static class SimpleOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
        }
        
        @Override
        public void onPageSelected(final int n) {
        }
    }
    
    static class ViewPositionComparator implements Comparator<View>
    {
        @Override
        public int compare(final View view, final View view2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final LayoutParams layoutParams2 = (LayoutParams)view2.getLayoutParams();
            if (layoutParams.isDecor == layoutParams2.isDecor) {
                return layoutParams.position - layoutParams2.position;
            }
            if (layoutParams.isDecor) {
                return 1;
            }
            return -1;
        }
    }
}
