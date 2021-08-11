// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class TaskStackBuilderJellybean
{
    public static PendingIntent getActivitiesPendingIntent(final Context context, final int n, final Intent[] array, final int n2, final Bundle bundle) {
        return PendingIntent.getActivities(context, n, array, n2, bundle);
    }
}
