package androidx.core.view;

import android.os.Build.VERSION;
import android.view.Menu;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenu;

public final class MenuCompat {
   private MenuCompat() {
   }

   public static void setGroupDividerEnabled(Menu var0, boolean var1) {
      if (var0 instanceof SupportMenu) {
         ((SupportMenu)var0).setGroupDividerEnabled(var1);
      } else {
         if (VERSION.SDK_INT >= 28) {
            var0.setGroupDividerEnabled(var1);
         }

      }
   }

   @Deprecated
   public static void setShowAsAction(MenuItem var0, int var1) {
      var0.setShowAsAction(var1);
   }
}
