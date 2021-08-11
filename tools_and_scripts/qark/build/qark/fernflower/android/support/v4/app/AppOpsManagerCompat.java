package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class AppOpsManagerCompat {
   public static final int MODE_ALLOWED = 0;
   public static final int MODE_DEFAULT = 3;
   public static final int MODE_IGNORED = 1;

   private AppOpsManagerCompat() {
   }

   public static int noteOp(@NonNull Context var0, @NonNull String var1, int var2, @NonNull String var3) {
      return VERSION.SDK_INT >= 23 ? ((AppOpsManager)var0.getSystemService(AppOpsManager.class)).noteOp(var1, var2, var3) : 1;
   }

   public static int noteProxyOp(@NonNull Context var0, @NonNull String var1, @NonNull String var2) {
      return VERSION.SDK_INT >= 23 ? ((AppOpsManager)var0.getSystemService(AppOpsManager.class)).noteProxyOp(var1, var2) : 1;
   }

   public static String permissionToOp(@NonNull String var0) {
      return VERSION.SDK_INT >= 23 ? AppOpsManager.permissionToOp(var0) : null;
   }
}
