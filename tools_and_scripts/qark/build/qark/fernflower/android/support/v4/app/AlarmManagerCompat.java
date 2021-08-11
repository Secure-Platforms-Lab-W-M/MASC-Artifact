package android.support.v4.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.AlarmManager.AlarmClockInfo;
import android.os.Build.VERSION;

public final class AlarmManagerCompat {
   private AlarmManagerCompat() {
   }

   public static void setAlarmClock(AlarmManager var0, long var1, PendingIntent var3, PendingIntent var4) {
      if (VERSION.SDK_INT >= 21) {
         var0.setAlarmClock(new AlarmClockInfo(var1, var3), var4);
      } else {
         setExact(var0, 0, var1, var4);
      }
   }

   public static void setAndAllowWhileIdle(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      if (VERSION.SDK_INT >= 23) {
         var0.setAndAllowWhileIdle(var1, var2, var4);
      } else {
         var0.set(var1, var2, var4);
      }
   }

   public static void setExact(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      if (VERSION.SDK_INT >= 19) {
         var0.setExact(var1, var2, var4);
      } else {
         var0.set(var1, var2, var4);
      }
   }

   public static void setExactAndAllowWhileIdle(AlarmManager var0, int var1, long var2, PendingIntent var4) {
      if (VERSION.SDK_INT >= 23) {
         var0.setExactAndAllowWhileIdle(var1, var2, var4);
      } else {
         setExact(var0, var1, var2, var4);
      }
   }
}
