package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(20)
@RequiresApi(20)
class PrintHelperApi20 extends PrintHelperKitkat {
   PrintHelperApi20(Context var1) {
      super(var1);
      this.mPrintActivityRespectsOrientation = false;
   }
}
