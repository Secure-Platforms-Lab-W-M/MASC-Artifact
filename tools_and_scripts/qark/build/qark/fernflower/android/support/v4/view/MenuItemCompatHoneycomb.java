package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;

@TargetApi(11)
@RequiresApi(11)
class MenuItemCompatHoneycomb {
   public static View getActionView(MenuItem var0) {
      return var0.getActionView();
   }

   public static MenuItem setActionView(MenuItem var0, int var1) {
      return var0.setActionView(var1);
   }

   public static MenuItem setActionView(MenuItem var0, View var1) {
      return var0.setActionView(var1);
   }

   public static void setShowAsAction(MenuItem var0, int var1) {
      var0.setShowAsAction(var1);
   }
}
