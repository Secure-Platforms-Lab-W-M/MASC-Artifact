package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnLongClickListener;
import androidx.databinding.library.baseAdapters.R.id;

public class ViewBindingAdapter {
   public static final int FADING_EDGE_HORIZONTAL = 1;
   public static final int FADING_EDGE_NONE = 0;
   public static final int FADING_EDGE_VERTICAL = 2;

   private static int pixelsToDimensionPixelSize(float var0) {
      int var1 = (int)(0.5F + var0);
      if (var1 != 0) {
         return var1;
      } else if (var0 == 0.0F) {
         return 0;
      } else {
         return var0 > 0.0F ? 1 : -1;
      }
   }

   public static void setBackground(View var0, Drawable var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setBackground(var1);
      } else {
         var0.setBackgroundDrawable(var1);
      }
   }

   public static void setClickListener(View var0, OnClickListener var1, boolean var2) {
      var0.setOnClickListener(var1);
      var0.setClickable(var2);
   }

   public static void setOnAttachStateChangeListener(View var0, final ViewBindingAdapter.OnViewDetachedFromWindow var1, final ViewBindingAdapter.OnViewAttachedToWindow var2) {
      OnAttachStateChangeListener var3;
      if (var1 == null && var2 == null) {
         var3 = null;
      } else {
         var3 = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View var1x) {
               ViewBindingAdapter.OnViewAttachedToWindow var2x = var2;
               if (var2x != null) {
                  var2x.onViewAttachedToWindow(var1x);
               }

            }

            public void onViewDetachedFromWindow(View var1x) {
               ViewBindingAdapter.OnViewDetachedFromWindow var2x = var1;
               if (var2x != null) {
                  var2x.onViewDetachedFromWindow(var1x);
               }

            }
         };
      }

      OnAttachStateChangeListener var4 = (OnAttachStateChangeListener)ListenerUtil.trackListener(var0, var3, id.onAttachStateChangeListener);
      if (var4 != null) {
         var0.removeOnAttachStateChangeListener(var4);
      }

      if (var3 != null) {
         var0.addOnAttachStateChangeListener(var3);
      }

   }

   public static void setOnClick(View var0, OnClickListener var1, boolean var2) {
      var0.setOnClickListener(var1);
      var0.setClickable(var2);
   }

   public static void setOnLayoutChangeListener(View var0, OnLayoutChangeListener var1, OnLayoutChangeListener var2) {
      if (var1 != null) {
         var0.removeOnLayoutChangeListener(var1);
      }

      if (var2 != null) {
         var0.addOnLayoutChangeListener(var2);
      }

   }

   public static void setOnLongClick(View var0, OnLongClickListener var1, boolean var2) {
      var0.setOnLongClickListener(var1);
      var0.setLongClickable(var2);
   }

   public static void setOnLongClickListener(View var0, OnLongClickListener var1, boolean var2) {
      var0.setOnLongClickListener(var1);
      var0.setLongClickable(var2);
   }

   public static void setPadding(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      var0.setPadding(var2, var2, var2, var2);
   }

   public static void setPaddingBottom(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      var0.setPadding(var0.getPaddingLeft(), var0.getPaddingTop(), var0.getPaddingRight(), var2);
   }

   public static void setPaddingEnd(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      if (VERSION.SDK_INT >= 17) {
         var0.setPaddingRelative(var0.getPaddingStart(), var0.getPaddingTop(), var2, var0.getPaddingBottom());
      } else {
         var0.setPadding(var0.getPaddingLeft(), var0.getPaddingTop(), var2, var0.getPaddingBottom());
      }
   }

   public static void setPaddingLeft(View var0, float var1) {
      var0.setPadding(pixelsToDimensionPixelSize(var1), var0.getPaddingTop(), var0.getPaddingRight(), var0.getPaddingBottom());
   }

   public static void setPaddingRight(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      var0.setPadding(var0.getPaddingLeft(), var0.getPaddingTop(), var2, var0.getPaddingBottom());
   }

   public static void setPaddingStart(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      if (VERSION.SDK_INT >= 17) {
         var0.setPaddingRelative(var2, var0.getPaddingTop(), var0.getPaddingEnd(), var0.getPaddingBottom());
      } else {
         var0.setPadding(var2, var0.getPaddingTop(), var0.getPaddingRight(), var0.getPaddingBottom());
      }
   }

   public static void setPaddingTop(View var0, float var1) {
      int var2 = pixelsToDimensionPixelSize(var1);
      var0.setPadding(var0.getPaddingLeft(), var2, var0.getPaddingRight(), var0.getPaddingBottom());
   }

   public static void setRequiresFadingEdge(View var0, int var1) {
      boolean var3 = false;
      boolean var2;
      if ((var1 & 2) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if ((var1 & 1) != 0) {
         var3 = true;
      }

      var0.setVerticalFadingEdgeEnabled(var2);
      var0.setHorizontalFadingEdgeEnabled(var3);
   }

   public interface OnViewAttachedToWindow {
      void onViewAttachedToWindow(View var1);
   }

   public interface OnViewDetachedFromWindow {
      void onViewDetachedFromWindow(View var1);
   }
}
