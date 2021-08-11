package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class NotificationManagerCompatApi24 {
   public static boolean areNotificationsEnabled(NotificationManager var0) {
      return var0.areNotificationsEnabled();
   }

   public static int getImportance(NotificationManager var0) {
      return var0.getImportance();
   }
}
