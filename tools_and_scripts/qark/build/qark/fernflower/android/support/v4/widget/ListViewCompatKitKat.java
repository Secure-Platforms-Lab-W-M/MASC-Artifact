package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.ListView;

@TargetApi(19)
@RequiresApi(19)
class ListViewCompatKitKat {
   static void scrollListBy(ListView var0, int var1) {
      var0.scrollListBy(var1);
   }
}
