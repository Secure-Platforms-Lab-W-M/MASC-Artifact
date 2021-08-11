package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;
import com.google.android.material.R.style;

public class BottomSheetDialog extends AppCompatDialog {
   private BottomSheetBehavior behavior;
   private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
   boolean cancelable;
   private boolean canceledOnTouchOutside;
   private boolean canceledOnTouchOutsideSet;
   private FrameLayout container;
   boolean dismissWithAnimation;

   public BottomSheetDialog(Context var1) {
      this(var1, 0);
   }

   public BottomSheetDialog(Context var1, int var2) {
      super(var1, getThemeResId(var1, var2));
      this.cancelable = true;
      this.canceledOnTouchOutside = true;
      this.bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
         public void onSlide(View var1, float var2) {
         }

         public void onStateChanged(View var1, int var2) {
            if (var2 == 5) {
               BottomSheetDialog.this.cancel();
            }

         }
      };
      this.supportRequestWindowFeature(1);
   }

   protected BottomSheetDialog(Context var1, boolean var2, OnCancelListener var3) {
      super(var1, var2, var3);
      this.cancelable = true;
      this.canceledOnTouchOutside = true;
      this.bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
         public void onSlide(View var1, float var2) {
         }

         public void onStateChanged(View var1, int var2) {
            if (var2 == 5) {
               BottomSheetDialog.this.cancel();
            }

         }
      };
      this.supportRequestWindowFeature(1);
      this.cancelable = var2;
   }

   private FrameLayout ensureContainerAndBehavior() {
      if (this.container == null) {
         FrameLayout var1 = (FrameLayout)View.inflate(this.getContext(), layout.design_bottom_sheet_dialog, (ViewGroup)null);
         this.container = var1;
         BottomSheetBehavior var2 = BottomSheetBehavior.from((FrameLayout)var1.findViewById(id.design_bottom_sheet));
         this.behavior = var2;
         var2.addBottomSheetCallback(this.bottomSheetCallback);
         this.behavior.setHideable(this.cancelable);
      }

      return this.container;
   }

   private static int getThemeResId(Context var0, int var1) {
      int var2 = var1;
      if (var1 == 0) {
         TypedValue var3 = new TypedValue();
         if (var0.getTheme().resolveAttribute(attr.bottomSheetDialogTheme, var3, true)) {
            return var3.resourceId;
         }

         var2 = style.Theme_Design_Light_BottomSheetDialog;
      }

      return var2;
   }

   private View wrapInBottomSheet(int var1, View var2, LayoutParams var3) {
      this.ensureContainerAndBehavior();
      CoordinatorLayout var5 = (CoordinatorLayout)this.container.findViewById(id.coordinator);
      View var4 = var2;
      if (var1 != 0) {
         var4 = var2;
         if (var2 == null) {
            var4 = this.getLayoutInflater().inflate(var1, var5, false);
         }
      }

      FrameLayout var6 = (FrameLayout)this.container.findViewById(id.design_bottom_sheet);
      if (var3 == null) {
         var6.addView(var4);
      } else {
         var6.addView(var4, var3);
      }

      var5.findViewById(id.touch_outside).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
               BottomSheetDialog.this.cancel();
            }

         }
      });
      ViewCompat.setAccessibilityDelegate(var6, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            if (BottomSheetDialog.this.cancelable) {
               var2.addAction(1048576);
               var2.setDismissable(true);
            } else {
               var2.setDismissable(false);
            }
         }

         public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
            if (var2 == 1048576 && BottomSheetDialog.this.cancelable) {
               BottomSheetDialog.this.cancel();
               return true;
            } else {
               return super.performAccessibilityAction(var1, var2, var3);
            }
         }
      });
      var6.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            return true;
         }
      });
      return this.container;
   }

   public void cancel() {
      BottomSheetBehavior var1 = this.getBehavior();
      if (this.dismissWithAnimation && var1.getState() != 5) {
         var1.setState(5);
      } else {
         super.cancel();
      }
   }

   public BottomSheetBehavior getBehavior() {
      if (this.behavior == null) {
         this.ensureContainerAndBehavior();
      }

      return this.behavior;
   }

   public boolean getDismissWithAnimation() {
      return this.dismissWithAnimation;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Window var2 = this.getWindow();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 21) {
            var2.clearFlags(67108864);
            var2.addFlags(Integer.MIN_VALUE);
         }

         var2.setLayout(-1, -1);
      }

   }

   protected void onStart() {
      super.onStart();
      BottomSheetBehavior var1 = this.behavior;
      if (var1 != null && var1.getState() == 5) {
         this.behavior.setState(4);
      }

   }

   void removeDefaultCallback() {
      this.behavior.removeBottomSheetCallback(this.bottomSheetCallback);
   }

   public void setCancelable(boolean var1) {
      super.setCancelable(var1);
      if (this.cancelable != var1) {
         this.cancelable = var1;
         BottomSheetBehavior var2 = this.behavior;
         if (var2 != null) {
            var2.setHideable(var1);
         }
      }

   }

   public void setCanceledOnTouchOutside(boolean var1) {
      super.setCanceledOnTouchOutside(var1);
      if (var1 && !this.cancelable) {
         this.cancelable = true;
      }

      this.canceledOnTouchOutside = var1;
      this.canceledOnTouchOutsideSet = true;
   }

   public void setContentView(int var1) {
      super.setContentView(this.wrapInBottomSheet(var1, (View)null, (LayoutParams)null));
   }

   public void setContentView(View var1) {
      super.setContentView(this.wrapInBottomSheet(0, var1, (LayoutParams)null));
   }

   public void setContentView(View var1, LayoutParams var2) {
      super.setContentView(this.wrapInBottomSheet(0, var1, var2));
   }

   public void setDismissWithAnimation(boolean var1) {
      this.dismissWithAnimation = var1;
   }

   boolean shouldWindowCloseOnTouchOutside() {
      if (!this.canceledOnTouchOutsideSet) {
         TypedArray var1 = this.getContext().obtainStyledAttributes(new int[]{16843611});
         this.canceledOnTouchOutside = var1.getBoolean(0, true);
         var1.recycle();
         this.canceledOnTouchOutsideSet = true;
      }

      return this.canceledOnTouchOutside;
   }
}
