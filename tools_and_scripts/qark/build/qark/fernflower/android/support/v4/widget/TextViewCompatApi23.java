package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.widget.TextView;

@TargetApi(23)
@RequiresApi(23)
class TextViewCompatApi23 {
   public static void setTextAppearance(@NonNull TextView var0, @StyleRes int var1) {
      var0.setTextAppearance(var1);
   }
}
