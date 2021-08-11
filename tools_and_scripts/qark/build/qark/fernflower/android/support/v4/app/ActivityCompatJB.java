package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ActivityCompatJB {
   public static void finishAffinity(Activity var0) {
      var0.finishAffinity();
   }

   public static void startActivityForResult(Activity var0, Intent var1, int var2, Bundle var3) {
      var0.startActivityForResult(var1, var2, var3);
   }

   public static void startIntentSenderForResult(Activity var0, IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      var0.startIntentSenderForResult(var1, var2, var3, var4, var5, var6, var7);
   }
}
