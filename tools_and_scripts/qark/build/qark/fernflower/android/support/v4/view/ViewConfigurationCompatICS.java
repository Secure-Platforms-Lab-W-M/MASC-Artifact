package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewConfiguration;

@TargetApi(14)
@RequiresApi(14)
class ViewConfigurationCompatICS {
   static boolean hasPermanentMenuKey(ViewConfiguration var0) {
      return var0.hasPermanentMenuKey();
   }
}
