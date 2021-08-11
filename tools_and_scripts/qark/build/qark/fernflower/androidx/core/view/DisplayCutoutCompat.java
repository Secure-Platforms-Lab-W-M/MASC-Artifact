package androidx.core.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.DisplayCutout;
import java.util.List;

public final class DisplayCutoutCompat {
   private final Object mDisplayCutout;

   public DisplayCutoutCompat(Rect var1, List var2) {
      DisplayCutout var3;
      if (VERSION.SDK_INT >= 28) {
         var3 = new DisplayCutout(var1, var2);
      } else {
         var3 = null;
      }

      this(var3);
   }

   private DisplayCutoutCompat(Object var1) {
      this.mDisplayCutout = var1;
   }

   static DisplayCutoutCompat wrap(Object var0) {
      return var0 == null ? null : new DisplayCutoutCompat(var0);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            DisplayCutoutCompat var3 = (DisplayCutoutCompat)var1;
            Object var2 = this.mDisplayCutout;
            if (var2 == null) {
               return var3.mDisplayCutout == null;
            } else {
               return var2.equals(var3.mDisplayCutout);
            }
         }
      } else {
         return false;
      }
   }

   public List getBoundingRects() {
      return VERSION.SDK_INT >= 28 ? ((DisplayCutout)this.mDisplayCutout).getBoundingRects() : null;
   }

   public int getSafeInsetBottom() {
      return VERSION.SDK_INT >= 28 ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom() : 0;
   }

   public int getSafeInsetLeft() {
      return VERSION.SDK_INT >= 28 ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft() : 0;
   }

   public int getSafeInsetRight() {
      return VERSION.SDK_INT >= 28 ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight() : 0;
   }

   public int getSafeInsetTop() {
      return VERSION.SDK_INT >= 28 ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop() : 0;
   }

   public int hashCode() {
      Object var1 = this.mDisplayCutout;
      return var1 == null ? 0 : var1.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DisplayCutoutCompat{");
      var1.append(this.mDisplayCutout);
      var1.append("}");
      return var1.toString();
   }
}
