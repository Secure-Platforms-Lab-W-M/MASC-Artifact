// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class AccessibilityNodeInfoCompatApi24
{
    public static Object getActionSetProgress() {
        return AccessibilityNodeInfo$AccessibilityAction.ACTION_SET_PROGRESS;
    }
    
    public static int getDrawingOrder(final Object o) {
        return ((AccessibilityNodeInfo)o).getDrawingOrder();
    }
    
    public static boolean isImportantForAccessibility(final Object o) {
        return ((AccessibilityNodeInfo)o).isImportantForAccessibility();
    }
    
    public static void setDrawingOrder(final Object o, final int drawingOrder) {
        ((AccessibilityNodeInfo)o).setDrawingOrder(drawingOrder);
    }
    
    public static void setImportantForAccessibility(final Object o, final boolean importantForAccessibility) {
        ((AccessibilityNodeInfo)o).setImportantForAccessibility(importantForAccessibility);
    }
}
