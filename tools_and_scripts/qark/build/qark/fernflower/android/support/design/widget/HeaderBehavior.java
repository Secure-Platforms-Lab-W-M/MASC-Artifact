package android.support.design.widget;

import android.content.Context;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

abstract class HeaderBehavior extends ViewOffsetBehavior {
   private static final int INVALID_POINTER = -1;
   private int mActivePointerId = -1;
   private Runnable mFlingRunnable;
   private boolean mIsBeingDragged;
   private int mLastMotionY;
   OverScroller mScroller;
   private int mTouchSlop = -1;
   private VelocityTracker mVelocityTracker;

   public HeaderBehavior() {
   }

   public HeaderBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void ensureVelocityTracker() {
      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }
   }

   boolean canDragView(View var1) {
      return false;
   }

   final boolean fling(CoordinatorLayout var1, View var2, int var3, int var4, float var5) {
      Runnable var6 = this.mFlingRunnable;
      if (var6 != null) {
         var2.removeCallbacks(var6);
         this.mFlingRunnable = null;
      }

      if (this.mScroller == null) {
         this.mScroller = new OverScroller(var2.getContext());
      }

      this.mScroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(var5), 0, 0, var3, var4);
      if (this.mScroller.computeScrollOffset()) {
         this.mFlingRunnable = new HeaderBehavior.FlingRunnable(var1, var2);
         ViewCompat.postOnAnimation(var2, this.mFlingRunnable);
         return true;
      } else {
         this.onFlingFinished(var1, var2);
         return false;
      }
   }

   int getMaxDragOffset(View var1) {
      return -var1.getHeight();
   }

   int getScrollRangeForDragFling(View var1) {
      return var1.getHeight();
   }

   int getTopBottomOffsetForScrollingSibling() {
      return this.getTopAndBottomOffset();
   }

   void onFlingFinished(CoordinatorLayout var1, View var2) {
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (this.mTouchSlop < 0) {
         this.mTouchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      if (var3.getAction() == 2 && this.mIsBeingDragged) {
         return true;
      } else {
         int var4;
         VelocityTracker var6;
         switch(var3.getActionMasked()) {
         case 0:
            this.mIsBeingDragged = false;
            var4 = (int)var3.getX();
            int var5 = (int)var3.getY();
            if (this.canDragView(var2) && var1.isPointInChildBounds(var2, var4, var5)) {
               this.mLastMotionY = var5;
               this.mActivePointerId = var3.getPointerId(0);
               this.ensureVelocityTracker();
            }
            break;
         case 1:
         case 3:
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            var6 = this.mVelocityTracker;
            if (var6 != null) {
               var6.recycle();
               this.mVelocityTracker = null;
            }
            break;
         case 2:
            var4 = this.mActivePointerId;
            if (var4 != -1) {
               var4 = var3.findPointerIndex(var4);
               if (var4 != -1) {
                  var4 = (int)var3.getY(var4);
                  if (Math.abs(var4 - this.mLastMotionY) > this.mTouchSlop) {
                     this.mIsBeingDragged = true;
                     this.mLastMotionY = var4;
                  }
               }
            }
         }

         var6 = this.mVelocityTracker;
         if (var6 != null) {
            var6.addMovement(var3);
         }

         return this.mIsBeingDragged;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (this.mTouchSlop < 0) {
         this.mTouchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      int var5;
      int var6;
      VelocityTracker var10;
      switch(var3.getActionMasked()) {
      case 0:
         var5 = (int)var3.getX();
         var6 = (int)var3.getY();
         if (var1.isPointInChildBounds(var2, var5, var6) && this.canDragView(var2)) {
            this.mLastMotionY = var6;
            this.mActivePointerId = var3.getPointerId(0);
            this.ensureVelocityTracker();
            break;
         }

         return false;
      case 1:
         VelocityTracker var9 = this.mVelocityTracker;
         if (var9 != null) {
            var9.addMovement(var3);
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float var4 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            this.fling(var1, var2, -this.getScrollRangeForDragFling(var2), 0, var4);
         }
      case 3:
         this.mIsBeingDragged = false;
         this.mActivePointerId = -1;
         var10 = this.mVelocityTracker;
         if (var10 != null) {
            var10.recycle();
            this.mVelocityTracker = null;
         }
         break;
      case 2:
         var5 = var3.findPointerIndex(this.mActivePointerId);
         if (var5 == -1) {
            return false;
         }

         var6 = (int)var3.getY(var5);
         var5 = this.mLastMotionY - var6;
         if (!this.mIsBeingDragged) {
            int var7 = Math.abs(var5);
            int var8 = this.mTouchSlop;
            if (var7 > var8) {
               this.mIsBeingDragged = true;
               if (var5 > 0) {
                  var5 -= var8;
               } else {
                  var5 += var8;
               }
            }
         }

         if (this.mIsBeingDragged) {
            this.mLastMotionY = var6;
            this.scroll(var1, var2, var5, this.getMaxDragOffset(var2), 0);
         }
      }

      var10 = this.mVelocityTracker;
      if (var10 != null) {
         var10.addMovement(var3);
         return true;
      } else {
         return true;
      }
   }

   final int scroll(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      return this.setHeaderTopBottomOffset(var1, var2, this.getTopBottomOffsetForScrollingSibling() - var3, var4, var5);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3) {
      return this.setHeaderTopBottomOffset(var1, var2, var3, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      int var6 = this.getTopAndBottomOffset();
      if (var4 != 0 && var6 >= var4 && var6 <= var5) {
         var3 = MathUtils.clamp(var3, var4, var5);
         if (var6 != var3) {
            this.setTopAndBottomOffset(var3);
            return var6 - var3;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   private class FlingRunnable implements Runnable {
      private final View mLayout;
      private final CoordinatorLayout mParent;

      FlingRunnable(CoordinatorLayout var2, View var3) {
         this.mParent = var2;
         this.mLayout = var3;
      }

      public void run() {
         if (this.mLayout != null && HeaderBehavior.this.mScroller != null) {
            if (HeaderBehavior.this.mScroller.computeScrollOffset()) {
               HeaderBehavior var1 = HeaderBehavior.this;
               var1.setHeaderTopBottomOffset(this.mParent, this.mLayout, var1.mScroller.getCurrY());
               ViewCompat.postOnAnimation(this.mLayout, this);
            } else {
               HeaderBehavior.this.onFlingFinished(this.mParent, this.mLayout);
            }
         }
      }
   }
}
