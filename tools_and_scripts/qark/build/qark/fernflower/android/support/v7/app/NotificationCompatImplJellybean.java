package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification.BigTextStyle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

@TargetApi(16)
@RequiresApi(16)
class NotificationCompatImplJellybean {
   public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1) {
      (new BigTextStyle(var0.getBuilder())).bigText(var1);
   }
}
