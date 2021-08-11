// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityRecord;
import android.view.View;

class AccessibilityRecordCompatJellyBean
{
    public static void setSource(final Object o, final View view, final int n) {
        ((AccessibilityRecord)o).setSource(view, n);
    }
}
