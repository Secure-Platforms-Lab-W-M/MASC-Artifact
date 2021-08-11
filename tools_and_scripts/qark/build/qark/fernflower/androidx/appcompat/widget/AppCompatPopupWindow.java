package androidx.appcompat.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;
import androidx.appcompat.R.styleable;
import androidx.core.widget.PopupWindowCompat;

class AppCompatPopupWindow extends PopupWindow {
   private static final boolean COMPAT_OVERLAP_ANCHOR;
   private boolean mOverlapAnchor;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      COMPAT_OVERLAP_ANCHOR = var0;
   }

   public AppCompatPopupWindow(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var1, var2, var3, 0);
   }

   public AppCompatPopupWindow(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.init(var1, var2, var3, var4);
   }

   private void init(Context var1, AttributeSet var2, int var3, int var4) {
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.PopupWindow, var3, var4);
      if (var5.hasValue(styleable.PopupWindow_overlapAnchor)) {
         this.setSupportOverlapAnchor(var5.getBoolean(styleable.PopupWindow_overlapAnchor, false));
      }

      this.setBackgroundDrawable(var5.getDrawable(styleable.PopupWindow_android_popupBackground));
      var5.recycle();
   }

   private void setSupportOverlapAnchor(boolean var1) {
      if (COMPAT_OVERLAP_ANCHOR) {
         this.mOverlapAnchor = var1;
      } else {
         PopupWindowCompat.setOverlapAnchor(this, var1);
      }
   }

   public void showAsDropDown(View var1, int var2, int var3) {
      int var4 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var4 = var3;
         if (this.mOverlapAnchor) {
            var4 = var3 - var1.getHeight();
         }
      }

      super.showAsDropDown(var1, var2, var4);
   }

   public void showAsDropDown(View var1, int var2, int var3, int var4) {
      int var5 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var5 = var3;
         if (this.mOverlapAnchor) {
            var5 = var3 - var1.getHeight();
         }
      }

      super.showAsDropDown(var1, var2, var5, var4);
   }

   public void update(View var1, int var2, int var3, int var4, int var5) {
      int var6 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var6 = var3;
         if (this.mOverlapAnchor) {
            var6 = var3 - var1.getHeight();
         }
      }

      super.update(var1, var2, var6, var4, var5);
   }
}
