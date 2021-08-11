/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityRecord
 */
package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;

class AccessibilityEventCompatIcs {
    AccessibilityEventCompatIcs() {
    }

    public static void appendRecord(AccessibilityEvent accessibilityEvent, Object object) {
        accessibilityEvent.appendRecord((AccessibilityRecord)object);
    }

    public static Object getRecord(AccessibilityEvent accessibilityEvent, int n) {
        return accessibilityEvent.getRecord(n);
    }

    public static int getRecordCount(AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }

    public static void setScrollable(AccessibilityEvent accessibilityEvent, boolean bl) {
        accessibilityEvent.setScrollable(bl);
    }
}

