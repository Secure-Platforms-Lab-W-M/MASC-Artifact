package android.support.design.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R$attr;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(AppBarLayout.Behavior.class)
public class AppBarLayout extends LinearLayout {
   private static final int INVALID_SCROLL_RANGE = -1;
   static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
   static final int PENDING_ACTION_COLLAPSED = 2;
   static final int PENDING_ACTION_EXPANDED = 1;
   static final int PENDING_ACTION_FORCE = 8;
   static final int PENDING_ACTION_NONE = 0;
   private boolean mCollapsed;
   private boolean mCollapsible;
   private int mDownPreScrollRange;
   private int mDownScrollRange;
   private boolean mHaveChildWithInterpolator;
   private WindowInsetsCompat mLastInsets;
   private List mListeners;
   private int mPendingAction;
   private int[] mTmpStatesArray;
   private int mTotalScrollRange;

   public AppBarLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppBarLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mTotalScrollRange = -1;
      this.mDownPreScrollRange = -1;
      this.mDownScrollRange = -1;
      this.mPendingAction = 0;
      this.setOrientation(1);
      ThemeUtils.checkAppCompatTheme(var1);
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
         ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, var2, 0, R$style.Widget_Design_AppBarLayout);
      }

      TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.AppBarLayout, 0, R$style.Widget_Design_AppBarLayout);
      ViewCompat.setBackground(this, var3.getDrawable(R$styleable.AppBarLayout_android_background));
      if (var3.hasValue(R$styleable.AppBarLayout_expanded)) {
         this.setExpanded(var3.getBoolean(R$styleable.AppBarLayout_expanded, false), false, false);
      }

      if (VERSION.SDK_INT >= 21 && var3.hasValue(R$styleable.AppBarLayout_elevation)) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, (float)var3.getDimensionPixelSize(R$styleable.AppBarLayout_elevation, 0));
      }

      if (VERSION.SDK_INT >= 26) {
         if (var3.hasValue(R$styleable.AppBarLayout_android_keyboardNavigationCluster)) {
            this.setKeyboardNavigationCluster(var3.getBoolean(R$styleable.AppBarLayout_android_keyboardNavigationCluster, false));
         }

         if (var3.hasValue(R$styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
            this.setTouchscreenBlocksFocus(var3.getBoolean(R$styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
         }
      }

      var3.recycle();
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            return AppBarLayout.this.onWindowInsetChanged(var2);
         }
      });
   }

   private void invalidateScrollRanges() {
      this.mTotalScrollRange = -1;
      this.mDownPreScrollRange = -1;
      this.mDownScrollRange = -1;
   }

   private boolean setCollapsibleState(boolean var1) {
      if (this.mCollapsible != var1) {
         this.mCollapsible = var1;
         this.refreshDrawableState();
         return true;
      } else {
         return false;
      }
   }

   private void setExpanded(boolean var1, boolean var2, boolean var3) {
      byte var4;
      if (var1) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      byte var6 = 0;
      byte var5;
      if (var2) {
         var5 = 4;
      } else {
         var5 = 0;
      }

      if (var3) {
         var6 = 8;
      }

      this.mPendingAction = var4 | var5 | var6;
      this.requestLayout();
   }

   private void updateCollapsible() {
      boolean var4 = false;
      int var1 = 0;
      int var2 = this.getChildCount();

      boolean var3;
      while(true) {
         var3 = var4;
         if (var1 >= var2) {
            break;
         }

         if (((AppBarLayout.LayoutParams)this.getChildAt(var1).getLayoutParams()).isCollapsible()) {
            var3 = true;
            break;
         }

         ++var1;
      }

      this.setCollapsibleState(var3);
   }

   public void addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      if (var1 != null && !this.mListeners.contains(var1)) {
         this.mListeners.add(var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof AppBarLayout.LayoutParams;
   }

   void dispatchOffsetUpdates(int var1) {
      List var4 = this.mListeners;
      if (var4 != null) {
         int var2 = 0;

         for(int var3 = var4.size(); var2 < var3; ++var2) {
            AppBarLayout.OnOffsetChangedListener var5 = (AppBarLayout.OnOffsetChangedListener)this.mListeners.get(var2);
            if (var5 != null) {
               var5.onOffsetChanged(this, var1);
            }
         }

      }
   }

   protected AppBarLayout.LayoutParams generateDefaultLayoutParams() {
      return new AppBarLayout.LayoutParams(-1, -2);
   }

   public AppBarLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new AppBarLayout.LayoutParams(this.getContext(), var1);
   }

   protected AppBarLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (VERSION.SDK_INT >= 19 && var1 instanceof android.widget.LinearLayout.LayoutParams) {
         return new AppBarLayout.LayoutParams((android.widget.LinearLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new AppBarLayout.LayoutParams((MarginLayoutParams)var1) : new AppBarLayout.LayoutParams(var1);
      }
   }

   int getDownNestedPreScrollRange() {
      int var1 = this.mDownPreScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;

         for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            View var5 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var6 = (AppBarLayout.LayoutParams)var5.getLayoutParams();
            int var3 = var5.getMeasuredHeight();
            int var4 = var6.mScrollFlags;
            if ((var4 & 5) == 5) {
               var1 += var6.topMargin + var6.bottomMargin;
               if ((var4 & 8) != 0) {
                  var1 += ViewCompat.getMinimumHeight(var5);
               } else if ((var4 & 2) != 0) {
                  var1 += var3 - ViewCompat.getMinimumHeight(var5);
               } else {
                  var1 += var3 - this.getTopInset();
               }
            } else if (var1 > 0) {
               break;
            }
         }

         var1 = Math.max(0, var1);
         this.mDownPreScrollRange = var1;
         return var1;
      }
   }

   int getDownNestedScrollRange() {
      int var1 = this.mDownScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;
         int var2 = 0;

         for(int var3 = this.getChildCount(); var2 < var3; ++var2) {
            View var8 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var9 = (AppBarLayout.LayoutParams)var8.getLayoutParams();
            int var5 = var8.getMeasuredHeight();
            int var6 = var9.topMargin;
            int var7 = var9.bottomMargin;
            int var4 = var9.mScrollFlags;
            if ((var4 & 1) == 0) {
               break;
            }

            var1 += var5 + var6 + var7;
            if ((var4 & 2) != 0) {
               var1 -= ViewCompat.getMinimumHeight(var8) + this.getTopInset();
               break;
            }
         }

         var1 = Math.max(0, var1);
         this.mDownScrollRange = var1;
         return var1;
      }
   }

   final int getMinimumHeightForVisibleOverlappingContent() {
      int var2 = this.getTopInset();
      int var1 = ViewCompat.getMinimumHeight(this);
      if (var1 != 0) {
         return var1 * 2 + var2;
      } else {
         var1 = this.getChildCount();
         if (var1 >= 1) {
            var1 = ViewCompat.getMinimumHeight(this.getChildAt(var1 - 1));
         } else {
            var1 = 0;
         }

         return var1 != 0 ? var1 * 2 + var2 : this.getHeight() / 3;
      }
   }

   int getPendingAction() {
      return this.mPendingAction;
   }

   @Deprecated
   public float getTargetElevation() {
      return 0.0F;
   }

   @VisibleForTesting
   final int getTopInset() {
      WindowInsetsCompat var1 = this.mLastInsets;
      return var1 != null ? var1.getSystemWindowInsetTop() : 0;
   }

   public final int getTotalScrollRange() {
      int var1 = this.mTotalScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;
         int var2 = 0;

         for(int var3 = this.getChildCount(); var2 < var3; ++var2) {
            View var6 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var7 = (AppBarLayout.LayoutParams)var6.getLayoutParams();
            int var5 = var6.getMeasuredHeight();
            int var4 = var7.mScrollFlags;
            if ((var4 & 1) == 0) {
               break;
            }

            var1 += var7.topMargin + var5 + var7.bottomMargin;
            if ((var4 & 2) != 0) {
               var1 -= ViewCompat.getMinimumHeight(var6);
               break;
            }
         }

         var1 = Math.max(0, var1 - this.getTopInset());
         this.mTotalScrollRange = var1;
         return var1;
      }
   }

   int getUpNestedPreScrollRange() {
      return this.getTotalScrollRange();
   }

   boolean hasChildWithInterpolator() {
      return this.mHaveChildWithInterpolator;
   }

   boolean hasScrollableChildren() {
      return this.getTotalScrollRange() != 0;
   }

   protected int[] onCreateDrawableState(int var1) {
      if (this.mTmpStatesArray == null) {
         this.mTmpStatesArray = new int[2];
      }

      int[] var2 = this.mTmpStatesArray;
      int[] var3 = super.onCreateDrawableState(var2.length + var1);
      if (this.mCollapsible) {
         var1 = R$attr.state_collapsible;
      } else {
         var1 = -R$attr.state_collapsible;
      }

      var2[0] = var1;
      if (this.mCollapsible && this.mCollapsed) {
         var1 = R$attr.state_collapsed;
      } else {
         var1 = -R$attr.state_collapsed;
      }

      var2[1] = var1;
      return mergeDrawableStates(var3, var2);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.invalidateScrollRanges();
      this.mHaveChildWithInterpolator = false;
      var2 = 0;

      for(var3 = this.getChildCount(); var2 < var3; ++var2) {
         if (((AppBarLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).getScrollInterpolator() != null) {
            this.mHaveChildWithInterpolator = true;
            break;
         }
      }

      this.updateCollapsible();
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      this.invalidateScrollRanges();
   }

   WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat var1) {
      WindowInsetsCompat var2 = null;
      if (ViewCompat.getFitsSystemWindows(this)) {
         var2 = var1;
      }

      if (!ObjectsCompat.equals(this.mLastInsets, var2)) {
         this.mLastInsets = var2;
         this.invalidateScrollRanges();
         return var1;
      } else {
         return var1;
      }
   }

   public void removeOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      List var2 = this.mListeners;
      if (var2 != null && var1 != null) {
         var2.remove(var1);
      }
   }

   void resetPendingAction() {
      this.mPendingAction = 0;
   }

   boolean setCollapsedState(boolean var1) {
      if (this.mCollapsed != var1) {
         this.mCollapsed = var1;
         this.refreshDrawableState();
         return true;
      } else {
         return false;
      }
   }

   public void setExpanded(boolean var1) {
      this.setExpanded(var1, ViewCompat.isLaidOut(this));
   }

   public void setExpanded(boolean var1, boolean var2) {
      this.setExpanded(var1, var2, true);
   }

   public void setOrientation(int var1) {
      if (var1 == 1) {
         super.setOrientation(var1);
      } else {
         throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
      }
   }

   @Deprecated
   public void setTargetElevation(float var1) {
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, var1);
      }
   }

   public static class Behavior extends HeaderBehavior {
      private static final int INVALID_POSITION = -1;
      private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
      private WeakReference mLastNestedScrollingChildRef;
      private ValueAnimator mOffsetAnimator;
      private int mOffsetDelta;
      private int mOffsetToChildIndexOnLayout = -1;
      private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
      private float mOffsetToChildIndexOnLayoutPerc;
      private AppBarLayout.Behavior.DragCallback mOnDragCallback;

      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      private void animateOffsetTo(CoordinatorLayout var1, AppBarLayout var2, int var3, float var4) {
         int var5 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - var3);
         var4 = Math.abs(var4);
         if (var4 > 0.0F) {
            var5 = Math.round((float)var5 / var4 * 1000.0F) * 3;
         } else {
            var5 = (int)((1.0F + (float)var5 / (float)var2.getHeight()) * 150.0F);
         }

         this.animateOffsetWithDuration(var1, var2, var3, var5);
      }

      private void animateOffsetWithDuration(final CoordinatorLayout var1, final AppBarLayout var2, int var3, int var4) {
         int var5 = this.getTopBottomOffsetForScrollingSibling();
         if (var5 == var3) {
            ValueAnimator var7 = this.mOffsetAnimator;
            if (var7 != null && var7.isRunning()) {
               this.mOffsetAnimator.cancel();
            }
         } else {
            ValueAnimator var6 = this.mOffsetAnimator;
            if (var6 == null) {
               this.mOffsetAnimator = new ValueAnimator();
               this.mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
               this.mOffsetAnimator.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1x) {
                     Behavior.this.setHeaderTopBottomOffset(var1, var2, (Integer)var1x.getAnimatedValue());
                  }
               });
            } else {
               var6.cancel();
            }

            this.mOffsetAnimator.setDuration((long)Math.min(var4, 600));
            this.mOffsetAnimator.setIntValues(new int[]{var5, var3});
            this.mOffsetAnimator.start();
         }
      }

      private static boolean checkFlag(int var0, int var1) {
         return (var0 & var1) == var1;
      }

      private static View getAppBarChildOnOffset(AppBarLayout var0, int var1) {
         int var2 = Math.abs(var1);
         var1 = 0;

         for(int var3 = var0.getChildCount(); var1 < var3; ++var1) {
            View var4 = var0.getChildAt(var1);
            if (var2 >= var4.getTop() && var2 <= var4.getBottom()) {
               return var4;
            }
         }

         return null;
      }

      private int getChildIndexOnOffset(AppBarLayout var1, int var2) {
         int var3 = 0;

         for(int var4 = var1.getChildCount(); var3 < var4; ++var3) {
            View var5 = var1.getChildAt(var3);
            if (var5.getTop() <= -var2 && var5.getBottom() >= -var2) {
               return var3;
            }
         }

         return -1;
      }

      private int interpolateOffset(AppBarLayout var1, int var2) {
         int var4 = Math.abs(var2);
         int var3 = 0;

         for(int var5 = var1.getChildCount(); var3 < var5; ++var3) {
            View var6 = var1.getChildAt(var3);
            AppBarLayout.LayoutParams var7 = (AppBarLayout.LayoutParams)var6.getLayoutParams();
            Interpolator var8 = var7.getScrollInterpolator();
            if (var4 >= var6.getTop() && var4 <= var6.getBottom()) {
               if (var8 != null) {
                  var3 = 0;
                  var5 = var7.getScrollFlags();
                  if ((var5 & 1) != 0) {
                     var3 = 0 + var6.getHeight() + var7.topMargin + var7.bottomMargin;
                     if ((var5 & 2) != 0) {
                        var3 -= ViewCompat.getMinimumHeight(var6);
                     }
                  }

                  if (ViewCompat.getFitsSystemWindows(var6)) {
                     var3 -= var1.getTopInset();
                  }

                  if (var3 > 0) {
                     var5 = var6.getTop();
                     var3 = Math.round((float)var3 * var8.getInterpolation((float)(var4 - var5) / (float)var3));
                     return Integer.signum(var2) * (var6.getTop() + var3);
                  }

                  return var2;
               }

               return var2;
            }
         }

         return var2;
      }

      private boolean shouldJumpElevationState(CoordinatorLayout var1, AppBarLayout var2) {
         List var6 = var1.getDependents(var2);
         int var3 = 0;
         int var4 = var6.size();

         while(true) {
            boolean var5 = false;
            if (var3 >= var4) {
               return false;
            }

            CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)((View)var6.get(var3)).getLayoutParams()).getBehavior();
            if (var7 instanceof AppBarLayout.ScrollingViewBehavior) {
               if (((AppBarLayout.ScrollingViewBehavior)var7).getOverlayTop() != 0) {
                  var5 = true;
               }

               return var5;
            }

            ++var3;
         }
      }

      private void snapToChildIfNeeded(CoordinatorLayout var1, AppBarLayout var2) {
         int var6 = this.getTopBottomOffsetForScrollingSibling();
         int var7 = this.getChildIndexOnOffset(var2, var6);
         if (var7 >= 0) {
            View var8 = var2.getChildAt(var7);
            int var4 = ((AppBarLayout.LayoutParams)var8.getLayoutParams()).getScrollFlags();
            if ((var4 & 17) == 17) {
               int var5 = -var8.getTop();
               int var3 = -var8.getBottom();
               if (var7 == var2.getChildCount() - 1) {
                  var3 += var2.getTopInset();
               }

               if (checkFlag(var4, 2)) {
                  var3 += ViewCompat.getMinimumHeight(var8);
                  var4 = var5;
               } else if (checkFlag(var4, 5)) {
                  var4 = ViewCompat.getMinimumHeight(var8) + var3;
                  if (var6 >= var4) {
                     var3 = var4;
                     var4 = var5;
                  }
               } else {
                  var4 = var5;
               }

               if (var6 >= (var3 + var4) / 2) {
                  var3 = var4;
               }

               this.animateOffsetTo(var1, var2, MathUtils.clamp(var3, -var2.getTotalScrollRange(), 0), 0.0F);
            }
         }
      }

      private void updateAppBarLayoutDrawableState(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, boolean var5) {
         View var11 = getAppBarChildOnOffset(var2, var3);
         if (var11 != null) {
            int var6 = ((AppBarLayout.LayoutParams)var11.getLayoutParams()).getScrollFlags();
            boolean var8 = false;
            if ((var6 & 1) != 0) {
               int var7 = ViewCompat.getMinimumHeight(var11);
               boolean var10 = false;
               boolean var9 = false;
               if (var4 > 0 && (var6 & 12) != 0) {
                  var8 = var9;
                  if (-var3 >= var11.getBottom() - var7 - var2.getTopInset()) {
                     var8 = true;
                  }
               } else if ((var6 & 2) != 0) {
                  var8 = var10;
                  if (-var3 >= var11.getBottom() - var7 - var2.getTopInset()) {
                     var8 = true;
                  }
               }
            }

            label36: {
               var8 = var2.setCollapsedState(var8);
               if (VERSION.SDK_INT >= 11) {
                  if (var5) {
                     break label36;
                  }

                  if (var8) {
                     if (!this.shouldJumpElevationState(var1, var2)) {
                        return;
                     }
                     break label36;
                  }
               }

               return;
            }

            var2.jumpDrawablesToCurrentState();
         }
      }

      boolean canDragView(AppBarLayout var1) {
         AppBarLayout.Behavior.DragCallback var2 = this.mOnDragCallback;
         if (var2 != null) {
            return var2.canDrag(var1);
         } else {
            WeakReference var3 = this.mLastNestedScrollingChildRef;
            if (var3 != null) {
               View var4 = (View)var3.get();
               return var4 != null && var4.isShown() && !var4.canScrollVertically(-1);
            } else {
               return true;
            }
         }
      }

      int getMaxDragOffset(AppBarLayout var1) {
         return -var1.getDownNestedScrollRange();
      }

      int getScrollRangeForDragFling(AppBarLayout var1) {
         return var1.getTotalScrollRange();
      }

      int getTopBottomOffsetForScrollingSibling() {
         return this.getTopAndBottomOffset() + this.mOffsetDelta;
      }

      @VisibleForTesting
      boolean isOffsetAnimatorRunning() {
         ValueAnimator var1 = this.mOffsetAnimator;
         return var1 != null && var1.isRunning();
      }

      void onFlingFinished(CoordinatorLayout var1, AppBarLayout var2) {
         this.snapToChildIfNeeded(var1, var2);
      }

      public boolean onLayoutChild(CoordinatorLayout var1, AppBarLayout var2, int var3) {
         boolean var5 = super.onLayoutChild(var1, var2, var3);
         int var4 = var2.getPendingAction();
         var3 = this.mOffsetToChildIndexOnLayout;
         if (var3 >= 0 && (var4 & 8) == 0) {
            View var6 = var2.getChildAt(var3);
            var3 = -var6.getBottom();
            if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
               var3 += ViewCompat.getMinimumHeight(var6) + var2.getTopInset();
            } else {
               var3 += Math.round((float)var6.getHeight() * this.mOffsetToChildIndexOnLayoutPerc);
            }

            this.setHeaderTopBottomOffset(var1, var2, var3);
         } else if (var4 != 0) {
            boolean var7;
            if ((var4 & 4) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if ((var4 & 2) != 0) {
               var4 = -var2.getUpNestedPreScrollRange();
               if (var7) {
                  this.animateOffsetTo(var1, var2, var4, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, var4);
               }
            } else if ((var4 & 1) != 0) {
               if (var7) {
                  this.animateOffsetTo(var1, var2, 0, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, 0);
               }
            }
         }

         var2.resetPendingAction();
         this.mOffsetToChildIndexOnLayout = -1;
         this.setTopAndBottomOffset(MathUtils.clamp(this.getTopAndBottomOffset(), -var2.getTotalScrollRange(), 0));
         this.updateAppBarLayoutDrawableState(var1, var2, this.getTopAndBottomOffset(), 0, true);
         var2.dispatchOffsetUpdates(this.getTopAndBottomOffset());
         return var5;
      }

      public boolean onMeasureChild(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5, int var6) {
         if (((CoordinatorLayout.LayoutParams)var2.getLayoutParams()).height == -2) {
            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(0, 0), var6);
            return true;
         } else {
            return super.onMeasureChild(var1, var2, var3, var4, var5, var6);
         }
      }

      public void onNestedPreScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int[] var6, int var7) {
         if (var5 != 0) {
            if (var5 < 0) {
               var7 = -var2.getTotalScrollRange();
               int var8 = var2.getDownNestedPreScrollRange();
               var4 = var7;
               var7 += var8;
            } else {
               var4 = -var2.getUpNestedPreScrollRange();
               var7 = 0;
            }

            if (var4 != var7) {
               var6[1] = this.scroll(var1, var2, var5, var4, var7);
            }
         }
      }

      public void onNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int var6, int var7, int var8) {
         if (var7 < 0) {
            this.scroll(var1, var2, var7, -var2.getDownNestedScrollRange(), 0);
         }
      }

      public void onRestoreInstanceState(CoordinatorLayout var1, AppBarLayout var2, Parcelable var3) {
         if (var3 instanceof AppBarLayout.Behavior.SavedState) {
            AppBarLayout.Behavior.SavedState var4 = (AppBarLayout.Behavior.SavedState)var3;
            super.onRestoreInstanceState(var1, var2, var4.getSuperState());
            this.mOffsetToChildIndexOnLayout = var4.firstVisibleChildIndex;
            this.mOffsetToChildIndexOnLayoutPerc = var4.firstVisibleChildPercentageShown;
            this.mOffsetToChildIndexOnLayoutIsMinHeight = var4.firstVisibleChildAtMinimumHeight;
         } else {
            super.onRestoreInstanceState(var1, var2, var3);
            this.mOffsetToChildIndexOnLayout = -1;
         }
      }

      public Parcelable onSaveInstanceState(CoordinatorLayout var1, AppBarLayout var2) {
         Parcelable var8 = super.onSaveInstanceState(var1, var2);
         int var4 = this.getTopAndBottomOffset();
         int var3 = 0;

         for(int var5 = var2.getChildCount(); var3 < var5; ++var3) {
            View var9 = var2.getChildAt(var3);
            int var6 = var9.getBottom() + var4;
            if (var9.getTop() + var4 <= 0 && var6 >= 0) {
               AppBarLayout.Behavior.SavedState var10 = new AppBarLayout.Behavior.SavedState(var8);
               var10.firstVisibleChildIndex = var3;
               boolean var7;
               if (var6 == ViewCompat.getMinimumHeight(var9) + var2.getTopInset()) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               var10.firstVisibleChildAtMinimumHeight = var7;
               var10.firstVisibleChildPercentageShown = (float)var6 / (float)var9.getHeight();
               return var10;
            }
         }

         return var8;
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, View var4, int var5, int var6) {
         boolean var7;
         if ((var5 & 2) != 0 && var2.hasScrollableChildren() && var1.getHeight() - var3.getHeight() <= var2.getHeight()) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var7) {
            ValueAnimator var8 = this.mOffsetAnimator;
            if (var8 != null) {
               var8.cancel();
            }
         }

         this.mLastNestedScrollingChildRef = null;
         return var7;
      }

      public void onStopNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4) {
         if (var4 == 0) {
            this.snapToChildIfNeeded(var1, var2);
         }

         this.mLastNestedScrollingChildRef = new WeakReference(var3);
      }

      public void setDragCallback(@Nullable AppBarLayout.Behavior.DragCallback var1) {
         this.mOnDragCallback = var1;
      }

      int setHeaderTopBottomOffset(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5) {
         int var6 = this.getTopBottomOffsetForScrollingSibling();
         if (var4 != 0 && var6 >= var4 && var6 <= var5) {
            var4 = MathUtils.clamp(var3, var4, var5);
            if (var6 != var4) {
               if (var2.hasChildWithInterpolator()) {
                  var3 = this.interpolateOffset(var2, var4);
               } else {
                  var3 = var4;
               }

               boolean var7 = this.setTopAndBottomOffset(var3);
               this.mOffsetDelta = var4 - var3;
               if (!var7 && var2.hasChildWithInterpolator()) {
                  var1.dispatchDependentViewsChanged(var2);
               }

               var2.dispatchOffsetUpdates(this.getTopAndBottomOffset());
               byte var8;
               if (var4 < var6) {
                  var8 = -1;
               } else {
                  var8 = 1;
               }

               this.updateAppBarLayoutDrawableState(var1, var2, var4, var8, false);
               return var6 - var4;
            } else {
               return 0;
            }
         } else {
            this.mOffsetDelta = 0;
            return 0;
         }
      }

      public abstract static class DragCallback {
         public abstract boolean canDrag(@NonNull AppBarLayout var1);
      }

      protected static class SavedState extends AbsSavedState {
         public static final Creator CREATOR = new ClassLoaderCreator() {
            public AppBarLayout.Behavior.SavedState createFromParcel(Parcel var1) {
               return new AppBarLayout.Behavior.SavedState(var1, (ClassLoader)null);
            }

            public AppBarLayout.Behavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
               return new AppBarLayout.Behavior.SavedState(var1, var2);
            }

            public AppBarLayout.Behavior.SavedState[] newArray(int var1) {
               return new AppBarLayout.Behavior.SavedState[var1];
            }
         };
         boolean firstVisibleChildAtMinimumHeight;
         int firstVisibleChildIndex;
         float firstVisibleChildPercentageShown;

         public SavedState(Parcel var1, ClassLoader var2) {
            super(var1, var2);
            this.firstVisibleChildIndex = var1.readInt();
            this.firstVisibleChildPercentageShown = var1.readFloat();
            boolean var3;
            if (var1.readByte() != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.firstVisibleChildAtMinimumHeight = var3;
         }

         public SavedState(Parcelable var1) {
            super(var1);
         }

         public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.firstVisibleChildIndex);
            var1.writeFloat(this.firstVisibleChildPercentageShown);
            var1.writeByte((byte)this.firstVisibleChildAtMinimumHeight);
         }
      }
   }

   public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
      static final int COLLAPSIBLE_FLAGS = 10;
      static final int FLAG_QUICK_RETURN = 5;
      static final int FLAG_SNAP = 17;
      public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
      public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
      public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
      public static final int SCROLL_FLAG_SCROLL = 1;
      public static final int SCROLL_FLAG_SNAP = 16;
      int mScrollFlags = 1;
      Interpolator mScrollInterpolator;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(int var1, int var2, float var3) {
         super(var1, var2, var3);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.AppBarLayout_Layout);
         this.mScrollFlags = var3.getInt(R$styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
         if (var3.hasValue(R$styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
            this.mScrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(var1, var3.getResourceId(R$styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
         }

         var3.recycle();
      }

      @RequiresApi(19)
      public LayoutParams(AppBarLayout.LayoutParams var1) {
         super(var1);
         this.mScrollFlags = var1.mScrollFlags;
         this.mScrollInterpolator = var1.mScrollInterpolator;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      @RequiresApi(19)
      public LayoutParams(android.widget.LinearLayout.LayoutParams var1) {
         super(var1);
      }

      public int getScrollFlags() {
         return this.mScrollFlags;
      }

      public Interpolator getScrollInterpolator() {
         return this.mScrollInterpolator;
      }

      boolean isCollapsible() {
         int var1 = this.mScrollFlags;
         return (var1 & 1) == 1 && (var1 & 10) != 0;
      }

      public void setScrollFlags(int var1) {
         this.mScrollFlags = var1;
      }

      public void setScrollInterpolator(Interpolator var1) {
         this.mScrollInterpolator = var1;
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public @interface ScrollFlags {
      }
   }

   public interface OnOffsetChangedListener {
      void onOffsetChanged(AppBarLayout var1, int var2);
   }

   public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
      public ScrollingViewBehavior() {
      }

      public ScrollingViewBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.ScrollingViewBehavior_Layout);
         this.setOverlayTop(var3.getDimensionPixelSize(R$styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
         var3.recycle();
      }

      private static int getAppBarLayoutOffset(AppBarLayout var0) {
         CoordinatorLayout.Behavior var1 = ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).getBehavior();
         return var1 instanceof AppBarLayout.Behavior ? ((AppBarLayout.Behavior)var1).getTopBottomOffsetForScrollingSibling() : 0;
      }

      private void offsetChildAsNeeded(CoordinatorLayout var1, View var2, View var3) {
         CoordinatorLayout.Behavior var4 = ((CoordinatorLayout.LayoutParams)var3.getLayoutParams()).getBehavior();
         if (var4 instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior var5 = (AppBarLayout.Behavior)var4;
            ViewCompat.offsetTopAndBottom(var2, var3.getBottom() - var2.getTop() + var5.mOffsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(var3));
         }
      }

      AppBarLayout findFirstDependency(List var1) {
         int var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            View var4 = (View)var1.get(var2);
            if (var4 instanceof AppBarLayout) {
               return (AppBarLayout)var4;
            }
         }

         return null;
      }

      float getOverlapRatioForOffset(View var1) {
         if (var1 instanceof AppBarLayout) {
            AppBarLayout var5 = (AppBarLayout)var1;
            int var3 = var5.getTotalScrollRange();
            int var4 = var5.getDownNestedPreScrollRange();
            int var2 = getAppBarLayoutOffset(var5);
            if (var4 != 0 && var3 + var2 <= var4) {
               return 0.0F;
            } else {
               var3 -= var4;
               return var3 != 0 ? (float)var2 / (float)var3 + 1.0F : 0.0F;
            }
         } else {
            return 0.0F;
         }
      }

      int getScrollRange(View var1) {
         return var1 instanceof AppBarLayout ? ((AppBarLayout)var1).getTotalScrollRange() : super.getScrollRange(var1);
      }

      public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
         return var3 instanceof AppBarLayout;
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, View var2, View var3) {
         this.offsetChildAsNeeded(var1, var2, var3);
         return false;
      }

      public boolean onRequestChildRectangleOnScreen(CoordinatorLayout var1, View var2, Rect var3, boolean var4) {
         AppBarLayout var5 = this.findFirstDependency(var1.getDependencies(var2));
         if (var5 != null) {
            var3.offset(var2.getLeft(), var2.getTop());
            Rect var6 = this.mTempRect1;
            var6.set(0, 0, var1.getWidth(), var1.getHeight());
            if (!var6.contains(var3)) {
               var5.setExpanded(false, var4 ^ true);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
