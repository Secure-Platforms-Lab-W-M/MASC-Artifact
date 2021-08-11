// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class AppOpsManagerCompat23
{
    public static int noteOp(final Context context, final String s, final int n, final String s2) {
        return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteOp(s, n, s2);
    }
    
    public static int noteProxyOp(final Context context, final String s, final String s2) {
        return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOp(s, s2);
    }
    
    public static String permissionToOp(final String s) {
        return AppOpsManager.permissionToOp(s);
    }
}
