/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.AppOpsManager
 *  android.content.Context
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(value=23)
@RequiresApi(value=23)
class AppOpsManagerCompat23 {
    AppOpsManagerCompat23() {
    }

    public static int noteOp(Context context, String string2, int n, String string3) {
        return ((AppOpsManager)context.getSystemService(AppOpsManager.class)).noteOp(string2, n, string3);
    }

    public static int noteProxyOp(Context context, String string2, String string3) {
        return ((AppOpsManager)context.getSystemService(AppOpsManager.class)).noteProxyOp(string2, string3);
    }

    public static String permissionToOp(String string2) {
        return AppOpsManager.permissionToOp((String)string2);
    }
}

