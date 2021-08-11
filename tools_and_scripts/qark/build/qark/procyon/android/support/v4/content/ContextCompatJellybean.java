// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class ContextCompatJellybean
{
    public static void startActivities(final Context context, final Intent[] array, final Bundle bundle) {
        context.startActivities(array, bundle);
    }
    
    public static void startActivity(final Context context, final Intent intent, final Bundle bundle) {
        context.startActivity(intent, bundle);
    }
}
