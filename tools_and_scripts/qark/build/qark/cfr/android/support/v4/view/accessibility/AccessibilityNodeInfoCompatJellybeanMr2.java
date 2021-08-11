/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityNodeInfo
 */
package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

class AccessibilityNodeInfoCompatJellybeanMr2 {
    AccessibilityNodeInfoCompatJellybeanMr2() {
    }

    public static List<Object> findAccessibilityNodeInfosByViewId(Object object, String string) {
        return ((AccessibilityNodeInfo)object).findAccessibilityNodeInfosByViewId(string);
    }

    public static int getTextSelectionEnd(Object object) {
        return ((AccessibilityNodeInfo)object).getTextSelectionEnd();
    }

    public static int getTextSelectionStart(Object object) {
        return ((AccessibilityNodeInfo)object).getTextSelectionStart();
    }

    public static String getViewIdResourceName(Object object) {
        return ((AccessibilityNodeInfo)object).getViewIdResourceName();
    }

    public static boolean isEditable(Object object) {
        return ((AccessibilityNodeInfo)object).isEditable();
    }

    public static boolean refresh(Object object) {
        return ((AccessibilityNodeInfo)object).refresh();
    }

    public static void setEditable(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setEditable(bl);
    }

    public static void setTextSelection(Object object, int n, int n2) {
        ((AccessibilityNodeInfo)object).setTextSelection(n, n2);
    }

    public static void setViewIdResourceName(Object object, String string) {
        ((AccessibilityNodeInfo)object).setViewIdResourceName(string);
    }
}

