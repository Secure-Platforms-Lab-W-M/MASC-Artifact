package androidx.core.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.WindowInsets;

public class WindowInsetsCompat {
   private final Object mInsets;

   public WindowInsetsCompat(WindowInsetsCompat var1) {
      int var2 = VERSION.SDK_INT;
      Object var3 = null;
      if (var2 >= 20) {
         WindowInsets var4;
         if (var1 == null) {
            var4 = (WindowInsets)var3;
         } else {
            var4 = new WindowInsets((WindowInsets)var1.mInsets);
         }

         this.mInsets = var4;
      } else {
         this.mInsets = null;
      }
   }

   private WindowInsetsCompat(Object var1) {
      this.mInsets = var1;
   }

   static Object unwrap(WindowInsetsCompat var0) {
      return var0 == null ? null : var0.mInsets;
   }

   static WindowInsetsCompat wrap(Object var0) {
      return var0 == null ? null : new WindowInsetsCompat(var0);
   }

   public WindowInsetsCompat consumeDisplayCutout() {
      return VERSION.SDK_INT >= 28 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeDisplayCutout()) : this;
   }

   public WindowInsetsCompat consumeStableInsets() {
      return VERSION.SDK_INT >= 21 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeStableInsets()) : null;
   }

   public WindowInsetsCompat consumeSystemWindowInsets() {
      return VERSION.SDK_INT >= 20 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeSystemWindowInsets()) : null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            WindowInsetsCompat var3 = (WindowInsetsCompat)var1;
            Object var2 = this.mInsets;
            if (var2 == null) {
               return var3.mInsets == null;
            } else {
               return var2.equals(var3.mInsets);
            }
         }
      } else {
         return false;
      }
   }

   public DisplayCutoutCompat getDisplayCutout() {
      return VERSION.SDK_INT >= 28 ? DisplayCutoutCompat.wrap(((WindowInsets)this.mInsets).getDisplayCutout()) : null;
   }

   public int getStableInsetBottom() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).getStableInsetBottom() : 0;
   }

   public int getStableInsetLeft() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).getStableInsetLeft() : 0;
   }

   public int getStableInsetRight() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).getStableInsetRight() : 0;
   }

   public int getStableInsetTop() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).getStableInsetTop() : 0;
   }

   public int getSystemWindowInsetBottom() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetBottom() : 0;
   }

   public int getSystemWindowInsetLeft() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetLeft() : 0;
   }

   public int getSystemWindowInsetRight() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetRight() : 0;
   }

   public int getSystemWindowInsetTop() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetTop() : 0;
   }

   public boolean hasInsets() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).hasInsets() : false;
   }

   public boolean hasStableInsets() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).hasStableInsets() : false;
   }

   public boolean hasSystemWindowInsets() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).hasSystemWindowInsets() : false;
   }

   public int hashCode() {
      Object var1 = this.mInsets;
      return var1 == null ? 0 : var1.hashCode();
   }

   public boolean isConsumed() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).isConsumed() : false;
   }

   public boolean isRound() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).isRound() : false;
   }

   public WindowInsetsCompat replaceSystemWindowInsets(int var1, int var2, int var3, int var4) {
      return VERSION.SDK_INT >= 20 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(var1, var2, var3, var4)) : null;
   }

   public WindowInsetsCompat replaceSystemWindowInsets(Rect var1) {
      return VERSION.SDK_INT >= 21 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(var1)) : null;
   }
}
