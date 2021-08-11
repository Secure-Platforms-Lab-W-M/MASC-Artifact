package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewParent;

@TargetApi(19)
@RequiresApi(19)
class ViewParentCompatKitKat {
   public static void notifySubtreeAccessibilityStateChanged(ViewParent var0, View var1, View var2, int var3) {
      var0.notifySubtreeAccessibilityStateChanged(var1, var2, var3);
   }
}
