package android.support.v4.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class AlarmManagerCompatApi23 {
   static void setAndAllowWhileIdle(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      var0.setAndAllowWhileIdle(var1, var2, var4);
   }

   static void setExactAndAllowWhileIdle(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      var0.setExactAndAllowWhileIdle(var1, var2, var4);
   }
}
