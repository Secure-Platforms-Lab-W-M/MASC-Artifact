package com.google.android.material.snackbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.layout;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar {
   static final int ANIMATION_DURATION = 250;
   static final int ANIMATION_FADE_DURATION = 180;
   private static final int ANIMATION_FADE_IN_DURATION = 150;
   private static final int ANIMATION_FADE_OUT_DURATION = 75;
   public static final int ANIMATION_MODE_FADE = 1;
   public static final int ANIMATION_MODE_SLIDE = 0;
   private static final float ANIMATION_SCALE_FROM_VALUE = 0.8F;
   public static final int LENGTH_INDEFINITE = -2;
   public static final int LENGTH_LONG = 0;
   public static final int LENGTH_SHORT = -1;
   static final int MSG_DISMISS = 1;
   static final int MSG_SHOW = 0;
   private static final int[] SNACKBAR_STYLE_ATTR;
   private static final String TAG;
   private static final boolean USE_OFFSET_API;
   static final Handler handler;
   private final AccessibilityManager accessibilityManager;
   private View anchorView;
   private BaseTransientBottomBar.Behavior behavior;
   private final Runnable bottomMarginGestureInsetRunnable = new Runnable() {
      public void run() {
         if (BaseTransientBottomBar.this.view != null) {
            if (BaseTransientBottomBar.this.context != null) {
               int var1 = BaseTransientBottomBar.this.getScreenHeight() - BaseTransientBottomBar.this.getViewAbsoluteBottom() + (int)BaseTransientBottomBar.this.view.getTranslationY();
               if (var1 < BaseTransientBottomBar.this.extraBottomMarginGestureInset) {
                  LayoutParams var2 = BaseTransientBottomBar.this.view.getLayoutParams();
                  if (!(var2 instanceof MarginLayoutParams)) {
                     Log.w(BaseTransientBottomBar.TAG, "Unable to apply gesture inset because layout params are not MarginLayoutParams");
                  } else {
                     MarginLayoutParams var3 = (MarginLayoutParams)var2;
                     var3.bottomMargin += BaseTransientBottomBar.this.extraBottomMarginGestureInset - var1;
                     BaseTransientBottomBar.this.view.requestLayout();
                  }
               }
            }
         }
      }
   };
   private List callbacks;
   private final com.google.android.material.snackbar.ContentViewCallback contentViewCallback;
   private final Context context;
   private int duration;
   private int extraBottomMarginAnchorView;
   private int extraBottomMarginGestureInset;
   private int extraBottomMarginWindowInset;
   private int extraLeftMarginWindowInset;
   private int extraRightMarginWindowInset;
   private boolean gestureInsetBottomIgnored;
   SnackbarManager.Callback managerCallback = new SnackbarManager.Callback() {
      public void dismiss(int var1) {
         BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, var1, 0, BaseTransientBottomBar.this));
      }

      public void show() {
         BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
      }
   };
   private Rect originalMargins;
   private final ViewGroup targetParent;
   protected final BaseTransientBottomBar.SnackbarBaseLayout view;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_OFFSET_API = var0;
      SNACKBAR_STYLE_ATTR = new int[]{attr.snackbarStyle};
      TAG = BaseTransientBottomBar.class.getSimpleName();
      handler = new Handler(Looper.getMainLooper(), new Callback() {
         public boolean handleMessage(Message var1) {
            int var2 = var1.what;
            if (var2 != 0) {
               if (var2 != 1) {
                  return false;
               } else {
                  ((BaseTransientBottomBar)var1.obj).hideView(var1.arg1);
                  return true;
               }
            } else {
               ((BaseTransientBottomBar)var1.obj).showView();
               return true;
            }
         }
      });
   }

   protected BaseTransientBottomBar(ViewGroup var1, View var2, com.google.android.material.snackbar.ContentViewCallback var3) {
      if (var1 != null) {
         if (var2 != null) {
            if (var3 != null) {
               this.targetParent = var1;
               this.contentViewCallback = var3;
               Context var4 = var1.getContext();
               this.context = var4;
               ThemeEnforcement.checkAppCompatTheme(var4);
               BaseTransientBottomBar.SnackbarBaseLayout var5 = (BaseTransientBottomBar.SnackbarBaseLayout)LayoutInflater.from(this.context).inflate(this.getSnackbarBaseLayoutResId(), this.targetParent, false);
               this.view = var5;
               if (var5.getBackground() == null) {
                  ViewCompat.setBackground(this.view, this.createThemedBackground());
               }

               if (var2 instanceof SnackbarContentLayout) {
                  ((SnackbarContentLayout)var2).updateActionTextColorAlphaIfNeeded(this.view.getActionTextColorAlpha());
               }

               this.view.addView(var2);
               LayoutParams var6 = this.view.getLayoutParams();
               if (var6 instanceof MarginLayoutParams) {
                  MarginLayoutParams var7 = (MarginLayoutParams)var6;
                  this.originalMargins = new Rect(var7.leftMargin, var7.topMargin, var7.rightMargin, var7.bottomMargin);
               }

               ViewCompat.setAccessibilityLiveRegion(this.view, 1);
               ViewCompat.setImportantForAccessibility(this.view, 1);
               ViewCompat.setFitsSystemWindows(this.view, true);
               ViewCompat.setOnApplyWindowInsetsListener(this.view, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     BaseTransientBottomBar.this.extraBottomMarginWindowInset = var2.getSystemWindowInsetBottom();
                     BaseTransientBottomBar.this.extraLeftMarginWindowInset = var2.getSystemWindowInsetLeft();
                     BaseTransientBottomBar.this.extraRightMarginWindowInset = var2.getSystemWindowInsetRight();
                     BaseTransientBottomBar.this.updateMargins();
                     return var2;
                  }
               });
               ViewCompat.setAccessibilityDelegate(this.view, new AccessibilityDelegateCompat() {
                  public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
                     super.onInitializeAccessibilityNodeInfo(var1, var2);
                     var2.addAction(1048576);
                     var2.setDismissable(true);
                  }

                  public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
                     if (var2 == 1048576) {
                        BaseTransientBottomBar.this.dismiss();
                        return true;
                     } else {
                        return super.performAccessibilityAction(var1, var2, var3);
                     }
                  }
               });
               this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
            } else {
               throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
            }
         } else {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
         }
      } else {
         throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
      }
   }

   private void animateViewOut(int var1) {
      if (this.view.getAnimationMode() == 1) {
         this.startFadeOutAnimation(var1);
      } else {
         this.startSlideOutAnimation(var1);
      }
   }

   private int calculateBottomMarginForAnchorView() {
      View var2 = this.anchorView;
      if (var2 == null) {
         return 0;
      } else {
         int[] var3 = new int[2];
         var2.getLocationOnScreen(var3);
         int var1 = var3[1];
         int[] var4 = new int[2];
         this.targetParent.getLocationOnScreen(var4);
         return var4[1] + this.targetParent.getHeight() - var1;
      }
   }

   private Drawable createThemedBackground() {
      int var2 = MaterialColors.layer(this.view, attr.colorSurface, attr.colorOnSurface, this.view.getBackgroundOverlayColorAlpha());
      float var1 = this.view.getResources().getDimension(dimen.mtrl_snackbar_background_corner_radius);
      GradientDrawable var3 = new GradientDrawable();
      var3.setShape(0);
      var3.setColor(var2);
      var3.setCornerRadius(var1);
      return var3;
   }

   private ValueAnimator getAlphaAnimator(float... var1) {
      ValueAnimator var2 = ValueAnimator.ofFloat(var1);
      var2.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      var2.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            BaseTransientBottomBar.this.view.setAlpha((Float)var1.getAnimatedValue());
         }
      });
      return var2;
   }

   private ValueAnimator getScaleAnimator(float... var1) {
      ValueAnimator var2 = ValueAnimator.ofFloat(var1);
      var2.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
      var2.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = (Float)var1.getAnimatedValue();
            BaseTransientBottomBar.this.view.setScaleX(var2);
            BaseTransientBottomBar.this.view.setScaleY(var2);
         }
      });
      return var2;
   }

   private int getScreenHeight() {
      WindowManager var1 = (WindowManager)this.context.getSystemService("window");
      DisplayMetrics var2 = new DisplayMetrics();
      var1.getDefaultDisplay().getRealMetrics(var2);
      return var2.heightPixels;
   }

   private int getTranslationYBottom() {
      int var2 = this.view.getHeight();
      LayoutParams var3 = this.view.getLayoutParams();
      int var1 = var2;
      if (var3 instanceof MarginLayoutParams) {
         var1 = var2 + ((MarginLayoutParams)var3).bottomMargin;
      }

      return var1;
   }

   private int getViewAbsoluteBottom() {
      int[] var1 = new int[2];
      this.view.getLocationOnScreen(var1);
      return var1[1] + this.view.getHeight();
   }

   private boolean isSwipeDismissable() {
      LayoutParams var1 = this.view.getLayoutParams();
      return var1 instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)var1).getBehavior() instanceof SwipeDismissBehavior;
   }

   private void setUpBehavior(CoordinatorLayout.LayoutParams var1) {
      Object var2 = this.behavior;
      if (var2 == null) {
         var2 = this.getNewBehavior();
      }

      if (var2 instanceof BaseTransientBottomBar.Behavior) {
         ((BaseTransientBottomBar.Behavior)var2).setBaseTransientBottomBar(this);
      }

      ((SwipeDismissBehavior)var2).setListener(new SwipeDismissBehavior.OnDismissListener() {
         public void onDismiss(View var1) {
            var1.setVisibility(8);
            BaseTransientBottomBar.this.dispatchDismiss(0);
         }

         public void onDragStateChanged(int var1) {
            if (var1 != 0) {
               if (var1 == 1 || var1 == 2) {
                  SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
               }
            } else {
               SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
            }
         }
      });
      var1.setBehavior((CoordinatorLayout.Behavior)var2);
      if (this.anchorView == null) {
         var1.insetEdge = 80;
      }

   }

   private boolean shouldUpdateGestureInset() {
      return this.extraBottomMarginGestureInset > 0 && !this.gestureInsetBottomIgnored && this.isSwipeDismissable();
   }

   private void showViewImpl() {
      if (this.shouldAnimate()) {
         this.animateViewIn();
      } else {
         this.view.setVisibility(0);
         this.onViewShown();
      }
   }

   private void startFadeInAnimation() {
      ValueAnimator var1 = this.getAlphaAnimator(0.0F, 1.0F);
      ValueAnimator var2 = this.getScaleAnimator(0.8F, 1.0F);
      AnimatorSet var3 = new AnimatorSet();
      var3.playTogether(new Animator[]{var1, var2});
      var3.setDuration(150L);
      var3.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            BaseTransientBottomBar.this.onViewShown();
         }
      });
      var3.start();
   }

   private void startFadeOutAnimation(final int var1) {
      ValueAnimator var2 = this.getAlphaAnimator(1.0F, 0.0F);
      var2.setDuration(75L);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            BaseTransientBottomBar.this.onViewHidden(var1);
         }
      });
      var2.start();
   }

   private void startSlideInAnimation() {
      final int var1 = this.getTranslationYBottom();
      if (USE_OFFSET_API) {
         ViewCompat.offsetTopAndBottom(this.view, var1);
      } else {
         this.view.setTranslationY((float)var1);
      }

      ValueAnimator var2 = new ValueAnimator();
      var2.setIntValues(new int[]{var1, 0});
      var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      var2.setDuration(250L);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            BaseTransientBottomBar.this.onViewShown();
         }

         public void onAnimationStart(Animator var1) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
         }
      });
      var2.addUpdateListener(new AnimatorUpdateListener() {
         private int previousAnimatedIntValue = var1;

         public void onAnimationUpdate(ValueAnimator var1x) {
            int var2 = (Integer)var1x.getAnimatedValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
               ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, var2 - this.previousAnimatedIntValue);
            } else {
               BaseTransientBottomBar.this.view.setTranslationY((float)var2);
            }

            this.previousAnimatedIntValue = var2;
         }
      });
      var2.start();
   }

   private void startSlideOutAnimation(final int var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setIntValues(new int[]{0, this.getTranslationYBottom()});
      var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      var2.setDuration(250L);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            BaseTransientBottomBar.this.onViewHidden(var1);
         }

         public void onAnimationStart(Animator var1x) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
         }
      });
      var2.addUpdateListener(new AnimatorUpdateListener() {
         private int previousAnimatedIntValue = 0;

         public void onAnimationUpdate(ValueAnimator var1) {
            int var2 = (Integer)var1.getAnimatedValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
               ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, var2 - this.previousAnimatedIntValue);
            } else {
               BaseTransientBottomBar.this.view.setTranslationY((float)var2);
            }

            this.previousAnimatedIntValue = var2;
         }
      });
      var2.start();
   }

   private void updateMargins() {
      LayoutParams var2 = this.view.getLayoutParams();
      if (var2 instanceof MarginLayoutParams && this.originalMargins != null) {
         int var1;
         if (this.anchorView != null) {
            var1 = this.extraBottomMarginAnchorView;
         } else {
            var1 = this.extraBottomMarginWindowInset;
         }

         MarginLayoutParams var3 = (MarginLayoutParams)var2;
         var3.bottomMargin = this.originalMargins.bottom + var1;
         var3.leftMargin = this.originalMargins.left + this.extraLeftMarginWindowInset;
         var3.rightMargin = this.originalMargins.right + this.extraRightMarginWindowInset;
         this.view.requestLayout();
         if (VERSION.SDK_INT >= 29 && this.shouldUpdateGestureInset()) {
            this.view.removeCallbacks(this.bottomMarginGestureInsetRunnable);
            this.view.post(this.bottomMarginGestureInsetRunnable);
         }

      } else {
         Log.w(TAG, "Unable to update margins because layout params are not MarginLayoutParams");
      }
   }

   public BaseTransientBottomBar addCallback(BaseTransientBottomBar.BaseCallback var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.callbacks == null) {
            this.callbacks = new ArrayList();
         }

         this.callbacks.add(var1);
         return this;
      }
   }

   void animateViewIn() {
      this.view.post(new Runnable() {
         public void run() {
            if (BaseTransientBottomBar.this.view != null) {
               BaseTransientBottomBar.this.view.setVisibility(0);
               if (BaseTransientBottomBar.this.view.getAnimationMode() == 1) {
                  BaseTransientBottomBar.this.startFadeInAnimation();
               } else {
                  BaseTransientBottomBar.this.startSlideInAnimation();
               }
            }
         }
      });
   }

   public void dismiss() {
      this.dispatchDismiss(3);
   }

   protected void dispatchDismiss(int var1) {
      SnackbarManager.getInstance().dismiss(this.managerCallback, var1);
   }

   public View getAnchorView() {
      return this.anchorView;
   }

   public int getAnimationMode() {
      return this.view.getAnimationMode();
   }

   public BaseTransientBottomBar.Behavior getBehavior() {
      return this.behavior;
   }

   public Context getContext() {
      return this.context;
   }

   public int getDuration() {
      return this.duration;
   }

   protected SwipeDismissBehavior getNewBehavior() {
      return new BaseTransientBottomBar.Behavior();
   }

   protected int getSnackbarBaseLayoutResId() {
      return this.hasSnackbarStyleAttr() ? layout.mtrl_layout_snackbar : layout.design_layout_snackbar;
   }

   public View getView() {
      return this.view;
   }

   protected boolean hasSnackbarStyleAttr() {
      TypedArray var3 = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
      boolean var2 = false;
      int var1 = var3.getResourceId(0, -1);
      var3.recycle();
      if (var1 != -1) {
         var2 = true;
      }

      return var2;
   }

   final void hideView(int var1) {
      if (this.shouldAnimate() && this.view.getVisibility() == 0) {
         this.animateViewOut(var1);
      } else {
         this.onViewHidden(var1);
      }
   }

   public boolean isGestureInsetBottomIgnored() {
      return this.gestureInsetBottomIgnored;
   }

   public boolean isShown() {
      return SnackbarManager.getInstance().isCurrent(this.managerCallback);
   }

   public boolean isShownOrQueued() {
      return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
   }

   void onViewHidden(int var1) {
      SnackbarManager.getInstance().onDismissed(this.managerCallback);
      List var3 = this.callbacks;
      if (var3 != null) {
         for(int var2 = var3.size() - 1; var2 >= 0; --var2) {
            ((BaseTransientBottomBar.BaseCallback)this.callbacks.get(var2)).onDismissed(this, var1);
         }
      }

      ViewParent var4 = this.view.getParent();
      if (var4 instanceof ViewGroup) {
         ((ViewGroup)var4).removeView(this.view);
      }

   }

   void onViewShown() {
      SnackbarManager.getInstance().onShown(this.managerCallback);
      List var2 = this.callbacks;
      if (var2 != null) {
         for(int var1 = var2.size() - 1; var1 >= 0; --var1) {
            ((BaseTransientBottomBar.BaseCallback)this.callbacks.get(var1)).onShown(this);
         }
      }

   }

   public BaseTransientBottomBar removeCallback(BaseTransientBottomBar.BaseCallback var1) {
      if (var1 == null) {
         return this;
      } else {
         List var2 = this.callbacks;
         if (var2 == null) {
            return this;
         } else {
            var2.remove(var1);
            return this;
         }
      }
   }

   public BaseTransientBottomBar setAnchorView(int var1) {
      View var2 = this.targetParent.findViewById(var1);
      this.anchorView = var2;
      if (var2 != null) {
         return this;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unable to find anchor view with id: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public BaseTransientBottomBar setAnchorView(View var1) {
      this.anchorView = var1;
      return this;
   }

   public BaseTransientBottomBar setAnimationMode(int var1) {
      this.view.setAnimationMode(var1);
      return this;
   }

   public BaseTransientBottomBar setBehavior(BaseTransientBottomBar.Behavior var1) {
      this.behavior = var1;
      return this;
   }

   public BaseTransientBottomBar setDuration(int var1) {
      this.duration = var1;
      return this;
   }

   public BaseTransientBottomBar setGestureInsetBottomIgnored(boolean var1) {
      this.gestureInsetBottomIgnored = var1;
      return this;
   }

   boolean shouldAnimate() {
      List var1 = this.accessibilityManager.getEnabledAccessibilityServiceList(1);
      return var1 != null && var1.isEmpty();
   }

   public void show() {
      SnackbarManager.getInstance().show(this.getDuration(), this.managerCallback);
   }

   final void showView() {
      this.view.setOnAttachStateChangeListener(new BaseTransientBottomBar.OnAttachStateChangeListener() {
         public void onViewAttachedToWindow(View var1) {
            if (VERSION.SDK_INT >= 29) {
               WindowInsets var2 = BaseTransientBottomBar.this.view.getRootWindowInsets();
               if (var2 != null) {
                  BaseTransientBottomBar.this.extraBottomMarginGestureInset = var2.getMandatorySystemGestureInsets().bottom;
                  BaseTransientBottomBar.this.updateMargins();
               }
            }

         }

         public void onViewDetachedFromWindow(View var1) {
            if (BaseTransientBottomBar.this.isShownOrQueued()) {
               BaseTransientBottomBar.handler.post(new Runnable() {
                  public void run() {
                     BaseTransientBottomBar.this.onViewHidden(3);
                  }
               });
            }

         }
      });
      if (this.view.getParent() == null) {
         LayoutParams var1 = this.view.getLayoutParams();
         if (var1 instanceof CoordinatorLayout.LayoutParams) {
            this.setUpBehavior((CoordinatorLayout.LayoutParams)var1);
         }

         this.extraBottomMarginAnchorView = this.calculateBottomMarginForAnchorView();
         this.updateMargins();
         this.view.setVisibility(4);
         this.targetParent.addView(this.view);
      }

      if (ViewCompat.isLaidOut(this.view)) {
         this.showViewImpl();
      } else {
         this.view.setOnLayoutChangeListener(new BaseTransientBottomBar.OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5) {
               BaseTransientBottomBar.this.view.setOnLayoutChangeListener((BaseTransientBottomBar.OnLayoutChangeListener)null);
               BaseTransientBottomBar.this.showViewImpl();
            }
         });
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface AnimationMode {
   }

   public abstract static class BaseCallback {
      public static final int DISMISS_EVENT_ACTION = 1;
      public static final int DISMISS_EVENT_CONSECUTIVE = 4;
      public static final int DISMISS_EVENT_MANUAL = 3;
      public static final int DISMISS_EVENT_SWIPE = 0;
      public static final int DISMISS_EVENT_TIMEOUT = 2;

      public void onDismissed(Object var1, int var2) {
      }

      public void onShown(Object var1) {
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface DismissEvent {
      }
   }

   public static class Behavior extends SwipeDismissBehavior {
      private final BaseTransientBottomBar.BehaviorDelegate delegate = new BaseTransientBottomBar.BehaviorDelegate(this);

      private void setBaseTransientBottomBar(BaseTransientBottomBar var1) {
         this.delegate.setBaseTransientBottomBar(var1);
      }

      public boolean canSwipeDismissView(View var1) {
         return this.delegate.canSwipeDismissView(var1);
      }

      public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         this.delegate.onInterceptTouchEvent(var1, var2, var3);
         return super.onInterceptTouchEvent(var1, var2, var3);
      }
   }

   public static class BehaviorDelegate {
      private SnackbarManager.Callback managerCallback;

      public BehaviorDelegate(SwipeDismissBehavior var1) {
         var1.setStartAlphaSwipeDistance(0.1F);
         var1.setEndAlphaSwipeDistance(0.6F);
         var1.setSwipeDirection(0);
      }

      public boolean canSwipeDismissView(View var1) {
         return var1 instanceof BaseTransientBottomBar.SnackbarBaseLayout;
      }

      public void onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         int var4 = var3.getActionMasked();
         if (var4 != 0) {
            if (var4 == 1 || var4 == 3) {
               SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
            }
         } else {
            if (var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY())) {
               SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
            }

         }
      }

      public void setBaseTransientBottomBar(BaseTransientBottomBar var1) {
         this.managerCallback = var1.managerCallback;
      }
   }

   @Deprecated
   public interface ContentViewCallback extends com.google.android.material.snackbar.ContentViewCallback {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Duration {
   }

   protected interface OnAttachStateChangeListener {
      void onViewAttachedToWindow(View var1);

      void onViewDetachedFromWindow(View var1);
   }

   protected interface OnLayoutChangeListener {
      void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
   }

   protected static class SnackbarBaseLayout extends FrameLayout {
      private static final OnTouchListener consumeAllTouchListener = new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            return true;
         }
      };
      private final float actionTextColorAlpha;
      private int animationMode;
      private final float backgroundOverlayColorAlpha;
      private BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener;
      private BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener;

      protected SnackbarBaseLayout(Context var1) {
         this(var1, (AttributeSet)null);
      }

      protected SnackbarBaseLayout(Context var1, AttributeSet var2) {
         super(ThemeEnforcement.createThemedContext(var1, var2, 0, 0), var2);
         TypedArray var3 = this.getContext().obtainStyledAttributes(var2, styleable.SnackbarLayout);
         if (var3.hasValue(styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, (float)var3.getDimensionPixelSize(styleable.SnackbarLayout_elevation, 0));
         }

         this.animationMode = var3.getInt(styleable.SnackbarLayout_animationMode, 0);
         this.backgroundOverlayColorAlpha = var3.getFloat(styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0F);
         this.actionTextColorAlpha = var3.getFloat(styleable.SnackbarLayout_actionTextColorAlpha, 1.0F);
         var3.recycle();
         this.setOnTouchListener(consumeAllTouchListener);
         this.setFocusable(true);
      }

      float getActionTextColorAlpha() {
         return this.actionTextColorAlpha;
      }

      int getAnimationMode() {
         return this.animationMode;
      }

      float getBackgroundOverlayColorAlpha() {
         return this.backgroundOverlayColorAlpha;
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         BaseTransientBottomBar.OnAttachStateChangeListener var1 = this.onAttachStateChangeListener;
         if (var1 != null) {
            var1.onViewAttachedToWindow(this);
         }

         ViewCompat.requestApplyInsets(this);
      }

      protected void onDetachedFromWindow() {
         super.onDetachedFromWindow();
         BaseTransientBottomBar.OnAttachStateChangeListener var1 = this.onAttachStateChangeListener;
         if (var1 != null) {
            var1.onViewDetachedFromWindow(this);
         }

      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         BaseTransientBottomBar.OnLayoutChangeListener var6 = this.onLayoutChangeListener;
         if (var6 != null) {
            var6.onLayoutChange(this, var2, var3, var4, var5);
         }

      }

      void setAnimationMode(int var1) {
         this.animationMode = var1;
      }

      void setOnAttachStateChangeListener(BaseTransientBottomBar.OnAttachStateChangeListener var1) {
         this.onAttachStateChangeListener = var1;
      }

      public void setOnClickListener(OnClickListener var1) {
         OnTouchListener var2;
         if (var1 != null) {
            var2 = null;
         } else {
            var2 = consumeAllTouchListener;
         }

         this.setOnTouchListener(var2);
         super.setOnClickListener(var1);
      }

      void setOnLayoutChangeListener(BaseTransientBottomBar.OnLayoutChangeListener var1) {
         this.onLayoutChangeListener = var1;
      }
   }
}
