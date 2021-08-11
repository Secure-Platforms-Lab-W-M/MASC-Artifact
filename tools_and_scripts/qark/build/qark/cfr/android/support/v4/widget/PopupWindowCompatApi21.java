/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.widget.PopupWindow
 */
package android.support.v4.widget;

import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

class PopupWindowCompatApi21 {
    private static final String TAG = "PopupWindowCompatApi21";
    private static Field sOverlapAnchorField;

    static {
        try {
            sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
            sOverlapAnchorField.setAccessible(true);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.i((String)"PopupWindowCompatApi21", (String)"Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)noSuchFieldException);
        }
    }

    PopupWindowCompatApi21() {
    }

    static boolean getOverlapAnchor(PopupWindow popupWindow) {
        if (sOverlapAnchorField != null) {
            try {
                boolean bl = (Boolean)sOverlapAnchorField.get((Object)popupWindow);
                return bl;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)"PopupWindowCompatApi21", (String)"Could not get overlap anchor field in PopupWindow", (Throwable)illegalAccessException);
            }
        }
        return false;
    }

    static void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
        if (sOverlapAnchorField != null) {
            try {
                sOverlapAnchorField.set((Object)popupWindow, bl);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)"PopupWindowCompatApi21", (String)"Could not set overlap anchor field in PopupWindow", (Throwable)illegalAccessException);
            }
        }
    }
}

