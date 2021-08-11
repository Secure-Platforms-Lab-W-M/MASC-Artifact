/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.database.Observable
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.StateListDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.FocusFinder
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.animation.Interpolator
 *  android.widget.EdgeEffect
 *  android.widget.OverScroller
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.AdapterHelper;
import android.support.v7.widget.ChildHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.FastScroller;
import android.support.v7.widget.GapWorker;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.support.v7.widget.ViewBoundsCheck;
import android.support.v7.widget.ViewInfoStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView
extends ViewGroup
implements ScrollingView,
NestedScrollingChild2 {
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
    private static final boolean ALLOW_THREAD_GAP_WORK;
    private static final int[] CLIP_TO_PADDING_ATTR;
    static final boolean DEBUG = false;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    static final long FOREVER_NS = Long.MAX_VALUE;
    public static final int HORIZONTAL = 0;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int[] NESTED_SCROLLING_ATTRS;
    public static final long NO_ID = -1L;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    static final Interpolator sQuinticInterpolator;
    RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private EdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    boolean mClipToPadding;
    boolean mDataSetHasChangedAfterLayout;
    private int mDispatchScrollCounter;
    private int mEatRequestLayout;
    private int mEatenAccessibilityChangeFlags;
    boolean mEnableFastScroller;
    @VisibleForTesting
    boolean mFirstLayoutComplete;
    GapWorker mGapWorker;
    boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    @VisibleForTesting
    LayoutManager mLayout;
    boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    boolean mLayoutRequestEaten;
    private EdgeEffect mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    @VisibleForTesting
    final List<ViewHolder> mPendingAccessibilityImportanceChange;
    private SavedState mPendingSavedState;
    boolean mPostedAnimatorRunner;
    GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
    private boolean mPreserveFocusAfterLayout;
    final Recycler mRecycler;
    RecyclerListener mRecyclerListener;
    private EdgeEffect mRightGlow;
    private float mScaledHorizontalScrollFactor;
    private float mScaledVerticalScrollFactor;
    private final int[] mScrollConsumed;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    final Rect mTempRect;
    private final Rect mTempRect2;
    final RectF mTempRectF;
    private EdgeEffect mTopGlow;
    private int mTouchSlop;
    final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;

    static {
        NESTED_SCROLLING_ATTRS = new int[]{16843830};
        CLIP_TO_PADDING_ATTR = new int[]{16842987};
        boolean bl = Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20;
        FORCE_INVALIDATE_DISPLAY_LIST = bl;
        bl = Build.VERSION.SDK_INT >= 23;
        ALLOW_SIZE_IN_UNSPECIFIED_SPEC = bl;
        bl = Build.VERSION.SDK_INT >= 16;
        POST_UPDATES_ON_ANIMATION = bl;
        bl = Build.VERSION.SDK_INT >= 21;
        ALLOW_THREAD_GAP_WORK = bl;
        bl = Build.VERSION.SDK_INT <= 15;
        FORCE_ABS_FOCUS_SEARCH_DIRECTION = bl;
        bl = Build.VERSION.SDK_INT <= 15;
        IGNORE_DETACHED_FOCUSED_CHILD = bl;
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
        sQuinticInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable(){

            @Override
            public void run() {
                if (RecyclerView.this.mFirstLayoutComplete) {
                    if (RecyclerView.this.isLayoutRequested()) {
                        return;
                    }
                    if (!RecyclerView.this.mIsAttached) {
                        RecyclerView.this.requestLayout();
                        return;
                    }
                    if (RecyclerView.this.mLayoutFrozen) {
                        RecyclerView.this.mLayoutRequestEaten = true;
                        return;
                    }
                    RecyclerView.this.consumePendingUpdateOperations();
                    return;
                }
            }
        };
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mItemDecorations = new ArrayList();
        this.mOnItemTouchListeners = new ArrayList();
        this.mEatRequestLayout = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = ALLOW_THREAD_GAP_WORK ? new GapWorker.LayoutPrefetchRegistryImpl() : null;
        this.mPrefetchRegistry = layoutPrefetchRegistryImpl;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList<ViewHolder>();
        this.mItemAnimatorRunner = new Runnable(){

            @Override
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback(){

            @Override
            public void processAppeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo, ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }

            @Override
            public void processDisappeared(ViewHolder viewHolder, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @Nullable ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }

            @Override
            public void processPersistent(ViewHolder viewHolder, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, itemHolderInfo, itemHolderInfo2)) {
                        RecyclerView.this.postAnimationRunner();
                        return;
                    }
                    return;
                }
                if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, itemHolderInfo, itemHolderInfo2)) {
                    RecyclerView.this.postAnimationRunner();
                    return;
                }
            }

            @Override
            public void unused(ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        if (attributeSet != null) {
            layoutPrefetchRegistryImpl = context.obtainStyledAttributes(attributeSet, CLIP_TO_PADDING_ATTR, n, 0);
            this.mClipToPadding = layoutPrefetchRegistryImpl.getBoolean(0, true);
            layoutPrefetchRegistryImpl.recycle();
        } else {
            this.mClipToPadding = true;
        }
        this.setScrollContainer(true);
        this.setFocusableInTouchMode(true);
        layoutPrefetchRegistryImpl = ViewConfiguration.get((Context)context);
        this.mTouchSlop = layoutPrefetchRegistryImpl.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor((ViewConfiguration)layoutPrefetchRegistryImpl, context);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor((ViewConfiguration)layoutPrefetchRegistryImpl, context);
        this.mMinFlingVelocity = layoutPrefetchRegistryImpl.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = layoutPrefetchRegistryImpl.getScaledMaximumFlingVelocity();
        boolean bl = this.getOverScrollMode() == 2;
        this.setWillNotDraw(bl);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        this.initAdapterManager();
        this.initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
        this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        boolean bl2 = true;
        bl = true;
        if (attributeSet != null) {
            layoutPrefetchRegistryImpl = context.obtainStyledAttributes(attributeSet, R.styleable.RecyclerView, n, 0);
            String string2 = layoutPrefetchRegistryImpl.getString(R.styleable.RecyclerView_layoutManager);
            if (layoutPrefetchRegistryImpl.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1) {
                this.setDescendantFocusability(262144);
            }
            this.mEnableFastScroller = layoutPrefetchRegistryImpl.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
            if (this.mEnableFastScroller) {
                this.initFastScroller((StateListDrawable)layoutPrefetchRegistryImpl.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), layoutPrefetchRegistryImpl.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)layoutPrefetchRegistryImpl.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), layoutPrefetchRegistryImpl.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
            }
            layoutPrefetchRegistryImpl.recycle();
            this.createLayoutManager(context, string2, attributeSet, n, 0);
            if (Build.VERSION.SDK_INT >= 21) {
                context = context.obtainStyledAttributes(attributeSet, NESTED_SCROLLING_ATTRS, n, 0);
                bl = context.getBoolean(0, true);
                context.recycle();
            }
        } else {
            this.setDescendantFocusability(262144);
            bl = bl2;
        }
        this.setNestedScrollingEnabled(bl);
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean bl = view.getParent() == this;
        this.mRecycler.unscrapView(this.getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
            return;
        }
        if (!bl) {
            this.mChildHelper.addView(view, true);
            return;
        }
        this.mChildHelper.hide(view);
    }

    private void animateChange(@NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2, boolean bl, boolean bl2) {
        viewHolder.setIsRecyclable(false);
        if (bl) {
            this.addAnimatingView(viewHolder);
        }
        if (viewHolder != viewHolder2) {
            if (bl2) {
                this.addAnimatingView(viewHolder2);
            }
            viewHolder.mShadowedHolder = viewHolder2;
            this.addAnimatingView(viewHolder);
            this.mRecycler.unscrapView(viewHolder);
            viewHolder2.setIsRecyclable(false);
            viewHolder2.mShadowingHolder = viewHolder;
        }
        if (this.mItemAnimator.animateChange(viewHolder, viewHolder2, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
            return;
        }
    }

    private void cancelTouch() {
        this.resetTouch();
        this.setScrollState(0);
    }

    static void clearNestedRecyclerViewIfNotNested(@NonNull ViewHolder viewHolder) {
        if (viewHolder.mNestedRecyclerView != null) {
            View view = (View)viewHolder.mNestedRecyclerView.get();
            while (view != null) {
                if (view == viewHolder.itemView) {
                    return;
                }
                if ((view = view.getParent()) instanceof View) continue;
                view = null;
            }
            viewHolder.mNestedRecyclerView = null;
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    private void createLayoutManager(Context var1_1, String var2_8, AttributeSet var3_9, int var4_10, int var5_11) {
        block11 : {
            block12 : {
                if (var2_8 == null) break block11;
                if ((var2_8 = var2_8.trim()).isEmpty()) break block12;
                var7_12 = this.getFullClassName((Context)var1_1, (String)var2_8);
                var2_8 = this.isInEditMode() != false ? this.getClass().getClassLoader() : var1_1.getClassLoader();
                var8_13 = var2_8.loadClass(var7_12).asSubclass(LayoutManager.class);
                var2_8 = null;
                var6_14 = var8_13.getConstructor(RecyclerView.LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                var2_8 = new Object[]{var1_1, var3_9, var4_10, var5_11};
                var1_1 = var6_14;
                ** GOTO lbl24
                {
                    catch (NoSuchMethodException var6_15) {
                        try {
                            try {
                                var1_1 = var8_13.getConstructor(new Class[0]);
                            }
                            catch (NoSuchMethodException var1_2) {
                                var1_2.initCause(var6_15);
                                var2_8 = new StringBuilder();
                                var2_8.append(var3_9.getPositionDescription());
                                var2_8.append(": Error creating LayoutManager ");
                                var2_8.append(var7_12);
                                throw new IllegalStateException(var2_8.toString(), var1_2);
                            }
lbl24: // 2 sources:
                            var1_1.setAccessible(true);
                            this.setLayoutManager(var1_1.newInstance((Object[])var2_8));
                            return;
                        }
                        catch (ClassCastException var1_3) {
                            var2_8 = new StringBuilder();
                            var2_8.append(var3_9.getPositionDescription());
                            var2_8.append(": Class is not a LayoutManager ");
                            var2_8.append(var7_12);
                            throw new IllegalStateException(var2_8.toString(), var1_3);
                        }
                        catch (IllegalAccessException var1_4) {
                            var2_8 = new StringBuilder();
                            var2_8.append(var3_9.getPositionDescription());
                            var2_8.append(": Cannot access non-public constructor ");
                            var2_8.append(var7_12);
                            throw new IllegalStateException(var2_8.toString(), var1_4);
                        }
                        catch (InstantiationException var1_5) {
                            var2_8 = new StringBuilder();
                            var2_8.append(var3_9.getPositionDescription());
                            var2_8.append(": Could not instantiate the LayoutManager: ");
                            var2_8.append(var7_12);
                            throw new IllegalStateException(var2_8.toString(), var1_5);
                        }
                        catch (InvocationTargetException var1_6) {
                            var2_8 = new StringBuilder();
                            var2_8.append(var3_9.getPositionDescription());
                            var2_8.append(": Could not instantiate the LayoutManager: ");
                            var2_8.append(var7_12);
                            throw new IllegalStateException(var2_8.toString(), var1_6);
                        }
                        catch (ClassNotFoundException var1_7) {
                            var2_8 = new StringBuilder();
                            var2_8.append(var3_9.getPositionDescription());
                            var2_8.append(": Unable to find LayoutManager ");
                            var2_8.append(var7_12);
                            throw new IllegalStateException(var2_8.toString(), var1_7);
                        }
                    }
                }
            }
            return;
        }
    }

    private boolean didChildRangeChange(int n, int n2) {
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        int[] arrn = this.mMinMaxLayoutPositions;
        if (arrn[0] == n && arrn[1] == n2) {
            return false;
        }
        return true;
    }

    private void dispatchContentChangedIfNecessary() {
        int n = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (n != 0 && this.isAccessibilityEnabled()) {
            AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
            accessibilityEvent.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, n);
            this.sendAccessibilityEventUnchecked(accessibilityEvent);
            return;
        }
    }

    private void dispatchLayoutStep1() {
        int n;
        int n2;
        ItemAnimator.ItemHolderInfo itemHolderInfo;
        Object object = this.mState;
        boolean bl = true;
        object.assertLayoutStep(1);
        this.fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        this.eatRequestLayout();
        this.mViewInfoStore.clear();
        this.onEnterLayoutOrScroll();
        this.processAdapterUpdatesAndSetAnimationFlags();
        this.saveFocusInfo();
        object = this.mState;
        if (!object.mRunSimpleAnimations || !this.mItemsChanged) {
            bl = false;
        }
        object.mTrackOldChangeHolders = bl;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        object = this.mState;
        object.mInPreLayout = object.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            n2 = this.mChildHelper.getChildCount();
            for (n = 0; n < n2; ++n) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n));
                if (object.shouldIgnore() || object.isInvalid() && !this.mAdapter.hasStableIds()) continue;
                itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, (ViewHolder)object, ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object), object.getUnmodifiedPayloads());
                this.mViewInfoStore.addToPreLayout((ViewHolder)object, itemHolderInfo);
                if (!this.mState.mTrackOldChangeHolders || !object.isUpdated() || object.isRemoved() || object.shouldIgnore() || object.isInvalid()) continue;
                long l = this.getChangedHolderKey((ViewHolder)object);
                this.mViewInfoStore.addToOldChangeHolders(l, (ViewHolder)object);
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            this.saveOldPositions();
            bl = this.mState.mStructureChanged;
            object = this.mState;
            object.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, (State)object);
            this.mState.mStructureChanged = bl;
            for (n = 0; n < this.mChildHelper.getChildCount(); ++n) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n));
                if (object.shouldIgnore() || this.mViewInfoStore.isInPreLayout((ViewHolder)object)) continue;
                n2 = ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object);
                bl = object.hasAnyOfTheFlags(8192);
                if (!bl) {
                    n2 |= 4096;
                }
                itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, (ViewHolder)object, n2, object.getUnmodifiedPayloads());
                if (bl) {
                    this.recordAnimationInfoIfBouncedHiddenView((ViewHolder)object, itemHolderInfo);
                    continue;
                }
                this.mViewInfoStore.addToAppearedInPreLayoutHolders((ViewHolder)object, itemHolderInfo);
            }
            this.clearOldPositions();
        } else {
            this.clearOldPositions();
        }
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }

    private void dispatchLayoutStep2() {
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        State state = this.mState;
        state.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        state.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, state);
        state = this.mState;
        state.mStructureChanged = false;
        this.mPendingSavedState = null;
        boolean bl = state.mRunSimpleAnimations && this.mItemAnimator != null;
        state.mRunSimpleAnimations = bl;
        this.mState.mLayoutStep = 4;
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
    }

    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        Object object = this.mState;
        object.mLayoutStep = 1;
        if (object.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (object.shouldIgnore()) continue;
                long l = this.getChangedHolderKey((ViewHolder)object);
                ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, (ViewHolder)object);
                ViewHolder viewHolder = this.mViewInfoStore.getFromOldChangeHolders(l);
                if (viewHolder != null && !viewHolder.shouldIgnore()) {
                    boolean bl = this.mViewInfoStore.isDisappearing(viewHolder);
                    boolean bl2 = this.mViewInfoStore.isDisappearing((ViewHolder)object);
                    if (bl && viewHolder == object) {
                        this.mViewInfoStore.addToPostLayout((ViewHolder)object, itemHolderInfo);
                        continue;
                    }
                    ItemAnimator.ItemHolderInfo itemHolderInfo2 = this.mViewInfoStore.popFromPreLayout(viewHolder);
                    this.mViewInfoStore.addToPostLayout((ViewHolder)object, itemHolderInfo);
                    itemHolderInfo = this.mViewInfoStore.popFromPostLayout((ViewHolder)object);
                    if (itemHolderInfo2 == null) {
                        this.handleMissingPreInfoForChangeError(l, (ViewHolder)object, viewHolder);
                        continue;
                    }
                    this.animateChange(viewHolder, (ViewHolder)object, itemHolderInfo2, itemHolderInfo, bl, bl2);
                    continue;
                }
                this.mViewInfoStore.addToPostLayout((ViewHolder)object, itemHolderInfo);
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        object = this.mState;
        object.mPreviousLayoutItemCount = object.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        object = this.mState;
        object.mRunSimpleAnimations = false;
        object.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            object = this.mLayout;
            object.mPrefetchMaxCountObserved = 0;
            object.mPrefetchMaxObservedInInitialPrefetch = false;
            this.mRecycler.updateViewCacheSize();
        }
        this.mLayout.onLayoutCompleted(this.mState);
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
        this.mViewInfoStore.clear();
        object = this.mMinMaxLayoutPositions;
        if (this.didChildRangeChange(object[0], object[1])) {
            this.dispatchOnScrolled(0, 0);
        }
        this.recoverFocusFromState();
        this.resetFocusInfo();
    }

    private boolean dispatchOnItemTouch(MotionEvent motionEvent) {
        int n = motionEvent.getAction();
        OnItemTouchListener onItemTouchListener = this.mActiveOnItemTouchListener;
        if (onItemTouchListener != null) {
            if (n == 0) {
                this.mActiveOnItemTouchListener = null;
            } else {
                onItemTouchListener.onTouchEvent(this, motionEvent);
                if (n != 3 && n != 1) {
                    return true;
                }
                this.mActiveOnItemTouchListener = null;
                return true;
            }
        }
        if (n != 0) {
            int n2 = this.mOnItemTouchListeners.size();
            for (n = 0; n < n2; ++n) {
                onItemTouchListener = this.mOnItemTouchListeners.get(n);
                if (!onItemTouchListener.onInterceptTouchEvent(this, motionEvent)) continue;
                this.mActiveOnItemTouchListener = onItemTouchListener;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent motionEvent) {
        int n = motionEvent.getAction();
        if (n == 3 || n == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        int n2 = this.mOnItemTouchListeners.size();
        for (int i = 0; i < n2; ++i) {
            OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (!onItemTouchListener.onInterceptTouchEvent(this, motionEvent) || n == 3) continue;
            this.mActiveOnItemTouchListener = onItemTouchListener;
            return true;
        }
        return false;
    }

    private void findMinMaxChildLayoutPositions(int[] arrn) {
        int n = this.mChildHelper.getChildCount();
        if (n == 0) {
            arrn[0] = -1;
            arrn[1] = -1;
            return;
        }
        int n2 = Integer.MAX_VALUE;
        int n3 = Integer.MIN_VALUE;
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (viewHolder.shouldIgnore()) continue;
            int n4 = viewHolder.getLayoutPosition();
            if (n4 < n2) {
                n2 = n4;
            }
            if (n4 <= n3) continue;
            n3 = n4;
        }
        arrn[0] = n2;
        arrn[1] = n3;
    }

    @Nullable
    static RecyclerView findNestedRecyclerView(@NonNull View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView)view;
        }
        view = (ViewGroup)view;
        int n = view.getChildCount();
        for (int i = 0; i < n; ++i) {
            RecyclerView recyclerView = RecyclerView.findNestedRecyclerView(view.getChildAt(i));
            if (recyclerView == null) continue;
            return recyclerView;
        }
        return null;
    }

    @Nullable
    private View findNextViewToFocus() {
        ViewHolder viewHolder;
        int n = this.mState.mFocusedItemPosition != -1 ? this.mState.mFocusedItemPosition : 0;
        int n2 = this.mState.getItemCount();
        for (int i = n; i < n2 && (viewHolder = this.findViewHolderForAdapterPosition(i)) != null; ++i) {
            if (!viewHolder.itemView.hasFocusable()) continue;
            return viewHolder.itemView;
        }
        for (n = Math.min((int)n2, (int)n) - 1; n >= 0; --n) {
            viewHolder = this.findViewHolderForAdapterPosition(n);
            if (viewHolder == null) {
                return null;
            }
            if (!viewHolder.itemView.hasFocusable()) continue;
            return viewHolder.itemView;
        }
        return null;
    }

    static ViewHolder getChildViewHolderInt(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).mViewHolder;
    }

    static void getDecoratedBoundsWithMarginsInt(View view, Rect rect) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect2 = layoutParams.mDecorInsets;
        rect.set(view.getLeft() - rect2.left - layoutParams.leftMargin, view.getTop() - rect2.top - layoutParams.topMargin, view.getRight() + rect2.right + layoutParams.rightMargin, view.getBottom() + rect2.bottom + layoutParams.bottomMargin);
    }

    private int getDeepestFocusedViewWithId(View view) {
        int n = view.getId();
        while (!view.isFocused() && view instanceof ViewGroup && view.hasFocus()) {
            if ((view = ((ViewGroup)view).getFocusedChild()).getId() == -1) continue;
            n = view.getId();
        }
        return n;
    }

    private String getFullClassName(Context object, String string2) {
        if (string2.charAt(0) == '.') {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(object.getPackageName());
            stringBuilder.append(string2);
            return stringBuilder.toString();
        }
        if (string2.contains(".")) {
            return string2;
        }
        object = new StringBuilder();
        object.append(RecyclerView.class.getPackage().getName());
        object.append('.');
        object.append(string2);
        return object.toString();
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        }
        return this.mScrollingChildHelper;
    }

    private void handleMissingPreInfoForChangeError(long l, ViewHolder viewHolder, ViewHolder object) {
        Object object2;
        int n = this.mChildHelper.getChildCount();
        for (int i = 0; i < n; ++i) {
            object2 = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (object2 == viewHolder || this.getChangedHolderKey((ViewHolder)object2) != l) continue;
            object = this.mAdapter;
            if (object != null && object.hasStableIds()) {
                object = new StringBuilder();
                object.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
                object.append(object2);
                object.append(" \n View Holder 2:");
                object.append(viewHolder);
                object.append(this.exceptionLabel());
                throw new IllegalStateException(object.toString());
            }
            object = new StringBuilder();
            object.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
            object.append(object2);
            object.append(" \n View Holder 2:");
            object.append(viewHolder);
            object.append(this.exceptionLabel());
            throw new IllegalStateException(object.toString());
        }
        object2 = new StringBuilder();
        object2.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
        object2.append(object);
        object2.append(" cannot be found but it is necessary for ");
        object2.append(viewHolder);
        object2.append(this.exceptionLabel());
        Log.e((String)"RecyclerView", (String)object2.toString());
    }

    private boolean hasUpdatedView() {
        int n = this.mChildHelper.getChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore() || !viewHolder.isUpdated()) continue;
            return true;
        }
        return false;
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new ChildHelper.Callback(){

            @Override
            public void addView(View view, int n) {
                RecyclerView.this.addView(view, n);
                RecyclerView.this.dispatchChildAttached(view);
            }

            @Override
            public void attachViewToParent(View object, int n, ViewGroup.LayoutParams layoutParams) {
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt((View)object);
                if (viewHolder != null) {
                    if (!viewHolder.isTmpDetached() && !viewHolder.shouldIgnore()) {
                        object = new StringBuilder();
                        object.append("Called attach on a child which is not detached: ");
                        object.append(viewHolder);
                        object.append(RecyclerView.this.exceptionLabel());
                        throw new IllegalArgumentException(object.toString());
                    }
                    viewHolder.clearTmpDetachFlag();
                }
                RecyclerView.this.attachViewToParent((View)object, n, layoutParams);
            }

            @Override
            public void detachViewFromParent(int n) {
                Object object = this.getChildAt(n);
                if (object != null && (object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    if (object.isTmpDetached() && !object.shouldIgnore()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("called detach on an already detached child ");
                        stringBuilder.append(object);
                        stringBuilder.append(RecyclerView.this.exceptionLabel());
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    object.addFlags(256);
                }
                RecyclerView.this.detachViewFromParent(n);
            }

            @Override
            public View getChildAt(int n) {
                return RecyclerView.this.getChildAt(n);
            }

            @Override
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }

            @Override
            public ViewHolder getChildViewHolder(View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }

            @Override
            public int indexOfChild(View view) {
                return RecyclerView.this.indexOfChild(view);
            }

            @Override
            public void onEnteredHiddenState(View object) {
                if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    ((ViewHolder)object).onEnteredHiddenState(RecyclerView.this);
                    return;
                }
            }

            @Override
            public void onLeftHiddenState(View object) {
                if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    ((ViewHolder)object).onLeftHiddenState(RecyclerView.this);
                    return;
                }
            }

            @Override
            public void removeAllViews() {
                int n = this.getChildCount();
                for (int i = 0; i < n; ++i) {
                    View view = this.getChildAt(i);
                    RecyclerView.this.dispatchChildDetached(view);
                    view.clearAnimation();
                }
                RecyclerView.this.removeAllViews();
            }

            @Override
            public void removeViewAt(int n) {
                View view = RecyclerView.this.getChildAt(n);
                if (view != null) {
                    RecyclerView.this.dispatchChildDetached(view);
                    view.clearAnimation();
                }
                RecyclerView.this.removeViewAt(n);
            }
        });
    }

    private boolean isPreferredNextFocus(View view, View view2, int n) {
        boolean bl = false;
        if (view2 != null) {
            if (view2 == this) {
                return false;
            }
            if (view == null) {
                return true;
            }
            if (n != 2 && n != 1) {
                return this.isPreferredNextFocusAbsolute(view, view2, n);
            }
            int n2 = this.mLayout.getLayoutDirection() == 1 ? 1 : 0;
            if (n == 2) {
                bl = true;
            }
            n2 = bl ^ n2 ? 66 : 17;
            if (this.isPreferredNextFocusAbsolute(view, view2, n2)) {
                return true;
            }
            if (n == 2) {
                return this.isPreferredNextFocusAbsolute(view, view2, 130);
            }
            return this.isPreferredNextFocusAbsolute(view, view2, 33);
        }
        return false;
    }

    private boolean isPreferredNextFocusAbsolute(View object, View view, int n) {
        this.mTempRect.set(0, 0, object.getWidth(), object.getHeight());
        this.mTempRect2.set(0, 0, view.getWidth(), view.getHeight());
        this.offsetDescendantRectToMyCoords((View)object, this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect2);
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
                            return true;
                        }
                        return false;
                    }
                    object = new StringBuilder();
                    object.append("direction must be absolute. received:");
                    object.append(n);
                    object.append(this.exceptionLabel());
                    throw new IllegalArgumentException(object.toString());
                }
                if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
                    return true;
                }
                return false;
            }
            if ((this.mTempRect.bottom > this.mTempRect2.bottom || this.mTempRect.top >= this.mTempRect2.bottom) && this.mTempRect.top > this.mTempRect2.top) {
                return true;
            }
            return false;
        }
        if ((this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left) {
            return true;
        }
        return false;
    }

    private void onPointerUp(MotionEvent motionEvent) {
        int n = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(n) == this.mScrollPointerId) {
            int n2;
            n = n == 0 ? 1 : 0;
            this.mScrollPointerId = motionEvent.getPointerId(n);
            this.mLastTouchX = n2 = (int)(motionEvent.getX(n) + 0.5f);
            this.mInitialTouchX = n2;
            this.mLastTouchY = n = (int)(motionEvent.getY(n) + 0.5f);
            this.mInitialTouchY = n;
            return;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
            return true;
        }
        return false;
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            this.mLayout.onItemsChanged(this);
        }
        if (this.predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean bl = this.mItemsAddedOrRemoved;
        boolean bl2 = false;
        boolean bl3 = bl || this.mItemsChanged;
        State state = this.mState;
        bl = this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || bl3 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds());
        state.mRunSimpleAnimations = bl;
        state = this.mState;
        bl = state.mRunSimpleAnimations && bl3 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled() ? true : bl2;
        state.mRunPredictiveAnimations = bl;
    }

    private void pullGlows(float f, float f2, float f3, float f4) {
        boolean bl = false;
        if (f2 < 0.0f) {
            this.ensureLeftGlow();
            EdgeEffectCompat.onPull(this.mLeftGlow, (- f2) / (float)this.getWidth(), 1.0f - f3 / (float)this.getHeight());
            bl = true;
        } else if (f2 > 0.0f) {
            this.ensureRightGlow();
            EdgeEffectCompat.onPull(this.mRightGlow, f2 / (float)this.getWidth(), f3 / (float)this.getHeight());
            bl = true;
        }
        if (f4 < 0.0f) {
            this.ensureTopGlow();
            EdgeEffectCompat.onPull(this.mTopGlow, (- f4) / (float)this.getHeight(), f / (float)this.getWidth());
            bl = true;
        } else if (f4 > 0.0f) {
            this.ensureBottomGlow();
            EdgeEffectCompat.onPull(this.mBottomGlow, f4 / (float)this.getHeight(), 1.0f - f / (float)this.getWidth());
            bl = true;
        }
        if (!bl && f2 == 0.0f && f4 == 0.0f) {
            return;
        }
        ViewCompat.postInvalidateOnAnimation((View)this);
    }

    private void recoverFocusFromState() {
        if (this.mPreserveFocusAfterLayout && this.mAdapter != null && this.hasFocus()) {
            if (this.getDescendantFocusability() != 393216) {
                Object object;
                if (this.getDescendantFocusability() == 131072 && this.isFocused()) {
                    return;
                }
                if (!this.isFocused()) {
                    object = this.getFocusedChild();
                    if (IGNORE_DETACHED_FOCUSED_CHILD && (object.getParent() == null || !object.hasFocus())) {
                        if (this.mChildHelper.getChildCount() == 0) {
                            this.requestFocus();
                            return;
                        }
                    } else if (!this.mChildHelper.isHidden((View)object)) {
                        return;
                    }
                }
                object = null;
                if (this.mState.mFocusedItemId != -1L && this.mAdapter.hasStableIds()) {
                    object = this.findViewHolderForItemId(this.mState.mFocusedItemId);
                }
                View view = null;
                object = object != null && !this.mChildHelper.isHidden(object.itemView) && object.itemView.hasFocusable() ? object.itemView : (this.mChildHelper.getChildCount() > 0 ? this.findNextViewToFocus() : view);
                if (object != null) {
                    if ((long)this.mState.mFocusedSubChildId != -1L && (view = object.findViewById(this.mState.mFocusedSubChildId)) != null && view.isFocusable()) {
                        object = view;
                    }
                    object.requestFocus();
                    return;
                }
                return;
            }
            return;
        }
    }

    private void releaseGlows() {
        boolean bl = false;
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            bl = this.mLeftGlow.isFinished();
        }
        if ((edgeEffect = this.mTopGlow) != null) {
            edgeEffect.onRelease();
            bl |= this.mTopGlow.isFinished();
        }
        if ((edgeEffect = this.mRightGlow) != null) {
            edgeEffect.onRelease();
            bl |= this.mRightGlow.isFinished();
        }
        if ((edgeEffect = this.mBottomGlow) != null) {
            edgeEffect.onRelease();
            bl |= this.mBottomGlow.isFinished();
        }
        if (bl) {
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
    }

    private void requestChildOnScreen(@NonNull View view, @Nullable View view2) {
        Rect rect;
        Object object = view2 != null ? view2 : view;
        this.mTempRect.set(0, 0, object.getWidth(), object.getHeight());
        object = object.getLayoutParams();
        if (object instanceof LayoutParams) {
            object = (LayoutParams)((Object)object);
            if (!object.mInsetsDirty) {
                object = object.mDecorInsets;
                rect = this.mTempRect;
                rect.left -= object.left;
                rect = this.mTempRect;
                rect.right += object.right;
                rect = this.mTempRect;
                rect.top -= object.top;
                rect = this.mTempRect;
                rect.bottom += object.bottom;
            }
        }
        if (view2 != null) {
            this.offsetDescendantRectToMyCoords(view2, this.mTempRect);
            this.offsetRectIntoDescendantCoords(view, this.mTempRect);
        }
        object = this.mLayout;
        rect = this.mTempRect;
        boolean bl = this.mFirstLayoutComplete;
        boolean bl2 = view2 == null;
        object.requestChildRectangleOnScreen(this, view, rect, bl ^ true, bl2);
    }

    private void resetFocusInfo() {
        State state = this.mState;
        state.mFocusedItemId = -1L;
        state.mFocusedItemPosition = -1;
        state.mFocusedSubChildId = -1;
    }

    private void resetTouch() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.clear();
        }
        this.stopNestedScroll(0);
        this.releaseGlows();
    }

    private void saveFocusInfo() {
        ViewHolder viewHolder = null;
        if (this.mPreserveFocusAfterLayout && this.hasFocus() && this.mAdapter != null) {
            viewHolder = this.getFocusedChild();
        }
        viewHolder = viewHolder == null ? null : this.findContainingViewHolder((View)viewHolder);
        if (viewHolder == null) {
            this.resetFocusInfo();
            return;
        }
        State state = this.mState;
        long l = this.mAdapter.hasStableIds() ? viewHolder.getItemId() : -1L;
        state.mFocusedItemId = l;
        state = this.mState;
        int n = this.mDataSetHasChangedAfterLayout ? -1 : (viewHolder.isRemoved() ? viewHolder.mOldPosition : viewHolder.getAdapterPosition());
        state.mFocusedItemPosition = n;
        this.mState.mFocusedSubChildId = this.getDeepestFocusedViewWithId(viewHolder.itemView);
    }

    private void setAdapterInternal(Adapter object, boolean bl, boolean bl2) {
        Adapter adapter = this.mAdapter;
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!bl || bl2) {
            this.removeAndRecycleViews();
        }
        this.mAdapterHelper.reset();
        adapter = this.mAdapter;
        this.mAdapter = object;
        if (object != null) {
            object.registerAdapterDataObserver(this.mObserver);
            object.onAttachedToRecyclerView(this);
        }
        if ((object = this.mLayout) != null) {
            object.onAdapterChanged(adapter, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(adapter, this.mAdapter, bl);
        this.mState.mStructureChanged = true;
        this.setDataSetChangedAfterLayout();
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.stopSmoothScroller();
            return;
        }
    }

    void absorbGlows(int n, int n2) {
        if (n < 0) {
            this.ensureLeftGlow();
            this.mLeftGlow.onAbsorb(- n);
        } else if (n > 0) {
            this.ensureRightGlow();
            this.mRightGlow.onAbsorb(n);
        }
        if (n2 < 0) {
            this.ensureTopGlow();
            this.mTopGlow.onAbsorb(- n2);
        } else if (n2 > 0) {
            this.ensureBottomGlow();
            this.mBottomGlow.onAbsorb(n2);
        }
        if (n == 0 && n2 == 0) {
            return;
        }
        ViewCompat.postInvalidateOnAnimation((View)this);
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.onAddFocusables(this, arrayList, n, n2)) {
            return;
        }
        super.addFocusables(arrayList, n, n2);
    }

    public void addItemDecoration(ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration, -1);
    }

    public void addItemDecoration(ItemDecoration itemDecoration, int n) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(false);
        }
        if (n < 0) {
            this.mItemDecorations.add(itemDecoration);
        } else {
            this.mItemDecorations.add(n, itemDecoration);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>();
        }
        this.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
    }

    public void addOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.add(onItemTouchListener);
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList<OnScrollListener>();
        }
        this.mScrollListeners.add(onScrollListener);
    }

    void animateAppearance(@NonNull ViewHolder viewHolder, @Nullable ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
            return;
        }
    }

    void animateDisappearance(@NonNull ViewHolder viewHolder, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @Nullable ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        this.addAnimatingView(viewHolder);
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
            return;
        }
    }

    void assertInLayoutOrScroll(String charSequence) {
        if (!this.isComputingLayout()) {
            if (charSequence == null) {
                charSequence = new StringBuilder();
                charSequence.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
                charSequence.append(this.exceptionLabel());
                throw new IllegalStateException(charSequence.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(this.exceptionLabel());
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    void assertNotInLayoutOrScroll(String charSequence) {
        if (this.isComputingLayout()) {
            if (charSequence == null) {
                charSequence = new StringBuilder();
                charSequence.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
                charSequence.append(this.exceptionLabel());
                throw new IllegalStateException(charSequence.toString());
            }
            throw new IllegalStateException((String)charSequence);
        }
        if (this.mDispatchScrollCounter > 0) {
            charSequence = new StringBuilder();
            charSequence.append("");
            charSequence.append(this.exceptionLabel());
            Log.w((String)"RecyclerView", (String)"Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", (Throwable)new IllegalStateException(charSequence.toString()));
            return;
        }
    }

    boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null && !itemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads())) {
            return false;
        }
        return true;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)layoutParams)) {
            return true;
        }
        return false;
    }

    void clearOldPositions() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.shouldIgnore()) continue;
            viewHolder.clearOldPosition();
        }
        this.mRecycler.clearOldPositions();
    }

    public void clearOnChildAttachStateChangeListeners() {
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list != null) {
            list.clear();
            return;
        }
    }

    public void clearOnScrollListeners() {
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            list.clear();
            return;
        }
    }

    @Override
    public int computeHorizontalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollHorizontally()) {
            n = this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return n;
    }

    @Override
    public int computeHorizontalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollHorizontally()) {
            n = this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return n;
    }

    @Override
    public int computeHorizontalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollHorizontally()) {
            n = this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return n;
    }

    @Override
    public int computeVerticalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollVertically()) {
            n = this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return n;
    }

    @Override
    public int computeVerticalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollVertically()) {
            n = this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return n;
    }

    @Override
    public int computeVerticalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        int n = 0;
        if (layoutManager == null) {
            return 0;
        }
        if (layoutManager.canScrollVertically()) {
            n = this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return n;
    }

    void considerReleasingGlowsOnScroll(int n, int n2) {
        boolean bl = false;
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect != null && !edgeEffect.isFinished() && n > 0) {
            this.mLeftGlow.onRelease();
            bl = this.mLeftGlow.isFinished();
        }
        if ((edgeEffect = this.mRightGlow) != null && !edgeEffect.isFinished() && n < 0) {
            this.mRightGlow.onRelease();
            bl |= this.mRightGlow.isFinished();
        }
        if ((edgeEffect = this.mTopGlow) != null && !edgeEffect.isFinished() && n2 > 0) {
            this.mTopGlow.onRelease();
            bl |= this.mTopGlow.isFinished();
        }
        if ((edgeEffect = this.mBottomGlow) != null && !edgeEffect.isFinished() && n2 < 0) {
            this.mBottomGlow.onRelease();
            bl |= this.mBottomGlow.isFinished();
        }
        if (bl) {
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
    }

    void consumePendingUpdateOperations() {
        if (this.mFirstLayoutComplete && !this.mDataSetHasChangedAfterLayout) {
            if (!this.mAdapterHelper.hasPendingUpdates()) {
                return;
            }
            if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
                TraceCompat.beginSection("RV PartialInvalidate");
                this.eatRequestLayout();
                this.onEnterLayoutOrScroll();
                this.mAdapterHelper.preProcess();
                if (!this.mLayoutRequestEaten) {
                    if (this.hasUpdatedView()) {
                        this.dispatchLayout();
                    } else {
                        this.mAdapterHelper.consumePostponedUpdates();
                    }
                }
                this.resumeRequestLayout(true);
                this.onExitLayoutOrScroll();
                TraceCompat.endSection();
                return;
            }
            if (this.mAdapterHelper.hasPendingUpdates()) {
                TraceCompat.beginSection("RV FullInvalidate");
                this.dispatchLayout();
                TraceCompat.endSection();
                return;
            }
            return;
        }
        TraceCompat.beginSection("RV FullInvalidate");
        this.dispatchLayout();
        TraceCompat.endSection();
    }

    void defaultOnMeasure(int n, int n2) {
        this.setMeasuredDimension(LayoutManager.chooseSize(n, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(n2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
    }

    void dispatchChildAttached(View view) {
        Object object = RecyclerView.getChildViewHolderInt(view);
        this.onChildAttachedToWindow(view);
        Adapter adapter = this.mAdapter;
        if (adapter != null && object != null) {
            adapter.onViewAttachedToWindow(object);
        }
        if ((object = this.mOnChildAttachStateListeners) != null) {
            for (int i = object.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(view);
            }
            return;
        }
    }

    void dispatchChildDetached(View view) {
        Object object = RecyclerView.getChildViewHolderInt(view);
        this.onChildDetachedFromWindow(view);
        Adapter adapter = this.mAdapter;
        if (adapter != null && object != null) {
            adapter.onViewDetachedFromWindow(object);
        }
        if ((object = this.mOnChildAttachStateListeners) != null) {
            for (int i = object.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(view);
            }
            return;
        }
    }

    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e((String)"RecyclerView", (String)"No adapter attached; skipping layout");
            return;
        }
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"No layout manager attached; skipping layout");
            return;
        }
        State state = this.mState;
        state.mIsMeasuring = false;
        if (state.mLayoutStep == 1) {
            this.dispatchLayoutStep1();
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        } else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == this.getWidth() && this.mLayout.getHeight() == this.getHeight()) {
            this.mLayout.setExactMeasureSpecsFrom(this);
        } else {
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        }
        this.dispatchLayoutStep3();
    }

    @Override
    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        return this.getScrollingChildHelper().dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.getScrollingChildHelper().dispatchNestedPreFling(f, f2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, arrn, arrn2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2, int n3) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, arrn, arrn2, n3);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, arrn);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn, int n5) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, arrn, n5);
    }

    void dispatchOnScrollStateChanged(int n) {
        Object object = this.mLayout;
        if (object != null) {
            object.onScrollStateChanged(n);
        }
        this.onScrollStateChanged(n);
        object = this.mScrollListener;
        if (object != null) {
            object.onScrollStateChanged(this, n);
        }
        if ((object = this.mScrollListeners) != null) {
            for (int i = object.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, n);
            }
            return;
        }
    }

    void dispatchOnScrolled(int n, int n2) {
        ++this.mDispatchScrollCounter;
        int n3 = this.getScrollX();
        int n4 = this.getScrollY();
        this.onScrollChanged(n3, n4, n3, n4);
        this.onScrolled(n, n2);
        Object object = this.mScrollListener;
        if (object != null) {
            object.onScrolled(this, n, n2);
        }
        if ((object = this.mScrollListeners) != null) {
            for (n3 = object.size() - 1; n3 >= 0; --n3) {
                this.mScrollListeners.get(n3).onScrolled(this, n, n2);
            }
        }
        --this.mDispatchScrollCounter;
    }

    void dispatchPendingImportantForAccessibilityChanges() {
        for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; --i) {
            int n;
            ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
            if (viewHolder.itemView.getParent() != this || viewHolder.shouldIgnore() || (n = viewHolder.mPendingAccessibilityState) == -1) continue;
            ViewCompat.setImportantForAccessibility(viewHolder.itemView, n);
            viewHolder.mPendingAccessibilityState = -1;
        }
        this.mPendingAccessibilityImportanceChange.clear();
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly(sparseArray);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly(sparseArray);
    }

    public void draw(Canvas canvas) {
        int n;
        int n2;
        super.draw(canvas);
        int n3 = this.mItemDecorations.size();
        for (n2 = 0; n2 < n3; ++n2) {
            this.mItemDecorations.get(n2).onDrawOver(canvas, this, this.mState);
        }
        n2 = 0;
        EdgeEffect edgeEffect = this.mLeftGlow;
        int n4 = 1;
        if (edgeEffect != null && !edgeEffect.isFinished()) {
            n3 = canvas.save();
            n2 = this.mClipToPadding ? this.getPaddingBottom() : 0;
            canvas.rotate(270.0f);
            canvas.translate((float)(- this.getHeight() + n2), 0.0f);
            edgeEffect = this.mLeftGlow;
            n2 = edgeEffect != null && edgeEffect.draw(canvas) ? 1 : 0;
            canvas.restoreToCount(n3);
        }
        if ((edgeEffect = this.mTopGlow) != null && !edgeEffect.isFinished()) {
            n = canvas.save();
            if (this.mClipToPadding) {
                canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }
            n3 = (edgeEffect = this.mTopGlow) != null && edgeEffect.draw(canvas) ? 1 : 0;
            n2 |= n3;
            canvas.restoreToCount(n);
        }
        if ((edgeEffect = this.mRightGlow) != null && !edgeEffect.isFinished()) {
            n = canvas.save();
            int n5 = this.getWidth();
            n3 = this.mClipToPadding ? this.getPaddingTop() : 0;
            canvas.rotate(90.0f);
            canvas.translate((float)(- n3), (float)(- n5));
            edgeEffect = this.mRightGlow;
            n3 = edgeEffect != null && edgeEffect.draw(canvas) ? 1 : 0;
            n2 |= n3;
            canvas.restoreToCount(n);
        }
        if ((edgeEffect = this.mBottomGlow) != null && !edgeEffect.isFinished()) {
            n = canvas.save();
            canvas.rotate(180.0f);
            if (this.mClipToPadding) {
                canvas.translate((float)(- this.getWidth() + this.getPaddingRight()), (float)(- this.getHeight() + this.getPaddingBottom()));
            } else {
                canvas.translate((float)(- this.getWidth()), (float)(- this.getHeight()));
            }
            edgeEffect = this.mBottomGlow;
            n3 = edgeEffect != null && edgeEffect.draw(canvas) ? n4 : 0;
            n2 |= n3;
            canvas.restoreToCount(n);
        }
        if (n2 == 0 && this.mItemAnimator != null && this.mItemDecorations.size() > 0 && this.mItemAnimator.isRunning()) {
            n2 = 1;
        }
        if (n2 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
    }

    public boolean drawChild(Canvas canvas, View view, long l) {
        return super.drawChild(canvas, view, l);
    }

    void eatRequestLayout() {
        ++this.mEatRequestLayout;
        if (this.mEatRequestLayout == 1 && !this.mLayoutFrozen) {
            this.mLayoutRequestEaten = false;
            return;
        }
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow != null) {
            return;
        }
        this.mBottomGlow = new EdgeEffect(this.getContext());
        if (this.mClipToPadding) {
            this.mBottomGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            return;
        }
        this.mBottomGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow != null) {
            return;
        }
        this.mLeftGlow = new EdgeEffect(this.getContext());
        if (this.mClipToPadding) {
            this.mLeftGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            return;
        }
        this.mLeftGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
    }

    void ensureRightGlow() {
        if (this.mRightGlow != null) {
            return;
        }
        this.mRightGlow = new EdgeEffect(this.getContext());
        if (this.mClipToPadding) {
            this.mRightGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            return;
        }
        this.mRightGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
    }

    void ensureTopGlow() {
        if (this.mTopGlow != null) {
            return;
        }
        this.mTopGlow = new EdgeEffect(this.getContext());
        if (this.mClipToPadding) {
            this.mTopGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            return;
        }
        this.mTopGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    String exceptionLabel() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ");
        stringBuilder.append(super.toString());
        stringBuilder.append(", adapter:");
        stringBuilder.append(this.mAdapter);
        stringBuilder.append(", layout:");
        stringBuilder.append(this.mLayout);
        stringBuilder.append(", context:");
        stringBuilder.append((Object)this.getContext());
        return stringBuilder.toString();
    }

    final void fillRemainingScrollValues(State state) {
        if (this.getScrollState() == 2) {
            OverScroller overScroller = this.mViewFlinger.mScroller;
            state.mRemainingScrollHorizontal = overScroller.getFinalX() - overScroller.getCurrX();
            state.mRemainingScrollVertical = overScroller.getFinalY() - overScroller.getCurrY();
            return;
        }
        state.mRemainingScrollHorizontal = 0;
        state.mRemainingScrollVertical = 0;
    }

    public View findChildViewUnder(float f, float f2) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
            View view = this.mChildHelper.getChildAt(i);
            float f3 = view.getTranslationX();
            float f4 = view.getTranslationY();
            if (f < (float)view.getLeft() + f3 || f > (float)view.getRight() + f3 || f2 < (float)view.getTop() + f4 || f2 > (float)view.getBottom() + f4) continue;
            return view;
        }
        return null;
    }

    @Nullable
    public View findContainingItemView(View view) {
        ViewParent viewParent = view.getParent();
        View view2 = view;
        view = viewParent;
        while (view != null && view != this && view instanceof View) {
            view2 = view;
            view = view2.getParent();
        }
        if (view == this) {
            return view2;
        }
        return null;
    }

    @Nullable
    public ViewHolder findContainingViewHolder(View view) {
        if ((view = this.findContainingItemView(view)) == null) {
            return null;
        }
        return this.getChildViewHolder(view);
    }

    public ViewHolder findViewHolderForAdapterPosition(int n) {
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        int n2 = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < n2; ++i) {
            ViewHolder viewHolder2 = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder2 == null || viewHolder2.isRemoved() || this.getAdapterPositionFor(viewHolder2) != n) continue;
            if (this.mChildHelper.isHidden(viewHolder2.itemView)) {
                viewHolder = viewHolder2;
                continue;
            }
            return viewHolder2;
        }
        return viewHolder;
    }

    public ViewHolder findViewHolderForItemId(long l) {
        Object object = this.mAdapter;
        if (object != null && object.hasStableIds()) {
            int n = this.mChildHelper.getUnfilteredChildCount();
            object = null;
            for (int i = 0; i < n; ++i) {
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                if (viewHolder == null || viewHolder.isRemoved() || viewHolder.getItemId() != l) continue;
                if (this.mChildHelper.isHidden(viewHolder.itemView)) {
                    object = viewHolder;
                    continue;
                }
                return viewHolder;
            }
            return object;
        }
        return null;
    }

    public ViewHolder findViewHolderForLayoutPosition(int n) {
        return this.findViewHolderForPosition(n, false);
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int n) {
        return this.findViewHolderForPosition(n, false);
    }

    ViewHolder findViewHolderForPosition(int n, boolean bl) {
        int n2 = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < n2; ++i) {
            ViewHolder viewHolder2 = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder2 == null || viewHolder2.isRemoved()) continue;
            if (bl) {
                if (viewHolder2.mPosition != n) {
                    continue;
                }
            } else if (viewHolder2.getLayoutPosition() != n) continue;
            if (this.mChildHelper.isHidden(viewHolder2.itemView)) {
                viewHolder = viewHolder2;
                continue;
            }
            return viewHolder2;
        }
        return viewHolder;
    }

    public boolean fling(int n, int n2) {
        Object object = this.mLayout;
        if (object == null) {
            Log.e((String)"RecyclerView", (String)"Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        boolean bl = object.canScrollHorizontally();
        boolean bl2 = this.mLayout.canScrollVertically();
        int n3 = bl && Math.abs(n) >= this.mMinFlingVelocity ? n : 0;
        if (!bl2 || Math.abs(n2) < this.mMinFlingVelocity) {
            n2 = 0;
        }
        if (n3 == 0 && n2 == 0) {
            return false;
        }
        if (!this.dispatchNestedPreFling(n3, n2)) {
            boolean bl3 = bl || bl2;
            this.dispatchNestedFling(n3, n2, bl3);
            object = this.mOnFlingListener;
            if (object != null && object.onFling(n3, n2)) {
                return true;
            }
            if (bl3) {
                n = 0;
                if (bl) {
                    n = false | true;
                }
                if (bl2) {
                    n |= 2;
                }
                this.startNestedScroll(n, 1);
                n = this.mMaxFlingVelocity;
                n = Math.max(- n, Math.min(n3, n));
                n3 = this.mMaxFlingVelocity;
                n2 = Math.max(- n3, Math.min(n2, n3));
                this.mViewFlinger.fling(n, n2);
                return true;
            }
            return false;
        }
        return false;
    }

    public View focusSearch(View view, int n) {
        Object object = this.mLayout.onInterceptFocusSearch(view, n);
        if (object != null) {
            return object;
        }
        object = this.mAdapter;
        int n2 = 1;
        int n3 = object != null && this.mLayout != null && !this.isComputingLayout() && !this.mLayoutFrozen ? 1 : 0;
        object = FocusFinder.getInstance();
        if (n3 != 0 && (n == 2 || n == 1)) {
            int n4;
            n3 = 0;
            if (this.mLayout.canScrollVertically()) {
                n3 = n == 2 ? 130 : 33;
                n4 = object.findNextFocus((ViewGroup)this, view, n3) == null ? 1 : 0;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    n = n3;
                    n3 = n4;
                } else {
                    n3 = n4;
                }
            }
            if (n3 == 0 && this.mLayout.canScrollHorizontally()) {
                n3 = this.mLayout.getLayoutDirection() == 1 ? 1 : 0;
                n4 = n == 2 ? 1 : 0;
                n3 = (n4 ^ n3) != 0 ? 66 : 17;
                n4 = object.findNextFocus((ViewGroup)this, view, n3) == null ? n2 : 0;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    n = n3;
                    n3 = n4;
                } else {
                    n3 = n4;
                }
            }
            if (n3 != 0) {
                this.consumePendingUpdateOperations();
                if (this.findContainingItemView(view) == null) {
                    return null;
                }
                this.eatRequestLayout();
                this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
                this.resumeRequestLayout(false);
            }
            object = object.findNextFocus((ViewGroup)this, view, n);
        } else if ((object = object.findNextFocus((ViewGroup)this, view, n)) == null && n3 != 0) {
            this.consumePendingUpdateOperations();
            if (this.findContainingItemView(view) == null) {
                return null;
            }
            this.eatRequestLayout();
            object = this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
            this.resumeRequestLayout(false);
        }
        if (object != null && !object.hasFocusable()) {
            if (this.getFocusedChild() == null) {
                return super.focusSearch(view, n);
            }
            this.requestChildOnScreen((View)object, null);
            return view;
        }
        if (this.isPreferredNextFocus(view, (View)object, n)) {
            return object;
        }
        return super.focusSearch(view, n);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        Object object = this.mLayout;
        if (object != null) {
            return object.generateDefaultLayoutParams();
        }
        object = new StringBuilder();
        object.append("RecyclerView has no LayoutManager");
        object.append(this.exceptionLabel());
        throw new IllegalStateException(object.toString());
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet object) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams(this.getContext(), (AttributeSet)object);
        }
        object = new StringBuilder();
        object.append("RecyclerView has no LayoutManager");
        object.append(this.exceptionLabel());
        throw new IllegalStateException(object.toString());
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams((ViewGroup.LayoutParams)object);
        }
        object = new StringBuilder();
        object.append("RecyclerView has no LayoutManager");
        object.append(this.exceptionLabel());
        throw new IllegalStateException(object.toString());
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    int getAdapterPositionFor(ViewHolder viewHolder) {
        if (!viewHolder.hasAnyOfTheFlags(524) && viewHolder.isBound()) {
            return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
        }
        return -1;
    }

    public int getBaseline() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.getBaseline();
        }
        return super.getBaseline();
    }

    long getChangedHolderKey(ViewHolder viewHolder) {
        if (this.mAdapter.hasStableIds()) {
            return viewHolder.getItemId();
        }
        return viewHolder.mPosition;
    }

    public int getChildAdapterPosition(View object) {
        if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
            return object.getAdapterPosition();
        }
        return -1;
    }

    protected int getChildDrawingOrder(int n, int n2) {
        ChildDrawingOrderCallback childDrawingOrderCallback = this.mChildDrawingOrderCallback;
        if (childDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(n, n2);
        }
        return childDrawingOrderCallback.onGetChildDrawingOrder(n, n2);
    }

    public long getChildItemId(View object) {
        Adapter adapter = this.mAdapter;
        long l = -1L;
        if (adapter != null) {
            if (!adapter.hasStableIds()) {
                return -1L;
            }
            if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                l = object.getItemId();
            }
            return l;
        }
        return -1L;
    }

    public int getChildLayoutPosition(View object) {
        if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
            return object.getLayoutPosition();
        }
        return -1;
    }

    @Deprecated
    public int getChildPosition(View view) {
        return this.getChildAdapterPosition(view);
    }

    public ViewHolder getChildViewHolder(View view) {
        Object object = view.getParent();
        if (object != null && object != this) {
            object = new StringBuilder();
            object.append("View ");
            object.append((Object)view);
            object.append(" is not a direct child of ");
            object.append(this);
            throw new IllegalArgumentException(object.toString());
        }
        return RecyclerView.getChildViewHolderInt(view);
    }

    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void getDecoratedBoundsWithMargins(View view, Rect rect) {
        RecyclerView.getDecoratedBoundsWithMarginsInt(view, rect);
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    Rect getItemDecorInsetsForChild(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid())) {
            return layoutParams.mDecorInsets;
        }
        Rect rect = layoutParams.mDecorInsets;
        rect.set(0, 0, 0, 0);
        int n = this.mItemDecorations.size();
        for (int i = 0; i < n; ++i) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
            rect.left += this.mTempRect.left;
            rect.top += this.mTempRect.top;
            rect.right += this.mTempRect.right;
            rect.bottom += this.mTempRect.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return rect;
    }

    public ItemDecoration getItemDecorationAt(int n) {
        if (n >= 0 && n < this.mItemDecorations.size()) {
            return this.mItemDecorations.get(n);
        }
        return null;
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    long getNanoTime() {
        if (ALLOW_THREAD_GAP_WORK) {
            return System.nanoTime();
        }
        return 0L;
    }

    @Nullable
    public OnFlingListener getOnFlingListener() {
        return this.mOnFlingListener;
    }

    public boolean getPreserveFocusAfterLayout() {
        return this.mPreserveFocusAfterLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return this.getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int n) {
        return this.getScrollingChildHelper().hasNestedScrollingParent(n);
    }

    public boolean hasPendingAdapterUpdates() {
        if (this.mFirstLayoutComplete && !this.mDataSetHasChangedAfterLayout && !this.mAdapterHelper.hasPendingUpdates()) {
            return false;
        }
        return true;
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback(){

            void dispatchUpdate(AdapterHelper.UpdateOp updateOp) {
                int n = updateOp.cmd;
                if (n != 4) {
                    if (n != 8) {
                        switch (n) {
                            default: {
                                return;
                            }
                            case 2: {
                                RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                                return;
                            }
                            case 1: 
                        }
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                        return;
                    }
                    RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, 1);
                    return;
                }
                RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, updateOp.payload);
            }

            @Override
            public ViewHolder findViewHolder(int n) {
                ViewHolder viewHolder = RecyclerView.this.findViewHolderForPosition(n, true);
                if (viewHolder == null) {
                    return null;
                }
                if (RecyclerView.this.mChildHelper.isHidden(viewHolder.itemView)) {
                    return null;
                }
                return viewHolder;
            }

            @Override
            public void markViewHoldersUpdated(int n, int n2, Object object) {
                RecyclerView.this.viewRangeUpdate(n, n2, object);
                RecyclerView.this.mItemsChanged = true;
            }

            @Override
            public void offsetPositionsForAdd(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForInsert(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void offsetPositionsForMove(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForMove(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void offsetPositionsForRemovingInvisible(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, true);
                Object object = RecyclerView.this;
                object.mItemsAddedOrRemoved = true;
                object = object.mState;
                object.mDeletedInvisibleItemCountSincePreviousLayout += n2;
            }

            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void onDispatchFirstPass(AdapterHelper.UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }

            @Override
            public void onDispatchSecondPass(AdapterHelper.UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
        });
    }

    @VisibleForTesting
    void initFastScroller(StateListDrawable object, Drawable drawable2, StateListDrawable stateListDrawable, Drawable drawable3) {
        if (object != null && drawable2 != null && stateListDrawable != null && drawable3 != null) {
            Resources resources = this.getContext().getResources();
            new FastScroller(this, (StateListDrawable)object, drawable2, stateListDrawable, drawable3, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
            return;
        }
        object = new StringBuilder();
        object.append("Trying to set fast scroller without both required drawables.");
        object.append(this.exceptionLabel());
        throw new IllegalArgumentException(object.toString());
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() == 0) {
            return;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    boolean isAccessibilityEnabled() {
        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            return true;
        }
        return false;
    }

    public boolean isAnimating() {
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null && itemAnimator.isRunning()) {
            return true;
        }
        return false;
    }

    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    public boolean isComputingLayout() {
        if (this.mLayoutOrScrollCounter > 0) {
            return true;
        }
        return false;
    }

    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.getScrollingChildHelper().isNestedScrollingEnabled();
    }

    void jumpToPositionForSmoothScroller(int n) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return;
        }
        layoutManager.scrollToPosition(n);
        this.awakenScrollBars();
    }

    void markItemDecorInsetsDirty() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ((LayoutParams)this.mChildHelper.getUnfilteredChildAt((int)i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    void markKnownViewsInvalid() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore()) continue;
            viewHolder.addFlags(6);
        }
        this.markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void offsetChildrenHorizontal(int n) {
        int n2 = this.mChildHelper.getChildCount();
        for (int i = 0; i < n2; ++i) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(n);
        }
    }

    public void offsetChildrenVertical(int n) {
        int n2 = this.mChildHelper.getChildCount();
        for (int i = 0; i < n2; ++i) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(n);
        }
    }

    void offsetPositionRecordsForInsert(int n, int n2) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n3; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore() || viewHolder.mPosition < n) continue;
            viewHolder.offsetPosition(n2, false);
            this.mState.mStructureChanged = true;
        }
        this.mRecycler.offsetPositionRecordsForInsert(n, n2);
        this.requestLayout();
    }

    void offsetPositionRecordsForMove(int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6 = this.mChildHelper.getUnfilteredChildCount();
        if (n < n2) {
            n5 = n;
            n3 = n2;
            n4 = -1;
        } else {
            n5 = n2;
            n3 = n;
            n4 = 1;
        }
        for (int i = 0; i < n6; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.mPosition < n5 || viewHolder.mPosition > n3) continue;
            if (viewHolder.mPosition == n) {
                viewHolder.offsetPosition(n2 - n, false);
            } else {
                viewHolder.offsetPosition(n4, false);
            }
            this.mState.mStructureChanged = true;
        }
        this.mRecycler.offsetPositionRecordsForMove(n, n2);
        this.requestLayout();
    }

    void offsetPositionRecordsForRemove(int n, int n2, boolean bl) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n3; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore()) continue;
            if (viewHolder.mPosition >= n + n2) {
                viewHolder.offsetPosition(- n2, bl);
                this.mState.mStructureChanged = true;
                continue;
            }
            if (viewHolder.mPosition < n) continue;
            viewHolder.flagRemovedAndOffsetPosition(n - 1, - n2, bl);
            this.mState.mStructureChanged = true;
        }
        this.mRecycler.offsetPositionRecordsForRemove(n, n2, bl);
        this.requestLayout();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        boolean bl = true;
        this.mIsAttached = true;
        if (!this.mFirstLayoutComplete || this.isLayoutRequested()) {
            bl = false;
        }
        this.mFirstLayoutComplete = bl;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
        if (ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker = GapWorker.sGapWorker.get();
            if (this.mGapWorker == null) {
                float f;
                this.mGapWorker = new GapWorker();
                layoutManager = ViewCompat.getDisplay((View)this);
                float f2 = 60.0f;
                if (!this.isInEditMode() && layoutManager != null && (f = layoutManager.getRefreshRate()) >= 30.0f) {
                    f2 = f;
                }
                this.mGapWorker.mFrameIntervalNs = (long)(1.0E9f / f2);
                GapWorker.sGapWorker.set(this.mGapWorker);
            }
            this.mGapWorker.add(this);
            return;
        }
    }

    public void onChildAttachedToWindow(View view) {
    }

    public void onChildDetachedFromWindow(View view) {
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Object object = this.mItemAnimator;
        if (object != null) {
            object.endAnimations();
        }
        this.stopScroll();
        this.mIsAttached = false;
        object = this.mLayout;
        if (object != null) {
            object.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        this.removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        if (ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker.remove(this);
            this.mGapWorker = null;
            return;
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = this.mItemDecorations.size();
        for (int i = 0; i < n; ++i) {
            this.mItemDecorations.get(i).onDraw(canvas, this, this.mState);
        }
    }

    void onEnterLayoutOrScroll() {
        ++this.mLayoutOrScrollCounter;
    }

    void onExitLayoutOrScroll() {
        this.onExitLayoutOrScroll(true);
    }

    void onExitLayoutOrScroll(boolean bl) {
        --this.mLayoutOrScrollCounter;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            if (bl) {
                this.dispatchContentChangedIfNecessary();
                this.dispatchPendingImportantForAccessibilityChanges();
                return;
            }
            return;
        }
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mLayout == null) {
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        if (motionEvent.getAction() == 8) {
            float f;
            float f2;
            if ((motionEvent.getSource() & 2) != 0) {
                f = this.mLayout.canScrollVertically() ? - motionEvent.getAxisValue(9) : 0.0f;
                f2 = this.mLayout.canScrollHorizontally() ? motionEvent.getAxisValue(10) : 0.0f;
            } else if ((motionEvent.getSource() & 4194304) != 0) {
                f2 = motionEvent.getAxisValue(26);
                if (this.mLayout.canScrollVertically()) {
                    f = - f2;
                    f2 = 0.0f;
                } else if (this.mLayout.canScrollHorizontally()) {
                    f = 0.0f;
                } else {
                    f2 = 0.0f;
                    f = 0.0f;
                }
            } else {
                f = 0.0f;
                f2 = 0.0f;
            }
            if (f == 0.0f && f2 == 0.0f) {
                return false;
            }
            this.scrollByInternal((int)(this.mScaledHorizontalScrollFactor * f2), (int)(this.mScaledVerticalScrollFactor * f), motionEvent);
            return false;
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent arrn) {
        boolean bl = this.mLayoutFrozen;
        boolean bl2 = false;
        if (bl) {
            return false;
        }
        if (this.dispatchOnItemTouchIntercept((MotionEvent)arrn)) {
            this.cancelTouch();
            return true;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return false;
        }
        bl = layoutManager.canScrollHorizontally();
        boolean bl3 = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement((MotionEvent)arrn);
        int n = arrn.getActionMasked();
        int n2 = arrn.getActionIndex();
        switch (n) {
            default: {
                break;
            }
            case 6: {
                this.onPointerUp((MotionEvent)arrn);
                break;
            }
            case 5: {
                this.mScrollPointerId = arrn.getPointerId(n2);
                this.mLastTouchX = n = (int)(arrn.getX(n2) + 0.5f);
                this.mInitialTouchX = n;
                this.mLastTouchY = n2 = (int)(arrn.getY(n2) + 0.5f);
                this.mInitialTouchY = n2;
                break;
            }
            case 3: {
                this.cancelTouch();
                break;
            }
            case 2: {
                n2 = arrn.findPointerIndex(this.mScrollPointerId);
                if (n2 < 0) {
                    arrn = new StringBuilder();
                    arrn.append("Error processing scroll; pointer index for id ");
                    arrn.append(this.mScrollPointerId);
                    arrn.append(" not found. Did any MotionEvents get skipped?");
                    Log.e((String)"RecyclerView", (String)arrn.toString());
                    return false;
                }
                int n3 = (int)(arrn.getX(n2) + 0.5f);
                n = (int)(arrn.getY(n2) + 0.5f);
                if (this.mScrollState == 1) break;
                int n4 = this.mInitialTouchX;
                int n5 = this.mInitialTouchY;
                n2 = 0;
                if (bl && Math.abs(n3 - n4) > this.mTouchSlop) {
                    this.mLastTouchX = n3;
                    n2 = 1;
                }
                if (bl3 && Math.abs(n - n5) > this.mTouchSlop) {
                    this.mLastTouchY = n;
                    n2 = 1;
                }
                if (n2 == 0) break;
                this.setScrollState(1);
                break;
            }
            case 1: {
                this.mVelocityTracker.clear();
                this.stopNestedScroll(0);
                break;
            }
            case 0: {
                if (this.mIgnoreMotionEventTillDown) {
                    this.mIgnoreMotionEventTillDown = false;
                }
                this.mScrollPointerId = arrn.getPointerId(0);
                this.mLastTouchX = n2 = (int)(arrn.getX() + 0.5f);
                this.mInitialTouchX = n2;
                this.mLastTouchY = n2 = (int)(arrn.getY() + 0.5f);
                this.mInitialTouchY = n2;
                if (this.mScrollState == 2) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.setScrollState(1);
                }
                arrn = this.mNestedOffsets;
                arrn[1] = 0;
                arrn[0] = 0;
                n2 = 0;
                if (bl) {
                    n2 = false | true;
                }
                if (bl3) {
                    n2 |= 2;
                }
                this.startNestedScroll(n2, 0);
            }
        }
        if (this.mScrollState == 1) {
            bl2 = true;
        }
        return bl2;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        TraceCompat.beginSection("RV OnLayout");
        this.dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }

    protected void onMeasure(int n, int n2) {
        Object object = this.mLayout;
        if (object == null) {
            this.defaultOnMeasure(n, n2);
            return;
        }
        boolean bl = object.mAutoMeasure;
        boolean bl2 = false;
        if (bl) {
            int n3 = View.MeasureSpec.getMode((int)n);
            int n4 = View.MeasureSpec.getMode((int)n2);
            boolean bl3 = bl2;
            if (n3 == 1073741824) {
                bl3 = bl2;
                if (n4 == 1073741824) {
                    bl3 = true;
                }
            }
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            if (!bl3) {
                if (this.mAdapter == null) {
                    return;
                }
                if (this.mState.mLayoutStep == 1) {
                    this.dispatchLayoutStep1();
                }
                this.mLayout.setMeasureSpecs(n, n2);
                this.mState.mIsMeasuring = true;
                this.dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(n, n2);
                if (this.mLayout.shouldMeasureTwice()) {
                    this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredHeight(), (int)1073741824));
                    this.mState.mIsMeasuring = true;
                    this.dispatchLayoutStep2();
                    this.mLayout.setMeasuredDimensionFromChildren(n, n2);
                }
                return;
            }
            return;
        }
        if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            return;
        }
        if (this.mAdapterUpdateDuringMeasure) {
            this.eatRequestLayout();
            this.onEnterLayoutOrScroll();
            this.processAdapterUpdatesAndSetAnimationFlags();
            this.onExitLayoutOrScroll();
            if (this.mState.mRunPredictiveAnimations) {
                this.mState.mInPreLayout = true;
            } else {
                this.mAdapterHelper.consumeUpdatesInOnePass();
                this.mState.mInPreLayout = false;
            }
            this.mAdapterUpdateDuringMeasure = false;
            this.resumeRequestLayout(false);
        } else if (this.mState.mRunPredictiveAnimations) {
            this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight());
            return;
        }
        object = this.mAdapter;
        this.mState.mItemCount = object != null ? object.getItemCount() : 0;
        this.eatRequestLayout();
        this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
        this.resumeRequestLayout(false);
        this.mState.mInPreLayout = false;
    }

    protected boolean onRequestFocusInDescendants(int n, Rect rect) {
        if (this.isComputingLayout()) {
            return false;
        }
        return super.onRequestFocusInDescendants(n, rect);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        this.mPendingSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
            return;
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Object object = this.mPendingSavedState;
        if (object != null) {
            savedState.copyFrom((SavedState)object);
            return savedState;
        }
        object = this.mLayout;
        if (object != null) {
            savedState.mLayoutState = object.onSaveInstanceState();
            return savedState;
        }
        savedState.mLayoutState = null;
        return savedState;
    }

    public void onScrollStateChanged(int n) {
    }

    public void onScrolled(int n, int n2) {
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n == n3 && n2 == n4) {
            return;
        }
        this.invalidateGlows();
    }

    public boolean onTouchEvent(MotionEvent arrn) {
        boolean bl = this.mLayoutFrozen;
        int n = 0;
        if (!bl) {
            int[] arrn2;
            if (this.mIgnoreMotionEventTillDown) {
                return false;
            }
            if (this.dispatchOnItemTouch((MotionEvent)arrn)) {
                this.cancelTouch();
                return true;
            }
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager == null) {
                return false;
            }
            bl = layoutManager.canScrollHorizontally();
            boolean bl2 = this.mLayout.canScrollVertically();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            int n2 = 0;
            layoutManager = MotionEvent.obtain((MotionEvent)arrn);
            int n3 = arrn.getActionMasked();
            int n4 = arrn.getActionIndex();
            if (n3 == 0) {
                arrn2 = this.mNestedOffsets;
                arrn2[1] = 0;
                arrn2[0] = 0;
            }
            arrn2 = this.mNestedOffsets;
            layoutManager.offsetLocation((float)arrn2[0], (float)arrn2[1]);
            switch (n3) {
                default: {
                    n4 = n2;
                    break;
                }
                case 6: {
                    this.onPointerUp((MotionEvent)arrn);
                    n4 = n2;
                    break;
                }
                case 5: {
                    this.mScrollPointerId = arrn.getPointerId(n4);
                    this.mLastTouchX = n3 = (int)(arrn.getX(n4) + 0.5f);
                    this.mInitialTouchX = n3;
                    this.mLastTouchY = n4 = (int)(arrn.getY(n4) + 0.5f);
                    this.mInitialTouchY = n4;
                    n4 = n2;
                    break;
                }
                case 3: {
                    this.cancelTouch();
                    n4 = n2;
                    break;
                }
                case 2: {
                    int n5;
                    n4 = arrn.findPointerIndex(this.mScrollPointerId);
                    if (n4 < 0) {
                        arrn = new StringBuilder();
                        arrn.append("Error processing scroll; pointer index for id ");
                        arrn.append(this.mScrollPointerId);
                        arrn.append(" not found. Did any MotionEvents get skipped?");
                        Log.e((String)"RecyclerView", (String)arrn.toString());
                        return false;
                    }
                    int n6 = (int)(arrn.getX(n4) + 0.5f);
                    int n7 = (int)(arrn.getY(n4) + 0.5f);
                    n3 = this.mLastTouchX - n6;
                    n4 = this.mLastTouchY - n7;
                    if (this.dispatchNestedPreScroll(n3, n4, this.mScrollConsumed, this.mScrollOffset, 0)) {
                        arrn = this.mScrollConsumed;
                        n3 -= arrn[0];
                        n4 -= arrn[1];
                        arrn = this.mScrollOffset;
                        layoutManager.offsetLocation((float)arrn[0], (float)arrn[1]);
                        arrn = this.mNestedOffsets;
                        n5 = arrn[0];
                        arrn2 = this.mScrollOffset;
                        arrn[0] = n5 + arrn2[0];
                        arrn[1] = arrn[1] + arrn2[1];
                    }
                    if (this.mScrollState != 1) {
                        int n8;
                        int n9;
                        n5 = 0;
                        if (bl && (n8 = Math.abs(n3)) > (n9 = this.mTouchSlop)) {
                            n3 = n3 > 0 ? (n3 -= n9) : (n3 += n9);
                            n5 = 1;
                        }
                        if (bl2 && (n8 = Math.abs(n4)) > (n9 = this.mTouchSlop)) {
                            n4 = n4 > 0 ? (n4 -= n9) : (n4 += n9);
                            n5 = 1;
                        }
                        if (n5 != 0) {
                            this.setScrollState(1);
                        }
                        n5 = n4;
                        n4 = n3;
                        n3 = n5;
                    } else {
                        n5 = n3;
                        n3 = n4;
                        n4 = n5;
                    }
                    if (this.mScrollState == 1) {
                        arrn = this.mScrollOffset;
                        this.mLastTouchX = n6 - arrn[0];
                        this.mLastTouchY = n7 - arrn[1];
                        n5 = bl ? n4 : 0;
                        if (bl2) {
                            n = n3;
                        }
                        if (this.scrollByInternal(n5, n, (MotionEvent)layoutManager)) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        if (this.mGapWorker != null && (n4 != 0 || n3 != 0)) {
                            this.mGapWorker.postFromTraversal(this, n4, n3);
                        }
                    }
                    n4 = n2;
                    break;
                }
                case 1: {
                    this.mVelocityTracker.addMovement((MotionEvent)layoutManager);
                    n4 = 1;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                    float f = bl ? - this.mVelocityTracker.getXVelocity(this.mScrollPointerId) : 0.0f;
                    float f2 = bl2 ? - this.mVelocityTracker.getYVelocity(this.mScrollPointerId) : 0.0f;
                    if (f == 0.0f && f2 == 0.0f || !this.fling((int)f, (int)f2)) {
                        this.setScrollState(0);
                    }
                    this.resetTouch();
                    break;
                }
                case 0: {
                    this.mScrollPointerId = arrn.getPointerId(0);
                    this.mLastTouchX = n4 = (int)(arrn.getX() + 0.5f);
                    this.mInitialTouchX = n4;
                    this.mLastTouchY = n4 = (int)(arrn.getY() + 0.5f);
                    this.mInitialTouchY = n4;
                    n4 = 0;
                    if (bl) {
                        n4 = false | true;
                    }
                    if (bl2) {
                        n4 |= 2;
                    }
                    this.startNestedScroll(n4, 0);
                    n4 = n2;
                }
            }
            if (n4 == 0) {
                this.mVelocityTracker.addMovement((MotionEvent)layoutManager);
            }
            layoutManager.recycle();
            return true;
        }
        return false;
    }

    void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
            return;
        }
    }

    void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            long l = this.getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(l, viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }

    void removeAndRecycleViews() {
        Object object = this.mItemAnimator;
        if (object != null) {
            object.endAnimations();
        }
        if ((object = this.mLayout) != null) {
            object.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        this.mRecycler.clear();
    }

    boolean removeAnimatingView(View object) {
        this.eatRequestLayout();
        boolean bl = this.mChildHelper.removeViewIfHidden((View)object);
        if (bl) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            this.mRecycler.unscrapView((ViewHolder)object);
            this.mRecycler.recycleViewHolderInternal((ViewHolder)object);
        }
        this.resumeRequestLayout(bl ^ true);
        return bl;
    }

    protected void removeDetachedView(View object, boolean bl) {
        ViewHolder viewHolder = RecyclerView.getChildViewHolderInt((View)object);
        if (viewHolder != null) {
            if (viewHolder.isTmpDetached()) {
                viewHolder.clearTmpDetachFlag();
            } else if (!viewHolder.shouldIgnore()) {
                object = new StringBuilder();
                object.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
                object.append(viewHolder);
                object.append(this.exceptionLabel());
                throw new IllegalArgumentException(object.toString());
            }
        }
        object.clearAnimation();
        this.dispatchChildDetached((View)object);
        super.removeDetachedView((View)object, bl);
    }

    public void removeItemDecoration(ItemDecoration itemDecoration) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(itemDecoration);
        if (this.mItemDecorations.isEmpty()) {
            boolean bl = this.getOverScrollMode() == 2;
            this.setWillNotDraw(bl);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list == null) {
            return;
        }
        list.remove(onChildAttachStateChangeListener);
    }

    public void removeOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.remove(onItemTouchListener);
        if (this.mActiveOnItemTouchListener == onItemTouchListener) {
            this.mActiveOnItemTouchListener = null;
            return;
        }
    }

    public void removeOnScrollListener(OnScrollListener onScrollListener) {
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            list.remove(onScrollListener);
            return;
        }
    }

    void repositionShadowingViews() {
        int n = this.mChildHelper.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.mChildHelper.getChildAt(i);
            ViewHolder viewHolder = this.getChildViewHolder(view);
            if (viewHolder == null || viewHolder.mShadowingHolder == null) continue;
            viewHolder = viewHolder.mShadowingHolder.itemView;
            int n2 = view.getLeft();
            int n3 = view.getTop();
            if (n2 == viewHolder.getLeft() && n3 == viewHolder.getTop()) continue;
            viewHolder.layout(n2, n3, viewHolder.getWidth() + n2, viewHolder.getHeight() + n3);
        }
    }

    public void requestChildFocus(View view, View view2) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, view, view2) && view2 != null) {
            this.requestChildOnScreen(view, view2);
        }
        super.requestChildFocus(view, view2);
    }

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean bl) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, bl);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        int n = this.mOnItemTouchListeners.size();
        for (int i = 0; i < n; ++i) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(bl);
        }
        super.requestDisallowInterceptTouchEvent(bl);
    }

    public void requestLayout() {
        if (this.mEatRequestLayout == 0 && !this.mLayoutFrozen) {
            super.requestLayout();
            return;
        }
        this.mLayoutRequestEaten = true;
    }

    void resumeRequestLayout(boolean bl) {
        if (this.mEatRequestLayout < 1) {
            this.mEatRequestLayout = 1;
        }
        if (!bl) {
            this.mLayoutRequestEaten = false;
        }
        if (this.mEatRequestLayout == 1) {
            if (bl && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                this.dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
        --this.mEatRequestLayout;
    }

    void saveOldPositions() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.shouldIgnore()) continue;
            viewHolder.saveOldPosition();
        }
    }

    public void scrollBy(int n, int n2) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e((String)"RecyclerView", (String)"Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        boolean bl = layoutManager.canScrollHorizontally();
        boolean bl2 = this.mLayout.canScrollVertically();
        if (!bl && !bl2) {
            return;
        }
        int n3 = 0;
        if (!bl) {
            n = 0;
        }
        if (bl2) {
            n3 = n2;
        }
        this.scrollByInternal(n, n3, null);
    }

    boolean scrollByInternal(int n, int n2, MotionEvent arrn) {
        int n3;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        this.consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            this.eatRequestLayout();
            this.onEnterLayoutOrScroll();
            TraceCompat.beginSection("RV Scroll");
            this.fillRemainingScrollValues(this.mState);
            if (n != 0) {
                n6 = this.mLayout.scrollHorizontallyBy(n, this.mRecycler, this.mState);
                n4 = n - n6;
            }
            if (n2 != 0) {
                n7 = this.mLayout.scrollVerticallyBy(n2, this.mRecycler, this.mState);
                n5 = n2 - n7;
            }
            TraceCompat.endSection();
            this.repositionShadowingViews();
            this.onExitLayoutOrScroll();
            this.resumeRequestLayout(false);
            n3 = n4;
            n4 = n7;
        } else {
            n3 = 0;
            n5 = 0;
            n6 = 0;
            n4 = 0;
        }
        if (!this.mItemDecorations.isEmpty()) {
            this.invalidate();
        }
        boolean bl = this.dispatchNestedScroll(n6, n4, n3, n5, this.mScrollOffset, 0);
        boolean bl2 = true;
        if (bl) {
            n = this.mLastTouchX;
            int[] arrn2 = this.mScrollOffset;
            this.mLastTouchX = n - arrn2[0];
            this.mLastTouchY -= arrn2[1];
            if (arrn != null) {
                arrn.offsetLocation((float)arrn2[0], (float)arrn2[1]);
            }
            arrn = this.mNestedOffsets;
            n = arrn[0];
            arrn2 = this.mScrollOffset;
            arrn[0] = n + arrn2[0];
            arrn[1] = arrn[1] + arrn2[1];
        } else if (this.getOverScrollMode() != 2) {
            if (arrn != null && !MotionEventCompat.isFromSource((MotionEvent)arrn, 8194)) {
                this.pullGlows(arrn.getX(), n3, arrn.getY(), n5);
            }
            this.considerReleasingGlowsOnScroll(n, n2);
        }
        if (n6 != 0 || n4 != 0) {
            this.dispatchOnScrolled(n6, n4);
        }
        if (!this.awakenScrollBars()) {
            this.invalidate();
        }
        if (n6 == 0) {
            if (n4 != 0) {
                return true;
            }
            bl2 = false;
        }
        return bl2;
    }

    public void scrollTo(int n, int n2) {
        Log.w((String)"RecyclerView", (String)"RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollToPosition(int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        this.stopScroll();
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e((String)"RecyclerView", (String)"Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        layoutManager.scrollToPosition(n);
        this.awakenScrollBars();
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent accessibilityEvent) {
        if (this.shouldDeferAccessibilityEvent(accessibilityEvent)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(accessibilityEvent);
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate) {
        this.mAccessibilityDelegate = recyclerViewAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate);
    }

    public void setAdapter(Adapter adapter) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, false, true);
        this.requestLayout();
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback == this.mChildDrawingOrderCallback) {
            return;
        }
        this.mChildDrawingOrderCallback = childDrawingOrderCallback;
        boolean bl = this.mChildDrawingOrderCallback != null;
        this.setChildrenDrawingOrderEnabled(bl);
    }

    @VisibleForTesting
    boolean setChildImportantForAccessibilityInternal(ViewHolder viewHolder, int n) {
        if (this.isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = n;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, n);
        return true;
    }

    public void setClipToPadding(boolean bl) {
        if (bl != this.mClipToPadding) {
            this.invalidateGlows();
        }
        this.mClipToPadding = bl;
        super.setClipToPadding(bl);
        if (this.mFirstLayoutComplete) {
            this.requestLayout();
            return;
        }
    }

    void setDataSetChangedAfterLayout() {
        this.mDataSetHasChangedAfterLayout = true;
        this.markKnownViewsInvalid();
    }

    public void setHasFixedSize(boolean bl) {
        this.mHasFixedSize = bl;
    }

    public void setItemAnimator(ItemAnimator itemAnimator) {
        ItemAnimator itemAnimator2 = this.mItemAnimator;
        if (itemAnimator2 != null) {
            itemAnimator2.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        if ((itemAnimator = (this.mItemAnimator = itemAnimator)) != null) {
            itemAnimator.setListener(this.mItemAnimatorListener);
            return;
        }
    }

    public void setItemViewCacheSize(int n) {
        this.mRecycler.setViewCacheSize(n);
    }

    public void setLayoutFrozen(boolean bl) {
        if (bl != this.mLayoutFrozen) {
            this.assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (!bl) {
                this.mLayoutFrozen = false;
                if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                    this.requestLayout();
                }
                this.mLayoutRequestEaten = false;
                return;
            }
            long l = SystemClock.uptimeMillis();
            this.onTouchEvent(MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0));
            this.mLayoutFrozen = true;
            this.mIgnoreMotionEventTillDown = true;
            this.stopScroll();
            return;
        }
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        Object object;
        if (layoutManager == this.mLayout) {
            return;
        }
        this.stopScroll();
        if (this.mLayout != null) {
            object = this.mItemAnimator;
            if (object != null) {
                object.endAnimations();
            }
            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            this.mRecycler.clear();
            if (this.mIsAttached) {
                this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }
            this.mLayout.setRecyclerView(null);
            this.mLayout = null;
        } else {
            this.mRecycler.clear();
        }
        this.mChildHelper.removeAllViewsUnfiltered();
        this.mLayout = layoutManager;
        if (layoutManager != null) {
            if (layoutManager.mRecyclerView == null) {
                this.mLayout.setRecyclerView(this);
                if (this.mIsAttached) {
                    this.mLayout.dispatchAttachedToWindow(this);
                }
            } else {
                object = new StringBuilder();
                object.append("LayoutManager ");
                object.append(layoutManager);
                object.append(" is already attached to a RecyclerView:");
                object.append(layoutManager.mRecyclerView.exceptionLabel());
                throw new IllegalArgumentException(object.toString());
            }
        }
        this.mRecycler.updateViewCacheSize();
        this.requestLayout();
    }

    @Override
    public void setNestedScrollingEnabled(boolean bl) {
        this.getScrollingChildHelper().setNestedScrollingEnabled(bl);
    }

    public void setOnFlingListener(@Nullable OnFlingListener onFlingListener) {
        this.mOnFlingListener = onFlingListener;
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setPreserveFocusAfterLayout(boolean bl) {
        this.mPreserveFocusAfterLayout = bl;
    }

    public void setRecycledViewPool(RecycledViewPool recycledViewPool) {
        this.mRecycler.setRecycledViewPool(recycledViewPool);
    }

    public void setRecyclerListener(RecyclerListener recyclerListener) {
        this.mRecyclerListener = recyclerListener;
    }

    void setScrollState(int n) {
        if (n == this.mScrollState) {
            return;
        }
        this.mScrollState = n;
        if (n != 2) {
            this.stopScrollersInternal();
        }
        this.dispatchOnScrollStateChanged(n);
    }

    public void setScrollingTouchSlop(int n) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)this.getContext());
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("setScrollingTouchSlop(): bad argument constant ");
                stringBuilder.append(n);
                stringBuilder.append("; using default value");
                Log.w((String)"RecyclerView", (String)stringBuilder.toString());
                break;
            }
            case 1: {
                this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
                return;
            }
            case 0: 
        }
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public void setViewCacheExtension(ViewCacheExtension viewCacheExtension) {
        this.mRecycler.setViewCacheExtension(viewCacheExtension);
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (this.isComputingLayout()) {
            int n = 0;
            if (accessibilityEvent != null) {
                n = AccessibilityEventCompat.getContentChangeTypes(accessibilityEvent);
            }
            if (n == 0) {
                n = 0;
            }
            this.mEatenAccessibilityChangeFlags |= n;
            return true;
        }
        return false;
    }

    public void smoothScrollBy(int n, int n2) {
        this.smoothScrollBy(n, n2, null);
    }

    public void smoothScrollBy(int n, int n2, Interpolator interpolator) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e((String)"RecyclerView", (String)"Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        if (!layoutManager.canScrollHorizontally()) {
            n = 0;
        }
        if (!this.mLayout.canScrollVertically()) {
            n2 = 0;
        }
        if (n == 0 && n2 == 0) {
            return;
        }
        this.mViewFlinger.smoothScrollBy(n, n2, interpolator);
    }

    public void smoothScrollToPosition(int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e((String)"RecyclerView", (String)"Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        layoutManager.smoothScrollToPosition(this, this.mState, n);
    }

    @Override
    public boolean startNestedScroll(int n) {
        return this.getScrollingChildHelper().startNestedScroll(n);
    }

    @Override
    public boolean startNestedScroll(int n, int n2) {
        return this.getScrollingChildHelper().startNestedScroll(n, n2);
    }

    @Override
    public void stopNestedScroll() {
        this.getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int n) {
        this.getScrollingChildHelper().stopNestedScroll(n);
    }

    public void stopScroll() {
        this.setScrollState(0);
        this.stopScrollersInternal();
    }

    public void swapAdapter(Adapter adapter, boolean bl) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, true, bl);
        this.requestLayout();
    }

    void viewRangeUpdate(int n, int n2, Object object) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n3; ++i) {
            View view = this.mChildHelper.getUnfilteredChildAt(i);
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder == null || viewHolder.shouldIgnore() || viewHolder.mPosition < n || viewHolder.mPosition >= n + n2) continue;
            viewHolder.addFlags(2);
            viewHolder.addChangePayload(object);
            ((LayoutParams)view.getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.viewRangeUpdate(n, n2);
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds = false;
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public final void bindViewHolder(VH object, int n) {
            object.mPosition = n;
            if (this.hasStableIds()) {
                object.mItemId = this.getItemId(n);
            }
            object.setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            this.onBindViewHolder(object, n, object.getUnmodifiedPayloads());
            object.clearPayload();
            object = object.itemView.getLayoutParams();
            if (object instanceof LayoutParams) {
                ((LayoutParams)object).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }

        public final VH createViewHolder(ViewGroup object, int n) {
            TraceCompat.beginSection("RV CreateView");
            object = this.onCreateViewHolder((ViewGroup)object, n);
            object.mItemViewType = n;
            TraceCompat.endSection();
            return (VH)object;
        }

        public abstract int getItemCount();

        public long getItemId(int n) {
            return -1L;
        }

        public int getItemViewType(int n) {
            return 0;
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int n) {
            this.mObservable.notifyItemRangeChanged(n, 1);
        }

        public final void notifyItemChanged(int n, Object object) {
            this.mObservable.notifyItemRangeChanged(n, 1, object);
        }

        public final void notifyItemInserted(int n) {
            this.mObservable.notifyItemRangeInserted(n, 1);
        }

        public final void notifyItemMoved(int n, int n2) {
            this.mObservable.notifyItemMoved(n, n2);
        }

        public final void notifyItemRangeChanged(int n, int n2) {
            this.mObservable.notifyItemRangeChanged(n, n2);
        }

        public final void notifyItemRangeChanged(int n, int n2, Object object) {
            this.mObservable.notifyItemRangeChanged(n, n2, object);
        }

        public final void notifyItemRangeInserted(int n, int n2) {
            this.mObservable.notifyItemRangeInserted(n, n2);
        }

        public final void notifyItemRangeRemoved(int n, int n2) {
            this.mObservable.notifyItemRangeRemoved(n, n2);
        }

        public final void notifyItemRemoved(int n) {
            this.mObservable.notifyItemRangeRemoved(n, 1);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public abstract void onBindViewHolder(VH var1, int var2);

        public void onBindViewHolder(VH VH, int n, List<Object> list) {
            this.onBindViewHolder(VH, n);
        }

        public abstract VH onCreateViewHolder(ViewGroup var1, int var2);

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public boolean onFailedToRecycleView(VH VH) {
            return false;
        }

        public void onViewAttachedToWindow(VH VH) {
        }

        public void onViewDetachedFromWindow(VH VH) {
        }

        public void onViewRecycled(VH VH) {
        }

        public void registerAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver((Object)adapterDataObserver);
        }

        public void setHasStableIds(boolean bl) {
            if (!this.hasObservers()) {
                this.mHasStableIds = bl;
                return;
            }
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
            this.mObservable.unregisterObserver((Object)adapterDataObserver);
        }
    }

    static class AdapterDataObservable
    extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return this.mObservers.isEmpty() ^ true;
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onChanged();
            }
        }

        public void notifyItemMoved(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(n, n2, 1);
            }
        }

        public void notifyItemRangeChanged(int n, int n2) {
            this.notifyItemRangeChanged(n, n2, null);
        }

        public void notifyItemRangeChanged(int n, int n2, Object object) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(n, n2, object);
            }
        }

        public void notifyItemRangeInserted(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(n, n2);
            }
        }

        public void notifyItemRangeRemoved(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(n, n2);
            }
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int n, int n2) {
        }

        public void onItemRangeChanged(int n, int n2, Object object) {
            this.onItemRangeChanged(n, n2);
        }

        public void onItemRangeInserted(int n, int n2) {
        }

        public void onItemRangeMoved(int n, int n2, int n3) {
        }

        public void onItemRangeRemoved(int n, int n2) {
        }
    }

    public static interface ChildDrawingOrderCallback {
        public int onGetChildDrawingOrder(int var1, int var2);
    }

    public static abstract class ItemAnimator {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration = 120L;
        private long mChangeDuration = 250L;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList();
        private ItemAnimatorListener mListener = null;
        private long mMoveDuration = 250L;
        private long mRemoveDuration = 120L;

        static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int n = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((n & 4) == 0) {
                int n2 = viewHolder.getOldPosition();
                int n3 = viewHolder.getAdapterPosition();
                if (n2 != -1 && n3 != -1 && n2 != n3) {
                    return n | 2048;
                }
                return n;
            }
            return n;
        }

        public abstract boolean animateAppearance(@NonNull ViewHolder var1, @Nullable ItemHolderInfo var2, @NonNull ItemHolderInfo var3);

        public abstract boolean animateChange(@NonNull ViewHolder var1, @NonNull ViewHolder var2, @NonNull ItemHolderInfo var3, @NonNull ItemHolderInfo var4);

        public abstract boolean animateDisappearance(@NonNull ViewHolder var1, @NonNull ItemHolderInfo var2, @Nullable ItemHolderInfo var3);

        public abstract boolean animatePersistence(@NonNull ViewHolder var1, @NonNull ItemHolderInfo var2, @NonNull ItemHolderInfo var3);

        public boolean canReuseUpdatedViewHolder(@NonNull ViewHolder viewHolder) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(@NonNull ViewHolder viewHolder, @NonNull List<Object> list) {
            return this.canReuseUpdatedViewHolder(viewHolder);
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            this.onAnimationFinished(viewHolder);
            ItemAnimatorListener itemAnimatorListener = this.mListener;
            if (itemAnimatorListener != null) {
                itemAnimatorListener.onAnimationFinished(viewHolder);
                return;
            }
        }

        public final void dispatchAnimationStarted(ViewHolder viewHolder) {
            this.onAnimationStarted(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            int n = this.mFinishedListeners.size();
            for (int i = 0; i < n; ++i) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public abstract void endAnimation(ViewHolder var1);

        public abstract void endAnimations();

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public abstract boolean isRunning();

        public final boolean isRunning(ItemAnimatorFinishedListener itemAnimatorFinishedListener) {
            boolean bl = this.isRunning();
            if (itemAnimatorFinishedListener != null) {
                if (!bl) {
                    itemAnimatorFinishedListener.onAnimationsFinished();
                    return bl;
                }
                this.mFinishedListeners.add(itemAnimatorFinishedListener);
                return bl;
            }
            return bl;
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }

        public void onAnimationFinished(ViewHolder viewHolder) {
        }

        public void onAnimationStarted(ViewHolder viewHolder) {
        }

        @NonNull
        public ItemHolderInfo recordPostLayoutInformation(@NonNull State state, @NonNull ViewHolder viewHolder) {
            return this.obtainHolderInfo().setFrom(viewHolder);
        }

        @NonNull
        public ItemHolderInfo recordPreLayoutInformation(@NonNull State state, @NonNull ViewHolder viewHolder, int n, @NonNull List<Object> list) {
            return this.obtainHolderInfo().setFrom(viewHolder);
        }

        public abstract void runPendingAnimations();

        public void setAddDuration(long l) {
            this.mAddDuration = l;
        }

        public void setChangeDuration(long l) {
            this.mChangeDuration = l;
        }

        void setListener(ItemAnimatorListener itemAnimatorListener) {
            this.mListener = itemAnimatorListener;
        }

        public void setMoveDuration(long l) {
            this.mMoveDuration = l;
        }

        public void setRemoveDuration(long l) {
            this.mRemoveDuration = l;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface AdapterChanges {
        }

        public static interface ItemAnimatorFinishedListener {
            public void onAnimationsFinished();
        }

        static interface ItemAnimatorListener {
            public void onAnimationFinished(ViewHolder var1);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder viewHolder) {
                return this.setFrom(viewHolder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder viewHolder, int n) {
                viewHolder = viewHolder.itemView;
                this.left = viewHolder.getLeft();
                this.top = viewHolder.getTop();
                this.right = viewHolder.getRight();
                this.bottom = viewHolder.getBottom();
                return this;
            }
        }

    }

    private class ItemAnimatorRestoreListener
    implements ItemAnimator.ItemAnimatorListener {
        ItemAnimatorRestoreListener() {
        }

        @Override
        public void onAnimationFinished(ViewHolder viewHolder) {
            viewHolder.setIsRecyclable(true);
            if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                viewHolder.mShadowedHolder = null;
            }
            viewHolder.mShadowingHolder = null;
            if (!viewHolder.shouldBeKeptAsChild()) {
                if (!RecyclerView.this.removeAnimatingView(viewHolder.itemView) && viewHolder.isTmpDetached()) {
                    RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                    return;
                }
                return;
            }
        }
    }

    public static abstract class ItemDecoration {
        @Deprecated
        public void getItemOffsets(Rect rect, int n, RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            this.getItemOffsets(rect, ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition(), recyclerView);
        }

        @Deprecated
        public void onDraw(Canvas canvas, RecyclerView recyclerView) {
        }

        public void onDraw(Canvas canvas, RecyclerView recyclerView, State state) {
            this.onDraw(canvas, recyclerView);
        }

        @Deprecated
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
        }

        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, State state) {
            this.onDrawOver(canvas, recyclerView);
        }
    }

    public static abstract class LayoutManager {
        boolean mAutoMeasure;
        ChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        ViewBoundsCheck mHorizontalBoundCheck;
        private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback;
        boolean mIsAttachedToWindow;
        private boolean mItemPrefetchEnabled;
        private boolean mMeasurementCacheEnabled;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        RecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations;
        @Nullable
        SmoothScroller mSmoothScroller;
        ViewBoundsCheck mVerticalBoundCheck;
        private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback;
        private int mWidth;
        private int mWidthMode;

        public LayoutManager() {
            this.mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback(){

                @Override
                public View getChildAt(int n) {
                    return LayoutManager.this.getChildAt(n);
                }

                @Override
                public int getChildCount() {
                    return LayoutManager.this.getChildCount();
                }

                @Override
                public int getChildEnd(View view) {
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    return LayoutManager.this.getDecoratedRight(view) + layoutParams.rightMargin;
                }

                @Override
                public int getChildStart(View view) {
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    return LayoutManager.this.getDecoratedLeft(view) - layoutParams.leftMargin;
                }

                @Override
                public View getParent() {
                    return LayoutManager.this.mRecyclerView;
                }

                @Override
                public int getParentEnd() {
                    return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
                }

                @Override
                public int getParentStart() {
                    return LayoutManager.this.getPaddingLeft();
                }
            };
            this.mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback(){

                @Override
                public View getChildAt(int n) {
                    return LayoutManager.this.getChildAt(n);
                }

                @Override
                public int getChildCount() {
                    return LayoutManager.this.getChildCount();
                }

                @Override
                public int getChildEnd(View view) {
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    return LayoutManager.this.getDecoratedBottom(view) + layoutParams.bottomMargin;
                }

                @Override
                public int getChildStart(View view) {
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    return LayoutManager.this.getDecoratedTop(view) - layoutParams.topMargin;
                }

                @Override
                public View getParent() {
                    return LayoutManager.this.mRecyclerView;
                }

                @Override
                public int getParentEnd() {
                    return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
                }

                @Override
                public int getParentStart() {
                    return LayoutManager.this.getPaddingTop();
                }
            };
            this.mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
            this.mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
            this.mRequestedSimpleAnimations = false;
            this.mIsAttachedToWindow = false;
            this.mAutoMeasure = false;
            this.mMeasurementCacheEnabled = true;
            this.mItemPrefetchEnabled = true;
        }

        /*
         * Enabled aggressive block sorting
         */
        private void addViewInt(View view, int n, boolean bl) {
            Object object = RecyclerView.getChildViewHolderInt(view);
            if (!bl && !object.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout((ViewHolder)object);
            } else {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout((ViewHolder)object);
            }
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (!object.wasReturnedFromScrap() && !object.isScrap()) {
                if (view.getParent() == this.mRecyclerView) {
                    int n2 = this.mChildHelper.indexOfChild(view);
                    if (n == -1) {
                        n = this.mChildHelper.getChildCount();
                    }
                    if (n2 == -1) {
                        object = new StringBuilder();
                        object.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                        object.append(this.mRecyclerView.indexOfChild(view));
                        object.append(this.mRecyclerView.exceptionLabel());
                        throw new IllegalStateException(object.toString());
                    }
                    if (n2 != n) {
                        this.mRecyclerView.mLayout.moveView(n2, n);
                    }
                } else {
                    this.mChildHelper.addView(view, n, false);
                    layoutParams.mInsetsDirty = true;
                    SmoothScroller smoothScroller = this.mSmoothScroller;
                    if (smoothScroller != null && smoothScroller.isRunning()) {
                        this.mSmoothScroller.onChildAttachedToWindow(view);
                    }
                }
            } else {
                if (object.isScrap()) {
                    object.unScrap();
                } else {
                    object.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(view, n, view.getLayoutParams(), false);
            }
            if (layoutParams.mPendingInvalidate) {
                object.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
                return;
            }
        }

        public static int chooseSize(int n, int n2, int n3) {
            int n4 = View.MeasureSpec.getMode((int)n);
            n = View.MeasureSpec.getSize((int)n);
            if (n4 != Integer.MIN_VALUE) {
                if (n4 != 1073741824) {
                    return Math.max(n2, n3);
                }
                return n;
            }
            return Math.min(n, Math.max(n2, n3));
        }

        private void detachViewInternal(int n, View view) {
            this.mChildHelper.detachViewFromParent(n);
        }

        public static int getChildMeasureSpec(int n, int n2, int n3, int n4, boolean bl) {
            block12 : {
                int n5;
                int n6;
                block10 : {
                    block13 : {
                        block14 : {
                            block15 : {
                                int n7;
                                block11 : {
                                    n = Math.max(0, n - n3);
                                    n5 = 0;
                                    n7 = 0;
                                    n6 = 0;
                                    n3 = 0;
                                    if (!bl) break block10;
                                    if (n4 < 0) break block11;
                                    n2 = n4;
                                    n = 1073741824;
                                    break block12;
                                }
                                if (n4 != -1) break block13;
                                if (n2 == Integer.MIN_VALUE) break block14;
                                if (n2 == 0) break block15;
                                if (n2 == 1073741824) break block14;
                                n2 = n7;
                                n = n3;
                                break block12;
                            }
                            n2 = 0;
                            n = 0;
                            break block12;
                        }
                        n3 = n;
                        n = n2;
                        n2 = n3;
                        break block12;
                    }
                    if (n4 == -2) {
                        n2 = 0;
                        n = 0;
                    } else {
                        n2 = n5;
                        n = n6;
                    }
                    break block12;
                }
                if (n4 >= 0) {
                    n2 = n4;
                    n = 1073741824;
                } else if (n4 == -1) {
                    n3 = n2;
                    n2 = n;
                    n = n3;
                } else if (n4 == -2) {
                    if (n2 != Integer.MIN_VALUE && n2 != 1073741824) {
                        n3 = 0;
                        n2 = n;
                        n = n3;
                    } else {
                        n3 = Integer.MIN_VALUE;
                        n2 = n;
                        n = n3;
                    }
                } else {
                    n = n6;
                    n2 = n5;
                }
            }
            return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
        }

        @Deprecated
        public static int getChildMeasureSpec(int n, int n2, int n3, boolean bl) {
            n = Math.max(0, n - n2);
            int n4 = 0;
            n2 = 0;
            if (bl) {
                if (n3 >= 0) {
                    n = n3;
                    n2 = 1073741824;
                } else {
                    n = 0;
                    n2 = 0;
                }
            } else if (n3 >= 0) {
                n = n3;
                n2 = 1073741824;
            } else if (n3 == -1) {
                n2 = 1073741824;
            } else if (n3 == -2) {
                n2 = Integer.MIN_VALUE;
            } else {
                n = n4;
            }
            return View.MeasureSpec.makeMeasureSpec((int)n, (int)n2);
        }

        private int[] getChildRectangleOnScreenScrollAmount(RecyclerView recyclerView, View view, Rect rect, boolean bl) {
            int n = this.getPaddingLeft();
            int n2 = this.getPaddingTop();
            int n3 = this.getWidth() - this.getPaddingRight();
            int n4 = this.getHeight();
            int n5 = this.getPaddingBottom();
            int n6 = view.getLeft() + rect.left - view.getScrollX();
            int n7 = view.getTop() + rect.top - view.getScrollY();
            int n8 = rect.width() + n6;
            int n9 = rect.height();
            int n10 = Math.min(0, n6 - n);
            int n11 = Math.min(0, n7 - n2);
            int n12 = Math.max(0, n8 - n3);
            n4 = Math.max(0, n9 + n7 - (n4 - n5));
            if (this.getLayoutDirection() == 1) {
                n10 = n12 != 0 ? n12 : Math.max(n10, n8 - n3);
            } else if (n10 == 0) {
                n10 = Math.min(n6 - n, n12);
            }
            if (n11 == 0) {
                n11 = Math.min(n7 - n2, n4);
            }
            return new int[]{n10, n11};
        }

        public static Properties getProperties(Context context, AttributeSet attributeSet, int n, int n2) {
            Properties properties = new Properties();
            context = context.obtainStyledAttributes(attributeSet, R.styleable.RecyclerView, n, n2);
            properties.orientation = context.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = context.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = context.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = context.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            context.recycle();
            return properties;
        }

        private boolean isFocusedChildVisibleAfterScrolling(RecyclerView recyclerView, int n, int n2) {
            if ((recyclerView = recyclerView.getFocusedChild()) == null) {
                return false;
            }
            int n3 = this.getPaddingLeft();
            int n4 = this.getPaddingTop();
            int n5 = this.getWidth();
            int n6 = this.getPaddingRight();
            int n7 = this.getHeight();
            int n8 = this.getPaddingBottom();
            Rect rect = this.mRecyclerView.mTempRect;
            this.getDecoratedBoundsWithMargins((View)recyclerView, rect);
            if (rect.left - n < n5 - n6 && rect.right - n > n3 && rect.top - n2 < n7 - n8) {
                if (rect.bottom - n2 <= n4) {
                    return false;
                }
                return true;
            }
            return false;
        }

        private static boolean isMeasurementUpToDate(int n, int n2, int n3) {
            int n4 = View.MeasureSpec.getMode((int)n2);
            n2 = View.MeasureSpec.getSize((int)n2);
            boolean bl = false;
            boolean bl2 = false;
            if (n3 > 0 && n != n3) {
                return false;
            }
            if (n4 != Integer.MIN_VALUE) {
                if (n4 != 0) {
                    if (n4 != 1073741824) {
                        return false;
                    }
                    if (n2 == n) {
                        bl2 = true;
                    }
                    return bl2;
                }
                return true;
            }
            bl2 = bl;
            if (n2 >= n) {
                bl2 = true;
            }
            return bl2;
        }

        private void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
                return;
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int n, View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.shouldIgnore()) {
                return;
            }
            if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                this.removeViewAt(n);
                recycler.recycleViewHolderInternal(viewHolder);
                return;
            }
            this.detachViewAt(n);
            recycler.scrapView(view);
            this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
        }

        public void addDisappearingView(View view) {
            this.addDisappearingView(view, -1);
        }

        public void addDisappearingView(View view, int n) {
            this.addViewInt(view, n, true);
        }

        public void addView(View view) {
            this.addView(view, -1);
        }

        public void addView(View view, int n) {
            this.addViewInt(view, n, false);
        }

        public void assertInLayoutOrScroll(String string2) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.assertInLayoutOrScroll(string2);
                return;
            }
        }

        public void assertNotInLayoutOrScroll(String string2) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.assertNotInLayoutOrScroll(string2);
                return;
            }
        }

        public void attachView(View view) {
            this.attachView(view, -1);
        }

        public void attachView(View view, int n) {
            this.attachView(view, n, (LayoutParams)view.getLayoutParams());
        }

        public void attachView(View view, int n, LayoutParams layoutParams) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
            }
            this.mChildHelper.attachViewToParent(view, n, (ViewGroup.LayoutParams)layoutParams, viewHolder.isRemoved());
        }

        public void calculateItemDecorationsForChild(View view, Rect rect) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                rect.set(0, 0, 0, 0);
                return;
            }
            rect.set(recyclerView.getItemDecorInsetsForChild(view));
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public boolean checkLayoutParams(LayoutParams layoutParams) {
            if (layoutParams != null) {
                return true;
            }
            return false;
        }

        public void collectAdjacentPrefetchPositions(int n, int n2, State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public void collectInitialPrefetchPositions(int n, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.scrapOrRecycleView(recycler, i, this.getChildAt(i));
            }
        }

        public void detachAndScrapView(View view, Recycler recycler) {
            this.scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(view), view);
        }

        public void detachAndScrapViewAt(int n, Recycler recycler) {
            this.scrapOrRecycleView(recycler, n, this.getChildAt(n));
        }

        public void detachView(View view) {
            int n = this.mChildHelper.indexOfChild(view);
            if (n >= 0) {
                this.detachViewInternal(n, view);
                return;
            }
        }

        public void detachViewAt(int n) {
            this.detachViewInternal(n, this.getChildAt(n));
        }

        void dispatchAttachedToWindow(RecyclerView recyclerView) {
            this.mIsAttachedToWindow = true;
            this.onAttachedToWindow(recyclerView);
        }

        void dispatchDetachedFromWindow(RecyclerView recyclerView, Recycler recycler) {
            this.mIsAttachedToWindow = false;
            this.onDetachedFromWindow(recyclerView, recycler);
        }

        public void endAnimation(View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
                return;
            }
        }

        @Nullable
        public View findContainingItemView(View view) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                return null;
            }
            if ((view = recyclerView.findContainingItemView(view)) == null) {
                return null;
            }
            if (this.mChildHelper.isHidden(view)) {
                return null;
            }
            return view;
        }

        public View findViewByPosition(int n) {
            int n2 = this.getChildCount();
            for (int i = 0; i < n2; ++i) {
                View view = this.getChildAt(i);
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
                if (viewHolder == null || viewHolder.getLayoutPosition() != n || viewHolder.shouldIgnore()) continue;
                if (!this.mRecyclerView.mState.isPreLayout()) {
                    if (viewHolder.isRemoved()) continue;
                    return view;
                }
                return view;
            }
            return null;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        public LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
            return new LayoutParams(context, attributeSet);
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            if (layoutParams instanceof LayoutParams) {
                return new LayoutParams((LayoutParams)layoutParams);
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
            }
            return new LayoutParams(layoutParams);
        }

        public int getBaseline() {
            return -1;
        }

        public int getBottomDecorationHeight(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.bottom;
        }

        public View getChildAt(int n) {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildAt(n);
            }
            return null;
        }

        public int getChildCount() {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildCount();
            }
            return 0;
        }

        public boolean getClipToPadding() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null && recyclerView.mClipToPadding) {
                return true;
            }
            return false;
        }

        public int getColumnCountForAccessibility(Recycler object, State state) {
            object = this.mRecyclerView;
            int n = 1;
            if (object != null) {
                if (object.mAdapter == null) {
                    return 1;
                }
                if (this.canScrollHorizontally()) {
                    n = this.mRecyclerView.mAdapter.getItemCount();
                }
                return n;
            }
            return 1;
        }

        public int getDecoratedBottom(View view) {
            return view.getBottom() + this.getBottomDecorationHeight(view);
        }

        public void getDecoratedBoundsWithMargins(View view, Rect rect) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(view, rect);
        }

        public int getDecoratedLeft(View view) {
            return view.getLeft() - this.getLeftDecorationWidth(view);
        }

        public int getDecoratedMeasuredHeight(View view) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + rect.top + rect.bottom;
        }

        public int getDecoratedMeasuredWidth(View view) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + rect.left + rect.right;
        }

        public int getDecoratedRight(View view) {
            return view.getRight() + this.getRightDecorationWidth(view);
        }

        public int getDecoratedTop(View view) {
            return view.getTop() - this.getTopDecorationHeight(view);
        }

        public View getFocusedChild() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                return null;
            }
            if ((recyclerView = recyclerView.getFocusedChild()) != null) {
                if (this.mChildHelper.isHidden((View)recyclerView)) {
                    return null;
                }
                return recyclerView;
            }
            return null;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getHeightMode() {
            return this.mHeightMode;
        }

        public int getItemCount() {
            Object object = this.mRecyclerView;
            object = object != null ? object.getAdapter() : null;
            if (object != null) {
                return object.getItemCount();
            }
            return 0;
        }

        public int getItemViewType(View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        }

        public int getLeftDecorationWidth(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.left;
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
        }

        public int getPaddingBottom() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingBottom();
            }
            return 0;
        }

        public int getPaddingEnd() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return ViewCompat.getPaddingEnd((View)recyclerView);
            }
            return 0;
        }

        public int getPaddingLeft() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingLeft();
            }
            return 0;
        }

        public int getPaddingRight() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingRight();
            }
            return 0;
        }

        public int getPaddingStart() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return ViewCompat.getPaddingStart((View)recyclerView);
            }
            return 0;
        }

        public int getPaddingTop() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingTop();
            }
            return 0;
        }

        public int getPosition(View view) {
            return ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }

        public int getRightDecorationWidth(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.right;
        }

        public int getRowCountForAccessibility(Recycler object, State state) {
            object = this.mRecyclerView;
            int n = 1;
            if (object != null) {
                if (object.mAdapter == null) {
                    return 1;
                }
                if (this.canScrollVertically()) {
                    n = this.mRecyclerView.mAdapter.getItemCount();
                }
                return n;
            }
            return 1;
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getTopDecorationHeight(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.top;
        }

        public void getTransformedBoundingBox(View view, boolean bl, Rect rect) {
            Rect rect2;
            if (bl) {
                rect2 = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
                rect.set(- rect2.left, - rect2.top, view.getWidth() + rect2.right, view.getHeight() + rect2.bottom);
            } else {
                rect.set(0, 0, view.getWidth(), view.getHeight());
            }
            if (this.mRecyclerView != null && (rect2 = view.getMatrix()) != null && !rect2.isIdentity()) {
                RectF rectF = this.mRecyclerView.mTempRectF;
                rectF.set(rect);
                rect2.mapRect(rectF);
                rect.set((int)Math.floor(rectF.left), (int)Math.floor(rectF.top), (int)Math.ceil(rectF.right), (int)Math.ceil(rectF.bottom));
            }
            rect.offset(view.getLeft(), view.getTop());
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getWidthMode() {
            return this.mWidthMode;
        }

        boolean hasFlexibleChildInBothOrientations() {
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                ViewGroup.LayoutParams layoutParams = this.getChildAt(i).getLayoutParams();
                if (layoutParams.width >= 0 || layoutParams.height >= 0) continue;
                return true;
            }
            return false;
        }

        public boolean hasFocus() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null && recyclerView.hasFocus()) {
                return true;
            }
            return false;
        }

        public void ignoreView(View object) {
            RecyclerView recyclerView;
            ViewParent viewParent = object.getParent();
            if (viewParent == (recyclerView = this.mRecyclerView) && recyclerView.indexOfChild((View)object) != -1) {
                object = RecyclerView.getChildViewHolderInt((View)object);
                object.addFlags(128);
                this.mRecyclerView.mViewInfoStore.removeViewHolder((ViewHolder)object);
                return;
            }
            object = new StringBuilder();
            object.append("View should be fully attached to be ignored");
            object.append(this.mRecyclerView.exceptionLabel());
            throw new IllegalArgumentException(object.toString());
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }

        public boolean isFocused() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null && recyclerView.isFocused()) {
                return true;
            }
            return false;
        }

        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }

        public boolean isSmoothScrolling() {
            SmoothScroller smoothScroller = this.mSmoothScroller;
            if (smoothScroller != null && smoothScroller.isRunning()) {
                return true;
            }
            return false;
        }

        public boolean isViewPartiallyVisible(@NonNull View view, boolean bl, boolean bl2) {
            bl2 = this.mHorizontalBoundCheck.isViewWithinBoundFlags(view, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(view, 24579);
            if (bl) {
                return bl2;
            }
            if (!bl2) {
                return true;
            }
            return false;
        }

        public void layoutDecorated(View view, int n, int n2, int n3, int n4) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            view.layout(rect.left + n, rect.top + n2, n3 - rect.right, n4 - rect.bottom);
        }

        public void layoutDecoratedWithMargins(View view, int n, int n2, int n3, int n4) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            Rect rect = layoutParams.mDecorInsets;
            view.layout(rect.left + n + layoutParams.leftMargin, rect.top + n2 + layoutParams.topMargin, n3 - rect.right - layoutParams.rightMargin, n4 - rect.bottom - layoutParams.bottomMargin);
        }

        public void measureChild(View view, int n, int n2) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(view);
            int n3 = rect.left;
            int n4 = rect.right;
            int n5 = rect.top;
            int n6 = rect.bottom;
            n = LayoutManager.getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + (n + (n3 + n4)), layoutParams.width, this.canScrollHorizontally());
            if (this.shouldMeasureChild(view, n, n2 = LayoutManager.getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + (n2 + (n5 + n6)), layoutParams.height, this.canScrollVertically()), layoutParams)) {
                view.measure(n, n2);
                return;
            }
        }

        public void measureChildWithMargins(View view, int n, int n2) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(view);
            int n3 = rect.left;
            int n4 = rect.right;
            int n5 = rect.top;
            int n6 = rect.bottom;
            n = LayoutManager.getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + (n + (n3 + n4)), layoutParams.width, this.canScrollHorizontally());
            if (this.shouldMeasureChild(view, n, n2 = LayoutManager.getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + (n2 + (n5 + n6)), layoutParams.height, this.canScrollVertically()), layoutParams)) {
                view.measure(n, n2);
                return;
            }
        }

        public void moveView(int n, int n2) {
            Object object = this.getChildAt(n);
            if (object != null) {
                this.detachViewAt(n);
                this.attachView((View)object, n2);
                return;
            }
            object = new StringBuilder();
            object.append("Cannot move a child from non-existing index:");
            object.append(n);
            object.append(this.mRecyclerView.toString());
            throw new IllegalArgumentException(object.toString());
        }

        public void offsetChildrenHorizontal(int n) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.offsetChildrenHorizontal(n);
                return;
            }
        }

        public void offsetChildrenVertical(int n) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.offsetChildrenVertical(n);
                return;
            }
        }

        public void onAdapterChanged(Adapter adapter, Adapter adapter2) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int n, int n2) {
            return false;
        }

        @CallSuper
        public void onAttachedToWindow(RecyclerView recyclerView) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView recyclerView) {
        }

        @CallSuper
        public void onDetachedFromWindow(RecyclerView recyclerView, Recycler recycler) {
            this.onDetachedFromWindow(recyclerView);
        }

        @Nullable
        public View onFocusSearchFailed(View view, int n, Recycler recycler, State state) {
            return null;
        }

        public void onInitializeAccessibilityEvent(Recycler object, State state, AccessibilityEvent accessibilityEvent) {
            object = this.mRecyclerView;
            if (object != null) {
                if (accessibilityEvent == null) {
                    return;
                }
                boolean bl = true;
                if (!(object.canScrollVertically(1) || this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1) || this.mRecyclerView.canScrollHorizontally(1))) {
                    bl = false;
                }
                accessibilityEvent.setScrollable(bl);
                if (this.mRecyclerView.mAdapter != null) {
                    accessibilityEvent.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                    return;
                }
                return;
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityEvent);
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityNodeInfoCompat);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(recycler, state), this.getColumnCountForAccessibility(recycler, state), this.isLayoutHierarchical(recycler, state), this.getSelectionModeForAccessibility(recycler, state)));
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int n = this.canScrollVertically() ? this.getPosition(view) : 0;
            int n2 = this.canScrollHorizontally() ? this.getPosition(view) : 0;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, 1, n2, 1, false, false));
        }

        void onInitializeAccessibilityNodeInfoForItem(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder != null && !viewHolder.isRemoved() && !this.mChildHelper.isHidden(viewHolder.itemView)) {
                this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, accessibilityNodeInfoCompat);
                return;
            }
        }

        public View onInterceptFocusSearch(View view, int n) {
            return null;
        }

        public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
            this.onItemsUpdated(recyclerView, n, n2);
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e((String)"RecyclerView", (String)"You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onLayoutCompleted(State state) {
        }

        public void onMeasure(Recycler recycler, State state, int n, int n2) {
            this.mRecyclerView.defaultOnMeasure(n, n2);
        }

        public boolean onRequestChildFocus(RecyclerView recyclerView, State state, View view, View view2) {
            return this.onRequestChildFocus(recyclerView, view, view2);
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView recyclerView, View view, View view2) {
            if (!this.isSmoothScrolling() && !recyclerView.isComputingLayout()) {
                return false;
            }
            return true;
        }

        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onScrollStateChanged(int n) {
        }

        boolean performAccessibilityAction(int n, Bundle bundle) {
            return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, n, bundle);
        }

        public boolean performAccessibilityAction(Recycler object, State state, int n, Bundle bundle) {
            object = this.mRecyclerView;
            if (object == null) {
                return false;
            }
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            if (n != 4096) {
                if (n != 8192) {
                    n = n3;
                } else {
                    n = object.canScrollVertically(-1) ? - this.getHeight() - this.getPaddingTop() - this.getPaddingBottom() : n4;
                    if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        n5 = - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                    }
                }
            } else {
                n = object.canScrollVertically(1) ? this.getHeight() - this.getPaddingTop() - this.getPaddingBottom() : n2;
                if (this.mRecyclerView.canScrollHorizontally(1)) {
                    n5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                }
            }
            if (n == 0 && n5 == 0) {
                return false;
            }
            this.mRecyclerView.scrollBy(n5, n);
            return true;
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int n, Bundle bundle) {
            return false;
        }

        boolean performAccessibilityActionForItem(View view, int n, Bundle bundle) {
            return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, n, bundle);
        }

        public void postOnAnimation(Runnable runnable) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                ViewCompat.postOnAnimation((View)recyclerView, runnable);
                return;
            }
        }

        public void removeAllViews() {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.mChildHelper.removeViewAt(i);
            }
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                if (RecyclerView.getChildViewHolderInt(this.getChildAt(i)).shouldIgnore()) continue;
                this.removeAndRecycleViewAt(i, recycler);
            }
        }

        void removeAndRecycleScrapInt(Recycler recycler) {
            int n = recycler.getScrapCount();
            for (int i = n - 1; i >= 0; --i) {
                View view = recycler.getScrapViewAt(i);
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
                if (viewHolder.shouldIgnore()) continue;
                viewHolder.setIsRecyclable(false);
                if (viewHolder.isTmpDetached()) {
                    this.mRecyclerView.removeDetachedView(view, false);
                }
                if (this.mRecyclerView.mItemAnimator != null) {
                    this.mRecyclerView.mItemAnimator.endAnimation(viewHolder);
                }
                viewHolder.setIsRecyclable(true);
                recycler.quickRecycleScrapView(view);
            }
            recycler.clearScrap();
            if (n > 0) {
                this.mRecyclerView.invalidate();
                return;
            }
        }

        public void removeAndRecycleView(View view, Recycler recycler) {
            this.removeView(view);
            recycler.recycleView(view);
        }

        public void removeAndRecycleViewAt(int n, Recycler recycler) {
            View view = this.getChildAt(n);
            this.removeViewAt(n);
            recycler.recycleView(view);
        }

        public boolean removeCallbacks(Runnable runnable) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.removeCallbacks(runnable);
            }
            return false;
        }

        public void removeDetachedView(View view) {
            this.mRecyclerView.removeDetachedView(view, false);
        }

        public void removeView(View view) {
            this.mChildHelper.removeView(view);
        }

        public void removeViewAt(int n) {
            if (this.getChildAt(n) != null) {
                this.mChildHelper.removeViewAt(n);
                return;
            }
        }

        public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean bl) {
            return this.requestChildRectangleOnScreen(recyclerView, view, rect, bl, false);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View arrn, Rect rect, boolean bl, boolean bl2) {
            arrn = this.getChildRectangleOnScreenScrollAmount(recyclerView, (View)arrn, rect, bl);
            int n = arrn[0];
            int n2 = arrn[1];
            if (bl2 && !this.isFocusedChildVisibleAfterScrolling(recyclerView, n, n2)) {
                return false;
            }
            if (n == 0 && n2 == 0) {
                return false;
            }
            if (bl) {
                recyclerView.scrollBy(n, n2);
                return true;
            }
            recyclerView.smoothScrollBy(n, n2);
            return true;
        }

        public void requestLayout() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.requestLayout();
                return;
            }
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int scrollHorizontallyBy(int n, Recycler recycler, State state) {
            return 0;
        }

        public void scrollToPosition(int n) {
        }

        public int scrollVerticallyBy(int n, Recycler recycler, State state) {
            return 0;
        }

        public void setAutoMeasureEnabled(boolean bl) {
            this.mAutoMeasure = bl;
        }

        void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
            this.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec((int)recyclerView.getWidth(), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)recyclerView.getHeight(), (int)1073741824));
        }

        public final void setItemPrefetchEnabled(boolean bl) {
            if (bl != this.mItemPrefetchEnabled) {
                this.mItemPrefetchEnabled = bl;
                this.mPrefetchMaxCountObserved = 0;
                RecyclerView recyclerView = this.mRecyclerView;
                if (recyclerView != null) {
                    recyclerView.mRecycler.updateViewCacheSize();
                    return;
                }
                return;
            }
        }

        void setMeasureSpecs(int n, int n2) {
            this.mWidth = View.MeasureSpec.getSize((int)n);
            this.mWidthMode = View.MeasureSpec.getMode((int)n);
            if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mWidth = 0;
            }
            this.mHeight = View.MeasureSpec.getSize((int)n2);
            this.mHeightMode = View.MeasureSpec.getMode((int)n2);
            if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mHeight = 0;
                return;
            }
        }

        public void setMeasuredDimension(int n, int n2) {
            this.mRecyclerView.setMeasuredDimension(n, n2);
        }

        public void setMeasuredDimension(Rect rect, int n, int n2) {
            int n3 = rect.width();
            int n4 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = rect.height();
            int n7 = this.getPaddingTop();
            int n8 = this.getPaddingBottom();
            this.setMeasuredDimension(LayoutManager.chooseSize(n, n3 + n4 + n5, this.getMinimumWidth()), LayoutManager.chooseSize(n2, n6 + n7 + n8, this.getMinimumHeight()));
        }

        void setMeasuredDimensionFromChildren(int n, int n2) {
            int n3 = this.getChildCount();
            if (n3 == 0) {
                this.mRecyclerView.defaultOnMeasure(n, n2);
                return;
            }
            int n4 = Integer.MAX_VALUE;
            int n5 = Integer.MAX_VALUE;
            int n6 = Integer.MIN_VALUE;
            int n7 = Integer.MIN_VALUE;
            for (int i = 0; i < n3; ++i) {
                View view = this.getChildAt(i);
                Rect rect = this.mRecyclerView.mTempRect;
                this.getDecoratedBoundsWithMargins(view, rect);
                if (rect.left < n4) {
                    n4 = rect.left;
                }
                if (rect.right > n6) {
                    n6 = rect.right;
                }
                if (rect.top < n5) {
                    n5 = rect.top;
                }
                if (rect.bottom <= n7) continue;
                n7 = rect.bottom;
            }
            this.mRecyclerView.mTempRect.set(n4, n5, n6, n7);
            this.setMeasuredDimension(this.mRecyclerView.mTempRect, n, n2);
        }

        public void setMeasurementCacheEnabled(boolean bl) {
            this.mMeasurementCacheEnabled = bl;
        }

        void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            } else {
                this.mRecyclerView = recyclerView;
                this.mChildHelper = recyclerView.mChildHelper;
                this.mWidth = recyclerView.getWidth();
                this.mHeight = recyclerView.getHeight();
            }
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
        }

        boolean shouldMeasureChild(View view, int n, int n2, LayoutParams layoutParams) {
            if (!view.isLayoutRequested() && this.mMeasurementCacheEnabled && LayoutManager.isMeasurementUpToDate(view.getWidth(), n, layoutParams.width) && LayoutManager.isMeasurementUpToDate(view.getHeight(), n2, layoutParams.height)) {
                return false;
            }
            return true;
        }

        boolean shouldMeasureTwice() {
            return false;
        }

        boolean shouldReMeasureChild(View view, int n, int n2, LayoutParams layoutParams) {
            if (this.mMeasurementCacheEnabled && LayoutManager.isMeasurementUpToDate(view.getMeasuredWidth(), n, layoutParams.width) && LayoutManager.isMeasurementUpToDate(view.getMeasuredHeight(), n2, layoutParams.height)) {
                return false;
            }
            return true;
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int n) {
            Log.e((String)"RecyclerView", (String)"You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            SmoothScroller smoothScroller2 = this.mSmoothScroller;
            if (smoothScroller2 != null && smoothScroller != smoothScroller2 && smoothScroller2.isRunning()) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            this.mSmoothScroller.start(this.mRecyclerView, this);
        }

        public void stopIgnoringView(View object) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            object.stopIgnoring();
            object.resetInternal();
            object.addFlags(4);
        }

        void stopSmoothScroller() {
            SmoothScroller smoothScroller = this.mSmoothScroller;
            if (smoothScroller != null) {
                smoothScroller.stop();
                return;
            }
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public static interface LayoutPrefetchRegistry {
            public void addPosition(int var1, int var2);
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets = new Rect();
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;
        ViewHolder mViewHolder;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams)layoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        @Deprecated
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }

    public static interface OnChildAttachStateChangeListener {
        public void onChildViewAttachedToWindow(View var1);

        public void onChildViewDetachedFromWindow(View var1);
    }

    public static abstract class OnFlingListener {
        public abstract boolean onFling(int var1, int var2);
    }

    public static interface OnItemTouchListener {
        public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2);

        public void onRequestDisallowInterceptTouchEvent(boolean var1);

        public void onTouchEvent(RecyclerView var1, MotionEvent var2);
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int n) {
        }

        public void onScrolled(RecyclerView recyclerView, int n, int n2) {
        }
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount = 0;
        SparseArray<ScrapData> mScrap = new SparseArray();

        private ScrapData getScrapDataForType(int n) {
            ScrapData scrapData = (ScrapData)this.mScrap.get(n);
            if (scrapData == null) {
                scrapData = new ScrapData();
                this.mScrap.put(n, (Object)scrapData);
                return scrapData;
            }
            return scrapData;
        }

        void attach(Adapter adapter) {
            ++this.mAttachCount;
        }

        public void clear() {
            for (int i = 0; i < this.mScrap.size(); ++i) {
                ((ScrapData)this.mScrap.valueAt((int)i)).mScrapHeap.clear();
            }
        }

        void detach() {
            --this.mAttachCount;
        }

        void factorInBindTime(int n, long l) {
            ScrapData scrapData = this.getScrapDataForType(n);
            scrapData.mBindRunningAverageNs = this.runningAverage(scrapData.mBindRunningAverageNs, l);
        }

        void factorInCreateTime(int n, long l) {
            ScrapData scrapData = this.getScrapDataForType(n);
            scrapData.mCreateRunningAverageNs = this.runningAverage(scrapData.mCreateRunningAverageNs, l);
        }

        public ViewHolder getRecycledView(int n) {
            Object object = (ScrapData)this.mScrap.get(n);
            if (object != null && !object.mScrapHeap.isEmpty()) {
                object = object.mScrapHeap;
                return (ViewHolder)object.remove(object.size() - 1);
            }
            return null;
        }

        public int getRecycledViewCount(int n) {
            return this.getScrapDataForType((int)n).mScrapHeap.size();
        }

        void onAdapterChanged(Adapter adapter, Adapter adapter2, boolean bl) {
            if (adapter != null) {
                this.detach();
            }
            if (!bl && this.mAttachCount == 0) {
                this.clear();
            }
            if (adapter2 != null) {
                this.attach(adapter2);
                return;
            }
        }

        public void putRecycledView(ViewHolder viewHolder) {
            int n = viewHolder.getItemViewType();
            ArrayList<ViewHolder> arrayList = this.getScrapDataForType((int)n).mScrapHeap;
            if (((ScrapData)this.mScrap.get((int)n)).mMaxScrap <= arrayList.size()) {
                return;
            }
            viewHolder.resetInternal();
            arrayList.add(viewHolder);
        }

        long runningAverage(long l, long l2) {
            if (l == 0L) {
                return l2;
            }
            return l / 4L * 3L + l2 / 4L;
        }

        public void setMaxRecycledViews(int n, int n2) {
            ArrayList<ViewHolder> arrayList = this.getScrapDataForType(n);
            arrayList.mMaxScrap = n2;
            arrayList = arrayList.mScrapHeap;
            if (arrayList != null) {
                while (arrayList.size() > n2) {
                    arrayList.remove(arrayList.size() - 1);
                }
                return;
            }
        }

        int size() {
            int n = 0;
            for (int i = 0; i < this.mScrap.size(); ++i) {
                ArrayList<ViewHolder> arrayList = ((ScrapData)this.mScrap.valueAt((int)i)).mScrapHeap;
                if (arrayList == null) continue;
                n += arrayList.size();
            }
            return n;
        }

        boolean willBindInTime(int n, long l, long l2) {
            long l3 = this.getScrapDataForType((int)n).mBindRunningAverageNs;
            if (l3 != 0L && l + l3 >= l2) {
                return false;
            }
            return true;
        }

        boolean willCreateInTime(int n, long l, long l2) {
            long l3 = this.getScrapDataForType((int)n).mCreateRunningAverageNs;
            if (l3 != 0L && l + l3 >= l2) {
                return false;
            }
            return true;
        }

        static class ScrapData {
            long mBindRunningAverageNs = 0L;
            long mCreateRunningAverageNs = 0L;
            int mMaxScrap = 5;
            ArrayList<ViewHolder> mScrapHeap = new ArrayList();

            ScrapData() {
            }
        }

    }

    public final class Recycler {
        static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews;
        ArrayList<ViewHolder> mChangedScrap;
        RecycledViewPool mRecyclerPool;
        private int mRequestedCacheMax;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        int mViewCacheMax;

        public Recycler() {
            this.mAttachedScrap = new ArrayList();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
            this.mRequestedCacheMax = 2;
            this.mViewCacheMax = 2;
        }

        private void attachAccessibilityDelegateOnBind(ViewHolder viewHolder) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                View view = viewHolder.itemView;
                if (ViewCompat.getImportantForAccessibility(view) == 0) {
                    ViewCompat.setImportantForAccessibility(view, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(view)) {
                    viewHolder.addFlags(16384);
                    ViewCompat.setAccessibilityDelegate(view, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                    return;
                }
                return;
            }
        }

        private void invalidateDisplayListInt(ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                this.invalidateDisplayListInt((ViewGroup)viewHolder.itemView, false);
                return;
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean bl) {
            int n;
            for (n = viewGroup.getChildCount() - 1; n >= 0; --n) {
                View view = viewGroup.getChildAt(n);
                if (!(view instanceof ViewGroup)) continue;
                this.invalidateDisplayListInt((ViewGroup)view, true);
            }
            if (!bl) {
                return;
            }
            if (viewGroup.getVisibility() == 4) {
                viewGroup.setVisibility(0);
                viewGroup.setVisibility(4);
                return;
            }
            n = viewGroup.getVisibility();
            viewGroup.setVisibility(4);
            viewGroup.setVisibility(n);
        }

        private boolean tryBindViewHolderByDeadline(ViewHolder viewHolder, int n, int n2, long l) {
            viewHolder.mOwnerRecyclerView = RecyclerView.this;
            int n3 = viewHolder.getItemViewType();
            long l2 = RecyclerView.this.getNanoTime();
            if (l != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(n3, l2, l)) {
                return false;
            }
            RecyclerView.this.mAdapter.bindViewHolder(viewHolder, n);
            l = RecyclerView.this.getNanoTime();
            this.mRecyclerPool.factorInBindTime(viewHolder.getItemViewType(), l - l2);
            this.attachAccessibilityDelegateOnBind(viewHolder);
            if (RecyclerView.this.mState.isPreLayout()) {
                viewHolder.mPreLayoutPosition = n2;
            }
            return true;
        }

        void addViewHolderToRecycledViewPool(ViewHolder viewHolder, boolean bl) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(viewHolder);
            if (viewHolder.hasAnyOfTheFlags(16384)) {
                viewHolder.setFlags(0, 16384);
                ViewCompat.setAccessibilityDelegate(viewHolder.itemView, null);
            }
            if (bl) {
                this.dispatchViewRecycled(viewHolder);
            }
            viewHolder.mOwnerRecyclerView = null;
            this.getRecycledViewPool().putRecycledView(viewHolder);
        }

        public void bindViewToPosition(View object, int n) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt((View)object);
            if (viewHolder != null) {
                int n2 = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
                if (n2 >= 0 && n2 < RecyclerView.this.mAdapter.getItemCount()) {
                    this.tryBindViewHolderByDeadline(viewHolder, n2, n, Long.MAX_VALUE);
                    object = viewHolder.itemView.getLayoutParams();
                    if (object == null) {
                        object = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                        viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
                    } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)object)) {
                        object = (LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)object);
                        viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
                    } else {
                        object = (LayoutParams)((Object)object);
                    }
                    boolean bl = true;
                    object.mInsetsDirty = true;
                    object.mViewHolder = viewHolder;
                    if (viewHolder.itemView.getParent() != null) {
                        bl = false;
                    }
                    object.mPendingInvalidate = bl;
                    return;
                }
                object = new StringBuilder();
                object.append("Inconsistency detected. Invalid item position ");
                object.append(n);
                object.append("(offset:");
                object.append(n2);
                object.append(").");
                object.append("state:");
                object.append(RecyclerView.this.mState.getItemCount());
                object.append(RecyclerView.this.exceptionLabel());
                throw new IndexOutOfBoundsException(object.toString());
            }
            object = new StringBuilder();
            object.append("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
            object.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(object.toString());
        }

        public void clear() {
            this.mAttachedScrap.clear();
            this.recycleAndClearCachedViews();
        }

        void clearOldPositions() {
            int n;
            int n2 = this.mCachedViews.size();
            for (n = 0; n < n2; ++n) {
                this.mCachedViews.get(n).clearOldPosition();
            }
            n2 = this.mAttachedScrap.size();
            for (n = 0; n < n2; ++n) {
                this.mAttachedScrap.get(n).clearOldPosition();
            }
            ArrayList<ViewHolder> arrayList = this.mChangedScrap;
            if (arrayList != null) {
                n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    this.mChangedScrap.get(n).clearOldPosition();
                }
                return;
            }
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
            ArrayList<ViewHolder> arrayList = this.mChangedScrap;
            if (arrayList != null) {
                arrayList.clear();
                return;
            }
        }

        public int convertPreLayoutPositionToPostLayout(int n) {
            if (n >= 0 && n < RecyclerView.this.mState.getItemCount()) {
                if (!RecyclerView.this.mState.isPreLayout()) {
                    return n;
                }
                return RecyclerView.this.mAdapterHelper.findPositionOffset(n);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid position ");
            stringBuilder.append(n);
            stringBuilder.append(". State ");
            stringBuilder.append("item count is ");
            stringBuilder.append(RecyclerView.this.mState.getItemCount());
            stringBuilder.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }

        void dispatchViewRecycled(ViewHolder viewHolder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
                return;
            }
        }

        ViewHolder getChangedScrapViewForPosition(int n) {
            Object object = this.mChangedScrap;
            if (object != null) {
                int n2 = object.size();
                if (n2 == 0) {
                    return null;
                }
                for (int i = 0; i < n2; ++i) {
                    object = this.mChangedScrap.get(i);
                    if (object.wasReturnedFromScrap() || object.getLayoutPosition() != n) continue;
                    object.addFlags(32);
                    return object;
                }
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    if ((n = RecyclerView.this.mAdapterHelper.findPositionOffset(n)) > 0 && n < RecyclerView.this.mAdapter.getItemCount()) {
                        long l = RecyclerView.this.mAdapter.getItemId(n);
                        for (n = 0; n < n2; ++n) {
                            object = this.mChangedScrap.get(n);
                            if (object.wasReturnedFromScrap() || object.getItemId() != l) continue;
                            object.addFlags(32);
                            return object;
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        ViewHolder getScrapOrCachedViewForId(long l, int n, boolean bl) {
            int n2;
            ViewHolder viewHolder;
            for (n2 = this.mAttachedScrap.size() - 1; n2 >= 0; --n2) {
                viewHolder = this.mAttachedScrap.get(n2);
                if (viewHolder.getItemId() != l || viewHolder.wasReturnedFromScrap()) continue;
                if (n == viewHolder.getItemViewType()) {
                    viewHolder.addFlags(32);
                    if (viewHolder.isRemoved()) {
                        if (!RecyclerView.this.mState.isPreLayout()) {
                            viewHolder.setFlags(2, 14);
                            return viewHolder;
                        }
                        return viewHolder;
                    }
                    return viewHolder;
                }
                if (bl) continue;
                this.mAttachedScrap.remove(n2);
                RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                this.quickRecycleScrapView(viewHolder.itemView);
            }
            for (n2 = this.mCachedViews.size() - 1; n2 >= 0; --n2) {
                viewHolder = this.mCachedViews.get(n2);
                if (viewHolder.getItemId() != l) continue;
                if (n == viewHolder.getItemViewType()) {
                    if (!bl) {
                        this.mCachedViews.remove(n2);
                        return viewHolder;
                    }
                    return viewHolder;
                }
                if (bl) continue;
                this.recycleCachedViewAt(n2);
                return null;
            }
            return null;
        }

        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int n, boolean bl) {
            Object object;
            ViewHolder viewHolder;
            int n2;
            int n3 = this.mAttachedScrap.size();
            for (n2 = 0; n2 < n3; ++n2) {
                viewHolder = this.mAttachedScrap.get(n2);
                if (viewHolder.wasReturnedFromScrap() || viewHolder.getLayoutPosition() != n || viewHolder.isInvalid() || !RecyclerView.this.mState.mInPreLayout && viewHolder.isRemoved()) continue;
                viewHolder.addFlags(32);
                return viewHolder;
            }
            if (!bl && (object = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(n)) != null) {
                viewHolder = RecyclerView.getChildViewHolderInt((View)object);
                RecyclerView.this.mChildHelper.unhide((View)object);
                n = RecyclerView.this.mChildHelper.indexOfChild((View)object);
                if (n != -1) {
                    RecyclerView.this.mChildHelper.detachViewFromParent(n);
                    this.scrapView((View)object);
                    viewHolder.addFlags(8224);
                    return viewHolder;
                }
                object = new StringBuilder();
                object.append("layout index should not be -1 after unhiding a view:");
                object.append(viewHolder);
                object.append(RecyclerView.this.exceptionLabel());
                throw new IllegalStateException(object.toString());
            }
            n3 = this.mCachedViews.size();
            for (n2 = 0; n2 < n3; ++n2) {
                viewHolder = this.mCachedViews.get(n2);
                if (viewHolder.isInvalid() || viewHolder.getLayoutPosition() != n) continue;
                if (!bl) {
                    this.mCachedViews.remove(n2);
                    return viewHolder;
                }
                return viewHolder;
            }
            return null;
        }

        View getScrapViewAt(int n) {
            return this.mAttachedScrap.get((int)n).itemView;
        }

        public View getViewForPosition(int n) {
            return this.getViewForPosition(n, false);
        }

        View getViewForPosition(int n, boolean bl) {
            return this.tryGetViewHolderForPositionByDeadline((int)n, (boolean)bl, (long)Long.MAX_VALUE).itemView;
        }

        void markItemDecorInsetsDirty() {
            int n = this.mCachedViews.size();
            for (int i = 0; i < n; ++i) {
                LayoutParams layoutParams = (LayoutParams)this.mCachedViews.get((int)i).itemView.getLayoutParams();
                if (layoutParams == null) continue;
                layoutParams.mInsetsDirty = true;
            }
        }

        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                int n = this.mCachedViews.size();
                for (int i = 0; i < n; ++i) {
                    ViewHolder viewHolder = this.mCachedViews.get(i);
                    if (viewHolder == null) continue;
                    viewHolder.addFlags(6);
                    viewHolder.addChangePayload(null);
                }
                return;
            }
            this.recycleAndClearCachedViews();
        }

        void offsetPositionRecordsForInsert(int n, int n2) {
            int n3 = this.mCachedViews.size();
            for (int i = 0; i < n3; ++i) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null || viewHolder.mPosition < n) continue;
                viewHolder.offsetPosition(n2, true);
            }
        }

        void offsetPositionRecordsForMove(int n, int n2) {
            int n3;
            int n4;
            int n5;
            if (n < n2) {
                n4 = n;
                n5 = n2;
                n3 = -1;
            } else {
                n4 = n2;
                n5 = n;
                n3 = 1;
            }
            int n6 = this.mCachedViews.size();
            for (int i = 0; i < n6; ++i) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null || viewHolder.mPosition < n4 || viewHolder.mPosition > n5) continue;
                if (viewHolder.mPosition == n) {
                    viewHolder.offsetPosition(n2 - n, false);
                    continue;
                }
                viewHolder.offsetPosition(n3, false);
            }
        }

        void offsetPositionRecordsForRemove(int n, int n2, boolean bl) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null) continue;
                if (viewHolder.mPosition >= n + n2) {
                    viewHolder.offsetPosition(- n2, bl);
                    continue;
                }
                if (viewHolder.mPosition < n) continue;
                viewHolder.addFlags(8);
                this.recycleCachedViewAt(i);
            }
        }

        void onAdapterChanged(Adapter adapter, Adapter adapter2, boolean bl) {
            this.clear();
            this.getRecycledViewPool().onAdapterChanged(adapter, adapter2, bl);
        }

        void quickRecycleScrapView(View object) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            ((ViewHolder)object).mScrapContainer = null;
            ((ViewHolder)object).mInChangeScrap = false;
            object.clearReturnedFromScrapFlag();
            this.recycleViewHolderInternal((ViewHolder)object);
        }

        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                this.recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
            if (ALLOW_THREAD_GAP_WORK) {
                RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                return;
            }
        }

        void recycleCachedViewAt(int n) {
            this.addViewHolderToRecycledViewPool(this.mCachedViews.get(n), true);
            this.mCachedViews.remove(n);
        }

        public void recycleView(View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (viewHolder.isScrap()) {
                viewHolder.unScrap();
            } else if (viewHolder.wasReturnedFromScrap()) {
                viewHolder.clearReturnedFromScrapFlag();
            }
            this.recycleViewHolderInternal(viewHolder);
        }

        void recycleViewHolderInternal(ViewHolder object) {
            boolean bl = object.isScrap();
            boolean bl2 = false;
            if (!bl && object.itemView.getParent() == null) {
                if (!object.isTmpDetached()) {
                    if (!object.shouldIgnore()) {
                        bl2 = ((ViewHolder)object).doesTransientStatePreventRecycling();
                        int n = RecyclerView.this.mAdapter != null && bl2 && RecyclerView.this.mAdapter.onFailedToRecycleView(object) ? 1 : 0;
                        int n2 = 0;
                        int n3 = 0;
                        int n4 = 0;
                        if (n == 0 && !object.isRecyclable()) {
                            n = n2;
                            n3 = n4;
                        } else {
                            if (this.mViewCacheMax > 0) {
                                if (!object.hasAnyOfTheFlags(526)) {
                                    n = this.mCachedViews.size();
                                    if (n >= this.mViewCacheMax && n > 0) {
                                        this.recycleCachedViewAt(0);
                                        --n;
                                    }
                                    n3 = n;
                                    if (ALLOW_THREAD_GAP_WORK && n > 0 && !RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(object.mPosition)) {
                                        --n;
                                        while (n >= 0 && RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(n3 = this.mCachedViews.get((int)n).mPosition)) {
                                            --n;
                                        }
                                        n3 = n + 1;
                                    }
                                    this.mCachedViews.add(n3, (ViewHolder)object);
                                    n = 1;
                                } else {
                                    n = n3;
                                }
                            } else {
                                n = n3;
                            }
                            if (n == 0) {
                                this.addViewHolderToRecycledViewPool((ViewHolder)object, true);
                                n3 = 1;
                            } else {
                                n3 = n4;
                            }
                        }
                        RecyclerView.this.mViewInfoStore.removeViewHolder((ViewHolder)object);
                        if (n == 0 && n3 == 0 && bl2) {
                            object.mOwnerRecyclerView = null;
                            return;
                        }
                        return;
                    }
                    object = new StringBuilder();
                    object.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                    object.append(RecyclerView.this.exceptionLabel());
                    throw new IllegalArgumentException(object.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
                stringBuilder.append(object);
                stringBuilder.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Scrapped or attached views may not be recycled. isScrap:");
            stringBuilder.append(object.isScrap());
            stringBuilder.append(" isAttached:");
            if (object.itemView.getParent() != null) {
                bl2 = true;
            }
            stringBuilder.append(bl2);
            stringBuilder.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        void recycleViewInternal(View view) {
            this.recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }

        void scrapView(View object) {
            if (!(object = RecyclerView.getChildViewHolderInt((View)object)).hasAnyOfTheFlags(12) && object.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder((ViewHolder)object)) {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList();
                }
                object.setScrapContainer(this, true);
                this.mChangedScrap.add((ViewHolder)object);
                return;
            }
            if (object.isInvalid() && !object.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                object = new StringBuilder();
                object.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                object.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(object.toString());
            }
            object.setScrapContainer(this, false);
            this.mAttachedScrap.add((ViewHolder)object);
        }

        void setRecycledViewPool(RecycledViewPool recycledViewPool) {
            RecycledViewPool recycledViewPool2 = this.mRecyclerPool;
            if (recycledViewPool2 != null) {
                recycledViewPool2.detach();
            }
            this.mRecyclerPool = recycledViewPool;
            if (recycledViewPool != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
                return;
            }
        }

        void setViewCacheExtension(ViewCacheExtension viewCacheExtension) {
            this.mViewCacheExtension = viewCacheExtension;
        }

        public void setViewCacheSize(int n) {
            this.mRequestedCacheMax = n;
            this.updateViewCacheSize();
        }

        @Nullable
        ViewHolder tryGetViewHolderForPositionByDeadline(int n, boolean bl, long l) {
            if (n >= 0 && n < RecyclerView.this.mState.getItemCount()) {
                Object object;
                int n2;
                boolean bl2 = false;
                Object object2 = null;
                boolean bl3 = RecyclerView.this.mState.isPreLayout();
                boolean bl4 = true;
                if (bl3) {
                    object2 = this.getChangedScrapViewForPosition(n);
                    bl2 = object2 != null;
                }
                if (object2 == null && (object2 = this.getScrapOrHiddenOrCachedHolderForPosition(n, bl)) != null) {
                    if (!this.validateViewHolderForOffsetPosition((ViewHolder)object2)) {
                        if (!bl) {
                            object2.addFlags(4);
                            if (object2.isScrap()) {
                                RecyclerView.this.removeDetachedView(object2.itemView, false);
                                object2.unScrap();
                            } else if (object2.wasReturnedFromScrap()) {
                                object2.clearReturnedFromScrapFlag();
                            }
                            this.recycleViewHolderInternal((ViewHolder)object2);
                        }
                        object2 = null;
                    } else {
                        bl2 = true;
                    }
                }
                if (object2 == null) {
                    int n3 = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
                    if (n3 >= 0 && n3 < RecyclerView.this.mAdapter.getItemCount()) {
                        n2 = RecyclerView.this.mAdapter.getItemViewType(n3);
                        if (RecyclerView.this.mAdapter.hasStableIds() && (object2 = this.getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(n3), n2, bl)) != null) {
                            object2.mPosition = n3;
                            bl2 = true;
                        }
                        if (object2 == null && (object = this.mViewCacheExtension) != null && (object = object.getViewForPositionAndType(this, n, n2)) != null) {
                            object2 = RecyclerView.this.getChildViewHolder((View)object);
                            if (object2 != null) {
                                if (object2.shouldIgnore()) {
                                    object2 = new StringBuilder();
                                    object2.append("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                                    object2.append(RecyclerView.this.exceptionLabel());
                                    throw new IllegalArgumentException(object2.toString());
                                }
                            } else {
                                object2 = new StringBuilder();
                                object2.append("getViewForPositionAndType returned a view which does not have a ViewHolder");
                                object2.append(RecyclerView.this.exceptionLabel());
                                throw new IllegalArgumentException(object2.toString());
                            }
                        }
                        if (object2 == null && (object2 = this.getRecycledViewPool().getRecycledView(n2)) != null) {
                            object2.resetInternal();
                            if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                                this.invalidateDisplayListInt((ViewHolder)object2);
                            }
                        }
                        if (object2 == null) {
                            long l2 = RecyclerView.this.getNanoTime();
                            if (l != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(n2, l2, l)) {
                                return null;
                            }
                            object2 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, n2);
                            if (ALLOW_THREAD_GAP_WORK && (object = RecyclerView.findNestedRecyclerView(object2.itemView)) != null) {
                                object2.mNestedRecyclerView = new WeakReference<Object>(object);
                            }
                            long l3 = RecyclerView.this.getNanoTime();
                            this.mRecyclerPool.factorInCreateTime(n2, l3 - l2);
                        }
                    } else {
                        object2 = new StringBuilder();
                        object2.append("Inconsistency detected. Invalid item position ");
                        object2.append(n);
                        object2.append("(offset:");
                        object2.append(n3);
                        object2.append(").");
                        object2.append("state:");
                        object2.append(RecyclerView.this.mState.getItemCount());
                        object2.append(RecyclerView.this.exceptionLabel());
                        throw new IndexOutOfBoundsException(object2.toString());
                    }
                }
                if (bl2 && !RecyclerView.this.mState.isPreLayout() && object2.hasAnyOfTheFlags(8192)) {
                    object2.setFlags(0, 8192);
                    if (RecyclerView.this.mState.mRunSimpleAnimations) {
                        n2 = ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object2);
                        object = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, (ViewHolder)object2, n2 | 4096, object2.getUnmodifiedPayloads());
                        RecyclerView.this.recordAnimationInfoIfBouncedHiddenView((ViewHolder)object2, (ItemAnimator.ItemHolderInfo)object);
                    }
                }
                bl = false;
                if (RecyclerView.this.mState.isPreLayout() && object2.isBound()) {
                    object2.mPreLayoutPosition = n;
                } else if (!object2.isBound() || object2.needsUpdate() || object2.isInvalid()) {
                    bl = this.tryBindViewHolderByDeadline((ViewHolder)object2, RecyclerView.this.mAdapterHelper.findPositionOffset(n), n, l);
                }
                object = object2.itemView.getLayoutParams();
                if (object == null) {
                    object = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                    object2.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
                } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)object)) {
                    object = (LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)object);
                    object2.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
                } else {
                    object = (LayoutParams)((Object)object);
                }
                object.mViewHolder = object2;
                bl = bl2 && bl ? bl4 : false;
                object.mPendingInvalidate = bl;
                return object2;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid item position ");
            stringBuilder.append(n);
            stringBuilder.append("(");
            stringBuilder.append(n);
            stringBuilder.append("). Item count:");
            stringBuilder.append(RecyclerView.this.mState.getItemCount());
            stringBuilder.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }

        void unscrapView(ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            } else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.clearReturnedFromScrapFlag();
        }

        void updateViewCacheSize() {
            int n = RecyclerView.this.mLayout != null ? RecyclerView.this.mLayout.mPrefetchMaxCountObserved : 0;
            this.mViewCacheMax = this.mRequestedCacheMax + n;
            for (n = this.mCachedViews.size() - 1; n >= 0 && this.mCachedViews.size() > this.mViewCacheMax; --n) {
                this.recycleCachedViewAt(n);
            }
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder viewHolder) {
            if (viewHolder.isRemoved()) {
                return RecyclerView.this.mState.isPreLayout();
            }
            if (viewHolder.mPosition >= 0 && viewHolder.mPosition < RecyclerView.this.mAdapter.getItemCount()) {
                boolean bl = RecyclerView.this.mState.isPreLayout();
                boolean bl2 = false;
                if (!bl && RecyclerView.this.mAdapter.getItemViewType(viewHolder.mPosition) != viewHolder.getItemViewType()) {
                    return false;
                }
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    if (viewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(viewHolder.mPosition)) {
                        bl2 = true;
                    }
                    return bl2;
                }
                return true;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Inconsistency detected. Invalid view holder adapter position");
            stringBuilder.append(viewHolder);
            stringBuilder.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }

        void viewRangeUpdate(int n, int n2) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                int n3;
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null || (n3 = viewHolder.mPosition) < n || n3 >= n + n2) continue;
                viewHolder.addFlags(2);
                this.recycleCachedViewAt(i);
            }
        }
    }

    public static interface RecyclerListener {
        public void onViewRecycled(ViewHolder var1);
    }

    private class RecyclerViewDataObserver
    extends AdapterDataObserver {
        RecyclerViewDataObserver() {
        }

        @Override
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            RecyclerView.this.mState.mStructureChanged = true;
            RecyclerView.this.setDataSetChangedAfterLayout();
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
                return;
            }
        }

        @Override
        public void onItemRangeChanged(int n, int n2, Object object) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(n, n2, object)) {
                this.triggerUpdateProcessor();
                return;
            }
        }

        @Override
        public void onItemRangeInserted(int n, int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(n, n2)) {
                this.triggerUpdateProcessor();
                return;
            }
        }

        @Override
        public void onItemRangeMoved(int n, int n2, int n3) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(n, n2, n3)) {
                this.triggerUpdateProcessor();
                return;
            }
        }

        @Override
        public void onItemRangeRemoved(int n, int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(n, n2)) {
                this.triggerUpdateProcessor();
                return;
            }
        }

        void triggerUpdateProcessor() {
            if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
                RecyclerView recyclerView = RecyclerView.this;
                ViewCompat.postOnAnimation((View)recyclerView, recyclerView.mUpdateChildViewsRunnable);
                return;
            }
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.mAdapterUpdateDuringMeasure = true;
            recyclerView.requestLayout();
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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
        Parcelable mLayoutState;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                classLoader = LayoutManager.class.getClassLoader();
            }
            this.mLayoutState = parcel.readParcelable(classLoader);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        void copyFrom(SavedState savedState) {
            this.mLayoutState = savedState.mLayoutState;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeParcelable(this.mLayoutState, 0);
        }

    }

    public static class SimpleOnItemTouchListener
    implements OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean bl) {
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction = new Action(0, 0);
        private boolean mRunning;
        private int mTargetPosition = -1;
        private View mTargetView;

        private void onAnimation(int n, int n2) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || recyclerView == null) {
                this.stop();
            }
            this.mPendingInitialRun = false;
            View view = this.mTargetView;
            if (view != null) {
                if (this.getChildPosition(view) == this.mTargetPosition) {
                    this.onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    this.stop();
                } else {
                    Log.e((String)"RecyclerView", (String)"Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                this.onSeekTargetStep(n, n2, recyclerView.mState, this.mRecyclingAction);
                boolean bl = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (bl) {
                    if (this.mRunning) {
                        this.mPendingInitialRun = true;
                        recyclerView.mViewFlinger.postOnAnimation();
                        return;
                    }
                    this.stop();
                    return;
                }
                return;
            }
        }

        public View findViewByPosition(int n) {
            return this.mRecyclerView.mLayout.findViewByPosition(n);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }

        @Nullable
        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        @Deprecated
        public void instantScrollToPosition(int n) {
            this.mRecyclerView.scrollToPosition(n);
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        protected void normalize(PointF pointF) {
            float f = (float)Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x /= f;
            pointF.y /= f;
        }

        protected void onChildAttachedToWindow(View view) {
            if (this.getChildPosition(view) == this.getTargetPosition()) {
                this.mTargetView = view;
                return;
            }
        }

        protected abstract void onSeekTargetStep(int var1, int var2, State var3, Action var4);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View var1, State var2, Action var3);

        public void setTargetPosition(int n) {
            this.mTargetPosition = n;
        }

        void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition != -1) {
                this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
                this.mRunning = true;
                this.mPendingInitialRun = true;
                this.mTargetView = this.findViewByPosition(this.getTargetPosition());
                this.onStart();
                this.mRecyclerView.mViewFlinger.postOnAnimation();
                return;
            }
            throw new IllegalArgumentException("Invalid target position");
        }

        protected final void stop() {
            if (!this.mRunning) {
                return;
            }
            this.onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mRunning = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
        }

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged = false;
            private int mConsecutiveUpdates = 0;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition = -1;

            public Action(int n, int n2) {
                this(n, n2, Integer.MIN_VALUE, null);
            }

            public Action(int n, int n2, int n3) {
                this(n, n2, n3, null);
            }

            public Action(int n, int n2, int n3, Interpolator interpolator) {
                this.mDx = n;
                this.mDy = n2;
                this.mDuration = n3;
                this.mInterpolator = interpolator;
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration >= 1) {
                    return;
                }
                throw new IllegalStateException("Scroll duration must be a positive number");
            }

            public int getDuration() {
                return this.mDuration;
            }

            public int getDx() {
                return this.mDx;
            }

            public int getDy() {
                return this.mDy;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            boolean hasJumpTarget() {
                if (this.mJumpToPosition >= 0) {
                    return true;
                }
                return false;
            }

            public void jumpTo(int n) {
                this.mJumpToPosition = n;
            }

            void runIfNecessary(RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    int n = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(n);
                    this.mChanged = false;
                    return;
                }
                if (this.mChanged) {
                    this.validate();
                    if (this.mInterpolator == null) {
                        if (this.mDuration == Integer.MIN_VALUE) {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                        } else {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                        }
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    }
                    ++this.mConsecutiveUpdates;
                    if (this.mConsecutiveUpdates > 10) {
                        Log.e((String)"RecyclerView", (String)"Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.mChanged = false;
                    return;
                }
                this.mConsecutiveUpdates = 0;
            }

            public void setDuration(int n) {
                this.mChanged = true;
                this.mDuration = n;
            }

            public void setDx(int n) {
                this.mChanged = true;
                this.mDx = n;
            }

            public void setDy(int n) {
                this.mChanged = true;
                this.mDy = n;
            }

            public void setInterpolator(Interpolator interpolator) {
                this.mChanged = true;
                this.mInterpolator = interpolator;
            }

            public void update(int n, int n2, int n3, Interpolator interpolator) {
                this.mDx = n;
                this.mDy = n2;
                this.mDuration = n3;
                this.mInterpolator = interpolator;
                this.mChanged = true;
            }
        }

        public static interface ScrollVectorProvider {
            public PointF computeScrollVectorForPosition(int var1);
        }

    }

    public static class State {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout = false;
        boolean mIsMeasuring = false;
        int mItemCount = 0;
        int mLayoutStep = 1;
        int mPreviousLayoutItemCount = 0;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations = false;
        boolean mRunSimpleAnimations = false;
        boolean mStructureChanged = false;
        private int mTargetPosition = -1;
        boolean mTrackOldChangeHolders = false;

        void assertLayoutStep(int n) {
            if ((this.mLayoutStep & n) != 0) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Layout state should be one of ");
            stringBuilder.append(Integer.toBinaryString(n));
            stringBuilder.append(" but it is ");
            stringBuilder.append(Integer.toBinaryString(this.mLayoutStep));
            throw new IllegalStateException(stringBuilder.toString());
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public <T> T get(int n) {
            SparseArray<Object> sparseArray = this.mData;
            if (sparseArray == null) {
                return null;
            }
            return (T)sparseArray.get(n);
        }

        public int getItemCount() {
            if (this.mInPreLayout) {
                return this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
            }
            return this.mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return this.mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return this.mRemainingScrollVertical;
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            if (this.mTargetPosition != -1) {
                return true;
            }
            return false;
        }

        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        void prepareForNestedPrefetch(Adapter adapter) {
            this.mLayoutStep = 1;
            this.mItemCount = adapter.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }

        public void put(int n, Object object) {
            if (this.mData == null) {
                this.mData = new SparseArray();
            }
            this.mData.put(n, object);
        }

        public void remove(int n) {
            SparseArray<Object> sparseArray = this.mData;
            if (sparseArray == null) {
                return;
            }
            sparseArray.remove(n);
        }

        State reset() {
            this.mTargetPosition = -1;
            SparseArray<Object> sparseArray = this.mData;
            if (sparseArray != null) {
                sparseArray.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mIsMeasuring = false;
            return this;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("State{mTargetPosition=");
            stringBuilder.append(this.mTargetPosition);
            stringBuilder.append(", mData=");
            stringBuilder.append(this.mData);
            stringBuilder.append(", mItemCount=");
            stringBuilder.append(this.mItemCount);
            stringBuilder.append(", mPreviousLayoutItemCount=");
            stringBuilder.append(this.mPreviousLayoutItemCount);
            stringBuilder.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
            stringBuilder.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
            stringBuilder.append(", mStructureChanged=");
            stringBuilder.append(this.mStructureChanged);
            stringBuilder.append(", mInPreLayout=");
            stringBuilder.append(this.mInPreLayout);
            stringBuilder.append(", mRunSimpleAnimations=");
            stringBuilder.append(this.mRunSimpleAnimations);
            stringBuilder.append(", mRunPredictiveAnimations=");
            stringBuilder.append(this.mRunPredictiveAnimations);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        static @interface LayoutState {
        }

    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler var1, int var2, int var3);
    }

    class ViewFlinger
    implements Runnable {
        private boolean mEatRunOnAnimationRequest;
        Interpolator mInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        private boolean mReSchedulePostAnimationCallback;
        private OverScroller mScroller;

        ViewFlinger() {
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mEatRunOnAnimationRequest = false;
            this.mReSchedulePostAnimationCallback = false;
            this.mScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        private int computeScrollDuration(int n, int n2, int n3, int n4) {
            int n5;
            int n6 = Math.abs(n);
            boolean bl = n6 > (n5 = Math.abs(n2));
            n3 = (int)Math.sqrt(n3 * n3 + n4 * n4);
            n2 = (int)Math.sqrt(n * n + n2 * n2);
            n = bl ? RecyclerView.this.getWidth() : RecyclerView.this.getHeight();
            n4 = n / 2;
            float f = Math.min(1.0f, (float)n2 * 1.0f / (float)n);
            float f2 = n4;
            float f3 = n4;
            f = this.distanceInfluenceForSnapDuration(f);
            if (n3 > 0) {
                n = Math.round(Math.abs((f2 + f3 * f) / (float)n3) * 1000.0f) * 4;
            } else {
                n2 = bl ? n6 : n5;
                n = (int)(((float)n2 / (float)n + 1.0f) * 300.0f);
            }
            return Math.min(n, 2000);
        }

        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float)Math.sin((f - 0.5f) * 0.47123894f);
        }

        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                this.postOnAnimation();
                return;
            }
        }

        public void fling(int n, int n2) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.postOnAnimation();
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
                return;
            }
            RecyclerView.this.removeCallbacks((Runnable)this);
            ViewCompat.postOnAnimation((View)RecyclerView.this, this);
        }

        @Override
        public void run() {
            if (RecyclerView.this.mLayout == null) {
                this.stop();
                return;
            }
            this.disableRunOnAnimationRequests();
            RecyclerView.this.consumePendingUpdateOperations();
            OverScroller overScroller = this.mScroller;
            SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
            if (overScroller.computeScrollOffset()) {
                Object object = RecyclerView.this.mScrollConsumed;
                int n = overScroller.getCurrX();
                int n2 = overScroller.getCurrY();
                int n3 = n - this.mLastFlingX;
                int n4 = n2 - this.mLastFlingY;
                int n5 = 0;
                int n6 = 0;
                int n7 = 0;
                this.mLastFlingX = n;
                this.mLastFlingY = n2;
                int n8 = 0;
                int n9 = 0;
                if (RecyclerView.this.dispatchNestedPreScroll(n3, n4, (int[])object, null, 1)) {
                    n3 -= object[0];
                    n4 -= object[1];
                }
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.eatRequestLayout();
                    RecyclerView.this.onEnterLayoutOrScroll();
                    TraceCompat.beginSection("RV Scroll");
                    object = RecyclerView.this;
                    object.fillRemainingScrollValues(object.mState);
                    if (n3 != 0) {
                        n6 = RecyclerView.this.mLayout.scrollHorizontallyBy(n3, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n5 = n3 - n6;
                    } else {
                        n5 = n8;
                    }
                    if (n4 != 0) {
                        n7 = RecyclerView.this.mLayout.scrollVerticallyBy(n4, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n9 = n4 - n7;
                    }
                    TraceCompat.endSection();
                    RecyclerView.this.repositionShadowingViews();
                    RecyclerView.this.onExitLayoutOrScroll();
                    RecyclerView.this.resumeRequestLayout(false);
                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        n8 = RecyclerView.this.mState.getItemCount();
                        if (n8 == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= n8) {
                            smoothScroller.setTargetPosition(n8 - 1);
                            smoothScroller.onAnimation(n3 - n5, n4 - n9);
                        } else {
                            smoothScroller.onAnimation(n3 - n5, n4 - n9);
                        }
                    }
                    n8 = n9;
                    n9 = n7;
                    n7 = n5;
                    n5 = n6;
                } else {
                    n9 = 0;
                    n7 = 0;
                    n8 = 0;
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(n3, n4);
                }
                if (!(RecyclerView.this.dispatchNestedScroll(n5, n9, n7, n8, null, 1) || n7 == 0 && n8 == 0)) {
                    int n10;
                    int n11 = (int)overScroller.getCurrVelocity();
                    if (n7 != n) {
                        n6 = n7 < 0 ? - n11 : (n7 > 0 ? n11 : 0);
                        n10 = n6;
                    } else {
                        n10 = 0;
                    }
                    n6 = n8 != n2 ? (n8 < 0 ? - n11 : (n8 > 0 ? n11 : 0)) : 0;
                    if (RecyclerView.this.getOverScrollMode() != 2) {
                        RecyclerView.this.absorbGlows(n10, n6);
                    }
                    if (n10 != 0 || n7 == n || overScroller.getFinalX() == 0) {
                        if (n6 != 0 || n8 == n2 || overScroller.getFinalY() == 0) {
                            overScroller.abortAnimation();
                        }
                    }
                }
                if (n5 != 0 || n9 != 0) {
                    RecyclerView.this.dispatchOnScrolled(n5, n9);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                n6 = n4 != 0 && RecyclerView.this.mLayout.canScrollVertically() && n9 == n4 ? 1 : 0;
                n5 = n3 != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && n5 == n3 ? 1 : 0;
                n6 = (n3 != 0 || n4 != 0) && n5 == 0 && n6 == 0 ? 0 : 1;
                if (!overScroller.isFinished() && (n6 != 0 || RecyclerView.this.hasNestedScrollingParent(1))) {
                    this.postOnAnimation();
                    if (RecyclerView.this.mGapWorker != null) {
                        RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, n3, n4);
                    }
                } else {
                    RecyclerView.this.setScrollState(0);
                    if (ALLOW_THREAD_GAP_WORK) {
                        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                    }
                    RecyclerView.this.stopNestedScroll(1);
                }
            }
            if (smoothScroller != null) {
                if (smoothScroller.isPendingInitialRun()) {
                    smoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    smoothScroller.stop();
                }
            }
            this.enableRunOnAnimationRequests();
        }

        public void smoothScrollBy(int n, int n2) {
            this.smoothScrollBy(n, n2, 0, 0);
        }

        public void smoothScrollBy(int n, int n2, int n3) {
            this.smoothScrollBy(n, n2, n3, RecyclerView.sQuinticInterpolator);
        }

        public void smoothScrollBy(int n, int n2, int n3, int n4) {
            this.smoothScrollBy(n, n2, this.computeScrollDuration(n, n2, n3, n4));
        }

        public void smoothScrollBy(int n, int n2, int n3, Interpolator interpolator) {
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mScroller = new OverScroller(RecyclerView.this.getContext(), interpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, n, n2, n3);
            if (Build.VERSION.SDK_INT < 23) {
                this.mScroller.computeScrollOffset();
            }
            this.postOnAnimation();
        }

        public void smoothScrollBy(int n, int n2, Interpolator interpolator) {
            int n3 = this.computeScrollDuration(n, n2, 0, 0);
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            this.smoothScrollBy(n, n2, n3, interpolator);
        }

        public void stop() {
            RecyclerView.this.removeCallbacks((Runnable)this);
            this.mScroller.abortAnimation();
        }
    }

    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_SET_A11Y_ITEM_DELEGATE = 16384;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap = false;
        private int mIsRecyclableCount = 0;
        long mItemId = -1L;
        int mItemViewType = -1;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition = -1;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads = null;
        @VisibleForTesting
        int mPendingAccessibilityState = -1;
        int mPosition = -1;
        int mPreLayoutPosition = -1;
        private Recycler mScrapContainer = null;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mWasImportantForAccessibilityBeforeHidden = 0;

        public ViewHolder(View view) {
            if (view != null) {
                this.itemView = view;
                return;
            }
            throw new IllegalArgumentException("itemView may not be null");
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList<Object>();
                this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
                return;
            }
        }

        private boolean doesTransientStatePreventRecycling() {
            if ((this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView)) {
                return true;
            }
            return false;
        }

        private void onEnteredHiddenState(RecyclerView recyclerView) {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            recyclerView.setChildImportantForAccessibilityInternal(this, 4);
        }

        private void onLeftHiddenState(RecyclerView recyclerView) {
            recyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        private boolean shouldBeKeptAsChild() {
            if ((this.mFlags & 16) != 0) {
                return true;
            }
            return false;
        }

        void addChangePayload(Object object) {
            if (object == null) {
                this.addFlags(1024);
                return;
            }
            if ((1024 & this.mFlags) == 0) {
                this.createPayloadsIfNeeded();
                this.mPayloads.add(object);
                return;
            }
        }

        void addFlags(int n) {
            this.mFlags |= n;
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void clearPayload() {
            List<Object> list = this.mPayloads;
            if (list != null) {
                list.clear();
            }
            this.mFlags &= -1025;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        void flagRemovedAndOffsetPosition(int n, int n2, boolean bl) {
            this.addFlags(8);
            this.offsetPosition(n2, bl);
            this.mPosition = n;
        }

        public final int getAdapterPosition() {
            RecyclerView recyclerView = this.mOwnerRecyclerView;
            if (recyclerView == null) {
                return -1;
            }
            return recyclerView.getAdapterPositionFor(this);
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        public final int getLayoutPosition() {
            int n;
            int n2 = n = this.mPreLayoutPosition;
            if (n == -1) {
                n2 = this.mPosition;
            }
            return n2;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        @Deprecated
        public final int getPosition() {
            int n;
            int n2 = n = this.mPreLayoutPosition;
            if (n == -1) {
                n2 = this.mPosition;
            }
            return n2;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) == 0) {
                List<Object> list = this.mPayloads;
                if (list != null && list.size() != 0) {
                    return this.mUnmodifiedPayloads;
                }
                return FULLUPDATE_PAYLOADS;
            }
            return FULLUPDATE_PAYLOADS;
        }

        boolean hasAnyOfTheFlags(int n) {
            if ((this.mFlags & n) != 0) {
                return true;
            }
            return false;
        }

        boolean isAdapterPositionUnknown() {
            if ((this.mFlags & 512) == 0 && !this.isInvalid()) {
                return false;
            }
            return true;
        }

        boolean isBound() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            return false;
        }

        boolean isInvalid() {
            if ((this.mFlags & 4) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isRecyclable() {
            if ((this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView)) {
                return true;
            }
            return false;
        }

        boolean isRemoved() {
            if ((this.mFlags & 8) != 0) {
                return true;
            }
            return false;
        }

        boolean isScrap() {
            if (this.mScrapContainer != null) {
                return true;
            }
            return false;
        }

        boolean isTmpDetached() {
            if ((this.mFlags & 256) != 0) {
                return true;
            }
            return false;
        }

        boolean isUpdated() {
            if ((this.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        boolean needsUpdate() {
            if ((this.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        void offsetPosition(int n, boolean bl) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (bl) {
                this.mPreLayoutPosition += n;
            }
            this.mPosition += n;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
                return;
            }
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }

        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
                return;
            }
        }

        void setFlags(int n, int n2) {
            this.mFlags = this.mFlags & ~ n2 | n & n2;
        }

        public final void setIsRecyclable(boolean bl) {
            int n = bl ? this.mIsRecyclableCount - 1 : this.mIsRecyclableCount + 1;
            this.mIsRecyclableCount = n;
            if ((n = this.mIsRecyclableCount) < 0) {
                this.mIsRecyclableCount = 0;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
                stringBuilder.append(this);
                Log.e((String)"View", (String)stringBuilder.toString());
                return;
            }
            if (!bl && n == 1) {
                this.mFlags |= 16;
                return;
            }
            if (bl && this.mIsRecyclableCount == 0) {
                this.mFlags &= -17;
                return;
            }
        }

        void setScrapContainer(Recycler recycler, boolean bl) {
            this.mScrapContainer = recycler;
            this.mInChangeScrap = bl;
        }

        boolean shouldIgnore() {
            if ((this.mFlags & 128) != 0) {
                return true;
            }
            return false;
        }

        void stopIgnoring() {
            this.mFlags &= -129;
        }

        public String toString() {
            CharSequence charSequence = new StringBuilder();
            charSequence.append("ViewHolder{");
            charSequence.append(Integer.toHexString(this.hashCode()));
            charSequence.append(" position=");
            charSequence.append(this.mPosition);
            charSequence.append(" id=");
            charSequence.append(this.mItemId);
            charSequence.append(", oldPos=");
            charSequence.append(this.mOldPosition);
            charSequence.append(", pLpos:");
            charSequence.append(this.mPreLayoutPosition);
            StringBuilder stringBuilder = new StringBuilder(charSequence.toString());
            if (this.isScrap()) {
                stringBuilder.append(" scrap ");
                charSequence = this.mInChangeScrap ? "[changeScrap]" : "[attachedScrap]";
                stringBuilder.append((String)charSequence);
            }
            if (this.isInvalid()) {
                stringBuilder.append(" invalid");
            }
            if (!this.isBound()) {
                stringBuilder.append(" unbound");
            }
            if (this.needsUpdate()) {
                stringBuilder.append(" update");
            }
            if (this.isRemoved()) {
                stringBuilder.append(" removed");
            }
            if (this.shouldIgnore()) {
                stringBuilder.append(" ignored");
            }
            if (this.isTmpDetached()) {
                stringBuilder.append(" tmpDetached");
            }
            if (!this.isRecyclable()) {
                charSequence = new StringBuilder();
                charSequence.append(" not recyclable(");
                charSequence.append(this.mIsRecyclableCount);
                charSequence.append(")");
                stringBuilder.append(charSequence.toString());
            }
            if (this.isAdapterPositionUnknown()) {
                stringBuilder.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                stringBuilder.append(" no parent");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }
    }

}

