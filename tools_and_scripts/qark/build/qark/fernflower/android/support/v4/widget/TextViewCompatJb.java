package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@TargetApi(16)
@RequiresApi(16)
class TextViewCompatJb {
   static int getMaxLines(TextView var0) {
      return var0.getMaxLines();
   }

   static int getMinLines(TextView var0) {
      return var0.getMinLines();
   }
}
