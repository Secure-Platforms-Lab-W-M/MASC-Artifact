/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.os.Build;
import android.view.ViewGroup;
import androidx.transition.ViewGroupOverlayApi14;
import androidx.transition.ViewGroupOverlayApi18;
import androidx.transition.ViewGroupOverlayImpl;
import androidx.transition.ViewGroupUtilsApi14;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtils {
    private static Method sGetChildDrawingOrderMethod;
    private static boolean sGetChildDrawingOrderMethodFetched;
    private static boolean sTryHiddenSuppressLayout;

    static {
        sTryHiddenSuppressLayout = true;
    }

    private ViewGroupUtils() {
    }

    static int getChildDrawingOrder(ViewGroup viewGroup, int n) {
        Method method;
        if (Build.VERSION.SDK_INT >= 29) {
            return viewGroup.getChildDrawingOrder(n);
        }
        if (!sGetChildDrawingOrderMethodFetched) {
            try {
                sGetChildDrawingOrderMethod = method = ViewGroup.class.getDeclaredMethod("getChildDrawingOrder", Integer.TYPE, Integer.TYPE);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            sGetChildDrawingOrderMethodFetched = true;
        }
        if ((method = sGetChildDrawingOrderMethod) != null) {
            try {
                int n2 = (Integer)method.invoke((Object)viewGroup, viewGroup.getChildCount(), n);
                return n2;
            }
            catch (InvocationTargetException invocationTargetException) {
                return n;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return n;
    }

    static ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewGroupOverlayApi18(viewGroup);
        }
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    private static void hiddenSuppressLayout(ViewGroup viewGroup, boolean bl) {
        if (sTryHiddenSuppressLayout) {
            try {
                viewGroup.suppressLayout(bl);
                return;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                sTryHiddenSuppressLayout = false;
            }
        }
    }

    static void suppressLayout(ViewGroup viewGroup, boolean bl) {
        if (Build.VERSION.SDK_INT >= 29) {
            viewGroup.suppressLayout(bl);
            return;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            ViewGroupUtils.hiddenSuppressLayout(viewGroup, bl);
            return;
        }
        ViewGroupUtilsApi14.suppressLayout(viewGroup, bl);
    }
}

