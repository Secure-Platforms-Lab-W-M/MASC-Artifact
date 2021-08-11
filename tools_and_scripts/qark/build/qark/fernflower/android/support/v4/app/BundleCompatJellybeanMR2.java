package android.support.v4.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class BundleCompatJellybeanMR2 {
   public static IBinder getBinder(Bundle var0, String var1) {
      return var0.getBinder(var1);
   }

   public static void putBinder(Bundle var0, String var1, IBinder var2) {
      var0.putBinder(var1, var2);
   }
}
