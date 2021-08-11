/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 */
package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatApi22 {
    AccessibilityNodeInfoCompatApi22() {
    }

    public static Object getTraversalAfter(Object object) {
        return ((AccessibilityNodeInfo)object).getTraversalAfter();
    }

    public static Object getTraversalBefore(Object object) {
        return ((AccessibilityNodeInfo)object).getTraversalBefore();
    }

    public static void setTraversalAfter(Object object, View view) {
        ((AccessibilityNodeInfo)object).setTraversalAfter(view);
    }

    public static void setTraversalAfter(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setTraversalAfter(view, n);
    }

    public static void setTraversalBefore(Object object, View view) {
        ((AccessibilityNodeInfo)object).setTraversalBefore(view);
    }

    public static void setTraversalBefore(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setTraversalBefore(view, n);
    }
}

