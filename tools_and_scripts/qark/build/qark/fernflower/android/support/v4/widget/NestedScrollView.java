package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import java.util.ArrayList;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent, NestedScrollingChild2, ScrollingView {
   private static final NestedScrollView.AccessibilityDelegate ACCESSIBILITY_DELEGATE = new NestedScrollView.AccessibilityDelegate();
   static final int ANIMATED_SCROLL_GAP = 250;
   private static final int INVALID_POINTER = -1;
   static final float MAX_SCROLL_FACTOR = 0.5F;
   private static final int[] SCROLLVIEW_STYLEABLE = new int[]{16843130};
   private static final String TAG = "NestedScrollView";
   private int mActivePointerId;
   private final NestedScrollingChildHelper mChildHelper;
   private View mChildToScrollTo;
   private EdgeEffect mEdgeGlowBottom;
   private EdgeEffect mEdgeGlowTop;
   private boolean mFillViewport;
   private boolean mIsBeingDragged;
   private boolean mIsLaidOut;
   private boolean mIsLayoutDirty;
   private int mLastMotionY;
   private long mLastScroll;
   private int mLastScrollerY;
   private int mMaximumVelocity;
   private int mMinimumVelocity;
   private int mNestedYOffset;
   private NestedScrollView.OnScrollChangeListener mOnScrollChangeListener;
   private final NestedScrollingParentHelper mParentHelper;
   private NestedScrollView.SavedState mSavedState;
   private final int[] mScrollConsumed;
   private final int[] mScrollOffset;
   private OverScroller mScroller;
   private boolean mSmoothScrollingEnabled;
   private final Rect mTempRect;
   private int mTouchSlop;
   private VelocityTracker mVelocityTracker;
   private float mVerticalScrollFactor;

   public NestedScrollView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NestedScrollView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NestedScrollView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mTempRect = new Rect();
      this.mIsLayoutDirty = true;
      this.mIsLaidOut = false;
      this.mChildToScrollTo = null;
      this.mIsBeingDragged = false;
      this.mSmoothScrollingEnabled = true;
      this.mActivePointerId = -1;
      this.mScrollOffset = new int[2];
      this.mScrollConsumed = new int[2];
      this.initScrollView();
      TypedArray var4 = var1.obtainStyledAttributes(var2, SCROLLVIEW_STYLEABLE, var3, 0);
      this.setFillViewport(var4.getBoolean(0, false));
      var4.recycle();
      this.mParentHelper = new NestedScrollingParentHelper(this);
      this.mChildHelper = new NestedScrollingChildHelper(this);
      this.setNestedScrollingEnabled(true);
      ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
   }

   private boolean canScroll() {
      boolean var2 = false;
      View var3 = this.getChildAt(0);
      if (var3 != null) {
         int var1 = var3.getHeight();
         if (this.getHeight() < this.getPaddingTop() + var1 + this.getPaddingBottom()) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   private static int clamp(int var0, int var1, int var2) {
      if (var1 < var2 && var0 >= 0) {
         return var1 + var0 > var2 ? var2 - var1 : var0;
      } else {
         return 0;
      }
   }

   private void doScrollY(int var1) {
      if (var1 != 0) {
         if (this.mSmoothScrollingEnabled) {
            this.smoothScrollBy(0, var1);
            return;
         }

         this.scrollBy(0, var1);
      }

   }

   private void endDrag() {
      this.mIsBeingDragged = false;
      this.recycleVelocityTracker();
      this.stopNestedScroll(0);
      EdgeEffect var1 = this.mEdgeGlowTop;
      if (var1 != null) {
         var1.onRelease();
         this.mEdgeGlowBottom.onRelease();
      }

   }

   private void ensureGlows() {
      if (this.getOverScrollMode() != 2) {
         if (this.mEdgeGlowTop == null) {
            Context var1 = this.getContext();
            this.mEdgeGlowTop = new EdgeEffect(var1);
            this.mEdgeGlowBottom = new EdgeEffect(var1);
            return;
         }
      } else {
         this.mEdgeGlowTop = null;
         this.mEdgeGlowBottom = null;
      }

   }

   private View findFocusableViewInBounds(boolean var1, int var2, int var3) {
      ArrayList var15 = this.getFocusables(2);
      View var14 = null;
      boolean var7 = false;
      int var9 = var15.size();

      boolean var5;
      for(int var6 = 0; var6 < var9; var7 = var5) {
         View var13 = (View)var15.get(var6);
         int var10 = var13.getTop();
         int var11 = var13.getBottom();
         View var12 = var14;
         var5 = var7;
         if (var2 < var11) {
            var12 = var14;
            var5 = var7;
            if (var10 < var3) {
               boolean var8 = false;
               boolean var4;
               if (var2 < var10 && var11 < var3) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var14 == null) {
                  var12 = var13;
                  var5 = var4;
               } else {
                  if (var1 && var10 < var14.getTop() || !var1 && var11 > var14.getBottom()) {
                     var8 = true;
                  }

                  if (var7) {
                     var12 = var14;
                     var5 = var7;
                     if (var4) {
                        var12 = var14;
                        var5 = var7;
                        if (var8) {
                           var12 = var13;
                           var5 = var7;
                        }
                     }
                  } else if (var4) {
                     var12 = var13;
                     var5 = true;
                  } else {
                     var12 = var14;
                     var5 = var7;
                     if (var8) {
                        var12 = var13;
                        var5 = var7;
                     }
                  }
               }
            }
         }

         ++var6;
         var14 = var12;
      }

      return var14;
   }

   private void flingWithNestedDispatch(int var1) {
      int var2 = this.getScrollY();
      boolean var3;
      if ((var2 > 0 || var1 > 0) && (var2 < this.getScrollRange() || var1 < 0)) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (!this.dispatchNestedPreFling(0.0F, (float)var1)) {
         this.dispatchNestedFling(0.0F, (float)var1, var3);
         this.fling(var1);
      }

   }

   private float getVerticalScrollFactorCompat() {
      if (this.mVerticalScrollFactor == 0.0F) {
         TypedValue var1 = new TypedValue();
         Context var2 = this.getContext();
         if (!var2.getTheme().resolveAttribute(16842829, var1, true)) {
            throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
         }

         this.mVerticalScrollFactor = var1.getDimension(var2.getResources().getDisplayMetrics());
      }

      return this.mVerticalScrollFactor;
   }

   private boolean inChild(int var1, int var2) {
      if (this.getChildCount() > 0) {
         int var3 = this.getScrollY();
         View var4 = this.getChildAt(0);
         return var2 >= var4.getTop() - var3 && var2 < var4.getBottom() - var3 && var1 >= var4.getLeft() && var1 < var4.getRight();
      } else {
         return false;
      }
   }

   private void initOrResetVelocityTracker() {
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      } else {
         var1.clear();
      }
   }

   private void initScrollView() {
      this.mScroller = new OverScroller(this.getContext());
      this.setFocusable(true);
      this.setDescendantFocusability(262144);
      this.setWillNotDraw(false);
      ViewConfiguration var1 = ViewConfiguration.get(this.getContext());
      this.mTouchSlop = var1.getScaledTouchSlop();
      this.mMinimumVelocity = var1.getScaledMinimumFlingVelocity();
      this.mMaximumVelocity = var1.getScaledMaximumFlingVelocity();
   }

   private void initVelocityTrackerIfNotExists() {
      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

   }

   private boolean isOffScreen(View var1) {
      return this.isWithinDeltaOfScreen(var1, 0, this.getHeight()) ^ true;
   }

   private static boolean isViewDescendantOf(View var0, View var1) {
      if (var0 == var1) {
         return true;
      } else {
         ViewParent var2 = var0.getParent();
         return var2 instanceof ViewGroup && isViewDescendantOf((View)var2, var1);
      }
   }

   private boolean isWithinDeltaOfScreen(View var1, int var2, int var3) {
      var1.getDrawingRect(this.mTempRect);
      this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
      return this.mTempRect.bottom + var2 >= this.getScrollY() && this.mTempRect.top - var2 <= this.getScrollY() + var3;
   }

   private void onSecondaryPointerUp(MotionEvent var1) {
      int var2 = var1.getActionIndex();
      if (var1.getPointerId(var2) == this.mActivePointerId) {
         byte var4;
         if (var2 == 0) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         this.mLastMotionY = (int)var1.getY(var4);
         this.mActivePointerId = var1.getPointerId(var4);
         VelocityTracker var3 = this.mVelocityTracker;
         if (var3 != null) {
            var3.clear();
         }
      }

   }

   private void recycleVelocityTracker() {
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 != null) {
         var1.recycle();
         this.mVelocityTracker = null;
      }

   }

   private boolean scrollAndFocus(int var1, int var2, int var3) {
      boolean var7 = true;
      int var5 = this.getHeight();
      int var4 = this.getScrollY();
      var5 += var4;
      boolean var6;
      if (var1 == 33) {
         var6 = true;
      } else {
         var6 = false;
      }

      View var9 = this.findFocusableViewInBounds(var6, var2, var3);
      Object var8 = var9;
      if (var9 == null) {
         var8 = this;
      }

      if (var2 >= var4 && var3 <= var5) {
         var6 = false;
      } else {
         if (var6) {
            var2 -= var4;
         } else {
            var2 = var3 - var5;
         }

         this.doScrollY(var2);
         var6 = var7;
      }

      if (var8 != this.findFocus()) {
         ((View)var8).requestFocus(var1);
      }

      return var6;
   }

   private void scrollToChild(View var1) {
      var1.getDrawingRect(this.mTempRect);
      this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
      int var2 = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
      if (var2 != 0) {
         this.scrollBy(0, var2);
      }

   }

   private boolean scrollToChildRect(Rect var1, boolean var2) {
      int var3 = this.computeScrollDeltaToGetChildRectOnScreen(var1);
      boolean var4;
      if (var3 != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var4) {
         if (var2) {
            this.scrollBy(0, var3);
            return var4;
         }

         this.smoothScrollBy(0, var3);
      }

      return var4;
   }

   public void addView(View var1) {
      if (this.getChildCount() <= 0) {
         super.addView(var1);
      } else {
         throw new IllegalStateException("ScrollView can host only one direct child");
      }
   }

   public void addView(View var1, int var2) {
      if (this.getChildCount() <= 0) {
         super.addView(var1, var2);
      } else {
         throw new IllegalStateException("ScrollView can host only one direct child");
      }
   }

   public void addView(View var1, int var2, LayoutParams var3) {
      if (this.getChildCount() <= 0) {
         super.addView(var1, var2, var3);
      } else {
         throw new IllegalStateException("ScrollView can host only one direct child");
      }
   }

   public void addView(View var1, LayoutParams var2) {
      if (this.getChildCount() <= 0) {
         super.addView(var1, var2);
      } else {
         throw new IllegalStateException("ScrollView can host only one direct child");
      }
   }

   public boolean arrowScroll(int var1) {
      View var8 = this.findFocus();
      View var7 = var8;
      if (var8 == this) {
         var7 = null;
      }

      var8 = FocusFinder.getInstance().findNextFocus(this, var7, var1);
      int var4 = this.getMaxScrollAmount();
      if (var8 != null && this.isWithinDeltaOfScreen(var8, var4, this.getHeight())) {
         var8.getDrawingRect(this.mTempRect);
         this.offsetDescendantRectToMyCoords(var8, this.mTempRect);
         this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
         var8.requestFocus(var1);
      } else {
         int var2;
         if (var1 == 33 && this.getScrollY() < var4) {
            var2 = this.getScrollY();
         } else {
            var2 = var4;
            if (var1 == 130) {
               var2 = var4;
               if (this.getChildCount() > 0) {
                  int var5 = this.getChildAt(0).getBottom();
                  int var6 = this.getScrollY() + this.getHeight() - this.getPaddingBottom();
                  var2 = var4;
                  if (var5 - var6 < var4) {
                     var2 = var5 - var6;
                  }
               }
            }
         }

         if (var2 == 0) {
            return false;
         }

         if (var1 != 130) {
            var2 = -var2;
         }

         this.doScrollY(var2);
      }

      if (var7 != null && var7.isFocused() && this.isOffScreen(var7)) {
         var1 = this.getDescendantFocusability();
         this.setDescendantFocusability(131072);
         this.requestFocus();
         this.setDescendantFocusability(var1);
      }

      return true;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollExtent() {
      return super.computeHorizontalScrollExtent();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollOffset() {
      return super.computeHorizontalScrollOffset();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollRange() {
      return super.computeHorizontalScrollRange();
   }

   public void computeScroll() {
      if (this.mScroller.computeScrollOffset()) {
         this.mScroller.getCurrX();
         int var3 = this.mScroller.getCurrY();
         int var1 = var3 - this.mLastScrollerY;
         if (this.dispatchNestedPreScroll(0, var1, this.mScrollConsumed, (int[])null, 1)) {
            var1 -= this.mScrollConsumed[1];
         }

         if (var1 != 0) {
            int var2 = this.getScrollRange();
            int var4 = this.getScrollY();
            this.overScrollByCompat(0, var1, this.getScrollX(), var4, 0, var2, 0, 0, false);
            int var5 = this.getScrollY() - var4;
            if (!this.dispatchNestedScroll(0, var5, 0, var1 - var5, (int[])null, 1)) {
               var1 = this.getOverScrollMode();
               boolean var6;
               if (var1 == 0 || var1 == 1 && var2 > 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               if (var6) {
                  this.ensureGlows();
                  if (var3 <= 0 && var4 > 0) {
                     this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
                  } else if (var3 >= var2 && var4 < var2) {
                     this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
                  }
               }
            }
         }

         this.mLastScrollerY = var3;
         ViewCompat.postInvalidateOnAnimation(this);
      } else {
         if (this.hasNestedScrollingParent(1)) {
            this.stopNestedScroll(1);
         }

         this.mLastScrollerY = 0;
      }
   }

   protected int computeScrollDeltaToGetChildRectOnScreen(Rect var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         int var6 = this.getHeight();
         int var2 = this.getScrollY();
         int var4 = var2 + var6;
         int var5 = this.getVerticalFadingEdgeLength();
         int var3 = var2;
         if (var1.top > 0) {
            var3 = var2 + var5;
         }

         var2 = var4;
         if (var1.bottom < this.getChildAt(0).getHeight()) {
            var2 = var4 - var5;
         }

         byte var7 = 0;
         if (var1.bottom > var2 && var1.top > var3) {
            if (var1.height() > var6) {
               var3 = 0 + (var1.top - var3);
            } else {
               var3 = 0 + (var1.bottom - var2);
            }

            var4 = Math.min(var3, this.getChildAt(0).getBottom() - var2);
         } else {
            var4 = var7;
            if (var1.top < var3) {
               var4 = var7;
               if (var1.bottom < var2) {
                  if (var1.height() > var6) {
                     var2 = 0 - (var2 - var1.bottom);
                  } else {
                     var2 = 0 - (var3 - var1.top);
                  }

                  return Math.max(var2, -this.getScrollY());
               }
            }
         }

         return var4;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollExtent() {
      return super.computeVerticalScrollExtent();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollOffset() {
      return Math.max(0, super.computeVerticalScrollOffset());
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollRange() {
      int var2 = this.getChildCount();
      int var1 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
      if (var2 == 0) {
         return var1;
      } else {
         var2 = this.getChildAt(0).getBottom();
         int var3 = this.getScrollY();
         int var4 = Math.max(0, var2 - var1);
         if (var3 < 0) {
            return var2 - var3;
         } else {
            var1 = var2;
            if (var3 > var4) {
               var1 = var2 + (var3 - var4);
            }

            return var1;
         }
      }
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      return super.dispatchKeyEvent(var1) || this.executeKeyEvent(var1);
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.mChildHelper.dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.mChildHelper.dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.mChildHelper.dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5) {
      return this.mChildHelper.dispatchNestedPreScroll(var1, var2, var3, var4, var5);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.mChildHelper.dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6) {
      return this.mChildHelper.dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.mEdgeGlowTop != null) {
         int var2 = this.getScrollY();
         int var3;
         int var4;
         int var5;
         if (!this.mEdgeGlowTop.isFinished()) {
            var3 = var1.save();
            var4 = this.getWidth();
            var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            var1.translate((float)this.getPaddingLeft(), (float)Math.min(0, var2));
            this.mEdgeGlowTop.setSize(var4 - var5 - var6, this.getHeight());
            if (this.mEdgeGlowTop.draw(var1)) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            var1.restoreToCount(var3);
         }

         if (!this.mEdgeGlowBottom.isFinished()) {
            var3 = var1.save();
            var4 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            var5 = this.getHeight();
            var1.translate((float)(-var4 + this.getPaddingLeft()), (float)(Math.max(this.getScrollRange(), var2) + var5));
            var1.rotate(180.0F, (float)var4, 0.0F);
            this.mEdgeGlowBottom.setSize(var4, var5);
            if (this.mEdgeGlowBottom.draw(var1)) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            var1.restoreToCount(var3);
         }
      }

   }

   public boolean executeKeyEvent(KeyEvent var1) {
      this.mTempRect.setEmpty();
      boolean var4 = this.canScroll();
      short var2 = 130;
      if (!var4) {
         if (this.isFocused() && var1.getKeyCode() != 4) {
            View var5 = this.findFocus();
            View var6 = var5;
            if (var5 == this) {
               var6 = null;
            }

            var6 = FocusFinder.getInstance().findNextFocus(this, var6, 130);
            return var6 != null && var6 != this && var6.requestFocus(130);
         } else {
            return false;
         }
      } else {
         var4 = false;
         if (var1.getAction() == 0) {
            int var3 = var1.getKeyCode();
            if (var3 != 19) {
               if (var3 != 20) {
                  if (var3 != 62) {
                     return false;
                  }

                  if (var1.isShiftPressed()) {
                     var2 = 33;
                  }

                  this.pageScroll(var2);
                  return false;
               }

               if (!var1.isAltPressed()) {
                  return this.arrowScroll(130);
               }

               return this.fullScroll(130);
            }

            if (!var1.isAltPressed()) {
               return this.arrowScroll(33);
            }

            var4 = this.fullScroll(33);
         }

         return var4;
      }
   }

   public void fling(int var1) {
      if (this.getChildCount() > 0) {
         this.startNestedScroll(2, 1);
         this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, var1, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
         this.mLastScrollerY = this.getScrollY();
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public boolean fullScroll(int var1) {
      boolean var2;
      if (var1 == 130) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = this.getHeight();
      Rect var4 = this.mTempRect;
      var4.top = 0;
      var4.bottom = var3;
      if (var2) {
         int var5 = this.getChildCount();
         if (var5 > 0) {
            View var6 = this.getChildAt(var5 - 1);
            this.mTempRect.bottom = var6.getBottom() + this.getPaddingBottom();
            var4 = this.mTempRect;
            var4.top = var4.bottom - var3;
         }
      }

      return this.scrollAndFocus(var1, this.mTempRect.top, this.mTempRect.bottom);
   }

   protected float getBottomFadingEdgeStrength() {
      if (this.getChildCount() == 0) {
         return 0.0F;
      } else {
         int var1 = this.getVerticalFadingEdgeLength();
         int var2 = this.getHeight();
         int var3 = this.getPaddingBottom();
         var2 = this.getChildAt(0).getBottom() - this.getScrollY() - (var2 - var3);
         return var2 < var1 ? (float)var2 / (float)var1 : 1.0F;
      }
   }

   public int getMaxScrollAmount() {
      return (int)((float)this.getHeight() * 0.5F);
   }

   public int getNestedScrollAxes() {
      return this.mParentHelper.getNestedScrollAxes();
   }

   int getScrollRange() {
      int var1 = 0;
      if (this.getChildCount() > 0) {
         var1 = Math.max(0, this.getChildAt(0).getHeight() - (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()));
      }

      return var1;
   }

   protected float getTopFadingEdgeStrength() {
      if (this.getChildCount() == 0) {
         return 0.0F;
      } else {
         int var1 = this.getVerticalFadingEdgeLength();
         int var2 = this.getScrollY();
         return var2 < var1 ? (float)var2 / (float)var1 : 1.0F;
      }
   }

   public boolean hasNestedScrollingParent() {
      return this.mChildHelper.hasNestedScrollingParent();
   }

   public boolean hasNestedScrollingParent(int var1) {
      return this.mChildHelper.hasNestedScrollingParent(var1);
   }

   public boolean isFillViewport() {
      return this.mFillViewport;
   }

   public boolean isNestedScrollingEnabled() {
      return this.mChildHelper.isNestedScrollingEnabled();
   }

   public boolean isSmoothScrollingEnabled() {
      return this.mSmoothScrollingEnabled;
   }

   protected void measureChild(View var1, int var2, int var3) {
      LayoutParams var4 = var1.getLayoutParams();
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight(), var4.width), MeasureSpec.makeMeasureSpec(0, 0));
   }

   protected void measureChildWithMargins(View var1, int var2, int var3, int var4, int var5) {
      MarginLayoutParams var6 = (MarginLayoutParams)var1.getLayoutParams();
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var6.leftMargin + var6.rightMargin + var3, var6.width), MeasureSpec.makeMeasureSpec(var6.topMargin + var6.bottomMargin, 0));
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mIsLaidOut = false;
   }

   public boolean onGenericMotionEvent(MotionEvent var1) {
      if ((var1.getSource() & 2) != 0 && var1.getAction() == 8 && !this.mIsBeingDragged) {
         float var2 = var1.getAxisValue(9);
         if (var2 != 0.0F) {
            int var3 = (int)(this.getVerticalScrollFactorCompat() * var2);
            int var4 = this.getScrollRange();
            int var6 = this.getScrollY();
            int var5 = var6 - var3;
            if (var5 < 0) {
               var3 = 0;
            } else {
               var3 = var5;
               if (var5 > var4) {
                  var3 = var4;
               }
            }

            if (var3 != var6) {
               super.scrollTo(this.getScrollX(), var3);
               return true;
            }
         }
      }

      return false;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var2 = var1.getAction();
      if (var2 == 2 && this.mIsBeingDragged) {
         return true;
      } else {
         var2 &= 255;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  var2 = this.mActivePointerId;
                  if (var2 != -1) {
                     int var3 = var1.findPointerIndex(var2);
                     if (var3 == -1) {
                        StringBuilder var4 = new StringBuilder();
                        var4.append("Invalid pointerId=");
                        var4.append(var2);
                        var4.append(" in onInterceptTouchEvent");
                        Log.e("NestedScrollView", var4.toString());
                     } else {
                        var2 = (int)var1.getY(var3);
                        if (Math.abs(var2 - this.mLastMotionY) > this.mTouchSlop && (2 & this.getNestedScrollAxes()) == 0) {
                           this.mIsBeingDragged = true;
                           this.mLastMotionY = var2;
                           this.initVelocityTrackerIfNotExists();
                           this.mVelocityTracker.addMovement(var1);
                           this.mNestedYOffset = 0;
                           ViewParent var5 = this.getParent();
                           if (var5 != null) {
                              var5.requestDisallowInterceptTouchEvent(true);
                           }

                           return this.mIsBeingDragged;
                        }
                     }

                     return this.mIsBeingDragged;
                  }

                  return this.mIsBeingDragged;
               }

               if (var2 != 3) {
                  if (var2 == 6) {
                     this.onSecondaryPointerUp(var1);
                  }

                  return this.mIsBeingDragged;
               }
            }

            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            this.recycleVelocityTracker();
            if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            this.stopNestedScroll(0);
         } else {
            var2 = (int)var1.getY();
            if (!this.inChild((int)var1.getX(), var2)) {
               this.mIsBeingDragged = false;
               this.recycleVelocityTracker();
            } else {
               this.mLastMotionY = var2;
               this.mActivePointerId = var1.getPointerId(0);
               this.initOrResetVelocityTracker();
               this.mVelocityTracker.addMovement(var1);
               this.mScroller.computeScrollOffset();
               this.mIsBeingDragged = true ^ this.mScroller.isFinished();
               this.startNestedScroll(2, 0);
            }
         }

         return this.mIsBeingDragged;
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.mIsLayoutDirty = false;
      View var6 = this.mChildToScrollTo;
      if (var6 != null && isViewDescendantOf(var6, this)) {
         this.scrollToChild(this.mChildToScrollTo);
      }

      this.mChildToScrollTo = null;
      if (!this.mIsLaidOut) {
         if (this.mSavedState != null) {
            this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
            this.mSavedState = null;
         }

         if (this.getChildCount() > 0) {
            var2 = this.getChildAt(0).getMeasuredHeight();
         } else {
            var2 = 0;
         }

         var2 = Math.max(0, var2 - (var5 - var3 - this.getPaddingBottom() - this.getPaddingTop()));
         if (this.getScrollY() > var2) {
            this.scrollTo(this.getScrollX(), var2);
         } else if (this.getScrollY() < 0) {
            this.scrollTo(this.getScrollX(), 0);
         }
      }

      this.scrollTo(this.getScrollX(), this.getScrollY());
      this.mIsLaidOut = true;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.mFillViewport) {
         if (MeasureSpec.getMode(var2) != 0) {
            if (this.getChildCount() > 0) {
               View var3 = this.getChildAt(0);
               var2 = this.getMeasuredHeight();
               if (var3.getMeasuredHeight() < var2) {
                  android.widget.FrameLayout.LayoutParams var4 = (android.widget.FrameLayout.LayoutParams)var3.getLayoutParams();
                  var3.measure(getChildMeasureSpec(var1, this.getPaddingLeft() + this.getPaddingRight(), var4.width), MeasureSpec.makeMeasureSpec(var2 - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
               }
            }

         }
      }
   }

   public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
      if (!var4) {
         this.flingWithNestedDispatch((int)var3);
         return true;
      } else {
         return false;
      }
   }

   public boolean onNestedPreFling(View var1, float var2, float var3) {
      return this.dispatchNestedPreFling(var2, var3);
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
      this.dispatchNestedPreScroll(var2, var3, var4, (int[])null);
   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
      var2 = this.getScrollY();
      this.scrollBy(0, var5);
      var2 = this.getScrollY() - var2;
      this.dispatchNestedScroll(0, var2, 0, var5 - var2, (int[])null);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.mParentHelper.onNestedScrollAccepted(var1, var2, var3);
      this.startNestedScroll(2);
   }

   protected void onOverScrolled(int var1, int var2, boolean var3, boolean var4) {
      super.scrollTo(var1, var2);
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      int var3;
      if (var1 == 2) {
         var3 = 130;
      } else {
         var3 = var1;
         if (var1 == 1) {
            var3 = 33;
         }
      }

      View var4;
      if (var2 == null) {
         var4 = FocusFinder.getInstance().findNextFocus(this, (View)null, var3);
      } else {
         var4 = FocusFinder.getInstance().findNextFocusFromRect(this, var2, var3);
      }

      if (var4 == null) {
         return false;
      } else {
         return this.isOffScreen(var4) ? false : var4.requestFocus(var3, var2);
      }
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof NestedScrollView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NestedScrollView.SavedState var2 = (NestedScrollView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mSavedState = var2;
         this.requestLayout();
      }
   }

   protected Parcelable onSaveInstanceState() {
      NestedScrollView.SavedState var1 = new NestedScrollView.SavedState(super.onSaveInstanceState());
      var1.scrollPosition = this.getScrollY();
      return var1;
   }

   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      super.onScrollChanged(var1, var2, var3, var4);
      NestedScrollView.OnScrollChangeListener var5 = this.mOnScrollChangeListener;
      if (var5 != null) {
         var5.onScrollChange(this, var1, var2, var3, var4);
      }

   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      View var5 = this.findFocus();
      if (var5 != null) {
         if (this != var5) {
            if (this.isWithinDeltaOfScreen(var5, 0, var4)) {
               var5.getDrawingRect(this.mTempRect);
               this.offsetDescendantRectToMyCoords(var5, this.mTempRect);
               this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            }

         }
      }
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3) {
      return (var3 & 2) != 0;
   }

   public void onStopNestedScroll(View var1) {
      this.mParentHelper.onStopNestedScroll(var1);
      this.stopNestedScroll();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.initVelocityTrackerIfNotExists();
      MotionEvent var9 = MotionEvent.obtain(var1);
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.mNestedYOffset = 0;
      }

      var9.offsetLocation(0.0F, (float)this.mNestedYOffset);
      ViewParent var10;
      VelocityTracker var14;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               if (var2 != 3) {
                  if (var2 != 5) {
                     if (var2 == 6) {
                        this.onSecondaryPointerUp(var1);
                        this.mLastMotionY = (int)var1.getY(var1.findPointerIndex(this.mActivePointerId));
                     }
                  } else {
                     var2 = var1.getActionIndex();
                     this.mLastMotionY = (int)var1.getY(var2);
                     this.mActivePointerId = var1.getPointerId(var2);
                  }
               } else {
                  if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                     ViewCompat.postInvalidateOnAnimation(this);
                  }

                  this.mActivePointerId = -1;
                  this.endDrag();
               }
            } else {
               int var4 = var1.findPointerIndex(this.mActivePointerId);
               if (var4 == -1) {
                  StringBuilder var11 = new StringBuilder();
                  var11.append("Invalid pointerId=");
                  var11.append(this.mActivePointerId);
                  var11.append(" in onTouchEvent");
                  Log.e("NestedScrollView", var11.toString());
               } else {
                  int var5 = (int)var1.getY(var4);
                  var2 = this.mLastMotionY - var5;
                  int var3 = var2;
                  if (this.dispatchNestedPreScroll(0, var2, this.mScrollConsumed, this.mScrollOffset, 0)) {
                     var3 = var2 - this.mScrollConsumed[1];
                     var9.offsetLocation(0.0F, (float)this.mScrollOffset[1]);
                     this.mNestedYOffset += this.mScrollOffset[1];
                  }

                  var2 = var3;
                  if (!this.mIsBeingDragged) {
                     var2 = var3;
                     if (Math.abs(var3) > this.mTouchSlop) {
                        var10 = this.getParent();
                        if (var10 != null) {
                           var10.requestDisallowInterceptTouchEvent(true);
                        }

                        this.mIsBeingDragged = true;
                        if (var3 > 0) {
                           var2 = var3 - this.mTouchSlop;
                        } else {
                           var2 = var3 + this.mTouchSlop;
                        }
                     }
                  }

                  if (this.mIsBeingDragged) {
                     this.mLastMotionY = var5 - this.mScrollOffset[1];
                     int var6 = this.getScrollY();
                     var5 = this.getScrollRange();
                     var3 = this.getOverScrollMode();
                     boolean var15;
                     if (var3 == 0 || var3 == 1 && var5 > 0) {
                        var15 = true;
                     } else {
                        var15 = false;
                     }

                     if (this.overScrollByCompat(0, var2, 0, this.getScrollY(), 0, var5, 0, 0, true) && !this.hasNestedScrollingParent(0)) {
                        this.mVelocityTracker.clear();
                     }

                     int var7 = this.getScrollY() - var6;
                     if (this.dispatchNestedScroll(0, var7, 0, var2 - var7, this.mScrollOffset, 0)) {
                        var2 = this.mLastMotionY;
                        int[] var12 = this.mScrollOffset;
                        this.mLastMotionY = var2 - var12[1];
                        var9.offsetLocation(0.0F, (float)var12[1]);
                        this.mNestedYOffset += this.mScrollOffset[1];
                     } else if (var15) {
                        this.ensureGlows();
                        var3 = var6 + var2;
                        if (var3 < 0) {
                           EdgeEffectCompat.onPull(this.mEdgeGlowTop, (float)var2 / (float)this.getHeight(), var1.getX(var4) / (float)this.getWidth());
                           if (!this.mEdgeGlowBottom.isFinished()) {
                              this.mEdgeGlowBottom.onRelease();
                           }
                        } else if (var3 > var5) {
                           EdgeEffectCompat.onPull(this.mEdgeGlowBottom, (float)var2 / (float)this.getHeight(), 1.0F - var1.getX(var4) / (float)this.getWidth());
                           if (!this.mEdgeGlowTop.isFinished()) {
                              this.mEdgeGlowTop.onRelease();
                           }
                        }

                        EdgeEffect var13 = this.mEdgeGlowTop;
                        if (var13 != null && (!var13.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                           ViewCompat.postInvalidateOnAnimation(this);
                        }
                     }
                  }
               }
            }
         } else {
            var14 = this.mVelocityTracker;
            var14.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            var2 = (int)var14.getYVelocity(this.mActivePointerId);
            if (Math.abs(var2) > this.mMinimumVelocity) {
               this.flingWithNestedDispatch(-var2);
            } else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            this.mActivePointerId = -1;
            this.endDrag();
         }
      } else {
         if (this.getChildCount() == 0) {
            return false;
         }

         boolean var8 = this.mScroller.isFinished() ^ true;
         this.mIsBeingDragged = var8;
         if (var8) {
            var10 = this.getParent();
            if (var10 != null) {
               var10.requestDisallowInterceptTouchEvent(true);
            }
         }

         if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
         }

         this.mLastMotionY = (int)var1.getY();
         this.mActivePointerId = var1.getPointerId(0);
         this.startNestedScroll(2, 0);
      }

      var14 = this.mVelocityTracker;
      if (var14 != null) {
         var14.addMovement(var9);
      }

      var9.recycle();
      return true;
   }

   boolean overScrollByCompat(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      int var12 = this.getOverScrollMode();
      boolean var10;
      if (this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent()) {
         var10 = true;
      } else {
         var10 = false;
      }

      boolean var11;
      if (this.computeVerticalScrollRange() > this.computeVerticalScrollExtent()) {
         var11 = true;
      } else {
         var11 = false;
      }

      if (var12 == 0 || var12 == 1 && var10) {
         var10 = true;
      } else {
         var10 = false;
      }

      if (var12 == 0 || var12 == 1 && var11) {
         var11 = true;
      } else {
         var11 = false;
      }

      var3 += var1;
      if (!var10) {
         var1 = 0;
      } else {
         var1 = var7;
      }

      var4 += var2;
      if (!var11) {
         var2 = 0;
      } else {
         var2 = var8;
      }

      var7 = -var1;
      var1 += var5;
      var5 = -var2;
      var2 += var6;
      if (var3 > var1) {
         var9 = true;
      } else if (var3 < var7) {
         var1 = var7;
         var9 = true;
      } else {
         var9 = false;
         var1 = var3;
      }

      boolean var13;
      if (var4 > var2) {
         var13 = true;
      } else if (var4 < var5) {
         var2 = var5;
         var13 = true;
      } else {
         var2 = var4;
         var13 = false;
      }

      if (var13 && !this.hasNestedScrollingParent(1)) {
         this.mScroller.springBack(var1, var2, 0, 0, 0, this.getScrollRange());
      }

      this.onOverScrolled(var1, var2, var9, var13);
      return var9 || var13;
   }

   public boolean pageScroll(int var1) {
      boolean var2;
      if (var1 == 130) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = this.getHeight();
      if (var2) {
         this.mTempRect.top = this.getScrollY() + var3;
         int var5 = this.getChildCount();
         if (var5 > 0) {
            View var4 = this.getChildAt(var5 - 1);
            if (this.mTempRect.top + var3 > var4.getBottom()) {
               this.mTempRect.top = var4.getBottom() - var3;
            }
         }
      } else {
         this.mTempRect.top = this.getScrollY() - var3;
         if (this.mTempRect.top < 0) {
            this.mTempRect.top = 0;
         }
      }

      Rect var6 = this.mTempRect;
      var6.bottom = var6.top + var3;
      return this.scrollAndFocus(var1, this.mTempRect.top, this.mTempRect.bottom);
   }

   public void requestChildFocus(View var1, View var2) {
      if (!this.mIsLayoutDirty) {
         this.scrollToChild(var2);
      } else {
         this.mChildToScrollTo = var2;
      }

      super.requestChildFocus(var1, var2);
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      var2.offset(var1.getLeft() - var1.getScrollX(), var1.getTop() - var1.getScrollY());
      return this.scrollToChildRect(var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      if (var1) {
         this.recycleVelocityTracker();
      }

      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void requestLayout() {
      this.mIsLayoutDirty = true;
      super.requestLayout();
   }

   public void scrollTo(int var1, int var2) {
      if (this.getChildCount() > 0) {
         View var3 = this.getChildAt(0);
         var1 = clamp(var1, this.getWidth() - this.getPaddingRight() - this.getPaddingLeft(), var3.getWidth());
         var2 = clamp(var2, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop(), var3.getHeight());
         if (var1 != this.getScrollX() || var2 != this.getScrollY()) {
            super.scrollTo(var1, var2);
         }
      }

   }

   public void setFillViewport(boolean var1) {
      if (var1 != this.mFillViewport) {
         this.mFillViewport = var1;
         this.requestLayout();
      }

   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.mChildHelper.setNestedScrollingEnabled(var1);
   }

   public void setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener var1) {
      this.mOnScrollChangeListener = var1;
   }

   public void setSmoothScrollingEnabled(boolean var1) {
      this.mSmoothScrollingEnabled = var1;
   }

   public boolean shouldDelayChildPressedState() {
      return true;
   }

   public final void smoothScrollBy(int var1, int var2) {
      if (this.getChildCount() != 0) {
         if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
            var1 = this.getHeight();
            int var3 = this.getPaddingBottom();
            int var4 = this.getPaddingTop();
            var3 = Math.max(0, this.getChildAt(0).getHeight() - (var1 - var3 - var4));
            var1 = this.getScrollY();
            var2 = Math.max(0, Math.min(var1 + var2, var3));
            this.mScroller.startScroll(this.getScrollX(), var1, 0, var2 - var1);
            ViewCompat.postInvalidateOnAnimation(this);
         } else {
            if (!this.mScroller.isFinished()) {
               this.mScroller.abortAnimation();
            }

            this.scrollBy(var1, var2);
         }

         this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
      }
   }

   public final void smoothScrollTo(int var1, int var2) {
      this.smoothScrollBy(var1 - this.getScrollX(), var2 - this.getScrollY());
   }

   public boolean startNestedScroll(int var1) {
      return this.mChildHelper.startNestedScroll(var1);
   }

   public boolean startNestedScroll(int var1, int var2) {
      return this.mChildHelper.startNestedScroll(var1, var2);
   }

   public void stopNestedScroll() {
      this.mChildHelper.stopNestedScroll();
   }

   public void stopNestedScroll(int var1) {
      this.mChildHelper.stopNestedScroll(var1);
   }

   static class AccessibilityDelegate extends AccessibilityDelegateCompat {
      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         NestedScrollView var4 = (NestedScrollView)var1;
         var2.setClassName(ScrollView.class.getName());
         boolean var3;
         if (var4.getScrollRange() > 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var2.setScrollable(var3);
         var2.setScrollX(var4.getScrollX());
         var2.setScrollY(var4.getScrollY());
         AccessibilityRecordCompat.setMaxScrollX(var2, var4.getScrollX());
         AccessibilityRecordCompat.setMaxScrollY(var2, var4.getScrollRange());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         NestedScrollView var4 = (NestedScrollView)var1;
         var2.setClassName(ScrollView.class.getName());
         if (var4.isEnabled()) {
            int var3 = var4.getScrollRange();
            if (var3 > 0) {
               var2.setScrollable(true);
               if (var4.getScrollY() > 0) {
                  var2.addAction(8192);
               }

               if (var4.getScrollY() < var3) {
                  var2.addAction(4096);
               }
            }
         }

      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         if (super.performAccessibilityAction(var1, var2, var3)) {
            return true;
         } else {
            NestedScrollView var6 = (NestedScrollView)var1;
            if (!var6.isEnabled()) {
               return false;
            } else {
               int var4;
               int var5;
               if (var2 != 4096) {
                  if (var2 != 8192) {
                     return false;
                  } else {
                     var2 = var6.getHeight();
                     var4 = var6.getPaddingBottom();
                     var5 = var6.getPaddingTop();
                     var2 = Math.max(var6.getScrollY() - (var2 - var4 - var5), 0);
                     if (var2 != var6.getScrollY()) {
                        var6.smoothScrollTo(0, var2);
                        return true;
                     } else {
                        return false;
                     }
                  }
               } else {
                  var2 = var6.getHeight();
                  var4 = var6.getPaddingBottom();
                  var5 = var6.getPaddingTop();
                  var2 = Math.min(var6.getScrollY() + (var2 - var4 - var5), var6.getScrollRange());
                  if (var2 != var6.getScrollY()) {
                     var6.smoothScrollTo(0, var2);
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }
      }
   }

   public interface OnScrollChangeListener {
      void onScrollChange(NestedScrollView var1, int var2, int var3, int var4, int var5);
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public NestedScrollView.SavedState createFromParcel(Parcel var1) {
            return new NestedScrollView.SavedState(var1);
         }

         public NestedScrollView.SavedState[] newArray(int var1) {
            return new NestedScrollView.SavedState[var1];
         }
      };
      public int scrollPosition;

      SavedState(Parcel var1) {
         super(var1);
         this.scrollPosition = var1.readInt();
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("HorizontalScrollView.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" scrollPosition=");
         var1.append(this.scrollPosition);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.scrollPosition);
      }
   }
}
