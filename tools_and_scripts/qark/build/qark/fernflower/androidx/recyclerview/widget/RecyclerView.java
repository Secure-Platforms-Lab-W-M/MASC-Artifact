package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
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
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.R.attr;
import androidx.recyclerview.R.dimen;
import androidx.recyclerview.R.styleable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
   static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
   static final boolean ALLOW_THREAD_GAP_WORK;
   static final boolean DEBUG = false;
   static final int DEFAULT_ORIENTATION = 1;
   static final boolean DISPATCH_TEMP_DETACH = false;
   private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
   static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
   static final long FOREVER_NS = Long.MAX_VALUE;
   public static final int HORIZONTAL = 0;
   private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
   private static final int INVALID_POINTER = -1;
   public static final int INVALID_TYPE = -1;
   private static final Class[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
   static final int MAX_SCROLL_DURATION = 2000;
   private static final int[] NESTED_SCROLLING_ATTRS = new int[]{16843830};
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
   public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
   static final boolean VERBOSE_TRACING = false;
   public static final int VERTICAL = 1;
   static final Interpolator sQuinticInterpolator;
   RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
   private final AccessibilityManager mAccessibilityManager;
   RecyclerView.Adapter mAdapter;
   AdapterHelper mAdapterHelper;
   boolean mAdapterUpdateDuringMeasure;
   private EdgeEffect mBottomGlow;
   private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback;
   ChildHelper mChildHelper;
   boolean mClipToPadding;
   boolean mDataSetHasChangedAfterLayout;
   boolean mDispatchItemsChangedEvent;
   private int mDispatchScrollCounter;
   private int mEatenAccessibilityChangeFlags;
   private RecyclerView.EdgeEffectFactory mEdgeEffectFactory;
   boolean mEnableFastScroller;
   boolean mFirstLayoutComplete;
   GapWorker mGapWorker;
   boolean mHasFixedSize;
   private boolean mIgnoreMotionEventTillDown;
   private int mInitialTouchX;
   private int mInitialTouchY;
   private int mInterceptRequestLayoutDepth;
   private RecyclerView.OnItemTouchListener mInterceptingOnItemTouchListener;
   boolean mIsAttached;
   RecyclerView.ItemAnimator mItemAnimator;
   private RecyclerView.ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
   private Runnable mItemAnimatorRunner;
   final ArrayList mItemDecorations;
   boolean mItemsAddedOrRemoved;
   boolean mItemsChanged;
   private int mLastTouchX;
   private int mLastTouchY;
   RecyclerView.LayoutManager mLayout;
   private int mLayoutOrScrollCounter;
   boolean mLayoutSuppressed;
   boolean mLayoutWasDefered;
   private EdgeEffect mLeftGlow;
   private final int mMaxFlingVelocity;
   private final int mMinFlingVelocity;
   private final int[] mMinMaxLayoutPositions;
   private final int[] mNestedOffsets;
   private final RecyclerView.RecyclerViewDataObserver mObserver;
   private List mOnChildAttachStateListeners;
   private RecyclerView.OnFlingListener mOnFlingListener;
   private final ArrayList mOnItemTouchListeners;
   final List mPendingAccessibilityImportanceChange;
   private RecyclerView.SavedState mPendingSavedState;
   boolean mPostedAnimatorRunner;
   GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
   private boolean mPreserveFocusAfterLayout;
   final RecyclerView.Recycler mRecycler;
   RecyclerView.RecyclerListener mRecyclerListener;
   final int[] mReusableIntPair;
   private EdgeEffect mRightGlow;
   private float mScaledHorizontalScrollFactor;
   private float mScaledVerticalScrollFactor;
   private RecyclerView.OnScrollListener mScrollListener;
   private List mScrollListeners;
   private final int[] mScrollOffset;
   private int mScrollPointerId;
   private int mScrollState;
   private NestedScrollingChildHelper mScrollingChildHelper;
   final RecyclerView.State mState;
   final Rect mTempRect;
   private final Rect mTempRect2;
   final RectF mTempRectF;
   private EdgeEffect mTopGlow;
   private int mTouchSlop;
   final Runnable mUpdateChildViewsRunnable;
   private VelocityTracker mVelocityTracker;
   final RecyclerView.ViewFlinger mViewFlinger;
   private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
   final ViewInfoStore mViewInfoStore;

   static {
      boolean var0;
      if (VERSION.SDK_INT != 18 && VERSION.SDK_INT != 19 && VERSION.SDK_INT != 20) {
         var0 = false;
      } else {
         var0 = true;
      }

      FORCE_INVALIDATE_DISPLAY_LIST = var0;
      if (VERSION.SDK_INT >= 23) {
         var0 = true;
      } else {
         var0 = false;
      }

      ALLOW_SIZE_IN_UNSPECIFIED_SPEC = var0;
      if (VERSION.SDK_INT >= 16) {
         var0 = true;
      } else {
         var0 = false;
      }

      POST_UPDATES_ON_ANIMATION = var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      ALLOW_THREAD_GAP_WORK = var0;
      if (VERSION.SDK_INT <= 15) {
         var0 = true;
      } else {
         var0 = false;
      }

      FORCE_ABS_FOCUS_SEARCH_DIRECTION = var0;
      if (VERSION.SDK_INT <= 15) {
         var0 = true;
      } else {
         var0 = false;
      }

      IGNORE_DETACHED_FOCUSED_CHILD = var0;
      LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
      sQuinticInterpolator = new Interpolator() {
         public float getInterpolation(float var1) {
            --var1;
            return var1 * var1 * var1 * var1 * var1 + 1.0F;
         }
      };
   }

   public RecyclerView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public RecyclerView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.recyclerViewStyle);
   }

   public RecyclerView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mObserver = new RecyclerView.RecyclerViewDataObserver();
      this.mRecycler = new RecyclerView.Recycler();
      this.mViewInfoStore = new ViewInfoStore();
      this.mUpdateChildViewsRunnable = new Runnable() {
         public void run() {
            if (RecyclerView.this.mFirstLayoutComplete) {
               if (!RecyclerView.this.isLayoutRequested()) {
                  if (!RecyclerView.this.mIsAttached) {
                     RecyclerView.this.requestLayout();
                  } else if (RecyclerView.this.mLayoutSuppressed) {
                     RecyclerView.this.mLayoutWasDefered = true;
                  } else {
                     RecyclerView.this.consumePendingUpdateOperations();
                  }
               }
            }
         }
      };
      this.mTempRect = new Rect();
      this.mTempRect2 = new Rect();
      this.mTempRectF = new RectF();
      this.mItemDecorations = new ArrayList();
      this.mOnItemTouchListeners = new ArrayList();
      this.mInterceptRequestLayoutDepth = 0;
      this.mDataSetHasChangedAfterLayout = false;
      this.mDispatchItemsChangedEvent = false;
      this.mLayoutOrScrollCounter = 0;
      this.mDispatchScrollCounter = 0;
      this.mEdgeEffectFactory = new RecyclerView.EdgeEffectFactory();
      this.mItemAnimator = new DefaultItemAnimator();
      this.mScrollState = 0;
      this.mScrollPointerId = -1;
      this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
      this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
      this.mPreserveFocusAfterLayout = true;
      this.mViewFlinger = new RecyclerView.ViewFlinger();
      GapWorker.LayoutPrefetchRegistryImpl var5;
      if (ALLOW_THREAD_GAP_WORK) {
         var5 = new GapWorker.LayoutPrefetchRegistryImpl();
      } else {
         var5 = null;
      }

      this.mPrefetchRegistry = var5;
      this.mState = new RecyclerView.State();
      this.mItemsAddedOrRemoved = false;
      this.mItemsChanged = false;
      this.mItemAnimatorListener = new RecyclerView.ItemAnimatorRestoreListener();
      this.mPostedAnimatorRunner = false;
      this.mMinMaxLayoutPositions = new int[2];
      this.mScrollOffset = new int[2];
      this.mNestedOffsets = new int[2];
      this.mReusableIntPair = new int[2];
      this.mPendingAccessibilityImportanceChange = new ArrayList();
      this.mItemAnimatorRunner = new Runnable() {
         public void run() {
            if (RecyclerView.this.mItemAnimator != null) {
               RecyclerView.this.mItemAnimator.runPendingAnimations();
            }

            RecyclerView.this.mPostedAnimatorRunner = false;
         }
      };
      this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
         public void processAppeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            RecyclerView.this.animateAppearance(var1, var2, var3);
         }

         public void processDisappeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            RecyclerView.this.mRecycler.unscrapView(var1);
            RecyclerView.this.animateDisappearance(var1, var2, var3);
         }

         public void processPersistent(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            var1.setIsRecyclable(false);
            if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
               if (RecyclerView.this.mItemAnimator.animateChange(var1, var1, var2, var3)) {
                  RecyclerView.this.postAnimationRunner();
                  return;
               }
            } else if (RecyclerView.this.mItemAnimator.animatePersistence(var1, var2, var3)) {
               RecyclerView.this.postAnimationRunner();
            }

         }

         public void unused(RecyclerView.ViewHolder var1) {
            RecyclerView.this.mLayout.removeAndRecycleView(var1.itemView, RecyclerView.this.mRecycler);
         }
      };
      this.setScrollContainer(true);
      this.setFocusableInTouchMode(true);
      ViewConfiguration var7 = ViewConfiguration.get(var1);
      this.mTouchSlop = var7.getScaledTouchSlop();
      this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(var7, var1);
      this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(var7, var1);
      this.mMinFlingVelocity = var7.getScaledMinimumFlingVelocity();
      this.mMaxFlingVelocity = var7.getScaledMaximumFlingVelocity();
      boolean var4;
      if (this.getOverScrollMode() == 2) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.setWillNotDraw(var4);
      this.mItemAnimator.setListener(this.mItemAnimatorListener);
      this.initAdapterManager();
      this.initChildrenHelper();
      this.initAutofill();
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
      this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
      TypedArray var8 = var1.obtainStyledAttributes(var2, styleable.RecyclerView, var3, 0);
      if (VERSION.SDK_INT >= 29) {
         this.saveAttributeDataForStyleable(var1, styleable.RecyclerView, var2, var8, var3, 0);
      }

      String var6 = var8.getString(styleable.RecyclerView_layoutManager);
      if (var8.getInt(styleable.RecyclerView_android_descendantFocusability, -1) == -1) {
         this.setDescendantFocusability(262144);
      }

      this.mClipToPadding = var8.getBoolean(styleable.RecyclerView_android_clipToPadding, true);
      var4 = var8.getBoolean(styleable.RecyclerView_fastScrollEnabled, false);
      this.mEnableFastScroller = var4;
      if (var4) {
         this.initFastScroller((StateListDrawable)var8.getDrawable(styleable.RecyclerView_fastScrollVerticalThumbDrawable), var8.getDrawable(styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)var8.getDrawable(styleable.RecyclerView_fastScrollHorizontalThumbDrawable), var8.getDrawable(styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
      }

      var8.recycle();
      this.createLayoutManager(var1, var6, var2, var3, 0);
      var4 = true;
      if (VERSION.SDK_INT >= 21) {
         var8 = var1.obtainStyledAttributes(var2, NESTED_SCROLLING_ATTRS, var3, 0);
         if (VERSION.SDK_INT >= 29) {
            this.saveAttributeDataForStyleable(var1, NESTED_SCROLLING_ATTRS, var2, var8, var3, 0);
         }

         var4 = var8.getBoolean(0, true);
         var8.recycle();
      }

      this.setNestedScrollingEnabled(var4);
   }

   private void addAnimatingView(RecyclerView.ViewHolder var1) {
      View var3 = var1.itemView;
      boolean var2;
      if (var3.getParent() == this) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mRecycler.unscrapView(this.getChildViewHolder(var3));
      if (var1.isTmpDetached()) {
         this.mChildHelper.attachViewToParent(var3, -1, var3.getLayoutParams(), true);
      } else if (!var2) {
         this.mChildHelper.addView(var3, true);
      } else {
         this.mChildHelper.hide(var3);
      }
   }

   private void animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, RecyclerView.ItemAnimator.ItemHolderInfo var3, RecyclerView.ItemAnimator.ItemHolderInfo var4, boolean var5, boolean var6) {
      var1.setIsRecyclable(false);
      if (var5) {
         this.addAnimatingView(var1);
      }

      if (var1 != var2) {
         if (var6) {
            this.addAnimatingView(var2);
         }

         var1.mShadowedHolder = var2;
         this.addAnimatingView(var1);
         this.mRecycler.unscrapView(var1);
         var2.setIsRecyclable(false);
         var2.mShadowingHolder = var1;
      }

      if (this.mItemAnimator.animateChange(var1, var2, var3, var4)) {
         this.postAnimationRunner();
      }

   }

   private void cancelScroll() {
      this.resetScroll();
      this.setScrollState(0);
   }

   static void clearNestedRecyclerViewIfNotNested(RecyclerView.ViewHolder var0) {
      if (var0.mNestedRecyclerView != null) {
         View var1 = (View)var0.mNestedRecyclerView.get();

         while(var1 != null) {
            if (var1 == var0.itemView) {
               return;
            }

            ViewParent var2 = var1.getParent();
            if (var2 instanceof View) {
               var1 = (View)var2;
            } else {
               var1 = null;
            }
         }

         var0.mNestedRecyclerView = null;
      }

   }

   private void createLayoutManager(Context param1, String param2, AttributeSet param3, int param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   private boolean didChildRangeChange(int var1, int var2) {
      this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
      int[] var4 = this.mMinMaxLayoutPositions;
      boolean var3 = false;
      if (var4[0] != var1 || var4[1] != var2) {
         var3 = true;
      }

      return var3;
   }

   private void dispatchContentChangedIfNecessary() {
      int var1 = this.mEatenAccessibilityChangeFlags;
      this.mEatenAccessibilityChangeFlags = 0;
      if (var1 != 0 && this.isAccessibilityEnabled()) {
         AccessibilityEvent var2 = AccessibilityEvent.obtain();
         var2.setEventType(2048);
         AccessibilityEventCompat.setContentChangeTypes(var2, var1);
         this.sendAccessibilityEventUnchecked(var2);
      }

   }

   private void dispatchLayoutStep1() {
      RecyclerView.State var7 = this.mState;
      boolean var4 = true;
      var7.assertLayoutStep(1);
      this.fillRemainingScrollValues(this.mState);
      this.mState.mIsMeasuring = false;
      this.startInterceptRequestLayout();
      this.mViewInfoStore.clear();
      this.onEnterLayoutOrScroll();
      this.processAdapterUpdatesAndSetAnimationFlags();
      this.saveFocusInfo();
      var7 = this.mState;
      if (!var7.mRunSimpleAnimations || !this.mItemsChanged) {
         var4 = false;
      }

      var7.mTrackOldChangeHolders = var4;
      this.mItemsChanged = false;
      this.mItemsAddedOrRemoved = false;
      var7 = this.mState;
      var7.mInPreLayout = var7.mRunPredictiveAnimations;
      this.mState.mItemCount = this.mAdapter.getItemCount();
      this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
      int var1;
      int var2;
      RecyclerView.ItemAnimator.ItemHolderInfo var8;
      RecyclerView.ViewHolder var9;
      if (this.mState.mRunSimpleAnimations) {
         var2 = this.mChildHelper.getChildCount();

         for(var1 = 0; var1 < var2; ++var1) {
            var9 = getChildViewHolderInt(this.mChildHelper.getChildAt(var1));
            if (!var9.shouldIgnore() && (!var9.isInvalid() || this.mAdapter.hasStableIds())) {
               var8 = this.mItemAnimator.recordPreLayoutInformation(this.mState, var9, RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var9), var9.getUnmodifiedPayloads());
               this.mViewInfoStore.addToPreLayout(var9, var8);
               if (this.mState.mTrackOldChangeHolders && var9.isUpdated() && !var9.isRemoved() && !var9.shouldIgnore() && !var9.isInvalid()) {
                  long var5 = this.getChangedHolderKey(var9);
                  this.mViewInfoStore.addToOldChangeHolders(var5, var9);
               }
            }
         }
      }

      if (this.mState.mRunPredictiveAnimations) {
         this.saveOldPositions();
         var4 = this.mState.mStructureChanged;
         this.mState.mStructureChanged = false;
         this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
         this.mState.mStructureChanged = var4;

         for(var1 = 0; var1 < this.mChildHelper.getChildCount(); ++var1) {
            var9 = getChildViewHolderInt(this.mChildHelper.getChildAt(var1));
            if (!var9.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(var9)) {
               int var3 = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var9);
               var4 = var9.hasAnyOfTheFlags(8192);
               var2 = var3;
               if (!var4) {
                  var2 = var3 | 4096;
               }

               var8 = this.mItemAnimator.recordPreLayoutInformation(this.mState, var9, var2, var9.getUnmodifiedPayloads());
               if (var4) {
                  this.recordAnimationInfoIfBouncedHiddenView(var9, var8);
               } else {
                  this.mViewInfoStore.addToAppearedInPreLayoutHolders(var9, var8);
               }
            }
         }

         this.clearOldPositions();
      } else {
         this.clearOldPositions();
      }

      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      this.mState.mLayoutStep = 2;
   }

   private void dispatchLayoutStep2() {
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      this.mState.assertLayoutStep(6);
      this.mAdapterHelper.consumeUpdatesInOnePass();
      this.mState.mItemCount = this.mAdapter.getItemCount();
      this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
      this.mState.mInPreLayout = false;
      this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
      this.mState.mStructureChanged = false;
      this.mPendingSavedState = null;
      RecyclerView.State var2 = this.mState;
      boolean var1;
      if (var2.mRunSimpleAnimations && this.mItemAnimator != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      var2.mRunSimpleAnimations = var1;
      this.mState.mLayoutStep = 4;
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
   }

   private void dispatchLayoutStep3() {
      this.mState.assertLayoutStep(4);
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      this.mState.mLayoutStep = 1;
      if (this.mState.mRunSimpleAnimations) {
         for(int var1 = this.mChildHelper.getChildCount() - 1; var1 >= 0; --var1) {
            RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getChildAt(var1));
            if (!var6.shouldIgnore()) {
               long var2 = this.getChangedHolderKey(var6);
               RecyclerView.ItemAnimator.ItemHolderInfo var9 = this.mItemAnimator.recordPostLayoutInformation(this.mState, var6);
               RecyclerView.ViewHolder var7 = this.mViewInfoStore.getFromOldChangeHolders(var2);
               if (var7 != null && !var7.shouldIgnore()) {
                  boolean var4 = this.mViewInfoStore.isDisappearing(var7);
                  boolean var5 = this.mViewInfoStore.isDisappearing(var6);
                  if (var4 && var7 == var6) {
                     this.mViewInfoStore.addToPostLayout(var6, var9);
                  } else {
                     RecyclerView.ItemAnimator.ItemHolderInfo var8 = this.mViewInfoStore.popFromPreLayout(var7);
                     this.mViewInfoStore.addToPostLayout(var6, var9);
                     var9 = this.mViewInfoStore.popFromPostLayout(var6);
                     if (var8 == null) {
                        this.handleMissingPreInfoForChangeError(var2, var6, var7);
                     } else {
                        this.animateChange(var7, var6, var8, var9, var4, var5);
                     }
                  }
               } else {
                  this.mViewInfoStore.addToPostLayout(var6, var9);
               }
            }
         }

         this.mViewInfoStore.process(this.mViewInfoProcessCallback);
      }

      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      RecyclerView.State var10 = this.mState;
      var10.mPreviousLayoutItemCount = var10.mItemCount;
      this.mDataSetHasChangedAfterLayout = false;
      this.mDispatchItemsChangedEvent = false;
      this.mState.mRunSimpleAnimations = false;
      this.mState.mRunPredictiveAnimations = false;
      this.mLayout.mRequestedSimpleAnimations = false;
      if (this.mRecycler.mChangedScrap != null) {
         this.mRecycler.mChangedScrap.clear();
      }

      if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
         this.mLayout.mPrefetchMaxCountObserved = 0;
         this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
         this.mRecycler.updateViewCacheSize();
      }

      this.mLayout.onLayoutCompleted(this.mState);
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      this.mViewInfoStore.clear();
      int[] var11 = this.mMinMaxLayoutPositions;
      if (this.didChildRangeChange(var11[0], var11[1])) {
         this.dispatchOnScrolled(0, 0);
      }

      this.recoverFocusFromState();
      this.resetFocusInfo();
   }

   private boolean dispatchToOnItemTouchListeners(MotionEvent var1) {
      RecyclerView.OnItemTouchListener var3 = this.mInterceptingOnItemTouchListener;
      if (var3 == null) {
         return var1.getAction() == 0 ? false : this.findInterceptingOnItemTouchListener(var1);
      } else {
         var3.onTouchEvent(this, var1);
         int var2 = var1.getAction();
         if (var2 == 3 || var2 == 1) {
            this.mInterceptingOnItemTouchListener = null;
         }

         return true;
      }
   }

   private boolean findInterceptingOnItemTouchListener(MotionEvent var1) {
      int var3 = var1.getAction();
      int var4 = this.mOnItemTouchListeners.size();

      for(int var2 = 0; var2 < var4; ++var2) {
         RecyclerView.OnItemTouchListener var5 = (RecyclerView.OnItemTouchListener)this.mOnItemTouchListeners.get(var2);
         if (var5.onInterceptTouchEvent(this, var1) && var3 != 3) {
            this.mInterceptingOnItemTouchListener = var5;
            return true;
         }
      }

      return false;
   }

   private void findMinMaxChildLayoutPositions(int[] var1) {
      int var8 = this.mChildHelper.getChildCount();
      if (var8 == 0) {
         var1[0] = -1;
         var1[1] = -1;
      } else {
         int var3 = Integer.MAX_VALUE;
         int var5 = Integer.MIN_VALUE;

         int var7;
         for(int var4 = 0; var4 < var8; var5 = var7) {
            RecyclerView.ViewHolder var9 = getChildViewHolderInt(this.mChildHelper.getChildAt(var4));
            if (var9.shouldIgnore()) {
               var7 = var5;
            } else {
               int var6 = var9.getLayoutPosition();
               int var2 = var3;
               if (var6 < var3) {
                  var2 = var6;
               }

               var3 = var2;
               var7 = var5;
               if (var6 > var5) {
                  var7 = var6;
                  var3 = var2;
               }
            }

            ++var4;
         }

         var1[0] = var3;
         var1[1] = var5;
      }
   }

   static RecyclerView findNestedRecyclerView(View var0) {
      if (!(var0 instanceof ViewGroup)) {
         return null;
      } else if (var0 instanceof RecyclerView) {
         return (RecyclerView)var0;
      } else {
         ViewGroup var4 = (ViewGroup)var0;
         int var2 = var4.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            RecyclerView var3 = findNestedRecyclerView(var4.getChildAt(var1));
            if (var3 != null) {
               return var3;
            }
         }

         return null;
      }
   }

   private View findNextViewToFocus() {
      int var1;
      if (this.mState.mFocusedItemPosition != -1) {
         var1 = this.mState.mFocusedItemPosition;
      } else {
         var1 = 0;
      }

      int var3 = this.mState.getItemCount();

      RecyclerView.ViewHolder var4;
      for(int var2 = var1; var2 < var3; ++var2) {
         var4 = this.findViewHolderForAdapterPosition(var2);
         if (var4 == null) {
            break;
         }

         if (var4.itemView.hasFocusable()) {
            return var4.itemView;
         }
      }

      for(var1 = Math.min(var3, var1) - 1; var1 >= 0; --var1) {
         var4 = this.findViewHolderForAdapterPosition(var1);
         if (var4 == null) {
            return null;
         }

         if (var4.itemView.hasFocusable()) {
            return var4.itemView;
         }
      }

      return null;
   }

   static RecyclerView.ViewHolder getChildViewHolderInt(View var0) {
      return var0 == null ? null : ((RecyclerView.LayoutParams)var0.getLayoutParams()).mViewHolder;
   }

   static void getDecoratedBoundsWithMarginsInt(View var0, Rect var1) {
      RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var0.getLayoutParams();
      Rect var3 = var2.mDecorInsets;
      var1.set(var0.getLeft() - var3.left - var2.leftMargin, var0.getTop() - var3.top - var2.topMargin, var0.getRight() + var3.right + var2.rightMargin, var0.getBottom() + var3.bottom + var2.bottomMargin);
   }

   private int getDeepestFocusedViewWithId(View var1) {
      int var2 = var1.getId();

      while(!var1.isFocused() && var1 instanceof ViewGroup && var1.hasFocus()) {
         var1 = ((ViewGroup)var1).getFocusedChild();
         if (var1.getId() != -1) {
            var2 = var1.getId();
         }
      }

      return var2;
   }

   private String getFullClassName(Context var1, String var2) {
      if (var2.charAt(0) == '.') {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getPackageName());
         var3.append(var2);
         return var3.toString();
      } else if (var2.contains(".")) {
         return var2;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(RecyclerView.class.getPackage().getName());
         var4.append('.');
         var4.append(var2);
         return var4.toString();
      }
   }

   private NestedScrollingChildHelper getScrollingChildHelper() {
      if (this.mScrollingChildHelper == null) {
         this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
      }

      return this.mScrollingChildHelper;
   }

   private void handleMissingPreInfoForChangeError(long var1, RecyclerView.ViewHolder var3, RecyclerView.ViewHolder var4) {
      int var6 = this.mChildHelper.getChildCount();

      for(int var5 = 0; var5 < var6; ++var5) {
         RecyclerView.ViewHolder var7 = getChildViewHolderInt(this.mChildHelper.getChildAt(var5));
         if (var7 != var3 && this.getChangedHolderKey(var7) == var1) {
            RecyclerView.Adapter var8 = this.mAdapter;
            StringBuilder var9;
            if (var8 != null && var8.hasStableIds()) {
               var9 = new StringBuilder();
               var9.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
               var9.append(var7);
               var9.append(" \n View Holder 2:");
               var9.append(var3);
               var9.append(this.exceptionLabel());
               throw new IllegalStateException(var9.toString());
            }

            var9 = new StringBuilder();
            var9.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
            var9.append(var7);
            var9.append(" \n View Holder 2:");
            var9.append(var3);
            var9.append(this.exceptionLabel());
            throw new IllegalStateException(var9.toString());
         }
      }

      StringBuilder var10 = new StringBuilder();
      var10.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
      var10.append(var4);
      var10.append(" cannot be found but it is necessary for ");
      var10.append(var3);
      var10.append(this.exceptionLabel());
      Log.e("RecyclerView", var10.toString());
   }

   private boolean hasUpdatedView() {
      int var2 = this.mChildHelper.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getChildAt(var1));
         if (var3 != null && !var3.shouldIgnore() && var3.isUpdated()) {
            return true;
         }
      }

      return false;
   }

   private void initAutofill() {
      if (ViewCompat.getImportantForAutofill(this) == 0) {
         ViewCompat.setImportantForAutofill(this, 8);
      }

   }

   private void initChildrenHelper() {
      this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
         public void addView(View var1, int var2) {
            RecyclerView.this.addView(var1, var2);
            RecyclerView.this.dispatchChildAttached(var1);
         }

         public void attachViewToParent(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
            RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var1);
            if (var4 != null) {
               if (!var4.isTmpDetached() && !var4.shouldIgnore()) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Called attach on a child which is not detached: ");
                  var5.append(var4);
                  var5.append(RecyclerView.this.exceptionLabel());
                  throw new IllegalArgumentException(var5.toString());
               }

               var4.clearTmpDetachFlag();
            }

            RecyclerView.this.attachViewToParent(var1, var2, var3);
         }

         public void detachViewFromParent(int var1) {
            View var2 = this.getChildAt(var1);
            if (var2 != null) {
               RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var2);
               if (var4 != null) {
                  if (var4.isTmpDetached() && !var4.shouldIgnore()) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("called detach on an already detached child ");
                     var3.append(var4);
                     var3.append(RecyclerView.this.exceptionLabel());
                     throw new IllegalArgumentException(var3.toString());
                  }

                  var4.addFlags(256);
               }
            }

            RecyclerView.this.detachViewFromParent(var1);
         }

         public View getChildAt(int var1) {
            return RecyclerView.this.getChildAt(var1);
         }

         public int getChildCount() {
            return RecyclerView.this.getChildCount();
         }

         public RecyclerView.ViewHolder getChildViewHolder(View var1) {
            return RecyclerView.getChildViewHolderInt(var1);
         }

         public int indexOfChild(View var1) {
            return RecyclerView.this.indexOfChild(var1);
         }

         public void onEnteredHiddenState(View var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
            if (var2 != null) {
               var2.onEnteredHiddenState(RecyclerView.this);
            }

         }

         public void onLeftHiddenState(View var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
            if (var2 != null) {
               var2.onLeftHiddenState(RecyclerView.this);
            }

         }

         public void removeAllViews() {
            int var2 = this.getChildCount();

            for(int var1 = 0; var1 < var2; ++var1) {
               View var3 = this.getChildAt(var1);
               RecyclerView.this.dispatchChildDetached(var3);
               var3.clearAnimation();
            }

            RecyclerView.this.removeAllViews();
         }

         public void removeViewAt(int var1) {
            View var2 = RecyclerView.this.getChildAt(var1);
            if (var2 != null) {
               RecyclerView.this.dispatchChildDetached(var2);
               var2.clearAnimation();
            }

            RecyclerView.this.removeViewAt(var1);
         }
      });
   }

   private boolean isPreferredNextFocus(View var1, View var2, int var3) {
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var9 = false;
      boolean var10 = false;
      boolean var8 = false;
      if (var2 != null) {
         if (var2 == this) {
            return false;
         } else if (this.findContainingItemView(var2) == null) {
            return false;
         } else if (var1 == null) {
            return true;
         } else if (this.findContainingItemView(var1) == null) {
            return true;
         } else {
            this.mTempRect.set(0, 0, var1.getWidth(), var1.getHeight());
            this.mTempRect2.set(0, 0, var2.getWidth(), var2.getHeight());
            this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
            this.offsetDescendantRectToMyCoords(var2, this.mTempRect2);
            byte var6;
            if (this.mLayout.getLayoutDirection() == 1) {
               var6 = -1;
            } else {
               var6 = 1;
            }

            byte var5 = 0;
            byte var4;
            if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
               var4 = 1;
            } else {
               label152: {
                  if (this.mTempRect.right <= this.mTempRect2.right) {
                     var4 = var5;
                     if (this.mTempRect.left < this.mTempRect2.right) {
                        break label152;
                     }
                  }

                  var4 = var5;
                  if (this.mTempRect.left > this.mTempRect2.left) {
                     var4 = -1;
                  }
               }
            }

            byte var7 = 0;
            byte var15;
            if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
               var15 = 1;
            } else {
               label153: {
                  if (this.mTempRect.bottom <= this.mTempRect2.bottom) {
                     var15 = var7;
                     if (this.mTempRect.top < this.mTempRect2.bottom) {
                        break label153;
                     }
                  }

                  var15 = var7;
                  if (this.mTempRect.top > this.mTempRect2.top) {
                     var15 = -1;
                  }
               }
            }

            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 17) {
                     if (var3 != 33) {
                        if (var3 != 66) {
                           if (var3 == 130) {
                              if (var15 > 0) {
                                 var8 = true;
                              }

                              return var8;
                           } else {
                              StringBuilder var14 = new StringBuilder();
                              var14.append("Invalid direction: ");
                              var14.append(var3);
                              var14.append(this.exceptionLabel());
                              throw new IllegalArgumentException(var14.toString());
                           }
                        } else {
                           var8 = var11;
                           if (var4 > 0) {
                              var8 = true;
                           }

                           return var8;
                        }
                     } else {
                        var8 = var12;
                        if (var15 < 0) {
                           var8 = true;
                        }

                        return var8;
                     }
                  } else {
                     var8 = var13;
                     if (var4 < 0) {
                        var8 = true;
                     }

                     return var8;
                  }
               } else {
                  if (var15 <= 0) {
                     var8 = var9;
                     if (var15 != 0) {
                        return var8;
                     }

                     var8 = var9;
                     if (var4 * var6 < 0) {
                        return var8;
                     }
                  }

                  var8 = true;
                  return var8;
               }
            } else {
               if (var15 >= 0) {
                  var8 = var10;
                  if (var15 != 0) {
                     return var8;
                  }

                  var8 = var10;
                  if (var4 * var6 > 0) {
                     return var8;
                  }
               }

               var8 = true;
               return var8;
            }
         }
      } else {
         return false;
      }
   }

   private void onPointerUp(MotionEvent var1) {
      int var2 = var1.getActionIndex();
      if (var1.getPointerId(var2) == this.mScrollPointerId) {
         byte var4;
         if (var2 == 0) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         this.mScrollPointerId = var1.getPointerId(var4);
         int var3 = (int)(var1.getX(var4) + 0.5F);
         this.mLastTouchX = var3;
         this.mInitialTouchX = var3;
         var2 = (int)(var1.getY(var4) + 0.5F);
         this.mLastTouchY = var2;
         this.mInitialTouchY = var2;
      }

   }

   private boolean predictiveItemAnimationsEnabled() {
      return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
   }

   private void processAdapterUpdatesAndSetAnimationFlags() {
      if (this.mDataSetHasChangedAfterLayout) {
         this.mAdapterHelper.reset();
         if (this.mDispatchItemsChangedEvent) {
            this.mLayout.onItemsChanged(this);
         }
      }

      if (this.predictiveItemAnimationsEnabled()) {
         this.mAdapterHelper.preProcess();
      } else {
         this.mAdapterHelper.consumeUpdatesInOnePass();
      }

      boolean var2 = this.mItemsAddedOrRemoved;
      boolean var3 = false;
      boolean var1;
      if (!var2 && !this.mItemsChanged) {
         var1 = false;
      } else {
         var1 = true;
      }

      RecyclerView.State var4 = this.mState;
      if (!this.mFirstLayoutComplete || this.mItemAnimator == null || !this.mDataSetHasChangedAfterLayout && !var1 && !this.mLayout.mRequestedSimpleAnimations || this.mDataSetHasChangedAfterLayout && !this.mAdapter.hasStableIds()) {
         var2 = false;
      } else {
         var2 = true;
      }

      var4.mRunSimpleAnimations = var2;
      var4 = this.mState;
      if (var4.mRunSimpleAnimations && var1 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled()) {
         var2 = true;
      } else {
         var2 = var3;
      }

      var4.mRunPredictiveAnimations = var2;
   }

   private void pullGlows(float var1, float var2, float var3, float var4) {
      boolean var5 = false;
      if (var2 < 0.0F) {
         this.ensureLeftGlow();
         EdgeEffectCompat.onPull(this.mLeftGlow, -var2 / (float)this.getWidth(), 1.0F - var3 / (float)this.getHeight());
         var5 = true;
      } else if (var2 > 0.0F) {
         this.ensureRightGlow();
         EdgeEffectCompat.onPull(this.mRightGlow, var2 / (float)this.getWidth(), var3 / (float)this.getHeight());
         var5 = true;
      }

      if (var4 < 0.0F) {
         this.ensureTopGlow();
         EdgeEffectCompat.onPull(this.mTopGlow, -var4 / (float)this.getHeight(), var1 / (float)this.getWidth());
         var5 = true;
      } else if (var4 > 0.0F) {
         this.ensureBottomGlow();
         EdgeEffectCompat.onPull(this.mBottomGlow, var4 / (float)this.getHeight(), 1.0F - var1 / (float)this.getWidth());
         var5 = true;
      }

      if (var5 || var2 != 0.0F || var4 != 0.0F) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   private void recoverFocusFromState() {
      if (this.mPreserveFocusAfterLayout && this.mAdapter != null && this.hasFocus() && this.getDescendantFocusability() != 393216) {
         if (this.getDescendantFocusability() != 131072 || !this.isFocused()) {
            View var1;
            if (!this.isFocused()) {
               var1 = this.getFocusedChild();
               if (IGNORE_DETACHED_FOCUSED_CHILD && (var1.getParent() == null || !var1.hasFocus())) {
                  if (this.mChildHelper.getChildCount() == 0) {
                     this.requestFocus();
                     return;
                  }
               } else if (!this.mChildHelper.isHidden(var1)) {
                  return;
               }
            }

            View var2 = null;
            RecyclerView.ViewHolder var4 = var2;
            if (this.mState.mFocusedItemId != -1L) {
               var4 = var2;
               if (this.mAdapter.hasStableIds()) {
                  var4 = this.findViewHolderForItemId(this.mState.mFocusedItemId);
               }
            }

            var2 = null;
            if (var4 != null && !this.mChildHelper.isHidden(var4.itemView) && var4.itemView.hasFocusable()) {
               var1 = var4.itemView;
            } else {
               var1 = var2;
               if (this.mChildHelper.getChildCount() > 0) {
                  var1 = this.findNextViewToFocus();
               }
            }

            if (var1 != null) {
               var2 = var1;
               if ((long)this.mState.mFocusedSubChildId != -1L) {
                  View var3 = var1.findViewById(this.mState.mFocusedSubChildId);
                  var2 = var1;
                  if (var3 != null) {
                     var2 = var1;
                     if (var3.isFocusable()) {
                        var2 = var3;
                     }
                  }
               }

               var2.requestFocus();
            }

         }
      }
   }

   private void releaseGlows() {
      boolean var2 = false;
      EdgeEffect var3 = this.mLeftGlow;
      if (var3 != null) {
         var3.onRelease();
         var2 = this.mLeftGlow.isFinished();
      }

      var3 = this.mTopGlow;
      boolean var1 = var2;
      if (var3 != null) {
         var3.onRelease();
         var1 = var2 | this.mTopGlow.isFinished();
      }

      var3 = this.mRightGlow;
      var2 = var1;
      if (var3 != null) {
         var3.onRelease();
         var2 = var1 | this.mRightGlow.isFinished();
      }

      var3 = this.mBottomGlow;
      var1 = var2;
      if (var3 != null) {
         var3.onRelease();
         var1 = var2 | this.mBottomGlow.isFinished();
      }

      if (var1) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   private void requestChildOnScreen(View var1, View var2) {
      View var5;
      if (var2 != null) {
         var5 = var2;
      } else {
         var5 = var1;
      }

      this.mTempRect.set(0, 0, var5.getWidth(), var5.getHeight());
      android.view.ViewGroup.LayoutParams var7 = var5.getLayoutParams();
      Rect var6;
      if (var7 instanceof RecyclerView.LayoutParams) {
         RecyclerView.LayoutParams var8 = (RecyclerView.LayoutParams)var7;
         if (!var8.mInsetsDirty) {
            Rect var9 = var8.mDecorInsets;
            var6 = this.mTempRect;
            var6.left -= var9.left;
            var6 = this.mTempRect;
            var6.right += var9.right;
            var6 = this.mTempRect;
            var6.top -= var9.top;
            var6 = this.mTempRect;
            var6.bottom += var9.bottom;
         }
      }

      if (var2 != null) {
         this.offsetDescendantRectToMyCoords(var2, this.mTempRect);
         this.offsetRectIntoDescendantCoords(var1, this.mTempRect);
      }

      RecyclerView.LayoutManager var10 = this.mLayout;
      var6 = this.mTempRect;
      boolean var4 = this.mFirstLayoutComplete;
      boolean var3;
      if (var2 == null) {
         var3 = true;
      } else {
         var3 = false;
      }

      var10.requestChildRectangleOnScreen(this, var1, var6, var4 ^ true, var3);
   }

   private void resetFocusInfo() {
      this.mState.mFocusedItemId = -1L;
      this.mState.mFocusedItemPosition = -1;
      this.mState.mFocusedSubChildId = -1;
   }

   private void resetScroll() {
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 != null) {
         var1.clear();
      }

      this.stopNestedScroll(0);
      this.releaseGlows();
   }

   private void saveFocusInfo() {
      RecyclerView.State var5 = null;
      View var4 = var5;
      if (this.mPreserveFocusAfterLayout) {
         var4 = var5;
         if (this.hasFocus()) {
            var4 = var5;
            if (this.mAdapter != null) {
               var4 = this.getFocusedChild();
            }
         }
      }

      RecyclerView.ViewHolder var6;
      if (var4 == null) {
         var6 = null;
      } else {
         var6 = this.findContainingViewHolder(var4);
      }

      if (var6 == null) {
         this.resetFocusInfo();
      } else {
         var5 = this.mState;
         long var2;
         if (this.mAdapter.hasStableIds()) {
            var2 = var6.getItemId();
         } else {
            var2 = -1L;
         }

         var5.mFocusedItemId = var2;
         var5 = this.mState;
         int var1;
         if (this.mDataSetHasChangedAfterLayout) {
            var1 = -1;
         } else if (var6.isRemoved()) {
            var1 = var6.mOldPosition;
         } else {
            var1 = var6.getAdapterPosition();
         }

         var5.mFocusedItemPosition = var1;
         this.mState.mFocusedSubChildId = this.getDeepestFocusedViewWithId(var6.itemView);
      }
   }

   private void setAdapterInternal(RecyclerView.Adapter var1, boolean var2, boolean var3) {
      RecyclerView.Adapter var4 = this.mAdapter;
      if (var4 != null) {
         var4.unregisterAdapterDataObserver(this.mObserver);
         this.mAdapter.onDetachedFromRecyclerView(this);
      }

      if (!var2 || var3) {
         this.removeAndRecycleViews();
      }

      this.mAdapterHelper.reset();
      var4 = this.mAdapter;
      this.mAdapter = var1;
      if (var1 != null) {
         var1.registerAdapterDataObserver(this.mObserver);
         var1.onAttachedToRecyclerView(this);
      }

      RecyclerView.LayoutManager var5 = this.mLayout;
      if (var5 != null) {
         var5.onAdapterChanged(var4, this.mAdapter);
      }

      this.mRecycler.onAdapterChanged(var4, this.mAdapter, var2);
      this.mState.mStructureChanged = true;
   }

   private void stopScrollersInternal() {
      this.mViewFlinger.stop();
      RecyclerView.LayoutManager var1 = this.mLayout;
      if (var1 != null) {
         var1.stopSmoothScroller();
      }

   }

   void absorbGlows(int var1, int var2) {
      if (var1 < 0) {
         this.ensureLeftGlow();
         if (this.mLeftGlow.isFinished()) {
            this.mLeftGlow.onAbsorb(-var1);
         }
      } else if (var1 > 0) {
         this.ensureRightGlow();
         if (this.mRightGlow.isFinished()) {
            this.mRightGlow.onAbsorb(var1);
         }
      }

      if (var2 < 0) {
         this.ensureTopGlow();
         if (this.mTopGlow.isFinished()) {
            this.mTopGlow.onAbsorb(-var2);
         }
      } else if (var2 > 0) {
         this.ensureBottomGlow();
         if (this.mBottomGlow.isFinished()) {
            this.mBottomGlow.onAbsorb(var2);
         }
      }

      if (var1 != 0 || var2 != 0) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      RecyclerView.LayoutManager var4 = this.mLayout;
      if (var4 == null || !var4.onAddFocusables(this, var1, var2, var3)) {
         super.addFocusables(var1, var2, var3);
      }

   }

   public void addItemDecoration(RecyclerView.ItemDecoration var1) {
      this.addItemDecoration(var1, -1);
   }

   public void addItemDecoration(RecyclerView.ItemDecoration var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 != null) {
         var3.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
      }

      if (this.mItemDecorations.isEmpty()) {
         this.setWillNotDraw(false);
      }

      if (var2 < 0) {
         this.mItemDecorations.add(var1);
      } else {
         this.mItemDecorations.add(var2, var1);
      }

      this.markItemDecorInsetsDirty();
      this.requestLayout();
   }

   public void addOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener var1) {
      if (this.mOnChildAttachStateListeners == null) {
         this.mOnChildAttachStateListeners = new ArrayList();
      }

      this.mOnChildAttachStateListeners.add(var1);
   }

   public void addOnItemTouchListener(RecyclerView.OnItemTouchListener var1) {
      this.mOnItemTouchListeners.add(var1);
   }

   public void addOnScrollListener(RecyclerView.OnScrollListener var1) {
      if (this.mScrollListeners == null) {
         this.mScrollListeners = new ArrayList();
      }

      this.mScrollListeners.add(var1);
   }

   void animateAppearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      var1.setIsRecyclable(false);
      if (this.mItemAnimator.animateAppearance(var1, var2, var3)) {
         this.postAnimationRunner();
      }

   }

   void animateDisappearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      this.addAnimatingView(var1);
      var1.setIsRecyclable(false);
      if (this.mItemAnimator.animateDisappearance(var1, var2, var3)) {
         this.postAnimationRunner();
      }

   }

   void assertInLayoutOrScroll(String var1) {
      if (!this.isComputingLayout()) {
         if (var1 == null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            var3.append(this.exceptionLabel());
            throw new IllegalStateException(var3.toString());
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(var1);
            var2.append(this.exceptionLabel());
            throw new IllegalStateException(var2.toString());
         }
      }
   }

   void assertNotInLayoutOrScroll(String var1) {
      StringBuilder var2;
      if (this.isComputingLayout()) {
         if (var1 == null) {
            var2 = new StringBuilder();
            var2.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
            var2.append(this.exceptionLabel());
            throw new IllegalStateException(var2.toString());
         } else {
            throw new IllegalStateException(var1);
         }
      } else {
         if (this.mDispatchScrollCounter > 0) {
            var2 = new StringBuilder();
            var2.append("");
            var2.append(this.exceptionLabel());
            Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(var2.toString()));
         }

      }
   }

   boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1) {
      RecyclerView.ItemAnimator var2 = this.mItemAnimator;
      return var2 == null || var2.canReuseUpdatedViewHolder(var1, var1.getUnmodifiedPayloads());
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof RecyclerView.LayoutParams && this.mLayout.checkLayoutParams((RecyclerView.LayoutParams)var1);
   }

   void clearOldPositions() {
      int var2 = this.mChildHelper.getUnfilteredChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var1));
         if (!var3.shouldIgnore()) {
            var3.clearOldPosition();
         }
      }

      this.mRecycler.clearOldPositions();
   }

   public void clearOnChildAttachStateChangeListeners() {
      List var1 = this.mOnChildAttachStateListeners;
      if (var1 != null) {
         var1.clear();
      }

   }

   public void clearOnScrollListeners() {
      List var1 = this.mScrollListeners;
      if (var1 != null) {
         var1.clear();
      }

   }

   public int computeHorizontalScrollExtent() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollHorizontally()) {
            var1 = this.mLayout.computeHorizontalScrollExtent(this.mState);
         }

         return var1;
      }
   }

   public int computeHorizontalScrollOffset() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollHorizontally()) {
            var1 = this.mLayout.computeHorizontalScrollOffset(this.mState);
         }

         return var1;
      }
   }

   public int computeHorizontalScrollRange() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollHorizontally()) {
            var1 = this.mLayout.computeHorizontalScrollRange(this.mState);
         }

         return var1;
      }
   }

   public int computeVerticalScrollExtent() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollVertically()) {
            var1 = this.mLayout.computeVerticalScrollExtent(this.mState);
         }

         return var1;
      }
   }

   public int computeVerticalScrollOffset() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollVertically()) {
            var1 = this.mLayout.computeVerticalScrollOffset(this.mState);
         }

         return var1;
      }
   }

   public int computeVerticalScrollRange() {
      RecyclerView.LayoutManager var2 = this.mLayout;
      int var1 = 0;
      if (var2 == null) {
         return 0;
      } else {
         if (var2.canScrollVertically()) {
            var1 = this.mLayout.computeVerticalScrollRange(this.mState);
         }

         return var1;
      }
   }

   void considerReleasingGlowsOnScroll(int var1, int var2) {
      boolean var4 = false;
      EdgeEffect var5 = this.mLeftGlow;
      boolean var3 = var4;
      if (var5 != null) {
         var3 = var4;
         if (!var5.isFinished()) {
            var3 = var4;
            if (var1 > 0) {
               this.mLeftGlow.onRelease();
               var3 = this.mLeftGlow.isFinished();
            }
         }
      }

      var5 = this.mRightGlow;
      var4 = var3;
      if (var5 != null) {
         var4 = var3;
         if (!var5.isFinished()) {
            var4 = var3;
            if (var1 < 0) {
               this.mRightGlow.onRelease();
               var4 = var3 | this.mRightGlow.isFinished();
            }
         }
      }

      var5 = this.mTopGlow;
      var3 = var4;
      if (var5 != null) {
         var3 = var4;
         if (!var5.isFinished()) {
            var3 = var4;
            if (var2 > 0) {
               this.mTopGlow.onRelease();
               var3 = var4 | this.mTopGlow.isFinished();
            }
         }
      }

      var5 = this.mBottomGlow;
      var4 = var3;
      if (var5 != null) {
         var4 = var3;
         if (!var5.isFinished()) {
            var4 = var3;
            if (var2 < 0) {
               this.mBottomGlow.onRelease();
               var4 = var3 | this.mBottomGlow.isFinished();
            }
         }
      }

      if (var4) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void consumePendingUpdateOperations() {
      if (this.mFirstLayoutComplete && !this.mDataSetHasChangedAfterLayout) {
         if (this.mAdapterHelper.hasPendingUpdates()) {
            if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
               TraceCompat.beginSection("RV PartialInvalidate");
               this.startInterceptRequestLayout();
               this.onEnterLayoutOrScroll();
               this.mAdapterHelper.preProcess();
               if (!this.mLayoutWasDefered) {
                  if (this.hasUpdatedView()) {
                     this.dispatchLayout();
                  } else {
                     this.mAdapterHelper.consumePostponedUpdates();
                  }
               }

               this.stopInterceptRequestLayout(true);
               this.onExitLayoutOrScroll();
               TraceCompat.endSection();
            } else {
               if (this.mAdapterHelper.hasPendingUpdates()) {
                  TraceCompat.beginSection("RV FullInvalidate");
                  this.dispatchLayout();
                  TraceCompat.endSection();
               }

            }
         }
      } else {
         TraceCompat.beginSection("RV FullInvalidate");
         this.dispatchLayout();
         TraceCompat.endSection();
      }
   }

   void defaultOnMeasure(int var1, int var2) {
      this.setMeasuredDimension(RecyclerView.LayoutManager.chooseSize(var1, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth(this)), RecyclerView.LayoutManager.chooseSize(var2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight(this)));
   }

   void dispatchChildAttached(View var1) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      this.onChildAttachedToWindow(var1);
      RecyclerView.Adapter var4 = this.mAdapter;
      if (var4 != null && var3 != null) {
         var4.onViewAttachedToWindow(var3);
      }

      List var5 = this.mOnChildAttachStateListeners;
      if (var5 != null) {
         for(int var2 = var5.size() - 1; var2 >= 0; --var2) {
            ((RecyclerView.OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(var2)).onChildViewAttachedToWindow(var1);
         }
      }

   }

   void dispatchChildDetached(View var1) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      this.onChildDetachedFromWindow(var1);
      RecyclerView.Adapter var4 = this.mAdapter;
      if (var4 != null && var3 != null) {
         var4.onViewDetachedFromWindow(var3);
      }

      List var5 = this.mOnChildAttachStateListeners;
      if (var5 != null) {
         for(int var2 = var5.size() - 1; var2 >= 0; --var2) {
            ((RecyclerView.OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(var2)).onChildViewDetachedFromWindow(var1);
         }
      }

   }

   void dispatchLayout() {
      if (this.mAdapter == null) {
         Log.e("RecyclerView", "No adapter attached; skipping layout");
      } else if (this.mLayout == null) {
         Log.e("RecyclerView", "No layout manager attached; skipping layout");
      } else {
         this.mState.mIsMeasuring = false;
         if (this.mState.mLayoutStep == 1) {
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
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.getScrollingChildHelper().dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.getScrollingChildHelper().dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.getScrollingChildHelper().dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5) {
      return this.getScrollingChildHelper().dispatchNestedPreScroll(var1, var2, var3, var4, var5);
   }

   public final void dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6, int[] var7) {
      this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5, var6, var7);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6) {
      return this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
   }

   void dispatchOnScrollStateChanged(int var1) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 != null) {
         var3.onScrollStateChanged(var1);
      }

      this.onScrollStateChanged(var1);
      RecyclerView.OnScrollListener var4 = this.mScrollListener;
      if (var4 != null) {
         var4.onScrollStateChanged(this, var1);
      }

      List var5 = this.mScrollListeners;
      if (var5 != null) {
         for(int var2 = var5.size() - 1; var2 >= 0; --var2) {
            ((RecyclerView.OnScrollListener)this.mScrollListeners.get(var2)).onScrollStateChanged(this, var1);
         }
      }

   }

   void dispatchOnScrolled(int var1, int var2) {
      ++this.mDispatchScrollCounter;
      int var3 = this.getScrollX();
      int var4 = this.getScrollY();
      this.onScrollChanged(var3, var4, var3 - var1, var4 - var2);
      this.onScrolled(var1, var2);
      RecyclerView.OnScrollListener var5 = this.mScrollListener;
      if (var5 != null) {
         var5.onScrolled(this, var1, var2);
      }

      List var6 = this.mScrollListeners;
      if (var6 != null) {
         for(var3 = var6.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.OnScrollListener)this.mScrollListeners.get(var3)).onScrolled(this, var1, var2);
         }
      }

      --this.mDispatchScrollCounter;
   }

   void dispatchPendingImportantForAccessibilityChanges() {
      for(int var1 = this.mPendingAccessibilityImportanceChange.size() - 1; var1 >= 0; --var1) {
         RecyclerView.ViewHolder var3 = (RecyclerView.ViewHolder)this.mPendingAccessibilityImportanceChange.get(var1);
         if (var3.itemView.getParent() == this && !var3.shouldIgnore()) {
            int var2 = var3.mPendingAccessibilityState;
            if (var2 != -1) {
               ViewCompat.setImportantForAccessibility(var3.itemView, var2);
               var3.mPendingAccessibilityState = -1;
            }
         }
      }

      this.mPendingAccessibilityImportanceChange.clear();
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      this.onPopulateAccessibilityEvent(var1);
      return true;
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.dispatchThawSelfOnly(var1);
   }

   protected void dispatchSaveInstanceState(SparseArray var1) {
      this.dispatchFreezeSelfOnly(var1);
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      int var3 = this.mItemDecorations.size();

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var2)).onDrawOver(var1, this, this.mState);
      }

      boolean var8 = false;
      EdgeEffect var7 = this.mLeftGlow;
      boolean var4 = true;
      boolean var9 = var8;
      if (var7 != null) {
         var9 = var8;
         if (!var7.isFinished()) {
            var3 = var1.save();
            if (this.mClipToPadding) {
               var2 = this.getPaddingBottom();
            } else {
               var2 = 0;
            }

            var1.rotate(270.0F);
            var1.translate((float)(-this.getHeight() + var2), 0.0F);
            var7 = this.mLeftGlow;
            if (var7 != null && var7.draw(var1)) {
               var8 = true;
            } else {
               var8 = false;
            }

            var1.restoreToCount(var3);
            var9 = var8;
         }
      }

      var7 = this.mTopGlow;
      var8 = var9;
      int var5;
      if (var7 != null) {
         var8 = var9;
         if (!var7.isFinished()) {
            var5 = var1.save();
            if (this.mClipToPadding) {
               var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }

            var7 = this.mTopGlow;
            if (var7 != null && var7.draw(var1)) {
               var8 = true;
            } else {
               var8 = false;
            }

            var8 |= var9;
            var1.restoreToCount(var5);
         }
      }

      var7 = this.mRightGlow;
      var9 = var8;
      if (var7 != null) {
         var9 = var8;
         if (!var7.isFinished()) {
            var5 = var1.save();
            int var6 = this.getWidth();
            if (this.mClipToPadding) {
               var3 = this.getPaddingTop();
            } else {
               var3 = 0;
            }

            var1.rotate(90.0F);
            var1.translate((float)(-var3), (float)(-var6));
            var7 = this.mRightGlow;
            if (var7 != null && var7.draw(var1)) {
               var9 = true;
            } else {
               var9 = false;
            }

            var9 |= var8;
            var1.restoreToCount(var5);
         }
      }

      var7 = this.mBottomGlow;
      var8 = var9;
      if (var7 != null) {
         var8 = var9;
         if (!var7.isFinished()) {
            var5 = var1.save();
            var1.rotate(180.0F);
            if (this.mClipToPadding) {
               var1.translate((float)(-this.getWidth() + this.getPaddingRight()), (float)(-this.getHeight() + this.getPaddingBottom()));
            } else {
               var1.translate((float)(-this.getWidth()), (float)(-this.getHeight()));
            }

            var7 = this.mBottomGlow;
            if (var7 != null && var7.draw(var1)) {
               var8 = var4;
            } else {
               var8 = false;
            }

            var8 |= var9;
            var1.restoreToCount(var5);
         }
      }

      var9 = var8;
      if (!var8) {
         var9 = var8;
         if (this.mItemAnimator != null) {
            var9 = var8;
            if (this.mItemDecorations.size() > 0) {
               var9 = var8;
               if (this.mItemAnimator.isRunning()) {
                  var9 = true;
               }
            }
         }
      }

      if (var9) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public boolean drawChild(Canvas var1, View var2, long var3) {
      return super.drawChild(var1, var2, var3);
   }

   void ensureBottomGlow() {
      if (this.mBottomGlow == null) {
         EdgeEffect var1 = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
         this.mBottomGlow = var1;
         if (this.mClipToPadding) {
            var1.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
         } else {
            var1.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
         }
      }
   }

   void ensureLeftGlow() {
      if (this.mLeftGlow == null) {
         EdgeEffect var1 = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
         this.mLeftGlow = var1;
         if (this.mClipToPadding) {
            var1.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
         } else {
            var1.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
         }
      }
   }

   void ensureRightGlow() {
      if (this.mRightGlow == null) {
         EdgeEffect var1 = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
         this.mRightGlow = var1;
         if (this.mClipToPadding) {
            var1.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
         } else {
            var1.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
         }
      }
   }

   void ensureTopGlow() {
      if (this.mTopGlow == null) {
         EdgeEffect var1 = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
         this.mTopGlow = var1;
         if (this.mClipToPadding) {
            var1.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
         } else {
            var1.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
         }
      }
   }

   String exceptionLabel() {
      StringBuilder var1 = new StringBuilder();
      var1.append(" ");
      var1.append(super.toString());
      var1.append(", adapter:");
      var1.append(this.mAdapter);
      var1.append(", layout:");
      var1.append(this.mLayout);
      var1.append(", context:");
      var1.append(this.getContext());
      return var1.toString();
   }

   final void fillRemainingScrollValues(RecyclerView.State var1) {
      if (this.getScrollState() == 2) {
         OverScroller var2 = this.mViewFlinger.mOverScroller;
         var1.mRemainingScrollHorizontal = var2.getFinalX() - var2.getCurrX();
         var1.mRemainingScrollVertical = var2.getFinalY() - var2.getCurrY();
      } else {
         var1.mRemainingScrollHorizontal = 0;
         var1.mRemainingScrollVertical = 0;
      }
   }

   public View findChildViewUnder(float var1, float var2) {
      for(int var5 = this.mChildHelper.getChildCount() - 1; var5 >= 0; --var5) {
         View var6 = this.mChildHelper.getChildAt(var5);
         float var3 = var6.getTranslationX();
         float var4 = var6.getTranslationY();
         if (var1 >= (float)var6.getLeft() + var3 && var1 <= (float)var6.getRight() + var3 && var2 >= (float)var6.getTop() + var4 && var2 <= (float)var6.getBottom() + var4) {
            return var6;
         }
      }

      return null;
   }

   public View findContainingItemView(View var1) {
      ViewParent var3 = var1.getParent();
      View var2 = var1;

      ViewParent var4;
      for(var4 = var3; var4 != null && var4 != this && var4 instanceof View; var4 = var2.getParent()) {
         var2 = (View)var4;
      }

      return var4 == this ? var2 : null;
   }

   public RecyclerView.ViewHolder findContainingViewHolder(View var1) {
      var1 = this.findContainingItemView(var1);
      return var1 == null ? null : this.getChildViewHolder(var1);
   }

   public RecyclerView.ViewHolder findViewHolderForAdapterPosition(int var1) {
      if (this.mDataSetHasChangedAfterLayout) {
         return null;
      } else {
         int var3 = this.mChildHelper.getUnfilteredChildCount();
         RecyclerView.ViewHolder var4 = null;

         RecyclerView.ViewHolder var5;
         for(int var2 = 0; var2 < var3; var4 = var5) {
            RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var2));
            var5 = var4;
            if (var6 != null) {
               var5 = var4;
               if (!var6.isRemoved()) {
                  var5 = var4;
                  if (this.getAdapterPositionFor(var6) == var1) {
                     if (!this.mChildHelper.isHidden(var6.itemView)) {
                        return var6;
                     }

                     var5 = var6;
                  }
               }
            }

            ++var2;
         }

         return var4;
      }
   }

   public RecyclerView.ViewHolder findViewHolderForItemId(long var1) {
      RecyclerView.Adapter var5 = this.mAdapter;
      if (var5 != null && var5.hasStableIds()) {
         int var4 = this.mChildHelper.getUnfilteredChildCount();
         RecyclerView.ViewHolder var8 = null;

         RecyclerView.ViewHolder var6;
         for(int var3 = 0; var3 < var4; var8 = var6) {
            RecyclerView.ViewHolder var7 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var3));
            var6 = var8;
            if (var7 != null) {
               var6 = var8;
               if (!var7.isRemoved()) {
                  var6 = var8;
                  if (var7.getItemId() == var1) {
                     if (!this.mChildHelper.isHidden(var7.itemView)) {
                        return var7;
                     }

                     var6 = var7;
                  }
               }
            }

            ++var3;
         }

         return var8;
      } else {
         return null;
      }
   }

   public RecyclerView.ViewHolder findViewHolderForLayoutPosition(int var1) {
      return this.findViewHolderForPosition(var1, false);
   }

   @Deprecated
   public RecyclerView.ViewHolder findViewHolderForPosition(int var1) {
      return this.findViewHolderForPosition(var1, false);
   }

   RecyclerView.ViewHolder findViewHolderForPosition(int var1, boolean var2) {
      int var4 = this.mChildHelper.getUnfilteredChildCount();
      RecyclerView.ViewHolder var5 = null;

      RecyclerView.ViewHolder var6;
      for(int var3 = 0; var3 < var4; var5 = var6) {
         RecyclerView.ViewHolder var7 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var3));
         var6 = var5;
         if (var7 != null) {
            var6 = var5;
            if (!var7.isRemoved()) {
               label37: {
                  if (var2) {
                     if (var7.mPosition != var1) {
                        var6 = var5;
                        break label37;
                     }
                  } else if (var7.getLayoutPosition() != var1) {
                     var6 = var5;
                     break label37;
                  }

                  if (!this.mChildHelper.isHidden(var7.itemView)) {
                     return var7;
                  }

                  var6 = var7;
               }
            }
         }

         ++var3;
      }

      return var5;
   }

   public boolean fling(int var1, int var2) {
      RecyclerView.LayoutManager var8 = this.mLayout;
      if (var8 == null) {
         Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
         return false;
      } else if (this.mLayoutSuppressed) {
         return false;
      } else {
         int var3;
         boolean var6;
         boolean var7;
         label60: {
            var6 = var8.canScrollHorizontally();
            var7 = this.mLayout.canScrollVertically();
            if (var6) {
               var3 = var1;
               if (Math.abs(var1) >= this.mMinFlingVelocity) {
                  break label60;
               }
            }

            var3 = 0;
         }

         int var4;
         label55: {
            if (var7) {
               var4 = var2;
               if (Math.abs(var2) >= this.mMinFlingVelocity) {
                  break label55;
               }
            }

            var4 = 0;
         }

         if (var3 == 0 && var4 == 0) {
            return false;
         } else {
            if (!this.dispatchNestedPreFling((float)var3, (float)var4)) {
               boolean var5;
               if (!var6 && !var7) {
                  var5 = false;
               } else {
                  var5 = true;
               }

               this.dispatchNestedFling((float)var3, (float)var4, var5);
               RecyclerView.OnFlingListener var9 = this.mOnFlingListener;
               if (var9 != null && var9.onFling(var3, var4)) {
                  return true;
               }

               if (var5) {
                  var1 = 0;
                  if (var6) {
                     var1 = 0 | 1;
                  }

                  var2 = var1;
                  if (var7) {
                     var2 = var1 | 2;
                  }

                  this.startNestedScroll(var2, 1);
                  var1 = this.mMaxFlingVelocity;
                  var1 = Math.max(-var1, Math.min(var3, var1));
                  var2 = this.mMaxFlingVelocity;
                  var2 = Math.max(-var2, Math.min(var4, var2));
                  this.mViewFlinger.fling(var1, var2);
                  return true;
               }
            }

            return false;
         }
      }
   }

   public View focusSearch(View var1, int var2) {
      View var8 = this.mLayout.onInterceptFocusSearch(var1, var2);
      if (var8 != null) {
         return var8;
      } else {
         RecyclerView.Adapter var15 = this.mAdapter;
         boolean var7 = true;
         boolean var3;
         if (var15 != null && this.mLayout != null && !this.isComputingLayout() && !this.mLayoutSuppressed) {
            var3 = true;
         } else {
            var3 = false;
         }

         FocusFinder var16 = FocusFinder.getInstance();
         int var4;
         if (var3 && (var2 == 2 || var2 == 1)) {
            boolean var5 = false;
            int var12 = var2;
            boolean var6;
            if (this.mLayout.canScrollVertically()) {
               short var13;
               if (var2 == 2) {
                  var13 = 130;
               } else {
                  var13 = 33;
               }

               if (var16.findNextFocus(this, var1, var13) == null) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var6 = var3;
               var5 = var3;
               var12 = var2;
               if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                  var12 = var13;
                  var5 = var6;
               }
            }

            var6 = var5;
            var4 = var12;
            if (!var5) {
               var6 = var5;
               var4 = var12;
               if (this.mLayout.canScrollHorizontally()) {
                  boolean var10;
                  if (this.mLayout.getLayoutDirection() == 1) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  boolean var14;
                  if (var12 == 2) {
                     var14 = true;
                  } else {
                     var14 = false;
                  }

                  byte var11;
                  if (var14 ^ var10) {
                     var11 = 66;
                  } else {
                     var11 = 17;
                  }

                  if (var16.findNextFocus(this, var1, var11) == null) {
                     var14 = var7;
                  } else {
                     var14 = false;
                  }

                  var5 = var14;
                  var6 = var14;
                  var4 = var12;
                  if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                     var4 = var11;
                     var6 = var5;
                  }
               }
            }

            if (var6) {
               this.consumePendingUpdateOperations();
               if (this.findContainingItemView(var1) == null) {
                  return null;
               }

               this.startInterceptRequestLayout();
               this.mLayout.onFocusSearchFailed(var1, var4, this.mRecycler, this.mState);
               this.stopInterceptRequestLayout(false);
            }

            var8 = var16.findNextFocus(this, var1, var4);
         } else {
            View var9 = var16.findNextFocus(this, var1, var2);
            var8 = var9;
            var4 = var2;
            if (var9 == null) {
               var8 = var9;
               var4 = var2;
               if (var3) {
                  this.consumePendingUpdateOperations();
                  if (this.findContainingItemView(var1) == null) {
                     return null;
                  }

                  this.startInterceptRequestLayout();
                  var8 = this.mLayout.onFocusSearchFailed(var1, var2, this.mRecycler, this.mState);
                  this.stopInterceptRequestLayout(false);
                  var4 = var2;
               }
            }
         }

         if (var8 != null && !var8.hasFocusable()) {
            if (this.getFocusedChild() == null) {
               return super.focusSearch(var1, var4);
            } else {
               this.requestChildOnScreen(var8, (View)null);
               return var1;
            }
         } else {
            return this.isPreferredNextFocus(var1, var8, var4) ? var8 : super.focusSearch(var1, var4);
         }
      }
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      if (var1 != null) {
         return var1.generateDefaultLayoutParams();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("RecyclerView has no LayoutManager");
         var2.append(this.exceptionLabel());
         throw new IllegalStateException(var2.toString());
      }
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         return var2.generateLayoutParams(this.getContext(), var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("RecyclerView has no LayoutManager");
         var3.append(this.exceptionLabel());
         throw new IllegalStateException(var3.toString());
      }
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         return var2.generateLayoutParams(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("RecyclerView has no LayoutManager");
         var3.append(this.exceptionLabel());
         throw new IllegalStateException(var3.toString());
      }
   }

   public CharSequence getAccessibilityClassName() {
      return "androidx.recyclerview.widget.RecyclerView";
   }

   public RecyclerView.Adapter getAdapter() {
      return this.mAdapter;
   }

   int getAdapterPositionFor(RecyclerView.ViewHolder var1) {
      return !var1.hasAnyOfTheFlags(524) && var1.isBound() ? this.mAdapterHelper.applyPendingUpdatesToPosition(var1.mPosition) : -1;
   }

   public int getBaseline() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      return var1 != null ? var1.getBaseline() : super.getBaseline();
   }

   long getChangedHolderKey(RecyclerView.ViewHolder var1) {
      return this.mAdapter.hasStableIds() ? var1.getItemId() : (long)var1.mPosition;
   }

   public int getChildAdapterPosition(View var1) {
      RecyclerView.ViewHolder var2 = getChildViewHolderInt(var1);
      return var2 != null ? var2.getAdapterPosition() : -1;
   }

   protected int getChildDrawingOrder(int var1, int var2) {
      RecyclerView.ChildDrawingOrderCallback var3 = this.mChildDrawingOrderCallback;
      return var3 == null ? super.getChildDrawingOrder(var1, var2) : var3.onGetChildDrawingOrder(var1, var2);
   }

   public long getChildItemId(View var1) {
      RecyclerView.Adapter var4 = this.mAdapter;
      long var2 = -1L;
      if (var4 != null) {
         if (!var4.hasStableIds()) {
            return -1L;
         } else {
            RecyclerView.ViewHolder var5 = getChildViewHolderInt(var1);
            if (var5 != null) {
               var2 = var5.getItemId();
            }

            return var2;
         }
      } else {
         return -1L;
      }
   }

   public int getChildLayoutPosition(View var1) {
      RecyclerView.ViewHolder var2 = getChildViewHolderInt(var1);
      return var2 != null ? var2.getLayoutPosition() : -1;
   }

   @Deprecated
   public int getChildPosition(View var1) {
      return this.getChildAdapterPosition(var1);
   }

   public RecyclerView.ViewHolder getChildViewHolder(View var1) {
      ViewParent var2 = var1.getParent();
      if (var2 != null && var2 != this) {
         StringBuilder var3 = new StringBuilder();
         var3.append("View ");
         var3.append(var1);
         var3.append(" is not a direct child of ");
         var3.append(this);
         throw new IllegalArgumentException(var3.toString());
      } else {
         return getChildViewHolderInt(var1);
      }
   }

   public boolean getClipToPadding() {
      return this.mClipToPadding;
   }

   public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
      return this.mAccessibilityDelegate;
   }

   public void getDecoratedBoundsWithMargins(View var1, Rect var2) {
      getDecoratedBoundsWithMarginsInt(var1, var2);
   }

   public RecyclerView.EdgeEffectFactory getEdgeEffectFactory() {
      return this.mEdgeEffectFactory;
   }

   public RecyclerView.ItemAnimator getItemAnimator() {
      return this.mItemAnimator;
   }

   Rect getItemDecorInsetsForChild(View var1) {
      RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
      if (!var4.mInsetsDirty) {
         return var4.mDecorInsets;
      } else if (this.mState.isPreLayout() && (var4.isItemChanged() || var4.isViewInvalid())) {
         return var4.mDecorInsets;
      } else {
         Rect var5 = var4.mDecorInsets;
         var5.set(0, 0, 0, 0);
         int var3 = this.mItemDecorations.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            this.mTempRect.set(0, 0, 0, 0);
            ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var2)).getItemOffsets(this.mTempRect, var1, this, this.mState);
            var5.left += this.mTempRect.left;
            var5.top += this.mTempRect.top;
            var5.right += this.mTempRect.right;
            var5.bottom += this.mTempRect.bottom;
         }

         var4.mInsetsDirty = false;
         return var5;
      }
   }

   public RecyclerView.ItemDecoration getItemDecorationAt(int var1) {
      int var2 = this.getItemDecorationCount();
      if (var1 >= 0 && var1 < var2) {
         return (RecyclerView.ItemDecoration)this.mItemDecorations.get(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" is an invalid index for size ");
         var3.append(var2);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   public int getItemDecorationCount() {
      return this.mItemDecorations.size();
   }

   public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayout;
   }

   public int getMaxFlingVelocity() {
      return this.mMaxFlingVelocity;
   }

   public int getMinFlingVelocity() {
      return this.mMinFlingVelocity;
   }

   long getNanoTime() {
      return ALLOW_THREAD_GAP_WORK ? System.nanoTime() : 0L;
   }

   public RecyclerView.OnFlingListener getOnFlingListener() {
      return this.mOnFlingListener;
   }

   public boolean getPreserveFocusAfterLayout() {
      return this.mPreserveFocusAfterLayout;
   }

   public RecyclerView.RecycledViewPool getRecycledViewPool() {
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

   public boolean hasNestedScrollingParent(int var1) {
      return this.getScrollingChildHelper().hasNestedScrollingParent(var1);
   }

   public boolean hasPendingAdapterUpdates() {
      return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
   }

   void initAdapterManager() {
      this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
         void dispatchUpdate(AdapterHelper.UpdateOp var1) {
            int var2 = var1.cmd;
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 4) {
                     if (var2 == 8) {
                        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, var1.positionStart, var1.itemCount, 1);
                     }
                  } else {
                     RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, var1.positionStart, var1.itemCount, var1.payload);
                  }
               } else {
                  RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, var1.positionStart, var1.itemCount);
               }
            } else {
               RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, var1.positionStart, var1.itemCount);
            }
         }

         public RecyclerView.ViewHolder findViewHolder(int var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.this.findViewHolderForPosition(var1, true);
            if (var2 == null) {
               return null;
            } else {
               return RecyclerView.this.mChildHelper.isHidden(var2.itemView) ? null : var2;
            }
         }

         public void markViewHoldersUpdated(int var1, int var2, Object var3) {
            RecyclerView.this.viewRangeUpdate(var1, var2, var3);
            RecyclerView.this.mItemsChanged = true;
         }

         public void offsetPositionsForAdd(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForInsert(var1, var2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void offsetPositionsForMove(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForMove(var1, var2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void offsetPositionsForRemovingInvisible(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForRemove(var1, var2, true);
            RecyclerView.this.mItemsAddedOrRemoved = true;
            RecyclerView.State var3 = RecyclerView.this.mState;
            var3.mDeletedInvisibleItemCountSincePreviousLayout += var2;
         }

         public void offsetPositionsForRemovingLaidOutOrNewView(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForRemove(var1, var2, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void onDispatchFirstPass(AdapterHelper.UpdateOp var1) {
            this.dispatchUpdate(var1);
         }

         public void onDispatchSecondPass(AdapterHelper.UpdateOp var1) {
            this.dispatchUpdate(var1);
         }
      });
   }

   void initFastScroller(StateListDrawable var1, Drawable var2, StateListDrawable var3, Drawable var4) {
      if (var1 != null && var2 != null && var3 != null && var4 != null) {
         Resources var5 = this.getContext().getResources();
         new FastScroller(this, var1, var2, var3, var4, var5.getDimensionPixelSize(dimen.fastscroll_default_thickness), var5.getDimensionPixelSize(dimen.fastscroll_minimum_range), var5.getDimensionPixelOffset(dimen.fastscroll_margin));
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("Trying to set fast scroller without both required drawables.");
         var6.append(this.exceptionLabel());
         throw new IllegalArgumentException(var6.toString());
      }
   }

   void invalidateGlows() {
      this.mBottomGlow = null;
      this.mTopGlow = null;
      this.mRightGlow = null;
      this.mLeftGlow = null;
   }

   public void invalidateItemDecorations() {
      if (this.mItemDecorations.size() != 0) {
         RecyclerView.LayoutManager var1 = this.mLayout;
         if (var1 != null) {
            var1.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
         }

         this.markItemDecorInsetsDirty();
         this.requestLayout();
      }
   }

   boolean isAccessibilityEnabled() {
      AccessibilityManager var1 = this.mAccessibilityManager;
      return var1 != null && var1.isEnabled();
   }

   public boolean isAnimating() {
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      return var1 != null && var1.isRunning();
   }

   public boolean isAttachedToWindow() {
      return this.mIsAttached;
   }

   public boolean isComputingLayout() {
      return this.mLayoutOrScrollCounter > 0;
   }

   @Deprecated
   public boolean isLayoutFrozen() {
      return this.isLayoutSuppressed();
   }

   public final boolean isLayoutSuppressed() {
      return this.mLayoutSuppressed;
   }

   public boolean isNestedScrollingEnabled() {
      return this.getScrollingChildHelper().isNestedScrollingEnabled();
   }

   void jumpToPositionForSmoothScroller(int var1) {
      if (this.mLayout != null) {
         this.setScrollState(2);
         this.mLayout.scrollToPosition(var1);
         this.awakenScrollBars();
      }
   }

   void markItemDecorInsetsDirty() {
      int var2 = this.mChildHelper.getUnfilteredChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((RecyclerView.LayoutParams)this.mChildHelper.getUnfilteredChildAt(var1).getLayoutParams()).mInsetsDirty = true;
      }

      this.mRecycler.markItemDecorInsetsDirty();
   }

   void markKnownViewsInvalid() {
      int var2 = this.mChildHelper.getUnfilteredChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var1));
         if (var3 != null && !var3.shouldIgnore()) {
            var3.addFlags(6);
         }
      }

      this.markItemDecorInsetsDirty();
      this.mRecycler.markKnownViewsInvalid();
   }

   public void offsetChildrenHorizontal(int var1) {
      int var3 = this.mChildHelper.getChildCount();

      for(int var2 = 0; var2 < var3; ++var2) {
         this.mChildHelper.getChildAt(var2).offsetLeftAndRight(var1);
      }

   }

   public void offsetChildrenVertical(int var1) {
      int var3 = this.mChildHelper.getChildCount();

      for(int var2 = 0; var2 < var3; ++var2) {
         this.mChildHelper.getChildAt(var2).offsetTopAndBottom(var1);
      }

   }

   void offsetPositionRecordsForInsert(int var1, int var2) {
      int var4 = this.mChildHelper.getUnfilteredChildCount();

      for(int var3 = 0; var3 < var4; ++var3) {
         RecyclerView.ViewHolder var5 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var3));
         if (var5 != null && !var5.shouldIgnore() && var5.mPosition >= var1) {
            var5.offsetPosition(var2, false);
            this.mState.mStructureChanged = true;
         }
      }

      this.mRecycler.offsetPositionRecordsForInsert(var1, var2);
      this.requestLayout();
   }

   void offsetPositionRecordsForMove(int var1, int var2) {
      int var7 = this.mChildHelper.getUnfilteredChildCount();
      int var3;
      int var4;
      byte var5;
      if (var1 < var2) {
         var3 = var1;
         var4 = var2;
         var5 = -1;
      } else {
         var3 = var2;
         var4 = var1;
         var5 = 1;
      }

      for(int var6 = 0; var6 < var7; ++var6) {
         RecyclerView.ViewHolder var8 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var6));
         if (var8 != null && var8.mPosition >= var3 && var8.mPosition <= var4) {
            if (var8.mPosition == var1) {
               var8.offsetPosition(var2 - var1, false);
            } else {
               var8.offsetPosition(var5, false);
            }

            this.mState.mStructureChanged = true;
         }
      }

      this.mRecycler.offsetPositionRecordsForMove(var1, var2);
      this.requestLayout();
   }

   void offsetPositionRecordsForRemove(int var1, int var2, boolean var3) {
      int var5 = this.mChildHelper.getUnfilteredChildCount();

      for(int var4 = 0; var4 < var5; ++var4) {
         RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var4));
         if (var6 != null && !var6.shouldIgnore()) {
            if (var6.mPosition >= var1 + var2) {
               var6.offsetPosition(-var2, var3);
               this.mState.mStructureChanged = true;
            } else if (var6.mPosition >= var1) {
               var6.flagRemovedAndOffsetPosition(var1 - 1, -var2, var3);
               this.mState.mStructureChanged = true;
            }
         }
      }

      this.mRecycler.offsetPositionRecordsForRemove(var1, var2, var3);
      this.requestLayout();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mLayoutOrScrollCounter = 0;
      boolean var4 = true;
      this.mIsAttached = true;
      if (!this.mFirstLayoutComplete || this.isLayoutRequested()) {
         var4 = false;
      }

      this.mFirstLayoutComplete = var4;
      RecyclerView.LayoutManager var5 = this.mLayout;
      if (var5 != null) {
         var5.dispatchAttachedToWindow(this);
      }

      this.mPostedAnimatorRunner = false;
      if (ALLOW_THREAD_GAP_WORK) {
         GapWorker var6 = (GapWorker)GapWorker.sGapWorker.get();
         this.mGapWorker = var6;
         if (var6 == null) {
            this.mGapWorker = new GapWorker();
            Display var7 = ViewCompat.getDisplay(this);
            float var2 = 60.0F;
            float var1 = var2;
            if (!this.isInEditMode()) {
               var1 = var2;
               if (var7 != null) {
                  float var3 = var7.getRefreshRate();
                  var1 = var2;
                  if (var3 >= 30.0F) {
                     var1 = var3;
                  }
               }
            }

            this.mGapWorker.mFrameIntervalNs = (long)(1.0E9F / var1);
            GapWorker.sGapWorker.set(this.mGapWorker);
         }

         this.mGapWorker.add(this);
      }

   }

   public void onChildAttachedToWindow(View var1) {
   }

   public void onChildDetachedFromWindow(View var1) {
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      if (var1 != null) {
         var1.endAnimations();
      }

      this.stopScroll();
      this.mIsAttached = false;
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.dispatchDetachedFromWindow(this, this.mRecycler);
      }

      this.mPendingAccessibilityImportanceChange.clear();
      this.removeCallbacks(this.mItemAnimatorRunner);
      this.mViewInfoStore.onDetach();
      if (ALLOW_THREAD_GAP_WORK) {
         GapWorker var3 = this.mGapWorker;
         if (var3 != null) {
            var3.remove(this);
            this.mGapWorker = null;
         }
      }

   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      int var3 = this.mItemDecorations.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var2)).onDraw(var1, this, this.mState);
      }

   }

   void onEnterLayoutOrScroll() {
      ++this.mLayoutOrScrollCounter;
   }

   void onExitLayoutOrScroll() {
      this.onExitLayoutOrScroll(true);
   }

   void onExitLayoutOrScroll(boolean var1) {
      int var2 = this.mLayoutOrScrollCounter - 1;
      this.mLayoutOrScrollCounter = var2;
      if (var2 < 1) {
         this.mLayoutOrScrollCounter = 0;
         if (var1) {
            this.dispatchContentChangedIfNecessary();
            this.dispatchPendingImportantForAccessibilityChanges();
         }
      }

   }

   public boolean onGenericMotionEvent(MotionEvent var1) {
      if (this.mLayout == null) {
         return false;
      } else if (this.mLayoutSuppressed) {
         return false;
      } else {
         if (var1.getAction() == 8) {
            float var2;
            float var3;
            if ((var1.getSource() & 2) != 0) {
               if (this.mLayout.canScrollVertically()) {
                  var2 = -var1.getAxisValue(9);
               } else {
                  var2 = 0.0F;
               }

               if (this.mLayout.canScrollHorizontally()) {
                  var3 = var1.getAxisValue(10);
               } else {
                  var3 = 0.0F;
               }
            } else if ((var1.getSource() & 4194304) != 0) {
               var3 = var1.getAxisValue(26);
               if (this.mLayout.canScrollVertically()) {
                  var2 = -var3;
                  var3 = 0.0F;
               } else if (this.mLayout.canScrollHorizontally()) {
                  var2 = 0.0F;
               } else {
                  var2 = 0.0F;
                  var3 = 0.0F;
               }
            } else {
               var2 = 0.0F;
               var3 = 0.0F;
            }

            if (var2 != 0.0F || var3 != 0.0F) {
               this.scrollByInternal((int)(this.mScaledHorizontalScrollFactor * var3), (int)(this.mScaledVerticalScrollFactor * var2), var1);
            }
         }

         return false;
      }
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var9 = this.mLayoutSuppressed;
      boolean var8 = false;
      if (var9) {
         return false;
      } else {
         this.mInterceptingOnItemTouchListener = null;
         if (this.findInterceptingOnItemTouchListener(var1)) {
            this.cancelScroll();
            return true;
         } else {
            RecyclerView.LayoutManager var11 = this.mLayout;
            if (var11 == null) {
               return false;
            } else {
               var9 = var11.canScrollHorizontally();
               boolean var10 = this.mLayout.canScrollVertically();
               if (this.mVelocityTracker == null) {
                  this.mVelocityTracker = VelocityTracker.obtain();
               }

               this.mVelocityTracker.addMovement(var1);
               int var3 = var1.getActionMasked();
               int var2 = var1.getActionIndex();
               if (var3 != 0) {
                  if (var3 != 1) {
                     if (var3 != 2) {
                        if (var3 != 3) {
                           if (var3 != 5) {
                              if (var3 == 6) {
                                 this.onPointerUp(var1);
                              }
                           } else {
                              this.mScrollPointerId = var1.getPointerId(var2);
                              var3 = (int)(var1.getX(var2) + 0.5F);
                              this.mLastTouchX = var3;
                              this.mInitialTouchX = var3;
                              var2 = (int)(var1.getY(var2) + 0.5F);
                              this.mLastTouchY = var2;
                              this.mInitialTouchY = var2;
                           }
                        } else {
                           this.cancelScroll();
                        }
                     } else {
                        var2 = var1.findPointerIndex(this.mScrollPointerId);
                        if (var2 < 0) {
                           StringBuilder var12 = new StringBuilder();
                           var12.append("Error processing scroll; pointer index for id ");
                           var12.append(this.mScrollPointerId);
                           var12.append(" not found. Did any MotionEvents get skipped?");
                           Log.e("RecyclerView", var12.toString());
                           return false;
                        }

                        int var6 = (int)(var1.getX(var2) + 0.5F);
                        int var4 = (int)(var1.getY(var2) + 0.5F);
                        if (this.mScrollState != 1) {
                           int var7 = this.mInitialTouchX;
                           int var5 = this.mInitialTouchY;
                           boolean var15 = false;
                           boolean var14 = var15;
                           if (var9) {
                              var14 = var15;
                              if (Math.abs(var6 - var7) > this.mTouchSlop) {
                                 this.mLastTouchX = var6;
                                 var14 = true;
                              }
                           }

                           var15 = var14;
                           if (var10) {
                              var15 = var14;
                              if (Math.abs(var4 - var5) > this.mTouchSlop) {
                                 this.mLastTouchY = var4;
                                 var15 = true;
                              }
                           }

                           if (var15) {
                              this.setScrollState(1);
                           }
                        }
                     }
                  } else {
                     this.mVelocityTracker.clear();
                     this.stopNestedScroll(0);
                  }
               } else {
                  if (this.mIgnoreMotionEventTillDown) {
                     this.mIgnoreMotionEventTillDown = false;
                  }

                  this.mScrollPointerId = var1.getPointerId(0);
                  var2 = (int)(var1.getX() + 0.5F);
                  this.mLastTouchX = var2;
                  this.mInitialTouchX = var2;
                  var2 = (int)(var1.getY() + 0.5F);
                  this.mLastTouchY = var2;
                  this.mInitialTouchY = var2;
                  if (this.mScrollState == 2) {
                     this.getParent().requestDisallowInterceptTouchEvent(true);
                     this.setScrollState(1);
                     this.stopNestedScroll(1);
                  }

                  int[] var13 = this.mNestedOffsets;
                  var13[1] = 0;
                  var13[0] = 0;
                  var2 = 0;
                  if (var9) {
                     var2 = 0 | 1;
                  }

                  var3 = var2;
                  if (var10) {
                     var3 = var2 | 2;
                  }

                  this.startNestedScroll(var3, 0);
               }

               if (this.mScrollState == 1) {
                  var8 = true;
               }

               return var8;
            }
         }
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      TraceCompat.beginSection("RV OnLayout");
      this.dispatchLayout();
      TraceCompat.endSection();
      this.mFirstLayoutComplete = true;
   }

   protected void onMeasure(int var1, int var2) {
      RecyclerView.LayoutManager var8 = this.mLayout;
      if (var8 == null) {
         this.defaultOnMeasure(var1, var2);
      } else {
         boolean var7 = var8.isAutoMeasureEnabled();
         boolean var4 = false;
         if (var7) {
            int var5 = MeasureSpec.getMode(var1);
            int var6 = MeasureSpec.getMode(var2);
            this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
            boolean var3 = var4;
            if (var5 == 1073741824) {
               var3 = var4;
               if (var6 == 1073741824) {
                  var3 = true;
               }
            }

            if (!var3) {
               if (this.mAdapter != null) {
                  if (this.mState.mLayoutStep == 1) {
                     this.dispatchLayoutStep1();
                  }

                  this.mLayout.setMeasureSpecs(var1, var2);
                  this.mState.mIsMeasuring = true;
                  this.dispatchLayoutStep2();
                  this.mLayout.setMeasuredDimensionFromChildren(var1, var2);
                  if (this.mLayout.shouldMeasureTwice()) {
                     this.mLayout.setMeasureSpecs(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
                     this.mState.mIsMeasuring = true;
                     this.dispatchLayoutStep2();
                     this.mLayout.setMeasuredDimensionFromChildren(var1, var2);
                  }

               }
            }
         } else if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
         } else {
            if (this.mAdapterUpdateDuringMeasure) {
               this.startInterceptRequestLayout();
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
               this.stopInterceptRequestLayout(false);
            } else if (this.mState.mRunPredictiveAnimations) {
               this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight());
               return;
            }

            RecyclerView.Adapter var9 = this.mAdapter;
            if (var9 != null) {
               this.mState.mItemCount = var9.getItemCount();
            } else {
               this.mState.mItemCount = 0;
            }

            this.startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
            this.stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
         }
      }
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      return this.isComputingLayout() ? false : super.onRequestFocusInDescendants(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof RecyclerView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         RecyclerView.SavedState var2 = (RecyclerView.SavedState)var1;
         this.mPendingSavedState = var2;
         super.onRestoreInstanceState(var2.getSuperState());
         if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
         }

      }
   }

   protected Parcelable onSaveInstanceState() {
      RecyclerView.SavedState var1 = new RecyclerView.SavedState(super.onSaveInstanceState());
      RecyclerView.SavedState var2 = this.mPendingSavedState;
      if (var2 != null) {
         var1.copyFrom(var2);
         return var1;
      } else {
         RecyclerView.LayoutManager var3 = this.mLayout;
         if (var3 != null) {
            var1.mLayoutState = var3.onSaveInstanceState();
            return var1;
         } else {
            var1.mLayoutState = null;
            return var1;
         }
      }
   }

   public void onScrollStateChanged(int var1) {
   }

   public void onScrolled(int var1, int var2) {
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3 || var2 != var4) {
         this.invalidateGlows();
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var13 = this.mLayoutSuppressed;
      byte var10 = 0;
      if (!var13) {
         if (this.mIgnoreMotionEventTillDown) {
            return false;
         } else if (this.dispatchToOnItemTouchListeners(var1)) {
            this.cancelScroll();
            return true;
         } else {
            RecyclerView.LayoutManager var15 = this.mLayout;
            if (var15 == null) {
               return false;
            } else {
               var13 = var15.canScrollHorizontally();
               boolean var14 = this.mLayout.canScrollVertically();
               if (this.mVelocityTracker == null) {
                  this.mVelocityTracker = VelocityTracker.obtain();
               }

               boolean var9 = false;
               int var5 = var1.getActionMasked();
               int var4 = var1.getActionIndex();
               if (var5 == 0) {
                  int[] var22 = this.mNestedOffsets;
                  var22[1] = 0;
                  var22[0] = 0;
               }

               MotionEvent var23 = MotionEvent.obtain(var1);
               int[] var16 = this.mNestedOffsets;
               var23.offsetLocation((float)var16[0], (float)var16[1]);
               boolean var19;
               if (var5 != 0) {
                  if (var5 != 1) {
                     if (var5 != 2) {
                        if (var5 != 3) {
                           if (var5 != 5) {
                              if (var5 != 6) {
                                 var19 = var9;
                              } else {
                                 this.onPointerUp(var1);
                                 var19 = var9;
                              }
                           } else {
                              this.mScrollPointerId = var1.getPointerId(var4);
                              var5 = (int)(var1.getX(var4) + 0.5F);
                              this.mLastTouchX = var5;
                              this.mInitialTouchX = var5;
                              var4 = (int)(var1.getY(var4) + 0.5F);
                              this.mLastTouchY = var4;
                              this.mInitialTouchY = var4;
                              var19 = var9;
                           }
                        } else {
                           this.cancelScroll();
                           var19 = var9;
                        }
                     } else {
                        var4 = var1.findPointerIndex(this.mScrollPointerId);
                        if (var4 < 0) {
                           StringBuilder var18 = new StringBuilder();
                           var18.append("Error processing scroll; pointer index for id ");
                           var18.append(this.mScrollPointerId);
                           var18.append(" not found. Did any MotionEvents get skipped?");
                           Log.e("RecyclerView", var18.toString());
                           return false;
                        }

                        int var11 = (int)(var1.getX(var4) + 0.5F);
                        int var12 = (int)(var1.getY(var4) + 0.5F);
                        int var6 = this.mLastTouchX - var11;
                        int var7 = this.mLastTouchY - var12;
                        if (this.mScrollState != 1) {
                           boolean var8 = false;
                           var4 = var6;
                           boolean var20 = var8;
                           if (var13) {
                              if (var6 > 0) {
                                 var6 = Math.max(0, var6 - this.mTouchSlop);
                              } else {
                                 var6 = Math.min(0, this.mTouchSlop + var6);
                              }

                              var4 = var6;
                              var20 = var8;
                              if (var6 != 0) {
                                 var20 = true;
                                 var4 = var6;
                              }
                           }

                           var6 = var7;
                           var8 = var20;
                           if (var14) {
                              if (var7 > 0) {
                                 var7 = Math.max(0, var7 - this.mTouchSlop);
                              } else {
                                 var7 = Math.min(0, this.mTouchSlop + var7);
                              }

                              var6 = var7;
                              var8 = var20;
                              if (var7 != 0) {
                                 var8 = true;
                                 var6 = var7;
                              }
                           }

                           if (var8) {
                              this.setScrollState(1);
                           }

                           var5 = var4;
                           var4 = var6;
                        } else {
                           var5 = var6;
                           var4 = var7;
                        }

                        if (this.mScrollState == 1) {
                           var16 = this.mReusableIntPair;
                           var16[0] = 0;
                           var16[1] = 0;
                           if (var13) {
                              var6 = var5;
                           } else {
                              var6 = 0;
                           }

                           if (var14) {
                              var7 = var4;
                           } else {
                              var7 = 0;
                           }

                           if (this.dispatchNestedPreScroll(var6, var7, this.mReusableIntPair, this.mScrollOffset, 0)) {
                              var16 = this.mReusableIntPair;
                              var7 = var16[0];
                              var6 = var16[1];
                              var16 = this.mNestedOffsets;
                              int var21 = var16[0];
                              int[] var17 = this.mScrollOffset;
                              var16[0] = var21 + var17[0];
                              var16[1] += var17[1];
                              this.getParent().requestDisallowInterceptTouchEvent(true);
                              var5 -= var7;
                              var6 = var4 - var6;
                              var4 = var5;
                              var5 = var6;
                           } else {
                              var6 = var5;
                              var5 = var4;
                              var4 = var6;
                           }

                           var16 = this.mScrollOffset;
                           this.mLastTouchX = var11 - var16[0];
                           this.mLastTouchY = var12 - var16[1];
                           if (var13) {
                              var6 = var4;
                           } else {
                              var6 = 0;
                           }

                           var7 = var10;
                           if (var14) {
                              var7 = var5;
                           }

                           if (this.scrollByInternal(var6, var7, var1)) {
                              this.getParent().requestDisallowInterceptTouchEvent(true);
                           }

                           if (this.mGapWorker != null && (var4 != 0 || var5 != 0)) {
                              this.mGapWorker.postFromTraversal(this, var4, var5);
                           }
                        }

                        var19 = var9;
                     }
                  } else {
                     this.mVelocityTracker.addMovement(var23);
                     var19 = true;
                     this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                     float var2;
                     if (var13) {
                        var2 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
                     } else {
                        var2 = 0.0F;
                     }

                     float var3;
                     if (var14) {
                        var3 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
                     } else {
                        var3 = 0.0F;
                     }

                     if (var2 == 0.0F && var3 == 0.0F || !this.fling((int)var2, (int)var3)) {
                        this.setScrollState(0);
                     }

                     this.resetScroll();
                  }
               } else {
                  this.mScrollPointerId = var1.getPointerId(0);
                  var4 = (int)(var1.getX() + 0.5F);
                  this.mLastTouchX = var4;
                  this.mInitialTouchX = var4;
                  var4 = (int)(var1.getY() + 0.5F);
                  this.mLastTouchY = var4;
                  this.mInitialTouchY = var4;
                  var4 = 0;
                  if (var13) {
                     var4 = 0 | 1;
                  }

                  var5 = var4;
                  if (var14) {
                     var5 = var4 | 2;
                  }

                  this.startNestedScroll(var5, 0);
                  var19 = var9;
               }

               if (!var19) {
                  this.mVelocityTracker.addMovement(var23);
               }

               var23.recycle();
               return true;
            }
         }
      } else {
         return false;
      }
   }

   void postAnimationRunner() {
      if (!this.mPostedAnimatorRunner && this.mIsAttached) {
         ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
         this.mPostedAnimatorRunner = true;
      }

   }

   void processDataSetCompletelyChanged(boolean var1) {
      this.mDispatchItemsChangedEvent |= var1;
      this.mDataSetHasChangedAfterLayout = true;
      this.markKnownViewsInvalid();
   }

   void recordAnimationInfoIfBouncedHiddenView(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      var1.setFlags(0, 8192);
      if (this.mState.mTrackOldChangeHolders && var1.isUpdated() && !var1.isRemoved() && !var1.shouldIgnore()) {
         long var3 = this.getChangedHolderKey(var1);
         this.mViewInfoStore.addToOldChangeHolders(var3, var1);
      }

      this.mViewInfoStore.addToPreLayout(var1, var2);
   }

   void removeAndRecycleViews() {
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      if (var1 != null) {
         var1.endAnimations();
      }

      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.removeAndRecycleAllViews(this.mRecycler);
         this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      }

      this.mRecycler.clear();
   }

   boolean removeAnimatingView(View var1) {
      this.startInterceptRequestLayout();
      boolean var2 = this.mChildHelper.removeViewIfHidden(var1);
      if (var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
         this.mRecycler.unscrapView(var3);
         this.mRecycler.recycleViewHolderInternal(var3);
      }

      this.stopInterceptRequestLayout(var2 ^ true);
      return var2;
   }

   protected void removeDetachedView(View var1, boolean var2) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      if (var3 != null) {
         if (var3.isTmpDetached()) {
            var3.clearTmpDetachFlag();
         } else if (!var3.shouldIgnore()) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
            var4.append(var3);
            var4.append(this.exceptionLabel());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      var1.clearAnimation();
      this.dispatchChildDetached(var1);
      super.removeDetachedView(var1, var2);
   }

   public void removeItemDecoration(RecyclerView.ItemDecoration var1) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 != null) {
         var3.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
      }

      this.mItemDecorations.remove(var1);
      if (this.mItemDecorations.isEmpty()) {
         boolean var2;
         if (this.getOverScrollMode() == 2) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.setWillNotDraw(var2);
      }

      this.markItemDecorInsetsDirty();
      this.requestLayout();
   }

   public void removeItemDecorationAt(int var1) {
      int var2 = this.getItemDecorationCount();
      if (var1 >= 0 && var1 < var2) {
         this.removeItemDecoration(this.getItemDecorationAt(var1));
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" is an invalid index for size ");
         var3.append(var2);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   public void removeOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener var1) {
      List var2 = this.mOnChildAttachStateListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener var1) {
      this.mOnItemTouchListeners.remove(var1);
      if (this.mInterceptingOnItemTouchListener == var1) {
         this.mInterceptingOnItemTouchListener = null;
      }

   }

   public void removeOnScrollListener(RecyclerView.OnScrollListener var1) {
      List var2 = this.mScrollListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   void repositionShadowingViews() {
      int var2 = this.mChildHelper.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         View var5 = this.mChildHelper.getChildAt(var1);
         RecyclerView.ViewHolder var6 = this.getChildViewHolder(var5);
         if (var6 != null && var6.mShadowingHolder != null) {
            View var7 = var6.mShadowingHolder.itemView;
            int var3 = var5.getLeft();
            int var4 = var5.getTop();
            if (var3 != var7.getLeft() || var4 != var7.getTop()) {
               var7.layout(var3, var4, var7.getWidth() + var3, var7.getHeight() + var4);
            }
         }
      }

   }

   public void requestChildFocus(View var1, View var2) {
      if (!this.mLayout.onRequestChildFocus(this, this.mState, var1, var2) && var2 != null) {
         this.requestChildOnScreen(var1, var2);
      }

      super.requestChildFocus(var1, var2);
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      return this.mLayout.requestChildRectangleOnScreen(this, var1, var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      int var3 = this.mOnItemTouchListeners.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((RecyclerView.OnItemTouchListener)this.mOnItemTouchListeners.get(var2)).onRequestDisallowInterceptTouchEvent(var1);
      }

      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void requestLayout() {
      if (this.mInterceptRequestLayoutDepth == 0 && !this.mLayoutSuppressed) {
         super.requestLayout();
      } else {
         this.mLayoutWasDefered = true;
      }
   }

   void saveOldPositions() {
      int var2 = this.mChildHelper.getUnfilteredChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var1));
         if (!var3.shouldIgnore()) {
            var3.saveOldPosition();
         }
      }

   }

   public void scrollBy(int var1, int var2) {
      RecyclerView.LayoutManager var6 = this.mLayout;
      if (var6 == null) {
         Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      } else if (!this.mLayoutSuppressed) {
         boolean var4 = var6.canScrollHorizontally();
         boolean var5 = this.mLayout.canScrollVertically();
         if (var4 || var5) {
            int var3 = 0;
            if (!var4) {
               var1 = 0;
            }

            if (var5) {
               var3 = var2;
            }

            this.scrollByInternal(var1, var3, (MotionEvent)null);
         }

      }
   }

   boolean scrollByInternal(int var1, int var2, MotionEvent var3) {
      this.consumePendingUpdateOperations();
      RecyclerView.Adapter var14 = this.mAdapter;
      boolean var13 = true;
      int var4;
      int var5;
      int var6;
      int var7;
      int[] var16;
      if (var14 != null) {
         var16 = this.mReusableIntPair;
         var16[0] = 0;
         var16[1] = 0;
         this.scrollStep(var1, var2, var16);
         var16 = this.mReusableIntPair;
         var5 = var16[0];
         var4 = var16[1];
         var6 = var1 - var5;
         var7 = var2 - var4;
      } else {
         var6 = 0;
         var7 = 0;
         var5 = 0;
         var4 = 0;
      }

      if (!this.mItemDecorations.isEmpty()) {
         this.invalidate();
      }

      var16 = this.mReusableIntPair;
      var16[0] = 0;
      var16[1] = 0;
      this.dispatchNestedScroll(var5, var4, var6, var7, this.mScrollOffset, 0, var16);
      var16 = this.mReusableIntPair;
      int var9 = var16[0];
      int var10 = var16[1];
      boolean var8;
      if (var16[0] == 0 && var16[1] == 0) {
         var8 = false;
      } else {
         var8 = true;
      }

      int var11 = this.mLastTouchX;
      var16 = this.mScrollOffset;
      this.mLastTouchX = var11 - var16[0];
      this.mLastTouchY -= var16[1];
      int[] var15 = this.mNestedOffsets;
      var15[0] += var16[0];
      var15[1] += var16[1];
      if (this.getOverScrollMode() != 2) {
         if (var3 != null && !MotionEventCompat.isFromSource(var3, 8194)) {
            this.pullGlows(var3.getX(), (float)(var6 - var9), var3.getY(), (float)(var7 - var10));
         }

         this.considerReleasingGlowsOnScroll(var1, var2);
      }

      if (var5 != 0 || var4 != 0) {
         this.dispatchOnScrolled(var5, var4);
      }

      if (!this.awakenScrollBars()) {
         this.invalidate();
      }

      boolean var12 = var13;
      if (!var8) {
         var12 = var13;
         if (var5 == 0) {
            if (var4 != 0) {
               return true;
            }

            var12 = false;
         }
      }

      return var12;
   }

   void scrollStep(int var1, int var2, int[] var3) {
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      TraceCompat.beginSection("RV Scroll");
      this.fillRemainingScrollValues(this.mState);
      int var4 = 0;
      byte var5 = 0;
      if (var1 != 0) {
         var4 = this.mLayout.scrollHorizontallyBy(var1, this.mRecycler, this.mState);
      }

      var1 = var5;
      if (var2 != 0) {
         var1 = this.mLayout.scrollVerticallyBy(var2, this.mRecycler, this.mState);
      }

      TraceCompat.endSection();
      this.repositionShadowingViews();
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      if (var3 != null) {
         var3[0] = var4;
         var3[1] = var1;
      }

   }

   public void scrollTo(int var1, int var2) {
      Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
   }

   public void scrollToPosition(int var1) {
      if (!this.mLayoutSuppressed) {
         this.stopScroll();
         RecyclerView.LayoutManager var2 = this.mLayout;
         if (var2 == null) {
            Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
         } else {
            var2.scrollToPosition(var1);
            this.awakenScrollBars();
         }
      }
   }

   public void sendAccessibilityEventUnchecked(AccessibilityEvent var1) {
      if (!this.shouldDeferAccessibilityEvent(var1)) {
         super.sendAccessibilityEventUnchecked(var1);
      }
   }

   public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate var1) {
      this.mAccessibilityDelegate = var1;
      ViewCompat.setAccessibilityDelegate(this, var1);
   }

   public void setAdapter(RecyclerView.Adapter var1) {
      this.setLayoutFrozen(false);
      this.setAdapterInternal(var1, false, true);
      this.processDataSetCompletelyChanged(false);
      this.requestLayout();
   }

   public void setChildDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback var1) {
      if (var1 != this.mChildDrawingOrderCallback) {
         this.mChildDrawingOrderCallback = var1;
         boolean var2;
         if (var1 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.setChildrenDrawingOrderEnabled(var2);
      }
   }

   boolean setChildImportantForAccessibilityInternal(RecyclerView.ViewHolder var1, int var2) {
      if (this.isComputingLayout()) {
         var1.mPendingAccessibilityState = var2;
         this.mPendingAccessibilityImportanceChange.add(var1);
         return false;
      } else {
         ViewCompat.setImportantForAccessibility(var1.itemView, var2);
         return true;
      }
   }

   public void setClipToPadding(boolean var1) {
      if (var1 != this.mClipToPadding) {
         this.invalidateGlows();
      }

      this.mClipToPadding = var1;
      super.setClipToPadding(var1);
      if (this.mFirstLayoutComplete) {
         this.requestLayout();
      }

   }

   public void setEdgeEffectFactory(RecyclerView.EdgeEffectFactory var1) {
      Preconditions.checkNotNull(var1);
      this.mEdgeEffectFactory = var1;
      this.invalidateGlows();
   }

   public void setHasFixedSize(boolean var1) {
      this.mHasFixedSize = var1;
   }

   public void setItemAnimator(RecyclerView.ItemAnimator var1) {
      RecyclerView.ItemAnimator var2 = this.mItemAnimator;
      if (var2 != null) {
         var2.endAnimations();
         this.mItemAnimator.setListener((RecyclerView.ItemAnimator.ItemAnimatorListener)null);
      }

      this.mItemAnimator = var1;
      if (var1 != null) {
         var1.setListener(this.mItemAnimatorListener);
      }

   }

   public void setItemViewCacheSize(int var1) {
      this.mRecycler.setViewCacheSize(var1);
   }

   @Deprecated
   public void setLayoutFrozen(boolean var1) {
      this.suppressLayout(var1);
   }

   public void setLayoutManager(RecyclerView.LayoutManager var1) {
      if (var1 != this.mLayout) {
         this.stopScroll();
         if (this.mLayout != null) {
            RecyclerView.ItemAnimator var2 = this.mItemAnimator;
            if (var2 != null) {
               var2.endAnimations();
            }

            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            this.mRecycler.clear();
            if (this.mIsAttached) {
               this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }

            this.mLayout.setRecyclerView((RecyclerView)null);
            this.mLayout = null;
         } else {
            this.mRecycler.clear();
         }

         this.mChildHelper.removeAllViewsUnfiltered();
         this.mLayout = var1;
         if (var1 != null) {
            if (var1.mRecyclerView != null) {
               StringBuilder var3 = new StringBuilder();
               var3.append("LayoutManager ");
               var3.append(var1);
               var3.append(" is already attached to a RecyclerView:");
               var3.append(var1.mRecyclerView.exceptionLabel());
               throw new IllegalArgumentException(var3.toString());
            }

            this.mLayout.setRecyclerView(this);
            if (this.mIsAttached) {
               this.mLayout.dispatchAttachedToWindow(this);
            }
         }

         this.mRecycler.updateViewCacheSize();
         this.requestLayout();
      }
   }

   @Deprecated
   public void setLayoutTransition(LayoutTransition var1) {
      if (VERSION.SDK_INT < 18) {
         if (var1 == null) {
            this.suppressLayout(false);
            return;
         }

         if (var1.getAnimator(0) == null && var1.getAnimator(1) == null && var1.getAnimator(2) == null && var1.getAnimator(3) == null && var1.getAnimator(4) == null) {
            this.suppressLayout(true);
            return;
         }
      }

      if (var1 == null) {
         super.setLayoutTransition((LayoutTransition)null);
      } else {
         throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
      }
   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.getScrollingChildHelper().setNestedScrollingEnabled(var1);
   }

   public void setOnFlingListener(RecyclerView.OnFlingListener var1) {
      this.mOnFlingListener = var1;
   }

   @Deprecated
   public void setOnScrollListener(RecyclerView.OnScrollListener var1) {
      this.mScrollListener = var1;
   }

   public void setPreserveFocusAfterLayout(boolean var1) {
      this.mPreserveFocusAfterLayout = var1;
   }

   public void setRecycledViewPool(RecyclerView.RecycledViewPool var1) {
      this.mRecycler.setRecycledViewPool(var1);
   }

   public void setRecyclerListener(RecyclerView.RecyclerListener var1) {
      this.mRecyclerListener = var1;
   }

   void setScrollState(int var1) {
      if (var1 != this.mScrollState) {
         this.mScrollState = var1;
         if (var1 != 2) {
            this.stopScrollersInternal();
         }

         this.dispatchOnScrollStateChanged(var1);
      }
   }

   public void setScrollingTouchSlop(int var1) {
      ViewConfiguration var2 = ViewConfiguration.get(this.getContext());
      if (var1 != 0) {
         if (var1 == 1) {
            this.mTouchSlop = var2.getScaledPagingTouchSlop();
            return;
         }

         StringBuilder var3 = new StringBuilder();
         var3.append("setScrollingTouchSlop(): bad argument constant ");
         var3.append(var1);
         var3.append("; using default value");
         Log.w("RecyclerView", var3.toString());
      }

      this.mTouchSlop = var2.getScaledTouchSlop();
   }

   public void setViewCacheExtension(RecyclerView.ViewCacheExtension var1) {
      this.mRecycler.setViewCacheExtension(var1);
   }

   boolean shouldDeferAccessibilityEvent(AccessibilityEvent var1) {
      if (this.isComputingLayout()) {
         int var2 = 0;
         if (var1 != null) {
            var2 = AccessibilityEventCompat.getContentChangeTypes(var1);
         }

         int var3 = var2;
         if (var2 == 0) {
            var3 = 0;
         }

         this.mEatenAccessibilityChangeFlags |= var3;
         return true;
      } else {
         return false;
      }
   }

   public void smoothScrollBy(int var1, int var2) {
      this.smoothScrollBy(var1, var2, (Interpolator)null);
   }

   public void smoothScrollBy(int var1, int var2, Interpolator var3) {
      this.smoothScrollBy(var1, var2, var3, Integer.MIN_VALUE);
   }

   public void smoothScrollBy(int var1, int var2, Interpolator var3, int var4) {
      this.smoothScrollBy(var1, var2, var3, var4, false);
   }

   void smoothScrollBy(int var1, int var2, Interpolator var3, int var4, boolean var5) {
      RecyclerView.LayoutManager var8 = this.mLayout;
      if (var8 == null) {
         Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      } else if (!this.mLayoutSuppressed) {
         int var6 = var1;
         if (!var8.canScrollHorizontally()) {
            var6 = 0;
         }

         if (!this.mLayout.canScrollVertically()) {
            var2 = 0;
         }

         if (var6 != 0 || var2 != 0) {
            boolean var9;
            if (var4 != Integer.MIN_VALUE && var4 <= 0) {
               var9 = false;
            } else {
               var9 = true;
            }

            if (var9) {
               if (var5) {
                  var1 = 0;
                  if (var6 != 0) {
                     var1 = 0 | 1;
                  }

                  int var7 = var1;
                  if (var2 != 0) {
                     var7 = var1 | 2;
                  }

                  this.startNestedScroll(var7, 1);
               }

               this.mViewFlinger.smoothScrollBy(var6, var2, var4, var3);
               return;
            }

            this.scrollBy(var6, var2);
         }

      }
   }

   public void smoothScrollToPosition(int var1) {
      if (!this.mLayoutSuppressed) {
         RecyclerView.LayoutManager var2 = this.mLayout;
         if (var2 == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
         } else {
            var2.smoothScrollToPosition(this, this.mState, var1);
         }
      }
   }

   void startInterceptRequestLayout() {
      int var1 = this.mInterceptRequestLayoutDepth + 1;
      this.mInterceptRequestLayoutDepth = var1;
      if (var1 == 1 && !this.mLayoutSuppressed) {
         this.mLayoutWasDefered = false;
      }

   }

   public boolean startNestedScroll(int var1) {
      return this.getScrollingChildHelper().startNestedScroll(var1);
   }

   public boolean startNestedScroll(int var1, int var2) {
      return this.getScrollingChildHelper().startNestedScroll(var1, var2);
   }

   void stopInterceptRequestLayout(boolean var1) {
      if (this.mInterceptRequestLayoutDepth < 1) {
         this.mInterceptRequestLayoutDepth = 1;
      }

      if (!var1 && !this.mLayoutSuppressed) {
         this.mLayoutWasDefered = false;
      }

      if (this.mInterceptRequestLayoutDepth == 1) {
         if (var1 && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null) {
            this.dispatchLayout();
         }

         if (!this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
         }
      }

      --this.mInterceptRequestLayoutDepth;
   }

   public void stopNestedScroll() {
      this.getScrollingChildHelper().stopNestedScroll();
   }

   public void stopNestedScroll(int var1) {
      this.getScrollingChildHelper().stopNestedScroll(var1);
   }

   public void stopScroll() {
      this.setScrollState(0);
      this.stopScrollersInternal();
   }

   public final void suppressLayout(boolean var1) {
      if (var1 != this.mLayoutSuppressed) {
         this.assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
         if (!var1) {
            this.mLayoutSuppressed = false;
            if (this.mLayoutWasDefered && this.mLayout != null && this.mAdapter != null) {
               this.requestLayout();
            }

            this.mLayoutWasDefered = false;
            return;
         }

         long var2 = SystemClock.uptimeMillis();
         this.onTouchEvent(MotionEvent.obtain(var2, var2, 3, 0.0F, 0.0F, 0));
         this.mLayoutSuppressed = true;
         this.mIgnoreMotionEventTillDown = true;
         this.stopScroll();
      }

   }

   public void swapAdapter(RecyclerView.Adapter var1, boolean var2) {
      this.setLayoutFrozen(false);
      this.setAdapterInternal(var1, true, var2);
      this.processDataSetCompletelyChanged(true);
      this.requestLayout();
   }

   void viewRangeUpdate(int var1, int var2, Object var3) {
      int var5 = this.mChildHelper.getUnfilteredChildCount();

      for(int var4 = 0; var4 < var5; ++var4) {
         View var6 = this.mChildHelper.getUnfilteredChildAt(var4);
         RecyclerView.ViewHolder var7 = getChildViewHolderInt(var6);
         if (var7 != null && !var7.shouldIgnore() && var7.mPosition >= var1 && var7.mPosition < var1 + var2) {
            var7.addFlags(2);
            var7.addChangePayload(var3);
            ((RecyclerView.LayoutParams)var6.getLayoutParams()).mInsetsDirty = true;
         }
      }

      this.mRecycler.viewRangeUpdate(var1, var2);
   }

   public abstract static class Adapter {
      private boolean mHasStableIds = false;
      private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();

      public final void bindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         var1.mPosition = var2;
         if (this.hasStableIds()) {
            var1.mItemId = this.getItemId(var2);
         }

         var1.setFlags(1, 519);
         TraceCompat.beginSection("RV OnBindView");
         this.onBindViewHolder(var1, var2, var1.getUnmodifiedPayloads());
         var1.clearPayload();
         android.view.ViewGroup.LayoutParams var3 = var1.itemView.getLayoutParams();
         if (var3 instanceof RecyclerView.LayoutParams) {
            ((RecyclerView.LayoutParams)var3).mInsetsDirty = true;
         }

         TraceCompat.endSection();
      }

      public final RecyclerView.ViewHolder createViewHolder(ViewGroup var1, int var2) {
         RecyclerView.ViewHolder var5;
         try {
            TraceCompat.beginSection("RV CreateView");
            var5 = this.onCreateViewHolder(var1, var2);
            if (var5.itemView.getParent() != null) {
               throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
            }

            var5.mItemViewType = var2;
         } finally {
            TraceCompat.endSection();
         }

         return var5;
      }

      public abstract int getItemCount();

      public long getItemId(int var1) {
         return -1L;
      }

      public int getItemViewType(int var1) {
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

      public final void notifyItemChanged(int var1) {
         this.mObservable.notifyItemRangeChanged(var1, 1);
      }

      public final void notifyItemChanged(int var1, Object var2) {
         this.mObservable.notifyItemRangeChanged(var1, 1, var2);
      }

      public final void notifyItemInserted(int var1) {
         this.mObservable.notifyItemRangeInserted(var1, 1);
      }

      public final void notifyItemMoved(int var1, int var2) {
         this.mObservable.notifyItemMoved(var1, var2);
      }

      public final void notifyItemRangeChanged(int var1, int var2) {
         this.mObservable.notifyItemRangeChanged(var1, var2);
      }

      public final void notifyItemRangeChanged(int var1, int var2, Object var3) {
         this.mObservable.notifyItemRangeChanged(var1, var2, var3);
      }

      public final void notifyItemRangeInserted(int var1, int var2) {
         this.mObservable.notifyItemRangeInserted(var1, var2);
      }

      public final void notifyItemRangeRemoved(int var1, int var2) {
         this.mObservable.notifyItemRangeRemoved(var1, var2);
      }

      public final void notifyItemRemoved(int var1) {
         this.mObservable.notifyItemRangeRemoved(var1, 1);
      }

      public void onAttachedToRecyclerView(RecyclerView var1) {
      }

      public abstract void onBindViewHolder(RecyclerView.ViewHolder var1, int var2);

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2, List var3) {
         this.onBindViewHolder(var1, var2);
      }

      public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2);

      public void onDetachedFromRecyclerView(RecyclerView var1) {
      }

      public boolean onFailedToRecycleView(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
      }

      public void onViewDetachedFromWindow(RecyclerView.ViewHolder var1) {
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
      }

      public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
         this.mObservable.registerObserver(var1);
      }

      public void setHasStableIds(boolean var1) {
         if (!this.hasObservers()) {
            this.mHasStableIds = var1;
         } else {
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
         }
      }

      public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
         this.mObservable.unregisterObserver(var1);
      }
   }

   static class AdapterDataObservable extends Observable {
      public boolean hasObservers() {
         return this.mObservers.isEmpty() ^ true;
      }

      public void notifyChanged() {
         for(int var1 = this.mObservers.size() - 1; var1 >= 0; --var1) {
            ((RecyclerView.AdapterDataObserver)this.mObservers.get(var1)).onChanged();
         }

      }

      public void notifyItemMoved(int var1, int var2) {
         for(int var3 = this.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)this.mObservers.get(var3)).onItemRangeMoved(var1, var2, 1);
         }

      }

      public void notifyItemRangeChanged(int var1, int var2) {
         this.notifyItemRangeChanged(var1, var2, (Object)null);
      }

      public void notifyItemRangeChanged(int var1, int var2, Object var3) {
         for(int var4 = this.mObservers.size() - 1; var4 >= 0; --var4) {
            ((RecyclerView.AdapterDataObserver)this.mObservers.get(var4)).onItemRangeChanged(var1, var2, var3);
         }

      }

      public void notifyItemRangeInserted(int var1, int var2) {
         for(int var3 = this.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)this.mObservers.get(var3)).onItemRangeInserted(var1, var2);
         }

      }

      public void notifyItemRangeRemoved(int var1, int var2) {
         for(int var3 = this.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)this.mObservers.get(var3)).onItemRangeRemoved(var1, var2);
         }

      }
   }

   public abstract static class AdapterDataObserver {
      public void onChanged() {
      }

      public void onItemRangeChanged(int var1, int var2) {
      }

      public void onItemRangeChanged(int var1, int var2, Object var3) {
         this.onItemRangeChanged(var1, var2);
      }

      public void onItemRangeInserted(int var1, int var2) {
      }

      public void onItemRangeMoved(int var1, int var2, int var3) {
      }

      public void onItemRangeRemoved(int var1, int var2) {
      }
   }

   public interface ChildDrawingOrderCallback {
      int onGetChildDrawingOrder(int var1, int var2);
   }

   public static class EdgeEffectFactory {
      public static final int DIRECTION_BOTTOM = 3;
      public static final int DIRECTION_LEFT = 0;
      public static final int DIRECTION_RIGHT = 2;
      public static final int DIRECTION_TOP = 1;

      protected EdgeEffect createEdgeEffect(RecyclerView var1, int var2) {
         return new EdgeEffect(var1.getContext());
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface EdgeDirection {
      }
   }

   public abstract static class ItemAnimator {
      public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
      public static final int FLAG_CHANGED = 2;
      public static final int FLAG_INVALIDATED = 4;
      public static final int FLAG_MOVED = 2048;
      public static final int FLAG_REMOVED = 8;
      private long mAddDuration = 120L;
      private long mChangeDuration = 250L;
      private ArrayList mFinishedListeners = new ArrayList();
      private RecyclerView.ItemAnimator.ItemAnimatorListener mListener = null;
      private long mMoveDuration = 250L;
      private long mRemoveDuration = 120L;

      static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder var0) {
         int var2 = var0.mFlags & 14;
         if (var0.isInvalid()) {
            return 4;
         } else {
            int var1 = var2;
            if ((var2 & 4) == 0) {
               int var3 = var0.getOldPosition();
               int var4 = var0.getAdapterPosition();
               var1 = var2;
               if (var3 != -1) {
                  var1 = var2;
                  if (var4 != -1) {
                     var1 = var2;
                     if (var3 != var4) {
                        var1 = var2 | 2048;
                     }
                  }
               }
            }

            return var1;
         }
      }

      public abstract boolean animateAppearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public abstract boolean animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, RecyclerView.ItemAnimator.ItemHolderInfo var3, RecyclerView.ItemAnimator.ItemHolderInfo var4);

      public abstract boolean animateDisappearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public abstract boolean animatePersistence(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1) {
         return true;
      }

      public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1, List var2) {
         return this.canReuseUpdatedViewHolder(var1);
      }

      public final void dispatchAnimationFinished(RecyclerView.ViewHolder var1) {
         this.onAnimationFinished(var1);
         RecyclerView.ItemAnimator.ItemAnimatorListener var2 = this.mListener;
         if (var2 != null) {
            var2.onAnimationFinished(var1);
         }

      }

      public final void dispatchAnimationStarted(RecyclerView.ViewHolder var1) {
         this.onAnimationStarted(var1);
      }

      public final void dispatchAnimationsFinished() {
         int var2 = this.mFinishedListeners.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ((RecyclerView.ItemAnimator.ItemAnimatorFinishedListener)this.mFinishedListeners.get(var1)).onAnimationsFinished();
         }

         this.mFinishedListeners.clear();
      }

      public abstract void endAnimation(RecyclerView.ViewHolder var1);

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

      public final boolean isRunning(RecyclerView.ItemAnimator.ItemAnimatorFinishedListener var1) {
         boolean var2 = this.isRunning();
         if (var1 != null) {
            if (!var2) {
               var1.onAnimationsFinished();
               return var2;
            }

            this.mFinishedListeners.add(var1);
         }

         return var2;
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo obtainHolderInfo() {
         return new RecyclerView.ItemAnimator.ItemHolderInfo();
      }

      public void onAnimationFinished(RecyclerView.ViewHolder var1) {
      }

      public void onAnimationStarted(RecyclerView.ViewHolder var1) {
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo recordPostLayoutInformation(RecyclerView.State var1, RecyclerView.ViewHolder var2) {
         return this.obtainHolderInfo().setFrom(var2);
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation(RecyclerView.State var1, RecyclerView.ViewHolder var2, int var3, List var4) {
         return this.obtainHolderInfo().setFrom(var2);
      }

      public abstract void runPendingAnimations();

      public void setAddDuration(long var1) {
         this.mAddDuration = var1;
      }

      public void setChangeDuration(long var1) {
         this.mChangeDuration = var1;
      }

      void setListener(RecyclerView.ItemAnimator.ItemAnimatorListener var1) {
         this.mListener = var1;
      }

      public void setMoveDuration(long var1) {
         this.mMoveDuration = var1;
      }

      public void setRemoveDuration(long var1) {
         this.mRemoveDuration = var1;
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface AdapterChanges {
      }

      public interface ItemAnimatorFinishedListener {
         void onAnimationsFinished();
      }

      interface ItemAnimatorListener {
         void onAnimationFinished(RecyclerView.ViewHolder var1);
      }

      public static class ItemHolderInfo {
         public int bottom;
         public int changeFlags;
         public int left;
         public int right;
         public int top;

         public RecyclerView.ItemAnimator.ItemHolderInfo setFrom(RecyclerView.ViewHolder var1) {
            return this.setFrom(var1, 0);
         }

         public RecyclerView.ItemAnimator.ItemHolderInfo setFrom(RecyclerView.ViewHolder var1, int var2) {
            View var3 = var1.itemView;
            this.left = var3.getLeft();
            this.top = var3.getTop();
            this.right = var3.getRight();
            this.bottom = var3.getBottom();
            return this;
         }
      }
   }

   private class ItemAnimatorRestoreListener implements RecyclerView.ItemAnimator.ItemAnimatorListener {
      ItemAnimatorRestoreListener() {
      }

      public void onAnimationFinished(RecyclerView.ViewHolder var1) {
         var1.setIsRecyclable(true);
         if (var1.mShadowedHolder != null && var1.mShadowingHolder == null) {
            var1.mShadowedHolder = null;
         }

         var1.mShadowingHolder = null;
         if (!var1.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(var1.itemView) && var1.isTmpDetached()) {
            RecyclerView.this.removeDetachedView(var1.itemView, false);
         }

      }
   }

   public abstract static class ItemDecoration {
      @Deprecated
      public void getItemOffsets(Rect var1, int var2, RecyclerView var3) {
         var1.set(0, 0, 0, 0);
      }

      public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
         this.getItemOffsets(var1, ((RecyclerView.LayoutParams)var2.getLayoutParams()).getViewLayoutPosition(), var3);
      }

      @Deprecated
      public void onDraw(Canvas var1, RecyclerView var2) {
      }

      public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
         this.onDraw(var1, var2);
      }

      @Deprecated
      public void onDrawOver(Canvas var1, RecyclerView var2) {
      }

      public void onDrawOver(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
         this.onDrawOver(var1, var2);
      }
   }

   public abstract static class LayoutManager {
      boolean mAutoMeasure;
      ChildHelper mChildHelper;
      private int mHeight;
      private int mHeightMode;
      ViewBoundsCheck mHorizontalBoundCheck;
      private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback() {
         public View getChildAt(int var1) {
            return LayoutManager.this.getChildAt(var1);
         }

         public int getChildEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedRight(var1) + var2.rightMargin;
         }

         public int getChildStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedLeft(var1) - var2.leftMargin;
         }

         public int getParentEnd() {
            return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
         }

         public int getParentStart() {
            return LayoutManager.this.getPaddingLeft();
         }
      };
      boolean mIsAttachedToWindow;
      private boolean mItemPrefetchEnabled;
      private boolean mMeasurementCacheEnabled;
      int mPrefetchMaxCountObserved;
      boolean mPrefetchMaxObservedInInitialPrefetch;
      RecyclerView mRecyclerView;
      boolean mRequestedSimpleAnimations;
      RecyclerView.SmoothScroller mSmoothScroller;
      ViewBoundsCheck mVerticalBoundCheck;
      private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback() {
         public View getChildAt(int var1) {
            return LayoutManager.this.getChildAt(var1);
         }

         public int getChildEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedBottom(var1) + var2.bottomMargin;
         }

         public int getChildStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedTop(var1) - var2.topMargin;
         }

         public int getParentEnd() {
            return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
         }

         public int getParentStart() {
            return LayoutManager.this.getPaddingTop();
         }
      };
      private int mWidth;
      private int mWidthMode;

      public LayoutManager() {
         this.mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
         this.mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
         this.mRequestedSimpleAnimations = false;
         this.mIsAttachedToWindow = false;
         this.mAutoMeasure = false;
         this.mMeasurementCacheEnabled = true;
         this.mItemPrefetchEnabled = true;
      }

      private void addViewInt(View var1, int var2, boolean var3) {
         RecyclerView.ViewHolder var6 = RecyclerView.getChildViewHolderInt(var1);
         if (!var3 && !var6.isRemoved()) {
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(var6);
         } else {
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(var6);
         }

         RecyclerView.LayoutParams var7 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         if (!var6.wasReturnedFromScrap() && !var6.isScrap()) {
            if (var1.getParent() == this.mRecyclerView) {
               int var5 = this.mChildHelper.indexOfChild(var1);
               int var4 = var2;
               if (var2 == -1) {
                  var4 = this.mChildHelper.getChildCount();
               }

               if (var5 == -1) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                  var9.append(this.mRecyclerView.indexOfChild(var1));
                  var9.append(this.mRecyclerView.exceptionLabel());
                  throw new IllegalStateException(var9.toString());
               }

               if (var5 != var4) {
                  this.mRecyclerView.mLayout.moveView(var5, var4);
               }
            } else {
               this.mChildHelper.addView(var1, var2, false);
               var7.mInsetsDirty = true;
               RecyclerView.SmoothScroller var8 = this.mSmoothScroller;
               if (var8 != null && var8.isRunning()) {
                  this.mSmoothScroller.onChildAttachedToWindow(var1);
               }
            }
         } else {
            if (var6.isScrap()) {
               var6.unScrap();
            } else {
               var6.clearReturnedFromScrapFlag();
            }

            this.mChildHelper.attachViewToParent(var1, var2, var1.getLayoutParams(), false);
         }

         if (var7.mPendingInvalidate) {
            var6.itemView.invalidate();
            var7.mPendingInvalidate = false;
         }

      }

      public static int chooseSize(int var0, int var1, int var2) {
         int var3 = MeasureSpec.getMode(var0);
         var0 = MeasureSpec.getSize(var0);
         if (var3 != Integer.MIN_VALUE) {
            return var3 != 1073741824 ? Math.max(var1, var2) : var0;
         } else {
            return Math.min(var0, Math.max(var1, var2));
         }
      }

      private void detachViewInternal(int var1, View var2) {
         this.mChildHelper.detachViewFromParent(var1);
      }

      public static int getChildMeasureSpec(int var0, int var1, int var2, int var3, boolean var4) {
         int var5 = Math.max(0, var0 - var2);
         byte var7 = 0;
         var2 = 0;
         byte var6 = 0;
         var0 = 0;
         if (var4) {
            if (var3 >= 0) {
               var2 = var3;
               var0 = 1073741824;
            } else if (var3 == -1) {
               if (var1 != Integer.MIN_VALUE) {
                  if (var1 == 0) {
                     var2 = 0;
                     var0 = 0;
                     return MeasureSpec.makeMeasureSpec(var2, var0);
                  }

                  if (var1 != 1073741824) {
                     return MeasureSpec.makeMeasureSpec(var2, var0);
                  }
               }

               var2 = var5;
               var0 = var1;
            } else {
               var2 = var7;
               var0 = var6;
               if (var3 == -2) {
                  var2 = 0;
                  var0 = 0;
               }
            }
         } else if (var3 >= 0) {
            var2 = var3;
            var0 = 1073741824;
         } else if (var3 == -1) {
            var2 = var5;
            var0 = var1;
         } else {
            var2 = var7;
            var0 = var6;
            if (var3 == -2) {
               var2 = var5;
               if (var1 != Integer.MIN_VALUE && var1 != 1073741824) {
                  var0 = 0;
               } else {
                  var0 = Integer.MIN_VALUE;
               }
            }
         }

         return MeasureSpec.makeMeasureSpec(var2, var0);
      }

      @Deprecated
      public static int getChildMeasureSpec(int var0, int var1, int var2, boolean var3) {
         int var4 = Math.max(0, var0 - var1);
         var0 = 0;
         var1 = 0;
         if (var3) {
            if (var2 >= 0) {
               var0 = var2;
               var1 = 1073741824;
            } else {
               var0 = 0;
               var1 = 0;
            }
         } else if (var2 >= 0) {
            var0 = var2;
            var1 = 1073741824;
         } else if (var2 == -1) {
            var0 = var4;
            var1 = 1073741824;
         } else if (var2 == -2) {
            var0 = var4;
            var1 = Integer.MIN_VALUE;
         }

         return MeasureSpec.makeMeasureSpec(var0, var1);
      }

      private int[] getChildRectangleOnScreenScrollAmount(View var1, Rect var2) {
         int var9 = this.getPaddingLeft();
         int var6 = this.getPaddingTop();
         int var10 = this.getWidth() - this.getPaddingRight();
         int var8 = this.getHeight();
         int var13 = this.getPaddingBottom();
         int var11 = var1.getLeft() + var2.left - var1.getScrollX();
         int var7 = var1.getTop() + var2.top - var1.getScrollY();
         int var12 = var2.width() + var11;
         int var14 = var2.height();
         int var3 = Math.min(0, var11 - var9);
         int var4 = Math.min(0, var7 - var6);
         int var5 = Math.max(0, var12 - var10);
         var8 = Math.max(0, var14 + var7 - (var8 - var13));
         if (this.getLayoutDirection() == 1) {
            if (var5 != 0) {
               var3 = var5;
            } else {
               var3 = Math.max(var3, var12 - var10);
            }
         } else if (var3 == 0) {
            var3 = Math.min(var11 - var9, var5);
         }

         if (var4 == 0) {
            var4 = Math.min(var7 - var6, var8);
         }

         return new int[]{var3, var4};
      }

      public static RecyclerView.LayoutManager.Properties getProperties(Context var0, AttributeSet var1, int var2, int var3) {
         RecyclerView.LayoutManager.Properties var4 = new RecyclerView.LayoutManager.Properties();
         TypedArray var5 = var0.obtainStyledAttributes(var1, styleable.RecyclerView, var2, var3);
         var4.orientation = var5.getInt(styleable.RecyclerView_android_orientation, 1);
         var4.spanCount = var5.getInt(styleable.RecyclerView_spanCount, 1);
         var4.reverseLayout = var5.getBoolean(styleable.RecyclerView_reverseLayout, false);
         var4.stackFromEnd = var5.getBoolean(styleable.RecyclerView_stackFromEnd, false);
         var5.recycle();
         return var4;
      }

      private boolean isFocusedChildVisibleAfterScrolling(RecyclerView var1, int var2, int var3) {
         View var11 = var1.getFocusedChild();
         if (var11 == null) {
            return false;
         } else {
            int var4 = this.getPaddingLeft();
            int var5 = this.getPaddingTop();
            int var6 = this.getWidth();
            int var7 = this.getPaddingRight();
            int var8 = this.getHeight();
            int var9 = this.getPaddingBottom();
            Rect var10 = this.mRecyclerView.mTempRect;
            this.getDecoratedBoundsWithMargins(var11, var10);
            if (var10.left - var2 < var6 - var7 && var10.right - var2 > var4 && var10.top - var3 < var8 - var9) {
               return var10.bottom - var3 > var5;
            } else {
               return false;
            }
         }
      }

      private static boolean isMeasurementUpToDate(int var0, int var1, int var2) {
         int var3 = MeasureSpec.getMode(var1);
         var1 = MeasureSpec.getSize(var1);
         boolean var5 = false;
         boolean var4 = false;
         if (var2 > 0 && var0 != var2) {
            return false;
         } else if (var3 != Integer.MIN_VALUE) {
            if (var3 != 0) {
               if (var3 != 1073741824) {
                  return false;
               } else {
                  if (var1 == var0) {
                     var4 = true;
                  }

                  return var4;
               }
            } else {
               return true;
            }
         } else {
            var4 = var5;
            if (var1 >= var0) {
               var4 = true;
            }

            return var4;
         }
      }

      private void scrapOrRecycleView(RecyclerView.Recycler var1, int var2, View var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var3);
         if (!var4.shouldIgnore()) {
            if (var4.isInvalid() && !var4.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
               this.removeViewAt(var2);
               var1.recycleViewHolderInternal(var4);
            } else {
               this.detachViewAt(var2);
               var1.scrapView(var3);
               this.mRecyclerView.mViewInfoStore.onViewDetached(var4);
            }
         }
      }

      public void addDisappearingView(View var1) {
         this.addDisappearingView(var1, -1);
      }

      public void addDisappearingView(View var1, int var2) {
         this.addViewInt(var1, var2, true);
      }

      public void addView(View var1) {
         this.addView(var1, -1);
      }

      public void addView(View var1, int var2) {
         this.addViewInt(var1, var2, false);
      }

      public void assertInLayoutOrScroll(String var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.assertInLayoutOrScroll(var1);
         }

      }

      public void assertNotInLayoutOrScroll(String var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.assertNotInLayoutOrScroll(var1);
         }

      }

      public void attachView(View var1) {
         this.attachView(var1, -1);
      }

      public void attachView(View var1, int var2) {
         this.attachView(var1, var2, (RecyclerView.LayoutParams)var1.getLayoutParams());
      }

      public void attachView(View var1, int var2, RecyclerView.LayoutParams var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var1);
         if (var4.isRemoved()) {
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(var4);
         } else {
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(var4);
         }

         this.mChildHelper.attachViewToParent(var1, var2, var3, var4.isRemoved());
      }

      public void calculateItemDecorationsForChild(View var1, Rect var2) {
         RecyclerView var3 = this.mRecyclerView;
         if (var3 == null) {
            var2.set(0, 0, 0, 0);
         } else {
            var2.set(var3.getItemDecorInsetsForChild(var1));
         }
      }

      public boolean canScrollHorizontally() {
         return false;
      }

      public boolean canScrollVertically() {
         return false;
      }

      public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
         return var1 != null;
      }

      public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      }

      public void collectInitialPrefetchPositions(int var1, RecyclerView.LayoutManager.LayoutPrefetchRegistry var2) {
      }

      public int computeHorizontalScrollExtent(RecyclerView.State var1) {
         return 0;
      }

      public int computeHorizontalScrollOffset(RecyclerView.State var1) {
         return 0;
      }

      public int computeHorizontalScrollRange(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollExtent(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollOffset(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollRange(RecyclerView.State var1) {
         return 0;
      }

      public void detachAndScrapAttachedViews(RecyclerView.Recycler var1) {
         for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            this.scrapOrRecycleView(var1, var2, this.getChildAt(var2));
         }

      }

      public void detachAndScrapView(View var1, RecyclerView.Recycler var2) {
         this.scrapOrRecycleView(var2, this.mChildHelper.indexOfChild(var1), var1);
      }

      public void detachAndScrapViewAt(int var1, RecyclerView.Recycler var2) {
         this.scrapOrRecycleView(var2, var1, this.getChildAt(var1));
      }

      public void detachView(View var1) {
         int var2 = this.mChildHelper.indexOfChild(var1);
         if (var2 >= 0) {
            this.detachViewInternal(var2, var1);
         }

      }

      public void detachViewAt(int var1) {
         this.detachViewInternal(var1, this.getChildAt(var1));
      }

      void dispatchAttachedToWindow(RecyclerView var1) {
         this.mIsAttachedToWindow = true;
         this.onAttachedToWindow(var1);
      }

      void dispatchDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
         this.mIsAttachedToWindow = false;
         this.onDetachedFromWindow(var1, var2);
      }

      public void endAnimation(View var1) {
         if (this.mRecyclerView.mItemAnimator != null) {
            this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(var1));
         }

      }

      public View findContainingItemView(View var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 == null) {
            return null;
         } else {
            var1 = var2.findContainingItemView(var1);
            if (var1 == null) {
               return null;
            } else {
               return this.mChildHelper.isHidden(var1) ? null : var1;
            }
         }
      }

      public View findViewByPosition(int var1) {
         int var3 = this.getChildCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            View var4 = this.getChildAt(var2);
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var4);
            if (var5 != null && var5.getLayoutPosition() == var1 && !var5.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !var5.isRemoved())) {
               return var4;
            }
         }

         return null;
      }

      public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();

      public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
         return new RecyclerView.LayoutParams(var1, var2);
      }

      public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
         if (var1 instanceof RecyclerView.LayoutParams) {
            return new RecyclerView.LayoutParams((RecyclerView.LayoutParams)var1);
         } else {
            return var1 instanceof MarginLayoutParams ? new RecyclerView.LayoutParams((MarginLayoutParams)var1) : new RecyclerView.LayoutParams(var1);
         }
      }

      public int getBaseline() {
         return -1;
      }

      public int getBottomDecorationHeight(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.bottom;
      }

      public View getChildAt(int var1) {
         ChildHelper var2 = this.mChildHelper;
         return var2 != null ? var2.getChildAt(var1) : null;
      }

      public int getChildCount() {
         ChildHelper var1 = this.mChildHelper;
         return var1 != null ? var1.getChildCount() : 0;
      }

      public boolean getClipToPadding() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null && var1.mClipToPadding;
      }

      public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         RecyclerView var4 = this.mRecyclerView;
         int var3 = 1;
         if (var4 != null) {
            if (var4.mAdapter == null) {
               return 1;
            } else {
               if (this.canScrollHorizontally()) {
                  var3 = this.mRecyclerView.mAdapter.getItemCount();
               }

               return var3;
            }
         } else {
            return 1;
         }
      }

      public int getDecoratedBottom(View var1) {
         return var1.getBottom() + this.getBottomDecorationHeight(var1);
      }

      public void getDecoratedBoundsWithMargins(View var1, Rect var2) {
         RecyclerView.getDecoratedBoundsWithMarginsInt(var1, var2);
      }

      public int getDecoratedLeft(View var1) {
         return var1.getLeft() - this.getLeftDecorationWidth(var1);
      }

      public int getDecoratedMeasuredHeight(View var1) {
         Rect var2 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         return var1.getMeasuredHeight() + var2.top + var2.bottom;
      }

      public int getDecoratedMeasuredWidth(View var1) {
         Rect var2 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         return var1.getMeasuredWidth() + var2.left + var2.right;
      }

      public int getDecoratedRight(View var1) {
         return var1.getRight() + this.getRightDecorationWidth(var1);
      }

      public int getDecoratedTop(View var1) {
         return var1.getTop() - this.getTopDecorationHeight(var1);
      }

      public View getFocusedChild() {
         RecyclerView var1 = this.mRecyclerView;
         if (var1 == null) {
            return null;
         } else {
            View var2 = var1.getFocusedChild();
            if (var2 != null) {
               return this.mChildHelper.isHidden(var2) ? null : var2;
            } else {
               return null;
            }
         }
      }

      public int getHeight() {
         return this.mHeight;
      }

      public int getHeightMode() {
         return this.mHeightMode;
      }

      public int getItemCount() {
         RecyclerView var1 = this.mRecyclerView;
         RecyclerView.Adapter var2;
         if (var1 != null) {
            var2 = var1.getAdapter();
         } else {
            var2 = null;
         }

         return var2 != null ? var2.getItemCount() : 0;
      }

      public int getItemViewType(View var1) {
         return RecyclerView.getChildViewHolderInt(var1).getItemViewType();
      }

      public int getLayoutDirection() {
         return ViewCompat.getLayoutDirection(this.mRecyclerView);
      }

      public int getLeftDecorationWidth(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.left;
      }

      public int getMinimumHeight() {
         return ViewCompat.getMinimumHeight(this.mRecyclerView);
      }

      public int getMinimumWidth() {
         return ViewCompat.getMinimumWidth(this.mRecyclerView);
      }

      public int getPaddingBottom() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? var1.getPaddingBottom() : 0;
      }

      public int getPaddingEnd() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? ViewCompat.getPaddingEnd(var1) : 0;
      }

      public int getPaddingLeft() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? var1.getPaddingLeft() : 0;
      }

      public int getPaddingRight() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? var1.getPaddingRight() : 0;
      }

      public int getPaddingStart() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? ViewCompat.getPaddingStart(var1) : 0;
      }

      public int getPaddingTop() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null ? var1.getPaddingTop() : 0;
      }

      public int getPosition(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).getViewLayoutPosition();
      }

      public int getRightDecorationWidth(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.right;
      }

      public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         RecyclerView var4 = this.mRecyclerView;
         int var3 = 1;
         if (var4 != null) {
            if (var4.mAdapter == null) {
               return 1;
            } else {
               if (this.canScrollVertically()) {
                  var3 = this.mRecyclerView.mAdapter.getItemCount();
               }

               return var3;
            }
         } else {
            return 1;
         }
      }

      public int getSelectionModeForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         return 0;
      }

      public int getTopDecorationHeight(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.top;
      }

      public void getTransformedBoundingBox(View var1, boolean var2, Rect var3) {
         if (var2) {
            Rect var4 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
            var3.set(-var4.left, -var4.top, var1.getWidth() + var4.right, var1.getHeight() + var4.bottom);
         } else {
            var3.set(0, 0, var1.getWidth(), var1.getHeight());
         }

         if (this.mRecyclerView != null) {
            Matrix var6 = var1.getMatrix();
            if (var6 != null && !var6.isIdentity()) {
               RectF var5 = this.mRecyclerView.mTempRectF;
               var5.set(var3);
               var6.mapRect(var5);
               var3.set((int)Math.floor((double)var5.left), (int)Math.floor((double)var5.top), (int)Math.ceil((double)var5.right), (int)Math.ceil((double)var5.bottom));
            }
         }

         var3.offset(var1.getLeft(), var1.getTop());
      }

      public int getWidth() {
         return this.mWidth;
      }

      public int getWidthMode() {
         return this.mWidthMode;
      }

      boolean hasFlexibleChildInBothOrientations() {
         int var2 = this.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            android.view.ViewGroup.LayoutParams var3 = this.getChildAt(var1).getLayoutParams();
            if (var3.width < 0 && var3.height < 0) {
               return true;
            }
         }

         return false;
      }

      public boolean hasFocus() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null && var1.hasFocus();
      }

      public void ignoreView(View var1) {
         ViewParent var2 = var1.getParent();
         RecyclerView var3 = this.mRecyclerView;
         if (var2 == var3 && var3.indexOfChild(var1) != -1) {
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var1);
            var5.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(var5);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("View should be fully attached to be ignored");
            var4.append(this.mRecyclerView.exceptionLabel());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      public boolean isAttachedToWindow() {
         return this.mIsAttachedToWindow;
      }

      public boolean isAutoMeasureEnabled() {
         return this.mAutoMeasure;
      }

      public boolean isFocused() {
         RecyclerView var1 = this.mRecyclerView;
         return var1 != null && var1.isFocused();
      }

      public final boolean isItemPrefetchEnabled() {
         return this.mItemPrefetchEnabled;
      }

      public boolean isLayoutHierarchical(RecyclerView.Recycler var1, RecyclerView.State var2) {
         return false;
      }

      public boolean isMeasurementCacheEnabled() {
         return this.mMeasurementCacheEnabled;
      }

      public boolean isSmoothScrolling() {
         RecyclerView.SmoothScroller var1 = this.mSmoothScroller;
         return var1 != null && var1.isRunning();
      }

      public boolean isViewPartiallyVisible(View var1, boolean var2, boolean var3) {
         if (this.mHorizontalBoundCheck.isViewWithinBoundFlags(var1, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(var1, 24579)) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var2) {
            return var3;
         } else {
            return !var3;
         }
      }

      public void layoutDecorated(View var1, int var2, int var3, int var4, int var5) {
         Rect var6 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         var1.layout(var6.left + var2, var6.top + var3, var4 - var6.right, var5 - var6.bottom);
      }

      public void layoutDecoratedWithMargins(View var1, int var2, int var3, int var4, int var5) {
         RecyclerView.LayoutParams var6 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var7 = var6.mDecorInsets;
         var1.layout(var7.left + var2 + var6.leftMargin, var7.top + var3 + var6.topMargin, var4 - var7.right - var6.rightMargin, var5 - var7.bottom - var6.bottomMargin);
      }

      public void measureChild(View var1, int var2, int var3) {
         RecyclerView.LayoutParams var8 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var9 = this.mRecyclerView.getItemDecorInsetsForChild(var1);
         int var6 = var9.left;
         int var7 = var9.right;
         int var4 = var9.top;
         int var5 = var9.bottom;
         var2 = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + var2 + var6 + var7, var8.width, this.canScrollHorizontally());
         var3 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + var3 + var4 + var5, var8.height, this.canScrollVertically());
         if (this.shouldMeasureChild(var1, var2, var3, var8)) {
            var1.measure(var2, var3);
         }

      }

      public void measureChildWithMargins(View var1, int var2, int var3) {
         RecyclerView.LayoutParams var8 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var9 = this.mRecyclerView.getItemDecorInsetsForChild(var1);
         int var6 = var9.left;
         int var7 = var9.right;
         int var4 = var9.top;
         int var5 = var9.bottom;
         var2 = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + var8.leftMargin + var8.rightMargin + var2 + var6 + var7, var8.width, this.canScrollHorizontally());
         var3 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + var8.topMargin + var8.bottomMargin + var3 + var4 + var5, var8.height, this.canScrollVertically());
         if (this.shouldMeasureChild(var1, var2, var3, var8)) {
            var1.measure(var2, var3);
         }

      }

      public void moveView(int var1, int var2) {
         View var3 = this.getChildAt(var1);
         if (var3 != null) {
            this.detachViewAt(var1);
            this.attachView(var3, var2);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Cannot move a child from non-existing index:");
            var4.append(var1);
            var4.append(this.mRecyclerView.toString());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      public void offsetChildrenHorizontal(int var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.offsetChildrenHorizontal(var1);
         }

      }

      public void offsetChildrenVertical(int var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.offsetChildrenVertical(var1);
         }

      }

      public void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2) {
      }

      public boolean onAddFocusables(RecyclerView var1, ArrayList var2, int var3, int var4) {
         return false;
      }

      public void onAttachedToWindow(RecyclerView var1) {
      }

      @Deprecated
      public void onDetachedFromWindow(RecyclerView var1) {
      }

      public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
         this.onDetachedFromWindow(var1);
      }

      public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
         return null;
      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, var1);
      }

      public void onInitializeAccessibilityEvent(RecyclerView.Recycler var1, RecyclerView.State var2, AccessibilityEvent var3) {
         RecyclerView var5 = this.mRecyclerView;
         if (var5 != null) {
            if (var3 != null) {
               boolean var4 = true;
               if (!var5.canScrollVertically(1) && !this.mRecyclerView.canScrollVertically(-1) && !this.mRecyclerView.canScrollHorizontally(-1) && !this.mRecyclerView.canScrollHorizontally(1)) {
                  var4 = false;
               }

               var3.setScrollable(var4);
               if (this.mRecyclerView.mAdapter != null) {
                  var3.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
               }

            }
         }
      }

      void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat var1) {
         this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, var1);
      }

      public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler var1, RecyclerView.State var2, AccessibilityNodeInfoCompat var3) {
         if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
            var3.addAction(8192);
            var3.setScrollable(true);
         }

         if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
            var3.addAction(4096);
            var3.setScrollable(true);
         }

         var3.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(var1, var2), this.getColumnCountForAccessibility(var1, var2), this.isLayoutHierarchical(var1, var2), this.getSelectionModeForAccessibility(var1, var2)));
      }

      void onInitializeAccessibilityNodeInfoForItem(View var1, AccessibilityNodeInfoCompat var2) {
         RecyclerView.ViewHolder var3 = RecyclerView.getChildViewHolderInt(var1);
         if (var3 != null && !var3.isRemoved() && !this.mChildHelper.isHidden(var3.itemView)) {
            this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, var1, var2);
         }

      }

      public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
         int var5;
         if (this.canScrollVertically()) {
            var5 = this.getPosition(var3);
         } else {
            var5 = 0;
         }

         int var6;
         if (this.canScrollHorizontally()) {
            var6 = this.getPosition(var3);
         } else {
            var6 = 0;
         }

         var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var5, 1, var6, 1, false, false));
      }

      public View onInterceptFocusSearch(View var1, int var2) {
         return null;
      }

      public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsChanged(RecyclerView var1) {
      }

      public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      }

      public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsUpdated(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
         this.onItemsUpdated(var1, var2, var3);
      }

      public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
         Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
      }

      public void onLayoutCompleted(RecyclerView.State var1) {
      }

      public void onMeasure(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4) {
         this.mRecyclerView.defaultOnMeasure(var3, var4);
      }

      @Deprecated
      public boolean onRequestChildFocus(RecyclerView var1, View var2, View var3) {
         return this.isSmoothScrolling() || var1.isComputingLayout();
      }

      public boolean onRequestChildFocus(RecyclerView var1, RecyclerView.State var2, View var3, View var4) {
         return this.onRequestChildFocus(var1, var3, var4);
      }

      public void onRestoreInstanceState(Parcelable var1) {
      }

      public Parcelable onSaveInstanceState() {
         return null;
      }

      public void onScrollStateChanged(int var1) {
      }

      void onSmoothScrollerStopped(RecyclerView.SmoothScroller var1) {
         if (this.mSmoothScroller == var1) {
            this.mSmoothScroller = null;
         }

      }

      boolean performAccessibilityAction(int var1, Bundle var2) {
         return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, var1, var2);
      }

      public boolean performAccessibilityAction(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, Bundle var4) {
         RecyclerView var9 = this.mRecyclerView;
         if (var9 == null) {
            return false;
         } else {
            byte var7 = 0;
            byte var8 = 0;
            int var5 = 0;
            int var6 = 0;
            if (var3 != 4096) {
               if (var3 != 8192) {
                  var3 = var8;
               } else {
                  if (var9.canScrollVertically(-1)) {
                     var5 = -(this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
                  }

                  var3 = var5;
                  if (this.mRecyclerView.canScrollHorizontally(-1)) {
                     var6 = -(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                     var3 = var5;
                  }
               }
            } else {
               var5 = var7;
               if (var9.canScrollVertically(1)) {
                  var5 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
               }

               var3 = var5;
               if (this.mRecyclerView.canScrollHorizontally(1)) {
                  var6 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                  var3 = var5;
               }
            }

            if (var3 == 0 && var6 == 0) {
               return false;
            } else {
               this.mRecyclerView.smoothScrollBy(var6, var3, (Interpolator)null, Integer.MIN_VALUE, true);
               return true;
            }
         }
      }

      boolean performAccessibilityActionForItem(View var1, int var2, Bundle var3) {
         return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, var1, var2, var3);
      }

      public boolean performAccessibilityActionForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, int var4, Bundle var5) {
         return false;
      }

      public void postOnAnimation(Runnable var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            ViewCompat.postOnAnimation(var2, var1);
         }

      }

      public void removeAllViews() {
         for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
            this.mChildHelper.removeViewAt(var1);
         }

      }

      public void removeAndRecycleAllViews(RecyclerView.Recycler var1) {
         for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            if (!RecyclerView.getChildViewHolderInt(this.getChildAt(var2)).shouldIgnore()) {
               this.removeAndRecycleViewAt(var2, var1);
            }
         }

      }

      void removeAndRecycleScrapInt(RecyclerView.Recycler var1) {
         int var3 = var1.getScrapCount();

         for(int var2 = var3 - 1; var2 >= 0; --var2) {
            View var4 = var1.getScrapViewAt(var2);
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var4);
            if (!var5.shouldIgnore()) {
               var5.setIsRecyclable(false);
               if (var5.isTmpDetached()) {
                  this.mRecyclerView.removeDetachedView(var4, false);
               }

               if (this.mRecyclerView.mItemAnimator != null) {
                  this.mRecyclerView.mItemAnimator.endAnimation(var5);
               }

               var5.setIsRecyclable(true);
               var1.quickRecycleScrapView(var4);
            }
         }

         var1.clearScrap();
         if (var3 > 0) {
            this.mRecyclerView.invalidate();
         }

      }

      public void removeAndRecycleView(View var1, RecyclerView.Recycler var2) {
         this.removeView(var1);
         var2.recycleView(var1);
      }

      public void removeAndRecycleViewAt(int var1, RecyclerView.Recycler var2) {
         View var3 = this.getChildAt(var1);
         this.removeViewAt(var1);
         var2.recycleView(var3);
      }

      public boolean removeCallbacks(Runnable var1) {
         RecyclerView var2 = this.mRecyclerView;
         return var2 != null ? var2.removeCallbacks(var1) : false;
      }

      public void removeDetachedView(View var1) {
         this.mRecyclerView.removeDetachedView(var1, false);
      }

      public void removeView(View var1) {
         this.mChildHelper.removeView(var1);
      }

      public void removeViewAt(int var1) {
         if (this.getChildAt(var1) != null) {
            this.mChildHelper.removeViewAt(var1);
         }

      }

      public boolean requestChildRectangleOnScreen(RecyclerView var1, View var2, Rect var3, boolean var4) {
         return this.requestChildRectangleOnScreen(var1, var2, var3, var4, false);
      }

      public boolean requestChildRectangleOnScreen(RecyclerView var1, View var2, Rect var3, boolean var4, boolean var5) {
         int[] var8 = this.getChildRectangleOnScreenScrollAmount(var2, var3);
         int var6 = var8[0];
         int var7 = var8[1];
         if ((!var5 || this.isFocusedChildVisibleAfterScrolling(var1, var6, var7)) && (var6 != 0 || var7 != 0)) {
            if (var4) {
               var1.scrollBy(var6, var7);
               return true;
            } else {
               var1.smoothScrollBy(var6, var7);
               return true;
            }
         } else {
            return false;
         }
      }

      public void requestLayout() {
         RecyclerView var1 = this.mRecyclerView;
         if (var1 != null) {
            var1.requestLayout();
         }

      }

      public void requestSimpleAnimationsInNextLayout() {
         this.mRequestedSimpleAnimations = true;
      }

      public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
         return 0;
      }

      public void scrollToPosition(int var1) {
      }

      public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
         return 0;
      }

      @Deprecated
      public void setAutoMeasureEnabled(boolean var1) {
         this.mAutoMeasure = var1;
      }

      void setExactMeasureSpecsFrom(RecyclerView var1) {
         this.setMeasureSpecs(MeasureSpec.makeMeasureSpec(var1.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var1.getHeight(), 1073741824));
      }

      public final void setItemPrefetchEnabled(boolean var1) {
         if (var1 != this.mItemPrefetchEnabled) {
            this.mItemPrefetchEnabled = var1;
            this.mPrefetchMaxCountObserved = 0;
            RecyclerView var2 = this.mRecyclerView;
            if (var2 != null) {
               var2.mRecycler.updateViewCacheSize();
            }
         }

      }

      void setMeasureSpecs(int var1, int var2) {
         this.mWidth = MeasureSpec.getSize(var1);
         var1 = MeasureSpec.getMode(var1);
         this.mWidthMode = var1;
         if (var1 == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
            this.mWidth = 0;
         }

         this.mHeight = MeasureSpec.getSize(var2);
         var1 = MeasureSpec.getMode(var2);
         this.mHeightMode = var1;
         if (var1 == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
            this.mHeight = 0;
         }

      }

      public void setMeasuredDimension(int var1, int var2) {
         this.mRecyclerView.setMeasuredDimension(var1, var2);
      }

      public void setMeasuredDimension(Rect var1, int var2, int var3) {
         int var4 = var1.width();
         int var5 = this.getPaddingLeft();
         int var6 = this.getPaddingRight();
         int var7 = var1.height();
         int var8 = this.getPaddingTop();
         int var9 = this.getPaddingBottom();
         this.setMeasuredDimension(chooseSize(var2, var4 + var5 + var6, this.getMinimumWidth()), chooseSize(var3, var7 + var8 + var9, this.getMinimumHeight()));
      }

      void setMeasuredDimensionFromChildren(int var1, int var2) {
         int var11 = this.getChildCount();
         if (var11 == 0) {
            this.mRecyclerView.defaultOnMeasure(var1, var2);
         } else {
            int var9 = Integer.MAX_VALUE;
            int var5 = Integer.MAX_VALUE;
            int var7 = Integer.MIN_VALUE;
            int var4 = Integer.MIN_VALUE;

            int var10;
            for(int var3 = 0; var3 < var11; var4 = var10) {
               View var12 = this.getChildAt(var3);
               Rect var13 = this.mRecyclerView.mTempRect;
               this.getDecoratedBoundsWithMargins(var12, var13);
               int var6 = var9;
               if (var13.left < var9) {
                  var6 = var13.left;
               }

               int var8 = var7;
               if (var13.right > var7) {
                  var8 = var13.right;
               }

               var7 = var5;
               if (var13.top < var5) {
                  var7 = var13.top;
               }

               var10 = var4;
               if (var13.bottom > var4) {
                  var10 = var13.bottom;
               }

               ++var3;
               var9 = var6;
               var5 = var7;
               var7 = var8;
            }

            this.mRecyclerView.mTempRect.set(var9, var5, var7, var4);
            this.setMeasuredDimension(this.mRecyclerView.mTempRect, var1, var2);
         }
      }

      public void setMeasurementCacheEnabled(boolean var1) {
         this.mMeasurementCacheEnabled = var1;
      }

      void setRecyclerView(RecyclerView var1) {
         if (var1 == null) {
            this.mRecyclerView = null;
            this.mChildHelper = null;
            this.mWidth = 0;
            this.mHeight = 0;
         } else {
            this.mRecyclerView = var1;
            this.mChildHelper = var1.mChildHelper;
            this.mWidth = var1.getWidth();
            this.mHeight = var1.getHeight();
         }

         this.mWidthMode = 1073741824;
         this.mHeightMode = 1073741824;
      }

      boolean shouldMeasureChild(View var1, int var2, int var3, RecyclerView.LayoutParams var4) {
         return var1.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(var1.getWidth(), var2, var4.width) || !isMeasurementUpToDate(var1.getHeight(), var3, var4.height);
      }

      boolean shouldMeasureTwice() {
         return false;
      }

      boolean shouldReMeasureChild(View var1, int var2, int var3, RecyclerView.LayoutParams var4) {
         return !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(var1.getMeasuredWidth(), var2, var4.width) || !isMeasurementUpToDate(var1.getMeasuredHeight(), var3, var4.height);
      }

      public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
         Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
      }

      public void startSmoothScroll(RecyclerView.SmoothScroller var1) {
         RecyclerView.SmoothScroller var2 = this.mSmoothScroller;
         if (var2 != null && var1 != var2 && var2.isRunning()) {
            this.mSmoothScroller.stop();
         }

         this.mSmoothScroller = var1;
         var1.start(this.mRecyclerView, this);
      }

      public void stopIgnoringView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         var2.stopIgnoring();
         var2.resetInternal();
         var2.addFlags(4);
      }

      void stopSmoothScroller() {
         RecyclerView.SmoothScroller var1 = this.mSmoothScroller;
         if (var1 != null) {
            var1.stop();
         }

      }

      public boolean supportsPredictiveItemAnimations() {
         return false;
      }

      public interface LayoutPrefetchRegistry {
         void addPosition(int var1, int var2);
      }

      public static class Properties {
         public int orientation;
         public boolean reverseLayout;
         public int spanCount;
         public boolean stackFromEnd;
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      final Rect mDecorInsets = new Rect();
      boolean mInsetsDirty = true;
      boolean mPendingInvalidate = false;
      RecyclerView.ViewHolder mViewHolder;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(RecyclerView.LayoutParams var1) {
         super(var1);
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

   public interface OnChildAttachStateChangeListener {
      void onChildViewAttachedToWindow(View var1);

      void onChildViewDetachedFromWindow(View var1);
   }

   public abstract static class OnFlingListener {
      public abstract boolean onFling(int var1, int var2);
   }

   public interface OnItemTouchListener {
      boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2);

      void onRequestDisallowInterceptTouchEvent(boolean var1);

      void onTouchEvent(RecyclerView var1, MotionEvent var2);
   }

   public abstract static class OnScrollListener {
      public void onScrollStateChanged(RecyclerView var1, int var2) {
      }

      public void onScrolled(RecyclerView var1, int var2, int var3) {
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Orientation {
   }

   public static class RecycledViewPool {
      private static final int DEFAULT_MAX_SCRAP = 5;
      private int mAttachCount = 0;
      SparseArray mScrap = new SparseArray();

      private RecyclerView.RecycledViewPool.ScrapData getScrapDataForType(int var1) {
         RecyclerView.RecycledViewPool.ScrapData var3 = (RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var1);
         RecyclerView.RecycledViewPool.ScrapData var2 = var3;
         if (var3 == null) {
            var2 = new RecyclerView.RecycledViewPool.ScrapData();
            this.mScrap.put(var1, var2);
         }

         return var2;
      }

      void attach() {
         ++this.mAttachCount;
      }

      public void clear() {
         for(int var1 = 0; var1 < this.mScrap.size(); ++var1) {
            ((RecyclerView.RecycledViewPool.ScrapData)this.mScrap.valueAt(var1)).mScrapHeap.clear();
         }

      }

      void detach() {
         --this.mAttachCount;
      }

      void factorInBindTime(int var1, long var2) {
         RecyclerView.RecycledViewPool.ScrapData var4 = this.getScrapDataForType(var1);
         var4.mBindRunningAverageNs = this.runningAverage(var4.mBindRunningAverageNs, var2);
      }

      void factorInCreateTime(int var1, long var2) {
         RecyclerView.RecycledViewPool.ScrapData var4 = this.getScrapDataForType(var1);
         var4.mCreateRunningAverageNs = this.runningAverage(var4.mCreateRunningAverageNs, var2);
      }

      public RecyclerView.ViewHolder getRecycledView(int var1) {
         RecyclerView.RecycledViewPool.ScrapData var2 = (RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var1);
         if (var2 != null && !var2.mScrapHeap.isEmpty()) {
            ArrayList var3 = var2.mScrapHeap;

            for(var1 = var3.size() - 1; var1 >= 0; --var1) {
               if (!((RecyclerView.ViewHolder)var3.get(var1)).isAttachedToTransitionOverlay()) {
                  return (RecyclerView.ViewHolder)var3.remove(var1);
               }
            }
         }

         return null;
      }

      public int getRecycledViewCount(int var1) {
         return this.getScrapDataForType(var1).mScrapHeap.size();
      }

      void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2, boolean var3) {
         if (var1 != null) {
            this.detach();
         }

         if (!var3 && this.mAttachCount == 0) {
            this.clear();
         }

         if (var2 != null) {
            this.attach();
         }

      }

      public void putRecycledView(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         ArrayList var3 = this.getScrapDataForType(var2).mScrapHeap;
         if (((RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var2)).mMaxScrap > var3.size()) {
            var1.resetInternal();
            var3.add(var1);
         }
      }

      long runningAverage(long var1, long var3) {
         return var1 == 0L ? var3 : var1 / 4L * 3L + var3 / 4L;
      }

      public void setMaxRecycledViews(int var1, int var2) {
         RecyclerView.RecycledViewPool.ScrapData var3 = this.getScrapDataForType(var1);
         var3.mMaxScrap = var2;
         ArrayList var4 = var3.mScrapHeap;

         while(var4.size() > var2) {
            var4.remove(var4.size() - 1);
         }

      }

      int size() {
         int var2 = 0;

         int var3;
         for(int var1 = 0; var1 < this.mScrap.size(); var2 = var3) {
            ArrayList var4 = ((RecyclerView.RecycledViewPool.ScrapData)this.mScrap.valueAt(var1)).mScrapHeap;
            var3 = var2;
            if (var4 != null) {
               var3 = var2 + var4.size();
            }

            ++var1;
         }

         return var2;
      }

      boolean willBindInTime(int var1, long var2, long var4) {
         long var6 = this.getScrapDataForType(var1).mBindRunningAverageNs;
         return var6 == 0L || var2 + var6 < var4;
      }

      boolean willCreateInTime(int var1, long var2, long var4) {
         long var6 = this.getScrapDataForType(var1).mCreateRunningAverageNs;
         return var6 == 0L || var2 + var6 < var4;
      }

      static class ScrapData {
         long mBindRunningAverageNs = 0L;
         long mCreateRunningAverageNs = 0L;
         int mMaxScrap = 5;
         final ArrayList mScrapHeap = new ArrayList();
      }
   }

   public final class Recycler {
      static final int DEFAULT_CACHE_SIZE = 2;
      final ArrayList mAttachedScrap = new ArrayList();
      final ArrayList mCachedViews = new ArrayList();
      ArrayList mChangedScrap = null;
      RecyclerView.RecycledViewPool mRecyclerPool;
      private int mRequestedCacheMax;
      private final List mUnmodifiableAttachedScrap;
      private RecyclerView.ViewCacheExtension mViewCacheExtension;
      int mViewCacheMax;

      public Recycler() {
         this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
         this.mRequestedCacheMax = 2;
         this.mViewCacheMax = 2;
      }

      private void attachAccessibilityDelegateOnBind(RecyclerView.ViewHolder var1) {
         if (RecyclerView.this.isAccessibilityEnabled()) {
            View var3 = var1.itemView;
            if (ViewCompat.getImportantForAccessibility(var3) == 0) {
               ViewCompat.setImportantForAccessibility(var3, 1);
            }

            if (RecyclerView.this.mAccessibilityDelegate == null) {
               return;
            }

            AccessibilityDelegateCompat var2 = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
            if (var2 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
               ((RecyclerViewAccessibilityDelegate.ItemDelegate)var2).saveOriginalDelegate(var3);
            }

            ViewCompat.setAccessibilityDelegate(var3, var2);
         }

      }

      private void invalidateDisplayListInt(ViewGroup var1, boolean var2) {
         int var3;
         for(var3 = var1.getChildCount() - 1; var3 >= 0; --var3) {
            View var4 = var1.getChildAt(var3);
            if (var4 instanceof ViewGroup) {
               this.invalidateDisplayListInt((ViewGroup)var4, true);
            }
         }

         if (var2) {
            if (var1.getVisibility() == 4) {
               var1.setVisibility(0);
               var1.setVisibility(4);
            } else {
               var3 = var1.getVisibility();
               var1.setVisibility(4);
               var1.setVisibility(var3);
            }
         }
      }

      private void invalidateDisplayListInt(RecyclerView.ViewHolder var1) {
         if (var1.itemView instanceof ViewGroup) {
            this.invalidateDisplayListInt((ViewGroup)var1.itemView, false);
         }

      }

      private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder var1, int var2, int var3, long var4) {
         var1.mOwnerRecyclerView = RecyclerView.this;
         int var6 = var1.getItemViewType();
         long var7 = RecyclerView.this.getNanoTime();
         if (var4 != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(var6, var7, var4)) {
            return false;
         } else {
            RecyclerView.this.mAdapter.bindViewHolder(var1, var2);
            var4 = RecyclerView.this.getNanoTime();
            this.mRecyclerPool.factorInBindTime(var1.getItemViewType(), var4 - var7);
            this.attachAccessibilityDelegateOnBind(var1);
            if (RecyclerView.this.mState.isPreLayout()) {
               var1.mPreLayoutPosition = var3;
            }

            return true;
         }
      }

      void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder var1, boolean var2) {
         RecyclerView.clearNestedRecyclerViewIfNotNested(var1);
         View var4 = var1.itemView;
         if (RecyclerView.this.mAccessibilityDelegate != null) {
            AccessibilityDelegateCompat var5 = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
            AccessibilityDelegateCompat var3 = null;
            if (var5 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
               var3 = ((RecyclerViewAccessibilityDelegate.ItemDelegate)var5).getAndRemoveOriginalDelegateForItem(var4);
            }

            ViewCompat.setAccessibilityDelegate(var4, var3);
         }

         if (var2) {
            this.dispatchViewRecycled(var1);
         }

         var1.mOwnerRecyclerView = null;
         this.getRecycledViewPool().putRecycledView(var1);
      }

      public void bindViewToPosition(View var1, int var2) {
         RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var1);
         StringBuilder var6;
         if (var5 != null) {
            int var3 = RecyclerView.this.mAdapterHelper.findPositionOffset(var2);
            if (var3 >= 0 && var3 < RecyclerView.this.mAdapter.getItemCount()) {
               this.tryBindViewHolderByDeadline(var5, var3, var2, Long.MAX_VALUE);
               android.view.ViewGroup.LayoutParams var7 = var5.itemView.getLayoutParams();
               RecyclerView.LayoutParams var8;
               if (var7 == null) {
                  var8 = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                  var5.itemView.setLayoutParams(var8);
               } else if (!RecyclerView.this.checkLayoutParams(var7)) {
                  var8 = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams(var7);
                  var5.itemView.setLayoutParams(var8);
               } else {
                  var8 = (RecyclerView.LayoutParams)var7;
               }

               boolean var4 = true;
               var8.mInsetsDirty = true;
               var8.mViewHolder = var5;
               if (var5.itemView.getParent() != null) {
                  var4 = false;
               }

               var8.mPendingInvalidate = var4;
            } else {
               var6 = new StringBuilder();
               var6.append("Inconsistency detected. Invalid item position ");
               var6.append(var2);
               var6.append("(offset:");
               var6.append(var3);
               var6.append(").state:");
               var6.append(RecyclerView.this.mState.getItemCount());
               var6.append(RecyclerView.this.exceptionLabel());
               throw new IndexOutOfBoundsException(var6.toString());
            }
         } else {
            var6 = new StringBuilder();
            var6.append("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
            var6.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(var6.toString());
         }
      }

      public void clear() {
         this.mAttachedScrap.clear();
         this.recycleAndClearCachedViews();
      }

      void clearOldPositions() {
         int var2 = this.mCachedViews.size();

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            ((RecyclerView.ViewHolder)this.mCachedViews.get(var1)).clearOldPosition();
         }

         var2 = this.mAttachedScrap.size();

         for(var1 = 0; var1 < var2; ++var1) {
            ((RecyclerView.ViewHolder)this.mAttachedScrap.get(var1)).clearOldPosition();
         }

         ArrayList var3 = this.mChangedScrap;
         if (var3 != null) {
            var2 = var3.size();

            for(var1 = 0; var1 < var2; ++var1) {
               ((RecyclerView.ViewHolder)this.mChangedScrap.get(var1)).clearOldPosition();
            }
         }

      }

      void clearScrap() {
         this.mAttachedScrap.clear();
         ArrayList var1 = this.mChangedScrap;
         if (var1 != null) {
            var1.clear();
         }

      }

      public int convertPreLayoutPositionToPostLayout(int var1) {
         if (var1 >= 0 && var1 < RecyclerView.this.mState.getItemCount()) {
            return !RecyclerView.this.mState.isPreLayout() ? var1 : RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("invalid position ");
            var2.append(var1);
            var2.append(". State item count is ");
            var2.append(RecyclerView.this.mState.getItemCount());
            var2.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(var2.toString());
         }
      }

      void dispatchViewRecycled(RecyclerView.ViewHolder var1) {
         if (RecyclerView.this.mRecyclerListener != null) {
            RecyclerView.this.mRecyclerListener.onViewRecycled(var1);
         }

         if (RecyclerView.this.mAdapter != null) {
            RecyclerView.this.mAdapter.onViewRecycled(var1);
         }

         if (RecyclerView.this.mState != null) {
            RecyclerView.this.mViewInfoStore.removeViewHolder(var1);
         }

      }

      RecyclerView.ViewHolder getChangedScrapViewForPosition(int var1) {
         ArrayList var6 = this.mChangedScrap;
         if (var6 == null) {
            return null;
         } else {
            int var3 = var6.size();
            if (var3 == 0) {
               return null;
            } else {
               RecyclerView.ViewHolder var7;
               for(int var2 = 0; var2 < var3; ++var2) {
                  var7 = (RecyclerView.ViewHolder)this.mChangedScrap.get(var2);
                  if (!var7.wasReturnedFromScrap() && var7.getLayoutPosition() == var1) {
                     var7.addFlags(32);
                     return var7;
                  }
               }

               if (RecyclerView.this.mAdapter.hasStableIds()) {
                  var1 = RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
                  if (var1 > 0 && var1 < RecyclerView.this.mAdapter.getItemCount()) {
                     long var4 = RecyclerView.this.mAdapter.getItemId(var1);

                     for(var1 = 0; var1 < var3; ++var1) {
                        var7 = (RecyclerView.ViewHolder)this.mChangedScrap.get(var1);
                        if (!var7.wasReturnedFromScrap() && var7.getItemId() == var4) {
                           var7.addFlags(32);
                           return var7;
                        }
                     }
                  }
               }

               return null;
            }
         }
      }

      RecyclerView.RecycledViewPool getRecycledViewPool() {
         if (this.mRecyclerPool == null) {
            this.mRecyclerPool = new RecyclerView.RecycledViewPool();
         }

         return this.mRecyclerPool;
      }

      int getScrapCount() {
         return this.mAttachedScrap.size();
      }

      public List getScrapList() {
         return this.mUnmodifiableAttachedScrap;
      }

      RecyclerView.ViewHolder getScrapOrCachedViewForId(long var1, int var3, boolean var4) {
         int var5;
         RecyclerView.ViewHolder var6;
         for(var5 = this.mAttachedScrap.size() - 1; var5 >= 0; --var5) {
            var6 = (RecyclerView.ViewHolder)this.mAttachedScrap.get(var5);
            if (var6.getItemId() == var1 && !var6.wasReturnedFromScrap()) {
               if (var3 == var6.getItemViewType()) {
                  var6.addFlags(32);
                  if (var6.isRemoved() && !RecyclerView.this.mState.isPreLayout()) {
                     var6.setFlags(2, 14);
                  }

                  return var6;
               }

               if (!var4) {
                  this.mAttachedScrap.remove(var5);
                  RecyclerView.this.removeDetachedView(var6.itemView, false);
                  this.quickRecycleScrapView(var6.itemView);
               }
            }
         }

         for(var5 = this.mCachedViews.size() - 1; var5 >= 0; --var5) {
            var6 = (RecyclerView.ViewHolder)this.mCachedViews.get(var5);
            if (var6.getItemId() == var1 && !var6.isAttachedToTransitionOverlay()) {
               if (var3 == var6.getItemViewType()) {
                  if (!var4) {
                     this.mCachedViews.remove(var5);
                  }

                  return var6;
               }

               if (!var4) {
                  this.recycleCachedViewAt(var5);
                  return null;
               }
            }
         }

         return null;
      }

      RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int var1, boolean var2) {
         int var4 = this.mAttachedScrap.size();

         int var3;
         RecyclerView.ViewHolder var5;
         for(var3 = 0; var3 < var4; ++var3) {
            var5 = (RecyclerView.ViewHolder)this.mAttachedScrap.get(var3);
            if (!var5.wasReturnedFromScrap() && var5.getLayoutPosition() == var1 && !var5.isInvalid() && (RecyclerView.this.mState.mInPreLayout || !var5.isRemoved())) {
               var5.addFlags(32);
               return var5;
            }
         }

         if (!var2) {
            View var6 = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(var1);
            if (var6 != null) {
               var5 = RecyclerView.getChildViewHolderInt(var6);
               RecyclerView.this.mChildHelper.unhide(var6);
               var1 = RecyclerView.this.mChildHelper.indexOfChild(var6);
               if (var1 != -1) {
                  RecyclerView.this.mChildHelper.detachViewFromParent(var1);
                  this.scrapView(var6);
                  var5.addFlags(8224);
                  return var5;
               }

               StringBuilder var7 = new StringBuilder();
               var7.append("layout index should not be -1 after unhiding a view:");
               var7.append(var5);
               var7.append(RecyclerView.this.exceptionLabel());
               throw new IllegalStateException(var7.toString());
            }
         }

         var4 = this.mCachedViews.size();

         for(var3 = 0; var3 < var4; ++var3) {
            var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var3);
            if (!var5.isInvalid() && var5.getLayoutPosition() == var1 && !var5.isAttachedToTransitionOverlay()) {
               if (!var2) {
                  this.mCachedViews.remove(var3);
               }

               return var5;
            }
         }

         return null;
      }

      View getScrapViewAt(int var1) {
         return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(var1)).itemView;
      }

      public View getViewForPosition(int var1) {
         return this.getViewForPosition(var1, false);
      }

      View getViewForPosition(int var1, boolean var2) {
         return this.tryGetViewHolderForPositionByDeadline(var1, var2, Long.MAX_VALUE).itemView;
      }

      void markItemDecorInsetsDirty() {
         int var2 = this.mCachedViews.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            RecyclerView.LayoutParams var3 = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(var1)).itemView.getLayoutParams();
            if (var3 != null) {
               var3.mInsetsDirty = true;
            }
         }

      }

      void markKnownViewsInvalid() {
         int var2 = this.mCachedViews.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            RecyclerView.ViewHolder var3 = (RecyclerView.ViewHolder)this.mCachedViews.get(var1);
            if (var3 != null) {
               var3.addFlags(6);
               var3.addChangePayload((Object)null);
            }
         }

         if (RecyclerView.this.mAdapter == null || !RecyclerView.this.mAdapter.hasStableIds()) {
            this.recycleAndClearCachedViews();
         }

      }

      void offsetPositionRecordsForInsert(int var1, int var2) {
         int var4 = this.mCachedViews.size();

         for(int var3 = 0; var3 < var4; ++var3) {
            RecyclerView.ViewHolder var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var3);
            if (var5 != null && var5.mPosition >= var1) {
               var5.offsetPosition(var2, true);
            }
         }

      }

      void offsetPositionRecordsForMove(int var1, int var2) {
         int var3;
         int var4;
         byte var5;
         if (var1 < var2) {
            var3 = var1;
            var4 = var2;
            var5 = -1;
         } else {
            var3 = var2;
            var4 = var1;
            var5 = 1;
         }

         int var7 = this.mCachedViews.size();

         for(int var6 = 0; var6 < var7; ++var6) {
            RecyclerView.ViewHolder var8 = (RecyclerView.ViewHolder)this.mCachedViews.get(var6);
            if (var8 != null && var8.mPosition >= var3 && var8.mPosition <= var4) {
               if (var8.mPosition == var1) {
                  var8.offsetPosition(var2 - var1, false);
               } else {
                  var8.offsetPosition(var5, false);
               }
            }
         }

      }

      void offsetPositionRecordsForRemove(int var1, int var2, boolean var3) {
         for(int var4 = this.mCachedViews.size() - 1; var4 >= 0; --var4) {
            RecyclerView.ViewHolder var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var4);
            if (var5 != null) {
               if (var5.mPosition >= var1 + var2) {
                  var5.offsetPosition(-var2, var3);
               } else if (var5.mPosition >= var1) {
                  var5.addFlags(8);
                  this.recycleCachedViewAt(var4);
               }
            }
         }

      }

      void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2, boolean var3) {
         this.clear();
         this.getRecycledViewPool().onAdapterChanged(var1, var2, var3);
      }

      void quickRecycleScrapView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         var2.mScrapContainer = null;
         var2.mInChangeScrap = false;
         var2.clearReturnedFromScrapFlag();
         this.recycleViewHolderInternal(var2);
      }

      void recycleAndClearCachedViews() {
         for(int var1 = this.mCachedViews.size() - 1; var1 >= 0; --var1) {
            this.recycleCachedViewAt(var1);
         }

         this.mCachedViews.clear();
         if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
         }

      }

      void recycleCachedViewAt(int var1) {
         this.addViewHolderToRecycledViewPool((RecyclerView.ViewHolder)this.mCachedViews.get(var1), true);
         this.mCachedViews.remove(var1);
      }

      public void recycleView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         if (var2.isTmpDetached()) {
            RecyclerView.this.removeDetachedView(var1, false);
         }

         if (var2.isScrap()) {
            var2.unScrap();
         } else if (var2.wasReturnedFromScrap()) {
            var2.clearReturnedFromScrapFlag();
         }

         this.recycleViewHolderInternal(var2);
         if (RecyclerView.this.mItemAnimator != null && !var2.isRecyclable()) {
            RecyclerView.this.mItemAnimator.endAnimation(var2);
         }

      }

      void recycleViewHolderInternal(RecyclerView.ViewHolder var1) {
         boolean var8 = var1.isScrap();
         boolean var7 = false;
         StringBuilder var9;
         if (!var8 && var1.itemView.getParent() == null) {
            if (!var1.isTmpDetached()) {
               if (var1.shouldIgnore()) {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                  var10.append(RecyclerView.this.exceptionLabel());
                  throw new IllegalArgumentException(var10.toString());
               } else {
                  var7 = var1.doesTransientStatePreventRecycling();
                  boolean var2;
                  if (RecyclerView.this.mAdapter != null && var7 && RecyclerView.this.mAdapter.onFailedToRecycleView(var1)) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  boolean var3;
                  boolean var4;
                  label97: {
                     var3 = false;
                     boolean var6 = false;
                     boolean var5 = false;
                     if (!var2) {
                        var4 = var5;
                        if (!var1.isRecyclable()) {
                           break label97;
                        }
                     }

                     var2 = var6;
                     if (this.mViewCacheMax > 0) {
                        var2 = var6;
                        if (!var1.hasAnyOfTheFlags(526)) {
                           int var11 = this.mCachedViews.size();
                           int var12 = var11;
                           if (var11 >= this.mViewCacheMax) {
                              var12 = var11;
                              if (var11 > 0) {
                                 this.recycleCachedViewAt(0);
                                 var12 = var11 - 1;
                              }
                           }

                           int var13 = var12;
                           if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                              var13 = var12;
                              if (var12 > 0) {
                                 var13 = var12;
                                 if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(var1.mPosition)) {
                                    --var12;

                                    while(var12 >= 0) {
                                       var11 = ((RecyclerView.ViewHolder)this.mCachedViews.get(var12)).mPosition;
                                       if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(var11)) {
                                          break;
                                       }

                                       --var12;
                                    }

                                    var13 = var12 + 1;
                                 }
                              }
                           }

                           this.mCachedViews.add(var13, var1);
                           var2 = true;
                        }
                     }

                     var3 = var2;
                     var4 = var5;
                     if (!var2) {
                        this.addViewHolderToRecycledViewPool(var1, true);
                        var4 = true;
                        var3 = var2;
                     }
                  }

                  RecyclerView.this.mViewInfoStore.removeViewHolder(var1);
                  if (!var3 && !var4 && var7) {
                     var1.mOwnerRecyclerView = null;
                  }

               }
            } else {
               var9 = new StringBuilder();
               var9.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
               var9.append(var1);
               var9.append(RecyclerView.this.exceptionLabel());
               throw new IllegalArgumentException(var9.toString());
            }
         } else {
            var9 = new StringBuilder();
            var9.append("Scrapped or attached views may not be recycled. isScrap:");
            var9.append(var1.isScrap());
            var9.append(" isAttached:");
            if (var1.itemView.getParent() != null) {
               var7 = true;
            }

            var9.append(var7);
            var9.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(var9.toString());
         }
      }

      void scrapView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         if (!var2.hasAnyOfTheFlags(12) && var2.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder(var2)) {
            if (this.mChangedScrap == null) {
               this.mChangedScrap = new ArrayList();
            }

            var2.setScrapContainer(this, true);
            this.mChangedScrap.add(var2);
         } else if (var2.isInvalid() && !var2.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
            var3.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(var3.toString());
         } else {
            var2.setScrapContainer(this, false);
            this.mAttachedScrap.add(var2);
         }
      }

      void setRecycledViewPool(RecyclerView.RecycledViewPool var1) {
         RecyclerView.RecycledViewPool var2 = this.mRecyclerPool;
         if (var2 != null) {
            var2.detach();
         }

         this.mRecyclerPool = var1;
         if (var1 != null && RecyclerView.this.getAdapter() != null) {
            this.mRecyclerPool.attach();
         }

      }

      void setViewCacheExtension(RecyclerView.ViewCacheExtension var1) {
         this.mViewCacheExtension = var1;
      }

      public void setViewCacheSize(int var1) {
         this.mRequestedCacheMax = var1;
         this.updateViewCacheSize();
      }

      RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int var1, boolean var2, long var3) {
         StringBuilder var15;
         if (var1 >= 0 && var1 < RecyclerView.this.mState.getItemCount()) {
            boolean var6 = false;
            RecyclerView.ViewHolder var16 = null;
            boolean var10 = RecyclerView.this.mState.isPreLayout();
            boolean var9 = true;
            boolean var5;
            if (var10) {
               var16 = this.getChangedScrapViewForPosition(var1);
               if (var16 != null) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var6 = var5;
            }

            var5 = var6;
            RecyclerView.ViewHolder var19 = var16;
            if (var16 == null) {
               var16 = this.getScrapOrHiddenOrCachedHolderForPosition(var1, var2);
               var5 = var6;
               var19 = var16;
               if (var16 != null) {
                  if (!this.validateViewHolderForOffsetPosition(var16)) {
                     if (!var2) {
                        var16.addFlags(4);
                        if (var16.isScrap()) {
                           RecyclerView.this.removeDetachedView(var16.itemView, false);
                           var16.unScrap();
                        } else if (var16.wasReturnedFromScrap()) {
                           var16.clearReturnedFromScrapFlag();
                        }

                        this.recycleViewHolderInternal(var16);
                     }

                     var19 = null;
                     var5 = var6;
                  } else {
                     var5 = true;
                     var19 = var16;
                  }
               }
            }

            if (var19 == null) {
               int var8 = RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
               if (var8 < 0 || var8 >= RecyclerView.this.mAdapter.getItemCount()) {
                  var15 = new StringBuilder();
                  var15.append("Inconsistency detected. Invalid item position ");
                  var15.append(var1);
                  var15.append("(offset:");
                  var15.append(var8);
                  var15.append(").state:");
                  var15.append(RecyclerView.this.mState.getItemCount());
                  var15.append(RecyclerView.this.exceptionLabel());
                  throw new IndexOutOfBoundsException(var15.toString());
               }

               int var7 = RecyclerView.this.mAdapter.getItemViewType(var8);
               var6 = var5;
               if (RecyclerView.this.mAdapter.hasStableIds()) {
                  var16 = this.getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(var8), var7, var2);
                  var6 = var5;
                  var19 = var16;
                  if (var16 != null) {
                     var16.mPosition = var8;
                     var6 = true;
                     var19 = var16;
                  }
               }

               var16 = var19;
               if (var19 == null) {
                  RecyclerView.ViewCacheExtension var17 = this.mViewCacheExtension;
                  var16 = var19;
                  if (var17 != null) {
                     View var20 = var17.getViewForPositionAndType(this, var1, var7);
                     var16 = var19;
                     if (var20 != null) {
                        var16 = RecyclerView.this.getChildViewHolder(var20);
                        if (var16 == null) {
                           var15 = new StringBuilder();
                           var15.append("getViewForPositionAndType returned a view which does not have a ViewHolder");
                           var15.append(RecyclerView.this.exceptionLabel());
                           throw new IllegalArgumentException(var15.toString());
                        }

                        if (var16.shouldIgnore()) {
                           var15 = new StringBuilder();
                           var15.append("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                           var15.append(RecyclerView.this.exceptionLabel());
                           throw new IllegalArgumentException(var15.toString());
                        }
                     }
                  }
               }

               var19 = var16;
               if (var16 == null) {
                  var16 = this.getRecycledViewPool().getRecycledView(var7);
                  var19 = var16;
                  if (var16 != null) {
                     var16.resetInternal();
                     var19 = var16;
                     if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                        this.invalidateDisplayListInt(var16);
                        var19 = var16;
                     }
                  }
               }

               if (var19 == null) {
                  long var11 = RecyclerView.this.getNanoTime();
                  if (var3 != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(var7, var11, var3)) {
                     return null;
                  }

                  var19 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, var7);
                  if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                     RecyclerView var21 = RecyclerView.findNestedRecyclerView(var19.itemView);
                     if (var21 != null) {
                        var19.mNestedRecyclerView = new WeakReference(var21);
                     }
                  }

                  long var13 = RecyclerView.this.getNanoTime();
                  this.mRecyclerPool.factorInCreateTime(var7, var13 - var11);
                  var5 = var6;
               } else {
                  var5 = var6;
               }
            }

            if (var5 && !RecyclerView.this.mState.isPreLayout() && var19.hasAnyOfTheFlags(8192)) {
               var19.setFlags(0, 8192);
               if (RecyclerView.this.mState.mRunSimpleAnimations) {
                  int var18 = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var19);
                  RecyclerView.ItemAnimator.ItemHolderInfo var22 = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, var19, var18 | 4096, var19.getUnmodifiedPayloads());
                  RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(var19, var22);
               }
            }

            var2 = false;
            if (RecyclerView.this.mState.isPreLayout() && var19.isBound()) {
               var19.mPreLayoutPosition = var1;
            } else if (!var19.isBound() || var19.needsUpdate() || var19.isInvalid()) {
               var2 = this.tryBindViewHolderByDeadline(var19, RecyclerView.this.mAdapterHelper.findPositionOffset(var1), var1, var3);
            }

            android.view.ViewGroup.LayoutParams var23 = var19.itemView.getLayoutParams();
            RecyclerView.LayoutParams var24;
            if (var23 == null) {
               var24 = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
               var19.itemView.setLayoutParams(var24);
            } else if (!RecyclerView.this.checkLayoutParams(var23)) {
               var24 = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams(var23);
               var19.itemView.setLayoutParams(var24);
            } else {
               var24 = (RecyclerView.LayoutParams)var23;
            }

            var24.mViewHolder = var19;
            if (var5 && var2) {
               var2 = var9;
            } else {
               var2 = false;
            }

            var24.mPendingInvalidate = var2;
            return var19;
         } else {
            var15 = new StringBuilder();
            var15.append("Invalid item position ");
            var15.append(var1);
            var15.append("(");
            var15.append(var1);
            var15.append("). Item count:");
            var15.append(RecyclerView.this.mState.getItemCount());
            var15.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(var15.toString());
         }
      }

      void unscrapView(RecyclerView.ViewHolder var1) {
         if (var1.mInChangeScrap) {
            this.mChangedScrap.remove(var1);
         } else {
            this.mAttachedScrap.remove(var1);
         }

         var1.mScrapContainer = null;
         var1.mInChangeScrap = false;
         var1.clearReturnedFromScrapFlag();
      }

      void updateViewCacheSize() {
         int var1;
         if (RecyclerView.this.mLayout != null) {
            var1 = RecyclerView.this.mLayout.mPrefetchMaxCountObserved;
         } else {
            var1 = 0;
         }

         this.mViewCacheMax = this.mRequestedCacheMax + var1;

         for(var1 = this.mCachedViews.size() - 1; var1 >= 0 && this.mCachedViews.size() > this.mViewCacheMax; --var1) {
            this.recycleCachedViewAt(var1);
         }

      }

      boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder var1) {
         if (var1.isRemoved()) {
            return RecyclerView.this.mState.isPreLayout();
         } else if (var1.mPosition >= 0 && var1.mPosition < RecyclerView.this.mAdapter.getItemCount()) {
            boolean var3 = RecyclerView.this.mState.isPreLayout();
            boolean var2 = false;
            if (!var3 && RecyclerView.this.mAdapter.getItemViewType(var1.mPosition) != var1.getItemViewType()) {
               return false;
            } else if (RecyclerView.this.mAdapter.hasStableIds()) {
               if (var1.getItemId() == RecyclerView.this.mAdapter.getItemId(var1.mPosition)) {
                  var2 = true;
               }

               return var2;
            } else {
               return true;
            }
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Inconsistency detected. Invalid view holder adapter position");
            var4.append(var1);
            var4.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(var4.toString());
         }
      }

      void viewRangeUpdate(int var1, int var2) {
         for(int var3 = this.mCachedViews.size() - 1; var3 >= 0; --var3) {
            RecyclerView.ViewHolder var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var3);
            if (var5 != null) {
               int var4 = var5.mPosition;
               if (var4 >= var1 && var4 < var1 + var2) {
                  var5.addFlags(2);
                  this.recycleCachedViewAt(var3);
               }
            }
         }

      }
   }

   public interface RecyclerListener {
      void onViewRecycled(RecyclerView.ViewHolder var1);
   }

   private class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
      RecyclerViewDataObserver() {
      }

      public void onChanged() {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         RecyclerView.this.mState.mStructureChanged = true;
         RecyclerView.this.processDataSetCompletelyChanged(true);
         if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
            RecyclerView.this.requestLayout();
         }

      }

      public void onItemRangeChanged(int var1, int var2, Object var3) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(var1, var2, var3)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeInserted(int var1, int var2) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(var1, var2)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeMoved(int var1, int var2, int var3) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(var1, var2, var3)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeRemoved(int var1, int var2) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(var1, var2)) {
            this.triggerUpdateProcessor();
         }

      }

      void triggerUpdateProcessor() {
         if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
            RecyclerView var1 = RecyclerView.this;
            ViewCompat.postOnAnimation(var1, var1.mUpdateChildViewsRunnable);
         } else {
            RecyclerView.this.mAdapterUpdateDuringMeasure = true;
            RecyclerView.this.requestLayout();
         }
      }
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public RecyclerView.SavedState createFromParcel(Parcel var1) {
            return new RecyclerView.SavedState(var1, (ClassLoader)null);
         }

         public RecyclerView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new RecyclerView.SavedState(var1, var2);
         }

         public RecyclerView.SavedState[] newArray(int var1) {
            return new RecyclerView.SavedState[var1];
         }
      };
      Parcelable mLayoutState;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         if (var2 == null) {
            var2 = RecyclerView.LayoutManager.class.getClassLoader();
         }

         this.mLayoutState = var1.readParcelable(var2);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      void copyFrom(RecyclerView.SavedState var1) {
         this.mLayoutState = var1.mLayoutState;
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeParcelable(this.mLayoutState, 0);
      }
   }

   public static class SimpleOnItemTouchListener implements RecyclerView.OnItemTouchListener {
      public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2) {
         return false;
      }

      public void onRequestDisallowInterceptTouchEvent(boolean var1) {
      }

      public void onTouchEvent(RecyclerView var1, MotionEvent var2) {
      }
   }

   public abstract static class SmoothScroller {
      private RecyclerView.LayoutManager mLayoutManager;
      private boolean mPendingInitialRun;
      private RecyclerView mRecyclerView;
      private final RecyclerView.SmoothScroller.Action mRecyclingAction = new RecyclerView.SmoothScroller.Action(0, 0);
      private boolean mRunning;
      private boolean mStarted;
      private int mTargetPosition = -1;
      private View mTargetView;

      public PointF computeScrollVectorForPosition(int var1) {
         RecyclerView.LayoutManager var2 = this.getLayoutManager();
         if (var2 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return ((RecyclerView.SmoothScroller.ScrollVectorProvider)var2).computeScrollVectorForPosition(var1);
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
            var3.append(RecyclerView.SmoothScroller.ScrollVectorProvider.class.getCanonicalName());
            Log.w("RecyclerView", var3.toString());
            return null;
         }
      }

      public View findViewByPosition(int var1) {
         return this.mRecyclerView.mLayout.findViewByPosition(var1);
      }

      public int getChildCount() {
         return this.mRecyclerView.mLayout.getChildCount();
      }

      public int getChildPosition(View var1) {
         return this.mRecyclerView.getChildLayoutPosition(var1);
      }

      public RecyclerView.LayoutManager getLayoutManager() {
         return this.mLayoutManager;
      }

      public int getTargetPosition() {
         return this.mTargetPosition;
      }

      @Deprecated
      public void instantScrollToPosition(int var1) {
         this.mRecyclerView.scrollToPosition(var1);
      }

      public boolean isPendingInitialRun() {
         return this.mPendingInitialRun;
      }

      public boolean isRunning() {
         return this.mRunning;
      }

      protected void normalize(PointF var1) {
         float var2 = (float)Math.sqrt((double)(var1.x * var1.x + var1.y * var1.y));
         var1.x /= var2;
         var1.y /= var2;
      }

      void onAnimation(int var1, int var2) {
         RecyclerView var4 = this.mRecyclerView;
         if (this.mTargetPosition == -1 || var4 == null) {
            this.stop();
         }

         if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null) {
            PointF var5 = this.computeScrollVectorForPosition(this.mTargetPosition);
            if (var5 != null && (var5.x != 0.0F || var5.y != 0.0F)) {
               var4.scrollStep((int)Math.signum(var5.x), (int)Math.signum(var5.y), (int[])null);
            }
         }

         this.mPendingInitialRun = false;
         View var6 = this.mTargetView;
         if (var6 != null) {
            if (this.getChildPosition(var6) == this.mTargetPosition) {
               this.onTargetFound(this.mTargetView, var4.mState, this.mRecyclingAction);
               this.mRecyclingAction.runIfNecessary(var4);
               this.stop();
            } else {
               Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
               this.mTargetView = null;
            }
         }

         if (this.mRunning) {
            this.onSeekTargetStep(var1, var2, var4.mState, this.mRecyclingAction);
            boolean var3 = this.mRecyclingAction.hasJumpTarget();
            this.mRecyclingAction.runIfNecessary(var4);
            if (var3 && this.mRunning) {
               this.mPendingInitialRun = true;
               var4.mViewFlinger.postOnAnimation();
            }
         }

      }

      protected void onChildAttachedToWindow(View var1) {
         if (this.getChildPosition(var1) == this.getTargetPosition()) {
            this.mTargetView = var1;
         }

      }

      protected abstract void onSeekTargetStep(int var1, int var2, RecyclerView.State var3, RecyclerView.SmoothScroller.Action var4);

      protected abstract void onStart();

      protected abstract void onStop();

      protected abstract void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3);

      public void setTargetPosition(int var1) {
         this.mTargetPosition = var1;
      }

      void start(RecyclerView var1, RecyclerView.LayoutManager var2) {
         var1.mViewFlinger.stop();
         if (this.mStarted) {
            StringBuilder var3 = new StringBuilder();
            var3.append("An instance of ");
            var3.append(this.getClass().getSimpleName());
            var3.append(" was started more than once. Each instance of");
            var3.append(this.getClass().getSimpleName());
            var3.append(" is intended to only be used once. You should create a new instance for each use.");
            Log.w("RecyclerView", var3.toString());
         }

         this.mRecyclerView = var1;
         this.mLayoutManager = var2;
         if (this.mTargetPosition != -1) {
            var1.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = this.findViewByPosition(this.getTargetPosition());
            this.onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
            this.mStarted = true;
         } else {
            throw new IllegalArgumentException("Invalid target position");
         }
      }

      protected final void stop() {
         if (this.mRunning) {
            this.mRunning = false;
            this.onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
         }
      }

      public static class Action {
         public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
         private boolean mChanged;
         private int mConsecutiveUpdates;
         private int mDuration;
         private int mDx;
         private int mDy;
         private Interpolator mInterpolator;
         private int mJumpToPosition;

         public Action(int var1, int var2) {
            this(var1, var2, Integer.MIN_VALUE, (Interpolator)null);
         }

         public Action(int var1, int var2, int var3) {
            this(var1, var2, var3, (Interpolator)null);
         }

         public Action(int var1, int var2, int var3, Interpolator var4) {
            this.mJumpToPosition = -1;
            this.mChanged = false;
            this.mConsecutiveUpdates = 0;
            this.mDx = var1;
            this.mDy = var2;
            this.mDuration = var3;
            this.mInterpolator = var4;
         }

         private void validate() {
            if (this.mInterpolator != null && this.mDuration < 1) {
               throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
            } else if (this.mDuration < 1) {
               throw new IllegalStateException("Scroll duration must be a positive number");
            }
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

         public void jumpTo(int var1) {
            this.mJumpToPosition = var1;
         }

         void runIfNecessary(RecyclerView var1) {
            int var2;
            if (this.mJumpToPosition >= 0) {
               var2 = this.mJumpToPosition;
               this.mJumpToPosition = -1;
               var1.jumpToPositionForSmoothScroller(var2);
               this.mChanged = false;
            } else if (this.mChanged) {
               this.validate();
               var1.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
               var2 = this.mConsecutiveUpdates + 1;
               this.mConsecutiveUpdates = var2;
               if (var2 > 10) {
                  Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
               }

               this.mChanged = false;
            } else {
               this.mConsecutiveUpdates = 0;
            }
         }

         public void setDuration(int var1) {
            this.mChanged = true;
            this.mDuration = var1;
         }

         public void setDx(int var1) {
            this.mChanged = true;
            this.mDx = var1;
         }

         public void setDy(int var1) {
            this.mChanged = true;
            this.mDy = var1;
         }

         public void setInterpolator(Interpolator var1) {
            this.mChanged = true;
            this.mInterpolator = var1;
         }

         public void update(int var1, int var2, int var3, Interpolator var4) {
            this.mDx = var1;
            this.mDy = var2;
            this.mDuration = var3;
            this.mInterpolator = var4;
            this.mChanged = true;
         }
      }

      public interface ScrollVectorProvider {
         PointF computeScrollVectorForPosition(int var1);
      }
   }

   public static class State {
      static final int STEP_ANIMATIONS = 4;
      static final int STEP_LAYOUT = 2;
      static final int STEP_START = 1;
      private SparseArray mData;
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
      int mTargetPosition = -1;
      boolean mTrackOldChangeHolders = false;

      void assertLayoutStep(int var1) {
         if ((this.mLayoutStep & var1) == 0) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Layout state should be one of ");
            var2.append(Integer.toBinaryString(var1));
            var2.append(" but it is ");
            var2.append(Integer.toBinaryString(this.mLayoutStep));
            throw new IllegalStateException(var2.toString());
         }
      }

      public boolean didStructureChange() {
         return this.mStructureChanged;
      }

      public Object get(int var1) {
         SparseArray var2 = this.mData;
         return var2 == null ? null : var2.get(var1);
      }

      public int getItemCount() {
         return this.mInPreLayout ? this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout : this.mItemCount;
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

      void prepareForNestedPrefetch(RecyclerView.Adapter var1) {
         this.mLayoutStep = 1;
         this.mItemCount = var1.getItemCount();
         this.mInPreLayout = false;
         this.mTrackOldChangeHolders = false;
         this.mIsMeasuring = false;
      }

      public void put(int var1, Object var2) {
         if (this.mData == null) {
            this.mData = new SparseArray();
         }

         this.mData.put(var1, var2);
      }

      public void remove(int var1) {
         SparseArray var2 = this.mData;
         if (var2 != null) {
            var2.remove(var1);
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("State{mTargetPosition=");
         var1.append(this.mTargetPosition);
         var1.append(", mData=");
         var1.append(this.mData);
         var1.append(", mItemCount=");
         var1.append(this.mItemCount);
         var1.append(", mIsMeasuring=");
         var1.append(this.mIsMeasuring);
         var1.append(", mPreviousLayoutItemCount=");
         var1.append(this.mPreviousLayoutItemCount);
         var1.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
         var1.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
         var1.append(", mStructureChanged=");
         var1.append(this.mStructureChanged);
         var1.append(", mInPreLayout=");
         var1.append(this.mInPreLayout);
         var1.append(", mRunSimpleAnimations=");
         var1.append(this.mRunSimpleAnimations);
         var1.append(", mRunPredictiveAnimations=");
         var1.append(this.mRunPredictiveAnimations);
         var1.append('}');
         return var1.toString();
      }

      public boolean willRunPredictiveAnimations() {
         return this.mRunPredictiveAnimations;
      }

      public boolean willRunSimpleAnimations() {
         return this.mRunSimpleAnimations;
      }
   }

   public abstract static class ViewCacheExtension {
      public abstract View getViewForPositionAndType(RecyclerView.Recycler var1, int var2, int var3);
   }

   class ViewFlinger implements Runnable {
      private boolean mEatRunOnAnimationRequest;
      Interpolator mInterpolator;
      private int mLastFlingX;
      private int mLastFlingY;
      OverScroller mOverScroller;
      private boolean mReSchedulePostAnimationCallback;

      ViewFlinger() {
         this.mInterpolator = RecyclerView.sQuinticInterpolator;
         this.mEatRunOnAnimationRequest = false;
         this.mReSchedulePostAnimationCallback = false;
         this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
      }

      private int computeScrollDuration(int var1, int var2, int var3, int var4) {
         int var9 = Math.abs(var1);
         int var10 = Math.abs(var2);
         boolean var8;
         if (var9 > var10) {
            var8 = true;
         } else {
            var8 = false;
         }

         var3 = (int)Math.sqrt((double)(var3 * var3 + var4 * var4));
         var2 = (int)Math.sqrt((double)(var1 * var1 + var2 * var2));
         RecyclerView var11 = RecyclerView.this;
         if (var8) {
            var1 = var11.getWidth();
         } else {
            var1 = var11.getHeight();
         }

         var4 = var1 / 2;
         float var7 = Math.min(1.0F, (float)var2 * 1.0F / (float)var1);
         float var5 = (float)var4;
         float var6 = (float)var4;
         var7 = this.distanceInfluenceForSnapDuration(var7);
         if (var3 > 0) {
            var1 = Math.round(Math.abs((var5 + var6 * var7) / (float)var3) * 1000.0F) * 4;
         } else {
            if (var8) {
               var2 = var9;
            } else {
               var2 = var10;
            }

            var1 = (int)(((float)var2 / (float)var1 + 1.0F) * 300.0F);
         }

         return Math.min(var1, 2000);
      }

      private float distanceInfluenceForSnapDuration(float var1) {
         return (float)Math.sin((double)((var1 - 0.5F) * 0.47123894F));
      }

      private void internalPostOnAnimation() {
         RecyclerView.this.removeCallbacks(this);
         ViewCompat.postOnAnimation(RecyclerView.this, this);
      }

      public void fling(int var1, int var2) {
         RecyclerView.this.setScrollState(2);
         this.mLastFlingY = 0;
         this.mLastFlingX = 0;
         if (this.mInterpolator != RecyclerView.sQuinticInterpolator) {
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
         }

         this.mOverScroller.fling(0, 0, var1, var2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
         this.postOnAnimation();
      }

      void postOnAnimation() {
         if (this.mEatRunOnAnimationRequest) {
            this.mReSchedulePostAnimationCallback = true;
         } else {
            this.internalPostOnAnimation();
         }
      }

      public void run() {
         if (RecyclerView.this.mLayout == null) {
            this.stop();
         } else {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            RecyclerView.this.consumePendingUpdateOperations();
            OverScroller var9 = this.mOverScroller;
            if (var9.computeScrollOffset()) {
               int var1 = var9.getCurrX();
               int var2 = var9.getCurrY();
               int var6 = var1 - this.mLastFlingX;
               int var5 = var2 - this.mLastFlingY;
               this.mLastFlingX = var1;
               this.mLastFlingY = var2;
               int var3 = 0;
               int var4 = 0;
               RecyclerView.this.mReusableIntPair[0] = 0;
               RecyclerView.this.mReusableIntPair[1] = 0;
               RecyclerView var10 = RecyclerView.this;
               var2 = var6;
               var1 = var5;
               if (var10.dispatchNestedPreScroll(var6, var5, var10.mReusableIntPair, (int[])null, 1)) {
                  var2 = var6 - RecyclerView.this.mReusableIntPair[0];
                  var1 = var5 - RecyclerView.this.mReusableIntPair[1];
               }

               if (RecyclerView.this.getOverScrollMode() != 2) {
                  RecyclerView.this.considerReleasingGlowsOnScroll(var2, var1);
               }

               var6 = var2;
               var5 = var1;
               RecyclerView.SmoothScroller var14;
               if (RecyclerView.this.mAdapter != null) {
                  RecyclerView.this.mReusableIntPair[0] = 0;
                  RecyclerView.this.mReusableIntPair[1] = 0;
                  var10 = RecyclerView.this;
                  var10.scrollStep(var2, var1, var10.mReusableIntPair);
                  int var7 = RecyclerView.this.mReusableIntPair[0];
                  int var8 = RecyclerView.this.mReusableIntPair[1];
                  var2 -= var7;
                  var1 -= var8;
                  var14 = RecyclerView.this.mLayout.mSmoothScroller;
                  var6 = var2;
                  var5 = var1;
                  var3 = var7;
                  var4 = var8;
                  if (var14 != null) {
                     var6 = var2;
                     var5 = var1;
                     var3 = var7;
                     var4 = var8;
                     if (!var14.isPendingInitialRun()) {
                        var6 = var2;
                        var5 = var1;
                        var3 = var7;
                        var4 = var8;
                        if (var14.isRunning()) {
                           var3 = RecyclerView.this.mState.getItemCount();
                           if (var3 == 0) {
                              var14.stop();
                              var6 = var2;
                              var5 = var1;
                              var3 = var7;
                              var4 = var8;
                           } else if (var14.getTargetPosition() >= var3) {
                              var14.setTargetPosition(var3 - 1);
                              var14.onAnimation(var7, var8);
                              var6 = var2;
                              var5 = var1;
                              var3 = var7;
                              var4 = var8;
                           } else {
                              var14.onAnimation(var7, var8);
                              var4 = var8;
                              var3 = var7;
                              var5 = var1;
                              var6 = var2;
                           }
                        }
                     }
                  }
               }

               if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                  RecyclerView.this.invalidate();
               }

               RecyclerView.this.mReusableIntPair[0] = 0;
               RecyclerView.this.mReusableIntPair[1] = 0;
               var10 = RecyclerView.this;
               var10.dispatchNestedScroll(var3, var4, var6, var5, (int[])null, 1, var10.mReusableIntPair);
               var6 -= RecyclerView.this.mReusableIntPair[0];
               var5 -= RecyclerView.this.mReusableIntPair[1];
               if (var3 != 0 || var4 != 0) {
                  RecyclerView.this.dispatchOnScrolled(var3, var4);
               }

               if (!RecyclerView.this.awakenScrollBars()) {
                  RecyclerView.this.invalidate();
               }

               boolean var11;
               if (var9.getCurrX() == var9.getFinalX()) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               boolean var12;
               if (var9.getCurrY() == var9.getFinalY()) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               if (var9.isFinished() || (var11 || var6 != 0) && (var12 || var5 != 0)) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               var14 = RecyclerView.this.mLayout.mSmoothScroller;
               if (var14 != null && var14.isPendingInitialRun()) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               if (!var12 && var11) {
                  if (RecyclerView.this.getOverScrollMode() != 2) {
                     var2 = (int)var9.getCurrVelocity();
                     if (var6 < 0) {
                        var1 = -var2;
                     } else if (var6 > 0) {
                        var1 = var2;
                     } else {
                        var1 = 0;
                     }

                     if (var5 < 0) {
                        var2 = -var2;
                     } else if (var5 <= 0) {
                        var2 = 0;
                     }

                     RecyclerView.this.absorbGlows(var1, var2);
                  }

                  if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                     RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                  }
               } else {
                  this.postOnAnimation();
                  if (RecyclerView.this.mGapWorker != null) {
                     RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, var3, var4);
                  }
               }
            }

            RecyclerView.SmoothScroller var13 = RecyclerView.this.mLayout.mSmoothScroller;
            if (var13 != null && var13.isPendingInitialRun()) {
               var13.onAnimation(0, 0);
            }

            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
               this.internalPostOnAnimation();
            } else {
               RecyclerView.this.setScrollState(0);
               RecyclerView.this.stopNestedScroll(1);
            }
         }
      }

      public void smoothScrollBy(int var1, int var2, int var3, Interpolator var4) {
         int var5 = var3;
         if (var3 == Integer.MIN_VALUE) {
            var5 = this.computeScrollDuration(var1, var2, 0, 0);
         }

         Interpolator var6 = var4;
         if (var4 == null) {
            var6 = RecyclerView.sQuinticInterpolator;
         }

         if (this.mInterpolator != var6) {
            this.mInterpolator = var6;
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), var6);
         }

         this.mLastFlingY = 0;
         this.mLastFlingX = 0;
         RecyclerView.this.setScrollState(2);
         this.mOverScroller.startScroll(0, 0, var1, var2, var5);
         if (VERSION.SDK_INT < 23) {
            this.mOverScroller.computeScrollOffset();
         }

         this.postOnAnimation();
      }

      public void stop() {
         RecyclerView.this.removeCallbacks(this);
         this.mOverScroller.abortAnimation();
      }
   }

   public abstract static class ViewHolder {
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
      static final int FLAG_TMP_DETACHED = 256;
      static final int FLAG_UPDATE = 2;
      private static final List FULLUPDATE_PAYLOADS = Collections.emptyList();
      static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
      public final View itemView;
      int mFlags;
      boolean mInChangeScrap = false;
      private int mIsRecyclableCount = 0;
      long mItemId = -1L;
      int mItemViewType = -1;
      WeakReference mNestedRecyclerView;
      int mOldPosition = -1;
      RecyclerView mOwnerRecyclerView;
      List mPayloads = null;
      int mPendingAccessibilityState = -1;
      int mPosition = -1;
      int mPreLayoutPosition = -1;
      RecyclerView.Recycler mScrapContainer = null;
      RecyclerView.ViewHolder mShadowedHolder = null;
      RecyclerView.ViewHolder mShadowingHolder = null;
      List mUnmodifiedPayloads = null;
      private int mWasImportantForAccessibilityBeforeHidden = 0;

      public ViewHolder(View var1) {
         if (var1 != null) {
            this.itemView = var1;
         } else {
            throw new IllegalArgumentException("itemView may not be null");
         }
      }

      private void createPayloadsIfNeeded() {
         if (this.mPayloads == null) {
            ArrayList var1 = new ArrayList();
            this.mPayloads = var1;
            this.mUnmodifiedPayloads = Collections.unmodifiableList(var1);
         }

      }

      void addChangePayload(Object var1) {
         if (var1 == null) {
            this.addFlags(1024);
         } else {
            if ((1024 & this.mFlags) == 0) {
               this.createPayloadsIfNeeded();
               this.mPayloads.add(var1);
            }

         }
      }

      void addFlags(int var1) {
         this.mFlags |= var1;
      }

      void clearOldPosition() {
         this.mOldPosition = -1;
         this.mPreLayoutPosition = -1;
      }

      void clearPayload() {
         List var1 = this.mPayloads;
         if (var1 != null) {
            var1.clear();
         }

         this.mFlags &= -1025;
      }

      void clearReturnedFromScrapFlag() {
         this.mFlags &= -33;
      }

      void clearTmpDetachFlag() {
         this.mFlags &= -257;
      }

      boolean doesTransientStatePreventRecycling() {
         return (this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView);
      }

      void flagRemovedAndOffsetPosition(int var1, int var2, boolean var3) {
         this.addFlags(8);
         this.offsetPosition(var2, var3);
         this.mPosition = var1;
      }

      public final int getAdapterPosition() {
         RecyclerView var1 = this.mOwnerRecyclerView;
         return var1 == null ? -1 : var1.getAdapterPositionFor(this);
      }

      public final long getItemId() {
         return this.mItemId;
      }

      public final int getItemViewType() {
         return this.mItemViewType;
      }

      public final int getLayoutPosition() {
         int var2 = this.mPreLayoutPosition;
         int var1 = var2;
         if (var2 == -1) {
            var1 = this.mPosition;
         }

         return var1;
      }

      public final int getOldPosition() {
         return this.mOldPosition;
      }

      @Deprecated
      public final int getPosition() {
         int var2 = this.mPreLayoutPosition;
         int var1 = var2;
         if (var2 == -1) {
            var1 = this.mPosition;
         }

         return var1;
      }

      List getUnmodifiedPayloads() {
         if ((this.mFlags & 1024) == 0) {
            List var1 = this.mPayloads;
            return var1 != null && var1.size() != 0 ? this.mUnmodifiedPayloads : FULLUPDATE_PAYLOADS;
         } else {
            return FULLUPDATE_PAYLOADS;
         }
      }

      boolean hasAnyOfTheFlags(int var1) {
         return (this.mFlags & var1) != 0;
      }

      boolean isAdapterPositionUnknown() {
         return (this.mFlags & 512) != 0 || this.isInvalid();
      }

      boolean isAttachedToTransitionOverlay() {
         return this.itemView.getParent() != null && this.itemView.getParent() != this.mOwnerRecyclerView;
      }

      boolean isBound() {
         return (this.mFlags & 1) != 0;
      }

      boolean isInvalid() {
         return (this.mFlags & 4) != 0;
      }

      public final boolean isRecyclable() {
         return (this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView);
      }

      boolean isRemoved() {
         return (this.mFlags & 8) != 0;
      }

      boolean isScrap() {
         return this.mScrapContainer != null;
      }

      boolean isTmpDetached() {
         return (this.mFlags & 256) != 0;
      }

      boolean isUpdated() {
         return (this.mFlags & 2) != 0;
      }

      boolean needsUpdate() {
         return (this.mFlags & 2) != 0;
      }

      void offsetPosition(int var1, boolean var2) {
         if (this.mOldPosition == -1) {
            this.mOldPosition = this.mPosition;
         }

         if (this.mPreLayoutPosition == -1) {
            this.mPreLayoutPosition = this.mPosition;
         }

         if (var2) {
            this.mPreLayoutPosition += var1;
         }

         this.mPosition += var1;
         if (this.itemView.getLayoutParams() != null) {
            ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
         }

      }

      void onEnteredHiddenState(RecyclerView var1) {
         int var2 = this.mPendingAccessibilityState;
         if (var2 != -1) {
            this.mWasImportantForAccessibilityBeforeHidden = var2;
         } else {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
         }

         var1.setChildImportantForAccessibilityInternal(this, 4);
      }

      void onLeftHiddenState(RecyclerView var1) {
         var1.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
         this.mWasImportantForAccessibilityBeforeHidden = 0;
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

      void setFlags(int var1, int var2) {
         this.mFlags = this.mFlags & var2 | var1 & var2;
      }

      public final void setIsRecyclable(boolean var1) {
         int var2 = this.mIsRecyclableCount;
         if (var1) {
            --var2;
         } else {
            ++var2;
         }

         this.mIsRecyclableCount = var2;
         if (var2 < 0) {
            this.mIsRecyclableCount = 0;
            StringBuilder var3 = new StringBuilder();
            var3.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
            var3.append(this);
            Log.e("View", var3.toString());
         } else if (!var1 && var2 == 1) {
            this.mFlags |= 16;
         } else {
            if (var1 && this.mIsRecyclableCount == 0) {
               this.mFlags &= -17;
            }

         }
      }

      void setScrapContainer(RecyclerView.Recycler var1, boolean var2) {
         this.mScrapContainer = var1;
         this.mInChangeScrap = var2;
      }

      boolean shouldBeKeptAsChild() {
         return (this.mFlags & 16) != 0;
      }

      boolean shouldIgnore() {
         return (this.mFlags & 128) != 0;
      }

      void stopIgnoring() {
         this.mFlags &= -129;
      }

      public String toString() {
         String var1;
         if (this.getClass().isAnonymousClass()) {
            var1 = "ViewHolder";
         } else {
            var1 = this.getClass().getSimpleName();
         }

         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append("{");
         var2.append(Integer.toHexString(this.hashCode()));
         var2.append(" position=");
         var2.append(this.mPosition);
         var2.append(" id=");
         var2.append(this.mItemId);
         var2.append(", oldPos=");
         var2.append(this.mOldPosition);
         var2.append(", pLpos:");
         var2.append(this.mPreLayoutPosition);
         var2 = new StringBuilder(var2.toString());
         if (this.isScrap()) {
            var2.append(" scrap ");
            if (this.mInChangeScrap) {
               var1 = "[changeScrap]";
            } else {
               var1 = "[attachedScrap]";
            }

            var2.append(var1);
         }

         if (this.isInvalid()) {
            var2.append(" invalid");
         }

         if (!this.isBound()) {
            var2.append(" unbound");
         }

         if (this.needsUpdate()) {
            var2.append(" update");
         }

         if (this.isRemoved()) {
            var2.append(" removed");
         }

         if (this.shouldIgnore()) {
            var2.append(" ignored");
         }

         if (this.isTmpDetached()) {
            var2.append(" tmpDetached");
         }

         if (!this.isRecyclable()) {
            StringBuilder var3 = new StringBuilder();
            var3.append(" not recyclable(");
            var3.append(this.mIsRecyclableCount);
            var3.append(")");
            var2.append(var3.toString());
         }

         if (this.isAdapterPositionUnknown()) {
            var2.append(" undefined adapter position");
         }

         if (this.itemView.getParent() == null) {
            var2.append(" no parent");
         }

         var2.append("}");
         return var2.toString();
      }

      void unScrap() {
         this.mScrapContainer.unscrapView(this);
      }

      boolean wasReturnedFromScrap() {
         return (this.mFlags & 32) != 0;
      }
   }
}
