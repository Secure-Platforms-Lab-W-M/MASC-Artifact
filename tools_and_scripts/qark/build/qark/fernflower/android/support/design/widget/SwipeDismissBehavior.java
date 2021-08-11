package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SwipeDismissBehavior extends CoordinatorLayout.Behavior {
   private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5F;
   private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0F;
   private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5F;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_IDLE = 0;
   public static final int STATE_SETTLING = 2;
   public static final int SWIPE_DIRECTION_ANY = 2;
   public static final int SWIPE_DIRECTION_END_TO_START = 1;
   public static final int SWIPE_DIRECTION_START_TO_END = 0;
   float mAlphaEndSwipeDistance = 0.5F;
   float mAlphaStartSwipeDistance = 0.0F;
   private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
      private static final int INVALID_POINTER_ID = -1;
      private int mActivePointerId = -1;
      private int mOriginalCapturedViewLeft;

      private boolean shouldDismiss(View var1, float var2) {
         boolean var7 = false;
         boolean var8 = false;
         boolean var6 = false;
         if (var2 != 0.0F) {
            boolean var9;
            if (ViewCompat.getLayoutDirection(var1) == 1) {
               var9 = true;
            } else {
               var9 = false;
            }

            if (SwipeDismissBehavior.this.mSwipeDirection == 2) {
               return true;
            } else if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
               if (var9) {
                  if (var2 >= 0.0F) {
                     return var6;
                  }
               } else if (var2 <= 0.0F) {
                  return var6;
               }

               var6 = true;
               return var6;
            } else if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
               return false;
            } else {
               if (var9) {
                  var6 = var7;
                  if (var2 <= 0.0F) {
                     return var6;
                  }
               } else {
                  var6 = var7;
                  if (var2 >= 0.0F) {
                     return var6;
                  }
               }

               var6 = true;
               return var6;
            }
         } else {
            int var3 = var1.getLeft();
            int var4 = this.mOriginalCapturedViewLeft;
            int var5 = Math.round((float)var1.getWidth() * SwipeDismissBehavior.this.mDragDismissThreshold);
            var6 = var8;
            if (Math.abs(var3 - var4) >= var5) {
               var6 = true;
            }

            return var6;
         }
      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         boolean var5;
         if (ViewCompat.getLayoutDirection(var1) == 1) {
            var5 = true;
         } else {
            var5 = false;
         }

         int var4;
         if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
            if (var5) {
               var3 = this.mOriginalCapturedViewLeft - var1.getWidth();
               var4 = this.mOriginalCapturedViewLeft;
            } else {
               var3 = this.mOriginalCapturedViewLeft;
               var4 = this.mOriginalCapturedViewLeft + var1.getWidth();
            }
         } else if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
            if (var5) {
               var3 = this.mOriginalCapturedViewLeft;
               var4 = this.mOriginalCapturedViewLeft + var1.getWidth();
            } else {
               var3 = this.mOriginalCapturedViewLeft - var1.getWidth();
               var4 = this.mOriginalCapturedViewLeft;
            }
         } else {
            var3 = this.mOriginalCapturedViewLeft - var1.getWidth();
            var4 = this.mOriginalCapturedViewLeft + var1.getWidth();
         }

         return SwipeDismissBehavior.clamp(var3, var2, var4);
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         return var1.getWidth();
      }

      public void onViewCaptured(View var1, int var2) {
         this.mActivePointerId = var2;
         this.mOriginalCapturedViewLeft = var1.getLeft();
         ViewParent var3 = var1.getParent();
         if (var3 != null) {
            var3.requestDisallowInterceptTouchEvent(true);
         }
      }

      public void onViewDragStateChanged(int var1) {
         if (SwipeDismissBehavior.this.mListener != null) {
            SwipeDismissBehavior.this.mListener.onDragStateChanged(var1);
         }
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         float var6 = (float)this.mOriginalCapturedViewLeft + (float)var1.getWidth() * SwipeDismissBehavior.this.mAlphaStartSwipeDistance;
         float var7 = (float)this.mOriginalCapturedViewLeft + (float)var1.getWidth() * SwipeDismissBehavior.this.mAlphaEndSwipeDistance;
         if ((float)var2 <= var6) {
            var1.setAlpha(1.0F);
         } else if ((float)var2 >= var7) {
            var1.setAlpha(0.0F);
         } else {
            var1.setAlpha(SwipeDismissBehavior.clamp(0.0F, 1.0F - SwipeDismissBehavior.fraction(var6, var7, (float)var2), 1.0F));
         }
      }

      public void onViewReleased(View var1, float var2, float var3) {
         this.mActivePointerId = -1;
         int var4 = var1.getWidth();
         boolean var7 = false;
         if (this.shouldDismiss(var1, var2)) {
            int var5 = var1.getLeft();
            int var6 = this.mOriginalCapturedViewLeft;
            if (var5 < var6) {
               var4 = var6 - var4;
            } else {
               var4 += var6;
            }

            var7 = true;
         } else {
            var4 = this.mOriginalCapturedViewLeft;
         }

         if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(var4, var1.getTop())) {
            ViewCompat.postOnAnimation(var1, SwipeDismissBehavior.this.new SettleRunnable(var1, var7));
         } else if (var7 && SwipeDismissBehavior.this.mListener != null) {
            SwipeDismissBehavior.this.mListener.onDismiss(var1);
         }
      }

      public boolean tryCaptureView(View var1, int var2) {
         return this.mActivePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(var1);
      }
   };
   float mDragDismissThreshold = 0.5F;
   private boolean mInterceptingEvents;
   SwipeDismissBehavior.OnDismissListener mListener;
   private float mSensitivity = 0.0F;
   private boolean mSensitivitySet;
   int mSwipeDirection = 2;
   ViewDragHelper mViewDragHelper;

   static float clamp(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   static int clamp(int var0, int var1, int var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   private void ensureViewDragHelper(ViewGroup var1) {
      if (this.mViewDragHelper == null) {
         ViewDragHelper var2;
         if (this.mSensitivitySet) {
            var2 = ViewDragHelper.create(var1, this.mSensitivity, this.mDragCallback);
         } else {
            var2 = ViewDragHelper.create(var1, this.mDragCallback);
         }

         this.mViewDragHelper = var2;
      }
   }

   static float fraction(float var0, float var1, float var2) {
      return (var2 - var0) / (var1 - var0);
   }

   public boolean canSwipeDismissView(@NonNull View var1) {
      return true;
   }

   public int getDragState() {
      ViewDragHelper var1 = this.mViewDragHelper;
      return var1 != null ? var1.getViewDragState() : 0;
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      boolean var5;
      label21: {
         var5 = this.mInterceptingEvents;
         int var4 = var3.getActionMasked();
         if (var4 != 3) {
            switch(var4) {
            case 0:
               this.mInterceptingEvents = var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY());
               var5 = this.mInterceptingEvents;
               break label21;
            case 1:
               break;
            default:
               break label21;
            }
         }

         this.mInterceptingEvents = false;
      }

      if (var5) {
         this.ensureViewDragHelper(var1);
         return this.mViewDragHelper.shouldInterceptTouchEvent(var3);
      } else {
         return false;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      ViewDragHelper var4 = this.mViewDragHelper;
      if (var4 != null) {
         var4.processTouchEvent(var3);
         return true;
      } else {
         return false;
      }
   }

   public void setDragDismissDistance(float var1) {
      this.mDragDismissThreshold = clamp(0.0F, var1, 1.0F);
   }

   public void setEndAlphaSwipeDistance(float var1) {
      this.mAlphaEndSwipeDistance = clamp(0.0F, var1, 1.0F);
   }

   public void setListener(SwipeDismissBehavior.OnDismissListener var1) {
      this.mListener = var1;
   }

   public void setSensitivity(float var1) {
      this.mSensitivity = var1;
      this.mSensitivitySet = true;
   }

   public void setStartAlphaSwipeDistance(float var1) {
      this.mAlphaStartSwipeDistance = clamp(0.0F, var1, 1.0F);
   }

   public void setSwipeDirection(int var1) {
      this.mSwipeDirection = var1;
   }

   public interface OnDismissListener {
      void onDismiss(View var1);

      void onDragStateChanged(int var1);
   }

   private class SettleRunnable implements Runnable {
      private final boolean mDismiss;
      private final View mView;

      SettleRunnable(View var2, boolean var3) {
         this.mView = var2;
         this.mDismiss = var3;
      }

      public void run() {
         if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
            ViewCompat.postOnAnimation(this.mView, this);
         } else if (this.mDismiss && SwipeDismissBehavior.this.mListener != null) {
            SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   private @interface SwipeDirection {
   }
}
