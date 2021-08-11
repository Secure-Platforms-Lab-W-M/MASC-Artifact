package android.support.v4.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.AlarmManager.AlarmClockInfo;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class AlarmManagerCompatApi21 {
   static void setAlarmClock(AlarmManager var0, long var1, PendingIntent var3, PendingIntent var4) {
      var0.setAlarmClock(new AlarmClockInfo(var1, var3), var4);
   }
}
