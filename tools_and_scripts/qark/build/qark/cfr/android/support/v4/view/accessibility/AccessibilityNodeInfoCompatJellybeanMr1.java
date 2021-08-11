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

class AccessibilityNodeInfoCompatJellybeanMr1 {
    AccessibilityNodeInfoCompatJellybeanMr1() {
    }

    public static Object getLabelFor(Object object) {
        return ((AccessibilityNodeInfo)object).getLabelFor();
    }

    public static Object getLabeledBy(Object object) {
        return ((AccessibilityNodeInfo)object).getLabeledBy();
    }

    public static void setLabelFor(Object object, View view) {
        ((AccessibilityNodeInfo)object).setLabelFor(view);
    }

    public static void setLabelFor(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setLabelFor(view, n);
    }

    public static void setLabeledBy(Object object, View view) {
        ((AccessibilityNodeInfo)object).setLabeledBy(view);
    }

    public static void setLabeledBy(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setLabeledBy(view, n);
    }
}

