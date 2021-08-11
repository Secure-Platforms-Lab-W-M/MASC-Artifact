// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityWindowInfo;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class AccessibilityWindowInfoCompatApi24
{
    public static Object getAnchor(final Object o) {
        return ((AccessibilityWindowInfo)o).getAnchor();
    }
    
    public static CharSequence getTitle(final Object o) {
        return ((AccessibilityWindowInfo)o).getTitle();
    }
}
