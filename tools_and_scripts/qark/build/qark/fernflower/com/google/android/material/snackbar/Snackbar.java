package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.layout;

public class Snackbar extends BaseTransientBottomBar {
   private static final int[] SNACKBAR_BUTTON_STYLE_ATTR;
   private final AccessibilityManager accessibilityManager;
   private BaseTransientBottomBar.BaseCallback callback;
   private boolean hasAction;

   static {
      SNACKBAR_BUTTON_STYLE_ATTR = new int[]{attr.snackbarButtonStyle};
   }

   private Snackbar(ViewGroup var1, View var2, com.google.android.material.snackbar.ContentViewCallback var3) {
      super(var1, var2, var3);
      this.accessibilityManager = (AccessibilityManager)var1.getContext().getSystemService("accessibility");
   }

   private static ViewGroup findSuitableParent(View var0) {
      View var2;
      for(ViewGroup var1 = null; !(var0 instanceof CoordinatorLayout); var0 = var2) {
         if (var0 instanceof FrameLayout) {
            if (var0.getId() == 16908290) {
               return (ViewGroup)var0;
            }

            var1 = (ViewGroup)var0;
         }

         var2 = var0;
         if (var0 != null) {
            ViewParent var3 = var0.getParent();
            if (var3 instanceof View) {
               var0 = (View)var3;
            } else {
               var0 = null;
            }

            var2 = var0;
         }

         if (var2 == null) {
            return var1;
         }
      }

      return (ViewGroup)var0;
   }

   protected static boolean hasSnackbarButtonStyleAttr(Context var0) {
      TypedArray var3 = var0.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
      boolean var2 = false;
      int var1 = var3.getResourceId(0, -1);
      var3.recycle();
      if (var1 != -1) {
         var2 = true;
      }

      return var2;
   }

   public static Snackbar make(View var0, int var1, int var2) {
      return make(var0, var0.getResources().getText(var1), var2);
   }

   public static Snackbar make(View var0, CharSequence var1, int var2) {
      ViewGroup var5 = findSuitableParent(var0);
      if (var5 != null) {
         LayoutInflater var4 = LayoutInflater.from(var5.getContext());
         int var3;
         if (hasSnackbarButtonStyleAttr(var5.getContext())) {
            var3 = layout.mtrl_layout_snackbar_include;
         } else {
            var3 = layout.design_layout_snackbar_include;
         }

         SnackbarContentLayout var7 = (SnackbarContentLayout)var4.inflate(var3, var5, false);
         Snackbar var6 = new Snackbar(var5, var7, var7);
         var6.setText(var1);
         var6.setDuration(var2);
         return var6;
      } else {
         throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
      }
   }

   public void dismiss() {
      super.dismiss();
   }

   public int getDuration() {
      int var2 = super.getDuration();
      if (var2 == -2) {
         return -2;
      } else if (VERSION.SDK_INT >= 29) {
         byte var1;
         if (this.hasAction) {
            var1 = 4;
         } else {
            var1 = 0;
         }

         return this.accessibilityManager.getRecommendedTimeoutMillis(var2, var1 | 1 | 2);
      } else {
         return this.hasAction && this.accessibilityManager.isTouchExplorationEnabled() ? -2 : var2;
      }
   }

   public boolean isShown() {
      return super.isShown();
   }

   public Snackbar setAction(int var1, OnClickListener var2) {
      return this.setAction(this.getContext().getText(var1), var2);
   }

   public Snackbar setAction(CharSequence var1, final OnClickListener var2) {
      Button var3 = ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView();
      if (!TextUtils.isEmpty(var1) && var2 != null) {
         this.hasAction = true;
         var3.setVisibility(0);
         var3.setText(var1);
         var3.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               var2.onClick(var1);
               Snackbar.this.dispatchDismiss(1);
            }
         });
         return this;
      } else {
         var3.setVisibility(8);
         var3.setOnClickListener((OnClickListener)null);
         this.hasAction = false;
         return this;
      }
   }

   public Snackbar setActionTextColor(int var1) {
      ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(var1);
      return this;
   }

   public Snackbar setActionTextColor(ColorStateList var1) {
      ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(var1);
      return this;
   }

   public Snackbar setBackgroundTint(int var1) {
      Drawable var2 = this.view.getBackground();
      if (var2 != null) {
         var2 = var2.mutate();
         if (VERSION.SDK_INT >= 22) {
            DrawableCompat.setTint(var2, var1);
            return this;
         }

         var2.setColorFilter(var1, Mode.SRC_IN);
      }

      return this;
   }

   public Snackbar setBackgroundTintList(ColorStateList var1) {
      DrawableCompat.setTintList(this.view.getBackground().mutate(), var1);
      return this;
   }

   @Deprecated
   public Snackbar setCallback(Snackbar.Callback var1) {
      BaseTransientBottomBar.BaseCallback var2 = this.callback;
      if (var2 != null) {
         this.removeCallback(var2);
      }

      if (var1 != null) {
         this.addCallback(var1);
      }

      this.callback = var1;
      return this;
   }

   public Snackbar setText(int var1) {
      return this.setText(this.getContext().getText(var1));
   }

   public Snackbar setText(CharSequence var1) {
      ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setText(var1);
      return this;
   }

   public Snackbar setTextColor(int var1) {
      ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setTextColor(var1);
      return this;
   }

   public Snackbar setTextColor(ColorStateList var1) {
      ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setTextColor(var1);
      return this;
   }

   public void show() {
      super.show();
   }

   public static class Callback extends BaseTransientBottomBar.BaseCallback {
      public static final int DISMISS_EVENT_ACTION = 1;
      public static final int DISMISS_EVENT_CONSECUTIVE = 4;
      public static final int DISMISS_EVENT_MANUAL = 3;
      public static final int DISMISS_EVENT_SWIPE = 0;
      public static final int DISMISS_EVENT_TIMEOUT = 2;

      public void onDismissed(Snackbar var1, int var2) {
      }

      public void onShown(Snackbar var1) {
      }
   }

   public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
      public SnackbarLayout(Context var1) {
         super(var1);
      }

      public SnackbarLayout(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         var2 = this.getChildCount();
         int var3 = this.getMeasuredWidth();
         int var4 = this.getPaddingLeft();
         int var5 = this.getPaddingRight();

         for(var1 = 0; var1 < var2; ++var1) {
            View var6 = this.getChildAt(var1);
            if (var6.getLayoutParams().width == -1) {
               var6.measure(MeasureSpec.makeMeasureSpec(var3 - var4 - var5, 1073741824), MeasureSpec.makeMeasureSpec(var6.getMeasuredHeight(), 1073741824));
            }
         }

      }
   }
}
