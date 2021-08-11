package android.support.v4.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AlarmManagerCompatKitKat {
   static void setExact(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      var0.setExact(var1, var2, var4);
   }
}
