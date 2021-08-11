/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityRecord
 */
package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityRecord;

class AccessibilityRecordCompatIcsMr1 {
    AccessibilityRecordCompatIcsMr1() {
    }

    public static int getMaxScrollX(Object object) {
        return ((AccessibilityRecord)object).getMaxScrollX();
    }

    public static int getMaxScrollY(Object object) {
        return ((AccessibilityRecord)object).getMaxScrollY();
    }

    public static void setMaxScrollX(Object object, int n) {
        ((AccessibilityRecord)object).setMaxScrollX(n);
    }

    public static void setMaxScrollY(Object object, int n) {
        ((AccessibilityRecord)object).setMaxScrollY(n);
    }
}

