package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class AppOpsManagerCompat23 {
   public static int noteOp(Context var0, String var1, int var2, String var3) {
      return ((AppOpsManager)var0.getSystemService(AppOpsManager.class)).noteOp(var1, var2, var3);
   }

   public static int noteProxyOp(Context var0, String var1, String var2) {
      return ((AppOpsManager)var0.getSystemService(AppOpsManager.class)).noteProxyOp(var1, var2);
   }

   public static String permissionToOp(String var0) {
      return AppOpsManager.permissionToOp(var0);
   }
}
