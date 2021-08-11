package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class PrintHelperApi23 extends PrintHelperApi20 {
   PrintHelperApi23(Context var1) {
      super(var1);
      this.mIsMinMarginsHandlingCorrect = false;
   }

   protected Builder copyAttributes(PrintAttributes var1) {
      Builder var2 = super.copyAttributes(var1);
      if (var1.getDuplexMode() != 0) {
         var2.setDuplexMode(var1.getDuplexMode());
      }

      return var2;
   }
}
