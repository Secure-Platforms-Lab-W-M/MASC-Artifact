/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;

@TargetApi(value=11)
@RequiresApi(value=11)
class TaskStackBuilderHoneycomb {
    TaskStackBuilderHoneycomb() {
    }

    public static PendingIntent getActivitiesPendingIntent(Context context, int n, Intent[] arrintent, int n2) {
        return PendingIntent.getActivities((Context)context, (int)n, (Intent[])arrintent, (int)n2);
    }
}

