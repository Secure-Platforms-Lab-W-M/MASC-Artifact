package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

@TargetApi(14)
@RequiresApi(14)
class ViewGroupCompatIcs {
   public static boolean onRequestSendAccessibilityEvent(ViewGroup var0, View var1, AccessibilityEvent var2) {
      return var0.onRequestSendAccessibilityEvent(var1, var2);
   }
}
