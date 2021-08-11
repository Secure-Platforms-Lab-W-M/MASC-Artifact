package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ContextCompatJellybean {
   public static void startActivities(Context var0, Intent[] var1, Bundle var2) {
      var0.startActivities(var1, var2);
   }

   public static void startActivity(Context var0, Intent var1, Bundle var2) {
      var0.startActivity(var1, var2);
   }
}
