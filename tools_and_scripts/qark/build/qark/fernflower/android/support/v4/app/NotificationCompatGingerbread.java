package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

class NotificationCompatGingerbread {
   public static Notification add(Notification var0, Context var1, CharSequence var2, CharSequence var3, PendingIntent var4, PendingIntent var5) {
      var0.setLatestEventInfo(var1, var2, var3, var4);
      var0.fullScreenIntent = var5;
      return var0;
   }
}
