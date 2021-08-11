package android.support.v7.widget;

import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ForwardingListener implements OnTouchListener, OnAttachStateChangeListener {
   private int mActivePointerId;
   private Runnable mDisallowIntercept;
   private boolean mForwarding;
   private final int mLongPressTimeout;
   private final float mScaledTouchSlop;
   final View mSrc;
   private final int mTapTimeout;
   private final int[] mTmpLocation = new int[2];
   private Runnable mTriggerLongPress;

   public ForwardingListener(View var1) {
      this.mSrc = var1;
      var1.setLongClickable(true);
      var1.addOnAttachStateChangeListener(this);
      this.mScaledTouchSlop = (float)ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      this.mTapTimeout = ViewConfiguration.getTapTimeout();
      this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
   }

   private void clearCallbacks() {
      Runnable var1 = this.mTriggerLongPress;
      if (var1 != null) {
         this.mSrc.removeCallbacks(var1);
      }

      var1 = this.mDisallowIntercept;
      if (var1 != null) {
         this.mSrc.removeCallbacks(var1);
      }

   }

   private boolean onTouchForwarded(MotionEvent var1) {
      View var6 = this.mSrc;
      ShowableListMenu var7 = this.getPopup();
      boolean var4 = false;
      if (var7 != null) {
         if (!var7.isShowing()) {
            return false;
         } else {
            DropDownListView var10 = (DropDownListView)var7.getListView();
            if (var10 != null) {
               if (!var10.isShown()) {
                  return false;
               } else {
                  MotionEvent var8 = MotionEvent.obtainNoHistory(var1);
                  this.toGlobalMotionEvent(var6, var8);
                  this.toLocalMotionEvent(var10, var8);
                  boolean var5 = var10.onForwardedEvent(var8, this.mActivePointerId);
                  var8.recycle();
                  int var2 = var1.getActionMasked();
                  boolean var9;
                  if (var2 != 1 && var2 != 3) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  boolean var3 = var4;
                  if (var5) {
                     var3 = var4;
                     if (var9) {
                        var3 = true;
                     }
                  }

                  return var3;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private boolean onTouchObserved(MotionEvent var1) {
      View var3 = this.mSrc;
      if (!var3.isEnabled()) {
         return false;
      } else {
         int var2 = var1.getActionMasked();
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  var2 = var1.findPointerIndex(this.mActivePointerId);
                  if (var2 >= 0) {
                     if (!pointInView(var3, var1.getX(var2), var1.getY(var2), this.mScaledTouchSlop)) {
                        this.clearCallbacks();
                        var3.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                     }

                     return false;
                  }

                  return false;
               }

               if (var2 != 3) {
                  return false;
               }
            }

            this.clearCallbacks();
            return false;
         } else {
            this.mActivePointerId = var1.getPointerId(0);
            if (this.mDisallowIntercept == null) {
               this.mDisallowIntercept = new ForwardingListener.DisallowIntercept();
            }

            var3.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
            if (this.mTriggerLongPress == null) {
               this.mTriggerLongPress = new ForwardingListener.TriggerLongPress();
            }

            var3.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
            return false;
         }
      }
   }

   private static boolean pointInView(View var0, float var1, float var2, float var3) {
      return var1 >= -var3 && var2 >= -var3 && var1 < (float)(var0.getRight() - var0.getLeft()) + var3 && var2 < (float)(var0.getBottom() - var0.getTop()) + var3;
   }

   private boolean toGlobalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)var3[0], (float)var3[1]);
      return true;
   }

   private boolean toLocalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)(-var3[0]), (float)(-var3[1]));
      return true;
   }

   public abstract ShowableListMenu getPopup();

   protected boolean onForwardingStarted() {
      ShowableListMenu var1 = this.getPopup();
      if (var1 != null && !var1.isShowing()) {
         var1.show();
      }

      return true;
   }

   protected boolean onForwardingStopped() {
      ShowableListMenu var1 = this.getPopup();
      if (var1 != null && var1.isShowing()) {
         var1.dismiss();
      }

      return true;
   }

   void onLongPress() {
      this.clearCallbacks();
      View var3 = this.mSrc;
      if (var3.isEnabled()) {
         if (!var3.isLongClickable()) {
            if (this.onForwardingStarted()) {
               var3.getParent().requestDisallowInterceptTouchEvent(true);
               long var1 = SystemClock.uptimeMillis();
               MotionEvent var4 = MotionEvent.obtain(var1, var1, 3, 0.0F, 0.0F, 0);
               var3.onTouchEvent(var4);
               var4.recycle();
               this.mForwarding = true;
            }
         }
      }
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      boolean var6 = this.mForwarding;
      boolean var5 = true;
      boolean var3;
      boolean var4;
      if (var6) {
         if (!this.onTouchForwarded(var2) && this.onForwardingStopped()) {
            var3 = false;
         } else {
            var3 = true;
         }

         var4 = var3;
      } else {
         if (this.onTouchObserved(var2) && this.onForwardingStarted()) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4 = var3;
         if (var3) {
            long var7 = SystemClock.uptimeMillis();
            MotionEvent var9 = MotionEvent.obtain(var7, var7, 3, 0.0F, 0.0F, 0);
            this.mSrc.onTouchEvent(var9);
            var9.recycle();
            var4 = var3;
         }
      }

      this.mForwarding = var4;
      var3 = var5;
      if (!var4) {
         if (var6) {
            return true;
         }

         var3 = false;
      }

      return var3;
   }

   public void onViewAttachedToWindow(View var1) {
   }

   public void onViewDetachedFromWindow(View var1) {
      this.mForwarding = false;
      this.mActivePointerId = -1;
      Runnable var2 = this.mDisallowIntercept;
      if (var2 != null) {
         this.mSrc.removeCallbacks(var2);
      }

   }

   private class DisallowIntercept implements Runnable {
      DisallowIntercept() {
      }

      public void run() {
         ViewParent var1 = ForwardingListener.this.mSrc.getParent();
         if (var1 != null) {
            var1.requestDisallowInterceptTouchEvent(true);
         }

      }
   }

   private class TriggerLongPress implements Runnable {
      TriggerLongPress() {
      }

      public void run() {
         ForwardingListener.this.onLongPress();
      }
   }
}
