/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import java.util.List;

class AccessibilityNodeInfoCompatApi21 {
    AccessibilityNodeInfoCompatApi21() {
    }

    static void addAction(Object object, Object object2) {
        ((AccessibilityNodeInfo)object).addAction((AccessibilityNodeInfo.AccessibilityAction)object2);
    }

    static int getAccessibilityActionId(Object object) {
        return ((AccessibilityNodeInfo.AccessibilityAction)object).getId();
    }

    static CharSequence getAccessibilityActionLabel(Object object) {
        return ((AccessibilityNodeInfo.AccessibilityAction)object).getLabel();
    }

    static List<Object> getActionList(Object object) {
        return ((AccessibilityNodeInfo)object).getActionList();
    }

    public static CharSequence getError(Object object) {
        return ((AccessibilityNodeInfo)object).getError();
    }

    public static int getMaxTextLength(Object object) {
        return ((AccessibilityNodeInfo)object).getMaxTextLength();
    }

    public static Object getWindow(Object object) {
        return ((AccessibilityNodeInfo)object).getWindow();
    }

    static Object newAccessibilityAction(int n, CharSequence charSequence) {
        return new AccessibilityNodeInfo.AccessibilityAction(n, charSequence);
    }

    public static Object obtainCollectionInfo(int n, int n2, boolean bl, int n3) {
        return AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl, (int)n3);
    }

    public static Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl, (boolean)bl2);
    }

    public static boolean removeAction(Object object, Object object2) {
        return ((AccessibilityNodeInfo)object).removeAction((AccessibilityNodeInfo.AccessibilityAction)object2);
    }

    public static boolean removeChild(Object object, View view) {
        return ((AccessibilityNodeInfo)object).removeChild(view);
    }

    public static boolean removeChild(Object object, View view, int n) {
        return ((AccessibilityNodeInfo)object).removeChild(view, n);
    }

    public static void setError(Object object, CharSequence charSequence) {
        ((AccessibilityNodeInfo)object).setError(charSequence);
    }

    public static void setMaxTextLength(Object object, int n) {
        ((AccessibilityNodeInfo)object).setMaxTextLength(n);
    }

    static class CollectionItemInfo {
        CollectionItemInfo() {
        }

        public static boolean isSelected(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).isSelected();
        }
    }

}

