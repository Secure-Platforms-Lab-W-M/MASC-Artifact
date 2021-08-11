// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.net.Uri;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(22)
@RequiresApi(22)
class ActivityCompatApi22
{
    public static Uri getReferrer(final Activity activity) {
        return activity.getReferrer();
    }
}
