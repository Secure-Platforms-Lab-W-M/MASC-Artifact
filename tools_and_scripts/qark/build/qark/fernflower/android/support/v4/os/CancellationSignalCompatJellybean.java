package android.support.v4.os;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class CancellationSignalCompatJellybean {
   public static void cancel(Object var0) {
      ((android.os.CancellationSignal)var0).cancel();
   }

   public static Object create() {
      return new android.os.CancellationSignal();
   }
}
