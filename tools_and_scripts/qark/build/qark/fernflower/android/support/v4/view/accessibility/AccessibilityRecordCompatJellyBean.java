package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityRecordCompatJellyBean {
   public static void setSource(Object var0, View var1, int var2) {
      ((AccessibilityRecord)var0).setSource(var1, var2);
   }
}
