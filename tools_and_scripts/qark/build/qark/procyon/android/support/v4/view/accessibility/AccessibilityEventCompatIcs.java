// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityRecord;
import android.view.accessibility.AccessibilityEvent;

class AccessibilityEventCompatIcs
{
    public static void appendRecord(final AccessibilityEvent accessibilityEvent, final Object o) {
        accessibilityEvent.appendRecord((AccessibilityRecord)o);
    }
    
    public static Object getRecord(final AccessibilityEvent accessibilityEvent, final int n) {
        return accessibilityEvent.getRecord(n);
    }
    
    public static int getRecordCount(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }
    
    public static void setScrollable(final AccessibilityEvent accessibilityEvent, final boolean scrollable) {
        accessibilityEvent.setScrollable(scrollable);
    }
}
