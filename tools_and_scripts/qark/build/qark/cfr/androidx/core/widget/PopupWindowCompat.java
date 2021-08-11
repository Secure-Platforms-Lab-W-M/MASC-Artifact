/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.View
 *  android.widget.PopupWindow
 */
package androidx.core.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;

    private PopupWindowCompat() {
    }

    public static boolean getOverlapAnchor(PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT >= 23) {
            return popupWindow.getOverlapAnchor();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Field field;
            if (!sOverlapAnchorFieldAttempted) {
                try {
                    sOverlapAnchorField = field = PopupWindow.class.getDeclaredField("mOverlapAnchor");
                    field.setAccessible(true);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    Log.i((String)"PopupWindowCompatApi21", (String)"Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)noSuchFieldException);
                }
                sOverlapAnchorFieldAttempted = true;
            }
            if ((field = sOverlapAnchorField) != null) {
                try {
                    boolean bl = (Boolean)field.get((Object)popupWindow);
                    return bl;
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.i((String)"PopupWindowCompatApi21", (String)"Could not get overlap anchor field in PopupWindow", (Throwable)illegalAccessException);
                }
            }
        }
        return false;
    }

    public static int getWindowLayoutType(PopupWindow popupWindow) {
        Method method;
        if (Build.VERSION.SDK_INT >= 23) {
            return popupWindow.getWindowLayoutType();
        }
        if (!sGetWindowLayoutTypeMethodAttempted) {
            try {
                sGetWindowLayoutTypeMethod = method = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", new Class[0]);
                method.setAccessible(true);
            }
            catch (Exception exception) {
                // empty catch block
            }
            sGetWindowLayoutTypeMethodAttempted = true;
        }
        if ((method = sGetWindowLayoutTypeMethod) != null) {
            try {
                int n = (Integer)method.invoke((Object)popupWindow, new Object[0]);
                return n;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return 0;
    }

    public static void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
        if (Build.VERSION.SDK_INT >= 23) {
            popupWindow.setOverlapAnchor(bl);
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Field field;
            if (!sOverlapAnchorFieldAttempted) {
                try {
                    sOverlapAnchorField = field = PopupWindow.class.getDeclaredField("mOverlapAnchor");
                    field.setAccessible(true);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    Log.i((String)"PopupWindowCompatApi21", (String)"Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)noSuchFieldException);
                }
                sOverlapAnchorFieldAttempted = true;
            }
            if ((field = sOverlapAnchorField) != null) {
                try {
                    field.set((Object)popupWindow, bl);
                    return;
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.i((String)"PopupWindowCompatApi21", (String)"Could not set overlap anchor field in PopupWindow", (Throwable)illegalAccessException);
                }
            }
        }
    }

    public static void setWindowLayoutType(PopupWindow popupWindow, int n) {
        Method method;
        if (Build.VERSION.SDK_INT >= 23) {
            popupWindow.setWindowLayoutType(n);
            return;
        }
        if (!sSetWindowLayoutTypeMethodAttempted) {
            try {
                sSetWindowLayoutTypeMethod = method = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE);
                method.setAccessible(true);
            }
            catch (Exception exception) {
                // empty catch block
            }
            sSetWindowLayoutTypeMethodAttempted = true;
        }
        if ((method = sSetWindowLayoutTypeMethod) != null) {
            try {
                method.invoke((Object)popupWindow, n);
                return;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static void showAsDropDown(PopupWindow popupWindow, View view, int n, int n2, int n3) {
        if (Build.VERSION.SDK_INT >= 19) {
            popupWindow.showAsDropDown(view, n, n2, n3);
            return;
        }
        int n4 = n;
        if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 7) == 5) {
            n4 = n - (popupWindow.getWidth() - view.getWidth());
        }
        popupWindow.showAsDropDown(view, n4, n2);
    }
}

