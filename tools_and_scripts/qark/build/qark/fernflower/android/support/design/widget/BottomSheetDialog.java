package android.support.design.widget;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.R$attr;
import android.support.design.R$id;
import android.support.design.R$layout;
import android.support.design.R$style;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class BottomSheetDialog extends AppCompatDialog {
   private BottomSheetBehavior mBehavior;
   private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback;
   boolean mCancelable;
   private boolean mCanceledOnTouchOutside;
   private boolean mCanceledOnTouchOutsideSet;

   public BottomSheetDialog(@NonNull Context var1) {
      this(var1, 0);
   }

   public BottomSheetDialog(@NonNull Context var1, @StyleRes int var2) {
      super(var1, getThemeResId(var1, var2));
      this.mCancelable = true;
      this.mCanceledOnTouchOutside = true;
      this.mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
         public void onSlide(@NonNull View var1, float var2) {
         }

         public void onStateChanged(@NonNull View var1, int var2) {
            if (var2 == 5) {
               BottomSheetDialog.this.cancel();
            }
         }
      };
      this.supportRequestWindowFeature(1);
   }

   protected BottomSheetDialog(@NonNull Context var1, boolean var2, OnCancelListener var3) {
      super(var1, var2, var3);
      this.mCancelable = true;
      this.mCanceledOnTouchOutside = true;
      this.mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
         public void onSlide(@NonNull View var1, float var2) {
         }

         public void onStateChanged(@NonNull View var1, int var2) {
            if (var2 == 5) {
               BottomSheetDialog.this.cancel();
            }
         }
      };
      this.supportRequestWindowFeature(1);
      this.mCancelable = var2;
   }

   private static int getThemeResId(Context var0, int var1) {
      if (var1 == 0) {
         TypedValue var2 = new TypedValue();
         return var0.getTheme().resolveAttribute(R$attr.bottomSheetDialogTheme, var2, true) ? var2.resourceId : R$style.Theme_Design_Light_BottomSheetDialog;
      } else {
         return var1;
      }
   }

   private View wrapInBottomSheet(int var1, View var2, LayoutParams var3) {
      FrameLayout var4 = (FrameLayout)View.inflate(this.getContext(), R$layout.design_bottom_sheet_dialog, (ViewGroup)null);
      CoordinatorLayout var5 = (CoordinatorLayout)var4.findViewById(R$id.coordinator);
      if (var1 != 0 && var2 == null) {
         var2 = this.getLayoutInflater().inflate(var1, var5, false);
      }

      FrameLayout var6 = (FrameLayout)var5.findViewById(R$id.design_bottom_sheet);
      this.mBehavior = BottomSheetBehavior.from(var6);
      this.mBehavior.setBottomSheetCallback(this.mBottomSheetCallback);
      this.mBehavior.setHideable(this.mCancelable);
      if (var3 == null) {
         var6.addView(var2);
      } else {
         var6.addView(var2, var3);
      }

      var5.findViewById(R$id.touch_outside).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (BottomSheetDialog.this.mCancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
               BottomSheetDialog.this.cancel();
            }
         }
      });
      ViewCompat.setAccessibilityDelegate(var6, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            if (BottomSheetDialog.this.mCancelable) {
               var2.addAction(1048576);
               var2.setDismissable(true);
            } else {
               var2.setDismissable(false);
            }
         }

         public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
            if (var2 == 1048576 && BottomSheetDialog.this.mCancelable) {
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
      return var4;
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
      BottomSheetBehavior var1 = this.mBehavior;
      if (var1 != null) {
         var1.setState(4);
      }
   }

   public void setCancelable(boolean var1) {
      super.setCancelable(var1);
      if (this.mCancelable != var1) {
         this.mCancelable = var1;
         BottomSheetBehavior var2 = this.mBehavior;
         if (var2 != null) {
            var2.setHideable(var1);
         }
      }
   }

   public void setCanceledOnTouchOutside(boolean var1) {
      super.setCanceledOnTouchOutside(var1);
      if (var1 && !this.mCancelable) {
         this.mCancelable = true;
      }

      this.mCanceledOnTouchOutside = var1;
      this.mCanceledOnTouchOutsideSet = true;
   }

   public void setContentView(@LayoutRes int var1) {
      super.setContentView(this.wrapInBottomSheet(var1, (View)null, (LayoutParams)null));
   }

   public void setContentView(View var1) {
      super.setContentView(this.wrapInBottomSheet(0, var1, (LayoutParams)null));
   }

   public void setContentView(View var1, LayoutParams var2) {
      super.setContentView(this.wrapInBottomSheet(0, var1, var2));
   }

   boolean shouldWindowCloseOnTouchOutside() {
      if (!this.mCanceledOnTouchOutsideSet) {
         if (VERSION.SDK_INT < 11) {
            this.mCanceledOnTouchOutside = true;
         } else {
            TypedArray var1 = this.getContext().obtainStyledAttributes(new int[]{16843611});
            this.mCanceledOnTouchOutside = var1.getBoolean(0, true);
            var1.recycle();
         }

         this.mCanceledOnTouchOutsideSet = true;
      }

      return this.mCanceledOnTouchOutside;
   }
}
