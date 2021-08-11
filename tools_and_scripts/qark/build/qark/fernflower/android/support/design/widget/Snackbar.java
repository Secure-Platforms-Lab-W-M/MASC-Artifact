package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R$layout;
import android.support.design.internal.SnackbarContentLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public final class Snackbar extends BaseTransientBottomBar {
   public static final int LENGTH_INDEFINITE = -2;
   public static final int LENGTH_LONG = 0;
   public static final int LENGTH_SHORT = -1;
   @Nullable
   private BaseTransientBottomBar.BaseCallback mCallback;

   private Snackbar(ViewGroup var1, View var2, BaseTransientBottomBar.ContentViewCallback var3) {
      super(var1, var2, var3);
   }

   private static ViewGroup findSuitableParent(View var0) {
      ViewGroup var1 = null;

      do {
         if (var0 instanceof CoordinatorLayout) {
            return (ViewGroup)var0;
         }

         if (var0 instanceof FrameLayout) {
            if (var0.getId() == 16908290) {
               return (ViewGroup)var0;
            }

            var1 = (ViewGroup)var0;
         }

         if (var0 != null) {
            ViewParent var2 = var0.getParent();
            if (var2 instanceof View) {
               var0 = (View)var2;
            } else {
               var0 = null;
            }
         }
      } while(var0 != null);

      return var1;
   }

   @NonNull
   public static Snackbar make(@NonNull View var0, @StringRes int var1, int var2) {
      return make(var0, var0.getResources().getText(var1), var2);
   }

   @NonNull
   public static Snackbar make(@NonNull View var0, @NonNull CharSequence var1, int var2) {
      ViewGroup var4 = findSuitableParent(var0);
      if (var4 != null) {
         SnackbarContentLayout var3 = (SnackbarContentLayout)LayoutInflater.from(var4.getContext()).inflate(R$layout.design_layout_snackbar_include, var4, false);
         Snackbar var5 = new Snackbar(var4, var3, var3);
         var5.setText(var1);
         var5.setDuration(var2);
         return var5;
      } else {
         throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
      }
   }

   @NonNull
   public Snackbar setAction(@StringRes int var1, OnClickListener var2) {
      return this.setAction(this.getContext().getText(var1), var2);
   }

   @NonNull
   public Snackbar setAction(CharSequence var1, final OnClickListener var2) {
      Button var3 = ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView();
      if (!TextUtils.isEmpty(var1) && var2 != null) {
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
         return this;
      }
   }

   @NonNull
   public Snackbar setActionTextColor(@ColorInt int var1) {
      ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(var1);
      return this;
   }

   @NonNull
   public Snackbar setActionTextColor(ColorStateList var1) {
      ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(var1);
      return this;
   }

   @Deprecated
   @NonNull
   public Snackbar setCallback(Snackbar.Callback var1) {
      BaseTransientBottomBar.BaseCallback var2 = this.mCallback;
      if (var2 != null) {
         this.removeCallback(var2);
      }

      if (var1 != null) {
         this.addCallback(var1);
      }

      this.mCallback = var1;
      return this;
   }

   @NonNull
   public Snackbar setText(@StringRes int var1) {
      return this.setText(this.getContext().getText(var1));
   }

   @NonNull
   public Snackbar setText(@NonNull CharSequence var1) {
      ((SnackbarContentLayout)this.mView.getChildAt(0)).getMessageView().setText(var1);
      return this;
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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
