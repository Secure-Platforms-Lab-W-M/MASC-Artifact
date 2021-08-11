/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.PopupWindow
 */
package android.support.v4.widget;

import android.widget.PopupWindow;

class PopupWindowCompatApi23 {
    PopupWindowCompatApi23() {
    }

    static boolean getOverlapAnchor(PopupWindow popupWindow) {
        return popupWindow.getOverlapAnchor();
    }

    static int getWindowLayoutType(PopupWindow popupWindow) {
        return popupWindow.getWindowLayoutType();
    }

    static void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
        popupWindow.setOverlapAnchor(bl);
    }

    static void setWindowLayoutType(PopupWindow popupWindow, int n) {
        popupWindow.setWindowLayoutType(n);
    }
}

