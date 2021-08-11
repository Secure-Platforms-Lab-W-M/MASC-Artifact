/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.Gravity
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  androidx.viewpager2.R
 *  androidx.viewpager2.R$styleable
 */
package androidx.viewpager2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R;
import androidx.viewpager2.adapter.StatefulAdapter;
import androidx.viewpager2.widget.CompositeOnPageChangeCallback;
import androidx.viewpager2.widget.FakeDrag;
import androidx.viewpager2.widget.PageTransformerAdapter;
import androidx.viewpager2.widget.ScrollEventAdapter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ViewPager2
extends ViewGroup {
    public static final int OFFSCREEN_PAGE_LIMIT_DEFAULT = -1;
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static boolean sFeatureEnhancedA11yEnabled = true;
    AccessibilityProvider mAccessibilityProvider;
    int mCurrentItem;
    private RecyclerView.AdapterDataObserver mCurrentItemDataSetChangeObserver;
    boolean mCurrentItemDirty = false;
    private CompositeOnPageChangeCallback mExternalPageChangeCallbacks = new CompositeOnPageChangeCallback(3);
    private FakeDrag mFakeDragger;
    private LinearLayoutManager mLayoutManager;
    private int mOffscreenPageLimit;
    private CompositeOnPageChangeCallback mPageChangeEventDispatcher;
    private PageTransformerAdapter mPageTransformerAdapter;
    private PagerSnapHelper mPagerSnapHelper;
    private Parcelable mPendingAdapterState;
    private int mPendingCurrentItem;
    RecyclerView mRecyclerView;
    private RecyclerView.ItemAnimator mSavedItemAnimator;
    private boolean mSavedItemAnimatorPresent;
    ScrollEventAdapter mScrollEventAdapter;
    private final Rect mTmpChildRect = new Rect();
    private final Rect mTmpContainerRect = new Rect();
    private boolean mUserInputEnabled;

    public ViewPager2(Context context) {
        super(context);
        this.mCurrentItemDataSetChangeObserver = new DataSetChangeObserver(){

            @Override
            public void onChanged() {
                ViewPager2.this.mCurrentItemDirty = true;
                ViewPager2.this.mScrollEventAdapter.notifyDataSetChangeHappened();
            }
        };
        this.mPendingCurrentItem = -1;
        this.mSavedItemAnimator = null;
        this.mSavedItemAnimatorPresent = false;
        this.mUserInputEnabled = true;
        this.mOffscreenPageLimit = -1;
        this.initialize(context, null);
    }

    public ViewPager2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentItemDataSetChangeObserver = new ;
        this.mPendingCurrentItem = -1;
        this.mSavedItemAnimator = null;
        this.mSavedItemAnimatorPresent = false;
        this.mUserInputEnabled = true;
        this.mOffscreenPageLimit = -1;
        this.initialize(context, attributeSet);
    }

    public ViewPager2(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mCurrentItemDataSetChangeObserver = new ;
        this.mPendingCurrentItem = -1;
        this.mSavedItemAnimator = null;
        this.mSavedItemAnimatorPresent = false;
        this.mUserInputEnabled = true;
        this.mOffscreenPageLimit = -1;
        this.initialize(context, attributeSet);
    }

    public ViewPager2(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.mCurrentItemDataSetChangeObserver = new ;
        this.mPendingCurrentItem = -1;
        this.mSavedItemAnimator = null;
        this.mSavedItemAnimatorPresent = false;
        this.mUserInputEnabled = true;
        this.mOffscreenPageLimit = -1;
        this.initialize(context, attributeSet);
    }

    private RecyclerView.OnChildAttachStateChangeListener enforceChildFillListener() {
        return new RecyclerView.OnChildAttachStateChangeListener(){

            @Override
            public void onChildViewAttachedToWindow(View object) {
                object = (RecyclerView.LayoutParams)object.getLayoutParams();
                if (object.width == -1 && object.height == -1) {
                    return;
                }
                throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        };
    }

    private void initialize(Context object, AttributeSet object2) {
        Object object3 = sFeatureEnhancedA11yEnabled ? new PageAwareAccessibilityProvider() : new BasicAccessibilityProvider();
        this.mAccessibilityProvider = object3;
        this.mRecyclerView = object3 = new RecyclerViewImpl((Context)object);
        object3.setId(ViewCompat.generateViewId());
        this.mRecyclerView.setDescendantFocusability(131072);
        this.mLayoutManager = object3 = new LinearLayoutManagerImpl((Context)object);
        this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)object3);
        this.mRecyclerView.setScrollingTouchSlop(1);
        this.setOrientation((Context)object, (AttributeSet)object2);
        this.mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mRecyclerView.addOnChildAttachStateChangeListener(this.enforceChildFillListener());
        this.mScrollEventAdapter = object = new ScrollEventAdapter(this);
        this.mFakeDragger = new FakeDrag(this, (ScrollEventAdapter)object, this.mRecyclerView);
        this.mPagerSnapHelper = object = new PagerSnapHelperImpl();
        object.attachToRecyclerView(this.mRecyclerView);
        this.mRecyclerView.addOnScrollListener(this.mScrollEventAdapter);
        this.mPageChangeEventDispatcher = object = new CompositeOnPageChangeCallback(3);
        this.mScrollEventAdapter.setOnPageChangeCallback((OnPageChangeCallback)object);
        object = new OnPageChangeCallback(){

            @Override
            public void onPageScrollStateChanged(int n) {
                if (n == 0) {
                    ViewPager2.this.updateCurrentItem();
                }
            }

            @Override
            public void onPageSelected(int n) {
                if (ViewPager2.this.mCurrentItem != n) {
                    ViewPager2.this.mCurrentItem = n;
                    ViewPager2.this.mAccessibilityProvider.onSetNewCurrentItem();
                }
            }
        };
        object2 = new OnPageChangeCallback(){

            @Override
            public void onPageSelected(int n) {
                ViewPager2.this.clearFocus();
                if (ViewPager2.this.hasFocus()) {
                    ViewPager2.this.mRecyclerView.requestFocus(2);
                }
            }
        };
        this.mPageChangeEventDispatcher.addOnPageChangeCallback((OnPageChangeCallback)object);
        this.mPageChangeEventDispatcher.addOnPageChangeCallback((OnPageChangeCallback)object2);
        this.mAccessibilityProvider.onInitialize(this.mPageChangeEventDispatcher, this.mRecyclerView);
        this.mPageChangeEventDispatcher.addOnPageChangeCallback(this.mExternalPageChangeCallbacks);
        this.mPageTransformerAdapter = object = new PageTransformerAdapter(this.mLayoutManager);
        this.mPageChangeEventDispatcher.addOnPageChangeCallback((OnPageChangeCallback)object);
        object = this.mRecyclerView;
        this.attachViewToParent((View)object, 0, object.getLayoutParams());
    }

    private void registerCurrentItemDataSetTracker(RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mCurrentItemDataSetChangeObserver);
        }
    }

    private void restorePendingState() {
        int n;
        if (this.mPendingCurrentItem == -1) {
            return;
        }
        RecyclerView.Adapter adapter = this.getAdapter();
        if (adapter == null) {
            return;
        }
        Parcelable parcelable = this.mPendingAdapterState;
        if (parcelable != null) {
            if (adapter instanceof StatefulAdapter) {
                ((StatefulAdapter)((Object)adapter)).restoreState(parcelable);
            }
            this.mPendingAdapterState = null;
        }
        this.mCurrentItem = n = Math.max(0, Math.min(this.mPendingCurrentItem, adapter.getItemCount() - 1));
        this.mPendingCurrentItem = -1;
        this.mRecyclerView.scrollToPosition(n);
        this.mAccessibilityProvider.onRestorePendingState();
    }

    private void setOrientation(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ViewPager2);
        if (Build.VERSION.SDK_INT >= 29) {
            this.saveAttributeDataForStyleable(context, R.styleable.ViewPager2, attributeSet, typedArray, 0, 0);
        }
        try {
            this.setOrientation(typedArray.getInt(R.styleable.ViewPager2_android_orientation, 0));
            return;
        }
        finally {
            typedArray.recycle();
        }
    }

    private void unregisterCurrentItemDataSetTracker(RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(this.mCurrentItemDataSetChangeObserver);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int n) {
        this.mRecyclerView.addItemDecoration(itemDecoration, n);
    }

    public boolean beginFakeDrag() {
        return this.mFakeDragger.beginFakeDrag();
    }

    public boolean canScrollHorizontally(int n) {
        return this.mRecyclerView.canScrollHorizontally(n);
    }

    public boolean canScrollVertically(int n) {
        return this.mRecyclerView.canScrollVertically(n);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = (Parcelable)sparseArray.get(this.getId());
        if (parcelable instanceof SavedState) {
            int n = ((SavedState)parcelable).mRecyclerViewId;
            sparseArray.put(this.mRecyclerView.getId(), sparseArray.get(n));
            sparseArray.remove(n);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        this.restorePendingState();
    }

    public boolean endFakeDrag() {
        return this.mFakeDragger.endFakeDrag();
    }

    public boolean fakeDragBy(float f) {
        return this.mFakeDragger.fakeDragBy(f);
    }

    public CharSequence getAccessibilityClassName() {
        if (this.mAccessibilityProvider.handlesGetAccessibilityClassName()) {
            return this.mAccessibilityProvider.onGetAccessibilityClassName();
        }
        return super.getAccessibilityClassName();
    }

    public RecyclerView.Adapter getAdapter() {
        return this.mRecyclerView.getAdapter();
    }

    public int getCurrentItem() {
        return this.mCurrentItem;
    }

    public RecyclerView.ItemDecoration getItemDecorationAt(int n) {
        return this.mRecyclerView.getItemDecorationAt(n);
    }

    public int getItemDecorationCount() {
        return this.mRecyclerView.getItemDecorationCount();
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getOrientation() {
        return this.mLayoutManager.getOrientation();
    }

    int getPageSize() {
        RecyclerView recyclerView = this.mRecyclerView;
        if (this.getOrientation() == 0) {
            return recyclerView.getWidth() - recyclerView.getPaddingLeft() - recyclerView.getPaddingRight();
        }
        return recyclerView.getHeight() - recyclerView.getPaddingTop() - recyclerView.getPaddingBottom();
    }

    public int getScrollState() {
        return this.mScrollEventAdapter.getScrollState();
    }

    public void invalidateItemDecorations() {
        this.mRecyclerView.invalidateItemDecorations();
    }

    public boolean isFakeDragging() {
        return this.mFakeDragger.isFakeDragging();
    }

    boolean isRtl() {
        if (this.mLayoutManager.getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    public boolean isUserInputEnabled() {
        return this.mUserInputEnabled;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.mAccessibilityProvider.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.mRecyclerView.getMeasuredWidth();
        int n6 = this.mRecyclerView.getMeasuredHeight();
        this.mTmpContainerRect.left = this.getPaddingLeft();
        this.mTmpContainerRect.right = n3 - n - this.getPaddingRight();
        this.mTmpContainerRect.top = this.getPaddingTop();
        this.mTmpContainerRect.bottom = n4 - n2 - this.getPaddingBottom();
        Gravity.apply((int)8388659, (int)n5, (int)n6, (Rect)this.mTmpContainerRect, (Rect)this.mTmpChildRect);
        this.mRecyclerView.layout(this.mTmpChildRect.left, this.mTmpChildRect.top, this.mTmpChildRect.right, this.mTmpChildRect.bottom);
        if (this.mCurrentItemDirty) {
            this.updateCurrentItem();
        }
    }

    protected void onMeasure(int n, int n2) {
        this.measureChild((View)this.mRecyclerView, n, n2);
        int n3 = this.mRecyclerView.getMeasuredWidth();
        int n4 = this.mRecyclerView.getMeasuredHeight();
        int n5 = this.mRecyclerView.getMeasuredState();
        int n6 = this.getPaddingLeft();
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingTop();
        int n9 = this.getPaddingBottom();
        n3 = Math.max(n3 + (n6 + n7), this.getSuggestedMinimumWidth());
        n4 = Math.max(n4 + (n8 + n9), this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(ViewPager2.resolveSizeAndState((int)n3, (int)n, (int)n5), ViewPager2.resolveSizeAndState((int)n4, (int)n2, (int)(n5 << 16)));
    }

    protected void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.mPendingCurrentItem = object.mCurrentItem;
        this.mPendingAdapterState = object.mAdapterState;
    }

    protected Parcelable onSaveInstanceState() {
        int n;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mRecyclerViewId = this.mRecyclerView.getId();
        int n2 = n = this.mPendingCurrentItem;
        if (n == -1) {
            n2 = this.mCurrentItem;
        }
        savedState.mCurrentItem = n2;
        Object object = this.mPendingAdapterState;
        if (object != null) {
            savedState.mAdapterState = object;
            return savedState;
        }
        object = this.mRecyclerView.getAdapter();
        if (object instanceof StatefulAdapter) {
            savedState.mAdapterState = ((StatefulAdapter)object).saveState();
        }
        return savedState;
    }

    public void onViewAdded(View object) {
        object = new StringBuilder();
        object.append(this.getClass().getSimpleName());
        object.append(" does not support direct child views");
        throw new IllegalStateException(object.toString());
    }

    public boolean performAccessibilityAction(int n, Bundle bundle) {
        if (this.mAccessibilityProvider.handlesPerformAccessibilityAction(n, bundle)) {
            return this.mAccessibilityProvider.onPerformAccessibilityAction(n, bundle);
        }
        return super.performAccessibilityAction(n, bundle);
    }

    public void registerOnPageChangeCallback(OnPageChangeCallback onPageChangeCallback) {
        this.mExternalPageChangeCallbacks.addOnPageChangeCallback(onPageChangeCallback);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.mRecyclerView.removeItemDecoration(itemDecoration);
    }

    public void removeItemDecorationAt(int n) {
        this.mRecyclerView.removeItemDecorationAt(n);
    }

    public void requestTransform() {
        if (this.mPageTransformerAdapter.getPageTransformer() == null) {
            return;
        }
        double d = this.mScrollEventAdapter.getRelativeScrollPosition();
        int n = (int)d;
        float f = (float)(d - (double)n);
        int n2 = Math.round((float)this.getPageSize() * f);
        this.mPageTransformerAdapter.onPageScrolled(n, f, n2);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter adapter2 = this.mRecyclerView.getAdapter();
        this.mAccessibilityProvider.onDetachAdapter(adapter2);
        this.unregisterCurrentItemDataSetTracker(adapter2);
        this.mRecyclerView.setAdapter(adapter);
        this.mCurrentItem = 0;
        this.restorePendingState();
        this.mAccessibilityProvider.onAttachAdapter(adapter);
        this.registerCurrentItemDataSetTracker(adapter);
    }

    public void setCurrentItem(int n) {
        this.setCurrentItem(n, true);
    }

    public void setCurrentItem(int n, boolean bl) {
        if (!this.isFakeDragging()) {
            this.setCurrentItemInternal(n, bl);
            return;
        }
        throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
    }

    void setCurrentItemInternal(int n, boolean bl) {
        Object object = this.getAdapter();
        if (object == null) {
            if (this.mPendingCurrentItem != -1) {
                this.mPendingCurrentItem = Math.max(n, 0);
            }
            return;
        }
        if (object.getItemCount() <= 0) {
            return;
        }
        int n2 = Math.min(Math.max(n, 0), object.getItemCount() - 1);
        if (n2 == this.mCurrentItem && this.mScrollEventAdapter.isIdle()) {
            return;
        }
        if (n2 == this.mCurrentItem && bl) {
            return;
        }
        double d = this.mCurrentItem;
        this.mCurrentItem = n2;
        this.mAccessibilityProvider.onSetNewCurrentItem();
        if (!this.mScrollEventAdapter.isIdle()) {
            d = this.mScrollEventAdapter.getRelativeScrollPosition();
        }
        this.mScrollEventAdapter.notifyProgrammaticScroll(n2, bl);
        if (!bl) {
            this.mRecyclerView.scrollToPosition(n2);
            return;
        }
        if (Math.abs((double)n2 - d) > 3.0) {
            object = this.mRecyclerView;
            n = (double)n2 > d ? n2 - 3 : n2 + 3;
            object.scrollToPosition(n);
            object = this.mRecyclerView;
            object.post((Runnable)new SmoothScrollToPosition(n2, (RecyclerView)object));
            return;
        }
        this.mRecyclerView.smoothScrollToPosition(n2);
    }

    public void setLayoutDirection(int n) {
        super.setLayoutDirection(n);
        this.mAccessibilityProvider.onSetLayoutDirection();
    }

    public void setOffscreenPageLimit(int n) {
        if (n < 1 && n != -1) {
            throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
        }
        this.mOffscreenPageLimit = n;
        this.mRecyclerView.requestLayout();
    }

    public void setOrientation(int n) {
        this.mLayoutManager.setOrientation(n);
        this.mAccessibilityProvider.onSetOrientation();
    }

    public void setPageTransformer(PageTransformer pageTransformer) {
        if (pageTransformer != null) {
            if (!this.mSavedItemAnimatorPresent) {
                this.mSavedItemAnimator = this.mRecyclerView.getItemAnimator();
                this.mSavedItemAnimatorPresent = true;
            }
            this.mRecyclerView.setItemAnimator(null);
        } else if (this.mSavedItemAnimatorPresent) {
            this.mRecyclerView.setItemAnimator(this.mSavedItemAnimator);
            this.mSavedItemAnimator = null;
            this.mSavedItemAnimatorPresent = false;
        }
        if (pageTransformer == this.mPageTransformerAdapter.getPageTransformer()) {
            return;
        }
        this.mPageTransformerAdapter.setPageTransformer(pageTransformer);
        this.requestTransform();
    }

    public void setUserInputEnabled(boolean bl) {
        this.mUserInputEnabled = bl;
        this.mAccessibilityProvider.onSetUserInputEnabled();
    }

    void snapToPage() {
        int[] arrn = this.mPagerSnapHelper.findSnapView(this.mLayoutManager);
        if (arrn == null) {
            return;
        }
        if ((arrn = this.mPagerSnapHelper.calculateDistanceToFinalSnap(this.mLayoutManager, (View)arrn))[0] != 0 || arrn[1] != 0) {
            this.mRecyclerView.smoothScrollBy(arrn[0], arrn[1]);
        }
    }

    public void unregisterOnPageChangeCallback(OnPageChangeCallback onPageChangeCallback) {
        this.mExternalPageChangeCallbacks.removeOnPageChangeCallback(onPageChangeCallback);
    }

    void updateCurrentItem() {
        PagerSnapHelper pagerSnapHelper = this.mPagerSnapHelper;
        if (pagerSnapHelper != null) {
            if ((pagerSnapHelper = pagerSnapHelper.findSnapView(this.mLayoutManager)) == null) {
                return;
            }
            int n = this.mLayoutManager.getPosition((View)pagerSnapHelper);
            if (n != this.mCurrentItem && this.getScrollState() == 0) {
                this.mPageChangeEventDispatcher.onPageSelected(n);
            }
            this.mCurrentItemDirty = false;
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    private abstract class AccessibilityProvider {
        private AccessibilityProvider() {
        }

        boolean handlesGetAccessibilityClassName() {
            return false;
        }

        boolean handlesLmPerformAccessibilityAction(int n) {
            return false;
        }

        boolean handlesPerformAccessibilityAction(int n, Bundle bundle) {
            return false;
        }

        boolean handlesRvGetAccessibilityClassName() {
            return false;
        }

        void onAttachAdapter(RecyclerView.Adapter<?> adapter) {
        }

        void onDetachAdapter(RecyclerView.Adapter<?> adapter) {
        }

        String onGetAccessibilityClassName() {
            throw new IllegalStateException("Not implemented.");
        }

        void onInitialize(CompositeOnPageChangeCallback compositeOnPageChangeCallback, RecyclerView recyclerView) {
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        }

        void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        }

        boolean onLmPerformAccessibilityAction(int n) {
            throw new IllegalStateException("Not implemented.");
        }

        boolean onPerformAccessibilityAction(int n, Bundle bundle) {
            throw new IllegalStateException("Not implemented.");
        }

        void onRestorePendingState() {
        }

        CharSequence onRvGetAccessibilityClassName() {
            throw new IllegalStateException("Not implemented.");
        }

        void onRvInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        }

        void onSetLayoutDirection() {
        }

        void onSetNewCurrentItem() {
        }

        void onSetOrientation() {
        }

        void onSetUserInputEnabled() {
        }
    }

    class BasicAccessibilityProvider
    extends AccessibilityProvider {
        BasicAccessibilityProvider() {
        }

        @Override
        public boolean handlesLmPerformAccessibilityAction(int n) {
            if (!(n != 8192 && n != 4096 || ViewPager2.this.isUserInputEnabled())) {
                return true;
            }
            return false;
        }

        @Override
        public boolean handlesRvGetAccessibilityClassName() {
            return true;
        }

        @Override
        public void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (!ViewPager2.this.isUserInputEnabled()) {
                accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
                accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
                accessibilityNodeInfoCompat.setScrollable(false);
            }
        }

        @Override
        public boolean onLmPerformAccessibilityAction(int n) {
            if (this.handlesLmPerformAccessibilityAction(n)) {
                return false;
            }
            throw new IllegalStateException();
        }

        @Override
        public CharSequence onRvGetAccessibilityClassName() {
            if (this.handlesRvGetAccessibilityClassName()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }
    }

    private static abstract class DataSetChangeObserver
    extends RecyclerView.AdapterDataObserver {
        private DataSetChangeObserver() {
        }

        @Override
        public abstract void onChanged();

        @Override
        public final void onItemRangeChanged(int n, int n2) {
            this.onChanged();
        }

        @Override
        public final void onItemRangeChanged(int n, int n2, Object object) {
            this.onChanged();
        }

        @Override
        public final void onItemRangeInserted(int n, int n2) {
            this.onChanged();
        }

        @Override
        public final void onItemRangeMoved(int n, int n2, int n3) {
            this.onChanged();
        }

        @Override
        public final void onItemRangeRemoved(int n, int n2) {
            this.onChanged();
        }
    }

    private class LinearLayoutManagerImpl
    extends LinearLayoutManager {
        LinearLayoutManagerImpl(Context context) {
            super(context);
        }

        @Override
        protected void calculateExtraLayoutSpace(RecyclerView.State state, int[] arrn) {
            int n = ViewPager2.this.getOffscreenPageLimit();
            if (n == -1) {
                super.calculateExtraLayoutSpace(state, arrn);
                return;
            }
            arrn[0] = n = ViewPager2.this.getPageSize() * n;
            arrn[1] = n;
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(recycler, state, accessibilityNodeInfoCompat);
            ViewPager2.this.mAccessibilityProvider.onLmInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
        }

        @Override
        public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int n, Bundle bundle) {
            if (ViewPager2.this.mAccessibilityProvider.handlesLmPerformAccessibilityAction(n)) {
                return ViewPager2.this.mAccessibilityProvider.onLmPerformAccessibilityAction(n);
            }
            return super.performAccessibilityAction(recycler, state, n, bundle);
        }

        @Override
        public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean bl, boolean bl2) {
            return false;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface OffscreenPageLimit {
    }

    public static abstract class OnPageChangeCallback {
        public void onPageScrollStateChanged(int n) {
        }

        public void onPageScrolled(int n, float f, int n2) {
        }

        public void onPageSelected(int n) {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Orientation {
    }

    class PageAwareAccessibilityProvider
    extends AccessibilityProvider {
        private final AccessibilityViewCommand mActionPageBackward;
        private final AccessibilityViewCommand mActionPageForward;
        private RecyclerView.AdapterDataObserver mAdapterDataObserver;

        PageAwareAccessibilityProvider() {
            this.mActionPageForward = new AccessibilityViewCommand(){

                @Override
                public boolean perform(View object, AccessibilityViewCommand.CommandArguments commandArguments) {
                    object = (ViewPager2)((Object)object);
                    PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(object.getCurrentItem() + 1);
                    return true;
                }
            };
            this.mActionPageBackward = new AccessibilityViewCommand(){

                @Override
                public boolean perform(View object, AccessibilityViewCommand.CommandArguments commandArguments) {
                    object = (ViewPager2)((Object)object);
                    PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(object.getCurrentItem() - 1);
                    return true;
                }
            };
        }

        private void addCollectionInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            int n = 0;
            int n2 = 0;
            int n3 = n;
            int n4 = n2;
            if (ViewPager2.this.getAdapter() != null) {
                if (ViewPager2.this.getOrientation() == 1) {
                    n3 = ViewPager2.this.getAdapter().getItemCount();
                    n4 = n2;
                } else {
                    n4 = ViewPager2.this.getAdapter().getItemCount();
                    n3 = n;
                }
            }
            AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(n3, n4, false, 0));
        }

        private void addScrollActions(AccessibilityNodeInfo accessibilityNodeInfo) {
            RecyclerView.Adapter adapter = ViewPager2.this.getAdapter();
            if (adapter == null) {
                return;
            }
            int n = adapter.getItemCount();
            if (n != 0) {
                if (!ViewPager2.this.isUserInputEnabled()) {
                    return;
                }
                if (ViewPager2.this.mCurrentItem > 0) {
                    accessibilityNodeInfo.addAction(8192);
                }
                if (ViewPager2.this.mCurrentItem < n - 1) {
                    accessibilityNodeInfo.addAction(4096);
                }
                accessibilityNodeInfo.setScrollable(true);
                return;
            }
        }

        @Override
        public boolean handlesGetAccessibilityClassName() {
            return true;
        }

        @Override
        public boolean handlesPerformAccessibilityAction(int n, Bundle bundle) {
            if (n != 8192 && n != 4096) {
                return false;
            }
            return true;
        }

        @Override
        public void onAttachAdapter(RecyclerView.Adapter<?> adapter) {
            this.updatePageAccessibilityActions();
            if (adapter != null) {
                adapter.registerAdapterDataObserver(this.mAdapterDataObserver);
            }
        }

        @Override
        public void onDetachAdapter(RecyclerView.Adapter<?> adapter) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(this.mAdapterDataObserver);
            }
        }

        @Override
        public String onGetAccessibilityClassName() {
            if (this.handlesGetAccessibilityClassName()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }

        @Override
        public void onInitialize(CompositeOnPageChangeCallback compositeOnPageChangeCallback, RecyclerView recyclerView) {
            ViewCompat.setImportantForAccessibility((View)recyclerView, 2);
            this.mAdapterDataObserver = new DataSetChangeObserver(){

                @Override
                public void onChanged() {
                    PageAwareAccessibilityProvider.this.updatePageAccessibilityActions();
                }
            };
            if (ViewCompat.getImportantForAccessibility((View)ViewPager2.this) == 0) {
                ViewCompat.setImportantForAccessibility((View)ViewPager2.this, 1);
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            this.addCollectionInfo(accessibilityNodeInfo);
            if (Build.VERSION.SDK_INT >= 16) {
                this.addScrollActions(accessibilityNodeInfo);
            }
        }

        @Override
        public boolean onPerformAccessibilityAction(int n, Bundle bundle) {
            if (this.handlesPerformAccessibilityAction(n, bundle)) {
                n = n == 8192 ? ViewPager2.this.getCurrentItem() - 1 : ViewPager2.this.getCurrentItem() + 1;
                this.setCurrentItemFromAccessibilityCommand(n);
                return true;
            }
            throw new IllegalStateException();
        }

        @Override
        public void onRestorePendingState() {
            this.updatePageAccessibilityActions();
        }

        @Override
        public void onRvInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setSource((View)ViewPager2.this);
            accessibilityEvent.setClassName((CharSequence)this.onGetAccessibilityClassName());
        }

        @Override
        public void onSetLayoutDirection() {
            this.updatePageAccessibilityActions();
        }

        @Override
        public void onSetNewCurrentItem() {
            this.updatePageAccessibilityActions();
        }

        @Override
        public void onSetOrientation() {
            this.updatePageAccessibilityActions();
        }

        @Override
        public void onSetUserInputEnabled() {
            this.updatePageAccessibilityActions();
            if (Build.VERSION.SDK_INT < 21) {
                ViewPager2.this.sendAccessibilityEvent(2048);
            }
        }

        void setCurrentItemFromAccessibilityCommand(int n) {
            if (ViewPager2.this.isUserInputEnabled()) {
                ViewPager2.this.setCurrentItemInternal(n, true);
            }
        }

        void updatePageAccessibilityActions() {
            ViewPager2 viewPager2 = ViewPager2.this;
            int n = 16908360;
            ViewCompat.removeAccessibilityAction((View)viewPager2, 16908360);
            ViewCompat.removeAccessibilityAction((View)viewPager2, 16908361);
            ViewCompat.removeAccessibilityAction((View)viewPager2, 16908358);
            ViewCompat.removeAccessibilityAction((View)viewPager2, 16908359);
            if (ViewPager2.this.getAdapter() == null) {
                return;
            }
            int n2 = ViewPager2.this.getAdapter().getItemCount();
            if (n2 == 0) {
                return;
            }
            if (!ViewPager2.this.isUserInputEnabled()) {
                return;
            }
            if (ViewPager2.this.getOrientation() == 0) {
                boolean bl = ViewPager2.this.isRtl();
                int n3 = bl ? 16908360 : 16908361;
                if (bl) {
                    n = 16908361;
                }
                if (ViewPager2.this.mCurrentItem < n2 - 1) {
                    ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(n3, null), null, this.mActionPageForward);
                }
                if (ViewPager2.this.mCurrentItem > 0) {
                    ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(n, null), null, this.mActionPageBackward);
                }
                return;
            }
            if (ViewPager2.this.mCurrentItem < n2 - 1) {
                ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908359, null), null, this.mActionPageForward);
            }
            if (ViewPager2.this.mCurrentItem > 0) {
                ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908358, null), null, this.mActionPageBackward);
            }
        }

    }

    public static interface PageTransformer {
        public void transformPage(View var1, float var2);
    }

    private class PagerSnapHelperImpl
    extends PagerSnapHelper {
        PagerSnapHelperImpl() {
        }

        @Override
        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            if (ViewPager2.this.isFakeDragging()) {
                return null;
            }
            return super.findSnapView(layoutManager);
        }
    }

    private class RecyclerViewImpl
    extends RecyclerView {
        RecyclerViewImpl(Context context) {
            super(context);
        }

        @Override
        public CharSequence getAccessibilityClassName() {
            if (ViewPager2.this.mAccessibilityProvider.handlesRvGetAccessibilityClassName()) {
                return ViewPager2.this.mAccessibilityProvider.onRvGetAccessibilityClassName();
            }
            return super.getAccessibilityClassName();
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setFromIndex(ViewPager2.this.mCurrentItem);
            accessibilityEvent.setToIndex(ViewPager2.this.mCurrentItem);
            ViewPager2.this.mAccessibilityProvider.onRvInitializeAccessibilityEvent(accessibilityEvent);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (ViewPager2.this.isUserInputEnabled() && super.onInterceptTouchEvent(motionEvent)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (ViewPager2.this.isUserInputEnabled() && super.onTouchEvent(motionEvent)) {
                return true;
            }
            return false;
        }
    }

    static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return this.createFromParcel(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                if (Build.VERSION.SDK_INT >= 24) {
                    return new SavedState(parcel, classLoader);
                }
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Parcelable mAdapterState;
        int mCurrentItem;
        int mRecyclerViewId;

        SavedState(Parcel parcel) {
            super(parcel);
            this.readValues(parcel, null);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.readValues(parcel, classLoader);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void readValues(Parcel parcel, ClassLoader classLoader) {
            this.mRecyclerViewId = parcel.readInt();
            this.mCurrentItem = parcel.readInt();
            this.mAdapterState = parcel.readParcelable(classLoader);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mRecyclerViewId);
            parcel.writeInt(this.mCurrentItem);
            parcel.writeParcelable(this.mAdapterState, n);
        }

    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ScrollState {
    }

    private static class SmoothScrollToPosition
    implements Runnable {
        private final int mPosition;
        private final RecyclerView mRecyclerView;

        SmoothScrollToPosition(int n, RecyclerView recyclerView) {
            this.mPosition = n;
            this.mRecyclerView = recyclerView;
        }

        @Override
        public void run() {
            this.mRecyclerView.smoothScrollToPosition(this.mPosition);
        }
    }

}

