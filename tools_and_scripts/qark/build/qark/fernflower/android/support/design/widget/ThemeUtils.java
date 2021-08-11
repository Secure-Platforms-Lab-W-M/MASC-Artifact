package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R$attr;

class ThemeUtils {
   private static final int[] APPCOMPAT_CHECK_ATTRS;

   static {
      APPCOMPAT_CHECK_ATTRS = new int[]{R$attr.colorPrimary};
   }

   static void checkAppCompatTheme(Context var0) {
      TypedArray var2 = var0.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
      boolean var1 = var2.hasValue(0);
      var2.recycle();
      if (var1 ^ true) {
         throw new IllegalArgumentException("You need to use a Theme.AppCompat theme (or descendant) with the design library.");
      }
   }
}
