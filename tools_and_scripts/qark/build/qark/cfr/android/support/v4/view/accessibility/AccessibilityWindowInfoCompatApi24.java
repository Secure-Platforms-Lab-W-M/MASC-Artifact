/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

@RequiresApi(value=24)
class AccessibilityWindowInfoCompatApi24 {
    AccessibilityWindowInfoCompatApi24() {
    }

    public static Object getAnchor(Object object) {
        return ((AccessibilityWindowInfo)object).getAnchor();
    }

    public static CharSequence getTitle(Object object) {
        return ((AccessibilityWindowInfo)object).getTitle();
    }
}

