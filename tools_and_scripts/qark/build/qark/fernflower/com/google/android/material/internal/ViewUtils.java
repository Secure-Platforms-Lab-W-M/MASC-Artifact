package com.google.android.material.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.View.OnAttachStateChangeListener;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewUtils {
   private ViewUtils() {
   }

   public static void doOnApplyWindowInsets(View var0, final ViewUtils.OnApplyWindowInsetsListener var1) {
      ViewCompat.setOnApplyWindowInsetsListener(var0, new androidx.core.view.OnApplyWindowInsetsListener(new ViewUtils.RelativePadding(ViewCompat.getPaddingStart(var0), var0.getPaddingTop(), ViewCompat.getPaddingEnd(var0), var0.getPaddingBottom())) {
         // $FF: synthetic field
         final ViewUtils.RelativePadding val$initialPadding;

         {
            this.val$initialPadding = var2;
         }

         public WindowInsetsCompat onApplyWindowInsets(View var1x, WindowInsetsCompat var2) {
            return var1.onApplyWindowInsets(var1x, var2, new ViewUtils.RelativePadding(this.val$initialPadding));
         }
      });
      requestApplyInsetsWhenAttached(var0);
   }

   public static float dpToPx(Context var0, int var1) {
      Resources var2 = var0.getResources();
      return TypedValue.applyDimension(1, (float)var1, var2.getDisplayMetrics());
   }

   public static float getParentAbsoluteElevation(View var0) {
      float var1 = 0.0F;

      for(ViewParent var2 = var0.getParent(); var2 instanceof View; var2 = var2.getParent()) {
         var1 += ViewCompat.getElevation((View)var2);
      }

      return var1;
   }

   public static boolean isLayoutRtl(View var0) {
      return ViewCompat.getLayoutDirection(var0) == 1;
   }

   public static Mode parseTintMode(int var0, Mode var1) {
      if (var0 != 3) {
         if (var0 != 5) {
            if (var0 != 9) {
               switch(var0) {
               case 14:
                  return Mode.MULTIPLY;
               case 15:
                  return Mode.SCREEN;
               case 16:
                  return Mode.ADD;
               default:
                  return var1;
               }
            } else {
               return Mode.SRC_ATOP;
            }
         } else {
            return Mode.SRC_IN;
         }
      } else {
         return Mode.SRC_OVER;
      }
   }

   public static void requestApplyInsetsWhenAttached(View var0) {
      if (ViewCompat.isAttachedToWindow(var0)) {
         ViewCompat.requestApplyInsets(var0);
      } else {
         var0.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View var1) {
               var1.removeOnAttachStateChangeListener(this);
               ViewCompat.requestApplyInsets(var1);
            }

            public void onViewDetachedFromWindow(View var1) {
            }
         });
      }
   }

   public static void requestFocusAndShowKeyboard(final View var0) {
      var0.requestFocus();
      var0.post(new Runnable() {
         public void run() {
            ((InputMethodManager)var0.getContext().getSystemService("input_method")).showSoftInput(var0, 1);
         }
      });
   }

   public interface OnApplyWindowInsetsListener {
      WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2, ViewUtils.RelativePadding var3);
   }

   public static class RelativePadding {
      public int bottom;
      public int end;
      public int start;
      public int top;

      public RelativePadding(int var1, int var2, int var3, int var4) {
         this.start = var1;
         this.top = var2;
         this.end = var3;
         this.bottom = var4;
      }

      public RelativePadding(ViewUtils.RelativePadding var1) {
         this.start = var1.start;
         this.top = var1.top;
         this.end = var1.end;
         this.bottom = var1.bottom;
      }

      public void applyToView(View var1) {
         ViewCompat.setPaddingRelative(var1, this.start, this.top, this.end, this.bottom);
      }
   }
}
