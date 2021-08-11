/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(value=9)
@RequiresApi(value=9)
class BundleCompatGingerbread {
    private static final String TAG = "BundleCompatGingerbread";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;

    BundleCompatGingerbread() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static IBinder getBinder(Bundle var0, String var1_5) {
        if (!BundleCompatGingerbread.sGetIBinderMethodFetched) {
            try {
                BundleCompatGingerbread.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});
                BundleCompatGingerbread.sGetIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException var2_6) {
                Log.i((String)"BundleCompatGingerbread", (String)"Failed to retrieve getIBinder method", (Throwable)var2_6);
            }
            BundleCompatGingerbread.sGetIBinderMethodFetched = true;
        }
        if (BundleCompatGingerbread.sGetIBinderMethod == null) return null;
        try {
            return (IBinder)BundleCompatGingerbread.sGetIBinderMethod.invoke((Object)var0, new Object[]{var1_5});
        }
        catch (IllegalAccessException var0_1) {}
        ** GOTO lbl-1000
        catch (IllegalArgumentException var0_3) {
            ** GOTO lbl-1000
        }
        catch (InvocationTargetException var0_4) {}
lbl-1000: // 3 sources:
        {
            Log.i((String)"BundleCompatGingerbread", (String)"Failed to invoke getIBinder via reflection", (Throwable)var0_2);
            BundleCompatGingerbread.sGetIBinderMethod = null;
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void putBinder(Bundle var0, String var1_5, IBinder var2_6) {
        if (!BundleCompatGingerbread.sPutIBinderMethodFetched) {
            try {
                BundleCompatGingerbread.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[]{String.class, IBinder.class});
                BundleCompatGingerbread.sPutIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException var3_7) {
                Log.i((String)"BundleCompatGingerbread", (String)"Failed to retrieve putIBinder method", (Throwable)var3_7);
            }
            BundleCompatGingerbread.sPutIBinderMethodFetched = true;
        }
        if (BundleCompatGingerbread.sPutIBinderMethod == null) return;
        try {
            BundleCompatGingerbread.sPutIBinderMethod.invoke((Object)var0, new Object[]{var1_5, var2_6});
            return;
        }
        catch (IllegalAccessException var0_1) {}
        ** GOTO lbl-1000
        catch (IllegalArgumentException var0_3) {
            ** GOTO lbl-1000
        }
        catch (InvocationTargetException var0_4) {}
lbl-1000: // 3 sources:
        {
            Log.i((String)"BundleCompatGingerbread", (String)"Failed to invoke putIBinder via reflection", (Throwable)var0_2);
            BundleCompatGingerbread.sPutIBinderMethod = null;
            return;
        }
    }
}

