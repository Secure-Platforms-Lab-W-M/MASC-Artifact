package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class PrintHelperApi24 extends PrintHelperApi23 {
   PrintHelperApi24(Context var1) {
      super(var1);
      this.mIsMinMarginsHandlingCorrect = true;
      this.mPrintActivityRespectsOrientation = true;
   }
}
