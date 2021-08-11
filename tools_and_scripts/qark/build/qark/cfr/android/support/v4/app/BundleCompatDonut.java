/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BundleCompatDonut {
    private static final String TAG = "BundleCompatDonut";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;

    BundleCompatDonut() {
    }

    public static IBinder getBinder(Bundle bundle, String string) {
        if (!sGetIBinderMethodFetched) {
            try {
                sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class);
                sGetIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to retrieve getIBinder method", (Throwable)noSuchMethodException);
            }
            sGetIBinderMethodFetched = true;
        }
        if (sGetIBinderMethod != null) {
            try {
                bundle = (IBinder)sGetIBinderMethod.invoke((Object)bundle, string);
                return bundle;
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to invoke getIBinder via reflection", (Throwable)exception);
                sGetIBinderMethod = null;
            }
        }
        return null;
    }

    public static void putBinder(Bundle bundle, String string, IBinder iBinder) {
        if (!sPutIBinderMethodFetched) {
            try {
                sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
                sPutIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to retrieve putIBinder method", (Throwable)noSuchMethodException);
            }
            sPutIBinderMethodFetched = true;
        }
        if (sPutIBinderMethod != null) {
            try {
                sPutIBinderMethod.invoke((Object)bundle, new Object[]{string, iBinder});
                return;
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to invoke putIBinder via reflection", (Throwable)exception);
                sPutIBinderMethod = null;
            }
        }
    }
}

