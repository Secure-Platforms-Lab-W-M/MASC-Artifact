package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import java.io.File;

@TargetApi(11)
@RequiresApi(11)
class ContextCompatHoneycomb {
   public static File getObbDir(Context var0) {
      return var0.getObbDir();
   }

   static void startActivities(Context var0, Intent[] var1) {
      var0.startActivities(var1);
   }
}
