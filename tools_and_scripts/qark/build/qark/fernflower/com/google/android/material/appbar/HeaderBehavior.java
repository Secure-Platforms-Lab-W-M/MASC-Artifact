package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

abstract class HeaderBehavior extends ViewOffsetBehavior {
   private static final int INVALID_POINTER = -1;
   private int activePointerId = -1;
   private Runnable flingRunnable;
   private boolean isBeingDragged;
   private int lastMotionY;
   OverScroller scroller;
   private int touchSlop = -1;
   private VelocityTracker velocityTracker;

   public HeaderBehavior() {
   }

   public HeaderBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void ensureVelocityTracker() {
      if (this.velocityTracker == null) {
         this.velocityTracker = VelocityTracker.obtain();
      }

   }

   boolean canDragView(View var1) {
      return false;
   }

   final boolean fling(CoordinatorLayout var1, View var2, int var3, int var4, float var5) {
      Runnable var6 = this.flingRunnable;
      if (var6 != null) {
         var2.removeCallbacks(var6);
         this.flingRunnable = null;
      }

      if (this.scroller == null) {
         this.scroller = new OverScroller(var2.getContext());
      }

      this.scroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(var5), 0, 0, var3, var4);
      if (this.scroller.computeScrollOffset()) {
         HeaderBehavior.FlingRunnable var7 = new HeaderBehavior.FlingRunnable(var1, var2);
         this.flingRunnable = var7;
         ViewCompat.postOnAnimation(var2, var7);
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
      if (this.touchSlop < 0) {
         this.touchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      if (var3.getAction() == 2 && this.isBeingDragged) {
         return true;
      } else {
         int var4 = var3.getActionMasked();
         VelocityTracker var6;
         if (var4 != 0) {
            label43: {
               if (var4 != 1) {
                  if (var4 == 2) {
                     var4 = this.activePointerId;
                     if (var4 != -1) {
                        var4 = var3.findPointerIndex(var4);
                        if (var4 != -1) {
                           var4 = (int)var3.getY(var4);
                           if (Math.abs(var4 - this.lastMotionY) > this.touchSlop) {
                              this.isBeingDragged = true;
                              this.lastMotionY = var4;
                           }
                        }
                     }
                     break label43;
                  }

                  if (var4 != 3) {
                     break label43;
                  }
               }

               this.isBeingDragged = false;
               this.activePointerId = -1;
               var6 = this.velocityTracker;
               if (var6 != null) {
                  var6.recycle();
                  this.velocityTracker = null;
               }
            }
         } else {
            this.isBeingDragged = false;
            var4 = (int)var3.getX();
            int var5 = (int)var3.getY();
            if (this.canDragView(var2) && var1.isPointInChildBounds(var2, var4, var5)) {
               this.lastMotionY = var5;
               this.activePointerId = var3.getPointerId(0);
               this.ensureVelocityTracker();
            }
         }

         var6 = this.velocityTracker;
         if (var6 != null) {
            var6.addMovement(var3);
         }

         return this.isBeingDragged;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (this.touchSlop < 0) {
         this.touchSlop = ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      }

      int var5 = var3.getActionMasked();
      int var6;
      VelocityTracker var11;
      if (var5 != 0) {
         label48: {
            if (var5 != 1) {
               if (var5 == 2) {
                  var5 = var3.findPointerIndex(this.activePointerId);
                  if (var5 == -1) {
                     return false;
                  }

                  int var7 = (int)var3.getY(var5);
                  var6 = this.lastMotionY - var7;
                  var5 = var6;
                  if (!this.isBeingDragged) {
                     int var8 = Math.abs(var6);
                     int var9 = this.touchSlop;
                     var5 = var6;
                     if (var8 > var9) {
                        this.isBeingDragged = true;
                        if (var6 > 0) {
                           var5 = var6 - var9;
                        } else {
                           var5 = var6 + var9;
                        }
                     }
                  }

                  if (this.isBeingDragged) {
                     this.lastMotionY = var7;
                     this.scroll(var1, var2, var5, this.getMaxDragOffset(var2), 0);
                  }
                  break label48;
               }

               if (var5 != 3) {
                  break label48;
               }
            } else {
               VelocityTracker var10 = this.velocityTracker;
               if (var10 != null) {
                  var10.addMovement(var3);
                  this.velocityTracker.computeCurrentVelocity(1000);
                  float var4 = this.velocityTracker.getYVelocity(this.activePointerId);
                  this.fling(var1, var2, -this.getScrollRangeForDragFling(var2), 0, var4);
               }
            }

            this.isBeingDragged = false;
            this.activePointerId = -1;
            var11 = this.velocityTracker;
            if (var11 != null) {
               var11.recycle();
               this.velocityTracker = null;
            }
         }
      } else {
         var5 = (int)var3.getX();
         var6 = (int)var3.getY();
         if (!var1.isPointInChildBounds(var2, var5, var6) || !this.canDragView(var2)) {
            return false;
         }

         this.lastMotionY = var6;
         this.activePointerId = var3.getPointerId(0);
         this.ensureVelocityTracker();
      }

      var11 = this.velocityTracker;
      if (var11 != null) {
         var11.addMovement(var3);
      }

      return true;
   }

   final int scroll(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      return this.setHeaderTopBottomOffset(var1, var2, this.getTopBottomOffsetForScrollingSibling() - var3, var4, var5);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3) {
      return this.setHeaderTopBottomOffset(var1, var2, var3, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   int setHeaderTopBottomOffset(CoordinatorLayout var1, View var2, int var3, int var4, int var5) {
      int var8 = this.getTopAndBottomOffset();
      byte var7 = 0;
      int var6 = var7;
      if (var4 != 0) {
         var6 = var7;
         if (var8 >= var4) {
            var6 = var7;
            if (var8 <= var5) {
               var3 = MathUtils.clamp(var3, var4, var5);
               var6 = var7;
               if (var8 != var3) {
                  this.setTopAndBottomOffset(var3);
                  var6 = var8 - var3;
               }
            }
         }
      }

      return var6;
   }

   private class FlingRunnable implements Runnable {
      private final View layout;
      private final CoordinatorLayout parent;

      FlingRunnable(CoordinatorLayout var2, View var3) {
         this.parent = var2;
         this.layout = var3;
      }

      public void run() {
         if (this.layout != null && HeaderBehavior.this.scroller != null) {
            if (HeaderBehavior.this.scroller.computeScrollOffset()) {
               HeaderBehavior var1 = HeaderBehavior.this;
               var1.setHeaderTopBottomOffset(this.parent, this.layout, var1.scroller.getCurrY());
               ViewCompat.postOnAnimation(this.layout, this);
               return;
            }

            HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
         }

      }
   }
}
