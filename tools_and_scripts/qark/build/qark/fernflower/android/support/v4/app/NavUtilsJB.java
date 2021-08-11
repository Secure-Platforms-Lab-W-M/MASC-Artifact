package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class NavUtilsJB {
   public static Intent getParentActivityIntent(Activity var0) {
      return var0.getParentActivityIntent();
   }

   public static String getParentActivityName(ActivityInfo var0) {
      return var0.parentActivityName;
   }

   public static void navigateUpTo(Activity var0, Intent var1) {
      var0.navigateUpTo(var1);
   }

   public static boolean shouldUpRecreateTask(Activity var0, Intent var1) {
      return var0.shouldUpRecreateTask(var1);
   }
}
