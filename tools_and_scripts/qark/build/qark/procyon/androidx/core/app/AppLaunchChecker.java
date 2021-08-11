// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Context;

public class AppLaunchChecker
{
    private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
    private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";
    
    @Deprecated
    public AppLaunchChecker() {
    }
    
    public static boolean hasStartedFromLauncher(final Context context) {
        return context.getSharedPreferences("android.support.AppLaunchChecker", 0).getBoolean("startedFromLauncher", false);
    }
    
    public static void onActivityCreate(final Activity activity) {
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("android.support.AppLaunchChecker", 0);
        if (sharedPreferences.getBoolean("startedFromLauncher", false)) {
            return;
        }
        final Intent intent = activity.getIntent();
        if (intent == null) {
            return;
        }
        if ("android.intent.action.MAIN".equals(intent.getAction()) && (intent.hasCategory("android.intent.category.LAUNCHER") || intent.hasCategory("android.intent.category.LEANBACK_LAUNCHER"))) {
            sharedPreferences.edit().putBoolean("startedFromLauncher", true).apply();
        }
    }
}
