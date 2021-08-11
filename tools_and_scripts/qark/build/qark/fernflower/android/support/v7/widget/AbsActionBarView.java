package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

abstract class AbsActionBarView extends ViewGroup {
   private static final int FADE_DURATION = 200;
   protected ActionMenuPresenter mActionMenuPresenter;
   protected int mContentHeight;
   private boolean mEatingHover;
   private boolean mEatingTouch;
   protected ActionMenuView mMenuView;
   protected final Context mPopupContext;
   protected final AbsActionBarView.VisibilityAnimListener mVisAnimListener;
   protected ViewPropertyAnimatorCompat mVisibilityAnim;

   AbsActionBarView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   AbsActionBarView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   AbsActionBarView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mVisAnimListener = new AbsActionBarView.VisibilityAnimListener();
      TypedValue var4 = new TypedValue();
      if (var1.getTheme().resolveAttribute(R$attr.actionBarPopupTheme, var4, true) && var4.resourceId != 0) {
         this.mPopupContext = new ContextThemeWrapper(var1, var4.resourceId);
      } else {
         this.mPopupContext = var1;
      }
   }

   protected static int next(int var0, int var1, boolean var2) {
      return var2 ? var0 - var1 : var0 + var1;
   }

   public void animateToVisibility(int var1) {
      this.setupAnimatorToVisibility(var1, 200L).start();
   }

   public boolean canShowOverflowMenu() {
      return this.isOverflowReserved() && this.getVisibility() == 0;
   }

   public void dismissPopupMenus() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      if (var1 != null) {
         var1.dismissPopupMenus();
      }

   }

   public int getAnimatedVisibility() {
      return this.mVisibilityAnim != null ? this.mVisAnimListener.mFinalVisibility : this.getVisibility();
   }

   public int getContentHeight() {
      return this.mContentHeight;
   }

   public boolean hideOverflowMenu() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      return var1 != null ? var1.hideOverflowMenu() : false;
   }

   public boolean isOverflowMenuShowPending() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      return var1 != null ? var1.isOverflowMenuShowPending() : false;
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      return var1 != null ? var1.isOverflowMenuShowing() : false;
   }

   public boolean isOverflowReserved() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      return var1 != null && var1.isOverflowReserved();
   }

   protected int measureChildView(View var1, int var2, int var3, int var4) {
      var1.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), var3);
      return Math.max(0, var2 - var1.getMeasuredWidth() - var4);
   }

   protected void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      TypedArray var2 = this.getContext().obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      this.setContentHeight(var2.getLayoutDimension(R$styleable.ActionBar_height, 0));
      var2.recycle();
      ActionMenuPresenter var3 = this.mActionMenuPresenter;
      if (var3 != null) {
         var3.onConfigurationChanged(var1);
      }

   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 9) {
         this.mEatingHover = false;
      }

      if (!this.mEatingHover) {
         boolean var3 = super.onHoverEvent(var1);
         if (var2 == 9 && !var3) {
            this.mEatingHover = true;
         }
      }

      if (var2 == 10 || var2 == 3) {
         this.mEatingHover = false;
      }

      return true;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.mEatingTouch = false;
      }

      if (!this.mEatingTouch) {
         boolean var3 = super.onTouchEvent(var1);
         if (var2 == 0 && !var3) {
            this.mEatingTouch = true;
         }
      }

      if (var2 == 1 || var2 == 3) {
         this.mEatingTouch = false;
      }

      return true;
   }

   protected int positionChild(View var1, int var2, int var3, int var4, boolean var5) {
      int var6 = var1.getMeasuredWidth();
      int var7 = var1.getMeasuredHeight();
      var3 += (var4 - var7) / 2;
      if (var5) {
         var1.layout(var2 - var6, var3, var2, var3 + var7);
      } else {
         var1.layout(var2, var3, var2 + var6, var3 + var7);
      }

      return var5 ? -var6 : var6;
   }

   public void postShowOverflowMenu() {
      this.post(new Runnable() {
         public void run() {
            AbsActionBarView.this.showOverflowMenu();
         }
      });
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
      this.requestLayout();
   }

   public void setVisibility(int var1) {
      if (var1 != this.getVisibility()) {
         ViewPropertyAnimatorCompat var2 = this.mVisibilityAnim;
         if (var2 != null) {
            var2.cancel();
         }

         super.setVisibility(var1);
      }

   }

   public ViewPropertyAnimatorCompat setupAnimatorToVisibility(int var1, long var2) {
      ViewPropertyAnimatorCompat var4 = this.mVisibilityAnim;
      if (var4 != null) {
         var4.cancel();
      }

      if (var1 == 0) {
         if (this.getVisibility() != 0) {
            this.setAlpha(0.0F);
         }

         var4 = ViewCompat.animate(this).alpha(1.0F);
         var4.setDuration(var2);
         var4.setListener(this.mVisAnimListener.withFinalVisibility(var4, var1));
         return var4;
      } else {
         var4 = ViewCompat.animate(this).alpha(0.0F);
         var4.setDuration(var2);
         var4.setListener(this.mVisAnimListener.withFinalVisibility(var4, var1));
         return var4;
      }
   }

   public boolean showOverflowMenu() {
      ActionMenuPresenter var1 = this.mActionMenuPresenter;
      return var1 != null ? var1.showOverflowMenu() : false;
   }

   protected class VisibilityAnimListener implements ViewPropertyAnimatorListener {
      private boolean mCanceled = false;
      int mFinalVisibility;

      public void onAnimationCancel(View var1) {
         this.mCanceled = true;
      }

      public void onAnimationEnd(View var1) {
         if (!this.mCanceled) {
            AbsActionBarView var2 = AbsActionBarView.this;
            var2.mVisibilityAnim = null;
            var2.setVisibility(this.mFinalVisibility);
         }
      }

      public void onAnimationStart(View var1) {
         AbsActionBarView.super.setVisibility(0);
         this.mCanceled = false;
      }

      public AbsActionBarView.VisibilityAnimListener withFinalVisibility(ViewPropertyAnimatorCompat var1, int var2) {
         AbsActionBarView.this.mVisibilityAnim = var1;
         this.mFinalVisibility = var2;
         return this;
      }
   }
}
