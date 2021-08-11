package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;

class NotificationManagerCompatEclair {
   static void cancelNotification(NotificationManager var0, String var1, int var2) {
      var0.cancel(var1, var2);
   }

   public static void postNotification(NotificationManager var0, String var1, int var2, Notification var3) {
      var0.notify(var1, var2, var3);
   }
}
