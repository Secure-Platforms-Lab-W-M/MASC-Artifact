/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

public class AppLaunchChecker {
    private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
    private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";

    public static boolean hasStartedFromLauncher(Context context) {
        return context.getSharedPreferences("android.support.AppLaunchChecker", 0).getBoolean("startedFromLauncher", false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void onActivityCreate(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("android.support.AppLaunchChecker", 0);
        if (sharedPreferences.getBoolean("startedFromLauncher", false) || (activity = activity.getIntent()) == null || !"android.intent.action.MAIN".equals(activity.getAction()) || !activity.hasCategory("android.intent.category.LAUNCHER") && !activity.hasCategory("android.intent.category.LEANBACK_LAUNCHER")) {
            return;
        }
        SharedPreferencesCompat.EditorCompat.getInstance().apply(sharedPreferences.edit().putBoolean("startedFromLauncher", true));
    }
}

