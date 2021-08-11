package android.support.v4.view;

import android.view.MenuItem;

public final class MenuCompat {
   private MenuCompat() {
   }

   @Deprecated
   public static void setShowAsAction(MenuItem var0, int var1) {
      var0.setShowAsAction(var1);
   }
}
