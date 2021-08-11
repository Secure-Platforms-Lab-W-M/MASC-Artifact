package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

public class ViewDragHelper {
   private static final int BASE_SETTLE_DURATION = 256;
   public static final int DIRECTION_ALL = 3;
   public static final int DIRECTION_HORIZONTAL = 1;
   public static final int DIRECTION_VERTICAL = 2;
   public static final int EDGE_ALL = 15;
   public static final int EDGE_BOTTOM = 8;
   public static final int EDGE_LEFT = 1;
   public static final int EDGE_RIGHT = 2;
   private static final int EDGE_SIZE = 20;
   public static final int EDGE_TOP = 4;
   public static final int INVALID_POINTER = -1;
   private static final int MAX_SETTLE_DURATION = 600;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_IDLE = 0;
   public static final int STATE_SETTLING = 2;
   private static final String TAG = "ViewDragHelper";
   private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float var1) {
         --var1;
         return var1 * var1 * var1 * var1 * var1 + 1.0F;
      }
   };
   private int mActivePointerId = -1;
   private final ViewDragHelper.Callback mCallback;
   private View mCapturedView;
   private int mDragState;
   private int[] mEdgeDragsInProgress;
   private int[] mEdgeDragsLocked;
   private int mEdgeSize;
   private int[] mInitialEdgesTouched;
   private float[] mInitialMotionX;
   private float[] mInitialMotionY;
   private float[] mLastMotionX;
   private float[] mLastMotionY;
   private float mMaxVelocity;
   private float mMinVelocity;
   private final ViewGroup mParentView;
   private int mPointersDown;
   private boolean mReleaseInProgress;
   private OverScroller mScroller;
   private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
         ViewDragHelper.this.setDragState(0);
      }
   };
   private int mTouchSlop;
   private int mTrackingEdges;
   private VelocityTracker mVelocityTracker;

   private ViewDragHelper(Context var1, ViewGroup var2, ViewDragHelper.Callback var3) {
      if (var2 != null) {
         if (var3 != null) {
            this.mParentView = var2;
            this.mCallback = var3;
            ViewConfiguration var4 = ViewConfiguration.get(var1);
            this.mEdgeSize = (int)(20.0F * var1.getResources().getDisplayMetrics().density + 0.5F);
            this.mTouchSlop = var4.getScaledTouchSlop();
            this.mMaxVelocity = (float)var4.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float)var4.getScaledMinimumFlingVelocity();
            this.mScroller = new OverScroller(var1, sInterpolator);
         } else {
            throw new IllegalArgumentException("Callback may not be null");
         }
      } else {
         throw new IllegalArgumentException("Parent view may not be null");
      }
   }

   private boolean checkNewEdgeDrag(float var1, float var2, int var3, int var4) {
      var1 = Math.abs(var1);
      var2 = Math.abs(var2);
      int var5 = this.mInitialEdgesTouched[var3];
      boolean var7 = false;
      if ((var5 & var4) == var4 && (this.mTrackingEdges & var4) != 0 && (this.mEdgeDragsLocked[var3] & var4) != var4 && (this.mEdgeDragsInProgress[var3] & var4) != var4) {
         var5 = this.mTouchSlop;
         if (var1 <= (float)var5 && var2 <= (float)var5) {
            return false;
         } else if (var1 < 0.5F * var2 && this.mCallback.onEdgeLock(var4)) {
            int[] var8 = this.mEdgeDragsLocked;
            var8[var3] |= var4;
            return false;
         } else {
            boolean var6 = var7;
            if ((this.mEdgeDragsInProgress[var3] & var4) == 0) {
               var6 = var7;
               if (var1 > (float)this.mTouchSlop) {
                  var6 = true;
               }
            }

            return var6;
         }
      } else {
         return false;
      }
   }

   private boolean checkTouchSlop(View var1, float var2, float var3) {
      boolean var7 = false;
      boolean var8 = false;
      boolean var6 = false;
      if (var1 == null) {
         return false;
      } else {
         boolean var4;
         if (this.mCallback.getViewHorizontalDragRange(var1) > 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         boolean var5;
         if (this.mCallback.getViewVerticalDragRange(var1) > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var4 && var5) {
            int var9 = this.mTouchSlop;
            if (var2 * var2 + var3 * var3 > (float)(var9 * var9)) {
               var6 = true;
            }

            return var6;
         } else if (var4) {
            var6 = var7;
            if (Math.abs(var2) > (float)this.mTouchSlop) {
               var6 = true;
            }

            return var6;
         } else if (var5) {
            var6 = var8;
            if (Math.abs(var3) > (float)this.mTouchSlop) {
               var6 = true;
            }

            return var6;
         } else {
            return false;
         }
      }
   }

   private float clampMag(float var1, float var2, float var3) {
      float var4 = Math.abs(var1);
      if (var4 < var2) {
         return 0.0F;
      } else if (var4 > var3) {
         return var1 > 0.0F ? var3 : -var3;
      } else {
         return var1;
      }
   }

   private int clampMag(int var1, int var2, int var3) {
      int var4 = Math.abs(var1);
      if (var4 < var2) {
         return 0;
      } else if (var4 > var3) {
         return var1 > 0 ? var3 : -var3;
      } else {
         return var1;
      }
   }

   private void clearMotionHistory() {
      float[] var1 = this.mInitialMotionX;
      if (var1 != null) {
         Arrays.fill(var1, 0.0F);
         Arrays.fill(this.mInitialMotionY, 0.0F);
         Arrays.fill(this.mLastMotionX, 0.0F);
         Arrays.fill(this.mLastMotionY, 0.0F);
         Arrays.fill(this.mInitialEdgesTouched, 0);
         Arrays.fill(this.mEdgeDragsInProgress, 0);
         Arrays.fill(this.mEdgeDragsLocked, 0);
         this.mPointersDown = 0;
      }
   }

   private void clearMotionHistory(int var1) {
      if (this.mInitialMotionX != null) {
         if (this.isPointerDown(var1)) {
            this.mInitialMotionX[var1] = 0.0F;
            this.mInitialMotionY[var1] = 0.0F;
            this.mLastMotionX[var1] = 0.0F;
            this.mLastMotionY[var1] = 0.0F;
            this.mInitialEdgesTouched[var1] = 0;
            this.mEdgeDragsInProgress[var1] = 0;
            this.mEdgeDragsLocked[var1] = 0;
            this.mPointersDown &= 1 << var1;
         }
      }
   }

   private int computeAxisDuration(int var1, int var2, int var3) {
      if (var1 == 0) {
         return 0;
      } else {
         int var7 = this.mParentView.getWidth();
         int var8 = var7 / 2;
         float var6 = Math.min(1.0F, (float)Math.abs(var1) / (float)var7);
         float var4 = (float)var8;
         float var5 = (float)var8;
         var6 = this.distanceInfluenceForSnapDuration(var6);
         var2 = Math.abs(var2);
         if (var2 > 0) {
            var1 = Math.round(Math.abs((var4 + var5 * var6) / (float)var2) * 1000.0F) * 4;
         } else {
            var1 = (int)((1.0F + (float)Math.abs(var1) / (float)var3) * 256.0F);
         }

         return Math.min(var1, 600);
      }
   }

   private int computeSettleDuration(View var1, int var2, int var3, int var4, int var5) {
      var4 = this.clampMag(var4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
      var5 = this.clampMag(var5, (int)this.mMinVelocity, (int)this.mMaxVelocity);
      int var9 = Math.abs(var2);
      int var10 = Math.abs(var3);
      int var11 = Math.abs(var4);
      int var12 = Math.abs(var5);
      int var13 = var11 + var12;
      int var14 = var9 + var10;
      float var6;
      float var7;
      if (var4 != 0) {
         var6 = (float)var11;
         var7 = (float)var13;
      } else {
         var6 = (float)var9;
         var7 = (float)var14;
      }

      float var8 = var6 / var7;
      if (var5 != 0) {
         var6 = (float)var12;
         var7 = (float)var13;
      } else {
         var6 = (float)var10;
         var7 = (float)var14;
      }

      var6 /= var7;
      var2 = this.computeAxisDuration(var2, var4, this.mCallback.getViewHorizontalDragRange(var1));
      var3 = this.computeAxisDuration(var3, var5, this.mCallback.getViewVerticalDragRange(var1));
      return (int)((float)var2 * var8 + (float)var3 * var6);
   }

   public static ViewDragHelper create(ViewGroup var0, float var1, ViewDragHelper.Callback var2) {
      ViewDragHelper var3 = create(var0, var2);
      var3.mTouchSlop = (int)((float)var3.mTouchSlop * (1.0F / var1));
      return var3;
   }

   public static ViewDragHelper create(ViewGroup var0, ViewDragHelper.Callback var1) {
      return new ViewDragHelper(var0.getContext(), var0, var1);
   }

   private void dispatchViewReleased(float var1, float var2) {
      this.mReleaseInProgress = true;
      this.mCallback.onViewReleased(this.mCapturedView, var1, var2);
      this.mReleaseInProgress = false;
      if (this.mDragState == 1) {
         this.setDragState(0);
      }

   }

   private float distanceInfluenceForSnapDuration(float var1) {
      return (float)Math.sin((double)((var1 - 0.5F) * 0.47123894F));
   }

   private void dragTo(int var1, int var2, int var3, int var4) {
      int var5 = var2;
      int var7 = this.mCapturedView.getLeft();
      int var8 = this.mCapturedView.getTop();
      if (var3 != 0) {
         var1 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, var1, var3);
         ViewCompat.offsetLeftAndRight(this.mCapturedView, var1 - var7);
      } else {
         var1 = var1;
      }

      if (var4 != 0) {
         var5 = this.mCallback.clampViewPositionVertical(this.mCapturedView, var2, var4);
         ViewCompat.offsetTopAndBottom(this.mCapturedView, var5 - var8);
      }

      if (var3 != 0 || var4 != 0) {
         this.mCallback.onViewPositionChanged(this.mCapturedView, var1, var5, var1 - var7, var5 - var8);
      }

   }

   private void ensureMotionHistorySizeForId(int var1) {
      float[] var2 = this.mInitialMotionX;
      if (var2 == null || var2.length <= var1) {
         var2 = new float[var1 + 1];
         float[] var3 = new float[var1 + 1];
         float[] var4 = new float[var1 + 1];
         float[] var5 = new float[var1 + 1];
         int[] var6 = new int[var1 + 1];
         int[] var7 = new int[var1 + 1];
         int[] var8 = new int[var1 + 1];
         float[] var9 = this.mInitialMotionX;
         if (var9 != null) {
            System.arraycopy(var9, 0, var2, 0, var9.length);
            var9 = this.mInitialMotionY;
            System.arraycopy(var9, 0, var3, 0, var9.length);
            var9 = this.mLastMotionX;
            System.arraycopy(var9, 0, var4, 0, var9.length);
            var9 = this.mLastMotionY;
            System.arraycopy(var9, 0, var5, 0, var9.length);
            int[] var10 = this.mInitialEdgesTouched;
            System.arraycopy(var10, 0, var6, 0, var10.length);
            var10 = this.mEdgeDragsInProgress;
            System.arraycopy(var10, 0, var7, 0, var10.length);
            var10 = this.mEdgeDragsLocked;
            System.arraycopy(var10, 0, var8, 0, var10.length);
         }

         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         this.mLastMotionX = var4;
         this.mLastMotionY = var5;
         this.mInitialEdgesTouched = var6;
         this.mEdgeDragsInProgress = var7;
         this.mEdgeDragsLocked = var8;
      }

   }

   private boolean forceSettleCapturedViewAt(int var1, int var2, int var3, int var4) {
      int var5 = this.mCapturedView.getLeft();
      int var6 = this.mCapturedView.getTop();
      var1 -= var5;
      var2 -= var6;
      if (var1 == 0 && var2 == 0) {
         this.mScroller.abortAnimation();
         this.setDragState(0);
         return false;
      } else {
         var3 = this.computeSettleDuration(this.mCapturedView, var1, var2, var3, var4);
         this.mScroller.startScroll(var5, var6, var1, var2, var3);
         this.setDragState(2);
         return true;
      }
   }

   private int getEdgesTouched(int var1, int var2) {
      int var4 = 0;
      if (var1 < this.mParentView.getLeft() + this.mEdgeSize) {
         var4 = 0 | 1;
      }

      int var3 = var4;
      if (var2 < this.mParentView.getTop() + this.mEdgeSize) {
         var3 = var4 | 4;
      }

      var4 = var3;
      if (var1 > this.mParentView.getRight() - this.mEdgeSize) {
         var4 = var3 | 2;
      }

      var1 = var4;
      if (var2 > this.mParentView.getBottom() - this.mEdgeSize) {
         var1 = var4 | 8;
      }

      return var1;
   }

   private boolean isValidPointerForActionMove(int var1) {
      if (!this.isPointerDown(var1)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Ignoring pointerId=");
         var2.append(var1);
         var2.append(" because ACTION_DOWN was not received ");
         var2.append("for this pointer before ACTION_MOVE. It likely happened because ");
         var2.append(" ViewDragHelper did not receive all the events in the event stream.");
         Log.e("ViewDragHelper", var2.toString());
         return false;
      } else {
         return true;
      }
   }

   private void releaseViewForPointerUp() {
      this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
      this.dispatchViewReleased(this.clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
   }

   private void reportNewEdgeDrags(float var1, float var2, int var3) {
      int var5 = 0;
      if (this.checkNewEdgeDrag(var1, var2, var3, 1)) {
         var5 = 0 | 1;
      }

      int var4 = var5;
      if (this.checkNewEdgeDrag(var2, var1, var3, 4)) {
         var4 = var5 | 4;
      }

      var5 = var4;
      if (this.checkNewEdgeDrag(var1, var2, var3, 2)) {
         var5 = var4 | 2;
      }

      var4 = var5;
      if (this.checkNewEdgeDrag(var2, var1, var3, 8)) {
         var4 = var5 | 8;
      }

      if (var4 != 0) {
         int[] var6 = this.mEdgeDragsInProgress;
         var6[var3] |= var4;
         this.mCallback.onEdgeDragStarted(var4, var3);
      }

   }

   private void saveInitialMotion(float var1, float var2, int var3) {
      this.ensureMotionHistorySizeForId(var3);
      float[] var4 = this.mInitialMotionX;
      this.mLastMotionX[var3] = var1;
      var4[var3] = var1;
      var4 = this.mInitialMotionY;
      this.mLastMotionY[var3] = var2;
      var4[var3] = var2;
      this.mInitialEdgesTouched[var3] = this.getEdgesTouched((int)var1, (int)var2);
      this.mPointersDown |= 1 << var3;
   }

   private void saveLastMotion(MotionEvent var1) {
      int var5 = var1.getPointerCount();

      for(int var4 = 0; var4 < var5; ++var4) {
         int var6 = var1.getPointerId(var4);
         if (this.isValidPointerForActionMove(var6)) {
            float var2 = var1.getX(var4);
            float var3 = var1.getY(var4);
            this.mLastMotionX[var6] = var2;
            this.mLastMotionY[var6] = var3;
         }
      }

   }

   public void abort() {
      this.cancel();
      if (this.mDragState == 2) {
         int var1 = this.mScroller.getCurrX();
         int var2 = this.mScroller.getCurrY();
         this.mScroller.abortAnimation();
         int var3 = this.mScroller.getCurrX();
         int var4 = this.mScroller.getCurrY();
         this.mCallback.onViewPositionChanged(this.mCapturedView, var3, var4, var3 - var1, var4 - var2);
      }

      this.setDragState(0);
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5, int var6) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var10 = (ViewGroup)var1;
         int var8 = var1.getScrollX();
         int var9 = var1.getScrollY();

         for(int var7 = var10.getChildCount() - 1; var7 >= 0; --var7) {
            View var11 = var10.getChildAt(var7);
            if (var5 + var8 >= var11.getLeft() && var5 + var8 < var11.getRight() && var6 + var9 >= var11.getTop() && var6 + var9 < var11.getBottom() && this.canScroll(var11, true, var3, var4, var5 + var8 - var11.getLeft(), var6 + var9 - var11.getTop())) {
               return true;
            }
         }
      }

      if (var2) {
         if (var1.canScrollHorizontally(-var3)) {
            return true;
         }

         if (var1.canScrollVertically(-var4)) {
            return true;
         }
      }

      return false;
   }

   public void cancel() {
      this.mActivePointerId = -1;
      this.clearMotionHistory();
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 != null) {
         var1.recycle();
         this.mVelocityTracker = null;
      }

   }

   public void captureChildView(View var1, int var2) {
      if (var1.getParent() == this.mParentView) {
         this.mCapturedView = var1;
         this.mActivePointerId = var2;
         this.mCallback.onViewCaptured(var1, var2);
         this.setDragState(1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
         var3.append(this.mParentView);
         var3.append(")");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public boolean checkTouchSlop(int var1) {
      int var3 = this.mInitialMotionX.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (this.checkTouchSlop(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   public boolean checkTouchSlop(int var1, int var2) {
      boolean var9 = this.isPointerDown(var2);
      boolean var7 = false;
      boolean var8 = false;
      boolean var6 = false;
      if (!var9) {
         return false;
      } else {
         boolean var5;
         if ((var1 & 1) == 1) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var10;
         if ((var1 & 2) == 2) {
            var10 = true;
         } else {
            var10 = false;
         }

         float var3 = this.mLastMotionX[var2] - this.mInitialMotionX[var2];
         float var4 = this.mLastMotionY[var2] - this.mInitialMotionY[var2];
         if (var5 && var10) {
            var1 = this.mTouchSlop;
            if (var3 * var3 + var4 * var4 > (float)(var1 * var1)) {
               var6 = true;
            }

            return var6;
         } else if (var5) {
            var6 = var7;
            if (Math.abs(var3) > (float)this.mTouchSlop) {
               var6 = true;
            }

            return var6;
         } else if (var10) {
            var6 = var8;
            if (Math.abs(var4) > (float)this.mTouchSlop) {
               var6 = true;
            }

            return var6;
         } else {
            return false;
         }
      }
   }

   public boolean continueSettling(boolean var1) {
      int var2 = this.mDragState;
      boolean var7 = false;
      if (var2 == 2) {
         boolean var8 = this.mScroller.computeScrollOffset();
         var2 = this.mScroller.getCurrX();
         int var3 = this.mScroller.getCurrY();
         int var4 = var2 - this.mCapturedView.getLeft();
         int var5 = var3 - this.mCapturedView.getTop();
         if (var4 != 0) {
            ViewCompat.offsetLeftAndRight(this.mCapturedView, var4);
         }

         if (var5 != 0) {
            ViewCompat.offsetTopAndBottom(this.mCapturedView, var5);
         }

         if (var4 != 0 || var5 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, var2, var3, var4, var5);
         }

         boolean var6 = var8;
         if (var8) {
            var6 = var8;
            if (var2 == this.mScroller.getFinalX()) {
               var6 = var8;
               if (var3 == this.mScroller.getFinalY()) {
                  this.mScroller.abortAnimation();
                  var6 = false;
               }
            }
         }

         if (!var6) {
            if (var1) {
               this.mParentView.post(this.mSetIdleRunnable);
            } else {
               this.setDragState(0);
            }
         }
      }

      var1 = var7;
      if (this.mDragState == 2) {
         var1 = true;
      }

      return var1;
   }

   public View findTopChildUnder(int var1, int var2) {
      for(int var3 = this.mParentView.getChildCount() - 1; var3 >= 0; --var3) {
         View var4 = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(var3));
         if (var1 >= var4.getLeft() && var1 < var4.getRight() && var2 >= var4.getTop() && var2 < var4.getBottom()) {
            return var4;
         }
      }

      return null;
   }

   public void flingCapturedView(int var1, int var2, int var3, int var4) {
      if (this.mReleaseInProgress) {
         this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), var1, var3, var2, var4);
         this.setDragState(2);
      } else {
         throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
      }
   }

   public int getActivePointerId() {
      return this.mActivePointerId;
   }

   public View getCapturedView() {
      return this.mCapturedView;
   }

   public int getEdgeSize() {
      return this.mEdgeSize;
   }

   public float getMinVelocity() {
      return this.mMinVelocity;
   }

   public int getTouchSlop() {
      return this.mTouchSlop;
   }

   public int getViewDragState() {
      return this.mDragState;
   }

   public boolean isCapturedViewUnder(int var1, int var2) {
      return this.isViewUnder(this.mCapturedView, var1, var2);
   }

   public boolean isEdgeTouched(int var1) {
      int var3 = this.mInitialEdgesTouched.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (this.isEdgeTouched(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   public boolean isEdgeTouched(int var1, int var2) {
      return this.isPointerDown(var2) && (this.mInitialEdgesTouched[var2] & var1) != 0;
   }

   public boolean isPointerDown(int var1) {
      return (this.mPointersDown & 1 << var1) != 0;
   }

   public boolean isViewUnder(View var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         return var2 >= var1.getLeft() && var2 < var1.getRight() && var3 >= var1.getTop() && var3 < var1.getBottom();
      }
   }

   public void processTouchEvent(MotionEvent var1) {
      int var6 = var1.getActionMasked();
      int var7 = var1.getActionIndex();
      if (var6 == 0) {
         this.cancel();
      }

      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(var1);
      float var2;
      float var3;
      int var8;
      if (var6 == 0) {
         var2 = var1.getX();
         var3 = var1.getY();
         var6 = var1.getPointerId(0);
         View var13 = this.findTopChildUnder((int)var2, (int)var3);
         this.saveInitialMotion(var2, var3, var6);
         this.tryCaptureViewForDrag(var13, var6);
         var7 = this.mInitialEdgesTouched[var6];
         var8 = this.mTrackingEdges;
         if ((var7 & var8) != 0) {
            this.mCallback.onEdgeTouched(var8 & var7, var6);
         }

      } else if (var6 == 1) {
         if (this.mDragState == 1) {
            this.releaseViewForPointerUp();
         }

         this.cancel();
      } else {
         View var11;
         if (var6 != 2) {
            if (var6 == 3) {
               if (this.mDragState == 1) {
                  this.dispatchViewReleased(0.0F, 0.0F);
               }

               this.cancel();
            } else if (var6 != 5) {
               if (var6 == 6) {
                  int var9 = var1.getPointerId(var7);
                  if (this.mDragState == 1 && var9 == this.mActivePointerId) {
                     byte var14 = -1;
                     int var10 = var1.getPointerCount();
                     var6 = 0;

                     while(true) {
                        var7 = var14;
                        if (var6 >= var10) {
                           break;
                        }

                        var7 = var1.getPointerId(var6);
                        if (var7 != this.mActivePointerId) {
                           var2 = var1.getX(var6);
                           var3 = var1.getY(var6);
                           var11 = this.findTopChildUnder((int)var2, (int)var3);
                           View var12 = this.mCapturedView;
                           if (var11 == var12 && this.tryCaptureViewForDrag(var12, var7)) {
                              var7 = this.mActivePointerId;
                              break;
                           }
                        }

                        ++var6;
                     }

                     if (var7 == -1) {
                        this.releaseViewForPointerUp();
                     }
                  }

                  this.clearMotionHistory(var9);
               }
            } else {
               var6 = var1.getPointerId(var7);
               var2 = var1.getX(var7);
               var3 = var1.getY(var7);
               this.saveInitialMotion(var2, var3, var6);
               if (this.mDragState == 0) {
                  this.tryCaptureViewForDrag(this.findTopChildUnder((int)var2, (int)var3), var6);
                  var7 = this.mInitialEdgesTouched[var6];
                  var8 = this.mTrackingEdges;
                  if ((var7 & var8) != 0) {
                     this.mCallback.onEdgeTouched(var8 & var7, var6);
                  }
               } else if (this.isCapturedViewUnder((int)var2, (int)var3)) {
                  this.tryCaptureViewForDrag(this.mCapturedView, var6);
                  return;
               }

            }
         } else if (this.mDragState == 1) {
            if (this.isValidPointerForActionMove(this.mActivePointerId)) {
               var6 = var1.findPointerIndex(this.mActivePointerId);
               var2 = var1.getX(var6);
               var3 = var1.getY(var6);
               float[] var15 = this.mLastMotionX;
               var7 = this.mActivePointerId;
               var6 = (int)(var2 - var15[var7]);
               var7 = (int)(var3 - this.mLastMotionY[var7]);
               this.dragTo(this.mCapturedView.getLeft() + var6, this.mCapturedView.getTop() + var7, var6, var7);
               this.saveLastMotion(var1);
            }
         } else {
            var7 = var1.getPointerCount();

            for(var6 = 0; var6 < var7; ++var6) {
               var8 = var1.getPointerId(var6);
               if (this.isValidPointerForActionMove(var8)) {
                  var2 = var1.getX(var6);
                  var3 = var1.getY(var6);
                  float var4 = var2 - this.mInitialMotionX[var8];
                  float var5 = var3 - this.mInitialMotionY[var8];
                  this.reportNewEdgeDrags(var4, var5, var8);
                  if (this.mDragState == 1) {
                     break;
                  }

                  var11 = this.findTopChildUnder((int)var2, (int)var3);
                  if (this.checkTouchSlop(var11, var4, var5) && this.tryCaptureViewForDrag(var11, var8)) {
                     break;
                  }
               }
            }

            this.saveLastMotion(var1);
         }
      }
   }

   void setDragState(int var1) {
      this.mParentView.removeCallbacks(this.mSetIdleRunnable);
      if (this.mDragState != var1) {
         this.mDragState = var1;
         this.mCallback.onViewDragStateChanged(var1);
         if (this.mDragState == 0) {
            this.mCapturedView = null;
         }
      }

   }

   public void setEdgeTrackingEnabled(int var1) {
      this.mTrackingEdges = var1;
   }

   public void setMinVelocity(float var1) {
      this.mMinVelocity = var1;
   }

   public boolean settleCapturedViewAt(int var1, int var2) {
      if (this.mReleaseInProgress) {
         return this.forceSettleCapturedViewAt(var1, var2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId));
      } else {
         throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
      }
   }

   public boolean shouldInterceptTouchEvent(MotionEvent var1) {
      int var8 = var1.getActionMasked();
      int var7 = var1.getActionIndex();
      if (var8 == 0) {
         this.cancel();
      }

      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(var1);
      float var2;
      float var3;
      int var6;
      View var20;
      if (var8 != 0) {
         label112: {
            if (var8 != 1) {
               if (var8 == 2) {
                  if (this.mInitialMotionX != null && this.mInitialMotionY != null) {
                     var6 = var1.getPointerCount();

                     for(int var9 = 0; var9 < var6; ++var9) {
                        int var11 = var1.getPointerId(var9);
                        if (this.isValidPointerForActionMove(var11)) {
                           var2 = var1.getX(var9);
                           var3 = var1.getY(var9);
                           float var4 = var2 - this.mInitialMotionX[var11];
                           float var5 = var3 - this.mInitialMotionY[var11];
                           View var19 = this.findTopChildUnder((int)var2, (int)var3);
                           boolean var10;
                           if (var19 != null && this.checkTouchSlop(var19, var4, var5)) {
                              var10 = true;
                           } else {
                              var10 = false;
                           }

                           if (var10) {
                              int var12 = var19.getLeft();
                              int var13 = (int)var4;
                              var13 = this.mCallback.clampViewPositionHorizontal(var19, var13 + var12, (int)var4);
                              int var14 = var19.getTop();
                              int var15 = (int)var5;
                              var15 = this.mCallback.clampViewPositionVertical(var19, var15 + var14, (int)var5);
                              int var16 = this.mCallback.getViewHorizontalDragRange(var19);
                              int var17 = this.mCallback.getViewVerticalDragRange(var19);
                              if ((var16 == 0 || var16 > 0 && var13 == var12) && (var17 == 0 || var17 > 0 && var15 == var14)) {
                                 break;
                              }
                           }

                           this.reportNewEdgeDrags(var4, var5, var11);
                           if (this.mDragState == 1 || var10 && this.tryCaptureViewForDrag(var19, var11)) {
                              break;
                           }
                        }
                     }

                     this.saveLastMotion(var1);
                  }
                  break label112;
               }

               if (var8 != 3) {
                  if (var8 != 5) {
                     if (var8 == 6) {
                        this.clearMotionHistory(var1.getPointerId(var7));
                     }
                  } else {
                     var6 = var1.getPointerId(var7);
                     var2 = var1.getX(var7);
                     var3 = var1.getY(var7);
                     this.saveInitialMotion(var2, var3, var6);
                     var7 = this.mDragState;
                     if (var7 == 0) {
                        var7 = this.mInitialEdgesTouched[var6];
                        var8 = this.mTrackingEdges;
                        if ((var7 & var8) != 0) {
                           this.mCallback.onEdgeTouched(var8 & var7, var6);
                        }
                     } else if (var7 == 2) {
                        var20 = this.findTopChildUnder((int)var2, (int)var3);
                        if (var20 == this.mCapturedView) {
                           this.tryCaptureViewForDrag(var20, var6);
                        }
                     }
                  }
                  break label112;
               }
            }

            this.cancel();
         }
      } else {
         var2 = var1.getX();
         var3 = var1.getY();
         var6 = var1.getPointerId(0);
         this.saveInitialMotion(var2, var3, var6);
         var20 = this.findTopChildUnder((int)var2, (int)var3);
         if (var20 == this.mCapturedView && this.mDragState == 2) {
            this.tryCaptureViewForDrag(var20, var6);
         }

         var7 = this.mInitialEdgesTouched[var6];
         var8 = this.mTrackingEdges;
         if ((var7 & var8) != 0) {
            this.mCallback.onEdgeTouched(var8 & var7, var6);
         }
      }

      boolean var18 = false;
      if (this.mDragState == 1) {
         var18 = true;
      }

      return var18;
   }

   public boolean smoothSlideViewTo(View var1, int var2, int var3) {
      this.mCapturedView = var1;
      this.mActivePointerId = -1;
      boolean var4 = this.forceSettleCapturedViewAt(var2, var3, 0, 0);
      if (!var4 && this.mDragState == 0 && this.mCapturedView != null) {
         this.mCapturedView = null;
      }

      return var4;
   }

   boolean tryCaptureViewForDrag(View var1, int var2) {
      if (var1 == this.mCapturedView && this.mActivePointerId == var2) {
         return true;
      } else if (var1 != null && this.mCallback.tryCaptureView(var1, var2)) {
         this.mActivePointerId = var2;
         this.captureChildView(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public abstract static class Callback {
      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         return 0;
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return 0;
      }

      public int getOrderedChildIndex(int var1) {
         return var1;
      }

      public int getViewHorizontalDragRange(View var1) {
         return 0;
      }

      public int getViewVerticalDragRange(View var1) {
         return 0;
      }

      public void onEdgeDragStarted(int var1, int var2) {
      }

      public boolean onEdgeLock(int var1) {
         return false;
      }

      public void onEdgeTouched(int var1, int var2) {
      }

      public void onViewCaptured(View var1, int var2) {
      }

      public void onViewDragStateChanged(int var1) {
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
      }

      public void onViewReleased(View var1, float var2, float var3) {
      }

      public abstract boolean tryCaptureView(View var1, int var2);
   }
}
