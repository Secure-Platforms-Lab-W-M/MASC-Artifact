/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.AppOpsManager
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(value=19)
@RequiresApi(value=19)
class NotificationManagerCompatKitKat {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    NotificationManagerCompatKitKat() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areNotificationsEnabled(Context object) {
        AppOpsManager appOpsManager = (AppOpsManager)object.getSystemService("appops");
        Object object2 = object.getApplicationInfo();
        object = object.getApplicationContext().getPackageName();
        int n = object2.uid;
        try {
            object2 = Class.forName(AppOpsManager.class.getName());
            n = (Integer)object2.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke((Object)appOpsManager, (int)((Integer)object2.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)), n, object);
            if (n != 0) return false;
            return true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            do {
                return true;
                break;
            } while (true);
        }
        catch (RuntimeException runtimeException) {
            return true;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            return true;
        }
        catch (IllegalAccessException illegalAccessException) {
            return true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return true;
        }
        catch (InvocationTargetException invocationTargetException) {
            return true;
        }
    }
}

