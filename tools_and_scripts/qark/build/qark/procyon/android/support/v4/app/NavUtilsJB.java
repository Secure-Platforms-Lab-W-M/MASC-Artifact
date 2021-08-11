// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class NavUtilsJB
{
    public static Intent getParentActivityIntent(final Activity activity) {
        return activity.getParentActivityIntent();
    }
    
    public static String getParentActivityName(final ActivityInfo activityInfo) {
        return activityInfo.parentActivityName;
    }
    
    public static void navigateUpTo(final Activity activity, final Intent intent) {
        activity.navigateUpTo(intent);
    }
    
    public static boolean shouldUpRecreateTask(final Activity activity, final Intent intent) {
        return activity.shouldUpRecreateTask(intent);
    }
}
