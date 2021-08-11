/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$RangeInfo
 */
package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

@RequiresApi(value=19)
class AccessibilityNodeInfoCompatKitKat {
    AccessibilityNodeInfoCompatKitKat() {
    }

    static class RangeInfo {
        RangeInfo() {
        }

        static float getCurrent(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getCurrent();
        }

        static float getMax(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getMax();
        }

        static float getMin(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getMin();
        }

        static int getType(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getType();
        }
    }

}

