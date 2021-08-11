/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction
 */
package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

@RequiresApi(value=24)
class AccessibilityNodeInfoCompatApi24 {
    AccessibilityNodeInfoCompatApi24() {
    }

    public static Object getActionSetProgress() {
        return AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
    }

    public static int getDrawingOrder(Object object) {
        return ((AccessibilityNodeInfo)object).getDrawingOrder();
    }

    public static boolean isImportantForAccessibility(Object object) {
        return ((AccessibilityNodeInfo)object).isImportantForAccessibility();
    }

    public static void setDrawingOrder(Object object, int n) {
        ((AccessibilityNodeInfo)object).setDrawingOrder(n);
    }

    public static void setImportantForAccessibility(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setImportantForAccessibility(bl);
    }
}

