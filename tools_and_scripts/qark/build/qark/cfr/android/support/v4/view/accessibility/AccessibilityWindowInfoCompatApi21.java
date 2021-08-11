/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

class AccessibilityWindowInfoCompatApi21 {
    AccessibilityWindowInfoCompatApi21() {
    }

    public static void getBoundsInScreen(Object object, Rect rect) {
        ((AccessibilityWindowInfo)object).getBoundsInScreen(rect);
    }

    public static Object getChild(Object object, int n) {
        return ((AccessibilityWindowInfo)object).getChild(n);
    }

    public static int getChildCount(Object object) {
        return ((AccessibilityWindowInfo)object).getChildCount();
    }

    public static int getId(Object object) {
        return ((AccessibilityWindowInfo)object).getId();
    }

    public static int getLayer(Object object) {
        return ((AccessibilityWindowInfo)object).getLayer();
    }

    public static Object getParent(Object object) {
        return ((AccessibilityWindowInfo)object).getParent();
    }

    public static Object getRoot(Object object) {
        return ((AccessibilityWindowInfo)object).getRoot();
    }

    public static int getType(Object object) {
        return ((AccessibilityWindowInfo)object).getType();
    }

    public static boolean isAccessibilityFocused(Object object) {
        return ((AccessibilityWindowInfo)object).isAccessibilityFocused();
    }

    public static boolean isActive(Object object) {
        return ((AccessibilityWindowInfo)object).isActive();
    }

    public static boolean isFocused(Object object) {
        return ((AccessibilityWindowInfo)object).isFocused();
    }

    public static Object obtain() {
        return AccessibilityWindowInfo.obtain();
    }

    public static Object obtain(Object object) {
        return AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)((AccessibilityWindowInfo)object));
    }

    public static void recycle(Object object) {
        ((AccessibilityWindowInfo)object).recycle();
    }
}

