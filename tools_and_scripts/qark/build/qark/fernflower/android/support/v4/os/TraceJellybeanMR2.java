package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Trace;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class TraceJellybeanMR2 {
   public static void beginSection(String var0) {
      Trace.beginSection(var0);
   }

   public static void endSection() {
      Trace.endSection();
   }
}
