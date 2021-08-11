package androidx.core.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;

public final class GestureDetectorCompat {
   private final GestureDetectorCompat.GestureDetectorCompatImpl mImpl;

   public GestureDetectorCompat(Context var1, OnGestureListener var2) {
      this(var1, var2, (Handler)null);
   }

   public GestureDetectorCompat(Context var1, OnGestureListener var2, Handler var3) {
      if (VERSION.SDK_INT > 17) {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplJellybeanMr2(var1, var2, var3);
      } else {
         this.mImpl = new GestureDetectorCompat.GestureDetectorCompatImplBase(var1, var2, var3);
      }
   }

   public boolean isLongpressEnabled() {
      return this.mImpl.isLongpressEnabled();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.mImpl.onTouchEvent(var1);
   }

   public void setIsLongpressEnabled(boolean var1) {
      this.mImpl.setIsLongpressEnabled(var1);
   }

   public void setOnDoubleTapListener(OnDoubleTapListener var1) {
      this.mImpl.setOnDoubleTapListener(var1);
   }

   interface GestureDetectorCompatImpl {
      boolean isLongpressEnabled();

      boolean onTouchEvent(MotionEvent var1);

      void setIsLongpressEnabled(boolean var1);

      void setOnDoubleTapListener(OnDoubleTapListener var1);
   }

   static class GestureDetectorCompatImplBase implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
      private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
      private static final int LONG_PRESS = 2;
      private static final int SHOW_PRESS = 1;
      private static final int TAP = 3;
      private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
      private boolean mAlwaysInBiggerTapRegion;
      private boolean mAlwaysInTapRegion;
      MotionEvent mCurrentDownEvent;
      boolean mDeferConfirmSingleTap;
      OnDoubleTapListener mDoubleTapListener;
      private int mDoubleTapSlopSquare;
      private float mDownFocusX;
      private float mDownFocusY;
      private final Handler mHandler;
      private boolean mInLongPress;
      private boolean mIsDoubleTapping;
      private boolean mIsLongpressEnabled;
      private float mLastFocusX;
      private float mLastFocusY;
      final OnGestureListener mListener;
      private int mMaximumFlingVelocity;
      private int mMinimumFlingVelocity;
      private MotionEvent mPreviousUpEvent;
      boolean mStillDown;
      private int mTouchSlopSquare;
      private VelocityTracker mVelocityTracker;

      GestureDetectorCompatImplBase(Context var1, OnGestureListener var2, Handler var3) {
         if (var3 != null) {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler(var3);
         } else {
            this.mHandler = new GestureDetectorCompat.GestureDetectorCompatImplBase.GestureHandler();
         }

         this.mListener = var2;
         if (var2 instanceof OnDoubleTapListener) {
            this.setOnDoubleTapListener((OnDoubleTapListener)var2);
         }

         this.init(var1);
      }

      private void cancel() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
         this.mIsDoubleTapping = false;
         this.mStillDown = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void cancelTaps() {
         this.mHandler.removeMessages(1);
         this.mHandler.removeMessages(2);
         this.mHandler.removeMessages(3);
         this.mIsDoubleTapping = false;
         this.mAlwaysInTapRegion = false;
         this.mAlwaysInBiggerTapRegion = false;
         this.mDeferConfirmSingleTap = false;
         if (this.mInLongPress) {
            this.mInLongPress = false;
         }

      }

      private void init(Context var1) {
         if (var1 != null) {
            if (this.mListener != null) {
               this.mIsLongpressEnabled = true;
               ViewConfiguration var4 = ViewConfiguration.get(var1);
               int var2 = var4.getScaledTouchSlop();
               int var3 = var4.getScaledDoubleTapSlop();
               this.mMinimumFlingVelocity = var4.getScaledMinimumFlingVelocity();
               this.mMaximumFlingVelocity = var4.getScaledMaximumFlingVelocity();
               this.mTouchSlopSquare = var2 * var2;
               this.mDoubleTapSlopSquare = var3 * var3;
            } else {
               throw new IllegalArgumentException("OnGestureListener must not be null");
            }
         } else {
            throw new IllegalArgumentException("Context must not be null");
         }
      }

      private boolean isConsideredDoubleTap(MotionEvent var1, MotionEvent var2, MotionEvent var3) {
         boolean var7 = this.mAlwaysInBiggerTapRegion;
         boolean var6 = false;
         if (!var7) {
            return false;
         } else if (var3.getEventTime() - var2.getEventTime() > (long)DOUBLE_TAP_TIMEOUT) {
            return false;
         } else {
            int var4 = (int)var1.getX() - (int)var3.getX();
            int var5 = (int)var1.getY() - (int)var3.getY();
            if (var4 * var4 + var5 * var5 < this.mDoubleTapSlopSquare) {
               var6 = true;
            }

            return var6;
         }
      }

      void dispatchLongPress() {
         this.mHandler.removeMessages(3);
         this.mDeferConfirmSingleTap = false;
         this.mInLongPress = true;
         this.mListener.onLongPress(this.mCurrentDownEvent);
      }

      public boolean isLongpressEnabled() {
         return this.mIsLongpressEnabled;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var9 = var1.getAction();
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         boolean var6;
         if ((var9 & 255) == 6) {
            var6 = true;
         } else {
            var6 = false;
         }

         int var7;
         if (var6) {
            var7 = var1.getActionIndex();
         } else {
            var7 = -1;
         }

         float var3 = 0.0F;
         float var2 = 0.0F;
         int var10 = var1.getPointerCount();

         int var8;
         for(var8 = 0; var8 < var10; ++var8) {
            if (var7 != var8) {
               var3 += var1.getX(var8);
               var2 += var1.getY(var8);
            }
         }

         if (var6) {
            var7 = var10 - 1;
         } else {
            var7 = var10;
         }

         var3 /= (float)var7;
         var2 /= (float)var7;
         boolean var13 = false;
         boolean var18 = false;
         boolean var12 = false;
         var8 = var9 & 255;
         MotionEvent var14;
         if (var8 != 0) {
            int var17;
            if (var8 != 1) {
               if (var8 != 2) {
                  if (var8 != 3) {
                     if (var8 != 5) {
                        if (var8 == 6) {
                           this.mLastFocusX = var3;
                           this.mDownFocusX = var3;
                           this.mLastFocusY = var2;
                           this.mDownFocusY = var2;
                           this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                           var8 = var1.getActionIndex();
                           var7 = var1.getPointerId(var8);
                           var2 = this.mVelocityTracker.getXVelocity(var7);
                           var3 = this.mVelocityTracker.getYVelocity(var7);

                           for(var9 = 0; var9 < var10; ++var9) {
                              if (var9 != var8) {
                                 int var11 = var1.getPointerId(var9);
                                 if (this.mVelocityTracker.getXVelocity(var11) * var2 + this.mVelocityTracker.getYVelocity(var11) * var3 < 0.0F) {
                                    this.mVelocityTracker.clear();
                                    break;
                                 }
                              }
                           }
                        }
                     } else {
                        this.mLastFocusX = var3;
                        this.mDownFocusX = var3;
                        this.mLastFocusY = var2;
                        this.mDownFocusY = var2;
                        this.cancelTaps();
                     }
                  } else {
                     this.cancel();
                  }
               } else if (!this.mInLongPress) {
                  float var4 = this.mLastFocusX - var3;
                  float var5 = this.mLastFocusY - var2;
                  if (this.mIsDoubleTapping) {
                     return false | this.mDoubleTapListener.onDoubleTapEvent(var1);
                  }

                  if (this.mAlwaysInTapRegion) {
                     var17 = (int)(var3 - this.mDownFocusX);
                     var7 = (int)(var2 - this.mDownFocusY);
                     var17 = var17 * var17 + var7 * var7;
                     if (var17 > this.mTouchSlopSquare) {
                        var12 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var4, var5);
                        this.mLastFocusX = var3;
                        this.mLastFocusY = var2;
                        this.mAlwaysInTapRegion = false;
                        this.mHandler.removeMessages(3);
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                     }

                     if (var17 > this.mTouchSlopSquare) {
                        this.mAlwaysInBiggerTapRegion = false;
                     }

                     return var12;
                  }

                  if (Math.abs(var4) >= 1.0F || Math.abs(var5) >= 1.0F) {
                     var12 = this.mListener.onScroll(this.mCurrentDownEvent, var1, var4, var5);
                     this.mLastFocusX = var3;
                     this.mLastFocusY = var2;
                     return var12;
                  }
               }

               return false;
            } else {
               this.mStillDown = false;
               var14 = MotionEvent.obtain(var1);
               if (this.mIsDoubleTapping) {
                  var12 = false | this.mDoubleTapListener.onDoubleTapEvent(var1);
               } else if (this.mInLongPress) {
                  this.mHandler.removeMessages(3);
                  this.mInLongPress = false;
                  var12 = var13;
               } else if (this.mAlwaysInTapRegion) {
                  var13 = this.mListener.onSingleTapUp(var1);
                  var12 = var13;
                  if (this.mDeferConfirmSingleTap) {
                     OnDoubleTapListener var19 = this.mDoubleTapListener;
                     var12 = var13;
                     if (var19 != null) {
                        var19.onSingleTapConfirmed(var1);
                        var12 = var13;
                     }
                  }
               } else {
                  label136: {
                     VelocityTracker var20 = this.mVelocityTracker;
                     var17 = var1.getPointerId(0);
                     var20.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                     var2 = var20.getYVelocity(var17);
                     var3 = var20.getXVelocity(var17);
                     if (Math.abs(var2) <= (float)this.mMinimumFlingVelocity) {
                        var12 = var13;
                        if (Math.abs(var3) <= (float)this.mMinimumFlingVelocity) {
                           break label136;
                        }
                     }

                     var12 = this.mListener.onFling(this.mCurrentDownEvent, var1, var3, var2);
                  }
               }

               var1 = this.mPreviousUpEvent;
               if (var1 != null) {
                  var1.recycle();
               }

               this.mPreviousUpEvent = var14;
               VelocityTracker var16 = this.mVelocityTracker;
               if (var16 != null) {
                  var16.recycle();
                  this.mVelocityTracker = null;
               }

               this.mIsDoubleTapping = false;
               this.mDeferConfirmSingleTap = false;
               this.mHandler.removeMessages(1);
               this.mHandler.removeMessages(2);
               return var12;
            }
         } else {
            var6 = var18;
            if (this.mDoubleTapListener != null) {
               label166: {
                  var12 = this.mHandler.hasMessages(3);
                  if (var12) {
                     this.mHandler.removeMessages(3);
                  }

                  var14 = this.mCurrentDownEvent;
                  if (var14 != null) {
                     MotionEvent var15 = this.mPreviousUpEvent;
                     if (var15 != null && var12 && this.isConsideredDoubleTap(var14, var15, var1)) {
                        this.mIsDoubleTapping = true;
                        var6 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(var1);
                        break label166;
                     }
                  }

                  this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
                  var6 = var18;
               }
            }

            this.mLastFocusX = var3;
            this.mDownFocusX = var3;
            this.mLastFocusY = var2;
            this.mDownFocusY = var2;
            var14 = this.mCurrentDownEvent;
            if (var14 != null) {
               var14.recycle();
            }

            this.mCurrentDownEvent = MotionEvent.obtain(var1);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled) {
               this.mHandler.removeMessages(2);
               this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT + (long)LONGPRESS_TIMEOUT);
            }

            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT);
            return var6 | this.mListener.onDown(var1);
         }
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mIsLongpressEnabled = var1;
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDoubleTapListener = var1;
      }

      private class GestureHandler extends Handler {
         GestureHandler() {
         }

         GestureHandler(Handler var2) {
            super(var2.getLooper());
         }

         public void handleMessage(Message var1) {
            int var2 = var1.what;
            if (var2 != 1) {
               if (var2 == 2) {
                  GestureDetectorCompatImplBase.this.dispatchLongPress();
                  return;
               }

               if (var2 != 3) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Unknown message ");
                  var3.append(var1);
                  throw new RuntimeException(var3.toString());
               }

               if (GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
                  if (!GestureDetectorCompatImplBase.this.mStillDown) {
                     GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                     return;
                  }

                  GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                  return;
               }
            } else {
               GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
            }

         }
      }
   }

   static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompat.GestureDetectorCompatImpl {
      private final GestureDetector mDetector;

      GestureDetectorCompatImplJellybeanMr2(Context var1, OnGestureListener var2, Handler var3) {
         this.mDetector = new GestureDetector(var1, var2, var3);
      }

      public boolean isLongpressEnabled() {
         return this.mDetector.isLongpressEnabled();
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return this.mDetector.onTouchEvent(var1);
      }

      public void setIsLongpressEnabled(boolean var1) {
         this.mDetector.setIsLongpressEnabled(var1);
      }

      public void setOnDoubleTapListener(OnDoubleTapListener var1) {
         this.mDetector.setOnDoubleTapListener(var1);
      }
   }
}
