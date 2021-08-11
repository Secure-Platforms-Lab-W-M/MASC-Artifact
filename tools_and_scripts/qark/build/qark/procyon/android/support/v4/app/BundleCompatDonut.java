// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.os.IBinder;
import android.os.Bundle;
import java.lang.reflect.Method;

class BundleCompatDonut
{
    private static final String TAG = "BundleCompatDonut";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;
    
    public static IBinder getBinder(final Bundle bundle, final String s) {
        if (!BundleCompatDonut.sGetIBinderMethodFetched) {
            try {
                (BundleCompatDonut.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("BundleCompatDonut", "Failed to retrieve getIBinder method", (Throwable)ex);
            }
            BundleCompatDonut.sGetIBinderMethodFetched = true;
        }
        if (BundleCompatDonut.sGetIBinderMethod != null) {
            try {
                return (IBinder)BundleCompatDonut.sGetIBinderMethod.invoke(bundle, s);
            }
            catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                final Throwable t;
                Log.i("BundleCompatDonut", "Failed to invoke getIBinder via reflection", t);
                BundleCompatDonut.sGetIBinderMethod = null;
            }
        }
        return null;
    }
    
    public static void putBinder(final Bundle bundle, final String s, final IBinder binder) {
        if (!BundleCompatDonut.sPutIBinderMethodFetched) {
            try {
                (BundleCompatDonut.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("BundleCompatDonut", "Failed to retrieve putIBinder method", (Throwable)ex);
            }
            BundleCompatDonut.sPutIBinderMethodFetched = true;
        }
        if (BundleCompatDonut.sPutIBinderMethod != null) {
            try {
                BundleCompatDonut.sPutIBinderMethod.invoke(bundle, s, binder);
            }
            catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                final Throwable t;
                Log.i("BundleCompatDonut", "Failed to invoke putIBinder via reflection", t);
                BundleCompatDonut.sPutIBinderMethod = null;
            }
        }
    }
}
