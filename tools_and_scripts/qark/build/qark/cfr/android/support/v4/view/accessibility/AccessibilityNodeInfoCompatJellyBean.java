/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 */
package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatJellyBean {
    AccessibilityNodeInfoCompatJellyBean() {
    }

    public static void addChild(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).addChild(view, n);
    }

    public static Object findFocus(Object object, int n) {
        return ((AccessibilityNodeInfo)object).findFocus(n);
    }

    public static Object focusSearch(Object object, int n) {
        return ((AccessibilityNodeInfo)object).focusSearch(n);
    }

    public static int getMovementGranularities(Object object) {
        return ((AccessibilityNodeInfo)object).getMovementGranularities();
    }

    public static boolean isAccessibilityFocused(Object object) {
        return ((AccessibilityNodeInfo)object).isAccessibilityFocused();
    }

    public static boolean isVisibleToUser(Object object) {
        return ((AccessibilityNodeInfo)object).isVisibleToUser();
    }

    public static Object obtain(View view, int n) {
        return AccessibilityNodeInfo.obtain((View)view, (int)n);
    }

    public static boolean performAction(Object object, int n, Bundle bundle) {
        return ((AccessibilityNodeInfo)object).performAction(n, bundle);
    }

    public static void setAccesibilityFocused(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setAccessibilityFocused(bl);
    }

    public static void setMovementGranularities(Object object, int n) {
        ((AccessibilityNodeInfo)object).setMovementGranularities(n);
    }

    public static void setParent(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setParent(view, n);
    }

    public static void setSource(Object object, View view, int n) {
        ((AccessibilityNodeInfo)object).setSource(view, n);
    }

    public static void setVisibleToUser(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setVisibleToUser(bl);
    }
}

