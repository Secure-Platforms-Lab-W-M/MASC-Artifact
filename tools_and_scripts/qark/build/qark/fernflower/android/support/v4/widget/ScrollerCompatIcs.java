package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.OverScroller;

@TargetApi(14)
@RequiresApi(14)
class ScrollerCompatIcs {
   public static float getCurrVelocity(Object var0) {
      return ((OverScroller)var0).getCurrVelocity();
   }
}
