// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.app.AppOpsManager;
import android.os.Build$VERSION;
import android.content.Context;

public final class AppOpsManagerCompat
{
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;
    
    private AppOpsManagerCompat() {
    }
    
    public static int noteOp(final Context context, final String s, final int n, final String s2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOp(s, n, s2);
        }
        return 1;
    }
    
    public static int noteOpNoThrow(final Context context, final String s, final int n, final String s2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOpNoThrow(s, n, s2);
        }
        return 1;
    }
    
    public static int noteProxyOp(final Context context, final String s, final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOp(s, s2);
        }
        return 1;
    }
    
    public static int noteProxyOpNoThrow(final Context context, final String s, final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOpNoThrow(s, s2);
        }
        return 1;
    }
    
    public static String permissionToOp(final String s) {
        if (Build$VERSION.SDK_INT >= 23) {
            return AppOpsManager.permissionToOp(s);
        }
        return null;
    }
}
