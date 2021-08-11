package android.support.design.widget;

import android.graphics.Outline;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class CircularBorderDrawableLollipop extends CircularBorderDrawable {
   public void getOutline(Outline var1) {
      this.copyBounds(this.mRect);
      var1.setOval(this.mRect);
   }
}
