package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

public class AppLaunchChecker {
   private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
   private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";

   public static boolean hasStartedFromLauncher(Context var0) {
      return var0.getSharedPreferences("android.support.AppLaunchChecker", 0).getBoolean("startedFromLauncher", false);
   }

   public static void onActivityCreate(Activity var0) {
      SharedPreferences var1 = var0.getSharedPreferences("android.support.AppLaunchChecker", 0);
      if (!var1.getBoolean("startedFromLauncher", false)) {
         Intent var2 = var0.getIntent();
         if (var2 != null) {
            if ("android.intent.action.MAIN".equals(var2.getAction()) && (var2.hasCategory("android.intent.category.LAUNCHER") || var2.hasCategory("android.intent.category.LEANBACK_LAUNCHER"))) {
               SharedPreferencesCompat.EditorCompat.getInstance().apply(var1.edit().putBoolean("startedFromLauncher", true));
            }

         }
      }
   }
}
