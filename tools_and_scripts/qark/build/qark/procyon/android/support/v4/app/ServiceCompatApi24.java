// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.Service;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(24)
@RequiresApi(24)
class ServiceCompatApi24
{
    public static void stopForeground(final Service service, final int n) {
        service.stopForeground(n);
    }
}
