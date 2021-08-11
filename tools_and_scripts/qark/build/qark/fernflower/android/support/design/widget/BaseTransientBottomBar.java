package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R$anim;
import android.support.design.R$layout;
import android.support.design.R$styleable;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar {
   static final int ANIMATION_DURATION = 250;
   static final int ANIMATION_FADE_DURATION = 180;
   public static final int LENGTH_INDEFINITE = -2;
   public static final int LENGTH_LONG = 0;
   public static final int LENGTH_SHORT = -1;
   static final int MSG_DISMISS = 1;
   static final int MSG_SHOW = 0;
   private static final boolean USE_OFFSET_API;
   static final Handler sHandler;
   private final AccessibilityManager mAccessibilityManager;
   private List mCallbacks;
   private final BaseTransientBottomBar.ContentViewCallback mContentViewCallback;
   private final Context mContext;
   private int mDuration;
   final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
      public void dismiss(int var1) {
         BaseTransientBottomBar.sHandler.sendMessage(BaseTransientBottomBar.sHandler.obtainMessage(1, var1, 0, BaseTransientBottomBar.this));
      }

      public void show() {
         BaseTransientBottomBar.sHandler.sendMessage(BaseTransientBottomBar.sHandler.obtainMessage(0, BaseTransientBottomBar.this));
      }
   };
   private final ViewGroup mTargetParent;
   final BaseTransientBottomBar.SnackbarBaseLayout mView;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_OFFSET_API = var0;
      sHandler = new Handler(Looper.getMainLooper(), new Callback() {
         public boolean handleMessage(Message var1) {
            switch(var1.what) {
            case 0:
               ((BaseTransientBottomBar)var1.obj).showView();
               return true;
            case 1:
               ((BaseTransientBottomBar)var1.obj).hideView(var1.arg1);
               return true;
            default:
               return false;
            }
         }
      });
   }

   protected BaseTransientBottomBar(@NonNull ViewGroup var1, @NonNull View var2, @NonNull BaseTransientBottomBar.ContentViewCallback var3) {
      if (var1 != null) {
         if (var2 != null) {
            if (var3 != null) {
               this.mTargetParent = var1;
               this.mContentViewCallback = var3;
               this.mContext = var1.getContext();
               ThemeUtils.checkAppCompatTheme(this.mContext);
               this.mView = (BaseTransientBottomBar.SnackbarBaseLayout)LayoutInflater.from(this.mContext).inflate(R$layout.design_layout_snackbar, this.mTargetParent, false);
               this.mView.addView(var2);
               ViewCompat.setAccessibilityLiveRegion(this.mView, 1);
               ViewCompat.setImportantForAccessibility(this.mView, 1);
               ViewCompat.setFitsSystemWindows(this.mView, true);
               ViewCompat.setOnApplyWindowInsetsListener(this.mView, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     var1.setPadding(var1.getPaddingLeft(), var1.getPaddingTop(), var1.getPaddingRight(), var2.getSystemWindowInsetBottom());
                     return var2;
                  }
               });
               this.mAccessibilityManager = (AccessibilityManager)this.mContext.getSystemService("accessibility");
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

   private void animateViewOut(final int var1) {
      if (VERSION.SDK_INT >= 12) {
         ValueAnimator var3 = new ValueAnimator();
         var3.setIntValues(new int[]{0, this.mView.getHeight()});
         var3.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         var3.setDuration(250L);
         var3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               BaseTransientBottomBar.this.onViewHidden(var1);
            }

            public void onAnimationStart(Animator var1x) {
               BaseTransientBottomBar.this.mContentViewCallback.animateContentOut(0, 180);
            }
         });
         var3.addUpdateListener(new AnimatorUpdateListener() {
            private int mPreviousAnimatedIntValue = 0;

            public void onAnimationUpdate(ValueAnimator var1) {
               int var2 = (Integer)var1.getAnimatedValue();
               if (BaseTransientBottomBar.USE_OFFSET_API) {
                  ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.mView, var2 - this.mPreviousAnimatedIntValue);
               } else {
                  BaseTransientBottomBar.this.mView.setTranslationY((float)var2);
               }

               this.mPreviousAnimatedIntValue = var2;
            }
         });
         var3.start();
      } else {
         Animation var2 = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R$anim.design_snackbar_out);
         var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         var2.setDuration(250L);
         var2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation var1x) {
               BaseTransientBottomBar.this.onViewHidden(var1);
            }

            public void onAnimationRepeat(Animation var1x) {
            }

            public void onAnimationStart(Animation var1x) {
            }
         });
         this.mView.startAnimation(var2);
      }
   }

   @NonNull
   public BaseTransientBottomBar addCallback(@NonNull BaseTransientBottomBar.BaseCallback var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.mCallbacks == null) {
            this.mCallbacks = new ArrayList();
         }

         this.mCallbacks.add(var1);
         return this;
      }
   }

   void animateViewIn() {
      if (VERSION.SDK_INT >= 12) {
         final int var1 = this.mView.getHeight();
         if (USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom(this.mView, var1);
         } else {
            this.mView.setTranslationY((float)var1);
         }

         ValueAnimator var3 = new ValueAnimator();
         var3.setIntValues(new int[]{var1, 0});
         var3.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         var3.setDuration(250L);
         var3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               BaseTransientBottomBar.this.onViewShown();
            }

            public void onAnimationStart(Animator var1) {
               BaseTransientBottomBar.this.mContentViewCallback.animateContentIn(70, 180);
            }
         });
         var3.addUpdateListener(new AnimatorUpdateListener() {
            private int mPreviousAnimatedIntValue = var1;

            public void onAnimationUpdate(ValueAnimator var1x) {
               int var2 = (Integer)var1x.getAnimatedValue();
               if (BaseTransientBottomBar.USE_OFFSET_API) {
                  ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.mView, var2 - this.mPreviousAnimatedIntValue);
               } else {
                  BaseTransientBottomBar.this.mView.setTranslationY((float)var2);
               }

               this.mPreviousAnimatedIntValue = var2;
            }
         });
         var3.start();
      } else {
         Animation var2 = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R$anim.design_snackbar_in);
         var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         var2.setDuration(250L);
         var2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation var1) {
               BaseTransientBottomBar.this.onViewShown();
            }

            public void onAnimationRepeat(Animation var1) {
            }

            public void onAnimationStart(Animation var1) {
            }
         });
         this.mView.startAnimation(var2);
      }
   }

   public void dismiss() {
      this.dispatchDismiss(3);
   }

   void dispatchDismiss(int var1) {
      SnackbarManager.getInstance().dismiss(this.mManagerCallback, var1);
   }

   @NonNull
   public Context getContext() {
      return this.mContext;
   }

   public int getDuration() {
      return this.mDuration;
   }

   @NonNull
   public View getView() {
      return this.mView;
   }

   final void hideView(int var1) {
      if (this.shouldAnimate() && this.mView.getVisibility() == 0) {
         this.animateViewOut(var1);
      } else {
         this.onViewHidden(var1);
      }
   }

   public boolean isShown() {
      return SnackbarManager.getInstance().isCurrent(this.mManagerCallback);
   }

   public boolean isShownOrQueued() {
      return SnackbarManager.getInstance().isCurrentOrNext(this.mManagerCallback);
   }

   void onViewHidden(int var1) {
      SnackbarManager.getInstance().onDismissed(this.mManagerCallback);
      List var3 = this.mCallbacks;
      if (var3 != null) {
         for(int var2 = var3.size() - 1; var2 >= 0; --var2) {
            ((BaseTransientBottomBar.BaseCallback)this.mCallbacks.get(var2)).onDismissed(this, var1);
         }
      }

      if (VERSION.SDK_INT < 11) {
         this.mView.setVisibility(8);
      }

      ViewParent var4 = this.mView.getParent();
      if (var4 instanceof ViewGroup) {
         ((ViewGroup)var4).removeView(this.mView);
      }
   }

   void onViewShown() {
      SnackbarManager.getInstance().onShown(this.mManagerCallback);
      List var2 = this.mCallbacks;
      if (var2 != null) {
         for(int var1 = var2.size() - 1; var1 >= 0; --var1) {
            ((BaseTransientBottomBar.BaseCallback)this.mCallbacks.get(var1)).onShown(this);
         }

      }
   }

   @NonNull
   public BaseTransientBottomBar removeCallback(@NonNull BaseTransientBottomBar.BaseCallback var1) {
      if (var1 == null) {
         return this;
      } else {
         List var2 = this.mCallbacks;
         if (var2 == null) {
            return this;
         } else {
            var2.remove(var1);
            return this;
         }
      }
   }

   @NonNull
   public BaseTransientBottomBar setDuration(int var1) {
      this.mDuration = var1;
      return this;
   }

   boolean shouldAnimate() {
      return this.mAccessibilityManager.isEnabled() ^ true;
   }

   public void show() {
      SnackbarManager.getInstance().show(this.mDuration, this.mManagerCallback);
   }

   final void showView() {
      if (this.mView.getParent() == null) {
         LayoutParams var1 = this.mView.getLayoutParams();
         if (var1 instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1;
            BaseTransientBottomBar.Behavior var2 = new BaseTransientBottomBar.Behavior();
            var2.setStartAlphaSwipeDistance(0.1F);
            var2.setEndAlphaSwipeDistance(0.6F);
            var2.setSwipeDirection(0);
            var2.setListener(new SwipeDismissBehavior.OnDismissListener() {
               public void onDismiss(View var1) {
                  var1.setVisibility(8);
                  BaseTransientBottomBar.this.dispatchDismiss(0);
               }

               public void onDragStateChanged(int var1) {
                  switch(var1) {
                  case 0:
                     SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
                     return;
                  case 1:
                  case 2:
                     SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
                     return;
                  default:
                  }
               }
            });
            var3.setBehavior(var2);
            var3.insetEdge = 80;
         }

         this.mTargetParent.addView(this.mView);
      }

      this.mView.setOnAttachStateChangeListener(new BaseTransientBottomBar.OnAttachStateChangeListener() {
         public void onViewAttachedToWindow(View var1) {
         }

         public void onViewDetachedFromWindow(View var1) {
            if (BaseTransientBottomBar.this.isShownOrQueued()) {
               BaseTransientBottomBar.sHandler.post(new Runnable() {
                  public void run() {
                     BaseTransientBottomBar.this.onViewHidden(3);
                  }
               });
            }
         }
      });
      if (ViewCompat.isLaidOut(this.mView)) {
         if (this.shouldAnimate()) {
            this.animateViewIn();
         } else {
            this.onViewShown();
         }
      } else {
         this.mView.setOnLayoutChangeListener(new BaseTransientBottomBar.OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5) {
               BaseTransientBottomBar.this.mView.setOnLayoutChangeListener((BaseTransientBottomBar.OnLayoutChangeListener)null);
               if (BaseTransientBottomBar.this.shouldAnimate()) {
                  BaseTransientBottomBar.this.animateViewIn();
               } else {
                  BaseTransientBottomBar.this.onViewShown();
               }
            }
         });
      }
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
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public @interface DismissEvent {
      }
   }

   final class Behavior extends SwipeDismissBehavior {
      public boolean canSwipeDismissView(View var1) {
         return var1 instanceof BaseTransientBottomBar.SnackbarBaseLayout;
      }

      public boolean onInterceptTouchEvent(CoordinatorLayout var1, BaseTransientBottomBar.SnackbarBaseLayout var2, MotionEvent var3) {
         int var4 = var3.getActionMasked();
         if (var4 != 3) {
            switch(var4) {
            case 0:
               if (var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY())) {
                  SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
               }

               return super.onInterceptTouchEvent(var1, var2, var3);
            case 1:
               break;
            default:
               return super.onInterceptTouchEvent(var1, var2, var3);
            }
         }

         SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
         return super.onInterceptTouchEvent(var1, var2, var3);
      }
   }

   public interface ContentViewCallback {
      void animateContentIn(int var1, int var2);

      void animateContentOut(int var1, int var2);
   }

   @Retention(RetentionPolicy.SOURCE)
   @IntRange(
      from = 1L
   )
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface Duration {
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   interface OnAttachStateChangeListener {
      void onViewAttachedToWindow(View var1);

      void onViewDetachedFromWindow(View var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   interface OnLayoutChangeListener {
      void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static class SnackbarBaseLayout extends FrameLayout {
      private BaseTransientBottomBar.OnAttachStateChangeListener mOnAttachStateChangeListener;
      private BaseTransientBottomBar.OnLayoutChangeListener mOnLayoutChangeListener;

      SnackbarBaseLayout(Context var1) {
         this(var1, (AttributeSet)null);
      }

      SnackbarBaseLayout(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.SnackbarLayout);
         if (var3.hasValue(R$styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, (float)var3.getDimensionPixelSize(R$styleable.SnackbarLayout_elevation, 0));
         }

         var3.recycle();
         this.setClickable(true);
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         BaseTransientBottomBar.OnAttachStateChangeListener var1 = this.mOnAttachStateChangeListener;
         if (var1 != null) {
            var1.onViewAttachedToWindow(this);
         }

         ViewCompat.requestApplyInsets(this);
      }

      protected void onDetachedFromWindow() {
         super.onDetachedFromWindow();
         BaseTransientBottomBar.OnAttachStateChangeListener var1 = this.mOnAttachStateChangeListener;
         if (var1 != null) {
            var1.onViewDetachedFromWindow(this);
         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         BaseTransientBottomBar.OnLayoutChangeListener var6 = this.mOnLayoutChangeListener;
         if (var6 != null) {
            var6.onLayoutChange(this, var2, var3, var4, var5);
         }
      }

      void setOnAttachStateChangeListener(BaseTransientBottomBar.OnAttachStateChangeListener var1) {
         this.mOnAttachStateChangeListener = var1;
      }

      void setOnLayoutChangeListener(BaseTransientBottomBar.OnLayoutChangeListener var1) {
         this.mOnLayoutChangeListener = var1;
      }
   }
}
