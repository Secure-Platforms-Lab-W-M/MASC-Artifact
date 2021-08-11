package androidx.appcompat.widget;

import android.os.Build.VERSION;
import android.view.View;

public class TooltipCompat {
   private TooltipCompat() {
   }

   public static void setTooltipText(View var0, CharSequence var1) {
      if (VERSION.SDK_INT >= 26) {
         var0.setTooltipText(var1);
      } else {
         TooltipCompatHandler.setTooltipText(var0, var1);
      }
   }
}
