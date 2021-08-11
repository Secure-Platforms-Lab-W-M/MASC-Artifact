/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
    private BundleCompat() {
    }

    public static IBinder getBinder(Bundle bundle, String string2) {
        if (Build.VERSION.SDK_INT >= 18) {
            return bundle.getBinder(string2);
        }
        return BundleCompatBaseImpl.getBinder(bundle, string2);
    }

    public static void putBinder(Bundle bundle, String string2, IBinder iBinder) {
        if (Build.VERSION.SDK_INT >= 18) {
            bundle.putBinder(string2, iBinder);
            return;
        }
        BundleCompatBaseImpl.putBinder(bundle, string2, iBinder);
    }

    static class BundleCompatBaseImpl {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;

        BundleCompatBaseImpl() {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public static IBinder getBinder(Bundle var0, String var1_5) {
            if (!BundleCompatBaseImpl.sGetIBinderMethodFetched) {
                try {
                    BundleCompatBaseImpl.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});
                    BundleCompatBaseImpl.sGetIBinderMethod.setAccessible(true);
                }
                catch (NoSuchMethodException var2_6) {
                    Log.i((String)"BundleCompatBaseImpl", (String)"Failed to retrieve getIBinder method", (Throwable)var2_6);
                }
                BundleCompatBaseImpl.sGetIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sGetIBinderMethod == null) return null;
            try {
                return (IBinder)BundleCompatBaseImpl.sGetIBinderMethod.invoke((Object)var0, new Object[]{var1_5});
            }
            catch (IllegalAccessException var0_1) {}
            ** GOTO lbl-1000
            catch (IllegalArgumentException var0_3) {
                ** GOTO lbl-1000
            }
            catch (InvocationTargetException var0_4) {}
lbl-1000: // 3 sources:
            {
                Log.i((String)"BundleCompatBaseImpl", (String)"Failed to invoke getIBinder via reflection", (Throwable)var0_2);
                BundleCompatBaseImpl.sGetIBinderMethod = null;
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
            if (!BundleCompatBaseImpl.sPutIBinderMethodFetched) {
                try {
                    BundleCompatBaseImpl.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[]{String.class, IBinder.class});
                    BundleCompatBaseImpl.sPutIBinderMethod.setAccessible(true);
                }
                catch (NoSuchMethodException var3_7) {
                    Log.i((String)"BundleCompatBaseImpl", (String)"Failed to retrieve putIBinder method", (Throwable)var3_7);
                }
                BundleCompatBaseImpl.sPutIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sPutIBinderMethod == null) return;
            try {
                BundleCompatBaseImpl.sPutIBinderMethod.invoke((Object)var0, new Object[]{var1_5, var2_6});
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
                Log.i((String)"BundleCompatBaseImpl", (String)"Failed to invoke putIBinder via reflection", (Throwable)var0_2);
                BundleCompatBaseImpl.sPutIBinderMethod = null;
                return;
            }
        }
    }

}

