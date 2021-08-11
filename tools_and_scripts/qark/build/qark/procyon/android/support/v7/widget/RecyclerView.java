// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import java.lang.reflect.AccessibleObject;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AbsSavedState;
import java.lang.ref.WeakReference;
import java.util.Collections;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.annotation.CallSuper;
import android.graphics.Matrix;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.database.Observable;
import android.os.SystemClock;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.View$MeasureSpec;
import android.view.Display;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.FocusFinder;
import android.widget.OverScroller;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.SparseArray;
import android.support.v4.os.TraceCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import android.view.ViewParent;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.recyclerview.R;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.view.VelocityTracker;
import android.graphics.RectF;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingChildHelper;
import java.util.List;
import java.util.ArrayList;
import android.support.annotation.VisibleForTesting;
import android.widget.EdgeEffect;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.ScrollingView;
import android.view.ViewGroup;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2
{
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
    private ItemAnimatorListener mItemAnimatorListener;
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
        NESTED_SCROLLING_ATTRS = new int[] { 16843830 };
        CLIP_TO_PADDING_ATTR = new int[] { 16842987 };
        FORCE_INVALIDATE_DISPLAY_LIST = (Build$VERSION.SDK_INT == 18 || Build$VERSION.SDK_INT == 19 || Build$VERSION.SDK_INT == 20);
        ALLOW_SIZE_IN_UNSPECIFIED_SPEC = (Build$VERSION.SDK_INT >= 23);
        POST_UPDATES_ON_ANIMATION = (Build$VERSION.SDK_INT >= 16);
        ALLOW_THREAD_GAP_WORK = (Build$VERSION.SDK_INT >= 21);
        FORCE_ABS_FOCUS_SEARCH_DIRECTION = (Build$VERSION.SDK_INT <= 15);
        IGNORE_DETACHED_FOCUSED_CHILD = (Build$VERSION.SDK_INT <= 15);
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE };
        sQuinticInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
    }
    
    public RecyclerView(final Context context) {
        this(context, null);
    }
    
    public RecyclerView(final Context context, @Nullable final AttributeSet set) {
        this(context, set, 0);
    }
    
    public RecyclerView(final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() {
            @Override
            public void run() {
                if (!RecyclerView.this.mFirstLayoutComplete) {
                    return;
                }
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
            }
        };
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mItemDecorations = new ArrayList<ItemDecoration>();
        this.mOnItemTouchListeners = new ArrayList<OnItemTouchListener>();
        this.mEatRequestLayout = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mItemAnimator = (ItemAnimator)new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            mPrefetchRegistry = new GapWorker.LayoutPrefetchRegistryImpl();
        }
        else {
            mPrefetchRegistry = null;
        }
        this.mPrefetchRegistry = mPrefetchRegistry;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = (ItemAnimatorListener)new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList<ViewHolder>();
        this.mItemAnimatorRunner = new Runnable() {
            @Override
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
            @Override
            public void processAppeared(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo, final ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processDisappeared(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processPersistent(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, itemHolderInfo, itemHolderInfo2)) {
                        RecyclerView.this.postAnimationRunner();
                    }
                }
                else if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, itemHolderInfo, itemHolderInfo2)) {
                    RecyclerView.this.postAnimationRunner();
                }
            }
            
            @Override
            public void unused(final ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, RecyclerView.CLIP_TO_PADDING_ATTR, n, 0);
            this.mClipToPadding = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
        }
        else {
            this.mClipToPadding = true;
        }
        this.setScrollContainer(true);
        this.setFocusableInTouchMode(true);
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(value, context);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(value, context);
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = value.getScaledMaximumFlingVelocity();
        this.setWillNotDraw(this.getOverScrollMode() == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        this.initAdapterManager();
        this.initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
        this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        final boolean b = true;
        boolean boolean1 = true;
        if (set != null) {
            final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, 0);
            final String string = obtainStyledAttributes2.getString(R.styleable.RecyclerView_layoutManager);
            if (obtainStyledAttributes2.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1) {
                this.setDescendantFocusability(262144);
            }
            this.mEnableFastScroller = obtainStyledAttributes2.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
            if (this.mEnableFastScroller) {
                this.initFastScroller((StateListDrawable)obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
            }
            obtainStyledAttributes2.recycle();
            this.createLayoutManager(context, string, set, n, 0);
            if (Build$VERSION.SDK_INT >= 21) {
                final TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(set, RecyclerView.NESTED_SCROLLING_ATTRS, n, 0);
                boolean1 = obtainStyledAttributes3.getBoolean(0, true);
                obtainStyledAttributes3.recycle();
            }
        }
        else {
            this.setDescendantFocusability(262144);
            boolean1 = b;
        }
        this.setNestedScrollingEnabled(boolean1);
    }
    
    static /* synthetic */ void access$000(final RecyclerView recyclerView, final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        recyclerView.attachViewToParent(view, n, viewGroup$LayoutParams);
    }
    
    static /* synthetic */ void access$100(final RecyclerView recyclerView, final int n) {
        recyclerView.detachViewFromParent(n);
    }
    
    static /* synthetic */ void access$1200(final RecyclerView recyclerView, final int n, final int n2) {
        recyclerView.setMeasuredDimension(n, n2);
    }
    
    static /* synthetic */ boolean access$700(final RecyclerView recyclerView) {
        return recyclerView.awakenScrollBars();
    }
    
    private void addAnimatingView(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        final boolean b = itemView.getParent() == this;
        this.mRecycler.unscrapView(this.getChildViewHolder(itemView));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(itemView, -1, itemView.getLayoutParams(), true);
            return;
        }
        if (!b) {
            this.mChildHelper.addView(itemView, true);
            return;
        }
        this.mChildHelper.hide(itemView);
    }
    
    private void animateChange(@NonNull final ViewHolder mShadowingHolder, @NonNull final ViewHolder mShadowedHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2, final boolean b, final boolean b2) {
        mShadowingHolder.setIsRecyclable(false);
        if (b) {
            this.addAnimatingView(mShadowingHolder);
        }
        if (mShadowingHolder != mShadowedHolder) {
            if (b2) {
                this.addAnimatingView(mShadowedHolder);
            }
            mShadowingHolder.mShadowedHolder = mShadowedHolder;
            this.addAnimatingView(mShadowingHolder);
            this.mRecycler.unscrapView(mShadowingHolder);
            mShadowedHolder.setIsRecyclable(false);
            mShadowedHolder.mShadowingHolder = mShadowingHolder;
        }
        if (this.mItemAnimator.animateChange(mShadowingHolder, mShadowedHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    private void cancelTouch() {
        this.resetTouch();
        this.setScrollState(0);
    }
    
    static void clearNestedRecyclerViewIfNotNested(@NonNull final ViewHolder viewHolder) {
        if (viewHolder.mNestedRecyclerView != null) {
            View view = viewHolder.mNestedRecyclerView.get();
            while (view != null) {
                if (view == viewHolder.itemView) {
                    return;
                }
                final ViewParent parent = view.getParent();
                if (parent instanceof View) {
                    view = (View)parent;
                }
                else {
                    view = null;
                }
            }
            viewHolder.mNestedRecyclerView = null;
        }
    }
    
    private void createLayoutManager(final Context context, String trim, final AttributeSet set, final int n, final int n2) {
        if (trim != null) {
            trim = ((String)trim).trim();
            if (!((String)trim).isEmpty()) {
                final String fullClassName = this.getFullClassName(context, (String)trim);
                try {
                    ClassLoader classLoader;
                    if (this.isInEditMode()) {
                        classLoader = this.getClass().getClassLoader();
                    }
                    else {
                        classLoader = context.getClassLoader();
                    }
                    final Class<? extends LayoutManager> subclass = classLoader.loadClass(fullClassName).asSubclass(LayoutManager.class);
                    trim = null;
                    try {
                        final Constructor<? extends LayoutManager> constructor = subclass.getConstructor(RecyclerView.LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        trim = new Object[] { context, set, n, n2 };
                        final Object constructor2 = constructor;
                    }
                    catch (NoSuchMethodException constructor) {
                        try {
                            final Object constructor2 = subclass.getConstructor((Class<?>[])new Class[0]);
                            ((AccessibleObject)constructor2).setAccessible(true);
                            this.setLayoutManager((LayoutManager)((Constructor<? extends LayoutManager>)constructor2).newInstance((Object[])trim));
                        }
                        catch (NoSuchMethodException ex) {
                            ex.initCause((Throwable)constructor);
                            final StringBuilder sb = new StringBuilder();
                            sb.append(set.getPositionDescription());
                            sb.append(": Error creating LayoutManager ");
                            sb.append(fullClassName);
                            throw new IllegalStateException(sb.toString(), ex);
                        }
                    }
                }
                catch (ClassCastException ex2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(set.getPositionDescription());
                    sb2.append(": Class is not a LayoutManager ");
                    sb2.append(fullClassName);
                    throw new IllegalStateException(sb2.toString(), ex2);
                }
                catch (IllegalAccessException ex3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(set.getPositionDescription());
                    sb3.append(": Cannot access non-public constructor ");
                    sb3.append(fullClassName);
                    throw new IllegalStateException(sb3.toString(), ex3);
                }
                catch (InstantiationException ex4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(set.getPositionDescription());
                    sb4.append(": Could not instantiate the LayoutManager: ");
                    sb4.append(fullClassName);
                    throw new IllegalStateException(sb4.toString(), ex4);
                }
                catch (InvocationTargetException ex5) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(set.getPositionDescription());
                    sb5.append(": Could not instantiate the LayoutManager: ");
                    sb5.append(fullClassName);
                    throw new IllegalStateException(sb5.toString(), ex5);
                }
                catch (ClassNotFoundException ex6) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(set.getPositionDescription());
                    sb6.append(": Unable to find LayoutManager ");
                    sb6.append(fullClassName);
                    throw new IllegalStateException(sb6.toString(), ex6);
                }
            }
        }
    }
    
    private boolean didChildRangeChange(final int n, final int n2) {
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        final int[] mMinMaxLayoutPositions = this.mMinMaxLayoutPositions;
        return mMinMaxLayoutPositions[0] != n || mMinMaxLayoutPositions[1] != n2;
    }
    
    private void dispatchContentChangedIfNecessary() {
        final int mEatenAccessibilityChangeFlags = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (mEatenAccessibilityChangeFlags != 0 && this.isAccessibilityEnabled()) {
            final AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(obtain, mEatenAccessibilityChangeFlags);
            this.sendAccessibilityEventUnchecked(obtain);
        }
    }
    
    private void dispatchLayoutStep1() {
        final State mState = this.mState;
        boolean mTrackOldChangeHolders = true;
        mState.assertLayoutStep(1);
        this.fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        this.eatRequestLayout();
        this.mViewInfoStore.clear();
        this.onEnterLayoutOrScroll();
        this.processAdapterUpdatesAndSetAnimationFlags();
        this.saveFocusInfo();
        final State mState2 = this.mState;
        if (!mState2.mRunSimpleAnimations || !this.mItemsChanged) {
            mTrackOldChangeHolders = false;
        }
        mState2.mTrackOldChangeHolders = mTrackOldChangeHolders;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        final State mState3 = this.mState;
        mState3.mInPreLayout = mState3.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore()) {
                    if (!childViewHolderInt.isInvalid() || this.mAdapter.hasStableIds()) {
                        this.mViewInfoStore.addToPreLayout(childViewHolderInt, this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt, ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt), childViewHolderInt.getUnmodifiedPayloads()));
                        if (this.mState.mTrackOldChangeHolders && childViewHolderInt.isUpdated() && !childViewHolderInt.isRemoved()) {
                            if (!childViewHolderInt.shouldIgnore() && !childViewHolderInt.isInvalid()) {
                                this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(childViewHolderInt), childViewHolderInt);
                            }
                        }
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            this.saveOldPositions();
            final boolean mStructureChanged = this.mState.mStructureChanged;
            final State mState4 = this.mState;
            mState4.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, mState4);
            this.mState.mStructureChanged = mStructureChanged;
            for (int j = 0; j < this.mChildHelper.getChildCount(); ++j) {
                final ViewHolder childViewHolderInt2 = getChildViewHolderInt(this.mChildHelper.getChildAt(j));
                if (!childViewHolderInt2.shouldIgnore()) {
                    if (!this.mViewInfoStore.isInPreLayout(childViewHolderInt2)) {
                        int buildAdapterChangeFlagsForAnimations = ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt2);
                        final boolean hasAnyOfTheFlags = childViewHolderInt2.hasAnyOfTheFlags(8192);
                        if (!hasAnyOfTheFlags) {
                            buildAdapterChangeFlagsForAnimations |= 0x1000;
                        }
                        final ItemHolderInfo recordPreLayoutInformation = this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt2, buildAdapterChangeFlagsForAnimations, childViewHolderInt2.getUnmodifiedPayloads());
                        if (hasAnyOfTheFlags) {
                            this.recordAnimationInfoIfBouncedHiddenView(childViewHolderInt2, recordPreLayoutInformation);
                        }
                        else {
                            this.mViewInfoStore.addToAppearedInPreLayoutHolders(childViewHolderInt2, recordPreLayoutInformation);
                        }
                    }
                }
            }
            this.clearOldPositions();
        }
        else {
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
        final State mState = this.mState;
        mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, mState);
        final State mState2 = this.mState;
        mState2.mStructureChanged = false;
        this.mPendingSavedState = null;
        mState2.mRunSimpleAnimations = (mState2.mRunSimpleAnimations && this.mItemAnimator != null);
        this.mState.mLayoutStep = 4;
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
    }
    
    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        final State mState = this.mState;
        mState.mLayoutStep = 1;
        if (mState.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore()) {
                    final long changedHolderKey = this.getChangedHolderKey(childViewHolderInt);
                    final ItemHolderInfo recordPostLayoutInformation = this.mItemAnimator.recordPostLayoutInformation(this.mState, childViewHolderInt);
                    final ViewHolder fromOldChangeHolders = this.mViewInfoStore.getFromOldChangeHolders(changedHolderKey);
                    if (fromOldChangeHolders != null && !fromOldChangeHolders.shouldIgnore()) {
                        final boolean disappearing = this.mViewInfoStore.isDisappearing(fromOldChangeHolders);
                        final boolean disappearing2 = this.mViewInfoStore.isDisappearing(childViewHolderInt);
                        if (disappearing && fromOldChangeHolders == childViewHolderInt) {
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                        }
                        else {
                            final ItemHolderInfo popFromPreLayout = this.mViewInfoStore.popFromPreLayout(fromOldChangeHolders);
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                            final ItemHolderInfo popFromPostLayout = this.mViewInfoStore.popFromPostLayout(childViewHolderInt);
                            if (popFromPreLayout == null) {
                                this.handleMissingPreInfoForChangeError(changedHolderKey, childViewHolderInt, fromOldChangeHolders);
                            }
                            else {
                                this.animateChange(fromOldChangeHolders, childViewHolderInt, popFromPreLayout, popFromPostLayout, disappearing, disappearing2);
                            }
                        }
                    }
                    else {
                        this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        final State mState2 = this.mState;
        mState2.mPreviousLayoutItemCount = mState2.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        final State mState3 = this.mState;
        mState3.mRunSimpleAnimations = false;
        mState3.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            final LayoutManager mLayout = this.mLayout;
            mLayout.mPrefetchMaxCountObserved = 0;
            mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
            this.mRecycler.updateViewCacheSize();
        }
        this.mLayout.onLayoutCompleted(this.mState);
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
        this.mViewInfoStore.clear();
        final int[] mMinMaxLayoutPositions = this.mMinMaxLayoutPositions;
        if (this.didChildRangeChange(mMinMaxLayoutPositions[0], mMinMaxLayoutPositions[1])) {
            this.dispatchOnScrolled(0, 0);
        }
        this.recoverFocusFromState();
        this.resetFocusInfo();
    }
    
    private boolean dispatchOnItemTouch(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        final OnItemTouchListener mActiveOnItemTouchListener = this.mActiveOnItemTouchListener;
        if (mActiveOnItemTouchListener != null) {
            if (action == 0) {
                this.mActiveOnItemTouchListener = null;
            }
            else {
                mActiveOnItemTouchListener.onTouchEvent(this, motionEvent);
                if (action != 3 && action != 1) {
                    return true;
                }
                this.mActiveOnItemTouchListener = null;
                return true;
            }
        }
        if (action != 0) {
            for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
                final OnItemTouchListener mActiveOnItemTouchListener2 = this.mOnItemTouchListeners.get(i);
                if (mActiveOnItemTouchListener2.onInterceptTouchEvent(this, motionEvent)) {
                    this.mActiveOnItemTouchListener = mActiveOnItemTouchListener2;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean dispatchOnItemTouchIntercept(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            final OnItemTouchListener mActiveOnItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (mActiveOnItemTouchListener.onInterceptTouchEvent(this, motionEvent) && action != 3) {
                this.mActiveOnItemTouchListener = mActiveOnItemTouchListener;
                return true;
            }
        }
        return false;
    }
    
    private void findMinMaxChildLayoutPositions(final int[] array) {
        final int childCount = this.mChildHelper.getChildCount();
        if (childCount == 0) {
            array[1] = (array[0] = -1);
            return;
        }
        int n = Integer.MAX_VALUE;
        int n2 = Integer.MIN_VALUE;
        for (int i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                final int layoutPosition = childViewHolderInt.getLayoutPosition();
                if (layoutPosition < n) {
                    n = layoutPosition;
                }
                if (layoutPosition > n2) {
                    n2 = layoutPosition;
                }
            }
        }
        array[0] = n;
        array[1] = n2;
    }
    
    @Nullable
    static RecyclerView findNestedRecyclerView(@NonNull final View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView)view;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final RecyclerView nestedRecyclerView = findNestedRecyclerView(viewGroup.getChildAt(i));
            if (nestedRecyclerView != null) {
                return nestedRecyclerView;
            }
        }
        return null;
    }
    
    @Nullable
    private View findNextViewToFocus() {
        int mFocusedItemPosition;
        if (this.mState.mFocusedItemPosition != -1) {
            mFocusedItemPosition = this.mState.mFocusedItemPosition;
        }
        else {
            mFocusedItemPosition = 0;
        }
        final int itemCount = this.mState.getItemCount();
        for (int i = mFocusedItemPosition; i < itemCount; ++i) {
            final ViewHolder viewHolderForAdapterPosition = this.findViewHolderForAdapterPosition(i);
            if (viewHolderForAdapterPosition == null) {
                break;
            }
            if (viewHolderForAdapterPosition.itemView.hasFocusable()) {
                return viewHolderForAdapterPosition.itemView;
            }
        }
        for (int j = Math.min(itemCount, mFocusedItemPosition) - 1; j >= 0; --j) {
            final ViewHolder viewHolderForAdapterPosition2 = this.findViewHolderForAdapterPosition(j);
            if (viewHolderForAdapterPosition2 == null) {
                return null;
            }
            if (viewHolderForAdapterPosition2.itemView.hasFocusable()) {
                return viewHolderForAdapterPosition2.itemView;
            }
        }
        return null;
    }
    
    static ViewHolder getChildViewHolderInt(final View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).mViewHolder;
    }
    
    static void getDecoratedBoundsWithMarginsInt(final View view, final Rect rect) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        rect.set(view.getLeft() - mDecorInsets.left - layoutParams.leftMargin, view.getTop() - mDecorInsets.top - layoutParams.topMargin, view.getRight() + mDecorInsets.right + layoutParams.rightMargin, view.getBottom() + mDecorInsets.bottom + layoutParams.bottomMargin);
    }
    
    private int getDeepestFocusedViewWithId(View focusedChild) {
        int n = focusedChild.getId();
        while (!focusedChild.isFocused() && focusedChild instanceof ViewGroup && focusedChild.hasFocus()) {
            focusedChild = ((ViewGroup)focusedChild).getFocusedChild();
            if (focusedChild.getId() != -1) {
                n = focusedChild.getId();
            }
        }
        return n;
    }
    
    private String getFullClassName(final Context context, final String s) {
        if (s.charAt(0) == '.') {
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(s);
            return sb.toString();
        }
        if (s.contains(".")) {
            return s;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(RecyclerView.class.getPackage().getName());
        sb2.append('.');
        sb2.append(s);
        return sb2.toString();
    }
    
    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        }
        return this.mScrollingChildHelper;
    }
    
    private void handleMissingPreInfoForChangeError(final long n, final ViewHolder viewHolder, final ViewHolder viewHolder2) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != viewHolder) {
                if (this.getChangedHolderKey(childViewHolderInt) == n) {
                    final Adapter mAdapter = this.mAdapter;
                    if (mAdapter != null && mAdapter.hasStableIds()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
                        sb.append(childViewHolderInt);
                        sb.append(" \n View Holder 2:");
                        sb.append(viewHolder);
                        sb.append(this.exceptionLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
                    sb2.append(childViewHolderInt);
                    sb2.append(" \n View Holder 2:");
                    sb2.append(viewHolder);
                    sb2.append(this.exceptionLabel());
                    throw new IllegalStateException(sb2.toString());
                }
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
        sb3.append(viewHolder2);
        sb3.append(" cannot be found but it is necessary for ");
        sb3.append(viewHolder);
        sb3.append(this.exceptionLabel());
        Log.e("RecyclerView", sb3.toString());
    }
    
    private boolean hasUpdatedView() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != null) {
                if (!childViewHolderInt.shouldIgnore()) {
                    if (childViewHolderInt.isUpdated()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper((ChildHelper.Callback)new ChildHelper.Callback() {
            @Override
            public void addView(final View view, final int n) {
                RecyclerView.this.addView(view, n);
                RecyclerView.this.dispatchChildAttached(view);
            }
            
            @Override
            public void attachViewToParent(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    if (!childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Called attach on a child which is not detached: ");
                        sb.append(childViewHolderInt);
                        sb.append(RecyclerView.this.exceptionLabel());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    childViewHolderInt.clearTmpDetachFlag();
                }
                RecyclerView.access$000(RecyclerView.this, view, n, viewGroup$LayoutParams);
            }
            
            @Override
            public void detachViewFromParent(final int n) {
                final View child = this.getChildAt(n);
                if (child != null) {
                    final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                    if (childViewHolderInt != null) {
                        if (childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("called detach on an already detached child ");
                            sb.append(childViewHolderInt);
                            sb.append(RecyclerView.this.exceptionLabel());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        childViewHolderInt.addFlags(256);
                    }
                }
                RecyclerView.access$100(RecyclerView.this, n);
            }
            
            @Override
            public View getChildAt(final int n) {
                return RecyclerView.this.getChildAt(n);
            }
            
            @Override
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }
            
            @Override
            public ViewHolder getChildViewHolder(final View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }
            
            @Override
            public int indexOfChild(final View view) {
                return RecyclerView.this.indexOfChild(view);
            }
            
            @Override
            public void onEnteredHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onEnteredHiddenState(RecyclerView.this);
                }
            }
            
            @Override
            public void onLeftHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onLeftHiddenState(RecyclerView.this);
                }
            }
            
            @Override
            public void removeAllViews() {
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    RecyclerView.this.dispatchChildDetached(child);
                    child.clearAnimation();
                }
                RecyclerView.this.removeAllViews();
            }
            
            @Override
            public void removeViewAt(final int n) {
                final View child = RecyclerView.this.getChildAt(n);
                if (child != null) {
                    RecyclerView.this.dispatchChildDetached(child);
                    child.clearAnimation();
                }
                RecyclerView.this.removeViewAt(n);
            }
        });
    }
    
    private boolean isPreferredNextFocus(final View view, final View view2, final int n) {
        int n2 = false ? 1 : 0;
        if (view2 == null) {
            return false;
        }
        if (view2 == this) {
            return false;
        }
        if (view == null) {
            return true;
        }
        if (n != 2 && n != 1) {
            return this.isPreferredNextFocusAbsolute(view, view2, n);
        }
        final boolean b = this.mLayout.getLayoutDirection() == 1;
        if (n == 2) {
            n2 = (true ? 1 : 0);
        }
        int n3;
        if ((n2 ^ (b ? 1 : 0)) != 0x0) {
            n3 = 66;
        }
        else {
            n3 = 17;
        }
        if (this.isPreferredNextFocusAbsolute(view, view2, n3)) {
            return true;
        }
        if (n == 2) {
            return this.isPreferredNextFocusAbsolute(view, view2, 130);
        }
        return this.isPreferredNextFocusAbsolute(view, view2, 33);
    }
    
    private boolean isPreferredNextFocusAbsolute(final View view, final View view2, final int n) {
        this.mTempRect.set(0, 0, view.getWidth(), view.getHeight());
        this.mTempRect2.set(0, 0, view2.getWidth(), view2.getHeight());
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        this.offsetDescendantRectToMyCoords(view2, this.mTempRect2);
        if (n == 17) {
            return (this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left;
        }
        if (n == 33) {
            return (this.mTempRect.bottom > this.mTempRect2.bottom || this.mTempRect.top >= this.mTempRect2.bottom) && this.mTempRect.top > this.mTempRect2.top;
        }
        if (n == 66) {
            return (this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right;
        }
        if (n == 130) {
            return (this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("direction must be absolute. received:");
        sb.append(n);
        sb.append(this.exceptionLabel());
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void onPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mScrollPointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mScrollPointerId = motionEvent.getPointerId(n);
            final int n2 = (int)(motionEvent.getX(n) + 0.5f);
            this.mLastTouchX = n2;
            this.mInitialTouchX = n2;
            final int n3 = (int)(motionEvent.getY(n) + 0.5f);
            this.mLastTouchY = n3;
            this.mInitialTouchY = n3;
        }
    }
    
    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }
    
    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            this.mLayout.onItemsChanged(this);
        }
        if (this.predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        }
        else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        final boolean mItemsAddedOrRemoved = this.mItemsAddedOrRemoved;
        final boolean b = false;
        final boolean b2 = mItemsAddedOrRemoved || this.mItemsChanged;
        this.mState.mRunSimpleAnimations = (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || b2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds()));
        final State mState = this.mState;
        mState.mRunPredictiveAnimations = ((mState.mRunSimpleAnimations && b2 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled()) || b);
    }
    
    private void pullGlows(final float n, final float n2, final float n3, final float n4) {
        boolean b = false;
        if (n2 < 0.0f) {
            this.ensureLeftGlow();
            EdgeEffectCompat.onPull(this.mLeftGlow, -n2 / this.getWidth(), 1.0f - n3 / this.getHeight());
            b = true;
        }
        else if (n2 > 0.0f) {
            this.ensureRightGlow();
            EdgeEffectCompat.onPull(this.mRightGlow, n2 / this.getWidth(), n3 / this.getHeight());
            b = true;
        }
        if (n4 < 0.0f) {
            this.ensureTopGlow();
            EdgeEffectCompat.onPull(this.mTopGlow, -n4 / this.getHeight(), n / this.getWidth());
            b = true;
        }
        else if (n4 > 0.0f) {
            this.ensureBottomGlow();
            EdgeEffectCompat.onPull(this.mBottomGlow, n4 / this.getHeight(), 1.0f - n / this.getWidth());
            b = true;
        }
        if (!b && n2 == 0.0f && n4 == 0.0f) {
            return;
        }
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    private void recoverFocusFromState() {
        if (!this.mPreserveFocusAfterLayout || this.mAdapter == null || !this.hasFocus()) {
            return;
        }
        if (this.getDescendantFocusability() == 393216) {
            return;
        }
        if (this.getDescendantFocusability() == 131072 && this.isFocused()) {
            return;
        }
        if (!this.isFocused()) {
            final View focusedChild = this.getFocusedChild();
            if (RecyclerView.IGNORE_DETACHED_FOCUSED_CHILD && (focusedChild.getParent() == null || !focusedChild.hasFocus())) {
                if (this.mChildHelper.getChildCount() == 0) {
                    this.requestFocus();
                    return;
                }
            }
            else if (!this.mChildHelper.isHidden(focusedChild)) {
                return;
            }
        }
        ViewHolder viewHolderForItemId = null;
        if (this.mState.mFocusedItemId != -1L && this.mAdapter.hasStableIds()) {
            viewHolderForItemId = this.findViewHolderForItemId(this.mState.mFocusedItemId);
        }
        final View view = null;
        View view2;
        if (viewHolderForItemId != null && !this.mChildHelper.isHidden(viewHolderForItemId.itemView) && viewHolderForItemId.itemView.hasFocusable()) {
            view2 = viewHolderForItemId.itemView;
        }
        else if (this.mChildHelper.getChildCount() > 0) {
            view2 = this.findNextViewToFocus();
        }
        else {
            view2 = view;
        }
        if (view2 != null) {
            if (this.mState.mFocusedSubChildId != -1L) {
                final View viewById = view2.findViewById(this.mState.mFocusedSubChildId);
                if (viewById != null && viewById.isFocusable()) {
                    view2 = viewById;
                }
            }
            view2.requestFocus();
        }
    }
    
    private void releaseGlows() {
        boolean finished = false;
        final EdgeEffect mLeftGlow = this.mLeftGlow;
        if (mLeftGlow != null) {
            mLeftGlow.onRelease();
            finished = this.mLeftGlow.isFinished();
        }
        final EdgeEffect mTopGlow = this.mTopGlow;
        if (mTopGlow != null) {
            mTopGlow.onRelease();
            finished |= this.mTopGlow.isFinished();
        }
        final EdgeEffect mRightGlow = this.mRightGlow;
        if (mRightGlow != null) {
            mRightGlow.onRelease();
            finished |= this.mRightGlow.isFinished();
        }
        final EdgeEffect mBottomGlow = this.mBottomGlow;
        if (mBottomGlow != null) {
            mBottomGlow.onRelease();
            finished |= this.mBottomGlow.isFinished();
        }
        if (finished) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void requestChildOnScreen(@NonNull final View view, @Nullable final View view2) {
        View view3;
        if (view2 != null) {
            view3 = view2;
        }
        else {
            view3 = view;
        }
        this.mTempRect.set(0, 0, view3.getWidth(), view3.getHeight());
        final ViewGroup$LayoutParams layoutParams = view3.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
            if (!layoutParams2.mInsetsDirty) {
                final Rect mDecorInsets = layoutParams2.mDecorInsets;
                final Rect mTempRect = this.mTempRect;
                mTempRect.left -= mDecorInsets.left;
                final Rect mTempRect2 = this.mTempRect;
                mTempRect2.right += mDecorInsets.right;
                final Rect mTempRect3 = this.mTempRect;
                mTempRect3.top -= mDecorInsets.top;
                final Rect mTempRect4 = this.mTempRect;
                mTempRect4.bottom += mDecorInsets.bottom;
            }
        }
        if (view2 != null) {
            this.offsetDescendantRectToMyCoords(view2, this.mTempRect);
            this.offsetRectIntoDescendantCoords(view, this.mTempRect);
        }
        this.mLayout.requestChildRectangleOnScreen(this, view, this.mTempRect, this.mFirstLayoutComplete ^ true, view2 == null);
    }
    
    private void resetFocusInfo() {
        final State mState = this.mState;
        mState.mFocusedItemId = -1L;
        mState.mFocusedItemPosition = -1;
        mState.mFocusedSubChildId = -1;
    }
    
    private void resetTouch() {
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        this.stopNestedScroll(0);
        this.releaseGlows();
    }
    
    private void saveFocusInfo() {
        View focusedChild = null;
        if (this.mPreserveFocusAfterLayout && this.hasFocus() && this.mAdapter != null) {
            focusedChild = this.getFocusedChild();
        }
        ViewHolder containingViewHolder;
        if (focusedChild == null) {
            containingViewHolder = null;
        }
        else {
            containingViewHolder = this.findContainingViewHolder(focusedChild);
        }
        if (containingViewHolder == null) {
            this.resetFocusInfo();
            return;
        }
        final State mState = this.mState;
        long itemId;
        if (this.mAdapter.hasStableIds()) {
            itemId = containingViewHolder.getItemId();
        }
        else {
            itemId = -1L;
        }
        mState.mFocusedItemId = itemId;
        final State mState2 = this.mState;
        int mFocusedItemPosition;
        if (this.mDataSetHasChangedAfterLayout) {
            mFocusedItemPosition = -1;
        }
        else if (containingViewHolder.isRemoved()) {
            mFocusedItemPosition = containingViewHolder.mOldPosition;
        }
        else {
            mFocusedItemPosition = containingViewHolder.getAdapterPosition();
        }
        mState2.mFocusedItemPosition = mFocusedItemPosition;
        this.mState.mFocusedSubChildId = this.getDeepestFocusedViewWithId(containingViewHolder.itemView);
    }
    
    private void setAdapterInternal(final Adapter mAdapter, final boolean b, final boolean b2) {
        final Adapter mAdapter2 = this.mAdapter;
        if (mAdapter2 != null) {
            mAdapter2.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!b || b2) {
            this.removeAndRecycleViews();
        }
        this.mAdapterHelper.reset();
        final Adapter mAdapter3 = this.mAdapter;
        this.mAdapter = mAdapter;
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(this.mObserver);
            mAdapter.onAttachedToRecyclerView(this);
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.onAdapterChanged(mAdapter3, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(mAdapter3, this.mAdapter, b);
        this.mState.mStructureChanged = true;
        this.setDataSetChangedAfterLayout();
    }
    
    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.stopSmoothScroller();
        }
    }
    
    void absorbGlows(final int n, final int n2) {
        if (n < 0) {
            this.ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-n);
        }
        else if (n > 0) {
            this.ensureRightGlow();
            this.mRightGlow.onAbsorb(n);
        }
        if (n2 < 0) {
            this.ensureTopGlow();
            this.mTopGlow.onAbsorb(-n2);
        }
        else if (n2 > 0) {
            this.ensureBottomGlow();
            this.mBottomGlow.onAbsorb(n2);
        }
        if (n == 0 && n2 == 0) {
            return;
        }
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null && mLayout.onAddFocusables(this, list, n, n2)) {
            return;
        }
        super.addFocusables((ArrayList)list, n, n2);
    }
    
    public void addItemDecoration(final ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration, -1);
    }
    
    public void addItemDecoration(final ItemDecoration itemDecoration, final int n) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(false);
        }
        if (n < 0) {
            this.mItemDecorations.add(itemDecoration);
        }
        else {
            this.mItemDecorations.add(n, itemDecoration);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void addOnChildAttachStateChangeListener(final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>();
        }
        this.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
    }
    
    public void addOnItemTouchListener(final OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.add(onItemTouchListener);
    }
    
    public void addOnScrollListener(final OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList<OnScrollListener>();
        }
        this.mScrollListeners.add(onScrollListener);
    }
    
    void animateAppearance(@NonNull final ViewHolder viewHolder, @Nullable final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    void animateDisappearance(@NonNull final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
        this.addAnimatingView(viewHolder);
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    void assertInLayoutOrScroll(final String s) {
        if (this.isComputingLayout()) {
            return;
        }
        if (s == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            sb.append(this.exceptionLabel());
            throw new IllegalStateException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(this.exceptionLabel());
        throw new IllegalStateException(sb2.toString());
    }
    
    void assertNotInLayoutOrScroll(final String s) {
        if (this.isComputingLayout()) {
            if (s == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
                sb.append(this.exceptionLabel());
                throw new IllegalStateException(sb.toString());
            }
            throw new IllegalStateException(s);
        }
        else if (this.mDispatchScrollCounter > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.exceptionLabel());
            Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", (Throwable)new IllegalStateException(sb2.toString()));
        }
    }
    
    boolean canReuseUpdatedViewHolder(final ViewHolder viewHolder) {
        final ItemAnimator mItemAnimator = this.mItemAnimator;
        return mItemAnimator == null || mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)viewGroup$LayoutParams);
    }
    
    void clearOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }
    
    public void clearOnChildAttachStateChangeListeners() {
        final List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners = this.mOnChildAttachStateListeners;
        if (mOnChildAttachStateListeners != null) {
            mOnChildAttachStateListeners.clear();
        }
    }
    
    public void clearOnScrollListeners() {
        final List<OnScrollListener> mScrollListeners = this.mScrollListeners;
        if (mScrollListeners != null) {
            mScrollListeners.clear();
        }
    }
    
    public int computeHorizontalScrollExtent() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollExtent = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollHorizontally()) {
            computeHorizontalScrollExtent = this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return computeHorizontalScrollExtent;
    }
    
    public int computeHorizontalScrollOffset() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollOffset = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollHorizontally()) {
            computeHorizontalScrollOffset = this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return computeHorizontalScrollOffset;
    }
    
    public int computeHorizontalScrollRange() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollRange = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollHorizontally()) {
            computeHorizontalScrollRange = this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return computeHorizontalScrollRange;
    }
    
    public int computeVerticalScrollExtent() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollExtent = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollVertically()) {
            computeVerticalScrollExtent = this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return computeVerticalScrollExtent;
    }
    
    public int computeVerticalScrollOffset() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollOffset = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollVertically()) {
            computeVerticalScrollOffset = this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return computeVerticalScrollOffset;
    }
    
    public int computeVerticalScrollRange() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollRange = 0;
        if (mLayout == null) {
            return 0;
        }
        if (mLayout.canScrollVertically()) {
            computeVerticalScrollRange = this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return computeVerticalScrollRange;
    }
    
    void considerReleasingGlowsOnScroll(final int n, final int n2) {
        boolean finished = false;
        final EdgeEffect mLeftGlow = this.mLeftGlow;
        if (mLeftGlow != null && !mLeftGlow.isFinished() && n > 0) {
            this.mLeftGlow.onRelease();
            finished = this.mLeftGlow.isFinished();
        }
        final EdgeEffect mRightGlow = this.mRightGlow;
        if (mRightGlow != null && !mRightGlow.isFinished() && n < 0) {
            this.mRightGlow.onRelease();
            finished |= this.mRightGlow.isFinished();
        }
        final EdgeEffect mTopGlow = this.mTopGlow;
        if (mTopGlow != null && !mTopGlow.isFinished() && n2 > 0) {
            this.mTopGlow.onRelease();
            finished |= this.mTopGlow.isFinished();
        }
        final EdgeEffect mBottomGlow = this.mBottomGlow;
        if (mBottomGlow != null && !mBottomGlow.isFinished() && n2 < 0) {
            this.mBottomGlow.onRelease();
            finished |= this.mBottomGlow.isFinished();
        }
        if (finished) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    void consumePendingUpdateOperations() {
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection("RV FullInvalidate");
            this.dispatchLayout();
            TraceCompat.endSection();
            return;
        }
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
                }
                else {
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
        }
    }
    
    void defaultOnMeasure(final int n, final int n2) {
        this.setMeasuredDimension(LayoutManager.chooseSize(n, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(n2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
    }
    
    void dispatchChildAttached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildAttachedToWindow(view);
        final Adapter mAdapter = this.mAdapter;
        if (mAdapter != null && childViewHolderInt != null) {
            mAdapter.onViewAttachedToWindow(childViewHolderInt);
        }
        final List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners = this.mOnChildAttachStateListeners;
        if (mOnChildAttachStateListeners != null) {
            for (int i = mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(view);
            }
        }
    }
    
    void dispatchChildDetached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildDetachedFromWindow(view);
        final Adapter mAdapter = this.mAdapter;
        if (mAdapter != null && childViewHolderInt != null) {
            mAdapter.onViewDetachedFromWindow(childViewHolderInt);
        }
        final List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners = this.mOnChildAttachStateListeners;
        if (mOnChildAttachStateListeners != null) {
            for (int i = mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(view);
            }
        }
    }
    
    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e("RecyclerView", "No adapter attached; skipping layout");
            return;
        }
        if (this.mLayout == null) {
            Log.e("RecyclerView", "No layout manager attached; skipping layout");
            return;
        }
        final State mState = this.mState;
        mState.mIsMeasuring = false;
        if (mState.mLayoutStep == 1) {
            this.dispatchLayoutStep1();
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        }
        else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == this.getWidth() && this.mLayout.getHeight() == this.getHeight()) {
            this.mLayout.setExactMeasureSpecsFrom(this);
        }
        else {
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        }
        this.dispatchLayoutStep3();
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.getScrollingChildHelper().dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.getScrollingChildHelper().dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2, final int n3) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, array, array2, n3);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array, final int n5) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, array, n5);
    }
    
    void dispatchOnScrollStateChanged(final int n) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.onScrollStateChanged(n);
        }
        this.onScrollStateChanged(n);
        final OnScrollListener mScrollListener = this.mScrollListener;
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(this, n);
        }
        final List<OnScrollListener> mScrollListeners = this.mScrollListeners;
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, n);
            }
        }
    }
    
    void dispatchOnScrolled(final int n, final int n2) {
        ++this.mDispatchScrollCounter;
        final int scrollX = this.getScrollX();
        final int scrollY = this.getScrollY();
        this.onScrollChanged(scrollX, scrollY, scrollX, scrollY);
        this.onScrolled(n, n2);
        final OnScrollListener mScrollListener = this.mScrollListener;
        if (mScrollListener != null) {
            mScrollListener.onScrolled(this, n, n2);
        }
        final List<OnScrollListener> mScrollListeners = this.mScrollListeners;
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrolled(this, n, n2);
            }
        }
        --this.mDispatchScrollCounter;
    }
    
    void dispatchPendingImportantForAccessibilityChanges() {
        for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; --i) {
            final ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
            if (viewHolder.itemView.getParent() == this) {
                if (!viewHolder.shouldIgnore()) {
                    final int mPendingAccessibilityState = viewHolder.mPendingAccessibilityState;
                    if (mPendingAccessibilityState != -1) {
                        ViewCompat.setImportantForAccessibility(viewHolder.itemView, mPendingAccessibilityState);
                        viewHolder.mPendingAccessibilityState = -1;
                    }
                }
            }
        }
        this.mPendingAccessibilityImportanceChange.clear();
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly((SparseArray)sparseArray);
    }
    
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly((SparseArray)sparseArray);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDrawOver(canvas, this, this.mState);
        }
        boolean b = false;
        final EdgeEffect mLeftGlow = this.mLeftGlow;
        final boolean b2 = true;
        if (mLeftGlow != null && !mLeftGlow.isFinished()) {
            final int save = canvas.save();
            int paddingBottom;
            if (this.mClipToPadding) {
                paddingBottom = this.getPaddingBottom();
            }
            else {
                paddingBottom = 0;
            }
            canvas.rotate(270.0f);
            canvas.translate((float)(-this.getHeight() + paddingBottom), 0.0f);
            final EdgeEffect mLeftGlow2 = this.mLeftGlow;
            b = (mLeftGlow2 != null && mLeftGlow2.draw(canvas) && true);
            canvas.restoreToCount(save);
        }
        final EdgeEffect mTopGlow = this.mTopGlow;
        if (mTopGlow != null && !mTopGlow.isFinished()) {
            final int save2 = canvas.save();
            if (this.mClipToPadding) {
                canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }
            final EdgeEffect mTopGlow2 = this.mTopGlow;
            b |= (mTopGlow2 != null && mTopGlow2.draw(canvas));
            canvas.restoreToCount(save2);
        }
        final EdgeEffect mRightGlow = this.mRightGlow;
        if (mRightGlow != null && !mRightGlow.isFinished()) {
            final int save3 = canvas.save();
            final int width = this.getWidth();
            int paddingTop;
            if (this.mClipToPadding) {
                paddingTop = this.getPaddingTop();
            }
            else {
                paddingTop = 0;
            }
            canvas.rotate(90.0f);
            canvas.translate((float)(-paddingTop), (float)(-width));
            final EdgeEffect mRightGlow2 = this.mRightGlow;
            b |= (mRightGlow2 != null && mRightGlow2.draw(canvas));
            canvas.restoreToCount(save3);
        }
        final EdgeEffect mBottomGlow = this.mBottomGlow;
        if (mBottomGlow != null && !mBottomGlow.isFinished()) {
            final int save4 = canvas.save();
            canvas.rotate(180.0f);
            if (this.mClipToPadding) {
                canvas.translate((float)(-this.getWidth() + this.getPaddingRight()), (float)(-this.getHeight() + this.getPaddingBottom()));
            }
            else {
                canvas.translate((float)(-this.getWidth()), (float)(-this.getHeight()));
            }
            final EdgeEffect mBottomGlow2 = this.mBottomGlow;
            b |= (mBottomGlow2 != null && mBottomGlow2.draw(canvas) && b2);
            canvas.restoreToCount(save4);
        }
        if (!b && this.mItemAnimator != null && this.mItemDecorations.size() > 0) {
            if (this.mItemAnimator.isRunning()) {
                b = true;
            }
        }
        if (b) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean drawChild(final Canvas canvas, final View view, final long n) {
        return super.drawChild(canvas, view, n);
    }
    
    void eatRequestLayout() {
        ++this.mEatRequestLayout;
        if (this.mEatRequestLayout == 1 && !this.mLayoutFrozen) {
            this.mLayoutRequestEaten = false;
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
        final StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(super.toString());
        sb.append(", adapter:");
        sb.append(this.mAdapter);
        sb.append(", layout:");
        sb.append(this.mLayout);
        sb.append(", context:");
        sb.append(this.getContext());
        return sb.toString();
    }
    
    final void fillRemainingScrollValues(final State state) {
        if (this.getScrollState() == 2) {
            final OverScroller access$400 = this.mViewFlinger.mScroller;
            state.mRemainingScrollHorizontal = access$400.getFinalX() - access$400.getCurrX();
            state.mRemainingScrollVertical = access$400.getFinalY() - access$400.getCurrY();
            return;
        }
        state.mRemainingScrollHorizontal = 0;
        state.mRemainingScrollVertical = 0;
    }
    
    public View findChildViewUnder(final float n, final float n2) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
            final View child = this.mChildHelper.getChildAt(i);
            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();
            if (n >= child.getLeft() + translationX) {
                if (n <= child.getRight() + translationX) {
                    if (n2 >= child.getTop() + translationY) {
                        if (n2 <= child.getBottom() + translationY) {
                            return child;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @Nullable
    public View findContainingItemView(final View view) {
        final ViewParent parent = view.getParent();
        View view2;
        ViewParent parent2;
        for (view2 = view, parent2 = parent; parent2 != null && parent2 != this && parent2 instanceof View; parent2 = view2.getParent()) {
            view2 = (View)parent2;
        }
        if (parent2 == this) {
            return view2;
        }
        return null;
    }
    
    @Nullable
    public ViewHolder findContainingViewHolder(View containingItemView) {
        containingItemView = this.findContainingItemView(containingItemView);
        if (containingItemView == null) {
            return null;
        }
        return this.getChildViewHolder(containingItemView);
    }
    
    public ViewHolder findViewHolderForAdapterPosition(final int n) {
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved()) {
                if (this.getAdapterPositionFor(childViewHolderInt) == n) {
                    if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                        return childViewHolderInt;
                    }
                    viewHolder = childViewHolderInt;
                }
            }
        }
        return viewHolder;
    }
    
    public ViewHolder findViewHolderForItemId(final long n) {
        final Adapter mAdapter = this.mAdapter;
        if (mAdapter != null && mAdapter.hasStableIds()) {
            final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
            ViewHolder viewHolder = null;
            for (int i = 0; i < unfilteredChildCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && childViewHolderInt.getItemId() == n) {
                    if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                        return childViewHolderInt;
                    }
                    viewHolder = childViewHolderInt;
                }
            }
            return viewHolder;
        }
        return null;
    }
    
    public ViewHolder findViewHolderForLayoutPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    @Deprecated
    public ViewHolder findViewHolderForPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    ViewHolder findViewHolderForPosition(final int n, final boolean b) {
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved()) {
                if (b) {
                    if (childViewHolderInt.mPosition != n) {
                        continue;
                    }
                }
                else if (childViewHolderInt.getLayoutPosition() != n) {
                    continue;
                }
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                viewHolder = childViewHolderInt;
            }
        }
        return viewHolder;
    }
    
    public boolean fling(int n, int max) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        int n2;
        if (canScrollHorizontally && Math.abs(n) >= this.mMinFlingVelocity) {
            n2 = n;
        }
        else {
            n2 = 0;
        }
        if (!canScrollVertically || Math.abs(max) < this.mMinFlingVelocity) {
            max = 0;
        }
        if (n2 == 0 && max == 0) {
            return false;
        }
        if (this.dispatchNestedPreFling((float)n2, (float)max)) {
            return false;
        }
        final boolean b = canScrollHorizontally || canScrollVertically;
        this.dispatchNestedFling((float)n2, (float)max, b);
        final OnFlingListener mOnFlingListener = this.mOnFlingListener;
        if (mOnFlingListener != null && mOnFlingListener.onFling(n2, max)) {
            return true;
        }
        if (b) {
            n = 0;
            if (canScrollHorizontally) {
                n = ((false | true) ? 1 : 0);
            }
            if (canScrollVertically) {
                n |= 0x2;
            }
            this.startNestedScroll(n, 1);
            n = this.mMaxFlingVelocity;
            n = Math.max(-n, Math.min(n2, n));
            final int mMaxFlingVelocity = this.mMaxFlingVelocity;
            max = Math.max(-mMaxFlingVelocity, Math.min(max, mMaxFlingVelocity));
            this.mViewFlinger.fling(n, max);
            return true;
        }
        return false;
    }
    
    public View focusSearch(final View view, int n) {
        final View onInterceptFocusSearch = this.mLayout.onInterceptFocusSearch(view, n);
        if (onInterceptFocusSearch != null) {
            return onInterceptFocusSearch;
        }
        final Adapter mAdapter = this.mAdapter;
        final boolean b = true;
        final boolean b2 = mAdapter != null && this.mLayout != null && (!this.isComputingLayout() && !this.mLayoutFrozen);
        final FocusFinder instance = FocusFinder.getInstance();
        View view2;
        if (b2 && (n == 2 || n == 1)) {
            int n2 = 0;
            if (this.mLayout.canScrollVertically()) {
                int n3;
                if (n == 2) {
                    n3 = 130;
                }
                else {
                    n3 = 33;
                }
                final boolean b3 = instance.findNextFocus((ViewGroup)this, view, n3) == null;
                if (RecyclerView.FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    n = n3;
                    n2 = (b3 ? 1 : 0);
                }
                else {
                    n2 = (b3 ? 1 : 0);
                }
            }
            if (n2 == 0 && this.mLayout.canScrollHorizontally()) {
                int n4;
                if (n == 2 ^ this.mLayout.getLayoutDirection() == 1) {
                    n4 = 66;
                }
                else {
                    n4 = 17;
                }
                final boolean b4 = instance.findNextFocus((ViewGroup)this, view, n4) == null && b;
                if (RecyclerView.FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    n = n4;
                    n2 = (b4 ? 1 : 0);
                }
                else {
                    n2 = (b4 ? 1 : 0);
                }
            }
            if (n2 != 0) {
                this.consumePendingUpdateOperations();
                if (this.findContainingItemView(view) == null) {
                    return null;
                }
                this.eatRequestLayout();
                this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
                this.resumeRequestLayout(false);
            }
            view2 = instance.findNextFocus((ViewGroup)this, view, n);
        }
        else {
            view2 = instance.findNextFocus((ViewGroup)this, view, n);
            if (view2 == null && b2) {
                this.consumePendingUpdateOperations();
                if (this.findContainingItemView(view) == null) {
                    return null;
                }
                this.eatRequestLayout();
                view2 = this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
                this.resumeRequestLayout(false);
            }
        }
        if (view2 != null && !view2.hasFocusable()) {
            if (this.getFocusedChild() == null) {
                return super.focusSearch(view, n);
            }
            this.requestChildOnScreen(view2, null);
            return view;
        }
        else {
            if (this.isPreferredNextFocus(view, view2, n)) {
                return view2;
            }
            return super.focusSearch(view, n);
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            return (ViewGroup$LayoutParams)mLayout.generateDefaultLayoutParams();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("RecyclerView has no LayoutManager");
        sb.append(this.exceptionLabel());
        throw new IllegalStateException(sb.toString());
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            return (ViewGroup$LayoutParams)mLayout.generateLayoutParams(this.getContext(), set);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("RecyclerView has no LayoutManager");
        sb.append(this.exceptionLabel());
        throw new IllegalStateException(sb.toString());
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            return (ViewGroup$LayoutParams)mLayout.generateLayoutParams(viewGroup$LayoutParams);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("RecyclerView has no LayoutManager");
        sb.append(this.exceptionLabel());
        throw new IllegalStateException(sb.toString());
    }
    
    public Adapter getAdapter() {
        return this.mAdapter;
    }
    
    int getAdapterPositionFor(final ViewHolder viewHolder) {
        if (!viewHolder.hasAnyOfTheFlags(524) && viewHolder.isBound()) {
            return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
        }
        return -1;
    }
    
    public int getBaseline() {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            return mLayout.getBaseline();
        }
        return super.getBaseline();
    }
    
    long getChangedHolderKey(final ViewHolder viewHolder) {
        if (this.mAdapter.hasStableIds()) {
            return viewHolder.getItemId();
        }
        return viewHolder.mPosition;
    }
    
    public int getChildAdapterPosition(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            return childViewHolderInt.getAdapterPosition();
        }
        return -1;
    }
    
    protected int getChildDrawingOrder(final int n, final int n2) {
        final ChildDrawingOrderCallback mChildDrawingOrderCallback = this.mChildDrawingOrderCallback;
        if (mChildDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(n, n2);
        }
        return mChildDrawingOrderCallback.onGetChildDrawingOrder(n, n2);
    }
    
    public long getChildItemId(final View view) {
        final Adapter mAdapter = this.mAdapter;
        long itemId = -1L;
        if (mAdapter == null) {
            return -1L;
        }
        if (!mAdapter.hasStableIds()) {
            return -1L;
        }
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            itemId = childViewHolderInt.getItemId();
        }
        return itemId;
    }
    
    public int getChildLayoutPosition(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            return childViewHolderInt.getLayoutPosition();
        }
        return -1;
    }
    
    @Deprecated
    public int getChildPosition(final View view) {
        return this.getChildAdapterPosition(view);
    }
    
    public ViewHolder getChildViewHolder(final View view) {
        final ViewParent parent = view.getParent();
        if (parent != null && parent != this) {
            final StringBuilder sb = new StringBuilder();
            sb.append("View ");
            sb.append(view);
            sb.append(" is not a direct child of ");
            sb.append(this);
            throw new IllegalArgumentException(sb.toString());
        }
        return getChildViewHolderInt(view);
    }
    
    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }
    
    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }
    
    public void getDecoratedBoundsWithMargins(final View view, final Rect rect) {
        getDecoratedBoundsWithMarginsInt(view, rect);
    }
    
    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }
    
    Rect getItemDecorInsetsForChild(final View view) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid())) {
            return layoutParams.mDecorInsets;
        }
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        mDecorInsets.set(0, 0, 0, 0);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
            mDecorInsets.left += this.mTempRect.left;
            mDecorInsets.top += this.mTempRect.top;
            mDecorInsets.right += this.mTempRect.right;
            mDecorInsets.bottom += this.mTempRect.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return mDecorInsets;
    }
    
    public ItemDecoration getItemDecorationAt(final int n) {
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
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
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
    
    public boolean hasNestedScrollingParent() {
        return this.getScrollingChildHelper().hasNestedScrollingParent();
    }
    
    public boolean hasNestedScrollingParent(final int n) {
        return this.getScrollingChildHelper().hasNestedScrollingParent(n);
    }
    
    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }
    
    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper((AdapterHelper.Callback)new AdapterHelper.Callback() {
            void dispatchUpdate(final UpdateOp updateOp) {
                final int cmd = updateOp.cmd;
                if (cmd == 4) {
                    RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                    return;
                }
                if (cmd == 8) {
                    RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, 1);
                    return;
                }
                switch (cmd) {
                    default: {}
                    case 2: {
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                    }
                    case 1: {
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                    }
                }
            }
            
            @Override
            public ViewHolder findViewHolder(final int n) {
                final ViewHolder viewHolderForPosition = RecyclerView.this.findViewHolderForPosition(n, true);
                if (viewHolderForPosition == null) {
                    return null;
                }
                if (RecyclerView.this.mChildHelper.isHidden(viewHolderForPosition.itemView)) {
                    return null;
                }
                return viewHolderForPosition;
            }
            
            @Override
            public void markViewHoldersUpdated(final int n, final int n2, final Object o) {
                RecyclerView.this.viewRangeUpdate(n, n2, o);
                RecyclerView.this.mItemsChanged = true;
            }
            
            @Override
            public void offsetPositionsForAdd(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForInsert(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForMove(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForMove(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForRemovingInvisible(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, true);
                final RecyclerView this$0 = RecyclerView.this;
                this$0.mItemsAddedOrRemoved = true;
                final State mState = this$0.mState;
                mState.mDeletedInvisibleItemCountSincePreviousLayout += n2;
            }
            
            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void onDispatchFirstPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
            
            @Override
            public void onDispatchSecondPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
        });
    }
    
    @VisibleForTesting
    void initFastScroller(final StateListDrawable stateListDrawable, final Drawable drawable, final StateListDrawable stateListDrawable2, final Drawable drawable2) {
        if (stateListDrawable != null && drawable != null && stateListDrawable2 != null && drawable2 != null) {
            final Resources resources = this.getContext().getResources();
            new FastScroller(this, stateListDrawable, drawable, stateListDrawable2, drawable2, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Trying to set fast scroller without both required drawables.");
        sb.append(this.exceptionLabel());
        throw new IllegalArgumentException(sb.toString());
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
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    boolean isAccessibilityEnabled() {
        final AccessibilityManager mAccessibilityManager = this.mAccessibilityManager;
        return mAccessibilityManager != null && mAccessibilityManager.isEnabled();
    }
    
    public boolean isAnimating() {
        final ItemAnimator mItemAnimator = this.mItemAnimator;
        return mItemAnimator != null && mItemAnimator.isRunning();
    }
    
    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }
    
    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }
    
    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.getScrollingChildHelper().isNestedScrollingEnabled();
    }
    
    void jumpToPositionForSmoothScroller(final int n) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            return;
        }
        mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }
    
    void markItemDecorInsetsDirty() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }
    
    void markKnownViewsInvalid() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(6);
            }
        }
        this.markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }
    
    public void offsetChildrenHorizontal(final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(n);
        }
    }
    
    public void offsetChildrenVertical(final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(n);
        }
    }
    
    void offsetPositionRecordsForInsert(final int n, final int n2) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= n) {
                childViewHolderInt.offsetPosition(n2, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForMove(final int n, final int n2) {
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        int n3;
        int n4;
        int n5;
        if (n < n2) {
            n3 = n;
            n4 = n2;
            n5 = -1;
        }
        else {
            n3 = n2;
            n4 = n;
            n5 = 1;
        }
        for (int i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && childViewHolderInt.mPosition >= n3) {
                if (childViewHolderInt.mPosition <= n4) {
                    if (childViewHolderInt.mPosition == n) {
                        childViewHolderInt.offsetPosition(n2 - n, false);
                    }
                    else {
                        childViewHolderInt.offsetPosition(n5, false);
                    }
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.mPosition >= n + n2) {
                    childViewHolderInt.offsetPosition(-n2, b);
                    this.mState.mStructureChanged = true;
                }
                else if (childViewHolderInt.mPosition >= n) {
                    childViewHolderInt.flagRemovedAndOffsetPosition(n - 1, -n2, b);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(n, n2, b);
        this.requestLayout();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        boolean mFirstLayoutComplete = true;
        this.mIsAttached = true;
        if (!this.mFirstLayoutComplete || this.isLayoutRequested()) {
            mFirstLayoutComplete = false;
        }
        this.mFirstLayoutComplete = mFirstLayoutComplete;
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker = GapWorker.sGapWorker.get();
            if (this.mGapWorker == null) {
                this.mGapWorker = new GapWorker();
                final Display display = ViewCompat.getDisplay((View)this);
                float n = 60.0f;
                if (!this.isInEditMode() && display != null) {
                    final float refreshRate = display.getRefreshRate();
                    if (refreshRate >= 30.0f) {
                        n = refreshRate;
                    }
                }
                this.mGapWorker.mFrameIntervalNs = (long)(1.0E9f / n);
                GapWorker.sGapWorker.set(this.mGapWorker);
            }
            this.mGapWorker.add(this);
        }
    }
    
    public void onChildAttachedToWindow(final View view) {
    }
    
    public void onChildDetachedFromWindow(final View view) {
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final ItemAnimator mItemAnimator = this.mItemAnimator;
        if (mItemAnimator != null) {
            mItemAnimator.endAnimations();
        }
        this.stopScroll();
        this.mIsAttached = false;
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        this.removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker.remove(this);
            this.mGapWorker = null;
        }
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDraw(canvas, this, this.mState);
        }
    }
    
    void onEnterLayoutOrScroll() {
        ++this.mLayoutOrScrollCounter;
    }
    
    void onExitLayoutOrScroll() {
        this.onExitLayoutOrScroll(true);
    }
    
    void onExitLayoutOrScroll(final boolean b) {
        --this.mLayoutOrScrollCounter;
        if (this.mLayoutOrScrollCounter >= 1) {
            return;
        }
        this.mLayoutOrScrollCounter = 0;
        if (b) {
            this.dispatchContentChangedIfNecessary();
            this.dispatchPendingImportantForAccessibilityChanges();
        }
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if (this.mLayout == null) {
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        if (motionEvent.getAction() != 8) {
            return false;
        }
        float n;
        float n2;
        if ((motionEvent.getSource() & 0x2) != 0x0) {
            if (this.mLayout.canScrollVertically()) {
                n = -motionEvent.getAxisValue(9);
            }
            else {
                n = 0.0f;
            }
            if (this.mLayout.canScrollHorizontally()) {
                n2 = motionEvent.getAxisValue(10);
            }
            else {
                n2 = 0.0f;
            }
        }
        else if ((motionEvent.getSource() & 0x400000) != 0x0) {
            n2 = motionEvent.getAxisValue(26);
            if (this.mLayout.canScrollVertically()) {
                n = -n2;
                n2 = 0.0f;
            }
            else if (this.mLayout.canScrollHorizontally()) {
                n = 0.0f;
            }
            else {
                n2 = 0.0f;
                n = 0.0f;
            }
        }
        else {
            n = 0.0f;
            n2 = 0.0f;
        }
        if (n == 0.0f && n2 == 0.0f) {
            return false;
        }
        this.scrollByInternal((int)(this.mScaledHorizontalScrollFactor * n2), (int)(this.mScaledVerticalScrollFactor * n), motionEvent);
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final boolean mLayoutFrozen = this.mLayoutFrozen;
        boolean b = false;
        if (mLayoutFrozen) {
            return false;
        }
        if (this.dispatchOnItemTouchIntercept(motionEvent)) {
            this.cancelTouch();
            return true;
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            return false;
        }
        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        switch (actionMasked) {
            case 6: {
                this.onPointerUp(motionEvent);
                break;
            }
            case 5: {
                this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
                final int n = (int)(motionEvent.getX(actionIndex) + 0.5f);
                this.mLastTouchX = n;
                this.mInitialTouchX = n;
                final int n2 = (int)(motionEvent.getY(actionIndex) + 0.5f);
                this.mLastTouchY = n2;
                this.mInitialTouchY = n2;
                break;
            }
            case 3: {
                this.cancelTouch();
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (pointerIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Error processing scroll; pointer index for id ");
                    sb.append(this.mScrollPointerId);
                    sb.append(" not found. Did any MotionEvents get skipped?");
                    Log.e("RecyclerView", sb.toString());
                    return false;
                }
                final int mLastTouchX = (int)(motionEvent.getX(pointerIndex) + 0.5f);
                final int mLastTouchY = (int)(motionEvent.getY(pointerIndex) + 0.5f);
                if (this.mScrollState != 1) {
                    final int mInitialTouchX = this.mInitialTouchX;
                    final int mInitialTouchY = this.mInitialTouchY;
                    boolean b2 = false;
                    if (canScrollHorizontally && Math.abs(mLastTouchX - mInitialTouchX) > this.mTouchSlop) {
                        this.mLastTouchX = mLastTouchX;
                        b2 = true;
                    }
                    if (canScrollVertically && Math.abs(mLastTouchY - mInitialTouchY) > this.mTouchSlop) {
                        this.mLastTouchY = mLastTouchY;
                        b2 = true;
                    }
                    if (b2) {
                        this.setScrollState(1);
                    }
                }
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
                this.mScrollPointerId = motionEvent.getPointerId(0);
                final int n3 = (int)(motionEvent.getX() + 0.5f);
                this.mLastTouchX = n3;
                this.mInitialTouchX = n3;
                final int n4 = (int)(motionEvent.getY() + 0.5f);
                this.mLastTouchY = n4;
                this.mInitialTouchY = n4;
                if (this.mScrollState == 2) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.setScrollState(1);
                }
                final int[] mNestedOffsets = this.mNestedOffsets;
                mNestedOffsets[mNestedOffsets[1] = 0] = 0;
                int n5 = 0;
                if (canScrollHorizontally) {
                    n5 = ((false | true) ? 1 : 0);
                }
                if (canScrollVertically) {
                    n5 |= 0x2;
                }
                this.startNestedScroll(n5, 0);
                break;
            }
        }
        if (this.mScrollState == 1) {
            b = true;
        }
        return b;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        TraceCompat.beginSection("RV OnLayout");
        this.dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }
    
    protected void onMeasure(final int n, final int n2) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            this.defaultOnMeasure(n, n2);
            return;
        }
        final boolean mAutoMeasure = mLayout.mAutoMeasure;
        final boolean b = false;
        if (mAutoMeasure) {
            final int mode = View$MeasureSpec.getMode(n);
            final int mode2 = View$MeasureSpec.getMode(n2);
            boolean b2 = b;
            if (mode == 1073741824) {
                b2 = b;
                if (mode2 == 1073741824) {
                    b2 = true;
                }
            }
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            if (b2) {
                return;
            }
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
                this.mLayout.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
                this.mState.mIsMeasuring = true;
                this.dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(n, n2);
            }
        }
        else {
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
                }
                else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.resumeRequestLayout(this.mAdapterUpdateDuringMeasure = false);
            }
            else if (this.mState.mRunPredictiveAnimations) {
                this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight());
                return;
            }
            final Adapter mAdapter = this.mAdapter;
            if (mAdapter != null) {
                this.mState.mItemCount = mAdapter.getItemCount();
            }
            else {
                this.mState.mItemCount = 0;
            }
            this.eatRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            this.resumeRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        return !this.isComputingLayout() && super.onRequestFocusInDescendants(n, rect);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        this.mPendingSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null) {
            savedState.copyFrom(mPendingSavedState);
            return (Parcelable)savedState;
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            savedState.mLayoutState = mLayout.onSaveInstanceState();
            return (Parcelable)savedState;
        }
        savedState.mLayoutState = null;
        return (Parcelable)savedState;
    }
    
    public void onScrollStateChanged(final int n) {
    }
    
    public void onScrolled(final int n, final int n2) {
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n == n3 && n2 == n4) {
            return;
        }
        this.invalidateGlows();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean mLayoutFrozen = this.mLayoutFrozen;
        int n = 0;
        if (mLayoutFrozen) {
            return false;
        }
        if (this.mIgnoreMotionEventTillDown) {
            return false;
        }
        if (this.dispatchOnItemTouch(motionEvent)) {
            this.cancelTouch();
            return true;
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            return false;
        }
        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        final boolean b = false;
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            final int[] mNestedOffsets = this.mNestedOffsets;
            mNestedOffsets[mNestedOffsets[1] = 0] = 0;
        }
        final int[] mNestedOffsets2 = this.mNestedOffsets;
        obtain.offsetLocation((float)mNestedOffsets2[0], (float)mNestedOffsets2[1]);
        boolean b2 = false;
        switch (actionMasked) {
            default: {
                b2 = b;
                break;
            }
            case 6: {
                this.onPointerUp(motionEvent);
                b2 = b;
                break;
            }
            case 5: {
                this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
                final int n2 = (int)(motionEvent.getX(actionIndex) + 0.5f);
                this.mLastTouchX = n2;
                this.mInitialTouchX = n2;
                final int n3 = (int)(motionEvent.getY(actionIndex) + 0.5f);
                this.mLastTouchY = n3;
                this.mInitialTouchY = n3;
                b2 = b;
                break;
            }
            case 3: {
                this.cancelTouch();
                b2 = b;
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (pointerIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Error processing scroll; pointer index for id ");
                    sb.append(this.mScrollPointerId);
                    sb.append(" not found. Did any MotionEvents get skipped?");
                    Log.e("RecyclerView", sb.toString());
                    return false;
                }
                final int n4 = (int)(motionEvent.getX(pointerIndex) + 0.5f);
                final int n5 = (int)(motionEvent.getY(pointerIndex) + 0.5f);
                int n6 = this.mLastTouchX - n4;
                int n7 = this.mLastTouchY - n5;
                if (this.dispatchNestedPreScroll(n6, n7, this.mScrollConsumed, this.mScrollOffset, 0)) {
                    final int[] mScrollConsumed = this.mScrollConsumed;
                    n6 -= mScrollConsumed[0];
                    n7 -= mScrollConsumed[1];
                    final int[] mScrollOffset = this.mScrollOffset;
                    obtain.offsetLocation((float)mScrollOffset[0], (float)mScrollOffset[1]);
                    final int[] mNestedOffsets3 = this.mNestedOffsets;
                    final int n8 = mNestedOffsets3[0];
                    final int[] mScrollOffset2 = this.mScrollOffset;
                    mNestedOffsets3[0] = n8 + mScrollOffset2[0];
                    mNestedOffsets3[1] += mScrollOffset2[1];
                }
                int n10;
                int n11;
                if (this.mScrollState != 1) {
                    boolean b3 = false;
                    if (canScrollHorizontally) {
                        final int abs = Math.abs(n6);
                        final int mTouchSlop = this.mTouchSlop;
                        if (abs > mTouchSlop) {
                            if (n6 > 0) {
                                n6 -= mTouchSlop;
                            }
                            else {
                                n6 += mTouchSlop;
                            }
                            b3 = true;
                        }
                    }
                    if (canScrollVertically) {
                        final int abs2 = Math.abs(n7);
                        final int mTouchSlop2 = this.mTouchSlop;
                        if (abs2 > mTouchSlop2) {
                            if (n7 > 0) {
                                n7 -= mTouchSlop2;
                            }
                            else {
                                n7 += mTouchSlop2;
                            }
                            b3 = true;
                        }
                    }
                    if (b3) {
                        this.setScrollState(1);
                    }
                    final int n9 = n7;
                    n10 = n6;
                    n11 = n9;
                }
                else {
                    final int n12 = n6;
                    n11 = n7;
                    n10 = n12;
                }
                if (this.mScrollState == 1) {
                    final int[] mScrollOffset3 = this.mScrollOffset;
                    this.mLastTouchX = n4 - mScrollOffset3[0];
                    this.mLastTouchY = n5 - mScrollOffset3[1];
                    int n13;
                    if (canScrollHorizontally) {
                        n13 = n10;
                    }
                    else {
                        n13 = 0;
                    }
                    if (canScrollVertically) {
                        n = n11;
                    }
                    if (this.scrollByInternal(n13, n, obtain)) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (this.mGapWorker != null && (n10 != 0 || n11 != 0)) {
                        this.mGapWorker.postFromTraversal(this, n10, n11);
                    }
                }
                b2 = b;
                break;
            }
            case 1: {
                this.mVelocityTracker.addMovement(obtain);
                b2 = true;
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                float n14;
                if (canScrollHorizontally) {
                    n14 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
                }
                else {
                    n14 = 0.0f;
                }
                float n15;
                if (canScrollVertically) {
                    n15 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
                }
                else {
                    n15 = 0.0f;
                }
                if ((n14 == 0.0f && n15 == 0.0f) || !this.fling((int)n14, (int)n15)) {
                    this.setScrollState(0);
                }
                this.resetTouch();
                break;
            }
            case 0: {
                this.mScrollPointerId = motionEvent.getPointerId(0);
                final int n16 = (int)(motionEvent.getX() + 0.5f);
                this.mLastTouchX = n16;
                this.mInitialTouchX = n16;
                final int n17 = (int)(motionEvent.getY() + 0.5f);
                this.mLastTouchY = n17;
                this.mInitialTouchY = n17;
                int n18 = 0;
                if (canScrollHorizontally) {
                    n18 = ((false | true) ? 1 : 0);
                }
                if (canScrollVertically) {
                    n18 |= 0x2;
                }
                this.startNestedScroll(n18, 0);
                b2 = b;
                break;
            }
        }
        if (!b2) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }
    
    void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }
    
    void recordAnimationInfoIfBouncedHiddenView(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated()) {
            if (!viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
                this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(viewHolder), viewHolder);
            }
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }
    
    void removeAndRecycleViews() {
        final ItemAnimator mItemAnimator = this.mItemAnimator;
        if (mItemAnimator != null) {
            mItemAnimator.endAnimations();
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        this.mRecycler.clear();
    }
    
    boolean removeAnimatingView(final View view) {
        this.eatRequestLayout();
        final boolean removeViewIfHidden = this.mChildHelper.removeViewIfHidden(view);
        if (removeViewIfHidden) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(childViewHolderInt);
            this.mRecycler.recycleViewHolderInternal(childViewHolderInt);
        }
        this.resumeRequestLayout(removeViewIfHidden ^ true);
        return removeViewIfHidden;
    }
    
    protected void removeDetachedView(final View view, final boolean b) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached()) {
                childViewHolderInt.clearTmpDetachFlag();
            }
            else if (!childViewHolderInt.shouldIgnore()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
                sb.append(childViewHolderInt);
                sb.append(this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        view.clearAnimation();
        this.dispatchChildDetached(view);
        super.removeDetachedView(view, b);
    }
    
    public void removeItemDecoration(final ItemDecoration itemDecoration) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(itemDecoration);
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(this.getOverScrollMode() == 2);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void removeOnChildAttachStateChangeListener(final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        final List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners = this.mOnChildAttachStateListeners;
        if (mOnChildAttachStateListeners == null) {
            return;
        }
        mOnChildAttachStateListeners.remove(onChildAttachStateChangeListener);
    }
    
    public void removeOnItemTouchListener(final OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.remove(onItemTouchListener);
        if (this.mActiveOnItemTouchListener == onItemTouchListener) {
            this.mActiveOnItemTouchListener = null;
        }
    }
    
    public void removeOnScrollListener(final OnScrollListener onScrollListener) {
        final List<OnScrollListener> mScrollListeners = this.mScrollListeners;
        if (mScrollListeners != null) {
            mScrollListeners.remove(onScrollListener);
        }
    }
    
    void repositionShadowingViews() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.mChildHelper.getChildAt(i);
            final ViewHolder childViewHolder = this.getChildViewHolder(child);
            if (childViewHolder != null && childViewHolder.mShadowingHolder != null) {
                final View itemView = childViewHolder.mShadowingHolder.itemView;
                final int left = child.getLeft();
                final int top = child.getTop();
                if (left != itemView.getLeft() || top != itemView.getTop()) {
                    itemView.layout(left, top, itemView.getWidth() + left, itemView.getHeight() + top);
                }
            }
        }
    }
    
    public void requestChildFocus(final View view, final View view2) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, view, view2) && view2 != null) {
            this.requestChildOnScreen(view, view2);
        }
        super.requestChildFocus(view, view2);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(b);
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        if (this.mEatRequestLayout == 0 && !this.mLayoutFrozen) {
            super.requestLayout();
            return;
        }
        this.mLayoutRequestEaten = true;
    }
    
    void resumeRequestLayout(final boolean b) {
        if (this.mEatRequestLayout < 1) {
            this.mEatRequestLayout = 1;
        }
        if (!b) {
            this.mLayoutRequestEaten = false;
        }
        if (this.mEatRequestLayout == 1) {
            if (b && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                this.dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
        --this.mEatRequestLayout;
    }
    
    void saveOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.saveOldPosition();
            }
        }
    }
    
    public void scrollBy(int n, final int n2) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (!canScrollHorizontally && !canScrollVertically) {
            return;
        }
        int n3 = 0;
        if (!canScrollHorizontally) {
            n = 0;
        }
        if (canScrollVertically) {
            n3 = n2;
        }
        this.scrollByInternal(n, n3, null);
    }
    
    boolean scrollByInternal(int mLastTouchX, final int n, final MotionEvent motionEvent) {
        int n2 = 0;
        int n3 = 0;
        int scrollHorizontallyBy = 0;
        int scrollVerticallyBy = 0;
        this.consumePendingUpdateOperations();
        int n4;
        int n5;
        if (this.mAdapter != null) {
            this.eatRequestLayout();
            this.onEnterLayoutOrScroll();
            TraceCompat.beginSection("RV Scroll");
            this.fillRemainingScrollValues(this.mState);
            if (mLastTouchX != 0) {
                scrollHorizontallyBy = this.mLayout.scrollHorizontallyBy(mLastTouchX, this.mRecycler, this.mState);
                n2 = mLastTouchX - scrollHorizontallyBy;
            }
            if (n != 0) {
                scrollVerticallyBy = this.mLayout.scrollVerticallyBy(n, this.mRecycler, this.mState);
                n3 = n - scrollVerticallyBy;
            }
            TraceCompat.endSection();
            this.repositionShadowingViews();
            this.onExitLayoutOrScroll();
            this.resumeRequestLayout(false);
            n4 = n2;
            n5 = scrollVerticallyBy;
        }
        else {
            n4 = 0;
            n3 = 0;
            scrollHorizontallyBy = 0;
            n5 = 0;
        }
        if (!this.mItemDecorations.isEmpty()) {
            this.invalidate();
        }
        final boolean dispatchNestedScroll = this.dispatchNestedScroll(scrollHorizontallyBy, n5, n4, n3, this.mScrollOffset, 0);
        boolean b = true;
        if (dispatchNestedScroll) {
            mLastTouchX = this.mLastTouchX;
            final int[] mScrollOffset = this.mScrollOffset;
            this.mLastTouchX = mLastTouchX - mScrollOffset[0];
            this.mLastTouchY -= mScrollOffset[1];
            if (motionEvent != null) {
                motionEvent.offsetLocation((float)mScrollOffset[0], (float)mScrollOffset[1]);
            }
            final int[] mNestedOffsets = this.mNestedOffsets;
            mLastTouchX = mNestedOffsets[0];
            final int[] mScrollOffset2 = this.mScrollOffset;
            mNestedOffsets[0] = mLastTouchX + mScrollOffset2[0];
            mNestedOffsets[1] += mScrollOffset2[1];
        }
        else if (this.getOverScrollMode() != 2) {
            if (motionEvent != null && !MotionEventCompat.isFromSource(motionEvent, 8194)) {
                this.pullGlows(motionEvent.getX(), (float)n4, motionEvent.getY(), (float)n3);
            }
            this.considerReleasingGlowsOnScroll(mLastTouchX, n);
        }
        if (scrollHorizontallyBy != 0 || n5 != 0) {
            this.dispatchOnScrolled(scrollHorizontallyBy, n5);
        }
        if (!this.awakenScrollBars()) {
            this.invalidate();
        }
        if (scrollHorizontallyBy == 0) {
            if (n5 != 0) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    public void scrollTo(final int n, final int n2) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }
    
    public void scrollToPosition(final int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        this.stopScroll();
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }
    
    public void sendAccessibilityEventUnchecked(final AccessibilityEvent accessibilityEvent) {
        if (this.shouldDeferAccessibilityEvent(accessibilityEvent)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(accessibilityEvent);
    }
    
    public void setAccessibilityDelegateCompat(final RecyclerViewAccessibilityDelegate mAccessibilityDelegate) {
        ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate = mAccessibilityDelegate);
    }
    
    public void setAdapter(final Adapter adapter) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, false, true);
        this.requestLayout();
    }
    
    public void setChildDrawingOrderCallback(final ChildDrawingOrderCallback mChildDrawingOrderCallback) {
        if (mChildDrawingOrderCallback == this.mChildDrawingOrderCallback) {
            return;
        }
        this.mChildDrawingOrderCallback = mChildDrawingOrderCallback;
        this.setChildrenDrawingOrderEnabled(this.mChildDrawingOrderCallback != null);
    }
    
    @VisibleForTesting
    boolean setChildImportantForAccessibilityInternal(final ViewHolder viewHolder, final int mPendingAccessibilityState) {
        if (this.isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = mPendingAccessibilityState;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, mPendingAccessibilityState);
        return true;
    }
    
    public void setClipToPadding(final boolean mClipToPadding) {
        if (mClipToPadding != this.mClipToPadding) {
            this.invalidateGlows();
        }
        super.setClipToPadding(this.mClipToPadding = mClipToPadding);
        if (this.mFirstLayoutComplete) {
            this.requestLayout();
        }
    }
    
    void setDataSetChangedAfterLayout() {
        this.mDataSetHasChangedAfterLayout = true;
        this.markKnownViewsInvalid();
    }
    
    public void setHasFixedSize(final boolean mHasFixedSize) {
        this.mHasFixedSize = mHasFixedSize;
    }
    
    public void setItemAnimator(ItemAnimator mItemAnimator) {
        final ItemAnimator mItemAnimator2 = this.mItemAnimator;
        if (mItemAnimator2 != null) {
            mItemAnimator2.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = mItemAnimator;
        mItemAnimator = this.mItemAnimator;
        if (mItemAnimator != null) {
            mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }
    
    public void setItemViewCacheSize(final int viewCacheSize) {
        this.mRecycler.setViewCacheSize(viewCacheSize);
    }
    
    public void setLayoutFrozen(final boolean b) {
        if (b == this.mLayoutFrozen) {
            return;
        }
        this.assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
        if (!b) {
            this.mLayoutFrozen = false;
            if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                this.requestLayout();
            }
            this.mLayoutRequestEaten = false;
            return;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        this.onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
        this.mLayoutFrozen = true;
        this.mIgnoreMotionEventTillDown = true;
        this.stopScroll();
    }
    
    public void setLayoutManager(final LayoutManager mLayout) {
        if (mLayout == this.mLayout) {
            return;
        }
        this.stopScroll();
        if (this.mLayout != null) {
            final ItemAnimator mItemAnimator = this.mItemAnimator;
            if (mItemAnimator != null) {
                mItemAnimator.endAnimations();
            }
            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            this.mRecycler.clear();
            if (this.mIsAttached) {
                this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }
            this.mLayout.setRecyclerView(null);
            this.mLayout = null;
        }
        else {
            this.mRecycler.clear();
        }
        this.mChildHelper.removeAllViewsUnfiltered();
        this.mLayout = mLayout;
        if (mLayout != null) {
            if (mLayout.mRecyclerView != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("LayoutManager ");
                sb.append(mLayout);
                sb.append(" is already attached to a RecyclerView:");
                sb.append(mLayout.mRecyclerView.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            this.mLayout.setRecyclerView(this);
            if (this.mIsAttached) {
                this.mLayout.dispatchAttachedToWindow(this);
            }
        }
        this.mRecycler.updateViewCacheSize();
        this.requestLayout();
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.getScrollingChildHelper().setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnFlingListener(@Nullable final OnFlingListener mOnFlingListener) {
        this.mOnFlingListener = mOnFlingListener;
    }
    
    @Deprecated
    public void setOnScrollListener(final OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }
    
    public void setPreserveFocusAfterLayout(final boolean mPreserveFocusAfterLayout) {
        this.mPreserveFocusAfterLayout = mPreserveFocusAfterLayout;
    }
    
    public void setRecycledViewPool(final RecycledViewPool recycledViewPool) {
        this.mRecycler.setRecycledViewPool(recycledViewPool);
    }
    
    public void setRecyclerListener(final RecyclerListener mRecyclerListener) {
        this.mRecyclerListener = mRecyclerListener;
    }
    
    void setScrollState(final int mScrollState) {
        if (mScrollState == this.mScrollState) {
            return;
        }
        if ((this.mScrollState = mScrollState) != 2) {
            this.stopScrollersInternal();
        }
        this.dispatchOnScrollStateChanged(mScrollState);
    }
    
    public void setScrollingTouchSlop(final int n) {
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        switch (n) {
            case 1: {
                this.mTouchSlop = value.getScaledPagingTouchSlop();
            }
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("setScrollingTouchSlop(): bad argument constant ");
                sb.append(n);
                sb.append("; using default value");
                Log.w("RecyclerView", sb.toString());
            }
            case 0: {
                this.mTouchSlop = value.getScaledTouchSlop();
            }
        }
    }
    
    public void setViewCacheExtension(final ViewCacheExtension viewCacheExtension) {
        this.mRecycler.setViewCacheExtension(viewCacheExtension);
    }
    
    boolean shouldDeferAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (this.isComputingLayout()) {
            int contentChangeTypes = 0;
            if (accessibilityEvent != null) {
                contentChangeTypes = AccessibilityEventCompat.getContentChangeTypes(accessibilityEvent);
            }
            if (contentChangeTypes == 0) {
                contentChangeTypes = 0;
            }
            this.mEatenAccessibilityChangeFlags |= contentChangeTypes;
            return true;
        }
        return false;
    }
    
    public void smoothScrollBy(final int n, final int n2) {
        this.smoothScrollBy(n, n2, null);
    }
    
    public void smoothScrollBy(int n, int n2, final Interpolator interpolator) {
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        if (!mLayout.canScrollHorizontally()) {
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
    
    public void smoothScrollToPosition(final int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        final LayoutManager mLayout = this.mLayout;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        mLayout.smoothScrollToPosition(this, this.mState, n);
    }
    
    public boolean startNestedScroll(final int n) {
        return this.getScrollingChildHelper().startNestedScroll(n);
    }
    
    public boolean startNestedScroll(final int n, final int n2) {
        return this.getScrollingChildHelper().startNestedScroll(n, n2);
    }
    
    public void stopNestedScroll() {
        this.getScrollingChildHelper().stopNestedScroll();
    }
    
    public void stopNestedScroll(final int n) {
        this.getScrollingChildHelper().stopNestedScroll(n);
    }
    
    public void stopScroll() {
        this.setScrollState(0);
        this.stopScrollersInternal();
    }
    
    public void swapAdapter(final Adapter adapter, final boolean b) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, true, b);
        this.requestLayout();
    }
    
    void viewRangeUpdate(final int n, final int n2, final Object o) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final View unfilteredChild = this.mChildHelper.getUnfilteredChildAt(i);
            final ViewHolder childViewHolderInt = getChildViewHolderInt(unfilteredChild);
            if (childViewHolderInt != null) {
                if (!childViewHolderInt.shouldIgnore()) {
                    if (childViewHolderInt.mPosition >= n && childViewHolderInt.mPosition < n + n2) {
                        childViewHolderInt.addFlags(2);
                        childViewHolderInt.addChangePayload(o);
                        ((LayoutParams)unfilteredChild.getLayoutParams()).mInsetsDirty = true;
                    }
                }
            }
        }
        this.mRecycler.viewRangeUpdate(n, n2);
    }
    
    public abstract static class Adapter<VH extends ViewHolder>
    {
        private boolean mHasStableIds;
        private final AdapterDataObservable mObservable;
        
        public Adapter() {
            this.mObservable = new AdapterDataObservable();
            this.mHasStableIds = false;
        }
        
        public final void bindViewHolder(final VH vh, final int mPosition) {
            vh.mPosition = mPosition;
            if (this.hasStableIds()) {
                vh.mItemId = this.getItemId(mPosition);
            }
            ((ViewHolder)vh).setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            this.onBindViewHolder(vh, mPosition, ((ViewHolder)vh).getUnmodifiedPayloads());
            ((ViewHolder)vh).clearPayload();
            final ViewGroup$LayoutParams layoutParams = vh.itemView.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams)layoutParams).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }
        
        public final VH createViewHolder(final ViewGroup viewGroup, final int mItemViewType) {
            TraceCompat.beginSection("RV CreateView");
            final ViewHolder onCreateViewHolder = this.onCreateViewHolder(viewGroup, mItemViewType);
            onCreateViewHolder.mItemViewType = mItemViewType;
            TraceCompat.endSection();
            return (VH)onCreateViewHolder;
        }
        
        public abstract int getItemCount();
        
        public long getItemId(final int n) {
            return -1L;
        }
        
        public int getItemViewType(final int n) {
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
        
        public final void notifyItemChanged(final int n) {
            this.mObservable.notifyItemRangeChanged(n, 1);
        }
        
        public final void notifyItemChanged(final int n, final Object o) {
            this.mObservable.notifyItemRangeChanged(n, 1, o);
        }
        
        public final void notifyItemInserted(final int n) {
            this.mObservable.notifyItemRangeInserted(n, 1);
        }
        
        public final void notifyItemMoved(final int n, final int n2) {
            this.mObservable.notifyItemMoved(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2) {
            this.mObservable.notifyItemRangeChanged(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2, final Object o) {
            this.mObservable.notifyItemRangeChanged(n, n2, o);
        }
        
        public final void notifyItemRangeInserted(final int n, final int n2) {
            this.mObservable.notifyItemRangeInserted(n, n2);
        }
        
        public final void notifyItemRangeRemoved(final int n, final int n2) {
            this.mObservable.notifyItemRangeRemoved(n, n2);
        }
        
        public final void notifyItemRemoved(final int n) {
            this.mObservable.notifyItemRangeRemoved(n, 1);
        }
        
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        }
        
        public abstract void onBindViewHolder(final VH p0, final int p1);
        
        public void onBindViewHolder(final VH vh, final int n, final List<Object> list) {
            this.onBindViewHolder(vh, n);
        }
        
        public abstract VH onCreateViewHolder(final ViewGroup p0, final int p1);
        
        public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        }
        
        public boolean onFailedToRecycleView(final VH vh) {
            return false;
        }
        
        public void onViewAttachedToWindow(final VH vh) {
        }
        
        public void onViewDetachedFromWindow(final VH vh) {
        }
        
        public void onViewRecycled(final VH vh) {
        }
        
        public void registerAdapterDataObserver(final AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver((Object)adapterDataObserver);
        }
        
        public void setHasStableIds(final boolean mHasStableIds) {
            if (!this.hasObservers()) {
                this.mHasStableIds = mHasStableIds;
                return;
            }
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        }
        
        public void unregisterAdapterDataObserver(final AdapterDataObserver adapterDataObserver) {
            this.mObservable.unregisterObserver((Object)adapterDataObserver);
        }
    }
    
    static class AdapterDataObservable extends Observable<AdapterDataObserver>
    {
        public boolean hasObservers() {
            return this.mObservers.isEmpty() ^ true;
        }
        
        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onChanged();
            }
        }
        
        public void notifyItemMoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(n, n2, 1);
            }
        }
        
        public void notifyItemRangeChanged(final int n, final int n2) {
            this.notifyItemRangeChanged(n, n2, null);
        }
        
        public void notifyItemRangeChanged(final int n, final int n2, final Object o) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(n, n2, o);
            }
        }
        
        public void notifyItemRangeInserted(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(n, n2);
            }
        }
        
        public void notifyItemRangeRemoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(n, n2);
            }
        }
    }
    
    public abstract static class AdapterDataObserver
    {
        public void onChanged() {
        }
        
        public void onItemRangeChanged(final int n, final int n2) {
        }
        
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            this.onItemRangeChanged(n, n2);
        }
        
        public void onItemRangeInserted(final int n, final int n2) {
        }
        
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
        }
        
        public void onItemRangeRemoved(final int n, final int n2) {
        }
    }
    
    public interface ChildDrawingOrderCallback
    {
        int onGetChildDrawingOrder(final int p0, final int p1);
    }
    
    public abstract static class ItemAnimator
    {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration;
        private long mChangeDuration;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners;
        private ItemAnimatorListener mListener;
        private long mMoveDuration;
        private long mRemoveDuration;
        
        public ItemAnimator() {
            this.mListener = null;
            this.mFinishedListeners = new ArrayList<ItemAnimatorFinishedListener>();
            this.mAddDuration = 120L;
            this.mRemoveDuration = 120L;
            this.mMoveDuration = 250L;
            this.mChangeDuration = 250L;
        }
        
        static int buildAdapterChangeFlagsForAnimations(final ViewHolder viewHolder) {
            final int n = viewHolder.mFlags & 0xE;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((n & 0x4) != 0x0) {
                return n;
            }
            final int oldPosition = viewHolder.getOldPosition();
            final int adapterPosition = viewHolder.getAdapterPosition();
            if (oldPosition != -1 && adapterPosition != -1 && oldPosition != adapterPosition) {
                return n | 0x800;
            }
            return n;
        }
        
        public abstract boolean animateAppearance(@NonNull final ViewHolder p0, @Nullable final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public abstract boolean animateChange(@NonNull final ViewHolder p0, @NonNull final ViewHolder p1, @NonNull final ItemHolderInfo p2, @NonNull final ItemHolderInfo p3);
        
        public abstract boolean animateDisappearance(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @Nullable final ItemHolderInfo p2);
        
        public abstract boolean animatePersistence(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder) {
            return true;
        }
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
            return this.canReuseUpdatedViewHolder(viewHolder);
        }
        
        public final void dispatchAnimationFinished(final ViewHolder viewHolder) {
            this.onAnimationFinished(viewHolder);
            final ItemAnimatorListener mListener = this.mListener;
            if (mListener != null) {
                mListener.onAnimationFinished(viewHolder);
            }
        }
        
        public final void dispatchAnimationStarted(final ViewHolder viewHolder) {
            this.onAnimationStarted(viewHolder);
        }
        
        public final void dispatchAnimationsFinished() {
            for (int size = this.mFinishedListeners.size(), i = 0; i < size; ++i) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }
        
        public abstract void endAnimation(final ViewHolder p0);
        
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
        
        public final boolean isRunning(final ItemAnimatorFinishedListener itemAnimatorFinishedListener) {
            final boolean running = this.isRunning();
            if (itemAnimatorFinishedListener == null) {
                return running;
            }
            if (!running) {
                itemAnimatorFinishedListener.onAnimationsFinished();
                return running;
            }
            this.mFinishedListeners.add(itemAnimatorFinishedListener);
            return running;
        }
        
        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }
        
        public void onAnimationFinished(final ViewHolder viewHolder) {
        }
        
        public void onAnimationStarted(final ViewHolder viewHolder) {
        }
        
        @NonNull
        public ItemHolderInfo recordPostLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        @NonNull
        public ItemHolderInfo recordPreLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from, final int n, @NonNull final List<Object> list) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        public abstract void runPendingAnimations();
        
        public void setAddDuration(final long mAddDuration) {
            this.mAddDuration = mAddDuration;
        }
        
        public void setChangeDuration(final long mChangeDuration) {
            this.mChangeDuration = mChangeDuration;
        }
        
        void setListener(final ItemAnimatorListener mListener) {
            this.mListener = mListener;
        }
        
        public void setMoveDuration(final long mMoveDuration) {
            this.mMoveDuration = mMoveDuration;
        }
        
        public void setRemoveDuration(final long mRemoveDuration) {
            this.mRemoveDuration = mRemoveDuration;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }
        
        public interface ItemAnimatorFinishedListener
        {
            void onAnimationsFinished();
        }
        
        interface ItemAnimatorListener
        {
            void onAnimationFinished(final ViewHolder p0);
        }
        
        public static class ItemHolderInfo
        {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;
            
            public ItemHolderInfo setFrom(final ViewHolder viewHolder) {
                return this.setFrom(viewHolder, 0);
            }
            
            public ItemHolderInfo setFrom(final ViewHolder viewHolder, final int n) {
                final View itemView = viewHolder.itemView;
                this.left = itemView.getLeft();
                this.top = itemView.getTop();
                this.right = itemView.getRight();
                this.bottom = itemView.getBottom();
                return this;
            }
        }
    }
    
    private class ItemAnimatorRestoreListener implements ItemAnimatorListener
    {
        ItemAnimatorRestoreListener() {
        }
        
        @Override
        public void onAnimationFinished(final ViewHolder viewHolder) {
            viewHolder.setIsRecyclable(true);
            if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                viewHolder.mShadowedHolder = null;
            }
            viewHolder.mShadowingHolder = null;
            if (viewHolder.shouldBeKeptAsChild()) {
                return;
            }
            if (!RecyclerView.this.removeAnimatingView(viewHolder.itemView) && viewHolder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
            }
        }
    }
    
    public abstract static class ItemDecoration
    {
        @Deprecated
        public void getItemOffsets(final Rect rect, final int n, final RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }
        
        public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
            this.getItemOffsets(rect, ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition(), recyclerView);
        }
        
        @Deprecated
        public void onDraw(final Canvas canvas, final RecyclerView recyclerView) {
        }
        
        public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final State state) {
            this.onDraw(canvas, recyclerView);
        }
        
        @Deprecated
        public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView) {
        }
        
        public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final State state) {
            this.onDrawOver(canvas, recyclerView);
        }
    }
    
    public abstract static class LayoutManager
    {
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
            this.mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback() {
                @Override
                public View getChildAt(final int n) {
                    return LayoutManager.this.getChildAt(n);
                }
                
                @Override
                public int getChildCount() {
                    return LayoutManager.this.getChildCount();
                }
                
                @Override
                public int getChildEnd(final View view) {
                    return LayoutManager.this.getDecoratedRight(view) + ((LayoutParams)view.getLayoutParams()).rightMargin;
                }
                
                @Override
                public int getChildStart(final View view) {
                    return LayoutManager.this.getDecoratedLeft(view) - ((LayoutParams)view.getLayoutParams()).leftMargin;
                }
                
                @Override
                public View getParent() {
                    return (View)LayoutManager.this.mRecyclerView;
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
            this.mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback() {
                @Override
                public View getChildAt(final int n) {
                    return LayoutManager.this.getChildAt(n);
                }
                
                @Override
                public int getChildCount() {
                    return LayoutManager.this.getChildCount();
                }
                
                @Override
                public int getChildEnd(final View view) {
                    return LayoutManager.this.getDecoratedBottom(view) + ((LayoutParams)view.getLayoutParams()).bottomMargin;
                }
                
                @Override
                public int getChildStart(final View view) {
                    return LayoutManager.this.getDecoratedTop(view) - ((LayoutParams)view.getLayoutParams()).topMargin;
                }
                
                @Override
                public View getParent() {
                    return (View)LayoutManager.this.mRecyclerView;
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
        
        private void addViewInt(final View view, int childCount, final boolean b) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!b && !childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (!childViewHolderInt.wasReturnedFromScrap() && !childViewHolderInt.isScrap()) {
                if (view.getParent() == this.mRecyclerView) {
                    final int indexOfChild = this.mChildHelper.indexOfChild(view);
                    if (childCount == -1) {
                        childCount = this.mChildHelper.getChildCount();
                    }
                    if (indexOfChild == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                        sb.append(this.mRecyclerView.indexOfChild(view));
                        sb.append(this.mRecyclerView.exceptionLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                    if (indexOfChild != childCount) {
                        this.mRecyclerView.mLayout.moveView(indexOfChild, childCount);
                    }
                }
                else {
                    this.mChildHelper.addView(view, childCount, false);
                    layoutParams.mInsetsDirty = true;
                    final SmoothScroller mSmoothScroller = this.mSmoothScroller;
                    if (mSmoothScroller != null && mSmoothScroller.isRunning()) {
                        this.mSmoothScroller.onChildAttachedToWindow(view);
                    }
                }
            }
            else {
                if (childViewHolderInt.isScrap()) {
                    childViewHolderInt.unScrap();
                }
                else {
                    childViewHolderInt.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(view, childCount, view.getLayoutParams(), false);
            }
            if (layoutParams.mPendingInvalidate) {
                childViewHolderInt.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }
        
        public static int chooseSize(int size, final int n, final int n2) {
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            if (mode == Integer.MIN_VALUE) {
                return Math.min(size, Math.max(n, n2));
            }
            if (mode != 1073741824) {
                return Math.max(n, n2);
            }
            return size;
        }
        
        private void detachViewInternal(final int n, final View view) {
            this.mChildHelper.detachViewFromParent(n);
        }
        
        public static int getChildMeasureSpec(int max, int n, int n2, final int n3, final boolean b) {
            max = Math.max(0, max - n2);
            final int n4 = 0;
            final int n5 = 0;
            final int n6 = 0;
            n2 = 0;
            if (b) {
                if (n3 >= 0) {
                    n = n3;
                    max = 1073741824;
                }
                else if (n3 == -1) {
                    if (n != Integer.MIN_VALUE) {
                        if (n == 0) {
                            n = 0;
                            max = 0;
                            return View$MeasureSpec.makeMeasureSpec(n, max);
                        }
                        if (n != 1073741824) {
                            n = n5;
                            max = n2;
                            return View$MeasureSpec.makeMeasureSpec(n, max);
                        }
                    }
                    n2 = max;
                    max = n;
                    n = n2;
                }
                else if (n3 == -2) {
                    n = 0;
                    max = 0;
                }
                else {
                    n = n4;
                    max = n6;
                }
            }
            else if (n3 >= 0) {
                n = n3;
                max = 1073741824;
            }
            else if (n3 == -1) {
                n2 = n;
                n = max;
                max = n2;
            }
            else if (n3 == -2) {
                if (n != Integer.MIN_VALUE && n != 1073741824) {
                    n2 = 0;
                    n = max;
                    max = n2;
                }
                else {
                    n2 = Integer.MIN_VALUE;
                    n = max;
                    max = n2;
                }
            }
            else {
                max = n6;
                n = n4;
            }
            return View$MeasureSpec.makeMeasureSpec(n, max);
        }
        
        @Deprecated
        public static int getChildMeasureSpec(int max, int n, final int n2, final boolean b) {
            max = Math.max(0, max - n);
            final int n3 = 0;
            n = 0;
            if (b) {
                if (n2 >= 0) {
                    max = n2;
                    n = 1073741824;
                }
                else {
                    max = 0;
                    n = 0;
                }
            }
            else if (n2 >= 0) {
                max = n2;
                n = 1073741824;
            }
            else if (n2 == -1) {
                n = 1073741824;
            }
            else if (n2 == -2) {
                n = Integer.MIN_VALUE;
            }
            else {
                max = n3;
            }
            return View$MeasureSpec.makeMeasureSpec(max, n);
        }
        
        private int[] getChildRectangleOnScreenScrollAmount(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b) {
            final int paddingLeft = this.getPaddingLeft();
            final int paddingTop = this.getPaddingTop();
            final int n = this.getWidth() - this.getPaddingRight();
            final int height = this.getHeight();
            final int paddingBottom = this.getPaddingBottom();
            final int n2 = view.getLeft() + rect.left - view.getScrollX();
            final int n3 = view.getTop() + rect.top - view.getScrollY();
            final int n4 = rect.width() + n2;
            final int height2 = rect.height();
            int n5 = Math.min(0, n2 - paddingLeft);
            int n6 = Math.min(0, n3 - paddingTop);
            final int max = Math.max(0, n4 - n);
            final int max2 = Math.max(0, height2 + n3 - (height - paddingBottom));
            if (this.getLayoutDirection() == 1) {
                if (max != 0) {
                    n5 = max;
                }
                else {
                    n5 = Math.max(n5, n4 - n);
                }
            }
            else if (n5 == 0) {
                n5 = Math.min(n2 - paddingLeft, max);
            }
            if (n6 == 0) {
                n6 = Math.min(n3 - paddingTop, max2);
            }
            return new int[] { n5, n6 };
        }
        
        public static Properties getProperties(final Context context, final AttributeSet set, final int n, final int n2) {
            final Properties properties = new Properties();
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, n2);
            properties.orientation = obtainStyledAttributes.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = obtainStyledAttributes.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            obtainStyledAttributes.recycle();
            return properties;
        }
        
        private boolean isFocusedChildVisibleAfterScrolling(final RecyclerView recyclerView, final int n, final int n2) {
            final View focusedChild = recyclerView.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            final int paddingLeft = this.getPaddingLeft();
            final int paddingTop = this.getPaddingTop();
            final int width = this.getWidth();
            final int paddingRight = this.getPaddingRight();
            final int height = this.getHeight();
            final int paddingBottom = this.getPaddingBottom();
            final Rect mTempRect = this.mRecyclerView.mTempRect;
            this.getDecoratedBoundsWithMargins(focusedChild, mTempRect);
            return mTempRect.left - n < width - paddingRight && mTempRect.right - n > paddingLeft && mTempRect.top - n2 < height - paddingBottom && mTempRect.bottom - n2 > paddingTop;
        }
        
        private static boolean isMeasurementUpToDate(final int n, int size, final int n2) {
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            final boolean b = false;
            boolean b2 = false;
            if (n2 > 0 && n != n2) {
                return false;
            }
            if (mode == Integer.MIN_VALUE) {
                boolean b3 = b;
                if (size >= n) {
                    b3 = true;
                }
                return b3;
            }
            if (mode == 0) {
                return true;
            }
            if (mode != 1073741824) {
                return false;
            }
            if (size == n) {
                b2 = true;
            }
            return b2;
        }
        
        private void onSmoothScrollerStopped(final SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }
        
        private void scrapOrRecycleView(final Recycler recycler, final int n, final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.shouldIgnore()) {
                return;
            }
            if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                this.removeViewAt(n);
                recycler.recycleViewHolderInternal(childViewHolderInt);
                return;
            }
            this.detachViewAt(n);
            recycler.scrapView(view);
            this.mRecyclerView.mViewInfoStore.onViewDetached(childViewHolderInt);
        }
        
        public void addDisappearingView(final View view) {
            this.addDisappearingView(view, -1);
        }
        
        public void addDisappearingView(final View view, final int n) {
            this.addViewInt(view, n, true);
        }
        
        public void addView(final View view) {
            this.addView(view, -1);
        }
        
        public void addView(final View view, final int n) {
            this.addViewInt(view, n, false);
        }
        
        public void assertInLayoutOrScroll(final String s) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.assertInLayoutOrScroll(s);
            }
        }
        
        public void assertNotInLayoutOrScroll(final String s) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.assertNotInLayoutOrScroll(s);
            }
        }
        
        public void attachView(final View view) {
            this.attachView(view, -1);
        }
        
        public void attachView(final View view, final int n) {
            this.attachView(view, n, (LayoutParams)view.getLayoutParams());
        }
        
        public void attachView(final View view, final int n, final LayoutParams layoutParams) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            this.mChildHelper.attachViewToParent(view, n, (ViewGroup$LayoutParams)layoutParams, childViewHolderInt.isRemoved());
        }
        
        public void calculateItemDecorationsForChild(final View view, final Rect rect) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView == null) {
                rect.set(0, 0, 0, 0);
                return;
            }
            rect.set(mRecyclerView.getItemDecorInsetsForChild(view));
        }
        
        public boolean canScrollHorizontally() {
            return false;
        }
        
        public boolean canScrollVertically() {
            return false;
        }
        
        public boolean checkLayoutParams(final LayoutParams layoutParams) {
            return layoutParams != null;
        }
        
        public void collectAdjacentPrefetchPositions(final int n, final int n2, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }
        
        public void collectInitialPrefetchPositions(final int n, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }
        
        public int computeHorizontalScrollExtent(final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollOffset(final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollRange(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollExtent(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollOffset(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollRange(final State state) {
            return 0;
        }
        
        public void detachAndScrapAttachedViews(final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.scrapOrRecycleView(recycler, i, this.getChildAt(i));
            }
        }
        
        public void detachAndScrapView(final View view, final Recycler recycler) {
            this.scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(view), view);
        }
        
        public void detachAndScrapViewAt(final int n, final Recycler recycler) {
            this.scrapOrRecycleView(recycler, n, this.getChildAt(n));
        }
        
        public void detachView(final View view) {
            final int indexOfChild = this.mChildHelper.indexOfChild(view);
            if (indexOfChild >= 0) {
                this.detachViewInternal(indexOfChild, view);
            }
        }
        
        public void detachViewAt(final int n) {
            this.detachViewInternal(n, this.getChildAt(n));
        }
        
        void dispatchAttachedToWindow(final RecyclerView recyclerView) {
            this.mIsAttachedToWindow = true;
            this.onAttachedToWindow(recyclerView);
        }
        
        void dispatchDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.mIsAttachedToWindow = false;
            this.onDetachedFromWindow(recyclerView, recycler);
        }
        
        public void endAnimation(final View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }
        
        @Nullable
        public View findContainingItemView(View containingItemView) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView == null) {
                return null;
            }
            containingItemView = mRecyclerView.findContainingItemView(containingItemView);
            if (containingItemView == null) {
                return null;
            }
            if (this.mChildHelper.isHidden(containingItemView)) {
                return null;
            }
            return containingItemView;
        }
        
        public View findViewByPosition(final int n) {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null) {
                    if (childViewHolderInt.getLayoutPosition() == n && !childViewHolderInt.shouldIgnore()) {
                        if (this.mRecyclerView.mState.isPreLayout()) {
                            return child;
                        }
                        if (!childViewHolderInt.isRemoved()) {
                            return child;
                        }
                    }
                }
            }
            return null;
        }
        
        public abstract LayoutParams generateDefaultLayoutParams();
        
        public LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
            return new LayoutParams(context, set);
        }
        
        public LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                return new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
                return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
            }
            return new LayoutParams(viewGroup$LayoutParams);
        }
        
        public int getBaseline() {
            return -1;
        }
        
        public int getBottomDecorationHeight(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.bottom;
        }
        
        public View getChildAt(final int n) {
            final ChildHelper mChildHelper = this.mChildHelper;
            if (mChildHelper != null) {
                return mChildHelper.getChildAt(n);
            }
            return null;
        }
        
        public int getChildCount() {
            final ChildHelper mChildHelper = this.mChildHelper;
            if (mChildHelper != null) {
                return mChildHelper.getChildCount();
            }
            return 0;
        }
        
        public boolean getClipToPadding() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            return mRecyclerView != null && mRecyclerView.mClipToPadding;
        }
        
        public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            int itemCount = 1;
            if (mRecyclerView == null) {
                return 1;
            }
            if (mRecyclerView.mAdapter == null) {
                return 1;
            }
            if (this.canScrollHorizontally()) {
                itemCount = this.mRecyclerView.mAdapter.getItemCount();
            }
            return itemCount;
        }
        
        public int getDecoratedBottom(final View view) {
            return view.getBottom() + this.getBottomDecorationHeight(view);
        }
        
        public void getDecoratedBoundsWithMargins(final View view, final Rect rect) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(view, rect);
        }
        
        public int getDecoratedLeft(final View view) {
            return view.getLeft() - this.getLeftDecorationWidth(view);
        }
        
        public int getDecoratedMeasuredHeight(final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + mDecorInsets.top + mDecorInsets.bottom;
        }
        
        public int getDecoratedMeasuredWidth(final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + mDecorInsets.left + mDecorInsets.right;
        }
        
        public int getDecoratedRight(final View view) {
            return view.getRight() + this.getRightDecorationWidth(view);
        }
        
        public int getDecoratedTop(final View view) {
            return view.getTop() - this.getTopDecorationHeight(view);
        }
        
        public View getFocusedChild() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView == null) {
                return null;
            }
            final View focusedChild = mRecyclerView.getFocusedChild();
            if (focusedChild == null) {
                return null;
            }
            if (this.mChildHelper.isHidden(focusedChild)) {
                return null;
            }
            return focusedChild;
        }
        
        public int getHeight() {
            return this.mHeight;
        }
        
        public int getHeightMode() {
            return this.mHeightMode;
        }
        
        public int getItemCount() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            Object adapter;
            if (mRecyclerView != null) {
                adapter = mRecyclerView.getAdapter();
            }
            else {
                adapter = null;
            }
            if (adapter != null) {
                return ((Adapter)adapter).getItemCount();
            }
            return 0;
        }
        
        public int getItemViewType(final View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }
        
        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        }
        
        public int getLeftDecorationWidth(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.left;
        }
        
        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
        }
        
        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
        }
        
        public int getPaddingBottom() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return mRecyclerView.getPaddingBottom();
            }
            return 0;
        }
        
        public int getPaddingEnd() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return ViewCompat.getPaddingEnd((View)mRecyclerView);
            }
            return 0;
        }
        
        public int getPaddingLeft() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return mRecyclerView.getPaddingLeft();
            }
            return 0;
        }
        
        public int getPaddingRight() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return mRecyclerView.getPaddingRight();
            }
            return 0;
        }
        
        public int getPaddingStart() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return ViewCompat.getPaddingStart((View)mRecyclerView);
            }
            return 0;
        }
        
        public int getPaddingTop() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                return mRecyclerView.getPaddingTop();
            }
            return 0;
        }
        
        public int getPosition(final View view) {
            return ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }
        
        public int getRightDecorationWidth(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.right;
        }
        
        public int getRowCountForAccessibility(final Recycler recycler, final State state) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            int itemCount = 1;
            if (mRecyclerView == null) {
                return 1;
            }
            if (mRecyclerView.mAdapter == null) {
                return 1;
            }
            if (this.canScrollVertically()) {
                itemCount = this.mRecyclerView.mAdapter.getItemCount();
            }
            return itemCount;
        }
        
        public int getSelectionModeForAccessibility(final Recycler recycler, final State state) {
            return 0;
        }
        
        public int getTopDecorationHeight(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.top;
        }
        
        public void getTransformedBoundingBox(final View view, final boolean b, final Rect rect) {
            if (b) {
                final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
                rect.set(-mDecorInsets.left, -mDecorInsets.top, view.getWidth() + mDecorInsets.right, view.getHeight() + mDecorInsets.bottom);
            }
            else {
                rect.set(0, 0, view.getWidth(), view.getHeight());
            }
            if (this.mRecyclerView != null) {
                final Matrix matrix = view.getMatrix();
                if (matrix != null && !matrix.isIdentity()) {
                    final RectF mTempRectF = this.mRecyclerView.mTempRectF;
                    mTempRectF.set(rect);
                    matrix.mapRect(mTempRectF);
                    rect.set((int)Math.floor(mTempRectF.left), (int)Math.floor(mTempRectF.top), (int)Math.ceil(mTempRectF.right), (int)Math.ceil(mTempRectF.bottom));
                }
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
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewGroup$LayoutParams layoutParams = this.getChildAt(i).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean hasFocus() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            return mRecyclerView != null && mRecyclerView.hasFocus();
        }
        
        public void ignoreView(final View view) {
            final ViewParent parent = view.getParent();
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (parent == mRecyclerView && mRecyclerView.indexOfChild(view) != -1) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                childViewHolderInt.addFlags(128);
                this.mRecyclerView.mViewInfoStore.removeViewHolder(childViewHolderInt);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("View should be fully attached to be ignored");
            sb.append(this.mRecyclerView.exceptionLabel());
            throw new IllegalArgumentException(sb.toString());
        }
        
        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }
        
        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }
        
        public boolean isFocused() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            return mRecyclerView != null && mRecyclerView.isFocused();
        }
        
        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }
        
        public boolean isLayoutHierarchical(final Recycler recycler, final State state) {
            return false;
        }
        
        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }
        
        public boolean isSmoothScrolling() {
            final SmoothScroller mSmoothScroller = this.mSmoothScroller;
            return mSmoothScroller != null && mSmoothScroller.isRunning();
        }
        
        public boolean isViewPartiallyVisible(@NonNull final View view, final boolean b, final boolean b2) {
            final boolean b3 = this.mHorizontalBoundCheck.isViewWithinBoundFlags(view, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(view, 24579);
            if (b) {
                return b3;
            }
            return !b3;
        }
        
        public void layoutDecorated(final View view, final int n, final int n2, final int n3, final int n4) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            view.layout(mDecorInsets.left + n, mDecorInsets.top + n2, n3 - mDecorInsets.right, n4 - mDecorInsets.bottom);
        }
        
        public void layoutDecoratedWithMargins(final View view, final int n, final int n2, final int n3, final int n4) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect mDecorInsets = layoutParams.mDecorInsets;
            view.layout(mDecorInsets.left + n + layoutParams.leftMargin, mDecorInsets.top + n2 + layoutParams.topMargin, n3 - mDecorInsets.right - layoutParams.rightMargin, n4 - mDecorInsets.bottom - layoutParams.bottomMargin);
        }
        
        public void measureChild(final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void measureChildWithMargins(final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void moveView(final int n, final int n2) {
            final View child = this.getChildAt(n);
            if (child != null) {
                this.detachViewAt(n);
                this.attachView(child, n2);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot move a child from non-existing index:");
            sb.append(n);
            sb.append(this.mRecyclerView.toString());
            throw new IllegalArgumentException(sb.toString());
        }
        
        public void offsetChildrenHorizontal(final int n) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.offsetChildrenHorizontal(n);
            }
        }
        
        public void offsetChildrenVertical(final int n) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.offsetChildrenVertical(n);
            }
        }
        
        public void onAdapterChanged(final Adapter adapter, final Adapter adapter2) {
        }
        
        public boolean onAddFocusables(final RecyclerView recyclerView, final ArrayList<View> list, final int n, final int n2) {
            return false;
        }
        
        @CallSuper
        public void onAttachedToWindow(final RecyclerView recyclerView) {
        }
        
        @Deprecated
        public void onDetachedFromWindow(final RecyclerView recyclerView) {
        }
        
        @CallSuper
        public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.onDetachedFromWindow(recyclerView);
        }
        
        @Nullable
        public View onFocusSearchFailed(final View view, final int n, final Recycler recycler, final State state) {
            return null;
        }
        
        public void onInitializeAccessibilityEvent(final Recycler recycler, final State state, final AccessibilityEvent accessibilityEvent) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView == null) {
                return;
            }
            if (accessibilityEvent == null) {
                return;
            }
            boolean scrollable = true;
            if (!mRecyclerView.canScrollVertically(1)) {
                if (!this.mRecyclerView.canScrollVertically(-1)) {
                    if (!this.mRecyclerView.canScrollHorizontally(-1)) {
                        if (!this.mRecyclerView.canScrollHorizontally(1)) {
                            scrollable = false;
                        }
                    }
                }
            }
            accessibilityEvent.setScrollable(scrollable);
            if (this.mRecyclerView.mAdapter != null) {
                accessibilityEvent.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
            }
        }
        
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityEvent);
        }
        
        void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityNodeInfoCompat);
        }
        
        public void onInitializeAccessibilityNodeInfo(final Recycler recycler, final State state, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
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
        
        public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int position;
            if (this.canScrollVertically()) {
                position = this.getPosition(view);
            }
            else {
                position = 0;
            }
            int position2;
            if (this.canScrollHorizontally()) {
                position2 = this.getPosition(view);
            }
            else {
                position2 = 0;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(position, 1, position2, 1, false, false));
        }
        
        void onInitializeAccessibilityNodeInfoForItem(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && !this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, accessibilityNodeInfoCompat);
            }
        }
        
        public View onInterceptFocusSearch(final View view, final int n) {
            return null;
        }
        
        public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsChanged(final RecyclerView recyclerView) {
        }
        
        public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        }
        
        public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
            this.onItemsUpdated(recyclerView, n, n2);
        }
        
        public void onLayoutChildren(final Recycler recycler, final State state) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }
        
        public void onLayoutCompleted(final State state) {
        }
        
        public void onMeasure(final Recycler recycler, final State state, final int n, final int n2) {
            this.mRecyclerView.defaultOnMeasure(n, n2);
        }
        
        public boolean onRequestChildFocus(final RecyclerView recyclerView, final State state, final View view, final View view2) {
            return this.onRequestChildFocus(recyclerView, view, view2);
        }
        
        @Deprecated
        public boolean onRequestChildFocus(final RecyclerView recyclerView, final View view, final View view2) {
            return this.isSmoothScrolling() || recyclerView.isComputingLayout();
        }
        
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        public void onScrollStateChanged(final int n) {
        }
        
        boolean performAccessibilityAction(final int n, final Bundle bundle) {
            return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, n, bundle);
        }
        
        public boolean performAccessibilityAction(final Recycler recycler, final State state, int n, final Bundle bundle) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView == null) {
                return false;
            }
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            int n5 = 0;
            if (n != 4096) {
                if (n != 8192) {
                    n = n3;
                }
                else {
                    if (mRecyclerView.canScrollVertically(-1)) {
                        n = -(this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
                    }
                    else {
                        n = n4;
                    }
                    if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        n5 = -(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                    }
                }
            }
            else {
                if (mRecyclerView.canScrollVertically(1)) {
                    n = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                }
                else {
                    n = n2;
                }
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
        
        public boolean performAccessibilityActionForItem(final Recycler recycler, final State state, final View view, final int n, final Bundle bundle) {
            return false;
        }
        
        boolean performAccessibilityActionForItem(final View view, final int n, final Bundle bundle) {
            return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, n, bundle);
        }
        
        public void postOnAnimation(final Runnable runnable) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                ViewCompat.postOnAnimation((View)mRecyclerView, runnable);
            }
        }
        
        public void removeAllViews() {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.mChildHelper.removeViewAt(i);
            }
        }
        
        public void removeAndRecycleAllViews(final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                if (!RecyclerView.getChildViewHolderInt(this.getChildAt(i)).shouldIgnore()) {
                    this.removeAndRecycleViewAt(i, recycler);
                }
            }
        }
        
        void removeAndRecycleScrapInt(final Recycler recycler) {
            final int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; --i) {
                final View scrapView = recycler.getScrapViewAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(scrapView);
                if (!childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.setIsRecyclable(false);
                    if (childViewHolderInt.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrapView, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(childViewHolderInt);
                    }
                    childViewHolderInt.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrapView);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }
        
        public void removeAndRecycleView(final View view, final Recycler recycler) {
            this.removeView(view);
            recycler.recycleView(view);
        }
        
        public void removeAndRecycleViewAt(final int n, final Recycler recycler) {
            final View child = this.getChildAt(n);
            this.removeViewAt(n);
            recycler.recycleView(child);
        }
        
        public boolean removeCallbacks(final Runnable runnable) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            return mRecyclerView != null && mRecyclerView.removeCallbacks(runnable);
        }
        
        public void removeDetachedView(final View view) {
            this.mRecyclerView.removeDetachedView(view, false);
        }
        
        public void removeView(final View view) {
            this.mChildHelper.removeView(view);
        }
        
        public void removeViewAt(final int n) {
            if (this.getChildAt(n) != null) {
                this.mChildHelper.removeViewAt(n);
            }
        }
        
        public boolean requestChildRectangleOnScreen(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b) {
            return this.requestChildRectangleOnScreen(recyclerView, view, rect, b, false);
        }
        
        public boolean requestChildRectangleOnScreen(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b, final boolean b2) {
            final int[] childRectangleOnScreenScrollAmount = this.getChildRectangleOnScreenScrollAmount(recyclerView, view, rect, b);
            final int n = childRectangleOnScreenScrollAmount[0];
            final int n2 = childRectangleOnScreenScrollAmount[1];
            if (b2 && !this.isFocusedChildVisibleAfterScrolling(recyclerView, n, n2)) {
                return false;
            }
            if (n == 0 && n2 == 0) {
                return false;
            }
            if (b) {
                recyclerView.scrollBy(n, n2);
                return true;
            }
            recyclerView.smoothScrollBy(n, n2);
            return true;
        }
        
        public void requestLayout() {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.requestLayout();
            }
        }
        
        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }
        
        public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        public void scrollToPosition(final int n) {
        }
        
        public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        public void setAutoMeasureEnabled(final boolean mAutoMeasure) {
            this.mAutoMeasure = mAutoMeasure;
        }
        
        void setExactMeasureSpecsFrom(final RecyclerView recyclerView) {
            this.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }
        
        public final void setItemPrefetchEnabled(final boolean mItemPrefetchEnabled) {
            if (mItemPrefetchEnabled == this.mItemPrefetchEnabled) {
                return;
            }
            this.mItemPrefetchEnabled = mItemPrefetchEnabled;
            this.mPrefetchMaxCountObserved = 0;
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (mRecyclerView != null) {
                mRecyclerView.mRecycler.updateViewCacheSize();
            }
        }
        
        void setMeasureSpecs(final int n, final int n2) {
            this.mWidth = View$MeasureSpec.getSize(n);
            this.mWidthMode = View$MeasureSpec.getMode(n);
            if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mWidth = 0;
            }
            this.mHeight = View$MeasureSpec.getSize(n2);
            this.mHeightMode = View$MeasureSpec.getMode(n2);
            if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mHeight = 0;
            }
        }
        
        public void setMeasuredDimension(final int n, final int n2) {
            RecyclerView.access$1200(this.mRecyclerView, n, n2);
        }
        
        public void setMeasuredDimension(final Rect rect, final int n, final int n2) {
            this.setMeasuredDimension(chooseSize(n, rect.width() + this.getPaddingLeft() + this.getPaddingRight(), this.getMinimumWidth()), chooseSize(n2, rect.height() + this.getPaddingTop() + this.getPaddingBottom(), this.getMinimumHeight()));
        }
        
        void setMeasuredDimensionFromChildren(final int n, final int n2) {
            final int childCount = this.getChildCount();
            if (childCount == 0) {
                this.mRecyclerView.defaultOnMeasure(n, n2);
                return;
            }
            int left = Integer.MAX_VALUE;
            int top = Integer.MAX_VALUE;
            int right = Integer.MIN_VALUE;
            int bottom = Integer.MIN_VALUE;
            for (int i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                final Rect mTempRect = this.mRecyclerView.mTempRect;
                this.getDecoratedBoundsWithMargins(child, mTempRect);
                if (mTempRect.left < left) {
                    left = mTempRect.left;
                }
                if (mTempRect.right > right) {
                    right = mTempRect.right;
                }
                if (mTempRect.top < top) {
                    top = mTempRect.top;
                }
                if (mTempRect.bottom > bottom) {
                    bottom = mTempRect.bottom;
                }
            }
            this.mRecyclerView.mTempRect.set(left, top, right, bottom);
            this.setMeasuredDimension(this.mRecyclerView.mTempRect, n, n2);
        }
        
        public void setMeasurementCacheEnabled(final boolean mMeasurementCacheEnabled) {
            this.mMeasurementCacheEnabled = mMeasurementCacheEnabled;
        }
        
        void setRecyclerView(final RecyclerView mRecyclerView) {
            if (mRecyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            }
            else {
                this.mRecyclerView = mRecyclerView;
                this.mChildHelper = mRecyclerView.mChildHelper;
                this.mWidth = mRecyclerView.getWidth();
                this.mHeight = mRecyclerView.getHeight();
            }
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
        }
        
        boolean shouldMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            if (!view.isLayoutRequested() && this.mMeasurementCacheEnabled) {
                if (isMeasurementUpToDate(view.getWidth(), n, layoutParams.width)) {
                    if (isMeasurementUpToDate(view.getHeight(), n2, layoutParams.height)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        boolean shouldMeasureTwice() {
            return false;
        }
        
        boolean shouldReMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            if (this.mMeasurementCacheEnabled) {
                if (isMeasurementUpToDate(view.getMeasuredWidth(), n, layoutParams.width)) {
                    if (isMeasurementUpToDate(view.getMeasuredHeight(), n2, layoutParams.height)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int n) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }
        
        public void startSmoothScroll(final SmoothScroller mSmoothScroller) {
            final SmoothScroller mSmoothScroller2 = this.mSmoothScroller;
            if (mSmoothScroller2 != null && mSmoothScroller != mSmoothScroller2) {
                if (mSmoothScroller2.isRunning()) {
                    this.mSmoothScroller.stop();
                }
            }
            (this.mSmoothScroller = mSmoothScroller).start(this.mRecyclerView, this);
        }
        
        public void stopIgnoringView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.stopIgnoring();
            childViewHolderInt.resetInternal();
            childViewHolderInt.addFlags(4);
        }
        
        void stopSmoothScroller() {
            final SmoothScroller mSmoothScroller = this.mSmoothScroller;
            if (mSmoothScroller != null) {
                mSmoothScroller.stop();
            }
        }
        
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
        
        public interface LayoutPrefetchRegistry
        {
            void addPosition(final int p0, final int p1);
        }
        
        public static class Properties
        {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
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
    
    public interface OnChildAttachStateChangeListener
    {
        void onChildViewAttachedToWindow(final View p0);
        
        void onChildViewDetachedFromWindow(final View p0);
    }
    
    public abstract static class OnFlingListener
    {
        public abstract boolean onFling(final int p0, final int p1);
    }
    
    public interface OnItemTouchListener
    {
        boolean onInterceptTouchEvent(final RecyclerView p0, final MotionEvent p1);
        
        void onRequestDisallowInterceptTouchEvent(final boolean p0);
        
        void onTouchEvent(final RecyclerView p0, final MotionEvent p1);
    }
    
    public abstract static class OnScrollListener
    {
        public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
        }
        
        public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
        }
    }
    
    public static class RecycledViewPool
    {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount;
        SparseArray<ScrapData> mScrap;
        
        public RecycledViewPool() {
            this.mScrap = (SparseArray<ScrapData>)new SparseArray();
            this.mAttachCount = 0;
        }
        
        private ScrapData getScrapDataForType(final int n) {
            final ScrapData scrapData = (ScrapData)this.mScrap.get(n);
            if (scrapData == null) {
                final ScrapData scrapData2 = new ScrapData();
                this.mScrap.put(n, (Object)scrapData2);
                return scrapData2;
            }
            return scrapData;
        }
        
        void attach(final Adapter adapter) {
            ++this.mAttachCount;
        }
        
        public void clear() {
            for (int i = 0; i < this.mScrap.size(); ++i) {
                ((ScrapData)this.mScrap.valueAt(i)).mScrapHeap.clear();
            }
        }
        
        void detach() {
            --this.mAttachCount;
        }
        
        void factorInBindTime(final int n, final long n2) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mBindRunningAverageNs = this.runningAverage(scrapDataForType.mBindRunningAverageNs, n2);
        }
        
        void factorInCreateTime(final int n, final long n2) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mCreateRunningAverageNs = this.runningAverage(scrapDataForType.mCreateRunningAverageNs, n2);
        }
        
        public ViewHolder getRecycledView(final int n) {
            final ScrapData scrapData = (ScrapData)this.mScrap.get(n);
            if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
                final ArrayList<ViewHolder> mScrapHeap = scrapData.mScrapHeap;
                return mScrapHeap.remove(mScrapHeap.size() - 1);
            }
            return null;
        }
        
        public int getRecycledViewCount(final int n) {
            return this.getScrapDataForType(n).mScrapHeap.size();
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            if (adapter != null) {
                this.detach();
            }
            if (!b && this.mAttachCount == 0) {
                this.clear();
            }
            if (adapter2 != null) {
                this.attach(adapter2);
            }
        }
        
        public void putRecycledView(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            final ArrayList<ViewHolder> mScrapHeap = this.getScrapDataForType(itemViewType).mScrapHeap;
            if (((ScrapData)this.mScrap.get(itemViewType)).mMaxScrap <= mScrapHeap.size()) {
                return;
            }
            viewHolder.resetInternal();
            mScrapHeap.add(viewHolder);
        }
        
        long runningAverage(final long n, final long n2) {
            if (n == 0L) {
                return n2;
            }
            return n / 4L * 3L + n2 / 4L;
        }
        
        public void setMaxRecycledViews(final int n, final int mMaxScrap) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mMaxScrap = mMaxScrap;
            final ArrayList<ViewHolder> mScrapHeap = scrapDataForType.mScrapHeap;
            if (mScrapHeap != null) {
                while (mScrapHeap.size() > mMaxScrap) {
                    mScrapHeap.remove(mScrapHeap.size() - 1);
                }
            }
        }
        
        int size() {
            int n = 0;
            for (int i = 0; i < this.mScrap.size(); ++i) {
                final ArrayList<ViewHolder> mScrapHeap = ((ScrapData)this.mScrap.valueAt(i)).mScrapHeap;
                if (mScrapHeap != null) {
                    n += mScrapHeap.size();
                }
            }
            return n;
        }
        
        boolean willBindInTime(final int n, final long n2, final long n3) {
            final long mBindRunningAverageNs = this.getScrapDataForType(n).mBindRunningAverageNs;
            return mBindRunningAverageNs == 0L || n2 + mBindRunningAverageNs < n3;
        }
        
        boolean willCreateInTime(final int n, final long n2, final long n3) {
            final long mCreateRunningAverageNs = this.getScrapDataForType(n).mCreateRunningAverageNs;
            return mCreateRunningAverageNs == 0L || n2 + mCreateRunningAverageNs < n3;
        }
        
        static class ScrapData
        {
            long mBindRunningAverageNs;
            long mCreateRunningAverageNs;
            int mMaxScrap;
            ArrayList<ViewHolder> mScrapHeap;
            
            ScrapData() {
                this.mScrapHeap = new ArrayList<ViewHolder>();
                this.mMaxScrap = 5;
                this.mCreateRunningAverageNs = 0L;
                this.mBindRunningAverageNs = 0L;
            }
        }
    }
    
    public final class Recycler
    {
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
            this.mAttachedScrap = new ArrayList<ViewHolder>();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList<ViewHolder>();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList((List<? extends ViewHolder>)this.mAttachedScrap);
            this.mRequestedCacheMax = 2;
            this.mViewCacheMax = 2;
        }
        
        private void attachAccessibilityDelegateOnBind(final ViewHolder viewHolder) {
            if (!RecyclerView.this.isAccessibilityEnabled()) {
                return;
            }
            final View itemView = viewHolder.itemView;
            if (ViewCompat.getImportantForAccessibility(itemView) == 0) {
                ViewCompat.setImportantForAccessibility(itemView, 1);
            }
            if (!ViewCompat.hasAccessibilityDelegate(itemView)) {
                viewHolder.addFlags(16384);
                ViewCompat.setAccessibilityDelegate(itemView, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
            }
        }
        
        private void invalidateDisplayListInt(final ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                this.invalidateDisplayListInt((ViewGroup)viewHolder.itemView, false);
            }
        }
        
        private void invalidateDisplayListInt(final ViewGroup viewGroup, final boolean b) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    this.invalidateDisplayListInt((ViewGroup)child, true);
                }
            }
            if (!b) {
                return;
            }
            if (viewGroup.getVisibility() == 4) {
                viewGroup.setVisibility(0);
                viewGroup.setVisibility(4);
                return;
            }
            final int visibility = viewGroup.getVisibility();
            viewGroup.setVisibility(4);
            viewGroup.setVisibility(visibility);
        }
        
        private boolean tryBindViewHolderByDeadline(final ViewHolder viewHolder, final int n, final int mPreLayoutPosition, long nanoTime) {
            viewHolder.mOwnerRecyclerView = RecyclerView.this;
            final int itemViewType = viewHolder.getItemViewType();
            final long nanoTime2 = RecyclerView.this.getNanoTime();
            if (nanoTime != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(itemViewType, nanoTime2, nanoTime)) {
                return false;
            }
            RecyclerView.this.mAdapter.bindViewHolder(viewHolder, n);
            nanoTime = RecyclerView.this.getNanoTime();
            this.mRecyclerPool.factorInBindTime(viewHolder.getItemViewType(), nanoTime - nanoTime2);
            this.attachAccessibilityDelegateOnBind(viewHolder);
            if (RecyclerView.this.mState.isPreLayout()) {
                viewHolder.mPreLayoutPosition = mPreLayoutPosition;
            }
            return true;
        }
        
        void addViewHolderToRecycledViewPool(final ViewHolder viewHolder, final boolean b) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(viewHolder);
            if (viewHolder.hasAnyOfTheFlags(16384)) {
                viewHolder.setFlags(0, 16384);
                ViewCompat.setAccessibilityDelegate(viewHolder.itemView, null);
            }
            if (b) {
                this.dispatchViewRecycled(viewHolder);
            }
            viewHolder.mOwnerRecyclerView = null;
            this.getRecycledViewPool().putRecycledView(viewHolder);
        }
        
        public void bindViewToPosition(final View view, final int n) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
                sb.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            final int positionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
            if (positionOffset >= 0 && positionOffset < RecyclerView.this.mAdapter.getItemCount()) {
                this.tryBindViewHolderByDeadline(childViewHolderInt, positionOffset, n, Long.MAX_VALUE);
                final ViewGroup$LayoutParams layoutParams = childViewHolderInt.itemView.getLayoutParams();
                LayoutParams layoutParams2;
                if (layoutParams == null) {
                    layoutParams2 = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                    childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else if (!RecyclerView.this.checkLayoutParams(layoutParams)) {
                    layoutParams2 = (LayoutParams)RecyclerView.this.generateLayoutParams(layoutParams);
                    childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else {
                    layoutParams2 = (LayoutParams)layoutParams;
                }
                boolean mPendingInvalidate = true;
                layoutParams2.mInsetsDirty = true;
                layoutParams2.mViewHolder = childViewHolderInt;
                if (childViewHolderInt.itemView.getParent() != null) {
                    mPendingInvalidate = false;
                }
                layoutParams2.mPendingInvalidate = mPendingInvalidate;
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Inconsistency detected. Invalid item position ");
            sb2.append(n);
            sb2.append("(offset:");
            sb2.append(positionOffset);
            sb2.append(").");
            sb2.append("state:");
            sb2.append(RecyclerView.this.mState.getItemCount());
            sb2.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        
        public void clear() {
            this.mAttachedScrap.clear();
            this.recycleAndClearCachedViews();
        }
        
        void clearOldPositions() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                this.mCachedViews.get(i).clearOldPosition();
            }
            for (int size2 = this.mAttachedScrap.size(), j = 0; j < size2; ++j) {
                this.mAttachedScrap.get(j).clearOldPosition();
            }
            final ArrayList<ViewHolder> mChangedScrap = this.mChangedScrap;
            if (mChangedScrap != null) {
                for (int size3 = mChangedScrap.size(), k = 0; k < size3; ++k) {
                    this.mChangedScrap.get(k).clearOldPosition();
                }
            }
        }
        
        void clearScrap() {
            this.mAttachedScrap.clear();
            final ArrayList<ViewHolder> mChangedScrap = this.mChangedScrap;
            if (mChangedScrap != null) {
                mChangedScrap.clear();
            }
        }
        
        public int convertPreLayoutPositionToPostLayout(final int n) {
            if (n < 0 || n >= RecyclerView.this.mState.getItemCount()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid position ");
                sb.append(n);
                sb.append(". State ");
                sb.append("item count is ");
                sb.append(RecyclerView.this.mState.getItemCount());
                sb.append(RecyclerView.this.exceptionLabel());
                throw new IndexOutOfBoundsException(sb.toString());
            }
            if (!RecyclerView.this.mState.isPreLayout()) {
                return n;
            }
            return RecyclerView.this.mAdapterHelper.findPositionOffset(n);
        }
        
        void dispatchViewRecycled(final ViewHolder viewHolder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
            }
        }
        
        ViewHolder getChangedScrapViewForPosition(int i) {
            final ArrayList<ViewHolder> mChangedScrap = this.mChangedScrap;
            if (mChangedScrap == null) {
                return null;
            }
            final int size = mChangedScrap.size();
            if (size == 0) {
                return null;
            }
            for (int j = 0; j < size; ++j) {
                final ViewHolder viewHolder = this.mChangedScrap.get(j);
                if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == i) {
                    viewHolder.addFlags(32);
                    return viewHolder;
                }
            }
            if (!RecyclerView.this.mAdapter.hasStableIds()) {
                return null;
            }
            i = RecyclerView.this.mAdapterHelper.findPositionOffset(i);
            if (i > 0 && i < RecyclerView.this.mAdapter.getItemCount()) {
                final long itemId = RecyclerView.this.mAdapter.getItemId(i);
                ViewHolder viewHolder2;
                for (i = 0; i < size; ++i) {
                    viewHolder2 = this.mChangedScrap.get(i);
                    if (!viewHolder2.wasReturnedFromScrap() && viewHolder2.getItemId() == itemId) {
                        viewHolder2.addFlags(32);
                        return viewHolder2;
                    }
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
        
        ViewHolder getScrapOrCachedViewForId(final long n, final int n2, final boolean b) {
            for (int i = this.mAttachedScrap.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (viewHolder.getItemId() == n && !viewHolder.wasReturnedFromScrap()) {
                    if (n2 == viewHolder.getItemViewType()) {
                        viewHolder.addFlags(32);
                        if (!viewHolder.isRemoved()) {
                            return viewHolder;
                        }
                        if (!RecyclerView.this.mState.isPreLayout()) {
                            viewHolder.setFlags(2, 14);
                            return viewHolder;
                        }
                        return viewHolder;
                    }
                    else if (!b) {
                        this.mAttachedScrap.remove(i);
                        RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                        this.quickRecycleScrapView(viewHolder.itemView);
                    }
                }
            }
            for (int j = this.mCachedViews.size() - 1; j >= 0; --j) {
                final ViewHolder viewHolder2 = this.mCachedViews.get(j);
                if (viewHolder2.getItemId() == n) {
                    if (n2 == viewHolder2.getItemViewType()) {
                        if (!b) {
                            this.mCachedViews.remove(j);
                            return viewHolder2;
                        }
                        return viewHolder2;
                    }
                    else if (!b) {
                        this.recycleCachedViewAt(j);
                        return null;
                    }
                }
            }
            return null;
        }
        
        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int indexOfChild, final boolean b) {
            for (int size = this.mAttachedScrap.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == indexOfChild && (!viewHolder.isInvalid() && (RecyclerView.this.mState.mInPreLayout || !viewHolder.isRemoved()))) {
                    viewHolder.addFlags(32);
                    return viewHolder;
                }
            }
            if (!b) {
                final View hiddenNonRemovedView = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(indexOfChild);
                if (hiddenNonRemovedView != null) {
                    final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(hiddenNonRemovedView);
                    RecyclerView.this.mChildHelper.unhide(hiddenNonRemovedView);
                    indexOfChild = RecyclerView.this.mChildHelper.indexOfChild(hiddenNonRemovedView);
                    if (indexOfChild != -1) {
                        RecyclerView.this.mChildHelper.detachViewFromParent(indexOfChild);
                        this.scrapView(hiddenNonRemovedView);
                        childViewHolderInt.addFlags(8224);
                        return childViewHolderInt;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("layout index should not be -1 after unhiding a view:");
                    sb.append(childViewHolderInt);
                    sb.append(RecyclerView.this.exceptionLabel());
                    throw new IllegalStateException(sb.toString());
                }
            }
            final int size2 = this.mCachedViews.size();
            int j = 0;
            while (j < size2) {
                final ViewHolder viewHolder2 = this.mCachedViews.get(j);
                if (!viewHolder2.isInvalid() && viewHolder2.getLayoutPosition() == indexOfChild) {
                    if (!b) {
                        this.mCachedViews.remove(j);
                        return viewHolder2;
                    }
                    return viewHolder2;
                }
                else {
                    ++j;
                }
            }
            return null;
        }
        
        View getScrapViewAt(final int n) {
            return this.mAttachedScrap.get(n).itemView;
        }
        
        public View getViewForPosition(final int n) {
            return this.getViewForPosition(n, false);
        }
        
        View getViewForPosition(final int n, final boolean b) {
            return this.tryGetViewHolderForPositionByDeadline(n, b, Long.MAX_VALUE).itemView;
        }
        
        void markItemDecorInsetsDirty() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.mCachedViews.get(i).itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
        
        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                    final ViewHolder viewHolder = this.mCachedViews.get(i);
                    if (viewHolder != null) {
                        viewHolder.addFlags(6);
                        viewHolder.addChangePayload(null);
                    }
                }
                return;
            }
            this.recycleAndClearCachedViews();
        }
        
        void offsetPositionRecordsForInsert(final int n, final int n2) {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n) {
                    viewHolder.offsetPosition(n2, true);
                }
            }
        }
        
        void offsetPositionRecordsForMove(final int n, final int n2) {
            int n3;
            int n4;
            int n5;
            if (n < n2) {
                n3 = n;
                n4 = n2;
                n5 = -1;
            }
            else {
                n3 = n2;
                n4 = n;
                n5 = 1;
            }
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n3) {
                    if (viewHolder.mPosition <= n4) {
                        if (viewHolder.mPosition == n) {
                            viewHolder.offsetPosition(n2 - n, false);
                        }
                        else {
                            viewHolder.offsetPosition(n5, false);
                        }
                    }
                }
            }
        }
        
        void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    if (viewHolder.mPosition >= n + n2) {
                        viewHolder.offsetPosition(-n2, b);
                    }
                    else if (viewHolder.mPosition >= n) {
                        viewHolder.addFlags(8);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            this.clear();
            this.getRecycledViewPool().onAdapterChanged(adapter, adapter2, b);
        }
        
        void quickRecycleScrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.mScrapContainer = null;
            childViewHolderInt.mInChangeScrap = false;
            childViewHolderInt.clearReturnedFromScrapFlag();
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                this.recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
            if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
            }
        }
        
        void recycleCachedViewAt(final int n) {
            this.addViewHolderToRecycledViewPool(this.mCachedViews.get(n), true);
            this.mCachedViews.remove(n);
        }
        
        public void recycleView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (childViewHolderInt.isScrap()) {
                childViewHolderInt.unScrap();
            }
            else if (childViewHolderInt.wasReturnedFromScrap()) {
                childViewHolderInt.clearReturnedFromScrapFlag();
            }
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleViewHolderInternal(final ViewHolder viewHolder) {
            final boolean scrap = viewHolder.isScrap();
            boolean b = false;
            if (scrap || viewHolder.itemView.getParent() != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Scrapped or attached views may not be recycled. isScrap:");
                sb.append(viewHolder.isScrap());
                sb.append(" isAttached:");
                if (viewHolder.itemView.getParent() != null) {
                    b = true;
                }
                sb.append(b);
                sb.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            if (viewHolder.isTmpDetached()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
                sb2.append(viewHolder);
                sb2.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(sb2.toString());
            }
            if (viewHolder.shouldIgnore()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                sb3.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(sb3.toString());
            }
            final boolean access$900 = viewHolder.doesTransientStatePreventRecycling();
            final boolean b2 = RecyclerView.this.mAdapter != null && access$900 && RecyclerView.this.mAdapter.onFailedToRecycleView(viewHolder);
            final boolean b3 = false;
            final boolean b4 = false;
            final boolean b5 = false;
            boolean b6;
            boolean b7;
            if (!b2 && !viewHolder.isRecyclable()) {
                b6 = b3;
                b7 = b5;
            }
            else {
                if (this.mViewCacheMax > 0) {
                    if (!viewHolder.hasAnyOfTheFlags(526)) {
                        int size = this.mCachedViews.size();
                        if (size >= this.mViewCacheMax && size > 0) {
                            this.recycleCachedViewAt(0);
                            --size;
                        }
                        int n = size;
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK && size > 0) {
                            if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(viewHolder.mPosition)) {
                                int n2;
                                for (n2 = size - 1; n2 >= 0 && RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(this.mCachedViews.get(n2).mPosition); --n2) {}
                                n = n2 + 1;
                            }
                        }
                        this.mCachedViews.add(n, viewHolder);
                        b6 = true;
                    }
                    else {
                        b6 = b4;
                    }
                }
                else {
                    b6 = b4;
                }
                if (!b6) {
                    this.addViewHolderToRecycledViewPool(viewHolder, true);
                    b7 = true;
                }
                else {
                    b7 = b5;
                }
            }
            RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
            if (!b6 && !b7 && access$900) {
                viewHolder.mOwnerRecyclerView = null;
            }
        }
        
        void recycleViewInternal(final View view) {
            this.recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }
        
        void scrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.hasAnyOfTheFlags(12) && (childViewHolderInt.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder(childViewHolderInt))) {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList<ViewHolder>();
                }
                childViewHolderInt.setScrapContainer(this, true);
                this.mChangedScrap.add(childViewHolderInt);
                return;
            }
            if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                sb.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            childViewHolderInt.setScrapContainer(this, false);
            this.mAttachedScrap.add(childViewHolderInt);
        }
        
        void setRecycledViewPool(final RecycledViewPool mRecyclerPool) {
            final RecycledViewPool mRecyclerPool2 = this.mRecyclerPool;
            if (mRecyclerPool2 != null) {
                mRecyclerPool2.detach();
            }
            if ((this.mRecyclerPool = mRecyclerPool) != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }
        
        void setViewCacheExtension(final ViewCacheExtension mViewCacheExtension) {
            this.mViewCacheExtension = mViewCacheExtension;
        }
        
        public void setViewCacheSize(final int mRequestedCacheMax) {
            this.mRequestedCacheMax = mRequestedCacheMax;
            this.updateViewCacheSize();
        }
        
        @Nullable
        ViewHolder tryGetViewHolderForPositionByDeadline(final int mPreLayoutPosition, final boolean b, final long n) {
            if (mPreLayoutPosition >= 0 && mPreLayoutPosition < RecyclerView.this.mState.getItemCount()) {
                boolean b2 = false;
                Object mViewHolder = null;
                final boolean preLayout = RecyclerView.this.mState.isPreLayout();
                final boolean b3 = true;
                if (preLayout) {
                    mViewHolder = this.getChangedScrapViewForPosition(mPreLayoutPosition);
                    b2 = (mViewHolder != null);
                }
                if (mViewHolder == null) {
                    mViewHolder = this.getScrapOrHiddenOrCachedHolderForPosition(mPreLayoutPosition, b);
                    if (mViewHolder != null) {
                        if (!this.validateViewHolderForOffsetPosition((ViewHolder)mViewHolder)) {
                            if (!b) {
                                ((ViewHolder)mViewHolder).addFlags(4);
                                if (((ViewHolder)mViewHolder).isScrap()) {
                                    RecyclerView.this.removeDetachedView(((ViewHolder)mViewHolder).itemView, false);
                                    ((ViewHolder)mViewHolder).unScrap();
                                }
                                else if (((ViewHolder)mViewHolder).wasReturnedFromScrap()) {
                                    ((ViewHolder)mViewHolder).clearReturnedFromScrapFlag();
                                }
                                this.recycleViewHolderInternal((ViewHolder)mViewHolder);
                            }
                            mViewHolder = null;
                        }
                        else {
                            b2 = true;
                        }
                    }
                }
                if (mViewHolder == null) {
                    final int positionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(mPreLayoutPosition);
                    if (positionOffset < 0 || positionOffset >= RecyclerView.this.mAdapter.getItemCount()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Inconsistency detected. Invalid item position ");
                        sb.append(mPreLayoutPosition);
                        sb.append("(offset:");
                        sb.append(positionOffset);
                        sb.append(").");
                        sb.append("state:");
                        sb.append(RecyclerView.this.mState.getItemCount());
                        sb.append(RecyclerView.this.exceptionLabel());
                        throw new IndexOutOfBoundsException(sb.toString());
                    }
                    final int itemViewType = RecyclerView.this.mAdapter.getItemViewType(positionOffset);
                    if (RecyclerView.this.mAdapter.hasStableIds()) {
                        mViewHolder = this.getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(positionOffset), itemViewType, b);
                        if (mViewHolder != null) {
                            ((ViewHolder)mViewHolder).mPosition = positionOffset;
                            b2 = true;
                        }
                    }
                    if (mViewHolder == null) {
                        final ViewCacheExtension mViewCacheExtension = this.mViewCacheExtension;
                        if (mViewCacheExtension != null) {
                            final View viewForPositionAndType = mViewCacheExtension.getViewForPositionAndType(this, mPreLayoutPosition, itemViewType);
                            if (viewForPositionAndType != null) {
                                mViewHolder = RecyclerView.this.getChildViewHolder(viewForPositionAndType);
                                if (mViewHolder == null) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("getViewForPositionAndType returned a view which does not have a ViewHolder");
                                    sb2.append(RecyclerView.this.exceptionLabel());
                                    throw new IllegalArgumentException(sb2.toString());
                                }
                                if (((ViewHolder)mViewHolder).shouldIgnore()) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                                    sb3.append(RecyclerView.this.exceptionLabel());
                                    throw new IllegalArgumentException(sb3.toString());
                                }
                            }
                        }
                    }
                    if (mViewHolder == null) {
                        mViewHolder = this.getRecycledViewPool().getRecycledView(itemViewType);
                        if (mViewHolder != null) {
                            ((ViewHolder)mViewHolder).resetInternal();
                            if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                                this.invalidateDisplayListInt((ViewHolder)mViewHolder);
                            }
                        }
                    }
                    if (mViewHolder == null) {
                        final long nanoTime = RecyclerView.this.getNanoTime();
                        if (n != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(itemViewType, nanoTime, n)) {
                            return null;
                        }
                        mViewHolder = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, itemViewType);
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                            final RecyclerView nestedRecyclerView = RecyclerView.findNestedRecyclerView(((ViewHolder)mViewHolder).itemView);
                            if (nestedRecyclerView != null) {
                                ((ViewHolder)mViewHolder).mNestedRecyclerView = new WeakReference<RecyclerView>(nestedRecyclerView);
                            }
                        }
                        this.mRecyclerPool.factorInCreateTime(itemViewType, RecyclerView.this.getNanoTime() - nanoTime);
                    }
                }
                if (b2 && !RecyclerView.this.mState.isPreLayout()) {
                    if (((ViewHolder)mViewHolder).hasAnyOfTheFlags(8192)) {
                        ((ViewHolder)mViewHolder).setFlags(0, 8192);
                        if (RecyclerView.this.mState.mRunSimpleAnimations) {
                            RecyclerView.this.recordAnimationInfoIfBouncedHiddenView((ViewHolder)mViewHolder, RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, (ViewHolder)mViewHolder, ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)mViewHolder) | 0x1000, ((ViewHolder)mViewHolder).getUnmodifiedPayloads()));
                        }
                    }
                }
                boolean tryBindViewHolderByDeadline = false;
                if (RecyclerView.this.mState.isPreLayout() && ((ViewHolder)mViewHolder).isBound()) {
                    ((ViewHolder)mViewHolder).mPreLayoutPosition = mPreLayoutPosition;
                }
                else if (!((ViewHolder)mViewHolder).isBound() || ((ViewHolder)mViewHolder).needsUpdate() || ((ViewHolder)mViewHolder).isInvalid()) {
                    tryBindViewHolderByDeadline = this.tryBindViewHolderByDeadline((ViewHolder)mViewHolder, RecyclerView.this.mAdapterHelper.findPositionOffset(mPreLayoutPosition), mPreLayoutPosition, n);
                }
                final ViewGroup$LayoutParams layoutParams = ((ViewHolder)mViewHolder).itemView.getLayoutParams();
                LayoutParams layoutParams2;
                if (layoutParams == null) {
                    layoutParams2 = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                    ((ViewHolder)mViewHolder).itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else if (!RecyclerView.this.checkLayoutParams(layoutParams)) {
                    layoutParams2 = (LayoutParams)RecyclerView.this.generateLayoutParams(layoutParams);
                    ((ViewHolder)mViewHolder).itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else {
                    layoutParams2 = (LayoutParams)layoutParams;
                }
                layoutParams2.mViewHolder = (ViewHolder)mViewHolder;
                layoutParams2.mPendingInvalidate = (b2 && tryBindViewHolderByDeadline && b3);
                return (ViewHolder)mViewHolder;
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Invalid item position ");
            sb4.append(mPreLayoutPosition);
            sb4.append("(");
            sb4.append(mPreLayoutPosition);
            sb4.append("). Item count:");
            sb4.append(RecyclerView.this.mState.getItemCount());
            sb4.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(sb4.toString());
        }
        
        void unscrapView(final ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            }
            else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.clearReturnedFromScrapFlag();
        }
        
        void updateViewCacheSize() {
            int mPrefetchMaxCountObserved;
            if (RecyclerView.this.mLayout != null) {
                mPrefetchMaxCountObserved = RecyclerView.this.mLayout.mPrefetchMaxCountObserved;
            }
            else {
                mPrefetchMaxCountObserved = 0;
            }
            this.mViewCacheMax = this.mRequestedCacheMax + mPrefetchMaxCountObserved;
            for (int n = this.mCachedViews.size() - 1; n >= 0 && this.mCachedViews.size() > this.mViewCacheMax; --n) {
                this.recycleCachedViewAt(n);
            }
        }
        
        boolean validateViewHolderForOffsetPosition(final ViewHolder viewHolder) {
            if (viewHolder.isRemoved()) {
                return RecyclerView.this.mState.isPreLayout();
            }
            if (viewHolder.mPosition < 0 || viewHolder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Inconsistency detected. Invalid view holder adapter position");
                sb.append(viewHolder);
                sb.append(RecyclerView.this.exceptionLabel());
                throw new IndexOutOfBoundsException(sb.toString());
            }
            final boolean preLayout = RecyclerView.this.mState.isPreLayout();
            boolean b = false;
            if (!preLayout && RecyclerView.this.mAdapter.getItemViewType(viewHolder.mPosition) != viewHolder.getItemViewType()) {
                return false;
            }
            if (RecyclerView.this.mAdapter.hasStableIds()) {
                if (viewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(viewHolder.mPosition)) {
                    b = true;
                }
                return b;
            }
            return true;
        }
        
        void viewRangeUpdate(final int n, final int n2) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    final int mPosition = viewHolder.mPosition;
                    if (mPosition >= n && mPosition < n + n2) {
                        viewHolder.addFlags(2);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
    }
    
    public interface RecyclerListener
    {
        void onViewRecycled(final ViewHolder p0);
    }
    
    private class RecyclerViewDataObserver extends AdapterDataObserver
    {
        RecyclerViewDataObserver() {
        }
        
        @Override
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            RecyclerView.this.mState.mStructureChanged = true;
            RecyclerView.this.setDataSetChangedAfterLayout();
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }
        
        @Override
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(n, n2, o)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeInserted(final int n, final int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(n, n2, n3)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeRemoved(final int n, final int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        void triggerUpdateProcessor() {
            if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
                final RecyclerView this$0 = RecyclerView.this;
                ViewCompat.postOnAnimation((View)this$0, this$0.mUpdateChildViewsRunnable);
                return;
            }
            final RecyclerView this$2 = RecyclerView.this;
            this$2.mAdapterUpdateDuringMeasure = true;
            this$2.requestLayout();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable mLayoutState;
        
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
                classLoader = LayoutManager.class.getClassLoader();
            }
            this.mLayoutState = parcel.readParcelable(classLoader);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        void copyFrom(final SavedState savedState) {
            this.mLayoutState = savedState.mLayoutState;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeParcelable(this.mLayoutState, 0);
        }
    }
    
    public static class SimpleOnItemTouchListener implements OnItemTouchListener
    {
        @Override
        public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
            return false;
        }
        
        @Override
        public void onRequestDisallowInterceptTouchEvent(final boolean b) {
        }
        
        @Override
        public void onTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
        }
    }
    
    public abstract static class SmoothScroller
    {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction;
        private boolean mRunning;
        private int mTargetPosition;
        private View mTargetView;
        
        public SmoothScroller() {
            this.mTargetPosition = -1;
            this.mRecyclingAction = new Action(0, 0);
        }
        
        private void onAnimation(final int n, final int n2) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || mRecyclerView == null) {
                this.stop();
            }
            this.mPendingInitialRun = false;
            final View mTargetView = this.mTargetView;
            if (mTargetView != null) {
                if (this.getChildPosition(mTargetView) == this.mTargetPosition) {
                    this.onTargetFound(this.mTargetView, mRecyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(mRecyclerView);
                    this.stop();
                }
                else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (!this.mRunning) {
                return;
            }
            this.onSeekTargetStep(n, n2, mRecyclerView.mState, this.mRecyclingAction);
            final boolean hasJumpTarget = this.mRecyclingAction.hasJumpTarget();
            this.mRecyclingAction.runIfNecessary(mRecyclerView);
            if (!hasJumpTarget) {
                return;
            }
            if (this.mRunning) {
                this.mPendingInitialRun = true;
                mRecyclerView.mViewFlinger.postOnAnimation();
                return;
            }
            this.stop();
        }
        
        public View findViewByPosition(final int n) {
            return this.mRecyclerView.mLayout.findViewByPosition(n);
        }
        
        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }
        
        public int getChildPosition(final View view) {
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
        public void instantScrollToPosition(final int n) {
            this.mRecyclerView.scrollToPosition(n);
        }
        
        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }
        
        public boolean isRunning() {
            return this.mRunning;
        }
        
        protected void normalize(final PointF pointF) {
            final float n = (float)Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x /= n;
            pointF.y /= n;
        }
        
        protected void onChildAttachedToWindow(final View mTargetView) {
            if (this.getChildPosition(mTargetView) == this.getTargetPosition()) {
                this.mTargetView = mTargetView;
            }
        }
        
        protected abstract void onSeekTargetStep(final int p0, final int p1, final State p2, final Action p3);
        
        protected abstract void onStart();
        
        protected abstract void onStop();
        
        protected abstract void onTargetFound(final View p0, final State p1, final Action p2);
        
        public void setTargetPosition(final int mTargetPosition) {
            this.mTargetPosition = mTargetPosition;
        }
        
        void start(final RecyclerView mRecyclerView, final LayoutManager mLayoutManager) {
            this.mRecyclerView = mRecyclerView;
            this.mLayoutManager = mLayoutManager;
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
        
        public static class Action
        {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged;
            private int mConsecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;
            
            public Action(final int n, final int n2) {
                this(n, n2, Integer.MIN_VALUE, null);
            }
            
            public Action(final int n, final int n2, final int n3) {
                this(n, n2, n3, null);
            }
            
            public Action(final int mDx, final int mDy, final int mDuration, final Interpolator mInterpolator) {
                this.mJumpToPosition = -1;
                this.mChanged = false;
                this.mConsecutiveUpdates = 0;
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
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
                return this.mJumpToPosition >= 0;
            }
            
            public void jumpTo(final int mJumpToPosition) {
                this.mJumpToPosition = mJumpToPosition;
            }
            
            void runIfNecessary(final RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    final int mJumpToPosition = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(mJumpToPosition);
                    this.mChanged = false;
                    return;
                }
                if (this.mChanged) {
                    this.validate();
                    if (this.mInterpolator == null) {
                        if (this.mDuration == Integer.MIN_VALUE) {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                        }
                        else {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                        }
                    }
                    else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    }
                    ++this.mConsecutiveUpdates;
                    if (this.mConsecutiveUpdates > 10) {
                        Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.mChanged = false;
                    return;
                }
                this.mConsecutiveUpdates = 0;
            }
            
            public void setDuration(final int mDuration) {
                this.mChanged = true;
                this.mDuration = mDuration;
            }
            
            public void setDx(final int mDx) {
                this.mChanged = true;
                this.mDx = mDx;
            }
            
            public void setDy(final int mDy) {
                this.mChanged = true;
                this.mDy = mDy;
            }
            
            public void setInterpolator(final Interpolator mInterpolator) {
                this.mChanged = true;
                this.mInterpolator = mInterpolator;
            }
            
            public void update(final int mDx, final int mDy, final int mDuration, final Interpolator mInterpolator) {
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
                this.mChanged = true;
            }
        }
        
        public interface ScrollVectorProvider
        {
            PointF computeScrollVectorForPosition(final int p0);
        }
    }
    
    public static class State
    {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout;
        boolean mIsMeasuring;
        int mItemCount;
        int mLayoutStep;
        int mPreviousLayoutItemCount;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations;
        boolean mRunSimpleAnimations;
        boolean mStructureChanged;
        private int mTargetPosition;
        boolean mTrackOldChangeHolders;
        
        public State() {
            this.mTargetPosition = -1;
            this.mPreviousLayoutItemCount = 0;
            this.mDeletedInvisibleItemCountSincePreviousLayout = 0;
            this.mLayoutStep = 1;
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
            this.mRunSimpleAnimations = false;
            this.mRunPredictiveAnimations = false;
        }
        
        void assertLayoutStep(final int n) {
            if ((this.mLayoutStep & n) != 0x0) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Layout state should be one of ");
            sb.append(Integer.toBinaryString(n));
            sb.append(" but it is ");
            sb.append(Integer.toBinaryString(this.mLayoutStep));
            throw new IllegalStateException(sb.toString());
        }
        
        public boolean didStructureChange() {
            return this.mStructureChanged;
        }
        
        public <T> T get(final int n) {
            final SparseArray<Object> mData = this.mData;
            if (mData == null) {
                return null;
            }
            return (T)mData.get(n);
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
            return this.mTargetPosition != -1;
        }
        
        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }
        
        public boolean isPreLayout() {
            return this.mInPreLayout;
        }
        
        void prepareForNestedPrefetch(final Adapter adapter) {
            this.mLayoutStep = 1;
            this.mItemCount = adapter.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }
        
        public void put(final int n, final Object o) {
            if (this.mData == null) {
                this.mData = (SparseArray<Object>)new SparseArray();
            }
            this.mData.put(n, o);
        }
        
        public void remove(final int n) {
            final SparseArray<Object> mData = this.mData;
            if (mData == null) {
                return;
            }
            mData.remove(n);
        }
        
        State reset() {
            this.mTargetPosition = -1;
            final SparseArray<Object> mData = this.mData;
            if (mData != null) {
                mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mIsMeasuring = false;
            return this;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("State{mTargetPosition=");
            sb.append(this.mTargetPosition);
            sb.append(", mData=");
            sb.append(this.mData);
            sb.append(", mItemCount=");
            sb.append(this.mItemCount);
            sb.append(", mPreviousLayoutItemCount=");
            sb.append(this.mPreviousLayoutItemCount);
            sb.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
            sb.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
            sb.append(", mStructureChanged=");
            sb.append(this.mStructureChanged);
            sb.append(", mInPreLayout=");
            sb.append(this.mInPreLayout);
            sb.append(", mRunSimpleAnimations=");
            sb.append(this.mRunSimpleAnimations);
            sb.append(", mRunPredictiveAnimations=");
            sb.append(this.mRunPredictiveAnimations);
            sb.append('}');
            return sb.toString();
        }
        
        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }
        
        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        @interface LayoutState {
        }
    }
    
    public abstract static class ViewCacheExtension
    {
        public abstract View getViewForPositionAndType(final Recycler p0, final int p1, final int p2);
    }
    
    class ViewFlinger implements Runnable
    {
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
            final int abs = Math.abs(n);
            final int abs2 = Math.abs(n2);
            final boolean b = abs > abs2;
            n3 = (int)Math.sqrt(n3 * n3 + n4 * n4);
            n2 = (int)Math.sqrt(n * n + n2 * n2);
            if (b) {
                n = RecyclerView.this.getWidth();
            }
            else {
                n = RecyclerView.this.getHeight();
            }
            n4 = n / 2;
            final float min = Math.min(1.0f, n2 * 1.0f / n);
            final float n5 = (float)n4;
            final float n6 = (float)n4;
            final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
            if (n3 > 0) {
                n = Math.round(Math.abs((n5 + n6 * distanceInfluenceForSnapDuration) / n3) * 1000.0f) * 4;
            }
            else {
                if (b) {
                    n2 = abs;
                }
                else {
                    n2 = abs2;
                }
                n = (int)((n2 / (float)n + 1.0f) * 300.0f);
            }
            return Math.min(n, 2000);
        }
        
        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }
        
        private float distanceInfluenceForSnapDuration(final float n) {
            return (float)Math.sin((n - 0.5f) * 0.47123894f);
        }
        
        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                this.postOnAnimation();
            }
        }
        
        public void fling(final int n, final int n2) {
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
            final OverScroller mScroller = this.mScroller;
            final SmoothScroller mSmoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
            if (mScroller.computeScrollOffset()) {
                final int[] access$500 = RecyclerView.this.mScrollConsumed;
                final int currX = mScroller.getCurrX();
                final int currY = mScroller.getCurrY();
                int n = currX - this.mLastFlingX;
                int n2 = currY - this.mLastFlingY;
                int n3 = 0;
                int scrollHorizontallyBy = 0;
                int scrollVerticallyBy = 0;
                this.mLastFlingX = currX;
                this.mLastFlingY = currY;
                final int n4 = 0;
                int n5 = 0;
                if (RecyclerView.this.dispatchNestedPreScroll(n, n2, access$500, null, 1)) {
                    n -= access$500[0];
                    n2 -= access$500[1];
                }
                int n7;
                int n8;
                int n9;
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.eatRequestLayout();
                    RecyclerView.this.onEnterLayoutOrScroll();
                    TraceCompat.beginSection("RV Scroll");
                    final RecyclerView this$0 = RecyclerView.this;
                    this$0.fillRemainingScrollValues(this$0.mState);
                    int n6;
                    if (n != 0) {
                        scrollHorizontallyBy = RecyclerView.this.mLayout.scrollHorizontallyBy(n, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n6 = n - scrollHorizontallyBy;
                    }
                    else {
                        n6 = n4;
                    }
                    if (n2 != 0) {
                        scrollVerticallyBy = RecyclerView.this.mLayout.scrollVerticallyBy(n2, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n5 = n2 - scrollVerticallyBy;
                    }
                    TraceCompat.endSection();
                    RecyclerView.this.repositionShadowingViews();
                    RecyclerView.this.onExitLayoutOrScroll();
                    RecyclerView.this.resumeRequestLayout(false);
                    if (mSmoothScroller != null && !mSmoothScroller.isPendingInitialRun()) {
                        if (mSmoothScroller.isRunning()) {
                            final int itemCount = RecyclerView.this.mState.getItemCount();
                            if (itemCount == 0) {
                                mSmoothScroller.stop();
                            }
                            else if (mSmoothScroller.getTargetPosition() >= itemCount) {
                                mSmoothScroller.setTargetPosition(itemCount - 1);
                                mSmoothScroller.onAnimation(n - n6, n2 - n5);
                            }
                            else {
                                mSmoothScroller.onAnimation(n - n6, n2 - n5);
                            }
                        }
                    }
                    n7 = n5;
                    n8 = scrollVerticallyBy;
                    n9 = n6;
                    n3 = scrollHorizontallyBy;
                }
                else {
                    n8 = 0;
                    n9 = 0;
                    n7 = 0;
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(n, n2);
                }
                if (!RecyclerView.this.dispatchNestedScroll(n3, n8, n9, n7, null, 1)) {
                    if (n9 != 0 || n7 != 0) {
                        final int n10 = (int)mScroller.getCurrVelocity();
                        int n12;
                        if (n9 != currX) {
                            int n11;
                            if (n9 < 0) {
                                n11 = -n10;
                            }
                            else if (n9 > 0) {
                                n11 = n10;
                            }
                            else {
                                n11 = 0;
                            }
                            n12 = n11;
                        }
                        else {
                            n12 = 0;
                        }
                        int n13;
                        if (n7 != currY) {
                            if (n7 < 0) {
                                n13 = -n10;
                            }
                            else if (n7 > 0) {
                                n13 = n10;
                            }
                            else {
                                n13 = 0;
                            }
                        }
                        else {
                            n13 = 0;
                        }
                        if (RecyclerView.this.getOverScrollMode() != 2) {
                            RecyclerView.this.absorbGlows(n12, n13);
                        }
                        if (n12 != 0 || n9 == currX || mScroller.getFinalX() == 0) {
                            if (n13 != 0 || n7 == currY || mScroller.getFinalY() == 0) {
                                mScroller.abortAnimation();
                            }
                        }
                    }
                }
                if (n3 != 0 || n8 != 0) {
                    RecyclerView.this.dispatchOnScrolled(n3, n8);
                }
                if (!RecyclerView.access$700(RecyclerView.this)) {
                    RecyclerView.this.invalidate();
                }
                final boolean b = n2 != 0 && RecyclerView.this.mLayout.canScrollVertically() && n8 == n2;
                final boolean b2 = n != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && n3 == n;
                final boolean b3 = (n == 0 && n2 == 0) || b2 || b;
                if (!mScroller.isFinished() && (b3 || RecyclerView.this.hasNestedScrollingParent(1))) {
                    this.postOnAnimation();
                    if (RecyclerView.this.mGapWorker != null) {
                        RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, n, n2);
                    }
                }
                else {
                    RecyclerView.this.setScrollState(0);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                    }
                    RecyclerView.this.stopNestedScroll(1);
                }
            }
            if (mSmoothScroller != null) {
                if (mSmoothScroller.isPendingInitialRun()) {
                    mSmoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    mSmoothScroller.stop();
                }
            }
            this.enableRunOnAnimationRequests();
        }
        
        public void smoothScrollBy(final int n, final int n2) {
            this.smoothScrollBy(n, n2, 0, 0);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3) {
            this.smoothScrollBy(n, n2, n3, RecyclerView.sQuinticInterpolator);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final int n4) {
            this.smoothScrollBy(n, n2, this.computeScrollDuration(n, n2, n3, n4));
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final Interpolator mInterpolator) {
            if (this.mInterpolator != mInterpolator) {
                this.mInterpolator = mInterpolator;
                this.mScroller = new OverScroller(RecyclerView.this.getContext(), mInterpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, n, n2, n3);
            if (Build$VERSION.SDK_INT < 23) {
                this.mScroller.computeScrollOffset();
            }
            this.postOnAnimation();
        }
        
        public void smoothScrollBy(final int n, final int n2, Interpolator sQuinticInterpolator) {
            final int computeScrollDuration = this.computeScrollDuration(n, n2, 0, 0);
            if (sQuinticInterpolator == null) {
                sQuinticInterpolator = RecyclerView.sQuinticInterpolator;
            }
            this.smoothScrollBy(n, n2, computeScrollDuration, sQuinticInterpolator);
        }
        
        public void stop() {
            RecyclerView.this.removeCallbacks((Runnable)this);
            this.mScroller.abortAnimation();
        }
    }
    
    public abstract static class ViewHolder
    {
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
        private static final List<Object> FULLUPDATE_PAYLOADS;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap;
        private int mIsRecyclableCount;
        long mItemId;
        int mItemViewType;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads;
        @VisibleForTesting
        int mPendingAccessibilityState;
        int mPosition;
        int mPreLayoutPosition;
        private Recycler mScrapContainer;
        ViewHolder mShadowedHolder;
        ViewHolder mShadowingHolder;
        List<Object> mUnmodifiedPayloads;
        private int mWasImportantForAccessibilityBeforeHidden;
        
        static {
            FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        }
        
        public ViewHolder(final View itemView) {
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mItemViewType = -1;
            this.mPreLayoutPosition = -1;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.mPayloads = null;
            this.mUnmodifiedPayloads = null;
            this.mIsRecyclableCount = 0;
            this.mScrapContainer = null;
            this.mInChangeScrap = false;
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            if (itemView != null) {
                this.itemView = itemView;
                return;
            }
            throw new IllegalArgumentException("itemView may not be null");
        }
        
        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList<Object>();
                this.mUnmodifiedPayloads = Collections.unmodifiableList((List<?>)this.mPayloads);
            }
        }
        
        private boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 0x10) == 0x0 && ViewCompat.hasTransientState(this.itemView);
        }
        
        private void onEnteredHiddenState(final RecyclerView recyclerView) {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            recyclerView.setChildImportantForAccessibilityInternal(this, 4);
        }
        
        private void onLeftHiddenState(final RecyclerView recyclerView) {
            recyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }
        
        private boolean shouldBeKeptAsChild() {
            return (this.mFlags & 0x10) != 0x0;
        }
        
        void addChangePayload(final Object o) {
            if (o == null) {
                this.addFlags(1024);
                return;
            }
            if ((0x400 & this.mFlags) == 0x0) {
                this.createPayloadsIfNeeded();
                this.mPayloads.add(o);
            }
        }
        
        void addFlags(final int n) {
            this.mFlags |= n;
        }
        
        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }
        
        void clearPayload() {
            final List<Object> mPayloads = this.mPayloads;
            if (mPayloads != null) {
                mPayloads.clear();
            }
            this.mFlags &= 0xFFFFFBFF;
        }
        
        void clearReturnedFromScrapFlag() {
            this.mFlags &= 0xFFFFFFDF;
        }
        
        void clearTmpDetachFlag() {
            this.mFlags &= 0xFFFFFEFF;
        }
        
        void flagRemovedAndOffsetPosition(final int mPosition, final int n, final boolean b) {
            this.addFlags(8);
            this.offsetPosition(n, b);
            this.mPosition = mPosition;
        }
        
        public final int getAdapterPosition() {
            final RecyclerView mOwnerRecyclerView = this.mOwnerRecyclerView;
            if (mOwnerRecyclerView == null) {
                return -1;
            }
            return mOwnerRecyclerView.getAdapterPositionFor(this);
        }
        
        public final long getItemId() {
            return this.mItemId;
        }
        
        public final int getItemViewType() {
            return this.mItemViewType;
        }
        
        public final int getLayoutPosition() {
            int n;
            if ((n = this.mPreLayoutPosition) == -1) {
                n = this.mPosition;
            }
            return n;
        }
        
        public final int getOldPosition() {
            return this.mOldPosition;
        }
        
        @Deprecated
        public final int getPosition() {
            int n;
            if ((n = this.mPreLayoutPosition) == -1) {
                n = this.mPosition;
            }
            return n;
        }
        
        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 0x400) != 0x0) {
                return ViewHolder.FULLUPDATE_PAYLOADS;
            }
            final List<Object> mPayloads = this.mPayloads;
            if (mPayloads != null && mPayloads.size()) {
                return this.mUnmodifiedPayloads;
            }
            return ViewHolder.FULLUPDATE_PAYLOADS;
        }
        
        boolean hasAnyOfTheFlags(final int n) {
            return (this.mFlags & n) != 0x0;
        }
        
        boolean isAdapterPositionUnknown() {
            return (this.mFlags & 0x200) != 0x0 || this.isInvalid();
        }
        
        boolean isBound() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        boolean isInvalid() {
            return (this.mFlags & 0x4) != 0x0;
        }
        
        public final boolean isRecyclable() {
            return (this.mFlags & 0x10) == 0x0 && !ViewCompat.hasTransientState(this.itemView);
        }
        
        boolean isRemoved() {
            return (this.mFlags & 0x8) != 0x0;
        }
        
        boolean isScrap() {
            return this.mScrapContainer != null;
        }
        
        boolean isTmpDetached() {
            return (this.mFlags & 0x100) != 0x0;
        }
        
        boolean isUpdated() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        boolean needsUpdate() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        void offsetPosition(final int n, final boolean b) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (b) {
                this.mPreLayoutPosition += n;
            }
            this.mPosition += n;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
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
            }
        }
        
        void setFlags(final int n, final int n2) {
            this.mFlags = ((this.mFlags & ~n2) | (n & n2));
        }
        
        public final void setIsRecyclable(final boolean b) {
            int mIsRecyclableCount;
            if (b) {
                mIsRecyclableCount = this.mIsRecyclableCount - 1;
            }
            else {
                mIsRecyclableCount = this.mIsRecyclableCount + 1;
            }
            this.mIsRecyclableCount = mIsRecyclableCount;
            final int mIsRecyclableCount2 = this.mIsRecyclableCount;
            if (mIsRecyclableCount2 < 0) {
                this.mIsRecyclableCount = 0;
                final StringBuilder sb = new StringBuilder();
                sb.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
                sb.append(this);
                Log.e("View", sb.toString());
                return;
            }
            if (!b && mIsRecyclableCount2 == 1) {
                this.mFlags |= 0x10;
                return;
            }
            if (b && this.mIsRecyclableCount == 0) {
                this.mFlags &= 0xFFFFFFEF;
            }
        }
        
        void setScrapContainer(final Recycler mScrapContainer, final boolean mInChangeScrap) {
            this.mScrapContainer = mScrapContainer;
            this.mInChangeScrap = mInChangeScrap;
        }
        
        boolean shouldIgnore() {
            return (this.mFlags & 0x80) != 0x0;
        }
        
        void stopIgnoring() {
            this.mFlags &= 0xFFFFFF7F;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ViewHolder{");
            sb.append(Integer.toHexString(this.hashCode()));
            sb.append(" position=");
            sb.append(this.mPosition);
            sb.append(" id=");
            sb.append(this.mItemId);
            sb.append(", oldPos=");
            sb.append(this.mOldPosition);
            sb.append(", pLpos:");
            sb.append(this.mPreLayoutPosition);
            final StringBuilder sb2 = new StringBuilder(sb.toString());
            if (this.isScrap()) {
                sb2.append(" scrap ");
                String s;
                if (this.mInChangeScrap) {
                    s = "[changeScrap]";
                }
                else {
                    s = "[attachedScrap]";
                }
                sb2.append(s);
            }
            if (this.isInvalid()) {
                sb2.append(" invalid");
            }
            if (!this.isBound()) {
                sb2.append(" unbound");
            }
            if (this.needsUpdate()) {
                sb2.append(" update");
            }
            if (this.isRemoved()) {
                sb2.append(" removed");
            }
            if (this.shouldIgnore()) {
                sb2.append(" ignored");
            }
            if (this.isTmpDetached()) {
                sb2.append(" tmpDetached");
            }
            if (!this.isRecyclable()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(" not recyclable(");
                sb3.append(this.mIsRecyclableCount);
                sb3.append(")");
                sb2.append(sb3.toString());
            }
            if (this.isAdapterPositionUnknown()) {
                sb2.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb2.append(" no parent");
            }
            sb2.append("}");
            return sb2.toString();
        }
        
        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }
        
        boolean wasReturnedFromScrap() {
            return (this.mFlags & 0x20) != 0x0;
        }
    }
}
