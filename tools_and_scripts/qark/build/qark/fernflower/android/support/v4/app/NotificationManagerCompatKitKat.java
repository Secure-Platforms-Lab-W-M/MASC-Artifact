package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.RequiresApi;
import java.lang.reflect.InvocationTargetException;

@TargetApi(19)
@RequiresApi(19)
class NotificationManagerCompatKitKat {
   private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
   private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

   public static boolean areNotificationsEnabled(Context var0) {
      AppOpsManager var2 = (AppOpsManager)var0.getSystemService("appops");
      ApplicationInfo var3 = var0.getApplicationInfo();
      String var10 = var0.getApplicationContext().getPackageName();
      int var1 = var3.uid;

      try {
         Class var11 = Class.forName(AppOpsManager.class.getName());
         var1 = (Integer)var11.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(var2, (Integer)var11.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class), var1, var10);
         return var1 == 0;
      } catch (ClassNotFoundException var4) {
      } catch (NoSuchMethodException var5) {
      } catch (NoSuchFieldException var6) {
      } catch (InvocationTargetException var7) {
      } catch (IllegalAccessException var8) {
      } catch (RuntimeException var9) {
      }

      return true;
   }
}
